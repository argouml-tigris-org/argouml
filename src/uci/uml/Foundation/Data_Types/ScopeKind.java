// Source file: f:/jr/projects/uml/Foundation/Data_Types/ScopeKind.java

package uci.uml.Foundation.Data_Types;

import java.util.*;

public class ScopeKind {
  public static final ScopeKind UNSPEC = new ScopeKind();
  public static final ScopeKind CLASSIFIER = new ScopeKind(); 
  public static final ScopeKind INSTANCE = new ScopeKind();
  
  public static final ScopeKind[] POSSIBLE_SCOPES = {
    UNSPEC, CLASSIFIER, INSTANCE };

  public ScopeKind() { }

}
