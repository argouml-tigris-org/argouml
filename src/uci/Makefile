REL_DIR=uci
include ../Makefile.config


all: classes archive

classes: gefDir umlDir argoumlDir criticsDir

#swingDir

#swingDir
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


clean: gefDir umlDir argoumlDir criticsDir
# swingDir

processDir:
	@echo
	@echo "Processing directory $(DIR)"
	@echo
	( $(CD) $(DIR) ; \
	$(MAKE) $(TARGET) )


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
