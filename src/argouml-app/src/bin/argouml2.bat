@echo off
java -Xms64m -Xmx512m -Dargouml.model.implementation=org.argouml.model.euml.EUMLModelImplementation -DeUML.resources=org.eclipse.uml2.uml.resources_3.1.1.v201008191505.jar -cp argouml.jar org.argouml.application.Main %1 %2 %3 %4 %5 %6 %7 %8

if errorlevel 1 pause
