// $Id$
// Copyright (c) 2005-2007 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

package org.argouml.model.mdr;

import java.util.Map;

import javax.jmi.reflect.RefObject;

import org.netbeans.api.xmi.XMIReferenceProvider;

/**
 * Custom reference provider for MDR XMI Writer.
 *
 * Returns our internal ID to be used when writing the xmi.id field
 *
 * @author Tom Morris
 */
class XmiReferenceProviderImpl implements XMIReferenceProvider {
    
    private Map<String, XmiReference> mofIdToXmiId;
    
    /**
     * Create a new reference provider which uses the given map for lookups.
     * 
     * @param idMap
     */
    XmiReferenceProviderImpl(Map<String, XmiReference> idMap) {
        mofIdToXmiId = idMap;
    }

    /*
     * @see org.netbeans.api.xmi.XMIReferenceProvider#getReference(javax.jmi.reflect.RefObject)
     */
    public XMIReferenceProvider.XMIReference getReference(RefObject object) {
        String mofId = object.refMofId();
        
        // Look for an existing reference matching our MofID
        XmiReference ref = mofIdToXmiId.get(mofId);

        // Anything not found is newly created, so return a null SystemID
        // indicating that it is in the parent document.  Otherwise return
        // the same reference that we read in originally.
        if (ref == null) {
            return new XMIReferenceProvider.XMIReference(null, mofId);
        } else {
            return new XMIReferenceProvider.XMIReference(ref.getSystemId(), 
                    ref.getXmiId());
        }
    }

}
