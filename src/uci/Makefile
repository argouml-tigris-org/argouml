# Copyright (c) 1995, 1996 Regents of the University of California.
# All rights reserved.
#
# This software was developed by the Arcadia project
# at the University of California, Irvine.
#
# Redistribution and use in source and binary forms are permitted
# provided that the above copyright notice and this paragraph are
# duplicated in all such forms and that any documentation,
# advertising materials, and other materials related to such
# distribution and use acknowledge that the software was developed
# by the University of California, Irvine.  The name of the
# University may not be used to endorse or promote products derived
# from this software without specific prior written permission.
# THIS SOFTWARE IS PROVIDED ``AS IS'' AND WITHOUT ANY EXPRESS OR
# IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
# WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.

# File: uci/Makefile
# makefile for the uci shared code directory
# original author: jrobbins@ics.uci.edu
# $Id$

include Makefile.config

all: classes


classes: gefClasses umlClasses argoumlClasses criticsClasses swingClasses

gefClasses:
	@if [ -d gef ] ; \
	then echo "\n\nCOMPILING GEF ================" ; \
	cd util; make classes; cd .. ; \
	cd gef; make classes; cd .. ; \
	cd gef/event; make classes; cd ../.. ; \
	cd graph; make classes; cd .. ; \
	cd beans/editors; make classes; cd ../.. ; \
	cd ui; make classes; cd .. ; \
	cd gef/demo; make classes; cd ../.. ; \
	fi

umlClasses:
	@if [ -d uml ] ; \
	then echo "\n\nCOMPILING UML META-MODEL ================" ; \
	cd uml ; \
	cd Foundation/Core; make classes1; cd ../.. ; \
	cd Foundation/Data_Types; make classes; cd ../.. ; \
	cd Foundation/Extension_Mechanisms; make classes; cd ../.. ; \
	cd Model_Management; make classes; cd .. ; \
	cd Foundation/Core; make classes2; cd ../.. ; \
	cd Behavioral_Elements/Common_Behavior; make classes; cd ../.. ; \
	cd Behavioral_Elements/State_Machines; make classes; cd ../.. ; \
	cd Behavioral_Elements/Use_Cases; make classes; cd ../.. ; \
	cd Behavioral_Elements/Collaborations; make classes; cd ../.. ; \
	cd util; make classes; cd .. ; \
	cd test/omg; make classes; cd ../.. ; \
	cd generate; make classes; cd .. ; \
	make classes; \
	cd .. ; \
	fi

argoumlClasses:
	@if [ -d uml/ui ] ; \
	then echo "\n\nCOMPILING ARGO/UML ================" ; \
	cd argo ; \
	cd kernel; make classes; cd .. ; \
	cd checklist; make classes; cd .. ; \
	cd .. ; \
	cd uml ; \
	cd ui; make classes; cd .. ; \
	cd visual; make classes; cd .. ; \
	cd .. ; \
	fi

#cd xmi; make classes; cd .. ; \

criticsClasses:
	@if [ -d uml/critics ] ; \
	then echo "\n\nCOMPILING UML CRITICS ================" ; \
	cd uml ; \
	cd checklist; make classes; cd .. ; \
	cd critics; make classes; cd .. ; \
	cd critics/patterns; make classes; cd ../.. ; \
	cd critics/java; make classes; cd ../.. ; \
	cd checklist; make classes; cd ../.. ; \
	cd .. ; \
	fi

swingClasses:
	@if [ -d ../com/sun ] ; \
	then echo "\n\nCOMPILING SWING MODIFICATIONS ================" ; \
	cd ../com/sun/java/swing; make classes;  \
	cd text; make classes; cd .. ; \
	cd plaf/metal; make classes; cd ../.. ; \
	cd ../../../../uci ; \
	fi


fresh: gefFresh umlFresh argoumlFresh criticsFresh


gefFresh: 
	@if [ -d gef ] ; \
	then echo "\n\nJAVAC *.java GEF ================" ; \
	cd util; $(JAVAC) $(JFLAGS) *.java; cd .. ; \
	cd gef; $(JAVAC) $(JFLAGS) *.java; cd .. ; \
	cd gef/event; $(JAVAC) $(JFLAGS) *.java; cd ../.. ; \
	cd graph; $(JAVAC) $(JFLAGS) *.java; cd .. ; \
	cd beans/editors; $(JAVAC) $(JFLAGS) *.java; cd ../.. ; \
	cd ui; $(JAVAC) $(JFLAGS) *.java; cd .. ; \
	cd gef/demo; $(JAVAC) $(JFLAGS) *.java; cd ../.. ; \
	fi

