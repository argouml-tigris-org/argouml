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

package org.argouml.ui.explorer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.argouml.application.api.Argo;
import org.argouml.application.api.Configuration;
import org.argouml.ui.explorer.rules.GoAssocRoleToMessages;
import org.argouml.ui.explorer.rules.GoBehavioralFeatureToStateDiagram;
import org.argouml.ui.explorer.rules.GoBehavioralFeatureToStateMachine;
import org.argouml.ui.explorer.rules.GoClassToAssociatedClass;
import org.argouml.ui.explorer.rules.GoClassToNavigableClass;
import org.argouml.ui.explorer.rules.GoClassToSummary;
import org.argouml.ui.explorer.rules.GoClassifierToBehavioralFeature;
import org.argouml.ui.explorer.rules.GoClassifierToCollaboration;
import org.argouml.ui.explorer.rules.GoClassifierToInstance;
import org.argouml.ui.explorer.rules.GoClassifierToSequenceDiagram;
import org.argouml.ui.explorer.rules.GoClassifierToStateMachine;
import org.argouml.ui.explorer.rules.GoClassifierToStructuralFeature;
import org.argouml.ui.explorer.rules.GoCollaborationToDiagram;
import org.argouml.ui.explorer.rules.GoCollaborationToInteraction;
import org.argouml.ui.explorer.rules.GoComponentToResidentModelElement;
import org.argouml.ui.explorer.rules.GoCompositeStateToSubvertex;
import org.argouml.ui.explorer.rules.GoDiagramToEdge;
import org.argouml.ui.explorer.rules.GoDiagramToNode;
import org.argouml.ui.explorer.rules.GoElementToMachine;
import org.argouml.ui.explorer.rules.GoEnumerationToLiterals;
import org.argouml.ui.explorer.rules.GoGeneralizableElementToSpecialized;
import org.argouml.ui.explorer.rules.GoInteractionToMessages;
import org.argouml.ui.explorer.rules.GoLinkToStimuli;
import org.argouml.ui.explorer.rules.GoMessageToAction;
import org.argouml.ui.explorer.rules.GoModelElementToBehavior;
import org.argouml.ui.explorer.rules.GoModelElementToComment;
import org.argouml.ui.explorer.rules.GoModelElementToContainedDiagrams;
import org.argouml.ui.explorer.rules.GoModelElementToContainedLostElements;
import org.argouml.ui.explorer.rules.GoModelElementToContents;
import org.argouml.ui.explorer.rules.GoModelToBaseElements;
import org.argouml.ui.explorer.rules.GoModelToCollaboration;
import org.argouml.ui.explorer.rules.GoModelToDiagrams;
import org.argouml.ui.explorer.rules.GoModelToElements;
import org.argouml.ui.explorer.rules.GoModelToNode;
import org.argouml.ui.explorer.rules.GoNamespaceToClassifierAndPackage;
import org.argouml.ui.explorer.rules.GoNamespaceToDiagram;
import org.argouml.ui.explorer.rules.GoNamespaceToOwnedElements;
import org.argouml.ui.explorer.rules.GoNodeToResidentComponent;
import org.argouml.ui.explorer.rules.GoOperationToCollaboration;
import org.argouml.ui.explorer.rules.GoOperationToCollaborationDiagram;
import org.argouml.ui.explorer.rules.GoOperationToSequenceDiagram;
import org.argouml.ui.explorer.rules.GoPackageToClass;
import org.argouml.ui.explorer.rules.GoProjectToCollaboration;
import org.argouml.ui.explorer.rules.GoProjectToDiagram;
import org.argouml.ui.explorer.rules.GoProjectToModel;
import org.argouml.ui.explorer.rules.GoProjectToStateMachine;
import org.argouml.ui.explorer.rules.GoSignalToReception;
import org.argouml.ui.explorer.rules.GoStateMachineToState;
import org.argouml.ui.explorer.rules.GoStateMachineToTop;
import org.argouml.ui.explorer.rules.GoStateMachineToTransition;
import org.argouml.ui.explorer.rules.GoStateToDoActivity;
import org.argouml.ui.explorer.rules.GoStateToDownstream;
import org.argouml.ui.explorer.rules.GoStateToEntry;
import org.argouml.ui.explorer.rules.GoStateToExit;
import org.argouml.ui.explorer.rules.GoStateToIncomingTrans;
import org.argouml.ui.explorer.rules.GoStateToInternalTrans;
import org.argouml.ui.explorer.rules.GoStateToOutgoingTrans;
import org.argouml.ui.explorer.rules.GoStatemachineToDiagram;
import org.argouml.ui.explorer.rules.GoStereotypeToTagDefinition;
import org.argouml.ui.explorer.rules.GoStimulusToAction;
import org.argouml.ui.explorer.rules.GoSubmachineStateToStateMachine;
import org.argouml.ui.explorer.rules.GoSummaryToAssociation;
import org.argouml.ui.explorer.rules.GoSummaryToAttribute;
import org.argouml.ui.explorer.rules.GoSummaryToIncomingDependency;
import org.argouml.ui.explorer.rules.GoSummaryToInheritance;
import org.argouml.ui.explorer.rules.GoSummaryToOperation;
import org.argouml.ui.explorer.rules.GoSummaryToOutgoingDependency;
import org.argouml.ui.explorer.rules.GoTransitionToGuard;
import org.argouml.ui.explorer.rules.GoTransitionToSource;
import org.argouml.ui.explorer.rules.GoTransitionToTarget;
import org.argouml.ui.explorer.rules.GoTransitiontoEffect;
import org.argouml.ui.explorer.rules.GoUseCaseToExtensionPoint;
import org.argouml.ui.explorer.rules.PerspectiveRule;

