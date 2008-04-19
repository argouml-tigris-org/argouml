Welcome to ArgoUML!

(This file was last updated by $Author$ 
on $Date$.)

In case you just downloaded and unzipped your ArgoUML distribution,
here's how to proceed.

First, you need the runtime environment for Java (J2SE) installed, get it
from http://www.java.com for your platform.

At least a JRE 1.5 is required. Please let us know if you experience
any JDK related problems. Try typing "java" in a console window to see
if Java is successfully installed.  You should get a list of your
options. If not, try reinstalling Java.

Now (after unpacking the distro, which you obviously have done,
because this file was in it) you should have a bunch of .jar files
(argouml.jar, gef.jar and others...) in your directory.  Please do
NOT unpack the .jars! Just leave them as jar files.

Now you can start the argouml.jar, on a Windows platform by
(double-)clicking it. 
Alternatively, you can type the following at a
command line console: java -jar argouml.jar

The same effect can be obtained by creating a batch-file (in the same
dir as argouml.jar), with the following contents:
  java -jar argouml.jar
which can then be started via a shortcut on the desktop.


Detailed instructions in running ArgoUML from a command line console
on Windows:

Suppose that you have downloaded ArgoUML to the C: drive, so your
directory looks like this:
  c:\ArgoUML
Open your DOS command prompt and type
  cd \
to set the current directory to the root. Then type:
  c\:>cd ArgoUML
which will give you the prompt:
  c:\ArgoUML>
Then type: 
  dir 
to see everything in the ArgoUML folder. You should have the
argouml.jar file present in this folder. Note that you need all
downloaded jar files together in this directory.

Then type
  java -jar argouml.jar
That's it, ArgoUML should start up now. 

If not, there is a problem with the downloading on your computer.
Please have a look at http://argouml.tigris.org in the FAQ 
and then consider posting an issue.


Modules:

If you downloaded the modules archive, which contains the support for 
code generation of other languages then Java, then put their jar files 
in a subdirectory named ext.


Memory:

If you have plenty of memory and wants ArgoUML to be able to handle
anything but the smallest projects, try:
java -Xms250M -Xmx500M -jar argouml.jar 


Batch mode:

The following is an example of how ArgoUML is run in batchmode:
java -jar argouml.jar -batch -command "org.argouml.uml.ui.ActionOpenProject=test.zargo" -command org.argouml.ui.cmd.ActionGotoDiagram=A -command "org.argouml.uml.ui.ActionSaveGraphics=test.PNG"
Instead of "test.zargo", it may be better to include the complete path, e.g.
c:\Documents and Settings\YourNameHere\My Documents\test.zargo


Have fun!