umlFresh:
	@if [ -d uml ] ; \
	then echo "\n\nJAVAC *.java UML META-MODEL ================" ; \
	cd uml ;\
	cd Foundation/Core; $(JAVAC) $(JFLAGS) *.java; cd ../.. ; \
	cd Foundation/Data_Types; $(JAVAC) $(JFLAGS) *.java; cd ../.. ; \
	cd Foundation/Extension_Mechanisms; $(JAVAC) $(JFLAGS) *.java; cd ../.. ; \
	cd Model_Management; $(JAVAC) $(JFLAGS) *.java; cd .. ; \
	cd Behavioral_Elements/Common_Behavior; $(JAVAC) $(JFLAGS) *.java; cd ../.. ; \
	cd Behavioral_Elements/State_Machines; $(JAVAC) $(JFLAGS) *.java; cd ../.. ; \
	cd Behavioral_Elements/Use_Cases; $(JAVAC) $(JFLAGS) *.java; cd ../.. ; \
	cd Behavioral_Elements/Collaborations; $(JAVAC) $(JFLAGS) *.java; cd ../.. ; \
	cd util; $(JAVAC) $(JFLAGS) *.java; cd .. ; \
	cd test/omg; $(JAVAC) $(JFLAGS) *.java; cd ../.. ; \
	cd generate; $(JAVAC) $(JFLAGS) *.java; cd .. ; \
	$(JAVAC) $(JFLAGS) *.java; \
	cd .. ; \
	fi

argoumlFresh:
	@if [ -d uml/ui ] ; \
	then echo "\n\nJAVAC *.java ARGO/UML ================" ; \
	cd argo ; \
	cd kernel; $(JAVAC) $(JFLAGS) *.java; cd .. ; \
	cd checklist; $(JAVAC) $(JFLAGS) *.java; cd .. ; \
	cd .. ; \
	cd uml ; \
	cd ui; $(JAVAC) $(JFLAGS) *.java; cd .. ; \
	cd visual; $(JAVAC) $(JFLAGS) *.java; cd .. ; \
	cd .. ; \
	fi

criticsFresh:
	@if [ -d uml/critics ] ; \
	then echo "\n\nJAVAC *.java UML CRITICS ================" ; \
	cd uml ; \
	cd critics; $(JAVAC) $(JFLAGS) *.java; cd .. ; \
	cd critics/patterns; $(JAVAC) $(JFLAGS) *.java; cd ../.. ; \
	cd .. ; \
	fi

clean: gefClean umlClean argoumlClean criticsClean swingClean


gefClean: 
	@if [ -d gef ] ; \
	then echo "\n\nCLEANING GEF ================" ; \
	cd util; make clean; cd .. ; \
	cd gef; make clean; cd .. ; \
	cd gef/event; make clean; cd ../.. ; \
	cd graph; make clean; cd .. ; \
	cd beans/editors; make clean; cd ../.. ; \
	cd ui; make clean; cd .. ; \
	cd gef/demo; make clean; cd ../.. ; \
	fi

umlClean:
	@if [ -d uml ] ; \
	then echo "\n\nCLEANING UML META-MODEL ================" ; \
	cd uml ;\
	cd Foundation/Core; make clean; cd ../.. ; \
	cd Foundation/Data_Types; make clean; cd ../.. ; \
	cd Foundation/Extension_Mechanisms; make clean; cd ../.. ; \
	cd Model_Management; make clean; cd .. ; \
	cd Behavioral_Elements/Common_Behavior; make clean; cd ../.. ; \
	cd Behavioral_Elements/State_Machines; make clean; cd ../.. ; \
	cd Behavioral_Elements/Use_Cases; make clean; cd ../.. ; \
	cd Behavioral_Elements/Collaborations; make clean; cd ../.. ; \
	cd util; make clean; cd .. ; \
	cd test/omg; make clean; cd ../.. ; \
	cd generate; make clean; cd .. ; \
	make clean ; \
	cd .. ; \
	fi

argoumlClean:
	@if [ -d uml/ui ] ; \
	then echo "\n\nCLEANING ARGO/UML ================" ; \
	cd argo ; \
	cd kernel; make clean; cd .. ; \
	cd checklist; make clean; cd .. ; \
	cd .. ; \
	cd uml ; \
	cd ui; make clean; cd .. ; \
	cd visual; make clean; cd .. ; \
	cd .. ; \
	fi

