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


package org.argouml.uml.ui;
import java.util.*;

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
 */
public class UMLResourceBundle extends ResourceBundle {

    private Hashtable _map = null;

    public UMLResourceBundle() {
        put("Add_Menu_Actor","Actor...");
        put("Add_Menu_Class","Class...");
        put("Add_Menu_Datatype","Datatype...");
        put("Add_Menu_Exception","Exception...");
        put("Add_Menu_Interface","Interface...");
        put("Add_Menu_Signal","Signal...");
        put("Add_Menu_UseCase","Use Case...");
    }

    public Enumeration getKeys() {
        Enumeration enum = null;
        if(_map != null) {
            enum = _map.elements();
        }
        return enum;
    }

    /**
     * Retrieves the corresponding object.  If no entry, just returns the key
     */
    protected Object handleGetObject(String key) throws java.util.MissingResourceException {
        Object object = key;
        if(_map != null) {
            object = _map.get(key);
            if(object == null) object = key;
        }
        return object;
    }

    public void put(String key,Object object) {
        if(_map == null) {
            _map = new Hashtable();
        }
        _map.put(key,object);
    }
}