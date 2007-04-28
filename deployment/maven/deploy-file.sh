#!/bin/sh

# Script to deploy a file to remove Maven repository
# Requires an entry in the ~/.m2/settings.xml with the repository id and the webdav username/password
# Copy settings.xml template to ~/.m2/settings.xml and replace with your username/password

MVN_GOAL="mvn deploy:deploy-file"
REPOSITORY_ID=codehaus.org
URL=dav:https://dav.codehaus.org/repository/jmock

if [ $# -lt 4 ]
then
	echo "usage: deploy-file.sh <groupId> <artifactId> <version> <file>"
	exit -1
fi

GROUP_ID=$1
ARTIFACT_ID=$2
VERSION=$3
FILE=$4
$MVN_GOAL -DrepositoryId=$REPOSITORY_UD -Durl=$URL -DgroupId=$GROUP_ID -DartifactId=$ARTIFACT_ID -Dversion=$VERSION -Dfile=$FILE -Dpackaging=jar -DgeneratePom=true 