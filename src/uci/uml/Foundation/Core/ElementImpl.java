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




// Source file: Foundation/Core/Element.java

package uci.uml.Foundation.Core;

import java.util.*;
import java.beans.*;

import uci.uml.Foundation.Extension_Mechanisms.TaggedValue;
import uci.uml.Foundation.Extension_Mechanisms.Stereotype;
import uci.uml.Foundation.Data_Types.Name;
import uci.ui.Highlightable;

/** Highlight is an added attribute that is not in the UML spec.  It
 *  is used in Argo/UML to visually identify the offending elements
 *  reported by a design critic. Most operations report state changes
 *  via the _vetoListeners, but setHighlight sends notifications to
 *  _propertyListeners. */


public class ElementImpl implements Element, Highlightable {
  ////////////////////////////////////////////////////////////////
  // static variables
  protected static PropertyChangeListener _staticListener = null;

  ////////////////////////////////////////////////////////////////
  // instance variables
  private static int elementCount = 0;
  public String elementID = newElementID();
  public Name _name = Name.UNSPEC;
  //% public TaggedValue _characteristic[];
  public Vector _characteristic = new Vector();
  public Stereotype _classification;
  //% public TaggedValue _taggedValue[];
  public Vector _taggedValue = new Vector();
  public transient Vector _vetoListeners;
  public transient Vector _propertyListeners;
  public boolean _highlight;

  ////////////////////////////////////////////////////////////////
  // constructors

  public ElementImpl() { }
  public ElementImpl(Name name) {
    try { setName(name); }
    catch (PropertyVetoException ex) { }
  }
  public ElementImpl(String nameStr) {
    try { setName(new Name(nameStr)); }
    catch (PropertyVetoException ex) { }
  }

  public static String newElementID () {
    elementCount++;
    return "S." + (100000 + elementCount);
  }

  ////////////////////////////////////////////////////////////////
  // static methods

