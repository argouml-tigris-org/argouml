Welcome to ArgoUML!
-------------------


This is the ArgoUML Documentation README file. In it you will find basic 
information on how to build and use the ArgoUML documentation.

1. DOCUMENTATION STRUCTURE

This README file should be located in the root directory of the ArgoUML
documentation directory structure. At the time of writing, this is the
'documentation' sub-directory of the ArgoUML root directory.

This directory will be referred to as ARGODOCS_HOME in the remainder of this
document.

The ArgoUML directory structure, with explanation of the parts of the
documenation sub-directory is shown diagramatically below:


argouml                 <<-- ArgoUML root directory (aka ARGO_HOME)
  build
  documentation         <<-- documentation root directory (aka ARGODOCS_HOME)
    cookbook            <<-- DocBook XML source for developers' cookbook
    docbook-setup       <<-- DTD, XSL stylesheets and customisation XSL
      docbookx          <<-- DocBook XML DTD v4.1.2
      docbook-xsl-1.49  <<-- DocBook XSL stylesheets v1.49
    faq                 <<-- DocBook XML source for the FAQ
    images              <<-- images referenced throughout the documentation
      callouts          <<-- callout images
      cookbook          <<-- images specific to the cookbook
      navigation        <<-- images relating to navigation
      reference         <<-- images specific to the user manual reference part
      screenshots       <<-- screenshots
      tutorial          <<-- images specific to the user manual tutorial part
    manual              <<-- DocBook XML source for the User Manual
    quick-guide         <<-- DocBook XML source for the Quick Guide
  lib
  modules
  src
  src_new
  www


In addition to this README file, ARGODOCS_HOME should contain at least the
following files:

- build.xml (an Ant build script file) 
- build.bat (a Windows batch file to start the build process).
- build.sh  (a Linux/Unix shell script file to start the build process)  


2. BUILDING ARGOUML DOCUMENTATION

The project web site contains the latest stable versions of all the
documentation in chunked HTML form, and also a snapshot of the most recent
interim build as a single HTML file.

The ArgoUML source contains almost everything you need to build the
documentation for yourself as single file HTML, chunked HTML, PDF or JavaHelp.

You need only to provide the following:
- Java Development Kit v1.2.2 or later (i.e. JDK1.2.2 or later).
- A version of Fop built with Jimi 1.0 if you wish to produce your own PDF
  (more on this below).
 
 Get a DOS console and type java. You should see a list of java options appear.
 If not, go get Java from sun.com

1. Open an MSDOS command window (windows 9x), or command prompt (Windows
   NT/2000) or terminal window (Linux/UNix)

2. Ensure that the ARGODOCS_HOME directory is your current directory (see
   diagram above for ARGODOCS_HOME)

3. Type one of the following and press the ENTER or RETURN key:

     build       (on Windows)

     ./build.sh  (on Unix/Linux)

4. This should give you some more info about the targets define for the build
   process. For instance:

    build defaulthtml	-- builds the documentation in HTML format chunked 
                           into mutiple pages for online usage.

    build docs          -- builds chunked HTML, single file HTML and PDF
	
Be sure to checkout printable.html, perhaps the easiest one to use for a quick
search of questions and answers.

Depending on the target selected, the resulting documentation files may be 
in the ARGO_HOME/build directory or in the ARGO_HOME/dist directory.

For example here is the file tree after 'build docs'

argouml
  build
    documentation
      defaulthtml
        cookbook
          images
        faq
          images
        manual
          images
        quick-guide
          images
        javahelp
      pdf
        cookbook
        faq
        manual
        quick-guide
      printablehtml
        cookbook
          images
        faq
          images
        manual
          images
        quick-guide
          images


If you have any problems, you can get assistance from the ArgoUML users 
mailing list, at http://www.argouml.org/


3. PROBLEMS WITH PDF

The documentation uses Fop (part of the Apache/XML project) to generate the
PDF. Fop relies on Sun's Jimi 1.0 library to process images in PNG format,
which cannot be distributed as part of ArgoUML for licensing reasons.

The version of Fop supplied will work fine, but any PNG images will be missing
from the final PDF.

If you wish to build the PDF with all images, you will have to create your own
version of Fop that includes Jimi.

1. Optain the source for Jimi (from http://www.sun.com)

2. Build Jimi

3. Create a jar file for Jimi, jimi-1.0.jar

4. Obtain the source for Fop (from http://www.apache.org)

5. Build Fop ENSURING that you have jimi-1.0.jar on your CLASSPATH

6. Replace the standard fop.jar in the argouml/lib directory with the new
   fop.jar you have just built.

You can then use the normal documentation build procedures for PDF, and PNG
images will be included.


4. CONTRIBUTING

If you wish to contribute to an existing manual, please read all the 
documents in the ARGO_DOCS directory, ...there's a lot of material.




===========================================================================

HISTORY

2001/05/17  Kunle	Initial check-in to CVS.

2001/12/05  Dennis Daniels

2002/03/07  Jeremy Bennett (mail@jeremybennet.com). Updated prior to upgrading
            to XSL 1.49 stylesheets

Have fun!
