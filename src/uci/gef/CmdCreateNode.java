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

public class CmdCreateNode extends Cmd implements GraphFactory {

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
    Editor ce = Globals.curEditor();
    GraphModel gm = ce.getGraphModel();
    if (!(gm instanceof MutableGraphModel)) return;


    Mode placeMode = new ModePlace(this);

    Object shouldBeSticky = getArg("shouldBeSticky");
    Globals.mode(placeMode, shouldBeSticky == Boolean.TRUE);
  }

  public void undoIt() {
    System.out.println("undo is not implemented");
  }

  ////////////////////////////////////////////////////////////////
  // GraphFactory implementation

  public GraphModel makeGraphModel() { return null; }
  public Object makeEdge() { return null; }

  public Object makeNode() {
    Object newNode;
    Class nodeClass = (Class) getArg("className", DEFAULT_NODE_CLASS);
    //assert _nodeClass != null
    try { newNode = nodeClass.newInstance(); }
    catch (java.lang.IllegalAccessException ignore) { return null; }
    catch (java.lang.InstantiationException ignore) { return null; }

    if (newNode instanceof GraphNodeHooks)
      ((GraphNodeHooks)newNode).initialize(_args);
    return newNode;
  }

  static final long serialVersionUID = 6058024926862904714L;

} /* end class CmdCreateNode */
