// $Id: OCLUtil.java 10734 2006-06-11 15:43:58Z mvw $
// Copyright (c) 1996-2006 The Regents of the University of California. All
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

import java.util.Collection;
import java.util.Iterator;

import org.argouml.model.Model;

/**
 * Helper methods for OCL support.
 *
 * @author Steffen Zschaler
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
	if (me == null || !(Model.getFacade().isAModelElement(me)))
	    return "";
	Object mnsContext =
	    getInnerMostEnclosingNamespace (me);

	if (Model.getFacade().isABehavioralFeature(me)) {
	    StringBuffer sbContext = new StringBuffer ("context ");
	    sbContext.append (Model.getFacade().getName(mnsContext));
	    sbContext.append ("::");
	    sbContext.append (Model.getFacade().getName(me));
	    sbContext.append (" (");

	    Collection lParams = Model.getFacade().getParameters(me);
	    String sReturnType = null;
	    boolean fFirstParam = true;

	    for (Iterator i = lParams.iterator(); i.hasNext();) {
		Object mp = i.next(); //MParameter

		if (Model.getFacade().isReturn(mp)) {
		    sReturnType = Model.getFacade().getName(
                                    Model.getFacade().getType(mp));
                } else {
		    if (fFirstParam) {
			fFirstParam = false;
		    } else {
			sbContext.append ("; ");
		    }

		    sbContext.append(
		            Model.getFacade().getType(mp)).append(": ");
		    sbContext.append(Model.getFacade().getName(
		            Model.getFacade().getType(mp)));
		}
	    }

	    sbContext.append (")");

	    // The ocl toolkit does not like void return types
	    if (sReturnType != null && !sReturnType.equalsIgnoreCase("void")) {
		sbContext.append (": ").append (sReturnType);
	    }

	    return sbContext.toString();
	} else {
	    return "context " + Model.getFacade().getName(mnsContext);
	}
    }
}
