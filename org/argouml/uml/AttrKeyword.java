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
import org.apache.log4j.Category;

import ru.novosoft.uml.foundation.core.MAttribute;
import ru.novosoft.uml.foundation.data_types.MChangeableKind;
import ru.novosoft.uml.foundation.data_types.MScopeKind;

/** This class  handles the
 * none
 * static
 * final
 * static final
 * transient
 * for the UML foundation data type
 */
public class AttrKeyword implements Serializable {
    protected static Category cat = Category.getInstance(AttrKeyword.class);
    
    public static final AttrKeyword NONE = new AttrKeyword("none");
    public static final AttrKeyword STATIC = new AttrKeyword("static");
    public static final AttrKeyword FINAL = new AttrKeyword("final");
    public static final AttrKeyword STATFIN = new AttrKeyword("static final");
    public static final AttrKeyword TRANS = new AttrKeyword("transient");


    public static final AttrKeyword[] POSSIBLES = {
	NONE, STATIC, FINAL, STATFIN, TRANS };

    protected String _label = null;
  
    private AttrKeyword(String label) { _label = label; }
  
    public static AttrKeyword KeywordFor(MAttribute attr) {
	MScopeKind sk = attr.getOwnerScope();
	MChangeableKind ck = attr.getChangeability();
	// TODO final?
	if (MScopeKind.CLASSIFIER.equals(sk)
	    && MChangeableKind.FROZEN.equals(ck))
	    return STATFIN;
	else if (MScopeKind.CLASSIFIER.equals(sk))
	    return STATIC;
	else if (MChangeableKind.FROZEN.equals(ck))
	    return FINAL;
	else
	    return NONE;
    }
  
    public boolean equals(Object o) {
	if (!(o instanceof AttrKeyword)) return false;
	String oLabel = ((AttrKeyword) o)._label;
	return _label.equals(oLabel);
    }

    public int hashCode() { return _label.hashCode(); }
  
    public String toString() { return _label.toString(); }

    public void set(MAttribute target) {
	//    MChangeableKind ck = MChangeableKind.NONE;
	MChangeableKind ck = null;
	MScopeKind sk = MScopeKind.INSTANCE;

	if (this == TRANS)
	    cat.info("TODO: transient not supported");
     
    
	if (this == FINAL || this == STATFIN) ck = MChangeableKind.FROZEN;
	if (this == STATIC || this == STATFIN) sk = MScopeKind.CLASSIFIER;
      
	target.setChangeability(ck);
	target.setOwnerScope(sk);
	// TODO: final
    }
} /* end class AttrKeyword */
