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

package ru.novosoft.uml.model_management;

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
import ru.novosoft.uml.behavior.activity_graphs.*;

import java.lang.reflect.Method;

public class MSubsystemImpl extends MClassifierImpl implements MSubsystem
{
  // ------------ code for class Subsystem -----------------
  // generating attributes
  // attribute: isInstantiable
  private final static Method _isInstantiable_setMethod = getMethod1(MSubsystemImpl.class, "setInstantiable", boolean.class);
  boolean _isInstantiable;
  public final boolean isInstantiable()
  {
    checkExists();
    return _isInstantiable;
  }
  public final void setInstantiable(boolean __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      logAttrSet(_isInstantiable_setMethod, _isInstantiable, __arg);
      fireAttrSet("isInstantiable", _isInstantiable, __arg);
      _isInstantiable = __arg;
    }
    finally
    {
      operationFinished();
    }
  }
  // generating associations
  // ------------ code for class Package -----------------
  // generating attributes
  // generating associations
  // opposite role: elementImport this role: package
  private final static Method _elementImport_setMethod = getMethod1(MSubsystemImpl.class, "setElementImports", Collection.class);
  private final static Method _elementImport_addMethod = getMethod1(MSubsystemImpl.class, "addElementImport", MElementImport.class);
  private final static Method _elementImport_removeMethod = getMethod1(MSubsystemImpl.class, "removeElementImport", MElementImport.class);
  Collection _elementImport = Collections.EMPTY_LIST;
  Collection _elementImport_ucopy = Collections.EMPTY_LIST;
  public final Collection getElementImports()
  {
    checkExists();
    if (null == _elementImport_ucopy)
    {
      _elementImport_ucopy = ucopy(_elementImport);
    }
    return _elementImport_ucopy;
  }
  public final void setElementImports(Collection __arg)
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
        old = getElementImports();
      }
      _elementImport_ucopy = null;
      Collection __added = bagdiff(__arg,_elementImport);
      Collection __removed = bagdiff(_elementImport, __arg);
      Iterator iter1 = __removed.iterator();
      while (iter1.hasNext())
      {
        MElementImport o = (MElementImport)iter1.next();
        o.internalUnrefByPackage(this);
      }
      Iterator iter2 = __added.iterator();
      while (iter2.hasNext())
      {
        MElementImport o = (MElementImport)iter2.next();
        o.internalRefByPackage(this);
      }
      _elementImport = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_elementImport_setMethod, old, getElementImports());
      }
      if (sendEvent)
      {
        fireBagSet("elementImport", old, getElementImports());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addElementImport(MElementImport __arg)
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
        old = getElementImports();
      }
      if (null != _elementImport_ucopy)
      {
        _elementImport = new ArrayList(_elementImport);
        _elementImport_ucopy = null;
      }
      __arg.internalRefByPackage(this);
      _elementImport.add(__arg);
      logBagAdd(_elementImport_addMethod, _elementImport_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("elementImport", old, getElementImports(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeElementImport(MElementImport __arg)
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
        old = getElementImports();
      }
      if (null != _elementImport_ucopy)
      {
        _elementImport = new ArrayList(_elementImport);
        _elementImport_ucopy = null;
      }
      if (!_elementImport.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByPackage(this);
      logBagRemove(_elementImport_removeMethod, _elementImport_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("elementImport", old, getElementImports(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByElementImport(MElementImport __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getElementImports();
    }
    if (null != _elementImport_ucopy)
    {
      _elementImport = new ArrayList(_elementImport);
      _elementImport_ucopy = null;
    }
    _elementImport.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("elementImport", old, getElementImports(), __arg);
    }
  }
  public final void internalUnrefByElementImport(MElementImport __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getElementImports();
    }
    if (null != _elementImport_ucopy)
    {
      _elementImport = new ArrayList(_elementImport);
      _elementImport_ucopy = null;
    }
    _elementImport.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("elementImport", old, getElementImports(), __arg);
    }
  }
  protected void cleanup(Collection scheduledForRemove)
  {
    // opposite role: elementImport this role: package
    if (_elementImport.size() != 0)
    {
      setElementImports(Collections.EMPTY_LIST);
    }
    super.cleanup(scheduledForRemove);
  }

  public String getUMLClassName()
  {
    return "Subsystem";
  }

  // Reflective API

  public Object reflectiveGetValue(String feature)
  {
    if ("isInstantiable".equals(feature))
    {
      return new Boolean(isInstantiable());
    }
    if ("elementImport".equals(feature))
    {
      return getElementImports();
    }

    return super.reflectiveGetValue(feature);
  }

  public void reflectiveSetValue(String feature, Object obj)
  {
    if ("isInstantiable".equals(feature))
    {
      setInstantiable(((Boolean)obj).booleanValue());
      return;
    }
    if ("elementImport".equals(feature))
    {
      setElementImports((Collection)obj);
      return;
    }

    super.reflectiveSetValue(feature, obj);
  }

  public void reflectiveAddValue(String feature, Object obj)
  {
    if ("elementImport".equals(feature))
    {
      addElementImport((MElementImport)obj);
      return;
    }

    super.reflectiveAddValue(feature, obj);
  }

  public void reflectiveRemoveValue(String feature, Object obj)
  {
    if ("elementImport".equals(feature))
    {
      removeElementImport((MElementImport)obj);
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
