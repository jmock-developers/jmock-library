#!/bin/sh
# Damage control build script for jMock.

sh continuous-integration/build-website.sh
sh continuous-integration/build-javadocs.sh

sh continuous-integration/deploy-website.sh
