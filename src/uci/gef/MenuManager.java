// Copyright (c) 1995, 1996 Regents of the University of California.
// All rights reserved.
//
// This software was developed by the Arcadia project
// at the University of California, Irvine.
//
// Redistribution and use in source and binary forms are permitted
// provided that the above copyright notice and this paragraph are
// duplicated in all such forms and that any documentation,
// advertising materials, and other materials related to such
// distribution and use acknowledge that the software was developed
// by the University of California, Irvine.  The name of the
// University may not be used to endorse or promote products derived
// from this software without specific prior written permission.
// THIS SOFTWARE IS PROVIDED ``AS IS'' AND WITHOUT ANY EXPRESS OR
// IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
// WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.

// File: MenuManager.java
// Classes: MenuManager
// Original Author: ics125b spring 1996
// $Id$

package uci.gef;

import java.awt.*;

/** Contains the menu items used in Editor and handles menu events. */

public class MenuManager extends Object { //implements GEF {

  ////////////////////////////////////////////////////////////////
  // instance variables

  /* Menu items */
  public MenuBar menuBar; /* Needs-More-Work */
  private Menu fileMenu;
  private MenuItem newItem, openItem, saveItem, printItem, prefsItem,
    closeItem, quitItem ;

  private Menu editMenu;
  private Menu selectMenu;
  private MenuItem selectAllItem, selectNextItem, selectPrevItem,
    selectInvertItem, selectSuchThatItem, deleteItem, disposeItem,
    reshapeItem, resizeItem, rotateItem, copyItem, cutItem, pasteItem,
    editNodeItem, executeActionItem; 

  private Menu viewMenu;
  private MenuItem spawnViewItem, spawnPropertySheetItem, zoomInItem,
    zoomOutItem, zoomNormalItem, adjustGridItem, adjustGuideItem,
    adjustBreaksItem;

  private Menu arrangeMenu;
  private MenuItem groupItem, ungroupItem;

  private Menu alignMenu;
  private MenuItem alignTopsItem, alignBottomsItem, alignLeftsItem,
    alignRightsItem, alignCentersItem, alignHCentersItem,
    alignVCentersItem, alignToGridItem;

  private Menu reorderMenu;
  private MenuItem sendToBackItem, bringToFrontItem, sendBackwardItem,
    bringForwardItem;

  Editor _editor;

  ////////////////////////////////////////////////////////////////
  // constructor

  public MenuManager(Editor ce) {
    menuBar = new MenuBar();
    _editor = ce;
    /* Initialize the file menu */
    menuBar.add(fileMenu = new Menu("File"));
    fileMenu.add(newItem = new MenuItem("New"));
    newItem.disable();
    fileMenu.add(openItem = new MenuItem("Open..."));
    fileMenu.add(saveItem = new MenuItem("Save..."));
    fileMenu.add(printItem = new MenuItem("Print Diagram..."));
    fileMenu.addSeparator();
    fileMenu.add(prefsItem = new MenuItem("Preferences..."));
    fileMenu.addSeparator();
    fileMenu.add(closeItem = new MenuItem("Close Window"));
    fileMenu.add(quitItem = new MenuItem("Quit"));

    /* Initialize the edit Menu */
    menuBar.add(editMenu = new Menu("Edit"));
    editMenu.add(selectMenu = new Menu("Select"));
    selectMenu.add(selectAllItem = new MenuItem("All"));
    selectMenu.add(selectNextItem = new MenuItem("Next"));
    selectMenu.add(selectPrevItem = new MenuItem("Prev"));
    selectMenu.add(selectInvertItem = new MenuItem("Invert Selection"));
    selectMenu.add(selectSuchThatItem = new MenuItem("Such That..."));
    selectSuchThatItem.disable();
    editMenu.add(disposeItem = new MenuItem("Delete"));
    editMenu.addSeparator();
    editMenu.add(reshapeItem = new MenuItem("Reshape"));
    editMenu.add(resizeItem = new MenuItem("Resize"));
    editMenu.add(rotateItem = new MenuItem("Rotate"));
    editMenu.addSeparator();
    editMenu.add(copyItem = new MenuItem("Copy"));
    copyItem.disable();
    editMenu.add(cutItem = new MenuItem("Cut"));
    cutItem.disable();
    editMenu.add(pasteItem = new MenuItem("Paste"));
    pasteItem.disable();
    editMenu.addSeparator();
    editMenu.add(editNodeItem = new MenuItem("Edit Node"));
    editMenu.addSeparator();
    editMenu.add(executeActionItem = new MenuItem("Execute Action..."));

    /* Initialize the view Menu */
    menuBar.add(viewMenu = new Menu("View"));
    viewMenu.add(spawnViewItem = new MenuItem("Spawn New View"));
    viewMenu.add(spawnPropertySheetItem =
		 new MenuItem("Spawn Property Sheet"));
    viewMenu.addSeparator();
    viewMenu.add(zoomInItem = new MenuItem("Zoom In"));
    zoomInItem.disable();
    viewMenu.add(zoomOutItem = new MenuItem("Zoom Out"));
    zoomOutItem.disable();
    viewMenu.add(zoomNormalItem = new MenuItem("Zoom Normal"));
    zoomNormalItem.disable();
    viewMenu.addSeparator();
    viewMenu.add(adjustGridItem = new MenuItem("Adjust Grid Appearance"));
    viewMenu.add(adjustGuideItem = new MenuItem("Adjust Grid Snap"));
    viewMenu.add(adjustBreaksItem = new MenuItem("Toggle Page Breaks"));

    menuBar.add(arrangeMenu = new Menu("Arrange"));
    arrangeMenu.add(groupItem = new MenuItem("Group"));
    arrangeMenu.add(ungroupItem = new MenuItem("Ungroup"));
    arrangeMenu.add(alignMenu = new Menu("Align"));
    alignMenu.add(alignTopsItem = new MenuItem("Tops"));
    alignMenu.add(alignBottomsItem = new MenuItem("Bottoms"));
    alignMenu.add(alignLeftsItem = new MenuItem("Lefts"));
    alignMenu.add(alignRightsItem = new MenuItem("Rights"));
    alignMenu.add(alignCentersItem = new MenuItem("Centers"));
    alignMenu.add(alignHCentersItem = new MenuItem("Horizontal Centers"));
    alignMenu.add(alignVCentersItem = new MenuItem("Vertical Centers"));
    alignMenu.add(alignToGridItem = new MenuItem("To Grid"));
    arrangeMenu.add(reorderMenu = new Menu("Reorder"));
    reorderMenu.add(sendToBackItem = new MenuItem("To Back"));
    reorderMenu.add(bringToFrontItem = new MenuItem("To Front"));
    reorderMenu.add(sendBackwardItem = new MenuItem("Backward"));
    reorderMenu.add(bringForwardItem = new MenuItem("Forward"));

    ce.setMenuBar(menuBar);
  }

