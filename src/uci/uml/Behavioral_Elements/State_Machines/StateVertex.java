// Source file: f:/jr/projects/uml/Behavioral_Elements/State_Machines/StateVertex.java

package uci.uml.Behavioral_Elements.State_Machines;

import java.util.*;

import uci.uml.Foundation.Core.*;


public abstract class StateVertex extends ModelElementImpl {
  public CompositeState _parent;
  //% public Transition _outgoing[];
  public Vector _outgoing;
  //% public Transition _incoming[];
  public Vector _incoming;
    
  public StateVertex() { }
  
}
