
VERSION=${1:?no version number given}
TRUNK=`svn info | grep URL: | cut -c 6-`
ROOT=`svn info | grep 'Repository Root:' | cut -c 18-`

CHANGES=`svn status`

if [ ! -z "$CHANGES" ]; then
	echo "Uncommitted changes:"
	svn status
	echo "Will not tag head revision as $VERSION until changes are committed"
	exit 1
fi

echo svn copy $TRUNK $ROOT/tags/$VERSION

