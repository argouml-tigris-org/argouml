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

package org.argouml.kernel;

import java.util.*;

import org.argouml.cognitive.*;

// TODO: how can this possibly be persistent?
// TODO: provide accessors
// TODO: define subclasses for: modification, criticism

public class HistoryItem {

  ////////////////////////////////////////////////////////////////
  // instance variables
  public Date _when;
  public Poster _who;
  public String _headline;
  public String _desc;
  public Object _target;
  public Object _oldValue;
  public Object _newValue;
  public Vector _relatedItems;

  ////////////////////////////////////////////////////////////////
  // constructors

  public HistoryItem(String headline, String desc) {
    _when = new Date(); // right now
    _who = Designer.TheDesigner;
    _headline = headline;
    _desc = desc;
  }

  public HistoryItem(ToDoItem item, String desc) {
    _when = new Date(); // right now
    _who = item.getPoster();
    //_desc = desc + item.getDescription();
    _headline = item.getHeadline();
    _desc = item.getDescription();
  }

  public HistoryItem(String headline, String desc, Object target,
		     Object oldValue, Object newValue) {
    _when = new Date(); // right now
    _who = Designer.TheDesigner;
    _headline = headline;
    _desc = desc;
    _target = target;
    _oldValue = oldValue;
    _newValue = newValue;
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  public String getDescription() { return _desc; }
  public String getHeadline() { return _headline; }

  public Vector getRelatedItems() { return _relatedItems; }
  public void setRelatedItems(Vector v) { _relatedItems = v; }

  ////////////////////////////////////////////////////////////////
  // debugging

  public String toString() {
    if (_desc == null) return "HI: (null)";
    return "HI: " + _desc;
  }

} /* end class HistoryItem */
