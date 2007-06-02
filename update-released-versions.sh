#!/bin/bash

NEW_VERSION=${1:?No version number given}
PROPERTIES_FILE=released-versions.properties

# Read in current version numbers
export $(sed s/version./version_/ < released-versions.properties | tr [:lower:] [:upper:])

case "$NEW_VERSION" in
	*RC*) 	NEW_VERSION_RELEASE=$VERSION_RELEASE; NEW_VERSION_PRERELEASE=$NEW_VERSION;;
	*) 		NEW_VERSION_RELEASE=$NEW_VERSION; NEW_VERSION_PRERELEASE=n/a;;
esac

cat > $PROPERTIES_FILE <<EOF
version.release=$NEW_VERSION_RELEASE
version.prerelease=$NEW_VERSION_PRERELEASE
EOF
