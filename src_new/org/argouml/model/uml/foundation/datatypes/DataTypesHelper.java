// $Id$
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

package org.argouml.model.uml.foundation.datatypes;

import java.util.Iterator;

import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.foundation.extension_mechanisms.MTaggedValue;
import ru.novosoft.uml.foundation.data_types.MPseudostateKind;

/**
 * Helper class for UML Foundation::DataTypes Package.
 *
 * Current implementation is a placeholder.
 * 
 * @since ARGO0.11.2
 * @author Thierry Lach
 * @stereotype singleton
 */
public class DataTypesHelper {

    /** Don't allow instantiation.
     */
    private DataTypesHelper() {
    }
    
     /** Singleton instance.
     */
    private static DataTypesHelper SINGLETON =
                   new DataTypesHelper();

    
    /** Singleton instance access method.
     */
    public static DataTypesHelper getHelper() {
        return SINGLETON;
    }

    public void copyTaggedValues(MModelElement from, MModelElement to) {
	Iterator it = from.getTaggedValues().iterator();
	while (it.hasNext()) {
	    MTaggedValue tv = (MTaggedValue) it.next();
	    to.setTaggedValue(tv.getTag(), tv.getValue());
	}
    }
    
    public boolean equalsINITIALKind(Object kind){
        
        if(!(kind instanceof MPseudostateKind)){
            throw new IllegalArgumentException();
        }
        
        if (MPseudostateKind.INITIAL.equals(kind))
            return true;
        else
            return false;
    }
    
    public boolean equalsDEEP_HISTORYKind(Object kind){
        
        if(!(kind instanceof MPseudostateKind)){
            throw new IllegalArgumentException();
        }
        
        if (MPseudostateKind.DEEP_HISTORY.equals(kind))
            return true;
        else
            return false;
    }
    
    public boolean equalsSHALLOW_HISTORYKind(Object kind){
        
        if(!(kind instanceof MPseudostateKind)){
            throw new IllegalArgumentException();
        }
        
        if (MPseudostateKind.SHALLOW_HISTORY.equals(kind))
            return true;
        else
            return false;
    }
    
    public boolean equalsFORKKind(Object kind){
        
        if(!(kind instanceof MPseudostateKind)){
            throw new IllegalArgumentException();
        }
        
        if (MPseudostateKind.FORK.equals(kind))
            return true;
        else
            return false;
    }
    
    public boolean equalsJOINKind(Object kind){
        
        if(!(kind instanceof MPseudostateKind)){
            throw new IllegalArgumentException();
        }
        
        if (MPseudostateKind.JOIN.equals(kind))
            return true;
        else
            return false;
    }
    
    public boolean equalsBRANCHKind(Object kind){
        
        if(!(kind instanceof MPseudostateKind)){
            throw new IllegalArgumentException();
        }
        
        if (MPseudostateKind.BRANCH.equals(kind))
            return true;
        else
            return false;
    }
}

