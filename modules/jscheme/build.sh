#!/bin/sh

#
# build.sh always calls the version of ant distributed with ArgoUML
#

ANT_HOME=../../tools/ant-1.4.1
export ANT_HOME

$ANT_HOME\bin\ant $*
