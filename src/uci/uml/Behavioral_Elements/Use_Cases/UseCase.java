// Source file: Behavioral_Elements/Use_Cases/UseCase.java

package uci.uml.Behavioral_Elements.Use_Cases;

import java.util.*;

import uci.uml.Foundation.Core.Classifier;


public class UseCase extends Classifier {
  //% public String _extensionPoint[];
  public Vector _extensionPoint;
    
  public UseCase() { }
  
  public Vector getExtensionPoint() { return _extensionPoint; }
  public void setExtensionPoint(Vector x) {
    _extensionPoint = x;
  }
  public void addExtensionPoint(String x) {
    if (_extensionPoint == null) _extensionPoint = new Vector();
    _extensionPoint.addElement(x);
  }
  public void removeExtensionPoint(String x) {
    _extensionPoint.removeElement(x);
  }

}

