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



package uci.uml.Foundation.Core;

import java.util.*;
import uci.uml.Foundation.Extension_Mechanisms.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Model_Management.*;
import uci.uml.Behavioral_Elements.Common_Behavior.*;
import uci.uml.Behavioral_Elements.State_Machines.*;
//import uci.uml.Behavioral_Elements.Collaborations.*;

/** By default, Constraints are in the Namespace of their constrained
 *  ModelElement or constrained Stereotype */

public class Constraint extends ModelElementImpl {
  public BooleanExpression _body;
  //- public ModelElement _constrainedElement[];
  //- public Stereotype _constrainedStereotype;

  public Constraint() { }
  public Constraint(Name name) { super(name); }
  public Constraint(String nameStr) { super(new Name(nameStr)); }
  public Constraint(Name name, String body) {
    super(name);
    setBody(new BooleanExpression(body));
  }
  public Constraint(String nameStr, String body) {
    super(new Name(nameStr));
    setBody(new BooleanExpression(body));
  }
  public Constraint(String nameStr, String lang, String body) {
    super(new Name(nameStr));
    setBody(new BooleanExpression(lang, body));
  }
  public Constraint(Name name, String lang, String body) {
    super(name);
    setBody(new BooleanExpression(lang, body));
  }

  public BooleanExpression getBody() { return _body; }
  public void setBody(BooleanExpression x) {
    _body = x;
  }

  //- public ModelElement[] getConstrainedElement() {
  //-   return _constrainedElement;
  //- }
  //- public void setConstrainedElement(ModelElement[] x) {
  //-   _constrainedElement = x;
  //- }

  //- public Stereotype getConstrainedStereotype() {
  //-   return _constrainedStereotype;
  //- }
  //- public void setConstrainedStereotype(Stereotype x) {
  //-   _constrainedStereotype = x;
  //- }

  static final long serialVersionUID = -5614209998665105340L;
}
