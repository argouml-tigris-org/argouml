#!/bin/sh

# Run checkstyle on the changed files only.

ARGOROOT=${ARGOROOT-..}

case "$1" in
--offline)
    find . -name \*.java -print |
    while read filename
    do
        if test "$filename" -nt `dirname $filename`/CVS/Entries
        then
            echo $filename
        fi
    done
    ;;
*)
    cvs -nq update |
    egrep "^M " |
    sed 's/^M //'
    ;;
esac |
xargs java -cp "`cygpath -wap ${ARGOROOT}/tools/checkstyle-3.3/checkstyle-all-3.3.jar:${ARGOROOT}/tools/ant-1.4.1/lib/xerces-1.2.3.jar`" "-Dcheckstyle.header.file=`cygpath -wa ${ARGOROOT}/tools/checkstyle/java.header`" com.puppycrawl.tools.checkstyle.Main -c "`cygpath -wa ${ARGOROOT}/tools/checkstyle/checkstyle_argouml.xml`"
