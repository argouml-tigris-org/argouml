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

package ru.novosoft.uml.behavior.state_machines;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import ru.novosoft.uml.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;
import ru.novosoft.uml.behavior.common_behavior.*;
import ru.novosoft.uml.behavior.collaborations.*;
import ru.novosoft.uml.behavior.use_cases.*;
import ru.novosoft.uml.behavior.activity_graphs.*;
import ru.novosoft.uml.model_management.*;

import java.lang.reflect.Method;

public class MStateImpl extends MStateVertexImpl implements MState
{
  // ------------ code for class State -----------------
  // generating attributes
  // generating associations
  // opposite role: doActivity this role: state3
  private final static Method _doActivity_setMethod = getMethod1(MStateImpl.class, "setDoActivity", MAction.class);
  MAction _doActivity;
  public final MAction getDoActivity()
  {
    checkExists();
    return _doActivity;
  }
  public final void setDoActivity(MAction __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      MAction __saved = _doActivity;
      if (_doActivity != __arg)
      {
        if (__saved != null)
        {
          __saved.internalUnrefByState3(this);
        }
        if (__arg != null)
        {
          __arg.internalRefByState3(this);
        }
        logRefSet(_doActivity_setMethod, __saved, __arg);
        fireRefSet("doActivity", __saved, __arg);
        _doActivity = __arg;
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByDoActivity(MAction __arg)
  {
    MAction __saved = _doActivity;
    if (__saved != null)
    {
      __saved.setState3(null);
    }
    fireRefSet("doActivity", __saved, __arg);
    _doActivity = __arg;
  }
  public final void internalUnrefByDoActivity(MAction __arg)
  {
    fireRefSet("doActivity", _doActivity, null);
    _doActivity = null;
  }
  // opposite role: internalTransition this role: state
  private final static Method _internalTransition_setMethod = getMethod1(MStateImpl.class, "setInternalTransitions", Collection.class);
  private final static Method _internalTransition_addMethod = getMethod1(MStateImpl.class, "addInternalTransition", MTransition.class);
  private final static Method _internalTransition_removeMethod = getMethod1(MStateImpl.class, "removeInternalTransition", MTransition.class);
  Collection _internalTransition = Collections.EMPTY_LIST;
  Collection _internalTransition_ucopy = Collections.EMPTY_LIST;
  public final Collection getInternalTransitions()
  {
    checkExists();
    if (null == _internalTransition_ucopy)
    {
      _internalTransition_ucopy = ucopy(_internalTransition);
    }
    return _internalTransition_ucopy;
  }
  public final void setInternalTransitions(Collection __arg)
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
        old = getInternalTransitions();
      }
      _internalTransition_ucopy = null;
      Collection __added = bagdiff(__arg,_internalTransition);
      Collection __removed = bagdiff(_internalTransition, __arg);
      Iterator iter1 = __removed.iterator();
      while (iter1.hasNext())
      {
        MTransition o = (MTransition)iter1.next();
        o.internalUnrefByState(this);
      }
      Iterator iter2 = __added.iterator();
      while (iter2.hasNext())
      {
        MTransition o = (MTransition)iter2.next();
        o.internalRefByState(this);
      }
      _internalTransition = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_internalTransition_setMethod, old, getInternalTransitions());
      }
      if (sendEvent)
      {
        fireBagSet("internalTransition", old, getInternalTransitions());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addInternalTransition(MTransition __arg)
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
        old = getInternalTransitions();
      }
      if (null != _internalTransition_ucopy)
      {
        _internalTransition = new ArrayList(_internalTransition);
        _internalTransition_ucopy = null;
      }
      __arg.internalRefByState(this);
      _internalTransition.add(__arg);
      logBagAdd(_internalTransition_addMethod, _internalTransition_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("internalTransition", old, getInternalTransitions(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeInternalTransition(MTransition __arg)
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
        old = getInternalTransitions();
      }
      if (null != _internalTransition_ucopy)
      {
        _internalTransition = new ArrayList(_internalTransition);
        _internalTransition_ucopy = null;
      }
      if (!_internalTransition.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByState(this);
      logBagRemove(_internalTransition_removeMethod, _internalTransition_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("internalTransition", old, getInternalTransitions(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByInternalTransition(MTransition __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getInternalTransitions();
    }
    if (null != _internalTransition_ucopy)
    {
      _internalTransition = new ArrayList(_internalTransition);
      _internalTransition_ucopy = null;
    }
    _internalTransition.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("internalTransition", old, getInternalTransitions(), __arg);
    }
  }
  public final void internalUnrefByInternalTransition(MTransition __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getInternalTransitions();
    }
    if (null != _internalTransition_ucopy)
    {
      _internalTransition = new ArrayList(_internalTransition);
      _internalTransition_ucopy = null;
    }
    _internalTransition.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("internalTransition", old, getInternalTransitions(), __arg);
    }
  }
  // opposite role: deferrableEvent this role: state
  private final static Method _deferrableEvent_setMethod = getMethod1(MStateImpl.class, "setDeferrableEvents", Collection.class);
  private final static Method _deferrableEvent_addMethod = getMethod1(MStateImpl.class, "addDeferrableEvent", MEvent.class);
  private final static Method _deferrableEvent_removeMethod = getMethod1(MStateImpl.class, "removeDeferrableEvent", MEvent.class);
  Collection _deferrableEvent = Collections.EMPTY_LIST;
  Collection _deferrableEvent_ucopy = Collections.EMPTY_LIST;
  public final Collection getDeferrableEvents()
  {
    checkExists();
    if (null == _deferrableEvent_ucopy)
    {
      _deferrableEvent_ucopy = ucopy(_deferrableEvent);
    }
    return _deferrableEvent_ucopy;
  }
  public final void setDeferrableEvents(Collection __arg)
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
        old = getDeferrableEvents();
      }
      _deferrableEvent_ucopy = null;
      Collection __added = bagdiff(__arg,_deferrableEvent);
      Collection __removed = bagdiff(_deferrableEvent, __arg);
      Iterator iter3 = __removed.iterator();
      while (iter3.hasNext())
      {
        MEvent o = (MEvent)iter3.next();
        o.internalUnrefByState(this);
      }
      Iterator iter4 = __added.iterator();
      while (iter4.hasNext())
      {
        MEvent o = (MEvent)iter4.next();
        o.internalRefByState(this);
      }
      _deferrableEvent = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_deferrableEvent_setMethod, old, getDeferrableEvents());
      }
      if (sendEvent)
      {
        fireBagSet("deferrableEvent", old, getDeferrableEvents());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addDeferrableEvent(MEvent __arg)
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
        old = getDeferrableEvents();
      }
      if (null != _deferrableEvent_ucopy)
      {
        _deferrableEvent = new ArrayList(_deferrableEvent);
        _deferrableEvent_ucopy = null;
      }
      __arg.internalRefByState(this);
      _deferrableEvent.add(__arg);
      logBagAdd(_deferrableEvent_addMethod, _deferrableEvent_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("deferrableEvent", old, getDeferrableEvents(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeDeferrableEvent(MEvent __arg)
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
        old = getDeferrableEvents();
      }
      if (null != _deferrableEvent_ucopy)
      {
        _deferrableEvent = new ArrayList(_deferrableEvent);
        _deferrableEvent_ucopy = null;
      }
      if (!_deferrableEvent.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByState(this);
      logBagRemove(_deferrableEvent_removeMethod, _deferrableEvent_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("deferrableEvent", old, getDeferrableEvents(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByDeferrableEvent(MEvent __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getDeferrableEvents();
    }
    if (null != _deferrableEvent_ucopy)
    {
      _deferrableEvent = new ArrayList(_deferrableEvent);
      _deferrableEvent_ucopy = null;
    }
    _deferrableEvent.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("deferrableEvent", old, getDeferrableEvents(), __arg);
    }
  }
  public final void internalUnrefByDeferrableEvent(MEvent __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getDeferrableEvents();
    }
    if (null != _deferrableEvent_ucopy)
    {
      _deferrableEvent = new ArrayList(_deferrableEvent);
      _deferrableEvent_ucopy = null;
    }
    _deferrableEvent.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("deferrableEvent", old, getDeferrableEvents(), __arg);
    }
  }
  // opposite role: stateMachine this role: top
  private final static Method _stateMachine_setMethod = getMethod1(MStateImpl.class, "setStateMachine", MStateMachine.class);
  MStateMachine _stateMachine;
  public final MStateMachine getStateMachine()
  {
    checkExists();
    return _stateMachine;
  }
  public final void setStateMachine(MStateMachine __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      MStateMachine __saved = _stateMachine;
      if (_stateMachine != __arg)
      {
        if (__saved != null)
        {
          __saved.internalUnrefByTop(this);
        }
        if (__arg != null)
        {
          __arg.internalRefByTop(this);
        }
        logRefSet(_stateMachine_setMethod, __saved, __arg);
        fireRefSet("stateMachine", __saved, __arg);
        _stateMachine = __arg;
        setModelElementContainer(_stateMachine, "stateMachine");
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByStateMachine(MStateMachine __arg)
  {
    MStateMachine __saved = _stateMachine;
    if (__saved != null)
    {
      __saved.setTop(null);
    }
    fireRefSet("stateMachine", __saved, __arg);
    _stateMachine = __arg;
    setModelElementContainer(_stateMachine, "stateMachine");
  }
  public final void internalUnrefByStateMachine(MStateMachine __arg)
  {
    fireRefSet("stateMachine", _stateMachine, null);
    _stateMachine = null;
    setModelElementContainer(null, null);
  }
  // opposite role: classifierInState this role: inState
  private final static Method _classifierInState_setMethod = getMethod1(MStateImpl.class, "setClassifiersInState", Collection.class);
  private final static Method _classifierInState_addMethod = getMethod1(MStateImpl.class, "addClassifierInState", MClassifierInState.class);
  private final static Method _classifierInState_removeMethod = getMethod1(MStateImpl.class, "removeClassifierInState", MClassifierInState.class);
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
      Iterator iter5 = __removed.iterator();
      while (iter5.hasNext())
      {
        MClassifierInState o = (MClassifierInState)iter5.next();
        o.internalUnrefByInState(this);
      }
      Iterator iter6 = __added.iterator();
      while (iter6.hasNext())
      {
        MClassifierInState o = (MClassifierInState)iter6.next();
        o.internalRefByInState(this);
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
      __arg.internalRefByInState(this);
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
      __arg.internalUnrefByInState(this);
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
  // opposite role: exit this role: state2
  private final static Method _exit_setMethod = getMethod1(MStateImpl.class, "setExit", MAction.class);
  MAction _exit;
  public final MAction getExit()
  {
    checkExists();
    return _exit;
  }
  public final void setExit(MAction __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      MAction __saved = _exit;
      if (_exit != __arg)
      {
        if (__saved != null)
        {
          __saved.internalUnrefByState2(this);
        }
        if (__arg != null)
        {
          __arg.internalRefByState2(this);
        }
        logRefSet(_exit_setMethod, __saved, __arg);
        fireRefSet("exit", __saved, __arg);
        _exit = __arg;
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByExit(MAction __arg)
  {
    MAction __saved = _exit;
    if (__saved != null)
    {
      __saved.setState2(null);
    }
    fireRefSet("exit", __saved, __arg);
    _exit = __arg;
  }
  public final void internalUnrefByExit(MAction __arg)
  {
    fireRefSet("exit", _exit, null);
    _exit = null;
  }
  // opposite role: entry this role: state1
  private final static Method _entry_setMethod = getMethod1(MStateImpl.class, "setEntry", MAction.class);
  MAction _entry;
  public final MAction getEntry()
  {
    checkExists();
    return _entry;
  }
  public final void setEntry(MAction __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      MAction __saved = _entry;
      if (_entry != __arg)
      {
        if (__saved != null)
        {
          __saved.internalUnrefByState1(this);
        }
        if (__arg != null)
        {
          __arg.internalRefByState1(this);
        }
        logRefSet(_entry_setMethod, __saved, __arg);
        fireRefSet("entry", __saved, __arg);
        _entry = __arg;
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByEntry(MAction __arg)
  {
    MAction __saved = _entry;
    if (__saved != null)
    {
      __saved.setState1(null);
    }
    fireRefSet("entry", __saved, __arg);
    _entry = __arg;
  }
  public final void internalUnrefByEntry(MAction __arg)
  {
    fireRefSet("entry", _entry, null);
    _entry = null;
  }
  protected void cleanup(Collection scheduledForRemove)
  {
    // opposite role: doActivity this role: state3
    if (_doActivity != null )
    {
      scheduledForRemove.add(_doActivity);
      setDoActivity(null);
    }
    // opposite role: internalTransition this role: state
    if (_internalTransition.size() != 0)
    {
      scheduledForRemove.addAll(_internalTransition);
      setInternalTransitions(Collections.EMPTY_LIST);
    }
    // opposite role: deferrableEvent this role: state
    if (_deferrableEvent.size() != 0)
    {
      setDeferrableEvents(Collections.EMPTY_LIST);
    }
    // opposite role: stateMachine this role: top
    if (_stateMachine != null )
    {
      setStateMachine(null);
    }
    // opposite role: classifierInState this role: inState
    if (_classifierInState.size() != 0)
    {
      setClassifiersInState(Collections.EMPTY_LIST);
    }
    // opposite role: exit this role: state2
    if (_exit != null )
    {
      scheduledForRemove.add(_exit);
      setExit(null);
    }
    // opposite role: entry this role: state1
    if (_entry != null )
    {
      scheduledForRemove.add(_entry);
      setEntry(null);
    }
    super.cleanup(scheduledForRemove);
  }

  public String getUMLClassName()
  {
    return "State";
  }

  // Reflective API

  public Object reflectiveGetValue(String feature)
  {
    if ("doActivity".equals(feature))
    {
      return getDoActivity();
    }
    if ("internalTransition".equals(feature))
    {
      return getInternalTransitions();
    }
    if ("deferrableEvent".equals(feature))
    {
      return getDeferrableEvents();
    }
    if ("stateMachine".equals(feature))
    {
      return getStateMachine();
    }
    if ("classifierInState".equals(feature))
    {
      return getClassifiersInState();
    }
    if ("exit".equals(feature))
    {
      return getExit();
    }
    if ("entry".equals(feature))
    {
      return getEntry();
    }

    return super.reflectiveGetValue(feature);
  }

  public void reflectiveSetValue(String feature, Object obj)
  {
    if ("doActivity".equals(feature))
    {
      setDoActivity((MAction)obj);
      return;
    }
    if ("internalTransition".equals(feature))
    {
      setInternalTransitions((Collection)obj);
      return;
    }
    if ("deferrableEvent".equals(feature))
    {
      setDeferrableEvents((Collection)obj);
      return;
    }
    if ("stateMachine".equals(feature))
    {
      setStateMachine((MStateMachine)obj);
      return;
    }
    if ("classifierInState".equals(feature))
    {
      setClassifiersInState((Collection)obj);
      return;
    }
    if ("exit".equals(feature))
    {
      setExit((MAction)obj);
      return;
    }
    if ("entry".equals(feature))
    {
      setEntry((MAction)obj);
      return;
    }

    super.reflectiveSetValue(feature, obj);
  }

  public void reflectiveAddValue(String feature, Object obj)
  {
    if ("internalTransition".equals(feature))
    {
      addInternalTransition((MTransition)obj);
      return;
    }
    if ("deferrableEvent".equals(feature))
    {
      addDeferrableEvent((MEvent)obj);
      return;
    }
    if ("classifierInState".equals(feature))
    {
      addClassifierInState((MClassifierInState)obj);
      return;
    }

    super.reflectiveAddValue(feature, obj);
  }

  public void reflectiveRemoveValue(String feature, Object obj)
  {
    if ("internalTransition".equals(feature))
    {
      removeInternalTransition((MTransition)obj);
      return;
    }
    if ("deferrableEvent".equals(feature))
    {
      removeDeferrableEvent((MEvent)obj);
      return;
    }
    if ("classifierInState".equals(feature))
    {
      removeClassifierInState((MClassifierInState)obj);
      return;
    }

    super.reflectiveRemoveValue(feature, obj);
  }

  public Object reflectiveGetValue(String feature, int pos)
  {

    return super.reflectiveGetValue(feature, pos);
  }

  public void reflectiveSetValue(String feature, int pos, Object obj)
  {

    super.reflectiveSetValue(feature, pos, obj);
  }

  public void reflectiveAddValue(String feature, int pos, Object obj)
  {

    super.reflectiveAddValue(feature, pos, obj);
  }

  public void reflectiveRemoveValue(String feature, int pos)
  {

    super.reflectiveRemoveValue(feature, pos);
  }
  public Collection getModelElementContents()
  {
    Collection ret = super.getModelElementContents();
    ret.add(getDoActivity());
    ret.addAll(getInternalTransitions());
    ret.add(getExit());
    ret.add(getEntry());
    return ret;
  }
}
