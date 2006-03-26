// $Id$
// Copyright (c) 2005 The Regents of the University of California. All
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

package org.argouml.uml.notation;

import java.util.HashMap;
import java.util.Map;

import org.argouml.notation.NotationProvider4;

/**
 * @author mvw@tigris.org
 */
public abstract class ValueHandler implements NotationProvider4 {

    /**
     * The register of remembered values.
     *
     * String key => Object values.
     */
    private Map keyTable;

    /**
     * @see org.argouml.notation.NotationProvider4#putValue(java.lang.String, java.lang.Object)
     */
    public void putValue(String key, Object newValue) {
        if (keyTable == null) {
            keyTable = new HashMap();
        }
        // Remove the entry for key if newValue is null
        // else put in the newValue for key.
        if (newValue == null) {
            keyTable.remove(key);
        } else {
            keyTable.put(key, newValue);
        }

    }

    /**
     * Gets the <code>Object</code> associated with the specified key.
     *
     * @param key a string containing the specified <code>key</code>
     * @return the binding <code>Object</code> stored with this key; if there
     *          are no keys, it will return <code>null</code>
     * @see javax.swing.Action#getValue(String)
     */
    public Object getValue(String key) {
        if (keyTable == null) {
            return null;
        }
        return keyTable.get(key);
    }

    /**
     * @see org.argouml.notation.NotationProvider4#putValue(java.lang.String, boolean)
     */
    public void putValue(String key, boolean newValue) {
        Boolean b = new Boolean(newValue);
        putValue(key, b);
    }

    /**
     * @see org.argouml.notation.NotationProvider4#isValue(java.lang.String)
     */
    public boolean isValue(String key) {
        Object o = this.getValue(key);
        if (!(o instanceof Boolean)) {
            return false;
        }
        return ((Boolean) o).booleanValue();
    }

}
