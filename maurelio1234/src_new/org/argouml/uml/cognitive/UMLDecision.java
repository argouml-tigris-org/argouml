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

package org.argouml.uml.cognitive;

import org.argouml.cognitive.Decision;

/**
 * UMLDecision is a utility class which provides a namespace for standard
 * decisions in the problem domain of UML Modelling.
 *
 * @stereotype utility
 */
public class UMLDecision extends Decision {
    /**
     * Decision type: INHERITANCE.
     */
    public static final UMLDecision INHERITANCE =
        new UMLDecision(
            "misc.decision.inheritance", 1);

    /**
     * Decision type: CONTAINMENT.
     */
    public static final UMLDecision CONTAINMENT =
        new UMLDecision(
            "misc.decision.containment", 1);

    /**
     * Decision type: PATTERNS.
     */
    public static final UMLDecision PATTERNS =
        new UMLDecision(
            "misc.decision.design-patterns", 1); //??

    /**
     * Decision type: RELATIONSHIPS.
     */
    public static final UMLDecision RELATIONSHIPS =
        new UMLDecision(
            "misc.decision.relationships", 1);

    /**
     * Decision type: STORAGE.
     */
    public static final UMLDecision STORAGE =
        new UMLDecision(
            "misc.decision.storage", 1);

    /**
     * Decision type: BEHAVIOR.
     */
    public static final UMLDecision BEHAVIOR =
        new UMLDecision(
            "misc.decision.behavior", 1);

    /**
     * Decision type: INSTANCIATION.
     */
    public static final UMLDecision INSTANCIATION =
        new UMLDecision(
            "misc.decision.instantiation", 1);

    /**
     * Decision type: NAMING.
     */
    public static final UMLDecision NAMING =
        new UMLDecision(
            "misc.decision.naming", 1);

    /**
     * Decision type: MODULARITY.
     */
    public static final UMLDecision MODULARITY =
        new UMLDecision(
            "misc.decision.modularity", 1);

    /**
     * Decision type: CLASS_SELECTION.
     */
    public static final UMLDecision CLASS_SELECTION =
        new UMLDecision(
            "misc.decision.class-selection", 1);

    /**
     * Decision type: EXPECTED_USAGE.
     */
    public static final UMLDecision EXPECTED_USAGE =
        new UMLDecision(
            "misc.decision.expected-usage", 1);

    /**
     * Decision type: METHODS.
     */
    public static final UMLDecision METHODS =
        new UMLDecision(
            "misc.decision.methods", 1); //??

    /**
     * Decision type: CODE_GEN.
     */
    public static final UMLDecision CODE_GEN =
        new UMLDecision(
            "misc.decision.code-generation", 1); //??

    /**
     * Decision type: PLANNED_EXTENSIONS.
     */
    public static final UMLDecision PLANNED_EXTENSIONS =
        new UMLDecision(
            "misc.decision.planned-extensions", 1);

    /**
     * Decision type: STEREOTYPES.
     */
    public static final UMLDecision STEREOTYPES =
        new UMLDecision(
            "misc.decision.stereotypes", 1);

    /**
     * Decision type: STATE_MACHINES.
     */
    public static final UMLDecision STATE_MACHINES =
        new UMLDecision(
            "misc.decision.mstate-machines", 1);

    /**
     * The constructor.
     *
     * @param name the localized decision name key
     * @param prio the priority
     */
    public UMLDecision(String name, int prio) {
        super(name, prio);
    }
}
