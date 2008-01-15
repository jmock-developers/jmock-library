#!/bin/sh

if [ -z $JESTER_HOME ]; then
	if [ -z $1 ]; then
		echo "Location of Simple Jester must given by JESTER_HOME environment variable or first argument"
		exit 1
	else
		export JESTER_HOME=$1
	fi
fi

CLASSPATH=$JESTER_HOME/simple-jester.jar:$JESTER_HOME \
	java jester.TestTester ./jester-build.sh src \
&& python $JESTER_HOME/makeWebView.py -z -p

