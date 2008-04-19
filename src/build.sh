#! /bin/sh
# $Id$
#

# A skeleton script to call the real build script at
# ./argouml-build/build.sh

cd argouml-build
./build.sh $*
cd ..
