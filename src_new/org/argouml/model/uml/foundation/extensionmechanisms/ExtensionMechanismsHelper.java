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

package org.argouml.model.uml.foundation.extensionmechanisms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.foundation.core.MNamespace;
import ru.novosoft.uml.foundation.extension_mechanisms.MStereotype;

/**
 * Helper class for UML Foundation::ExtensionMechanisms Package.
 *
 * Current implementation is a placeholder.
 * 
 * @since ARGO0.11.2
 * @author Thierry Lach
 */
public class ExtensionMechanismsHelper {

    /** Don't allow instantiation.
     */
    private ExtensionMechanismsHelper() {
    }
    
     /** Singleton instance.
     */
    private static ExtensionMechanismsHelper SINGLETON =
                   new ExtensionMechanismsHelper();

    
    /** Singleton instance access method.
     */
    public static ExtensionMechanismsHelper getHelper() {
        return SINGLETON;
    }
    
    /**
     * Returns all stereotypes in some namespace
     */    
    public Collection getStereotypes(MNamespace ns) {
    	Iterator it = ns.getOwnedElements().iterator();
    	List l = new ArrayList();
    	while (it.hasNext()) {
    		Object o = it.next();
    		if (o instanceof MStereotype) {
    			l.add(o);
    		}
    	}
    	return l;
    }
    
    /**
     * Finds a stereotype in some namespace. Returns null if no such stereotype is found.
     */
    public MStereotype getStereotype(MNamespace ns, MStereotype stereo) {
    	String name = stereo.getName();
    	String baseClass = stereo.getBaseClass();
    	Iterator it = getStereotypes(ns).iterator();
    	while (it.hasNext()) {
    		Object o = it.next();
    		if (o instanceof MStereotype && 
    			((MStereotype)o).getName().equals(name) &&
    			((MStereotype)o).getBaseClass().equals(baseClass)) {
    			return (MStereotype)o;
    		}
    	}
    	return null;
    }
    
    public String getMetaModelName(MModelElement m) {
        String name = m.getClass().getName();
        name = name.substring(name.lastIndexOf('.')+2,name.length());
        if (name.endsWith("Impl")) {
            name = name.substring(0,name.lastIndexOf("Impl"));
        }
        return name;
    }
    
    /**
     * Returns all possible stereotypes for some modelelement. Possible stereotypes 
     * are those stereotypes that are owned by the same namespace the modelelement
     * is owned by and that have a baseclass that is the same as the metamodelelement
     * name of the modelelement.
     * @param m
     * @return Collection
     */
    public Collection getAllPossibleStereotypes(MModelElement m) {
        List ret = new ArrayList();
        if (m == null || m.getNamespace() == null) return ret;
        MNamespace ns = m.getNamespace();
        Iterator it = getStereotypes(ns).iterator();
        String baseClass = getMetaModelName(m);
        while (it.hasNext()) {
            MStereotype stereo = (MStereotype)it.next();
            if (stereo.getBaseClass().equals(baseClass)) {
                ret.add(stereo);
            }
        }
        return ret;
    }
    
   
}

