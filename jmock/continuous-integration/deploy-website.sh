#!/bin/sh
#
# Script to build and deploy the jMock website.

scp -r $WEBDIR/* $WEBSITE
