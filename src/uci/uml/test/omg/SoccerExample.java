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
import uci.uml.Model_Management.*;

/** This is a very simple demo of how to represent a UML design that
 *  consists of three classes and one 3-way association with an
 *  association class.  This example is taken from page 62 of the UML
 *  1.1 notation guide (OMG document ad/97-08-05). */


public class SoccerExample {
  public Model model;
  public MMClass teamClass, yearClass, playerClass;
  public AssociationClass recordAC;
  
  public SoccerExample() {
    try {
      model = new Model("SoccerExample");
      playerClass = new MMClass("Player");
      yearClass = new MMClass("Year");
      teamClass = new MMClass("Team");

      recordAC = new AssociationClass("Record");
      recordAC.addStructuralFeature(new Attribute("goals for"));
      recordAC.addStructuralFeature(new Attribute("goals against"));
      recordAC.addStructuralFeature(new Attribute("wins"));
      recordAC.addStructuralFeature(new Attribute("loses"));
      recordAC.addStructuralFeature(new Attribute("ties"));

      AssociationEnd ae1 =
	new AssociationEnd(new Name("team"), teamClass,
			   Multiplicity.ZERO_OR_MORE, AggregationKind.NONE);
      AssociationEnd ae2 =
	new AssociationEnd(new Name("season"), yearClass,
			   Multiplicity.ZERO_OR_MORE, AggregationKind.NONE);
      AssociationEnd ae3 =
	new AssociationEnd(new Name("goalKeeper"), playerClass,
			   Multiplicity.ZERO_OR_MORE, AggregationKind.NONE);


      recordAC.addConnection(ae1);
      recordAC.addConnection(ae2);
      recordAC.addConnection(ae3);

      model.addPublicOwnedElement(playerClass);
      model.addPublicOwnedElement(teamClass);
      model.addPublicOwnedElement(yearClass);
      model.addPublicOwnedElement(recordAC);
    }
    catch (PropertyVetoException ex) {
      System.out.println("an veto execption occured in SoccerExample");
    }
  }

  public void print() {
    //System.out.println(playerClass.dbgString());
    //System.out.println(teamClass.dbgString());
    //System.out.println(yearClass.dbgString());
    System.out.println(recordAC.dbgString());
  }
  
} /* end class GraphicsExample */
