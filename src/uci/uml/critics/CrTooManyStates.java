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

// File: CrTooManyStates.java
// Classes: CrTooManyStates
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.uml.critics;

import java.util.*;
import javax.swing.*;

import uci.argo.kernel.*;
import uci.util.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Behavioral_Elements.State_Machines.*;

/** A critic to detect when a class can never have instances (of
 *  itself of any subclasses). */

public class CrTooManyStates extends CrUML {

  ////////////////////////////////////////////////////////////////
  // constants
  public static String THRESHOLD = "Threshold";

  ////////////////////////////////////////////////////////////////
  // constructor
  public CrTooManyStates() {
    setHeadline("Reduce States in machine <ocl>self</ocl>");
    sd("There are too many States in <ocl>self</ocl>.  If one state machine "+
       "has too many states it may become very difficult for humans "+
       "to understand. \n\n"+
       "Defining an understandable set of states is an important "+
       "part of your design. \n\n"+
       "To fix this, press the \"Next>\" button, or remove states manually "+
       "by clicking on a states in the navigator pane or "+
       "diagram and presing the \"Del\" key.  Or you can nest states...");

    addSupportedDecision(CrUML.decSTATE_MACHINES);
    setArg(THRESHOLD, new Integer(20));
    addTrigger("substate");
  }

  ////////////////////////////////////////////////////////////////
  // critiquing API
  public boolean predicate2(Object dm, Designer dsgr) {
    if (!(dm instanceof CompositeState)) return NO_PROBLEM;
    CompositeState cs = (CompositeState) dm;

    int threshold = ((Integer)getArg(THRESHOLD)).intValue();
    Vector subs = cs.getSubstate();
    if (subs.size() <= threshold) return NO_PROBLEM;
    return PROBLEM_FOUND;
  }

} /* end class CrTooManyStates */

