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


# gen is not used now that GEF does not use a preprocessor
gen:
	cd util; make gen; cd ..
	cd gef; make gen; cd ..
	cd gef/event; make gen; cd ../..
	cd graph; make gen; cd ..
	cd beans/editors; make gen; cd ../..
	cd ui; make gen; cd ..
	cd gef/demo; make gen; cd ../..

classes:
	cd util; make classes; cd ..
	cd gef; make classes; cd ..
	cd gef/event; make classes; cd ../..
	cd graph; make classes; cd ..
	cd beans/editors; make classes; cd ../..
	cd ui; make classes; cd ..
	cd gef/demo; make classes; cd ../..


clean:
	cd util; make clean; cd ..
	cd gef; make clean; cd ..
	cd gef/event; make clean; cd ../..
	cd graph; make clean; cd ..
	cd beans/editors; make clean; cd ../..
	cd ui; make clean; cd ..
	cd gef/demo; make clean; cd ../..

javadocs:
	$(JAVADOC) -d api \
	uci.gef uci.ui uci.util uci.gef.demo uci.gef.event \
	uci.graph uci.beans.editors


