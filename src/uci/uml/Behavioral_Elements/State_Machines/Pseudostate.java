// Source file: f:/jr/projects/uml/Behavioral_Elements/State_Machines/Pseudostate.java

package uci.uml.Behavioral_Elements.State_Machines;

import java.util.*;

import uci.uml.Foundation.Data_Types.PseudostateKind;

public class Pseudostate extends StateVertex {
  public PseudostateKind _kind;
  
  public Pseudostate() { }
  
  public PseudostateKind getKind() { return _kind; }
  public void setKind(PseudostateKind x) {
    _kind = x;
  }
  
}
