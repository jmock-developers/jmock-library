#!/bin/bash
# Damage control build script for jMock.

echo **** ENVIRONMENT VARIABLES ****
env
echo *******************************


export BUILD_TIMESTAMP=$(date --utc +%Y%m%d-%H%M%S)

source VERSION
export RELEASE_VERSION
export PRERELEASE_VERSION
export SNAPSHOT_VERSION=$BUILD_TIMESTAMP

export LIBDIR=lib
export BUILDDIR=build
export WEBDIR=website/output

export CLASSPATH=$LIBDIR/junit-3.8.1.jar:$LIBDIR/cglib-full-2.0.jar:$BUILDDIR/core:$BUILDDIR/cglib
case $(uname --operating-system) in
Cygwin) export CLASSPATH=$(echo $CLASSPATH | tr ':' ';');;
esac

DEPLOY=${DEPLOY:-1} # deploy by default
DEPLOY_JAR_ROOT=${DEPLOY_JAR_ROOT:-dcontrol@dist.codehaus.org:/home/projects/jmock/dist/}
DEPLOY_WEB_ROOT=${DEPLOY_WEB_ROOT:-dcontrol@jmock.codehaus.org:/home/projects/jmock/public_html}


function build-step {
	  $* || exit 1
}

function deploy {
	echo deploying $*
    scp -r $*
}

echo version.archive=SNAPSHOT > build.properties
echo version.extract=$BUILD_TIMESTAMP >> build.properties

build-step ant jars website

echo $BUILD_TIMESTAMP > $BUILDDIR/dist/jars/jmock-snapshot-version

if let $DEPLOY; then
    build-step deploy $BUILDDIR/dist/* $DEPLOY_JAR_ROOT
    build-step deploy $WEBDIR/* $DEPLOY_WEB_ROOT/
    build-step deploy $BUILDDIR/javadoc-$BUILD_TIMESTAMP/* $DEPLOY_WEB_ROOT/docs/javadoc/
fi

echo all done.
