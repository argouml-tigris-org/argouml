// $Id: Clarifier.java 10734 2006-06-11 15:43:58Z mvw $
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

package org.argouml.ui;

import javax.swing.Icon;
import org.argouml.cognitive.ToDoItem;
import org.tigris.gef.presentation.Fig;

/**
 * This interface represents a "Clarifier", i.e. the red wavy line over e.g.
 * a class name, attribute or operation compartiment, to indicate the
 * existence of a critic.
 *
 */
public interface Clarifier extends Icon {
    /**
     * @param f the Fig on which the clarifier is to be painted
     */
    public void setFig(Fig f);

    /**
     * @param i the todo item that is visualised by this clarifier
     */
    public void setToDoItem(ToDoItem i);

    /**
     * Reply true if the given point is inside the given Fig.
     *
     * @param x the x of the given point
     * @param y the y of the given point
     * @return true if hit
     */
    public boolean hit(int x, int y);
} /* end interface Clarifier */
