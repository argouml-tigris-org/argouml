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

package org.argouml.uml.diagram.static_structure.ui;

import junit.framework.*;

/** Testcase to clone all Figs in static_structure.ui. */
public class TestFigClonable extends TestCase {
    public TestFigClonable(String name) {
        super(name);
    }


    /** try to clone FigClass in package diagram.static_structure.ui.
     */
    public void testClassClonable() {

            FigClass fig = new FigClass();
            FigClass figclone;
             
            figclone = (FigClass) fig.clone();

    }

    /** try to clone FigEdgeNote in package diagram.static_structure.ui.
     */
    public void testEdgeNoteClonable() {

            FigEdgeNote fig = new FigEdgeNote();
            FigEdgeNote figclone;
             
            figclone = (FigEdgeNote) fig.clone();

    }

    /** try to clone FigInstance in package diagram.static_structure.ui.
     */
    public void testInstanceClonable() {

            FigInstance fig = new FigInstance();
            FigInstance figclone;
             
            figclone = (FigInstance) fig.clone();

    }

    /** try to clone FigInterface in package diagram.static_structure.ui.
     */
    public void testInterfaceClonable() {

            FigInterface fig = new FigInterface();
            FigInterface figclone;
             
            figclone = (FigInterface) fig.clone();

    }

    /** try to clone FigLink in package diagram.static_structure.ui.
     */
    public void testLinkClonable() {

            FigLink fig = new FigLink();
            FigLink figclone;
             
            figclone = (FigLink) fig.clone();

    }


    /** try to clone FigModel in package diagram.static_structure.ui.
     */
    public void testModelClonable() {

            FigModel fig = new FigModel();
            FigModel figclone;
             
            figclone = (FigModel) fig.clone();

    }

    /** try to clone FigSubsystem in package diagram.static_structure.ui.
     */
    public void testSubsystemClonable() {

            FigSubsystem fig = new FigSubsystem();
            FigSubsystem figclone;
             
            figclone = (FigSubsystem) fig.clone();

    }
    
    

    /** try to clone FigComment in package diagram.static_structure.ui.
     */
    public void testNoteClonable() {

            FigComment fig = new FigComment();
            FigComment figclone;
             
            figclone = (FigComment) fig.clone();

    }


    /** try to clone FigPackage in package diagram.static_structure.ui.
     */
    public void testPackageClonable() {

            FigPackage fig = new FigPackage();
            FigPackage figclone;
             
            figclone = (FigPackage) fig.clone();

    }


}
