// Copyright (c) 1996-2001 The Regents of the University of California. All
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

// File: CompartmentFigText.java
// Classes: CompartmentFigText
// Original Author: thn

package org.argouml.uml.diagram.ui;

import java.awt.*;
import java.awt.event.*;

import org.argouml.ui.*;
import org.tigris.gef.presentation.*;
import ru.novosoft.uml.foundation.core.*;

/** A FigText class extension for FigClass/FigInterface compartments */

public class CompartmentFigText extends FigText
{

  ////////////////////////////////////////////////////////////////
  // instance variables

  protected Fig refFig;
  protected int storedKey = -1; // nothing stored
  protected boolean _isHighlighted = false;
  protected MFeature _feature = null;

  ////////////////////////////////////////////////////////////////
  // constructors

  public CompartmentFigText(int x, int y, int w, int h, Fig aFig) {
    super(x,y,w,h,true);
    refFig = aFig; // not allowed: null (is not handled!)
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  // The following method overrides are necessary for proper graphical behavior
  public void setLineWidth(int w) {super.setLineWidth(0);}
  public int getLineWidth() {return 1;}
  public boolean getFilled() {return true;}
  public Color getFillColor() {return refFig.getFillColor();}
  public Color getLineColor() {return refFig.getLineColor();}

  /*
  public FigTextEditor startTextEditor(InputEvent ie) {
    FigTextEditor te = new CompartmentFigTextEditor(this, ie);
	_editMode = true;
    return te;
  }
  */

  public void setStoredKey(int keyCode) {
    storedKey = keyCode;
  }

  public int getStoredKey() {
    return storedKey;
  }

  public void setHighlighted(boolean flag) {
    _isHighlighted = flag;
    super.setLineWidth(_isHighlighted ? 1 : 0);
    if (flag && _feature != null)
	    ProjectBrowser.TheInstance.setTarget(_feature);
  }

  public boolean isHighlighted() {
    return _isHighlighted;
  }

  public void setFeature(MFeature feature) {
    _feature = feature;
  }

  public MFeature getFeature() {
    return _feature;
  }

  ////////////////////////////////////////////////////////////////
  // event handlers

  /** This method handles backspace, enter and up/down arrow keys. */
  public void keyPressed(KeyEvent ke) {
	  System.out.println("CompartmentFigText.keyPressed");
	setStoredKey(ke.getKeyCode());
	super.keyPressed(ke);
	System.out.println("key pressed");
	/*
    if (!ke.isActionKey() && !isNonStartEditingKey(ke)) {
      if (!_editable) return;
      CompartmentFigTextEditor te = (CompartmentFigTextEditor)startTextEditor(ke);
      te.keyPressed(ke);
      ke.consume();
    }
    */
  }

  ////////////////////////////////////////////////////////////////
  // inner classes

  /*
  public class CompartmentFigTextEditor extends FigTextEditor {
    public CompartmentFigTextEditor(FigText ft, InputEvent ie) {
      super(ft,ie);
      System.out.println("new CompartmentFigTextEditor: "+ft.getText());
    }
    public void keyPressed(KeyEvent ke) {
      int keyCode = ke.getKeyCode();
      if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_DOWN) {
        setStoredKey(keyCode);
        endEditing();
        ke.consume();
	  } else
	    super.keyPressed(ke);
    }
  }
  */
}
