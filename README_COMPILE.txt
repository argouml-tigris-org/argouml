In this document it is shortly described how to compile the ArgoUML sources 
after you downloaded them. There is more information in the file build.xml.

First, you need Java installed. At the moment JDK1.2.2 is best for Argo, 
but 1.3 works to some extent. Please do

--- set JAVA_HOME environment variable to where Java is installed on your disk.

Then, you need ANT 1.2 (http://jakarta.apache.org) installed. This includes 

--- setting ANT_HOME environment variable

Next, you need to have 

--- gef.jar, xerces.jar, nsuml.jar, antlrall.jar and ocl-argo.jar in argouml/lib

which must be next to argouml/src_new in your directory structure.

Then, you can just 

--- type (run) "build package"

and ArgoUML will be compiled. Please note that the build.xml sets its own 
CLASSPATH, there is no need for you to set one up.

Good luck,
Toby 
(with modifications by Curt Arnold)