/**
 * Provides a model and event management for perspectives(views) of the
 * Explorer.<p>
 *
 * This class defines the complete list of perspective rules, and knows the
 * default perspectives and their contents.
 *
 * @author alexb
 * @since 0.15.2
 */
public final class PerspectiveManager {
    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(PerspectiveManager.class);

    private static PerspectiveManager instance;

    private List perspectiveListeners;

    private List perspectives;

    private List rules;

    /**
     * @return the instance (singleton)
     */
    public static PerspectiveManager getInstance() {
        if (instance == null) {
            instance = new PerspectiveManager();
        }
        return instance;
    }

    /**
     * Creates a new instance of PerspectiveManager.
     */
    private PerspectiveManager() {

        perspectiveListeners = new ArrayList();
        perspectives = new ArrayList();
        rules = new ArrayList();
        loadRules();
    }

    /**
     * @param listener
     *            the listener to be added
     */
    public void addListener(PerspectiveManagerListener listener) {
        perspectiveListeners.add(listener);
    }

    /**
     * @param listener
     *            the listener to be removed
     */
    public void removeListener(PerspectiveManagerListener listener) {
        perspectiveListeners.remove(listener);
    }

    /**
     * @param perspective
     *            the perspective to be added
     */
    public void addPerspective(Object perspective) {
        perspectives.add(perspective);
        Iterator listenerIt = perspectiveListeners.iterator();
        while (listenerIt.hasNext()) {

            PerspectiveManagerListener listener =
                (PerspectiveManagerListener) listenerIt.next();

            listener.addPerspective(perspective);
        }
    }

    /**
     * @param newPerspectives
     *            the collection of perspectives to be added
     */
    public void addAllPerspectives(Collection newPerspectives) {

        Iterator newPerspectivesIt = newPerspectives.iterator();
        while (newPerspectivesIt.hasNext()) {

            Object newPerspective = newPerspectivesIt.next();
            addPerspective(newPerspective);
        }
    }

    /**
     * @param perspective
     *            the perspective to be removed
     */
    public void removePerspective(Object perspective) {

        perspectives.remove(perspective);
        Iterator listenerIt = perspectiveListeners.iterator();
        while (listenerIt.hasNext()) {

            PerspectiveManagerListener listener =
                (PerspectiveManagerListener) listenerIt.next();

            listener.removePerspective(perspective);
        }
    }

    /**
     * Remove all perspectives.
     */
    public void removeAllPerspectives() {

        List pers = new ArrayList();
        pers.addAll(getPerspectives());
        for (int i = 0; i < pers.size(); i++) {
            removePerspective(pers.get(i));
        }
    }

    /**
     * @return the list of all persppectives
     */
    public List getPerspectives() {
        return perspectives;
    }

