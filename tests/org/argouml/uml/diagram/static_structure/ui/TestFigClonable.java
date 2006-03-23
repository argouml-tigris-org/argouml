// $Id$
// Copyright (c) 2005 The Regents of the University of California. All
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

package org.argouml.uml.diagram.static_structure.ui;

import junit.framework.TestCase;

/**
 * Testcase to clone all Figs in static_structure.ui.
 */
public class TestFigClonable extends TestCase {

    /**
     * The constructor.
     *
     * @param name the test name
     */
    public TestFigClonable(String name) {
        super(name);
    }


    /**
     * Try to clone {@link FigClass}.
     */
    public void testClassClonable() {

	FigClass fig = new FigClass();
	FigClass figclone;

	figclone = (FigClass) fig.clone();

    }

    /**
     * Try to clone {@link FigEdgeNote}.
     */
    public void testEdgeNoteClonable() {

	FigEdgeNote fig = new FigEdgeNote();
	FigEdgeNote figclone;

	figclone = (FigEdgeNote) fig.clone();

    }

    /**
     * Try to clone {@link FigInstance}.
     */
    public void testInstanceClonable() {

	FigInstance fig = new FigInstance();
	FigInstance figclone;

	figclone = (FigInstance) fig.clone();

    }

    /**
     * Try to clone {@link FigInterface}.
     */
    public void testInterfaceClonable() {

	FigInterface fig = new FigInterface();
	FigInterface figclone;

	figclone = (FigInterface) fig.clone();

    }

    /**
     * Try to clone {@link FigLink}.
     */
    public void testLinkClonable() {

	FigLink fig = new FigLink();
	FigLink figclone;

	figclone = (FigLink) fig.clone();

    }

    /**
     * Try to clone {@link FigComment}.
     */
    public void testNoteClonable() {

	FigComment fig = new FigComment();
	FigComment figclone;

	figclone = (FigComment) fig.clone();

    }
}
