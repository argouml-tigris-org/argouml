#!/bin/sh
java  -cp /opt/cocoon2/xml-cocoon/lib/xalan-2.0.1.jar:/opt/cocoon2/xml-cocoon/lib/xerces_1_3_0.jar org.apache.xalan.xslt.Process -TT -V -IN devfaq.xml -OUT faq.html
