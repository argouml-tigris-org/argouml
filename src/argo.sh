#!/bin/sh
java -green -Xmx128m -classpath ./build/argouml.jar:/usr/local/xml4j/xml4j.jar:../../nsuml/lib/xml4j_dom.jar:/usr/local/antlr:../../nsuml/lib/nsuml.jar:../../nsuml/lib/collections.jar:$CLASSPATH uci.uml.Main -big 

