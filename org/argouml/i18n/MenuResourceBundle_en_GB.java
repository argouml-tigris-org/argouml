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
import org.argouml.util.*;
import javax.swing.*;
import java.awt.event.*;

/** English Great Britain
 * This is hardcoded. This class should be
 * modified to use something similar to the about
 * box. Ideally this class would call on a
 * xml file for all the languages and allow the users
 * to specify which language through a GUI
 * dialog.
 */
public class MenuResourceBundle_en_GB extends ListResourceBundle {



   static final Object[][] _contents = {
        {"New", "New" },
        {"Open Project...", "Open Project..." },
        {"Save Project", "Save Project" },
        {"Load model from DB", "Load Model from DB" },
        {"Store model to DB", "Store Model to DB" },
        {"Save Project As...", "Save Project As..." },
        {"Print...", "Print..." },
        {"Save GIF...", "Save GIF..." },
        {"Save Graphics...", "Save Graphics..." },
        {"Exit", "Exit" },
        {"Undo", "Undo" },
        {"Redo", "Redo" },
        {"Cut", "Cut" },
        {"Copy", "Copy" },
        {"Paste", "Paste" },
        {"Set Source Path...", "Set Source Path..." },
        {"Delete From Diagram", "Delete From Diagram" },
        {"Erase From Model", "Erase From Model" },
        {"Empty Trash", "Empty Trash" },
        {"Navigate Back", "Navigate Back" },
        {"Navigate Forward", "Navigate Forward" },
        {"NavConfig", "NavConfig" },
        {"Find...", "Find..." },
        {"Goto Diagram...", "Goto Diagram..." },
        {"Next Editing Tab", "Next Editing Tab" },
        {"Next Details Tab", "Next Details Tab" },
        {"Buttons on Selection", "Buttons on Selection" },
        {"Create Multiple...", "Create Multiple..." },
        {"Add Top-Level Package", "Add Top-Level Package" },
        {"ClassDiagram", "Class Diagram" },
        {"UseCaseDiagram", "Use Case Diagram" },
        {"StateDiagram", "State Diagram" },
        {"ActivityDiagram", "Activity Diagram" },
        {"CollaborationDiagram", "Collaboration Diagram" },
        {"DeploymentDiagram", "Deployment Diagram" },
        {"SequenceDiagram", "Sequence Diagram" },
        {"button.add-attribute", "Add Attribute" },
        {"button.add-operation", "Add Operation" },
        {"Add Message", "Add Message" },
        {"Add Internal Transition", "Add Internal Transition" },
        {"Generate Selected Classes", "Generate Selected Classes..." },
        {"Generate All Classes", "Generate All Classes..." },
        {"Generate Code for Project", "Generate Code for Project..." },
        {"Settings for Generate for Project", "Settings for Generate for Project..." },
        {"Toggle Auto-Critique", "Toggle Auto-Critique" },
        {"Design Issues...", "Design Issues..." },
        {"Design Goals...", "Design Goals..." },
        {"Browse Critics...", "Browse Critics..." },
        {"Toggle Flat View", "Toggle Flat View" },
        {"New To Do Item...", "New To Do Item..." },
        {"Resolve Item...", "Resolve Item..." },
        {"Send Email To Expert...", "Send Email To Expert..." },
        {"More Info...", "More Info..." },
        {"Snooze Critic", "Snooze Critic" },
        {"About Argo/UML", "About ArgoUML..." },
        {"Properties", "Properties" },
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
        {"As Diagram", "As Diagram" },
        {"As Table", "As Table" },
        {"As Metrics", "As Metrics" },
        {"ToDoItem", "ToDoItem" },
        {"Javadocs", "Javadocs" },
        {"Source", "Source" },
        {"Constraints", "Constraints" },
        {"TaggedValues", "TaggedValues" },
        {"Checklist", "Checklist" },
        {"History", "History" },

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
