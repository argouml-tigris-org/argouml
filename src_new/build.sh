#!/bin/sh

echo
echo "Argo Build System (borrowed from FOP)"
echo "-------------------------------------"
echo

ANT_HOME=$HOME/utils/jakarta-ant/lib
NSUML_HOME=$HOME/gentleware/lib
XML_HOME=$NSUML_HOME
OCL_HOME=$NSUML_HOME
LOCALCLASSPATH=$NSUML_HOME/nsuml.jar:$ANT_HOME/ant.jar:$NSUML_HOME/xml4j.jar:$OCL_HOME/ocl-argo.jar

#if [ "$JAVA_HOME" = "" ] ; then
#  echo "ERROR: JAVA_HOME not found in your environment."
#  echo
#  echo "Please, set the JAVA_HOME variable in your environment to match the"
#  echo "location of the Java Virtual Machine you want to use."
#  exit 1
#fi

echo Building with classpath $CLASSPATH:$LOCALCLASSPATH
echo

echo Starting Ant...
echo

java -Dant.home=$ANT_HOME -classpath $CLASSPATH:$LOCALCLASSPATH org.apache.tools.ant.Main $*

#$JAVA_HOME/bin/java -Dant.home=$ANT_HOME -classpath "$LOCALCLASSPATH:$CLASSPATH" org.apache.tools.ant.Main $*
