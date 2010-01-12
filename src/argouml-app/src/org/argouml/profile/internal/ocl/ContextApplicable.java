/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    euluis
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

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

import org.argouml.model.Model;

import tudresden.ocl.parser.analysis.DepthFirstAdapter;
import tudresden.ocl.parser.node.AClassifierContext;
import tudresden.ocl.parser.node.APostStereotype;
import tudresden.ocl.parser.node.APreStereotype;

/**
 * Checks the context clause of the OCL expression to verify if it is applicable
 * to the given model element.
 *
 * @author maurelio1234
 */
public class ContextApplicable extends DepthFirstAdapter {

    private boolean applicable = true;

    private Object modelElement;

    /**
     * Constructor.
     *
     * @param element the model element
     */
    public ContextApplicable(Object element) {
        this.modelElement = element;
    }

    /**
     * @return Returns the applicable.
     */
    public boolean isApplicable() {
        return applicable;
    }

    /**
     * @param node the node visited
     * @see tudresden.ocl.parser.analysis.DepthFirstAdapter#caseAClassifierContext(tudresden.ocl.parser.node.AClassifierContext)
     */
    @Override
    public void caseAClassifierContext(AClassifierContext node) {
        String metaclass = ("" + node.getPathTypeName()).trim();
        applicable &= Model.getFacade().isA(metaclass, modelElement);
    }

    /**
     * @param node the node visited
     * @see tudresden.ocl.parser.analysis.DepthFirstAdapter#inAPreStereotype(tudresden.ocl.parser.node.APreStereotype)
     */
    public void inAPreStereotype(APreStereotype node) {
        applicable = false;
    }

    /**
     * @param node the node visited
     * @see tudresden.ocl.parser.analysis.DepthFirstAdapter#inAPostStereotype(tudresden.ocl.parser.node.APostStereotype)
     */
    public void inAPostStereotype(APostStereotype node) {
        applicable = false;
    }

}
