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
 *  page 108 of the UML 1.1 notation guide (OMG document ad/97-08-05). */


public class ConcurrentSubstatesExample {
  public Model model;
  public MMClass courseClass;
  public CompositeState taking, incomplete, labTrack, projTrack, testTrack;
  public Pseudostate takingInitial, labInitial, projInitial,
    testInitial, labFinal, projFinal, testFinal;
  public State lab1, lab2, termProject, finalTest;
  public State passed, failed;

  public Transition t01, t02, t03, t04, t05, t06, t07, t08, t09, t10;
  public StateMachine sm;
  
  public ConcurrentSubstatesExample() {
    try {
      model = new Model("ConcurrentSubstatesExample");
      courseClass = new MMClass("Course");
      sm = new StateMachine("States", courseClass);
      
      taking = new CompositeState("Taking Class");
      
      taking.addSubstate(incomplete = new CompositeState("Incomplete"));
      taking.addSubstate(passed = new SimpleState("Passed"));
      taking.addSubstate(failed = new SimpleState("Failed"));
      taking.addSubstate(takingInitial = new Pseudostate(PseudostateKind.INITIAL));
      
      incomplete.setIsConcurrent(true);
      incomplete.addSubstate(labTrack = new CompositeState("Lab Track"));
      incomplete.addSubstate(projTrack = new CompositeState("Term Project Track"));
      incomplete.addSubstate(testTrack = new CompositeState("Final Test Track"));

      labTrack.addSubstate(labInitial = new Pseudostate(PseudostateKind.INITIAL));
      labTrack.addSubstate(lab1 = new SimpleState("Lab1"));
      labTrack.addSubstate(lab2 = new SimpleState("Lab2"));
      labTrack.addSubstate(labFinal = new Pseudostate(PseudostateKind.FINAL));
      
      projTrack.addSubstate(projInitial = new Pseudostate(PseudostateKind.INITIAL));
      projTrack.addSubstate(termProject = new SimpleState("Term Project"));
      projTrack.addSubstate(projFinal = new Pseudostate(PseudostateKind.FINAL));

      testTrack.addSubstate(testInitial = new Pseudostate(PseudostateKind.INITIAL));
      testTrack.addSubstate(finalTest = new SimpleState("Final Test"));
      testTrack.addSubstate(testFinal = new Pseudostate(PseudostateKind.FINAL));


      sm.setTop(taking);


      // these go roughtly top to bottom, left to righ in the figure
      t01 = new Transition(takingInitial, incomplete);
      
      t02 = new Transition(labInitial, lab1);
      
      t03 = new Transition(lab1, lab2);
      t03.setTrigger(new Event("lab done"));

      t04 = new Transition(lab2, labFinal);
      t04.setTrigger(new Event("lab done"));

      t05 = new Transition(projInitial, termProject);

      t06 = new Transition(termProject, projFinal);
      t06.setTrigger(new Event("project done"));
      
      t07 = new Transition(testInitial, finalTest);

      t08 = new Transition(finalTest, testFinal);
      t08.setTrigger(new Event("pass"));

      t09 = new Transition(finalTest, failed);
      t09.setTrigger(new Event("fail"));

      t10 = new Transition(incomplete, passed);

      sm.addTransition(t01);
      sm.addTransition(t02);
      sm.addTransition(t03);
      sm.addTransition(t04);
      sm.addTransition(t05);
      sm.addTransition(t06);
      sm.addTransition(t07);
      sm.addTransition(t08);
      sm.addTransition(t09);
      sm.addTransition(t10);

      model.addPublicOwnedElement(courseClass);
    }
    catch (PropertyVetoException ex) {
      System.out.println("an veto execption in ConcurrentSubstatesExample");
    }
  }

  public void print() {       
    System.out.println(courseClass.dbgString());
    System.out.println(sm.dbgString());
  }


} /* end class ConcurrentSubstatesExample */
