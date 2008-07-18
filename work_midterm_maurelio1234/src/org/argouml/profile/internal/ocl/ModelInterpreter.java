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

import java.util.HashMap;

/**
 * Actually interprets the feature and operations in the model, in the sense
 * that the OclInterpreter is only responsible for interpreting the OCL
 * constructors
 * 
 * @author maurelio1234
 */
public interface ModelInterpreter {

    /**
     * Actually interprets the feature and operations in the model
     * 
     * @param vt the variable table
     * @param subject the subject (the object from which the feature is
     *            accessed)
     * @param feature the feature name (operation, attribute or collection
     *            operation)
     * @param type the type of feature ("." for operations and attributes and
     *            "->" for collection operations)
     * @param parameters the parameters for this invokation
     * @return the return value
     */
    Object invokeFeature(HashMap<String, Object> vt, Object subject,
            String feature, String type, Object[] parameters);

    /**
     * Looks for a built-in symbol. In the case that not all possible built-in
     * symbols are not in the variable table, the remaining ones should be
     * resolved using this method. This should be the case, e.g., for the names
     * of the UML metaclasses.
     * 
     * @param sym the symbol name
     * @return the symbol value, or null if the passed symbol is not built-in
     */
    Object getBuiltInSymbol(String sym);

}
