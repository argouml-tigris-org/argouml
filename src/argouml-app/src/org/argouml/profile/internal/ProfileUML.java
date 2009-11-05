// $Id$
// Copyright (c) 1996-2009 The Regents of the University of California. All
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

package org.argouml.profile.internal;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.argouml.cognitive.Critic;
import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.Translator;
import org.argouml.model.Model;
import org.argouml.profile.CoreProfileReference;
import org.argouml.profile.DefaultTypeStrategy;
import org.argouml.profile.FormatingStrategy;
import org.argouml.profile.Profile;
import org.argouml.profile.ProfileException;
import org.argouml.profile.ProfileModelLoader;
import org.argouml.profile.ProfileReference;
import org.argouml.profile.ResourceModelLoader;
import org.argouml.profile.internal.ocl.CrOCL;
import org.argouml.profile.internal.ocl.InvalidOclException;
import org.argouml.uml.cognitive.critics.CrAssocNameConflict;
import org.argouml.uml.cognitive.critics.CrAttrNameConflict;
import org.argouml.uml.cognitive.critics.CrCircularAssocClass;
import org.argouml.uml.cognitive.critics.CrCircularInheritance;
import org.argouml.uml.cognitive.critics.CrClassMustBeAbstract;
import org.argouml.uml.cognitive.critics.CrCrossNamespaceAssoc;
import org.argouml.uml.cognitive.critics.CrDupParamName;
import org.argouml.uml.cognitive.critics.CrDupRoleNames;
import org.argouml.uml.cognitive.critics.CrFinalSubclassed;
import org.argouml.uml.cognitive.critics.CrForkOutgoingTransition;
import org.argouml.uml.cognitive.critics.CrIllegalGeneralization;
import org.argouml.uml.cognitive.critics.CrInterfaceAllPublic;
import org.argouml.uml.cognitive.critics.CrInterfaceOperOnly;
import org.argouml.uml.cognitive.critics.CrInvalidBranch;
import org.argouml.uml.cognitive.critics.CrInvalidFork;
import org.argouml.uml.cognitive.critics.CrInvalidHistory;
import org.argouml.uml.cognitive.critics.CrInvalidInitial;
import org.argouml.uml.cognitive.critics.CrInvalidJoin;
import org.argouml.uml.cognitive.critics.CrInvalidJoinTriggerOrGuard;
import org.argouml.uml.cognitive.critics.CrInvalidPseudoStateTrigger;
import org.argouml.uml.cognitive.critics.CrInvalidSynch;
import org.argouml.uml.cognitive.critics.CrJoinIncomingTransition;
import org.argouml.uml.cognitive.critics.CrMultiComposite;
import org.argouml.uml.cognitive.critics.CrMultipleAgg;
import org.argouml.uml.cognitive.critics.CrMultipleDeepHistoryStates;
import org.argouml.uml.cognitive.critics.CrMultipleShallowHistoryStates;
import org.argouml.uml.cognitive.critics.CrNWayAgg;
import org.argouml.uml.cognitive.critics.CrNameConflict;
import org.argouml.uml.cognitive.critics.CrNameConflictAC;
import org.argouml.uml.cognitive.critics.CrNameConfusion;
import org.argouml.uml.cognitive.critics.CrOppEndConflict;
import org.argouml.uml.cognitive.critics.CrOppEndVsAttr;

/**
 * This class represents the default UML profile
 *
 * @author maurelio1234
 */
public class ProfileUML extends Profile {
    
    private static final String PROFILE_UML14_FILE = "default-uml14.xmi";
    private static final String PROFILE_UML22_FILE = "default-uml22.xmi";

    static final String NAME_UML14 = "UML 1.4";
    static final String NAME_UML22 = "UML 2.2";
    
    private FormatingStrategy formatingStrategy;
    private ProfileModelLoader profileModelLoader;
    private Collection model;
        
    /**
     * Construct a Profile for UML modeling. 
     * @throws ProfileException 
     */
    @SuppressWarnings("unchecked")
    ProfileUML() throws ProfileException {
        formatingStrategy = new FormatingStrategyUML();
        profileModelLoader = new ResourceModelLoader();
        ProfileReference profileReference = null;
        try {
            if (Model.getFacade().getUmlVersion().charAt(0) == '1') {
                profileReference =
                    new CoreProfileReference(PROFILE_UML14_FILE);
            } else {
                //TODO: reference should be handled better
                CoreProfileReference.setProfileDirectory("uml22");
                profileReference =
                    new CoreProfileReference(PROFILE_UML22_FILE);
            }
        } catch (MalformedURLException e) {
            throw new ProfileException(
                "Exception while creating profile reference.", e);
        }
        model = profileModelLoader.loadModel(profileReference);

        if (model == null) {
            model = new ArrayList();
            model.add(Model.getModelManagementFactory().createProfile());
        }
        
        for (Object p : model) {
            Model.getExtensionMechanismsHelper().makeProfileApplicable(p);
        }
        
        loadWellFormednessRules();
    }

