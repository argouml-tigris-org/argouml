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

# 	+---------------------------------------------------------------------+
# 	| Check for existence of ANT_HOME environment variable                |
# 	+---------------------------------------------------------------------+

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

#
# build.sh always calls the version of ant distributed with ArgoUML
#
ANT_HOME=../tools/ant-1.4.1

# 	+--------------------------------------------------------------------+
# 	| Add required .jar files to local classpath string                  |
# 	+--------------------------------------------------------------------+
LOCAL_CLASSPATH=$LOCAL_CLASSPATH:$ANT_HOME/lib/ant.jar
LOCAL_CLASSPATH=$LOCAL_CLASSPATH:$ANT_HOME/lib/optional.jar
# LOCAL_CLASSPATH=$LOCAL_CLASSPATH:$ANT_HOME/lib/NetComponents.jar
# LOCAL_CLASSPATH=$LOCAL_CLASSPATH:$ANT_HOME/lib/parser.jar
LOCAL_CLASSPATH=$LOCAL_CLASSPATH:$ANT_HOME/lib/jaxp.jar
LOCAL_CLASSPATH=$LOCAL_CLASSPATH:$ANT_HOME/lib/crimson.jar
# LOCAL_CLASSPATH=$LOCAL_CLASSPATH:$ANT_HOME/lib/xalan.jar
# LOCAL_CLASSPATH=$LOCAL_CLASSPATH:$ANT_HOME/lib/xerces.jar
# LOCAL_CLASSPATH=$LOCAL_CLASSPATH:$ANT_HOME/lib/bsf.jar
# LOCAL_CLASSPATH=$LOCAL_CLASSPATH:$ANT_HOME/lib/js.jar
# #LOCAL_CLASSPATH=$LOCAL_CLASSPATH:../lib/fop.jar
# LOCAL_CLASSPATH=$LOCAL_CLASSPATH:$ANT_HOME/lib/w3c.jar
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

$ANT_HOME/bin/ant $*

exit
