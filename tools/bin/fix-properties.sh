#!/bin/sh

# add max-args 1 to xargs if it can go wrong
CANGOWRONG="--max-args=1"

svn ls -R > ls-R.tmp

egrep '\.(java|xml|properties|txt|html)$' < ls-R.tmp | 
  xargs -r $CANGOWRONG svn propdel svn:executable
egrep '\.(java|xml|properties|txt|html)$' < ls-R.tmp | 
  xargs -r svn propset svn:keywords 'Author Date Id Revision'
egrep '\.(java|xml|txt)$' < ls-R.tmp | 
  xargs -r $CANGOWRONG svn propset svn:eol-style native

# Scripts
egrep '\.(bat)$' < ls-R.tmp | 
  xargs -r $CANGOWRONG svn propset svn:eol-style 'CRLF'
egrep '\.(sh)$' < ls-R.tmp | 
  xargs -r $CANGOWRONG svn propset svn:eol-style 'LF'
egrep '\.(bat|sh)$' < ls-R.tmp | 
  xargs -r svn propset svn:executable '*'

rm ls-R.tmp
