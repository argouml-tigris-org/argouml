This document shortly describes how to compile the ArgoUML sources 
after you downloaded them. There is more information in the file build.xml.

(This file was last updated by $Author$ on $Date$.)
Disclamer:
  For the last two years, the recommendation has been to download from
  cvs if you intend to build from source. For that reason this
  procedure is not that well tested. If you verify that it works,
  please let us know so that this text can be updated.

At the bottom of this page you will find how to compile under Netbeans
3.x

First, you need to get Java installed. The easiest way to check that
is to get a command line and type java. A list of options should
appear.

At the moment you can build with JDK 1.4 and JDK 5.0. Set the
JAVA_HOME environment variable to where Java is installed on your
disk.  For example under Windows:
  set JAVA_HOME=C:\j2sdk_nb\j2sdk1.4.2
(Using cygwin there has been some ant problem when this includes
spaces. If you encounter it too, try moving the jdk to somewhere
without a space.)

Then, you need at least ANT 1.2 (http://jakarta.apache.org) installed. 
Ant 1.4.1 works as well. 

This includes setting the ANT_HOME environment variable.
For example for Windoze:
  path C:\jdk1.3.1\bin;C:\jakarta-ant-1.4.1\bin
  set ANT_HOME=C:\jakarta-ant-1.4.1

Here's a sample autoexec.bat that works under Win98:

  path C:\jdk1.3.1\bin;C:\jakarta-ant-1.4.1\bin
  set JAVA_HOME=C:\jdk1.3.1
  set ANT_HOME=C:\jakarta-ant-1.4.1

Please note that the build.xml sets its own CLASSPATH, there is no need 
for you to set one up.

Most problems with building Argo will stem from not having set 
the Java environment correctly.

Next, you need to have 
gef.jar, xercesImpl.jar, xml-apis.jar, nsuml.jar, antlrall.jar and ocl-argo.jar in argouml/lib
which must be next to argouml/src_new in your directory structure.
If you've downloaded the CVS then everything is set to run:
type "build" to test the Ant environment.  You should see a
list of options appear.
Type (run) "build package"
and ArgoUML will be compiled. 

<<<<<Netbeans 3.3.x>>>>>>

You must set the src_new folder as a relative mount!

Load the following jars into the project:
antlrall.jar (this is used for Java Re)
gef.jar (this is the diagram handler)
log4j (used for logging, see resource folder for existing log sets)
W3C xml etc
nsuml
ocl-argo (Object constraint language)
xerces
xml-apis
tools (from the jdk library, without this you will probably get SAX errors)

Be sure to set the main class under the Project and then compile. 


Toby 
with modifications by 
Curt Arnold
Dennis Daniels

problems? questions? comments?
users@argouml.org
dev@argouml.org 