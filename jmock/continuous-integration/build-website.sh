#!/bin/sh
#
# Script to build and deploy the jMock website.

# Create clean output directory
rm -r $WEBDIR
mkdir -p $WEBDIR

# Generate the skinned and styled site content
cd website && ruby ./skinner.rb
exit $?
