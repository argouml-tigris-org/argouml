// $Id$
// Copyright (c) 1996-2003 The Regents of the University of California. All
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

/** Deutsch
 * This is hardcoded. This class should be
 * modified to use something similar to the about
 * box. Ideally this class would call on a
 * xml file for all the languages and allow the users
 * to specify which language through a GUI
 * dialog.
 */
public class MenuResourceBundle_de extends ListResourceBundle {

   static final Object[][] _contents = {
        {"1", "1" },
        {"0..1", "0..1" },
        {"0..*", "0..*" },
        {"1..*", "1..*" },
        {"aggregate", "vereinigte" },
        {"composite", "zusammengesetzte" },
        {"none", "keine" },
        {"Show Attribute Compartment", "Attribute anzeigen" },
        {"Hide Attribute Compartment", "Attribute verstecken" },
        {"Show Operation Compartment", "Operationen anzeigen" },
        {"Hide Operation Compartment", "Operationen verstecken" },
        {"Show All Compartments", "Alles anzeigen" },
        {"Hide All Compartments", "Alles verstecken" },
        {"File", "Datei" },
        {"Mnemonic_File", "D" },
        {"Mnemonic_New", "N" },
        {"Mnemonic_Open", "O" },
        {"Mnemonic_Save", "S" },
        {"Mnemonic_SaveAs", "U" },
        {"Mnemonic_Print", "P" },
        {"Mnemonic_SaveGraphics", "G" },
        {"Mnemonic_Exit", "B" },
        {"Edit", "Bearbeiten" },
        {"Mnemonic_Edit", "E" },
        {"Select", "Ausw\u00e4hlen" },
        {"Mnemonic_Cut", "X" },
        {"Mnemonic_Copy", "C" },
        {"Mnemonic_Paste", "V" },
        {"Mnemonic_RemoveFromDiagram", "R" },
        {"Mnemonic_DeleteFromModel", "D" },
        {"View", "Anzeigen" },
        {"Mnemonic_View", "V" },
        {"Editor Tabs", "Editor Registerkarten" },
        {"Details Tabs", "Details Registerkarten" },
        {"Create", "Erzeugen" },
        {"Mnemonic_Create", "C" },
        {"Diagrams", "Diagramme" },
        {"Create Diagram", "Neues Diagramm" },
        {"Arrange", "Anordnen" },
        {"Mnemonic_Arrange", "A" },
        {"Align", "Ausrichten" },
        {"Distribute", "Verteilen" },
        {"Reorder", "Neu anordnen" },
        {"Nudge", "Verschieben" },
        {"Generation", "Generieren" },
        {"Mnemonic_Generate", "G" },
        {"Critique", "Hinweise" },
        {"Mnemonic_Critique", "i" },
        {"Help", "Hilfe" },
        {"Mnemonic_Help", "H" },
        {"ToDoItem", "\"Noch zu bearbeiten\"-Element" },
        {"Javadocs", "Javadocs" },
        {"Source", "Sourcecode" },
        {"Constraints", "Restriktionen" },
        {"TaggedValues", "TaggedValues" },
        {"Checklist", "Checkliste" },
        {"History", "History" },
    // arrange menu
        {"Set minimum size", "Gr\u00f6sse an Inhalt anpassen"},
        {"Toggle Auto Resizing", "Automatisch Gr\u00f6sse an Inhalt anpassen"},
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
