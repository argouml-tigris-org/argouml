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

public abstract class MEventImpl extends MModelElementImpl implements MEvent
{
  // ------------ code for class Event -----------------
  // generating attributes
  // generating associations
  // opposite role: transition this role: trigger
  private final static Method _transition_setMethod = getMethod1(MEventImpl.class, "setTransitions", Collection.class);
  private final static Method _transition_addMethod = getMethod1(MEventImpl.class, "addTransition", MTransition.class);
  private final static Method _transition_removeMethod = getMethod1(MEventImpl.class, "removeTransition", MTransition.class);
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
      Iterator iter1 = __removed.iterator();
      while (iter1.hasNext())
      {
        MTransition o = (MTransition)iter1.next();
        o.internalUnrefByTrigger(this);
      }
      Iterator iter2 = __added.iterator();
      while (iter2.hasNext())
      {
        MTransition o = (MTransition)iter2.next();
        o.internalRefByTrigger(this);
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
      __arg.internalRefByTrigger(this);
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
      __arg.internalUnrefByTrigger(this);
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
  // opposite role: state this role: deferrableEvent
  private final static Method _state_setMethod = getMethod1(MEventImpl.class, "setStates", Collection.class);
  private final static Method _state_addMethod = getMethod1(MEventImpl.class, "addState", MState.class);
  private final static Method _state_removeMethod = getMethod1(MEventImpl.class, "removeState", MState.class);
  Collection _state = Collections.EMPTY_LIST;
  Collection _state_ucopy = Collections.EMPTY_LIST;
  public final Collection getStates()
  {
    checkExists();
    if (null == _state_ucopy)
    {
      _state_ucopy = ucopy(_state);
    }
    return _state_ucopy;
  }
  public final void setStates(Collection __arg)
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
        old = getStates();
      }
      _state_ucopy = null;
      Collection __added = bagdiff(__arg,_state);
      Collection __removed = bagdiff(_state, __arg);
      Iterator iter3 = __removed.iterator();
      while (iter3.hasNext())
      {
        MState o = (MState)iter3.next();
        o.internalUnrefByDeferrableEvent(this);
      }
      Iterator iter4 = __added.iterator();
      while (iter4.hasNext())
      {
        MState o = (MState)iter4.next();
        o.internalRefByDeferrableEvent(this);
      }
      _state = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_state_setMethod, old, getStates());
      }
      if (sendEvent)
      {
        fireBagSet("state", old, getStates());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addState(MState __arg)
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
        old = getStates();
      }
      if (null != _state_ucopy)
      {
        _state = new ArrayList(_state);
        _state_ucopy = null;
      }
      __arg.internalRefByDeferrableEvent(this);
      _state.add(__arg);
      logBagAdd(_state_addMethod, _state_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("state", old, getStates(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeState(MState __arg)
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
        old = getStates();
      }
      if (null != _state_ucopy)
      {
        _state = new ArrayList(_state);
        _state_ucopy = null;
      }
      if (!_state.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByDeferrableEvent(this);
      logBagRemove(_state_removeMethod, _state_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("state", old, getStates(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByState(MState __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getStates();
    }
    if (null != _state_ucopy)
    {
      _state = new ArrayList(_state);
      _state_ucopy = null;
    }
    _state.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("state", old, getStates(), __arg);
    }
  }
  public final void internalUnrefByState(MState __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getStates();
    }
    if (null != _state_ucopy)
    {
      _state = new ArrayList(_state);
      _state_ucopy = null;
    }
    _state.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("state", old, getStates(), __arg);
    }
  }
  // opposite role: parameter this role: event
  private final static Method _parameter_setMethod = getMethod1(MEventImpl.class, "setParameters", List.class);
  private final static Method _parameter_removeMethod = getMethod1(MEventImpl.class, "removeParameter", int.class);
  private final static Method _parameter_addMethod = getMethod2(MEventImpl.class, "addParameter", int.class, MParameter.class);
  private final static Method _parameter_listSetMethod = getMethod2(MEventImpl.class, "setParameter", int.class, MParameter.class);
  List _parameter = Collections.EMPTY_LIST;
  List _parameter_ucopy = Collections.EMPTY_LIST;
  public final List getParameters()
  {
    checkExists();
    if (null == _parameter_ucopy)
    {
      _parameter_ucopy = ucopy(_parameter);
    }
    return _parameter_ucopy;
  }
  public final void setParameters(List __arg)
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
        old = getParameters();
      }
      _parameter_ucopy = null;
      Collection __added = bagdiff(__arg,_parameter);
      Collection __removed = bagdiff(_parameter, __arg);
      Iterator iter5 = __removed.iterator();
      while (iter5.hasNext())
      {
        MParameter o = (MParameter)iter5.next();
        o.internalUnrefByEvent(this);
      }
      Iterator iter6 = __added.iterator();
      while (iter6.hasNext())
      {
        MParameter o = (MParameter)iter6.next();
        o.internalRefByEvent(this);
      }
      _parameter = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_parameter_setMethod, old, getParameters());
      }
      if (sendEvent)
      {
        fireListSet("parameter", old, getParameters());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addParameter(MParameter __arg)
  {
    addParameter(_parameter.size(), __arg);
  }
  public final void removeParameter(MParameter __arg)
  {
    if (__arg == null)
    {
      throw new NullPointerException();
    }
    int __pos = _parameter.indexOf(__arg);
    removeParameter(__pos);
  }
  public final void addParameter(int __pos, MParameter __arg)
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
        old = getParameters();
      }
      if (null != _parameter_ucopy)
      {
        _parameter = new ArrayList(_parameter);
        _parameter_ucopy = null;
      }
      _parameter.add(__pos, __arg);
      __arg.internalRefByEvent(this);
      logListAdd(_parameter_addMethod, _parameter_removeMethod, __arg, __pos);
      if (sendEvent)
      {
        fireListAdd("parameter", old, getParameters(), __arg, __pos);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeParameter(int __pos)
  {
    operationStarted();
    try
    {
      checkExists();
      final boolean sendEvent = needEvent();
      List old = null;
      if (sendEvent)
      {
        old = getParameters();
      }
      if (null != _parameter_ucopy)
      {
        _parameter = new ArrayList(_parameter);
        _parameter_ucopy = null;
      }
      MParameter __arg = (MParameter)_parameter.remove(__pos);
      __arg.internalUnrefByEvent(this);
      logListRemove(_parameter_removeMethod, _parameter_addMethod, __arg, __pos);
      if (sendEvent)
      {
        fireListRemove("parameter", old, getParameters(), __arg, __pos);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void setParameter(int __pos, MParameter __arg)
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
        old = getParameters();
      }
      if (null != _parameter_ucopy)
      {
        _parameter = new ArrayList(_parameter);
        _parameter_ucopy = null;
      }
      MParameter __old = (MParameter)_parameter.get(__pos);
      __old.internalUnrefByEvent(this);
      __arg.internalRefByEvent(this);
      _parameter.set(__pos,__arg);
      logListSet(_parameter_listSetMethod, __old, __arg, __pos);
      if (sendEvent)
      {
        fireListItemSet("parameter", old, getParameters(), __old, __arg, __pos);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final MParameter getParameter(int __pos)
  {
    checkExists();
    return (MParameter)_parameter.get(__pos);
  }
  protected void cleanup(Collection scheduledForRemove)
  {
    // opposite role: transition this role: trigger
    if (_transition.size() != 0)
    {
      setTransitions(Collections.EMPTY_LIST);
    }
    // opposite role: state this role: deferrableEvent
    if (_state.size() != 0)
    {
      setStates(Collections.EMPTY_LIST);
    }
    // opposite role: parameter this role: event
    if (_parameter.size() != 0)
    {
      scheduledForRemove.addAll(_parameter);
      setParameters(Collections.EMPTY_LIST);
    }
    super.cleanup(scheduledForRemove);
  }

  public String getUMLClassName()
  {
    return "Event";
  }

  // Reflective API

  public Object reflectiveGetValue(String feature)
  {
    if ("transition".equals(feature))
    {
      return getTransitions();
    }
    if ("state".equals(feature))
    {
      return getStates();
    }
    if ("parameter".equals(feature))
    {
      return getParameters();
    }

    return super.reflectiveGetValue(feature);
  }

  public void reflectiveSetValue(String feature, Object obj)
  {
    if ("transition".equals(feature))
    {
      setTransitions((Collection)obj);
      return;
    }
    if ("state".equals(feature))
    {
      setStates((Collection)obj);
      return;
    }
    if ("parameter".equals(feature))
    {
      setParameters((List)obj);
      return;
    }

    super.reflectiveSetValue(feature, obj);
  }

  public void reflectiveAddValue(String feature, Object obj)
  {
    if ("transition".equals(feature))
    {
      addTransition((MTransition)obj);
      return;
    }
    if ("state".equals(feature))
    {
      addState((MState)obj);
      return;
    }
    if ("parameter".equals(feature))
    {
      addParameter((MParameter)obj);
      return;
    }

    super.reflectiveAddValue(feature, obj);
  }

  public void reflectiveRemoveValue(String feature, Object obj)
  {
    if ("transition".equals(feature))
    {
      removeTransition((MTransition)obj);
      return;
    }
    if ("state".equals(feature))
    {
      removeState((MState)obj);
      return;
    }
    if ("parameter".equals(feature))
    {
      removeParameter((MParameter)obj);
      return;
    }

    super.reflectiveRemoveValue(feature, obj);
  }

  public Object reflectiveGetValue(String feature, int pos)
  {
    if ("parameter".equals(feature))
    {
      return getParameter(pos);
    }

    return super.reflectiveGetValue(feature, pos);
  }

  public void reflectiveSetValue(String feature, int pos, Object obj)
  {
    if ("parameter".equals(feature))
    {
      setParameter(pos, (MParameter)obj);
      return;
    }

    super.reflectiveSetValue(feature, pos, obj);
  }

  public void reflectiveAddValue(String feature, int pos, Object obj)
  {
    if ("parameter".equals(feature))
    {
      addParameter(pos, (MParameter)obj);
      return;
    }

    super.reflectiveAddValue(feature, pos, obj);
  }

  public void reflectiveRemoveValue(String feature, int pos)
  {
    if ("parameter".equals(feature))
    {
      removeParameter(pos);
      return;
    }

    super.reflectiveRemoveValue(feature, pos);
  }
  public Collection getModelElementContents()
  {
    Collection ret = super.getModelElementContents();
    ret.addAll(getParameters());
    return ret;
  }
}
