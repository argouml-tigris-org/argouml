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


/** This is the language specs for French.
 * This is hardcoded. This class should be
 * modified to use something similar to the about
 * box. Ideally this class would call on the
 * xml file for all the languages and allow the users
 * to specify which language through a GUI
 * dialog.
 */
public class ActionResourceBundle_fr extends ListResourceBundle {
  
  static final Object[][] _contents = {
    {"template.save_project.confirm_overwrite", "Êtes-vous sûr de vouloir écraser {0}?"},
    {"template.save_project.status_writing", "Écriture en cours {0}..."},
    {"template.save_project.status_wrote", "Écriture terminée {0}"},
    {"template.save_project.file_not_found",
       "Problème rencontré lors de la sauvegarde: \"{0}\".\n" +
       "Votre fichier est peut-être corrompu."},
    {"template.save_project.io_exception",
       "Problème rencontré lors de la sauvegarde: \"{0}\".\n" +
       "Votre fichier est peut-être corrompu."},
    {"text.save_project.confirm_overwrite_title", "Confirmez l'écrasement"},
    {"text.save_project.file_not_found_title", "Problème rencontré lors de la sauvegarde"},
    {"text.save_project.io_exception_title", "Problème rencontré lors de la sauvegarde"},
    {"text.save_as_project.unstable_release",
       "Ceci est une version de développement d'ArgoUML. Ne l'utilisez pas pour\n"
     + "de la production, elle n'est destinée qu'à faire des tests. Vous pouvez\n"
     + "sauvegarder vos modèles, mais il n'est pas sûr que des versions futures\n"
     + "d'ArgoUML pourront les relire. Si vous voulez utiliser une version « stable »,\n"
     + "consultez le site www.argouml.org et prenez en une là bas. Merci."},
    {"text.save_as_project.unstable_release_title", "Avertissement"},
    {"text.save_as_project.chooser_title", "Enregistrer le projet: "},
    {"template.new_project.save_changes_to", "Enregistrer les changements sous {0}?"},
    {"template.open_project.save_changes_to", "Enregistrer les changements sous {0}?"},
    {"text.open_project.chooser_title", "Ouvrir un projet"},
    {"template.open_project.status_read", "Lecture {0}."},
    {"text.remove_from_model.will_remove_from_diagrams", "\nCet élément sera éliminé de tous les diagrammes."},
    {"text.remove_from_model.will_remove_subdiagram", "\nSon sous-diagramme sera également éliminé."},
    {"text.remove_from_model.anon_element_name", "cet élément"},
    {"template.remove_from_model.confirm_delete", "Êtes-vous sûr de vouloir éliminer {0}?{1}"},
    {"text.remove_from_model.confirm_delete_title", "Êtes-vous sûr ?"},
    {"template.exit.save_changes_to", "enregistrez les changements sous {0}?"}
  };

  public Object[][] getContents() {
    return _contents;
  }
}

