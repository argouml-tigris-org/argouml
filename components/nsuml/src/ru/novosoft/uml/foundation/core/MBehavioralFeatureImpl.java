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

public abstract class MBehavioralFeatureImpl extends MFeatureImpl implements MBehavioralFeature
{
  // ------------ code for class BehavioralFeature -----------------
  // generating attributes
  // attribute: isQuery
  private final static Method _isQuery_setMethod = getMethod1(MBehavioralFeatureImpl.class, "setQuery", boolean.class);
  boolean _isQuery;
  public final boolean isQuery()
  {
    checkExists();
    return _isQuery;
  }
  public final void setQuery(boolean __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      logAttrSet(_isQuery_setMethod, _isQuery, __arg);
      fireAttrSet("isQuery", _isQuery, __arg);
      _isQuery = __arg;
    }
    finally
    {
      operationFinished();
    }
  }
  // generating associations
  // opposite role: raisedSignal this role: context
  private final static Method _raisedSignal_setMethod = getMethod1(MBehavioralFeatureImpl.class, "setRaisedSignals", Collection.class);
  private final static Method _raisedSignal_addMethod = getMethod1(MBehavioralFeatureImpl.class, "addRaisedSignal", MSignal.class);
  private final static Method _raisedSignal_removeMethod = getMethod1(MBehavioralFeatureImpl.class, "removeRaisedSignal", MSignal.class);
  Collection _raisedSignal = Collections.EMPTY_LIST;
  Collection _raisedSignal_ucopy = Collections.EMPTY_LIST;
  public final Collection getRaisedSignals()
  {
    checkExists();
    if (null == _raisedSignal_ucopy)
    {
      _raisedSignal_ucopy = ucopy(_raisedSignal);
    }
    return _raisedSignal_ucopy;
  }
  public final void setRaisedSignals(Collection __arg)
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
        old = getRaisedSignals();
      }
      _raisedSignal_ucopy = null;
      Collection __added = bagdiff(__arg,_raisedSignal);
      Collection __removed = bagdiff(_raisedSignal, __arg);
      Iterator iter1 = __removed.iterator();
      while (iter1.hasNext())
      {
        MSignal o = (MSignal)iter1.next();
        o.internalUnrefByContext(this);
      }
      Iterator iter2 = __added.iterator();
      while (iter2.hasNext())
      {
        MSignal o = (MSignal)iter2.next();
        o.internalRefByContext(this);
      }
      _raisedSignal = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_raisedSignal_setMethod, old, getRaisedSignals());
      }
      if (sendEvent)
      {
        fireBagSet("raisedSignal", old, getRaisedSignals());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addRaisedSignal(MSignal __arg)
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
        old = getRaisedSignals();
      }
      if (null != _raisedSignal_ucopy)
      {
        _raisedSignal = new ArrayList(_raisedSignal);
        _raisedSignal_ucopy = null;
      }
      __arg.internalRefByContext(this);
      _raisedSignal.add(__arg);
      logBagAdd(_raisedSignal_addMethod, _raisedSignal_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("raisedSignal", old, getRaisedSignals(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeRaisedSignal(MSignal __arg)
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
        old = getRaisedSignals();
      }
      if (null != _raisedSignal_ucopy)
      {
        _raisedSignal = new ArrayList(_raisedSignal);
        _raisedSignal_ucopy = null;
      }
      if (!_raisedSignal.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByContext(this);
      logBagRemove(_raisedSignal_removeMethod, _raisedSignal_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("raisedSignal", old, getRaisedSignals(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByRaisedSignal(MSignal __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getRaisedSignals();
    }
    if (null != _raisedSignal_ucopy)
    {
      _raisedSignal = new ArrayList(_raisedSignal);
      _raisedSignal_ucopy = null;
    }
    _raisedSignal.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("raisedSignal", old, getRaisedSignals(), __arg);
    }
  }
  public final void internalUnrefByRaisedSignal(MSignal __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getRaisedSignals();
    }
    if (null != _raisedSignal_ucopy)
    {
      _raisedSignal = new ArrayList(_raisedSignal);
      _raisedSignal_ucopy = null;
    }
    _raisedSignal.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("raisedSignal", old, getRaisedSignals(), __arg);
    }
  }
  // opposite role: parameter this role: behavioralFeature
  private final static Method _parameter_setMethod = getMethod1(MBehavioralFeatureImpl.class, "setParameters", List.class);
  private final static Method _parameter_removeMethod = getMethod1(MBehavioralFeatureImpl.class, "removeParameter", int.class);
  private final static Method _parameter_addMethod = getMethod2(MBehavioralFeatureImpl.class, "addParameter", int.class, MParameter.class);
  private final static Method _parameter_listSetMethod = getMethod2(MBehavioralFeatureImpl.class, "setParameter", int.class, MParameter.class);
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
      Iterator iter3 = __removed.iterator();
      while (iter3.hasNext())
      {
        MParameter o = (MParameter)iter3.next();
        o.internalUnrefByBehavioralFeature(this);
      }
      Iterator iter4 = __added.iterator();
      while (iter4.hasNext())
      {
        MParameter o = (MParameter)iter4.next();
        o.internalRefByBehavioralFeature(this);
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
      __arg.internalRefByBehavioralFeature(this);
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
      __arg.internalUnrefByBehavioralFeature(this);
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
      __old.internalUnrefByBehavioralFeature(this);
      __arg.internalRefByBehavioralFeature(this);
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
    // opposite role: raisedSignal this role: context
    if (_raisedSignal.size() != 0)
    {
      setRaisedSignals(Collections.EMPTY_LIST);
    }
    // opposite role: parameter this role: behavioralFeature
    if (_parameter.size() != 0)
    {
      scheduledForRemove.addAll(_parameter);
      setParameters(Collections.EMPTY_LIST);
    }
    super.cleanup(scheduledForRemove);
  }

  public String getUMLClassName()
  {
    return "BehavioralFeature";
  }

  // Reflective API

  public Object reflectiveGetValue(String feature)
  {
    if ("isQuery".equals(feature))
    {
      return new Boolean(isQuery());
    }
    if ("raisedSignal".equals(feature))
    {
      return getRaisedSignals();
    }
    if ("parameter".equals(feature))
    {
      return getParameters();
    }

    return super.reflectiveGetValue(feature);
  }

  public void reflectiveSetValue(String feature, Object obj)
  {
    if ("isQuery".equals(feature))
    {
      setQuery(((Boolean)obj).booleanValue());
      return;
    }
    if ("raisedSignal".equals(feature))
    {
      setRaisedSignals((Collection)obj);
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
    if ("raisedSignal".equals(feature))
    {
      addRaisedSignal((MSignal)obj);
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
    if ("raisedSignal".equals(feature))
    {
      removeRaisedSignal((MSignal)obj);
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
