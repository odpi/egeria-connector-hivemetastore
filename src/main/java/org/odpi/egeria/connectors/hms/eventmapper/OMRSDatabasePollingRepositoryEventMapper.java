/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.egeria.connectors.hms.eventmapper;

import org.apache.hadoop.hive.metastore.IMetaStoreClient;

import org.odpi.egeria.connectors.hms.auditlog.HMSOMRSAuditCode;
import org.odpi.egeria.connectors.hms.auditlog.HMSOMRSErrorCode;
import org.odpi.egeria.connectors.hms.helpers.ExceptionHelper;
import org.odpi.openmetadata.adapters.repositoryservices.caching.repositoryconnector.CachingOMRSRepositoryProxyConnector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryeventmapper.OMRSRepositoryEventMapperBase;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * OMRSDatabasePollingRepositoryEventMapper supports the event mapper function for a metastore used as an open metadata repository.
 * <p>
 * This class is an implementation of an OMRS event mapper, it polls for content in the 3rd party metastore and puts
 * that content into an embedded Egeria repository. It then (if configured to send batch events) extracts the entities and relationships
 * from the embedded repository and sends a batch event for
 * 1) for the asset Entities and relationships
 * 2) for each RelationalTable, it's RelationalColumns and associated relationships
 */
@SuppressWarnings("Varifier")
public class OMRSDatabasePollingRepositoryEventMapper extends OMRSRepositoryEventMapperBase {


    /**
     * Running field is a thread safe indicator that the thread is running. So stop the thread set the running flag to false.
     */
    private final AtomicBoolean running = new AtomicBoolean(false);

    /**
     * map to help manage type guids, by keeping a map for type name to guid.
     */
    private Map<String, String> typeNameToGuidMap = null;

    /**
     * UserId associated with this connector
     */
    private String userId = null;
    /**
     * Default polling refresh interval in milliseconds.
     */
    private int refreshInterval = 5000;

    protected OMRSMetadataCollection metadataCollection = null;
    private PollingThread pollingThread;

    private HMSOMRSEventProducer omrsEventProducer = null;

    private IMetaStoreClientFacade client = null;
    private boolean testing = false;



    /**
     * Default constructor
     */
    public OMRSDatabasePollingRepositoryEventMapper() {
        super();
    }

    public void setClient(IMetaStoreClientFacade client) {
        this.client = client;
    }

