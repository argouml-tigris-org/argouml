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

/** This class is the abstract super class that defines a code
 * generation framework.  It is basically a depth-first traversal of
 * the UML model that generates strings as it goes.  This framework
 * should probably be redesigned to separate the traversal logic from
 * the generation logic.  See the <a href=
 * "http://hillside.net/patterns/patterns.html">Vistor design
 * pattern</a> in "Design Patterns", and the <a href=
 * "http://www.ccs.neu.edu/research/demeter/"> Demeter project</a>. */

public abstract class Generator {

  public String generate(Object o) {
    if (o instanceof Operation)
      return generateOperation((Operation) o);
    if (o instanceof Attribute)
      return generateAttribute((Attribute) o);
    if (o instanceof Parameter)
      return generateParameter((Parameter) o);
    if (o instanceof MMPackage)
      return generatePackage((MMPackage) o);
    if (o instanceof Classifier)
      return generateClassifier((Classifier) o);
    if (o instanceof Expression)
      return generateExpression((Expression) o);
    if (o instanceof Name)
      return generateName((Name) o);
    if (o instanceof Uninterpreted)
      return generateUninterpreted((Uninterpreted) o);
    if (o instanceof Stereotype)
      return generateStereotype((Stereotype) o);
    if (o instanceof TaggedValue)
      return generateTaggedValue((TaggedValue) o);
    if (o instanceof IAssociation)
      return generateAssociation((IAssociation)o);
    if (o instanceof AssociationEnd)
      return generateAssociationEnd((AssociationEnd)o);
    if (o instanceof Multiplicity)
      return generateMultiplicity((Multiplicity)o);
    if (o instanceof State)
      return generateState((State)o);
    if (o instanceof Transition)
      return generateTransition((Transition)o);
    if (o instanceof MMAction)
      return generateAction((MMAction)o);
    if (o instanceof Guard)
      return generateGuard((Guard)o);

    if (o instanceof Element)
      return generateName(((Element)o).getName());
    if (o == null) return "";
    return o.toString();
  }

  public abstract String generateOperation(Operation op);
  public abstract String generateAttribute(Attribute attr);
  public abstract String generateParameter(Parameter param);
  public abstract String generatePackage(MMPackage p);
  public abstract String generateClassifier(Classifier cls);
  public abstract String generateStereotype(Stereotype s);
  public abstract String generateTaggedValue(TaggedValue s);
  public abstract String generateAssociation(IAssociation a);
  public abstract String generateAssociationEnd(AssociationEnd ae);
  public abstract String generateMultiplicity(Multiplicity m);
  public abstract String generateState(State m);
  public abstract String generateTransition(Transition m);
  public abstract String generateAction(MMAction m);
  public abstract String generateGuard(Guard m);


  public String generateExpression(Expression expr) {
    if (expr == null) return "";
    return generateUninterpreted(expr.getBody());
  }

  public String generateName(Name n) {
    return n.getBody();
  }

  public String generateUninterpreted(Uninterpreted un) {
    if (un == null) return "";
    return un.getBody();
  }

  public String generateClassifierRef(Classifier cls) {
    if (cls == null) return "";
    return generateName(cls.getName());
  }

} /* end class Generator */
