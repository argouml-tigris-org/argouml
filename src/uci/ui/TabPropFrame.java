// Copyright (c) 1996-99 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.




// File: TabPropFrame.java
// Interfaces: TabPropFrame
// Original Author: jrobbins@ics.uci.edu
// $Id$



package uci.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

/** A window that displays a tabbed Property Sheet user interface with
 *  one tab for each PropSheet instance that is added to this object.
 *  This class can operate using the tab widget that comes with Symantec
 *  VisualCafe (TM), or it can use a java.awt.Choice to select which
 *  PropSheet should be shown. Needs-More-Work: This part of GEF overlaps the
 *  JavaBeans(TM) spec to some degree and might be changed or replaced to
 *  take advantage of beans. Needs-More-Work: I would like to handle
 *  multple selections by showing values that all selected objects have in
 *  common and applying changes to all selected objects.
 */

public class TabPropFrame extends JFrame
implements ChangeListener, ActionListener {
  ////////////////////////////////////////////////////////////////
  // instance variables

  private PropSheet _lastPropSheet = null;
  private int _lastTab = -1;

  /** The collection of PropSheets shown in this frame. */
  private Vector _sheets = new Vector();

  //{{DECLARE_CONTROLS
  //private symantec.itools.awt.TabPanel tabPanel;
  private JTabbedPane tabPanel;
  //private Panel tabPanel;
  private Panel choicePanel;
  private PropSheetCategory PropSheetCategory1;
  private PropSheetCategory PropSheetCategory2;
  private PropSheetCategory PropSheetCategory3;
  private PropSheetCategory PropSheetCategory4;
  private PropSheetCategory PropSheetCategory5;
  private JPanel buttonPanel;
  /** <A HREF="../features.html#property_sheet_auto_apply">
   *  <TT>FEATURE: property_sheet_auto_apply</TT></A>
   */
  private JCheckBox autoApplyCheckbox;
  private JButton applyButton;
  /** <A HREF="../features.html#property_sheet_revert">
   *  <TT>FEATURE: property_sheet_revert</TT></A>
   */
  private JButton revertButton;
  private JButton closeButton;
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
    getContentPane().setLayout(new BorderLayout(0,0));
    addNotify();
    Insets insets = getInsets();
    setSize(insets.left + insets.right + 300,insets.top + insets.bottom + 406);
    getContentPane().setFont(new Font("Dialog", Font.PLAIN, 10));
    getContentPane().setBackground(new Color(12632256));
    tabPanel = new JTabbedPane();
    tabPanel.addChangeListener(this);
    //tabPanel.setLayout(null);
    //tabPanel.setBounds(getInsets().left + 0,
    //                  getInsets().top + 24,300,351);
    getContentPane().add(tabPanel, BorderLayout.CENTER);
    //tabPanel = new Panel();
    //tabPanel.setLayout(new CardLayout());
    //tabPanel.setBounds(getInsets().left + 0,
    //                  getInsets().top + 24,300,351);
    //tabPanel.setBackground(new Color(12632256));
    //add("Center", tabPanel);
    PropSheetCategory1 = new PropSheetCategory(this);
    PropSheetCategory1.setBounds(12,33,276,307);
    PropSheetCategory2 = new PropSheetCategory(this);
    PropSheetCategory2.setBounds(12,33,276,307);
    PropSheetCategory3 = new PropSheetCategory(this);
    PropSheetCategory3.setBounds(12,33,276,307);
    PropSheetCategory4 = new PropSheetCategory(this);
    PropSheetCategory4.setBounds(12,33,276,307);
    PropSheetCategory5 = new PropSheetCategory(this);
    PropSheetCategory5.setBounds(12,33,276,307);
    buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER,5,5));
    buttonPanel.setBounds(getInsets().left + 0,getInsets().top + 375,300,31);
    getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    autoApplyCheckbox = new JCheckBox("AutoApply");
    autoApplyCheckbox.setBounds(66,5,78,21);
    autoApplyCheckbox.setSelected(true);
    autoApplyCheckbox.addChangeListener(this);
    buttonPanel.add(autoApplyCheckbox);
    applyButton = new JButton("Apply");
    applyButton.setBounds(149,5,40,21);
    applyButton.setEnabled(false);
    buttonPanel.add(applyButton);
    applyButton.addActionListener(this);
    applyButton.setEnabled(false);
    revertButton = new JButton("Revert");
    revertButton.setBounds(149,5,40,21);
    revertButton.setEnabled(false);
    revertButton.addActionListener(this);
    revertButton.setEnabled(false);
    buttonPanel.add(revertButton);
    closeButton = new JButton("Close");
    closeButton.setBounds(194,5,40,21);
    closeButton.addActionListener(this);
    //buttonPanel.add(closeButton);
    
    // //universeChoice = new java.awt.Choice();
    // // universeChoice.setBounds(getInsets().left + 0,getInsets().top + 0,300,21);
    //sheetChoice = new java.awt.Choice();
    //sheetChoice.setBounds(getInsets().left + 0,
    //                    getInsets().top + 0,300,21);
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
    tabPanel.setSelectedIndex(0);
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
    return tabPanel.getSelectedComponent();
