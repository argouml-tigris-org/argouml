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
        {"New", "Neu" },
        {"Open Project...", "Projekt \u00d6ffnen..." },
        {"Save Project", "Projekt speichern" },
        {"Load model from DB", "Lade Modell aus DB" },
        {"Store model to DB", "Speichere Modell in DB" },
	{"Save Project As...", "Projekt speichern unter..." },
	{"Import", "Importieren" },
	{"Import sources...", "Dateien importieren..." },
        {"Print...", "Drucken..." },
        {"Save GIF...", "Exportieren als GIF..." },
        {"Save Graphics...", "Grafik exportieren..." },
	{"Save Configuration", "Konfiguration speichern"},
        {"Exit", "Beenden" },
        {"Undo", "R\u00fcckg\u00e4ngig" },
        {"Redo", "Wiederherstellen" },
        {"Cut", "Ausschneiden" },
        {"Copy", "Kopieren" },
        {"Paste", "Einf\u00fcgen" },
	{"Settings...", "Einstellungen..."},
        {"Set Source Path...", "Setzen des Quell-Pfades..." },
        {"Delete From Diagram", "Aus Diagramm entfernen" },
        {"Erase From Model", "Aus Modell entfernen" },
        {"Empty Trash", "Papierkorb leeren" },
        {"Navigate Back", "Zur\u00fcck" },
        {"Navigate Forward", "Vorw\u00e4rts" },
        {"NavConfig", "Navigationskonfiguration" },
        {"Find...", "Suchen..." },
        {"Goto Diagram...", "Gehe zu Diagramm..." },
        {"Next Editing Tab", "N\u00e4chste Editierungs Registerkarte" },
        {"Next Details Tab", "N\u00e4chste Details Registerkarte" },
        {"Buttons on Selection", "Buttons zur Auswahl" },
        {"Create Multiple...", "Erzeuge mehrere..." },
        {"Add Top-Level Package", "F\u00fcge toplevel Paket hinzu" },
        {"ClassDiagram", "Klassendiagramm" },
        {"UseCaseDiagram", "Anwendungsfalldiagramm" },
        {"StateDiagram", "Zustandsdiagramm" },
        {"ActivityDiagram", "Aktivit\u00e4tsdiagramm" },
        {"CollaborationDiagram", "Kollaborationsdiagramm" },
        {"DeploymentDiagram", "Verteilungsdiagramm" },
        {"SequenceDiagram", "Sequenzdiagramm" },
        {"button.add-attribute", "Attribut hinzuf\u00fcgen" },
        {"button.add-operation", "Operation hinzuf\u00fcgen" },
        {"Add Message", "Nachricht hinzuf\u00fcgen" },
        {"Add Internal Transition", "Interne Transition hinzuf\u00fcgen" },
        {"Generate Selected Classes", "Erzeuge Code f\u00fcr selektierte Klassen..." },
        {"Generate All Classes", "Erzeuge Code f\u00fcr alle Klassen..." },
        {"Generate Code for Project", "Erzeuge Code f\u00fcr das Projekt..." },
        {"Toggle Auto-Critique", "Auto-Critique umschalten" },
        {"Design Issues...", "Entwurfsprobleme..." },
        {"Design Goals...", "Entwurfsziele..." },
        {"Browse Critics...", "Critics anpassen..." },
        {"Toggle Flat View", "Ebene Ansicht umschalten" },
        {"New To Do Item...", "Neuer Todo Eintrag..." },
        {"Resolve Item...", "Arbeite an Eintrag..." },
        {"Send Email To Expert...", "Sende Email an Experten..." },
        {"More Info...", "Mehr Informationen..." },
        {"Snooze Critic", "Critics ruhigstellen" },
        {"About Argo/UML", "\u00dcber ArgoUML..." },
        {"Properties", "Eigenschaften" },
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
        {"Nudge", "Stupsen" },
        {"Generation", "Generieren" },
        {"Mnemonic_Generate", "G" },
        {"Critique", "Critique" },
        {"Mnemonic_Critique", "R" },
        {"Help", "Hilfe" },
        {"Mnemonic_Help", "H" },
        {"As Diagram", "Als Diagramm" },
        {"As Table", "Als Tabelle" },
        {"As Metrics", "Als Metrik" },
        {"ToDoItem", "ToDo Eintrag" },
        {"Javadocs", "Javadocs" },
        {"Source", "Sourcecode" },
        {"Constraints", "Bedingungen" },
        {"TaggedValues", "TaggedValues" },
        {"Checklist", "Checkliste" },
        {"History", "History" },
    // arrange menu
        {"Set minimum size", "Gr\u00f6sse an Inhalt anpassen"},
        {"Toggle Auto Resizing", "Automatisch Gr\u00f6sse an Inhalt anpassen"},
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
