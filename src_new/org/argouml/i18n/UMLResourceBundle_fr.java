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

/**
 *   This class is the default member of a resource bundle that
 *   provides strings for UML related PropPanels.
 *
 *   If there is not an explicit entry for a key, handleGetObject
 *   just returns the key.  This class should not be called directly
 *   but should be called through the PropPanel.localize() method.
 *
 *   @author Curt Arnold
 *   @since 0.9
 *   @see java.util.ResourceBundle
 *   @see UMLResourceBundle_de
 *   @see org.argouml.util.CheckResourceBundle
 */
public class UMLResourceBundle_fr extends ListResourceBundle {

    private static final Object[][] _contents = {
        { "abstract", "abstrait" },
        { "Abstract", "Abstrait" },  
        { "active", "actif" },
        { "Add actor", "Ajouter un acteur" },  
        { "Add association" , "Ajouter une association" },
        { "Add association end", "Ajouter une extr\u00e9mit\u00e9 d'association" },  
        { "Add attribute" , "Ajouter un attribut" },
        { "Add class", "Ajouter une classe" },  
        { "Add datatype" , "Ajouter un type de donn\u00e9e" },
        { "Add enumeration literal", "Ajouter un litt\u00e9ral d'enum\u00e9ration" },  
        { "Add generalization", "Ajouter g\u00e9n\u00e9ralisation" },  
        { "Add inner class" , "Ajouter une classe interne" },
        { "Add interface", "Ajouter une interface" },  
        { "Add_Menu_Actor" , "Acteur..." } ,
        { "Add_Menu_Class" , "Classe..." } ,
        { "Add_Menu_Datatype" , "Type de Donn\u00e9e..." } ,
        { "Add_Menu_Exception" , "Exception..." } ,
        { "Add_Menu_Interface" , "Interface..." } ,
        { "Add_Menu_Signal" , "Signal..." } ,
        { "Add_Menu_UseCase" , "Cas d'Utilisation..." },
        { "Add operation" , "Ajouter une operation" },
        { "Add parameter" , "Ajouter un param\u00e8tre" },
        { "Add realization", "Ajouter r\u00e9alisation" },  
        { "Add stereotype", "Ajouter un st\u00e9r\u00e9otype" },  
        { "Add subpackage", "Ajouter un sous-paquetage" },  
        { "Add use case" , "Ajouter un cas d'utilisation" },
        { "Association:" , "Association:" },
        { "Association Ends:", "Extr\u00e9mit\u00e9s d'Association:" },  
        { "Associations:" , "Associations:" },
        { "Attributes:" , "Attributs:" },

        { "Base Class:" , "Super-classe:" },

        { "changeable" , "modifiable" },
        { "Checklist", "Liste de contr\u00f4le" },         
        { "Class" , "Classe" },
        { "Components:" , "Composants:" },
        { "Connections:" , "Liens:" },
        { "Constraints", "Contraintes" },                  

        { "Delete actor" , "Supprimer l'acteur" },
        { "Delete association" , "Supprimer l'association" },
        { "Delete association end" , "Supprimer l'extr\u00e9mit\u00e9 de l'association" },
        { "Delete attribute" , "Supprimer l'attribut" },
        { "Delete class" , "Supprimer la classe" },
        { "Delete interface" , "Supprimer l'interface" },
        { "Delete operation" , "Supprimer l'op\u00e9ration" },
        { "Delete package" , "Supprimer le paquetage" },
        { "Delete parameter" , "Supprimer le param\u00e8tre" },
        { "Derived:" , "D\u00e9rivation:" },  // TBC, probably completely wrong
        { "Documentation", "Documentation" },              

        { "Expression:" , "Expression:" },
        { "Extends:" , "Enrichit:" },

        { "final", "final" },
        { "Final", "Final" },  

        { "Go back" , "Revenir en arri\u00e8re" },
        { "Go forward" , "Aller en avant" },
        { "Go up", "Remonter" },

        { "Implementations:", "Impl\u00e9mentations:" },  
        { "Implements:" , "Impl\u00e9mente:" },
        { "Incoming:" , "Entr\u00e9es:" },  // TBC, probably completely wrong

        { "Language:" , "Langage:" },
        { "Literals:" , "Lit\u00e9raux:" },

        { "Modifiers:", "Modificateurs:" },
        { "Multiplicity:" , "Multiplicit\u00e9:" },

        { "Name:" , "Nom:" },
        { "Namespace:" , "Espace de nommage:" },
        { "NavClass", "Aller \u00e0 la classe s\u00e9lectionn\u00e9e" },
        { "NavStereo", "Aller au st\u00e9r\u00e9otype s\u00e9lectionn\u00e9" },
        { "New actor" , "Nouvel acteur" },
        { "New association" , "Nouvelle association" },
        { "New attribute" , "Nouvel attribut" },
        { "New class" , "Nouvelle classe" },
        { "New data type" , "Nouveau type de donn\u00e9e" },
        { "New interface" , "Nouvelle interface" },
        { "New operation" , "Nouvelle op\u00e9ration" },
        { "New signal" , "Nouveau signal" },
        { "New stereotype", "Nouveau st\u00e9r\u00e9otype" },  
        { "none" , "aucun" },

        { "Operations:" , "Op\u00e9rations:" },
        { "Ordering:" , "Ordre:" },
        { "Outgoing:" , "Sorties:" }, // TBC, probably completely wrong
        { "Owned Elements", "\u00c9l\u00e9ments poss\u00e9d\u00e9s" },  
        { "Owned Elements:", "\u00c9l\u00e9ments poss\u00e9d\u00e9s:" },
        { "Owner:" , "Propri\u00e9taire:" },

        { "Properties", "Propri\u00e9t\u00e9s" },          
        { "public", "public" },
        { "Public", "Public" },  

        { "Receiver:" , "R\u00e9cepteur:" },
        { "Receives:" , "Re\u00e7oit:" },
        { "root", "racine" },
        { "Root", "Racine" },  

        { "Sends:" , "Envoie:" },
        { "sorted" , "tri\u00e9s" },
        { "Source", "Source" },                            
        { "Stereotype:", "St\u00e9r\u00e9otype:" },
        { "Style", "Style" },                              

        { "TaggedValues", "\u00c9tiquettes" },            
        { "ToDoItem", "\u00c9l\u00e9ment \u00e0 corriger" },
        { "Transition" , "Transition" },
        { "Trigger:" , "Trigger:" },
        { "Type:" , "Type:" },

        { "Visibility:" , "Visibilit\u00e9:" },
    };

    public Object[][] getContents() {
        return _contents;
    }


}
