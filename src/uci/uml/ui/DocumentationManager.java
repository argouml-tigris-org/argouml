package uci.uml.ui;

import java.util.*;

public class DocumentationManager {

  public static Hashtable _docs = new Hashtable();
  
  public static String getDocs(Object o) {
    String s = (String) _docs.get(o);
    if (s == null) {
      return defaultFor(o);

    }
    return s;
  }

  public static void setDocs(Object o, String s) {
    if (o == null) return;
    _docs.put(o, s);
  }

  ////////////////////////////////////////////////////////////////
  // default documentation

  public static String defaultFor(Object o) {
    return "(No documentation)";
  }
  
} /* end class DocumentationManager */
