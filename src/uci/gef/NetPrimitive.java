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




// File: NetPrimitive.java
// Classes: NetPrimitive
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.util.*;
import java.beans.*;

import uci.util.*;
import uci.ui.*;

/** Abstract superclass for all Net-level objects. I currently
 *  anticipate exactly 4 subclasses: NetNode, NetPort, NetEdge, and
 *  NetList. <p>
 *
 * The classes that subclass from this class are all used by
 * DefaulGraphModel.  You can also define your own GraphModel with
 * your own application-specific objects for nodes, ports, and
 * edges.<p>
 *
 * This class may be removed from future versions of GEF.
 *
 * @see DefaultgraphModel */

public class NetPrimitive implements java.io.Serializable {
  ////////////////////////////////////////////////////////////////
  // instance variables

  protected PropertyChangeSupport _changeSup = new PropertyChangeSupport(this);
  protected boolean _highlight = false;
  
  /** Construct a new net-level object, currently does nothing */
  public NetPrimitive() { }

  /** Draw the user's attention to any and all visualizations of this
   *  net-level object. */
  public boolean getHighlight() { return _highlight; }
  
  public void setHighlight(boolean b) {
    boolean old = _highlight;
    _highlight = b;
    firePropertyChange("highlight", old, _highlight);
  }


  ////////////////////////////////////////////////////////////////
  // notifications and updates

  public void addPropertyChangeListener(PropertyChangeListener l) {
    _changeSup.addPropertyChangeListener(l);
  }

  public void removePropertyChangeListener(PropertyChangeListener l) {
    _changeSup.removePropertyChangeListener(l);
  }

  public void firePropertyChange(String pName, Object oldV, Object newV) {
    _changeSup.firePropertyChange(pName, oldV, newV);
  }

  public void firePropertyChange(String pName, boolean oldV, boolean newV) {
    _changeSup.firePropertyChange(pName,
				  oldV ? Boolean.TRUE : Boolean.FALSE,
				  newV ? Boolean.TRUE : Boolean.FALSE);
  }

  public void firePropertyChange(String pName, int oldV, int newV) {
    _changeSup.firePropertyChange(pName, new Integer(oldV), new Integer(newV));
  }

  static final long serialVersionUID = 2197255223665843110L;
} /* end class NetPrimitive */
