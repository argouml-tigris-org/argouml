// Copyright (c) 1995, 1996 Regents of the University of California.
// All rights reserved.
//
// This software was developed by the Arcadia project
// at the University of California, Irvine.
//
// Redistribution and use in source and binary forms are permitted
// provided that the above copyright notice and this paragraph are
// duplicated in all such forms and that any documentation,
// advertising materials, and other materials related to such
// distribution and use acknowledge that the software was developed
// by the University of California, Irvine.  The name of the
// University may not be used to endorse or promote products derived
// from this software without specific prior written permission.
// THIS SOFTWARE IS PROVIDED ``AS IS'' AND WITHOUT ANY EXPRESS OR
// IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
// WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.

// File: CheckManager.java
// Class: CheckManager
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.argo.checklist;

import java.util.*;

import uci.util.*;

/** 
 */

public class CheckManager implements java.io.Serializable {

  ////////////////////////////////////////////////////////////////
  // static variables

  protected static Hashtable _lists = new Hashtable();
  protected static Hashtable _stats = new Hashtable();

  ////////////////////////////////////////////////////////////////
  // constructor
  protected CheckManager() { }


  ////////////////////////////////////////////////////////////////
  // static accessors

  public static Checklist getChecklistFor(Object dm) {
    Checklist cl = (Checklist) _lists.get(dm);
    if (cl != null) return cl;
    
    java.lang.Class cls = dm.getClass();
    while (cls != null) {
      cl = (Checklist) _lists.get(cls);
      if (cl != null) return cl;
      cls = cls.getSuperclass();
    }

    return null;    
  }


  public static void register(Object dm, Checklist cl) {
    _lists.put(dm, cl);
  }


  public static ChecklistStatus getStatusFor(Object dm) {
    ChecklistStatus cls = (ChecklistStatus) _stats.get(dm);
    if (cls == null) {
      cls = new ChecklistStatus();
      _stats.put(dm, cls);
    }
    return cls;
  }
} /* end class CheckManager */

