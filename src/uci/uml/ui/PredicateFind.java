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

package uci.uml.ui;

import uci.util.Predicate;
import uci.util.PredicateTrue;
import uci.gef.Diagram;
import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Model_Management.*;

public class PredicateFind implements Predicate {

  ////////////////////////////////////////////////////////////////
  // instance variables
  Predicate _elementName;
  Predicate _packageName;
  Predicate _diagramName;
  Predicate _type;
  Predicate _specific = PredicateTrue.theInstance();

  ////////////////////////////////////////////////////////////////
  // constructor
  public PredicateFind(Predicate e, Predicate p, Predicate d,
		       Predicate t) {
    _elementName = e;
    _packageName = p;
    _diagramName = d;
    _type = t;
  }


  public boolean matchDiagram(Diagram d) {
    boolean res = _diagramName.predicate(d.getName());
    return res;
  }

  public boolean matchPackage(Model m) {
    boolean res = _packageName.predicate(m.getName().getBody());
    return res;
  }

  public boolean predicate(Object o) {
    if (!(o instanceof ModelElement)) return false;
    ModelElement me = (ModelElement) o;
    return _type.predicate(me) && _specific.predicate(me) &&
      _elementName.predicate(me.getName().getBody());
  }

} /* end class PredicateFind */
