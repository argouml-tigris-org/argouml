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

/** This is a very simple demo of how to represent a UML state machine
 *  that deals with dialing a telephone.  This example is taken from
 *  page 113 of the UML 1.1 notation guide (OMG document ad/97-08-05). */


public class ComplexTransExample {
  public Model model;
  public MMClass complexClass;
  public CompositeState top, s1, a, b;
  public State setup, cleanup, a1, a2, b1, b2;
  public Transition t01, t02, t03, t04, t05, t06, t07, t08;
  public Pseudostate fork, join;
  public StateMachine sm;
  
  public ComplexTransExample() {
    try {
      model = new Model("ComplexTransExample");
      complexClass = new MMClass("ComplexClass");
      sm = new StateMachine("States", complexClass);
      
      top = new CompositeState();
      
      top.addSubstate(s1 = new CompositeState());
      top.addSubstate(setup = new SimpleState("Setup"));
      top.addSubstate(cleanup = new SimpleState("Cleanup"));
      top.addSubstate(fork = new Pseudostate(PseudostateKind.FORK));
      top.addSubstate(join = new Pseudostate(PseudostateKind.JOIN));
      s1.addSubstate(a = new CompositeState());
      a.addSubstate(a1 = new SimpleState("A1"));
      a.addSubstate(a2 = new SimpleState("A2"));
      s1.addSubstate(b = new CompositeState());
      b.addSubstate(b1 = new SimpleState("B1"));
      b.addSubstate(b2 = new SimpleState("B2"));

      sm.setTop(top);

      sm.addTransition(t01 = new Transition(setup, fork));
      sm.addTransition(t02 = new Transition(fork, a1));
      sm.addTransition(t03 = new Transition(fork, b1));
      sm.addTransition(t04 = new Transition(a1, a2));
      sm.addTransition(t05 = new Transition(b1, b2));
      sm.addTransition(t06 = new Transition(a2, join));
      sm.addTransition(t07 = new Transition(b2, join));
      sm.addTransition(t08 = new Transition(join, cleanup));

      model.addPublicOwnedElement(complexClass);
      
    }
    catch (PropertyVetoException ex) {
      System.out.println("an veto execption occured in ComplexTransExample");
    }


  }


  public void print() {
    System.out.println(complexClass.dbgString());
    System.out.println(sm.dbgString());
  }
} /* end class ComplexTransExample */
