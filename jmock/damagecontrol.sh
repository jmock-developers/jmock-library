#!/bin/sh
# Damage control build script for jMock.

#export RELEASE_ID=1.0
#export PRERELEASE_ID=1.0-RC1
export SNAPSHOT_ID=$(date +%Y%m%d-%H%M%S)

sh continuous-integration/build-website.sh
sh continuous-integration/build-javadocs.sh

# deploy by default
if ((${DEPLOY:-1})); then
	sh continuous-integration/deploy-website.sh;
fi

echo done.
