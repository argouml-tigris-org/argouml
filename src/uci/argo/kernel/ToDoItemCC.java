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
