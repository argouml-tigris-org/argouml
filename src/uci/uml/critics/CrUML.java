// Copyright (c) 1996-98 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation for educational, research and non-profit
// purposes, without fee, and without a written agreement is hereby granted,
// provided that the above copyright notice and this paragraph appear in all
// copies. Permission to incorporate this software into commercial products
// must be negotiated with University of California. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "as is",
// without any accompanying services from The Regents. The Regents do not
// warrant that the operation of the program will be uninterrupted or
// error-free. The end-user understands that the program was developed for
// research purposes and is advised not to rely exclusively on the program for
// any reason. IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY
// PARTY FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES,
// INCLUDING LOST PROFITS, ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS
// DOCUMENTATION, EVEN IF THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY
// DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
// WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE
// SOFTWARE PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,
// ENHANCEMENTS, OR MODIFICATIONS.



// File: CrUML.java
// Classes: CrUML
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.uml.critics;

import uci.util.*;
import uci.argo.kernel.*;
import uci.uml.util.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;

/** "Abstract" Critic subclass that captures commonalities among all
 *  critics in the UML domain.  This class also defines and registers
 *  the categories of design decisions that the critics can
 *  address.
 *
 * @see uci.argo.kernel.Designer
 * @see uci.argo.kernel.DecisionModel
 */

public class CrUML extends Critic {
  public static final Decision decINHERITANCE = new
  Decision("Inheritance", 5);

  public static final Decision decCONTAINMENT = new
  Decision("Containment", 5);

  public static final Decision decPATTERNS = new
  Decision("Design Patterns", 5); //??

  public static final Decision decRELATIONSHIPS = new
  Decision("Relationships", 5);

  public static final Decision decSTORAGE = new
  Decision("Storage", 5);

  public static final Decision decINSTANCIATION = new
  Decision("Instantiation", 5);

  public static final Decision decNAMING = new
  Decision("Naming", 5);

  public static final Decision decMODULARITY = new
  Decision("Modularity", 5);

  public static final Decision decCLASS_SELECTION = new
  Decision("Class Selection", 5);

  public static final Decision decMETHODS = new
  Decision("Methods", 5); //??

  public static final Decision decCODE_GEN = new
  Decision("Code Generation", 5); //??

  public static final Decision decPLANNED_EXTENSIONS = new
  Decision("Planned Extensions", 5);

  public static final Decision decSTEREOTYPES = new
  Decision("Stereotypes", 5);



  /** Static initializer for this class. Called when the class is
   *  loaded (which is before any subclass instances are instanciated). */
  static {
    Designer d = Designer.theDesigner();
    d.startConsidering(decINHERITANCE);
    d.startConsidering(decCONTAINMENT);
    d.startConsidering(decPATTERNS);
    d.startConsidering(decRELATIONSHIPS);
    d.startConsidering(decSTORAGE);
    d.startConsidering(decINSTANCIATION);
    d.startConsidering(decNAMING);
    d.startConsidering(decMODULARITY);
    d.startConsidering(decCLASS_SELECTION);
    d.startConsidering(decMETHODS);
    d.startConsidering(decCODE_GEN);
    d.startConsidering(decPLANNED_EXTENSIONS);
    d.startConsidering(decSTEREOTYPES);
  }



  ////////////////////////////////////////////////////////////////
  // constructor

  public CrUML() {
    //decisionCategory("UML Decisions");
    // what do UML critics have in common? anything?
  }

  ////////////////////////////////////////////////////////////////
  // display related methods


  public String expand(String res, Set offs) {
    //System.out.println("expanding: " + res);
    if (offs.size() == 0) return res;
    Object off1 = offs.firstElement();
    if (!(off1 instanceof Element)) return res;
    return UMLDescription.expand(res, off1);
  }

} /* end class CrUML */
