
package uci.ui;

import java.beans.*;
import java.awt.*;
import java.util.*;

public class PropCategoryManager {

  protected static Hashtable _categories = new Hashtable();

  public static void categorizeProperty(String catName, String propName) {
    Hashtable cat = (Hashtable) _categories.get(catName);
    if (cat == null) cat = new Hashtable();
    cat.put(propName, propName);
    _categories.put(catName, cat);
  }

  public static void categorizeProperty(String catName, PropertyDescriptor pd){
    categorizeProperty(catName, pd.getName());
  }

  public static boolean inCategory(String catName, PropertyDescriptor pd) {
    return inCategory(catName, pd.getName());
  }

  public static boolean inCategory(String catName, String propName) {
    if ("All".equals(catName)) return true;
    Hashtable cat = (Hashtable) _categories.get(catName);
    if (cat == null) return false;
    return cat.containsKey(propName);
  }

} /* end class PropCategoryManager */
