This document shortly describes how to compile the ArgoUML sources 
from a downloaded tarball or zip. There is more information in the 
file build.xml.

                      **** NOTE ****
  We recommend downloading sources from SVN instead of using tarballs.
  Instructions for building from SVN are available in the developer's 
  "cookbook" on the project web site (http://argouml.tigris.org).
  The Quick Start section will get you up and running in minutes.
  
  The procedure documented here is not well tested and only gets updated
  sporadicaly. If you verify that it works (or you have corrections), please
  let us know so that this text can be updated.

When using Eclipse, see the cookbook for an extensive description.

First, you need to get Java installed. The easiest way to check that
is to get a command line and type java. A list of options should
appear.

At the moment you can build with JDK 5 and 6. Set the
JAVA_HOME environment variable to where Java is installed on your
disk.  For example under Windows:
  set JAVA_HOME=C:\java\jdk1.5.0_11
(Using cygwin there has been some ant problem when this includes
spaces. If you encounter it too, try moving the jdk to somewhere
without a space.)

Then, you need at least Ant 1.7.0 (http://ant.apache.org) installed. 
Later versions of Ant should work as well. 

This includes setting the ANT_HOME environment variable.
For example for Windows:
  path C:\java\jdk1.5.0_11\bin;C:\java\ant-1.7.0\bin
  set ANT_HOME=C:\java\ant-1.7.0

Here's a sample autoexec.bat that works under Win98:

  path C:\java\jdk1.5.0_11\bin;C:\java\ant-1.7.0\bin
  set JAVA_HOME=C:\java\jdk1.5.0_11
  set ANT_HOME=C:\java\ant-1.7.0

Please note that the build.xml sets its own CLASSPATH, there is no need 
for you to set one up.

Most problems with building Argo will stem from not having set 
the Java environment correctly.

Next, you need to have all the jar files from the associated
libs archive that matches your source archive
(e.g. ArgoUML-0.25.4-libs.zip for the sources in ArgoUML-0.25.4-src.zip)
unpacked into argouml/lib, next to the directory argouml/src_new
that was created when you unpacked the source archive.

If you've downloaded from SVN then everything is set to run:
type "build" to test the Ant environment.  You should see a
list of options appear.
Type (run) "build package"
and ArgoUML will be compiled. 


Toby 
with modifications by 
Curt Arnold
Dennis Daniels
Michiel van der Wulp

problems? questions? comments?
users@argouml.org
dev@argouml.org 

(This file was last updated by $Author$
on $Date$.)