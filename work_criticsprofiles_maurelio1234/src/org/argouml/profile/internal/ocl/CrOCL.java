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

import java.util.Vector;

import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ToDoItem;
import org.argouml.uml.cognitive.UMLDecision;
import org.argouml.uml.cognitive.critics.CrUML;

/**
 * Represents an critics defined as an OCL expression in
 * a profile
 *
 * @author maurelio1234
 */
/**
 *
 * @author maas
 */
public class CrOCL extends CrUML {

    /**
     * Logger.
     */
    //private static final Logger LOG = Logger.getLogger(CrOCL.class);
    
    /**
     * the OCL Interpreter 
     */
    private OclInterpreter interpreter = null;

    /**
     * Creates a new OCL critic
     * 
     * @param ocl ocl expression
     */
    public CrOCL(String ocl) {
        interpreter = new OclInterpreter(ocl);
        
        addSupportedDecision(UMLDecision.PLANNED_EXTENSIONS);
        setPriority(ToDoItem.HIGH_PRIORITY);
        
        Vector<String> triggers = interpreter.getTriggers();
        
        for (String string : triggers) {
            addTrigger(string);            
        }
        
        super.setHeadline("OCL Expression");
        super.setDescription(ocl);
    }

    /**
     * Creates a new OCL critic
     * 
     * @param ocl ocl expression
     * @param headline headline
     */
    public CrOCL(String ocl, String headline) {
        this(ocl);
        super.setHeadline(headline);
    }       
    
    /**
     * @see org.argouml.uml.cognitive.critics.CrUML#predicate2(java.lang.Object,
     *      org.argouml.cognitive.Designer)
     */
    public boolean predicate2(Object dm, Designer dsgr) {
        if (!interpreter.applicable(dm)) {
            return NO_PROBLEM;
        } else {
            if (interpreter.check(dm)) {
                return NO_PROBLEM;
            } else {
                return PROBLEM_FOUND;
            }
        }
    }
    
}
