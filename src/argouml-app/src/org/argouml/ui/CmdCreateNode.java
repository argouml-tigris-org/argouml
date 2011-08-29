/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2011 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    tfmorris
 *    Michiel van der Wulp
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 1996-2007 The Regents of the University of California. All
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

package org.argouml.ui;

import javax.swing.Action;

import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.tigris.gef.base.CreateNodeAction;

/**
 * Command to create nodes with the appropriate modelelement. The modelelement
 * is initialized using the UmlFactory.buildNode method in the Model subsystem.
 *
 * @author jaap.branderhorst@xs4all.nl
 */
public class CmdCreateNode extends CreateNodeAction {
    
    private static final long serialVersionUID = 4813526025971574818L;

    /**
     * Constructor for CmdCreateNode.
     *
     * @param nodeType the type of model element for which to create a FigNode
     * @param name the i18n key used to generate the tooltip and icon.
     */
    public CmdCreateNode(Object nodeType, String name) {
        super(nodeType,
                Translator.localize(name),
                ResourceLoaderWrapper.lookupIconResource(
                        ResourceLoaderWrapper.getImageBinding(name)));
        putToolTip(name);
    }


    /**
     * Delegate creation of the node to the uml model subsystem.
     *
     * @return an object which represents a particular UML
     *         Element.
     *
     * @see org.tigris.gef.graph.GraphFactory#makeNode()
     * @see org.tigris.gef.base.CmdCreateNode#makeNode()
     */
    @Override
    public Object makeNode() {
        // TODO: We need to get the model/extent (and package?) associated with
        // the current diagram so that we can create the new element in the
        // right place.
        Object newNode = Model.getUmlFactory().buildNode(getArg("className"));
        return newNode;
    }

    /**
     * Adds tooltip text to the Action.
     *
     * @param name The key to localize as the name.
     */
    private void putToolTip(String name) {
        putValue(Action.SHORT_DESCRIPTION, Translator.localize(name));
    }
}
