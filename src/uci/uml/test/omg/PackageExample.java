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




package uci.uml.test.omg;

import java.util.*;
import java.beans.*;


import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Foundation.Extension_Mechanisms.*;
import uci.uml.Behavioral_Elements.Common_Behavior.*;
import uci.uml.Behavioral_Elements.State_Machines.*;
import uci.uml.Model_Management.*;

/** This is a very simple demo of how to represent nested UML
 *  packages. This example is taken from page 15 of the UML 1.1
 *  notation guide (OMG document ad/97-08-05). */


public class PackageExample {
  public Model windowingSystem, motif, msWindows;
  public Subsystem editor;
  public Model controller, domainElements, diagramElements;
  public Model graphicsCore, motifCore, windowsCore;
  public Generalization g1, g2, g3, g4;
  public Dependency d1, d2, d3, d4, d5, d6, d7, d8;
  
  public PackageExample() {
    try {
      windowingSystem = new Model("Windowing System");
      motif = new Model("Motif");
      msWindows = new Model("MS Windows)");
      editor = new Subsystem("Editor");
      controller = new Model("controller");
      domainElements = new Model("Domain Elements");
      diagramElements = new Model("Diagram Elements");
      graphicsCore = new Model("Graphics Core");
      motifCore = new Model("Motif Core");      
      windowsCore = new Model("Windows Core");

      g1 = new Generalization(motif, windowingSystem);
      g2 = new Generalization(msWindows, windowingSystem);
      g3 = new Generalization(motifCore, graphicsCore);
      g4 = new Generalization(windowsCore, graphicsCore);

      d1 = new Dependency(graphicsCore, windowingSystem);
      d2 = new Dependency(motifCore, motif);
      d3 = new Dependency(windowsCore, msWindows);
      d4 = new Dependency(controller, domainElements);
      d5 = new Dependency(diagramElements, domainElements);
      d6 = new Dependency(controller, graphicsCore);
      d7 = new Dependency(diagramElements, graphicsCore);
      d8 = new Dependency(controller, diagramElements);

      editor.addPublicOwnedElement(controller);
      editor.addPublicOwnedElement(domainElements);
      editor.addPublicOwnedElement(diagramElements);
      editor.addPublicOwnedElement(graphicsCore);
      editor.addPublicOwnedElement(motifCore);
      editor.addPublicOwnedElement(windowsCore);
    }
    catch (PropertyVetoException ex) {
      System.out.println("an veto execption in PackageExample");
    }
  }
  
  public void print() {       
    System.out.println(windowingSystem.dbgString());
    System.out.println(motif.dbgString());
    System.out.println(msWindows.dbgString());
    System.out.println(editor.dbgString());
    System.out.println(controller.dbgString());
    System.out.println(domainElements.dbgString());
    System.out.println(diagramElements.dbgString());
    System.out.println(graphicsCore.dbgString());
    System.out.println(motifCore.dbgString());
    System.out.println(windowsCore.dbgString());

    System.out.println(d1.dbgString());
    System.out.println(d2.dbgString());
    System.out.println(d3.dbgString());
    System.out.println(d4.dbgString());
    System.out.println(d5.dbgString());
    System.out.println(d6.dbgString());
    System.out.println(d7.dbgString());
    System.out.println(d8.dbgString());    
  }

  
} /* end class PackageExample */
