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


function deploy {
    echo deploying contents of $1 to $2
    cp --recursive $1/* $2/ || exit 1
}

ant -Dbuild.timestamp=$BUILD_TIMESTAMP jars website || exit 1

if let $DEPLOY; then
    cp $BUILDDIR/dist/jars/* $DEPLOY_ROOT/dist/jars/
    cp $BUILDDIR/dist/distributions/* $DEPLOY_ROOT/dist/distributions/
    #deploy $WEBDIR $DEPLOY_ROOT/public_html
    #deploy $BUILDDIR/javadoc $DEPLOY_ROOT/public_html/docs/javadoc
fi

echo all done.
