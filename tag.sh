#!/bin/sh

VERSION=${1:?no version number given}
TRUNK=`svn info | grep URL: | cut -c 6-`
REVNO=`svn info | grep Revision: | cut -c 11-`
ROOT=`svn info | grep 'Repository Root:' | cut -c 18-`

CHANGES=`svn status`

if [ ! -z "$CHANGES" ]; then
	echo "Uncommitted changes:"
	svn status
	echo "Will not tag as $VERSION until changes are committed"
	exit 1
fi

svn copy -r $REVNO $TRUNK $ROOT/tags/$VERSION -m "Tagging version $VERSION"

