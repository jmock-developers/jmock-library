#!/bin/sh
#
# Package a source snapshot

ROOT=jmock-$SNAPSHOT_ID

ln -s . $ROOT

ALLSRC=$(echo $ROOT/{core,examples,extensions,lib,.classpath,.project,.cvsignore,BUILD.txt,LICENSE.txt})
CALCULATOR_SRC=$ROOT/examples/calculator

mkdir -p $PACKAGEDIR
jar cMf $PACKAGEDIR/jmock-SNAPSHOT-src.jar $ALLSRC
jar cMf $PACKAGEDIR/calculator-example-SNAPSHOT-src.jar $CALCULATOR_SRC
echo $SNAPSHOT_ID > $PACKAGEDIR/jmock-snapshot-version
rm $ROOT
