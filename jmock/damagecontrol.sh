#!/bin/sh
# Damage control build script for jMock.

WEBSITE=${WEBSITE:-dcontrol@www.codehaus.org:/www/jmock.codehaus.org/}

cd website
ruby ./skinner.rb
scp -r output/* $WEBSITE

