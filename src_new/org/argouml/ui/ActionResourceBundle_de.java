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

package org.argouml.ui;
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
public class ActionResourceBundle_de extends ListResourceBundle {

  static final Object[][] _contents = {
    {"template.save_project.confirm_overwrite", "Sind Sie sicher, daß Sie {0} überschreiben wollen?"},
    {"template.save_project.status_writing", "Speichere {0}..."},
    {"template.save_project.status_wrote", "{0} wurde gespeichert."},
    {"template.save_project.file_not_found",
       "Beim Speichern ist ein Problem aufgetreten: \"{0}\".\n" +
       "Dies könnte Ihre Datei beschädigt haben."},
    {"template.save_project.io_exception",
       "Beim Speichern ist ein Problem aufgetreten: \"{0}\".\n" +
       "Dies könnte Ihre Datei beschädigt haben."},
    {"text.save_project.confirm_overwrite_title", "Überschreiben bestätigen"},
    {"text.save_project.file_not_found_title", "Problem beim Speichern"},
    {"text.save_project.io_exception_title", "Problem beim Speichern"},
    {"text.save_as_project.unstable_release",
       "Dies ist eine Entwicklerversion von ArgoUML. Sie ist nur für Testzwecke, \n" +
       "nicht für Produktionszwecke gedacht. Es ist möglich, Modelle zu speichern,\n" +
       "allerdings gibt es keine Garantie, daß diese von späteren Versionen noch gelesen\n" +
       "werden können.\n" +
       "Eine \"stabile\" Version finden Sie unter www.argouml.org. Danke."},
    {"text.save_as_project.unstable_release_title", "Warnung"},
    {"text.save_as_project.chooser_title", "Projekt speichern: "},
    {"template.new_project.save_changes_to", "Änderungen in {0} speichern?"},
    {"template.open_project.save_changes_to", "Änderungen in {0} speichern?"},
    {"text.open_project.chooser_title", "Projekt öffnen"},
    {"template.open_project.status_read", "{0} gelesen."},
    {"text.remove_from_model.will_remove_from_diagrams", "\nEs wird aus allen Diagrammen entfernt werden."},
    {"text.remove_from_model.will_remove_subdiagram", "\nSeine Unterdiagramme werden ebenfalls entfernt werden."},
    {"text.remove_from_model.anon_element_name", "dieses Element"},
    {"template.remove_from_model.confirm_delete", "Sind Sie sicher, daß Sie {0} entfernen wollen?{1}"},
    {"text.remove_from_model.confirm_delete_title", "Sind Sie sicher?"},
    {"template.exit.save_changes_to", "Änderungen in {0} speichern?"}
  };
  
  public Object[][] getContents() {
    return _contents;
  }
}