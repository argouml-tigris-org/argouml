/* Novosoft UML API for Java. Version 0.4.19
 * Copyright (C) 1999, 2000, NovoSoft.
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA. The text of license can be also found 
 * at http://www.gnu.org/copyleft/lgpl.html
 */

package ru.novosoft.uml;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;
import ru.novosoft.uml.behavior.*;
import ru.novosoft.uml.behavior.use_cases.*;
import ru.novosoft.uml.behavior.common_behavior.*;
import ru.novosoft.uml.behavior.state_machines.*;
import ru.novosoft.uml.behavior.collaborations.*;
import ru.novosoft.uml.behavior.activity_graphs.*;
import ru.novosoft.uml.model_management.*;


public abstract class MFactory
{
  /** default factory */
  private static boolean firstTime = true;
  private static MFactory defaultFactory;
  /** get default factory */
  public static MFactory getDefaultFactory()
  {
    if(firstTime)
    {
      firstTime = false;
      defaultFactory = MFactoryImpl.getFactory();
    }
    return defaultFactory;
  }
  /** set default factory */
  public static void setDefaultFactory(MFactory arg)
  {
    firstTime = false;
    defaultFactory = arg;
  }
  public abstract MComment createComment();
  public abstract MTerminateAction createTerminateAction();
  public abstract MClass createClass();
  public abstract MRelationship createRelationship();
  public abstract MActor createActor();
  public abstract MExtensionPoint createExtensionPoint();
  public abstract MTimeEvent createTimeEvent();
  public abstract MStateMachine createStateMachine();
  public abstract MUninterpretedAction createUninterpretedAction();
  public abstract MElementResidence createElementResidence();
  public abstract MArgument createArgument();
  public abstract MTransition createTransition();
  public abstract MLink createLink();
  public abstract MState createState();
  public abstract MAssociationClass createAssociationClass();
  public abstract MUsage createUsage();
  public abstract MSynchState createSynchState();
  public abstract MModel createModel();
  public abstract MAttributeLink createAttributeLink();
  public abstract MReturnAction createReturnAction();
  public abstract MActionState createActionState();
  public abstract MAction createAction();
  public abstract MUseCaseInstance createUseCaseInstance();
  public abstract MMessage createMessage();
  public abstract MSubactivityState createSubactivityState();
  public abstract MSendAction createSendAction();
  public abstract MSignal createSignal();
  public abstract MNodeInstance createNodeInstance();
  public abstract MReception createReception();
  public abstract MTemplateParameter createTemplateParameter();
  public abstract MExtension createExtension();
  public abstract MAssociationRole createAssociationRole();
  public abstract MExtend createExtend();
  public abstract MParameter createParameter();
  public abstract MCompositeState createCompositeState();
  public abstract MDataType createDataType();
  public abstract MStubState createStubState();
  public abstract MActivityGraph createActivityGraph();
  public abstract MPackage createPackage();
  public abstract MLinkObject createLinkObject();
  public abstract MAbstraction createAbstraction();
  public abstract MChangeEvent createChangeEvent();
  public abstract MNode createNode();
  public abstract MCallState createCallState();
  public abstract MInterface createInterface();
  public abstract MComponentInstance createComponentInstance();
  public abstract MObjectFlowState createObjectFlowState();
  public abstract MSignalEvent createSignalEvent();
  public abstract MDependency createDependency();
  public abstract MInstance createInstance();
  public abstract MAttribute createAttribute();
  public abstract MGeneralization createGeneralization();
  public abstract MCallAction createCallAction();
  public abstract MGuard createGuard();
  public abstract MClassifier createClassifier();
  public abstract MObject createObject();
  public abstract MOperation createOperation();
  public abstract MInteraction createInteraction();
  public abstract MFinalState createFinalState();
  public abstract MBinding createBinding();
  public abstract MCreateAction createCreateAction();
  public abstract MStereotype createStereotype();
  public abstract MSimpleState createSimpleState();
  public abstract MSubsystem createSubsystem();
  public abstract MInclude createInclude();
  public abstract MAssociationEndRole createAssociationEndRole();
  public abstract MCollaboration createCollaboration();
  public abstract MCallEvent createCallEvent();
  public abstract MFlow createFlow();
  public abstract MDataValue createDataValue();
  public abstract MComponent createComponent();
  public abstract MLinkEnd createLinkEnd();
  public abstract MPartition createPartition();
  public abstract MAssociationEnd createAssociationEnd();
  public abstract MPermission createPermission();
  public abstract MUseCase createUseCase();
  public abstract MActionSequence createActionSequence();
  public abstract MNamespace createNamespace();
  public abstract MMethod createMethod();
  public abstract MDestroyAction createDestroyAction();
  public abstract MStimulus createStimulus();
  public abstract MConstraint createConstraint();
  public abstract MAssociation createAssociation();
  public abstract MException createException();
  public abstract MPseudostate createPseudostate();
  public abstract MElementImport createElementImport();
  public abstract MSubmachineState createSubmachineState();
  public abstract MClassifierInState createClassifierInState();
  public abstract MClassifierRole createClassifierRole();
  public abstract MTaggedValue createTaggedValue();

}
