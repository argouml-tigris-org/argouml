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

package org.argouml.uml.diagram.static_structure;

import java.util.*;
import java.beans.*;

import ru.novosoft.uml.foundation.core.*;

public class MMClassKeyword implements java.io.Serializable {
    public static final MMClassKeyword NONE = new MMClassKeyword("none"); 
    public static final MMClassKeyword ABSTRACT =
	new MMClassKeyword("abstract"); 
    public static final MMClassKeyword FINAL = new MMClassKeyword("final");

    public static final MMClassKeyword[] POSSIBLES = {
	NONE, ABSTRACT, FINAL };

    protected String _label = null;
  
    private MMClassKeyword(String label) { _label = label; }
  
    public static MMClassKeyword KeywordFor(MClassifier cls) {
	if (cls.isLeaf()) return FINAL;
	if (cls.isAbstract()) return ABSTRACT;
	return NONE;
    }
  
    public boolean equals(Object o) {
	if (!(o instanceof MMClassKeyword)) return false;
	String oLabel = ((MMClassKeyword) o)._label;
	return _label.equals(oLabel);
    }

    public int hashCode() { return _label.hashCode(); }
  
    public String toString() { return _label.toString(); }

    public void set(MClassifier target) {
	target.setAbstract(this == ABSTRACT);
	target.setLeaf(this == FINAL);
    }
} /* end class MMClassKeyword */
