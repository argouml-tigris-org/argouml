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

public class MComponentInstanceImpl extends MInstanceImpl implements MComponentInstance
{
  // ------------ code for class ComponentInstance -----------------
  // generating attributes
  // generating associations
  // opposite role: resident this role: componentInstance
  private final static Method _resident_setMethod = getMethod1(MComponentInstanceImpl.class, "setResidents", Collection.class);
  private final static Method _resident_addMethod = getMethod1(MComponentInstanceImpl.class, "addResident", MInstance.class);
  private final static Method _resident_removeMethod = getMethod1(MComponentInstanceImpl.class, "removeResident", MInstance.class);
  Collection _resident = Collections.EMPTY_LIST;
  Collection _resident_ucopy = Collections.EMPTY_LIST;
  public final Collection getResidents()
  {
    checkExists();
    if (null == _resident_ucopy)
    {
      _resident_ucopy = ucopy(_resident);
    }
    return _resident_ucopy;
  }
  public final void setResidents(Collection __arg)
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
        old = getResidents();
      }
      _resident_ucopy = null;
      Collection __added = bagdiff(__arg,_resident);
      Collection __removed = bagdiff(_resident, __arg);
      Iterator iter1 = __removed.iterator();
      while (iter1.hasNext())
      {
        MInstance o = (MInstance)iter1.next();
        o.internalUnrefByComponentInstance(this);
      }
      Iterator iter2 = __added.iterator();
      while (iter2.hasNext())
      {
        MInstance o = (MInstance)iter2.next();
        o.internalRefByComponentInstance(this);
      }
      _resident = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_resident_setMethod, old, getResidents());
      }
      if (sendEvent)
      {
        fireBagSet("resident", old, getResidents());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addResident(MInstance __arg)
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
        old = getResidents();
      }
      if (null != _resident_ucopy)
      {
        _resident = new ArrayList(_resident);
        _resident_ucopy = null;
      }
      __arg.internalRefByComponentInstance(this);
      _resident.add(__arg);
      logBagAdd(_resident_addMethod, _resident_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("resident", old, getResidents(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeResident(MInstance __arg)
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
        old = getResidents();
      }
      if (null != _resident_ucopy)
      {
        _resident = new ArrayList(_resident);
        _resident_ucopy = null;
      }
      if (!_resident.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByComponentInstance(this);
      logBagRemove(_resident_removeMethod, _resident_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("resident", old, getResidents(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByResident(MInstance __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getResidents();
    }
    if (null != _resident_ucopy)
    {
      _resident = new ArrayList(_resident);
      _resident_ucopy = null;
    }
    _resident.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("resident", old, getResidents(), __arg);
    }
  }
  public final void internalUnrefByResident(MInstance __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getResidents();
    }
    if (null != _resident_ucopy)
    {
      _resident = new ArrayList(_resident);
      _resident_ucopy = null;
    }
    _resident.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("resident", old, getResidents(), __arg);
    }
  }
  // opposite role: nodeInstance this role: resident
  private final static Method _nodeInstance_setMethod = getMethod1(MComponentInstanceImpl.class, "setNodeInstance", MNodeInstance.class);
  MNodeInstance _nodeInstance;
  public final MNodeInstance getNodeInstance()
  {
    checkExists();
    return _nodeInstance;
  }
  public final void setNodeInstance(MNodeInstance __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      MNodeInstance __saved = _nodeInstance;
      if (_nodeInstance != __arg)
      {
        if (__saved != null)
        {
          __saved.internalUnrefByResident(this);
        }
        if (__arg != null)
        {
          __arg.internalRefByResident(this);
        }
        logRefSet(_nodeInstance_setMethod, __saved, __arg);
        fireRefSet("nodeInstance", __saved, __arg);
        _nodeInstance = __arg;
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByNodeInstance(MNodeInstance __arg)
  {
    MNodeInstance __saved = _nodeInstance;
    if (_nodeInstance != null)
    {
      _nodeInstance.removeResident(this);
    }
    fireRefSet("nodeInstance", __saved, __arg);
    _nodeInstance = __arg;
  }
  public final void internalUnrefByNodeInstance(MNodeInstance __arg)
  {
    fireRefSet("nodeInstance", _nodeInstance, null);
    _nodeInstance = null;
  }
  protected void cleanup(Collection scheduledForRemove)
  {
    // opposite role: resident this role: componentInstance
    if (_resident.size() != 0)
    {
      setResidents(Collections.EMPTY_LIST);
    }
    // opposite role: nodeInstance this role: resident
    if (_nodeInstance != null )
    {
      setNodeInstance(null);
    }
    super.cleanup(scheduledForRemove);
  }

  public String getUMLClassName()
  {
    return "ComponentInstance";
  }

  // Reflective API

  public Object reflectiveGetValue(String feature)
  {
    if ("resident".equals(feature))
    {
      return getResidents();
    }
    if ("nodeInstance".equals(feature))
    {
      return getNodeInstance();
    }

    return super.reflectiveGetValue(feature);
  }

  public void reflectiveSetValue(String feature, Object obj)
  {
    if ("resident".equals(feature))
    {
      setResidents((Collection)obj);
      return;
    }
    if ("nodeInstance".equals(feature))
    {
      setNodeInstance((MNodeInstance)obj);
      return;
    }

    super.reflectiveSetValue(feature, obj);
  }

  public void reflectiveAddValue(String feature, Object obj)
  {
    if ("resident".equals(feature))
    {
      addResident((MInstance)obj);
      return;
    }

    super.reflectiveAddValue(feature, obj);
  }

  public void reflectiveRemoveValue(String feature, Object obj)
  {
    if ("resident".equals(feature))
    {
      removeResident((MInstance)obj);
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
