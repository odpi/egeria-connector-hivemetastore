#!/bin/bash
# SPDX-License-Identifier: Apache-2.0
# Copyright Contributors to the ODPi Egeria project.

# This script is supplied as-is for use by developers to create a local Egeria deployment incuding the hms connector built to talk to the IBM Data Engine.

# This script assumes
#  -  there is a working Kafka on the system
#  -  there is a folder in the home directory called a egeria, which has a built version of Egeria. See https://egeria-project.org/education/egeria-dojo/developer/overview/?h=building+egeria for information on this.
# The script
#  -  git clones the egeria hive connector code
#  -  downloads the IBM jar
#  - amends the build file in a temporary folder
#  - builds and builds the connector knitting in the IBM jar file
#  - there should now be an Egeria deployment in folder  ~/ibm-HMS/testplatform which is started


# Egeria version
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