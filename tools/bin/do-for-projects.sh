#!/bin/sh
# $Id$

# Do the same thing for each project involved in the release.

PROJECTS="argouml \
    argouml-classfile \
    argouml-cpp \
    argouml-csharp \
    argouml-idl \
    argouml-php \
    argouml-de argouml-es argouml-en-gb argouml-fr argouml-it argouml-nb \
    argouml-pt argouml-ru \
    argouml-i18n-zh
    argoumlinstaller"

case $1 in
--checkout)
    cvs co -r $2 $PROJECTS
    ;;
*)
    for dir in $PROJECTS
    do
        ( cd $dir && $* ) 2>&1 | sed "s/^/$dir: /"
    done
    ;;
esac

