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

public class MOperationImpl extends MBehavioralFeatureImpl implements MOperation
{
  // ------------ code for class Operation -----------------
  // generating attributes
  // attribute: specification
  private final static Method _specification_setMethod = getMethod1(MOperationImpl.class, "setSpecification", String.class);
  String _specification;
  public final String getSpecification()
  {
    checkExists();
    return _specification;
  }
  public final void setSpecification(String __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      logAttrSet(_specification_setMethod, _specification, __arg);
      fireAttrSet("specification", _specification, __arg);
      _specification = __arg;
    }
    finally
    {
      operationFinished();
    }
  }
  // attribute: isAbstract
  private final static Method _isAbstract_setMethod = getMethod1(MOperationImpl.class, "setAbstract", boolean.class);
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
  private final static Method _isLeaf_setMethod = getMethod1(MOperationImpl.class, "setLeaf", boolean.class);
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
  private final static Method _isRoot_setMethod = getMethod1(MOperationImpl.class, "setRoot", boolean.class);
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
  // attribute: concurrency
  private final static Method _concurrency_setMethod = getMethod1(MOperationImpl.class, "setConcurrency", MCallConcurrencyKind.class);
  MCallConcurrencyKind _concurrency;
  public final MCallConcurrencyKind getConcurrency()
  {
    checkExists();
    return _concurrency;
  }
  public final void setConcurrency(MCallConcurrencyKind __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      logAttrSet(_concurrency_setMethod, _concurrency, __arg);
      fireAttrSet("concurrency", _concurrency, __arg);
      _concurrency = __arg;
    }
    finally
    {
      operationFinished();
    }
  }
  // generating associations
  // opposite role: callAction this role: operation
  private final static Method _callAction_setMethod = getMethod1(MOperationImpl.class, "setCallActions", Collection.class);
  private final static Method _callAction_addMethod = getMethod1(MOperationImpl.class, "addCallAction", MCallAction.class);
  private final static Method _callAction_removeMethod = getMethod1(MOperationImpl.class, "removeCallAction", MCallAction.class);
  Collection _callAction = Collections.EMPTY_LIST;
  Collection _callAction_ucopy = Collections.EMPTY_LIST;
  public final Collection getCallActions()
  {
    checkExists();
    if (null == _callAction_ucopy)
    {
      _callAction_ucopy = ucopy(_callAction);
    }
    return _callAction_ucopy;
  }
  public final void setCallActions(Collection __arg)
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
        old = getCallActions();
      }
      _callAction_ucopy = null;
      Collection __added = bagdiff(__arg,_callAction);
      Collection __removed = bagdiff(_callAction, __arg);
      Iterator iter1 = __removed.iterator();
      while (iter1.hasNext())
      {
        MCallAction o = (MCallAction)iter1.next();
        o.internalUnrefByOperation(this);
      }
      Iterator iter2 = __added.iterator();
      while (iter2.hasNext())
      {
        MCallAction o = (MCallAction)iter2.next();
        o.internalRefByOperation(this);
      }
      _callAction = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_callAction_setMethod, old, getCallActions());
      }
      if (sendEvent)
      {
        fireBagSet("callAction", old, getCallActions());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addCallAction(MCallAction __arg)
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
        old = getCallActions();
      }
      if (null != _callAction_ucopy)
      {
        _callAction = new ArrayList(_callAction);
        _callAction_ucopy = null;
      }
      __arg.internalRefByOperation(this);
      _callAction.add(__arg);
      logBagAdd(_callAction_addMethod, _callAction_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("callAction", old, getCallActions(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeCallAction(MCallAction __arg)
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
        old = getCallActions();
      }
      if (null != _callAction_ucopy)
      {
        _callAction = new ArrayList(_callAction);
        _callAction_ucopy = null;
      }
      if (!_callAction.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByOperation(this);
      logBagRemove(_callAction_removeMethod, _callAction_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("callAction", old, getCallActions(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByCallAction(MCallAction __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getCallActions();
    }
    if (null != _callAction_ucopy)
    {
      _callAction = new ArrayList(_callAction);
      _callAction_ucopy = null;
    }
    _callAction.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("callAction", old, getCallActions(), __arg);
    }
  }
  public final void internalUnrefByCallAction(MCallAction __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getCallActions();
    }
    if (null != _callAction_ucopy)
    {
      _callAction = new ArrayList(_callAction);
      _callAction_ucopy = null;
    }
    _callAction.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("callAction", old, getCallActions(), __arg);
    }
  }
  // opposite role: collaboration this role: representedOperation
  private final static Method _collaboration_setMethod = getMethod1(MOperationImpl.class, "setCollaborations", Collection.class);
  private final static Method _collaboration_addMethod = getMethod1(MOperationImpl.class, "addCollaboration", MCollaboration.class);
  private final static Method _collaboration_removeMethod = getMethod1(MOperationImpl.class, "removeCollaboration", MCollaboration.class);
  Collection _collaboration = Collections.EMPTY_LIST;
  Collection _collaboration_ucopy = Collections.EMPTY_LIST;
  public final Collection getCollaborations()
  {
    checkExists();
    if (null == _collaboration_ucopy)
    {
      _collaboration_ucopy = ucopy(_collaboration);
    }
    return _collaboration_ucopy;
  }
  public final void setCollaborations(Collection __arg)
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
        old = getCollaborations();
      }
      _collaboration_ucopy = null;
      Collection __added = bagdiff(__arg,_collaboration);
      Collection __removed = bagdiff(_collaboration, __arg);
      Iterator iter3 = __removed.iterator();
      while (iter3.hasNext())
      {
        MCollaboration o = (MCollaboration)iter3.next();
        o.internalUnrefByRepresentedOperation(this);
      }
      Iterator iter4 = __added.iterator();
      while (iter4.hasNext())
      {
        MCollaboration o = (MCollaboration)iter4.next();
        o.internalRefByRepresentedOperation(this);
      }
      _collaboration = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_collaboration_setMethod, old, getCollaborations());
      }
      if (sendEvent)
      {
        fireBagSet("collaboration", old, getCollaborations());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addCollaboration(MCollaboration __arg)
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
        old = getCollaborations();
      }
      if (null != _collaboration_ucopy)
      {
        _collaboration = new ArrayList(_collaboration);
        _collaboration_ucopy = null;
      }
      __arg.internalRefByRepresentedOperation(this);
      _collaboration.add(__arg);
      logBagAdd(_collaboration_addMethod, _collaboration_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("collaboration", old, getCollaborations(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeCollaboration(MCollaboration __arg)
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
        old = getCollaborations();
      }
      if (null != _collaboration_ucopy)
      {
        _collaboration = new ArrayList(_collaboration);
        _collaboration_ucopy = null;
      }
      if (!_collaboration.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByRepresentedOperation(this);
      logBagRemove(_collaboration_removeMethod, _collaboration_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("collaboration", old, getCollaborations(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByCollaboration(MCollaboration __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getCollaborations();
    }
    if (null != _collaboration_ucopy)
    {
      _collaboration = new ArrayList(_collaboration);
      _collaboration_ucopy = null;
    }
    _collaboration.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("collaboration", old, getCollaborations(), __arg);
    }
  }
  public final void internalUnrefByCollaboration(MCollaboration __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getCollaborations();
    }
    if (null != _collaboration_ucopy)
    {
      _collaboration = new ArrayList(_collaboration);
      _collaboration_ucopy = null;
    }
    _collaboration.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("collaboration", old, getCollaborations(), __arg);
    }
  }
  // opposite role: occurrence this role: operation
  private final static Method _occurrence_setMethod = getMethod1(MOperationImpl.class, "setOccurrences", Collection.class);
  private final static Method _occurrence_addMethod = getMethod1(MOperationImpl.class, "addOccurrence", MCallEvent.class);
  private final static Method _occurrence_removeMethod = getMethod1(MOperationImpl.class, "removeOccurrence", MCallEvent.class);
  Collection _occurrence = Collections.EMPTY_LIST;
  Collection _occurrence_ucopy = Collections.EMPTY_LIST;
  public final Collection getOccurrences()
  {
    checkExists();
    if (null == _occurrence_ucopy)
    {
      _occurrence_ucopy = ucopy(_occurrence);
    }
    return _occurrence_ucopy;
  }
  public final void setOccurrences(Collection __arg)
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
        old = getOccurrences();
      }
      _occurrence_ucopy = null;
      Collection __added = bagdiff(__arg,_occurrence);
      Collection __removed = bagdiff(_occurrence, __arg);
      Iterator iter5 = __removed.iterator();
      while (iter5.hasNext())
      {
        MCallEvent o = (MCallEvent)iter5.next();
        o.internalUnrefByOperation(this);
      }
      Iterator iter6 = __added.iterator();
      while (iter6.hasNext())
      {
        MCallEvent o = (MCallEvent)iter6.next();
        o.internalRefByOperation(this);
      }
      _occurrence = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_occurrence_setMethod, old, getOccurrences());
      }
      if (sendEvent)
      {
        fireBagSet("occurrence", old, getOccurrences());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addOccurrence(MCallEvent __arg)
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
        old = getOccurrences();
      }
      if (null != _occurrence_ucopy)
      {
        _occurrence = new ArrayList(_occurrence);
        _occurrence_ucopy = null;
      }
      __arg.internalRefByOperation(this);
      _occurrence.add(__arg);
      logBagAdd(_occurrence_addMethod, _occurrence_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("occurrence", old, getOccurrences(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeOccurrence(MCallEvent __arg)
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
        old = getOccurrences();
      }
      if (null != _occurrence_ucopy)
      {
        _occurrence = new ArrayList(_occurrence);
        _occurrence_ucopy = null;
      }
      if (!_occurrence.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByOperation(this);
      logBagRemove(_occurrence_removeMethod, _occurrence_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("occurrence", old, getOccurrences(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByOccurrence(MCallEvent __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getOccurrences();
    }
    if (null != _occurrence_ucopy)
    {
      _occurrence = new ArrayList(_occurrence);
      _occurrence_ucopy = null;
    }
    _occurrence.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("occurrence", old, getOccurrences(), __arg);
    }
  }
  public final void internalUnrefByOccurrence(MCallEvent __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getOccurrences();
    }
    if (null != _occurrence_ucopy)
    {
      _occurrence = new ArrayList(_occurrence);
      _occurrence_ucopy = null;
    }
    _occurrence.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("occurrence", old, getOccurrences(), __arg);
    }
  }
  // opposite role: method this role: specification
  private final static Method _method_setMethod = getMethod1(MOperationImpl.class, "setMethods", Collection.class);
  private final static Method _method_addMethod = getMethod1(MOperationImpl.class, "addMethod", MMethod.class);
  private final static Method _method_removeMethod = getMethod1(MOperationImpl.class, "removeMethod", MMethod.class);
  Collection _method = Collections.EMPTY_LIST;
  Collection _method_ucopy = Collections.EMPTY_LIST;
  public final Collection getMethods()
  {
    checkExists();
    if (null == _method_ucopy)
    {
      _method_ucopy = ucopy(_method);
    }
    return _method_ucopy;
  }
  public final void setMethods(Collection __arg)
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
        old = getMethods();
      }
      _method_ucopy = null;
      Collection __added = bagdiff(__arg,_method);
      Collection __removed = bagdiff(_method, __arg);
      Iterator iter7 = __removed.iterator();
      while (iter7.hasNext())
      {
        MMethod o = (MMethod)iter7.next();
        o.internalUnrefBySpecification(this);
      }
      Iterator iter8 = __added.iterator();
      while (iter8.hasNext())
      {
        MMethod o = (MMethod)iter8.next();
        o.internalRefBySpecification(this);
      }
      _method = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_method_setMethod, old, getMethods());
      }
      if (sendEvent)
      {
        fireBagSet("method", old, getMethods());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addMethod(MMethod __arg)
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
        old = getMethods();
      }
      if (null != _method_ucopy)
      {
        _method = new ArrayList(_method);
        _method_ucopy = null;
      }
      __arg.internalRefBySpecification(this);
      _method.add(__arg);
      logBagAdd(_method_addMethod, _method_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("method", old, getMethods(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeMethod(MMethod __arg)
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
        old = getMethods();
      }
      if (null != _method_ucopy)
      {
        _method = new ArrayList(_method);
        _method_ucopy = null;
      }
      if (!_method.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefBySpecification(this);
      logBagRemove(_method_removeMethod, _method_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("method", old, getMethods(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByMethod(MMethod __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getMethods();
    }
    if (null != _method_ucopy)
    {
      _method = new ArrayList(_method);
      _method_ucopy = null;
    }
    _method.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("method", old, getMethods(), __arg);
    }
  }
  public final void internalUnrefByMethod(MMethod __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getMethods();
    }
    if (null != _method_ucopy)
    {
      _method = new ArrayList(_method);
      _method_ucopy = null;
    }
    _method.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("method", old, getMethods(), __arg);
    }
  }
  protected void cleanup(Collection scheduledForRemove)
  {
    // opposite role: callAction this role: operation
    if (_callAction.size() != 0)
    {
      setCallActions(Collections.EMPTY_LIST);
    }
    // opposite role: collaboration this role: representedOperation
    if (_collaboration.size() != 0)
    {
      setCollaborations(Collections.EMPTY_LIST);
    }
    // opposite role: occurrence this role: operation
    if (_occurrence.size() != 0)
    {
      setOccurrences(Collections.EMPTY_LIST);
    }
    // opposite role: method this role: specification
    if (_method.size() != 0)
    {
      setMethods(Collections.EMPTY_LIST);
    }
    super.cleanup(scheduledForRemove);
  }

  public String getUMLClassName()
  {
    return "Operation";
  }

  // Reflective API

  public Object reflectiveGetValue(String feature)
  {
    if ("specification".equals(feature))
    {
      return getSpecification();
    }
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
    if ("concurrency".equals(feature))
    {
      return getConcurrency();
    }
    if ("callAction".equals(feature))
    {
      return getCallActions();
    }
    if ("collaboration".equals(feature))
    {
      return getCollaborations();
    }
    if ("occurrence".equals(feature))
    {
      return getOccurrences();
    }
    if ("method".equals(feature))
    {
      return getMethods();
    }

    return super.reflectiveGetValue(feature);
  }

  public void reflectiveSetValue(String feature, Object obj)
  {
    if ("specification".equals(feature))
    {
      setSpecification((String)obj);
      return;
    }
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
    if ("concurrency".equals(feature))
    {
      setConcurrency((MCallConcurrencyKind)obj);
      return;
    }
    if ("callAction".equals(feature))
    {
      setCallActions((Collection)obj);
      return;
    }
    if ("collaboration".equals(feature))
    {
      setCollaborations((Collection)obj);
      return;
    }
    if ("occurrence".equals(feature))
    {
      setOccurrences((Collection)obj);
      return;
    }
    if ("method".equals(feature))
    {
      setMethods((Collection)obj);
      return;
    }

    super.reflectiveSetValue(feature, obj);
  }

  public void reflectiveAddValue(String feature, Object obj)
  {
    if ("callAction".equals(feature))
    {
      addCallAction((MCallAction)obj);
      return;
    }
    if ("collaboration".equals(feature))
    {
      addCollaboration((MCollaboration)obj);
      return;
    }
    if ("occurrence".equals(feature))
    {
      addOccurrence((MCallEvent)obj);
      return;
    }
    if ("method".equals(feature))
    {
      addMethod((MMethod)obj);
      return;
    }

    super.reflectiveAddValue(feature, obj);
  }

  public void reflectiveRemoveValue(String feature, Object obj)
  {
    if ("callAction".equals(feature))
    {
      removeCallAction((MCallAction)obj);
      return;
    }
    if ("collaboration".equals(feature))
    {
      removeCollaboration((MCollaboration)obj);
      return;
    }
    if ("occurrence".equals(feature))
    {
      removeOccurrence((MCallEvent)obj);
      return;
    }
    if ("method".equals(feature))
    {
      removeMethod((MMethod)obj);
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
