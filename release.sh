#!/bin/bash
# Release tool for jMock

export VERSION=${1:?No version number given}
export TAG=V$(echo $VERSION | tr ".-" _)
export CVSROOT=:ext:cvs.jmock.codehaus.org:/home/projects/jmock/scm 
export CVS_RSH=ssh
WORKING_DIR=build/release
EXPORT_SUBDIR=jmock-$VERSION
WEBSITE_SUBDIR=jmock-website

DIST=${DIST:-jmock@www.jmock.org:/home/jmock/public_dist}


function export_from_cvs() {
    cvs export -R -r $TAG -d $EXPORT_SUBDIR jmock
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

function checkout_website() {
    cvs checkout -l -d $WEBSITE_SUBDIR jmock-website
    if [ $? -ne 0 ]; then
	exit 1
    fi
}



echo "Publishing release of jMock $VERSION (CVS tag $TAG) to $DIST"
rm -rf $WORKING_DIR
mkdir -p $WORKING_DIR
cd $WORKING_DIR
export_from_cvs
cd $EXPORT_SUBDIR
build_release
publish_release

exit 0

cd ..
checkout_website()
cd $WEBSITE_SUBDIR
bash update-released-versions.sh $VERSION
cvs commit -m "Releasing version $VERSION" released-versions.properties

#send updates to Freshmeat, Codehaus news blogs, etc.
