#!/bin/sh

echo
echo "Argo Build System (borrowed from FOP)"
echo "-------------------------------------"
echo

ANT_HOME=~/bin/jakarta-ant/lib
LIBS=~/jars
JAVA_HOME=/usr/remote/jdk12
CLASSPATH=$ANT_HOME/jaxp.jar:$ANT_HOME/parser.jar:$JAVA_HOME/lib/tools.jar:$JAVA_HOME/lib/classes.zip:$ANT_HOME/ant.jar:$ANT_HOME/xml.jar:$LIBS/nsuml.jar:$LIBS/xml4j.jar:$LIBS/ocl-argo.jar:$CLASSPATH

if [ "$JAVA_HOME" = "" ] ; then
  echo "ERROR: JAVA_HOME not found in your environment."
  echo
  echo "Please, set the JAVA_HOME variable in your environment to match the"
  echo "location of the Java Virtual Machine you want to use."
  exit 1
fi

echo Building with classpath $CLASSPATH
echo

echo Starting Ant...
echo

ant $*

# $JAVA_HOME/bin/java -Dant.home=$ANT_HOME -classpath "$CLASSPATH" org.apache.tools.ant.Main $*
