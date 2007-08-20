// $Id:Poster.java 12950 2007-07-01 08:10:04Z tfmorris $
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

package org.argouml.cognitive;

import java.util.List;

import javax.swing.Icon;


/**
 * Interface that defines methods required on any object that can
 * post a ToDoItem to the Designer's ToDoList. Basically requires that
 * the poster (1) have contact information, (2) be able to snooze
 * and unsnooze itself, and (3) be able to determine if a ToDoItem it
 * posted previously should still be on the Designer's ToDoList. <p>
 *
 * Currently Critic and Designer implement this interface.
 *
 * @see org.argouml.cognitive.Critic
 * @see Designer
 * @author Jason Robbins
 * @since 0.25.4 An incompatible change was made to the API so that methods
 * which used to return Vector now return List<listType>.  Introducing a new
 * interface with the different methods would have been just as incompatible,
 * so we just changed this one.
 */
public interface Poster {

    ////////////////////////////////////////////////////////////////
    // accessors

    /**
     * Reply true if the given item should be kept on the Designer's
     * ToDoList, false if it is no longer valid.
     *
     * @param i the todo item
     * @param d the designer
     * @return true if thisitem is still valid
     */
    boolean stillValid(ToDoItem i, Designer d);

    /**
     * @param d the decision
     * @return true if the decision is still supported
     */
    boolean supports(Decision d);

    /**
     * @return the list of supported decisions
     * @since 0.25.4 An incompatible change was made to the return type. It used
     *        to return Vector.  Deprecation wasn't used since this is only used
     *        one place in ArgoUML.
     */
    List<Decision> getSupportedDecisions();

    /**
     * @param g the goal
     * @return true if the goal is still supported
     */
    boolean supports(Goal g);

    /**
     * @return the list of supported goals
     * @since 0.25.4 An incompatible change was made to the return type. It used
     *        to return Vector.  Deprecation wasn't used since this is only used
     *        one place in ArgoUML.
     */
    List<Goal> getSupportedGoals();

    /**
     * @param knowledgeType the knowledge type
     * @return true if it is valid
     */
    boolean containsKnowledgeType(String knowledgeType);

    /**
     * Customize the description string just before it is displayed.
     *
     * @param desc the description
     * @param offs the offenders
     * @return the customized/expanded string
     */
    String expand(String desc, ListSet offs);

    /**
     * @return the icon shown on the todo item to show the wizard's progress
     */
    Icon getClarifier();

    ////////////////////////////////////////////////////////////////
    // criticism control

    /**
     * Temporarily disable this Poster.
     */
    void snooze();

    /**
     * Unsnooze this Poster, it may resume posting without further
     * delay.
     */
    void unsnooze();

    ////////////////////////////////////////////////////////////////
    // issue resolution

    /**
     * TODO: Not implemented yet. If the given ToDoItem can
     * be fixed automatically, and the user wants that to happen, then do
     * it. Obviously, this depends on the specific Critic and
     * problem. By default this method does nothing.
     *
     * @param item the todo item
     * @param arg the design material (?)
     */
    void fixIt(ToDoItem item, Object arg);

    /**
     * @param item the todo item
     * @return true if it can be fixed
     */
    boolean canFixIt(ToDoItem item);

} /* end interface Poster */
