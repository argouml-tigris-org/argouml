// Copyright (c) 1996-2002 The Regents of the University of California. All
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

package org.argouml.uml.diagram.state.ui;

import junit.framework.*;

/** Tests whether Figs in state.ui are clonable, apart from FigStateVertex which is abstract. */
public class TestFigClonable extends TestCase {
    public TestFigClonable(String name) {
        super(name);
    }

    /** try to clone FigBranchState in package diagram.state.ui.
     */
    public void testBranchStateClonable() {
        try {
            FigBranchState fig = new FigBranchState();
            FigBranchState figclone;
             
            figclone = (FigBranchState) fig.clone();
            assert("FigBranchState cloned", true);
        }
        catch(Exception e) {
            assert("FigBranchState not clonable", false);
        }
    }

    /** try to clone FigCompositeState in package diagram.state.ui.
     */
    public void testCompositeStateClonable() {
        try {
            FigCompositeState fig = new FigCompositeState();
            FigCompositeState figclone;
             
            figclone = (FigCompositeState) fig.clone();
            assert("FigCompositeState cloned", true);
        }
        catch(Exception e) {
            assert("FigCompositeState not clonable", false);
        }
    }

    /** try to clone FigDeepHistoryState in package diagram.state.ui.
     */
    public void testDeepHistoryStateClonable() {
        try {
            FigDeepHistoryState fig = new FigDeepHistoryState();
            FigDeepHistoryState figclone;
             
            figclone = (FigDeepHistoryState) fig.clone();
            assert("FigDeepHistoryState cloned", true);
        }
        catch(Exception e) {
            assert("FigDeepHistoryState not clonable", false);
        }
    }

    /** try to clone FigFinalState in package diagram.state.ui.
     */
    public void testFinalStateClonable() {
        try {
            FigFinalState fig = new FigFinalState();
            FigFinalState figclone;
             
            figclone = (FigFinalState) fig.clone();
            assert("FigFinalState cloned", true);
        }
        catch(Exception e) {
            assert("FigFinalState not clonable", false);
        }
    }


    /** try to clone FigForkState in package diagram.state.ui.
     */
    public void testForkStateClonable() {
        try {
            FigForkState fig = new FigForkState();
            FigForkState figclone;
             
            figclone = (FigForkState) fig.clone();
            assert("FigForkState cloned", true);
        }
        catch(Exception e) {
            assert("FigForkState not clonable", false);
        }
    }

    /** try to clone FigHistoryState in package diagram.state.ui.
     */
    public void testHistoryStateClonable() {
        try {
            FigHistoryState fig = new FigHistoryState();
            FigHistoryState figclone;
             
            figclone = (FigHistoryState) fig.clone();
            assert("FigHistoryState cloned", true);
        }
        catch(Exception e) {
            assert("FigHistoryState not clonable", false);
        }
    }

    /** try to clone FigInitialState in package diagram.state.ui.
     */
    public void testInitialStateClonable() {
        try {
            FigInitialState fig = new FigInitialState();
            FigInitialState figclone;
             
            figclone = (FigInitialState) fig.clone();
            assert("FigInitialState cloned", true);
        }
        catch(Exception e) {
            assert("FigInitialState not clonable", false);
        }
    }


    /** try to clone FigJoinState in package diagram.state.ui.
     */
    public void testJoinStateClonable() {
        try {
            FigJoinState fig = new FigJoinState();
            FigJoinState figclone;
             
            figclone = (FigJoinState) fig.clone();
            assert("FigJoinState cloned", true);
        }
        catch(Exception e) {
            assert("FigJoinState not clonable", false);
        }
    }

    /** try to clone FigShallowHistoryState in package diagram.state.ui.
     */
    public void testShallowHistoryStateClonable() {
        try {
            FigShallowHistoryState fig = new FigShallowHistoryState();
            FigShallowHistoryState figclone;
             
            figclone = (FigShallowHistoryState) fig.clone();
            assert("FigShallowHistoryState cloned", true);
        }
        catch(Exception e) {
            assert("FigShallowHistoryState not clonable", false);
        }
    }

    /** try to clone FigState in package diagram.state.ui.
     */
    public void testStateClonable() {
        try {
            FigState fig = new FigState();
            FigState figclone;
             
            figclone = (FigState) fig.clone();
            assert("FigState cloned", true);
        }
        catch(Exception e) {
            assert("FigState not clonable", false);
        }
    }


    /** try to clone FigTransistion in package diagram.state.ui.
     */
    public void testTransitionClonable() {
        try {
            FigTransition fig = new FigTransition();
            FigTransition figclone;
             
            figclone = (FigTransition) fig.clone();
            assert("FigTransition cloned", true);
        }
        catch(Exception e) {
            assert("FigTransition not clonable", false);
        }
    }


}
