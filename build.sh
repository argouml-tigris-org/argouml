#!/bin/sh

# Modifications:
# ==============

# 10 Dec 2001:  Jeremy Bennett. LOCALCLASSPATH modified to point to lib sudir
#               of $ANT_HOME for ant.jar. Checks added for $JAVA_HOME and
#               $ARGO_HOME - these should be set in the environment.

#  8 Jan 2001:  Thierry Lach. LOCALCLASSPATH simplified.

#  9 Jan 2001:  Jeremy Bennett. xerces.jar added back to LOCALCLASSPATH, since
#               otherwise build cannot find org.xml.sax.InputSource

echo
echo "Argo Build System (borrowed from FOP)"
echo "-------------------------------------"
echo

# Check JAVA_HOME and ARGO_HOME are set

if [ "$JAVA_HOME" = "" ]
then
  echo "ERROR: JAVA_HOME not found in your environment."
  echo
  echo "Please, set the JAVA_HOME variable in your environment to match the"
  echo "location of the Java Virtual Machine you want to use."
  exit 1
fi

if [ "$ARGO_HOME" = "" ]
then
    #  If it isn't set, check to see if we are in the src_new
    #  directory and try it.
    if [ -f ../src_new/org/argouml.mf ]
    then
        ARGO_HOME=..
    fi
fi

if [ "$ARGO_HOME" = "" ]
then
  echo "ERROR: ARGO_HOME not found in your environment."
  echo
  echo "Please, set the ARGO_HOME variable in your environment to match the"
  echo "location of your copy of the  ArgoUML CVS source tree."
  exit 1
fi

# If $ANT_HOME is not set, use ARGO's copy

if [ "$ANT_HOME" = "" ]
then
  ANT_HOME=$ARGO_HOME
fi


PATH=$PATH:$JAVA_HOME/bin
NSUML_HOME=$ARGO_HOME/lib
XML_HOME=$NSUML_HOME
OCL_HOME=$NSUML_HOME
LOCALCLASSPATH=$ANT_HOME/lib/ant.jar:$JAVA_HOME/lib/tools.jar:$ANT_HOME/lib/parser.jar:$ANT_HOME/lib/jaxp.jar:$XML_HOME/xerces.jar

echo Building with classpath $CLASSPATH:$LOCALCLASSPATH
echo

echo Starting Ant...
echo

java -Dant.home=$ANT_HOME -classpath $CLASSPATH:$LOCALCLASSPATH org.apache.tools.ant.Main $*

