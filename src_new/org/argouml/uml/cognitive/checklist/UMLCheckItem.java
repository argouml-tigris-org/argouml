// $Id$
// Copyright (c) 1996-2001 The Regents of the University of California. All
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

package org.argouml.uml.cognitive.checklist;

import java.util.*;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;

import org.tigris.gef.util.*;

import org.apache.log4j.Category;
import org.argouml.cognitive.checklist.*;
//
//  slightly different from its GEF counterpart
//
import org.argouml.ocl.OCLEvaluator;

/** A special kind of CheckItem that can replace OCL expressions with
 *  their values in the generated advice.
 *
 * @see org.argouml.ocl.OCLEvaluator */

public class UMLCheckItem extends CheckItem {
    protected static Category cat = 
        Category.getInstance(UMLCheckItem.class);

    public UMLCheckItem(String c, String d) { super(c, d); }

    public UMLCheckItem(String c, String d, String m, Predicate p) {
	super(c, d, m, p);
    }


    public String expand(String res, Object dm) {
	int searchPos = 0;
	int matchPos = res.indexOf(OCLEvaluator.OCL_START, searchPos);

	// replace all occurances of OFFENDER with the name of the
	// first offender
	while (matchPos != -1) {
	    int endExpr = res.indexOf(OCLEvaluator.OCL_END, matchPos + 1);
	    String expr =
		res.substring(matchPos + OCLEvaluator.OCL_START.length(),
			      endExpr);
	    String evalStr = OCLEvaluator.SINGLETON.evalToString(dm, expr);
	    cat.debug("expr='" + expr + "' = '" + evalStr + "'");
	    res = res.substring(0, matchPos) +
		evalStr +
		res.substring(endExpr + OCLEvaluator.OCL_END.length());
	    searchPos = endExpr + 1;
	    matchPos = res.indexOf(OCLEvaluator.OCL_START, searchPos);
	}
	return res;
    }

} /* end class UMLCheckItem */
