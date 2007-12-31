#!/bin/sh

if [ -z $JESTER_HOME ]; then
	echo "JESTER_HOME is not set"
	exit 1
fi

CLASSPATH=$JESTER_HOME/simple-jester.jar:$JESTER_HOME \
	java jester.TestTester ./jester-build.sh src

python $JESTER_HOME/makeWebView.py -z -p

