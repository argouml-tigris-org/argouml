// Copyright (c) 1995, 1996 Regents of the University of California.
// All rights reserved.
//
// This software was developed by the Arcadia project
// at the University of California, Irvine.
//
// Redistribution and use in source and binary forms are permitted
// provided that the above copyright notice and this paragraph are
// duplicated in all such forms and that any documentation,
// advertising materials, and other materials related to such
// distribution and use acknowledge that the software was developed
// by the University of California, Irvine.  The name of the
// University may not be used to endorse or promote products derived
// from this software without specific prior written permission.
// THIS SOFTWARE IS PROVIDED ``AS IS'' AND WITHOUT ANY EXPRESS OR
// IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
// WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.

// File: IEventHandler.java
// Classes: IEventHandler
// Original Author: jrobbins@ics.uci.edu
// $Id$


package uci.util;

import java.awt.*;

/** This interface basically lists the event handling methods of
 *  JDK1.0.2. I want to define my own objects that handle events in a
 *  familiar way, but that do not subclass from java.awt.Component.
 *  Needs-More-Work: this might all be replaced when I go 100% Java
 *  1.1. */

public interface IEventHandler extends java.io.Serializable {

  public abstract boolean handleEvent(Event e);
  public abstract boolean mouseEnter(Event e, int x, int y);
  public abstract boolean mouseExit(Event e, int x, int y);
  public abstract boolean mouseUp(Event e, int x, int y);
  public abstract boolean mouseDown(Event e, int x, int y);
  public abstract boolean mouseDrag(Event e, int x, int y);
  public abstract boolean mouseMove(Event e, int x, int y);
  public abstract boolean keyUp(Event e, int key);
  public abstract boolean keyDown(Event e, int key);
  public abstract boolean handleMenuEvent(Event e);

} /* end interface IEventHandler */
