/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.egeria.connectors.hms.auditlog;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageSet;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;

/**
 * The FileOMRSAuditCode is used to define the message content for the OMRS Audit Log.
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

    HIVE_GETTABLES_FAILED("OMRS-HMS-REPOSITORY-023",
                          OMRSAuditLogRecordSeverity.INFO,
                          "The Hive metastore connector getTables call failed with error {0}",
                          "Connector is will continue to poll and will pick up tables if they are added.",
                          "Ensure that the Hive connection details correctly point to an active Hive server with tables defined"),

    HIVE_GETTABLE_FAILED( "OMRS-HMS-REPOSITORY-024 ",
                         OMRSAuditLogRecordSeverity.INFO,
                         "The Hive metastore connector getTable for table {0} failed with error {1}",
                         "Connector is will continue to poll, but this table will not be picked up.",
                         "Review the error and assess why this table is not being. Raise a Git issue if it is not obvious what the cause is"),

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
