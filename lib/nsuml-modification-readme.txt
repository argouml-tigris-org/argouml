The source of "nsuml.jar" can be found at http://nsuml.sourceforge.net/ .
A bug has been reported, but the project seems to be dead. 
None of the ArgoUML developers has any right in this project, 
so there is no way to get this jar updated.
The latest stable version published is V0.4.20 and ArgoUML used 0.4.19. 

Problem 1: 
----------
The published nsuml does not save the isConcurent composite state's attribute.

Jose (kolepep@tigris.org) downloaded the 0.4.20 code and just added 
two code lines which fix the problem.

The added code is:

al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isConcurent()));
    dh.startElement("Behavioral_Elements.State_Machines.CompositeState.isConcurent", al); al.clear();
    dh.endElement("Behavioral_Elements.State_Machines.CompositeState.isConcurent");

at line 10047 of XMIWriter.java

Jose has been working with this 0.4.20 release for a while and the .zargo 
and XMI files seem to be valid. 

Michiel
20040410
mvw@tigris.org


