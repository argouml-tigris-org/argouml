// Source file: f:/jr/projects/uml/Foundation/Data_Types/MultiplicityRange.java

package uci.uml.Foundation.Data_Types;

import java.util.*;

public class MultiplicityRange {
  public Integer lower;
  public Integer upper;
  //-public Multiplicity m_Multiplicity;
  
  public MultiplicityRange() { }
  public MultiplicityRange(int lower, int upper) {
    this(new Integer(lower), new Integer(upper));
  }
  public MultiplicityRange(Integer lower, Integer upper) {
    setLower(lower);
    setUpper(upper);
  }
  
  public Integer getLower() { return lower; }
  public void setLower(Integer x) {
    lower = x;
  }
  
  public Integer getUpper() { return upper; }
  public void setUpper(Integer x) {
    upper = x;
  }
  
  //-public Multiplicity m_getMultiplicity() { return m_Multiplicity; }
  //-public void setM_Multiplicity(Multiplicity x) {
  //-  Multiplicity Multiplicity = x;
  //-}
  
}
