#!/bin/sh
# Script to download artifacts 
#  	- wget 
#	- unzip 

if [ $# -lt 4 ]
then
	echo "usage: download-jars.sh <download-url> <download-zip> <download-dir> <download-name>"
	exit -1
fi

DOWNLOAD_URL=$1
DOWNLOAD_ZIP=$2
DOWNLOAD_DIR=$3
DOWNLOAD_NAME=$4

if [ -d "$DOWNLOAD_DIR/$DOWNLOAD_NAME" ] ; then
    echo "Directory $DOWNLOAD_DIR/$DOWNLOAD_NAME exists"
else
	wget $DOWNLOAD_URL/$DOWNLOAD_ZIP -P $DOWNLOAD_DIR
	unzip $DOWNLOAD_DIR/$DOWNLOAD_ZIP -d$DOWNLOAD_DIR/$DOWNLOAD_NAME
fi    

