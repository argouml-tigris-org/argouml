#!/bin/sh

# Descend into directories.
for dir in `find . -type d -print | grep -v /.svn`
do
  (
    cd $dir
    for file in *.java
    do
      if head -1 $file | grep '^//'
      then
        author=`svn info $file | egrep '^Last Changed Author:' | sed 's/^.*: //'`
	ed $file <<EOF
1d
1i
/* \$Id\$
 *******************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    $author
 *******************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

.
w
q
EOF
	svn diff -x -w $file
	echo -n "Replace? N/Y/Q "
	read ans
	case "$ans" in
	Y)
	  ;;
	Q)
	  svn revert $file
	  exit 0;
	  ;;
	*)
	  svn revert $file
	  ;;
	esac
      fi
    done
    svn ci -N -m'Added EPL License header.'
  )
done