    /**
     * Tries to load user defined perspectives, if it can't it loads the
     * (predefined) default perspectives.
     */
    public void loadUserPerspectives() {

        try {
            String userPerspectives =
                Configuration.getString(
                    Argo.KEY_USER_EXPLORER_PERSPECTIVES, "");

            StringTokenizer pst = new StringTokenizer(userPerspectives, ";");

            if (pst.hasMoreTokens()) {

                // load user perspectives
                while (pst.hasMoreTokens()) {
                    String perspective = pst.nextToken();
                    StringTokenizer perspectiveDetails =
                        new StringTokenizer(perspective, ",");

                    // get the perspective name
                    String perspectiveName = perspectiveDetails.nextToken();

                    ExplorerPerspective userDefinedPerspective =
                        new ExplorerPerspective(perspectiveName);

                    // make sure there are some rules...
                    if (perspectiveDetails.hasMoreTokens()) {

                        // get the rules
                        while (perspectiveDetails.hasMoreTokens()) {

                            // get the rule name
                            String ruleName = perspectiveDetails.nextToken();

                            // create the rule
                            try {
                                Class ruleClass = Class.forName(ruleName);

                                PerspectiveRule rule =
                                    (PerspectiveRule) ruleClass.newInstance();

                                userDefinedPerspective.addRule(rule);
                            } catch (NoClassDefFoundError ex) {
                                LOG.error(
                                    "could not create rule, you can try to "
                                    + "refresh the perspectives to the "
                                    + "default settings.",
                                    ex);
                            }
                        }
                    } else {
                        // rule name but no rules
                        continue;
                    }

                    // add the perspective
                    addPerspective(userDefinedPerspective);
                }
            } else {
                // no user defined perspectives
                loadDefaultPerspectives();
            }

            // one last check that some loaded.
            if (getPerspectives().size() == 0) {
                loadDefaultPerspectives();
            }
        } catch (ClassNotFoundException e) {

        } catch (InstantiationException e) {

        } catch (IllegalAccessException e) {

        }
    }

    /**
     * Loads a pre-defined default set of perspectives.
     */
    public void loadDefaultPerspectives() {
        Collection c = getDefaultPerspectives();

        addAllPerspectives(c);
    }

