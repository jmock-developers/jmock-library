#!/bin/sh
#
# Script to build and deploy the jMock website.

scp -r $WEBDIR/* $WEBSITE
echo "the live website has been updated"
