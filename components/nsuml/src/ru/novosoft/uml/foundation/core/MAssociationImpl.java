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

public class MAssociationImpl extends MRelationshipImpl implements MAssociation
{
  // ------------ code for class GeneralizableElement -----------------
  // generating attributes
  // attribute: isAbstract
  private final static Method _isAbstract_setMethod = getMethod1(MAssociationImpl.class, "setAbstract", boolean.class);
  boolean _isAbstract;
  public final boolean isAbstract()
  {
    checkExists();
    return _isAbstract;
  }
  public final void setAbstract(boolean __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      logAttrSet(_isAbstract_setMethod, _isAbstract, __arg);
      fireAttrSet("isAbstract", _isAbstract, __arg);
      _isAbstract = __arg;
    }
    finally
    {
      operationFinished();
    }
  }
  // attribute: isLeaf
  private final static Method _isLeaf_setMethod = getMethod1(MAssociationImpl.class, "setLeaf", boolean.class);
  boolean _isLeaf;
  public final boolean isLeaf()
  {
    checkExists();
    return _isLeaf;
  }
  public final void setLeaf(boolean __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      logAttrSet(_isLeaf_setMethod, _isLeaf, __arg);
      fireAttrSet("isLeaf", _isLeaf, __arg);
      _isLeaf = __arg;
    }
    finally
    {
      operationFinished();
    }
  }
  // attribute: isRoot
  private final static Method _isRoot_setMethod = getMethod1(MAssociationImpl.class, "setRoot", boolean.class);
  boolean _isRoot;
  public final boolean isRoot()
  {
    checkExists();
    return _isRoot;
  }
  public final void setRoot(boolean __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      logAttrSet(_isRoot_setMethod, _isRoot, __arg);
      fireAttrSet("isRoot", _isRoot, __arg);
      _isRoot = __arg;
    }
    finally
    {
      operationFinished();
    }
  }
  // generating associations
  // opposite role: specialization this role: parent
  private final static Method _specialization_setMethod = getMethod1(MAssociationImpl.class, "setSpecializations", Collection.class);
  private final static Method _specialization_addMethod = getMethod1(MAssociationImpl.class, "addSpecialization", MGeneralization.class);
  private final static Method _specialization_removeMethod = getMethod1(MAssociationImpl.class, "removeSpecialization", MGeneralization.class);
  Collection _specialization = Collections.EMPTY_LIST;
  Collection _specialization_ucopy = Collections.EMPTY_LIST;
  public final Collection getSpecializations()
  {
    checkExists();
    if (null == _specialization_ucopy)
    {
      _specialization_ucopy = ucopy(_specialization);
    }
    return _specialization_ucopy;
  }
  public final void setSpecializations(Collection __arg)
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
        old = getSpecializations();
      }
      _specialization_ucopy = null;
      Collection __added = bagdiff(__arg,_specialization);
      Collection __removed = bagdiff(_specialization, __arg);
      Iterator iter1 = __removed.iterator();
      while (iter1.hasNext())
      {
        MGeneralization o = (MGeneralization)iter1.next();
        o.internalUnrefByParent(this);
      }
      Iterator iter2 = __added.iterator();
      while (iter2.hasNext())
      {
        MGeneralization o = (MGeneralization)iter2.next();
        o.internalRefByParent(this);
      }
      _specialization = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_specialization_setMethod, old, getSpecializations());
      }
      if (sendEvent)
      {
        fireBagSet("specialization", old, getSpecializations());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addSpecialization(MGeneralization __arg)
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
        old = getSpecializations();
      }
      if (null != _specialization_ucopy)
      {
        _specialization = new ArrayList(_specialization);
        _specialization_ucopy = null;
      }
      __arg.internalRefByParent(this);
      _specialization.add(__arg);
      logBagAdd(_specialization_addMethod, _specialization_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("specialization", old, getSpecializations(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeSpecialization(MGeneralization __arg)
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
        old = getSpecializations();
      }
      if (null != _specialization_ucopy)
      {
        _specialization = new ArrayList(_specialization);
        _specialization_ucopy = null;
      }
      if (!_specialization.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByParent(this);
      logBagRemove(_specialization_removeMethod, _specialization_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("specialization", old, getSpecializations(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefBySpecialization(MGeneralization __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getSpecializations();
    }
    if (null != _specialization_ucopy)
    {
      _specialization = new ArrayList(_specialization);
      _specialization_ucopy = null;
    }
    _specialization.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("specialization", old, getSpecializations(), __arg);
    }
  }
  public final void internalUnrefBySpecialization(MGeneralization __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getSpecializations();
    }
    if (null != _specialization_ucopy)
    {
      _specialization = new ArrayList(_specialization);
      _specialization_ucopy = null;
    }
    _specialization.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("specialization", old, getSpecializations(), __arg);
    }
  }
  // opposite role: generalization this role: child
  private final static Method _generalization_setMethod = getMethod1(MAssociationImpl.class, "setGeneralizations", Collection.class);
  private final static Method _generalization_addMethod = getMethod1(MAssociationImpl.class, "addGeneralization", MGeneralization.class);
  private final static Method _generalization_removeMethod = getMethod1(MAssociationImpl.class, "removeGeneralization", MGeneralization.class);
  Collection _generalization = Collections.EMPTY_LIST;
  Collection _generalization_ucopy = Collections.EMPTY_LIST;
  public final Collection getGeneralizations()
  {
    checkExists();
    if (null == _generalization_ucopy)
    {
      _generalization_ucopy = ucopy(_generalization);
    }
    return _generalization_ucopy;
  }
  public final void setGeneralizations(Collection __arg)
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
        old = getGeneralizations();
      }
      _generalization_ucopy = null;
      Collection __added = bagdiff(__arg,_generalization);
      Collection __removed = bagdiff(_generalization, __arg);
      Iterator iter3 = __removed.iterator();
      while (iter3.hasNext())
      {
        MGeneralization o = (MGeneralization)iter3.next();
        o.internalUnrefByChild(this);
      }
      Iterator iter4 = __added.iterator();
      while (iter4.hasNext())
      {
        MGeneralization o = (MGeneralization)iter4.next();
        o.internalRefByChild(this);
      }
      _generalization = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_generalization_setMethod, old, getGeneralizations());
      }
      if (sendEvent)
      {
        fireBagSet("generalization", old, getGeneralizations());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addGeneralization(MGeneralization __arg)
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
        old = getGeneralizations();
      }
      if (null != _generalization_ucopy)
      {
        _generalization = new ArrayList(_generalization);
        _generalization_ucopy = null;
      }
      __arg.internalRefByChild(this);
      _generalization.add(__arg);
      logBagAdd(_generalization_addMethod, _generalization_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("generalization", old, getGeneralizations(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeGeneralization(MGeneralization __arg)
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
        old = getGeneralizations();
      }
      if (null != _generalization_ucopy)
      {
        _generalization = new ArrayList(_generalization);
        _generalization_ucopy = null;
      }
      if (!_generalization.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByChild(this);
      logBagRemove(_generalization_removeMethod, _generalization_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("generalization", old, getGeneralizations(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByGeneralization(MGeneralization __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getGeneralizations();
    }
    if (null != _generalization_ucopy)
    {
      _generalization = new ArrayList(_generalization);
      _generalization_ucopy = null;
    }
    _generalization.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("generalization", old, getGeneralizations(), __arg);
    }
  }
  public final void internalUnrefByGeneralization(MGeneralization __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getGeneralizations();
    }
    if (null != _generalization_ucopy)
    {
      _generalization = new ArrayList(_generalization);
      _generalization_ucopy = null;
    }
    _generalization.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("generalization", old, getGeneralizations(), __arg);
    }
  }
  // ------------ code for class Association -----------------
  // generating attributes
  // generating associations
  // opposite role: link this role: association
  private final static Method _link_setMethod = getMethod1(MAssociationImpl.class, "setLinks", Collection.class);
  private final static Method _link_addMethod = getMethod1(MAssociationImpl.class, "addLink", MLink.class);
  private final static Method _link_removeMethod = getMethod1(MAssociationImpl.class, "removeLink", MLink.class);
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
      Iterator iter5 = __removed.iterator();
      while (iter5.hasNext())
      {
        MLink o = (MLink)iter5.next();
        o.internalUnrefByAssociation(this);
      }
      Iterator iter6 = __added.iterator();
      while (iter6.hasNext())
      {
        MLink o = (MLink)iter6.next();
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
  private final static Method _associationRole_setMethod = getMethod1(MAssociationImpl.class, "setAssociationRoles", Collection.class);
  private final static Method _associationRole_addMethod = getMethod1(MAssociationImpl.class, "addAssociationRole", MAssociationRole.class);
  private final static Method _associationRole_removeMethod = getMethod1(MAssociationImpl.class, "removeAssociationRole", MAssociationRole.class);
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
      Iterator iter7 = __removed.iterator();
      while (iter7.hasNext())
      {
        MAssociationRole o = (MAssociationRole)iter7.next();
        o.internalUnrefByBase(this);
      }
      Iterator iter8 = __added.iterator();
      while (iter8.hasNext())
      {
        MAssociationRole o = (MAssociationRole)iter8.next();
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
  private final static Method _connection_setMethod = getMethod1(MAssociationImpl.class, "setConnections", List.class);
  private final static Method _connection_removeMethod = getMethod1(MAssociationImpl.class, "removeConnection", int.class);
  private final static Method _connection_addMethod = getMethod2(MAssociationImpl.class, "addConnection", int.class, MAssociationEnd.class);
  private final static Method _connection_listSetMethod = getMethod2(MAssociationImpl.class, "setConnection", int.class, MAssociationEnd.class);
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
      Iterator iter9 = __removed.iterator();
      while (iter9.hasNext())
      {
        MAssociationEnd o = (MAssociationEnd)iter9.next();
        o.internalUnrefByAssociation(this);
      }
      Iterator iter10 = __added.iterator();
      while (iter10.hasNext())
      {
        MAssociationEnd o = (MAssociationEnd)iter10.next();
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
    // opposite role: specialization this role: parent
    if (_specialization.size() != 0)
    {
      setSpecializations(Collections.EMPTY_LIST);
    }
    // opposite role: generalization this role: child
    if (_generalization.size() != 0)
    {
      setGeneralizations(Collections.EMPTY_LIST);
    }
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
    return "Association";
  }

  // Reflective API

  public Object reflectiveGetValue(String feature)
  {
    if ("isAbstract".equals(feature))
    {
      return new Boolean(isAbstract());
    }
    if ("isLeaf".equals(feature))
    {
      return new Boolean(isLeaf());
    }
    if ("isRoot".equals(feature))
    {
      return new Boolean(isRoot());
    }
    if ("specialization".equals(feature))
    {
      return getSpecializations();
    }
    if ("generalization".equals(feature))
    {
      return getGeneralizations();
    }
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
    if ("isAbstract".equals(feature))
    {
      setAbstract(((Boolean)obj).booleanValue());
      return;
    }
    if ("isLeaf".equals(feature))
    {
      setLeaf(((Boolean)obj).booleanValue());
      return;
    }
    if ("isRoot".equals(feature))
    {
      setRoot(((Boolean)obj).booleanValue());
      return;
    }
    if ("specialization".equals(feature))
    {
      setSpecializations((Collection)obj);
      return;
    }
    if ("generalization".equals(feature))
    {
      setGeneralizations((Collection)obj);
      return;
    }
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
    if ("specialization".equals(feature))
    {
      addSpecialization((MGeneralization)obj);
      return;
    }
    if ("generalization".equals(feature))
    {
      addGeneralization((MGeneralization)obj);
      return;
    }
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
    if ("specialization".equals(feature))
    {
      removeSpecialization((MGeneralization)obj);
      return;
    }
    if ("generalization".equals(feature))
    {
      removeGeneralization((MGeneralization)obj);
      return;
    }
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
  //public boolean isRoot()
  //{
  //  return getGeneralizations().size() == 0;
  //}

  //public boolean isLeaf()
  //{
  //  return getSpecializations().size() == 0;
  //}

  /** get parents */
  public List getChildren()
  {
    List rc = new ArrayList();
    Iterator i = getSpecializations().iterator();
    while(i.hasNext())
    {
      ru.novosoft.uml.foundation.core.MGeneralization g = (ru.novosoft.uml.foundation.core.MGeneralization)i.next();
      rc.add(g.getChild());
    }
    return rc;
  }

  /** get childs */
  public List getParents()
  {
    List rc = new ArrayList();
    Iterator i = getGeneralizations().iterator();
    while(i.hasNext())
    {
      ru.novosoft.uml.foundation.core.MGeneralization g = (ru.novosoft.uml.foundation.core.MGeneralization)i.next();
      rc.add(g.getParent());
    }
    return rc;
  }
}