    private void loadWellFormednessRules() {
        Set<Critic> critics = new HashSet<Critic>();
        
        critics.add(new CrAssocNameConflict());
        critics.add(new CrAttrNameConflict());
        critics.add(new CrCircularAssocClass());
        critics.add(new CrCircularInheritance());
        critics.add(new CrClassMustBeAbstract());
        critics.add(new CrCrossNamespaceAssoc());
        critics.add(new CrDupParamName());
        critics.add(new CrDupRoleNames());
        critics.add(new CrNameConfusion());

        critics.add(new CrInvalidHistory());
        critics.add(new CrInvalidSynch());
        critics.add(new CrInvalidJoinTriggerOrGuard());
        critics.add(new CrInvalidPseudoStateTrigger());
        critics.add(new CrInvalidInitial());
        
        critics.add(new CrInvalidJoin());
        critics.add(new CrInvalidFork());
        critics.add(new CrInvalidBranch());

        critics.add(new CrMultipleDeepHistoryStates());
        critics.add(new CrMultipleShallowHistoryStates());
        critics.add(new CrForkOutgoingTransition());
        critics.add(new CrJoinIncomingTransition());

        critics.add(new CrFinalSubclassed());
        critics.add(new CrIllegalGeneralization());
        critics.add(new CrInterfaceAllPublic());
        critics.add(new CrInterfaceOperOnly());
        critics.add(new CrMultipleAgg());
        critics.add(new CrNWayAgg());
        critics.add(new CrNameConflictAC());
        
        critics.add(new CrOppEndConflict());
        critics.add(new CrMultiComposite());
        critics.add(new CrNameConflict());
        critics.add(new CrOppEndVsAttr());

        // Missing WFRs
        
        // Association Class
        // 4.5.3.2 [1]
        /* Testing: does not fire. */
        try {
            critics.add(new CrOCL("context AssociationClass inv:"
                    + "self.allConnections->"
                    + "forAll( ar | self.allFeatures->"
                    + "forAll( f | f.oclIsKindOf(StructuralFeature) "
                    + "implies ar.name <> f.name ))",
                    Translator.localize("wfr.UML142.AssociationClass.1-head"),
                    Translator.localize("wfr.UML142.AssociationClass.1-desc"),
                    ToDoItem.HIGH_PRIORITY, null, null, "http://www.uml.org/"));
        } catch (InvalidOclException e) {
            e.printStackTrace();
        }
        
        // 4.5.3.2 [2]
        /* Testing: Works Ok. */
        try {
            critics.add(new CrOCL("context AssociationClass inv:"
                    + "self.allConnections->"
                    + "forAll(ar | ar.participant <> self)",
                    Translator.localize("wfr.UML142.AssociationClass.2-head"),
                    Translator.localize("wfr.UML142.AssociationClass.2-desc"),
                    ToDoItem.HIGH_PRIORITY, null, null, "http://www.uml.org/"));
        } catch (InvalidOclException e) {
            e.printStackTrace();
        }
        
        // Behavioral Feature
        // 4.5.3.5 [2]

        // it works, but a bug in namespace.contents prevents it from 
        // working when the type of the parameter comes from a profile         
//        try {
//            Agency.register(new CrOCL("context BehavioralFeature inv:"
//                    + "self.parameter->"
//                    + "forAll( p | self.owner.namespace.allContents->"
//                    + "includes (p.type) )",
//                    "The type of the Parameters should be "
//                            + "included in the Namespace of the Classifier.",
//                    null, ToDoItem.HIGH_PRIORITY, null, null,
//                    "http://www.uml.org/"));
//        } catch (InvalidOclException e) {
//            e.printStackTrace();
//        }
        
        // Classifier
        // 4.5.3.8 [5]
        /* TODO: Partly overlaps CrOppEndVsAttr. */
        /* Testing: does not fire. */
        try {
            critics.add(new CrOCL("context Classifier inv:"
                    + "self.oppositeAssociationEnds->" 
                    + "forAll( o | not self.allAttributes->" 
                    + "union (self.allContents)->" 
                    + "collect ( q | q.name )->includes (o.name) )",
                Translator.localize("wfr.UML142.Classifier.5-head"), 
                Translator.localize("wfr.UML142.Classifier.5-desc"), 
                ToDoItem.HIGH_PRIORITY, null, null, "http://www.uml.org/"));
        } catch (InvalidOclException e) {
            e.printStackTrace();
        }

        // DataType
        // 4.5.3.12 [1]
        /* Tested with fabricated XMI - OK. */
        try {
            critics.add(new CrOCL("context DataType inv:"
                    + "self.allFeatures->forAll(f | f.oclIsKindOf(Operation)"
                    + " and f.oclAsType(Operation).isQuery)",
                    Translator.localize("wfr.UML142.DataType.1-head"),
                    Translator.localize("wfr.UML142.DataType.1-desc"),
                    ToDoItem.HIGH_PRIORITY, null, null, "http://www.uml.org/"));
        } catch (InvalidOclException e) {
            e.printStackTrace();
        }

        // GeneralizableElement
        // 4.5.3.20 [1]
        /* Testing: does not fire. */
        try {
            critics.add(new CrOCL("context GeneralizableElement inv:"
                    + "self.isRoot implies self.generalization->isEmpty",
                    Translator.localize("wfr.UML142.GeneralizableElement.1-head"),
                    Translator.localize("wfr.UML142.GeneralizableElement.1-desc"),
                    ToDoItem.HIGH_PRIORITY, null, null, "http://www.uml.org/"));
        } catch (InvalidOclException e) {
            e.printStackTrace();
        }

        // 4.5.3.20 [4]
        /* Testing: does not fire. */
        try {
            critics.add(new CrOCL("context GeneralizableElement inv:"
                    + "self.generalization->"
                    + "forAll(g |self.namespace.allContents->"
                    + "includes(g.parent) )",
                    Translator.localize("wfr.UML142.GeneralizableElement.4-head"),
                    Translator.localize("wfr.UML142.GeneralizableElement.4-desc"),
                    ToDoItem.HIGH_PRIORITY, null, null, "http://www.uml.org/"));
        } catch (InvalidOclException e) {
            e.printStackTrace();
        }
 
        // Namespace
        // 4.5.3.26 [2]
        /* Testing: Does not fire. Conflict with CrNameConflict. */
        try {
            critics.add(new CrOCL("context Namespace inv:"
                    + "self.allContents -> "
                    + "select(x|x.oclIsKindOf(Association))->"
                    + "forAll(a1, a2 |a1.name = a2.name and "
                    + "a1.connection.participant = a2.connection.participant"
                    + " implies a1 = a2)",
                    Translator.localize("wfr.UML142.Namespace.2-head"),
                    Translator.localize("wfr.UML142.Namespace.2-desc"),
                    ToDoItem.HIGH_PRIORITY, null, null,
                    "http://www.uml.org/"));
        } catch (InvalidOclException e) {
            e.printStackTrace();
        }

        // Actor
        // 4.11.3.1 [1]
        /* Testing: does not fire. */
        try {
            critics.add(new CrOCL("context Actor inv: "
                    + "self.associations->forAll(a | "
                    + "a.connection->size = 2)",
                    Translator.localize("wfr.UML142.Actor.1a-head"),
                    Translator.localize("wfr.UML142.Actor.1a-desc"), 
                    ToDoItem.HIGH_PRIORITY, null, null,
                    "http://www.uml.org/"));
        } catch (InvalidOclException e) {
            e.printStackTrace();
        }
        /* Testing: does not fire. */
        try {
            critics.add(new CrOCL("context Actor inv: "
                    + "self.associations->forAll(a | "
//                    + "a.allConnections->exists(r | r.type.oclIsKindOf(Actor)) and "
                    + "a.allConnections->exists(r | "
                    + "r.type.oclIsKindOf(UseCase) or "
                    + "r.type.oclIsKindOf(Subsystem) or "
                    + "r.type.oclIsKindOf(Class)))",
                    Translator.localize("wfr.UML142.Actor.1b-head"),
                    Translator.localize("wfr.UML142.Actor.1b-desc"), 
                    ToDoItem.HIGH_PRIORITY, null, null,
                    "http://www.uml.org/"));
        } catch (InvalidOclException e) {
            e.printStackTrace();
        }

        // Actor
        // 4.11.3.1 [2]
        /* Tested with fabricated XMI - OK. */
        try {
            critics.add(new CrOCL("context Actor inv:"
                    + "self.contents->isEmpty",
                    Translator.localize("wfr.UML142.Actor.2-head"),
                    Translator.localize("wfr.UML142.Actor.2-desc"), 
                    ToDoItem.HIGH_PRIORITY, null, null,
                    "http://www.uml.org/"));
        } catch (InvalidOclException e) {
            e.printStackTrace();
        }

        // UseCase
        // 4.11.3.5 [1]
        /* Testing: does not fire. */
        try {
            critics.add(new CrOCL("context UseCase inv:"
                    + "self.associations->forAll(a | a.connection->size = 2)",
                    Translator.localize("wfr.UML142.UseCase.1-head"),
                    Translator.localize("wfr.UML142.UseCase.1-desc"), 
                    ToDoItem.HIGH_PRIORITY, null, null,
                    "http://www.uml.org/"));
        } catch (InvalidOclException e) {
            e.printStackTrace();
        }

        // UseCase
        // 4.11.3.5 [2]
        /* Testing: does not fire. */
        try {
            critics.add(new CrOCL("context UseCase inv:"
                    + "self.associations->forAll(a | "
                    + "a.allConnections->forAll(s, o| "
                    + "(s.type.specificationPath->isEmpty and "
                    + "o.type.specificationPath->isEmpty ) "
                    + "or "
                    + "(not s.type.specificationPath->includesAll( "
                    + "o.type.specificationPath) and "
                    + "not o.type.specificationPath->includesAll( "
                    + "s.type.specificationPath)) "
                    + "))",
                    Translator.localize("wfr.UML142.UseCase.2-head"),
                    Translator.localize("wfr.UML142.UseCase.2-desc"), 
                    ToDoItem.HIGH_PRIORITY, null, null,
                    "http://www.uml.org/"));
        } catch (InvalidOclException e) {
            e.printStackTrace();
        }

        // UseCase
        // 4.11.3.5 [3]
        /* Tested with fabricated XMI - OK. */
        try {
            critics.add(new CrOCL("context UseCase inv:"
                    + "self.contents->isEmpty",
                    Translator.localize("wfr.UML142.UseCase.3-head"),
                    Translator.localize("wfr.UML142.UseCase.3-desc"), 
                    ToDoItem.HIGH_PRIORITY, null, null,
                    "http://www.uml.org/"));
        } catch (InvalidOclException e) {
            e.printStackTrace();
        }

        // UseCase
        // 4.11.3.5 [4]
        /* Tested OK, except in some cases, depending on the 
         * sequence of the EPs. Probably the implementation of
         * "forAll (x, y | ..." does not cover all combinations. */
        try {
            critics.add(new CrOCL("context UseCase inv:"
                    + "self.allExtensionPoints -> forAll (x, y | "
                    + "x.name = y.name implies x = y )",
                    Translator.localize("wfr.UML142.UseCase.4-head"),
                    Translator.localize("wfr.UML142.UseCase.4-desc"), 
                    ToDoItem.HIGH_PRIORITY, null, null,
                    "http://www.uml.org/"));
        } catch (InvalidOclException e) {
            e.printStackTrace();
        }

        // ActionState
        // 4.13.3.2 [3]
        // Issue 715
        try {
            critics.add(new CrOCL("context ActionState inv:"
                    + "self.outgoing->forAll(t | t.trigger->size = 0)",
                    Translator.localize("wfr.UML142.ActionState.3-head"),
                    Translator.localize("wfr.UML142.ActionState.3-desc"), 
                    ToDoItem.HIGH_PRIORITY, null, null,
                    "http://www.uml.org/"));
        } catch (InvalidOclException e) {
            e.printStackTrace();
        }

        setCritics(critics);
    }

    @Override
    public FormatingStrategy getFormatingStrategy() {
        return formatingStrategy;
    }

    @Override
    public String getDisplayName() {
        if (Model.getFacade().getUmlVersion().charAt(0) == '1') {
            return NAME_UML14;
        }
        return NAME_UML22;
    }


    @Override
    public Collection getProfilePackages() {
        return model;
    }
    

    @Override
    public DefaultTypeStrategy getDefaultTypeStrategy() {
        return new DefaultTypeStrategy() {
            public Object getDefaultAttributeType() {
                return ModelUtils.findTypeInModel("Integer", model.iterator()
                        .next());
            }

            public Object getDefaultParameterType() {
                return ModelUtils.findTypeInModel("Integer", model.iterator()
                        .next());
            }

            public Object getDefaultReturnType() {
                return null;
            }

        };
    }
}
