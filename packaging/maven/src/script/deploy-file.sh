#!/bin/sh

# Script to deploy a file to remote Maven repository
# Requires an entry in the ~/.m2/settings.xml with the repository id
# Copy settings.xml template to ~/.m2/settings.xml and replace with your username

MVN_GOAL="mvn deploy:deploy-file"

if [ $# -lt 6 ]
then
	echo "usage: deploy-file.sh <repositoryId> <url> <groupId> <artifactId> <version> <file>"
	exit -1
fi

REPOSITORY_ID=$1
URL=$2
GROUP_ID=$3
ARTIFACT_ID=$4
VERSION=$5
FILE=$6
$MVN_GOAL -DrepositoryId=$REPOSITORY_ID -Durl=$URL -DgroupId=$GROUP_ID -DartifactId=$ARTIFACT_ID -Dversion=$VERSION -Dfile=$FILE -Dpackaging=jar -DgeneratePom=true 