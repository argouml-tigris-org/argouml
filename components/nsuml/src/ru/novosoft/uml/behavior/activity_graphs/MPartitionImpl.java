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

public class MPartitionImpl extends MModelElementImpl implements MPartition
{
  // ------------ code for class Partition -----------------
  // generating attributes
  // generating associations
  // opposite role: contents this role: partition1
  private final static Method _contents_setMethod = getMethod1(MPartitionImpl.class, "setContents", Collection.class);
  private final static Method _contents_addMethod = getMethod1(MPartitionImpl.class, "addContents", MModelElement.class);
  private final static Method _contents_removeMethod = getMethod1(MPartitionImpl.class, "removeContents", MModelElement.class);
  Collection _contents = Collections.EMPTY_LIST;
  Collection _contents_ucopy = Collections.EMPTY_LIST;
  public final Collection getContents()
  {
    checkExists();
    if (null == _contents_ucopy)
    {
      _contents_ucopy = ucopy(_contents);
    }
    return _contents_ucopy;
  }
  public final void setContents(Collection __arg)
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
        old = getContents();
      }
      _contents_ucopy = null;
      Collection __added = bagdiff(__arg,_contents);
      Collection __removed = bagdiff(_contents, __arg);
      Iterator iter1 = __removed.iterator();
      while (iter1.hasNext())
      {
        MModelElement o = (MModelElement)iter1.next();
        o.internalUnrefByPartition1(this);
      }
      Iterator iter2 = __added.iterator();
      while (iter2.hasNext())
      {
        MModelElement o = (MModelElement)iter2.next();
        o.internalRefByPartition1(this);
      }
      _contents = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_contents_setMethod, old, getContents());
      }
      if (sendEvent)
      {
        fireBagSet("contents", old, getContents());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addContents(MModelElement __arg)
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
        old = getContents();
      }
      if (null != _contents_ucopy)
      {
        _contents = new ArrayList(_contents);
        _contents_ucopy = null;
      }
      __arg.internalRefByPartition1(this);
      _contents.add(__arg);
      logBagAdd(_contents_addMethod, _contents_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("contents", old, getContents(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeContents(MModelElement __arg)
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
        old = getContents();
      }
      if (null != _contents_ucopy)
      {
        _contents = new ArrayList(_contents);
        _contents_ucopy = null;
      }
      if (!_contents.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByPartition1(this);
      logBagRemove(_contents_removeMethod, _contents_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("contents", old, getContents(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByContents(MModelElement __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getContents();
    }
    if (null != _contents_ucopy)
    {
      _contents = new ArrayList(_contents);
      _contents_ucopy = null;
    }
    _contents.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("contents", old, getContents(), __arg);
    }
  }
  public final void internalUnrefByContents(MModelElement __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getContents();
    }
    if (null != _contents_ucopy)
    {
      _contents = new ArrayList(_contents);
      _contents_ucopy = null;
    }
    _contents.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("contents", old, getContents(), __arg);
    }
  }
  // opposite role: activityGraph this role: partition
  private final static Method _activityGraph_setMethod = getMethod1(MPartitionImpl.class, "setActivityGraph", MActivityGraph.class);
  MActivityGraph _activityGraph;
  public final MActivityGraph getActivityGraph()
  {
    checkExists();
    return _activityGraph;
  }
  public final void setActivityGraph(MActivityGraph __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      MActivityGraph __saved = _activityGraph;
      if (_activityGraph != __arg)
      {
        if (__saved != null)
        {
          __saved.internalUnrefByPartition(this);
        }
        if (__arg != null)
        {
          __arg.internalRefByPartition(this);
        }
        logRefSet(_activityGraph_setMethod, __saved, __arg);
        fireRefSet("activityGraph", __saved, __arg);
        _activityGraph = __arg;
        setModelElementContainer(_activityGraph, "activityGraph");
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByActivityGraph(MActivityGraph __arg)
  {
    MActivityGraph __saved = _activityGraph;
    if (_activityGraph != null)
    {
      _activityGraph.removePartition(this);
    }
    fireRefSet("activityGraph", __saved, __arg);
    _activityGraph = __arg;
    setModelElementContainer(_activityGraph, "activityGraph");
  }
  public final void internalUnrefByActivityGraph(MActivityGraph __arg)
  {
    fireRefSet("activityGraph", _activityGraph, null);
    _activityGraph = null;
    setModelElementContainer(null, null);
  }
  protected void cleanup(Collection scheduledForRemove)
  {
    // opposite role: contents this role: partition1
    if (_contents.size() != 0)
    {
      setContents(Collections.EMPTY_LIST);
    }
    // opposite role: activityGraph this role: partition
    if (_activityGraph != null )
    {
      setActivityGraph(null);
    }
    super.cleanup(scheduledForRemove);
  }

  public String getUMLClassName()
  {
    return "Partition";
  }

  // Reflective API

  public Object reflectiveGetValue(String feature)
  {
    if ("contents".equals(feature))
    {
      return getContents();
    }
    if ("activityGraph".equals(feature))
    {
      return getActivityGraph();
    }

    return super.reflectiveGetValue(feature);
  }

  public void reflectiveSetValue(String feature, Object obj)
  {
    if ("contents".equals(feature))
    {
      setContents((Collection)obj);
      return;
    }
    if ("activityGraph".equals(feature))
    {
      setActivityGraph((MActivityGraph)obj);
      return;
    }

    super.reflectiveSetValue(feature, obj);
  }

  public void reflectiveAddValue(String feature, Object obj)
  {
    if ("contents".equals(feature))
    {
      addContents((MModelElement)obj);
      return;
    }

    super.reflectiveAddValue(feature, obj);
  }

  public void reflectiveRemoveValue(String feature, Object obj)
  {
    if ("contents".equals(feature))
    {
      removeContents((MModelElement)obj);
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