  public void add(Menu m) {
    menuBar.add(m);
  }

  ////////////////////////////////////////////////////////////////
  // event handlers

  // needs-more-work: could use a hashtable of MenuItems to Actions
  public boolean handleMenuEvent(Event e) {
    Action act = null;
    Object arg = e.arg;
    if ("Quit".equals(arg)) act = new ActionQuit();
    else if ("Close Window".equals(arg)) _editor.close();
    else if ("Preferences...".equals(arg))
      act = new ActionOpenWindow("uci.gef.PrefsEditor");
    else if ("Open...".equals(arg)) act = new ActionLoad();
    else if ("Save...".equals(arg)) act = new ActionSave();
    else if ("Print Diagram...".equals(arg)) act = new ActionPrint();
    else if ("Edit Node".equals(arg)) act = new ActionEditNode();
    else if ("Execute Action...".equals(arg))
      act = new ActionOpenWindow("uci.gef.ExecuteActionWindow");
    else if ("Spawn New View".equals(arg)) act = new ActionSpawn();
    else if ("Spawn Property Sheet".equals(arg)) {
      act = null;
      Globals.startPropertySheet();
      return true;
    }
    // Needs-More-Work: there is no action Zoom yet
    // Needs-More-Work: there is no action Zoom yet
    // else if (e.target == zoomInItem)
    //   (new ActionZoom(ActionZoom.IN)).doIt(e);
    // else if (e.target == zoomOutItem)
    //   (new ActionZoom(ActionZoom.OUT)).doIt(e);
    // else if (e.target == zoomNormalItem)
    //   (new ActionZoom(ActionZoom.NORMAL)).doIt(e);
    else if ("Adjust Grid Appearance".equals(arg))
      act = new ActionAdjustGrid();
    else if ("Adjust Grid Snap".equals(arg)) act = new ActionAdjustGuide();
    else if ("Toggle Page Breaks".equals(arg))
      act = new ActionAdjustPageBreaks();
    else if ("Group".equals(arg)) act = new ActionGroup();
    else if ("Ungroup".equals(arg)) act = new ActionUngroup();
    else if ("Tops".equals(arg))
      act = new ActionAlign(ActionAlign.ALIGN_TOPS);
    else if ("Bottoms".equals(arg))
      act = new ActionAlign(ActionAlign.ALIGN_BOTTOMS);
    else if ("Lefts".equals(arg))
      act = new ActionAlign(ActionAlign.ALIGN_LEFTS);
    else if ("Rights".equals(arg))
      act = new ActionAlign(ActionAlign.ALIGN_RIGHTS);
    else if ("Centers".equals(arg))
      act = new ActionAlign(ActionAlign.ALIGN_CENTERS);
    else if ("Horizontal Centers".equals(arg))
      act = new ActionAlign(ActionAlign.ALIGN_H_CENTERS);
    else if ("Vertical Centers".equals(arg))
      act = new ActionAlign(ActionAlign.ALIGN_V_CENTERS);
    else if ("To Grid".equals(arg))
      act = new ActionAlign(ActionAlign.ALIGN_TO_GRID);
    //else if ("Delete".equals(arg))
    //      act = new ActionDelete();
    else if ("To Back".equals(arg))
      act = new ActionReorder(ActionReorder.SEND_TO_BACK);
    else if ("To Front".equals(arg))
      act = new ActionReorder(ActionReorder.BRING_TO_FRONT);
    else if ("Backward".equals(arg))
      act = new ActionReorder(ActionReorder.SEND_BACKWARD);
    else if ("Forward".equals(arg))
      act = new ActionReorder(ActionReorder.BRING_FORWARD);
    else if ("All".equals(arg)) act = new ActionSelectAll();
    else if ("Next".equals(arg)) act = new ActionSelectNext(true);
    else if ("Prev".equals(arg)) act = new ActionSelectNext(false);
    else if ("Invert Selection".equals(arg)) act = new ActionSelectInvert();
    //else if ("Such That...".equals(arg)) act = new ActionSelectSuchThat();
    else if ("Delete".equals(arg)) act = new ActionDispose();
    else if ("Reshape".equals(arg)) act = new ActionReshape();
    else if ("Resize".equals(arg)) act = new ActionResize();
    else if ("Rotate".equals(arg)) act = new ActionRotate();
    else return false;

    _editor.executeAction(act, e);
    return true;
  }

} /* end class MenuManager */
