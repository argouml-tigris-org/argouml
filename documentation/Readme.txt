Welcome to ArgoUML!
-------------------


This is the ArgoUML Documentation README file. In it you will find basic 
information on how to build and use the ArgoUML documentation.

1. DOCUMENTATION STRUCTURE

This README file should be located in the root directory of the ArgoUML 
documentation directory structure. At the time of writing, this was the 
'documentation' sub-directory of the ArgoUML root directory. 
This directory will be referred to as ARGODOCS_HOME in the remainder of this 
document. This is shown diagramatically below:

	
	                    argouml <<-- ArgoUML root directory (aka ARGO_HOME)
                       bin
                       build
                       Cvs
                       documentation<<-- documentation root directory (aka ARGODOCS_HOME)
                           cookbook <<-- there is cookbook material but where?
                           Cvs
                           docbook-setup <<-- contains DocBook XML v4.1.2 & DocBook Stylesheets v1.37  (has sub-dirs)
                          faq <<-- for DocBook source files for "FAQ"
                           images<<-- contains images referenced in documentation              (has sub-dirs)
                              callouts <<-- contains images referenced in documentation used in doc book 
                              Cvs
                              navigation
                              screenshots <<-- lots of screen shots here! 
                          javahelp
                          manual <<-- for DocBook source files for "Manual"
                          quick-guide <<-- for DocBook source files for "Quick Guide"
                       lib
                       modules



In addition to this README file, ARGODOCS_HOME should contain at least the following files:
- build.xml (an Ant build script file) 
- build.bat (a Windows batch file to start the build process).
- build.sh  (a Linux/Unix shell script file to start the build process)  

***THIS HAS NOT BEEN TESTED!!***


2. BUILDING ARGOUML DOCUMENTATION

Typically, before you can use the ArgoUML documentation, you will need to build
 it first. The ArgoUML source distribution and CVS contains almost everything 
 that is needed to build the documentation. You only need to ensure that you 
 have installed Java Development Kit v1.2.2 or later (i.e. JDK1.2.2 or later).
 
 Get a DOS console and type java. You should see a list of java options appear.
 If not, go get Java from sun.com

Once you have the JDK installed, you can build the ArgoUML documentation by:

1. Open an MSDOS command window (windows 9x)
or 
Command prompt (Windows NT/2000) 
or 
Terminal window (Linux/UNix)

2. Ensure that the ARGODOCS_HOME directory is your current directory 
(see diagram above for ARGODOCS_HOME)

3. Type the following and press the ENTER or RETURN key:
	build

4. This should give you some more info about the targets define for the build process. 

For instance:

	build defaulthtml	-- builds the documentation in HTML format chunked 
                                   into mutiple pages for online usage.
	build docs -- builds everything 
	
	Be sure to checkout printable.html, perhaps the easiest one to use for a quick
	search of questions and answers.																 

Depending on the target selected, the resulting documentation files may be 
in the ARGO_HOME/build directory or in the ARGO_HOME/dist directory.

For example here is the file tree after 'build docs'
               argo
                   argouml
                       bin
                       build
                           Cvs
                           documentation
                              defaulthtml
                                  cookbook
                                     images
                                          callouts
                                          navigation
                                         screenshots
                                  faq
                                     images
                                         callouts
                                          navigation
                                          screenshots
                                  javahelp
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
                              printablehtml<< these are very good searching for key words
                                  cookbook
                                      images
                                  faq
                                     images
                                  manual
                                      images
                                  quick-guide
                                     images
                       src
                       src_new
                       www



If you have any problems, you can get assistance from the ArgoUML users 
mailing list, at http://www.argouml.org/


3. CONTRIBUTING

If you wish to contribute to an existing manual, please read all the 
documents in the ARGO_DOCS directory, ...there's a lot of material.




===========================================================================

HISTORY

2001/05/17  Kunle	Initial check-in to CVS.

05/Dec/2001 Dennis Daniels

Have fun!
