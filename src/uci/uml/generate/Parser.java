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

package uci.uml.generate;

import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Foundation.Extension_Mechanisms.*;
import uci.uml.Behavioral_Elements.Common_Behavior.*;
import uci.uml.Behavioral_Elements.State_Machines.*;
import uci.uml.Model_Management.*;

public abstract class Parser {

  public abstract Operation parseOperation(String s);
  public abstract Attribute parseAttribute(String s);
  public abstract Parameter parseParameter(String s);
//   public abstract Package parsePackage(String s);
//   public abstract Classifier parseClassifier(String s);
  public abstract Stereotype parseStereotype(String s);
  public abstract TaggedValue parseTaggedValue(String s);
//   public abstract IAssociation parseAssociation(String s);
//   public abstract AssociationEnd parseAssociationEnd(String s);
  public abstract Multiplicity parseMultiplicity(String s);
  public abstract State parseState(String s);
  public abstract Transition parseTransition(String s);
  public abstract MMAction parseAction(String s);
  public abstract Guard parseGuard(String s);
  public abstract Event parseEvent(String s);


  public Expression parseExpression(String s) {
    return new Expression(parseUninterpreted(s));
  }

  public Name parseName(String s) {
    return new Name(s);
  }

  public Uninterpreted parseUninterpreted(String s) {
    return new Uninterpreted(s);
  }

} /* end class Parser */
