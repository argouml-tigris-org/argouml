# Copyright (c) 1996-99 The Regents of the University of California. All
# Rights Reserved. Permission to use, copy, modify, and distribute this
# software and its documentation without fee, and without a written
# agreement is hereby granted, provided that the above copyright notice
# and this paragraph appear in all copies.  This software program and
# documentation are copyrighted by The Regents of the University of
# California. The software program and documentation are supplied "AS
# IS", without any accompanying services from The Regents. The Regents
# does not warrant that the operation of the program will be
# uninterrupted or error-free. The end-user understands that the program
# was developed for research purposes and is advised not to rely
# exclusively on the program for any reason.  IN NO EVENT SHALL THE
# UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
# SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
# ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
# THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
# SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
# WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
# MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
# PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
# CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
# UPDATES, ENHANCEMENTS, OR MODIFICATIONS.
#
# This is a GNU make compatible makefile for
# Argo/UML.
#
# Author: Scott A. Guyer
# Date: 3/06/1999
#
#################################################
#
# Look at Makefile.config and make sure
# the top portion is defined correctly
# for your site.
#
include Makefile.config



#######################
#  DO NOT EDIT BELOW  #
# ------------------- #
#######################

UCI_DIR=$(ARGO_DIR)/uci

VER_NAME=argouml072

JAR_NAME=argouml072

SRC_NAME=argosrc072


all: classes jar

jar:
	( $(CD) $(UCI_DIR) ; \
	$(MAKE) $(MAKEFLAGS) TARGET=archive archive )
	( $(CD) $(OBJ_DIR) ; \
	$(JAVA_AR) cvf0 $(JAR_NAME).jar uci )

classes:
	( $(CD) $(UCI_DIR) ; \
	$(MAKE) $(MAKEFLAGS) TARGET=classes classes )


cleandist:
	( $(CD) $(UCI_DIR) ; \
	$(MAKE) $(MAKEFLAGS) TARGET=clean clean )


