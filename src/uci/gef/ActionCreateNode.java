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

// File: ActionCreateNode.java
// Classes: ActionCreateNode
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.util.*;

/** Editor Action to create a new FigNode on a new NetNode. When
 *  this Action is executed it makes the new objects as per its
 *  arguments, and then it sets the global next mode to ModePlace so
 *  that the user can place the new node in any editor window.
 *
 * @see ModePlace
 * @see NetNode
 * @see FigNode */

public class ActionCreateNode extends Action {

  ////////////////////////////////////////////////////////////////
  // constants
  public static String DEFAULT_NODE_CLASS = "uci.gef.demo.SampleNode";

  ////////////////////////////////////////////////////////////////
  // instance variables

  /** The class of the new NetNode. */
  private Class _nodeClass;

  /** The place to get default node attributes for the new NetNode. */
  private NetNode _default_node;

  ////////////////////////////////////////////////////////////////
  // constructors

  /** Construct a new Action with the given arguments for node class. */
  public ActionCreateNode(Properties args) {
    super(args);
    String className = args.getProperty("className", DEFAULT_NODE_CLASS);
    try { _nodeClass = Class.forName(className); }
    catch (java.lang.ClassNotFoundException ignore) { return; }
  }

  /** Construct a new Action with the given classes for the NetNode
   *  and its FigNode. */
  public ActionCreateNode(String nodeClassName, String modelClassName) {
    super(new Properties());
    setArg("className", nodeClassName);
    if (modelClassName != null)
      setArg("modelClassName", modelClassName);
    try { _nodeClass = Class.forName(nodeClassName); }
    catch (java.lang.ClassNotFoundException ignore) {
        System.out.println("Class " + nodeClassName + " could not be found!");
        return; }
  }

  /** Construct a new Action with the given classes for the NetNode
   *  and its FigNode, and set the global sticky mode boolean to
   *  the given value. This allows the user to place several nodes
   *  rapidly.  */
  public ActionCreateNode(String node, String model, boolean sticky) {
    this(node, model);
    setArg("shouldBeSticky", sticky ? "True" : "False");
  }

  ////////////////////////////////////////////////////////////////
  // Action API

  public String name() { return "Create Node"; }

  /** Actually instanciate the NetNode and FigNode objects and
   * set the global next mode to ModePlace */
  public void doIt(java.awt.Event e) {
    NetNode newNode;
    Object model = null;
    try { newNode = (NetNode) _nodeClass.newInstance(); }
    catch (java.lang.IllegalAccessException ignore) { return; }
    catch (java.lang.InstantiationException ignore) { return; }
    // _default_node = newNode.defaults(); /* needs-more-work */

    String modelClassName = (String)getArg("modelClassName");
    if (modelClassName != null) {
      Class modelClass;
      try { modelClass = Class.forName(modelClassName); }
      catch (java.lang.ClassNotFoundException ignore) { return; }
      try { model = modelClass.newInstance(); }
      catch (java.lang.IllegalAccessException ignore) { return; }
      catch (java.lang.InstantiationException ignore) { return; }
      Editor ed = Globals.curEditor();
      newNode.initialize(_default_node, model);
    }
    else
      newNode.initialize(_default_node, null);

    Mode placeMode = new ModePlace(newNode);

    String shouldBeSticky = (String)getArg("shouldBeSticky");
    if (shouldBeSticky == null) Globals.mode(placeMode);
    else Globals.mode(placeMode, shouldBeSticky.equals("True"));
  }

  public void undoIt() {
    System.out.println("undo is not implemented");
  }

} /* end class ActionCreateNode */
