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

public class MActivityGraphImpl extends MStateMachineImpl implements MActivityGraph
{
  // ------------ code for class ActivityGraph -----------------
  // generating attributes
  // generating associations
  // opposite role: partition this role: activityGraph
  private final static Method _partition_setMethod = getMethod1(MActivityGraphImpl.class, "setPartitions", Collection.class);
  private final static Method _partition_addMethod = getMethod1(MActivityGraphImpl.class, "addPartition", MPartition.class);
  private final static Method _partition_removeMethod = getMethod1(MActivityGraphImpl.class, "removePartition", MPartition.class);
  Collection _partition = Collections.EMPTY_LIST;
  Collection _partition_ucopy = Collections.EMPTY_LIST;
  public final Collection getPartitions()
  {
    checkExists();
    if (null == _partition_ucopy)
    {
      _partition_ucopy = ucopy(_partition);
    }
    return _partition_ucopy;
  }
  public final void setPartitions(Collection __arg)
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
        old = getPartitions();
      }
      _partition_ucopy = null;
      Collection __added = bagdiff(__arg,_partition);
      Collection __removed = bagdiff(_partition, __arg);
      Iterator iter1 = __removed.iterator();
      while (iter1.hasNext())
      {
        MPartition o = (MPartition)iter1.next();
        o.internalUnrefByActivityGraph(this);
      }
      Iterator iter2 = __added.iterator();
      while (iter2.hasNext())
      {
        MPartition o = (MPartition)iter2.next();
        o.internalRefByActivityGraph(this);
      }
      _partition = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_partition_setMethod, old, getPartitions());
      }
      if (sendEvent)
      {
        fireBagSet("partition", old, getPartitions());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addPartition(MPartition __arg)
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
        old = getPartitions();
      }
      if (null != _partition_ucopy)
      {
        _partition = new ArrayList(_partition);
        _partition_ucopy = null;
      }
      __arg.internalRefByActivityGraph(this);
      _partition.add(__arg);
      logBagAdd(_partition_addMethod, _partition_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("partition", old, getPartitions(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removePartition(MPartition __arg)
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
        old = getPartitions();
      }
      if (null != _partition_ucopy)
      {
        _partition = new ArrayList(_partition);
        _partition_ucopy = null;
      }
      if (!_partition.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByActivityGraph(this);
      logBagRemove(_partition_removeMethod, _partition_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("partition", old, getPartitions(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByPartition(MPartition __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getPartitions();
    }
    if (null != _partition_ucopy)
    {
      _partition = new ArrayList(_partition);
      _partition_ucopy = null;
    }
    _partition.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("partition", old, getPartitions(), __arg);
    }
  }
  public final void internalUnrefByPartition(MPartition __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getPartitions();
    }
    if (null != _partition_ucopy)
    {
      _partition = new ArrayList(_partition);
      _partition_ucopy = null;
    }
    _partition.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("partition", old, getPartitions(), __arg);
    }
  }
  protected void cleanup(Collection scheduledForRemove)
  {
    // opposite role: partition this role: activityGraph
    if (_partition.size() != 0)
    {
      scheduledForRemove.addAll(_partition);
      setPartitions(Collections.EMPTY_LIST);
    }
    super.cleanup(scheduledForRemove);
  }

  public String getUMLClassName()
  {
    return "ActivityGraph";
  }

  // Reflective API

  public Object reflectiveGetValue(String feature)
  {
    if ("partition".equals(feature))
    {
      return getPartitions();
    }

    return super.reflectiveGetValue(feature);
  }

  public void reflectiveSetValue(String feature, Object obj)
  {
    if ("partition".equals(feature))
    {
      setPartitions((Collection)obj);
      return;
    }

    super.reflectiveSetValue(feature, obj);
  }

  public void reflectiveAddValue(String feature, Object obj)
  {
    if ("partition".equals(feature))
    {
      addPartition((MPartition)obj);
      return;
    }

    super.reflectiveAddValue(feature, obj);
  }

  public void reflectiveRemoveValue(String feature, Object obj)
  {
    if ("partition".equals(feature))
    {
      removePartition((MPartition)obj);
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
    ret.addAll(getPartitions());
    return ret;
  }
}
