#!/bin/sh
#
# Script to deploy packages to the jMock distribution site.

scp $PACKAGEDIR/*-SNAPSHOT-src.jar $DISTSITE/distributions/
exit $?
