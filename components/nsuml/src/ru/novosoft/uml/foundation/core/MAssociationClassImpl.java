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

public class MAssociationClassImpl extends MClassImpl implements MAssociationClass
{
  // ------------ code for class AssociationClass -----------------
  // generating attributes
  // generating associations
  // ------------ code for class Relationship -----------------
  // generating attributes
  // generating associations
  // ------------ code for class Association -----------------
  // generating attributes
  // generating associations
  // opposite role: link this role: association
  private final static Method _link_setMethod = getMethod1(MAssociationClassImpl.class, "setLinks", Collection.class);
  private final static Method _link_addMethod = getMethod1(MAssociationClassImpl.class, "addLink", MLink.class);
  private final static Method _link_removeMethod = getMethod1(MAssociationClassImpl.class, "removeLink", MLink.class);
  Collection _link = Collections.EMPTY_LIST;
  Collection _link_ucopy = Collections.EMPTY_LIST;
  public final Collection getLinks()
  {
    checkExists();
    if (null == _link_ucopy)
    {
      _link_ucopy = ucopy(_link);
    }
    return _link_ucopy;
  }
  public final void setLinks(Collection __arg)
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
        old = getLinks();
      }
      _link_ucopy = null;
      Collection __added = bagdiff(__arg,_link);
      Collection __removed = bagdiff(_link, __arg);
      Iterator iter1 = __removed.iterator();
      while (iter1.hasNext())
      {
        MLink o = (MLink)iter1.next();
        o.internalUnrefByAssociation(this);
      }
      Iterator iter2 = __added.iterator();
      while (iter2.hasNext())
      {
        MLink o = (MLink)iter2.next();
        o.internalRefByAssociation(this);
      }
      _link = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_link_setMethod, old, getLinks());
      }
      if (sendEvent)
      {
        fireBagSet("link", old, getLinks());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addLink(MLink __arg)
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
        old = getLinks();
      }
      if (null != _link_ucopy)
      {
        _link = new ArrayList(_link);
        _link_ucopy = null;
      }
      __arg.internalRefByAssociation(this);
      _link.add(__arg);
      logBagAdd(_link_addMethod, _link_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("link", old, getLinks(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeLink(MLink __arg)
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
        old = getLinks();
      }
      if (null != _link_ucopy)
      {
        _link = new ArrayList(_link);
        _link_ucopy = null;
      }
      if (!_link.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByAssociation(this);
      logBagRemove(_link_removeMethod, _link_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("link", old, getLinks(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByLink(MLink __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getLinks();
    }
    if (null != _link_ucopy)
    {
      _link = new ArrayList(_link);
      _link_ucopy = null;
    }
    _link.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("link", old, getLinks(), __arg);
    }
  }
  public final void internalUnrefByLink(MLink __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getLinks();
    }
    if (null != _link_ucopy)
    {
      _link = new ArrayList(_link);
      _link_ucopy = null;
    }
    _link.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("link", old, getLinks(), __arg);
    }
  }
  // opposite role: associationRole this role: base
  private final static Method _associationRole_setMethod = getMethod1(MAssociationClassImpl.class, "setAssociationRoles", Collection.class);
  private final static Method _associationRole_addMethod = getMethod1(MAssociationClassImpl.class, "addAssociationRole", MAssociationRole.class);
  private final static Method _associationRole_removeMethod = getMethod1(MAssociationClassImpl.class, "removeAssociationRole", MAssociationRole.class);
  Collection _associationRole = Collections.EMPTY_LIST;
  Collection _associationRole_ucopy = Collections.EMPTY_LIST;
  public final Collection getAssociationRoles()
  {
    checkExists();
    if (null == _associationRole_ucopy)
    {
      _associationRole_ucopy = ucopy(_associationRole);
    }
    return _associationRole_ucopy;
  }
  public final void setAssociationRoles(Collection __arg)
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
        old = getAssociationRoles();
      }
      _associationRole_ucopy = null;
      Collection __added = bagdiff(__arg,_associationRole);
      Collection __removed = bagdiff(_associationRole, __arg);
      Iterator iter3 = __removed.iterator();
      while (iter3.hasNext())
      {
        MAssociationRole o = (MAssociationRole)iter3.next();
        o.internalUnrefByBase(this);
      }
      Iterator iter4 = __added.iterator();
      while (iter4.hasNext())
      {
        MAssociationRole o = (MAssociationRole)iter4.next();
        o.internalRefByBase(this);
      }
      _associationRole = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_associationRole_setMethod, old, getAssociationRoles());
      }
      if (sendEvent)
      {
        fireBagSet("associationRole", old, getAssociationRoles());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addAssociationRole(MAssociationRole __arg)
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
        old = getAssociationRoles();
      }
      if (null != _associationRole_ucopy)
      {
        _associationRole = new ArrayList(_associationRole);
        _associationRole_ucopy = null;
      }
      __arg.internalRefByBase(this);
      _associationRole.add(__arg);
      logBagAdd(_associationRole_addMethod, _associationRole_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("associationRole", old, getAssociationRoles(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeAssociationRole(MAssociationRole __arg)
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
        old = getAssociationRoles();
      }
      if (null != _associationRole_ucopy)
      {
        _associationRole = new ArrayList(_associationRole);
        _associationRole_ucopy = null;
      }
      if (!_associationRole.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByBase(this);
      logBagRemove(_associationRole_removeMethod, _associationRole_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("associationRole", old, getAssociationRoles(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByAssociationRole(MAssociationRole __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getAssociationRoles();
    }
    if (null != _associationRole_ucopy)
    {
      _associationRole = new ArrayList(_associationRole);
      _associationRole_ucopy = null;
    }
    _associationRole.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("associationRole", old, getAssociationRoles(), __arg);
    }
  }
  public final void internalUnrefByAssociationRole(MAssociationRole __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getAssociationRoles();
    }
    if (null != _associationRole_ucopy)
    {
      _associationRole = new ArrayList(_associationRole);
      _associationRole_ucopy = null;
    }
    _associationRole.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("associationRole", old, getAssociationRoles(), __arg);
    }
  }
  // opposite role: connection this role: association
  private final static Method _connection_setMethod = getMethod1(MAssociationClassImpl.class, "setConnections", List.class);
  private final static Method _connection_removeMethod = getMethod1(MAssociationClassImpl.class, "removeConnection", int.class);
  private final static Method _connection_addMethod = getMethod2(MAssociationClassImpl.class, "addConnection", int.class, MAssociationEnd.class);
  private final static Method _connection_listSetMethod = getMethod2(MAssociationClassImpl.class, "setConnection", int.class, MAssociationEnd.class);
  List _connection = Collections.EMPTY_LIST;
  List _connection_ucopy = Collections.EMPTY_LIST;
  public final List getConnections()
  {
    checkExists();
    if (null == _connection_ucopy)
    {
      _connection_ucopy = ucopy(_connection);
    }
    return _connection_ucopy;
  }
  public final void setConnections(List __arg)
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
        old = getConnections();
      }
      _connection_ucopy = null;
      Collection __added = bagdiff(__arg,_connection);
      Collection __removed = bagdiff(_connection, __arg);
      Iterator iter5 = __removed.iterator();
      while (iter5.hasNext())
      {
        MAssociationEnd o = (MAssociationEnd)iter5.next();
        o.internalUnrefByAssociation(this);
      }
      Iterator iter6 = __added.iterator();
      while (iter6.hasNext())
      {
        MAssociationEnd o = (MAssociationEnd)iter6.next();
        o.internalRefByAssociation(this);
      }
      _connection = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_connection_setMethod, old, getConnections());
      }
      if (sendEvent)
      {
        fireListSet("connection", old, getConnections());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addConnection(MAssociationEnd __arg)
  {
    addConnection(_connection.size(), __arg);
  }
  public final void removeConnection(MAssociationEnd __arg)
  {
    if (__arg == null)
    {
      throw new NullPointerException();
    }
    int __pos = _connection.indexOf(__arg);
    removeConnection(__pos);
  }
  public final void addConnection(int __pos, MAssociationEnd __arg)
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
        old = getConnections();
      }
      if (null != _connection_ucopy)
      {
        _connection = new ArrayList(_connection);
        _connection_ucopy = null;
      }
      _connection.add(__pos, __arg);
      __arg.internalRefByAssociation(this);
      logListAdd(_connection_addMethod, _connection_removeMethod, __arg, __pos);
      if (sendEvent)
      {
        fireListAdd("connection", old, getConnections(), __arg, __pos);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeConnection(int __pos)
  {
    operationStarted();
    try
    {
      checkExists();
      final boolean sendEvent = needEvent();
      List old = null;
      if (sendEvent)
      {
        old = getConnections();
      }
      if (null != _connection_ucopy)
      {
        _connection = new ArrayList(_connection);
        _connection_ucopy = null;
      }
      MAssociationEnd __arg = (MAssociationEnd)_connection.remove(__pos);
      __arg.internalUnrefByAssociation(this);
      logListRemove(_connection_removeMethod, _connection_addMethod, __arg, __pos);
      if (sendEvent)
      {
        fireListRemove("connection", old, getConnections(), __arg, __pos);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void setConnection(int __pos, MAssociationEnd __arg)
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
        old = getConnections();
      }
      if (null != _connection_ucopy)
      {
        _connection = new ArrayList(_connection);
        _connection_ucopy = null;
      }
      MAssociationEnd __old = (MAssociationEnd)_connection.get(__pos);
      __old.internalUnrefByAssociation(this);
      __arg.internalRefByAssociation(this);
      _connection.set(__pos,__arg);
      logListSet(_connection_listSetMethod, __old, __arg, __pos);
      if (sendEvent)
      {
        fireListItemSet("connection", old, getConnections(), __old, __arg, __pos);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final MAssociationEnd getConnection(int __pos)
  {
    checkExists();
    return (MAssociationEnd)_connection.get(__pos);
  }
  protected void cleanup(Collection scheduledForRemove)
  {
    // opposite role: link this role: association
    if (_link.size() != 0)
    {
      setLinks(Collections.EMPTY_LIST);
    }
    // opposite role: associationRole this role: base
    if (_associationRole.size() != 0)
    {
      setAssociationRoles(Collections.EMPTY_LIST);
    }
    // opposite role: connection this role: association
    if (_connection.size() != 0)
    {
      scheduledForRemove.addAll(_connection);
      setConnections(Collections.EMPTY_LIST);
    }
    super.cleanup(scheduledForRemove);
  }

  public String getUMLClassName()
  {
    return "AssociationClass";
  }

  // Reflective API

  public Object reflectiveGetValue(String feature)
  {
    if ("link".equals(feature))
    {
      return getLinks();
    }
    if ("associationRole".equals(feature))
    {
      return getAssociationRoles();
    }
    if ("connection".equals(feature))
    {
      return getConnections();
    }

    return super.reflectiveGetValue(feature);
  }

  public void reflectiveSetValue(String feature, Object obj)
  {
    if ("link".equals(feature))
    {
      setLinks((Collection)obj);
      return;
    }
    if ("associationRole".equals(feature))
    {
      setAssociationRoles((Collection)obj);
      return;
    }
    if ("connection".equals(feature))
    {
      setConnections((List)obj);
      return;
    }

    super.reflectiveSetValue(feature, obj);
  }

  public void reflectiveAddValue(String feature, Object obj)
  {
    if ("link".equals(feature))
    {
      addLink((MLink)obj);
      return;
    }
    if ("associationRole".equals(feature))
    {
      addAssociationRole((MAssociationRole)obj);
      return;
    }
    if ("connection".equals(feature))
    {
      addConnection((MAssociationEnd)obj);
      return;
    }

    super.reflectiveAddValue(feature, obj);
  }

  public void reflectiveRemoveValue(String feature, Object obj)
  {
    if ("link".equals(feature))
    {
      removeLink((MLink)obj);
      return;
    }
    if ("associationRole".equals(feature))
    {
      removeAssociationRole((MAssociationRole)obj);
      return;
    }
    if ("connection".equals(feature))
    {
      removeConnection((MAssociationEnd)obj);
      return;
    }

    super.reflectiveRemoveValue(feature, obj);
  }

  public Object reflectiveGetValue(String feature, int pos)
  {
    if ("connection".equals(feature))
    {
      return getConnection(pos);
    }

    return super.reflectiveGetValue(feature, pos);
  }

  public void reflectiveSetValue(String feature, int pos, Object obj)
  {
    if ("connection".equals(feature))
    {
      setConnection(pos, (MAssociationEnd)obj);
      return;
    }

    super.reflectiveSetValue(feature, pos, obj);
  }

  public void reflectiveAddValue(String feature, int pos, Object obj)
  {
    if ("connection".equals(feature))
    {
      addConnection(pos, (MAssociationEnd)obj);
      return;
    }

    super.reflectiveAddValue(feature, pos, obj);
  }

  public void reflectiveRemoveValue(String feature, int pos)
  {
    if ("connection".equals(feature))
    {
      removeConnection(pos);
      return;
    }

    super.reflectiveRemoveValue(feature, pos);
  }
  public Collection getModelElementContents()
  {
    Collection ret = super.getModelElementContents();
    ret.addAll(getConnections());
    return ret;
  }
}
