package uci.uml.generate;

import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Foundation.Extension_Mechanisms.*;

// needs-more-work: always check for null!!!

public class GeneratorDisplay extends Generator {

  public static GeneratorDisplay SINGLETON = new GeneratorDisplay();

  public static String Generate(Object o) {
    return SINGLETON.generate(o);
  }
  
  public String generateOperation(Operation op) {
    String s = "";
    s += generateVisibility(op);
    s += generateScope(op);

    // pick out return type
    Classifier returnType = null;
    java.util.Enumeration params = op.getParameter().elements();
    while (params.hasMoreElements()) {
      Parameter p = (Parameter) params.nextElement();
      if (p.getName() == Parameter.RETURN_NAME) {
	returnType = p.getType();
	break;
      }
    }
    if (returnType == null) s += "void?? ";
    else s += generateClassifierRef(returnType) + " ";

    
    // name and params
    s += generateName(op.getName()) + "(";
    params = op.getParameter().elements();
    boolean first = true;
    while (params.hasMoreElements()) {
      Parameter p = (Parameter) params.nextElement();
      if (p.getName() == Parameter.RETURN_NAME) continue;
      if (!first) s += ", ";
      s += generateParameter(p);
      first = false;
    }
    s += ")";
    return s;
  }

  public String generateAttribute(Attribute attr) {
    String s = "";
    s += generateVisibility(attr);
    s += generateScope(attr);
    s += generateMultiplicity(attr.getMultiplicity());

    Classifier type = attr.getType();
    if (type != null) s += generateClassifierRef(type) + " ";
    
    s += generateName(attr.getName());
    Expression init = attr.getInitialValue();
    if (init != null)
      s += " = " + generateExpression(init);
    return s;
  }


  public String generateParameter(Parameter param) {
    String s = "";
    //needs-more-work: qualifiers (e.g., const)
    //needs-more-work: stereotypes...
    s += generateClassifierRef(param.getType()) + " ";
    s += generateName(param.getName());
    //needs-more-work: initial value
    return s;
  }


  public String generateClassifier(Classifier cls) {
    return "";
  }

  public String generateStereotype(Stereotype s) {
    return "<<" + generateName(s.getName()) + ">>";
  }

  public String generateTaggedValue(TaggedValue tv) {
    if (tv == null) return "";
    return generateName(tv.getTag()) + "=" +
      generateUninterpreted(tv.getValue());
  }


  public String generateAssociation(Association a) {
    return "";
  }

  public String generateAssociationEnd(AssociationEnd ae) {
    System.out.println("asdasdasdqwdasd");
    String s = "";
    Name n = ae.getName();
    if (n != null && n != Name.UNSPEC) s += generateName(n) + " ";
    if (ae.getIsNavigable()) s += "navigable ";
    if (ae.getIsOrdered()) s += "ordered ";
    Multiplicity m = ae.getMultiplicity();
    if (m != null) s+= generateMultiplicity(m) + " ";
    s += generateClassifierRef(ae.getType());
    return s;
  }

  ////////////////////////////////////////////////////////////////
  // internal methods?
  
  public String generateVisibility(Feature f) {
    VisibilityKind vis = f.getVisibility();
    if (vis == null) return "";
    if (vis == VisibilityKind.PUBLIC) return "public ";
    if (vis == VisibilityKind.PRIVATE) return "private ";
    if (vis == VisibilityKind.PROTECTED) return "protected ";
    return "";
  }

  public String generateScope(Feature f) {
    ScopeKind scope = f.getOwnerScope();
    if (scope == null) return "";
    if (scope == ScopeKind.CLASSIFIER) return "static ";
    return "";
  }

  public String generateMultiplicity(Multiplicity m) {
    // needs-more-work 
    return "";
  }



} /* end class GeneratorDisplay */
