#!/bin/sh
#
# Script to build and deploy the jMock website.

WEBDIR=website/output

# Create clean output directory
rm -r $WEBDIR
mkdir $WEBDIR

# Generate the skinned and styled site content
(cd website && ruby ./skinner.rb)
