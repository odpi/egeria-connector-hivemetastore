#!/bin/bash
# SPDX-License-Identifier: Apache-2.0
# Copyright Contributors to the ODPi Egeria project.

# This script is supplied as-is for use by developers to create a local Egeria deployment incuding the hms connector built to talk to the IBM Data Engine.

# This script assumes
#  -  there is a working Kafka on the system
#  -  there is a folder in the home directory called a egeria, which has a built version of Egeria. See https://egeria-project.org/education/egeria-dojo/developer/overview/?h=building+egeria for information on this.
# The script
#  - git clones the egeria hive connector code
#  - downloads the IBM jar
#  - amends the build file in a temporary folder
#  - builds and builds the connector knitting in the IBM jar file
#  - there should now be an Egeria deployment in folder  ~/ibm-HMS/testplatform which is started


# Egeria version - amend to match the version of Egeria in ~/egeria
version=3.15
temp_folder=ibm-HMS

# uncomment next line to attempt to kill existing processes using the port.
# kill -9 $(lsof -ti:9443)


cd ~ || exit
# remove temp folder if it exists
if [ -d "${temp_folder}" ]; then
  rm -rf ${temp_folder}
fi
# create new temp folder
mkdir ${temp_folder}

cd ${temp_folder} || exit
mkdir src
cd src || exit
git clone  https://github.com/odpi/egeria-connector-hivemetastore
cd egeria-connector-hivemetastore || exit

mkdir libs
cd libs || exit
curl https://us.sql-query.cloud.ibm.com/download/catalog/hive-metastore-standalone-client-3.1.2-sqlquery.jar -o ./hive-metastore-standalone-client-3.1.2-sqlquery.jar
cd ~/${temp_folder}/src/egeria-connector-hivemetastore || exit
mv build.gradle build.gradle.org
sed "/hive:hive-standalone-metastore/ s= 'org.apache.hive:hive-standalone-metastore:3.1.3'=(files('libs/hive-metastore-standalone-client-3.1.2-sqlquery.jar'))=" build.gradle.org>build.gradle.org2
sed "/Standard version/ s=Standard=IBM=" build.gradle.org2 >build.gradle
rm build.gradle.org
rm build.gradle.org2
./gradlew clean build

cd ~/${temp_folder}/src || exit

# clone the caching repository
git clone  https://github.com/odpi/egeria-connector-omrs-caching
cd egeria-connector-omrs-caching || exit
# build caching repository
./gradlew clean build

cd ~/${temp_folder} || exit

# create the testplatform folder, where we will assemble the files required for runtime
mkdir testplatform
cd testplatform || exit

mkdir lib

# copy over server chassis
cp ~/egeria/open-metadata-implementation/server-chassis/server-chassis-spring/build/libs/server-chassis-spring-${version}-SNAPSHOT.jar .

# copy over trust store
cp ~/egeria/open-metadata-implementation/server-chassis/server-chassis-spring/src/main/resources/truststore.p12 .

# copy over hms connector jar into the lib folder
cp ~/${temp_folder}/src/egeria-connector-hivemetastore/build/libs/egeria-connector-hivemetastore-1.0-SNAPSHOT-jar-with-dependencies.jar lib
# copy over caching connector jar into the lib folder
cp ~/${temp_folder}/src/egeria-connector-omrs-caching/build/libs/egeria-connector-omrs-caching-1.0-SNAPSHOT-jar-with-dependencies.jar lib

# start the platform with the above jar files
java -agentlib:jdwp=transport=dt_socket,address=5005,server=y,suspend=n -Dserver.port=9443 -Dloader.path="lib" -jar server-chassis-spring-${version}-SNAPSHOT.jar &