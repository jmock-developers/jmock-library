#!/bin/sh
#
# Script to build and deploy the jMock website.

WEBDIR=website/output
WEBSITE=${WEBSITE:-dcontrol@www.codehaus.org:/www/jmock.codehaus.org/}

scp -r $WEBDIR/* $WEBSITE
echo "the live website has been updated"