  public static void setStaticChangeListener(PropertyChangeListener l) {
    _staticListener = l;
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  public static int getElementCount() { return elementCount; }
  public static void setElementCount(int c) { elementCount = c; }

  public Vector getNamedProperty(String propName) {
    Class voidArray[] = {};
    Object objArray[] = {};
    java.lang.reflect.Method methodToCall = null;
    Vector returnVector = new Vector();
    String realName = null;

    try {
      realName = "get" + propName.substring(0,1).toUpperCase() + propName.substring(1, propName.length());
      methodToCall = this.getClass().getMethod(realName, voidArray); 
    } catch (NoSuchMethodException ne) {
      System.err.println("NO method (" + realName + ") matched in getNamedProperty!");
    }

    try {
    returnVector.addElement(methodToCall.invoke(this, objArray));
    
    if (returnVector.firstElement() instanceof Vector) returnVector = (Vector)(returnVector.firstElement());

    } catch (Exception e) {
      System.err.println("Not happy with invoke!");
    }
    
    return returnVector;
  } 

  public Vector getVetoListeners() { return _vetoListeners; }


  public String getId() { return elementID; }
  public void setId(String id) { elementID = id; }

  public Vector getCharacteristic() { return (Vector) _characteristic;}
  public void setCharacteristic(Vector x) throws PropertyVetoException {
    if (_characteristic == null) _characteristic = new Vector();
    fireVetoableChangeNoCompare("characteristic", _characteristic, x);
    _characteristic = x;
  }
  public void addCharacteristic(TaggedValue x) throws PropertyVetoException {
    if (_characteristic == null) _characteristic = new Vector();
    fireVetoableChange("characteristic", _characteristic, x);
    _characteristic.addElement(x);
  }
  public void removeCharacteristic(TaggedValue x)throws PropertyVetoException {
    if (_characteristic == null) return;
    fireVetoableChange("characteristic", _characteristic, x);
    _characteristic.removeElement(x);
  }

  public Stereotype getClassification() { return _classification; }
  public void setClassification(Stereotype x) throws PropertyVetoException {
    fireVetoableChange("classification", _classification, x);
    _classification = x;
  }

  public Name getName() { return _name; }
  public void setName(String x) throws PropertyVetoException {
    setName(new Name(x));
  }
  public void setName(Name x) throws PropertyVetoException {
    fireVetoableChange("name", _name, x);
    _name = x;
  }
  public Vector getTaggedValue() { return (Vector) _taggedValue;}
  public void setTaggedValue(Vector x) throws PropertyVetoException {
    if (_taggedValue == null) _taggedValue = new Vector();
    fireVetoableChangeNoCompare("taggedValue", _taggedValue, x);
    _taggedValue = x;
  }
  public void addTaggedValue(TaggedValue x) throws PropertyVetoException {
    if (_taggedValue == null) _taggedValue = new Vector();
    fireVetoableChange("taggedValue", _taggedValue, x);
    _taggedValue.addElement(x);
  }
  public void removeTaggedValue(TaggedValue x) throws PropertyVetoException {
    if (_taggedValue == null) return;
    fireVetoableChange("taggedValue", _taggedValue, x);
    _taggedValue.removeElement(x);
  }

  public boolean getHighlight() { return _highlight; }
  public void setHighlight(boolean x) {
    boolean old = _highlight;
    _highlight = x;
    firePropertyChangeNoCritique("highlight", old, _highlight);
  }


  ////////////////////////////////////////////////////////////////
  // VetoableChangeSupport

  public synchronized void
  addVetoableChangeListener(VetoableChangeListener listener) {
    if (_vetoListeners == null) _vetoListeners = new Vector();
    _vetoListeners.removeElement(listener);
    _vetoListeners.addElement(listener);
    //System.out.println("add _vetoListeners.size() = " + _vetoListeners.size());
  }

  public synchronized void
  removeVetoableChangeListener(VetoableChangeListener listener) {
    if (_vetoListeners == null) return;
    _vetoListeners.removeElement(listener);
    //System.out.println("rm _vetoListeners.size() = " + _vetoListeners.size());
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
    if (oldValue != null && oldValue.equals(newValue)) return;
    fireVetoableChangeNoCompare(propertyName, oldValue, newValue);
  }

  public void fireVetoableChangeNoCompare(String propertyName,
				 Object oldValue, Object newValue)
       throws PropertyVetoException {
    if (_vetoListeners == null) return;
    PropertyChangeEvent evt =
      new PropertyChangeEvent(this,
			      propertyName, oldValue, newValue);
    try {
      if (_staticListener != null) _staticListener.propertyChange(evt);
      for (int i = 0; i < _vetoListeners.size(); i++) {
	VetoableChangeListener target =
	  (VetoableChangeListener) _vetoListeners.elementAt(i);
	target.vetoableChange(evt);
      }
    } catch (PropertyVetoException veto) {
      // Create an event to revert everyone to the old value.
      evt = new PropertyChangeEvent(this, propertyName, newValue, oldValue);
      for (int i = 0; i < _vetoListeners.size(); i++) {
	try {
	  VetoableChangeListener target =
	    (VetoableChangeListener) _vetoListeners.elementAt(i);
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
  // property change events

  public synchronized void
  addPropertyChangeListener(PropertyChangeListener listener) {
    if (_propertyListeners == null) _propertyListeners = new Vector();
    _propertyListeners.removeElement(listener);
    _propertyListeners.addElement(listener);
    //System.out.println("add _propListeners.size() = " + _propertyListeners.size());
  }

  public synchronized void
  removePropertyChangeListener(PropertyChangeListener listener) {
    if (_propertyListeners == null) return;
    _propertyListeners.removeElement(listener);
    //System.out.println("rm _propListeners.size() = " + _propertyListeners.size());
  }

  public void firePropertyChange(String propertyName,
				 boolean oldValue, boolean newValue) {
	 firePropertyChange(propertyName,
			    new Boolean(oldValue),
			    new Boolean(newValue));
  }

  public void firePropertyChangeNoCritique(String propertyName,
				 boolean oldValue, boolean newValue) {
	 firePropertyChangeNoCritique(propertyName,
			    new Boolean(oldValue),
			    new Boolean(newValue));
  }

  public void firePropertyChange(String propertyName,
				 int oldValue, int newValue) {
	 firePropertyChange(propertyName,
			    new Integer(oldValue),
			    new Integer(newValue));
  }



  public void firePropertyChange(String propertyName,
				 Object oldValue, Object newValue) {
    firePropertyChangeNoCritique(propertyName, oldValue, newValue);
    PropertyChangeEvent evt =
      new PropertyChangeEvent(this, propertyName, oldValue, newValue);
    if (_staticListener != null) _staticListener.propertyChange(evt);
  }

  public void firePropertyChangeNoCritique(String propertyName,
				 Object oldValue, Object newValue) {
    if (_propertyListeners == null) return;
    if (oldValue != null && oldValue.equals(newValue)) return;
    PropertyChangeEvent evt =
      new PropertyChangeEvent(this, propertyName, oldValue, newValue);
    //System.out.println("fire _propertyListeners.size() = " +
    //_propertyListeners.size());
    for (int i = 0; i < _propertyListeners.size(); i++) {
      PropertyChangeListener target =
	(PropertyChangeListener) _propertyListeners.elementAt(i);
      target.propertyChange(evt);
    }
  }

  public String toString() {
    return getOCLTypeStr() + "[id=" + getId() + "]";
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

  static final long serialVersionUID = -2347127736438090568L;
}
