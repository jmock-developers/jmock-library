#!/bin/sh
#
# Script to build and deploy the jMock website.

SRCDIRS=core/src:extensions/cglib/src:extensions/dynamock/src

$JAVA_HOME/bin/javadoc \
	-windowtitle 'jMock API Documentation' \
	-d $JAVADOCDIR \
	-link http://www.junit.org/junit/javadoc/3.8.1 \
	-link http://java.sun.com/j2se/1.4.2/docs/api \
	-link http://cglib.sourceforge.net/apidocs \
	-sourcepath $SRCDIRS \
	-subpackages org.jmock > /dev/null
