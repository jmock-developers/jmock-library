#!/bin/bash
# Release tool for jMock 2

export VERSION=${1:?No version number given}
export TAG=$VERSION

# Configure ssh to use the appropriate user when logging into Codehaus
REPOSITORY=https://svn.codehaus.org/jmock

WORKING_DIR=build/release
EXPORT_SUBDIR=jmock-$VERSION
WEBSITE_SUBDIR=jmock-website

REMOTE=${REMOTE:-jmock@www.jmock.org:/home/jmock}
DIST=${DIST:-$REMOTE/public_dist}
JAVADOC=${JAVADOC:-$REMOTE/public_javadoc}


function export_release() {
    svn export $REPOSITORY/tags/$VERSION $EXPORT_SUBDIR
    if [ $? -ne 0 ]; then
        exit 1
    fi
}

function build_release() {
    CLASSPATH=lib/junit-3.8.1.jar ant -Dversion=$VERSION
    if [ $? -ne 0 ]; then
        exit 1
    fi
}

function publish_release() {
    scp build/jmock-$VERSION-*.zip $DIST
    if [ $? -ne 0 ]; then
        exit 1
    fi	
}

function publish_javadoc() {
    scp -r build/jmock-$VERSION/doc/ $JAVADOC/$VERSION
}

function checkout_website() {
    svn co $REPOSITORY/website $WEBSITE_SUBDIR
    if [ $? -ne 0 ]; then
        exit 1
    fi
}

echo "Publishing release of jMock $VERSION to $DIST"
rm -rf $WORKING_DIR
mkdir -p $WORKING_DIR
cd $WORKING_DIR
export_release
cd $EXPORT_SUBDIR
build_release
publish_release
publish_javadoc


