// Copyright (c) 1996-2001 The Regents of the University of California. All
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

package org.argouml.i18n;

import java.awt.Toolkit;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;

public class MenuResourceBundle extends ListResourceBundle {


   static final Object[][] _contents = {
        {"1", "1" },
        {"0..1", "0..1" },
        {"0..*", "0..*" },
        {"1..*", "1..*" },
        {"aggregate", "aggregate" },
        {"composite", "composite" },
        {"none", "none" },
        {"Show Attribute Compartment", "Show Attribute Compartment" },
        {"Hide Attribute Compartment", "Hide Attribute Compartment" },
        {"Show Operation Compartment", "Show Operation Compartment" },
        {"Hide Operation Compartment", "Hide Operation Compartment" },
        {"Show All Compartments", "Show All Compartments" },
        {"Hide All Compartments", "Hide All Compartments" },
        {"File", "File" },
        {"Mnemonic_File", "F" },
        {"Mnemonic_New", "N" },
        {"Mnemonic_Open", "O" },
        {"Mnemonic_Save", "S" },
        {"Mnemonic_SaveAs", "A" },
        {"Mnemonic_RevertToSaved", "R" },
        {"Mnemonic_Print", "P" },
        {"Mnemonic_SaveGraphics", "G" },
        {"Mnemonic_Exit", "X" },
        {"Edit", "Edit" },
        {"Mnemonic_Edit", "E" },
        {"Select", "Select" },
        {"Mnemonic_Cut", "X" },
        {"Mnemonic_Copy", "C" },
        {"Mnemonic_Paste", "V" },
        {"Mnemonic_RemoveFromDiagram", "R" },
        {"Mnemonic_DeleteFromModel", "D" },
        {"View", "View" },
        {"Mnemonic_View", "V" },
        {"Editor Tabs", "Editor Tabs" },
        {"Details Tabs", "Details Tabs" },
        {"Create", "Create" },
        {"Mnemonic_Create", "C" },
        {"Diagrams", "Diagrams" },
        {"Create Diagram", "Create Diagram" },
        {"Arrange", "Arrange" },
        {"Mnemonic_Arrange", "A" },
        {"Align", "Align" },
        {"Distribute", "Distribute" },
        {"Reorder", "Reorder" },
        {"Nudge", "Nudge" },
        {"Generation", "Generation" },
        {"Mnemonic_Generate", "G" },
        {"Critique", "Critique" },
        {"Mnemonic_Critique", "R" },
        {"Help", "Help" },
        {"Mnemonic_Help", "H" },
        {"Automatic", "Automatic" },
        {"Incremental", "Incremental" },
        {"ToDoItem", "ToDoItem" },
        {"Javadocs", "Javadocs" },
        {"Source", "Source" },
        {"Constraints", "Constraints" },
        {"TaggedValues", "TaggedValues" },
        {"Checklist", "Checklist" },
        {"History", "History" },
        {"Layout", "Layout" },
        {"Zoom", "Zoom"},
        {"Tools", "Tools"},
    // arrange menu
        {"Set minimum size", "Fit size for content"},
        {"Toggle Auto Resizing", "Automatically fit size for content"},
    // shortcut keys
        { "Shortcut_New", KeyStroke.getKeyStroke(KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()) },
        { "Shortcut_Open", KeyStroke.getKeyStroke(KeyEvent.VK_O, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()) },
        { "Shortcut_Save", KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()) },
        { "Shortcut_Print", KeyStroke.getKeyStroke(KeyEvent.VK_P, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()) },
        { "Shortcut_Select_All", KeyStroke.getKeyStroke(KeyEvent.VK_A, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()) },
        { "Shortcut_Copy", KeyStroke.getKeyStroke(KeyEvent.VK_C, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()) },
        { "Shortcut_Paste", KeyStroke.getKeyStroke(KeyEvent.VK_V, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()) },
        { "Shortcut_Cut", KeyStroke.getKeyStroke(KeyEvent.VK_X, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()) },
        { "Shortcut_Remove_From_Diagram", KeyStroke.getKeyStroke(KeyEvent.VK_R, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()) },
        { "Shortcut_Find", KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0) },
        { "Shortcut_Generate_All", KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0) },
        { "Shortcut_Exit", KeyStroke.getKeyStroke(KeyEvent.VK_F4, KeyEvent.ALT_MASK) },
        { "Shortcut_Delete", KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0)}
   };

     public Object[][] getContents() {
        return _contents;
     }
}
