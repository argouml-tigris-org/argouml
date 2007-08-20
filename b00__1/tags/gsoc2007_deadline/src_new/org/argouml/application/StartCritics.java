// $Id:StartCritics.java 13028 2007-07-10 18:00:08Z tfmorris $
// Copyright (c) 1996-2007 The Regents of the University of California. All
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

package org.argouml.application;

import org.apache.log4j.Logger;
import org.argouml.application.api.Argo;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.cognitive.Designer;
import org.argouml.configuration.Configuration;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.language.java.cognitive.critics.InitJavaCritics;
import org.argouml.model.Model;
import org.argouml.pattern.cognitive.critics.InitPatternCritics;
import org.argouml.uml.cognitive.UMLDecision;
import org.argouml.uml.cognitive.critics.ChildGenUML;
import org.argouml.uml.cognitive.critics.InitCognitiveCritics;

/**
 * StartCritics is a thread which helps to start the critiquing thread.
 */
public class StartCritics implements Runnable {
    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(StartCritics.class);

    /*
     * @see java.lang.Runnable#run()
     */
    public void run() {
        Designer dsgr = Designer.theDesigner();
        Main.initSubsystem(new InitCognitiveCritics());
        Main.initSubsystem(new InitJavaCritics());
        Main.initSubsystem(new InitPatternCritics());
        org.argouml.uml.cognitive.checklist.Init.init();
        Project p = ProjectManager.getManager().getCurrentProject();
        // set the icon for this poster
        dsgr.setClarifier(ResourceLoaderWrapper.lookupIconResource("PostItD0"));
        dsgr.setDesignerName(Configuration.getString(Argo.KEY_USER_FULLNAME));
        Configuration.addListener(Argo.KEY_USER_FULLNAME, dsgr); //MVW
        dsgr.spawnCritiquer(p);
        dsgr.setChildGenerator(new ChildGenUML());
        for (Object model : p.getUserDefinedModelList()) {
            Model.getPump().addModelEventListener(dsgr, model);
        }
        LOG.info("spawned critiquing thread");
        dsgr.getDecisionModel().startConsidering(UMLDecision.CLASS_SELECTION);
        dsgr.getDecisionModel().startConsidering(UMLDecision.BEHAVIOR);
        dsgr.getDecisionModel().startConsidering(UMLDecision.NAMING);
        dsgr.getDecisionModel().startConsidering(UMLDecision.STORAGE);
        dsgr.getDecisionModel().startConsidering(UMLDecision.INHERITANCE);
        dsgr.getDecisionModel().startConsidering(UMLDecision.CONTAINMENT);
        dsgr.getDecisionModel()
                .startConsidering(UMLDecision.PLANNED_EXTENSIONS);
        dsgr.getDecisionModel().startConsidering(UMLDecision.STATE_MACHINES);
        dsgr.getDecisionModel().startConsidering(UMLDecision.PATTERNS);
        dsgr.getDecisionModel().startConsidering(UMLDecision.RELATIONSHIPS);
        dsgr.getDecisionModel().startConsidering(UMLDecision.INSTANCIATION);
        dsgr.getDecisionModel().startConsidering(UMLDecision.MODULARITY);
        dsgr.getDecisionModel().startConsidering(UMLDecision.EXPECTED_USAGE);
        dsgr.getDecisionModel().startConsidering(UMLDecision.METHODS);
        dsgr.getDecisionModel().startConsidering(UMLDecision.CODE_GEN);
        dsgr.getDecisionModel().startConsidering(UMLDecision.STEREOTYPES);
        Designer.setUserWorking(true);
    }

} /* end class StartCritics */

