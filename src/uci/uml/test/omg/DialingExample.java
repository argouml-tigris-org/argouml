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
 *  page 104 of the UML 1.1 notation guide (OMG document ad/97-08-05). */


public class DialingExample {
  public Model model;
  public MMClass phoneClass;
  public CompositeState top, active;
  public Pseudostate activeInitial;
  public State idle, timeout, dialTone, dialing;
  public State invalid, connecting, busy, pinned, ringing, talking;

  public Transition timeoutDo, dialToneDo, invalidDo, busyDo, ringingDo;
  public Transition t01, t02, t03, t04, t05, t06, t07, t08, t09;
  public Transition t10, t11, t12, t13, t14;
  public StateMachine sm;
  
  public DialingExample() {
    try {
      model = new Model("DialingExample");
      phoneClass = new MMClass("Phone");
      sm = new StateMachine("States", phoneClass);
      
      top = new CompositeState();
      
      idle = new SimpleState("Idle");
      active = new CompositeState("Active");
      active.setIsConcurrent(false);
      // note: top does not have an initial state, this is an incompletness
      
      activeInitial = new Pseudostate(PseudostateKind.INITIAL);
      timeout = new SimpleState("Timeout");
      dialTone = new SimpleState("DialTone");
      dialing = new SimpleState("Dialing");
      invalid = new SimpleState("Invalid");
      connecting = new SimpleState("Connecting");
      busy = new SimpleState("Busy");
      pinned = new SimpleState("Pinned");
      ringing = new SimpleState("Ringing");
      talking = new SimpleState("Talking");

      top.addSubstate(idle);
      top.addSubstate(active);
      
      active.addSubstate(activeInitial);
      active.addSubstate(timeout);
      active.addSubstate(dialTone);
      active.addSubstate(dialing);
      active.addSubstate(invalid);
      active.addSubstate(connecting);
      active.addSubstate(busy);
      active.addSubstate(pinned);
      active.addSubstate(ringing);
      active.addSubstate(talking);

      sm.setTop(top);


      timeoutDo = new Transition(new Name("do"), timeout, timeout);
      timeoutDo.setEffect(new ActionSequence("play message"));
      dialToneDo = new Transition(new Name("do"), dialTone, dialTone);
      dialToneDo.setEffect(new ActionSequence("play dial tone"));
      invalidDo = new Transition(new Name("do"), invalid, invalid);
      invalidDo.setEffect(new ActionSequence("play message"));
      busyDo = new Transition(new Name("do"), busy, busy);
      busyDo.setEffect(new ActionSequence("play busy tone"));
      ringingDo = new Transition(new Name("do"), ringing, ringing);
      ringingDo.setEffect(new ActionSequence("play ringing tone"));

      
      timeout.addInternalTransition(timeoutDo);
      dialTone.addInternalTransition(dialToneDo);
      invalid.addInternalTransition(invalidDo);
      busy.addInternalTransition(busyDo);
      ringing.addInternalTransition(ringingDo);
      

      
      // these go roughtly clockwise in the figure from 9 o'clock
      t01 = new Transition(idle, active);
      t01.setTrigger(new Event("lift receiver"));
      t01.setEffect(new ActionSequence("get dial tone"));
      
      t02 = new Transition(dialTone, timeout);
      t02.setTrigger(new TimeEvent("after(15 sec.)"));
      
      t03 = new Transition(dialing, timeout);
      t03.setTrigger(new TimeEvent("after(15 sec.)"));

      t04 = new Transition(dialing, dialing);
      t04.setTrigger(new Event("dial digit(n)"));
      t04.setGuard(new Guard("incomplete"));
      
      t05 = new Transition(dialTone, dialing);
      t05.setTrigger(new Event("dial digit(n)"));

      t06 = new Transition(activeInitial, dialTone);

      t07 = new Transition(dialing, invalid);
      t07.setTrigger(new Event("dial digit(n)"));
      t07.setGuard(new Guard("invalid"));

      t08 = new Transition(dialing, connecting);
      t08.setTrigger(new Event("dial digit(n)"));
      t08.setGuard(new Guard("valid"));
      t08.setEffect(new ActionSequence("connect"));

      t09 = new Transition(connecting, busy);
      t09.setTrigger(new Event("busy"));

      t10 = new Transition(connecting, ringing);
      t10.setTrigger(new Event("connected"));

      t11 = new Transition(ringing, talking);
      t11.setTrigger(new Event("callee answers"));
      t11.setEffect(new ActionSequence("enable speech"));

      t12 = new Transition(talking, pinned);
      t11.setTrigger(new Event("callee hangs up"));

      t13 = new Transition(pinned, talking);
      t13.setTrigger(new Event("callee answers"));

      t14 = new Transition(active, idle);
      t14.setTrigger(new Event("caller hangs up"));
      t11.setEffect(new ActionSequence("disconnect"));
      
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
      sm.addTransition(t11);
      sm.addTransition(t12);
      sm.addTransition(t13);
      sm.addTransition(t14);

      model.addPublicOwnedElement(phoneClass);
    }
    catch (PropertyVetoException ex) {
      System.out.println("an veto execption occured in DialingExample");
    }
  }

  public void print() {
    //System.out.println(playerClass.dbgString());
    //System.out.println(teamClass.dbgString());
    //System.out.println(yearClass.dbgString());
    System.out.println(phoneClass.dbgString());
    System.out.println(sm.dbgString());
  }
  
} /* end class DialingExample */
