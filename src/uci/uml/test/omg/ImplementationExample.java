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
import uci.uml.generate.*;

/** This is a very simple demo of how to represent a UML
 *  implementation diagram.  This example is taken from page 136 of
 *  the UML 1.1 notation guide (OMG document ad/97-08-05). */

// needs-more-work: these should be instances instead of MMClass, but
// I have not filled in the Link and LinkEnd parts of the meta-model yet.

public class ImplementationExample {
  public Model model;
  public MMClass node1, node2, cluster1, cluster2, database; //Instance
  public MMClass x1, y1, x2, y2, w, z; // Instance
  public Association a1, a2, a3, a11, a12, a21, a22, a31, a32;
  public Dependency d;
  
  public ImplementationExample() {
    try {
      model = new Model("ImplementationExample");
      Stereotype stCluster = new Stereotype("cluster");
      Stereotype stDatabase = new Stereotype("database");

      node1 = new MMClass("Node1");
      node1.addStereotype(Stereotype.NODE);
      node2 = new MMClass("Node2");
      node2.addStereotype(Stereotype.NODE);
      cluster1 = new MMClass("");
      cluster1.addStereotype(stCluster);
      cluster2 = new MMClass("");
      cluster2.addStereotype(stCluster);
      database = new MMClass("");
      database.addStereotype(stDatabase);
      x1 = new MMClass("x1");
      x2 = new MMClass("x2");
      y1 = new MMClass("y1");
      y2 = new MMClass("y2");
      w = new MMClass("w");
      z = new MMClass("z");


      /** Nesting of classes/objects in nodes is modeled by an
       *  association with a composite (black diamond) end. */
      a1 = new Association(node1, cluster1);
      a11 = new Association(cluster1, x1);
      a12 = new Association(cluster1, y1);

      a2 = new Association(node1, database);
      a21 = new Association(database, w);
      a22 = new Association(database, z);

      a3 = new Association(node2, cluster2);
      a31 = new Association(cluster1, x1);
      a32 = new Association(cluster1, y1);

      AssociationEnd ae;
      ae = (AssociationEnd) a1.getConnection().elementAt(0);
      ae.setAggregation(AggregationKind.COMPOSITE);
      ae = (AssociationEnd) a11.getConnection().elementAt(0);
      ae.setAggregation(AggregationKind.COMPOSITE);
      ae = (AssociationEnd) a12.getConnection().elementAt(0);
      ae.setAggregation(AggregationKind.COMPOSITE);

      ae = (AssociationEnd) a2.getConnection().elementAt(0);
      ae.setAggregation(AggregationKind.COMPOSITE);
      ae = (AssociationEnd) a21.getConnection().elementAt(0);
      ae.setAggregation(AggregationKind.COMPOSITE);
      ae = (AssociationEnd) a22.getConnection().elementAt(0);
      ae.setAggregation(AggregationKind.COMPOSITE);

      ae = (AssociationEnd) a3.getConnection().elementAt(0);
      ae.setAggregation(AggregationKind.COMPOSITE);
      ae = (AssociationEnd) a31.getConnection().elementAt(0);
      ae.setAggregation(AggregationKind.COMPOSITE);
      ae = (AssociationEnd) a32.getConnection().elementAt(0);
      ae.setAggregation(AggregationKind.COMPOSITE);

      
      model.addPublicOwnedElement(node1);
      model.addPublicOwnedElement(node2);
      model.addPublicOwnedElement(cluster1);
      model.addPublicOwnedElement(cluster2);
      model.addPublicOwnedElement(database);
      model.addPublicOwnedElement(x1);
      model.addPublicOwnedElement(y1);
      model.addPublicOwnedElement(x2);
      model.addPublicOwnedElement(y2);
      model.addPublicOwnedElement(w);
      model.addPublicOwnedElement(z);

    }
    catch (PropertyVetoException ex) {
      System.out.println("an veto execption occured in ImplementationExample");
    }
  }

  public void print() {
    System.out.println(GeneratorDisplay.Generate(model));
  }
  
} /* end class ImplementationExample */


