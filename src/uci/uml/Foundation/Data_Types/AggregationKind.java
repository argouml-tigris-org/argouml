// Source file: f:/jr/projects/uml/Foundation/Data_Types/AggregationKind.java

package uci.uml.Foundation.Data_Types;

import java.util.*;

public class AggregationKind {
  public static final AggregationKind UNSPEC = new AggregationKind("unspec");
  public static final AggregationKind AGG = new AggregationKind("Aggregate");
  public static final AggregationKind COMPOSITE =
  new AggregationKind("Composite"); 
  public static final AggregationKind NONE = new AggregationKind("None");
  
  public static final AggregationKind[] POSSIBLE_AGGS = {
    UNSPEC, AGG, COMPOSITE, NONE };

  protected String _label;
  public AggregationKind(String label) { _label = label; }

  public boolean equals(Object o) {
    if (!(o instanceof VisibilityKind)) return false;
    String oLabel = ((VisibilityKind)o)._label;
    return _label.equals(oLabel);
  }

  public int hashCode() { return _label.hashCode(); }
  
  public String toString() { return _label.toString(); }

}
