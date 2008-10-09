// $Id: eclipse-argo-codetemplates.xml 11347 2006-10-26 22:37:44Z linus $
// Copyright (c) 2008 The Regents of the University of California. All
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

package org.argouml.profile.internal.ocl;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.argouml.model.MetaTypes;
import org.argouml.model.Model;

import tudresden.ocl.parser.analysis.DepthFirstAdapter;
import tudresden.ocl.parser.node.AClassifierContext;

/**
 * Check the design materials related to this OCL
 * 
 * @author maurelio1234
 */
public class ComputeDesignMaterials extends DepthFirstAdapter {

    /**
     * Logger.
     */
    private static final Logger LOG = 
        Logger.getLogger(ComputeDesignMaterials.class);

    private Set<Object> dms = new HashSet<Object>();

    /*
     * @see tudresden.ocl.parser.analysis.DepthFirstAdapter#caseAClassifierContext(tudresden.ocl.parser.node.AClassifierContext)
     */
    public void caseAClassifierContext(AClassifierContext node) {
        String str = ("" + node.getPathTypeName()).trim();
        
        if (str.equals("Class")) {
            dms.add(Model.getMetaTypes().getUMLClass());            
        } else {
            try {
                Method m = MetaTypes.class.getDeclaredMethod("get" + str,
                        new Class[0]);
                if (m != null) {
                    dms.add(m.invoke(Model.getMetaTypes(), new Object[0]));
                }
            } catch (Exception e) {
                LOG.error("Metaclass not found: " + str, e);
            }
        }
    }

    /**
     * @return the criticized design materials
     */
    public Set<Object> getCriticizedDesignMaterials() {
        return dms;
    }

}