    /**
     * @return a collection of default perspectives (i.e. the predefined ones)
     */
    public Collection getDefaultPerspectives() {
        ExplorerPerspective classPerspective =
            new ExplorerPerspective(
                "combobox.item.class-centric");
        classPerspective.addRule(new GoProjectToModel());
        classPerspective.addRule(new GoNamespaceToClassifierAndPackage());
        classPerspective.addRule(new GoNamespaceToDiagram());
        classPerspective.addRule(new GoClassToSummary());
        classPerspective.addRule(new GoSummaryToAssociation());
        classPerspective.addRule(new GoSummaryToAttribute());
        classPerspective.addRule(new GoSummaryToOperation());
        classPerspective.addRule(new GoSummaryToInheritance());
        classPerspective.addRule(new GoSummaryToIncomingDependency());
        classPerspective.addRule(new GoSummaryToOutgoingDependency());

        ExplorerPerspective packagePerspective =
            new ExplorerPerspective(
                "combobox.item.package-centric");
        packagePerspective.addRule(new GoProjectToModel());
        packagePerspective.addRule(new GoNamespaceToOwnedElements());
        packagePerspective.addRule(new GoNamespaceToDiagram());
        packagePerspective.addRule(new GoUseCaseToExtensionPoint());
        packagePerspective.addRule(new GoClassifierToStructuralFeature());
        packagePerspective.addRule(new GoClassifierToBehavioralFeature());
        packagePerspective.addRule(new GoEnumerationToLiterals());
        packagePerspective.addRule(new GoCollaborationToInteraction());
        packagePerspective.addRule(new GoInteractionToMessages());
        packagePerspective.addRule(new GoMessageToAction());
        packagePerspective.addRule(new GoSignalToReception());
        packagePerspective.addRule(new GoLinkToStimuli());
        packagePerspective.addRule(new GoStimulusToAction());
        packagePerspective.addRule(new GoClassifierToCollaboration());
        packagePerspective.addRule(new GoOperationToCollaboration());
        packagePerspective.addRule(new GoModelElementToComment());
        packagePerspective.addRule(new GoCollaborationToDiagram());
        /*
         * Removed the next one due to issue 2165.
         * packagePerspective.addRule(new GoOperationToCollaborationDiagram());
         */
        packagePerspective.addRule(new GoBehavioralFeatureToStateMachine());
        // works for both statediagram as activitygraph
        packagePerspective.addRule(new GoStatemachineToDiagram());
        packagePerspective.addRule(new GoStateMachineToState());
        packagePerspective.addRule(new GoCompositeStateToSubvertex());
        packagePerspective.addRule(new GoStateToInternalTrans());
        packagePerspective.addRule(new GoStateToDoActivity());
        packagePerspective.addRule(new GoStateToEntry());
        packagePerspective.addRule(new GoStateToExit());
        packagePerspective.addRule(new GoClassifierToSequenceDiagram());
        packagePerspective.addRule(new GoOperationToSequenceDiagram());
        packagePerspective.addRule(new GoClassifierToInstance());
        packagePerspective.addRule(new GoStateToIncomingTrans());
        packagePerspective.addRule(new GoStateToOutgoingTrans());
        packagePerspective.addRule(new GoSubmachineStateToStateMachine());
        packagePerspective.addRule(new GoStereotypeToTagDefinition());
        packagePerspective.addRule(new GoModelElementToBehavior());
        packagePerspective.addRule(new GoModelElementToContainedLostElements());

        ExplorerPerspective diagramPerspective =
            new ExplorerPerspective(
                "combobox.item.diagram-centric");
        diagramPerspective.addRule(new GoProjectToModel());
        diagramPerspective.addRule(new GoModelToDiagrams());
        diagramPerspective.addRule(new GoDiagramToNode());
        diagramPerspective.addRule(new GoDiagramToEdge());
        diagramPerspective.addRule(new GoUseCaseToExtensionPoint());
        diagramPerspective.addRule(new GoClassifierToStructuralFeature());
        diagramPerspective.addRule(new GoClassifierToBehavioralFeature());

        ExplorerPerspective inheritancePerspective =
            new ExplorerPerspective(
                "combobox.item.inheritance-centric");
        inheritancePerspective.addRule(new GoProjectToModel());
        inheritancePerspective.addRule(new GoModelToBaseElements());
        inheritancePerspective
                .addRule(new GoGeneralizableElementToSpecialized());

        ExplorerPerspective associationsPerspective =
            new ExplorerPerspective(
                "combobox.item.class-associations");
        associationsPerspective.addRule(new GoProjectToModel());
        associationsPerspective.addRule(new GoNamespaceToDiagram());
        associationsPerspective.addRule(new GoPackageToClass());
        associationsPerspective.addRule(new GoClassToAssociatedClass());

        ExplorerPerspective residencePerspective =
            new ExplorerPerspective(
                "combobox.item.residence-centric");
        residencePerspective.addRule(new GoProjectToModel());
        residencePerspective.addRule(new GoModelToNode());
        residencePerspective.addRule(new GoNodeToResidentComponent());
        residencePerspective.addRule(new GoComponentToResidentModelElement());

        ExplorerPerspective statePerspective =
            new ExplorerPerspective(
                "combobox.item.state-centric");
        statePerspective.addRule(new GoProjectToStateMachine());
        statePerspective.addRule(new GoStatemachineToDiagram());
        statePerspective.addRule(new GoStateMachineToState());
        statePerspective.addRule(new GoCompositeStateToSubvertex());
        statePerspective.addRule(new GoStateToIncomingTrans());
        statePerspective.addRule(new GoStateToOutgoingTrans());
        statePerspective.addRule(new GoTransitiontoEffect());
        statePerspective.addRule(new GoTransitionToGuard());

        ExplorerPerspective transitionsPerspective =
            new ExplorerPerspective(
                "combobox.item.transitions-centric");
        transitionsPerspective.addRule(new GoProjectToStateMachine());
        transitionsPerspective.addRule(new GoStatemachineToDiagram());
        transitionsPerspective.addRule(new GoStateMachineToTransition());
        transitionsPerspective.addRule(new GoTransitionToSource());
        transitionsPerspective.addRule(new GoTransitionToTarget());
        transitionsPerspective.addRule(new GoTransitiontoEffect());
        transitionsPerspective.addRule(new GoTransitionToGuard());

        ExplorerPerspective compositionPerspective =
            new ExplorerPerspective(
                "combobox.item.composite-centric");
        compositionPerspective.addRule(new GoProjectToModel());
        compositionPerspective.addRule(new GoModelElementToContents());
        compositionPerspective.addRule(new GoModelElementToContainedDiagrams());

        Collection c = new ArrayList();
        c.add(packagePerspective);
        c.add(classPerspective);
        c.add(diagramPerspective);
        c.add(inheritancePerspective);
        c.add(associationsPerspective);
        c.add(residencePerspective);
        c.add(statePerspective);
        c.add(transitionsPerspective);
        c.add(compositionPerspective);
        return c;
    }

