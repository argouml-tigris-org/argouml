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
import java.util.*;
import javax.swing.*;
import java.awt.event.*;


/** This is the language specs for French.
 * This is hardcoded. This class should be
 * modified to use something similar to the about
 * box. Ideally this class would call on the
 * xml file for all the languages and allow the users
 * to specify which language through a GUI
 * dialog.
 */
public class MenuResourceBundle_fr extends ListResourceBundle {


   static final Object[][] _contents = {
        {"1", "1" },
        {"0..1", "0..1" },
        {"0..*", "0..*" },
        {"1..*", "1..*" },
        {"aggregate", "agr\u00e9gat" },
        {"composite", "composite" },
        {"none", "aucun" },
        {"Show Attribute Compartment", "Afficher le Compartiment des Attributs" },
        {"Hide Attribute Compartment", "Masquer le Compartiment des Attributs" },
        {"Show Operation Compartment", "Afficher le Compartiment des Op\u00e9rations" },
        {"Hide Operation Compartment", "Masquer le Compartiment des Op\u00e9rations" },
        {"Show All Compartments", "Afficher Tous les Compartiments" },
        {"Hide All Compartments", "Masquer Tous les Compartiments" },
        {"File", "Fichier" },
        {"Mnemonic_File", "F" },
        {"Mnemonic_New", "N" },
        {"Mnemonic_Open", "O" },
        {"Mnemonic_Save", "S" },
        {"Mnemonic_SaveAs", "A" },
        {"Mnemonic_Print", "P" },
        {"Mnemonic_SaveGraphics", "G" },
        {"Mnemonic_Exit", "X" },
        {"Edit", "\u00c9dition" },
        {"Mnemonic_Edit", "E" },
        {"Select", "S\u00e9lectionner" },
        {"Mnemonic_Cut", "X" },
        {"Mnemonic_Copy", "C" },
        {"Mnemonic_Paste", "V" },
        {"Mnemonic_RemoveFromDiagram", "R" },
        {"Mnemonic_DeleteFromModel", "D" },
        {"View", "Voir" },            // To Be Confirmed
        {"Mnemonic_View", "V" },
        {"Editor Tabs", "Onglet d'\u00c9dition" },
        {"Details Tabs", "Onglet des D\u00e9tails" },
        {"Create", "Cr\u00e9er" },
        {"Mnemonic_Create", "C" },
        {"Diagrams", "Diagrammes" },
        {"Create Diagram", "Cr\u00e9er Diagramme" },
        {"Arrange", "Organiser" },
        {"Mnemonic_Arrange", "A" },
        {"Align", "Aligner" },
        {"Distribute", "Distribuer" },
        {"Reorder", "R\u00e9ordonner" },
        {"Nudge", "Ajuster" },
        {"Generation", "G\u00e9n\u00e9ration" },
        {"Mnemonic_Generate", "G" },
        {"Critique", "Critique" },
        {"Mnemonic_Critique", "R" },
        {"Help", "Aide" },
        {"Mnemonic_Help", "H" },
        {"ToDoItem", "RubriqueAFaire" },
        {"Javadocs", "Javadocs" },
        {"Source", "Source" },
        {"Constraints", "Contraintes" },
        {"TaggedValues", "\u00c9tiquettes" },
        {"Checklist", "V\u00e9rifications" },      // To Be Confirmed
        {"History", "Historique" },

        { "Shortcut_New", KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_MASK) },
        { "Shortcut_Open", KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_MASK) },
        { "Shortcut_Save", KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_MASK) },
        { "Shortcut_Print", KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_MASK) },
        { "Shortcut_Select_All", KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_MASK) },
        { "Shortcut_Copy", KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_MASK) },
        { "Shortcut_Paste", KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_MASK) },
        { "Shortcut_Cut", KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_MASK) },
        { "Shortcut_Remove_From_Diagram", KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_MASK) },
        { "Shortcut_Find", KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0) },
        { "Shortcut_Generate_All", KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0) },
        { "Shortcut_Exit", KeyStroke.getKeyStroke(KeyEvent.VK_F4, KeyEvent.ALT_MASK) },
        { "Shortcut_Delete", KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0)}
   };

     public Object[][] getContents() {
        return _contents;
     }
}
