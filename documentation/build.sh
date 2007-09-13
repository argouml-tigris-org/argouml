#! /bin/sh
#

# $Id$

#
 
# Always use the ant that comes with ArgoUML
ANT_HOME=../tools/apache-ant-1.7.0

echo ANT_HOME is: $ANT_HOME
echo
echo Starting Ant...
echo

$ANT_HOME/bin/ant $*

exit
