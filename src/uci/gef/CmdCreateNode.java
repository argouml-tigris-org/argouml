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

// File: CmdCreateNode.java
// Classes: CmdCreateNode
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.util.*;

import uci.graph.*;

/** Editor Cmd to create a new FigNode on a new NetNode. When
 *  this Cmd is executed it makes the new objects as per its
 *  arguments, and then it sets the global next mode to ModePlace so
 *  that the user can place the new node in any editor window.
 *
 * @see ModePlace
 * @see NetNode
 * @see FigNode */

public class CmdCreateNode extends Cmd {

  ////////////////////////////////////////////////////////////////
  // constants
  public static Class DEFAULT_NODE_CLASS = uci.gef.demo.SampleNode.class;

  ////////////////////////////////////////////////////////////////
  // instance variables

  // All instance variables are stored in the _args Hashtable
  
  ////////////////////////////////////////////////////////////////
  // constructors

  /** Construct a new Cmd with the given arguments for node class. */
  public CmdCreateNode(Hashtable args, String name) {
    super(args, name);
  }

  /** Construct a new Cmd with the given classes for the NetNode
   *  and its FigNode. */
  public CmdCreateNode(Class nodeClass, String name) {
    this(new Hashtable(), name);
    setArg("className", nodeClass);
  }

  /** Construct a new Cmd with the given classes for the NetNode
   *  and its FigNode, and set the global sticky mode boolean to
   *  the given value. This allows the user to place several nodes
   *  rapidly.  */
  public CmdCreateNode(Class nodeClass, boolean sticky, String name) {
    this(nodeClass, name);
    setArg("shouldBeSticky", sticky ? Boolean.TRUE : Boolean.FALSE);
  }

  ////////////////////////////////////////////////////////////////
  // Cmd API

  /** Actually instanciate the NetNode and FigNode objects and
   * set the global next mode to ModePlace */
  public void doIt() {
    Object newNode;
    Class nodeClass = (Class) getArg("className", DEFAULT_NODE_CLASS);
    //assert _nodeClass != null
    try { newNode = nodeClass.newInstance(); }
    catch (java.lang.IllegalAccessException ignore) { return; }
    catch (java.lang.InstantiationException ignore) { return; }

    if (newNode instanceof GraphNodeHooks)
      ((GraphNodeHooks)newNode).initialize(_args);

    Mode placeMode = new ModePlace(newNode);

    Object shouldBeSticky = getArg("shouldBeSticky");
    Globals.mode(placeMode, shouldBeSticky == Boolean.TRUE);
  }

  public void undoIt() {
    System.out.println("undo is not implemented");
  }

} /* end class CmdCreateNode */
