// Source file: f:/jr/projects/uml/Foundation/Data_Types/Multiplicity.java

package uci.uml.Foundation.Data_Types;

import java.util.*;

public class Multiplicity {
    
  public static final Multiplicity ONE = new Multiplicity(1, 1);
  public static final Multiplicity ONE_OR_ZERO = new Multiplicity(0, 1);
  public static final Multiplicity ONE_OR_MORE =
  new Multiplicity(new Integer(1), null);
  public static final Multiplicity ZERO_OR_MORE =
  new Multiplicity(new Integer(0), null);
  public static int MAX_MULTIPLICITY_RANGES = 10;
  
  //% public MultiplicityRange _ranges[];
  public Vector _ranges;
    
  public Multiplicity() { }
  public Multiplicity(int low, int high) {
    addRange(new MultiplicityRange(low, high));
  }
  public Multiplicity(Integer low, Integer high) {
    addRange(new MultiplicityRange(low, high));
  }

  public Vector getRange() { return _ranges; }
  public void setRange(Vector x) {
    _ranges = x;
  }
  public void addRange(MultiplicityRange x) {
    if (_ranges == null) _ranges = new Vector();
    _ranges.addElement(x);
  }
  public void removeRange(MultiplicityRange x) {
    _ranges.removeElement(x);
  }

}
