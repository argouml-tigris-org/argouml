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

package ru.novosoft.uml.behavior.collaborations;

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
import ru.novosoft.uml.behavior.use_cases.*;
import ru.novosoft.uml.behavior.state_machines.*;
import ru.novosoft.uml.behavior.activity_graphs.*;
import ru.novosoft.uml.model_management.*;

import java.lang.reflect.Method;

public class MAssociationEndRoleImpl extends MAssociationEndImpl implements MAssociationEndRole
{
  // ------------ code for class AssociationEndRole -----------------
  // generating attributes
  // generating associations
  // opposite role: availableQualifier this role: associationEndRole
  private final static Method _availableQualifier_setMethod = getMethod1(MAssociationEndRoleImpl.class, "setAvailableQualifiers", Collection.class);
  private final static Method _availableQualifier_addMethod = getMethod1(MAssociationEndRoleImpl.class, "addAvailableQualifier", MAttribute.class);
  private final static Method _availableQualifier_removeMethod = getMethod1(MAssociationEndRoleImpl.class, "removeAvailableQualifier", MAttribute.class);
  Collection _availableQualifier = Collections.EMPTY_LIST;
  Collection _availableQualifier_ucopy = Collections.EMPTY_LIST;
  public final Collection getAvailableQualifiers()
  {
    checkExists();
    if (null == _availableQualifier_ucopy)
    {
      _availableQualifier_ucopy = ucopy(_availableQualifier);
    }
    return _availableQualifier_ucopy;
  }
  public final void setAvailableQualifiers(Collection __arg)
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
        old = getAvailableQualifiers();
      }
      _availableQualifier_ucopy = null;
      Collection __added = bagdiff(__arg,_availableQualifier);
      Collection __removed = bagdiff(_availableQualifier, __arg);
      Iterator iter1 = __removed.iterator();
      while (iter1.hasNext())
      {
        MAttribute o = (MAttribute)iter1.next();
        o.internalUnrefByAssociationEndRole(this);
      }
      Iterator iter2 = __added.iterator();
      while (iter2.hasNext())
      {
        MAttribute o = (MAttribute)iter2.next();
        o.internalRefByAssociationEndRole(this);
      }
      _availableQualifier = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_availableQualifier_setMethod, old, getAvailableQualifiers());
      }
      if (sendEvent)
      {
        fireBagSet("availableQualifier", old, getAvailableQualifiers());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addAvailableQualifier(MAttribute __arg)
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
        old = getAvailableQualifiers();
      }
      if (null != _availableQualifier_ucopy)
      {
        _availableQualifier = new ArrayList(_availableQualifier);
        _availableQualifier_ucopy = null;
      }
      __arg.internalRefByAssociationEndRole(this);
      _availableQualifier.add(__arg);
      logBagAdd(_availableQualifier_addMethod, _availableQualifier_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("availableQualifier", old, getAvailableQualifiers(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeAvailableQualifier(MAttribute __arg)
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
        old = getAvailableQualifiers();
      }
      if (null != _availableQualifier_ucopy)
      {
        _availableQualifier = new ArrayList(_availableQualifier);
        _availableQualifier_ucopy = null;
      }
      if (!_availableQualifier.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByAssociationEndRole(this);
      logBagRemove(_availableQualifier_removeMethod, _availableQualifier_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("availableQualifier", old, getAvailableQualifiers(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByAvailableQualifier(MAttribute __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getAvailableQualifiers();
    }
    if (null != _availableQualifier_ucopy)
    {
      _availableQualifier = new ArrayList(_availableQualifier);
      _availableQualifier_ucopy = null;
    }
    _availableQualifier.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("availableQualifier", old, getAvailableQualifiers(), __arg);
    }
  }
  public final void internalUnrefByAvailableQualifier(MAttribute __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getAvailableQualifiers();
    }
    if (null != _availableQualifier_ucopy)
    {
      _availableQualifier = new ArrayList(_availableQualifier);
      _availableQualifier_ucopy = null;
    }
    _availableQualifier.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("availableQualifier", old, getAvailableQualifiers(), __arg);
    }
  }
  // opposite role: base this role: associationEndRole
  private final static Method _base_setMethod = getMethod1(MAssociationEndRoleImpl.class, "setBase", MAssociationEnd.class);
  MAssociationEnd _base;
  public final MAssociationEnd getBase()
  {
    checkExists();
    return _base;
  }
  public final void setBase(MAssociationEnd __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      MAssociationEnd __saved = _base;
      if (_base != __arg)
      {
        if (__saved != null)
        {
          __saved.internalUnrefByAssociationEndRole(this);
        }
        if (__arg != null)
        {
          __arg.internalRefByAssociationEndRole(this);
        }
        logRefSet(_base_setMethod, __saved, __arg);
        fireRefSet("base", __saved, __arg);
        _base = __arg;
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByBase(MAssociationEnd __arg)
  {
    MAssociationEnd __saved = _base;
    if (_base != null)
    {
      _base.removeAssociationEndRole(this);
    }
    fireRefSet("base", __saved, __arg);
    _base = __arg;
  }
  public final void internalUnrefByBase(MAssociationEnd __arg)
  {
    fireRefSet("base", _base, null);
    _base = null;
  }
  protected void cleanup(Collection scheduledForRemove)
  {
    // opposite role: availableQualifier this role: associationEndRole
    if (_availableQualifier.size() != 0)
    {
      setAvailableQualifiers(Collections.EMPTY_LIST);
    }
    // opposite role: base this role: associationEndRole
    if (_base != null )
    {
      setBase(null);
    }
    super.cleanup(scheduledForRemove);
  }

  public String getUMLClassName()
  {
    return "AssociationEndRole";
  }

  // Reflective API

  public Object reflectiveGetValue(String feature)
  {
    if ("availableQualifier".equals(feature))
    {
      return getAvailableQualifiers();
    }
    if ("base".equals(feature))
    {
      return getBase();
    }

    return super.reflectiveGetValue(feature);
  }

  public void reflectiveSetValue(String feature, Object obj)
  {
    if ("availableQualifier".equals(feature))
    {
      setAvailableQualifiers((Collection)obj);
      return;
    }
    if ("base".equals(feature))
    {
      setBase((MAssociationEnd)obj);
      return;
    }

    super.reflectiveSetValue(feature, obj);
  }

  public void reflectiveAddValue(String feature, Object obj)
  {
    if ("availableQualifier".equals(feature))
    {
      addAvailableQualifier((MAttribute)obj);
      return;
    }

    super.reflectiveAddValue(feature, obj);
  }

  public void reflectiveRemoveValue(String feature, Object obj)
  {
    if ("availableQualifier".equals(feature))
    {
      removeAvailableQualifier((MAttribute)obj);
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
