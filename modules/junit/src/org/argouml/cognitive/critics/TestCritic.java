// Copyright (c) 1996-2001 The Regents of the University of California. All

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



package org.argouml.cognitive.critics;

import junit.framework.*;



import org.apache.log4j.*;

import org.argouml.cognitive.*;

import javax.swing.*;





public class TestCritic extends TestCase {

    public TestCritic(String name) {

	super(name);

    }



    public void testCreate0() {

	Critic c = new Critic();

	assert("getCriticCategory()", "unclassified".equals(c.getCriticCategory()));

	assert("getCriticName()", "Critic".equals(c.getCriticName()));

	assert("predicate()", !c.predicate(new Object(), new Designer()));

    }



    /** Function never actually called. Provided in order to make

     *  sure that the static interface has not changed.

     */

    private void compileTestStatics() {

	boolean b;

	b = Critic.PROBLEM_FOUND;

	b = Critic.NO_PROBLEM;



	String s;

	s = Critic.ENABLED;

	s = Critic.SNOOZE_ORDER;

	s = Critic.KT_DESIGNERS;

	s = Critic.KT_CORRECTNESS;

	s = Critic.KT_COMPLETENESS;

	s = Critic.KT_CONSISTENCY;

	s = Critic.KT_SYNTAX;

	s = Critic.KT_SEMANTICS;

	s = Critic.KT_OPTIMIZATION;

	s = Critic.KT_PRESENTATION;

	s = Critic.KT_ORGANIZATIONAL;

	s = Critic.KT_EXPERIENCIAL;

	s = Critic.KT_TOOL;



	Category c;

	c = Critic.cat;



	Icon ic;

	ic = Critic.DEFAULT_CLARIFIER;



	int i;

	i = Critic._numCriticsFired;

    }



    /** Function never actually called. Provided in order to make

     *  sure that the public interface has not changed.

     */

    private void compileTestConstructors() {

	new Critic();

    }



    private void compileTestMethods() {

	Critic.reasonCodeFor(new String());

    }

}

