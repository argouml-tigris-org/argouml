// Copyright (c) 1996-99 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.




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
