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
        {"action.new", "Nouveau" },
        {"action.open-project", "Ouvrir un Projet..." },
        {"action.save-project", "Enregistrer le Projet" },
        {"action.save-project-as", "Enregistrer le Projet Sous..." },
        {"action.import-sources", "Importer depuis les sources..."},
        {"Import", "Importer"},
        {"action.print", "Imprimer..." },
        {"action.save-graphics", "Enregistrer les Graphiques..." },
	{"action.save-configuration", "Enregistrer la Configuration"},
        {"action.exit", "Quitter" },
        {"action.undo", "Annuler" },
        {"action.redo", "R\u00e9tablir" },
        {"action.cut", "Couper" },
        {"action.copy", "Copier" },
        {"action.paste", "Coller" },
	{"action.settings", "Param\u00e9trages..."},
        {"Set Source Path...", "Set Source Path..." },
        {"action.remove-from-diagram", "Retirer du Diagramme" },
        {"action.delete-from-model", "\u00c9liminer du Mod\u00e8le" },
        {"action.empty-trash", "Vider la Corbeille" },
        {"action.navigate-back", "Retourner en Arri\u00e8re" },
        {"action.navigate-forward", "Aller en Avant" },
        {"action.nav-config", "ConfigNavigation" },
        {"action.find", "Rechercher..." },
        {"action.goto-diagram", "Aller au Diagramme..." },
        {"action.next-editing-tab", "Onglet d'\u00c9dition Suivant" },
        {"Next Details Tab", "Onglet de D\u00e9tails Suivant" },
        {"action.buttons-on-selection", "Boutons sur la S\u00e9lection" },
        {"action.create-multiple", "Cr\u00e9er Multiple..." },
        {"action.add-top-level-package", "Ajouter un Paquetage de Haut Niveau" },
        {"action.class-diagram", "Diagramme de classe" },
        {"action.usecase-diagram", "Diagramme de cas d'utilisation" },
        {"action.state-diagram", "Diagramme d'\u00e9tat" },
        {"action.activity-diagram", "Diagramme d'activit\u00e9" },
        {"action.collaboration-diagram", "Diagramme de collaboration" },
        {"action.deployment-diagram", "Diagramme de d\u00e9ploiement" },
        {"action.sequence-diagram", "Diagramme de sequence" },
        {"button.add-attribute", "Ajouter un Attribut" },
        {"button.add-operation", "Ajouter une Op\u00e9ration" },
        {"Add Message", "Ajouter un Message" },
        {"Add Internal Transition", "Ajouter une Transition Interne" },
        {"action.generate-selected-classes", "G\u00e9n\u00e9rer les Classes S\u00e9lectionn\u00e9es..." },
        {"action.generate-all-classes", "G\u00e9n\u00e9rer toutes les Classes..." },
        {"Generate Code for Project", "Generate Code for Project..." },
        {"Settings for Generate for Project", "Settings for Generate for Project..." },
        {"action.toggle-auto-critique", "Basculer l'Auto-Critique" },
        {"action.design-issues", "Questions de Conception..." },
        {"action.design-goals", "Buts de Conception..." },
        {"action.browse-critics", "Parcourir les Critiques..." },
        {"action.toggle-flat-view", "Basculer la Vue \u00e0 Plat" },
        {"action.new-todo-item", "Nouvelle Rubrique \u00ab \u00c0 Faire \u00bb..." },
        {"action.resolve-item", "R\u00e9soudre la Rubrique..." },
        {"action.send-email-to-expert", "Envoyer un Courriel \u00e0 l'Expert..." },
        {"action.more-info", "Informations Suppl\u00e9mentaires..." },
        {"action.snooze-critic", "Suspendre la Critique" },
        {"action.about-argouml", "\u00c0 Propos d'ArgoUML..." },
        {"Properties", "Propri\u00e9t\u00e9s" },
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
        {"action.layout-automatic", "Automatique" },
        {"action.layout-incremental", "Incr\u00e9mental" },
        {"action.as-diagram", "En Tant que Diagramme" },
        {"action.as-table", "En Tant que Table" },
        {"action.as-metrics", "En Tant que M\u00e9triques" },
        {"ToDoItem", "RubriqueAFaire" },
        {"Javadocs", "Javadocs" },
        {"Source", "Source" },
        {"Constraints", "Contraintes" },
        {"tab.checklist", "Liste de contr\u00f4le" },
        {"tab.constraints", "Contraintes" },
        {"tab.documentation", "Documentation" },
        {"tab.history", "Historique" },
        {"tab.properties", "Propri\u00E9t\u00E9s" },
        {"tab.source", "Source" },
        {"tab.style", "Style" },
        {"tab.tagged-values", "\u00c9tiquettes" },
        {"tab.todo-item", "Element \u00e0 corriger" },
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