criticsClean:
	@if [ -d uml/critics ] ; \
	then echo "\n\nCLEANING UML CRITICS ================" ; \
	cd uml ; \
	cd critics; make clean; cd .. ; \
	cd critics/patterns; make clean; cd ../.. ; \
	cd critics/java; make clean; cd ../.. ; \
	cd checklist; make clean; cd ../.. ; \
	cd .. ; \
	fi

swingClean: 
	@if [ -d ../com/sun ] ; \
	then echo "\n\nCLEANING SWING MODIFICATIONS ================" ; \
	cd ../com/sun/java/swing; make clean; \
	cd text; make clean; cd .. ; \
	cd plaf/metal; make clean; cd ../.. ; \
	cd ../../../../uci ; \
	fi

GEFPACKS = \
	uci.gef uci.ui uci.util uci.gef.demo uci.gef.event \
	uci.graph uci.beans.editors


UMLPACKS = \
	uci.uml.Foundation.Core \
	uci.uml.Foundation.Data_Types \
	uci.uml.Foundation.Extension_Mechanisms \
	uci.uml.Model_Management \
	uci.uml.Behavioral_Elements.Common_Behavior \
	uci.uml.Behavioral_Elements.State_Machines \
	uci.uml.Behavioral_Elements.Use_Cases \
	uci.uml.Behavioral_Elements.Collaborations \
	uci.uml.test.omg \
	uci.uml.generate \
	uci.uml.util

ARGOUMLPACKS = \
	uci.uml.ui uci.uml.visual

CRITICPACKS = \
	uci.uml.critics uci.uml.critics.patterns uci.uml.critics.java \
	uci.uml.checklist uci.argo.kernel uci.argo.checklist


gefjavadocs:
	$(JAVADOC) -d gefapi $(GEFPACKS)

umljavadocs:
	$(JAVADOC) -d umlapi $(UMLPACKS)

argoumljavadocs:
	$(JAVADOC) -d umluiapi $(ARGOUMLPACKS)

javadocs:
	$(JAVADOC) -J-mx40m -d api \
	$(GEFPACKS) $(UMLPACKS) $(ARGOUMLPACKS) $(CRITICPACKS)




# gen is not used now that GEF does not use a preprocessor
#gen:
#	if [ -d gef ] ; \
#	then echo "\n\nGENERATING GEF ================" ; \
#	cd util; make gen; cd .. ; \
#	cd gef; make gen; cd .. ; \
#	cd gef/event; make gen; cd ../.. ; \
#	cd graph; make gen; cd .. ; \
#	cd beans/editors; make gen; cd ../.. ; \
#	cd ui; make gen; cd .. \ ;
#	cd gef/demo; make gen; cd ../.. ; \
#	fi ; \
#	if [ -d uml ] ; \
#	then echo "\n\nGENERATING UML META-MODEL ================" ; \
#	cd uml ; \
#	cd Foundation/Core; make gen; cd ../.. ; \
#	cd Foundation/Data_Types; make gen; cd ../.. ; \
#	cd Foundation/Extension_Mechanisms; make gen; cd ../.. ; \
#	cd Model_Management; make gen; cd .. ; \
#	cd Behavioral_Elements/Common_Behavior; make gen; cd ../.. ; \
#	cd Behavioral_Elements/State_Machines; make gen; cd ../.. ; \
#	cd Behavioral_Elements/Use_Cases; make gen; cd ../.. ; \
#	cd Behavioral_Elements/Collaborations; make gen; cd ../.. ; \
#	cd util; make gen; cd .. ; \
#	cd test/omg; make gen; cd ../.. ; \
#	cd generate; make gen; cd .. ; \
#	cd .. ; \
#	fi ; \
#	if [ -d uml/ui ] ; \
#	then echo "\n\nGENERATING ARGO/UML ================" ; \
#	cd uml;\
#	cd ui; make gen; cd .. ; \
#	cd visual; make gen; cd .. ; \
#	cd .. ; \
#	fi ; \
#	if [ -d uml/critics ] ; \
#	then echo "\n\nGENERATING UML CRITICS ================" ; \
#	cd uml ; \
#	cd critics; make gen; cd .. ; \
#	cd critics/patterns; make gen; cd ../.. ; \
#	cd .. ; \
#	fi
