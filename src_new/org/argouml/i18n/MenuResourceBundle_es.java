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

/**
 *
 * Provides strings for spanish localization of the interface.
 *
 * See source of UMLCognitiveResourceBundle_es for some notes about
 *
 * the translation.
 *
 *
 *
 * This is hardcoded. This class should be
 * modified to use something similar to the about
 * box. Ideally this class would call on the
 * xml file for all the languages and allow the users
 * to specify which language through a GUI
 * dialog.
 * @author Curt Arnold, Alejandro Ramirez
 * @since 0.9
 * @see java.util.ResourceBundle
 * @see org.argouml.uml.cognitive.UMLCognitiveResourceBundle_es
 */
public class MenuResourceBundle_es extends ListResourceBundle {

   static final Object[][] _contents = {
        {"action.new", "Nuevo" },
        {"action.open-project", "Abrir proyecto..." },
        {"action.save-project", "Guardar proyecto" },
        {"action.save-project-as", "Guardar proyecto como..." },
        {"action.import-sources", "Importar fuentes..." },
        {"action.import", "Importar" },
        {"action.print", "Imprimir..." },
        {"action.save-graphics", "Guardar gr\u00e1ficos..." },
        {"action.save-configuration", "Guardar configuracion"},
        {"action.exit", "Salir" },
        {"action.undo", "Deshacer" },
        {"action.redo", "Rehacer" },
        {"action.cut", "Cortar" },
        {"action.copy", "Copiar" },
        {"action.paste", "Pegar" },
        {"action.settings", "Propiedades..."},
        {"action.set-source-path", "Set Source Path..." },
        {"action.remove-from-diagram", "Quitar del diagrama" },
        {"action.delete-from-model", "Borrar del modelo" },
        {"action.empty-trash", "Eliminar basura" },
        {"action.navigate-back", "Navegar atras" },
        {"action.navigate-forward", "Navegar adelante" },
        {"action.nav-config", "NavConfig" },
        {"action.find", "Buscar..." },
        {"action.goto-diagram", "Ir a diagrama..." },
        {"action.next-editing-tab", "Siguiente solapa de edici\u00f3n" },
        {"action.next-details-tab", "Siguiente solapa de detalle" },
        {"action.buttons-on-selection", "Botones en la selecci\u00f3n" },
        {"action.create-multiple", "Crear multiple..." },
        {"action.add-top-level-package", "A\u00f1adir paquete de nivel superior" },
        {"action.class-diagram", "Diagrama de clase" },
        {"action.usecase-diagram", "Diagrama de casos" },
        {"action.state-diagram", "Diagrama de estado" },
        {"action.activity-diagram", "Diagrama de actividades" },
        {"action.collaboration-diagram", "Diagrama de colaboracion" },
        {"action.deployment-diagram", "Diagrama de Instalaci\u00f3n" },
        {"action.sequence-diagram", "Diagrama de secuencia" },
        {"action.add-message", "A\u00f1adir mensaje" },
        {"action.add-internal-transition", "A\u00f1adir transici\u00f3n interna" },
        {"action.generate-selected-classes", "Generar clases seleccionadas..." },
        {"action.generate-all-classes", "Generar todas las clases..." },
        {"action.generate-code-for-project", "Generate Code for Project..." },
        {"action.settings-for-project-code-generation", "Settings for Generate for Project..." },
        {"action.toggle-auto-critique", "(Des)activar Auto-Criticos" },
        {"action.design-issues", "Temas de dise\u00f1o..." },
        {"action.design-goals", "Objetivos de dise\u00f1o..." },
        {"action.browse-critics", "Ojear criticos..." },
        {"action.toggle-flat-view", "(Des)activar vista plana" },
        {"action.new-todo-item", "Nueva tarea pendiente..." },
        {"action.resolve-item", "Resolver tarea..." },
        {"action.send-email-to-expert", "Enviar email al experto..." },
        {"action.more-info", "M\u00e1s informaci\u00f3n..." },
        {"action.snooze-critic", "Dormir critico" },
        {"action.about-argouml", "Acerca de ArgoUML..." },
        {"action.properties", "Propiedades" },
        {"1", "1" },
        {"0..1", "0..1" },
        {"0..*", "0..*" },
        {"1..*", "1..*" },
        {"aggregate", "agregaci\u00f3n" },
        {"composite", "composici\u00f3n" },
        {"none", "ninguno" },
        {"Show Attribute Compartment", "Muestra compartimento atributo" },
        {"Hide Attribute Compartment", "Oculta compartimento atributo" },
        {"Show Operation Compartment", "Muestra compartimento operaci\u00f3n" },
        {"Hide Operation Compartment", "Oculta compartimento operaci\u00f3n" },
        {"Show All Compartments", "Muestra ambos compartimentos" },
        {"Hide All Compartments", "Oculta ambos compartimentos" },
        {"File", "Archivo" },
        {"Mnemonic_File", "F" },
        {"Mnemonic_New", "N" },
        {"Mnemonic_Open", "O" },
        {"Mnemonic_Save", "S" },
        {"Mnemonic_SaveAs", "A" },
        {"Mnemonic_Print", "P" },
        {"Mnemonic_SaveGraphics", "G" },
        {"Mnemonic_Exit", "X" },
        {"Edit", "Editar" },
        {"Mnemonic_Edit", "E" },
        {"Select", "Seleccionar" },
        {"Mnemonic_Cut", "X" },
        {"Mnemonic_Copy", "C" },
        {"Mnemonic_Paste", "V" },
        {"Mnemonic_RemoveFromDiagram", "R" },
        {"Mnemonic_DeleteFromModel", "D" },
        {"View", "Ver" },
        {"Mnemonic_View", "V" },
        {"Editor Tabs", "Solapas de edici\u00f3n" },
        {"Details Tabs", "Solapas de detalle" },
        {"Create", "Crear" },
        {"Mnemonic_Create", "C" },
        {"Diagrams", "Diagramas" },
        {"Create Diagram", "Crear diagrama" },
        {"Arrange", "Arreglar" },
        {"Mnemonic_Arrange", "A" },
        {"Align", "Alinear" },
        {"Distribute", "Distribuir" },
        {"Reorder", "Reordenar" },
        {"Nudge", "Mover" },
        {"Generation", "Generaci\u00f3n" },
        {"Mnemonic_Generate", "G" },
        {"Critique", "Critico" },
        {"Mnemonic_Critique", "R" },
        {"Help", "Ayuda" },
        {"Mnemonic_Help", "H" },
        {"action.layout-automatic", "Automatico" },
        {"action.as-diagram", "Como diagrama" },
        {"action.as-table", "Como tabla" },
        {"action.as-metrics", "Como m\u00e9trica" },
        {"ToDoItem", "Tarea pendiente" },
        {"Javadocs", "Javadocs" },
        {"Source", "C\u00f3digo fuente" },
        {"Constraints", "Restricciones" },
        {"TaggedValues", "Valores etiquetados" },
        {"Checklist", "Lista de comprobaci\u00f3n" },
        {"History", "Historia" },

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

