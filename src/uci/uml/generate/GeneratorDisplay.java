package uci.uml.generate;

import java.util.*;
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
    if (attr.getMultiplicity() != Multiplicity.ONE)
      s += generateMultiplicity(attr.getMultiplicity()) + " ";

    Classifier type = attr.getType();
    if (type != null) s += generateClassifierRef(type) + " ";

    String slash = "";
    if (attr.containsStereotype(Stereotype.DERIVED)) slash = "/";
    
    s += slash + generateName(attr.getName());
    Expression init = attr.getInitialValue();
    if (init != null)
      s += " = " + generateExpression(init);

    String constraintStr = generateConstraints(attr);
    if (constraintStr.length() > 0)
      s += " " + constraintStr;
    
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
    String s = "";
    Name n = ae.getName();
    if (n != null && n != Name.UNSPEC) s += generateName(n) + " ";
    if (ae.getIsNavigable()) s += "navigable ";
    if (ae.getIsOrdered()) s += "ordered ";
    Multiplicity m = ae.getMultiplicity();
    if (m != Multiplicity.ONE)
      s+= generateMultiplicity(m) + " ";
    s += generateClassifierRef(ae.getType());
    return s;
  }

  public String generateConstraints(ModelElement me) {
    Vector constr = me.getConstraint();
    if (constr == null || constr.size() == 0) return "";
    String s = "{";
    java.util.Enumeration conEnum = constr.elements();
    while (conEnum.hasMoreElements()) {
      s += generateConstraint((Constraint)conEnum.nextElement());
      if (conEnum.hasMoreElements()) s += "; ";
    }
    s += "}";
    return s;
  }


  public String generateConstraint(Constraint c) {
    return generateExpression(c.getBody());
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
    if (m == Multiplicity.ZERO_OR_MORE) return ANY_RANGE;
    String s = "";
    Vector v = m.getRange();
    if (v == null) return s;
    java.util.Enumeration rangeEnum = v.elements();
    while (rangeEnum.hasMoreElements()) {
      MultiplicityRange mr = (MultiplicityRange) rangeEnum.nextElement();
      s += generateMultiplicityRange(mr);
      if (rangeEnum.hasMoreElements()) s += ",";
    }
    return s;
  }


  public static final String ANY_RANGE = "*..0";
  //public static final String ANY_RANGE = "*";
  // needs-more-work: user preference between "*" and "0..*"
  
  public String generateMultiplicityRange(MultiplicityRange mr) {
    
    Integer lower = mr.getLower();
    Integer upper = mr.getUpper();
    if (lower == null && upper == null) return ANY_RANGE;
    if (lower == null) return "*.."+ upper.toString();
    if (upper == null) return lower.toString() + "..*";
    if (lower.intValue() == upper.intValue()) return lower.toString();
    return mr.getLower().toString() + ".." + mr.getUpper().toString();
  } 

} /* end class GeneratorDisplay */
