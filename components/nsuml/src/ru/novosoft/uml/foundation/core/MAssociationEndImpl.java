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

public class MAssociationEndImpl extends MModelElementImpl implements MAssociationEnd
{
  // ------------ code for class AssociationEnd -----------------
  // generating attributes
  // attribute: changeability
  private final static Method _changeability_setMethod = getMethod1(MAssociationEndImpl.class, "setChangeability", MChangeableKind.class);
  MChangeableKind _changeability;
  public final MChangeableKind getChangeability()
  {
    checkExists();
    return _changeability;
  }
  public final void setChangeability(MChangeableKind __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      logAttrSet(_changeability_setMethod, _changeability, __arg);
      fireAttrSet("changeability", _changeability, __arg);
      _changeability = __arg;
    }
    finally
    {
      operationFinished();
    }
  }
  // attribute: multiplicity
  private final static Method _multiplicity_setMethod = getMethod1(MAssociationEndImpl.class, "setMultiplicity", MMultiplicity.class);
  MMultiplicity _multiplicity;
  public final MMultiplicity getMultiplicity()
  {
    checkExists();
    return _multiplicity;
  }
  public final void setMultiplicity(MMultiplicity __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      logAttrSet(_multiplicity_setMethod, _multiplicity, __arg);
      fireAttrSet("multiplicity", _multiplicity, __arg);
      _multiplicity = __arg;
    }
    finally
    {
      operationFinished();
    }
  }
  // attribute: targetScope
  private final static Method _targetScope_setMethod = getMethod1(MAssociationEndImpl.class, "setTargetScope", MScopeKind.class);
  MScopeKind _targetScope;
  public final MScopeKind getTargetScope()
  {
    checkExists();
    return _targetScope;
  }
  public final void setTargetScope(MScopeKind __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      logAttrSet(_targetScope_setMethod, _targetScope, __arg);
      fireAttrSet("targetScope", _targetScope, __arg);
      _targetScope = __arg;
    }
    finally
    {
      operationFinished();
    }
  }
  // attribute: aggregation
  private final static Method _aggregation_setMethod = getMethod1(MAssociationEndImpl.class, "setAggregation", MAggregationKind.class);
  MAggregationKind _aggregation;
  public final MAggregationKind getAggregation()
  {
    checkExists();
    return _aggregation;
  }
  public final void setAggregation(MAggregationKind __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      logAttrSet(_aggregation_setMethod, _aggregation, __arg);
      fireAttrSet("aggregation", _aggregation, __arg);
      _aggregation = __arg;
    }
    finally
    {
      operationFinished();
    }
  }
  // attribute: ordering
  private final static Method _ordering_setMethod = getMethod1(MAssociationEndImpl.class, "setOrdering", MOrderingKind.class);
  MOrderingKind _ordering;
  public final MOrderingKind getOrdering()
  {
    checkExists();
    return _ordering;
  }
  public final void setOrdering(MOrderingKind __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      logAttrSet(_ordering_setMethod, _ordering, __arg);
      fireAttrSet("ordering", _ordering, __arg);
      _ordering = __arg;
    }
    finally
    {
      operationFinished();
    }
  }
  // attribute: isNavigable
  private final static Method _isNavigable_setMethod = getMethod1(MAssociationEndImpl.class, "setNavigable", boolean.class);
  boolean _isNavigable;
  public final boolean isNavigable()
  {
    checkExists();
    return _isNavigable;
  }
  public final void setNavigable(boolean __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      logAttrSet(_isNavigable_setMethod, _isNavigable, __arg);
      fireAttrSet("isNavigable", _isNavigable, __arg);
      _isNavigable = __arg;
    }
    finally
    {
      operationFinished();
    }
  }
  // generating associations
  // opposite role: linkEnd this role: associationEnd
  private final static Method _linkEnd_setMethod = getMethod1(MAssociationEndImpl.class, "setLinkEnds", Collection.class);
  private final static Method _linkEnd_addMethod = getMethod1(MAssociationEndImpl.class, "addLinkEnd", MLinkEnd.class);
  private final static Method _linkEnd_removeMethod = getMethod1(MAssociationEndImpl.class, "removeLinkEnd", MLinkEnd.class);
  Collection _linkEnd = Collections.EMPTY_LIST;
  Collection _linkEnd_ucopy = Collections.EMPTY_LIST;
  public final Collection getLinkEnds()
  {
    checkExists();
    if (null == _linkEnd_ucopy)
    {
      _linkEnd_ucopy = ucopy(_linkEnd);
    }
    return _linkEnd_ucopy;
  }
  public final void setLinkEnds(Collection __arg)
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
        old = getLinkEnds();
      }
      _linkEnd_ucopy = null;
      Collection __added = bagdiff(__arg,_linkEnd);
      Collection __removed = bagdiff(_linkEnd, __arg);
      Iterator iter1 = __removed.iterator();
      while (iter1.hasNext())
      {
        MLinkEnd o = (MLinkEnd)iter1.next();
        o.internalUnrefByAssociationEnd(this);
      }
      Iterator iter2 = __added.iterator();
      while (iter2.hasNext())
      {
        MLinkEnd o = (MLinkEnd)iter2.next();
        o.internalRefByAssociationEnd(this);
      }
      _linkEnd = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_linkEnd_setMethod, old, getLinkEnds());
      }
      if (sendEvent)
      {
        fireBagSet("linkEnd", old, getLinkEnds());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addLinkEnd(MLinkEnd __arg)
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
        old = getLinkEnds();
      }
      if (null != _linkEnd_ucopy)
      {
        _linkEnd = new ArrayList(_linkEnd);
        _linkEnd_ucopy = null;
      }
      __arg.internalRefByAssociationEnd(this);
      _linkEnd.add(__arg);
      logBagAdd(_linkEnd_addMethod, _linkEnd_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("linkEnd", old, getLinkEnds(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeLinkEnd(MLinkEnd __arg)
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
        old = getLinkEnds();
      }
      if (null != _linkEnd_ucopy)
      {
        _linkEnd = new ArrayList(_linkEnd);
        _linkEnd_ucopy = null;
      }
      if (!_linkEnd.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByAssociationEnd(this);
      logBagRemove(_linkEnd_removeMethod, _linkEnd_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("linkEnd", old, getLinkEnds(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByLinkEnd(MLinkEnd __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getLinkEnds();
    }
    if (null != _linkEnd_ucopy)
    {
      _linkEnd = new ArrayList(_linkEnd);
      _linkEnd_ucopy = null;
    }
    _linkEnd.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("linkEnd", old, getLinkEnds(), __arg);
    }
  }
  public final void internalUnrefByLinkEnd(MLinkEnd __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getLinkEnds();
    }
    if (null != _linkEnd_ucopy)
    {
      _linkEnd = new ArrayList(_linkEnd);
      _linkEnd_ucopy = null;
    }
    _linkEnd.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("linkEnd", old, getLinkEnds(), __arg);
    }
  }
  // opposite role: associationEndRole this role: base
  private final static Method _associationEndRole_setMethod = getMethod1(MAssociationEndImpl.class, "setAssociationEndRoles", Collection.class);
  private final static Method _associationEndRole_addMethod = getMethod1(MAssociationEndImpl.class, "addAssociationEndRole", MAssociationEndRole.class);
  private final static Method _associationEndRole_removeMethod = getMethod1(MAssociationEndImpl.class, "removeAssociationEndRole", MAssociationEndRole.class);
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
        o.internalUnrefByBase(this);
      }
      Iterator iter4 = __added.iterator();
      while (iter4.hasNext())
      {
        MAssociationEndRole o = (MAssociationEndRole)iter4.next();
        o.internalRefByBase(this);
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
      __arg.internalRefByBase(this);
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
      __arg.internalUnrefByBase(this);
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
  // opposite role: specification this role: participant
  private final static Method _specification_setMethod = getMethod1(MAssociationEndImpl.class, "setSpecifications", Collection.class);
  private final static Method _specification_addMethod = getMethod1(MAssociationEndImpl.class, "addSpecification", MClassifier.class);
  private final static Method _specification_removeMethod = getMethod1(MAssociationEndImpl.class, "removeSpecification", MClassifier.class);
  Collection _specification = Collections.EMPTY_LIST;
  Collection _specification_ucopy = Collections.EMPTY_LIST;
  public final Collection getSpecifications()
  {
    checkExists();
    if (null == _specification_ucopy)
    {
      _specification_ucopy = ucopy(_specification);
    }
    return _specification_ucopy;
  }
  public final void setSpecifications(Collection __arg)
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
        old = getSpecifications();
      }
      _specification_ucopy = null;
      Collection __added = bagdiff(__arg,_specification);
      Collection __removed = bagdiff(_specification, __arg);
      Iterator iter5 = __removed.iterator();
      while (iter5.hasNext())
      {
        MClassifier o = (MClassifier)iter5.next();
        o.internalUnrefByParticipant(this);
      }
      Iterator iter6 = __added.iterator();
      while (iter6.hasNext())
      {
        MClassifier o = (MClassifier)iter6.next();
        o.internalRefByParticipant(this);
      }
      _specification = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_specification_setMethod, old, getSpecifications());
      }
      if (sendEvent)
      {
        fireBagSet("specification", old, getSpecifications());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addSpecification(MClassifier __arg)
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
        old = getSpecifications();
      }
      if (null != _specification_ucopy)
      {
        _specification = new ArrayList(_specification);
        _specification_ucopy = null;
      }
      __arg.internalRefByParticipant(this);
      _specification.add(__arg);
      logBagAdd(_specification_addMethod, _specification_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("specification", old, getSpecifications(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeSpecification(MClassifier __arg)
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
        old = getSpecifications();
      }
      if (null != _specification_ucopy)
      {
        _specification = new ArrayList(_specification);
        _specification_ucopy = null;
      }
      if (!_specification.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByParticipant(this);
      logBagRemove(_specification_removeMethod, _specification_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("specification", old, getSpecifications(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefBySpecification(MClassifier __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getSpecifications();
    }
    if (null != _specification_ucopy)
    {
      _specification = new ArrayList(_specification);
      _specification_ucopy = null;
    }
    _specification.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("specification", old, getSpecifications(), __arg);
    }
  }
  public final void internalUnrefBySpecification(MClassifier __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getSpecifications();
    }
    if (null != _specification_ucopy)
    {
      _specification = new ArrayList(_specification);
      _specification_ucopy = null;
    }
    _specification.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("specification", old, getSpecifications(), __arg);
    }
  }
  // opposite role: type this role: associationEnd
  private final static Method _type_setMethod = getMethod1(MAssociationEndImpl.class, "setType", MClassifier.class);
  MClassifier _type;
  public final MClassifier getType()
  {
    checkExists();
    return _type;
  }
  public final void setType(MClassifier __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      MClassifier __saved = _type;
      if (_type != __arg)
      {
        if (__saved != null)
        {
          __saved.internalUnrefByAssociationEnd(this);
        }
        if (__arg != null)
        {
          __arg.internalRefByAssociationEnd(this);
        }
        logRefSet(_type_setMethod, __saved, __arg);
        fireRefSet("type", __saved, __arg);
        _type = __arg;
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByType(MClassifier __arg)
  {
    MClassifier __saved = _type;
    if (_type != null)
    {
      _type.removeAssociationEnd(this);
    }
    fireRefSet("type", __saved, __arg);
    _type = __arg;
  }
  public final void internalUnrefByType(MClassifier __arg)
  {
    fireRefSet("type", _type, null);
    _type = null;
  }
  // opposite role: qualifier this role: associationEnd
  private final static Method _qualifier_setMethod = getMethod1(MAssociationEndImpl.class, "setQualifiers", List.class);
  private final static Method _qualifier_removeMethod = getMethod1(MAssociationEndImpl.class, "removeQualifier", int.class);
  private final static Method _qualifier_addMethod = getMethod2(MAssociationEndImpl.class, "addQualifier", int.class, MAttribute.class);
  private final static Method _qualifier_listSetMethod = getMethod2(MAssociationEndImpl.class, "setQualifier", int.class, MAttribute.class);
  List _qualifier = Collections.EMPTY_LIST;
  List _qualifier_ucopy = Collections.EMPTY_LIST;
  public final List getQualifiers()
  {
    checkExists();
    if (null == _qualifier_ucopy)
    {
      _qualifier_ucopy = ucopy(_qualifier);
    }
    return _qualifier_ucopy;
  }
  public final void setQualifiers(List __arg)
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
        old = getQualifiers();
      }
      _qualifier_ucopy = null;
      Collection __added = bagdiff(__arg,_qualifier);
      Collection __removed = bagdiff(_qualifier, __arg);
      Iterator iter7 = __removed.iterator();
      while (iter7.hasNext())
      {
        MAttribute o = (MAttribute)iter7.next();
        o.internalUnrefByAssociationEnd(this);
      }
      Iterator iter8 = __added.iterator();
      while (iter8.hasNext())
      {
        MAttribute o = (MAttribute)iter8.next();
        o.internalRefByAssociationEnd(this);
      }
      _qualifier = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_qualifier_setMethod, old, getQualifiers());
      }
      if (sendEvent)
      {
        fireListSet("qualifier", old, getQualifiers());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addQualifier(MAttribute __arg)
  {
    addQualifier(_qualifier.size(), __arg);
  }
  public final void removeQualifier(MAttribute __arg)
  {
    if (__arg == null)
    {
      throw new NullPointerException();
    }
    int __pos = _qualifier.indexOf(__arg);
    removeQualifier(__pos);
  }
  public final void addQualifier(int __pos, MAttribute __arg)
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
        old = getQualifiers();
      }
      if (null != _qualifier_ucopy)
      {
        _qualifier = new ArrayList(_qualifier);
        _qualifier_ucopy = null;
      }
      _qualifier.add(__pos, __arg);
      __arg.internalRefByAssociationEnd(this);
      logListAdd(_qualifier_addMethod, _qualifier_removeMethod, __arg, __pos);
      if (sendEvent)
      {
        fireListAdd("qualifier", old, getQualifiers(), __arg, __pos);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeQualifier(int __pos)
  {
    operationStarted();
    try
    {
      checkExists();
      final boolean sendEvent = needEvent();
      List old = null;
      if (sendEvent)
      {
        old = getQualifiers();
      }
      if (null != _qualifier_ucopy)
      {
        _qualifier = new ArrayList(_qualifier);
        _qualifier_ucopy = null;
      }
      MAttribute __arg = (MAttribute)_qualifier.remove(__pos);
      __arg.internalUnrefByAssociationEnd(this);
      logListRemove(_qualifier_removeMethod, _qualifier_addMethod, __arg, __pos);
      if (sendEvent)
      {
        fireListRemove("qualifier", old, getQualifiers(), __arg, __pos);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void setQualifier(int __pos, MAttribute __arg)
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
        old = getQualifiers();
      }
      if (null != _qualifier_ucopy)
      {
        _qualifier = new ArrayList(_qualifier);
        _qualifier_ucopy = null;
      }
      MAttribute __old = (MAttribute)_qualifier.get(__pos);
      __old.internalUnrefByAssociationEnd(this);
      __arg.internalRefByAssociationEnd(this);
      _qualifier.set(__pos,__arg);
      logListSet(_qualifier_listSetMethod, __old, __arg, __pos);
      if (sendEvent)
      {
        fireListItemSet("qualifier", old, getQualifiers(), __old, __arg, __pos);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final MAttribute getQualifier(int __pos)
  {
    checkExists();
    return (MAttribute)_qualifier.get(__pos);
  }
  // opposite role: association this role: connection
  MAssociation _association;
  public final MAssociation getAssociation()
  {
    checkExists();
    return _association;
  }
  public final void setAssociation(MAssociation __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      if (_association != __arg)
      {
        if (_association != null)
        {
          _association.removeConnection(this);
        }
        if (__arg != null)
        {
          __arg.addConnection(this);
        }
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByAssociation(MAssociation __arg)
  {
    MAssociation __saved = _association;
    if (_association != null)
    {
      _association.removeConnection(this);
    }
    fireRefSet("association", __saved, __arg);
    _association = __arg;
    setModelElementContainer(_association, "association");
  }
  public final void internalUnrefByAssociation(MAssociation __arg)
  {
    fireRefSet("association", _association, null);
    _association = null;
    setModelElementContainer(null, null);
  }
  protected void cleanup(Collection scheduledForRemove)
  {
    // opposite role: linkEnd this role: associationEnd
    if (_linkEnd.size() != 0)
    {
      setLinkEnds(Collections.EMPTY_LIST);
    }
    // opposite role: associationEndRole this role: base
    if (_associationEndRole.size() != 0)
    {
      setAssociationEndRoles(Collections.EMPTY_LIST);
    }
    // opposite role: specification this role: participant
    if (_specification.size() != 0)
    {
      setSpecifications(Collections.EMPTY_LIST);
    }
    // opposite role: type this role: associationEnd
    if (_type != null )
    {
      setType(null);
    }
    // opposite role: qualifier this role: associationEnd
    if (_qualifier.size() != 0)
    {
      scheduledForRemove.addAll(_qualifier);
      setQualifiers(Collections.EMPTY_LIST);
    }
    // opposite role: association this role: connection
    if (_association != null )
    {
      setAssociation(null);
    }
    super.cleanup(scheduledForRemove);
  }

  public String getUMLClassName()
  {
    return "AssociationEnd";
  }

  // Reflective API

  public Object reflectiveGetValue(String feature)
  {
    if ("changeability".equals(feature))
    {
      return getChangeability();
    }
    if ("multiplicity".equals(feature))
    {
      return getMultiplicity();
    }
    if ("targetScope".equals(feature))
    {
      return getTargetScope();
    }
    if ("aggregation".equals(feature))
    {
      return getAggregation();
    }
    if ("ordering".equals(feature))
    {
      return getOrdering();
    }
    if ("isNavigable".equals(feature))
    {
      return new Boolean(isNavigable());
    }
    if ("linkEnd".equals(feature))
    {
      return getLinkEnds();
    }
    if ("associationEndRole".equals(feature))
    {
      return getAssociationEndRoles();
    }
    if ("specification".equals(feature))
    {
      return getSpecifications();
    }
    if ("type".equals(feature))
    {
      return getType();
    }
    if ("qualifier".equals(feature))
    {
      return getQualifiers();
    }
    if ("association".equals(feature))
    {
      return getAssociation();
    }

    return super.reflectiveGetValue(feature);
  }

  public void reflectiveSetValue(String feature, Object obj)
  {
    if ("changeability".equals(feature))
    {
      setChangeability((MChangeableKind)obj);
      return;
    }
    if ("multiplicity".equals(feature))
    {
      setMultiplicity((MMultiplicity)obj);
      return;
    }
    if ("targetScope".equals(feature))
    {
      setTargetScope((MScopeKind)obj);
      return;
    }
    if ("aggregation".equals(feature))
    {
      setAggregation((MAggregationKind)obj);
      return;
    }
    if ("ordering".equals(feature))
    {
      setOrdering((MOrderingKind)obj);
      return;
    }
    if ("isNavigable".equals(feature))
    {
      setNavigable(((Boolean)obj).booleanValue());
      return;
    }
    if ("linkEnd".equals(feature))
    {
      setLinkEnds((Collection)obj);
      return;
    }
    if ("associationEndRole".equals(feature))
    {
      setAssociationEndRoles((Collection)obj);
      return;
    }
    if ("specification".equals(feature))
    {
      setSpecifications((Collection)obj);
      return;
    }
    if ("type".equals(feature))
    {
      setType((MClassifier)obj);
      return;
    }
    if ("qualifier".equals(feature))
    {
      setQualifiers((List)obj);
      return;
    }
    if ("association".equals(feature))
    {
      setAssociation((MAssociation)obj);
      return;
    }

    super.reflectiveSetValue(feature, obj);
  }

  public void reflectiveAddValue(String feature, Object obj)
  {
    if ("linkEnd".equals(feature))
    {
      addLinkEnd((MLinkEnd)obj);
      return;
    }
    if ("associationEndRole".equals(feature))
    {
      addAssociationEndRole((MAssociationEndRole)obj);
      return;
    }
    if ("specification".equals(feature))
    {
      addSpecification((MClassifier)obj);
      return;
    }
    if ("qualifier".equals(feature))
    {
      addQualifier((MAttribute)obj);
      return;
    }

    super.reflectiveAddValue(feature, obj);
  }

  public void reflectiveRemoveValue(String feature, Object obj)
  {
    if ("linkEnd".equals(feature))
    {
      removeLinkEnd((MLinkEnd)obj);
      return;
    }
    if ("associationEndRole".equals(feature))
    {
      removeAssociationEndRole((MAssociationEndRole)obj);
      return;
    }
    if ("specification".equals(feature))
    {
      removeSpecification((MClassifier)obj);
      return;
    }
    if ("qualifier".equals(feature))
    {
      removeQualifier((MAttribute)obj);
      return;
    }

    super.reflectiveRemoveValue(feature, obj);
  }

  public Object reflectiveGetValue(String feature, int pos)
  {
    if ("qualifier".equals(feature))
    {
      return getQualifier(pos);
    }

    return super.reflectiveGetValue(feature, pos);
  }

  public void reflectiveSetValue(String feature, int pos, Object obj)
  {
    if ("qualifier".equals(feature))
    {
      setQualifier(pos, (MAttribute)obj);
      return;
    }

    super.reflectiveSetValue(feature, pos, obj);
  }

  public void reflectiveAddValue(String feature, int pos, Object obj)
  {
    if ("qualifier".equals(feature))
    {
      addQualifier(pos, (MAttribute)obj);
      return;
    }

    super.reflectiveAddValue(feature, pos, obj);
  }

  public void reflectiveRemoveValue(String feature, int pos)
  {
    if ("qualifier".equals(feature))
    {
      removeQualifier(pos);
      return;
    }

    super.reflectiveRemoveValue(feature, pos);
  }
  public Collection getModelElementContents()
  {
    Collection ret = super.getModelElementContents();
    ret.addAll(getQualifiers());
    return ret;
  }
  /** get opposite association end */
  public ru.novosoft.uml.foundation.core.MAssociationEnd getOppositeEnd()
  {
    ArrayList aes = new ArrayList(getAssociation().getConnections());
    if(aes.size() != 2)
    {
      throw new IllegalStateException("This method can be only called if association is binary");
    }
    aes.remove(this);
    return (ru.novosoft.uml.foundation.core.MAssociationEnd)aes.get(0);
  }
}
