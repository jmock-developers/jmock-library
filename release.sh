#!/bin/bash
# Release tool for jMock 2
set -ex

export VERSION=${1:?No version number given}
export TAG=$VERSION

WORKING_DIR=$PWD/build/release/jmock-$VERSION
WEBSITE_DIR=$PWD/build/release/jmock-website

JAVADOC_SUBDIR=javadoc/jmock-$VERSION


echo "Publishing release of jMock $VERSION to $DIST"

ant clean

rm -rf $WORKING_DIR
git clone . $WORKING_DIR

(
    cd $WORKING_DIR
    git checkout $TAG
    CLASSPATH=lib/junit-3.8.1.jar ant -Dversion=$VERSION
)

rm -rf $WEBSITE_DIR
git clone git@github.com:jmock-developers/jmock-website.git $WEBSITE_DIR

(
    cd $WEBSITE_DIR    
    ./version.py data/versions.xml $VERSION
    make all
    git ci -a -m "publishing $VERSION"
)

(
    cd $WORKING_DIR
    git checkout gh-pages
    cp -R $WEBSITE_DIR/skinned/* .
    cp $WORKING_DIR/build/jmock-$VERSION-*.zip downloads/
    rm -rf javadoc/jmock-$VERSION
    cp -R $WORKING_DIR/build/jmock-$VERSION/doc/ javadoc/jmock-$VERSION
    
    git add downloads/ javadoc/
    git ci -a -m "publishing $VERSION"
)

(
    cd $WEBSITE_DIR
    git push
)

(
    cd $WORKING_DIR
    git push origin gh-pages
    git checkout jmock2
)

git co gh-pages
git push
git co jmock2

