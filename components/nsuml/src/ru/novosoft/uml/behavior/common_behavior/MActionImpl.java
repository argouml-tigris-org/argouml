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

package ru.novosoft.uml.behavior.common_behavior;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import ru.novosoft.uml.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;
import ru.novosoft.uml.behavior.collaborations.*;
import ru.novosoft.uml.behavior.use_cases.*;
import ru.novosoft.uml.behavior.state_machines.*;
import ru.novosoft.uml.behavior.activity_graphs.*;
import ru.novosoft.uml.model_management.*;

import java.lang.reflect.Method;

public class MActionImpl extends MModelElementImpl implements MAction
{
  // ------------ code for class Action -----------------
  // generating attributes
  // attribute: script
  private final static Method _script_setMethod = getMethod1(MActionImpl.class, "setScript", MActionExpression.class);
  MActionExpression _script;
  public final MActionExpression getScript()
  {
    checkExists();
    return _script;
  }
  public final void setScript(MActionExpression __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      logAttrSet(_script_setMethod, _script, __arg);
      fireAttrSet("script", _script, __arg);
      _script = __arg;
    }
    finally
    {
      operationFinished();
    }
  }
  // attribute: isAsynchronous
  private final static Method _isAsynchronous_setMethod = getMethod1(MActionImpl.class, "setAsynchronous", boolean.class);
  boolean _isAsynchronous;
  public final boolean isAsynchronous()
  {
    checkExists();
    return _isAsynchronous;
  }
  public final void setAsynchronous(boolean __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      logAttrSet(_isAsynchronous_setMethod, _isAsynchronous, __arg);
      fireAttrSet("isAsynchronous", _isAsynchronous, __arg);
      _isAsynchronous = __arg;
    }
    finally
    {
      operationFinished();
    }
  }
  // attribute: target
  private final static Method _target_setMethod = getMethod1(MActionImpl.class, "setTarget", MObjectSetExpression.class);
  MObjectSetExpression _target;
  public final MObjectSetExpression getTarget()
  {
    checkExists();
    return _target;
  }
  public final void setTarget(MObjectSetExpression __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      logAttrSet(_target_setMethod, _target, __arg);
      fireAttrSet("target", _target, __arg);
      _target = __arg;
    }
    finally
    {
      operationFinished();
    }
  }
  // attribute: recurrence
  private final static Method _recurrence_setMethod = getMethod1(MActionImpl.class, "setRecurrence", MIterationExpression.class);
  MIterationExpression _recurrence;
  public final MIterationExpression getRecurrence()
  {
    checkExists();
    return _recurrence;
  }
  public final void setRecurrence(MIterationExpression __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      logAttrSet(_recurrence_setMethod, _recurrence, __arg);
      fireAttrSet("recurrence", _recurrence, __arg);
      _recurrence = __arg;
    }
    finally
    {
      operationFinished();
    }
  }
  // generating associations
  // opposite role: stimulus this role: dispatchAction
  private final static Method _stimulus_setMethod = getMethod1(MActionImpl.class, "setStimuli", Collection.class);
  private final static Method _stimulus_addMethod = getMethod1(MActionImpl.class, "addStimulus", MStimulus.class);
  private final static Method _stimulus_removeMethod = getMethod1(MActionImpl.class, "removeStimulus", MStimulus.class);
  Collection _stimulus = Collections.EMPTY_LIST;
  Collection _stimulus_ucopy = Collections.EMPTY_LIST;
  public final Collection getStimuli()
  {
    checkExists();
    if (null == _stimulus_ucopy)
    {
      _stimulus_ucopy = ucopy(_stimulus);
    }
    return _stimulus_ucopy;
  }
  public final void setStimuli(Collection __arg)
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
        old = getStimuli();
      }
      _stimulus_ucopy = null;
      Collection __added = bagdiff(__arg,_stimulus);
      Collection __removed = bagdiff(_stimulus, __arg);
      Iterator iter1 = __removed.iterator();
      while (iter1.hasNext())
      {
        MStimulus o = (MStimulus)iter1.next();
        o.internalUnrefByDispatchAction(this);
      }
      Iterator iter2 = __added.iterator();
      while (iter2.hasNext())
      {
        MStimulus o = (MStimulus)iter2.next();
        o.internalRefByDispatchAction(this);
      }
      _stimulus = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_stimulus_setMethod, old, getStimuli());
      }
      if (sendEvent)
      {
        fireBagSet("stimulus", old, getStimuli());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addStimulus(MStimulus __arg)
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
        old = getStimuli();
      }
      if (null != _stimulus_ucopy)
      {
        _stimulus = new ArrayList(_stimulus);
        _stimulus_ucopy = null;
      }
      __arg.internalRefByDispatchAction(this);
      _stimulus.add(__arg);
      logBagAdd(_stimulus_addMethod, _stimulus_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("stimulus", old, getStimuli(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeStimulus(MStimulus __arg)
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
        old = getStimuli();
      }
      if (null != _stimulus_ucopy)
      {
        _stimulus = new ArrayList(_stimulus);
        _stimulus_ucopy = null;
      }
      if (!_stimulus.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByDispatchAction(this);
      logBagRemove(_stimulus_removeMethod, _stimulus_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("stimulus", old, getStimuli(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByStimulus(MStimulus __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getStimuli();
    }
    if (null != _stimulus_ucopy)
    {
      _stimulus = new ArrayList(_stimulus);
      _stimulus_ucopy = null;
    }
    _stimulus.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("stimulus", old, getStimuli(), __arg);
    }
  }
  public final void internalUnrefByStimulus(MStimulus __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getStimuli();
    }
    if (null != _stimulus_ucopy)
    {
      _stimulus = new ArrayList(_stimulus);
      _stimulus_ucopy = null;
    }
    _stimulus.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("stimulus", old, getStimuli(), __arg);
    }
  }
  // opposite role: actionSequence this role: action
  MActionSequence _actionSequence;
  public final MActionSequence getActionSequence()
  {
    checkExists();
    return _actionSequence;
  }
  public final void setActionSequence(MActionSequence __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      if (_actionSequence != __arg)
      {
        if (_actionSequence != null)
        {
          _actionSequence.removeAction(this);
        }
        if (__arg != null)
        {
          __arg.addAction(this);
        }
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByActionSequence(MActionSequence __arg)
  {
    MActionSequence __saved = _actionSequence;
    if (_actionSequence != null)
    {
      _actionSequence.removeAction(this);
    }
    fireRefSet("actionSequence", __saved, __arg);
    _actionSequence = __arg;
    setModelElementContainer(_actionSequence, "actionSequence");
  }
  public final void internalUnrefByActionSequence(MActionSequence __arg)
  {
    fireRefSet("actionSequence", _actionSequence, null);
    _actionSequence = null;
    setModelElementContainer(null, null);
  }
  // opposite role: actualArgument this role: action
  private final static Method _actualArgument_setMethod = getMethod1(MActionImpl.class, "setActualArguments", List.class);
  private final static Method _actualArgument_removeMethod = getMethod1(MActionImpl.class, "removeActualArgument", int.class);
  private final static Method _actualArgument_addMethod = getMethod2(MActionImpl.class, "addActualArgument", int.class, MArgument.class);
  private final static Method _actualArgument_listSetMethod = getMethod2(MActionImpl.class, "setActualArgument", int.class, MArgument.class);
  List _actualArgument = Collections.EMPTY_LIST;
  List _actualArgument_ucopy = Collections.EMPTY_LIST;
  public final List getActualArguments()
  {
    checkExists();
    if (null == _actualArgument_ucopy)
    {
      _actualArgument_ucopy = ucopy(_actualArgument);
    }
    return _actualArgument_ucopy;
  }
  public final void setActualArguments(List __arg)
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
        old = getActualArguments();
      }
      _actualArgument_ucopy = null;
      Collection __added = bagdiff(__arg,_actualArgument);
      Collection __removed = bagdiff(_actualArgument, __arg);
      Iterator iter3 = __removed.iterator();
      while (iter3.hasNext())
      {
        MArgument o = (MArgument)iter3.next();
        o.internalUnrefByAction(this);
      }
      Iterator iter4 = __added.iterator();
      while (iter4.hasNext())
      {
        MArgument o = (MArgument)iter4.next();
        o.internalRefByAction(this);
      }
      _actualArgument = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_actualArgument_setMethod, old, getActualArguments());
      }
      if (sendEvent)
      {
        fireListSet("actualArgument", old, getActualArguments());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addActualArgument(MArgument __arg)
  {
    addActualArgument(_actualArgument.size(), __arg);
  }
  public final void removeActualArgument(MArgument __arg)
  {
    if (__arg == null)
    {
      throw new NullPointerException();
    }
    int __pos = _actualArgument.indexOf(__arg);
    removeActualArgument(__pos);
  }
  public final void addActualArgument(int __pos, MArgument __arg)
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
        old = getActualArguments();
      }
      if (null != _actualArgument_ucopy)
      {
        _actualArgument = new ArrayList(_actualArgument);
        _actualArgument_ucopy = null;
      }
      _actualArgument.add(__pos, __arg);
      __arg.internalRefByAction(this);
      logListAdd(_actualArgument_addMethod, _actualArgument_removeMethod, __arg, __pos);
      if (sendEvent)
      {
        fireListAdd("actualArgument", old, getActualArguments(), __arg, __pos);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeActualArgument(int __pos)
  {
    operationStarted();
    try
    {
      checkExists();
      final boolean sendEvent = needEvent();
      List old = null;
      if (sendEvent)
      {
        old = getActualArguments();
      }
      if (null != _actualArgument_ucopy)
      {
        _actualArgument = new ArrayList(_actualArgument);
        _actualArgument_ucopy = null;
      }
      MArgument __arg = (MArgument)_actualArgument.remove(__pos);
      __arg.internalUnrefByAction(this);
      logListRemove(_actualArgument_removeMethod, _actualArgument_addMethod, __arg, __pos);
      if (sendEvent)
      {
        fireListRemove("actualArgument", old, getActualArguments(), __arg, __pos);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void setActualArgument(int __pos, MArgument __arg)
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
        old = getActualArguments();
      }
      if (null != _actualArgument_ucopy)
      {
        _actualArgument = new ArrayList(_actualArgument);
        _actualArgument_ucopy = null;
      }
      MArgument __old = (MArgument)_actualArgument.get(__pos);
      __old.internalUnrefByAction(this);
      __arg.internalRefByAction(this);
      _actualArgument.set(__pos,__arg);
      logListSet(_actualArgument_listSetMethod, __old, __arg, __pos);
      if (sendEvent)
      {
        fireListItemSet("actualArgument", old, getActualArguments(), __old, __arg, __pos);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final MArgument getActualArgument(int __pos)
  {
    checkExists();
    return (MArgument)_actualArgument.get(__pos);
  }
  // opposite role: message this role: action
  private final static Method _message_setMethod = getMethod1(MActionImpl.class, "setMessages", Collection.class);
  private final static Method _message_addMethod = getMethod1(MActionImpl.class, "addMessage", MMessage.class);
  private final static Method _message_removeMethod = getMethod1(MActionImpl.class, "removeMessage", MMessage.class);
  Collection _message = Collections.EMPTY_LIST;
  Collection _message_ucopy = Collections.EMPTY_LIST;
  public final Collection getMessages()
  {
    checkExists();
    if (null == _message_ucopy)
    {
      _message_ucopy = ucopy(_message);
    }
    return _message_ucopy;
  }
  public final void setMessages(Collection __arg)
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
        old = getMessages();
      }
      _message_ucopy = null;
      Collection __added = bagdiff(__arg,_message);
      Collection __removed = bagdiff(_message, __arg);
      Iterator iter5 = __removed.iterator();
      while (iter5.hasNext())
      {
        MMessage o = (MMessage)iter5.next();
        o.internalUnrefByAction(this);
      }
      Iterator iter6 = __added.iterator();
      while (iter6.hasNext())
      {
        MMessage o = (MMessage)iter6.next();
        o.internalRefByAction(this);
      }
      _message = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_message_setMethod, old, getMessages());
      }
      if (sendEvent)
      {
        fireBagSet("message", old, getMessages());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addMessage(MMessage __arg)
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
        old = getMessages();
      }
      if (null != _message_ucopy)
      {
        _message = new ArrayList(_message);
        _message_ucopy = null;
      }
      __arg.internalRefByAction(this);
      _message.add(__arg);
      logBagAdd(_message_addMethod, _message_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("message", old, getMessages(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeMessage(MMessage __arg)
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
        old = getMessages();
      }
      if (null != _message_ucopy)
      {
        _message = new ArrayList(_message);
        _message_ucopy = null;
      }
      if (!_message.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByAction(this);
      logBagRemove(_message_removeMethod, _message_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("message", old, getMessages(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByMessage(MMessage __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getMessages();
    }
    if (null != _message_ucopy)
    {
      _message = new ArrayList(_message);
      _message_ucopy = null;
    }
    _message.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("message", old, getMessages(), __arg);
    }
  }
  public final void internalUnrefByMessage(MMessage __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getMessages();
    }
    if (null != _message_ucopy)
    {
      _message = new ArrayList(_message);
      _message_ucopy = null;
    }
    _message.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("message", old, getMessages(), __arg);
    }
  }
  // opposite role: state3 this role: doActivity
  private final static Method _state3_setMethod = getMethod1(MActionImpl.class, "setState3", MState.class);
  MState _state3;
  public final MState getState3()
  {
    checkExists();
    return _state3;
  }
  public final void setState3(MState __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      MState __saved = _state3;
      if (_state3 != __arg)
      {
        if (__saved != null)
        {
          __saved.internalUnrefByDoActivity(this);
        }
        if (__arg != null)
        {
          __arg.internalRefByDoActivity(this);
        }
        logRefSet(_state3_setMethod, __saved, __arg);
        fireRefSet("state3", __saved, __arg);
        _state3 = __arg;
        setModelElementContainer(_state3, "state3");
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByState3(MState __arg)
  {
    MState __saved = _state3;
    if (__saved != null)
    {
      __saved.setDoActivity(null);
    }
    fireRefSet("state3", __saved, __arg);
    _state3 = __arg;
    setModelElementContainer(_state3, "state3");
  }
  public final void internalUnrefByState3(MState __arg)
  {
    fireRefSet("state3", _state3, null);
    _state3 = null;
    setModelElementContainer(null, null);
  }
  // opposite role: transition this role: effect
  private final static Method _transition_setMethod = getMethod1(MActionImpl.class, "setTransition", MTransition.class);
  MTransition _transition;
  public final MTransition getTransition()
  {
    checkExists();
    return _transition;
  }
  public final void setTransition(MTransition __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      MTransition __saved = _transition;
      if (_transition != __arg)
      {
        if (__saved != null)
        {
          __saved.internalUnrefByEffect(this);
        }
        if (__arg != null)
        {
          __arg.internalRefByEffect(this);
        }
        logRefSet(_transition_setMethod, __saved, __arg);
        fireRefSet("transition", __saved, __arg);
        _transition = __arg;
        setModelElementContainer(_transition, "transition");
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByTransition(MTransition __arg)
  {
    MTransition __saved = _transition;
    if (__saved != null)
    {
      __saved.setEffect(null);
    }
    fireRefSet("transition", __saved, __arg);
    _transition = __arg;
    setModelElementContainer(_transition, "transition");
  }
  public final void internalUnrefByTransition(MTransition __arg)
  {
    fireRefSet("transition", _transition, null);
    _transition = null;
    setModelElementContainer(null, null);
  }
  // opposite role: state2 this role: exit
  private final static Method _state2_setMethod = getMethod1(MActionImpl.class, "setState2", MState.class);
  MState _state2;
  public final MState getState2()
  {
    checkExists();
    return _state2;
  }
  public final void setState2(MState __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      MState __saved = _state2;
      if (_state2 != __arg)
      {
        if (__saved != null)
        {
          __saved.internalUnrefByExit(this);
        }
        if (__arg != null)
        {
          __arg.internalRefByExit(this);
        }
        logRefSet(_state2_setMethod, __saved, __arg);
        fireRefSet("state2", __saved, __arg);
        _state2 = __arg;
        setModelElementContainer(_state2, "state2");
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByState2(MState __arg)
  {
    MState __saved = _state2;
    if (__saved != null)
    {
      __saved.setExit(null);
    }
    fireRefSet("state2", __saved, __arg);
    _state2 = __arg;
    setModelElementContainer(_state2, "state2");
  }
  public final void internalUnrefByState2(MState __arg)
  {
    fireRefSet("state2", _state2, null);
    _state2 = null;
    setModelElementContainer(null, null);
  }
  // opposite role: state1 this role: entry
  private final static Method _state1_setMethod = getMethod1(MActionImpl.class, "setState1", MState.class);
  MState _state1;
  public final MState getState1()
  {
    checkExists();
    return _state1;
  }
  public final void setState1(MState __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      MState __saved = _state1;
      if (_state1 != __arg)
      {
        if (__saved != null)
        {
          __saved.internalUnrefByEntry(this);
        }
        if (__arg != null)
        {
          __arg.internalRefByEntry(this);
        }
        logRefSet(_state1_setMethod, __saved, __arg);
        fireRefSet("state1", __saved, __arg);
        _state1 = __arg;
        setModelElementContainer(_state1, "state1");
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByState1(MState __arg)
  {
    MState __saved = _state1;
    if (__saved != null)
    {
      __saved.setEntry(null);
    }
    fireRefSet("state1", __saved, __arg);
    _state1 = __arg;
    setModelElementContainer(_state1, "state1");
  }
  public final void internalUnrefByState1(MState __arg)
  {
    fireRefSet("state1", _state1, null);
    _state1 = null;
    setModelElementContainer(null, null);
  }
  protected void cleanup(Collection scheduledForRemove)
  {
    // opposite role: stimulus this role: dispatchAction
    if (_stimulus.size() != 0)
    {
      setStimuli(Collections.EMPTY_LIST);
    }
    // opposite role: actionSequence this role: action
    if (_actionSequence != null )
    {
      setActionSequence(null);
    }
    // opposite role: actualArgument this role: action
    if (_actualArgument.size() != 0)
    {
      scheduledForRemove.addAll(_actualArgument);
      setActualArguments(Collections.EMPTY_LIST);
    }
    // opposite role: message this role: action
    if (_message.size() != 0)
    {
      setMessages(Collections.EMPTY_LIST);
    }
    // opposite role: state3 this role: doActivity
    if (_state3 != null )
    {
      setState3(null);
    }
    // opposite role: transition this role: effect
    if (_transition != null )
    {
      setTransition(null);
    }
    // opposite role: state2 this role: exit
    if (_state2 != null )
    {
      setState2(null);
    }
    // opposite role: state1 this role: entry
    if (_state1 != null )
    {
      setState1(null);
    }
    super.cleanup(scheduledForRemove);
  }

  public String getUMLClassName()
  {
    return "Action";
  }

  // Reflective API

  public Object reflectiveGetValue(String feature)
  {
    if ("script".equals(feature))
    {
      return getScript();
    }
    if ("isAsynchronous".equals(feature))
    {
      return new Boolean(isAsynchronous());
    }
    if ("target".equals(feature))
    {
      return getTarget();
    }
    if ("recurrence".equals(feature))
    {
      return getRecurrence();
    }
    if ("stimulus".equals(feature))
    {
      return getStimuli();
    }
    if ("actionSequence".equals(feature))
    {
      return getActionSequence();
    }
    if ("actualArgument".equals(feature))
    {
      return getActualArguments();
    }
    if ("message".equals(feature))
    {
      return getMessages();
    }
    if ("state3".equals(feature))
    {
      return getState3();
    }
    if ("transition".equals(feature))
    {
      return getTransition();
    }
    if ("state2".equals(feature))
    {
      return getState2();
    }
    if ("state1".equals(feature))
    {
      return getState1();
    }

    return super.reflectiveGetValue(feature);
  }

  public void reflectiveSetValue(String feature, Object obj)
  {
    if ("script".equals(feature))
    {
      setScript((MActionExpression)obj);
      return;
    }
    if ("isAsynchronous".equals(feature))
    {
      setAsynchronous(((Boolean)obj).booleanValue());
      return;
    }
    if ("target".equals(feature))
    {
      setTarget((MObjectSetExpression)obj);
      return;
    }
    if ("recurrence".equals(feature))
    {
      setRecurrence((MIterationExpression)obj);
      return;
    }
    if ("stimulus".equals(feature))
    {
      setStimuli((Collection)obj);
      return;
    }
    if ("actionSequence".equals(feature))
    {
      setActionSequence((MActionSequence)obj);
      return;
    }
    if ("actualArgument".equals(feature))
    {
      setActualArguments((List)obj);
      return;
    }
    if ("message".equals(feature))
    {
      setMessages((Collection)obj);
      return;
    }
    if ("state3".equals(feature))
    {
      setState3((MState)obj);
      return;
    }
    if ("transition".equals(feature))
    {
      setTransition((MTransition)obj);
      return;
    }
    if ("state2".equals(feature))
    {
      setState2((MState)obj);
      return;
    }
    if ("state1".equals(feature))
    {
      setState1((MState)obj);
      return;
    }

    super.reflectiveSetValue(feature, obj);
  }

  public void reflectiveAddValue(String feature, Object obj)
  {
    if ("stimulus".equals(feature))
    {
      addStimulus((MStimulus)obj);
      return;
    }
    if ("actualArgument".equals(feature))
    {
      addActualArgument((MArgument)obj);
      return;
    }
    if ("message".equals(feature))
    {
      addMessage((MMessage)obj);
      return;
    }

    super.reflectiveAddValue(feature, obj);
  }

  public void reflectiveRemoveValue(String feature, Object obj)
  {
    if ("stimulus".equals(feature))
    {
      removeStimulus((MStimulus)obj);
      return;
    }
    if ("actualArgument".equals(feature))
    {
      removeActualArgument((MArgument)obj);
      return;
    }
    if ("message".equals(feature))
    {
      removeMessage((MMessage)obj);
      return;
    }

    super.reflectiveRemoveValue(feature, obj);
  }

  public Object reflectiveGetValue(String feature, int pos)
  {
    if ("actualArgument".equals(feature))
    {
      return getActualArgument(pos);
    }

    return super.reflectiveGetValue(feature, pos);
  }

  public void reflectiveSetValue(String feature, int pos, Object obj)
  {
    if ("actualArgument".equals(feature))
    {
      setActualArgument(pos, (MArgument)obj);
      return;
    }

    super.reflectiveSetValue(feature, pos, obj);
  }

  public void reflectiveAddValue(String feature, int pos, Object obj)
  {
    if ("actualArgument".equals(feature))
    {
      addActualArgument(pos, (MArgument)obj);
      return;
    }

    super.reflectiveAddValue(feature, pos, obj);
  }

  public void reflectiveRemoveValue(String feature, int pos)
  {
    if ("actualArgument".equals(feature))
    {
      removeActualArgument(pos);
      return;
    }

    super.reflectiveRemoveValue(feature, pos);
  }
  public Collection getModelElementContents()
  {
    Collection ret = super.getModelElementContents();
    ret.addAll(getActualArguments());
    return ret;
  }
}
