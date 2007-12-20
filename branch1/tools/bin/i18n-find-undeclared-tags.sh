#!/bin/sh

# Non-portable script to find not declared tags.
#
# Actually only finds all lines with something that looks like a tag but isn't
# declared as a tag.
#
# Assumptions:
# Tags shall have names like XXX.blabla where XXX is the same as the first part
# of a filename of a property file in org/argouml/i18n. It is the first part
# up to the first _.
#
# Usage: 
# $ cd ../../src_new
# $ ../tools/bin/i18n-find-undeclared-tags.sh
# The list should be empty.
#

I18NFILES=`ls org/argouml/i18n/*.properties | grep -v _`

find . -name \*.java -print0 |
xargs -0 egrep -nH `ls org/argouml/i18n/*.properties | grep -v _ |
                    sed 's%.*/\([^/.]*\).properties%\1%' | 
                    awk 'NR == 1 { printf "\"(%s", $0; next; } 
                                 { printf "|%s", $0; } 
                         END     { printf ")\\\\."; }'` |
egrep -v `cat $I18NFILES |
          sed '/^#/d;/^$/d' |
          awk 'NR == 1 { printf "\"(%s", $1; next; } 
                       { printf "|%s", $1; } 
               END     { printf ")\""; }'`
