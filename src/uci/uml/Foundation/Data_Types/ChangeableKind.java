// Source file: f:/jr/projects/uml/Foundation/Data_Types/ChangeableKind.java

package uci.uml.Foundation.Data_Types;

import java.util.*;

public class ChangeableKind {
  public static final ChangeableKind UNSPEC = new ChangeableKind("unspec"); 
  public static final ChangeableKind NONE = new ChangeableKind("none"); 
  public static final ChangeableKind FROZEN = new ChangeableKind("frozen");
  public static final ChangeableKind ADDONLY = new ChangeableKind("add only");
  
  public static final ChangeableKind[] POSSIBLE_CHANGABILITIES = {
    UNSPEC, NONE, FROZEN, ADDONLY };

  protected String _label; 
  
  public ChangeableKind(String label) { _label = label; }

  public boolean equals(Object o) {
    if (!(o instanceof VisibilityKind)) return false;
    String oLabel = ((VisibilityKind)o)._label;
    return _label.equals(oLabel);
  }

  public int hashCode() { return _label.hashCode(); }
  
  public String toString() { return _label.toString(); }

}
