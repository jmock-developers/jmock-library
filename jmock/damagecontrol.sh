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

export CLASSPATH=$LIBDIR/junit-3.8.1.jar:$LIBDIR/cglib-full-2.0.jar:$BUILDDIR/core:$BUILDDIR/cglib
case $(uname --operating-system) in
Cygwin) export CLASSPATH=$(echo $CLASSPATH | tr ':' ';');;
esac

DEPLOY=${DEPLOY:-1} # deploy by default
DEPLOY_ROOT=${DEPLOY_ROOT:-dcontrol@$HOSTNAME:/home/projects/jmock}

function build-step {
	  $* || exit 1
}

function deploy {
	  echo deploying $1 to $2
    scp -r $1 $2
}


build-step ant -Dbuild.timestamp=$BUILD_TIMESTAMP jars website

echo $BUILD_TIMESTAMP > $BUILDDIR/dist/jars/jmock-snapshot-version

if let $DEPLOY; then
    build-step deploy $BUILDDIR/dist/ $DEPLOY_ROOT
    build-step deploy $WEBDIR $DEPLOY_ROOT
    build-step deploy $BUILDDIR/javadoc/ $DEPLOY_ROOT/public_html/docs/
fi

echo all done.
