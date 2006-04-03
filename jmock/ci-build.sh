#!/bin/bash
# Damage control build script for jMock.

export BUILD_TIMESTAMP=$(date --utc +%Y%m%d-%H%M%S)

source VERSION
export RELEASE_VERSION
export PRERELEASE_VERSION
export SNAPSHOT_VERSION=$BUILD_TIMESTAMP

export LIBDIR=lib
export BUILDDIR=build
export WEBDIR=website/output

export JAVA_HOME=/usr/local/j2sdk1.4.2_10
export JDK_HOME=$JAVA_HOME
export CLASSPATH=$LIBDIR/junit-3.8.1.jar:$LIBDIR/cglib-full-2.0.jar:$BUILDDIR/core:$BUILDDIR/cglib
case $(uname --operating-system) in
Cygwin) export CLASSPATH=$(echo $CLASSPATH | tr ':' ';');;
esac

DEPLOY=${DEPLOY:-1} # deploy by default
DEPLOY_JAR_ROOT=/home/projects/jmock/dist/
DEPLOY_WEB_ROOT=/home/projects/jmock/public_html

ANT_HOME=/usr/local/ant
ANT=$ANT_HOME/bin/ant

# Debug output
echo ENVIRONMENT----------------------------------------------------------------
env
echo ---------------------------------------------------------------------------
echo

function build-step {
	  $* || exit 1
}

function deploy {
	cp --recursive --verbose $*
}

echo version.archive=SNAPSHOT > build.properties
echo version.extract=$BUILD_TIMESTAMP >> build.properties

build-step $ANT clean test jars website

echo $BUILD_TIMESTAMP > $BUILDDIR/dist/jars/jmock-snapshot-version

if (($DEPLOY)) 
then
	echo Deploying...
	
    build-step deploy $BUILDDIR/dist/* $DEPLOY_JAR_ROOT
    build-step deploy $WEBDIR/* $DEPLOY_WEB_ROOT/
    build-step deploy $BUILDDIR/javadoc-$BUILD_TIMESTAMP/* $DEPLOY_WEB_ROOT/docs/javadoc/
else
	echo Not deploying.
fi

echo all done.
