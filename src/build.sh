#!/bin/sh

echo
echo "Argo Build System (borrowed from FOP)"
echo "-------------------------------------"
echo

ANT_HOME=/usr/local/jakarta-ant/lib/
NSUML_HOME=/home/andreas/argo/nsuml/
XML_HOME=/usr/local/xml4j/
OCL_HOME=/home/andreas/argo/ocl/
LOCALCLASSPATH=$JAVA_HOME/lib/tools.jar:$JAVA_HOME/lib/classes.zip:$ANT_HOME/ant.jar:$ANT_HOME/xml.jar:$NSUML_HOME/lib/nsuml.jar:$NSUML_HOME/lib/xml4j_dom.jar:$NSUML_HOME/lib/collections.jar:$XML_HOME/xml4j.jar:$OCL_HOME/ocl-argo-cmplt.jar

if [ "$JAVA_HOME" = "" ] ; then
  echo "ERROR: JAVA_HOME not found in your environment."
  echo
  echo "Please, set the JAVA_HOME variable in your environment to match the"
  echo "location of the Java Virtual Machine you want to use."
  exit 1
fi

echo Building with classpath $CLASSPATH:$LOCALCLASSPATH
echo

echo Starting Ant...
echo

$JAVA_HOME/bin/java -green -Dant.home=$ANT_HOME -classpath "$LOCALCLASSPATH:$CLASSPATH" org.apache.tools.ant.Main $*
