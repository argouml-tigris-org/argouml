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

package ru.novosoft.uml.behavior.activity_graphs;

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
import ru.novosoft.uml.behavior.state_machines.*;
import ru.novosoft.uml.model_management.*;

import java.lang.reflect.Method;

public class MObjectFlowStateImpl extends MSimpleStateImpl implements MObjectFlowState
{
  // ------------ code for class ObjectFlowState -----------------
  // generating attributes
  // attribute: isSynch
  private final static Method _isSynch_setMethod = getMethod1(MObjectFlowStateImpl.class, "setSynch", boolean.class);
  boolean _isSynch;
  public final boolean isSynch()
  {
    checkExists();
    return _isSynch;
  }
  public final void setSynch(boolean __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      logAttrSet(_isSynch_setMethod, _isSynch, __arg);
      fireAttrSet("isSynch", _isSynch, __arg);
      _isSynch = __arg;
    }
    finally
    {
      operationFinished();
    }
  }
  // generating associations
  // opposite role: parameter this role: state
  private final static Method _parameter_setMethod = getMethod1(MObjectFlowStateImpl.class, "setParameters", Collection.class);
  private final static Method _parameter_addMethod = getMethod1(MObjectFlowStateImpl.class, "addParameter", MParameter.class);
  private final static Method _parameter_removeMethod = getMethod1(MObjectFlowStateImpl.class, "removeParameter", MParameter.class);
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
      Iterator iter1 = __removed.iterator();
      while (iter1.hasNext())
      {
        MParameter o = (MParameter)iter1.next();
        o.internalUnrefByState(this);
      }
      Iterator iter2 = __added.iterator();
      while (iter2.hasNext())
      {
        MParameter o = (MParameter)iter2.next();
        o.internalRefByState(this);
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
      __arg.internalRefByState(this);
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
      __arg.internalUnrefByState(this);
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
  // opposite role: type this role: objectFlowState
  private final static Method _type_setMethod = getMethod1(MObjectFlowStateImpl.class, "setType", MClassifier.class);
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
          __saved.internalUnrefByObjectFlowState(this);
        }
        if (__arg != null)
        {
          __arg.internalRefByObjectFlowState(this);
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
      _type.removeObjectFlowState(this);
    }
    fireRefSet("type", __saved, __arg);
    _type = __arg;
  }
  public final void internalUnrefByType(MClassifier __arg)
  {
    fireRefSet("type", _type, null);
    _type = null;
  }
  protected void cleanup(Collection scheduledForRemove)
  {
    // opposite role: parameter this role: state
    if (_parameter.size() != 0)
    {
      setParameters(Collections.EMPTY_LIST);
    }
    // opposite role: type this role: objectFlowState
    if (_type != null )
    {
      setType(null);
    }
    super.cleanup(scheduledForRemove);
  }

  public String getUMLClassName()
  {
    return "ObjectFlowState";
  }

  // Reflective API

  public Object reflectiveGetValue(String feature)
  {
    if ("isSynch".equals(feature))
    {
      return new Boolean(isSynch());
    }
    if ("parameter".equals(feature))
    {
      return getParameters();
    }
    if ("type".equals(feature))
    {
      return getType();
    }

    return super.reflectiveGetValue(feature);
  }

  public void reflectiveSetValue(String feature, Object obj)
  {
    if ("isSynch".equals(feature))
    {
      setSynch(((Boolean)obj).booleanValue());
      return;
    }
    if ("parameter".equals(feature))
    {
      setParameters((Collection)obj);
      return;
    }
    if ("type".equals(feature))
    {
      setType((MClassifier)obj);
      return;
    }

    super.reflectiveSetValue(feature, obj);
  }

  public void reflectiveAddValue(String feature, Object obj)
  {
    if ("parameter".equals(feature))
    {
      addParameter((MParameter)obj);
      return;
    }

    super.reflectiveAddValue(feature, obj);
  }

  public void reflectiveRemoveValue(String feature, Object obj)
  {
    if ("parameter".equals(feature))
    {
      removeParameter((MParameter)obj);
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
