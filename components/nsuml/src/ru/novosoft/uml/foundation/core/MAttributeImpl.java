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

public class MAttributeImpl extends MStructuralFeatureImpl implements MAttribute
{
  // ------------ code for class Attribute -----------------
  // generating attributes
  // attribute: initialValue
  private final static Method _initialValue_setMethod = getMethod1(MAttributeImpl.class, "setInitialValue", MExpression.class);
  MExpression _initialValue;
  public final MExpression getInitialValue()
  {
    checkExists();
    return _initialValue;
  }
  public final void setInitialValue(MExpression __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      logAttrSet(_initialValue_setMethod, _initialValue, __arg);
      fireAttrSet("initialValue", _initialValue, __arg);
      _initialValue = __arg;
    }
    finally
    {
      operationFinished();
    }
  }
  // generating associations
  // opposite role: attributeLink this role: attribute
  private final static Method _attributeLink_setMethod = getMethod1(MAttributeImpl.class, "setAttributeLinks", Collection.class);
  private final static Method _attributeLink_addMethod = getMethod1(MAttributeImpl.class, "addAttributeLink", MAttributeLink.class);
  private final static Method _attributeLink_removeMethod = getMethod1(MAttributeImpl.class, "removeAttributeLink", MAttributeLink.class);
  Collection _attributeLink = Collections.EMPTY_LIST;
  Collection _attributeLink_ucopy = Collections.EMPTY_LIST;
  public final Collection getAttributeLinks()
  {
    checkExists();
    if (null == _attributeLink_ucopy)
    {
      _attributeLink_ucopy = ucopy(_attributeLink);
    }
    return _attributeLink_ucopy;
  }
  public final void setAttributeLinks(Collection __arg)
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
        old = getAttributeLinks();
      }
      _attributeLink_ucopy = null;
      Collection __added = bagdiff(__arg,_attributeLink);
      Collection __removed = bagdiff(_attributeLink, __arg);
      Iterator iter1 = __removed.iterator();
      while (iter1.hasNext())
      {
        MAttributeLink o = (MAttributeLink)iter1.next();
        o.internalUnrefByAttribute(this);
      }
      Iterator iter2 = __added.iterator();
      while (iter2.hasNext())
      {
        MAttributeLink o = (MAttributeLink)iter2.next();
        o.internalRefByAttribute(this);
      }
      _attributeLink = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_attributeLink_setMethod, old, getAttributeLinks());
      }
      if (sendEvent)
      {
        fireBagSet("attributeLink", old, getAttributeLinks());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addAttributeLink(MAttributeLink __arg)
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
        old = getAttributeLinks();
      }
      if (null != _attributeLink_ucopy)
      {
        _attributeLink = new ArrayList(_attributeLink);
        _attributeLink_ucopy = null;
      }
      __arg.internalRefByAttribute(this);
      _attributeLink.add(__arg);
      logBagAdd(_attributeLink_addMethod, _attributeLink_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("attributeLink", old, getAttributeLinks(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeAttributeLink(MAttributeLink __arg)
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
        old = getAttributeLinks();
      }
      if (null != _attributeLink_ucopy)
      {
        _attributeLink = new ArrayList(_attributeLink);
        _attributeLink_ucopy = null;
      }
      if (!_attributeLink.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByAttribute(this);
      logBagRemove(_attributeLink_removeMethod, _attributeLink_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("attributeLink", old, getAttributeLinks(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByAttributeLink(MAttributeLink __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getAttributeLinks();
    }
    if (null != _attributeLink_ucopy)
    {
      _attributeLink = new ArrayList(_attributeLink);
      _attributeLink_ucopy = null;
    }
    _attributeLink.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("attributeLink", old, getAttributeLinks(), __arg);
    }
  }
  public final void internalUnrefByAttributeLink(MAttributeLink __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getAttributeLinks();
    }
    if (null != _attributeLink_ucopy)
    {
      _attributeLink = new ArrayList(_attributeLink);
      _attributeLink_ucopy = null;
    }
    _attributeLink.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("attributeLink", old, getAttributeLinks(), __arg);
    }
  }
  // opposite role: associationEndRole this role: availableQualifier
  private final static Method _associationEndRole_setMethod = getMethod1(MAttributeImpl.class, "setAssociationEndRoles", Collection.class);
  private final static Method _associationEndRole_addMethod = getMethod1(MAttributeImpl.class, "addAssociationEndRole", MAssociationEndRole.class);
  private final static Method _associationEndRole_removeMethod = getMethod1(MAttributeImpl.class, "removeAssociationEndRole", MAssociationEndRole.class);
  Collection _associationEndRole = Collections.EMPTY_LIST;
  Collection _associationEndRole_ucopy = Collections.EMPTY_LIST;
  public final Collection getAssociationEndRoles()
  {
    checkExists();
    if (null == _associationEndRole_ucopy)
    {
      _associationEndRole_ucopy = ucopy(_associationEndRole);
    }
    return _associationEndRole_ucopy;
  }
  public final void setAssociationEndRoles(Collection __arg)
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
        old = getAssociationEndRoles();
      }
      _associationEndRole_ucopy = null;
      Collection __added = bagdiff(__arg,_associationEndRole);
      Collection __removed = bagdiff(_associationEndRole, __arg);
      Iterator iter3 = __removed.iterator();
      while (iter3.hasNext())
      {
        MAssociationEndRole o = (MAssociationEndRole)iter3.next();
        o.internalUnrefByAvailableQualifier(this);
      }
      Iterator iter4 = __added.iterator();
      while (iter4.hasNext())
      {
        MAssociationEndRole o = (MAssociationEndRole)iter4.next();
        o.internalRefByAvailableQualifier(this);
      }
      _associationEndRole = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_associationEndRole_setMethod, old, getAssociationEndRoles());
      }
      if (sendEvent)
      {
        fireBagSet("associationEndRole", old, getAssociationEndRoles());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addAssociationEndRole(MAssociationEndRole __arg)
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
        old = getAssociationEndRoles();
      }
      if (null != _associationEndRole_ucopy)
      {
        _associationEndRole = new ArrayList(_associationEndRole);
        _associationEndRole_ucopy = null;
      }
      __arg.internalRefByAvailableQualifier(this);
      _associationEndRole.add(__arg);
      logBagAdd(_associationEndRole_addMethod, _associationEndRole_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("associationEndRole", old, getAssociationEndRoles(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeAssociationEndRole(MAssociationEndRole __arg)
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
        old = getAssociationEndRoles();
      }
      if (null != _associationEndRole_ucopy)
      {
        _associationEndRole = new ArrayList(_associationEndRole);
        _associationEndRole_ucopy = null;
      }
      if (!_associationEndRole.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByAvailableQualifier(this);
      logBagRemove(_associationEndRole_removeMethod, _associationEndRole_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("associationEndRole", old, getAssociationEndRoles(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByAssociationEndRole(MAssociationEndRole __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getAssociationEndRoles();
    }
    if (null != _associationEndRole_ucopy)
    {
      _associationEndRole = new ArrayList(_associationEndRole);
      _associationEndRole_ucopy = null;
    }
    _associationEndRole.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("associationEndRole", old, getAssociationEndRoles(), __arg);
    }
  }
  public final void internalUnrefByAssociationEndRole(MAssociationEndRole __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getAssociationEndRoles();
    }
    if (null != _associationEndRole_ucopy)
    {
      _associationEndRole = new ArrayList(_associationEndRole);
      _associationEndRole_ucopy = null;
    }
    _associationEndRole.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("associationEndRole", old, getAssociationEndRoles(), __arg);
    }
  }
  // opposite role: associationEnd this role: qualifier
  MAssociationEnd _associationEnd;
  public final MAssociationEnd getAssociationEnd()
  {
    checkExists();
    return _associationEnd;
  }
  public final void setAssociationEnd(MAssociationEnd __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      if (_associationEnd != __arg)
      {
        if (_associationEnd != null)
        {
          _associationEnd.removeQualifier(this);
        }
        if (__arg != null)
        {
          __arg.addQualifier(this);
        }
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByAssociationEnd(MAssociationEnd __arg)
  {
    MAssociationEnd __saved = _associationEnd;
    if (_associationEnd != null)
    {
      _associationEnd.removeQualifier(this);
    }
    fireRefSet("associationEnd", __saved, __arg);
    _associationEnd = __arg;
    setModelElementContainer(_associationEnd, "associationEnd");
  }
  public final void internalUnrefByAssociationEnd(MAssociationEnd __arg)
  {
    fireRefSet("associationEnd", _associationEnd, null);
    _associationEnd = null;
    setModelElementContainer(null, null);
  }
  protected void cleanup(Collection scheduledForRemove)
  {
    // opposite role: attributeLink this role: attribute
    if (_attributeLink.size() != 0)
    {
      setAttributeLinks(Collections.EMPTY_LIST);
    }
    // opposite role: associationEndRole this role: availableQualifier
    if (_associationEndRole.size() != 0)
    {
      setAssociationEndRoles(Collections.EMPTY_LIST);
    }
    // opposite role: associationEnd this role: qualifier
    if (_associationEnd != null )
    {
      setAssociationEnd(null);
    }
    super.cleanup(scheduledForRemove);
  }

  public String getUMLClassName()
  {
    return "Attribute";
  }

  // Reflective API

  public Object reflectiveGetValue(String feature)
  {
    if ("initialValue".equals(feature))
    {
      return getInitialValue();
    }
    if ("attributeLink".equals(feature))
    {
      return getAttributeLinks();
    }
    if ("associationEndRole".equals(feature))
    {
      return getAssociationEndRoles();
    }
    if ("associationEnd".equals(feature))
    {
      return getAssociationEnd();
    }

    return super.reflectiveGetValue(feature);
  }

  public void reflectiveSetValue(String feature, Object obj)
  {
    if ("initialValue".equals(feature))
    {
      setInitialValue((MExpression)obj);
      return;
    }
    if ("attributeLink".equals(feature))
    {
      setAttributeLinks((Collection)obj);
      return;
    }
    if ("associationEndRole".equals(feature))
    {
      setAssociationEndRoles((Collection)obj);
      return;
    }
    if ("associationEnd".equals(feature))
    {
      setAssociationEnd((MAssociationEnd)obj);
      return;
    }

    super.reflectiveSetValue(feature, obj);
  }

  public void reflectiveAddValue(String feature, Object obj)
  {
    if ("attributeLink".equals(feature))
    {
      addAttributeLink((MAttributeLink)obj);
      return;
    }
    if ("associationEndRole".equals(feature))
    {
      addAssociationEndRole((MAssociationEndRole)obj);
      return;
    }

    super.reflectiveAddValue(feature, obj);
  }

  public void reflectiveRemoveValue(String feature, Object obj)
  {
    if ("attributeLink".equals(feature))
    {
      removeAttributeLink((MAttributeLink)obj);
      return;
    }
    if ("associationEndRole".equals(feature))
    {
      removeAssociationEndRole((MAssociationEndRole)obj);
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
