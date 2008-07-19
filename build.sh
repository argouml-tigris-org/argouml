#! /bin/sh
# $Id$
#
# build.sh always calls the version of ant distributed with ArgoUML
#

# OS specific support.
darwin=false
case "`uname`" in
    Darwin*) darwin=true;;
esac

# +-------------------------------------------------------------------------+
# | Verify and Set Required Environment Variables                           |
# +-------------------------------------------------------------------------+
if [ "$JAVA_HOME" = "" ] ; then
    if $darwin; then
	# Set Java Home automatically
        JAVA_HOME=/Library/Java/Home
	export JAVA_HOME
    else
	echo "***************************************************************"
	echo "  ERROR: JAVA_HOME environment variable not found."
	echo ""
	echo "  Please set JAVA_HOME to the Java JDK installation directory."
	echo "***************************************************************"
	exit 1
    fi
fi


if [ -d `pwd`/tools ] ; then
    ANT_HOME=`pwd`/tools/apache-ant-1.7.0
else
    echo "***************************************************************"
    echo "  ERROR: tools directory not found."
    echo ""
    echo "  Please check-out the argouml tools directory to"
    echo ""
    echo " `pwd`/tools"
    echo "***************************************************************"
    exit 1
fi

echo ANT_HOME is: $ANT_HOME
echo
echo Starting Ant...
echo

$ANT_HOME/bin/ant $*

#exit
