#!/bin/sh
#
# Script to build and deploy the jMock website.

SRCDIRS=core/src:extensions/cglib/src:extensions/dynamock/src
LIBJARS=lib/*.jar

$JAVA_HOME/bin/javadoc \
	-quiet \
	-windowtitle 'jMock API Documentation' \
	-d $JAVADOCDIR \
	-link http://www.junit.org/junit/javadoc/3.8.1 \
	-link http://java.sun.com/j2se/1.4.2/docs/api \
	-link http://cglib.sourceforge.net/apidocs \
	-sourcepath $SRCDIRS \
	-subpackages org.jmock \
	-group "User API" org.jmock.builder \
	-group "Extension API" org.jmock:org.jmock.constraint:org.jmock.dynamic:org.jmock.dynamic.matcher:org.jmock.dynamic.stub:org.jmock.util \
	-group "Optional Extensions" org.jmock.cglib:org.jmock.dynamock \
	-group "Legacy API" org.jmock.expectation
exit $?
