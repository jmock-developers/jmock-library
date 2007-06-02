#!/bin/bash
# Damage control build script for jMock.

VERSION=${VERSION:?must give the version number}

source VERSION
export RELEASE_VERSION
export PRERELEASE_VERSION

if [[ "$VERSION" != "$RELEASE_VERSION" && "$VERSION" != "$PRERELEASE_VERSION" ]]; then
    echo "version $VERSION is not defined in the VERSION file; versions defined are"
    grep -v '#' VERSION
    exit 1
fi

CVS_TAG=V$(echo $VERSION | tr '.' '_')

echo
echo "YOU MUST HAVE TAGGED THE CODE AS $CVS_TAG BEFORE RUNNING THIS SCRIPT"
echo
echo "HIT CONTROL-C NOW IF YOU HAVE NOT DONE SO"
echo

export LIBDIR=lib
export BUILDDIR=build
export WEBDIR=website/output

export CLASSPATH=$LIBDIR/junit-3.8.1.jar:$LIBDIR/cglib-full-2.0.jar
case $(uname --operating-system) in
Cygwin) export CLASSPATH=$(echo $CLASSPATH | tr ':' ';');;
esac

DEPLOY=${DEPLOY:-1} # deploy by default
DEPLOY_JAR_ROOT=${DEPLOY_JAR_ROOT:-dcontrol@dist.codehaus.org:/home/projects/jmock/dist/}

function build-step {
    $* || exit 1
}

function deploy {
    echo deploying $*
    scp -r $*
}

echo version.archive=$VERSION > build.properties
echo version.extract=$VERSION >> build.properties

build-step ant rebuild test.acceptance

if let $DEPLOY; then
    #Cannot get this to work yet
    #build-step cvs -d:ext:$CVS_USER@cvs.jmock.codehaus.org:/home/projects/jmock/scm tag $CVS_TAG

    build-step deploy $BUILDDIR/dist/* $DEPLOY_JAR_ROOT
    echo release $VERSION deployed; now announce the release on the jMock web site
fi

echo all done
