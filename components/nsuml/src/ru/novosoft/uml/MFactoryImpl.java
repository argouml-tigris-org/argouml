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
import ru.novosoft.uml.undo.*;
import java.util.TooManyListenersException;
import java.lang.reflect.Method;


public final class MFactoryImpl extends MFactory
{
  private static MFactoryImpl singleton = new MFactoryImpl();
  public static MFactory getFactory()
  {
    return singleton;
  }

  static class MethodUndoAction implements MUndoableAction
  {
    Object obj;
    Method undoMethod;
    Object undoArgs[];
    Method redoMethod;
    Object redoArgs[];

    MethodUndoAction(Object o, Method um, Object ua[], 
                     Method rm, Object ra[])
    {
      obj = o;
      undoMethod = um;
      undoArgs = ua;
      redoMethod = rm;
      redoArgs = ra;
    }
    
    public String toString()
    {
      return "Undo "+undoMethod+" Redo "+redoMethod;
    }

    public void undo()
    {
      try
      {
        undoMethod.invoke(obj, undoArgs);
      }
      catch(Exception ex)
      {
        throw new IllegalStateException("invalid undo object");
      }
    }


    public void redo()
    {
      try
      {
        redoMethod.invoke(obj, redoArgs);
      }
      catch(Exception ex)
      {
        throw new IllegalStateException("invalid undo object");
      }
    }
  }

  static MUndoManager undoManager = null;

  public static void addUndoManager(MUndoManager um) throws TooManyListenersException
  {
    if( um != null && undoManager != null)
    {
      throw new TooManyListenersException("only one undo manager is allowed");
    }
    undoManager = um;
  }

  public static void removeUndoManager(MUndoManager um)
  {
    if(um == undoManager)
    {
      undoManager = null;
    }
  }

  static void enlistUndo(MBase e, Method um, Object ua[], Method rm, Object ra[])
  {
    undoManager.enlistUndo(new MethodUndoAction(e, um, ua, rm, ra));
  }

  public static final int EVENT_POLICY_DISABLED = 0;
  public static final int EVENT_POLICY_IMMEDIATE = 1;
  public static final int EVENT_POLICY_FLUSH = 2;
  static int event_policy = EVENT_POLICY_DISABLED;
  public static void setEventPolicy(int policy)
  { 
    if (event_policy != EVENT_POLICY_DISABLED &&
        policy == EVENT_POLICY_DISABLED &&
        events.size() != 0)
    {
      events = new ArrayList();
    }
    event_policy = policy;
  }
  public static int getEventPolicy()
  { 
    return event_policy;
  }

  static int operationDepthCount = 0;
  /** helper function */
  static void operationStarted() 
  {
    operationDepthCount++;
  }
  /** helper function */
  static void operationFinished()
  {
    operationDepthCount--;
    if (operationDepthCount == 0 && event_policy == EVENT_POLICY_IMMEDIATE)
    {
      flushEvents();
    }
  }

  static ArrayList events = new ArrayList();

  static void scheduleEvent(MElementEvent e)
  {
    //System.out.println("event scheduled");
    events.add(e);
  }

