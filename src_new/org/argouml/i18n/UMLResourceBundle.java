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
public class UMLResourceBundle extends ListResourceBundle {

    private static final Object[][] _contents = {
        { "Add_Menu_Actor" , "Actor..." } ,
        { "Add_Menu_Class" , "Class..." } ,
        { "Add_Menu_Datatype" , "Datatype..." } ,
        { "Add_Menu_Exception" , "Exception..." } ,
        { "Add_Menu_Interface" , "Interface..." } ,
        { "Add_Menu_Signal" , "Signal..." } ,
        { "Add_Menu_UseCase" , "Use Case..." },
        { "Add association" , "Add association" },
        { "Add attribute" , "Add attribute" },
        { "Add datatype" , "Add datatype" },
        { "Add inner class" , "Add inner class" },
        { "Add operation" , "Add operation" },
        { "Add parameter" , "Add parameter" },
        { "Add use case" , "Add use case" },
        { "Association:" , "Association:" },
        { "Associations:" , "Associations:" },
        { "Attributes:" , "Attributes:" },
        { "Base Class:" , "Base Class:" },
        { "Class" , "Class" },
        { "changeable" , "changeable" },
        { "Components:" , "Components:" },
        { "Connections:" , "Connections:" },
        { "Delete actor" , "Delete actor" },
        { "Delete association" , "Delete association" },
        { "Delete association end" , "Delete association end" },
        { "Delete attribute" , "Delete attribute" },
        { "Delete class" , "Delete class" },
        { "Delete interface" , "Delete interface" },
        { "Delete operation" , "Delete operation" },
        { "Delete package" , "Delete package" },
        { "Delete parameter" , "Delete parameter" },
        { "Derived:" , "Derived:" },
        { "Expression:" , "Expression:" },
        { "Extends:" , "Extends:" },
        { "Go back" , "Go back" },
        { "Go forward" , "Go forward" },
        { "Implements:" , "Implements:" },
        { "Incoming:" , "Incoming:" },
        { "Language:" , "Language:" },
        { "Literals:" , "Literals:" },
        { "Multiplicity:" , "Multiplicity:" },
        { "Name:" , "Name:" },
        { "Namespace:" , "Namespace:" },
        { "New actor" , "New actor" },
        { "New association" , "New association" },
        { "New attribute" , "New attribute" },
        { "New class" , "New class" },
        { "New data type" , "New data type" },
        { "New interface" , "New interface" },
        { "New operation" , "New operation" },
        { "New signal" , "New signal" },
        { "none" , "none" },
        { "Operations:" , "Operations:" },
        { "Ordering:" , "Ordering:" },
        { "Outgoing:" , "Outgoing:" },
        { "Owner:" , "Owner:" },
        { "Receiver:" , "Receiver:" },
        { "Receives:" , "Receives:" },
        { "Sends:" , "Sends:" },
        { "sorted" , "sorted" },
        { "Transition" , "Transition" },
        { "Trigger:" , "Trigger:" },
        { "Type:" , "Type:" },
        { "Visibility:" , "Visibility:" },
        {"public", "public" },
        {"abstract", "abstract" },
        {"final", "final" },
        {"root", "root" },
        {"Stereotype:", "Stereotype:" },
        {"Modifiers:", "Modifiers:" },
        {"active", "active" },
        {"Owned Elements:", "Owned Elements:" },
        {"Go up", "Go up" },
        {"Add generalization", "Add generalization" },  
        {"Add realization", "Add realization" },  
        {"Owned Elements", "Owned Elements" },  
        {"Add class", "Add class" },  
        {"Add interface", "Add interface" },  
        {"Add stereotype", "Add stereotype" },  
        {"Add actor", "Add actor" },  
        {"Add subpackage", "Add subpackage" },  
        {"Abstract", "Abstract" },  
        {"Final", "Final" },  
        {"Root", "Root" },  
        {"Association Ends:", "Association Ends:" },  
        {"Add association end", "Add association end" },  
        {"Public", "Public" },  
        {"Implementations:", "Implementations:" },  
        {"New stereotype", "New stereotype" },  
        {"Add enumeration literal", "Add enumeration literal" },  
        {"NavStereo", "Go to selected stereotype" },
        {"NavClass", "Go to selected classifier" }
    };

    public Object[][] getContents() {
        return _contents;
    }


}
