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

public class MTransitionImpl extends MModelElementImpl implements MTransition
{
  // ------------ code for class Transition -----------------
  // generating attributes
  // generating associations
  // opposite role: target this role: incoming
  private final static Method _target_setMethod = getMethod1(MTransitionImpl.class, "setTarget", MStateVertex.class);
  MStateVertex _target;
  public final MStateVertex getTarget()
  {
    checkExists();
    return _target;
  }
  public final void setTarget(MStateVertex __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      MStateVertex __saved = _target;
      if (_target != __arg)
      {
        if (__saved != null)
        {
          __saved.internalUnrefByIncoming(this);
        }
        if (__arg != null)
        {
          __arg.internalRefByIncoming(this);
        }
        logRefSet(_target_setMethod, __saved, __arg);
        fireRefSet("target", __saved, __arg);
        _target = __arg;
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByTarget(MStateVertex __arg)
  {
    MStateVertex __saved = _target;
    if (_target != null)
    {
      _target.removeIncoming(this);
    }
    fireRefSet("target", __saved, __arg);
    _target = __arg;
  }
  public final void internalUnrefByTarget(MStateVertex __arg)
  {
    fireRefSet("target", _target, null);
    _target = null;
  }
  // opposite role: source this role: outgoing
  private final static Method _source_setMethod = getMethod1(MTransitionImpl.class, "setSource", MStateVertex.class);
  MStateVertex _source;
  public final MStateVertex getSource()
  {
    checkExists();
    return _source;
  }
  public final void setSource(MStateVertex __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      MStateVertex __saved = _source;
      if (_source != __arg)
      {
        if (__saved != null)
        {
          __saved.internalUnrefByOutgoing(this);
        }
        if (__arg != null)
        {
          __arg.internalRefByOutgoing(this);
        }
        logRefSet(_source_setMethod, __saved, __arg);
        fireRefSet("source", __saved, __arg);
        _source = __arg;
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefBySource(MStateVertex __arg)
  {
    MStateVertex __saved = _source;
    if (_source != null)
    {
      _source.removeOutgoing(this);
    }
    fireRefSet("source", __saved, __arg);
    _source = __arg;
  }
  public final void internalUnrefBySource(MStateVertex __arg)
  {
    fireRefSet("source", _source, null);
    _source = null;
  }
  // opposite role: stateMachine this role: transition
  private final static Method _stateMachine_setMethod = getMethod1(MTransitionImpl.class, "setStateMachine", MStateMachine.class);
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
          __saved.internalUnrefByTransition(this);
        }
        if (__arg != null)
        {
          __arg.internalRefByTransition(this);
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
    if (_stateMachine != null)
    {
      _stateMachine.removeTransition(this);
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
  // opposite role: trigger this role: transition
  private final static Method _trigger_setMethod = getMethod1(MTransitionImpl.class, "setTrigger", MEvent.class);
  MEvent _trigger;
  public final MEvent getTrigger()
  {
    checkExists();
    return _trigger;
  }
  public final void setTrigger(MEvent __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      MEvent __saved = _trigger;
      if (_trigger != __arg)
      {
        if (__saved != null)
        {
          __saved.internalUnrefByTransition(this);
        }
        if (__arg != null)
        {
          __arg.internalRefByTransition(this);
        }
        logRefSet(_trigger_setMethod, __saved, __arg);
        fireRefSet("trigger", __saved, __arg);
        _trigger = __arg;
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByTrigger(MEvent __arg)
  {
    MEvent __saved = _trigger;
    if (_trigger != null)
    {
      _trigger.removeTransition(this);
    }
    fireRefSet("trigger", __saved, __arg);
    _trigger = __arg;
  }
  public final void internalUnrefByTrigger(MEvent __arg)
  {
    fireRefSet("trigger", _trigger, null);
    _trigger = null;
  }
  // opposite role: state this role: internalTransition
  private final static Method _state_setMethod = getMethod1(MTransitionImpl.class, "setState", MState.class);
  MState _state;
  public final MState getState()
  {
    checkExists();
    return _state;
  }
  public final void setState(MState __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      MState __saved = _state;
      if (_state != __arg)
      {
        if (__saved != null)
        {
          __saved.internalUnrefByInternalTransition(this);
        }
        if (__arg != null)
        {
          __arg.internalRefByInternalTransition(this);
        }
        logRefSet(_state_setMethod, __saved, __arg);
        fireRefSet("state", __saved, __arg);
        _state = __arg;
        setModelElementContainer(_state, "state");
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByState(MState __arg)
  {
    MState __saved = _state;
    if (_state != null)
    {
      _state.removeInternalTransition(this);
    }
    fireRefSet("state", __saved, __arg);
    _state = __arg;
    setModelElementContainer(_state, "state");
  }
  public final void internalUnrefByState(MState __arg)
  {
    fireRefSet("state", _state, null);
    _state = null;
    setModelElementContainer(null, null);
  }
  // opposite role: effect this role: transition
  private final static Method _effect_setMethod = getMethod1(MTransitionImpl.class, "setEffect", MAction.class);
  MAction _effect;
  public final MAction getEffect()
  {
    checkExists();
    return _effect;
  }
  public final void setEffect(MAction __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      MAction __saved = _effect;
      if (_effect != __arg)
      {
        if (__saved != null)
        {
          __saved.internalUnrefByTransition(this);
        }
        if (__arg != null)
        {
          __arg.internalRefByTransition(this);
        }
        logRefSet(_effect_setMethod, __saved, __arg);
        fireRefSet("effect", __saved, __arg);
        _effect = __arg;
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByEffect(MAction __arg)
  {
    MAction __saved = _effect;
    if (__saved != null)
    {
      __saved.setTransition(null);
    }
    fireRefSet("effect", __saved, __arg);
    _effect = __arg;
  }
  public final void internalUnrefByEffect(MAction __arg)
  {
    fireRefSet("effect", _effect, null);
    _effect = null;
  }
  // opposite role: guard this role: transition
  private final static Method _guard_setMethod = getMethod1(MTransitionImpl.class, "setGuard", MGuard.class);
  MGuard _guard;
  public final MGuard getGuard()
  {
    checkExists();
    return _guard;
  }
  public final void setGuard(MGuard __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      MGuard __saved = _guard;
      if (_guard != __arg)
      {
        if (__saved != null)
        {
          __saved.internalUnrefByTransition(this);
        }
        if (__arg != null)
        {
          __arg.internalRefByTransition(this);
        }
        logRefSet(_guard_setMethod, __saved, __arg);
        fireRefSet("guard", __saved, __arg);
        _guard = __arg;
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByGuard(MGuard __arg)
  {
    MGuard __saved = _guard;
    if (__saved != null)
    {
      __saved.setTransition(null);
    }
    fireRefSet("guard", __saved, __arg);
    _guard = __arg;
  }
  public final void internalUnrefByGuard(MGuard __arg)
  {
    fireRefSet("guard", _guard, null);
    _guard = null;
  }
  protected void cleanup(Collection scheduledForRemove)
  {
    // opposite role: target this role: incoming
    if (_target != null )
    {
      setTarget(null);
    }
    // opposite role: source this role: outgoing
    if (_source != null )
    {
      setSource(null);
    }
    // opposite role: stateMachine this role: transition
    if (_stateMachine != null )
    {
      setStateMachine(null);
    }
    // opposite role: trigger this role: transition
    if (_trigger != null )
    {
      setTrigger(null);
    }
    // opposite role: state this role: internalTransition
    if (_state != null )
    {
      setState(null);
    }
    // opposite role: effect this role: transition
    if (_effect != null )
    {
      scheduledForRemove.add(_effect);
      setEffect(null);
    }
    // opposite role: guard this role: transition
    if (_guard != null )
    {
      scheduledForRemove.add(_guard);
      setGuard(null);
    }
    super.cleanup(scheduledForRemove);
  }

  public String getUMLClassName()
  {
    return "Transition";
  }

  // Reflective API

  public Object reflectiveGetValue(String feature)
  {
    if ("target".equals(feature))
    {
      return getTarget();
    }
    if ("source".equals(feature))
    {
      return getSource();
    }
    if ("stateMachine".equals(feature))
    {
      return getStateMachine();
    }
    if ("trigger".equals(feature))
    {
      return getTrigger();
    }
    if ("state".equals(feature))
    {
      return getState();
    }
    if ("effect".equals(feature))
    {
      return getEffect();
    }
    if ("guard".equals(feature))
    {
      return getGuard();
    }

    return super.reflectiveGetValue(feature);
  }

  public void reflectiveSetValue(String feature, Object obj)
  {
    if ("target".equals(feature))
    {
      setTarget((MStateVertex)obj);
      return;
    }
    if ("source".equals(feature))
    {
      setSource((MStateVertex)obj);
      return;
    }
    if ("stateMachine".equals(feature))
    {
      setStateMachine((MStateMachine)obj);
      return;
    }
    if ("trigger".equals(feature))
    {
      setTrigger((MEvent)obj);
      return;
    }
    if ("state".equals(feature))
    {
      setState((MState)obj);
      return;
    }
    if ("effect".equals(feature))
    {
      setEffect((MAction)obj);
      return;
    }
    if ("guard".equals(feature))
    {
      setGuard((MGuard)obj);
      return;
    }

    super.reflectiveSetValue(feature, obj);
  }

  public void reflectiveAddValue(String feature, Object obj)
  {

    super.reflectiveAddValue(feature, obj);
  }

  public void reflectiveRemoveValue(String feature, Object obj)
  {

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
    ret.add(getEffect());
    ret.add(getGuard());
    return ret;
  }
}
