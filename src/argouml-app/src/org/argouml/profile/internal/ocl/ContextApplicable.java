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

import org.apache.log4j.Logger;
import org.argouml.model.Facade;
import org.argouml.model.Model;

import tudresden.ocl.parser.analysis.DepthFirstAdapter;
import tudresden.ocl.parser.node.AClassifierContext;
import tudresden.ocl.parser.node.APostStereotype;
import tudresden.ocl.parser.node.APreStereotype;

/**
 * Checks the context clause of the OCL expression to verify if it
 * is applicable to the given model element.
 * 
 * @author maurelio1234
 */
public class ContextApplicable extends DepthFirstAdapter {

    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(ContextApplicable.class);

    private boolean applicable = true;

    private Object modelElement;
    
    /**
     * Constructos
     * 
     * @param modelElement the element
     */
    public ContextApplicable(Object modelElement) {
        this.modelElement = modelElement;
    }

    /**
     * @return Returns the applicable.
     */
    public boolean isApplicable() {
        return applicable;
    }

    /**
     * @param node
     * @see tudresden.ocl.parser.analysis.DepthFirstAdapter#caseAClassifierContext(tudresden.ocl.parser.node.AClassifierContext)
     */
    public void caseAClassifierContext(AClassifierContext node) {
        String metaclass = ("" + node.getPathTypeName()).trim();

        try {
            Method m = Facade.class.getDeclaredMethod("isA" + metaclass,
                    new Class[] { Object.class });
            if (m != null) {
                applicable &= (Boolean) m.invoke(Model.getFacade(),
                        new Object[] { modelElement });
            }
        } catch (Exception e) {
            LOG.error("Exception", e);
        }

    }
    
    /**
     * @param node
     * @see tudresden.ocl.parser.analysis.DepthFirstAdapter#inAPreStereotype(tudresden.ocl.parser.node.APreStereotype)
     */
    public void inAPreStereotype(APreStereotype node)
    {
        applicable = false;
    }
    

    /**
     * @param node
     * @see tudresden.ocl.parser.analysis.DepthFirstAdapter#inAPostStereotype(tudresden.ocl.parser.node.APostStereotype)
     */
    public void inAPostStereotype(APostStereotype node)
    {
        applicable = false;
    }
    
}
