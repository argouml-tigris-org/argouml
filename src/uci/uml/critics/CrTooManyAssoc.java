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

// File: CrTooManyAssoc.java
// Classes: CrTooManyAssoc
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.uml.critics;

import java.util.*;
import javax.swing.*;

import uci.argo.kernel.*;
import uci.util.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Foundation.Extension_Mechanisms.*;

/** A critic to detect when a class can never have instances (of
 *  itself of any subclasses). */

public class CrTooManyAssoc extends CrUML {

  ////////////////////////////////////////////////////////////////
  // constants
  public static String THRESHOLD = "Threshold";

  ////////////////////////////////////////////////////////////////
  // constructor
  public CrTooManyAssoc() {
    setHeadline("Reduce Associations on <ocl>self</ocl>");
    sd("There are too many Associations on class <ocl>self</ocl>.  Whenever one class "+
       "becomes too central to the design it may become a maintenance "+
       "bottleneck that must be updated frequently. \n\n"+
       "Defining the associations between objects is an important "+
       "part of your design. \n\n"+
       "To fix this, press the \"Next>\" button, or remove associations manually "+
       "by clicking on an association in the navigator pane or "+
       "diagram and presing the \"Del\" key. ");

    addSupportedDecision(CrUML.decRELATIONSHIPS);
    setArg(THRESHOLD, new Integer(7));
    addTrigger("associationEnd");
  }

  ////////////////////////////////////////////////////////////////
  // critiquing API
  public boolean predicate2(Object dm, Designer dsgr) {
    if (!(dm instanceof Classifier)) return NO_PROBLEM;
    Classifier cls = (Classifier) dm;
    // needs-more-work: consider inherited associations?
    // needs-more-work: self loops are double counted
    int threshold = ((Integer)getArg(THRESHOLD)).intValue();
    Vector aes = cls.getAssociationEnd();
    if (aes == null || aes.size() <= threshold) return NO_PROBLEM;
    return PROBLEM_FOUND;
  }

} /* end class CrTooManyAssoc */

