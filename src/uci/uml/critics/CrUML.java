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

// File: CrUML.java
// Classes: CrUML
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.uml.critics;

import uci.argo.kernel.*;
import uci.uml.Foundation.Core.*;

/** "Abstract" Critic subclass that captures commonalities among all
 *  critics in the UML domain. */

public class CrUML extends Critic {
  public static final Decision decINHERITANCE = new
  Decision("Inheritance", 5);

  public static final Decision decCONTAINMENT = new
  Decision("Containment", 5);

  public static final Decision decPATTERNS = new
  Decision("Design Paterns", 5); //??

  public static final Decision decRELATIONSHIPS = new
  Decision("Relationships", 5);

  public static final Decision decSTORAGE = new
  Decision("Storage", 5);

  public static final Decision decINSTANCIATION = new
  Decision("Instanciation", 5);

  public static final Decision decNAMING = new
  Decision("Naming", 5);

  public static final Decision decMODULARITY = new
  Decision("Modularity", 5);

  public static final Decision decCLASS_SELECTION = new
  Decision("Class Selection", 5);

  public static final Decision decMETHODS = new
  Decision("methods", 5); //??

  public static final Decision decCODE_GEN = new
  Decision("Code Generation", 5); //??

  public static final Decision decPLANNED_EXTENSIONS = new
  Decision("Planned Extensions", 5);

    
  public CrUML() {
    //decisionCategory("UML Decisions");
    // what do UML critics have in common? anything?
  }

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
  }

} /* end class CrUML */
