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
import uci.uml.Behavioral_Elements.Use_Cases.*;
import uci.uml.Model_Management.*;

/** This is a very simple demo of how to represent some UML Use
 *  Cases. This example is taken from pages 76-79 of the UML 1.1
 *  notation guide (OMG document ad/97-08-05). */


public class TelephoneCatalogExample {
  public Model model; // UseCaseModel?
  public Actor customer, salesperson, shippingclerk, supervisor;
  public UseCase checkStatus, placeOrder, fillOrders, establishCredit;
  public UseCase requestCatalog, arrangePayment, orderProduct, supplyCustomerData;
  public Association a1, a2, a3, a4, a5, a6, a7;
  public Generalization g1, g2, g3, g4;

  
  public TelephoneCatalogExample() {
    try {
      model = new Model("Telephone Catalog"); // UseCaseModel?
      customer = new Actor("customer");
      salesperson = new Actor("salesperson");
      shippingclerk = new Actor("shippingclerk");
      supervisor = new Actor("supervisor");

      checkStatus = new UseCase("checkStatus");
      placeOrder = new UseCase("placeOrder");
      fillOrders = new UseCase("fillOrders");
      establishCredit = new UseCase("establishCredit");
      requestCatalog = new UseCase("requestCatalog");
      arrangePayment = new UseCase("arrangePayment");
      orderProduct = new UseCase("orderProduct");
      supplyCustomerData = new UseCase("supplyCustomerData");

      a1 = new Association(customer, checkStatus);
      a2 = new Association(customer, placeOrder);
      a3 = new Association(customer, establishCredit);
      a4 = new Association(salesperson, checkStatus);
      a5 = new Association(salesperson, placeOrder);
      a6 = new Association(shippingclerk, fillOrders);
      a7 = new Association(supervisor, establishCredit);

      g1 = new Generalization(requestCatalog, placeOrder);
      g1.addStereotype(Stereotype.EXTENDS);
      g2 = new Generalization(placeOrder, supplyCustomerData);
      g2.addStereotype(Stereotype.USES);
      g3 = new Generalization(placeOrder, orderProduct);
      g3.addStereotype(Stereotype.USES);
      g4 = new Generalization(placeOrder, arrangePayment);
      g4.addStereotype(Stereotype.USES);

      placeOrder.addExtensionPoint("additional requests");

      model.addPublicOwnedElement(customer);
      model.addPublicOwnedElement(salesperson);
      model.addPublicOwnedElement(shippingclerk);
      model.addPublicOwnedElement(supplyCustomerData);

      model.addPublicOwnedElement(checkStatus);
      model.addPublicOwnedElement(placeOrder);
      model.addPublicOwnedElement(fillOrders);
      model.addPublicOwnedElement(establishCredit);
      model.addPublicOwnedElement(requestCatalog);
      model.addPublicOwnedElement(arrangePayment);
      model.addPublicOwnedElement(orderProduct);
      model.addPublicOwnedElement(supplyCustomerData);

      model.addPublicOwnedElement(a1);
      model.addPublicOwnedElement(a2);
      model.addPublicOwnedElement(a3);
      model.addPublicOwnedElement(a4);
      model.addPublicOwnedElement(a5);
      model.addPublicOwnedElement(a6);
      model.addPublicOwnedElement(a7);

      model.addPublicOwnedElement(g1);
      model.addPublicOwnedElement(g2);
      model.addPublicOwnedElement(g3);
      model.addPublicOwnedElement(g4);
      
    }
    catch (PropertyVetoException ex) {
      System.out.println("an veto execption in TelephoneCatalogExample");
    }
  }
  
  public void print() {       
    System.out.println(customer.dbgString());
    System.out.println(salesperson.dbgString());
    System.out.println(shippingclerk.dbgString());
    System.out.println(supervisor.dbgString());
    System.out.println(placeOrder.dbgString());
    System.out.println(fillOrders.dbgString());
    System.out.println(checkStatus.dbgString());
    System.out.println(establishCredit.dbgString());
    // needs-more-work:...

    System.out.println(a1.dbgString());
    System.out.println(a2.dbgString());
    System.out.println(a3.dbgString());
    System.out.println(a4.dbgString());
    System.out.println(a5.dbgString());
    System.out.println(a6.dbgString());
    System.out.println(a7.dbgString());

//     System.out.println(g1.dbgString());
//     System.out.println(g2.dbgString());
//     System.out.println(g3.dbgString());
//     System.out.println(g4.dbgString());    
  }

  
} /* end class TelephoneCatalogExample */
