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
// THIS SOFTWARE IS PROVIDED `AS IS' AND WITHOUT ANY EXPRESS OR
// IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
// WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.

// File: TabPropFrame.java
// Interfaces: TabPropFrame
// Original Author: jrobbins@ics.uci.edu
// $Id$



package uci.ui;

import java.awt.*;
import java.util.*;
import symantec.itools.awt.TabPanel;

/** A window that displays a tabbed Property Sheet user interface with
 *  one tab for each PropSheet instance that is added to this object.
 *  This class can operate using the tab widget that comes with Symantec
 *  VisualCafe (TM), or it can use a java.awt.Choice to select which
 *  PropSheet should be shown. Needs-More-Work: This part of GEF overlaps the
 *  JavaBeans(TM) spec to some degree and might be changed or replaced to
 *  take advantage of beans. Needs-More-Work: I would like to handle
 *  multple selections by showing values that all selected objects have in
 *  common and applying changes to all selected objects.
 *  <A HREF="../features.html#property_sheet">
 *  <TT>FEATURE: property_sheet</TT></A>
 */

public class TabPropFrame extends Frame {
  ////////////////////////////////////////////////////////////////
  // instance variables

  private PropSheet _lastPropSheet = null;
  private int _lastTab = -1;

  /** The collection of PropSheets shown in this frame. */
  private Vector _sheets = new Vector();

  //{{DECLARE_CONTROLS
  private symantec.itools.awt.TabPanel tabPanel;
  //private Panel tabPanel;
  private Panel choicePanel;
  private PropSheetCategory PropSheetCategory1;
  private PropSheetCategory PropSheetCategory2;
  private PropSheetCategory PropSheetCategory3;
  private PropSheetCategory PropSheetCategory4;
  private PropSheetCategory PropSheetCategory5;
  private java.awt.Panel buttonPanel;
  /** <A HREF="../features.html#property_sheet_auto_apply">
   *  <TT>FEATURE: property_sheet_auto_apply</TT></A>
   */
  private java.awt.Checkbox autoApplyCheckbox;
  private java.awt.Button applyButton;
  /** <A HREF="../features.html#property_sheet_revert">
   *  <TT>FEATURE: property_sheet_revert</TT></A>
   */
  private java.awt.Button revertButton;
  private java.awt.Button closeButton;
  // // private java.awt.Choice universeChoice;
  //}}

  //{{DECLARE_MENUS
  //}}

  //  // private Vector _universe = new Vector();
  protected Object _selection = null;

  ////////////////////////////////////////////////////////////////
  // constructors

