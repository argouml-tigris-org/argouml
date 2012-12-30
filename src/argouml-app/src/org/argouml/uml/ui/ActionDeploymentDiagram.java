/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    tfmorris
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 1996-2007 The Regents of the University of California. All
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

import java.util.logging.Level;
import java.util.logging.Logger;

import org.argouml.model.Model;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.DiagramFactory;

/**
 * Action to trigger creation of a deployment diagram.
 */
public class ActionDeploymentDiagram extends ActionAddDiagram {

    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(ActionDeploymentDiagram.class.getName());

    /**
     * Constructor.
     */
    public ActionDeploymentDiagram() {
        super("action.deployment-diagram");
    }

    /*
     * @see org.argouml.uml.ui.ActionAddDiagram#createDiagram(Object)
     */
    public ArgoDiagram createDiagram(Object namespace) {
        // a deployment diagram shows something about the whole model
        // according to the UML spec, but we rely on the caller to enforce
        // that if desired.
        if (!Model.getFacade().isANamespace(namespace)) {
            LOG.log(Level.SEVERE, "No namespace as argument {0}",namespace);
            
            throw new IllegalArgumentException(
					       "The argument " + namespace
					       + "is not a namespace.");
        }
        return DiagramFactory.getInstance().createDiagram(
                DiagramFactory.DiagramType.Deployment,
                namespace,
                null);
    }

    /*
     * @see org.argouml.uml.ui.ActionAddDiagram#isValidNamespace(Object)
     */
    public boolean isValidNamespace(Object namespace) {
        // a deployment diagram shows something about the whole model
        // according to the uml spec
        if (!Model.getFacade().isANamespace(namespace)) {
            LOG.log(Level.SEVERE, "No namespace as argument {0}",namespace);
            
            throw new IllegalArgumentException("The argument " + namespace
					       + "is not a namespace.");
        }
        // may only occur as child of the model or in a package
        if (Model.getFacade().isAPackage(namespace)) {
            return true;
        }
        return false;
    }

    /*
     * For a deployment diagram, not just any
     * namespace will do - we need a package.
     */
    @Override
    protected Object findNamespace() {
        Object ns = super.findNamespace();
        if (ns == null) {
            return ns;
        }
        if (!Model.getFacade().isANamespace(ns)) {
            return ns;
        }
        while (!Model.getFacade().isAPackage(ns)) {
            // ns is a namespace, but not a package
            Object candidate = Model.getFacade().getNamespace(ns);
            if (!Model.getFacade().isANamespace(candidate)) {
                return null;
            }
            ns = candidate;
        }
        return ns;
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = 9027235104963895167L;
}
