// $Id$
// Copyright (c) 1996-99 The Regents of the University of California. All
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

package org.argouml.uml;

import java.io.Serializable;
import org.argouml.model.ModelFacade;

/**
 * @deprecated this class is deprecated since 0.15.1 and should be removed
 *             in 0.15.2, due to the fact that the TableModel classes
 *             have never been used, and are not maintained,
 *
 *             There is no reason why someone cannot try to complete the
 *             TableModel implementation, however, a higher priority
 *             at the moment is to clean argouml of un maintained code.
 */
public class OperKeyword implements Serializable {
    public static final OperKeyword NONE = new OperKeyword("none");
    public static final OperKeyword STATIC = new OperKeyword("static");
    public static final OperKeyword FINAL = new OperKeyword("final");
    public static final OperKeyword STATFIN = new OperKeyword("static final");
    public static final OperKeyword SYNC = new OperKeyword("synchronized");
    public static final OperKeyword STSYNC = new OperKeyword("static sync"); 
    public static final OperKeyword FINSYNC = new OperKeyword("final sync"); 
    public static final OperKeyword SFSYNC =
	new OperKeyword("static final sync");


    public static final OperKeyword[] POSSIBLES = {
	NONE, STATIC, FINAL, STATFIN, SYNC, STSYNC, FINSYNC, SFSYNC };

    protected String _label = null;
  
    private OperKeyword(String label) { _label = label; }
  
    public static OperKeyword KeywordFor(Object/*MOperation*/ op) {
	Object/*MScopeKind*/ sk = ModelFacade.getOwnerScope(op);
	Object/*MCallConcurrencyKind*/ ck = ModelFacade.getConcurrency(op);
	// TODO final?
	if (ModelFacade.CONCURRENT_CONCURRENCYKIND.equals(ck)) {
	    if (ModelFacade.CLASSIFIER_SCOPEKIND.equals(ck)) return STATIC;
	    return NONE;
	}
	else {
	    if (ModelFacade.CLASSIFIER_SCOPEKIND.equals(ck)) return STSYNC;
	    return SYNC;
	}
    }
  
    public boolean equals(Object o) {
	if (!(o instanceof OperKeyword)) return false;
	String oLabel = ((OperKeyword) o)._label;
	return _label.equals(oLabel);
    }

    public int hashCode() { return _label.hashCode(); }
  
    public String toString() { return _label.toString(); }

    public void set(Object/*MOperation*/ target) {
	Object/*MCallConcurrencyKind*/ ck = ModelFacade.CONCURRENT_CONCURRENCYKIND;
	if (this == SYNC || this == STSYNC || this == FINSYNC ||
	    this == SFSYNC)
	    ck = ModelFacade.GUARDED_CONCURRENCYKIND;
    
	Object/*MScopeKind*/ sk = ModelFacade.INSTANCE_SCOPEKIND;
	if (this == STATIC || this == STATFIN || this == STSYNC ||
	    this == SFSYNC)
	    sk = ModelFacade.CLASSIFIER_SCOPEKIND;
	//TODO: final
      
	ModelFacade.setConcurrency(target, ck);
	ModelFacade.setOwnerScope(target, sk);
	// TODO: final
    }
} /* end class OperKeyword */
