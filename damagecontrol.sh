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
DEPLOY_ROOT=${DEPLOY_ROOT:-~jmock}

function build-step {
	$* || exit 1
}


echo ****
echo id = $(id)
echo ****


build-step ant -Dbuild.timestamp=$BUILD_TIMESTAMP jars website

if let $DEPLOY; then
    build-step cp --recursive $BUILDDIR/dist/ $DEPLOY_ROOT
    build-step cp --recursive $WEBDIR $DEPLOY_ROOT
    build-step mkdir --parents $DEPLOY_ROOT/public_html/docs/
    build-step cp --recursive $BUILDDIR/javadoc/ $DEPLOY_ROOT/public_html/docs/
fi

echo all done.
