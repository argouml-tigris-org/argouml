// Copyright (c) 1996-2002 The Regents of the University of California. All
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

/** Russian Resource bundle for internationalization of menu
 *
 *   @author Alexey Aphanasyev (Alexey@tigris.org)
 *   @see org.argouml.i18n.MenuResourceBundle
 */
public class MenuResourceBundle_ru extends ListResourceBundle {


   static final Object[][] _contents = {
        {"New", "\u041d\u043e\u0432\u044b\u0439 \u043f\u0440\u043e\u0435\u043a\u0442" },
        {"Open Project...", "\u041e\u0442\u043a\u0440\u044b\u0442\u044c \u043f\u0440\u043e\u0435\u043a\u0442..." },
        {"Save Project", "\u0421\u043e\u0445\u0440\u0430\u043d\u0438\u0442\u044c \u043f\u0440\u043e\u0435\u043a\u0442" },
        {"Load model from DB", "\u0417\u0430\u0433\u0440\u0443\u0437\u0438\u0442\u044c \u043c\u043e\u0434\u0435\u043b\u044c \u0438\u0437 \u0411\u0414" },
        {"Store model to DB", "\u0421\u043e\u0445\u0440\u0430\u043d\u0438\u0442\u044c \u043c\u043e\u0434\u0435\u043b\u044c \u0432 \u0411\u0414" },
        {"Save Project As...", "\u0421\u043e\u0445\u0440\u0430\u043d\u0438\u0442\u044c \u043f\u0440\u043e\u0435\u043a\u0442 \u043a\u0430\u043a..." },
        {"Import", "\u0418\u043c\u043f\u043e\u0440\u0442\u0438\u0440\u043e\u0432\u0430\u0442\u044c" },
        {"Import sources...", "\u0418\u043c\u043f\u043e\u0440\u0442\u0438\u0440\u043e\u0432\u0430\u0442\u044c \u0438\u0441\u0445\u043e\u0434\u043d\u044b\u0439 \u043a\u043e\u0434..." },
        {"Print...", "\u041f\u0435\u0447\u0430\u0442\u0430\u0442\u044c..." },
        {"Save GIF...", "\u0421\u043e\u0445\u0440\u0430\u043d\u0438\u0442\u044c \u043a\u0430\u043a GIF..." },
        {"Save Graphics...", "\u0421\u043e\u0445\u0440\u0430\u043d\u0438\u0442\u044c \u0434\u0438\u0430\u0433\u0440\u0430\u043c\u044b \u043a\u0430\u043a \u0433\u0440\u0430\u0444\u0438\u043a\u0443..." },
	{"Save Configuration", "\u0421\u043e\u0445\u0440\u0430\u043d\u0438\u0442\u044c \u043a\u043e\u043d\u0444\u0438\u0433\u0443\u0440\u0430\u0446\u0438\u044e"},
        {"Exit", "\u0412\u044b\u0445\u043e\u0434" },
        {"Undo", "\u041e\u0442\u043c\u0435\u043d\u0438\u0442\u044c" },
        {"Redo", "\u041f\u043e\u0432\u0442\u043e\u0440\u0438\u0442\u044c" },
        {"Cut", "\u0412\u044b\u0440\u0435\u0437\u0430\u0442\u044c" },
        {"Copy", "\u041a\u043e\u043f\u0438\u0440\u043e\u0432\u0430\u0442\u044c" },
        {"Paste", "\u0412\u0441\u0442\u0430\u0432\u0438\u0442\u044c" },
	{"Settings...", "\u0423\u0441\u0442\u0430\u043d\u043e\u0432\u043a\u0438..."},
        {"Set Source Path...", "Set Source Path..." },
        {"Delete From Diagram", "\u0423\u0434\u0430\u043b\u0438\u0442\u044c \u0438\u0437 \u0434\u0438\u0430\u0433\u0440\u0430\u043c\u043c\u044b" },
        {"Erase From Model", "\u0423\u0434\u0430\u043b\u0438\u0442\u044c \u0438\u0437 \u043c\u043e\u0434\u0435\u043b\u0438" },
        {"Empty Trash", "\u0423\u043d\u0438\u0447\u0442\u043e\u0436\u0438\u0442\u044c \u043c\u0443\u0441\u043e\u0440" },
        {"Navigate Back", "\u041f\u0440\u043e\u0434\u0432\u0438\u043d\u0443\u0442\u044c\u0441\u044f \u0432\u043f\u0435\u0440\u0435\u0434" },
        {"Navigate Forward", "\u0412\u0435\u0440\u043d\u0443\u0442\u044c\u0441\u044f \u043d\u0430\u0437\u0430\u0434" },
        {"NavConfig", "\u041a\u043e\u043d\u0444\u0438\u0433\u0443\u0440\u0438\u0440\u043e\u0432\u0430\u043d\u0438\u0435 \u043f\u0430\u043d\u0435\u043b\u0438 \u043d\u0430\u0432\u0438\u0433\u0430\u0442\u043e\u0440\u0430" },
        {"Find...", "\u041d\u0430\u0439\u0442\u0438..." },
        {"Goto Diagram...", "\u041f\u0435\u0440\u0435\u0439\u0442\u0438 \u043a \u0434\u0438\u0430\u0433\u0440\u0430\u043c\u0435..." },
        {"Next Editing Tab", "\u0421\u043b\u0435\u0434\u0443\u044e\u0449\u0430\u044f \u0437\u0430\u043a\u043b\u0430\u0434\u043a\u0430 \u043f\u0430\u043d\u0435\u043b\u0438 \u0440\u0435\u0434\u0430\u043a\u0442\u043e\u0440\u0430" },
        {"Next Details Tab", "\u0421\u043b\u0435\u0434\u0443\u044e\u0449\u0430\u044f \u0437\u0430\u043a\u043b\u0430\u0434\u043a\u0430 \u043f\u0430\u043d\u0435\u043b\u0438 \u0434\u0435\u0442\u0430\u043b\u0435\u0439" },
        {"Buttons on Selection", "\u041a\u043d\u043e\u043f\u043a\u0438 \u043d\u0430 \u0432\u044b\u0431\u0440\u0430\u043d\u043d\u043e\u043c \u044d\u043b\u0435\u043c\u0435\u043d\u0442\u0435" },
        {"Create Multiple...", "\u0421\u043e\u0437\u0434\u0430\u0442\u044c \u043d\u0435\u0441\u043a\u043e\u043b\u044c\u043a\u043e..." },
        {"Add Top-Level Package", "\u0414\u043e\u0431\u0430\u0432\u0438\u0442\u044c \u043f\u0430\u043a\u0435\u0442 \u0432\u0435\u0440\u0445\u043d\u0435\u0433\u043e \u0443\u0440\u043e\u0432\u043d\u044f" },
        {"ClassDiagram", "\u0414\u0438\u0430\u0433\u0440\u0430\u043c\u043c\u0430 \u041a\u043b\u0430\u0441\u0441\u043e\u0432" },
        {"UseCaseDiagram", "\u0414\u0438\u0430\u0433\u0440\u0430\u043c\u043c\u0430 \u0412\u0430\u0440\u0438\u0430\u043d\u0442\u043e\u0432 \u0438\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u043d\u0438\u044f" },
        {"StateDiagram", "\u0414\u0438\u0430\u0433\u0440\u0430\u043c\u043c\u0430 \u0441\u043e\u0441\u0442\u043e\u044f\u043d\u0438\u0439" },
        {"ActivityDiagram", "\u0414\u0438\u0430\u0433\u0440\u0430\u043c\u043c\u0430 \u0434\u0435\u044f\u0442\u0435\u043b\u044c\u043d\u043e\u0441\u0442\u0438" },
        {"CollaborationDiagram", "\u0414\u0438\u0430\u0433\u0440\u0430\u043c\u043c\u0430 \u043a\u043e\u043e\u043f\u0435\u0440\u0430\u0446\u0438\u0439" },
        {"DeploymentDiagram", "\u0414\u0438\u0430\u0433\u0440\u0430\u043c\u043c\u0430 \u0440\u0430\u0437\u0432\u0435\u0440\u0442\u044b\u0432\u0430\u043d\u0438\u044f" },
        {"SequenceDiagram", "\u0414\u0438\u0430\u0433\u0440\u0430\u043c\u043c\u0430 \u043f\u043e\u0441\u043b\u0435\u0434\u043e\u0432\u0430\u0442\u0435\u043b\u044c\u043d\u043e\u0441\u0442\u0438" },
        {"button.add-attribute", "\u0414\u043e\u0431\u0430\u0432\u0438\u0442\u044c \u0430\u0442\u0440\u0438\u0431\u0443\u0442" },
        {"button.add-operation", "\u0414\u043e\u0431\u0430\u0432\u0438\u0442\u044c \u043e\u043f\u0435\u0440\u0430\u0446\u0438\u044e" },
        {"Add Message", "\u0414\u043e\u0431\u0430\u0432\u0438\u0442\u044c \u0441\u043e\u043e\u0431\u0449\u0435\u043d\u0438\u0435" },
        {"Add Internal Transition", "\u0414\u043e\u0431\u0430\u0432\u0438\u0442\u044c \u0432\u043d\u0443\u0442\u0440\u0435\u043d\u043d\u0438\u0439 \u043f\u0435\u0440\u0435\u0445\u043e\u0434" },
        {"Generate Selected Classes", "\u0421\u0433\u0435\u043d\u0435\u0440\u0438\u0440\u043e\u0432\u0430\u0442\u044c \u0432\u044b\u0431\u0440\u0430\u043d\u043d\u044b\u0435 \u043a\u043b\u0430\u0441\u0441\u044b..." },
        {"Generate All Classes", "\u0421\u0433\u0435\u043d\u0435\u0440\u0438\u0440\u043e\u0432\u0430\u0442\u044c \u0432\u0441\u0435 \u043a\u043b\u0430\u0441\u0441\u044b..." },
        {"Generate Code for Project", "\u0421\u0433\u0435\u043d\u0435\u0440\u0438\u0440\u043e\u0432\u0430\u0442\u044c \u043a\u043e\u0434 \u0434\u043b\u044f \u043f\u0440\u043e\u0435\u043a\u0442\u0430..." },
        {"Settings for Generate for Project", "Settings for Generate for Project..." },
        {"Toggle Auto-Critique", "\u0412\u043a\u043b\u044e\u0447\u0438\u0442\u044c \u0430\u0432\u0442\u043e\u043a\u0440\u0438\u0442\u0438\u043a\u0443" },
        {"Design Issues...", "\u0421\u043f\u043e\u0440\u043d\u044b\u0435 \u043c\u043e\u043c\u0435\u043d\u0442\u044b \u043f\u0440\u043e\u0435\u043a\u0442\u0438\u0440\u043e\u0432\u0430\u043d\u0438\u044f..." },
        {"Design Goals...", "\u0426\u0435\u043b\u0438 \u043f\u0440\u043e\u0435\u043a\u0442\u0438\u0440\u043e\u0432\u0430\u043d\u0438\u044f..." },
        {"Browse Critics...", "\u041f\u0440\u043e\u0441\u043c\u043e\u0442\u0440 \u043a\u0440\u0438\u0442\u0438\u0447\u0435\u0441\u043a\u0438\u0445 \u0437\u0430\u043c\u0435\u0447\u0430\u043d\u0438\u0439..." },
        {"Toggle Flat View", "\u0412\u043a\u043b\u044e\u0447\u0438\u0442\u044c \u043f\u043b\u043e\u0441\u043a\u0438\u0439 \u0432\u0438\u0434" },
        {"New To Do Item...", "\u041d\u043e\u0432\u043e\u0435 \u0437\u0430\u0434\u0430\u043d\u0438\u0435..." },
        {"Resolve Item...", "\u0420\u0435\u0448\u0438\u0442\u044c \u0437\u0430\u0434\u0430\u043d\u0438\u0435..." },
        {"Send Email To Expert...", "\u0421\u043f\u0440\u043e\u0441\u0438\u0442\u0435 \u044d\u043a\u0441\u043f\u0435\u0440\u0442\u0430..." },
        {"More Info...", "\u0414\u043e\u043f. \u0438\u043d\u0444\u043e\u0440\u043c\u0430\u0446\u0438\u044f..." },
        {"Snooze Critic", "\u0423\u0441\u044b\u043f\u0438\u0442\u044c \u043a\u0440\u0438\u0442\u0438\u043a\u0443" },
        {"About Argo/UML", "\u041a\u043e\u0440\u043e\u0442\u043a\u043e \u043e\u0431 ArgoUML..." },
        {"Properties", "\u0421\u0432\u043e\u0439\u0441\u0442\u0432\u0430" },
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
        {"Zoom", "\u041c\u0430\u0441\u0448\u0442\u0430\u0431" },
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
        {"Layout", "\u041a\u043e\u043c\u043f\u043e\u043d\u043e\u0432\u043a\u0430" },
        {"Generation", "\u0413\u0435\u043d\u0435\u0440\u0438\u0440\u043e\u0432\u0430\u043d\u0438\u0435" },
        {"Mnemonic_Generate", "G" },
        {"Critique", "\u041a\u0440\u0438\u0442\u0438\u043a\u0430" },
        {"Mnemonic_Critique", "R" },
        {"Help", "\u041f\u043e\u043c\u043e\u0449\u044c" },
        {"Mnemonic_Help", "H" },
        {"Tools", "\u0418\u043d\u0441\u0442\u0440\u0443\u043c\u0435\u043d\u0442\u044b" },
        {"Automatic", "\u0410\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438" },
        {"Incremental", "\u0418\u043d\u043a\u0440\u0435\u043c\u0435\u043d\u0442\u043d\u043e" },
        {"As Diagram", "\u041a\u0430\u043a \u0434\u0438\u0430\u0433\u0440\u0430\u043c\u043c\u0430" },
        {"As Table", "\u041a\u0430\u043a \u0442\u0430\u0431\u043b\u0438\u0446\u0430" },
        {"As Metrics", "\u041a\u0430\u043a \u043c\u0435\u0442\u0440\u0438\u043a\u0430" },
        {"ToDoItem", "\u0417\u0430\u0434\u0430\u043d\u0438\u0435" },
        {"Javadocs", "\u0414\u043e\u043a\u0443\u043c\u0435\u043d\u0442\u0430\u0446\u0438\u044f Javadocs" },
        {"Source", "\u0418\u0441\u0445\u043e\u0434\u043d\u044b\u0439 \u043a\u043e\u0434" },
        {"Constraints", "\u041e\u0433\u0440\u0430\u043d\u0438\u0447\u0435\u043d\u0438\u044f" },
        {"TaggedValues", "\u0418\u043c\u0435\u043d\u043e\u0432\u0430\u043d\u043d\u044b\u0435 \u0437\u043d\u0430\u0447\u0435\u043d\u0438\u044f" },
        {"Checklist", "\u041a\u043e\u043d\u0442\u0440\u043e\u043b\u044c\u043d\u044b\u0439 \u0441\u043f\u0438\u0441\u043e\u043a" },
        {"History", "\u0418\u0441\u0442\u043e\u0440\u0438\u044f" },

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
