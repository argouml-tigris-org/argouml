// Source file: f:/jr/projects/uml/Behavioral_Elements/Common_Behavior/Exception.java

package uci.uml.Behavioral_Elements.Common_Behavior;

import java.util.*;

import uci.uml.Foundation.Core.BehavioralFeature;


public class Exception extends Signal {
  //% public BehavioralFeature _context[];
  public Vector _context;
    
  public Exception() { }

  public Vector getContext() { return _context; }
  public void setContext(Vector x) {
    _context = x;
  }
  public void addContext(BehavioralFeature x) {
    if (_context == null) _context = new Vector();
    _context.addElement(x);
  }
  public void removeContext(BehavioralFeature x) {
    _context.removeElement(x);
  }
  
}
