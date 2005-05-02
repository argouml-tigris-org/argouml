// $Id$
// Copyright (c) 2005 The Regents of the University of California. All
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

/*REMOVE_BEGIN*/
package org.argouml.language.cpp.reveng;
/*REMOVE_END*/

import java.util.BitSet;

/**
 * This class provides support for the grammar indexing of parsed tokens.
 * 
 * @author euluis
 * @since 0.17.6
 */
public class CPPvariables {
	/**
     * Marks an invalid construct.
	 */
    public static final BitSet QI_INVALID = new BitSet(8);
    
    /**
     * Marks a type (includes enum, class, typedefs).
     */
    public static final BitSet QI_TYPE = new BitSet(8);
    
    /**
     * Marks a destructor. 
     */
    public static final BitSet QI_DTOR = new BitSet(8);
    
    /**
     * Marks a constructor.
     */
    public static final BitSet QI_CTOR = new BitSet(8);
    
    /**
     * Marks an operator.
     */
    public static final BitSet QI_OPERATOR = new BitSet(8);
    
    /**
     * Marks a pointer to member.
     */
    public static final BitSet QI_PTR_MEMBER = new BitSet(8);
    
    /**
     * Marks a variable.
     */
    public static final BitSet QI_VAR = new BitSet(8);
    
    /**
     * Marks a function.
     */
    public static final BitSet QI_FUN = new BitSet(8);
    
    /**
     * Marks a ID. Not a type, but could be a var, func...
     */
    public static final BitSet QI_ID = new BitSet(8);

    /**
     * Initialization of the above markers.
     */
    static {
        QI_TYPE.set(0);
        QI_DTOR.set(1);
        QI_CTOR.set(2);
        QI_OPERATOR.set(3);
        QI_PTR_MEMBER.set(4);
        QI_ID.set(5);
        QI_VAR.set(6);
        QI_FUN.set(7);
    }
    
    /**
     * Maximum template token scan depth.
     */
    public static final int MAX_TEMPLATE_TOKEN_SCAN = 200;
    
    /**
     * Type def string identifier.
     */
    public static final String OT_TYPE_DEF = "otTypeDef";
    
    /**
     * Struct string identifier.
     */
    public static final String OT_STRUCT = "otStruct";
    
    /**
     * Union string identifier.
     */
    public static final String OT_UNION = "otUnion";
    
    /**
     * Enum string identifier.
     */
    public static final String OT_ENUM = "otEnum";
    
    /**
     * Class string identifier.
     */
    public static final String OT_CLASS = "otClass";
    
}
