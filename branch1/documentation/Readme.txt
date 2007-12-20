Welcome to ArgoUML!
-------------------


This is the ArgoUML Documentation README file. In it you will find basic 
information on how to build and use the ArgoUML documentation.

1. DOCUMENTATION STRUCTURE

This README file should be located in the root directory of the ArgoUML
documentation directory structure.  If you are using an Eclipse project
based directory structure, the documentation root is named 
'argouml-documentation' and is at the same level as the argouml directory.
If you checked out the sources as a single tree for use with Ant from the
command line, the documentation is a subdirectory of the root argouml
directory.

This directory will be referred to as ARGODOCS_HOME in the remainder of this
document.

Eclipse
-------
argouml                 <<-- ArgoUML root directory (aka ARGO_HOME)
argouml-documentation   <<-- documentation root directory (aka ARGODOCS_HOME)

Traditional Ant command line
----------------------------
argouml                 <<-- ArgoUML root directory (aka ARGO_HOME)
  documentation         <<-- documentation root directory (aka ARGODOCS_HOME)


The ArgoUML directory structure, with explanation of the parts of the
documentation sub-directory is shown diagramatically below:

  ARGODOCS_HOME
    cookbook            <<-- DocBook XML source for developers' cookbook
    docbook-setup       <<-- DTD, XSL stylesheets and customisation XSL
      docbookx          <<-- DocBook XML DTD v4.1.2
    faq                 <<-- DocBook XML source for the FAQ (moved to html)
    images              <<-- images referenced throughout the documentation
      callouts          <<-- callout images
      cookbook          <<-- images specific to the cookbook
      navigation        <<-- images relating to navigation
      reference         <<-- images specific to the user manual reference part
      screenshots       <<-- screenshots
      tutorial          <<-- images specific to the user manual tutorial part
    manual              <<-- DocBook XML source for the User Manual
    quick-guide         <<-- DocBook XML source for the Quick Guide


In addition to this README file, ARGODOCS_HOME should contain at least the
following files:

- build.xml (an Ant build script file) 
- build.bat (a Windows batch file to start the build process when using 
             the Ant/command line directory structure).
- build.sh  (a Linux/Unix shell script file to start the build process when
             using the Ant/command line directory structure).
- build2.bat (a Windows batch file to start the build process when using 
              the Eclipse project directory structure).
- build2.sh  (a Linux/Unix shell script file to start the build process when
              using the Eclipse project directory structure). 
- default.properties - properties controlling the build process

2. BUILDING ARGOUML DOCUMENTATION

The project web site contains the latest stable versions of all the
documentation in chunked HTML form, and sometimes a snapshot of the most recent
interim build as a single HTML file.

The ArgoUML source contains almost everything you need to build the
documentation for yourself as single file HTML, chunked HTML, or PDF.

You need only to provide the following:
- Java Development Kit v1.5 or later (i.e. JDK1.5 or later).
- Jimi 1.0 if you wish to produce your own PDF (more on this below).
 
Get a DOS console and type java. You should see a list of java options appear.
If not, go get Java from sun.com

1. Open a command prompt (Windows) or terminal window (Linux/Unix)

2. Ensure that the ARGODOCS_HOME directory is your current directory (see
   diagram above for ARGODOCS_HOME)

3. Type one of the following and press the ENTER or RETURN key:

     build       (on Windows using the command line Ant directory structure)

     ./build.sh  (on Unix/Linux using the command line Ant directory structure)

     build2       (on Windows using the Eclipse project directory structure)

     ./build2.sh  (on Unix/Linux using the Eclipse project directory structure)
     
4. This executes the default target in the build file, which is:

    build defaulthtml	-- builds the documentation in HTML format chunked 
                           into mutiple pages for online usage.

    NOTE: the first execution of the build will need to fetch from the internet 
    a set of files needed for the processing of the docbook files. This is the 
    target docbook-xsl-get. So, you must be online to be able to get this, at 
    least the first time. If you must use a proxy to access the internet, then 
    either you need to change the build file using Ant's setproxy task or get 
    it manually (the URL is in the file default.properties).

The resulting documentation files are written into the 
./build/documentation directory.

For example here is the file tree after 'build docs'


  build
    documentation
      defaulthtml
        cookbook
          images
        manual
          images
        quick-guide
          images
        javahelp
      pdf
        cookbook
        manual
        quick-guide
      printablehtml
        cookbook
          images
        manual
          images
        quick-guide
          images


If you have any problems, you can get assistance from the ArgoUML developers'
mailing list, at http://argouml.tigris.org/


3. PROBLEMS WITH PDF

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

If you wish to contribute to an existing manual, please read all the 
documents in the ARGO_DOCS directory, ...there's a lot of material.


Have fun!
