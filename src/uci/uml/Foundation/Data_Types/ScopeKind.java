// Source file: f:/jr/projects/uml/Foundation/Data_Types/ScopeKind.java

package uci.uml.Foundation.Data_Types;

import java.util.*;

public class ScopeKind {
  public static final ScopeKind UNSPEC = new ScopeKind("unspec");
  public static final ScopeKind CLASSIFIER = new ScopeKind("static"); 
  public static final ScopeKind INSTANCE = new ScopeKind("instance");
  
  public static final ScopeKind[] POSSIBLE_SCOPES = {
    UNSPEC, CLASSIFIER, INSTANCE };

  protected String _label;
  
  public ScopeKind(String label) { _label = label; }

  public boolean equals(Object o) {
    if (!(o instanceof VisibilityKind)) return false;
    String oLabel = ((VisibilityKind)o)._label;
    return _label.equals(oLabel);
  }

  public int hashCode() { return _label.hashCode(); }
  
  public String toString() { return _label.toString(); }
}
