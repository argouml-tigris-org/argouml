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
 *  page 106 of the UML 1.1 notation guide (OMG document ad/97-08-05). */


public class PasswordExample {
  public Model model;
  public MMClass passwordClass;
  public CompositeState top;
  public State typingPassword;
  public Transition character, help;
  public StateMachine sm;
  
  public PasswordExample() {
    try {
      model = new Model("PasswordExample");
      passwordClass = new MMClass("PasswordClass");
      sm = new StateMachine("States", passwordClass);
      
      top = new CompositeState();
      
      typingPassword = new SimpleState("Idle");

      top.addSubstate(typingPassword);
      
      sm.setTop(top);


      character = new Transition(new Name("do"), typingPassword, typingPassword);
      character.setEffect(new ActionSequence("handle character"));
      help = new Transition(new Name("do"), typingPassword, typingPassword);
      help.setEffect(new ActionSequence("display help"));

      typingPassword.setEntry(new ActionSequence("set echo invisible"));
      typingPassword.setExit(new ActionSequence("set echo normal"));
      typingPassword.addInternalTransition(character);
      typingPassword.addInternalTransition(help);

      model.addPublicOwnedElement(passwordClass);
    }
    catch (PropertyVetoException ex) {
      System.out.println("an veto execption occured in PasswordExample");
    }
  }

  public void print() {
    System.out.println(passwordClass.dbgString());
    System.out.println(sm.dbgString());
  }
  
} /* end class PasswordExample */