//     Component sheet = null;
//     //int sheetIndex = sheetChoice.getSelectedIndex();
//     int sheetIndex = tabPanel.getCurrentPanelNdx();
//     if (sheetIndex >= 0 && sheetIndex < _sheets.size()) {
//       //sheet = (Component) _sheets.elementAt(sheetIndex);
//       sheet = tabPanel.getTabPanel(sheetIndex);
//     }
//     return sheet;
  }

  /** Add a new PropSheet to this window under the name it supplies. */
  public void addPropSheet(PropSheet ps) {
    if (tabPanel == null) return;
    //tabPanel.addTabPanel(ps.getTabName(), ps.canEdit(_selection),
    //ps);
    int i = tabPanel.getTabCount();
    //System.out.println("adding tab, size = " + tabPanel.getTabCount());
    tabPanel.addTab(ps.getTabName(), ps);
    tabPanel.setEnabledAt(i, ps.canEdit(_selection));
    String curSheetName = ps.getTabName();
    //tabPanel.add(curSheetName, ps);
    //sheetChoice.addItem(curSheetName);
    _sheets.addElement(ps);
    // Needs-More-Work: no way to disable a choice item in jdk1.0.2
  }

  ////////////////////////////////////////////////////////////////
  // window operations

  public synchronized void setVisible(boolean b) {
    setLocation(50, 50);
    super.setVisible(b);
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
    //System.out.println("TabPropFrame select");
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

  public void actionPerformed(ActionEvent ae) {
    Object src = ae.getSource();
    if (src == closeButton) closeButton_Clicked(ae);
    if (src == applyButton) applyButton_Clicked(ae);
    if (src == revertButton) revertButton_Clicked(ae);
  }

  public void stateChanged(ChangeEvent ce) {
    Object src = ce.getSource();
    if (src == autoApplyCheckbox) autoApplyCheckbox_Action(ce);
    if (src == tabPanel) updateCurSheet();
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

  protected void autoApplyCheckbox_Action(ChangeEvent event) {
    //{{CONNECTION
    // Enable the Button on condition... Is Checkbox Off?
    applyButton.setEnabled(!autoApplyCheckbox.isSelected());
    revertButton.setEnabled(!autoApplyCheckbox.isSelected());
    //}}
    Component p = getCurrentSheet();
    if (p instanceof PropSheet)//?
      ((PropSheet)p).setAutoApply(autoApplyCheckbox.isSelected());
  }

  void closeButton_Clicked(ActionEvent event) {
    //{{CONNECTION
    // Hide the Frame
    setVisible(false);
    //}}
    dispose();
  }

  /** Ask the currently shown PropSheet to apply any outstanding changes.
   *  <A HREF="../bugs.html#switch_tabs_without_apply">BUG:</A>
   *  When auto-apply is off, if I make changes and then switch tabs
   *  and then press Apply, nothing happens.
   * */
  void applyButton_Clicked(ActionEvent event) {
    Component sheet = getCurrentSheet();
    if (sheet instanceof PropSheet) ((PropSheet)sheet).apply();
  }

  /** Ask the currently shown PropSheet to apply any outstanding changes. */
  void revertButton_Clicked(ActionEvent event) {
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
    //System.out.println("updateTabs");
    try {
    int firstEnabled = -1;
    int numTabs = tabPanel.getTabCount();
    for (int tab = 0; tab < numTabs; ++tab)
      if (updateTabEnabled(tab) && firstEnabled == -1)
	firstEnabled = tab;
    int curTab = tabPanel.getSelectedIndex();
    if (curTab < 0 && firstEnabled != -1)
      tabPanel.setSelectedIndex(firstEnabled);
    }
    catch (Exception ex) { }
  }

  public boolean updateTabEnabled(int tab) {
    boolean canEdit;
    try {
    Component c = tabPanel.getComponentAt(tab);
    if (c instanceof PropSheet)
      canEdit = ((PropSheet) c).canEdit(_selection);
    else canEdit = true;
    tabPanel.setEnabledAt(tab, canEdit);
    return canEdit;
    }
    catch (Exception ex) { }
    return false;
  }
    

  public void updateCurSheet() {
    //System.out.println("updateCurSheet");
    int curTab = tabPanel.getSelectedIndex();
    Component c = tabPanel.getSelectedComponent();
    //int curTab = sheetChoice.getSelectedIndex();
    //Component c = getCurrentSheet();
    //updateTabEnabled(_lastTab);
    if (c instanceof PropSheet) {
      PropSheet curPropSheet = (PropSheet) c;
      curPropSheet.setSelection(_selection);
      //System.out.println("updateCurSheet1: " + curPropSheet);
      if (_lastPropSheet != curPropSheet) {
	//System.out.println("updateCurSheet2");
	//uci.beans.editors.ColorPickerGrid.stopEditing();
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
    ps.setVisible(true);
  }

} /* end class TabPropFrame */

