package uci.uml.generate;

import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Foundation.Extension_Mechanisms.*;
import uci.uml.Model_Management.*;

public abstract class Generator {
  
  public String generate(Object o) {
    if (o instanceof Operation)
      return generateOperation((Operation) o);
    if (o instanceof Attribute)
      return generateAttribute((Attribute) o);
    if (o instanceof Parameter)
      return generateParameter((Parameter) o);
    if (o instanceof Package)
      return generatePackage((Package) o);
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
    if (o instanceof Element)
      return generateName(((Element)o).getName());
    else
      return o.toString();
  }

  public abstract String generateOperation(Operation op);
  public abstract String generateAttribute(Attribute attr);
  public abstract String generateParameter(Parameter param);
  public abstract String generatePackage(Package p);
  public abstract String generateClassifier(Classifier cls);
  public abstract String generateStereotype(Stereotype s);
  public abstract String generateTaggedValue(TaggedValue s);
  public abstract String generateAssociation(IAssociation a);
  public abstract String generateAssociationEnd(AssociationEnd ae);

  
  public String generateExpression(Expression expr) {
    return generateUninterpreted(expr.getBody());
  }
  
  public String generateName(Name n) {
    return n.getBody();
  }

  public String generateUninterpreted(Uninterpreted un) {
    return un.getBody();
  }

  public String generateClassifierRef(Classifier cls) {
    return generateName(cls.getName());
  }

} /* end class Generator */
