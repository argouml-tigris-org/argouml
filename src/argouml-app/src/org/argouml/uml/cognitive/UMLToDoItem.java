// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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


/*
 * UMLToDoItem.java
 *
 * Created on December 14, 2003, 4:19 PM
 */

package org.argouml.uml.cognitive;

import java.util.Iterator;

import org.argouml.cognitive.Critic;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.Highlightable;
import org.argouml.cognitive.ListSet;
import org.argouml.cognitive.Poster;
import org.argouml.cognitive.ToDoItem;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.ui.ProjectActions;


/**
 * UMLToDoItem is the preferred class for newly created ToDoItems within
 * ArgoUML. It knows more about possible designmaterial and can for example
 * highlight offenders when they are ModelElements by finding the according Fig
 * in the current diagram for them.
 *
 * @see org.argouml.cognitive.ToDoItem
 * @since 0.15.3
 * @author  mkl
 */
public class UMLToDoItem extends ToDoItem {

    /**
     * The constructor.
     *
     * @param poster the poster
     * @param h the headline
     * @param p the priority
     * @param d the description
     * @param m the more-info-url
     * @param offs the offenders
     */
    public UMLToDoItem(Poster poster, String h, int p, String d, String m,
    ListSet offs) {
        super(poster, h, p, d, m, offs);
    }

    /**
     * The constructor.
     *
     * @param poster the poster
     * @param h the headline
     * @param p the priority
     * @param d the description
     * @param m the more-info-url
     */
    public UMLToDoItem(Poster poster, String h, int p, String d, String m) {
        super(poster, h, p, d, m);
    }

    /**
     * The constructor.
     *
     * @param c the poster (critic)
     * @param dm the offenders
     * @param dsgr the designer
     */
    public UMLToDoItem(Critic c, Object dm, Designer dsgr) {
        super(c, dm, dsgr);
    }

    /**
     * The constructor.
     *
     * @param c the poster (critic)
     * @param offs the offenders
     * @param dsgr the designer
     */
    public UMLToDoItem(Critic c, ListSet offs, Designer dsgr) {
        super(c, offs, dsgr);
    }

    /**
     * The constructor.
     *
     * @param c the critic that created this todoitem
     */
    public UMLToDoItem(Critic c) {
        super(c);
    }

    /**
     * Action jumps to the diagram containing all or most of the
     * offenders and calls {@link #deselect()}, {@link #select()}
     * around the call to
     * {@link ProjectActions#jumpToDiagramShowing(java.util.List)}.
     */
    @Override
    public void action() {
        deselect();
        // this also sets the target as a convenient side effect
        ProjectActions.jumpToDiagramShowing(getOffenders());
        select();
    }

    /*
     * @see org.argouml.cognitive.ToDoItem#deselect()
     */
    @Override
    public void deselect() {
        Project p = ProjectManager.getManager().getCurrentProject();
        for (Object dm : getOffenders()) {
            if (dm instanceof Highlightable) {
                ((Highlightable) dm).setHighlight(false);
	    } else if (p != null) {
                Iterator iterFigs = p.findFigsForMember(dm).iterator();
                while (iterFigs.hasNext()) {
                    Object f = iterFigs.next();
                    if (f instanceof Highlightable) {
                        ((Highlightable) f).setHighlight(false);
		    }
                }
            }
        }
    }

    /*
     * @see org.argouml.cognitive.ToDoItem#select()
     */
    @Override
    public void select() {
        Project p = ProjectManager.getManager().getCurrentProject();
        for (Object dm : getOffenders()) {
            if (dm instanceof Highlightable) {
                ((Highlightable) dm).setHighlight(true);
	    } else if (p != null) {
                Iterator iterFigs = p.findFigsForMember(dm).iterator();
                while (iterFigs.hasNext()) {
                    Object f = iterFigs.next();
                    if (f instanceof Highlightable) {
                        ((Highlightable) f).setHighlight(true);
		    }
                }
            }
        }
    }
}
