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
 *  page 115 of the UML 1.1 notation guide (OMG document ad/97-08-05). */


public class StubbedTransExample {
  public Model model;
  public MMClass stubbedClass;
  public CompositeState top, w;
  public State a, b, c, d, e, f;
  public Transition t01, t02, t03, t04, t05, t06, t07, t08;
  public Pseudostate wInitial, wFinal, fork, join;
  public StateMachine sm;
  
  public StubbedTransExample() {
    try {
      model = new Model("StubbedTransExample");
      stubbedClass = new MMClass("StubbedClass");
      sm = new StateMachine("States", stubbedClass);
      
      top = new CompositeState();
      
      top.addSubstate(w = new CompositeState());
      top.addSubstate(a = new SimpleState("A"));
      top.addSubstate(b = new SimpleState("B"));
      top.addSubstate(c = new SimpleState("C"));
      top.addSubstate(d = new SimpleState("D"));

      w.addSubstate(e = new SimpleState("E"));
      w.addSubstate(f = new SimpleState("F"));
      w.addSubstate(wInitial = new Pseudostate(PseudostateKind.INITIAL));
      w.addSubstate(wFinal = new Pseudostate(PseudostateKind.FINAL));

      sm.setTop(top);

      sm.addTransition(t01 = new Transition(new Name("P"), a, e));
      sm.addTransition(t02 = new Transition(new Name("r"), b, w));
      sm.addTransition(t03 = new Transition(new Name("U"), e, f));
      sm.addTransition(t04 = new Transition(wInitial, f));
      sm.addTransition(t05 = new Transition(new Name("T"), f, wFinal));
      sm.addTransition(t06 = new Transition(new Name("S"), e, c));
      sm.addTransition(t07 = new Transition(new Name("S"), w, c));
      sm.addTransition(t08 = new Transition(w, d));

      model.addPublicOwnedElement(stubbedClass);
    }
    catch (PropertyVetoException ex) {
      System.out.println("an veto execption occured in StubbedTransExample");
    }
  }

  public void print() {
    System.out.println(stubbedClass.dbgString());
    System.out.println(sm.dbgString());
  }

} /* end class StubbedTransExample */
