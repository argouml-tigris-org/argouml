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

public abstract class MFeatureImpl extends MModelElementImpl implements MFeature
{
  // ------------ code for class Feature -----------------
  // generating attributes
  // attribute: ownerScope
  private final static Method _ownerScope_setMethod = getMethod1(MFeatureImpl.class, "setOwnerScope", MScopeKind.class);
  MScopeKind _ownerScope;
  public final MScopeKind getOwnerScope()
  {
    checkExists();
    return _ownerScope;
  }
  public final void setOwnerScope(MScopeKind __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      logAttrSet(_ownerScope_setMethod, _ownerScope, __arg);
      fireAttrSet("ownerScope", _ownerScope, __arg);
      _ownerScope = __arg;
    }
    finally
    {
      operationFinished();
    }
  }
  // generating associations
  // opposite role: classifierRole this role: availableFeature
  private final static Method _classifierRole_setMethod = getMethod1(MFeatureImpl.class, "setClassifierRoles", Collection.class);
  private final static Method _classifierRole_addMethod = getMethod1(MFeatureImpl.class, "addClassifierRole", MClassifierRole.class);
  private final static Method _classifierRole_removeMethod = getMethod1(MFeatureImpl.class, "removeClassifierRole", MClassifierRole.class);
  Collection _classifierRole = Collections.EMPTY_LIST;
  Collection _classifierRole_ucopy = Collections.EMPTY_LIST;
  public final Collection getClassifierRoles()
  {
    checkExists();
    if (null == _classifierRole_ucopy)
    {
      _classifierRole_ucopy = ucopy(_classifierRole);
    }
    return _classifierRole_ucopy;
  }
  public final void setClassifierRoles(Collection __arg)
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
        old = getClassifierRoles();
      }
      _classifierRole_ucopy = null;
      Collection __added = bagdiff(__arg,_classifierRole);
      Collection __removed = bagdiff(_classifierRole, __arg);
      Iterator iter1 = __removed.iterator();
      while (iter1.hasNext())
      {
        MClassifierRole o = (MClassifierRole)iter1.next();
        o.internalUnrefByAvailableFeature(this);
      }
      Iterator iter2 = __added.iterator();
      while (iter2.hasNext())
      {
        MClassifierRole o = (MClassifierRole)iter2.next();
        o.internalRefByAvailableFeature(this);
      }
      _classifierRole = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_classifierRole_setMethod, old, getClassifierRoles());
      }
      if (sendEvent)
      {
        fireBagSet("classifierRole", old, getClassifierRoles());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addClassifierRole(MClassifierRole __arg)
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
        old = getClassifierRoles();
      }
      if (null != _classifierRole_ucopy)
      {
        _classifierRole = new ArrayList(_classifierRole);
        _classifierRole_ucopy = null;
      }
      __arg.internalRefByAvailableFeature(this);
      _classifierRole.add(__arg);
      logBagAdd(_classifierRole_addMethod, _classifierRole_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("classifierRole", old, getClassifierRoles(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeClassifierRole(MClassifierRole __arg)
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
        old = getClassifierRoles();
      }
      if (null != _classifierRole_ucopy)
      {
        _classifierRole = new ArrayList(_classifierRole);
        _classifierRole_ucopy = null;
      }
      if (!_classifierRole.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByAvailableFeature(this);
      logBagRemove(_classifierRole_removeMethod, _classifierRole_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("classifierRole", old, getClassifierRoles(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByClassifierRole(MClassifierRole __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getClassifierRoles();
    }
    if (null != _classifierRole_ucopy)
    {
      _classifierRole = new ArrayList(_classifierRole);
      _classifierRole_ucopy = null;
    }
    _classifierRole.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("classifierRole", old, getClassifierRoles(), __arg);
    }
  }
  public final void internalUnrefByClassifierRole(MClassifierRole __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getClassifierRoles();
    }
    if (null != _classifierRole_ucopy)
    {
      _classifierRole = new ArrayList(_classifierRole);
      _classifierRole_ucopy = null;
    }
    _classifierRole.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("classifierRole", old, getClassifierRoles(), __arg);
    }
  }
  // opposite role: owner this role: feature
  MClassifier _owner;
  public final MClassifier getOwner()
  {
    checkExists();
    return _owner;
  }
  public final void setOwner(MClassifier __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      if (_owner != __arg)
      {
        if (_owner != null)
        {
          _owner.removeFeature(this);
        }
        if (__arg != null)
        {
          __arg.addFeature(this);
        }
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByOwner(MClassifier __arg)
  {
    MClassifier __saved = _owner;
    if (_owner != null)
    {
      _owner.removeFeature(this);
    }
    fireRefSet("owner", __saved, __arg);
    _owner = __arg;
    setModelElementContainer(_owner, "owner");
  }
  public final void internalUnrefByOwner(MClassifier __arg)
  {
    fireRefSet("owner", _owner, null);
    _owner = null;
    setModelElementContainer(null, null);
  }
  protected void cleanup(Collection scheduledForRemove)
  {
    // opposite role: classifierRole this role: availableFeature
    if (_classifierRole.size() != 0)
    {
      setClassifierRoles(Collections.EMPTY_LIST);
    }
    // opposite role: owner this role: feature
    if (_owner != null )
    {
      setOwner(null);
    }
    super.cleanup(scheduledForRemove);
  }

  public String getUMLClassName()
  {
    return "Feature";
  }

  // Reflective API

  public Object reflectiveGetValue(String feature)
  {
    if ("ownerScope".equals(feature))
    {
      return getOwnerScope();
    }
    if ("classifierRole".equals(feature))
    {
      return getClassifierRoles();
    }
    if ("owner".equals(feature))
    {
      return getOwner();
    }

    return super.reflectiveGetValue(feature);
  }

  public void reflectiveSetValue(String feature, Object obj)
  {
    if ("ownerScope".equals(feature))
    {
      setOwnerScope((MScopeKind)obj);
      return;
    }
    if ("classifierRole".equals(feature))
    {
      setClassifierRoles((Collection)obj);
      return;
    }
    if ("owner".equals(feature))
    {
      setOwner((MClassifier)obj);
      return;
    }

    super.reflectiveSetValue(feature, obj);
  }

  public void reflectiveAddValue(String feature, Object obj)
  {
    if ("classifierRole".equals(feature))
    {
      addClassifierRole((MClassifierRole)obj);
      return;
    }

    super.reflectiveAddValue(feature, obj);
  }

  public void reflectiveRemoveValue(String feature, Object obj)
  {
    if ("classifierRole".equals(feature))
    {
      removeClassifierRole((MClassifierRole)obj);
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
