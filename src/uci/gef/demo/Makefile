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

# File: Makefile in uci/graphedit/demo
# Oringinal Author: jrobbins@ics.uci.edu
# $Id$

include ../../Makefile.config

GEN =

CLASSES = \
	BasicApplication.class \
	FlexibleApplet.class \
	EquipmentApplet.class \
	SampleArc.class \
	SampleArc2.class \
	SampleNode.class \
	SampleNode2.class \
	SamplePort.class \
	SamplePort2.class \
	SamplePalette.class \
	EquipmentPalette.class \
	NodeCPU.class \
	NodePrinter.class \
	NodeWall.class \
	PortData.class \
	PortPower.class \
	ArcData.class \
	ArcPower.class



all: $(GEN) $(CLASSES)

gen: $(GEN)

classes: $(CLASSES)

clean:
	$(RM) *.class $(GEN)

