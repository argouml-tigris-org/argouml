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

include ../Makefile.config

GEN = 	GEF.java \
	Compat.java  \
	ActionLoad.java  \
	ActionSave.java  \
	ActionPrint.java  

CLASSES = \
	Compat.class \
	GEF.class \
	PackageGraphEdit.class\
	Action.class \
	ActionAdjustGrid.class \
	ActionAdjustGuide.class \
	ActionAlign.class \
	ActionCreateNode.class \
	ActionDelete.class \
	ActionDispose.class \
	ActionEditNode.class \
	ActionGroup.class \
	ActionInsertVertex.class \
	ActionNudge.class \
	ActionNull.class \
	ActionOpenWindow.class \
	ActionPrint.class \
	ActionRemoveVertex.class \
	ActionReorder.class \
	ActionScroll.class \
	ActionSequence.class \
	ActionSelectNext.class \
	ActionSelectAll.class \
	ActionSelectInvert.class \
	ActionSetAttr.class \
	ActionSetMode.class \
	ActionShowURL.class \
	ActionSpawn.class \
	ActionUngroup.class \
	ActionQuit.class \
	ActiveComponent.class \
	ActiveButton.class \
	ActiveChoice.class \
	ActiveList.class \
	ArcPerspective.class \
	ArcPerzRectiline.class \
	ArcPerzLine.class \
	DiagramElement.class \
	Editor.class \
	EditorMenus.class \
	EditorFrame.class \
	ExecuteActionWindow.class \
	Fig.class \
	FigCircle.class \
	FigInk.class \
	FigImage.class \
	FigLine.class \
	FigList.class \
	FigPoly.class \
	FigRect.class \
	FigRRect.class \
	FigText.class \
	ForwardingPanel.class \
	ForwardingFrame.class \
	ForwardingComponent.class \
	GEF.class \
	Geometry.class \
	Globals.class \
	Guide.class \
	GuideGrid.class \
	Handle.class \
	Layer.class \
	LayerComposite.class \
	LayerDiagram.class \
	LayerGrid.class \
	LayerPageBreaks.class \
	LayerPerspective.class \
	LayerPolar.class \
	Mode.class \
	ModeCreate.class \
	ModeCreateArc.class \
	ModeCreateFigCircle.class \
	ModeCreateFigInk.class \
	ModeCreateFigImage.class \
	ModeCreateFigLine.class \
	ModeCreateFigPoly.class \
	ModeCreateFigRect.class \
	ModeCreateFigRRect.class \
	ModeCreateFigText.class \
	ModeExampleKeys.class \
	ModeModify.class \
	ModePlace.class \
	ModeSelect.class \
	ModeStack.class \
	NetArc.class \
	NetList.class \
	NetNode.class \
	NetPort.class \
	NetPrimitive.class \
	Palette.class \
	PaletteAttr.class \
	PaletteCompound.class \
	PaletteDecorator.class \
	PaletteFig.class \
	PaletteSticky.class \
	PaletteTop.class \
	Perspective.class \
	Prefs.class \
	PrefsEditor.class \
	PredDEInRect.class \
	RedrawManager.class \
	Selection.class \
	SelectionBox.class \
	SelectionHandles.class \
	SelectionMultiple.class \
	SelectionSingle.class \
	ActionSave.class \
	ActionLoad.class 

all: $(GEN) $(CLASSES)

gen: $(GEN)

classes: $(CLASSES)


GEF.java: GEF.m4 ../Makefile.config
Compat.java: Compat.m4 ../Makefile.config
ActionLoad.java: ActionLoad.m4 ../Makefile.config
ActionSave.java: ActionSave.m4 ../Makefile.config
ActionPrint.java: ActionPrint.m4 ../Makefile.config


clean:
	$(RM) *.class $(GEN)

