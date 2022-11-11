/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.egeria.connectors.hms.auditlog;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;

public enum HMSOMRSErrorCode implements ExceptionMessageSet {

   ENDPOINT_NOT_SUPPLIED_IN_CONFIG(400, "OMRS-HMS-REPOSITORY-400-001 ",
                                  "The endpoint was not supplied in the connector configuration \"{1}\"",
                                  "Connector unable to continue",
                                  "Supply a valid thrift end point in the configuration endpoint."),
    FAILED_TO_START_CONNECTOR(400, "OMRS-HMS-REPOSITORY-400-002 ",
                                   "The Hive metastore connector failed to start",
                                   "Connector is unable to be used",
                                   "Review your configuration to ensure it is valid."),

    TYPE_ERROR_EXCEPTION(400, "OMRS-HMS-REPOSITORY-400-003 ",
                         "Type error exception",
                         "Connector is unable to be used",
                         "Review the configuration. Check the logs and debug."),

    ENCODING_EXCEPTION(400, "OMRS-HMS-REPOSITORY-400-004 ",
                                          "The event mapper failed to encode '{0}' with value '{1}' to create a guid",
                                          "The system will shutdown the server",
                                          "Debug the cause of the encoding error."),

    TYPEDEF_NAME_NOT_KNOWN(404, "OMRS-HMS-REPOSITORY-404-001§",
                           "On Server {0} for request {1}, the TypeDef unique name {2} passed is not known to this repository connector",
                           "The system is unable to retrieve the properties for the requested TypeDef because the supplied identifiers are not recognized.",
                           "The identifier is supplied by the caller.  It may have a logic problem that has corrupted the identifier, or the TypeDef has been deleted since the identifier was retrieved."),


    ;

    final private ExceptionMessageDefinition messageDefinition;

    /**
     * The constructor for HMSOMRSErrorCode expects to be passed one of the enumeration rows defined in
     * HMSOMRSErrorCode above.   For example:
     *
     *     HMSOMRSErrorCode   errorCode = HMSOMRSErrorCode.NULL_INSTANCE;
     *
     * This will expand out to the 5 parameters shown below.
     *
     * @param newHTTPErrorCode - error code to use over REST calls
     * @param newErrorMessageId - unique Id for the message
     * @param newErrorMessage - text for the message
     * @param newSystemAction - description of the action taken by the system when the error condition happened
     * @param newUserAction - instructions for resolving the error
     */
    HMSOMRSErrorCode(int newHTTPErrorCode, String newErrorMessageId, String newErrorMessage, String newSystemAction, String newUserAction) {
        this.messageDefinition = new ExceptionMessageDefinition(newHTTPErrorCode,
                newErrorMessageId,
                newErrorMessage,
                newSystemAction,
                newUserAction);
    }

    /**
     * Retrieve a message definition object for an exception.  This method is used when there are no message inserts.
     *
     * @return message definition object.
     */
    @Override
    public ExceptionMessageDefinition getMessageDefinition() {
        return messageDefinition;
    }


    /**
     * Retrieve a message definition object for an exception.  This method is used when there are values to be inserted into the message.
     *
     * @param params array of parameters (all strings).  They are inserted into the message according to the numbering in the message text.
     * @return message definition object.
     */
    @Override
    public ExceptionMessageDefinition getMessageDefinition(String... params) {
        messageDefinition.setMessageParameters(params);
        return messageDefinition;
    }

    /**
     * toString() JSON-style
     *
     * @return string description
     */
    @Override
    public String toString() {
        return "HMSOMRSErrorCode{" +
                "messageDefinition=" + messageDefinition +
                '}';
    }

}
