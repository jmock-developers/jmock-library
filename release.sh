#!/bin/bash
# Release tool for jMock

export VERSION=${1:?No version number given}
export TAG=V$(echo $VERSION | tr . _)
export CVSROOT=:ext:cvs.jmock.codehaus.org:/home/projects/jmock/scm 
export CVS_RSH=ssh
WORKING_DIR=build/release
EXPORT_SUBDIR=jmock-$VERSION
HEAD_SUBDIR=jmock-HEAD
DIST_DIR=${DIST_DIR:-/home/projects/jmock/dist}

if [ ! -d $DIST_DIR ]
then
	echo "$DIST_DIR does not exist"
	exit 1
fi

function export_from_cvs() {
	cvs export -R -r $TAG -d $EXPORT_SUBDIR jmock
	if [ $? -ne 0 ]
	then
		exit 1
	fi
}

function build_release() {
	CLASSPATH=lib/junit-3.8.1.jar ant -Dversion.extract=$VERSION -Dversion.archive=$VERSION jars
	if [ $? -ne 0 ]
	then
		exit 1
	fi
}

function publish_release() {
	cp -R build/dist/* $DIST_DIR
	if [ $? -ne 0 ]
	then
		exit 1
	fi	
}

function checkout_head_properties() {
	cvs checkout -l -d $HEAD_SUBDIR jmock
	if [ $? -ne 0 ]
	then
		exit 1
	fi	
}

echo "Publishing release of jMock $VERSION (CVS tag $TAG) to $DIST_DIR"
rm -rf $WORKING_DIR
mkdir -p $WORKING_DIR
cd $WORKING_DIR
export_from_cvs
cd $EXPORT_SUBDIR
build_release
publish_release

cd ..
checkout_head_properties
cd $HEAD_SUBDIR
bash update-released-versions.sh $VERSION
cvs commit -m "Releasing version $VERSION" released-versions.properties

#send updates to Freshmeat, Codehaus news blogs, etc.
