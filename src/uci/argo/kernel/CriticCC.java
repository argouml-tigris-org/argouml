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



// File: CriticCC.java
// Classes: CriticCC
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.argo.kernel;

import java.util.*;
import uci.util.*;

/** */

public class CriticCC extends Critic {

  ////////////////////////////////////////////////////////////////
  // instance variables

  protected CCNetwork _network;

  ////////////////////////////////////////////////////////////////
  // constructor

  /**  */
  public CriticCC() { }


  ////////////////////////////////////////////////////////////////
  // critiquing

  /** Examine the given Object and Designer and, if
   *  appropriate, produce one or more ToDoItem's and add them to the
   *  offending design material's and the Designer's ToDoList. By
   *  default this is basically a simple if-statement that relies on
   *  predicate() to determine if there is some appropriate feedback,
   *  and toDoItem() to produce the ToDoItem.
   *
   *  The predicate() and toDoItem() pair of methods is simple and
   *  convient for many critics. More sophisticated critics that
   *  produce more than one ToDoItem per critiquing, or that produce
   *  ToDoItem's that contain information that was already computed in
   *  the predicate, should override critique. If you override this
   *  method, you should call super.critique().
   *
   * @see Critic#predicate
   # @see Critic#toDoItem */
  public void critique(Object dm, Designer dsgr) {
    System.out.println("applying CriticCC: " + getHeadline());
    CCContext detectContext = new CCContext(dm, dsgr, _network);
    detectContext.run();

    if (detectContext.fired(this)) {
      System.out.println(this.toString() + " fired");
      CCContext fixItContext = new CCContext(detectContext, this);
      ToDoItemCC item = new ToDoItemCC(this, detectContext);
      if (dm instanceof DesignMaterial) ((DesignMaterial)dm).inform(item);
      dsgr.inform(item);
    }
    else {
      System.out.println(this.toString() + " found " + dm.toString() + " OK ");
    }
  }

  /** This is not used in CriticCC. */
  public boolean predicate(Object dm, Designer dsgr) {
    return false;
  }

  /** Return true iff the given ToDoItem is still valid and should be
   *  kept in the given designers ToDoList. Critics that are not
   *  enabled should always return false so that their ToDoItems will
   *  be removed. Subclasses of Critic that supply multiple offenders
   *  should always override this method. <p>
   *
   *  By default this method basically asks the critic to again
   *  critique the offending Object and then it checks if the
   *  resulting ToDoItem is the same as the one already posted. This is
   *  simple and it works fine for light-weight critics. Critics that
   *  expend a lot of computational effort in making feedback that can
   *  be easily check to see if it still holds, should override this
   *  method. <p>
   *
   *  Needs-More-Work: Maybe ToDoItem should carry some data to make
   *  this method more efficient. */
  public boolean stillValid(ToDoItem i, Designer dsgr) {
    if (!isActive()) return false;
    // needs-more-work: not implemented yet
    return true;
  }



  ////////////////////////////////////////////////////////////////
  // design feedback

  /** This is not used by CriticCC */
  public ToDoItem toDoItem(Object dm, Designer dsgr) {
    return null;
  }

} /* end class Critic */
