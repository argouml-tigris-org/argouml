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

package ru.novosoft.uml.foundation.core;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import ru.novosoft.uml.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;
import ru.novosoft.uml.behavior.common_behavior.*;
import ru.novosoft.uml.behavior.collaborations.*;
import ru.novosoft.uml.behavior.use_cases.*;
import ru.novosoft.uml.behavior.state_machines.*;
import ru.novosoft.uml.behavior.activity_graphs.*;
import ru.novosoft.uml.model_management.*;

import java.lang.reflect.Method;

public class MClassifierImpl extends MNamespaceImpl implements MClassifier
{
  // ------------ code for class Classifier -----------------
  // generating attributes
  // generating associations
  // opposite role: createAction this role: instantiation
  private final static Method _createAction_setMethod = getMethod1(MClassifierImpl.class, "setCreateActions", Collection.class);
  private final static Method _createAction_addMethod = getMethod1(MClassifierImpl.class, "addCreateAction", MCreateAction.class);
  private final static Method _createAction_removeMethod = getMethod1(MClassifierImpl.class, "removeCreateAction", MCreateAction.class);
  Collection _createAction = Collections.EMPTY_LIST;
  Collection _createAction_ucopy = Collections.EMPTY_LIST;
  public final Collection getCreateActions()
  {
    checkExists();
    if (null == _createAction_ucopy)
    {
      _createAction_ucopy = ucopy(_createAction);
    }
    return _createAction_ucopy;
  }
  public final void setCreateActions(Collection __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      if (__arg == null)
      {
        throw new NullPointerException();
      }
      final boolean sendEvent = needEvent();
      final boolean logForUndo = needUndo();
      Collection old = null;
      if (sendEvent || logForUndo)
      {
        old = getCreateActions();
      }
      _createAction_ucopy = null;
      Collection __added = bagdiff(__arg,_createAction);
      Collection __removed = bagdiff(_createAction, __arg);
      Iterator iter1 = __removed.iterator();
      while (iter1.hasNext())
      {
        MCreateAction o = (MCreateAction)iter1.next();
        o.internalUnrefByInstantiation(this);
      }
      Iterator iter2 = __added.iterator();
      while (iter2.hasNext())
      {
        MCreateAction o = (MCreateAction)iter2.next();
        o.internalRefByInstantiation(this);
      }
      _createAction = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_createAction_setMethod, old, getCreateActions());
      }
      if (sendEvent)
      {
        fireBagSet("createAction", old, getCreateActions());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addCreateAction(MCreateAction __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      if (__arg == null)
      {
        throw new NullPointerException();
      }
      final boolean sendEvent = needEvent();
      Collection old = null;
      if (sendEvent)
      {
        old = getCreateActions();
      }
      if (null != _createAction_ucopy)
      {
        _createAction = new ArrayList(_createAction);
        _createAction_ucopy = null;
      }
      __arg.internalRefByInstantiation(this);
      _createAction.add(__arg);
      logBagAdd(_createAction_addMethod, _createAction_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("createAction", old, getCreateActions(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeCreateAction(MCreateAction __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      if (__arg == null)
      {
        throw new NullPointerException();
      }
      final boolean sendEvent = needEvent();
      Collection old = null;
      if (sendEvent)
      {
        old = getCreateActions();
      }
      if (null != _createAction_ucopy)
      {
        _createAction = new ArrayList(_createAction);
        _createAction_ucopy = null;
      }
      if (!_createAction.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByInstantiation(this);
      logBagRemove(_createAction_removeMethod, _createAction_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("createAction", old, getCreateActions(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByCreateAction(MCreateAction __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getCreateActions();
    }
    if (null != _createAction_ucopy)
    {
      _createAction = new ArrayList(_createAction);
      _createAction_ucopy = null;
    }
    _createAction.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("createAction", old, getCreateActions(), __arg);
    }
  }
  public final void internalUnrefByCreateAction(MCreateAction __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getCreateActions();
    }
    if (null != _createAction_ucopy)
    {
      _createAction = new ArrayList(_createAction);
      _createAction_ucopy = null;
    }
    _createAction.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("createAction", old, getCreateActions(), __arg);
    }
  }
  // opposite role: instance this role: classifier
  private final static Method _instance_setMethod = getMethod1(MClassifierImpl.class, "setInstances", Collection.class);
  private final static Method _instance_addMethod = getMethod1(MClassifierImpl.class, "addInstance", MInstance.class);
  private final static Method _instance_removeMethod = getMethod1(MClassifierImpl.class, "removeInstance", MInstance.class);
  Collection _instance = Collections.EMPTY_LIST;
  Collection _instance_ucopy = Collections.EMPTY_LIST;
  public final Collection getInstances()
  {
    checkExists();
    if (null == _instance_ucopy)
    {
      _instance_ucopy = ucopy(_instance);
    }
    return _instance_ucopy;
  }
  public final void setInstances(Collection __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      if (__arg == null)
      {
        throw new NullPointerException();
      }
      final boolean sendEvent = needEvent();
      final boolean logForUndo = needUndo();
      Collection old = null;
      if (sendEvent || logForUndo)
      {
        old = getInstances();
      }
      _instance_ucopy = null;
      Collection __added = bagdiff(__arg,_instance);
      Collection __removed = bagdiff(_instance, __arg);
      Iterator iter3 = __removed.iterator();
      while (iter3.hasNext())
      {
        MInstance o = (MInstance)iter3.next();
        o.internalUnrefByClassifier(this);
      }
      Iterator iter4 = __added.iterator();
      while (iter4.hasNext())
      {
        MInstance o = (MInstance)iter4.next();
        o.internalRefByClassifier(this);
      }
      _instance = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_instance_setMethod, old, getInstances());
      }
      if (sendEvent)
      {
        fireBagSet("instance", old, getInstances());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addInstance(MInstance __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      if (__arg == null)
      {
        throw new NullPointerException();
      }
      final boolean sendEvent = needEvent();
      Collection old = null;
      if (sendEvent)
      {
        old = getInstances();
      }
      if (null != _instance_ucopy)
      {
        _instance = new ArrayList(_instance);
        _instance_ucopy = null;
      }
      __arg.internalRefByClassifier(this);
      _instance.add(__arg);
      logBagAdd(_instance_addMethod, _instance_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("instance", old, getInstances(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeInstance(MInstance __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      if (__arg == null)
      {
        throw new NullPointerException();
      }
      final boolean sendEvent = needEvent();
      Collection old = null;
      if (sendEvent)
      {
        old = getInstances();
      }
      if (null != _instance_ucopy)
      {
        _instance = new ArrayList(_instance);
        _instance_ucopy = null;
      }
      if (!_instance.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByClassifier(this);
      logBagRemove(_instance_removeMethod, _instance_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("instance", old, getInstances(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByInstance(MInstance __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getInstances();
    }
    if (null != _instance_ucopy)
    {
      _instance = new ArrayList(_instance);
      _instance_ucopy = null;
    }
    _instance.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("instance", old, getInstances(), __arg);
    }
  }
  public final void internalUnrefByInstance(MInstance __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getInstances();
    }
    if (null != _instance_ucopy)
    {
      _instance = new ArrayList(_instance);
      _instance_ucopy = null;
    }
    _instance.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("instance", old, getInstances(), __arg);
    }
  }
  // opposite role: collaboration this role: representedClassifier
  private final static Method _collaboration_setMethod = getMethod1(MClassifierImpl.class, "setCollaborations", Collection.class);
  private final static Method _collaboration_addMethod = getMethod1(MClassifierImpl.class, "addCollaboration", MCollaboration.class);
  private final static Method _collaboration_removeMethod = getMethod1(MClassifierImpl.class, "removeCollaboration", MCollaboration.class);
  Collection _collaboration = Collections.EMPTY_LIST;
  Collection _collaboration_ucopy = Collections.EMPTY_LIST;
  public final Collection getCollaborations()
  {
    checkExists();
    if (null == _collaboration_ucopy)
    {
      _collaboration_ucopy = ucopy(_collaboration);
    }
    return _collaboration_ucopy;
  }
  public final void setCollaborations(Collection __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      if (__arg == null)
      {
        throw new NullPointerException();
      }
      final boolean sendEvent = needEvent();
      final boolean logForUndo = needUndo();
      Collection old = null;
      if (sendEvent || logForUndo)
      {
        old = getCollaborations();
      }
      _collaboration_ucopy = null;
      Collection __added = bagdiff(__arg,_collaboration);
      Collection __removed = bagdiff(_collaboration, __arg);
      Iterator iter5 = __removed.iterator();
      while (iter5.hasNext())
      {
        MCollaboration o = (MCollaboration)iter5.next();
        o.internalUnrefByRepresentedClassifier(this);
      }
      Iterator iter6 = __added.iterator();
      while (iter6.hasNext())
      {
        MCollaboration o = (MCollaboration)iter6.next();
        o.internalRefByRepresentedClassifier(this);
      }
      _collaboration = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_collaboration_setMethod, old, getCollaborations());
      }
      if (sendEvent)
      {
        fireBagSet("collaboration", old, getCollaborations());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addCollaboration(MCollaboration __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      if (__arg == null)
      {
        throw new NullPointerException();
      }
      final boolean sendEvent = needEvent();
      Collection old = null;
      if (sendEvent)
      {
        old = getCollaborations();
      }
      if (null != _collaboration_ucopy)
      {
        _collaboration = new ArrayList(_collaboration);
        _collaboration_ucopy = null;
      }
      __arg.internalRefByRepresentedClassifier(this);
      _collaboration.add(__arg);
      logBagAdd(_collaboration_addMethod, _collaboration_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("collaboration", old, getCollaborations(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeCollaboration(MCollaboration __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      if (__arg == null)
      {
        throw new NullPointerException();
      }
      final boolean sendEvent = needEvent();
      Collection old = null;
      if (sendEvent)
      {
        old = getCollaborations();
      }
      if (null != _collaboration_ucopy)
      {
        _collaboration = new ArrayList(_collaboration);
        _collaboration_ucopy = null;
      }
      if (!_collaboration.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByRepresentedClassifier(this);
      logBagRemove(_collaboration_removeMethod, _collaboration_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("collaboration", old, getCollaborations(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByCollaboration(MCollaboration __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getCollaborations();
    }
    if (null != _collaboration_ucopy)
    {
      _collaboration = new ArrayList(_collaboration);
      _collaboration_ucopy = null;
    }
    _collaboration.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("collaboration", old, getCollaborations(), __arg);
    }
  }
  public final void internalUnrefByCollaboration(MCollaboration __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getCollaborations();
    }
    if (null != _collaboration_ucopy)
    {
      _collaboration = new ArrayList(_collaboration);
      _collaboration_ucopy = null;
    }
    _collaboration.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("collaboration", old, getCollaborations(), __arg);
    }
  }
  // opposite role: classifierRole this role: base
  private final static Method _classifierRole_setMethod = getMethod1(MClassifierImpl.class, "setClassifierRoles", Collection.class);
  private final static Method _classifierRole_addMethod = getMethod1(MClassifierImpl.class, "addClassifierRole", MClassifierRole.class);
  private final static Method _classifierRole_removeMethod = getMethod1(MClassifierImpl.class, "removeClassifierRole", MClassifierRole.class);
  Collection _classifierRole = Collections.EMPTY_LIST;
  Collection _classifierRole_ucopy = Collections.EMPTY_LIST;
  public final Collection getClassifierRoles()
  {
    checkExists();
    if (null == _classifierRole_ucopy)
    {
      _classifierRole_ucopy = ucopy(_classifierRole);
    }
    return _classifierRole_ucopy;
  }
  public final void setClassifierRoles(Collection __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      if (__arg == null)
      {
        throw new NullPointerException();
      }
      final boolean sendEvent = needEvent();
      final boolean logForUndo = needUndo();
      Collection old = null;
      if (sendEvent || logForUndo)
      {
        old = getClassifierRoles();
      }
      _classifierRole_ucopy = null;
      Collection __added = bagdiff(__arg,_classifierRole);
      Collection __removed = bagdiff(_classifierRole, __arg);
      Iterator iter7 = __removed.iterator();
      while (iter7.hasNext())
      {
        MClassifierRole o = (MClassifierRole)iter7.next();
        o.internalUnrefByBase(this);
      }
      Iterator iter8 = __added.iterator();
      while (iter8.hasNext())
      {
        MClassifierRole o = (MClassifierRole)iter8.next();
        o.internalRefByBase(this);
      }
      _classifierRole = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_classifierRole_setMethod, old, getClassifierRoles());
      }
      if (sendEvent)
      {
        fireBagSet("classifierRole", old, getClassifierRoles());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addClassifierRole(MClassifierRole __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      if (__arg == null)
      {
        throw new NullPointerException();
      }
      final boolean sendEvent = needEvent();
      Collection old = null;
      if (sendEvent)
      {
        old = getClassifierRoles();
      }
      if (null != _classifierRole_ucopy)
      {
        _classifierRole = new ArrayList(_classifierRole);
        _classifierRole_ucopy = null;
      }
      __arg.internalRefByBase(this);
      _classifierRole.add(__arg);
      logBagAdd(_classifierRole_addMethod, _classifierRole_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("classifierRole", old, getClassifierRoles(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeClassifierRole(MClassifierRole __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      if (__arg == null)
      {
        throw new NullPointerException();
      }
      final boolean sendEvent = needEvent();
      Collection old = null;
      if (sendEvent)
      {
        old = getClassifierRoles();
      }
      if (null != _classifierRole_ucopy)
      {
        _classifierRole = new ArrayList(_classifierRole);
        _classifierRole_ucopy = null;
      }
      if (!_classifierRole.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByBase(this);
      logBagRemove(_classifierRole_removeMethod, _classifierRole_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("classifierRole", old, getClassifierRoles(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByClassifierRole(MClassifierRole __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getClassifierRoles();
    }
    if (null != _classifierRole_ucopy)
    {
      _classifierRole = new ArrayList(_classifierRole);
      _classifierRole_ucopy = null;
    }
    _classifierRole.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("classifierRole", old, getClassifierRoles(), __arg);
    }
  }
  public final void internalUnrefByClassifierRole(MClassifierRole __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getClassifierRoles();
    }
    if (null != _classifierRole_ucopy)
    {
      _classifierRole = new ArrayList(_classifierRole);
      _classifierRole_ucopy = null;
    }
    _classifierRole.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("classifierRole", old, getClassifierRoles(), __arg);
    }
  }
  // opposite role: classifierInState this role: type
  private final static Method _classifierInState_setMethod = getMethod1(MClassifierImpl.class, "setClassifiersInState", Collection.class);
  private final static Method _classifierInState_addMethod = getMethod1(MClassifierImpl.class, "addClassifierInState", MClassifierInState.class);
  private final static Method _classifierInState_removeMethod = getMethod1(MClassifierImpl.class, "removeClassifierInState", MClassifierInState.class);
  Collection _classifierInState = Collections.EMPTY_LIST;
  Collection _classifierInState_ucopy = Collections.EMPTY_LIST;
  public final Collection getClassifiersInState()
  {
    checkExists();
    if (null == _classifierInState_ucopy)
    {
      _classifierInState_ucopy = ucopy(_classifierInState);
    }
    return _classifierInState_ucopy;
  }
  public final void setClassifiersInState(Collection __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      if (__arg == null)
      {
        throw new NullPointerException();
      }
      final boolean sendEvent = needEvent();
      final boolean logForUndo = needUndo();
      Collection old = null;
      if (sendEvent || logForUndo)
      {
        old = getClassifiersInState();
      }
      _classifierInState_ucopy = null;
      Collection __added = bagdiff(__arg,_classifierInState);
      Collection __removed = bagdiff(_classifierInState, __arg);
      Iterator iter9 = __removed.iterator();
      while (iter9.hasNext())
      {
        MClassifierInState o = (MClassifierInState)iter9.next();
        o.internalUnrefByType(this);
      }
      Iterator iter10 = __added.iterator();
      while (iter10.hasNext())
      {
        MClassifierInState o = (MClassifierInState)iter10.next();
        o.internalRefByType(this);
      }
      _classifierInState = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_classifierInState_setMethod, old, getClassifiersInState());
      }
      if (sendEvent)
      {
        fireBagSet("classifierInState", old, getClassifiersInState());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addClassifierInState(MClassifierInState __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      if (__arg == null)
      {
        throw new NullPointerException();
      }
      final boolean sendEvent = needEvent();
      Collection old = null;
      if (sendEvent)
      {
        old = getClassifiersInState();
      }
      if (null != _classifierInState_ucopy)
      {
        _classifierInState = new ArrayList(_classifierInState);
        _classifierInState_ucopy = null;
      }
      __arg.internalRefByType(this);
      _classifierInState.add(__arg);
      logBagAdd(_classifierInState_addMethod, _classifierInState_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("classifierInState", old, getClassifiersInState(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeClassifierInState(MClassifierInState __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      if (__arg == null)
      {
        throw new NullPointerException();
      }
      final boolean sendEvent = needEvent();
      Collection old = null;
      if (sendEvent)
      {
        old = getClassifiersInState();
      }
      if (null != _classifierInState_ucopy)
      {
        _classifierInState = new ArrayList(_classifierInState);
        _classifierInState_ucopy = null;
      }
      if (!_classifierInState.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByType(this);
      logBagRemove(_classifierInState_removeMethod, _classifierInState_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("classifierInState", old, getClassifiersInState(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByClassifierInState(MClassifierInState __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getClassifiersInState();
    }
    if (null != _classifierInState_ucopy)
    {
      _classifierInState = new ArrayList(_classifierInState);
      _classifierInState_ucopy = null;
    }
    _classifierInState.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("classifierInState", old, getClassifiersInState(), __arg);
    }
  }
  public final void internalUnrefByClassifierInState(MClassifierInState __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getClassifiersInState();
    }
    if (null != _classifierInState_ucopy)
    {
      _classifierInState = new ArrayList(_classifierInState);
      _classifierInState_ucopy = null;
    }
    _classifierInState.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("classifierInState", old, getClassifiersInState(), __arg);
    }
  }
  // opposite role: objectFlowState this role: type
  private final static Method _objectFlowState_setMethod = getMethod1(MClassifierImpl.class, "setObjectFlowStates", Collection.class);
  private final static Method _objectFlowState_addMethod = getMethod1(MClassifierImpl.class, "addObjectFlowState", MObjectFlowState.class);
  private final static Method _objectFlowState_removeMethod = getMethod1(MClassifierImpl.class, "removeObjectFlowState", MObjectFlowState.class);
  Collection _objectFlowState = Collections.EMPTY_LIST;
  Collection _objectFlowState_ucopy = Collections.EMPTY_LIST;
  public final Collection getObjectFlowStates()
  {
    checkExists();
    if (null == _objectFlowState_ucopy)
    {
      _objectFlowState_ucopy = ucopy(_objectFlowState);
    }
    return _objectFlowState_ucopy;
  }
  public final void setObjectFlowStates(Collection __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      if (__arg == null)
      {
        throw new NullPointerException();
      }
      final boolean sendEvent = needEvent();
      final boolean logForUndo = needUndo();
      Collection old = null;
      if (sendEvent || logForUndo)
      {
        old = getObjectFlowStates();
      }
      _objectFlowState_ucopy = null;
      Collection __added = bagdiff(__arg,_objectFlowState);
      Collection __removed = bagdiff(_objectFlowState, __arg);
      Iterator iter11 = __removed.iterator();
      while (iter11.hasNext())
      {
        MObjectFlowState o = (MObjectFlowState)iter11.next();
        o.internalUnrefByType(this);
      }
      Iterator iter12 = __added.iterator();
      while (iter12.hasNext())
      {
        MObjectFlowState o = (MObjectFlowState)iter12.next();
        o.internalRefByType(this);
      }
      _objectFlowState = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_objectFlowState_setMethod, old, getObjectFlowStates());
      }
      if (sendEvent)
      {
        fireBagSet("objectFlowState", old, getObjectFlowStates());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addObjectFlowState(MObjectFlowState __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      if (__arg == null)
      {
        throw new NullPointerException();
      }
      final boolean sendEvent = needEvent();
      Collection old = null;
      if (sendEvent)
      {
        old = getObjectFlowStates();
      }
      if (null != _objectFlowState_ucopy)
      {
        _objectFlowState = new ArrayList(_objectFlowState);
        _objectFlowState_ucopy = null;
      }
      __arg.internalRefByType(this);
      _objectFlowState.add(__arg);
      logBagAdd(_objectFlowState_addMethod, _objectFlowState_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("objectFlowState", old, getObjectFlowStates(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeObjectFlowState(MObjectFlowState __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      if (__arg == null)
      {
        throw new NullPointerException();
      }
      final boolean sendEvent = needEvent();
      Collection old = null;
      if (sendEvent)
      {
        old = getObjectFlowStates();
      }
      if (null != _objectFlowState_ucopy)
      {
        _objectFlowState = new ArrayList(_objectFlowState);
        _objectFlowState_ucopy = null;
      }
      if (!_objectFlowState.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByType(this);
      logBagRemove(_objectFlowState_removeMethod, _objectFlowState_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("objectFlowState", old, getObjectFlowStates(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByObjectFlowState(MObjectFlowState __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getObjectFlowStates();
    }
    if (null != _objectFlowState_ucopy)
    {
      _objectFlowState = new ArrayList(_objectFlowState);
      _objectFlowState_ucopy = null;
    }
    _objectFlowState.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("objectFlowState", old, getObjectFlowStates(), __arg);
    }
  }
  public final void internalUnrefByObjectFlowState(MObjectFlowState __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getObjectFlowStates();
    }
    if (null != _objectFlowState_ucopy)
    {
      _objectFlowState = new ArrayList(_objectFlowState);
      _objectFlowState_ucopy = null;
    }
    _objectFlowState.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("objectFlowState", old, getObjectFlowStates(), __arg);
    }
  }
  // opposite role: powertypeRange this role: powertype
  private final static Method _powertypeRange_setMethod = getMethod1(MClassifierImpl.class, "setPowertypeRanges", Collection.class);
  private final static Method _powertypeRange_addMethod = getMethod1(MClassifierImpl.class, "addPowertypeRange", MGeneralization.class);
  private final static Method _powertypeRange_removeMethod = getMethod1(MClassifierImpl.class, "removePowertypeRange", MGeneralization.class);
  Collection _powertypeRange = Collections.EMPTY_LIST;
  Collection _powertypeRange_ucopy = Collections.EMPTY_LIST;
  public final Collection getPowertypeRanges()
  {
    checkExists();
    if (null == _powertypeRange_ucopy)
    {
      _powertypeRange_ucopy = ucopy(_powertypeRange);
    }
    return _powertypeRange_ucopy;
  }
  public final void setPowertypeRanges(Collection __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      if (__arg == null)
      {
        throw new NullPointerException();
      }
      final boolean sendEvent = needEvent();
      final boolean logForUndo = needUndo();
      Collection old = null;
      if (sendEvent || logForUndo)
      {
        old = getPowertypeRanges();
      }
      _powertypeRange_ucopy = null;
      Collection __added = bagdiff(__arg,_powertypeRange);
      Collection __removed = bagdiff(_powertypeRange, __arg);
      Iterator iter13 = __removed.iterator();
      while (iter13.hasNext())
      {
        MGeneralization o = (MGeneralization)iter13.next();
        o.internalUnrefByPowertype(this);
      }
      Iterator iter14 = __added.iterator();
      while (iter14.hasNext())
      {
        MGeneralization o = (MGeneralization)iter14.next();
        o.internalRefByPowertype(this);
      }
      _powertypeRange = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_powertypeRange_setMethod, old, getPowertypeRanges());
      }
      if (sendEvent)
      {
        fireBagSet("powertypeRange", old, getPowertypeRanges());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addPowertypeRange(MGeneralization __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      if (__arg == null)
      {
        throw new NullPointerException();
      }
      final boolean sendEvent = needEvent();
      Collection old = null;
      if (sendEvent)
      {
        old = getPowertypeRanges();
      }
      if (null != _powertypeRange_ucopy)
      {
        _powertypeRange = new ArrayList(_powertypeRange);
        _powertypeRange_ucopy = null;
      }
      __arg.internalRefByPowertype(this);
      _powertypeRange.add(__arg);
      logBagAdd(_powertypeRange_addMethod, _powertypeRange_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("powertypeRange", old, getPowertypeRanges(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removePowertypeRange(MGeneralization __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      if (__arg == null)
      {
        throw new NullPointerException();
      }
      final boolean sendEvent = needEvent();
      Collection old = null;
      if (sendEvent)
      {
        old = getPowertypeRanges();
      }
      if (null != _powertypeRange_ucopy)
      {
        _powertypeRange = new ArrayList(_powertypeRange);
        _powertypeRange_ucopy = null;
      }
      if (!_powertypeRange.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByPowertype(this);
      logBagRemove(_powertypeRange_removeMethod, _powertypeRange_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("powertypeRange", old, getPowertypeRanges(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByPowertypeRange(MGeneralization __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getPowertypeRanges();
    }
    if (null != _powertypeRange_ucopy)
    {
      _powertypeRange = new ArrayList(_powertypeRange);
      _powertypeRange_ucopy = null;
    }
    _powertypeRange.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("powertypeRange", old, getPowertypeRanges(), __arg);
    }
  }
  public final void internalUnrefByPowertypeRange(MGeneralization __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getPowertypeRanges();
    }
    if (null != _powertypeRange_ucopy)
    {
      _powertypeRange = new ArrayList(_powertypeRange);
      _powertypeRange_ucopy = null;
    }
    _powertypeRange.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("powertypeRange", old, getPowertypeRanges(), __arg);
    }
  }
  // opposite role: participant this role: specification
  private final static Method _participant_setMethod = getMethod1(MClassifierImpl.class, "setParticipants", Collection.class);
  private final static Method _participant_addMethod = getMethod1(MClassifierImpl.class, "addParticipant", MAssociationEnd.class);
  private final static Method _participant_removeMethod = getMethod1(MClassifierImpl.class, "removeParticipant", MAssociationEnd.class);
  Collection _participant = Collections.EMPTY_LIST;
  Collection _participant_ucopy = Collections.EMPTY_LIST;
  public final Collection getParticipants()
  {
    checkExists();
    if (null == _participant_ucopy)
    {
      _participant_ucopy = ucopy(_participant);
    }
    return _participant_ucopy;
  }
  public final void setParticipants(Collection __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      if (__arg == null)
      {
        throw new NullPointerException();
      }
      final boolean sendEvent = needEvent();
      final boolean logForUndo = needUndo();
      Collection old = null;
      if (sendEvent || logForUndo)
      {
        old = getParticipants();
      }
      _participant_ucopy = null;
      Collection __added = bagdiff(__arg,_participant);
      Collection __removed = bagdiff(_participant, __arg);
      Iterator iter15 = __removed.iterator();
      while (iter15.hasNext())
      {
        MAssociationEnd o = (MAssociationEnd)iter15.next();
        o.internalUnrefBySpecification(this);
      }
      Iterator iter16 = __added.iterator();
      while (iter16.hasNext())
      {
        MAssociationEnd o = (MAssociationEnd)iter16.next();
        o.internalRefBySpecification(this);
      }
      _participant = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_participant_setMethod, old, getParticipants());
      }
      if (sendEvent)
      {
        fireBagSet("participant", old, getParticipants());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addParticipant(MAssociationEnd __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      if (__arg == null)
      {
        throw new NullPointerException();
      }
      final boolean sendEvent = needEvent();
      Collection old = null;
      if (sendEvent)
      {
        old = getParticipants();
      }
      if (null != _participant_ucopy)
      {
        _participant = new ArrayList(_participant);
        _participant_ucopy = null;
      }
      __arg.internalRefBySpecification(this);
      _participant.add(__arg);
      logBagAdd(_participant_addMethod, _participant_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("participant", old, getParticipants(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeParticipant(MAssociationEnd __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      if (__arg == null)
      {
        throw new NullPointerException();
      }
      final boolean sendEvent = needEvent();
      Collection old = null;
      if (sendEvent)
      {
        old = getParticipants();
      }
      if (null != _participant_ucopy)
      {
        _participant = new ArrayList(_participant);
        _participant_ucopy = null;
      }
      if (!_participant.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefBySpecification(this);
      logBagRemove(_participant_removeMethod, _participant_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("participant", old, getParticipants(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByParticipant(MAssociationEnd __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getParticipants();
    }
    if (null != _participant_ucopy)
    {
      _participant = new ArrayList(_participant);
      _participant_ucopy = null;
    }
    _participant.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("participant", old, getParticipants(), __arg);
    }
  }
  public final void internalUnrefByParticipant(MAssociationEnd __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getParticipants();
    }
    if (null != _participant_ucopy)
    {
      _participant = new ArrayList(_participant);
      _participant_ucopy = null;
    }
    _participant.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("participant", old, getParticipants(), __arg);
    }
  }
  // opposite role: associationEnd this role: type
  private final static Method _associationEnd_setMethod = getMethod1(MClassifierImpl.class, "setAssociationEnds", Collection.class);
  private final static Method _associationEnd_addMethod = getMethod1(MClassifierImpl.class, "addAssociationEnd", MAssociationEnd.class);
  private final static Method _associationEnd_removeMethod = getMethod1(MClassifierImpl.class, "removeAssociationEnd", MAssociationEnd.class);
  Collection _associationEnd = Collections.EMPTY_LIST;
  Collection _associationEnd_ucopy = Collections.EMPTY_LIST;
  public final Collection getAssociationEnds()
  {
    checkExists();
    if (null == _associationEnd_ucopy)
    {
      _associationEnd_ucopy = ucopy(_associationEnd);
    }
    return _associationEnd_ucopy;
  }
  public final void setAssociationEnds(Collection __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      if (__arg == null)
      {
        throw new NullPointerException();
      }
      final boolean sendEvent = needEvent();
      final boolean logForUndo = needUndo();
      Collection old = null;
      if (sendEvent || logForUndo)
      {
        old = getAssociationEnds();
      }
      _associationEnd_ucopy = null;
      Collection __added = bagdiff(__arg,_associationEnd);
      Collection __removed = bagdiff(_associationEnd, __arg);
      Iterator iter17 = __removed.iterator();
      while (iter17.hasNext())
      {
        MAssociationEnd o = (MAssociationEnd)iter17.next();
        o.internalUnrefByType(this);
      }
      Iterator iter18 = __added.iterator();
      while (iter18.hasNext())
      {
        MAssociationEnd o = (MAssociationEnd)iter18.next();
        o.internalRefByType(this);
      }
      _associationEnd = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_associationEnd_setMethod, old, getAssociationEnds());
      }
      if (sendEvent)
      {
        fireBagSet("associationEnd", old, getAssociationEnds());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addAssociationEnd(MAssociationEnd __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      if (__arg == null)
      {
        throw new NullPointerException();
      }
      final boolean sendEvent = needEvent();
      Collection old = null;
      if (sendEvent)
      {
        old = getAssociationEnds();
      }
      if (null != _associationEnd_ucopy)
      {
        _associationEnd = new ArrayList(_associationEnd);
        _associationEnd_ucopy = null;
      }
      __arg.internalRefByType(this);
      _associationEnd.add(__arg);
      logBagAdd(_associationEnd_addMethod, _associationEnd_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("associationEnd", old, getAssociationEnds(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeAssociationEnd(MAssociationEnd __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      if (__arg == null)
      {
        throw new NullPointerException();
      }
      final boolean sendEvent = needEvent();
      Collection old = null;
      if (sendEvent)
      {
        old = getAssociationEnds();
      }
      if (null != _associationEnd_ucopy)
      {
        _associationEnd = new ArrayList(_associationEnd);
        _associationEnd_ucopy = null;
      }
      if (!_associationEnd.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByType(this);
      logBagRemove(_associationEnd_removeMethod, _associationEnd_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("associationEnd", old, getAssociationEnds(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByAssociationEnd(MAssociationEnd __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getAssociationEnds();
    }
    if (null != _associationEnd_ucopy)
    {
      _associationEnd = new ArrayList(_associationEnd);
      _associationEnd_ucopy = null;
    }
    _associationEnd.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("associationEnd", old, getAssociationEnds(), __arg);
    }
  }
  public final void internalUnrefByAssociationEnd(MAssociationEnd __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getAssociationEnds();
    }
    if (null != _associationEnd_ucopy)
    {
      _associationEnd = new ArrayList(_associationEnd);
      _associationEnd_ucopy = null;
    }
    _associationEnd.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("associationEnd", old, getAssociationEnds(), __arg);
    }
  }
  // opposite role: parameter this role: type
  private final static Method _parameter_setMethod = getMethod1(MClassifierImpl.class, "setParameters", Collection.class);
  private final static Method _parameter_addMethod = getMethod1(MClassifierImpl.class, "addParameter", MParameter.class);
  private final static Method _parameter_removeMethod = getMethod1(MClassifierImpl.class, "removeParameter", MParameter.class);
  Collection _parameter = Collections.EMPTY_LIST;
  Collection _parameter_ucopy = Collections.EMPTY_LIST;
  public final Collection getParameters()
  {
    checkExists();
    if (null == _parameter_ucopy)
    {
      _parameter_ucopy = ucopy(_parameter);
    }
    return _parameter_ucopy;
  }
  public final void setParameters(Collection __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      if (__arg == null)
      {
        throw new NullPointerException();
      }
      final boolean sendEvent = needEvent();
      final boolean logForUndo = needUndo();
      Collection old = null;
      if (sendEvent || logForUndo)
      {
        old = getParameters();
      }
      _parameter_ucopy = null;
      Collection __added = bagdiff(__arg,_parameter);
      Collection __removed = bagdiff(_parameter, __arg);
      Iterator iter19 = __removed.iterator();
      while (iter19.hasNext())
      {
        MParameter o = (MParameter)iter19.next();
        o.internalUnrefByType(this);
      }
      Iterator iter20 = __added.iterator();
      while (iter20.hasNext())
      {
        MParameter o = (MParameter)iter20.next();
        o.internalRefByType(this);
      }
      _parameter = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_parameter_setMethod, old, getParameters());
      }
      if (sendEvent)
      {
        fireBagSet("parameter", old, getParameters());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addParameter(MParameter __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      if (__arg == null)
      {
        throw new NullPointerException();
      }
      final boolean sendEvent = needEvent();
      Collection old = null;
      if (sendEvent)
      {
        old = getParameters();
      }
      if (null != _parameter_ucopy)
      {
        _parameter = new ArrayList(_parameter);
        _parameter_ucopy = null;
      }
      __arg.internalRefByType(this);
      _parameter.add(__arg);
      logBagAdd(_parameter_addMethod, _parameter_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("parameter", old, getParameters(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeParameter(MParameter __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      if (__arg == null)
      {
        throw new NullPointerException();
      }
      final boolean sendEvent = needEvent();
      Collection old = null;
      if (sendEvent)
      {
        old = getParameters();
      }
      if (null != _parameter_ucopy)
      {
        _parameter = new ArrayList(_parameter);
        _parameter_ucopy = null;
      }
      if (!_parameter.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByType(this);
      logBagRemove(_parameter_removeMethod, _parameter_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("parameter", old, getParameters(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByParameter(MParameter __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getParameters();
    }
    if (null != _parameter_ucopy)
    {
      _parameter = new ArrayList(_parameter);
      _parameter_ucopy = null;
    }
    _parameter.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("parameter", old, getParameters(), __arg);
    }
  }
  public final void internalUnrefByParameter(MParameter __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getParameters();
    }
    if (null != _parameter_ucopy)
    {
      _parameter = new ArrayList(_parameter);
      _parameter_ucopy = null;
    }
    _parameter.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("parameter", old, getParameters(), __arg);
    }
  }
  // opposite role: structuralFeature this role: type
  private final static Method _structuralFeature_setMethod = getMethod1(MClassifierImpl.class, "setStructuralFeatures", Collection.class);
  private final static Method _structuralFeature_addMethod = getMethod1(MClassifierImpl.class, "addStructuralFeature", MStructuralFeature.class);
  private final static Method _structuralFeature_removeMethod = getMethod1(MClassifierImpl.class, "removeStructuralFeature", MStructuralFeature.class);
  Collection _structuralFeature = Collections.EMPTY_LIST;
  Collection _structuralFeature_ucopy = Collections.EMPTY_LIST;
  public final Collection getStructuralFeatures()
  {
    checkExists();
    if (null == _structuralFeature_ucopy)
    {
      _structuralFeature_ucopy = ucopy(_structuralFeature);
    }
    return _structuralFeature_ucopy;
  }
  public final void setStructuralFeatures(Collection __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      if (__arg == null)
      {
        throw new NullPointerException();
      }
      final boolean sendEvent = needEvent();
      final boolean logForUndo = needUndo();
      Collection old = null;
      if (sendEvent || logForUndo)
      {
        old = getStructuralFeatures();
      }
      _structuralFeature_ucopy = null;
      Collection __added = bagdiff(__arg,_structuralFeature);
      Collection __removed = bagdiff(_structuralFeature, __arg);
      Iterator iter21 = __removed.iterator();
      while (iter21.hasNext())
      {
        MStructuralFeature o = (MStructuralFeature)iter21.next();
        o.internalUnrefByType(this);
      }
      Iterator iter22 = __added.iterator();
      while (iter22.hasNext())
      {
        MStructuralFeature o = (MStructuralFeature)iter22.next();
        o.internalRefByType(this);
      }
      _structuralFeature = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_structuralFeature_setMethod, old, getStructuralFeatures());
      }
      if (sendEvent)
      {
        fireBagSet("structuralFeature", old, getStructuralFeatures());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addStructuralFeature(MStructuralFeature __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      if (__arg == null)
      {
        throw new NullPointerException();
      }
      final boolean sendEvent = needEvent();
      Collection old = null;
      if (sendEvent)
      {
        old = getStructuralFeatures();
      }
      if (null != _structuralFeature_ucopy)
      {
        _structuralFeature = new ArrayList(_structuralFeature);
        _structuralFeature_ucopy = null;
      }
      __arg.internalRefByType(this);
      _structuralFeature.add(__arg);
      logBagAdd(_structuralFeature_addMethod, _structuralFeature_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("structuralFeature", old, getStructuralFeatures(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeStructuralFeature(MStructuralFeature __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      if (__arg == null)
      {
        throw new NullPointerException();
      }
      final boolean sendEvent = needEvent();
      Collection old = null;
      if (sendEvent)
      {
        old = getStructuralFeatures();
      }
      if (null != _structuralFeature_ucopy)
      {
        _structuralFeature = new ArrayList(_structuralFeature);
        _structuralFeature_ucopy = null;
      }
      if (!_structuralFeature.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByType(this);
      logBagRemove(_structuralFeature_removeMethod, _structuralFeature_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("structuralFeature", old, getStructuralFeatures(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByStructuralFeature(MStructuralFeature __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getStructuralFeatures();
    }
    if (null != _structuralFeature_ucopy)
    {
      _structuralFeature = new ArrayList(_structuralFeature);
      _structuralFeature_ucopy = null;
    }
    _structuralFeature.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("structuralFeature", old, getStructuralFeatures(), __arg);
    }
  }
  public final void internalUnrefByStructuralFeature(MStructuralFeature __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getStructuralFeatures();
    }
    if (null != _structuralFeature_ucopy)
    {
      _structuralFeature = new ArrayList(_structuralFeature);
      _structuralFeature_ucopy = null;
    }
    _structuralFeature.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("structuralFeature", old, getStructuralFeatures(), __arg);
    }
  }
  // opposite role: feature this role: owner
  private final static Method _feature_setMethod = getMethod1(MClassifierImpl.class, "setFeatures", List.class);
  private final static Method _feature_removeMethod = getMethod1(MClassifierImpl.class, "removeFeature", int.class);
  private final static Method _feature_addMethod = getMethod2(MClassifierImpl.class, "addFeature", int.class, MFeature.class);
  private final static Method _feature_listSetMethod = getMethod2(MClassifierImpl.class, "setFeature", int.class, MFeature.class);
  List _feature = Collections.EMPTY_LIST;
  List _feature_ucopy = Collections.EMPTY_LIST;
  public final List getFeatures()
  {
    checkExists();
    if (null == _feature_ucopy)
    {
      _feature_ucopy = ucopy(_feature);
    }
    return _feature_ucopy;
  }
  public final void setFeatures(List __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      if (__arg == null)
      {
        throw new NullPointerException();
      }
      final boolean sendEvent = needEvent();
      final boolean logForUndo = needUndo();
      List old = null;
      if (sendEvent || logForUndo)
      {
        old = getFeatures();
      }
      _feature_ucopy = null;
      Collection __added = bagdiff(__arg,_feature);
      Collection __removed = bagdiff(_feature, __arg);
      Iterator iter23 = __removed.iterator();
      while (iter23.hasNext())
      {
        MFeature o = (MFeature)iter23.next();
        o.internalUnrefByOwner(this);
      }
      Iterator iter24 = __added.iterator();
      while (iter24.hasNext())
      {
        MFeature o = (MFeature)iter24.next();
        o.internalRefByOwner(this);
      }
      _feature = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_feature_setMethod, old, getFeatures());
      }
      if (sendEvent)
      {
        fireListSet("feature", old, getFeatures());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addFeature(MFeature __arg)
  {
    addFeature(_feature.size(), __arg);
  }
  public final void removeFeature(MFeature __arg)
  {
    if (__arg == null)
    {
      throw new NullPointerException();
    }
    int __pos = _feature.indexOf(__arg);
    removeFeature(__pos);
  }
  public final void addFeature(int __pos, MFeature __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      if (__arg == null)
      {
        throw new NullPointerException();
      }
      final boolean sendEvent = needEvent();
      List old = null;
      if (sendEvent)
      {
        old = getFeatures();
      }
      if (null != _feature_ucopy)
      {
        _feature = new ArrayList(_feature);
        _feature_ucopy = null;
      }
      _feature.add(__pos, __arg);
      __arg.internalRefByOwner(this);
      logListAdd(_feature_addMethod, _feature_removeMethod, __arg, __pos);
      if (sendEvent)
      {
        fireListAdd("feature", old, getFeatures(), __arg, __pos);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeFeature(int __pos)
  {
    operationStarted();
    try
    {
      checkExists();
      final boolean sendEvent = needEvent();
      List old = null;
      if (sendEvent)
      {
        old = getFeatures();
      }
      if (null != _feature_ucopy)
      {
        _feature = new ArrayList(_feature);
        _feature_ucopy = null;
      }
      MFeature __arg = (MFeature)_feature.remove(__pos);
      __arg.internalUnrefByOwner(this);
      logListRemove(_feature_removeMethod, _feature_addMethod, __arg, __pos);
      if (sendEvent)
      {
        fireListRemove("feature", old, getFeatures(), __arg, __pos);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void setFeature(int __pos, MFeature __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      if (__arg == null)
      {
        throw new NullPointerException();
      }
      final boolean sendEvent = needEvent();
      List old = null;
      if (sendEvent)
      {
        old = getFeatures();
      }
      if (null != _feature_ucopy)
      {
        _feature = new ArrayList(_feature);
        _feature_ucopy = null;
      }
      MFeature __old = (MFeature)_feature.get(__pos);
      __old.internalUnrefByOwner(this);
      __arg.internalRefByOwner(this);
      _feature.set(__pos,__arg);
      logListSet(_feature_listSetMethod, __old, __arg, __pos);
      if (sendEvent)
      {
        fireListItemSet("feature", old, getFeatures(), __old, __arg, __pos);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final MFeature getFeature(int __pos)
  {
    checkExists();
    return (MFeature)_feature.get(__pos);
  }
  // ------------ code for class GeneralizableElement -----------------
  // generating attributes
  // attribute: isAbstract
  private final static Method _isAbstract_setMethod = getMethod1(MClassifierImpl.class, "setAbstract", boolean.class);
  boolean _isAbstract;
  public final boolean isAbstract()
  {
    checkExists();
    return _isAbstract;
  }
  public final void setAbstract(boolean __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      logAttrSet(_isAbstract_setMethod, _isAbstract, __arg);
      fireAttrSet("isAbstract", _isAbstract, __arg);
      _isAbstract = __arg;
    }
    finally
    {
      operationFinished();
    }
  }
  // attribute: isLeaf
  private final static Method _isLeaf_setMethod = getMethod1(MClassifierImpl.class, "setLeaf", boolean.class);
  boolean _isLeaf;
  public final boolean isLeaf()
  {
    checkExists();
    return _isLeaf;
  }
  public final void setLeaf(boolean __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      logAttrSet(_isLeaf_setMethod, _isLeaf, __arg);
      fireAttrSet("isLeaf", _isLeaf, __arg);
      _isLeaf = __arg;
    }
    finally
    {
      operationFinished();
    }
  }
  // attribute: isRoot
  private final static Method _isRoot_setMethod = getMethod1(MClassifierImpl.class, "setRoot", boolean.class);
  boolean _isRoot;
  public final boolean isRoot()
  {
    checkExists();
    return _isRoot;
  }
  public final void setRoot(boolean __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      logAttrSet(_isRoot_setMethod, _isRoot, __arg);
      fireAttrSet("isRoot", _isRoot, __arg);
      _isRoot = __arg;
    }
    finally
    {
      operationFinished();
    }
  }
  // generating associations
  // opposite role: specialization this role: parent
  private final static Method _specialization_setMethod = getMethod1(MClassifierImpl.class, "setSpecializations", Collection.class);
  private final static Method _specialization_addMethod = getMethod1(MClassifierImpl.class, "addSpecialization", MGeneralization.class);
  private final static Method _specialization_removeMethod = getMethod1(MClassifierImpl.class, "removeSpecialization", MGeneralization.class);
  Collection _specialization = Collections.EMPTY_LIST;
  Collection _specialization_ucopy = Collections.EMPTY_LIST;
  public final Collection getSpecializations()
  {
    checkExists();
    if (null == _specialization_ucopy)
    {
      _specialization_ucopy = ucopy(_specialization);
    }
    return _specialization_ucopy;
  }
  public final void setSpecializations(Collection __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      if (__arg == null)
      {
        throw new NullPointerException();
      }
      final boolean sendEvent = needEvent();
      final boolean logForUndo = needUndo();
      Collection old = null;
      if (sendEvent || logForUndo)
      {
        old = getSpecializations();
      }
      _specialization_ucopy = null;
      Collection __added = bagdiff(__arg,_specialization);
      Collection __removed = bagdiff(_specialization, __arg);
      Iterator iter25 = __removed.iterator();
      while (iter25.hasNext())
      {
        MGeneralization o = (MGeneralization)iter25.next();
        o.internalUnrefByParent(this);
      }
      Iterator iter26 = __added.iterator();
      while (iter26.hasNext())
      {
        MGeneralization o = (MGeneralization)iter26.next();
        o.internalRefByParent(this);
      }
      _specialization = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_specialization_setMethod, old, getSpecializations());
      }
      if (sendEvent)
      {
        fireBagSet("specialization", old, getSpecializations());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addSpecialization(MGeneralization __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      if (__arg == null)
      {
        throw new NullPointerException();
      }
      final boolean sendEvent = needEvent();
      Collection old = null;
      if (sendEvent)
      {
        old = getSpecializations();
      }
      if (null != _specialization_ucopy)
      {
        _specialization = new ArrayList(_specialization);
        _specialization_ucopy = null;
      }
      __arg.internalRefByParent(this);
      _specialization.add(__arg);
      logBagAdd(_specialization_addMethod, _specialization_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("specialization", old, getSpecializations(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeSpecialization(MGeneralization __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      if (__arg == null)
      {
        throw new NullPointerException();
      }
      final boolean sendEvent = needEvent();
      Collection old = null;
      if (sendEvent)
      {
        old = getSpecializations();
      }
      if (null != _specialization_ucopy)
      {
        _specialization = new ArrayList(_specialization);
        _specialization_ucopy = null;
      }
      if (!_specialization.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByParent(this);
      logBagRemove(_specialization_removeMethod, _specialization_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("specialization", old, getSpecializations(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefBySpecialization(MGeneralization __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getSpecializations();
    }
    if (null != _specialization_ucopy)
    {
      _specialization = new ArrayList(_specialization);
      _specialization_ucopy = null;
    }
    _specialization.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("specialization", old, getSpecializations(), __arg);
    }
  }
  public final void internalUnrefBySpecialization(MGeneralization __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getSpecializations();
    }
    if (null != _specialization_ucopy)
    {
      _specialization = new ArrayList(_specialization);
      _specialization_ucopy = null;
    }
    _specialization.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("specialization", old, getSpecializations(), __arg);
    }
  }
  // opposite role: generalization this role: child
  private final static Method _generalization_setMethod = getMethod1(MClassifierImpl.class, "setGeneralizations", Collection.class);
  private final static Method _generalization_addMethod = getMethod1(MClassifierImpl.class, "addGeneralization", MGeneralization.class);
  private final static Method _generalization_removeMethod = getMethod1(MClassifierImpl.class, "removeGeneralization", MGeneralization.class);
  Collection _generalization = Collections.EMPTY_LIST;
  Collection _generalization_ucopy = Collections.EMPTY_LIST;
  public final Collection getGeneralizations()
  {
    checkExists();
    if (null == _generalization_ucopy)
    {
      _generalization_ucopy = ucopy(_generalization);
    }
    return _generalization_ucopy;
  }
  public final void setGeneralizations(Collection __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      if (__arg == null)
      {
        throw new NullPointerException();
      }
      final boolean sendEvent = needEvent();
      final boolean logForUndo = needUndo();
      Collection old = null;
      if (sendEvent || logForUndo)
      {
        old = getGeneralizations();
      }
      _generalization_ucopy = null;
      Collection __added = bagdiff(__arg,_generalization);
      Collection __removed = bagdiff(_generalization, __arg);
      Iterator iter27 = __removed.iterator();
      while (iter27.hasNext())
      {
        MGeneralization o = (MGeneralization)iter27.next();
        o.internalUnrefByChild(this);
      }
      Iterator iter28 = __added.iterator();
      while (iter28.hasNext())
      {
        MGeneralization o = (MGeneralization)iter28.next();
        o.internalRefByChild(this);
      }
      _generalization = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_generalization_setMethod, old, getGeneralizations());
      }
      if (sendEvent)
      {
        fireBagSet("generalization", old, getGeneralizations());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addGeneralization(MGeneralization __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      if (__arg == null)
      {
        throw new NullPointerException();
      }
      final boolean sendEvent = needEvent();
      Collection old = null;
      if (sendEvent)
      {
        old = getGeneralizations();
      }
      if (null != _generalization_ucopy)
      {
        _generalization = new ArrayList(_generalization);
        _generalization_ucopy = null;
      }
      __arg.internalRefByChild(this);
      _generalization.add(__arg);
      logBagAdd(_generalization_addMethod, _generalization_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("generalization", old, getGeneralizations(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeGeneralization(MGeneralization __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      if (__arg == null)
      {
        throw new NullPointerException();
      }
      final boolean sendEvent = needEvent();
      Collection old = null;
      if (sendEvent)
      {
        old = getGeneralizations();
      }
      if (null != _generalization_ucopy)
      {
        _generalization = new ArrayList(_generalization);
        _generalization_ucopy = null;
      }
      if (!_generalization.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByChild(this);
      logBagRemove(_generalization_removeMethod, _generalization_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("generalization", old, getGeneralizations(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByGeneralization(MGeneralization __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getGeneralizations();
    }
    if (null != _generalization_ucopy)
    {
      _generalization = new ArrayList(_generalization);
      _generalization_ucopy = null;
    }
    _generalization.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("generalization", old, getGeneralizations(), __arg);
    }
  }
  public final void internalUnrefByGeneralization(MGeneralization __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getGeneralizations();
    }
    if (null != _generalization_ucopy)
    {
      _generalization = new ArrayList(_generalization);
      _generalization_ucopy = null;
    }
    _generalization.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("generalization", old, getGeneralizations(), __arg);
    }
  }
  protected void cleanup(Collection scheduledForRemove)
  {
    // opposite role: createAction this role: instantiation
    if (_createAction.size() != 0)
    {
      setCreateActions(Collections.EMPTY_LIST);
    }
    // opposite role: instance this role: classifier
    if (_instance.size() != 0)
    {
      setInstances(Collections.EMPTY_LIST);
    }
    // opposite role: collaboration this role: representedClassifier
    if (_collaboration.size() != 0)
    {
      setCollaborations(Collections.EMPTY_LIST);
    }
    // opposite role: classifierRole this role: base
    if (_classifierRole.size() != 0)
    {
      setClassifierRoles(Collections.EMPTY_LIST);
    }
    // opposite role: classifierInState this role: type
    if (_classifierInState.size() != 0)
    {
      setClassifiersInState(Collections.EMPTY_LIST);
    }
    // opposite role: objectFlowState this role: type
    if (_objectFlowState.size() != 0)
    {
      setObjectFlowStates(Collections.EMPTY_LIST);
    }
    // opposite role: powertypeRange this role: powertype
    if (_powertypeRange.size() != 0)
    {
      setPowertypeRanges(Collections.EMPTY_LIST);
    }
    // opposite role: participant this role: specification
    if (_participant.size() != 0)
    {
      setParticipants(Collections.EMPTY_LIST);
    }
    // opposite role: associationEnd this role: type
    if (_associationEnd.size() != 0)
    {
      setAssociationEnds(Collections.EMPTY_LIST);
    }
    // opposite role: parameter this role: type
    if (_parameter.size() != 0)
    {
      setParameters(Collections.EMPTY_LIST);
    }
    // opposite role: structuralFeature this role: type
    if (_structuralFeature.size() != 0)
    {
      setStructuralFeatures(Collections.EMPTY_LIST);
    }
    // opposite role: feature this role: owner
    if (_feature.size() != 0)
    {
      scheduledForRemove.addAll(_feature);
      setFeatures(Collections.EMPTY_LIST);
    }
    // opposite role: specialization this role: parent
    if (_specialization.size() != 0)
    {
      setSpecializations(Collections.EMPTY_LIST);
    }
    // opposite role: generalization this role: child
    if (_generalization.size() != 0)
    {
      setGeneralizations(Collections.EMPTY_LIST);
    }
    super.cleanup(scheduledForRemove);
  }

  public String getUMLClassName()
  {
    return "Classifier";
  }

  // Reflective API

  public Object reflectiveGetValue(String feature)
  {
    if ("createAction".equals(feature))
    {
      return getCreateActions();
    }
    if ("instance".equals(feature))
    {
      return getInstances();
    }
    if ("collaboration".equals(feature))
    {
      return getCollaborations();
    }
    if ("classifierRole".equals(feature))
    {
      return getClassifierRoles();
    }
    if ("classifierInState".equals(feature))
    {
      return getClassifiersInState();
    }
    if ("objectFlowState".equals(feature))
    {
      return getObjectFlowStates();
    }
    if ("powertypeRange".equals(feature))
    {
      return getPowertypeRanges();
    }
    if ("participant".equals(feature))
    {
      return getParticipants();
    }
    if ("associationEnd".equals(feature))
    {
      return getAssociationEnds();
    }
    if ("parameter".equals(feature))
    {
      return getParameters();
    }
    if ("structuralFeature".equals(feature))
    {
      return getStructuralFeatures();
    }
    if ("feature".equals(feature))
    {
      return getFeatures();
    }
    if ("isAbstract".equals(feature))
    {
      return new Boolean(isAbstract());
    }
    if ("isLeaf".equals(feature))
    {
      return new Boolean(isLeaf());
    }
    if ("isRoot".equals(feature))
    {
      return new Boolean(isRoot());
    }
    if ("specialization".equals(feature))
    {
      return getSpecializations();
    }
    if ("generalization".equals(feature))
    {
      return getGeneralizations();
    }

    return super.reflectiveGetValue(feature);
  }

  public void reflectiveSetValue(String feature, Object obj)
  {
    if ("createAction".equals(feature))
    {
      setCreateActions((Collection)obj);
      return;
    }
    if ("instance".equals(feature))
    {
      setInstances((Collection)obj);
      return;
    }
    if ("collaboration".equals(feature))
    {
      setCollaborations((Collection)obj);
      return;
    }
    if ("classifierRole".equals(feature))
    {
      setClassifierRoles((Collection)obj);
      return;
    }
    if ("classifierInState".equals(feature))
    {
      setClassifiersInState((Collection)obj);
      return;
    }
    if ("objectFlowState".equals(feature))
    {
      setObjectFlowStates((Collection)obj);
      return;
    }
    if ("powertypeRange".equals(feature))
    {
      setPowertypeRanges((Collection)obj);
      return;
    }
    if ("participant".equals(feature))
    {
      setParticipants((Collection)obj);
      return;
    }
    if ("associationEnd".equals(feature))
    {
      setAssociationEnds((Collection)obj);
      return;
    }
    if ("parameter".equals(feature))
    {
      setParameters((Collection)obj);
      return;
    }
    if ("structuralFeature".equals(feature))
    {
      setStructuralFeatures((Collection)obj);
      return;
    }
    if ("feature".equals(feature))
    {
      setFeatures((List)obj);
      return;
    }
    if ("isAbstract".equals(feature))
    {
      setAbstract(((Boolean)obj).booleanValue());
      return;
    }
    if ("isLeaf".equals(feature))
    {
      setLeaf(((Boolean)obj).booleanValue());
      return;
    }
    if ("isRoot".equals(feature))
    {
      setRoot(((Boolean)obj).booleanValue());
      return;
    }
    if ("specialization".equals(feature))
    {
      setSpecializations((Collection)obj);
      return;
    }
    if ("generalization".equals(feature))
    {
      setGeneralizations((Collection)obj);
      return;
    }

    super.reflectiveSetValue(feature, obj);
  }

  public void reflectiveAddValue(String feature, Object obj)
  {
    if ("createAction".equals(feature))
    {
      addCreateAction((MCreateAction)obj);
      return;
    }
    if ("instance".equals(feature))
    {
      addInstance((MInstance)obj);
      return;
    }
    if ("collaboration".equals(feature))
    {
      addCollaboration((MCollaboration)obj);
      return;
    }
    if ("classifierRole".equals(feature))
    {
      addClassifierRole((MClassifierRole)obj);
      return;
    }
    if ("classifierInState".equals(feature))
    {
      addClassifierInState((MClassifierInState)obj);
      return;
    }
    if ("objectFlowState".equals(feature))
    {
      addObjectFlowState((MObjectFlowState)obj);
      return;
    }
    if ("powertypeRange".equals(feature))
    {
      addPowertypeRange((MGeneralization)obj);
      return;
    }
    if ("participant".equals(feature))
    {
      addParticipant((MAssociationEnd)obj);
      return;
    }
    if ("associationEnd".equals(feature))
    {
      addAssociationEnd((MAssociationEnd)obj);
      return;
    }
    if ("parameter".equals(feature))
    {
      addParameter((MParameter)obj);
      return;
    }
    if ("structuralFeature".equals(feature))
    {
      addStructuralFeature((MStructuralFeature)obj);
      return;
    }
    if ("feature".equals(feature))
    {
      addFeature((MFeature)obj);
      return;
    }
    if ("specialization".equals(feature))
    {
      addSpecialization((MGeneralization)obj);
      return;
    }
    if ("generalization".equals(feature))
    {
      addGeneralization((MGeneralization)obj);
      return;
    }

    super.reflectiveAddValue(feature, obj);
  }

  public void reflectiveRemoveValue(String feature, Object obj)
  {
    if ("createAction".equals(feature))
    {
      removeCreateAction((MCreateAction)obj);
      return;
    }
    if ("instance".equals(feature))
    {
      removeInstance((MInstance)obj);
      return;
    }
    if ("collaboration".equals(feature))
    {
      removeCollaboration((MCollaboration)obj);
      return;
    }
    if ("classifierRole".equals(feature))
    {
      removeClassifierRole((MClassifierRole)obj);
      return;
    }
    if ("classifierInState".equals(feature))
    {
      removeClassifierInState((MClassifierInState)obj);
      return;
    }
    if ("objectFlowState".equals(feature))
    {
      removeObjectFlowState((MObjectFlowState)obj);
      return;
    }
    if ("powertypeRange".equals(feature))
    {
      removePowertypeRange((MGeneralization)obj);
      return;
    }
    if ("participant".equals(feature))
    {
      removeParticipant((MAssociationEnd)obj);
      return;
    }
    if ("associationEnd".equals(feature))
    {
      removeAssociationEnd((MAssociationEnd)obj);
      return;
    }
    if ("parameter".equals(feature))
    {
      removeParameter((MParameter)obj);
      return;
    }
    if ("structuralFeature".equals(feature))
    {
      removeStructuralFeature((MStructuralFeature)obj);
      return;
    }
    if ("feature".equals(feature))
    {
      removeFeature((MFeature)obj);
      return;
    }
    if ("specialization".equals(feature))
    {
      removeSpecialization((MGeneralization)obj);
      return;
    }
    if ("generalization".equals(feature))
    {
      removeGeneralization((MGeneralization)obj);
      return;
    }

    super.reflectiveRemoveValue(feature, obj);
  }

  public Object reflectiveGetValue(String feature, int pos)
  {
    if ("feature".equals(feature))
    {
      return getFeature(pos);
    }

    return super.reflectiveGetValue(feature, pos);
  }

  public void reflectiveSetValue(String feature, int pos, Object obj)
  {
    if ("feature".equals(feature))
    {
      setFeature(pos, (MFeature)obj);
      return;
    }

    super.reflectiveSetValue(feature, pos, obj);
  }

  public void reflectiveAddValue(String feature, int pos, Object obj)
  {
    if ("feature".equals(feature))
    {
      addFeature(pos, (MFeature)obj);
      return;
    }

    super.reflectiveAddValue(feature, pos, obj);
  }

  public void reflectiveRemoveValue(String feature, int pos)
  {
    if ("feature".equals(feature))
    {
      removeFeature(pos);
      return;
    }

    super.reflectiveRemoveValue(feature, pos);
  }
  public Collection getModelElementContents()
  {
    Collection ret = super.getModelElementContents();
    ret.addAll(getFeatures());
    return ret;
  }
  //public boolean isRoot()
  //{
  //  return getGeneralizations().size() == 0;
  //}

  //public boolean isLeaf()
  //{
  //  return getSpecializations().size() == 0;
  //}

  /** get parents */
  public List getChildren()
  {
    List rc = new ArrayList();
    Iterator i = getSpecializations().iterator();
    while(i.hasNext())
    {
      ru.novosoft.uml.foundation.core.MGeneralization g = (ru.novosoft.uml.foundation.core.MGeneralization)i.next();
      rc.add(g.getChild());
    }
    return rc;
  }

  /** get childs */
  public List getParents()
  {
    List rc = new ArrayList();
    Iterator i = getGeneralizations().iterator();
    while(i.hasNext())
    {
      ru.novosoft.uml.foundation.core.MGeneralization g = (ru.novosoft.uml.foundation.core.MGeneralization)i.next();
      rc.add(g.getParent());
    }
    return rc;
  }
}
