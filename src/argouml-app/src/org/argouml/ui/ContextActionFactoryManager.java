// $Id: ContextActionFactoryManager.java 16277 2008-12-07 20:36:40Z bobtarling $
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

package org.argouml.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import javax.swing.Action;
import javax.swing.JSeparator;

import org.argouml.ui.targetmanager.TargetManager;

/**
 * Manager of context meny factories registered in ArgoUML.
 * <p>
 * 
 * You can add and remove a ContextPopupFactory instance to this central
 * registry. The registered factories are then used when a new Context menu has
 * to be build.
 * 
 * The factory creates an additional popup on the context menu
 * 
 * @author EnDaRos
 */
public class ContextActionFactoryManager {
    private static List<ContextActionFactory> epfactories =
        new ArrayList<ContextActionFactory>();

    /**
     * @param factory
     *            add the given factory
     */
    public static void addContextPopupFactory(ContextActionFactory factory) {
	epfactories.add(0, factory);
    }

    /**
     * For modules, it would be useful to be able to remove their factories.
     * <p>
     * 
     * TODO: The effect of this method is not yet tested!
     * 
     * @param factory
     *            the factory to remove
     */
    public static void removeContextPopupFactory(ContextActionFactory factory) {
	epfactories.remove(factory);
    }

    /**
     * This method is not public since it is meant to be used exclusively by
     * TabProps.
     */
    static Collection<ContextActionFactory> getFactories() {
        return epfactories;
    }

    /**
     * Adds to the default actions the new menus for the registered factories
     * @return
     */
    public static List<Action> getContextPopupActions() {
	List<Action> allActionsFound = new ArrayList<Action>();
	Object element = TargetManager.getInstance().getSingleModelTarget();

	List tmpActionsContainer = null;
	for (ContextActionFactory factory : ContextActionFactoryManager
				.getFactories()) {
	    tmpActionsContainer = factory.createContextPopupActions(element);
	    if (tmpActionsContainer != null && tmpActionsContainer.size() > 0) {
		if (allActionsFound.size() > 0) {
    	    	    allActionsFound.add(null);
    	    	}
   	    	allActionsFound.addAll(tmpActionsContainer);
	    }
	}
	return allActionsFound;
    }
}
