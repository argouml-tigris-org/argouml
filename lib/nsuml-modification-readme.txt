The source of "nsuml.jar" can be found at http://nsuml.sourceforge.net/ .
A bug has been reported, but the project seems to be dead. 
None of the ArgoUML developers has any right in this project, 
so there is no way to get this jar updated.
The latest stable version published is V0.4.20 and ArgoUML used 0.4.19. 

Problem 1: 
----------
The published nsuml does not save the isConcurrent composite state's attribute.

I (kolepep@tigris.org) downloaded the 0.4.20 code and just added or changed 
some code lines which fix the problem.

The added code is:

al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isConcurrent()));
    dh.startElement("Behavioral_Elements.State_Machines.CompositeState.isConcurrent", al); al.clear();
    dh.endElement("Behavioral_Elements.State_Machines.CompositeState.isConcurrent");

at line 10047 of XMIWriter.java


The changed code is:

if (lastName.equals("isConcurrent"))
      {
        lastMethod = "isConcurrent";	at line 12136 of XMIReader.java
        

if (lastName.equals("isConcurrent"))	at line 12160 of XMIReader.java



<attribute  name="isConcurrent" type="Boolean"/>	at line 469 of ..\generator\ru\novosoft\uml\gen\resources\metamodel.xml


I have been working with this 0.4.20 release for a while and the .zargo 
and XMI files seem to be valid. 

Jose Luis
20050109
kolepep@tigris.org






