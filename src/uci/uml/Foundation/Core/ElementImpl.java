// Copyright (c) 1996-98 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation for educational, research and non-profit
// purposes, without fee, and without a written agreement is hereby granted,
// provided that the above copyright notice and this paragraph appear in all
// copies. Permission to incorporate this software into commercial products may
// be obtained by contacting the University of California. David F. Redmiles
// Department of Information and Computer Science (ICS) University of
// California Irvine, California 92697-3425 Phone: 714-824-3823. This software
// program and documentation are copyrighted by The Regents of the University
// of California. The software program and documentation are supplied "as is",
// without any accompanying services from The Regents. The Regents do not
// warrant that the operation of the program will be uninterrupted or
// error-free. The end-user understands that the program was developed for
// research purposes and is advised not to rely exclusively on the program for
// any reason. IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY
// PARTY FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES,
// INCLUDING LOST PROFITS, ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS
// DOCUMENTATION, EVEN IF THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY
// DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
// WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE
// SOFTWARE PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,
// ENHANCEMENTS, OR MODIFICATIONS.


// Source file: Foundation/Core/Element.java

package uci.uml.Foundation.Core;

import java.util.*;
import java.beans.*;
import uci.uml.Foundation.Extension_Mechanisms.TaggedValue;
import uci.uml.Foundation.Extension_Mechanisms.Stereotype;
import uci.uml.Foundation.Data_Types.Name;


public class ElementImpl implements Element {
  public Name _name = Name.UNSPEC;
  //% public TaggedValue _characteristic[];
  public Vector _characteristic;
  public Stereotype _classification;
  //% public TaggedValue _taggedValue[];
  public Vector _taggedValue;
  public transient Vector vetoListeners;
  
  public ElementImpl() { }
  public ElementImpl(Name name) {
    try { setName(name); }
    catch (PropertyVetoException ex) { }
  }
  public ElementImpl(String nameStr) {
    try { setName(new Name(nameStr)); }
    catch (PropertyVetoException ex) { }
  }

  public Vector getCharacteristic() { return _characteristic; }
  public void setCharacteristic(Vector x)
  throws PropertyVetoException {
    if (_characteristic == null) _characteristic = new Vector();
    fireVetoableChange("characteristic", _characteristic, x);
    _characteristic = x;
  }
  public void addCharacteristic(TaggedValue x)
  throws PropertyVetoException {
    if (_characteristic == null) _characteristic = new Vector();
    fireVetoableChange("characteristic", _characteristic, x);
    _characteristic.addElement(x);
  }
  public void removeCharacteristic(TaggedValue x)
  throws PropertyVetoException {
    if (_characteristic == null) return;
    fireVetoableChange("characteristic", _characteristic, x);
    _characteristic.removeElement(x);
  }

  public Stereotype getClassification() { return _classification; }
  public void setClassification(Stereotype x)
  throws PropertyVetoException {
    fireVetoableChange("classification", _classification, x);
    _classification = x;
  }

  public Name getName() { return _name; }
  public void setName(Name x)
  throws PropertyVetoException {
    fireVetoableChange("name", _name, x);
    _name = x;
  }

  public Vector getTaggedValue() { return _taggedValue; }
  public void setTaggedValue(Vector x)
  throws PropertyVetoException {
    if (_taggedValue == null) _taggedValue = new Vector();
    fireVetoableChange("taggedValue", _taggedValue, x);
    _taggedValue = x;
  }
  public void addTaggedValue(TaggedValue x)
  throws PropertyVetoException {
    if (_taggedValue == null) _taggedValue = new Vector();
    fireVetoableChange("taggedValue", _taggedValue, x);
    _taggedValue.addElement(x);
  }
  public void removeTaggedValue(TaggedValue x)
  throws PropertyVetoException {
    if (_taggedValue == null) return;
    fireVetoableChange("taggedValue", _taggedValue, x);
    _taggedValue.removeElement(x);
  }

  ////////////////////////////////////////////////////////////////
  // VetoableChangeSupport

  public synchronized void
  addVetoableChangeListener(VetoableChangeListener listener) {
    if (vetoListeners == null)
      vetoListeners = new Vector();
    vetoListeners.removeElement(listener);
    vetoListeners.addElement(listener);
  }

  public synchronized void
  removeVetoableChangeListener(VetoableChangeListener listener) {
    if (vetoListeners == null) return;
    vetoListeners.removeElement(listener);
  }

  public void fireVetoableChange(String propertyName, 
				 boolean oldValue, boolean newValue) 
       throws PropertyVetoException {
	 fireVetoableChange(propertyName,
			    new Boolean(oldValue),
			    new Boolean(newValue));
  }

  public void fireVetoableChange(String propertyName, 
				 int oldValue, int newValue) 
       throws PropertyVetoException {
	 fireVetoableChange(propertyName,
			    new Integer(oldValue),
			    new Integer(newValue));
  }



  public void fireVetoableChange(String propertyName, 
				 Object oldValue, Object newValue) 
       throws PropertyVetoException {
	 if (vetoListeners == null) return;
    if (oldValue != null && oldValue.equals(newValue)) return;
    PropertyChangeEvent evt =
      new PropertyChangeEvent(this,
			      propertyName, oldValue, newValue);
    try {
      for (int i = 0; i < vetoListeners.size(); i++) {
	VetoableChangeListener target = 
	  (VetoableChangeListener) vetoListeners.elementAt(i);
	target.vetoableChange(evt);
      }
    } catch (PropertyVetoException veto) {
      // Create an event to revert everyone to the old value.
      evt = new PropertyChangeEvent(this, propertyName, newValue, oldValue);
      for (int i = 0; i < vetoListeners.size(); i++) {
	try {
	  VetoableChangeListener target =
	    (VetoableChangeListener) vetoListeners.elementAt(i);
	  target.vetoableChange(evt);
	} catch (PropertyVetoException ex) {
	  // We just ignore exceptions that occur during reversions.
	}
      }
      // And now rethrow the PropertyVetoException.
      throw veto;
    }    
  }
  
  ////////////////////////////////////////////////////////////////
  // OCL support
  
  public String getOCLTypeStr() {
    String javaClassName = getClass().getName();
    int dotIndex = javaClassName.lastIndexOf(".");
    String OCLTypeString = javaClassName.substring(dotIndex+1);
    if (OCLTypeString.equals("MMAction")) return "Action";
    if (OCLTypeString.equals("MMClass")) return "Class";
    if (OCLTypeString.equals("MMException")) return "Exception";
    return OCLTypeString;
  }

  public String dbgString() {
    String s = getOCLTypeStr();
    if (getName() != null) s += "(" + getName().getBody() + ")";
    return s;
  }

}
