// Copyright (c) 1996-98 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation for educational, research and non-profit
// purposes, without fee, and without a written agreement is hereby granted,
// provided that the above copyright notice and this paragraph appear in all
// copies. Permission to incorporate this software into commercial products may
// be obtained by contacting the University of California. David F. Redmiles
// Department of Information and Computer Science (ICS) University of
// California Irvine, California 92697-3425 Phone: 714-824-3823. This software
// program and documentation are copyrighted by The Regents of the University
// of California. The software program and documentation are supplied "as is",
// without any accompanying services from The Regents. The Regents do not
// warrant that the operation of the program will be uninterrupted or
// error-free. The end-user understands that the program was developed for
// research purposes and is advised not to rely exclusively on the program for
// any reason. IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY
// PARTY FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES,
// INCLUDING LOST PROFITS, ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS
// DOCUMENTATION, EVEN IF THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY
// DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
// WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE
// SOFTWARE PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,
// ENHANCEMENTS, OR MODIFICATIONS.


// Source file: Behavioral_Elements/State_Machines/StateMachine.java

package uci.uml.Behavioral_Elements.State_Machines;

import java.util.*;

import uci.uml.Foundation.Core.*;


public class StateMachine extends ModelElementImpl {
  public ModelElement _context;
  public State _top;
  //% public Transition _transitions[];
  public Vector _transitions;
  //% public SubmachineState _submachineState[];
  public Vector _submachineState;
  
  public StateMachine() { }

  public ModelElement getContext() { return _context; }
  public void setContext(ModelElement x) {
    _context = x;
  }

  public State getTop() { return _top; }
  public void setTop(State x) {
    _top = x;
  }

  public Vector getTransitions() { return _transitions; }
  public void setTransitions(Vector x) {
    _transitions = x;
  }
  public void addTransitions(Transition x) {
    if (_transitions == null) _transitions = new Vector();
    _transitions.addElement(x);
  }
  public void removeTransitions(Transition x) {
    _transitions.removeElement(x);
  }

  public Vector getSubmachineState() { return _submachineState; }
  public void setSubmachineState(Vector x) {
    _submachineState = x;
  }
  public void addSubmachineState(SubmachineState x) {
    if (_submachineState == null) _submachineState = new Vector();
    _submachineState.addElement(x);
  }
  public void removeSubmachineState(SubmachineState x) {
    _submachineState.removeElement(x);
  }
  
}
