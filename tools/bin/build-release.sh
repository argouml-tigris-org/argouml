#!/bin/sh
# $Id$

# The purpose of this shellscript is to make all the release work
#
# This will hopefully grow

# Check that JAVA_HOME is set.
if test ! -x $JAVA_HOME/bin/javac
then
    echo JAVA_HOME is not set correctly.
    exit 1;
fi

if test ! -n "$CVSROOT"
then
    echo CVSROOT is not set.
    exit 1;
fi

BUILD=RELEASESCRIPT:

# Set up things
echo "$BUILD You have to set up the cvs login correctly!"
# 1. Tag the whole CVS repository with the freeze tag!
echo "$BUILD You also need to tag the respostory with the freeze tag!"
# 2. Check out a new copy of the source!
echo $BUILD Will check out a new copy of the source.
echo "Give the tag name of the freeze tag (like VERSION_X_Y_Z_F):"
read freezetag
mkdir "$freezetag" || exit 1;
cd "$freezetag"
echo "Give the tag name of the release tag (like VERSION_X_Y_Z):"
read releasetag
echo "Give the name of the release (like X.Y.Z):"
read releasename

echo $BUILD Checking out
cvs co -r "$freezetag" argouml/src_new
cvs co -r "$freezetag" argouml/src
cvs co -r "$freezetag" argouml/modules
cvs co -r "$freezetag" argouml/lib
cvs co -r "$freezetag" argouml/tools
cvs co -r "$freezetag" argouml/tests
cvs co -r "$freezetag" argouml/documentation

# 3. Build the release.
echo "$BUILD Will build the release."
cd argouml
chmod +x tools/ant-1.4.1/bin/ant
cd src_new
./build.sh dist-release

echo "$BUILD build the documentation in pdf."
( cd ../documentation && ../tools/ant-1.4.1/bin/ant docbook-xsl-get )
if test ! -d ../documentation/docbook-setup/docbook-xsl-1.66.1
then
    echo "docbook-xsl download failed. Fix it and press return."
    read garbage
fi

echo "Copy the jimi file to tools/lib! and press return."
read garbage
( cd ../documentation && ../tools/ant-1.4.1/bin/ant pdf ) &

# 4. Test the release!
echo "$BUILD Will test the release."
./build.sh alltests

echo "$BUILD Starting ArgoUML for you to do the manual testing in modules/junit"
echo "$BUILD Give the test case TestAll, uncheck Reload at every run"
echo "$BUILD When done, Exit the tool."
( cd ../modules/junit && ../../tools/ant-1.4.1/bin/ant run )
echo "$BUILD Tests done."
echo "$BUILD No more input."

# 5. Tag with the release tag
cd ..
cvs tag $releasetag

# 6. Open repository - not done in the script form.

# 7. Upload the files to the tigris website.
#    This number should be gotten from the default.properties file.
directoryname=argouml-$releasename
wait
for pdffile in build/documentation/pdf/*/*.pdf
do
  mv $pdffile $directoryname/`basename $pdffile .pdf`-${releasename}.pdf
done

echo $BUILD uploading
ssh upload@tigris.org mkdir argouml/$directoryname
ssh upload@tigris.org mkdir argouml/$directoryname/jws
scp $directoryname/* upload@tigris.org:argouml/$directoryname
scp $directoryname/jws/* upload@tigris.org:argouml/$directoryname/jws

# 8. Copy the index file to the download directory (not in the tagged version)
echo "Copy the index file to the download directory (argouml/www/download)"
echo "and add it there. This is not in the tagged version but in the"
echo original version.

# 9. Announcements
echo "Make announcements: News item, mails, ..."