  public static void flushEvents()
  {
    do
    {
      List old = events;
      events = new ArrayList();

      Iterator i = old.iterator();
      while (i.hasNext())
      {
        MElementEvent evt = (MElementEvent)i.next();
        MBaseImpl e = (MBaseImpl)evt.getSource();
        Iterator j = e.getMElementListeners().iterator();
        while (j.hasNext())
        {
          try
          {
            MElementListener l = (MElementListener)j.next();
            switch (evt.getType())
            {
            case MElementEvent.ELEMENT_REMOVED:
              l.removed(evt);
              break;
            case MElementEvent.ELEMENT_RECOVERED:
              l.recovered(evt);
              break;
            case MElementEvent.ATTRIBUTE_SET:
            case MElementEvent.REFERENCE_SET:
            case MElementEvent.BAG_ROLE_SET:
            case MElementEvent.LIST_ROLE_SET:
              l.propertySet(evt);
              break;
            case MElementEvent.BAG_ROLE_ADDED:
            case MElementEvent.LIST_ROLE_ADDED:
              l.roleAdded(evt);
              break;
            case MElementEvent.BAG_ROLE_REMOVED:
            case MElementEvent.LIST_ROLE_REMOVED:
              l.roleRemoved(evt);
              break;
            case MElementEvent.LIST_ROLE_ITEM_SET:
              l.listRoleItemSet(evt);
              break;
            default:
              System.err.println("bad event: "+evt.getType());
            }
          }
          catch(Exception ex)
          {
          }
        }
      }
    } while (events.size() != 0);
  }
  public final MComment createComment()
  {
    return new MCommentImpl();
  }
  public final MTerminateAction createTerminateAction()
  {
    return new MTerminateActionImpl();
  }
  public final MClass createClass()
  {
    return new MClassImpl();
  }
  public final MRelationship createRelationship()
  {
    return new MRelationshipImpl();
  }
  public final MActor createActor()
  {
    return new MActorImpl();
  }
  public final MExtensionPoint createExtensionPoint()
  {
    return new MExtensionPointImpl();
  }
  public final MTimeEvent createTimeEvent()
  {
    return new MTimeEventImpl();
  }
  public final MStateMachine createStateMachine()
  {
    return new MStateMachineImpl();
  }
  public final MUninterpretedAction createUninterpretedAction()
  {
    return new MUninterpretedActionImpl();
  }
  public final MElementResidence createElementResidence()
  {
    return new MElementResidenceImpl();
  }
  public final MArgument createArgument()
  {
    return new MArgumentImpl();
  }
  public final MTransition createTransition()
  {
    return new MTransitionImpl();
  }
  public final MLink createLink()
  {
    return new MLinkImpl();
  }
  public final MState createState()
  {
    return new MStateImpl();
  }
  public final MAssociationClass createAssociationClass()
  {
    return new MAssociationClassImpl();
  }
  public final MUsage createUsage()
  {
    return new MUsageImpl();
  }
  public final MSynchState createSynchState()
  {
    return new MSynchStateImpl();
  }
  public final MModel createModel()
  {
    return new MModelImpl();
  }
  public final MAttributeLink createAttributeLink()
  {
    return new MAttributeLinkImpl();
  }
  public final MReturnAction createReturnAction()
  {
    return new MReturnActionImpl();
  }
  public final MActionState createActionState()
  {
    return new MActionStateImpl();
  }
  public final MAction createAction()
  {
    return new MActionImpl();
  }
  public final MUseCaseInstance createUseCaseInstance()
  {
    return new MUseCaseInstanceImpl();
  }
  public final MMessage createMessage()
  {
    return new MMessageImpl();
  }
  public final MSubactivityState createSubactivityState()
  {
    return new MSubactivityStateImpl();
  }
  public final MSendAction createSendAction()
  {
    return new MSendActionImpl();
  }
  public final MSignal createSignal()
  {
    return new MSignalImpl();
  }
  public final MNodeInstance createNodeInstance()
  {
    return new MNodeInstanceImpl();
  }
  public final MReception createReception()
  {
    return new MReceptionImpl();
  }
  public final MTemplateParameter createTemplateParameter()
  {
    return new MTemplateParameterImpl();
  }
  public final MExtension createExtension()
  {
    return new MExtensionImpl();
  }
  public final MAssociationRole createAssociationRole()
  {
    return new MAssociationRoleImpl();
  }
  public final MExtend createExtend()
  {
    return new MExtendImpl();
  }
  public final MParameter createParameter()
  {
    return new MParameterImpl();
  }
  public final MCompositeState createCompositeState()
  {
    return new MCompositeStateImpl();
  }
  public final MDataType createDataType()
  {
    return new MDataTypeImpl();
  }
  public final MStubState createStubState()
  {
    return new MStubStateImpl();
  }
  public final MActivityGraph createActivityGraph()
  {
    return new MActivityGraphImpl();
  }
  public final MPackage createPackage()
  {
    return new MPackageImpl();
  }
  public final MLinkObject createLinkObject()
  {
    return new MLinkObjectImpl();
  }
  public final MAbstraction createAbstraction()
  {
    return new MAbstractionImpl();
  }
  public final MChangeEvent createChangeEvent()
  {
    return new MChangeEventImpl();
  }
  public final MNode createNode()
  {
    return new MNodeImpl();
  }
  public final MCallState createCallState()
  {
    return new MCallStateImpl();
  }
  public final MInterface createInterface()
  {
    return new MInterfaceImpl();
  }
  public final MComponentInstance createComponentInstance()
  {
    return new MComponentInstanceImpl();
  }
  public final MObjectFlowState createObjectFlowState()
  {
    return new MObjectFlowStateImpl();
  }
  public final MSignalEvent createSignalEvent()
  {
    return new MSignalEventImpl();
  }
  public final MDependency createDependency()
  {
    return new MDependencyImpl();
  }
  public final MInstance createInstance()
  {
    return new MInstanceImpl();
  }
  public final MAttribute createAttribute()
  {
    return new MAttributeImpl();
  }
  public final MGeneralization createGeneralization()
  {
    return new MGeneralizationImpl();
  }
  public final MCallAction createCallAction()
  {
    return new MCallActionImpl();
  }
  public final MGuard createGuard()
  {
    return new MGuardImpl();
  }
  public final MClassifier createClassifier()
  {
    return new MClassifierImpl();
  }
  public final MObject createObject()
  {
    return new MObjectImpl();
  }
  public final MOperation createOperation()
  {
    return new MOperationImpl();
  }
  public final MInteraction createInteraction()
  {
    return new MInteractionImpl();
  }
  public final MFinalState createFinalState()
  {
    return new MFinalStateImpl();
  }
  public final MBinding createBinding()
  {
    return new MBindingImpl();
  }
  public final MCreateAction createCreateAction()
  {
    return new MCreateActionImpl();
  }
  public final MStereotype createStereotype()
  {
    return new MStereotypeImpl();
  }
  public final MSimpleState createSimpleState()
  {
    return new MSimpleStateImpl();
  }
  public final MSubsystem createSubsystem()
  {
    return new MSubsystemImpl();
  }
  public final MInclude createInclude()
  {
    return new MIncludeImpl();
  }
  public final MAssociationEndRole createAssociationEndRole()
  {
    return new MAssociationEndRoleImpl();
  }
  public final MCollaboration createCollaboration()
  {
    return new MCollaborationImpl();
  }
  public final MCallEvent createCallEvent()
  {
    return new MCallEventImpl();
  }
  public final MFlow createFlow()
  {
    return new MFlowImpl();
  }
  public final MDataValue createDataValue()
  {
    return new MDataValueImpl();
  }
  public final MComponent createComponent()
  {
    return new MComponentImpl();
  }
  public final MLinkEnd createLinkEnd()
  {
    return new MLinkEndImpl();
  }
  public final MPartition createPartition()
  {
    return new MPartitionImpl();
  }
  public final MAssociationEnd createAssociationEnd()
  {
    return new MAssociationEndImpl();
  }
  public final MPermission createPermission()
  {
    return new MPermissionImpl();
  }
  public final MUseCase createUseCase()
  {
    return new MUseCaseImpl();
  }
  public final MActionSequence createActionSequence()
  {
    return new MActionSequenceImpl();
  }
  public final MNamespace createNamespace()
  {
    return new MNamespaceImpl();
  }
  public final MMethod createMethod()
  {
    return new MMethodImpl();
  }
  public final MDestroyAction createDestroyAction()
  {
    return new MDestroyActionImpl();
  }
  public final MStimulus createStimulus()
  {
    return new MStimulusImpl();
  }
  public final MConstraint createConstraint()
  {
    return new MConstraintImpl();
  }
  public final MAssociation createAssociation()
  {
    return new MAssociationImpl();
  }
  public final MException createException()
  {
    return new MExceptionImpl();
  }
  public final MPseudostate createPseudostate()
  {
    return new MPseudostateImpl();
  }
  public final MElementImport createElementImport()
  {
    return new MElementImportImpl();
  }
  public final MSubmachineState createSubmachineState()
  {
    return new MSubmachineStateImpl();
  }
  public final MClassifierInState createClassifierInState()
  {
    return new MClassifierInStateImpl();
  }
  public final MClassifierRole createClassifierRole()
  {
    return new MClassifierRoleImpl();
  }
  public final MTaggedValue createTaggedValue()
  {
    return new MTaggedValueImpl();
  }

}
