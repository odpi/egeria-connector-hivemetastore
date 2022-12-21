#!/bin/bash

# This script has been tested on a Mac and is for use in development. This is not the way to deploy Egeria in production,
# where something like a Kubenetes approach would be more appropriate.
# It assumes there is :
#  - a working Kafka on the system
#  - egeria source code build in a folder called egeria from the home directory.

# In the home directory it creates or uses a folder called 'testplatforms' to build an Egeria deployment
# It brings down the Egeria hive connector, downloads the IBM jar and builds the connector and starts the platform with that jar in.
# having constructed the Egeria deployment, it starts the Egeria OMAG platform.

# Egeria version - this can updated to pickup later version of Egeria.
version=3.15

cd ~
rm -rf ibm-HMS
mkdir ibm-HMS
cd ibm-HMS
mkdir src
cd src
git clone  https://github.com/odpi/egeria-connector-hivemetastore
cd egeria-connector-hivemetastore

mkdir libs
cd libs
curl https://us.sql-query.cloud.ibm.com/download/catalog/hive-metastore-standalone-client-3.1.2-sqlquery.jar -o ./hive-metastore-standalone-client-3.1.2-sqlquery.jar
cd ..
mv build.gradle build.gradle.org
cat build.gradle.org  | sed "/hive:hive-standalone-metastore/ s= 'org.apache.hive:hive-standalone-metastore:3.1.3'=(files('libs/hive-metastore-standalone-client-3.1.2-sqlquery.jar'))=" >build.gradle.org2

cat build.gradle.org2  | sed "/Standard version/ s=Standard=IBM=" >build.gradle
rm build.gradle.org
rm build.gradle.org2
./gradlew clean build
cd ~
cd ibm-HMS
mkdir testplatform
cd testplatform
mkdir lib

# copy over server chassis

cp ../../egeria/open-metadata-distribution/open-metadata-assemblies/target/egeria-${version}-SNAPSHOT-distribution/egeria-omag-${version}-SNAPSHOT/server/server-chassis-spring-${version}-SNAPSHOT.jar .

# copy over trust store
cp ../../egeria/open-metadata-implementation/server-chassis/server-chassis-spring/src/main/resources/truststore.p12 .

# copy over connector jar
cp ../src/egeria-connector-hivemetastore/build/libs/egeria-connector-hivemetastore-1.0-SNAPSHOT-jar-with-dependencies.jar lib

java -agentlib:jdwp=transport=dt_socket,address=5005,server=y,suspend=n -Dserver.port=9443 -Dloader.path="lib" -jar server-chassis-spring-${version}-SNAPSHOT.jar &



