// Source file: f:/jr/projects/uml/Foundation/Data_Types/AggregationKind.java

package uci.uml.Foundation.Data_Types;

import java.util.*;

public class AggregationKind {
  public static final AggregationKind UNSPEC = new AggregationKind();
  public static final AggregationKind AGG = new AggregationKind();
  public static final AggregationKind COMPOSITE = new AggregationKind(); 
  public static final AggregationKind NONE = new AggregationKind();
  
  public static final AggregationKind[] POSSIBLE_AGGS = {
    UNSPEC, AGG, COMPOSITE, NONE };
  
  public AggregationKind() { }

}