  public TabPropFrame() {
    //{{INIT_CONTROLS
    setLayout(new BorderLayout(0,0));
    addNotify();
    resize(insets().left + insets().right + 300,insets().top + insets().bottom + 406);
    setFont(new Font("Dialog", Font.PLAIN, 10));
    setBackground(new Color(12632256));
    tabPanel = new TabPanel();
    tabPanel.setLayout(null);
    //tabPanel.reshape(insets().left + 0,
    //                  insets().top + 24,300,351);
    tabPanel.setBackground(new Color(12632256));
    add("Center", tabPanel);
    //tabPanel = new Panel();
    //tabPanel.setLayout(new CardLayout());
    //tabPanel.reshape(insets().left + 0,
    //                  insets().top + 24,300,351);
    tabPanel.setBackground(new Color(12632256));
    add("Center", tabPanel);
    PropSheetCategory1 = new PropSheetCategory(this);
    PropSheetCategory1.reshape(12,33,276,307);
    PropSheetCategory2 = new PropSheetCategory(this);
    PropSheetCategory2.reshape(12,33,276,307);
    PropSheetCategory3 = new PropSheetCategory(this);
    PropSheetCategory3.reshape(12,33,276,307);
    PropSheetCategory4 = new PropSheetCategory(this);
    PropSheetCategory4.reshape(12,33,276,307);
    PropSheetCategory5 = new PropSheetCategory(this);
    PropSheetCategory5.reshape(12,33,276,307);
    buttonPanel = new java.awt.Panel();
    buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER,5,5));
    buttonPanel.reshape(insets().left + 0,insets().top + 375,300,31);
    add("South", buttonPanel);
    autoApplyCheckbox = new java.awt.Checkbox("AutoApply");
    autoApplyCheckbox.reshape(66,5,78,21);
    autoApplyCheckbox.setState(true);
    buttonPanel.add(autoApplyCheckbox);
    applyButton = new java.awt.Button("Apply");
    applyButton.reshape(149,5,40,21);
    applyButton.enable(false);
    buttonPanel.add(applyButton);
    revertButton = new java.awt.Button("Revert");
    revertButton.reshape(149,5,40,21);
    revertButton.enable(false);
    buttonPanel.add(revertButton);
    closeButton = new java.awt.Button("Close");
    closeButton.reshape(194,5,40,21);
    buttonPanel.add(closeButton);
    // //universeChoice = new java.awt.Choice();
    // // universeChoice.reshape(insets().left + 0,insets().top + 0,300,21);
    //sheetChoice = new java.awt.Choice();
    //sheetChoice.reshape(insets().left + 0,
    //                    insets().top + 0,300,21);
    // //choicePanel = new Panel();
    // //choicePanel.setLayout(new GridLayout(1, 1, 5, 5));
    //choicePanel.setLayout(new GridLayout(2, 1, 5, 5));
    // // choicePanel.add(universeChoice);
    //choicePanel.add(sheetChoice);
    //add("North", universeChoice);
    // //add("North", choicePanel);
    setTitle("Props");
    //}}
    // //universeChoice.addItem("no objects");

    //{{INIT_MENUS
    //}}
    PropSheetCategory1.setCategory("Geometry");
    PropSheetCategory2.setCategory("Text");
    PropSheetCategory3.setCategory("Style");
    PropSheetCategory4.setCategory("Model");
    PropSheetCategory5.setCategory("All");
    addPropSheet(PropSheetCategory4);
    addPropSheet(PropSheetCategory1);
    addPropSheet(PropSheetCategory2);
    addPropSheet(PropSheetCategory3);
    addPropSheet(PropSheetCategory5);
    tabPanel.showTabPanel(0);
    //((CardLayout)tabPanel.getLayout()).first(tabPanel);
  }

  public TabPropFrame(String title) {
    this();
    setTitle(title);
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  /** Reply the PropSheet that is currently shown. */
  public Component getCurrentSheet() {
    Component sheet = null;
    //int sheetIndex = sheetChoice.getSelectedIndex();
    int sheetIndex = tabPanel.getCurrentPanelNdx();
    if (sheetIndex >= 0 && sheetIndex < _sheets.size()) {
      //sheet = (Component) _sheets.elementAt(sheetIndex);
      sheet = tabPanel.getTabPanel(sheetIndex);
    }
    return sheet;
  }

  /** Add a new PropSheet to this window under the name it supplies. */
  public void addPropSheet(PropSheet ps) {
    if (tabPanel == null) return;
    tabPanel.addTabPanel(ps.getTabName(), ps.canEdit(_selection), ps);
    String curSheetName = ps.getTabName();
    //tabPanel.add(curSheetName, ps);
    //sheetChoice.addItem(curSheetName);
    _sheets.addElement(ps);
    // Needs-More-Work: no way to disable a choice item in jdk1.0.2
  }

  ////////////////////////////////////////////////////////////////
  // window operations

  public synchronized void show() {
    move(50, 50);
    super.show();
  }

  ////////////////////////////////////////////////////////////////
  // selections and the universe list

// //   public void setUniverse(Vector items) {
// //     _universe = items;
// //     updateUniverseChoice();
// //   }

// //   public void addToUniverse(Object item) {
// //     _universe.addElement(item);
// //     updateUniverseChoice();
// //   }

// //   public void removeFromUniverse(Object item) {
// //     _universe.removeElement(item);
// //     updateUniverseChoice();
// //   }

// //   public void select(int universeIndex) {
// //     if (_universe != null && universeIndex >=0 &&
// // 	universeIndex <= _universe.size())
// //       select((IProps)_universe.elementAt(universeIndex));
// //   }

  public void select(Object item) {
    if (item != _selection) {
      _selection = item;
      // //updateUniverseSelection();
      updateTabs();
      updateCurSheet();
      setTitle("Properties - " +
	       ((item == null) ? "(nothing)" : item.toString()));
      //needs-more-work: bean display name
    }
  }

  ////////////////////////////////////////////////////////////////
  // event handlers

  public boolean handleEvent(Event event) {
    if (event.id == Event.WINDOW_DESTROY) {
      hide();         // hide the Frame
      return true;
    }
    if (event.target == closeButton && event.id == Event.ACTION_EVENT) {
      closeButton_Clicked(event);
      return true;
    }
    if (event.target == applyButton && event.id == Event.ACTION_EVENT) {
      applyButton_Clicked(event);
      return true;
    }
    if (event.target == revertButton && event.id == Event.ACTION_EVENT) {
      revertButton_Clicked(event);
      return true;
    }
    if (event.target == autoApplyCheckbox && event.id == Event.ACTION_EVENT) {
      autoApplyCheckbox_Action(event);
      return true;
    }
// // if (event.target == universeChoice && event.id == Event.ACTION_EVENT) {
// //       universeChoice_Action(event);
// //       return true;
// //     }
    if (event.target == tabPanel && event.id == Event.ACTION_EVENT) {
      tabPanel_Action(event);
      return true;
    }
  //    if (event.target == sheetChoice && event.id == Event.ACTION_EVENT) {
  //      sheetChoice_Action(event);
  //      return true;
  //    }
    return super.handleEvent(event);
  }

  /** When the user switches tabs, update the newly shown PropSheet.
   *  <A HREF="../bugs.html#switch_tabs_without_apply">
   *  <FONT COLOR=660000><B>BUG: switch_tabs_without_apply</B></FONT></A>
   */
  void tabPanel_Action(Event event) { updateCurSheet(); }

// //   /** If the user selects an object from the universe list, start editing
// //    *  that object's properties. */
// //   void universeChoice_Action(Event event) {
// //     select(universeChoice.getSelectedIndex());
// //   }

  //  /** If the user choose an item from the "tabs" pop up menu, show the appropriate PropSheet and make it edit the current object. */
  //  void sheetChoice_Action(Event event) {
  //    String curSheetName = sheetChoice.getSelectedItem();
  //    ((CardLayout)tabPanel.getLayout()).show(tabPanel, curSheetName);
  //    updateCurSheet();
  //  }

  protected void autoApplyCheckbox_Action(Event event) {
    //{{CONNECTION
    // Enable the Button on condition... Is Checkbox Off?
    applyButton.enable(!autoApplyCheckbox.getState());
    revertButton.enable(!autoApplyCheckbox.getState());
    //}}
    Component p = getCurrentSheet();
    if (p instanceof PropSheet)//?
      ((PropSheet)p).setAutoApply(autoApplyCheckbox.getState());
  }

  void closeButton_Clicked(Event event) {
    //{{CONNECTION
    // Hide the Frame
    hide();
    //}}
    dispose();
  }

  /** Ask the currently shown PropSheet to apply any outstanding changes.
   *  <A HREF="../bugs.html#switch_tabs_without_apply">BUG:</A>
   *  When auto-apply is off, if I make changes and then switch tabs
   *  and then press Apply, nothing happens.
   * */
  void applyButton_Clicked(Event event) {
    Component sheet = getCurrentSheet();
    if (sheet instanceof PropSheet) ((PropSheet)sheet).apply();
  }

  /** Ask the currently shown PropSheet to apply any outstanding changes. */
  void revertButton_Clicked(Event event) {
    Component sheet = getCurrentSheet();
    if (sheet instanceof PropSheet) ((PropSheet)sheet).revert();
  }

  ////////////////////////////////////////////////////////////////
  // update methods

// //   public void updateUniverseChoice() {
// //     choicePanel.remove(universeChoice);
// //     //choicePanel.remove(sheetChoice);
// //     universeChoice = new Choice();
// //     if (_universe != null) {
// //       Enumeration items = _universe.elements();
// //       while (items.hasMoreElements()) {
// // 	Object item = items.nextElement();
// // 	String str = item.toString();
// // 	universeChoice.addItem(str);
// //       }
// //     }
// //     choicePanel.add(universeChoice);
// //   //    choicePanel.add(sheetChoice);
// //     updateUniverseSelection();
// //     repaint();
// //   }

// //   // Needs-More-Work: multiple selections
// //   public void updateUniverseSelection() {
// //     if (_universe == null || _selection == null) return;
// //     if (_universe.contains(_selection))
// //       universeChoice.select(_universe.indexOf(_selection));
// //   }

  public void updateTabs() {
    int firstEnabled = -1;
    int numTabs = tabPanel.countTabs();
    for (int tab = 0; tab < numTabs; ++tab)
      if (updateTabEnabled(tab) && firstEnabled == -1)
	firstEnabled = tab;
    int curTab = tabPanel.getCurrentPanelNdx();
    if (curTab < 0 && firstEnabled != -1)
      tabPanel.showTab(firstEnabled);
  }

  public boolean updateTabEnabled(int tab) {
    boolean canEdit;
    Component c = tabPanel.getTabPanel(tab);
    if (c instanceof PropSheet)
      canEdit = ((PropSheet) c).canEdit(_selection);
    else canEdit = true;
    tabPanel.enableTabPanel(canEdit, tab);
    return canEdit;
    }

/** <A HREF="../bugs.html#win_prop_sheet_grabs_focus">
 *  <FONT COLOR=660000><B>BUG: win_prop_sheet_grabs_focus</B></FONT></A>
 */
  public void updateCurSheet() {
    int curTab = tabPanel.getCurrentPanelNdx();
    Component c = tabPanel.getTabPanel(curTab);
    //int curTab = sheetChoice.getSelectedIndex();
    //Component c = getCurrentSheet();
    //updateTabEnabled(_lastTab);
    if (c instanceof PropSheet) {
      PropSheet curPropSheet = (PropSheet) c;
      curPropSheet.setSelection(_selection);
      if (_lastPropSheet != curPropSheet) {
	uci.beans.editors.ColorPickerGrid.stopEditing();
	if (_lastPropSheet != null) _lastPropSheet.setSelection(null);
      }
      _lastTab = curTab;
      _lastPropSheet = curPropSheet;
    }
  }

  ////////////////////////////////////////////////////////////////
  // methods that support testing

  public static void main(String args[]) {
    TabPropFrame ps = new TabPropFrame();
    ps.show();
  }

} /* end class TabPropFrame */

