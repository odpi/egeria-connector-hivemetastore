package org.odpi.egeria.connectors.hive.metastore.auditlog;


import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageSet;

public enum HiveMetastoreOMRSErrorCode implements ExceptionMessageSet 
{

    TYPEDEF_NOT_SUPPORTED(404, "OMRS-HIVE-METASTORE-REPOSITORY-404-001 ",
                                  "The typedef \"{0}\" is not supported by repository \"{1}\"",
                                  "The system is currently unable to support the requested the typedef.",
                                  "Request support through Egeria GitHub issue."),
    SKELETON_ERROR(404, "OMRS-HIVE-METASTORE-REPOSITORY-500-001 ",
            "The Hive Repository Connector Failed to obtain a Skeleton Entity ",
            "The Hive Repository Connector Failed to obtain a Skelelton Entity from Repository Services and is shutting down.",
            "Request support through Egeria GitHub issue."),
    JDBC_CLIENT_FAILURE(500, "OMRS-HIVE-METASTORE-REPOSITORY-500-001 ",
            "The Apache Hive Metastore jdbc client was not successfully initialized to \"{0}\"",
            "The system was unable to login to or access the Apache Hive Metastore environment via JDBC.",
            "Check your authorization details are accurate, the Apache Atlas environment started, and is network-accessible."),
    ;

    private ExceptionMessageDefinition messageDefinition;

    /**
     * Retrieve a message definition object for an exception.  This method is used when there are no message inserts.
     *
     * @return message definition object.
     */
    @Override
    public ExceptionMessageDefinition getMessageDefinition() {
        return null;
    }

    /**
     * Retrieve a message definition object for an exception.  This method is used when there are values to be inserted into the message.
     *
     * @param params array of parameters (all strings).  They are inserted into the message according to the numbering in the message text.
     * @return message definition object.
     */
    @Override
    public ExceptionMessageDefinition getMessageDefinition(String... params) {
        return null;
    }

    /**
     * The constructor for LocalAtlasOMRSErrorCode expects to be passed one of the enumeration rows defined in
     * LocalAtlasOMRSErrorCode above.   For example:
     *
     *     LocalAtlasOMRSErrorCode   errorCode = LocalAtlasOMRSErrorCode.NULL_INSTANCE;
     *
     * This will expand out to the 5 parameters shown below.
     *
     * @param newHTTPErrorCode - error code to use over REST calls
     * @param newErrorMessageId - unique Id for the message
     * @param newErrorMessage - text for the message
     * @param newSystemAction - description of the action taken by the system when the error condition happened
     * @param newUserAction - instructions for resolving the error
     */
    HiveMetastoreOMRSErrorCode(int newHTTPErrorCode, String newErrorMessageId, String newErrorMessage, String newSystemAction, String newUserAction) {
        this.messageDefinition = new ExceptionMessageDefinition(newHTTPErrorCode,
                newErrorMessageId,
                newErrorMessage,
                newSystemAction,
                newUserAction);
    }

}
