// $Id$
// Copyright (c) 2008 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

package org.argouml.application.api;

import java.util.ArrayList;
import java.util.Collection;

import junit.framework.TestCase;

import org.argouml.cognitive.checklist.ui.InitCheckListUI;
import org.argouml.cognitive.ui.InitCognitiveUI;
import org.argouml.language.java.cognitive.critics.InitJavaCritics;
import org.argouml.model.InitializeModel;
import org.argouml.moduleloader.InitModuleLoader;
import org.argouml.notation.InitNotation;
import org.argouml.notation.providers.java.InitNotationJava;
import org.argouml.notation.providers.uml.InitNotationUml;
import org.argouml.notation.ui.InitNotationUI;
import org.argouml.pattern.cognitive.critics.InitPatternCritics;
import org.argouml.profile.init.InitProfileSubsystem;
import org.argouml.ui.ProjectBrowser;
import org.argouml.ui.cmd.InitUiCmdSubsystem;
import org.argouml.uml.cognitive.critics.InitCognitiveCritics;
import org.argouml.uml.diagram.activity.ui.InitActivityDiagram;
import org.argouml.uml.diagram.collaboration.ui.InitCollaborationDiagram;
import org.argouml.uml.diagram.deployment.ui.InitDeploymentDiagram;
import org.argouml.uml.diagram.sequence.ui.InitSequenceDiagram;
import org.argouml.uml.diagram.state.ui.InitStateDiagram;
import org.argouml.uml.diagram.static_structure.ui.InitClassDiagram;
import org.argouml.uml.diagram.ui.InitDiagramAppearanceUI;
import org.argouml.uml.diagram.use_case.ui.InitUseCaseDiagram;
import org.argouml.uml.ui.InitUmlUI;

/**
 * Verify the contract of InitSubsystem implementations.
 *
 * @author Michiel
 */
public class GUITestInitSubsystem extends TestCase {

    private Collection<InitSubsystem> initialisers;

    protected void setUp() {
        /* These 3 steps are only needed for 
         * the 2 cases indicated with a X below. */
        InitializeModel.initializeDefault();
        new InitProfileSubsystem().init();
        ProjectBrowser.makeInstance(null, false, null);

        initialisers = new ArrayList<InitSubsystem>();
        initialisers.add(new InitActivityDiagram());
        initialisers.add(new InitCheckListUI());
        initialisers.add(new InitClassDiagram());
        initialisers.add(new InitCognitiveCritics());
        initialisers.add(new InitCognitiveUI());
        initialisers.add(new InitCollaborationDiagram());
        initialisers.add(new InitDeploymentDiagram());
        initialisers.add(new InitDiagramAppearanceUI());
        initialisers.add(new InitJavaCritics());
        initialisers.add(new InitModuleLoader());
        initialisers.add(new InitNotation());
        initialisers.add(new InitNotationJava());
        initialisers.add(new InitNotationUI());
        initialisers.add(new InitNotationUml());
        initialisers.add(new InitPatternCritics());
        initialisers.add(new InitSequenceDiagram());
        initialisers.add(new InitStateDiagram());
        initialisers.add(new InitUiCmdSubsystem()); // X
        initialisers.add(new InitUmlUI()); // X
        initialisers.add(new InitUseCaseDiagram());
    }

    /**
     * Test if the methods that should 
     * return a List do not return null.
     */
    public void testContract() {
        for (InitSubsystem i : initialisers) {
            i.init(); // Obey the contract ourselves!
            assertNotNull(i.getDetailsTabs());
            assertNotNull(i.getSettingsTabs());
            assertNotNull(i.getProjectSettingsTabs());
        }
    }
}
