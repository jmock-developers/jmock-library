#!/bin/sh
# Damage control build script for jMock.

SRCDIR=src/framework
WEBSITE=${WEBSITE:-dcontrol@www.codehaus.org:/www/jmock.codehaus.org/}

WEBDIR=website/output
JAVADOCDIR=$WEBDIR/docs/javadoc

(cd website; ruby ./skinner.rb)
javadoc \
	-windowtitle 'jMock API Documentation' \
	-d $JAVADOCDIR \
	-sourcepath $SRCDIR \
	-subpackages org.jmock

scp -r $WEBDIR/* $WEBSITE
