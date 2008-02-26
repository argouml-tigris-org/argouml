Welcome to ArgoUML!
-------------------


This is the ArgoUML Documentation README file. In it you will find basic 
information on how to build and use the ArgoUML documentation.

1. DOCUMENTATION STRUCTURE

This README file should be located in the root directory of the ArgoUML
documentation directory structure.  If you are using an Eclipse project
based directory structure, the documentation root is named 
'argouml-documentation' and is at the same level as the other subsystem
directories (argouml-core-model, argouml-core-infra, argouml-app, ...).
If you checked out the sources as a single tree for use with Ant from the
command line, the documentation is a subdirectory of the root argouml
directory.

This directory will be referred to as ARGODOCS_HOME in the remainder of this
document.

Eclipse
-------
argouml-core-tools      <<-- ArgoUML tools directory (checked out from trunk/tools)
argouml-documentation   <<-- documentation root directory (checked out from trunk/documentation)

Traditional Ant command line
----------------------------
argouml                 <<-- ArgoUML root directory (checked out from trunk)
  documentation         <<-- documentation root directory (checked out from trunk/documentation)


The ArgoUML directory structure, with explanation of the parts of the
documentation sub-directory is shown diagramatically below:

  ARGODOCS_HOME
    cookbook            <<-- DocBook XML source for developers' cookbook
    docbook-setup       <<-- DTD, XSL stylesheets and customisation XSL
    manual              <<-- DocBook XML source for the User Manual
    quick-guide         <<-- DocBook XML source for the Quick Guide


In addition to this README file, ARGODOCS_HOME contains the
following files:

- build.xml (an Ant build script file) 
- build.bat (a Windows batch file to start the build process when using 
             the Ant/command line directory structure).
- build.sh  (a Linux/Unix shell script file to start the build process when
             using the Ant/command line directory structure).
- default.properties - properties controlling the build process
- *.launch, eclipse-ant-documentation.properties - Launch configurations
            and special property file to build within Eclipse.


2. BUILDING PDF

The documentation uses Fop (part of the Apache/XML project) to generate the
PDF. Fop relies on Sun's Jimi 1.0 library to process images in PNG format,
which cannot be distributed as part of ArgoUML for licensing reasons.

The version of Fop supplied will work fine, but any PNG images will be missing
from the final PDF.

If you wish to build the PDF with all images, you will have to fetch
you own version of Jimi. The included Fop file is prepared for Jimi.

(This information is from the Fop release notice 
 http://xml.apache.org/fop/relnotes.html)

1. Download Jimi (from http://java.sun.com/products/jimi/)

2. Extract the files from the archive

3. Copy JimiProClasses.zip to the tools/lib directory
   (argouml-core-tools/lib if using Eclipse projects)

You can then use the normal documentation build procedures for PDF, and PNG
images will be included.


4. CONTRIBUTING

If you wish to contribute to an existing manual, please read the Cookbook
on how to work with the documentation and contact the editor to discuss
your contributions. You are most welcome!


Have fun!
