#!/bin/sh
#
# Script to deploy packages to the jMock distribution site.

SOURCE_DIST_DIR=$DISTROOT/distributions/

ssh $DISTHOST mkdir -p $DISTDIR
scp $PACKAGEDIR/*-SNAPSHOT-src.jar $SOURCE_DIST_DIR
exit $?
