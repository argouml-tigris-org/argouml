// Source file: f:/jr/projects/uml/Foundation/Data_Types/VisibilityKind.java

package uci.uml.Foundation.Data_Types;

import java.util.*;

public class VisibilityKind {
  public static final VisibilityKind UNSPEC = new VisibilityKind(); 
  public static final VisibilityKind PUBLIC = new VisibilityKind(); 
  public static final VisibilityKind PRIVATE = new VisibilityKind();
  public static final VisibilityKind PROTECTED = new VisibilityKind();
  public static final VisibilityKind PACKAGE = new VisibilityKind();

  public static final VisibilityKind[] POSSIBLE_VISIBILITIES = {
    UNSPEC, PUBLIC, PRIVATE, PROTECTED, PACKAGE };
  
  public VisibilityKind() { }
  
}
