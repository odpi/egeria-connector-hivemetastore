/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.egeria.connectors.hms.auditlog;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;

/**
 * The HMSOMRSAuditCode is used to define the message content for the OMRS Audit Log.
 *
 * The 5 fields in the enum are:
 * <ul>
 *     <li>Log Message Id - to uniquely identify the message</li>
 *     <li>Severity - is this an event, decision, action, error or exception</li>
 *     <li>Log Message Text - includes placeholder to allow additional values to be captured</li>
 *     <li>Additional Information - further parameters and data relating to the audit message (optional)</li>
 *     <li>SystemAction - describes the result of the situation</li>
 *     <li>UserAction - describes how a user should correct the situation</li>
 * </ul>
 */
public enum HMSOMRSAuditCode implements AuditLogMessageSet {

    EVENT_MAPPER_SHUTDOWN("OMRS-HMS-REPOSITORY-0001",
            OMRSAuditLogRecordSeverity.INFO,
            "The Hive metastore repository proxy event mapper has shutdown its instance for server {0}",
            "The local server has requested shut down of an  Hive metastore repository proxy event mapper instance.",
            "No action is required.  This is part of the normal operation of the service."),
    EVENT_MAPPER_STARTING("OMRS-HMS-REPOSITORY-0002",
            OMRSAuditLogRecordSeverity.INFO,
            "The Hive metastore repository proxy event mapper consumer thread is starting up",
            "The local server has requested startup of an  Hive metastore repository proxy event mapper consumer.",
            "No action is required.  This is part of the normal operation of the service."),
    EVENT_MAPPER_ACQUIRING_TYPES_LOOP("OMRS-HMS-REPOSITORY-0003",
                                  OMRSAuditLogRecordSeverity.TRACE,
                                  "The Hive metastore repository proxy event mapper loop for acquiring types has started",
                                  "The Hive metastore repository proxy event mapper loop is attempting to get the types.",
                                  "No action is required.  This is part of the normal operation of the service."),
    EVENT_MAPPER_ACQUIRING_TYPES_LOOP_FOUND_TYPE("OMRS-HMS-REPOSITORY-0004",
                                      OMRSAuditLogRecordSeverity.TRACE,
                                      "The Hive metastore repository proxy event mapper loop for acquiring types has found type {0}",
                                      "The Hive metastore repository proxy event mapper loop has found a type.",
                                      "No action is required.  This is part of the normal operation of the service."),
    EVENT_MAPPER_ACQUIRED_ALL_TYPES("OMRS-HMS-REPOSITORY-0005",
                                    OMRSAuditLogRecordSeverity.TRACE,
                                    "The Hive metastore repository proxy event mapper has acquired the types it needs",
                                    "The Hive metastore repository proxy event mapper is about to construct the events.",
                                    "No action is required.  This is part of the normal operation of the service."),
    EVENT_MAPPER_ACQUIRING_TYPES_LOOP_INTERRUPTED_EXCEPTION("OMRS-HMS-REPOSITORY-0006",
    OMRSAuditLogRecordSeverity.EXCEPTION,
            "The Hive metastore repository proxy event mapper sleep has failed with an interrupted exception",
            "The Hive metastore repository proxy event mapper's acquiring types loop sleep has been interrupted.",
            "No action is required.  This is part of the normal operation of the service."),
    EVENT_MAPPER_POLL_LOOP_PRE_WAIT("OMRS-HMS-REPOSITORY-0007",
                                               OMRSAuditLogRecordSeverity.INFO,
                                               "The Hive metastore repository proxy event mapper polling loop  has completed a scan",
                                               "The Hive metastore repository proxy event mapper polling loop is about the sleep for 1 second then retry.",
                                               "No action is required.  This is part of the normal operation of the service."),
    EVENT_MAPPER_POLL_LOOP_POST_WAIT("OMRS-HMS-REPOSITORY-0008",
                                                OMRSAuditLogRecordSeverity.INFO,
                                                "The Hive metastore repository proxy event mapper polling loop has woken.",
                                                "The Hive metastore repository proxy event mapper loop has woken from its sleep between polls.",
                                                "No action is required.  This is part of the normal operation of the service."),