argosrc:
	ln -s . $(VER_NAME) ; \
	zip -9 $(SRC_NAME).zip \
	$(VER_NAME)/*Makefile \
	$(VER_NAME)/*Makefile.config \
	$(VER_NAME)/makeall.bat \
	$(VER_NAME)/Acme/*Makefile \
	$(VER_NAME)/Acme/Sources.list \
	$(VER_NAME)/Acme/*.java \
	$(VER_NAME)/Acme/JPM/Encoders/*.java \
	$(VER_NAME)/Acme/JPM/Encoders/*Makefile \
	$(VER_NAME)/Acme/JPM/Encoders/Sources.list \
	$(VER_NAME)/uci/COPYRIGHT \
	$(VER_NAME)/uci/*Makefile \
	$(VER_NAME)/uci/argouml.mf \
	$(VER_NAME)/uci/Images/*.gif \
	$(VER_NAME)/uci/gef/*.java \
	$(VER_NAME)/uci/gef/*Makefile \
	$(VER_NAME)/uci/gef/Sources.list \
	$(VER_NAME)/uci/gef/demo/*.java \
	$(VER_NAME)/uci/gef/demo/*.html \
	$(VER_NAME)/uci/gef/demo/*Makefile \
	$(VER_NAME)/uci/gef/demo/Sources.list \
	$(VER_NAME)/uci/gef/event/*.java \
	$(VER_NAME)/uci/gef/event/*Makefile \
	$(VER_NAME)/uci/gef/event/Sources.list \
	$(VER_NAME)/uci/ui/*.java \
	$(VER_NAME)/uci/ui/*Makefile \
	$(VER_NAME)/uci/ui/Sources.list \
	$(VER_NAME)/uci/util/*.java \
	$(VER_NAME)/uci/util/*Makefile \
	$(VER_NAME)/uci/util/Sources.list \
	$(VER_NAME)/uci/graph/*.java \
	$(VER_NAME)/uci/graph/*Makefile \
	$(VER_NAME)/uci/graph/Sources.list \
	$(VER_NAME)/uci/beans/editors/*.java \
	$(VER_NAME)/uci/beans/editors/*Makefile \
	$(VER_NAME)/uci/beans/editors/Sources.list \
	$(VER_NAME)/uci/uml/Foundation/Core/*.java \
	$(VER_NAME)/uci/uml/Foundation/Core/*Makefile \
	$(VER_NAME)/uci/uml/Foundation/Core/Sources.list \
	$(VER_NAME)/uci/uml/Foundation/Data_Types/*.java \
	$(VER_NAME)/uci/uml/Foundation/Data_Types/*Makefile \
	$(VER_NAME)/uci/uml/Foundation/Data_Types/Sources.list \
	$(VER_NAME)/uci/uml/Foundation/Extension_Mechanisms/*.java \
	$(VER_NAME)/uci/uml/Foundation/Extension_Mechanisms/*Makefile \
	$(VER_NAME)/uci/uml/Foundation/Extension_Mechanisms/Sources.list \
	$(VER_NAME)/uci/uml/Model_Management/*.java \
	$(VER_NAME)/uci/uml/Model_Management/*Makefile \
	$(VER_NAME)/uci/uml/Model_Management/Sources.list \
	$(VER_NAME)/uci/uml/Behavioral_Elements/Common_Behavior/*.java \
	$(VER_NAME)/uci/uml/Behavioral_Elements/Common_Behavior/*Makefile \
	$(VER_NAME)/uci/uml/Behavioral_Elements/Common_Behavior/Sources.list \
	$(VER_NAME)/uci/uml/Behavioral_Elements/Use_Cases/*.java \
	$(VER_NAME)/uci/uml/Behavioral_Elements/Use_Cases/*Makefile \
	$(VER_NAME)/uci/uml/Behavioral_Elements/Use_Cases/Sources.list \
	$(VER_NAME)/uci/uml/Behavioral_Elements/State_Machines/*.java \
	$(VER_NAME)/uci/uml/Behavioral_Elements/State_Machines/*Makefile \
	$(VER_NAME)/uci/uml/Behavioral_Elements/State_Machines/Sources.list \
	$(VER_NAME)/uci/uml/Behavioral_Elements/Collaborations/*.java \
	$(VER_NAME)/uci/uml/Behavioral_Elements/Collaborations/*Makefile \
	$(VER_NAME)/uci/uml/Behavioral_Elements/Collaborations/Sources.list \
	$(VER_NAME)/uci/uml/templates/*.argo \
	$(VER_NAME)/uci/uml/templates/*.pgml \
	$(VER_NAME)/uci/uml/templates/*.xmi \
	$(VER_NAME)/uci/uml/templates/*.txt \
	$(VER_NAME)/uci/uml/templates/*.html \
	$(VER_NAME)/uci/uml/examples/*.argo \
	$(VER_NAME)/uci/uml/examples/*.pgml \
	$(VER_NAME)/uci/uml/examples/*.xmi \
	$(VER_NAME)/uci/uml/examples/*.txt \
	$(VER_NAME)/uci/uml/examples/*.html \
	$(VER_NAME)/uci/uml/help/*.html \
	$(VER_NAME)/uci/uml/help/*.txt \
	$(VER_NAME)/uci/uml/util/*.java \
	$(VER_NAME)/uci/uml/util/*Makefile \
	$(VER_NAME)/uci/uml/util/Sources.list \
	$(VER_NAME)/uci/uml/ocl/*.java \
	$(VER_NAME)/uci/uml/ocl/*Makefile \
	$(VER_NAME)/uci/uml/ocl/Sources.list \
	$(VER_NAME)/uci/uml/test/omg/*.java \
	$(VER_NAME)/uci/uml/test/omg/*Makefile \
	$(VER_NAME)/uci/uml/test/omg/Sources.list \
	$(VER_NAME)/uci/uml/generate/*.java \
	$(VER_NAME)/uci/uml/generate/*Makefile \
	$(VER_NAME)/uci/uml/generate/Sources.list \
	$(VER_NAME)/uci/uml/critics/*.java \
	$(VER_NAME)/uci/uml/critics/*Makefile \
	$(VER_NAME)/uci/uml/critics/Sources.list \
	$(VER_NAME)/uci/uml/critics/java/*.java \
	$(VER_NAME)/uci/uml/critics/java/*Makefile \
	$(VER_NAME)/uci/uml/critics/java/Sources.list \
	$(VER_NAME)/uci/uml/critics/patterns/*.java \
	$(VER_NAME)/uci/uml/critics/patterns/*Makefile \
	$(VER_NAME)/uci/uml/critics/patterns/Sources.list \
	$(VER_NAME)/uci/uml/critics/presentation/*.java \
	$(VER_NAME)/uci/uml/critics/presentation/*Makefile \
	$(VER_NAME)/uci/uml/critics/presentation/Sources.list \
	$(VER_NAME)/uci/uml/checklist/*.java \
	$(VER_NAME)/uci/uml/checklist/*Makefile \
	$(VER_NAME)/uci/uml/checklist/Sources.list \
	$(VER_NAME)/uci/uml/ui/*.java \
	$(VER_NAME)/uci/uml/ui/*.ini \
	$(VER_NAME)/uci/uml/ui/*Makefile \
	$(VER_NAME)/uci/uml/ui/Sources.list \
	$(VER_NAME)/uci/uml/ui/nav/*.java \
	$(VER_NAME)/uci/uml/ui/nav/*Makefile \
	$(VER_NAME)/uci/uml/ui/nav/Sources.list \
	$(VER_NAME)/uci/uml/ui/todo/*.java \
	$(VER_NAME)/uci/uml/ui/todo/*Makefile \
	$(VER_NAME)/uci/uml/ui/todo/Sources.list \
	$(VER_NAME)/uci/uml/ui/table/*.java \
	$(VER_NAME)/uci/uml/ui/table/*Makefile \
	$(VER_NAME)/uci/uml/ui/table/Sources.list \
	$(VER_NAME)/uci/uml/ui/props/*.java \
	$(VER_NAME)/uci/uml/ui/props/*Makefile \
	$(VER_NAME)/uci/uml/ui/props/Sources.list \
	$(VER_NAME)/uci/uml/ui/style/*.java \
	$(VER_NAME)/uci/uml/ui/style/*Makefile \
	$(VER_NAME)/uci/uml/ui/style/Sources.list \
	$(VER_NAME)/uci/uml/visual/*.java \
	$(VER_NAME)/uci/uml/visual/*Makefile \
	$(VER_NAME)/uci/uml/visual/Sources.list \
	$(VER_NAME)/uci/uml/*.java \
	$(VER_NAME)/uci/uml/*Makefile \
	$(VER_NAME)/uci/uml/Sources.list \
	$(VER_NAME)/uci/xml/*Makefile \
	$(VER_NAME)/uci/xml/Sources.list \
	$(VER_NAME)/uci/xml/*.java \
	$(VER_NAME)/uci/xml/argo/*Makefile \
	$(VER_NAME)/uci/xml/argo/Sources.list \
	$(VER_NAME)/uci/xml/argo/*.java \
	$(VER_NAME)/uci/xml/pgml/*Makefile \
	$(VER_NAME)/uci/xml/pgml/Sources.list \
	$(VER_NAME)/uci/xml/pgml/*.java \
	$(VER_NAME)/uci/xml/xmi/*Makefile \
	$(VER_NAME)/uci/xml/xmi/Sources.list \
	$(VER_NAME)/uci/xml/xmi/*.java \
	$(VER_NAME)/uci/xml/dtd/*.tee \
	$(VER_NAME)/uci/xml/dtd/*.dtd \
	$(VER_NAME)/uci/argo/kernel/*.java \
	$(VER_NAME)/uci/argo/kernel/*Makefile \
	$(VER_NAME)/uci/argo/kernel/Sources.list \
	$(VER_NAME)/uci/argo/checklist/*.java \
	$(VER_NAME)/uci/argo/checklist/*Makefile \
	$(VER_NAME)/uci/argo/checklist/Sources.list ; \
	$(DELETE) $(VER_NAME)


argouml:
	$(JAVA_AR) -J-mx40m cmvf0 uci/argouml.mf $(JAR_NAME).jar \
	uci/COPYRIGHT  \
	uci/Images/*.gif \
	Acme/*.class \
	Acme/JPM/Encoders/*.class \
	uci/gef/*.class  \
	uci/gef/demo/*.class \
	uci/gef/event/*.class \
	uci/ui/*.class \
	uci/util/*.class \
	uci/graph/*.class \
	uci/beans/editors/*.class \
	uci/uml/Foundation/Core/*.class \
	uci/uml/Foundation/Data_Types/*.class \
	uci/uml/Foundation/Extension_Mechanisms/*.class \
	uci/uml/Model_Management/*.class \
	uci/uml/Behavioral_Elements/Common_Behavior/*.class \
	uci/uml/Behavioral_Elements/Use_Cases/*.class \
	uci/uml/Behavioral_Elements/State_Machines/*.class \
	uci/uml/Behavioral_Elements/Collaborations/*.class \
	uci/uml/templates/*.argo \
	uci/uml/templates/*.pgml \
	uci/uml/templates/*.xmi \
	uci/uml/templates/*.txt \
	uci/uml/templates/*.html \
	uci/uml/help/*.html \
	uci/uml/help/*.txt \
	uci/uml/util/*.class \
	uci/uml/test/omg/*.class \
	uci/uml/generate/*.class \
	uci/uml/critics/*.class \
	uci/uml/critics/patterns/*.class \
	uci/uml/critics/java/*.class \
	uci/uml/critics/presentation/*.class \
	uci/uml/checklist/*.class \
	uci/uml/ui/*.class \
	uci/uml/ui/*.ini \
	uci/uml/ui/table/*.class \
	uci/uml/ui/nav/*.class \
	uci/uml/ui/todo/*.class \
	uci/uml/ui/style/*.class \
	uci/uml/ui/props/*.class \
	uci/uml/visual/*.class \
	uci/uml/ocl/*.class \
	uci/uml/*.class \
	uci/xml/*.class \
	uci/xml/argo/*.class \
	uci/xml/pgml/*.class \
	uci/xml/xmi/*.class \
	uci/xml/dtd/*.tee \
	uci/xml/dtd/*.dtd \
	uci/argo/checklist/*.class \
	uci/argo/kernel/*.class 

enrollZip:
	cd uci/uml/examples ; \
	zip -9 	../../../enroll.zip \
		enroll.argo \
		enroll.xmi \
		enroll_mainclassdiagram.pgml \
		enroll_LogInstates.pgml \
		enroll_telephoneusecases.pgml \
		enroll_Gradstates.pgml  ; \
	cd ../../..


tinygefZip:
	cd uci/uml/examples ; \
	zip -9 	../../../tinygef.zip \
		tinygef.argo \
		tinygef.xmi \
		gefclasses.pgml ; \
	cd ../../..


argosrc1:
	ln -s . $(VER_NAME) ; \
	zip -9 $(SRC_NAME)_part1.zip \
	$(VER_NAME)/*Makefile \
	$(VER_NAME)/*Makefile.config \
	$(VER_NAME)/makeall.bat \
	$(VER_NAME)/Acme/*Makefile \
	$(VER_NAME)/Acme/Sources.list \
	$(VER_NAME)/Acme/*.java \
	$(VER_NAME)/Acme/JPM/Encoders/*.java \
	$(VER_NAME)/Acme/JPM/Encoders/*Makefile \
	$(VER_NAME)/Acme/JPM/Encoders/Sources.list \
	$(VER_NAME)/uci/COPYRIGHT \
	$(VER_NAME)/uci/*Makefile \
	$(VER_NAME)/uci/argouml.mf \
	$(VER_NAME)/uci/Images/*.gif \
	$(VER_NAME)/uci/gef/*.java \
	$(VER_NAME)/uci/gef/*Makefile \
	$(VER_NAME)/uci/gef/Sources.list \
	$(VER_NAME)/uci/gef/demo/*.java \
	$(VER_NAME)/uci/gef/demo/*.html \
	$(VER_NAME)/uci/gef/demo/*Makefile \
	$(VER_NAME)/uci/gef/demo/Sources.list \
	$(VER_NAME)/uci/gef/event/*.java \
	$(VER_NAME)/uci/gef/event/*Makefile \
	$(VER_NAME)/uci/gef/event/Sources.list ; \
	$(DELETE) $(VER_NAME)


argosrc2:
	ln -s . $(VER_NAME) ; \
	zip -9 $(SRC_NAME)_part2.zip \
	$(VER_NAME)/uci/ui/*.java \
	$(VER_NAME)/uci/ui/*Makefile \
	$(VER_NAME)/uci/ui/Sources.list \
	$(VER_NAME)/uci/util/*.java \
	$(VER_NAME)/uci/util/*Makefile \
	$(VER_NAME)/uci/util/Sources.list \
	$(VER_NAME)/uci/graph/*.java \
	$(VER_NAME)/uci/graph/*Makefile \
	$(VER_NAME)/uci/graph/Sources.list \
	$(VER_NAME)/uci/beans/editors/*.java \
	$(VER_NAME)/uci/beans/editors/*Makefile \
	$(VER_NAME)/uci/beans/editors/Sources.list \
	$(VER_NAME)/uci/uml/Foundation/Core/*.java \
	$(VER_NAME)/uci/uml/Foundation/Core/*Makefile \
	$(VER_NAME)/uci/uml/Foundation/Core/Sources.list \
	$(VER_NAME)/uci/uml/Foundation/Data_Types/*.java \
	$(VER_NAME)/uci/uml/Foundation/Data_Types/*Makefile \
	$(VER_NAME)/uci/uml/Foundation/Data_Types/Sources.list \
	$(VER_NAME)/uci/uml/Foundation/Extension_Mechanisms/*.java \
	$(VER_NAME)/uci/uml/Foundation/Extension_Mechanisms/*Makefile \
	$(VER_NAME)/uci/uml/Foundation/Extension_Mechanisms/Sources.list \
	$(VER_NAME)/uci/uml/Model_Management/*.java \
	$(VER_NAME)/uci/uml/Model_Management/*Makefile \
	$(VER_NAME)/uci/uml/Model_Management/Sources.list \
	$(VER_NAME)/uci/uml/Behavioral_Elements/Common_Behavior/*.java \
	$(VER_NAME)/uci/uml/Behavioral_Elements/Common_Behavior/*Makefile \
	$(VER_NAME)/uci/uml/Behavioral_Elements/Common_Behavior/Sources.list \
	$(VER_NAME)/uci/uml/Behavioral_Elements/Use_Cases/*.java \
	$(VER_NAME)/uci/uml/Behavioral_Elements/Use_Cases/*Makefile \
	$(VER_NAME)/uci/uml/Behavioral_Elements/Use_Cases/Sources.list \
	$(VER_NAME)/uci/uml/Behavioral_Elements/State_Machines/*.java \
	$(VER_NAME)/uci/uml/Behavioral_Elements/State_Machines/*Makefile \
	$(VER_NAME)/uci/uml/Behavioral_Elements/State_Machines/Sources.list \
	$(VER_NAME)/uci/uml/Behavioral_Elements/Collaborations/*.java \
	$(VER_NAME)/uci/uml/Behavioral_Elements/Collaborations/*Makefile \
	$(VER_NAME)/uci/uml/Behavioral_Elements/Collaborations/Sources.list \
	$(VER_NAME)/uci/uml/templates/*.argo \
	$(VER_NAME)/uci/uml/templates/*.pgml \
	$(VER_NAME)/uci/uml/templates/*.xmi \
	$(VER_NAME)/uci/uml/templates/*.txt \
	$(VER_NAME)/uci/uml/templates/*.html \
	$(VER_NAME)/uci/uml/examples/*.argo \
	$(VER_NAME)/uci/uml/examples/*.pgml \
	$(VER_NAME)/uci/uml/examples/*.xmi \
	$(VER_NAME)/uci/uml/examples/*.txt \
	$(VER_NAME)/uci/uml/examples/*.html \
	$(VER_NAME)/uci/uml/help/*.html \
	$(VER_NAME)/uci/uml/help/*.txt \
	$(VER_NAME)/uci/uml/util/*.java \
	$(VER_NAME)/uci/uml/util/*Makefile \
	$(VER_NAME)/uci/uml/util/Sources.list \
	$(VER_NAME)/uci/uml/ocl/*.java \
	$(VER_NAME)/uci/uml/ocl/*Makefile \
	$(VER_NAME)/uci/uml/ocl/Sources.list \
	$(VER_NAME)/uci/uml/test/omg/*.java \
	$(VER_NAME)/uci/uml/test/omg/*Makefile \
	$(VER_NAME)/uci/uml/test/omg/Sources.list \
	$(VER_NAME)/uci/uml/generate/*.java \
	$(VER_NAME)/uci/uml/generate/*Makefile \
	$(VER_NAME)/uci/uml/generate/Sources.list ; \
	$(DELETE) $(VER_NAME)


argosrc3:
	ln -s . $(VER_NAME) ; \
	zip -9 $(SRC_NAME)_part3.zip \
	$(VER_NAME)/uci/uml/critics/*.java \
	$(VER_NAME)/uci/uml/critics/*Makefile \
	$(VER_NAME)/uci/uml/critics/Sources.list \
	$(VER_NAME)/uci/uml/critics/java/*.java \
	$(VER_NAME)/uci/uml/critics/java/*Makefile \
	$(VER_NAME)/uci/uml/critics/java/Sources.list \
	$(VER_NAME)/uci/uml/critics/patterns/*.java \
	$(VER_NAME)/uci/uml/critics/patterns/*Makefile \
	$(VER_NAME)/uci/uml/critics/patterns/Sources.list \
	$(VER_NAME)/uci/uml/critics/presentation/*.java \
	$(VER_NAME)/uci/uml/critics/presentation/*Makefile \
	$(VER_NAME)/uci/uml/critics/presentation/Sources.list \
	$(VER_NAME)/uci/uml/checklist/*.java \
	$(VER_NAME)/uci/uml/checklist/*Makefile \
	$(VER_NAME)/uci/uml/checklist/Sources.list \
	$(VER_NAME)/uci/uml/ui/*.java \
	$(VER_NAME)/uci/uml/ui/*.ini \
	$(VER_NAME)/uci/uml/ui/*Makefile \
	$(VER_NAME)/uci/uml/ui/Sources.list \
	$(VER_NAME)/uci/uml/ui/nav/*.java \
	$(VER_NAME)/uci/uml/ui/nav/*Makefile \
	$(VER_NAME)/uci/uml/ui/nav/Sources.list ; \
	$(DELETE) $(VER_NAME)


argosrc4:
	ln -s . $(VER_NAME) ; \
	zip -9 $(SRC_NAME)_part4.zip \
	$(VER_NAME)/uci/uml/ui/todo/*.java \
	$(VER_NAME)/uci/uml/ui/todo/*Makefile \
	$(VER_NAME)/uci/uml/ui/todo/Sources.list \
	$(VER_NAME)/uci/uml/ui/table/*.java \
	$(VER_NAME)/uci/uml/ui/table/*Makefile \
	$(VER_NAME)/uci/uml/ui/table/Sources.list \
	$(VER_NAME)/uci/uml/ui/props/*.java \
	$(VER_NAME)/uci/uml/ui/props/*Makefile \
	$(VER_NAME)/uci/uml/ui/props/Sources.list \
	$(VER_NAME)/uci/uml/ui/style/*.java \
	$(VER_NAME)/uci/uml/ui/style/*Makefile \
	$(VER_NAME)/uci/uml/ui/style/Sources.list \
	$(VER_NAME)/uci/uml/visual/*.java \
	$(VER_NAME)/uci/uml/visual/*Makefile \
	$(VER_NAME)/uci/uml/visual/Sources.list \
	$(VER_NAME)/uci/uml/*.java \
	$(VER_NAME)/uci/uml/*Makefile \
	$(VER_NAME)/uci/uml/Sources.list \
	$(VER_NAME)/uci/xml/*Makefile \
	$(VER_NAME)/uci/xml/Sources.list \
	$(VER_NAME)/uci/xml/*.java \
	$(VER_NAME)/uci/xml/argo/*Makefile \
	$(VER_NAME)/uci/xml/argo/Sources.list \
	$(VER_NAME)/uci/xml/argo/*.java \
	$(VER_NAME)/uci/xml/pgml/*Makefile \
	$(VER_NAME)/uci/xml/pgml/Sources.list \
	$(VER_NAME)/uci/xml/pgml/*.java \
	$(VER_NAME)/uci/xml/xmi/*Makefile \
	$(VER_NAME)/uci/xml/xmi/Sources.list \
	$(VER_NAME)/uci/xml/xmi/*.java \
	$(VER_NAME)/uci/xml/dtd/*.tee \
	$(VER_NAME)/uci/xml/dtd/*.dtd \
	$(VER_NAME)/uci/argo/kernel/*.java \
	$(VER_NAME)/uci/argo/kernel/*Makefile \
	$(VER_NAME)/uci/argo/kernel/Sources.list \
	$(VER_NAME)/uci/argo/checklist/*.java \
	$(VER_NAME)/uci/argo/checklist/*Makefile \
	$(VER_NAME)/uci/argo/checklist/Sources.list ; \
	$(DELETE) $(VER_NAME)


javadocs:
	$(JAVADOC) -J-mx40m -d api -splitindex -use \
        -link http://java.sun.com/products/jdk/1.2/docs/api \
	-group "GEF (Graph Editing Framework)" "uci.gef*:uci.ui:uci.graph:uci.beans*:uci.util" \
	-group "UML Meta-Model" "uci.uml.F*:uci.uml.M*:uci.uml.B*:uci.uml.util:uci.uml.test*" \
	-group "Argo Cognitive Support Library" "uci.argo*" \
	-group "Argo/UML Knowledge Support" "uci.uml.critics*:uci.uml.checklist" \
	-group "Argo/UML User Interface" "uci.uml:uci.uml.ui*:uci.uml.visual" \
	-group "Argo/UML Code Generation" "uci.uml.generate:uci.uml.ocl" \
	-group "Argo/UML XML Related" "uci.xml*" \
	uci.gef  \
	uci.gef.demo \
	uci.gef.event \
	uci.ui \
	uci.util \
	uci.graph \
	uci.beans.editors \
	uci.uml.Foundation.Core \
	uci.uml.Foundation.Data_Types \
	uci.uml.Foundation.Extension_Mechanisms \
	uci.uml.Model_Management \
	uci.uml.Behavioral_Elements.Common_Behavior \
	uci.uml.Behavioral_Elements.Use_Cases \
	uci.uml.Behavioral_Elements.State_Machines \
	uci.uml.Behavioral_Elements.Collaborations \
	uci.uml.util \
	uci.uml.test.omg \
	uci.uml.generate \
	uci.uml.critics \
	uci.uml.critics.patterns \
	uci.uml.critics.java \
	uci.uml.critics.presentation \
	uci.uml.checklist \
	uci.uml.ui \
	uci.uml.ui.table \
	uci.uml.ui.nav \
	uci.uml.ui.todo \
	uci.uml.ui.style \
	uci.uml.ui.props \
	uci.uml.visual \
	uci.uml.ocl \
	uci.uml \
	uci.xml \
	uci.xml.argo \
	uci.xml.pgml \
	uci.xml.xmi \
	uci.argo.checklist \
	uci.argo.kernel 
