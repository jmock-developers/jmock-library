#!/bin/sh
# Damage control build script for jMock.

export BUILD_TIMESTAMP=$(date --utc +%Y%m%d-%H%M%S)

source VERSION
export RELEASE_VERSION
export PRERELEASE_VERSION
export SNAPSHOT_VERSION=$BUILD_TIMESTAMP

export LIBDIR=lib
export BUILDDIR=build
export PACKAGEDIR=$BUILDDIR/dist
export WEBDIR=website/output
export JAVADOCDIR=$WEBDIR/docs/javadoc

export WEBSITE=dcontrol@jmock.codehaus.org:/www/jmock.codehaus.org
export DISTHOST=dcontrol@dist.codehaus.org
export DISTROOT=/www/dist.codehaus.org/jmock
export DISTSITE=$DISTHOST:$DISTROOT

# Note: change the path separators to ':' before committing to CVS
export CLASSPATH=$LIBDIR/junit-3.8.1.jar\;$LIBDIR/cglib-full-2.0.jar\;$BUILDDIR/core\;$BUILDDIR/cglib

function run_task {
	local task=$1
	
	echo -n $task ""
	if sh continuous-integration/$task.sh > /dev/null; then
		echo done
	else
		echo failed
		exit 1
	fi
}

function tasks {
	for task in $*; do
		run_task $task;
	done
}

echo
echo Build time: $(date --utc "+%d/%m/%Y %H:%M:%S")
echo

ant -Dbuild.timestamp=$BUILD_TIMESTAMP jars 
tasks build-website
ant -Djava.doc.dir=$JAVADOCDIR javadoc

if let ${DEPLOY:-1}; then
    # deploy by default
	tasks deploy-website deploy-snapshots;
fi
echo all done.
