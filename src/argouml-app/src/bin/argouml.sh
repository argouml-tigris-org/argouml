#!/bin/sh
#
# Shell script to launch ArgoUML on Unix systems.  Mostly borrowed from
# the Apache Ant project.
#
# The idea is that you can put a softlink in your "bin" directory back
# to this file in the ArgoUML install directory and this script will
# use the link to find the jars that it needs, e.g.:
#
# ln -s /usr/local/ArgoUML/argouml.sh /usr/local/bin/argo
#
# 2002-02-25 toby@caboteria.org

## resolve links - $0 may be a link to ArgoUML's home
PRG=$0
progname=`basename $0`

while [ -h "$PRG" ] ; do
  ls=`ls -ld "$PRG"`
  link=`expr "$ls" : '.*-> \(.*\)$'`
  if expr "$link" : '.*/.*' > /dev/null; then
      PRG="$link"
  else
      PRG="`dirname $PRG`/$link"
  fi
done

java -Xms64m -Xmx512m -jar `dirname $PRG`/argouml.jar "$@"

