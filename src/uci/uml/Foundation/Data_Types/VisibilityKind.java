// Source file: f:/jr/projects/uml/Foundation/Data_Types/VisibilityKind.java

package uci.uml.Foundation.Data_Types;

import java.util.*;

public class VisibilityKind {
  public static final VisibilityKind UNSPEC = new VisibilityKind("unspec"); 
  public static final VisibilityKind PUBLIC = new VisibilityKind("public"); 
  public static final VisibilityKind PRIVATE = new VisibilityKind("private");
  public static final VisibilityKind PROTECTED = new VisibilityKind("protected");
  public static final VisibilityKind PACKAGE = new VisibilityKind("package");

  public static final VisibilityKind[] POSSIBLE_VISIBILITIES = {
    UNSPEC, PUBLIC, PRIVATE, PROTECTED, PACKAGE };

  protected String _label = null;
  
  public VisibilityKind(String label) { _label = label; }
  
  public boolean equals(Object o) {
    if (!(o instanceof VisibilityKind)) return false;
    String oLabel = ((VisibilityKind)o)._label;
    return _label.equals(oLabel);
  }

  public int hashCode() { return _label.hashCode(); }
  
  public String toString() { return _label.toString(); }
  
}
