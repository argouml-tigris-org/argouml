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

public class MParameterImpl extends MModelElementImpl implements MParameter
{
  // ------------ code for class Parameter -----------------
  // generating attributes
  // attribute: kind
  private final static Method _kind_setMethod = getMethod1(MParameterImpl.class, "setKind", MParameterDirectionKind.class);
  MParameterDirectionKind _kind;
  public final MParameterDirectionKind getKind()
  {
    checkExists();
    return _kind;
  }
  public final void setKind(MParameterDirectionKind __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      logAttrSet(_kind_setMethod, _kind, __arg);
      fireAttrSet("kind", _kind, __arg);
      _kind = __arg;
    }
    finally
    {
      operationFinished();
    }
  }
  // attribute: defaultValue
  private final static Method _defaultValue_setMethod = getMethod1(MParameterImpl.class, "setDefaultValue", MExpression.class);
  MExpression _defaultValue;
  public final MExpression getDefaultValue()
  {
    checkExists();
    return _defaultValue;
  }
  public final void setDefaultValue(MExpression __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      logAttrSet(_defaultValue_setMethod, _defaultValue, __arg);
      fireAttrSet("defaultValue", _defaultValue, __arg);
      _defaultValue = __arg;
    }
    finally
    {
      operationFinished();
    }
  }
  // generating associations
  // opposite role: state this role: parameter
  private final static Method _state_setMethod = getMethod1(MParameterImpl.class, "setStates", Collection.class);
  private final static Method _state_addMethod = getMethod1(MParameterImpl.class, "addState", MObjectFlowState.class);
  private final static Method _state_removeMethod = getMethod1(MParameterImpl.class, "removeState", MObjectFlowState.class);
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
      Iterator iter1 = __removed.iterator();
      while (iter1.hasNext())
      {
        MObjectFlowState o = (MObjectFlowState)iter1.next();
        o.internalUnrefByParameter(this);
      }
      Iterator iter2 = __added.iterator();
      while (iter2.hasNext())
      {
        MObjectFlowState o = (MObjectFlowState)iter2.next();
        o.internalRefByParameter(this);
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
  public final void addState(MObjectFlowState __arg)
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
      __arg.internalRefByParameter(this);
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
  public final void removeState(MObjectFlowState __arg)
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
      __arg.internalUnrefByParameter(this);
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
  public final void internalRefByState(MObjectFlowState __arg)
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
  public final void internalUnrefByState(MObjectFlowState __arg)
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
  // opposite role: event this role: parameter
  MEvent _event;
  public final MEvent getEvent()
  {
    checkExists();
    return _event;
  }
  public final void setEvent(MEvent __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      if (_event != __arg)
      {
        if (_event != null)
        {
          _event.removeParameter(this);
        }
        if (__arg != null)
        {
          __arg.addParameter(this);
        }
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByEvent(MEvent __arg)
  {
    MEvent __saved = _event;
    if (_event != null)
    {
      _event.removeParameter(this);
    }
    fireRefSet("event", __saved, __arg);
    _event = __arg;
    setModelElementContainer(_event, "event");
  }
  public final void internalUnrefByEvent(MEvent __arg)
  {
    fireRefSet("event", _event, null);
    _event = null;
    setModelElementContainer(null, null);
  }
  // opposite role: type this role: parameter
  private final static Method _type_setMethod = getMethod1(MParameterImpl.class, "setType", MClassifier.class);
  MClassifier _type;
  public final MClassifier getType()
  {
    checkExists();
    return _type;
  }
  public final void setType(MClassifier __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      MClassifier __saved = _type;
      if (_type != __arg)
      {
        if (__saved != null)
        {
          __saved.internalUnrefByParameter(this);
        }
        if (__arg != null)
        {
          __arg.internalRefByParameter(this);
        }
        logRefSet(_type_setMethod, __saved, __arg);
        fireRefSet("type", __saved, __arg);
        _type = __arg;
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByType(MClassifier __arg)
  {
    MClassifier __saved = _type;
    if (_type != null)
    {
      _type.removeParameter(this);
    }
    fireRefSet("type", __saved, __arg);
    _type = __arg;
  }
  public final void internalUnrefByType(MClassifier __arg)
  {
    fireRefSet("type", _type, null);
    _type = null;
  }
  // opposite role: behavioralFeature this role: parameter
  MBehavioralFeature _behavioralFeature;
  public final MBehavioralFeature getBehavioralFeature()
  {
    checkExists();
    return _behavioralFeature;
  }
  public final void setBehavioralFeature(MBehavioralFeature __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      if (_behavioralFeature != __arg)
      {
        if (_behavioralFeature != null)
        {
          _behavioralFeature.removeParameter(this);
        }
        if (__arg != null)
        {
          __arg.addParameter(this);
        }
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByBehavioralFeature(MBehavioralFeature __arg)
  {
    MBehavioralFeature __saved = _behavioralFeature;
    if (_behavioralFeature != null)
    {
      _behavioralFeature.removeParameter(this);
    }
    fireRefSet("behavioralFeature", __saved, __arg);
    _behavioralFeature = __arg;
    setModelElementContainer(_behavioralFeature, "behavioralFeature");
  }
  public final void internalUnrefByBehavioralFeature(MBehavioralFeature __arg)
  {
    fireRefSet("behavioralFeature", _behavioralFeature, null);
    _behavioralFeature = null;
    setModelElementContainer(null, null);
  }
  protected void cleanup(Collection scheduledForRemove)
  {
    // opposite role: state this role: parameter
    if (_state.size() != 0)
    {
      setStates(Collections.EMPTY_LIST);
    }
    // opposite role: event this role: parameter
    if (_event != null )
    {
      setEvent(null);
    }
    // opposite role: type this role: parameter
    if (_type != null )
    {
      setType(null);
    }
    // opposite role: behavioralFeature this role: parameter
    if (_behavioralFeature != null )
    {
      setBehavioralFeature(null);
    }
    super.cleanup(scheduledForRemove);
  }

  public String getUMLClassName()
  {
    return "Parameter";
  }

  // Reflective API

  public Object reflectiveGetValue(String feature)
  {
    if ("kind".equals(feature))
    {
      return getKind();
    }
    if ("defaultValue".equals(feature))
    {
      return getDefaultValue();
    }
    if ("state".equals(feature))
    {
      return getStates();
    }
    if ("event".equals(feature))
    {
      return getEvent();
    }
    if ("type".equals(feature))
    {
      return getType();
    }
    if ("behavioralFeature".equals(feature))
    {
      return getBehavioralFeature();
    }

    return super.reflectiveGetValue(feature);
  }

  public void reflectiveSetValue(String feature, Object obj)
  {
    if ("kind".equals(feature))
    {
      setKind((MParameterDirectionKind)obj);
      return;
    }
    if ("defaultValue".equals(feature))
    {
      setDefaultValue((MExpression)obj);
      return;
    }
    if ("state".equals(feature))
    {
      setStates((Collection)obj);
      return;
    }
    if ("event".equals(feature))
    {
      setEvent((MEvent)obj);
      return;
    }
    if ("type".equals(feature))
    {
      setType((MClassifier)obj);
      return;
    }
    if ("behavioralFeature".equals(feature))
    {
      setBehavioralFeature((MBehavioralFeature)obj);
      return;
    }

    super.reflectiveSetValue(feature, obj);
  }

  public void reflectiveAddValue(String feature, Object obj)
  {
    if ("state".equals(feature))
    {
      addState((MObjectFlowState)obj);
      return;
    }

    super.reflectiveAddValue(feature, obj);
  }

  public void reflectiveRemoveValue(String feature, Object obj)
  {
    if ("state".equals(feature))
    {
      removeState((MObjectFlowState)obj);
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
    return ret;
  }
}
