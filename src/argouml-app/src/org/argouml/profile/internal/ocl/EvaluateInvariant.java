/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
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

import tudresden.ocl.parser.analysis.DepthFirstAdapter;
import tudresden.ocl.parser.node.AConstraint;
import tudresden.ocl.parser.node.PConstraintBody;

/**
 * Evaluates OCL invariants, this class should not depend on the model
 * subsystem.
 * 
 * @author maurelio1234
 */
public class EvaluateInvariant extends DepthFirstAdapter {

    /**
     * Is this invariant satified?
     */
    private boolean ok = true;

    /**
     * The Expression Evaluator
     */
    private EvaluateExpression expEvaluator = null;

    private Object modelElement;

    private ModelInterpreter mi;

    /**
     * Constructor
     * 
     * @param element self
     * @param interpreter model interpreter
     */
    public EvaluateInvariant(Object element, ModelInterpreter interpreter) {
        this.modelElement = element;
        this.mi = interpreter;
        this.expEvaluator = new EvaluateExpression(element, interpreter);
    }

    /**
     * @return is the invariant ok?
     */
    public boolean isOK() {
        return ok;
    }

    /** Interpreter Code * */

    /*
     * @see tudresden.ocl.parser.analysis.DepthFirstAdapter#caseAConstraint(tudresden.ocl.parser.node.AConstraint)
     */
    @Override
    public void caseAConstraint(AConstraint node) {
        inAConstraint(node);
        if (node.getContextDeclaration() != null) {
            node.getContextDeclaration().apply(this);
        }
        {
            boolean localOk = true;

            Object temp[] = node.getConstraintBody().toArray();
            for (int i = 0; i < temp.length; i++) {
                expEvaluator.reset(modelElement, mi);
                ((PConstraintBody) temp[i]).apply(expEvaluator);

                Object val = expEvaluator.getValue();
                localOk &= val != null && (val instanceof Boolean)
                        && (Boolean) val;
            }

            ok = localOk;
        }
        outAConstraint(node);
    }

}
