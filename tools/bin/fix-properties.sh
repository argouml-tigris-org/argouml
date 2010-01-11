#!/bin/sh

find . -type f \( -name \*.java -o -name \*.xml -o -name \*.txt \) -print |
xargs -r svn propset svn:eol-style native

find . -type f \( -name \*.java -o -name \*.xml -o -name \*.properties -o -name \*.txt -o -name \*.html \) -print |
xargs -r svn propset svn:keywords 'Author Date Id Revision'

find . -type f \( -name \*.java -o -name \*.xml -o -name \*.properties -o -name \*.txt \) -print |
xargs -r svn propdel svn:executable

# Scripts
find . -type f -name \*.bat -print |
xargs -r svn propset svn:eol-style 'CRLF'

find . -type f -name \*.sh -print |
xargs -r svn propset svn:eol-style 'LF'

find . -type f \( -name \*.sh -o -name \*.bat \) -print |
xargs -r svn propset svn:executable '*'
