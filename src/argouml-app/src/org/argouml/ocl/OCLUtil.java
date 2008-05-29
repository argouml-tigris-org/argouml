// $Id$
// Copyright (c) 1996-2008 The Regents of the University of California. All
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

package org.argouml.ocl;

import java.util.List;

import org.argouml.model.Model;

/**
 * Helper methods for OCL support.
 *
 * @author Steffen Zschaler
 * @author Raphael Schmid
 */
public final class OCLUtil {

    /** OCLUtil shall not be instantiated! */
    private OCLUtil () { }

    /**
     * Get the inner-most enclosing namespace for the model element.
     *
     * @param me the modelelement
     * @return a namespace
     */
    public static Object getInnerMostEnclosingNamespace (Object me) {

        if (Model.getFacade().isAFeature(me)) {
            me = Model.getFacade().getOwner(me);
        }

        if (!Model.getFacade().isANamespace(me)) {
            throw new IllegalArgumentException();
        }

	return me;
    }

    
    /**
     * Return a context string for the given model element.
     *
     * @param me the model element for which to create a context string.
     *
     * @return the context string for the model element.
     */
    public static String getContextString (final Object me) {
    	  
      // Extension by Raphael Schmid

        String packageName = null;
        String className = null;
        String featureName = null;
        String returnTypeName = null;

        // Object selectedObject = TargetManager.getInstance().getTarget();
        Object selectedObject = me;
        if (Model.getFacade().isAClass(selectedObject)) {
            packageName = Model.getFacade().getName(
                    Model.getFacade().getNamespace(selectedObject));
            className = Model.getFacade().getName(selectedObject);
        }
        if (Model.getFacade().isAFeature(selectedObject)) {
            packageName = Model.getFacade().getName(
                    Model.getFacade().getNamespace(
                            Model.getFacade().getOwner(selectedObject)));
            className = Model.getFacade().getName(
                    Model.getFacade().getOwner(selectedObject));
            featureName = Model.getFacade().getName(selectedObject);

            if (Model.getFacade().isABehavioralFeature(selectedObject)) {
                List parameters = Model.getFacade().getParametersList(
                        selectedObject);
                for (Object p : parameters) {
                    if (Model.getFacade().hasReturnParameterDirectionKind(p)) {
                        returnTypeName = Model.getFacade().getName(
                                Model.getFacade().getType(p));
                        break;
                    }
                }
            }
        }
   	  
        String text = new String("package ");
        if (packageName != null) {
            text = text.concat(packageName);
        }
        text = text.concat("\n");

        text = text.concat("context ");
        if (className != null) {
            text = text.concat(className);
        }

        if (featureName != null) {
            text = text.concat("::");
            text = text.concat(featureName);

            if (returnTypeName != null) {
                text = text.concat("(): ");
                text = text.concat(returnTypeName);
            }
        }

        text = text.concat("\n");
        text = text.concat("\n");
        text = text.concat("\n");
        text = text.concat("endpackage");
        // LOG.debug(text);
        return text;

    }
  
}
