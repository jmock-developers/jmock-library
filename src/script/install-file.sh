#!/bin/sh

# Script to install a file to remote Maven repository
# Requires an entry in the ~/.m2/settings.xml with the repository id
# Copy settings.xml template to ~/.m2/settings.xml and replace with your username

MVN_GOAL="mvn install:install-file"

if [ $# -lt 4 ]
then
	echo "usage: install-file.sh <groupId> <artifactId> <version> <file>"
	exit -1
fi

GROUP_ID=$1
ARTIFACT_ID=$2
VERSION=$3
FILE=$4
$MVN_GOAL -DgroupId=$GROUP_ID -DartifactId=$ARTIFACT_ID -Dversion=$VERSION -Dfile=$FILE -Dpackaging=jar -DgeneratePom=true 