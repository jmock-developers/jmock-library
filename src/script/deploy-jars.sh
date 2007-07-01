#!/bin/sh
# Script to deploy JMock artifacts to remote Maven repository which supports scp
# System requirements:
# 	- maven 2.0.x 
#  	- wget 
#	- unzip 

if [ $# -lt 4 ]
then
	echo "usage: deploy-jars.sh [jmock1|jmock2] <version> <repo-server> <repo-path>"
	echo "eg: deploy-jars.sh jmock2 2.0.0 some.server.com path/to/repo"
	exit -1
fi

TYPE=$1
VERSION=$2
REPO_SERVER=$3
REPO_PATH=$4
REPO_ID=$REPO_SERVER
URL=scp://$REPO_ID/$REPO_PATH
GROUP_ID=org.jmock
JARS_ZIP=jmock-$VERSION-jars.zip
DOWNLOAD_URL=http://www.jmock.org/dist/$JARS_ZIP
DOWNLOAD_DIR=target
rm -r $DOWNLOAD_DIR
wget $DOWNLOAD_URL -P $DOWNLOAD_DIR
JARS_DIR=jmock-$VERSION
unzip $DOWNLOAD_DIR/$JARS_ZIP -d$DOWNLOAD_DIR/$JARS_DIR

if [ "$TYPE" = "jmock1" ]
then	
	ARTIFACT_LIST="jmock-core jmock-cglib"
	FILE_DIR=$DOWNLOAD_DIR/$JARS_DIR
fi
if [ "$TYPE" = "jmock2" ]
then
	ARTIFACT_LIST="jmock jmock-junit3 jmock-junit4"
	FILE_DIR=$DOWNLOAD_DIR/$JARS_DIR/$JARS_DIR
fi

for ARTIFACT_ID in $ARTIFACT_LIST
do
	FILE=$FILE_DIR/$ARTIFACT_ID-$VERSION.jar
	echo "Deploying: $FILE"
	./deploy-file.sh $REPO_ID $URL $GROUP_ID $ARTIFACT_ID $VERSION $FILE
done


