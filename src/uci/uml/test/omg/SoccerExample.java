package uci.uml.test.omg;

import java.util.*;
import java.beans.*;


import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Core.Class;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Foundation.Extension_Mechanisms.*;
import uci.uml.Model_Management.*;

/** This is a very simple demo of how to represent a UML design that
 *  consists of three classes and one 3-way association with an
 *  association class.  This example is taken from page 62 of the UML
 *  1.1 notation guide (OMG document ad/97-08-05). */


public class SoccerExample {
  public Model model;
  public Class teamClass, yearClass, playerClass;
  public AssociationClass recordAC;
  
  public SoccerExample() {
    try {
      model = new Model("default");
      playerClass = new Class("Player");
      yearClass = new Class("Year");
      teamClass = new Class("Team");

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
      
      //System.out.println(playerClass.dbgString());
      //System.out.println(teamClass.dbgString());
      //System.out.println(yearClass.dbgString());
      System.out.println(recordAC.dbgString());
    }
    catch (PropertyVetoException ex) {
      System.out.println("an veto execption occured in SoccerExample");
    }


  }

} /* end class GraphicsExample */
