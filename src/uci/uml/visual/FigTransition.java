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



// File: FigTransition.java
// Classes: FigTransition
// Original Author: your email address here
// $Id$


package uci.uml.visual;

import java.awt.Color;
import java.beans.*;

import uci.gef.*;
import uci.uml.ui.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Behavioral_Elements.State_Machines.*;
import uci.uml.Behavioral_Elements.Common_Behavior.*;
import uci.uml.generate.*;

public class FigTransition extends FigEdgeModelElement {

  ////////////////////////////////////////////////////////////////
  // constructors
  public FigTransition() {
    addPathItem(_name, new PathConvPercent(this, 50, 10));
    _fig.setLineColor(Color.black);
    setDestArrowHead(new ArrowHeadGreater());
  }

  public FigTransition(Object edge) {
    this();
    setOwner(edge);
  }

  ////////////////////////////////////////////////////////////////
  // event handlers

  /** This method is called after the user finishes editing a text
   *  field that is in the FigEdgeModelElement.  Determine which field
   *  and update the model.  This class handles the name, subclasses
   *  should override to handle other text elements. */
  protected void textEdited(FigText ft) throws PropertyVetoException {
    String s = ft.getText();
    Transition newTrans = ParserDisplay.SINGLETON.parseTransition(s);
    if (newTrans == null) System.out.println("null new Transition!!");
    Name newName     = (newTrans == null) ? Name.UNSPEC : newTrans.getName();
    Event newTrigger = (newTrans == null) ? null : newTrans.getTrigger();
    Guard newGuard   = (newTrans == null) ? null : newTrans.getGuard();
    ActionSequence newEffect = (newTrans == null) ?
      null : newTrans.getEffect();

    Transition t = (Transition) getOwner();
    if (t == null) return;
    try {
      t.setName(newName);
      t.setTrigger(newTrigger);
      t.setGuard(newGuard);
      t.setEffect(newEffect);
    }
    catch (PropertyVetoException pve) {
      System.out.println("PropertyVetoException in FigTransition#textEdited");
    }
  }

  /** This is called aftern any part of the UML ModelElement has
   *  changed. This method automatically updates the name FigText.
   *  Subclasses should override and update other parts. */
  protected void modelChanged() {
    ModelElement me = (ModelElement) getOwner();
    if (me == null) return;
    //System.out.println("FigTransition modelChanged: " + me.getClass());
    String nameStr = GeneratorDisplay.Generate(me);
    _name.setText(nameStr);
  }

} /* end class FigTransition */

