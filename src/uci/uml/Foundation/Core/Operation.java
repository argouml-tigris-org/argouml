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
import java.beans.PropertyVetoException;

import uci.uml.Foundation.Data_Types.*;
import uci.uml.Foundation.Data_Types.Name;
import uci.uml.generate.*;

public class Operation extends BehavioralFeature {
  public Uninterpreted _specification;
  public boolean _isPolymorphic;
  public CallConcurrencyKind _concurrency = CallConcurrencyKind.SEQUENTIAL;
  //% public Method _method[];
  public Vector _method = new Vector();

  public Operation() { }
  public Operation(Name name) { super(name); }
  public Operation(String nameStr) { super(new Name(nameStr)); }

  public Operation(Classifier returnType, String nameStr) {
    this(nameStr);
    try {
      addParameter(new Parameter(returnType,
				 ParameterDirectionKind.RETURN,
				 Parameter.RETURN_NAME));
    } catch (PropertyVetoException pve) { }
  }

  public Operation(Classifier returnType, String nameStr,
		   Classifier arg1Type, String arg1Name) {
    this(nameStr);
    try {
      addParameter(new Parameter(returnType,
				 ParameterDirectionKind.RETURN,
				 Parameter.RETURN_NAME));
      addParameter(new Parameter(arg1Type,
				 ParameterDirectionKind.IN,
				 arg1Name));
    } catch (PropertyVetoException pve) { }
  }

  public Operation(Classifier returnType, String nameStr,
		   Classifier arg1Type, String arg1Name,
		   Classifier arg2Type, String arg2Name) {
    this(nameStr);
    try {
      addParameter(new Parameter(returnType,
				 ParameterDirectionKind.RETURN,
				 Parameter.RETURN_NAME));
      addParameter(new Parameter(arg1Type, ParameterDirectionKind.IN, arg1Name));
      addParameter(new Parameter(arg2Type, ParameterDirectionKind.IN, arg2Name));
    } catch (PropertyVetoException pve) { }
  }

  public Operation(Classifier returnType, String nameStr,
		   Classifier arg1Type, String arg1Name,
		   Classifier arg2Type, String arg2Name,
		   Classifier arg3Type, String arg3Name) {
    this(nameStr);
    try {
      addParameter(new Parameter(returnType,
				 ParameterDirectionKind.RETURN,
				 Parameter.RETURN_NAME));
      addParameter(new Parameter(arg1Type, ParameterDirectionKind.IN, arg1Name));
      addParameter(new Parameter(arg2Type, ParameterDirectionKind.IN, arg2Name));
      addParameter(new Parameter(arg3Type, ParameterDirectionKind.IN, arg3Name));
    } catch (PropertyVetoException pve) { }
  }

  public Uninterpreted getSpecification() { return _specification; }
  public void setSpecification(Uninterpreted x) throws PropertyVetoException {
    fireVetoableChange("specification", _specification, x);
    _specification = x;
  }

  public boolean getIsPolymorphic() { return _isPolymorphic; }
  public void setIsPolymorphic(boolean x) throws PropertyVetoException {
    fireVetoableChange("isPolymorphic", _isPolymorphic, x);
    _isPolymorphic = x;
  }

  public CallConcurrencyKind getConcurrency() { return _concurrency; }
  public void setConcurrency(CallConcurrencyKind x) {
    _concurrency = x;
  }

  public Vector getMethod() { return (Vector) _method;}
  public void setMethod(Vector x) throws PropertyVetoException {
    if (_method == null) _method = new Vector();
    fireVetoableChangeNoCompare("method", _method, x);
    _method = x;
    java.util.Enumeration enum = _method.elements();
    while (enum.hasMoreElements()) {
      Method m = (Method) enum.nextElement();
      m.setNamespace(getNamespace());
    }
  }
  public void addMethod(Method x) throws PropertyVetoException {
    if (_method == null) _method = new Vector();
    fireVetoableChange("method", _method, x);
    _method.addElement(x);
    x.setNamespace(getNamespace());
  }
  public void removeMethod(Method x) throws PropertyVetoException {
    if (_method == null) return;
    fireVetoableChange("method", _method, x);
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

  static final long serialVersionUID = -6599260432021893292L;
}
