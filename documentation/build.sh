#! /bin/sh
#
# Author: Kunle Odutola
#         May 2001
#
# $Id$
#
 
# +-------------------------------------------------------------------------+
# | Verify and Set Required Environment Variables                           |
# +-------------------------------------------------------------------------+

# Always use the ant that comes with ArgoUML

ANT_HOME=../tools/ant-1.4.1

#	+---------------------------------------------------------------------+
#	| Check for existence of JAVA_HOME environment variable               |
#	+---------------------------------------------------------------------+

if [ "$JAVA_HOME" = "" ] ; then
	#	+---------------------------------------------------------------+
	#	| JAVA_HOME environment variable not found                      |
	#	+---------------------------------------------------------------+

	echo "******************************************************************"
	echo "  ERROR: JAVA_HOME environment variable not found."
	echo ""
	echo "  Please set JAVA_HOME to the Java JDK installation directory."
	echo "******************************************************************"
	exit 1
fi

#	+--------------------------------------------------------------------+
# 	| Add Java's tools.jar to the classpath for running Ant              |
#	+--------------------------------------------------------------------+

if [ -s $JAVA_HOME/lib/tools.jar ] ; then
	LOCAL_CLASSPATH=$LOCAL_CLASSPATH:$JAVA_HOME/lib/tools.jar
fi

# For JDK1.1 only
#if [ -s $JAVA_HOME/jre/lib/classes.zip ] ; then
#	LOCAL_CLASSPATH=$LOCAL_CLASSPATH:$JAVA_HOME/jre/lib/classes.zip
#fi

# 	+--------------------------------------------------------------------+
# 	| Add required .jar files to local classpath string                  |
# 	+--------------------------------------------------------------------+
# LOCAL_CLASSPATH=$LOCAL_CLASSPATH:../lib/ant.jar
# LOCAL_CLASSPATH=$LOCAL_CLASSPATH:../lib/optional.jar
# LOCAL_CLASSPATH=$LOCAL_CLASSPATH:../lib/NetComponents.jar
# LOCAL_CLASSPATH=$LOCAL_CLASSPATH:../lib/parser.jar
# LOCAL_CLASSPATH=$LOCAL_CLASSPATH:../lib/jaxp.jar
# LOCAL_CLASSPATH=$LOCAL_CLASSPATH:../lib/xalan.jar
# LOCAL_CLASSPATH=$LOCAL_CLASSPATH:../lib/xerces.jar
# LOCAL_CLASSPATH=$LOCAL_CLASSPATH:../lib/bsf.jar
# LOCAL_CLASSPATH=$LOCAL_CLASSPATH:../lib/js.jar
# #LOCAL_CLASSPATH=$LOCAL_CLASSPATH:../lib/fop.jar
# LOCAL_CLASSPATH=$LOCAL_CLASSPATH:../lib/w3c.jar
# LOCAL_CLASSPATH=$LOCAL_CLASSPATH:../lib

# Uncomment following line to add current CLASSPATH to end of LOCAL_CLASSPATH
# LOCAL_CLASSPATH=$LOCAL_CLASSPATH:$CLASSPATH


if [ "$JAVACMD" = "" ] ; then
   JAVACMD=java
fi

echo Building with classpath: $LOCAL_CLASSPATH
echo
echo ANT_HOME is: $ANT_HOME
echo
echo Starting Ant...
echo

$ANT_HOME/bin/$ANT $*

exit
