
VERSION=${1:?no version number given}
TRUNK=`svn info | grep URL: | cut -c 6-`
ROOT=`svn info | grep 'Repository Root:' | cut -c 18-`

echo svn copy $TRUNK $ROOT/tags/$VERSION

