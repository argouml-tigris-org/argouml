#! /bin/sh
#

# $Id$

#
 
# Always use the ant that comes with ArgoUML
if [ -e `pwd`/../argouml-core-tools ] ; then
    ANT_HOME=`pwd`/../argouml-core-tools/apache-ant-1.7.0
elif [ -e `pwd`/../tools ] ; then
    ANT_HOME=`pwd`/../tools/apache-ant-1.7.0
else
    echo "***************************************************************"
    echo "  ERROR: tools directory not found."
    echo "***************************************************************"
    exit 1
fi

echo ANT_HOME is: $ANT_HOME
echo
echo Starting Ant...
echo

$ANT_HOME/bin/ant $*

exit