    synchronized public String getUserId() {
        return userId;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings({"Var","Varifier"})
    @Override
    synchronized public void start() throws ConnectorCheckedException {

        super.start();

        final String methodName = "start";
        // synchronise in case the start occurs while the thread is running accessing the private varioables.
        // this synchronisation should ensure that all the config is updated together before the polling thread accesses them
        synchronized (this) {
            //repositoryName = this.repositoryConnector.getRepositoryName();
            if (auditLog !=null) {
                auditLog.logMessage(methodName, HMSOMRSAuditCode.EVENT_MAPPER_STARTING.getMessageDefinition());
            }
            if (!(repositoryConnector instanceof CachingOMRSRepositoryProxyConnector)) {
                ExceptionHelper.raiseConnectorCheckedException(this.getClass().getName(), HMSOMRSErrorCode.EVENT_MAPPER_IMPROPERLY_INITIALIZED, methodName, null, repositoryConnector.getServerName());
            }

            this.repositoryHelper = this.repositoryConnector.getRepositoryHelper();

            Map<String, Object> configurationProperties = connectionProperties.getConfigurationProperties();
            String userId = connectionProperties.getUserId();

            if (userId == null) {
                // default
                userId = "OMAGServer";
            }

            omrsEventProducer = new HMSOMRSEventProducer(auditLog,
                    repositoryHelper,
                    repositoryConnector,
                    repositoryEventProcessor,
                    configurationProperties,
                    connectionProperties.getEndpoint(),
                    userId);
            if (configurationProperties != null) {
                Integer configuredRefreshInterval = (Integer) configurationProperties.get(HMSOMRSRepositoryEventMapperProvider.REFRESH_TIME_INTERVAL);
                if (configuredRefreshInterval != null) {
                    refreshInterval = configuredRefreshInterval * 1000 * 60;
                }
            }
            if (metadataCollection == null) {
                try {
                    if (client != null) {
                        // replace with the test client
                        omrsEventProducer.setClient(client);
                    }
                    omrsEventProducer.connectTo3rdParty();
                } catch (RepositoryErrorException e) {
                    ExceptionHelper.raiseConnectorCheckedException(this.getClass().getName(), HMSOMRSErrorCode.FAILED_TO_START_CONNECTOR, methodName, null);
                }
            }
        }

        this.pollingThread = new PollingThread();
        if (!testing) {
            pollingThread.start();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    synchronized public void disconnect() throws ConnectorCheckedException {
        super.disconnect();
        final String methodName = "disconnect";
        pollingThread.stop();
        if (auditLog !=null) {
            auditLog.logMessage(methodName, HMSOMRSAuditCode.EVENT_MAPPER_SHUTDOWN.getMessageDefinition(repositoryConnector.getServerName()));
        }
    }

    public void setTesting() {
        testing = true;
    }


    /**
     * Class to poll for Meta store content
     */
    public class PollingThread implements Runnable {

        /**
         * Start the thread
         */
        void start() {
            Thread worker = new Thread(this);
            worker.start();
        }

        /**
         * Stop the thread
         */

        void stop() {
            if (!running.compareAndSet(true, false)) {
                if (auditLog !=null) {
                    auditLog.logMessage("stop", HMSOMRSAuditCode.POLLING_THREAD_INFO_ALREADY_STOPPED.getMessageDefinition());
                }
            }
        }
        @Override
        public void run() {

            final String methodName = "run";
            if (running.compareAndSet(false, true)) {
                while (running.get()) {
                    try {
                        // synchronise the processing to ensure that a start does not change the instance variables under us.
                        synchronized (this) {
                            if (!omrsEventProducer.execute()) {
                                disconnect();
                            }
                        }
                        // if still running then continue polling
                        if (running.get()) {
                            // come out of synchronization when waiting.
                            //  wait the polling interval.
                            if (auditLog !=null) {
                                auditLog.logMessage(methodName, HMSOMRSAuditCode.EVENT_MAPPER_POLL_LOOP_PRE_WAIT.getMessageDefinition());
                            }
                            try {
                                Thread.sleep(refreshInterval);
                                if (auditLog !=null) {
                                    auditLog.logMessage(methodName, HMSOMRSAuditCode.EVENT_MAPPER_POLL_LOOP_POST_WAIT.getMessageDefinition());
                                }
                            } catch (InterruptedException e) {
                                // should not happen as there is only one thread
                                // if it happens then continue in the while
                                if (auditLog !=null) {
                                    auditLog.logMessage(methodName, HMSOMRSAuditCode.EVENT_MAPPER_POLL_LOOP_INTERRUPTED_EXCEPTION.getMessageDefinition());
                                }
                            }
                        }
                    } catch (Exception e) {
                        Throwable cause = e.getCause();
                        // catch everything else
                        String msg = "No Exception message";
                        if (e.getMessage() != null) {
                            msg = e.getMessage();
                        }
                        String causeMsg = "No cause message";
                        if (cause != null && cause.getMessage() != null) {
                            causeMsg = cause.getMessage();
                        }
                        if (auditLog !=null) {
                            auditLog.logMessage(methodName, HMSOMRSAuditCode.EVENT_MAPPER_POLL_LOOP_GOT_AN_EXCEPTION_WITH_CAUSE.getMessageDefinition(msg, causeMsg));
                        }
                    } finally {
                        // stop the thread if we came out of the loop.
                        this.stop();
                    }
                }
            }
        }
    }
}
