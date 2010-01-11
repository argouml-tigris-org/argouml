#!/bin/sh

# Descend into directories.
for dir in `find . -type d -print | grep -v /.svn`
do
  (
    echo Processing $dir...
    cd $dir
    for file in *.java
    do
      if [ "$file" = "*.java" ]
      then
        continue;
      fi

      if head -1 $file | grep '^//'
      then
        author=`svn info -r {2010-01-01} $file | egrep '^Last Changed Author:' | sed 's/^.*: //'`
	ed $file <<EOF
1d
1i
/* \$Id\$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    $author
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

.
w
q
EOF
	svn diff -x -w $file > svn-diff.tmp
        if [ "X`wc -l < svn-diff.tmp`" != X24 ]
        then
          cat svn-diff.tmp
	  echo -n "Replace? N/Y/Q "
	  read ans
	  case "$ans" in
	  Y)
	    ;;
	  Q)
	    svn revert $file
            rm svn-diff.tmp
	    exit 0;
	    ;;
	  *)
	    svn revert $file
	    ;;
	  esac
        fi
      fi
    done
    svn ci --depth files -m'Added EPL License header.'
    rm -f svn-diff.tmp
    echo Processing $dir...done.
  )
done
