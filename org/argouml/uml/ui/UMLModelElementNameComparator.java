// $Id$
// Copyright (c) 1996-99 The Regents of the University of California. All
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
import ru.novosoft.uml.foundation.core.*;

/**
 * useful, but should be implemented as an inner class of the model that uses it.
 *
 * @deprecated as of ArgoUml 0.13.5 (10-may-2003),
 *             replaced by nothing?,
 *             this class is part of the 'old'(pre 0.13.*) implementation of proppanels
 *             that used reflection a lot.
 */
public class UMLModelElementNameComparator implements Comparator {

    /** Creates new BooleanChangeListener */
    public UMLModelElementNameComparator() {
    }
    
    public int compare(Object obj1, Object obj2) {
        String name1 = toString(obj1);
        String name2 = toString(obj2);
        int compare = name1.compareToIgnoreCase(name2);
        if (compare == 0) {
            compare = name1.compareTo(name2);
        }
        return compare;        
    }
    
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        return false;
    }
        
    private static final String toString(Object obj) {
        String name = null;
        if (obj instanceof MModelElement) {
            name = ((MModelElement) obj).getName();
        }
        if (name == null && obj != null) {
            name = obj.toString();
        }
        if (name == null) {
            name = "unnamed";
        }
        return name;
    }
            
        
}