    /**
     * Get the predefined rules.<p>
     *
     * This is a hard coded rules library for now, since it is quite a lot of
     * work to get all possible rule names in "org.argouml.ui.explorer.rules"
     * from the classpath (which would also not allow adding rules from other
     * locations).
     */
    public void loadRules() {

        PerspectiveRule[] ruleNamesArray = {new GoAssocRoleToMessages(),
            new GoBehavioralFeatureToStateDiagram(),
            new GoBehavioralFeatureToStateMachine(),
            new GoClassifierToBehavioralFeature(),
            new GoClassifierToCollaboration(),
            new GoClassifierToInstance(),
            new GoClassifierToSequenceDiagram(),
            new GoClassifierToStateMachine(),
            new GoClassifierToStructuralFeature(),
            new GoClassToAssociatedClass(), new GoClassToNavigableClass(),
            new GoClassToSummary(), new GoCollaborationToDiagram(),
            new GoCollaborationToInteraction(),
            new GoComponentToResidentModelElement(),
            new GoCompositeStateToSubvertex(), new GoDiagramToEdge(),
            new GoDiagramToNode(), new GoElementToMachine(),
            new GoEnumerationToLiterals(),
            new GoGeneralizableElementToSpecialized(),
            new GoInteractionToMessages(), new GoLinkToStimuli(),
            new GoMessageToAction(), new GoModelElementToComment(),
            new GoModelElementToBehavior(),
            new GoModelElementToContents(),
            new GoModelElementToContainedDiagrams(),
            new GoModelElementToContainedLostElements(),
            new GoModelToBaseElements(), new GoModelToCollaboration(),
            new GoModelToDiagrams(), new GoModelToElements(),
            new GoModelToNode(), new GoNamespaceToClassifierAndPackage(),
            new GoNamespaceToDiagram(), new GoNamespaceToOwnedElements(),
            new GoNodeToResidentComponent(),
            new GoOperationToCollaborationDiagram(),
            new GoOperationToCollaboration(),
            new GoOperationToSequenceDiagram(), new GoPackageToClass(),
            new GoProjectToCollaboration(), new GoProjectToDiagram(),
            new GoProjectToModel(), new GoProjectToStateMachine(),
            new GoSignalToReception(), new GoStateMachineToTop(),
            new GoStatemachineToDiagram(), new GoStateMachineToState(),
            new GoStateMachineToTransition(), new GoStateToDoActivity(),
            new GoStateToDownstream(), new GoStateToEntry(),
            new GoStateToExit(), new GoStateToIncomingTrans(),
            new GoStateToInternalTrans(), new GoStateToOutgoingTrans(),
            new GoStereotypeToTagDefinition(),
            new GoStimulusToAction(), new GoSummaryToAssociation(),
            new GoSummaryToAttribute(),
            new GoSummaryToIncomingDependency(),
            new GoSummaryToInheritance(), new GoSummaryToOperation(),
            new GoSummaryToOutgoingDependency(),
            new GoTransitionToSource(), new GoTransitionToTarget(),
            new GoTransitiontoEffect(), new GoTransitionToGuard(),
            new GoUseCaseToExtensionPoint(),
            new GoSubmachineStateToStateMachine(),
        };

        rules = Arrays.asList(ruleNamesArray);
    }

    /**
     * Add a rule to the list of rules.
     *
     * @param rule
     *            the PerspectiveRule to be added
     */
    public void addRule(PerspectiveRule rule) {
        rules.add(rule);
    }

    /**
     * Remove a rule from the list.
     *
     * @param rule
     *            the PerspectiveRule to be removed
     */
    public void removeRule(PerspectiveRule rule) {
        rules.remove(rule);
    }

    /**
     * @return the collection of rules
     */
    public Collection getRules() {
        return rules;
    }

    /**
     * Save the user perspectives in the ArgoUML configuration.
     */
    public void saveUserPerspectives() {
        Configuration.setString(Argo.KEY_USER_EXPLORER_PERSPECTIVES, this
                .toString());
    }

    /**
     * string representation of the perspectives in the same format as saved in
     * the user properties.
     *
     * @see java.lang.Object#toString()
     */
    public String toString() {

        String p = "";

        Iterator perspectivesIt = getPerspectives().iterator();
        while (perspectivesIt.hasNext()) {

            ExplorerPerspective perspective =
                (ExplorerPerspective) perspectivesIt.next();

            String name = perspective.toString();

            p += name + ",";

            Object[] rulesArray = perspective.getRulesArray();

            for (int x = 0; x < rulesArray.length; x++) {

                PerspectiveRule rule = (PerspectiveRule) rulesArray[x];
                p += rule.getClass().getName();

                if (x < rulesArray.length - 1) {
                    p += ",";
                }
            }

            if (perspectivesIt.hasNext()) {
                p += ";";
            }
        }

        return p;
    }
}
