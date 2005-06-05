#!/bin/sh
# $Id$

BUILD=PACKRELEASE:

echo $BUILD The purpose of this shellscript is to take the contents
echo $BUILD of the DIST directory, tar/zip/whatever it and upload it.

# Check that JAVA_HOME is set.
if test ! -x $JAVA_HOME/bin/javac
then
    echo JAVA_HOME is not set correctly.
    exit 1;
fi

if test ! -d ../svn/argouml-downloads/trunk/www
then
    echo The output directory ../svn/argouml-downloads/trunk/www does not exist.
    exit 1;
fi

echo "Give the name of the release (like X.Y.Z)."
read releasename

echo "$BUILD Create the zip and tar files."
mkdir DIST
(
  cd argouml/build;
  $JAVA_HOME/bin/jar cvf ../../DIST/ArgoUML-$releasename.zip *.jar README.txt
  tar cvf ../../DIST/ArgoUML-$releasename.tar *.jar README.txt
)
(
  cd argouml/lib;
  $JAVA_HOME/bin/jar cvf ../../DIST/ArgoUML-$releasename-libs.zip *.txt *.jar
  tar cvf ../../DIST/ArgoUML-$releasename-libs.tar *.txt *.jar
)
(
  cd argouml/build;
  $JAVA_HOME/bin/jar cvf ../../DIST/ArgoUML-$releasename-modules.zip ext/*.jar
  tar cvf ../../DIST/ArgoUML-$releasename.tar ext/*.jar
)
( cd DIST && gzip -v *.tar )

echo $BUILD copying to the svn directory
mkdir ../../../svn/argouml-downloads/trunk/www/argouml-$releasename
(
  cd DIST &&
  cp * ../../../../svn/argouml-downloads/trunk/www/argouml-$releasename
)




echo Add and commit the newly created directory
echo ../svn/argouml-downloads/trunk/www/$directoryname

echo Update the index.html in the argouml-downloads project.

echo "Copy the index file to the download directory (argouml/www/download)"
echo and add and commit it there. This is not in the tagged version but
echo in the echo original version.
