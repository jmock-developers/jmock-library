#!/bin/sh
#
# Script to deploy packages to the jMock distribution site.

SOURCE_DIST_DIR=$DISTROOT/distributions/

ssh $DISTHOST mkdir -p $SOURCE_DIST_DIR
scp $PACKAGEDIR/*-SNAPSHOT-src.jar $DISTHOST:$SOURCE_DIST_DIR
exit $?
