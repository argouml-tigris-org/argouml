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

public class MStateMachineImpl extends MModelElementImpl implements MStateMachine
{
  // ------------ code for class StateMachine -----------------
  // generating attributes
  // generating associations
  // opposite role: submachineState this role: submachine
  private final static Method _submachineState_setMethod = getMethod1(MStateMachineImpl.class, "setSubmachineStates", Collection.class);
  private final static Method _submachineState_addMethod = getMethod1(MStateMachineImpl.class, "addSubmachineState", MSubmachineState.class);
  private final static Method _submachineState_removeMethod = getMethod1(MStateMachineImpl.class, "removeSubmachineState", MSubmachineState.class);
  Collection _submachineState = Collections.EMPTY_LIST;
  Collection _submachineState_ucopy = Collections.EMPTY_LIST;
  public final Collection getSubmachineStates()
  {
    checkExists();
    if (null == _submachineState_ucopy)
    {
      _submachineState_ucopy = ucopy(_submachineState);
    }
    return _submachineState_ucopy;
  }
  public final void setSubmachineStates(Collection __arg)
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
        old = getSubmachineStates();
      }
      _submachineState_ucopy = null;
      Collection __added = bagdiff(__arg,_submachineState);
      Collection __removed = bagdiff(_submachineState, __arg);
      Iterator iter1 = __removed.iterator();
      while (iter1.hasNext())
      {
        MSubmachineState o = (MSubmachineState)iter1.next();
        o.internalUnrefBySubmachine(this);
      }
      Iterator iter2 = __added.iterator();
      while (iter2.hasNext())
      {
        MSubmachineState o = (MSubmachineState)iter2.next();
        o.internalRefBySubmachine(this);
      }
      _submachineState = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_submachineState_setMethod, old, getSubmachineStates());
      }
      if (sendEvent)
      {
        fireBagSet("submachineState", old, getSubmachineStates());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addSubmachineState(MSubmachineState __arg)
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
        old = getSubmachineStates();
      }
      if (null != _submachineState_ucopy)
      {
        _submachineState = new ArrayList(_submachineState);
        _submachineState_ucopy = null;
      }
      __arg.internalRefBySubmachine(this);
      _submachineState.add(__arg);
      logBagAdd(_submachineState_addMethod, _submachineState_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("submachineState", old, getSubmachineStates(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeSubmachineState(MSubmachineState __arg)
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
        old = getSubmachineStates();
      }
      if (null != _submachineState_ucopy)
      {
        _submachineState = new ArrayList(_submachineState);
        _submachineState_ucopy = null;
      }
      if (!_submachineState.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefBySubmachine(this);
      logBagRemove(_submachineState_removeMethod, _submachineState_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("submachineState", old, getSubmachineStates(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefBySubmachineState(MSubmachineState __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getSubmachineStates();
    }
    if (null != _submachineState_ucopy)
    {
      _submachineState = new ArrayList(_submachineState);
      _submachineState_ucopy = null;
    }
    _submachineState.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("submachineState", old, getSubmachineStates(), __arg);
    }
  }
  public final void internalUnrefBySubmachineState(MSubmachineState __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getSubmachineStates();
    }
    if (null != _submachineState_ucopy)
    {
      _submachineState = new ArrayList(_submachineState);
      _submachineState_ucopy = null;
    }
    _submachineState.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("submachineState", old, getSubmachineStates(), __arg);
    }
  }
  // opposite role: transition this role: stateMachine
  private final static Method _transition_setMethod = getMethod1(MStateMachineImpl.class, "setTransitions", Collection.class);
  private final static Method _transition_addMethod = getMethod1(MStateMachineImpl.class, "addTransition", MTransition.class);
  private final static Method _transition_removeMethod = getMethod1(MStateMachineImpl.class, "removeTransition", MTransition.class);
  Collection _transition = Collections.EMPTY_LIST;
  Collection _transition_ucopy = Collections.EMPTY_LIST;
  public final Collection getTransitions()
  {
    checkExists();
    if (null == _transition_ucopy)
    {
      _transition_ucopy = ucopy(_transition);
    }
    return _transition_ucopy;
  }
  public final void setTransitions(Collection __arg)
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
        old = getTransitions();
      }
      _transition_ucopy = null;
      Collection __added = bagdiff(__arg,_transition);
      Collection __removed = bagdiff(_transition, __arg);
      Iterator iter3 = __removed.iterator();
      while (iter3.hasNext())
      {
        MTransition o = (MTransition)iter3.next();
        o.internalUnrefByStateMachine(this);
      }
      Iterator iter4 = __added.iterator();
      while (iter4.hasNext())
      {
        MTransition o = (MTransition)iter4.next();
        o.internalRefByStateMachine(this);
      }
      _transition = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_transition_setMethod, old, getTransitions());
      }
      if (sendEvent)
      {
        fireBagSet("transition", old, getTransitions());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addTransition(MTransition __arg)
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
        old = getTransitions();
      }
      if (null != _transition_ucopy)
      {
        _transition = new ArrayList(_transition);
        _transition_ucopy = null;
      }
      __arg.internalRefByStateMachine(this);
      _transition.add(__arg);
      logBagAdd(_transition_addMethod, _transition_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("transition", old, getTransitions(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeTransition(MTransition __arg)
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
        old = getTransitions();
      }
      if (null != _transition_ucopy)
      {
        _transition = new ArrayList(_transition);
        _transition_ucopy = null;
      }
      if (!_transition.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByStateMachine(this);
      logBagRemove(_transition_removeMethod, _transition_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("transition", old, getTransitions(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByTransition(MTransition __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getTransitions();
    }
    if (null != _transition_ucopy)
    {
      _transition = new ArrayList(_transition);
      _transition_ucopy = null;
    }
    _transition.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("transition", old, getTransitions(), __arg);
    }
  }
  public final void internalUnrefByTransition(MTransition __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getTransitions();
    }
    if (null != _transition_ucopy)
    {
      _transition = new ArrayList(_transition);
      _transition_ucopy = null;
    }
    _transition.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("transition", old, getTransitions(), __arg);
    }
  }
  // opposite role: top this role: stateMachine
  private final static Method _top_setMethod = getMethod1(MStateMachineImpl.class, "setTop", MState.class);
  MState _top;
  public final MState getTop()
  {
    checkExists();
    return _top;
  }
  public final void setTop(MState __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      MState __saved = _top;
      if (_top != __arg)
      {
        if (__saved != null)
        {
          __saved.internalUnrefByStateMachine(this);
        }
        if (__arg != null)
        {
          __arg.internalRefByStateMachine(this);
        }
        logRefSet(_top_setMethod, __saved, __arg);
        fireRefSet("top", __saved, __arg);
        _top = __arg;
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByTop(MState __arg)
  {
    MState __saved = _top;
    if (__saved != null)
    {
      __saved.setStateMachine(null);
    }
    fireRefSet("top", __saved, __arg);
    _top = __arg;
  }
  public final void internalUnrefByTop(MState __arg)
  {
    fireRefSet("top", _top, null);
    _top = null;
  }
  // opposite role: context this role: behavior
  private final static Method _context_setMethod = getMethod1(MStateMachineImpl.class, "setContext", MModelElement.class);
  MModelElement _context;
  public final MModelElement getContext()
  {
    checkExists();
    return _context;
  }
  public final void setContext(MModelElement __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      MModelElement __saved = _context;
      if (_context != __arg)
      {
        if (__saved != null)
        {
          __saved.internalUnrefByBehavior(this);
        }
        if (__arg != null)
        {
          __arg.internalRefByBehavior(this);
        }
        logRefSet(_context_setMethod, __saved, __arg);
        fireRefSet("context", __saved, __arg);
        _context = __arg;
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByContext(MModelElement __arg)
  {
    MModelElement __saved = _context;
    if (_context != null)
    {
      _context.removeBehavior(this);
    }
    fireRefSet("context", __saved, __arg);
    _context = __arg;
  }
  public final void internalUnrefByContext(MModelElement __arg)
  {
    fireRefSet("context", _context, null);
    _context = null;
  }
  protected void cleanup(Collection scheduledForRemove)
  {
    // opposite role: submachineState this role: submachine
    if (_submachineState.size() != 0)
    {
      setSubmachineStates(Collections.EMPTY_LIST);
    }
    // opposite role: transition this role: stateMachine
    if (_transition.size() != 0)
    {
      scheduledForRemove.addAll(_transition);
      setTransitions(Collections.EMPTY_LIST);
    }
    // opposite role: top this role: stateMachine
    if (_top != null )
    {
      scheduledForRemove.add(_top);
      setTop(null);
    }
    // opposite role: context this role: behavior
    if (_context != null )
    {
      setContext(null);
    }
    super.cleanup(scheduledForRemove);
  }

  public String getUMLClassName()
  {
    return "StateMachine";
  }

  // Reflective API

  public Object reflectiveGetValue(String feature)
  {
    if ("submachineState".equals(feature))
    {
      return getSubmachineStates();
    }
    if ("transition".equals(feature))
    {
      return getTransitions();
    }
    if ("top".equals(feature))
    {
      return getTop();
    }
    if ("context".equals(feature))
    {
      return getContext();
    }

    return super.reflectiveGetValue(feature);
  }

  public void reflectiveSetValue(String feature, Object obj)
  {
    if ("submachineState".equals(feature))
    {
      setSubmachineStates((Collection)obj);
      return;
    }
    if ("transition".equals(feature))
    {
      setTransitions((Collection)obj);
      return;
    }
    if ("top".equals(feature))
    {
      setTop((MState)obj);
      return;
    }
    if ("context".equals(feature))
    {
      setContext((MModelElement)obj);
      return;
    }

    super.reflectiveSetValue(feature, obj);
  }

  public void reflectiveAddValue(String feature, Object obj)
  {
    if ("submachineState".equals(feature))
    {
      addSubmachineState((MSubmachineState)obj);
      return;
    }
    if ("transition".equals(feature))
    {
      addTransition((MTransition)obj);
      return;
    }

    super.reflectiveAddValue(feature, obj);
  }

  public void reflectiveRemoveValue(String feature, Object obj)
  {
    if ("submachineState".equals(feature))
    {
      removeSubmachineState((MSubmachineState)obj);
      return;
    }
    if ("transition".equals(feature))
    {
      removeTransition((MTransition)obj);
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
    ret.addAll(getTransitions());
    ret.add(getTop());
    return ret;
  }
}
