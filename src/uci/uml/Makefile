#
# Default UNIX Makefile for Argo/UML
#
# Must specify the relative dir of this
# Makefile to $(TOP)
#
REL_DIR=uci/uml
include Sources.list
include ../../Makefile.config


archive: objdir
	$(COPY) *.class $(OUT_DIR)
	@if [ ! -d $(OUT_DIR)/templates ] ; \
	then $(MKDIR) $(OUT_DIR)/templates ; \
	fi
	( $(CD) templates ; \
	$(COPY) *.xmi *.pgml *.argo $(OUT_DIR)/templates )
