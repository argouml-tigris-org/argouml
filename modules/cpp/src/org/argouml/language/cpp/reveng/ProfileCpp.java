// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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
package org.argouml.language.cpp.reveng;

import java.util.HashSet;
import java.util.Set;

import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;

/**
 * The UML profile for C++. This might also take the form of an UML model, but,
 * for now it is this way.
 * 
 * TODO: should extend <code>org.argouml.uml.Profile</code>, but, I don't
 * really understand what is attempted there...
 * 
 * TODO: discuss with Daniele what to do about this problem. Both the generator
 * and the importer must use the same profile, if not we are going to make
 * future RTE very difficult. Also, the users of the module are going to be
 * confused. The main point in favor of this is that there is no open source UML
 * profile for C++. Lets be pioneers here ;-)
 * 
 * About the profile being a UML model: this class could be serializable into an
 * ArgoUML model, using its model subsystem. This would be a simple way to offer
 * the user the functionality of applying the C++ profile into a model he is
 * working on!
 * 
 * @author Luis Sergio Oliveira (euluis)
 * @since 0.19.3
 */
public class ProfileCpp {
    /**
     * Name of the C++ class specifier tagged value. Possible values are: class,
     * union, and struct. When this is ommited, class is used.
     * 
     * FIXME: Maybe we should have a more powerfull representation of the
     * TaggedValues in the profile. I would like that restrictions, default and
     * naming format to be enforced.
     */
    public static final String TV_NAME_CLASS_SPECIFIER = "cpp_class_specifier";

    /**
     * Name of the C++ reference tagged value which is also used in the
     * <code>GeneratorCpp</code>.
     */
    public static final String TV_NAME_REFERENCE = "reference";

    /**
     * Set of built in types tokens.
     */
    private static final Set BUILT_IN_TYPES;

    static {
        BUILT_IN_TYPES = new HashSet();
        BUILT_IN_TYPES.add("char");
        BUILT_IN_TYPES.add("wchar_t");
        BUILT_IN_TYPES.add("bool");
        BUILT_IN_TYPES.add("short");
        BUILT_IN_TYPES.add("int");
        BUILT_IN_TYPES.add("__int64");
        BUILT_IN_TYPES.add("__w64");
        BUILT_IN_TYPES.add("long");
        BUILT_IN_TYPES.add("signed");
        BUILT_IN_TYPES.add("unsigned");
        BUILT_IN_TYPES.add("float");
        BUILT_IN_TYPES.add("double");
        BUILT_IN_TYPES.add("void");
    }

    /**
     * Checks if the given type is a C++ builtin type.
     * 
     * @param typeName name of the type to check
     * @return true if typeName is a builtin type, false otherwise
     */
    public static boolean isBuiltIn(String typeName) {
        if (BUILT_IN_TYPES.contains(typeName.split(" ")[0])) {
            return true;
        }
        return false;
    }

    /**
     * Retrieves the given builtin type model element representation as a
     * DataType.
     * 
     * @param typeName name of the type
     * @return the model element that models the C++ builtin type
     */
    public static Object getBuiltIn(String typeName) {
        assert isBuiltIn(typeName) : "Must be a C++ built in!";
        Object builtinType = ProjectManager.getManager().getCurrentProject()
                .findType(typeName.toString(), false);
        if (builtinType == null) {
            builtinType = Model.getCoreFactory().buildDataType(typeName,
                getModel());
        }
        return builtinType;
    }

    /**
     * @return the model
     */
    private static Object getModel() {
        return ProjectManager.getManager().getCurrentProject().getModel();
    }
}