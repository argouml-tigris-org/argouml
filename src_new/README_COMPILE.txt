In this document it is shortly described how to compile the ArgoUML sources after you downloaded them. There is more information in the file build.xml.

First, you need Java installed. At the moment JDK1.2.2 is best for Argo, but 1.3 works to some extent. Please do

--- set JAVA_HOME environment variable to where Java is installed on your disk.

Then, you need jakarta-ant installed. This includes 

--- setting ANT_HOME environment variable
--- adding ANT_HOME/bin to your PATH environment variable, so you can call "ant" from command prompt

Next, you need to have 

--- xml4j.jar, nsuml.jar and ocl-argo.jar in argouml/lib

which must be next to argouml/src_new in your directory structure.

Then, you can just 

--- type (run) "ant"

and ArgoUML will be compiled. Please note that the build.xml sets its own CLASSPATH, there is no need for you to set one up.

Good luck,
Toby