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

import java.io.PushbackReader;
import java.io.StringReader;
import java.util.Vector;

import org.apache.log4j.Logger;

import tudresden.ocl.parser.OclParser;
import tudresden.ocl.parser.lexer.Lexer;
import tudresden.ocl.parser.node.Start;

/**
 * Encapsulates actual interpretation of the OCL expressions for
 * OCL critics provided by a Profile
 *
 * @author maurelio1234
 */
public class OclInterpreter {

    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(OclInterpreter.class);

    /**
     * Parser OCL tree
     */
    private Start     tree   = null;

    /**
     * The model interpreter
     */
    private ModelInterpreter modelInterpreter;
    
    /**
     * Creates a new OCL interpreter for a given OCL expression
     * 
     * @param ocl expression
     * @param interpreter 
     */
    public OclInterpreter(String ocl, ModelInterpreter interpreter) {
        this.modelInterpreter = interpreter;
        
        Lexer lexer = new Lexer(new PushbackReader(new StringReader(ocl), 2));

        OclParser parser = new OclParser(lexer);

        try {
            tree = parser.parse();
        } catch (Exception e) {
            LOG.error("Exception", e);
        }
    }

    /**
     * Checks whether this OCL expression is applicable to a given
     * model element 
     * 
     * @param modelElement the ModelElement
     * @return if is applicable 
     */
    public boolean applicable(Object modelElement) {
        ContextApplicable ca = new ContextApplicable(modelElement);
        tree.apply(ca);
        return ca.isApplicable();
    }

    /**
     * Checks if this expression (invariant) is satisfied for
     * the given model element
     * 
     * @param modelElement the ModelElement
     * @return if is satisfied
     */
    public boolean check(Object modelElement) {
        EvaluateInvariant ei = new EvaluateInvariant(modelElement,
                modelInterpreter);
        tree.apply(ei);
        return ei.isOK();
    }

    /**
     * @return the triggers for this OCL
     */
    public Vector<String> getTriggers() {
        ComputeTriggers ct = new ComputeTriggers();
        tree.apply(ct);       
        return ct.getTriggers();
    }

}
