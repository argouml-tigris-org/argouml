#!/bin/sh

# Run checkstyle on the changed files only.

ARGOROOT=${ARGOROOT-..}

CHECKSTYLE_CONFIG=${ARGOROOT}/tools/checkstyle/checkstyle_argouml.xml
CLASSPATH=${ARGOROOT}/tools/checkstyle-3.3/checkstyle-all-3.3.jar
CHECKSTYLE_HEADER_FILE=${ARGOROOT}/tools/checkstyle/java.header

OFFLINE=false
case "$1" in
--offline)
    OFFLINE=true
    ;;
--paranoid)
    CHECKSTYLE_CONFIG=${ARGOROOT}/tools/checkstyle/checkstyle_argouml_paranoid.xml
    ;;
esac

cygwin=false
case "`uname`" in
  CYGWIN*) cygwin=true ;;
esac

if $cygwin
then
    CLASSPATH=`cygpath -wap $CLASSPATH`
    CHECKSTYLE_HEADER_FILE=`cygpath -wa $CHECKSTYLE_HEADER_FILE`
    CHECKSTYLE_CONFIG=`cygpath -wa $CHECKSTYLE_CONFIG`
fi

if $OFFLINE
then
    find . -name \*.java -print |
    while read filename
    do
        if test "$filename" -nt `dirname $filename`/CVS/Entries
        then
            echo $filename
        fi
    done
else
    cvs -nq update |
    egrep "^M " |
    sed 's/^M //'
fi |
xargs java -cp "$CLASSPATH" "-Dcheckstyle.header.file=$CHECKSTYLE_HEADER_FILE" com.puppycrawl.tools.checkstyle.Main -c "$CHECKSTYLE_CONFIG"
