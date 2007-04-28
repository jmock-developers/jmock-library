#!/bin/sh
# Script to JMock artifacts to Codehaus Maven repository
# System requirements:
# 	- maven 2.0.x 
#  	- wget 
#	- unzip 

if [ $# -lt 2 ]
then
	echo "usage: deploy-jars.sh [jmock1|jmock2] <version>"
	echo "eg: deploy-jars.sh jmock2 2.0.0"
	exit -1
fi

TYPE=$1
VERSION=$2
GROUP_ID=org.jmock
JARS_ZIP=jmock-$VERSION-jars.zip
DOWNLOAD_URL=http://www.jmock.org/dist/$JARS_ZIP
wget $DOWNLOAD_URL
DIR=jmock-$VERSION
unzip $JARS_ZIP -d$DIR


if [ "$TYPE" = "jmock1" ]
then	
	ARTIFACT_LIST="jmock-core jmock-cglib"
	FILE_ROOT=$DIR
fi
if [ "$TYPE" = "jmock2" ]
then
	ARTIFACT_LIST="jmock jmock-junit3 jmock-junit4"
	FILE_ROOT=$DIR/$DIR
fi

for ARTIFACT_ID in $ARTIFACT_LIST
do
	FILE=$FILE_ROOT/$ARTIFACT_ID-$VERSION.jar
	echo "Deploying: $FILE"
	#./deploy-file.sh $GROUP_ID $ARTIFACT_ID $VERSION $FILE
done


