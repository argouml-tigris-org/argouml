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
import java.util.*;
import javax.swing.*;
import java.awt.event.*;

/** Russian Resource bundle for internationalization of menu
 *
 *   @author Alexey Aphanasyev (Alexey@tigris.org)
 *   @see org.argouml.i18n.MenuResourceBundle
 */
public class MenuResourceBundle_ru extends ListResourceBundle {

   static final Object[][] _contents = {
        {"1", "1" },
        {"0..1", "0..1" },
        {"0..*", "0..*" },
        {"1..*", "1..*" },
        {"aggregate", "\u0430\u0433\u0440\u0435\u0433\u0430\u0442" },
        {"composite", "\u043a\u043e\u043c\u043f\u043e\u0437\u0438\u0442\u043d\u044b\u0439" },
        {"none", "\u043f\u0443\u0441\u0442\u043e" },
        {"Show Attribute Compartment", "\u041f\u043e\u043a\u0430\u0437\u0430\u0442\u044c \u0440\u0430\u0437\u0434\u0435\u043b \u0430\u0442\u0440\u0438\u0431\u0443\u0442\u043e\u0432" },
        {"Hide Attribute Compartment", "\u0421\u043f\u0440\u044f\u0442\u0430\u0442\u044c \u0440\u0430\u0437\u0434\u0435\u043b \u0430\u0442\u0440\u0438\u0431\u0443\u0442\u043e\u0432" },
        {"Show Operation Compartment", "\u043f\u043e\u043a\u0430\u0437\u0430\u0442\u044c \u0440\u0430\u0437\u0434\u0435\u043b \u043e\u043f\u0435\u0440\u0430\u0446\u0438\u0439" },
        {"Hide Operation Compartment", "\u0421\u043f\u0440\u044f\u0442\u0430\u0442\u044c \u0440\u0430\u0437\u0434\u0435\u043b \u043e\u043f\u0435\u0440\u0430\u0446\u0438\u0439" },
        {"Show All Compartments", "\u041f\u043e\u043a\u0430\u0437\u0430\u0442\u044c \u0432\u0441\u0435 \u0440\u0430\u0437\u0434\u0435\u043b\u044b" },
        {"Hide All Compartments", "\u0421\u043f\u0440\u044f\u0442\u0430\u0442\u044c \u0432\u0441\u0435 \u0440\u0430\u0437\u0434\u0435\u043b\u044b" },
        {"File", "\u0424\u0430\u0439\u043b" },
        {"Mnemonic_File", "F" },
        {"Mnemonic_New", "N" },
        {"Mnemonic_Open", "O" },
        {"Mnemonic_Save", "S" },
        {"Mnemonic_SaveAs", "A" },
        {"Mnemonic_Print", "P" },
        {"Mnemonic_SaveGraphics", "G" },
        {"Mnemonic_Exit", "X" },
        {"Edit", "\u0420\u0435\u0434\u0430\u043a\u0442\u0438\u0440\u043e\u0432\u0430\u0442\u044c" },
        {"Mnemonic_Edit", "E" },
        {"Select", "\u0412\u044b\u0431\u0440\u0430\u0442\u044c" },
        {"Mnemonic_Cut", "X" },
        {"Mnemonic_Copy", "C" },
        {"Mnemonic_Paste", "V" },
        {"Mnemonic_RemoveFromDiagram", "R" },
        {"Mnemonic_DeleteFromModel", "D" },
        {"View", "\u041f\u0440\u043e\u0441\u043c\u043e\u0442\u0440" },
        {"Mnemonic_View", "V" },
        {"Editor Tabs", "\u0417\u0430\u043a\u043b\u0430\u0434\u043a\u0438 \u043f\u0430\u043d\u0435\u043b\u0438 \u0440\u0435\u0434\u0430\u043a\u0442\u043e\u0440\u0430" },
        {"Details Tabs", "\u0417\u0430\u043a\u043b\u0430\u0434\u043a\u0438 \u043f\u0430\u043d\u0435\u043b\u0438 \u0434\u0435\u0442\u0430\u043b\u0435\u0439" },
        {"Create", "\u0421\u043e\u0437\u0434\u0430\u0442\u044c" },
        {"Mnemonic_Create", "C" },
        {"Diagrams", "\u0414\u0438\u0430\u0433\u0440\u0430\u043c\u043c\u044b" },
        {"Create Diagram", "\u0421\u043e\u0437\u0434\u0430\u0442\u044c \u0434\u0438\u0430\u0433\u0440\u0430\u043c\u043c\u0443" },
        {"Arrange", "\u0420\u0430\u0441\u0441\u0442\u0430\u0432\u0438\u0442\u044c" },
        {"Mnemonic_Arrange", "A" },
        {"Align", "\u0412\u044b\u0440\u043e\u0432\u043d\u044f\u0442\u044c" },
        {"Distribute", "\u0420\u0430\u0441\u043f\u0440\u0435\u0434\u0435\u043b\u0438\u0442\u044c" },
        {"Reorder", "\u0420\u0435\u043e\u0440\u0433\u0430\u043d\u0438\u0437\u043e\u0432\u0430\u0442\u044c" },
        {"Nudge", "\u041f\u043e\u0434\u0442\u043e\u043b\u043a\u043d\u0443\u0442\u044c" },
        {"Generation", "\u0413\u0435\u043d\u0435\u0440\u0438\u0440\u043e\u0432\u0430\u043d\u0438\u0435" },
        {"Mnemonic_Generate", "G" },
        {"Critique", "\u041a\u0440\u0438\u0442\u0438\u043a\u0430" },
        {"Mnemonic_Critique", "R" },
        {"Help", "\u041f\u043e\u043c\u043e\u0449\u044c" },
        {"Mnemonic_Help", "H" },
        {"ToDoItem", "\u0417\u0430\u0434\u0430\u043d\u0438\u0435" },
        {"Javadocs", "\u0414\u043e\u043a\u0443\u043c\u0435\u043d\u0442\u0430\u0446\u0438\u044f Javadocs" },
        {"Source", "\u0418\u0441\u0445\u043e\u0434\u043d\u044b\u0439 \u043a\u043e\u0434" },
        {"Constraints", "\u041e\u0433\u0440\u0430\u043d\u0438\u0447\u0435\u043d\u0438\u044f" },
        {"TaggedValues", "\u0418\u043c\u0435\u043d\u043e\u0432\u0430\u043d\u043d\u044b\u0435 \u0437\u043d\u0430\u0447\u0435\u043d\u0438\u044f" },
        {"Checklist", "\u041a\u043e\u043d\u0442\u0440\u043e\u043b\u044c\u043d\u044b\u0439 \u0441\u043f\u0438\u0441\u043e\u043a" },
        {"History", "\u0418\u0441\u0442\u043e\u0440\u0438\u044f" },
        {"Layout", "\u041a\u043e\u043c\u043f\u043e\u043d\u043e\u0432\u043a\u0430" },
        {"Zoom", "\u041c\u0430\u0441\u0448\u0442\u0430\u0431"},
        {"Tools", "\u0418\u043d\u0441\u0442\u0440\u0443\u043c\u0435\u043d\u0442\u044b" },
    // arrange menu
        {"Set minimum size", "\u041f\u043e\u0434\u043e\u0433\u043d\u0430\u0442\u044c \u0440\u0430\u0437\u043c\u0435\u0440 \u043f\u043e \u0441\u043e\u0434\u0435\u0440\u0436\u0438\u043c\u043e\u043c\u0443"},
        {"Toggle Auto Resizing", "\u0410\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438 \u043f\u043e\u0434\u043e\u0433\u043d\u0430\u0442\u044c \u0440\u0430\u0437\u043c\u0435\u0440 \u043f\u043e \u0441\u043e\u0434\u0435\u0440\u0436\u0438\u043c\u043e\u043c\u0443"},
    // shortcut keys
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
