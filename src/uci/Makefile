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
#

REL_DIR=uci
include ../Makefile.config


all: classes archive

classes: acmeDir gefDir umlDir argoumlDir criticsDir


archive:: objdir gefDir umlDir argoumlDir criticsDir 
	@if [ ! -d $(OUT_DIR)/xml/dtd ] ; \
		then $(MKDIR) $(OUT_DIR)/xml/dtd ; \
	fi
	( $(CD) xml/dtd ; \
	$(COPY) *.dtd *.tee $(OUT_DIR)/xml/dtd )
	@if [ ! -d $(OUT_DIR)/Images ] ; \
		then $(MKDIR) $(OUT_DIR)/Images ; \
	fi
	( $(CD) Images ; \
	$(COPY) *.gif $(OUT_DIR)/Images )
	$(COPY) COPYRIGHT $(OUT_DIR)


cleandist: 
	$(MAKE) $(MAKEFLAGS) TARGET=clean cleandirs


cleandirs: acmeDir gefDir umlDir argoumlDir criticsDir


processDir:
	@echo
	@echo "Processing directory $(DIR)"
	@echo
	( $(CD) $(DIR) ; \
	$(MAKE) $(TARGET) )


ACME_DIRS= ../Acme          \
	../Acme/JPM/Encoders 

GEF_DIRS= util           \
	gef              \
	gef/event        \
	graph            \
	beans/editors    \
	ui               \
	gef/demo	 \
	xml 	 	 \
	xml/pgml	 

UML_DIRS= uml/Foundation/Core                   \
	uml/Foundation/Data_Types               \
	uml/Foundation/Extension_Mechanisms     \
	uml/Model_Management                    \
	uml/Behavioral_Elements/Common_Behavior \
	uml/Behavioral_Elements/State_Machines  \
	uml/Behavioral_Elements/Use_Cases       \
	uml/Behavioral_Elements/Collaborations  \
	uml/util                                \
	uml/ocl                                 \
	uml/test/omg                            \
	uml/generate

ARGOUML_DIRS= argo/kernel        \
	argo/checklist           \
	uml/ui                   \
	uml/ui/nav               \
	uml/ui/todo              \
	uml/ui/props             \
	uml/ui/table             \
	uml/ui/style             \
	uml/visual               \
	xml/xmi                  \
	xml/argo                 \
	uml                      

#	uml/ui/colorize       

CRITICS_DIRS= uml/checklist       \
	uml/critics               \
	uml/critics/patterns      \
	uml/critics/java          \
	uml/critics/presentation


#SWING_DIR=../com/sun/java
#SWING_DIRS=$(SWING_DIR)/swing         \
#	$(SWING_DIR)/swing/text       \
#	$(SWING_DIR)/swing/plaf/metal 


acmeDir:
	@echo
	@echo
	@echo "PROCESSING Acme ========================="
	test ! -d ../Acme || for d in $(ACME_DIRS) ; do \
		$(MAKE) TARGET=$(TARGET) DIR=$$d processDir ; done

gefDir:
	@echo
	@echo
	@echo "PROCESSING GEF ========================="
	test ! -d gef || for d in $(GEF_DIRS) ; do \
		$(MAKE) TARGET=$(TARGET) DIR=$$d processDir ; done


umlDir:
	@echo
	@echo
	@echo "PROCESSING UML ========================="
	test ! -d uml || for d in $(UML_DIRS) ; do \
		$(MAKE) TARGET=$(TARGET) DIR=$$d processDir ; done


argoumlDir:
	@echo
	@echo
	@echo "PROCESSING ARGO/UML ===================="
	test ! -d argo || for d in $(ARGOUML_DIRS) ; do \
		$(MAKE) TARGET=$(TARGET) DIR=$$d processDir ; done


criticsDir:
	@echo
	@echo
	@echo "PROCESSING GEF ========================="
	test ! -d uml/critics || for d in $(CRITICS_DIRS) ; do \
		$(MAKE) TARGET=$(TARGET) DIR=$$d processDir ; done


#swingDir:
#	@echo
#	@echo
#	@echo "PROCESSING SWING MODIFICATIONS ========="
#	test ! -d $(SWING_DIR) || for d in $(SWING_DIRS) ; do \
#		$(MAKE) TARGET=$(TARGET) DIR=$$d processDir ; done
