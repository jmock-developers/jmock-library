#!/bin/sh
# Script to install Hamcrest artifacts 
# System requirements:
# 	- maven 2.0.x 
#  	- wget 
#	- unzip 

if [ $# -lt 2 ]
then
	echo "usage: install-hamcrest-jars.sh <version> <files-url>"
	echo "eg: install-hamcrest-jars.sh 1.1 http://hamcrest.googlecode.com/files"
	exit -1
fi

VERSION=$1
FILES_URL=$2
GROUP_ID=org.hamcrest
JARS_ZIP=hamcrest-$VERSION.zip
DOWNLOAD_URL=$FILES_URL/$JARS_ZIP
DOWNLOAD_DIR=target
rm -r $DOWNLOAD_DIR
wget $DOWNLOAD_URL -P $DOWNLOAD_DIR
JARS_DIR=hamcrest-$VERSION
unzip $DOWNLOAD_DIR/$JARS_ZIP -d$DOWNLOAD_DIR/$JARS_DIR

ARTIFACT_LIST="hamcrest-core hamcrest-library hamcrest-generator hamcrest-integration hamcrest-all"
FILE_DIR=$DOWNLOAD_DIR/$JARS_DIR/$JARS_DIR

for ARTIFACT_ID in $ARTIFACT_LIST
do
	FILE=$FILE_DIR/$ARTIFACT_ID-$VERSION.jar
	echo "Deploying: $FILE"
	./install-file.sh $GROUP_ID $ARTIFACT_ID $VERSION $FILE
done


