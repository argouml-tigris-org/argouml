// Source file: f:/jr/projects/uml/Foundation/Core/Operation.java

package uci.uml.Foundation.Core;

import java.util.*;
import java.beans.PropertyVetoException;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Foundation.Data_Types.Name;
import uci.uml.generate.*;

public class Operation extends BehavioralFeature {
  public Uninterpreted _specification;
  public Boolean _isPolymorphic;
  public CallConcurrencyKind _concurrency;
  //% public Method _method[];
  public Vector _method;

  public Operation() { }
  public Operation(Name name) { super(name); }
  public Operation(String nameStr) { super(new Name(nameStr)); }

  public Operation(Classifier returnType, String nameStr) {
    this(nameStr);
    try {
      addParameter(new Parameter(returnType, Parameter.RETURN_NAME));
    }
    catch (PropertyVetoException pve) { }
  }
  
  public Operation(Classifier returnType, String nameStr,
		   Classifier arg1Type, String arg1Name) {
    this(nameStr);
    try {
      addParameter(new Parameter(returnType, Parameter.RETURN_NAME));
      addParameter(new Parameter(arg1Type, arg1Name));
    }
    catch (PropertyVetoException pve) { }
  }

  public Operation(Classifier returnType, String nameStr,
		   Classifier arg1Type, String arg1Name,
		   Classifier arg2Type, String arg2Name) {
    this(nameStr);
    try {
      addParameter(new Parameter(returnType, Parameter.RETURN_NAME));
      addParameter(new Parameter(arg1Type, arg1Name));
      addParameter(new Parameter(arg2Type, arg2Name));
    }
    catch (PropertyVetoException pve) { }
  }

  public Operation(Classifier returnType, String nameStr,
		   Classifier arg1Type, String arg1Name,
		   Classifier arg2Type, String arg2Name,
		   Classifier arg3Type, String arg3Name) {
    this(nameStr);
    try {
      addParameter(new Parameter(returnType, Parameter.RETURN_NAME));
      addParameter(new Parameter(arg1Type, arg1Name));
      addParameter(new Parameter(arg2Type, arg2Name));
      addParameter(new Parameter(arg3Type, arg3Name));
    }
    catch (PropertyVetoException pve) { }
  }

  public Uninterpreted getSpecification() { return _specification; }
  public void setSpecification(Uninterpreted x) {
    _specification = x;
  }
  
  public Boolean getIsPolymorphic() { return _isPolymorphic; }
  public void setIsPolymorphic(Boolean x) {
    _isPolymorphic = x;
  }

  public CallConcurrencyKind getConcurrency() { return _concurrency; }
  public void setConcurrency(CallConcurrencyKind x) {
    _concurrency = x;
  }
  
  public Vector getMethod() { return _method; }
  public void setMethod(Vector x) {
    _method = x;
  }
  public void addMethod(Method x) {
    if (_method == null) _method = new Vector();
    _method.addElement(x);
  }
  public void removeMethod(Method x) {
    _method.removeElement(x);
  }


  ////////////////////////////////////////////////////////////////
  // debugging

  public String dbgString() {
    String s = getOCLTypeStr() + "{" +
      GeneratorDisplay.Generate(this) +
      "}";
    return s;
  }
  
  
}
