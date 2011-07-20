/* $Id$
 *******************************************************************************
 * Copyright (c) 2010 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Michiel van der Wulp
 *******************************************************************************
 */

package org.argouml.notation;

/**
 * An interface to be implemented by Objects that wish to render a 
 * string-representation for an UML modelElement. <p>
 * 
 * The Object will be notified of model changes that cause 
 * a redraw of the string.<p>
 * 
 * These functions have the NotationProvider as a parameter, since 
 * the Object may have multiple NotationProviders, and may need 
 * to keep track which of their strings to refresh.
 *
 * @author mvw
 */
public interface NotationRenderer {

    /**
     * This function is called by the NotationProvider 
     * to notify the renderer (e.g. a Fig) of a change 
     * in the textual representation of the modelElement. <p>
     * 
     * The <code>np</code> argument is needed 
     * because one Fig might have 
     * multiple notationProviders (even of the same type).
     * 
     * @param np the notationProvider that noticed the change
     * @param rendering the new textual representation
     */
    public void notationRenderingChanged(NotationProvider np, 
            String rendering);
    
    /**
     * Getter for the NotationSettings that apply at the moment of this call.
     * <p>
     * The <code>np</code> argument is needed 
     * because one Fig might have a
     * different notationSetting per NotationProvider.
     * 
     * @param np the notationProvider that noticed the change
     * @return the current NotationSettings for this renderer
     */
    public NotationSettings getNotationSettings(NotationProvider np);
    
    /**
     * Getter for the UML object that forms the basis of this NotationProvider.
     * <p>
     * The <code>np</code> argument is needed 
     * because one Fig might have a
     * different owner per notationProvider.
     * 
     * @param np the notationProvider that noticed the change
     * @return the UML object shown to the NotationProvider at creation time.
     */
    public Object getOwner(NotationProvider np);
}
