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


/** French Resource bundle for internationalization of Settings dialog
*
*  @author Luc Maisonobe
*  @since 0.9.4
*/
public class SettingsResourceBundle_fr extends ListResourceBundle {
   /*
    *  Notes for non-french maintainers who might get text and
    *  have to convert accented letters to unicode characters:
    *
    *  \u00e8 is e with grave accent (accent is from NW toward SE)
    *  \u00e9 is e with acute accent (accent is from NE toward SW)
    */

   static final Object[][] _contents = {
        {"button.apply", "Appliquer"},
        {"button.cancel", "Annuler"},
        {"button.ok", "OK"},

        {"caption.settings", "Param\u00e9trages"},

        {"label.edem", "Rendre compte des statistiques d'utilisation"},
        {"label.email", "Adresse \u00e9lectronique :"},
	{"label.fonts.big", "Grande taille" },
	{"label.fonts.huge", "Tr\u00E8s grande taille" },
	{"label.fonts.normal", "Taille normale" },
        {"label.preload", "Charger les classes communes"},
        {"label.profile", "Rendre compte des temps de chargement"},
        {"label.reload-recent", "Recharger au d\u00e9marrage le dernier projet enregistr\u00e9" }, 
        {"label.splash", "Afficher le panneau d'accueil"},
        {"label.startup-directory", "R\u00e9pertoire de d\u00e9marrage" }, 
        {"label.uml-notation-only", "n'utiliser que la notation UML" }, 
        {"label.use-guillemots", "utiliser des guillemets" }, 
        {"label.user", "Nom complet :"},

        {"tab.environment", "Environnement"},
	{"tab.fonts", "Polices" },
        {"tab.notation", "Notations"},
        {"tab.preferences", "Pr\u00e9f\u00e9rences"},
        {"tab.user", "Utilisateur"},
   };

     public Object[][] getContents() {
        return _contents;
     }
}
