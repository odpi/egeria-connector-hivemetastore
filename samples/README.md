<!-- SPDX-License-Identifier: Apache-2.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Samples

These sample Postman collections illustrate configuring and using the IBM Information Server connectors
for ODPi Egeria.

Each should be used with the
[environment defined in the Egeria Core samples](https://github.com/odpi/egeria/blob/main/open-metadata-resources/open-metadata-samples/postman-rest-samples/README.md),
which has all of the needed variables defined within it.

# Egeria-IBM-IGC-config.postman_collection.json

This script can be used to configure Egeria for use with an existing Hive Metastore ("HMS") environment.

Prerequisites:

- a Hive metastore instance (v3.1.3)

HMS-specific variables:

- `hmsep` the endpoint of the HMS, this is a thrift url - for example for local hive this could be "thrift://127.0.0.1:9083"
- `qualifiedNamePrefix` the prefix of all the reference entities qualified names that are saved by this connector
-  `sendPollEvents` a boolean flag indicating whether to poll for events. true to poll. 
- `refreshTimeInterval` the time interval between polls
- `hmsCatalogName` the password of the user to access IGC's REST API
- `hmsDatabaseName` the hostname (or IP address) and port of IGC's internal Kafka bus (`domain_host:59092` for non-UG environments; `ug_host:9092` for UG environments)
- `hmsUserid` the userId used to communicate with HMS
- `hmsPassword` the password used to communicate with HMS

Advanced options
- `hmssourceep` the endpoint address associated with the connection i.e. associated with the data source (e.g. a JDBC url to Hive). 
- `useSSL` indicate to use SSL when communicating with HMS (this can only be used if the HMS supports SSL)


Each step is sequentially numbered so that they can be executed in-order as part of a Postman "Runner", if desired. If you are doing this
remove either 4a or 4b. 

# Egeria-HMS-Repository-Proxy_collection.json

This script can be used to configure Egeria for use with an existing HMS. It uses an embedded caching connector.

Prerequisites:

- a version of HMS that is compatible with 3.1.3.  


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.