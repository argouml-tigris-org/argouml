#! /bin/sh
#
# $Id$
#
 
# Always use the ant that comes with ArgoUML
ANT_HOME=../tools/ant-1.4.1

echo ANT_HOME is: $ANT_HOME
echo
echo Starting Ant...
echo

$ANT_HOME/bin/ant $*

exit