    EVENT_MAPPER_POLL_LOOP_INTERRUPTED_EXCEPTION("OMRS-HMS-REPOSITORY-0009",
                                                            OMRSAuditLogRecordSeverity.EXCEPTION,
                                                            "The Hive metastore repository proxy event mapper polling loop sleep has failed with an interrupted exception",
                                                            "The Hive metastore repository proxy event mapper's polling loop sleep has been interrupted.",
                                                            "No action is required.  This is part of the normal operation of the service."),
    POLLING_THREAD_INFO_ALREADY_STOPPED("OMRS-HMS-REPOSITORY-0010",
                                        OMRSAuditLogRecordSeverity.INFO,
                                        "The Hive metastore repository proxy event mapper polling loop thread got an exception",
                                        "The Hive metastore repository proxy event mapper's polling thread got an exception but its state was already not running.",
                                        "See other audit entries for action."),
    EVENT_MAPPER_POLL_LOOP_GOT_AN_EXCEPTION_WITH_CAUSE("OMRS-HMS-REPOSITORY-0011",
                                            OMRSAuditLogRecordSeverity.EXCEPTION,
                                            "Error {0} in Event mapper Polling loop, the cause is {1}",
                                            "Connector is unable to be used",
                                            "Check the cause in the message to see what has occurred."),
    EVENT_MAPPER_POLL_LOOP_GOT_AN_EXCEPTION("OMRS-HMS-REPOSITORY-0012",
                                                       OMRSAuditLogRecordSeverity.EXCEPTION,
                                                       "Error {0} in Event mapper Polling loop",
                                                       "Connector is unable to be used",
                                                       "Check the logs for the details of the Exception and debug."),

    HIVE_GETTABLES_FAILED("OMRS-HMS-REPOSITORY-013",
                          OMRSAuditLogRecordSeverity.INFO,
                          "The Hive metastore connector getTables call failed with error {0}",
                          "Connector is will continue to poll and will pick up tables if they are added.",
                          "Ensure that the Hive connection details correctly point to an active Hive server with tables defined"),

    HIVE_GETTABLE_FAILED( "OMRS-HMS-REPOSITORY-014 ",
                         OMRSAuditLogRecordSeverity.INFO,
                         "The Hive metastore connector getTable for table {0} failed with error {1}",
                         "Connector is will continue to poll, but this table will not be picked up.",
                         "Review the error and assess why this table is not being. Raise a Git issue if it is not obvious what the cause is"),
    // TODO should this be a fail? i.e. stop the connector
    HIVE_GETCATALOGS_FAILED("OMRS-HMS-REPOSITORY-015",
            OMRSAuditLogRecordSeverity.INFO,
            "The Hive metastore connector getCatalogs call failed with error {0}",
            "Connector is will continue to poll and will pick up tables if they are added.",
            "Ensure that the Hive connection details correctly point to an active Hive server with tables defined"),
    // TODO should this be a fail? i.e. stop the connector
    HIVE_GETDATABASES_FAILED("OMRS-HMS-REPOSITORY-016",
            OMRSAuditLogRecordSeverity.INFO,
            "The Hive metastore connector getAllDatabases call failed with error {0}",
            "Connector is will continue to poll and will pick up tables if they are added.",
            "Ensure that the Hive connection details correctly point to an active Hive server with tables defined"),

    ;


    final private String logMessageId;
    final private OMRSAuditLogRecordSeverity severity;
    final private String logMessage;
    final private String systemAction;
    final private String userAction;


    /**
     * The constructor for OMRSAuditCode expects to be passed one of the enumeration rows defined in
     * OMRSAuditCode above.   For example:
     * <p>
     * OMRSAuditCode   auditCode = OMRSAuditCode.SERVER_NOT_AVAILABLE;
     * <p>
     * This will expand out to the 4 parameters shown below.
     *
     * @param messageId    - unique Id for the message
     * @param severity     - the severity of the message
     * @param message      - text for the message
     * @param systemAction - description of the action taken by the system when the condition happened
     * @param userAction   - instructions for resolving the situation, if any
     */
   HMSOMRSAuditCode(String messageId, OMRSAuditLogRecordSeverity severity, String message,
                    String systemAction, String userAction) {
        this.logMessageId = messageId;
        this.severity = severity;
        this.logMessage = message;
        this.systemAction = systemAction;
        this.userAction = userAction;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AuditLogMessageDefinition getMessageDefinition() {
        return new AuditLogMessageDefinition(logMessageId,
                severity,
                logMessage,
                systemAction,
                userAction);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AuditLogMessageDefinition getMessageDefinition(String ...params) {
        AuditLogMessageDefinition messageDefinition = new AuditLogMessageDefinition(logMessageId,
                severity,
                logMessage,
                systemAction,
                userAction);
        messageDefinition.setMessageParameters(params);
        return messageDefinition;
    }

}
