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

public class MFlowImpl extends MRelationshipImpl implements MFlow
{
  // ------------ code for class Flow -----------------
  // generating attributes
  // generating associations
  // opposite role: source this role: sourceFlow
  private final static Method _source_setMethod = getMethod1(MFlowImpl.class, "setSources", Collection.class);
  private final static Method _source_addMethod = getMethod1(MFlowImpl.class, "addSource", MModelElement.class);
  private final static Method _source_removeMethod = getMethod1(MFlowImpl.class, "removeSource", MModelElement.class);
  Collection _source = Collections.EMPTY_LIST;
  Collection _source_ucopy = Collections.EMPTY_LIST;
  public final Collection getSources()
  {
    checkExists();
    if (null == _source_ucopy)
    {
      _source_ucopy = ucopy(_source);
    }
    return _source_ucopy;
  }
  public final void setSources(Collection __arg)
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
        old = getSources();
      }
      _source_ucopy = null;
      Collection __added = bagdiff(__arg,_source);
      Collection __removed = bagdiff(_source, __arg);
      Iterator iter1 = __removed.iterator();
      while (iter1.hasNext())
      {
        MModelElement o = (MModelElement)iter1.next();
        o.internalUnrefBySourceFlow(this);
      }
      Iterator iter2 = __added.iterator();
      while (iter2.hasNext())
      {
        MModelElement o = (MModelElement)iter2.next();
        o.internalRefBySourceFlow(this);
      }
      _source = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_source_setMethod, old, getSources());
      }
      if (sendEvent)
      {
        fireBagSet("source", old, getSources());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addSource(MModelElement __arg)
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
        old = getSources();
      }
      if (null != _source_ucopy)
      {
        _source = new ArrayList(_source);
        _source_ucopy = null;
      }
      __arg.internalRefBySourceFlow(this);
      _source.add(__arg);
      logBagAdd(_source_addMethod, _source_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("source", old, getSources(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeSource(MModelElement __arg)
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
        old = getSources();
      }
      if (null != _source_ucopy)
      {
        _source = new ArrayList(_source);
        _source_ucopy = null;
      }
      if (!_source.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefBySourceFlow(this);
      logBagRemove(_source_removeMethod, _source_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("source", old, getSources(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefBySource(MModelElement __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getSources();
    }
    if (null != _source_ucopy)
    {
      _source = new ArrayList(_source);
      _source_ucopy = null;
    }
    _source.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("source", old, getSources(), __arg);
    }
  }
  public final void internalUnrefBySource(MModelElement __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getSources();
    }
    if (null != _source_ucopy)
    {
      _source = new ArrayList(_source);
      _source_ucopy = null;
    }
    _source.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("source", old, getSources(), __arg);
    }
  }
  // opposite role: target this role: targetFlow
  private final static Method _target_setMethod = getMethod1(MFlowImpl.class, "setTargets", Collection.class);
  private final static Method _target_addMethod = getMethod1(MFlowImpl.class, "addTarget", MModelElement.class);
  private final static Method _target_removeMethod = getMethod1(MFlowImpl.class, "removeTarget", MModelElement.class);
  Collection _target = Collections.EMPTY_LIST;
  Collection _target_ucopy = Collections.EMPTY_LIST;
  public final Collection getTargets()
  {
    checkExists();
    if (null == _target_ucopy)
    {
      _target_ucopy = ucopy(_target);
    }
    return _target_ucopy;
  }
  public final void setTargets(Collection __arg)
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
        old = getTargets();
      }
      _target_ucopy = null;
      Collection __added = bagdiff(__arg,_target);
      Collection __removed = bagdiff(_target, __arg);
      Iterator iter3 = __removed.iterator();
      while (iter3.hasNext())
      {
        MModelElement o = (MModelElement)iter3.next();
        o.internalUnrefByTargetFlow(this);
      }
      Iterator iter4 = __added.iterator();
      while (iter4.hasNext())
      {
        MModelElement o = (MModelElement)iter4.next();
        o.internalRefByTargetFlow(this);
      }
      _target = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_target_setMethod, old, getTargets());
      }
      if (sendEvent)
      {
        fireBagSet("target", old, getTargets());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addTarget(MModelElement __arg)
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
        old = getTargets();
      }
      if (null != _target_ucopy)
      {
        _target = new ArrayList(_target);
        _target_ucopy = null;
      }
      __arg.internalRefByTargetFlow(this);
      _target.add(__arg);
      logBagAdd(_target_addMethod, _target_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("target", old, getTargets(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeTarget(MModelElement __arg)
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
        old = getTargets();
      }
      if (null != _target_ucopy)
      {
        _target = new ArrayList(_target);
        _target_ucopy = null;
      }
      if (!_target.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByTargetFlow(this);
      logBagRemove(_target_removeMethod, _target_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("target", old, getTargets(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByTarget(MModelElement __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getTargets();
    }
    if (null != _target_ucopy)
    {
      _target = new ArrayList(_target);
      _target_ucopy = null;
    }
    _target.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("target", old, getTargets(), __arg);
    }
  }
  public final void internalUnrefByTarget(MModelElement __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getTargets();
    }
    if (null != _target_ucopy)
    {
      _target = new ArrayList(_target);
      _target_ucopy = null;
    }
    _target.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("target", old, getTargets(), __arg);
    }
  }
  protected void cleanup(Collection scheduledForRemove)
  {
    // opposite role: source this role: sourceFlow
    if (_source.size() != 0)
    {
      setSources(Collections.EMPTY_LIST);
    }
    // opposite role: target this role: targetFlow
    if (_target.size() != 0)
    {
      setTargets(Collections.EMPTY_LIST);
    }
    super.cleanup(scheduledForRemove);
  }

  public String getUMLClassName()
  {
    return "Flow";
  }

  // Reflective API

  public Object reflectiveGetValue(String feature)
  {
    if ("source".equals(feature))
    {
      return getSources();
    }
    if ("target".equals(feature))
    {
      return getTargets();
    }

    return super.reflectiveGetValue(feature);
  }

  public void reflectiveSetValue(String feature, Object obj)
  {
    if ("source".equals(feature))
    {
      setSources((Collection)obj);
      return;
    }
    if ("target".equals(feature))
    {
      setTargets((Collection)obj);
      return;
    }

    super.reflectiveSetValue(feature, obj);
  }

  public void reflectiveAddValue(String feature, Object obj)
  {
    if ("source".equals(feature))
    {
      addSource((MModelElement)obj);
      return;
    }
    if ("target".equals(feature))
    {
      addTarget((MModelElement)obj);
      return;
    }

    super.reflectiveAddValue(feature, obj);
  }

  public void reflectiveRemoveValue(String feature, Object obj)
  {
    if ("source".equals(feature))
    {
      removeSource((MModelElement)obj);
      return;
    }
    if ("target".equals(feature))
    {
      removeTarget((MModelElement)obj);
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
