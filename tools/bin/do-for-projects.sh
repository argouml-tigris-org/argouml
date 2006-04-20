#!/bin/sh
# $Id$

# Do the same thing for each project involved in the release.

PROJECTS="CHECKOUT_PROJECTS="argouml \
    argouml-classfile \
    argouml-cpp \
    argouml-csharp \
    argouml-idl \
    argouml-php \
    argouml-de argouml-es argouml-en-gb argouml-fr argouml-nb \
    argouml-pt argouml-ru \
    argouml-i18n-zh"

case $1 in
--checkout)
    cvs co -r $2 $CHECKOUT_PROJECTS
    ;;
*)
    for dir in $PROJECTS
    do
        ( cd $dir && $* )
    done
    ;;
esac

