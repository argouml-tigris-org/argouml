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

// File: ToDoItemCC.java
// Classes: ToDoItemCC
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.argo.kernel;


/** This class defines the feedback items that can be placed on the
 *  Designer's ToDoList.  The main point of a ToDoItem is to inform
 *  the Designer of some problem or open design issue.  Additional
 *  information in the ToDoItem helps put the designer in a mental
 *  context suitable for resolving the issue: ToDoItem's are well tied
 *  into the design and design process so that the Designer can see
 *  which design material's are the subject of this ToDoItem, and which
 *  Critic raised it.  The expert email address helps connect the
 *  designer with the organizational context.  The more info URL helps
 *  provide background knowledge of the domain. In the future
 *  ToDoItems will include ties back to the design rationale log.
 *  Also the run-time system needs to know who posted each ToDoItem so
 *  that it can automatically remove it if it is no longer valid. */

public class ToDoItemCC extends ToDoItem
implements java.io.Serializable {
  ////////////////////////////////////////////////////////////////
  // instance variables

  /** The context that caused the critic to produce this ToDoItemCC */
  private CCContext _context;


  ////////////////////////////////////////////////////////////////
  // constructors

  public ToDoItemCC(Critic crit, CCContext context) {
    super(crit);
    _context = context;
  }

  ////////////////////////////////////////////////////////////////
  // accessors


  public CCContext getContext() { return _context; }
  public void setContext(CCContext c) { _context = c; }

  ////////////////////////////////////////////////////////////////
  // issue resolutions


} /* end class ToDoItemCC */
