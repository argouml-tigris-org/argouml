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

public abstract class MModelElementImpl extends MElementImpl implements MModelElement
{
  // ------------ code for class ModelElement -----------------
  // generating attributes
  // attribute: isSpecification
  private final static Method _isSpecification_setMethod = getMethod1(MModelElementImpl.class, "setSpecification", boolean.class);
  boolean _isSpecification;
  public final boolean isSpecification()
  {
    checkExists();
    return _isSpecification;
  }
  public final void setSpecification(boolean __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      logAttrSet(_isSpecification_setMethod, _isSpecification, __arg);
      fireAttrSet("isSpecification", _isSpecification, __arg);
      _isSpecification = __arg;
    }
    finally
    {
      operationFinished();
    }
  }
  // attribute: visibility
  private final static Method _visibility_setMethod = getMethod1(MModelElementImpl.class, "setVisibility", MVisibilityKind.class);
  MVisibilityKind _visibility;
  public final MVisibilityKind getVisibility()
  {
    checkExists();
    return _visibility;
  }
  public final void setVisibility(MVisibilityKind __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      logAttrSet(_visibility_setMethod, _visibility, __arg);
      fireAttrSet("visibility", _visibility, __arg);
      _visibility = __arg;
    }
    finally
    {
      operationFinished();
    }
  }
  // attribute: name
  private final static Method _name_setMethod = getMethod1(MModelElementImpl.class, "setName", String.class);
  String _name;
  public final String getName()
  {
    checkExists();
    return _name;
  }
  public final void setName(String __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      logAttrSet(_name_setMethod, _name, __arg);
      fireAttrSet("name", _name, __arg);
      _name = __arg;
    }
    finally
    {
      operationFinished();
    }
  }
  // generating associations
  // opposite role: elementImport2 this role: modelElement
  private final static Method _elementImport2_setMethod = getMethod1(MModelElementImpl.class, "setElementImports2", Collection.class);
  private final static Method _elementImport2_addMethod = getMethod1(MModelElementImpl.class, "addElementImport2", MElementImport.class);
  private final static Method _elementImport2_removeMethod = getMethod1(MModelElementImpl.class, "removeElementImport2", MElementImport.class);
  Collection _elementImport2 = Collections.EMPTY_LIST;
  Collection _elementImport2_ucopy = Collections.EMPTY_LIST;
  public final Collection getElementImports2()
  {
    checkExists();
    if (null == _elementImport2_ucopy)
    {
      _elementImport2_ucopy = ucopy(_elementImport2);
    }
    return _elementImport2_ucopy;
  }
  public final void setElementImports2(Collection __arg)
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
        old = getElementImports2();
      }
      _elementImport2_ucopy = null;
      Collection __added = bagdiff(__arg,_elementImport2);
      Collection __removed = bagdiff(_elementImport2, __arg);
      Iterator iter1 = __removed.iterator();
      while (iter1.hasNext())
      {
        MElementImport o = (MElementImport)iter1.next();
        o.internalUnrefByModelElement(this);
      }
      Iterator iter2 = __added.iterator();
      while (iter2.hasNext())
      {
        MElementImport o = (MElementImport)iter2.next();
        o.internalRefByModelElement(this);
      }
      _elementImport2 = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_elementImport2_setMethod, old, getElementImports2());
      }
      if (sendEvent)
      {
        fireBagSet("elementImport2", old, getElementImports2());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addElementImport2(MElementImport __arg)
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
        old = getElementImports2();
      }
      if (null != _elementImport2_ucopy)
      {
        _elementImport2 = new ArrayList(_elementImport2);
        _elementImport2_ucopy = null;
      }
      __arg.internalRefByModelElement(this);
      _elementImport2.add(__arg);
      logBagAdd(_elementImport2_addMethod, _elementImport2_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("elementImport2", old, getElementImports2(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeElementImport2(MElementImport __arg)
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
        old = getElementImports2();
      }
      if (null != _elementImport2_ucopy)
      {
        _elementImport2 = new ArrayList(_elementImport2);
        _elementImport2_ucopy = null;
      }
      if (!_elementImport2.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByModelElement(this);
      logBagRemove(_elementImport2_removeMethod, _elementImport2_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("elementImport2", old, getElementImports2(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByElementImport2(MElementImport __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getElementImports2();
    }
    if (null != _elementImport2_ucopy)
    {
      _elementImport2 = new ArrayList(_elementImport2);
      _elementImport2_ucopy = null;
    }
    _elementImport2.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("elementImport2", old, getElementImports2(), __arg);
    }
  }
  public final void internalUnrefByElementImport2(MElementImport __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getElementImports2();
    }
    if (null != _elementImport2_ucopy)
    {
      _elementImport2 = new ArrayList(_elementImport2);
      _elementImport2_ucopy = null;
    }
    _elementImport2.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("elementImport2", old, getElementImports2(), __arg);
    }
  }
  // opposite role: classifierRole1 this role: availableContents
  private final static Method _classifierRole1_setMethod = getMethod1(MModelElementImpl.class, "setClassifierRoles1", Collection.class);
  private final static Method _classifierRole1_addMethod = getMethod1(MModelElementImpl.class, "addClassifierRole1", MClassifierRole.class);
  private final static Method _classifierRole1_removeMethod = getMethod1(MModelElementImpl.class, "removeClassifierRole1", MClassifierRole.class);
  Collection _classifierRole1 = Collections.EMPTY_LIST;
  Collection _classifierRole1_ucopy = Collections.EMPTY_LIST;
  public final Collection getClassifierRoles1()
  {
    checkExists();
    if (null == _classifierRole1_ucopy)
    {
      _classifierRole1_ucopy = ucopy(_classifierRole1);
    }
    return _classifierRole1_ucopy;
  }
  public final void setClassifierRoles1(Collection __arg)
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
        old = getClassifierRoles1();
      }
      _classifierRole1_ucopy = null;
      Collection __added = bagdiff(__arg,_classifierRole1);
      Collection __removed = bagdiff(_classifierRole1, __arg);
      Iterator iter3 = __removed.iterator();
      while (iter3.hasNext())
      {
        MClassifierRole o = (MClassifierRole)iter3.next();
        o.internalUnrefByAvailableContents(this);
      }
      Iterator iter4 = __added.iterator();
      while (iter4.hasNext())
      {
        MClassifierRole o = (MClassifierRole)iter4.next();
        o.internalRefByAvailableContents(this);
      }
      _classifierRole1 = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_classifierRole1_setMethod, old, getClassifierRoles1());
      }
      if (sendEvent)
      {
        fireBagSet("classifierRole1", old, getClassifierRoles1());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addClassifierRole1(MClassifierRole __arg)
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
        old = getClassifierRoles1();
      }
      if (null != _classifierRole1_ucopy)
      {
        _classifierRole1 = new ArrayList(_classifierRole1);
        _classifierRole1_ucopy = null;
      }
      __arg.internalRefByAvailableContents(this);
      _classifierRole1.add(__arg);
      logBagAdd(_classifierRole1_addMethod, _classifierRole1_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("classifierRole1", old, getClassifierRoles1(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeClassifierRole1(MClassifierRole __arg)
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
        old = getClassifierRoles1();
      }
      if (null != _classifierRole1_ucopy)
      {
        _classifierRole1 = new ArrayList(_classifierRole1);
        _classifierRole1_ucopy = null;
      }
      if (!_classifierRole1.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByAvailableContents(this);
      logBagRemove(_classifierRole1_removeMethod, _classifierRole1_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("classifierRole1", old, getClassifierRoles1(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByClassifierRole1(MClassifierRole __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getClassifierRoles1();
    }
    if (null != _classifierRole1_ucopy)
    {
      _classifierRole1 = new ArrayList(_classifierRole1);
      _classifierRole1_ucopy = null;
    }
    _classifierRole1.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("classifierRole1", old, getClassifierRoles1(), __arg);
    }
  }
  public final void internalUnrefByClassifierRole1(MClassifierRole __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getClassifierRoles1();
    }
    if (null != _classifierRole1_ucopy)
    {
      _classifierRole1 = new ArrayList(_classifierRole1);
      _classifierRole1_ucopy = null;
    }
    _classifierRole1.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("classifierRole1", old, getClassifierRoles1(), __arg);
    }
  }
  // opposite role: collaboration1 this role: constrainingElement
  private final static Method _collaboration1_setMethod = getMethod1(MModelElementImpl.class, "setCollaborations1", Collection.class);
  private final static Method _collaboration1_addMethod = getMethod1(MModelElementImpl.class, "addCollaboration1", MCollaboration.class);
  private final static Method _collaboration1_removeMethod = getMethod1(MModelElementImpl.class, "removeCollaboration1", MCollaboration.class);
  Collection _collaboration1 = Collections.EMPTY_LIST;
  Collection _collaboration1_ucopy = Collections.EMPTY_LIST;
  public final Collection getCollaborations1()
  {
    checkExists();
    if (null == _collaboration1_ucopy)
    {
      _collaboration1_ucopy = ucopy(_collaboration1);
    }
    return _collaboration1_ucopy;
  }
  public final void setCollaborations1(Collection __arg)
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
        old = getCollaborations1();
      }
      _collaboration1_ucopy = null;
      Collection __added = bagdiff(__arg,_collaboration1);
      Collection __removed = bagdiff(_collaboration1, __arg);
      Iterator iter5 = __removed.iterator();
      while (iter5.hasNext())
      {
        MCollaboration o = (MCollaboration)iter5.next();
        o.internalUnrefByConstrainingElement(this);
      }
      Iterator iter6 = __added.iterator();
      while (iter6.hasNext())
      {
        MCollaboration o = (MCollaboration)iter6.next();
        o.internalRefByConstrainingElement(this);
      }
      _collaboration1 = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_collaboration1_setMethod, old, getCollaborations1());
      }
      if (sendEvent)
      {
        fireBagSet("collaboration1", old, getCollaborations1());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addCollaboration1(MCollaboration __arg)
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
        old = getCollaborations1();
      }
      if (null != _collaboration1_ucopy)
      {
        _collaboration1 = new ArrayList(_collaboration1);
        _collaboration1_ucopy = null;
      }
      __arg.internalRefByConstrainingElement(this);
      _collaboration1.add(__arg);
      logBagAdd(_collaboration1_addMethod, _collaboration1_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("collaboration1", old, getCollaborations1(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeCollaboration1(MCollaboration __arg)
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
        old = getCollaborations1();
      }
      if (null != _collaboration1_ucopy)
      {
        _collaboration1 = new ArrayList(_collaboration1);
        _collaboration1_ucopy = null;
      }
      if (!_collaboration1.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByConstrainingElement(this);
      logBagRemove(_collaboration1_removeMethod, _collaboration1_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("collaboration1", old, getCollaborations1(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByCollaboration1(MCollaboration __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getCollaborations1();
    }
    if (null != _collaboration1_ucopy)
    {
      _collaboration1 = new ArrayList(_collaboration1);
      _collaboration1_ucopy = null;
    }
    _collaboration1.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("collaboration1", old, getCollaborations1(), __arg);
    }
  }
  public final void internalUnrefByCollaboration1(MCollaboration __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getCollaborations1();
    }
    if (null != _collaboration1_ucopy)
    {
      _collaboration1 = new ArrayList(_collaboration1);
      _collaboration1_ucopy = null;
    }
    _collaboration1.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("collaboration1", old, getCollaborations1(), __arg);
    }
  }
  // opposite role: partition1 this role: contents
  private final static Method _partition1_setMethod = getMethod1(MModelElementImpl.class, "setPartitions1", Collection.class);
  private final static Method _partition1_addMethod = getMethod1(MModelElementImpl.class, "addPartition1", MPartition.class);
  private final static Method _partition1_removeMethod = getMethod1(MModelElementImpl.class, "removePartition1", MPartition.class);
  Collection _partition1 = Collections.EMPTY_LIST;
  Collection _partition1_ucopy = Collections.EMPTY_LIST;
  public final Collection getPartitions1()
  {
    checkExists();
    if (null == _partition1_ucopy)
    {
      _partition1_ucopy = ucopy(_partition1);
    }
    return _partition1_ucopy;
  }
  public final void setPartitions1(Collection __arg)
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
        old = getPartitions1();
      }
      _partition1_ucopy = null;
      Collection __added = bagdiff(__arg,_partition1);
      Collection __removed = bagdiff(_partition1, __arg);
      Iterator iter7 = __removed.iterator();
      while (iter7.hasNext())
      {
        MPartition o = (MPartition)iter7.next();
        o.internalUnrefByContents(this);
      }
      Iterator iter8 = __added.iterator();
      while (iter8.hasNext())
      {
        MPartition o = (MPartition)iter8.next();
        o.internalRefByContents(this);
      }
      _partition1 = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_partition1_setMethod, old, getPartitions1());
      }
      if (sendEvent)
      {
        fireBagSet("partition1", old, getPartitions1());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addPartition1(MPartition __arg)
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
        old = getPartitions1();
      }
      if (null != _partition1_ucopy)
      {
        _partition1 = new ArrayList(_partition1);
        _partition1_ucopy = null;
      }
      __arg.internalRefByContents(this);
      _partition1.add(__arg);
      logBagAdd(_partition1_addMethod, _partition1_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("partition1", old, getPartitions1(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removePartition1(MPartition __arg)
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
        old = getPartitions1();
      }
      if (null != _partition1_ucopy)
      {
        _partition1 = new ArrayList(_partition1);
        _partition1_ucopy = null;
      }
      if (!_partition1.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByContents(this);
      logBagRemove(_partition1_removeMethod, _partition1_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("partition1", old, getPartitions1(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByPartition1(MPartition __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getPartitions1();
    }
    if (null != _partition1_ucopy)
    {
      _partition1 = new ArrayList(_partition1);
      _partition1_ucopy = null;
    }
    _partition1.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("partition1", old, getPartitions1(), __arg);
    }
  }
  public final void internalUnrefByPartition1(MPartition __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getPartitions1();
    }
    if (null != _partition1_ucopy)
    {
      _partition1 = new ArrayList(_partition1);
      _partition1_ucopy = null;
    }
    _partition1.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("partition1", old, getPartitions1(), __arg);
    }
  }
  // opposite role: behavior this role: context
  private final static Method _behavior_setMethod = getMethod1(MModelElementImpl.class, "setBehaviors", Collection.class);
  private final static Method _behavior_addMethod = getMethod1(MModelElementImpl.class, "addBehavior", MStateMachine.class);
  private final static Method _behavior_removeMethod = getMethod1(MModelElementImpl.class, "removeBehavior", MStateMachine.class);
  Collection _behavior = Collections.EMPTY_LIST;
  Collection _behavior_ucopy = Collections.EMPTY_LIST;
  public final Collection getBehaviors()
  {
    checkExists();
    if (null == _behavior_ucopy)
    {
      _behavior_ucopy = ucopy(_behavior);
    }
    return _behavior_ucopy;
  }
  public final void setBehaviors(Collection __arg)
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
        old = getBehaviors();
      }
      _behavior_ucopy = null;
      Collection __added = bagdiff(__arg,_behavior);
      Collection __removed = bagdiff(_behavior, __arg);
      Iterator iter9 = __removed.iterator();
      while (iter9.hasNext())
      {
        MStateMachine o = (MStateMachine)iter9.next();
        o.internalUnrefByContext(this);
      }
      Iterator iter10 = __added.iterator();
      while (iter10.hasNext())
      {
        MStateMachine o = (MStateMachine)iter10.next();
        o.internalRefByContext(this);
      }
      _behavior = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_behavior_setMethod, old, getBehaviors());
      }
      if (sendEvent)
      {
        fireBagSet("behavior", old, getBehaviors());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addBehavior(MStateMachine __arg)
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
        old = getBehaviors();
      }
      if (null != _behavior_ucopy)
      {
        _behavior = new ArrayList(_behavior);
        _behavior_ucopy = null;
      }
      __arg.internalRefByContext(this);
      _behavior.add(__arg);
      logBagAdd(_behavior_addMethod, _behavior_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("behavior", old, getBehaviors(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeBehavior(MStateMachine __arg)
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
        old = getBehaviors();
      }
      if (null != _behavior_ucopy)
      {
        _behavior = new ArrayList(_behavior);
        _behavior_ucopy = null;
      }
      if (!_behavior.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByContext(this);
      logBagRemove(_behavior_removeMethod, _behavior_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("behavior", old, getBehaviors(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByBehavior(MStateMachine __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getBehaviors();
    }
    if (null != _behavior_ucopy)
    {
      _behavior = new ArrayList(_behavior);
      _behavior_ucopy = null;
    }
    _behavior.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("behavior", old, getBehaviors(), __arg);
    }
  }
  public final void internalUnrefByBehavior(MStateMachine __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getBehaviors();
    }
    if (null != _behavior_ucopy)
    {
      _behavior = new ArrayList(_behavior);
      _behavior_ucopy = null;
    }
    _behavior.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("behavior", old, getBehaviors(), __arg);
    }
  }
  // opposite role: stereotype this role: extendedElement
  private final static Method _stereotype_setMethod = getMethod1(MModelElementImpl.class, "setStereotype", MStereotype.class);
  MStereotype _stereotype;
  public final MStereotype getStereotype()
  {
    checkExists();
    return _stereotype;
  }
  public final void setStereotype(MStereotype __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      MStereotype __saved = _stereotype;
      if (_stereotype != __arg)
      {
        if (__saved != null)
        {
          __saved.internalUnrefByExtendedElement(this);
        }
        if (__arg != null)
        {
          __arg.internalRefByExtendedElement(this);
        }
        logRefSet(_stereotype_setMethod, __saved, __arg);
        fireRefSet("stereotype", __saved, __arg);
        _stereotype = __arg;
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByStereotype(MStereotype __arg)
  {
    MStereotype __saved = _stereotype;
    if (_stereotype != null)
    {
      _stereotype.removeExtendedElement(this);
    }
    fireRefSet("stereotype", __saved, __arg);
    _stereotype = __arg;
  }
  public final void internalUnrefByStereotype(MStereotype __arg)
  {
    fireRefSet("stereotype", _stereotype, null);
    _stereotype = null;
  }
  // opposite role: templateParameter2 this role: modelElement2
  private final static Method _templateParameter2_setMethod = getMethod1(MModelElementImpl.class, "setTemplateParameters2", Collection.class);
  private final static Method _templateParameter2_addMethod = getMethod1(MModelElementImpl.class, "addTemplateParameter2", MTemplateParameter.class);
  private final static Method _templateParameter2_removeMethod = getMethod1(MModelElementImpl.class, "removeTemplateParameter2", MTemplateParameter.class);
  Collection _templateParameter2 = Collections.EMPTY_LIST;
  Collection _templateParameter2_ucopy = Collections.EMPTY_LIST;
  public final Collection getTemplateParameters2()
  {
    checkExists();
    if (null == _templateParameter2_ucopy)
    {
      _templateParameter2_ucopy = ucopy(_templateParameter2);
    }
    return _templateParameter2_ucopy;
  }
  public final void setTemplateParameters2(Collection __arg)
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
        old = getTemplateParameters2();
      }
      _templateParameter2_ucopy = null;
      Collection __added = bagdiff(__arg,_templateParameter2);
      Collection __removed = bagdiff(_templateParameter2, __arg);
      Iterator iter11 = __removed.iterator();
      while (iter11.hasNext())
      {
        MTemplateParameter o = (MTemplateParameter)iter11.next();
        o.internalUnrefByModelElement2(this);
      }
      Iterator iter12 = __added.iterator();
      while (iter12.hasNext())
      {
        MTemplateParameter o = (MTemplateParameter)iter12.next();
        o.internalRefByModelElement2(this);
      }
      _templateParameter2 = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_templateParameter2_setMethod, old, getTemplateParameters2());
      }
      if (sendEvent)
      {
        fireBagSet("templateParameter2", old, getTemplateParameters2());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addTemplateParameter2(MTemplateParameter __arg)
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
        old = getTemplateParameters2();
      }
      if (null != _templateParameter2_ucopy)
      {
        _templateParameter2 = new ArrayList(_templateParameter2);
        _templateParameter2_ucopy = null;
      }
      __arg.internalRefByModelElement2(this);
      _templateParameter2.add(__arg);
      logBagAdd(_templateParameter2_addMethod, _templateParameter2_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("templateParameter2", old, getTemplateParameters2(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeTemplateParameter2(MTemplateParameter __arg)
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
        old = getTemplateParameters2();
      }
      if (null != _templateParameter2_ucopy)
      {
        _templateParameter2 = new ArrayList(_templateParameter2);
        _templateParameter2_ucopy = null;
      }
      if (!_templateParameter2.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByModelElement2(this);
      logBagRemove(_templateParameter2_removeMethod, _templateParameter2_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("templateParameter2", old, getTemplateParameters2(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByTemplateParameter2(MTemplateParameter __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getTemplateParameters2();
    }
    if (null != _templateParameter2_ucopy)
    {
      _templateParameter2 = new ArrayList(_templateParameter2);
      _templateParameter2_ucopy = null;
    }
    _templateParameter2.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("templateParameter2", old, getTemplateParameters2(), __arg);
    }
  }
  public final void internalUnrefByTemplateParameter2(MTemplateParameter __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getTemplateParameters2();
    }
    if (null != _templateParameter2_ucopy)
    {
      _templateParameter2 = new ArrayList(_templateParameter2);
      _templateParameter2_ucopy = null;
    }
    _templateParameter2.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("templateParameter2", old, getTemplateParameters2(), __arg);
    }
  }
  // opposite role: elementResidence this role: resident
  private final static Method _elementResidence_setMethod = getMethod1(MModelElementImpl.class, "setElementResidences", Collection.class);
  private final static Method _elementResidence_addMethod = getMethod1(MModelElementImpl.class, "addElementResidence", MElementResidence.class);
  private final static Method _elementResidence_removeMethod = getMethod1(MModelElementImpl.class, "removeElementResidence", MElementResidence.class);
  Collection _elementResidence = Collections.EMPTY_LIST;
  Collection _elementResidence_ucopy = Collections.EMPTY_LIST;
  public final Collection getElementResidences()
  {
    checkExists();
    if (null == _elementResidence_ucopy)
    {
      _elementResidence_ucopy = ucopy(_elementResidence);
    }
    return _elementResidence_ucopy;
  }
  public final void setElementResidences(Collection __arg)
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
        old = getElementResidences();
      }
      _elementResidence_ucopy = null;
      Collection __added = bagdiff(__arg,_elementResidence);
      Collection __removed = bagdiff(_elementResidence, __arg);
      Iterator iter13 = __removed.iterator();
      while (iter13.hasNext())
      {
        MElementResidence o = (MElementResidence)iter13.next();
        o.internalUnrefByResident(this);
      }
      Iterator iter14 = __added.iterator();
      while (iter14.hasNext())
      {
        MElementResidence o = (MElementResidence)iter14.next();
        o.internalRefByResident(this);
      }
      _elementResidence = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_elementResidence_setMethod, old, getElementResidences());
      }
      if (sendEvent)
      {
        fireBagSet("elementResidence", old, getElementResidences());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addElementResidence(MElementResidence __arg)
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
        old = getElementResidences();
      }
      if (null != _elementResidence_ucopy)
      {
        _elementResidence = new ArrayList(_elementResidence);
        _elementResidence_ucopy = null;
      }
      __arg.internalRefByResident(this);
      _elementResidence.add(__arg);
      logBagAdd(_elementResidence_addMethod, _elementResidence_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("elementResidence", old, getElementResidences(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeElementResidence(MElementResidence __arg)
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
        old = getElementResidences();
      }
      if (null != _elementResidence_ucopy)
      {
        _elementResidence = new ArrayList(_elementResidence);
        _elementResidence_ucopy = null;
      }
      if (!_elementResidence.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByResident(this);
      logBagRemove(_elementResidence_removeMethod, _elementResidence_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("elementResidence", old, getElementResidences(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByElementResidence(MElementResidence __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getElementResidences();
    }
    if (null != _elementResidence_ucopy)
    {
      _elementResidence = new ArrayList(_elementResidence);
      _elementResidence_ucopy = null;
    }
    _elementResidence.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("elementResidence", old, getElementResidences(), __arg);
    }
  }
  public final void internalUnrefByElementResidence(MElementResidence __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getElementResidences();
    }
    if (null != _elementResidence_ucopy)
    {
      _elementResidence = new ArrayList(_elementResidence);
      _elementResidence_ucopy = null;
    }
    _elementResidence.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("elementResidence", old, getElementResidences(), __arg);
    }
  }
  // opposite role: comment this role: annotatedElement
  private final static Method _comment_setMethod = getMethod1(MModelElementImpl.class, "setComments", Collection.class);
  private final static Method _comment_addMethod = getMethod1(MModelElementImpl.class, "addComment", MComment.class);
  private final static Method _comment_removeMethod = getMethod1(MModelElementImpl.class, "removeComment", MComment.class);
  Collection _comment = Collections.EMPTY_LIST;
  Collection _comment_ucopy = Collections.EMPTY_LIST;
  public final Collection getComments()
  {
    checkExists();
    if (null == _comment_ucopy)
    {
      _comment_ucopy = ucopy(_comment);
    }
    return _comment_ucopy;
  }
  public final void setComments(Collection __arg)
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
        old = getComments();
      }
      _comment_ucopy = null;
      Collection __added = bagdiff(__arg,_comment);
      Collection __removed = bagdiff(_comment, __arg);
      Iterator iter15 = __removed.iterator();
      while (iter15.hasNext())
      {
        MComment o = (MComment)iter15.next();
        o.internalUnrefByAnnotatedElement(this);
      }
      Iterator iter16 = __added.iterator();
      while (iter16.hasNext())
      {
        MComment o = (MComment)iter16.next();
        o.internalRefByAnnotatedElement(this);
      }
      _comment = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_comment_setMethod, old, getComments());
      }
      if (sendEvent)
      {
        fireBagSet("comment", old, getComments());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addComment(MComment __arg)
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
        old = getComments();
      }
      if (null != _comment_ucopy)
      {
        _comment = new ArrayList(_comment);
        _comment_ucopy = null;
      }
      __arg.internalRefByAnnotatedElement(this);
      _comment.add(__arg);
      logBagAdd(_comment_addMethod, _comment_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("comment", old, getComments(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeComment(MComment __arg)
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
        old = getComments();
      }
      if (null != _comment_ucopy)
      {
        _comment = new ArrayList(_comment);
        _comment_ucopy = null;
      }
      if (!_comment.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByAnnotatedElement(this);
      logBagRemove(_comment_removeMethod, _comment_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("comment", old, getComments(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByComment(MComment __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getComments();
    }
    if (null != _comment_ucopy)
    {
      _comment = new ArrayList(_comment);
      _comment_ucopy = null;
    }
    _comment.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("comment", old, getComments(), __arg);
    }
  }
  public final void internalUnrefByComment(MComment __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getComments();
    }
    if (null != _comment_ucopy)
    {
      _comment = new ArrayList(_comment);
      _comment_ucopy = null;
    }
    _comment.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("comment", old, getComments(), __arg);
    }
  }
  // opposite role: binding this role: argument
  Collection _binding = Collections.EMPTY_LIST;
  Collection _binding_ucopy = Collections.EMPTY_LIST;
  public final Collection getBindings()
  {
    checkExists();
    if (null == _binding_ucopy)
    {
      _binding_ucopy = ucopy(_binding);
    }
    return _binding_ucopy;
  }
  public final void setBindings(Collection __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      if (__arg == null)
      {
        throw new NullPointerException();
      }
      Collection __added = bagdiff(__arg,_binding);
      Collection __removed = bagdiff(_binding, __arg);
      Iterator iter17 = __removed.iterator();
      while (iter17.hasNext())
      {
        MBinding o = (MBinding)iter17.next();
        o.removeArgument(this);
      }
      Iterator iter18 = __added.iterator();
      while (iter18.hasNext())
      {
        MBinding o = (MBinding)iter18.next();
        o.addArgument(this);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addBinding(MBinding __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      if (__arg == null)
      {
        throw new NullPointerException();
      }
      __arg.addArgument(this);
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeBinding(MBinding __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      if (__arg == null)
      {
        throw new NullPointerException();
      }
      __arg.removeArgument(this);
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByBinding(MBinding __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getBindings();
    }
    if (null != _binding_ucopy)
    {
      _binding = new ArrayList(_binding);
      _binding_ucopy = null;
    }
    _binding.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("binding", old, getBindings(), __arg);
    }
  }
  public final void internalUnrefByBinding(MBinding __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getBindings();
    }
    if (null != _binding_ucopy)
    {
      _binding = new ArrayList(_binding);
      _binding_ucopy = null;
    }
    _binding.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("binding", old, getBindings(), __arg);
    }
  }
  // opposite role: templateParameter3 this role: defaultElement
  private final static Method _templateParameter3_setMethod = getMethod1(MModelElementImpl.class, "setTemplateParameters3", Collection.class);
  private final static Method _templateParameter3_addMethod = getMethod1(MModelElementImpl.class, "addTemplateParameter3", MTemplateParameter.class);
  private final static Method _templateParameter3_removeMethod = getMethod1(MModelElementImpl.class, "removeTemplateParameter3", MTemplateParameter.class);
  Collection _templateParameter3 = Collections.EMPTY_LIST;
  Collection _templateParameter3_ucopy = Collections.EMPTY_LIST;
  public final Collection getTemplateParameters3()
  {
    checkExists();
    if (null == _templateParameter3_ucopy)
    {
      _templateParameter3_ucopy = ucopy(_templateParameter3);
    }
    return _templateParameter3_ucopy;
  }
  public final void setTemplateParameters3(Collection __arg)
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
        old = getTemplateParameters3();
      }
      _templateParameter3_ucopy = null;
      Collection __added = bagdiff(__arg,_templateParameter3);
      Collection __removed = bagdiff(_templateParameter3, __arg);
      Iterator iter19 = __removed.iterator();
      while (iter19.hasNext())
      {
        MTemplateParameter o = (MTemplateParameter)iter19.next();
        o.internalUnrefByDefaultElement(this);
      }
      Iterator iter20 = __added.iterator();
      while (iter20.hasNext())
      {
        MTemplateParameter o = (MTemplateParameter)iter20.next();
        o.internalRefByDefaultElement(this);
      }
      _templateParameter3 = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_templateParameter3_setMethod, old, getTemplateParameters3());
      }
      if (sendEvent)
      {
        fireBagSet("templateParameter3", old, getTemplateParameters3());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addTemplateParameter3(MTemplateParameter __arg)
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
        old = getTemplateParameters3();
      }
      if (null != _templateParameter3_ucopy)
      {
        _templateParameter3 = new ArrayList(_templateParameter3);
        _templateParameter3_ucopy = null;
      }
      __arg.internalRefByDefaultElement(this);
      _templateParameter3.add(__arg);
      logBagAdd(_templateParameter3_addMethod, _templateParameter3_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("templateParameter3", old, getTemplateParameters3(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeTemplateParameter3(MTemplateParameter __arg)
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
        old = getTemplateParameters3();
      }
      if (null != _templateParameter3_ucopy)
      {
        _templateParameter3 = new ArrayList(_templateParameter3);
        _templateParameter3_ucopy = null;
      }
      if (!_templateParameter3.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByDefaultElement(this);
      logBagRemove(_templateParameter3_removeMethod, _templateParameter3_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("templateParameter3", old, getTemplateParameters3(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByTemplateParameter3(MTemplateParameter __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getTemplateParameters3();
    }
    if (null != _templateParameter3_ucopy)
    {
      _templateParameter3 = new ArrayList(_templateParameter3);
      _templateParameter3_ucopy = null;
    }
    _templateParameter3.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("templateParameter3", old, getTemplateParameters3(), __arg);
    }
  }
  public final void internalUnrefByTemplateParameter3(MTemplateParameter __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getTemplateParameters3();
    }
    if (null != _templateParameter3_ucopy)
    {
      _templateParameter3 = new ArrayList(_templateParameter3);
      _templateParameter3_ucopy = null;
    }
    _templateParameter3.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("templateParameter3", old, getTemplateParameters3(), __arg);
    }
  }
  // opposite role: sourceFlow this role: source
  private final static Method _sourceFlow_setMethod = getMethod1(MModelElementImpl.class, "setSourceFlows", Collection.class);
  private final static Method _sourceFlow_addMethod = getMethod1(MModelElementImpl.class, "addSourceFlow", MFlow.class);
  private final static Method _sourceFlow_removeMethod = getMethod1(MModelElementImpl.class, "removeSourceFlow", MFlow.class);
  Collection _sourceFlow = Collections.EMPTY_LIST;
  Collection _sourceFlow_ucopy = Collections.EMPTY_LIST;
  public final Collection getSourceFlows()
  {
    checkExists();
    if (null == _sourceFlow_ucopy)
    {
      _sourceFlow_ucopy = ucopy(_sourceFlow);
    }
    return _sourceFlow_ucopy;
  }
  public final void setSourceFlows(Collection __arg)
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
        old = getSourceFlows();
      }
      _sourceFlow_ucopy = null;
      Collection __added = bagdiff(__arg,_sourceFlow);
      Collection __removed = bagdiff(_sourceFlow, __arg);
      Iterator iter21 = __removed.iterator();
      while (iter21.hasNext())
      {
        MFlow o = (MFlow)iter21.next();
        o.internalUnrefBySource(this);
      }
      Iterator iter22 = __added.iterator();
      while (iter22.hasNext())
      {
        MFlow o = (MFlow)iter22.next();
        o.internalRefBySource(this);
      }
      _sourceFlow = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_sourceFlow_setMethod, old, getSourceFlows());
      }
      if (sendEvent)
      {
        fireBagSet("sourceFlow", old, getSourceFlows());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addSourceFlow(MFlow __arg)
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
        old = getSourceFlows();
      }
      if (null != _sourceFlow_ucopy)
      {
        _sourceFlow = new ArrayList(_sourceFlow);
        _sourceFlow_ucopy = null;
      }
      __arg.internalRefBySource(this);
      _sourceFlow.add(__arg);
      logBagAdd(_sourceFlow_addMethod, _sourceFlow_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("sourceFlow", old, getSourceFlows(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeSourceFlow(MFlow __arg)
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
        old = getSourceFlows();
      }
      if (null != _sourceFlow_ucopy)
      {
        _sourceFlow = new ArrayList(_sourceFlow);
        _sourceFlow_ucopy = null;
      }
      if (!_sourceFlow.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefBySource(this);
      logBagRemove(_sourceFlow_removeMethod, _sourceFlow_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("sourceFlow", old, getSourceFlows(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefBySourceFlow(MFlow __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getSourceFlows();
    }
    if (null != _sourceFlow_ucopy)
    {
      _sourceFlow = new ArrayList(_sourceFlow);
      _sourceFlow_ucopy = null;
    }
    _sourceFlow.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("sourceFlow", old, getSourceFlows(), __arg);
    }
  }
  public final void internalUnrefBySourceFlow(MFlow __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getSourceFlows();
    }
    if (null != _sourceFlow_ucopy)
    {
      _sourceFlow = new ArrayList(_sourceFlow);
      _sourceFlow_ucopy = null;
    }
    _sourceFlow.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("sourceFlow", old, getSourceFlows(), __arg);
    }
  }
  // opposite role: targetFlow this role: target
  private final static Method _targetFlow_setMethod = getMethod1(MModelElementImpl.class, "setTargetFlows", Collection.class);
  private final static Method _targetFlow_addMethod = getMethod1(MModelElementImpl.class, "addTargetFlow", MFlow.class);
  private final static Method _targetFlow_removeMethod = getMethod1(MModelElementImpl.class, "removeTargetFlow", MFlow.class);
  Collection _targetFlow = Collections.EMPTY_LIST;
  Collection _targetFlow_ucopy = Collections.EMPTY_LIST;
  public final Collection getTargetFlows()
  {
    checkExists();
    if (null == _targetFlow_ucopy)
    {
      _targetFlow_ucopy = ucopy(_targetFlow);
    }
    return _targetFlow_ucopy;
  }
  public final void setTargetFlows(Collection __arg)
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
        old = getTargetFlows();
      }
      _targetFlow_ucopy = null;
      Collection __added = bagdiff(__arg,_targetFlow);
      Collection __removed = bagdiff(_targetFlow, __arg);
      Iterator iter23 = __removed.iterator();
      while (iter23.hasNext())
      {
        MFlow o = (MFlow)iter23.next();
        o.internalUnrefByTarget(this);
      }
      Iterator iter24 = __added.iterator();
      while (iter24.hasNext())
      {
        MFlow o = (MFlow)iter24.next();
        o.internalRefByTarget(this);
      }
      _targetFlow = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_targetFlow_setMethod, old, getTargetFlows());
      }
      if (sendEvent)
      {
        fireBagSet("targetFlow", old, getTargetFlows());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addTargetFlow(MFlow __arg)
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
        old = getTargetFlows();
      }
      if (null != _targetFlow_ucopy)
      {
        _targetFlow = new ArrayList(_targetFlow);
        _targetFlow_ucopy = null;
      }
      __arg.internalRefByTarget(this);
      _targetFlow.add(__arg);
      logBagAdd(_targetFlow_addMethod, _targetFlow_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("targetFlow", old, getTargetFlows(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeTargetFlow(MFlow __arg)
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
        old = getTargetFlows();
      }
      if (null != _targetFlow_ucopy)
      {
        _targetFlow = new ArrayList(_targetFlow);
        _targetFlow_ucopy = null;
      }
      if (!_targetFlow.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByTarget(this);
      logBagRemove(_targetFlow_removeMethod, _targetFlow_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("targetFlow", old, getTargetFlows(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByTargetFlow(MFlow __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getTargetFlows();
    }
    if (null != _targetFlow_ucopy)
    {
      _targetFlow = new ArrayList(_targetFlow);
      _targetFlow_ucopy = null;
    }
    _targetFlow.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("targetFlow", old, getTargetFlows(), __arg);
    }
  }
  public final void internalUnrefByTargetFlow(MFlow __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getTargetFlows();
    }
    if (null != _targetFlow_ucopy)
    {
      _targetFlow = new ArrayList(_targetFlow);
      _targetFlow_ucopy = null;
    }
    _targetFlow.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("targetFlow", old, getTargetFlows(), __arg);
    }
  }
  // opposite role: templateParameter this role: modelElement
  private final static Method _templateParameter_setMethod = getMethod1(MModelElementImpl.class, "setTemplateParameters", List.class);
  private final static Method _templateParameter_removeMethod = getMethod1(MModelElementImpl.class, "removeTemplateParameter", int.class);
  private final static Method _templateParameter_addMethod = getMethod2(MModelElementImpl.class, "addTemplateParameter", int.class, MTemplateParameter.class);
  private final static Method _templateParameter_listSetMethod = getMethod2(MModelElementImpl.class, "setTemplateParameter", int.class, MTemplateParameter.class);
  List _templateParameter = Collections.EMPTY_LIST;
  List _templateParameter_ucopy = Collections.EMPTY_LIST;
  public final List getTemplateParameters()
  {
    checkExists();
    if (null == _templateParameter_ucopy)
    {
      _templateParameter_ucopy = ucopy(_templateParameter);
    }
    return _templateParameter_ucopy;
  }
  public final void setTemplateParameters(List __arg)
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
        old = getTemplateParameters();
      }
      _templateParameter_ucopy = null;
      Collection __added = bagdiff(__arg,_templateParameter);
      Collection __removed = bagdiff(_templateParameter, __arg);
      Iterator iter25 = __removed.iterator();
      while (iter25.hasNext())
      {
        MTemplateParameter o = (MTemplateParameter)iter25.next();
        o.internalUnrefByModelElement(this);
      }
      Iterator iter26 = __added.iterator();
      while (iter26.hasNext())
      {
        MTemplateParameter o = (MTemplateParameter)iter26.next();
        o.internalRefByModelElement(this);
      }
      _templateParameter = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_templateParameter_setMethod, old, getTemplateParameters());
      }
      if (sendEvent)
      {
        fireListSet("templateParameter", old, getTemplateParameters());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addTemplateParameter(MTemplateParameter __arg)
  {
    addTemplateParameter(_templateParameter.size(), __arg);
  }
  public final void removeTemplateParameter(MTemplateParameter __arg)
  {
    if (__arg == null)
    {
      throw new NullPointerException();
    }
    int __pos = _templateParameter.indexOf(__arg);
    removeTemplateParameter(__pos);
  }
  public final void addTemplateParameter(int __pos, MTemplateParameter __arg)
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
        old = getTemplateParameters();
      }
      if (null != _templateParameter_ucopy)
      {
        _templateParameter = new ArrayList(_templateParameter);
        _templateParameter_ucopy = null;
      }
      _templateParameter.add(__pos, __arg);
      __arg.internalRefByModelElement(this);
      logListAdd(_templateParameter_addMethod, _templateParameter_removeMethod, __arg, __pos);
      if (sendEvent)
      {
        fireListAdd("templateParameter", old, getTemplateParameters(), __arg, __pos);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeTemplateParameter(int __pos)
  {
    operationStarted();
    try
    {
      checkExists();
      final boolean sendEvent = needEvent();
      List old = null;
      if (sendEvent)
      {
        old = getTemplateParameters();
      }
      if (null != _templateParameter_ucopy)
      {
        _templateParameter = new ArrayList(_templateParameter);
        _templateParameter_ucopy = null;
      }
      MTemplateParameter __arg = (MTemplateParameter)_templateParameter.remove(__pos);
      __arg.internalUnrefByModelElement(this);
      logListRemove(_templateParameter_removeMethod, _templateParameter_addMethod, __arg, __pos);
      if (sendEvent)
      {
        fireListRemove("templateParameter", old, getTemplateParameters(), __arg, __pos);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void setTemplateParameter(int __pos, MTemplateParameter __arg)
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
        old = getTemplateParameters();
      }
      if (null != _templateParameter_ucopy)
      {
        _templateParameter = new ArrayList(_templateParameter);
        _templateParameter_ucopy = null;
      }
      MTemplateParameter __old = (MTemplateParameter)_templateParameter.get(__pos);
      __old.internalUnrefByModelElement(this);
      __arg.internalRefByModelElement(this);
      _templateParameter.set(__pos,__arg);
      logListSet(_templateParameter_listSetMethod, __old, __arg, __pos);
      if (sendEvent)
      {
        fireListItemSet("templateParameter", old, getTemplateParameters(), __old, __arg, __pos);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final MTemplateParameter getTemplateParameter(int __pos)
  {
    checkExists();
    return (MTemplateParameter)_templateParameter.get(__pos);
  }
  // opposite role: presentation this role: subject
  private final static Method _presentation_setMethod = getMethod1(MModelElementImpl.class, "setPresentations", Collection.class);
  private final static Method _presentation_addMethod = getMethod1(MModelElementImpl.class, "addPresentation", MPresentationElement.class);
  private final static Method _presentation_removeMethod = getMethod1(MModelElementImpl.class, "removePresentation", MPresentationElement.class);
  Collection _presentation = Collections.EMPTY_LIST;
  Collection _presentation_ucopy = Collections.EMPTY_LIST;
  public final Collection getPresentations()
  {
    checkExists();
    if (null == _presentation_ucopy)
    {
      _presentation_ucopy = ucopy(_presentation);
    }
    return _presentation_ucopy;
  }
  public final void setPresentations(Collection __arg)
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
        old = getPresentations();
      }
      _presentation_ucopy = null;
      Collection __added = bagdiff(__arg,_presentation);
      Collection __removed = bagdiff(_presentation, __arg);
      Iterator iter27 = __removed.iterator();
      while (iter27.hasNext())
      {
        MPresentationElement o = (MPresentationElement)iter27.next();
        o.internalUnrefBySubject(this);
      }
      Iterator iter28 = __added.iterator();
      while (iter28.hasNext())
      {
        MPresentationElement o = (MPresentationElement)iter28.next();
        o.internalRefBySubject(this);
      }
      _presentation = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_presentation_setMethod, old, getPresentations());
      }
      if (sendEvent)
      {
        fireBagSet("presentation", old, getPresentations());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addPresentation(MPresentationElement __arg)
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
        old = getPresentations();
      }
      if (null != _presentation_ucopy)
      {
        _presentation = new ArrayList(_presentation);
        _presentation_ucopy = null;
      }
      __arg.internalRefBySubject(this);
      _presentation.add(__arg);
      logBagAdd(_presentation_addMethod, _presentation_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("presentation", old, getPresentations(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removePresentation(MPresentationElement __arg)
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
        old = getPresentations();
      }
      if (null != _presentation_ucopy)
      {
        _presentation = new ArrayList(_presentation);
        _presentation_ucopy = null;
      }
      if (!_presentation.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefBySubject(this);
      logBagRemove(_presentation_removeMethod, _presentation_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("presentation", old, getPresentations(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByPresentation(MPresentationElement __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getPresentations();
    }
    if (null != _presentation_ucopy)
    {
      _presentation = new ArrayList(_presentation);
      _presentation_ucopy = null;
    }
    _presentation.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("presentation", old, getPresentations(), __arg);
    }
  }
  public final void internalUnrefByPresentation(MPresentationElement __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getPresentations();
    }
    if (null != _presentation_ucopy)
    {
      _presentation = new ArrayList(_presentation);
      _presentation_ucopy = null;
    }
    _presentation.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("presentation", old, getPresentations(), __arg);
    }
  }
  // opposite role: supplierDependency this role: supplier
  private final static Method _supplierDependency_setMethod = getMethod1(MModelElementImpl.class, "setSupplierDependencies", Collection.class);
  private final static Method _supplierDependency_addMethod = getMethod1(MModelElementImpl.class, "addSupplierDependency", MDependency.class);
  private final static Method _supplierDependency_removeMethod = getMethod1(MModelElementImpl.class, "removeSupplierDependency", MDependency.class);
  Collection _supplierDependency = Collections.EMPTY_LIST;
  Collection _supplierDependency_ucopy = Collections.EMPTY_LIST;
  public final Collection getSupplierDependencies()
  {
    checkExists();
    if (null == _supplierDependency_ucopy)
    {
      _supplierDependency_ucopy = ucopy(_supplierDependency);
    }
    return _supplierDependency_ucopy;
  }
  public final void setSupplierDependencies(Collection __arg)
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
        old = getSupplierDependencies();
      }
      _supplierDependency_ucopy = null;
      Collection __added = bagdiff(__arg,_supplierDependency);
      Collection __removed = bagdiff(_supplierDependency, __arg);
      Iterator iter29 = __removed.iterator();
      while (iter29.hasNext())
      {
        MDependency o = (MDependency)iter29.next();
        o.internalUnrefBySupplier(this);
      }
      Iterator iter30 = __added.iterator();
      while (iter30.hasNext())
      {
        MDependency o = (MDependency)iter30.next();
        o.internalRefBySupplier(this);
      }
      _supplierDependency = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_supplierDependency_setMethod, old, getSupplierDependencies());
      }
      if (sendEvent)
      {
        fireBagSet("supplierDependency", old, getSupplierDependencies());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addSupplierDependency(MDependency __arg)
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
        old = getSupplierDependencies();
      }
      if (null != _supplierDependency_ucopy)
      {
        _supplierDependency = new ArrayList(_supplierDependency);
        _supplierDependency_ucopy = null;
      }
      __arg.internalRefBySupplier(this);
      _supplierDependency.add(__arg);
      logBagAdd(_supplierDependency_addMethod, _supplierDependency_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("supplierDependency", old, getSupplierDependencies(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeSupplierDependency(MDependency __arg)
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
        old = getSupplierDependencies();
      }
      if (null != _supplierDependency_ucopy)
      {
        _supplierDependency = new ArrayList(_supplierDependency);
        _supplierDependency_ucopy = null;
      }
      if (!_supplierDependency.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefBySupplier(this);
      logBagRemove(_supplierDependency_removeMethod, _supplierDependency_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("supplierDependency", old, getSupplierDependencies(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefBySupplierDependency(MDependency __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getSupplierDependencies();
    }
    if (null != _supplierDependency_ucopy)
    {
      _supplierDependency = new ArrayList(_supplierDependency);
      _supplierDependency_ucopy = null;
    }
    _supplierDependency.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("supplierDependency", old, getSupplierDependencies(), __arg);
    }
  }
  public final void internalUnrefBySupplierDependency(MDependency __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getSupplierDependencies();
    }
    if (null != _supplierDependency_ucopy)
    {
      _supplierDependency = new ArrayList(_supplierDependency);
      _supplierDependency_ucopy = null;
    }
    _supplierDependency.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("supplierDependency", old, getSupplierDependencies(), __arg);
    }
  }
  // opposite role: constraint this role: constrainedElement
  Collection _constraint = Collections.EMPTY_LIST;
  Collection _constraint_ucopy = Collections.EMPTY_LIST;
  public final Collection getConstraints()
  {
    checkExists();
    if (null == _constraint_ucopy)
    {
      _constraint_ucopy = ucopy(_constraint);
    }
    return _constraint_ucopy;
  }
  public final void setConstraints(Collection __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      if (__arg == null)
      {
        throw new NullPointerException();
      }
      Collection __added = bagdiff(__arg,_constraint);
      Collection __removed = bagdiff(_constraint, __arg);
      Iterator iter31 = __removed.iterator();
      while (iter31.hasNext())
      {
        MConstraint o = (MConstraint)iter31.next();
        o.removeConstrainedElement(this);
      }
      Iterator iter32 = __added.iterator();
      while (iter32.hasNext())
      {
        MConstraint o = (MConstraint)iter32.next();
        o.addConstrainedElement(this);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addConstraint(MConstraint __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      if (__arg == null)
      {
        throw new NullPointerException();
      }
      __arg.addConstrainedElement(this);
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeConstraint(MConstraint __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      if (__arg == null)
      {
        throw new NullPointerException();
      }
      __arg.removeConstrainedElement(this);
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByConstraint(MConstraint __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getConstraints();
    }
    if (null != _constraint_ucopy)
    {
      _constraint = new ArrayList(_constraint);
      _constraint_ucopy = null;
    }
    _constraint.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("constraint", old, getConstraints(), __arg);
    }
  }
  public final void internalUnrefByConstraint(MConstraint __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getConstraints();
    }
    if (null != _constraint_ucopy)
    {
      _constraint = new ArrayList(_constraint);
      _constraint_ucopy = null;
    }
    _constraint.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("constraint", old, getConstraints(), __arg);
    }
  }
  // opposite role: taggedValue this role: modelElement
  private final static Method _taggedValue_setMethod = getMethod1(MModelElementImpl.class, "setTaggedValues", Collection.class);
  private final static Method _taggedValue_addMethod = getMethod1(MModelElementImpl.class, "addTaggedValue", MTaggedValue.class);
  private final static Method _taggedValue_removeMethod = getMethod1(MModelElementImpl.class, "removeTaggedValue", MTaggedValue.class);
  Collection _taggedValue = Collections.EMPTY_LIST;
  Collection _taggedValue_ucopy = Collections.EMPTY_LIST;
  public final Collection getTaggedValues()
  {
    checkExists();
    if (null == _taggedValue_ucopy)
    {
      _taggedValue_ucopy = ucopy(_taggedValue);
    }
    return _taggedValue_ucopy;
  }
  public final void setTaggedValues(Collection __arg)
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
        old = getTaggedValues();
      }
      _taggedValue_ucopy = null;
      Collection __added = bagdiff(__arg,_taggedValue);
      Collection __removed = bagdiff(_taggedValue, __arg);
      Iterator iter33 = __removed.iterator();
      while (iter33.hasNext())
      {
        MTaggedValue o = (MTaggedValue)iter33.next();
        o.internalUnrefByModelElement(this);
      }
      Iterator iter34 = __added.iterator();
      while (iter34.hasNext())
      {
        MTaggedValue o = (MTaggedValue)iter34.next();
        o.internalRefByModelElement(this);
      }
      _taggedValue = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_taggedValue_setMethod, old, getTaggedValues());
      }
      if (sendEvent)
      {
        fireBagSet("taggedValue", old, getTaggedValues());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addTaggedValue(MTaggedValue __arg)
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
        old = getTaggedValues();
      }
      if (null != _taggedValue_ucopy)
      {
        _taggedValue = new ArrayList(_taggedValue);
        _taggedValue_ucopy = null;
      }
      __arg.internalRefByModelElement(this);
      _taggedValue.add(__arg);
      logBagAdd(_taggedValue_addMethod, _taggedValue_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("taggedValue", old, getTaggedValues(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeTaggedValue(MTaggedValue __arg)
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
        old = getTaggedValues();
      }
      if (null != _taggedValue_ucopy)
      {
        _taggedValue = new ArrayList(_taggedValue);
        _taggedValue_ucopy = null;
      }
      if (!_taggedValue.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByModelElement(this);
      logBagRemove(_taggedValue_removeMethod, _taggedValue_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("taggedValue", old, getTaggedValues(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByTaggedValue(MTaggedValue __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getTaggedValues();
    }
    if (null != _taggedValue_ucopy)
    {
      _taggedValue = new ArrayList(_taggedValue);
      _taggedValue_ucopy = null;
    }
    _taggedValue.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("taggedValue", old, getTaggedValues(), __arg);
    }
  }
  public final void internalUnrefByTaggedValue(MTaggedValue __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getTaggedValues();
    }
    if (null != _taggedValue_ucopy)
    {
      _taggedValue = new ArrayList(_taggedValue);
      _taggedValue_ucopy = null;
    }
    _taggedValue.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("taggedValue", old, getTaggedValues(), __arg);
    }
  }
  // opposite role: clientDependency this role: client
  private final static Method _clientDependency_setMethod = getMethod1(MModelElementImpl.class, "setClientDependencies", Collection.class);
  private final static Method _clientDependency_addMethod = getMethod1(MModelElementImpl.class, "addClientDependency", MDependency.class);
  private final static Method _clientDependency_removeMethod = getMethod1(MModelElementImpl.class, "removeClientDependency", MDependency.class);
  Collection _clientDependency = Collections.EMPTY_LIST;
  Collection _clientDependency_ucopy = Collections.EMPTY_LIST;
  public final Collection getClientDependencies()
  {
    checkExists();
    if (null == _clientDependency_ucopy)
    {
      _clientDependency_ucopy = ucopy(_clientDependency);
    }
    return _clientDependency_ucopy;
  }
  public final void setClientDependencies(Collection __arg)
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
        old = getClientDependencies();
      }
      _clientDependency_ucopy = null;
      Collection __added = bagdiff(__arg,_clientDependency);
      Collection __removed = bagdiff(_clientDependency, __arg);
      Iterator iter35 = __removed.iterator();
      while (iter35.hasNext())
      {
        MDependency o = (MDependency)iter35.next();
        o.internalUnrefByClient(this);
      }
      Iterator iter36 = __added.iterator();
      while (iter36.hasNext())
      {
        MDependency o = (MDependency)iter36.next();
        o.internalRefByClient(this);
      }
      _clientDependency = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_clientDependency_setMethod, old, getClientDependencies());
      }
      if (sendEvent)
      {
        fireBagSet("clientDependency", old, getClientDependencies());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addClientDependency(MDependency __arg)
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
        old = getClientDependencies();
      }
      if (null != _clientDependency_ucopy)
      {
        _clientDependency = new ArrayList(_clientDependency);
        _clientDependency_ucopy = null;
      }
      __arg.internalRefByClient(this);
      _clientDependency.add(__arg);
      logBagAdd(_clientDependency_addMethod, _clientDependency_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("clientDependency", old, getClientDependencies(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeClientDependency(MDependency __arg)
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
        old = getClientDependencies();
      }
      if (null != _clientDependency_ucopy)
      {
        _clientDependency = new ArrayList(_clientDependency);
        _clientDependency_ucopy = null;
      }
      if (!_clientDependency.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByClient(this);
      logBagRemove(_clientDependency_removeMethod, _clientDependency_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("clientDependency", old, getClientDependencies(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByClientDependency(MDependency __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getClientDependencies();
    }
    if (null != _clientDependency_ucopy)
    {
      _clientDependency = new ArrayList(_clientDependency);
      _clientDependency_ucopy = null;
    }
    _clientDependency.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("clientDependency", old, getClientDependencies(), __arg);
    }
  }
  public final void internalUnrefByClientDependency(MDependency __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getClientDependencies();
    }
    if (null != _clientDependency_ucopy)
    {
      _clientDependency = new ArrayList(_clientDependency);
      _clientDependency_ucopy = null;
    }
    _clientDependency.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("clientDependency", old, getClientDependencies(), __arg);
    }
  }
  // opposite role: namespace this role: ownedElement
  private final static Method _namespace_setMethod = getMethod1(MModelElementImpl.class, "setNamespace", MNamespace.class);
  MNamespace _namespace;
  public final MNamespace getNamespace()
  {
    checkExists();
    return _namespace;
  }
  public final void setNamespace(MNamespace __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      MNamespace __saved = _namespace;
      if (_namespace != __arg)
      {
        if (__saved != null)
        {
          __saved.internalUnrefByOwnedElement(this);
        }
        if (__arg != null)
        {
          __arg.internalRefByOwnedElement(this);
        }
        logRefSet(_namespace_setMethod, __saved, __arg);
        fireRefSet("namespace", __saved, __arg);
        _namespace = __arg;
        setModelElementContainer(_namespace, "namespace");
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByNamespace(MNamespace __arg)
  {
    MNamespace __saved = _namespace;
    if (_namespace != null)
    {
      _namespace.removeOwnedElement(this);
    }
    fireRefSet("namespace", __saved, __arg);
    _namespace = __arg;
    setModelElementContainer(_namespace, "namespace");
  }
  public final void internalUnrefByNamespace(MNamespace __arg)
  {
    fireRefSet("namespace", _namespace, null);
    _namespace = null;
    setModelElementContainer(null, null);
  }
  protected void cleanup(Collection scheduledForRemove)
  {
    // opposite role: elementImport2 this role: modelElement
    if (_elementImport2.size() != 0)
    {
      setElementImports2(Collections.EMPTY_LIST);
    }
    // opposite role: classifierRole1 this role: availableContents
    if (_classifierRole1.size() != 0)
    {
      setClassifierRoles1(Collections.EMPTY_LIST);
    }
    // opposite role: collaboration1 this role: constrainingElement
    if (_collaboration1.size() != 0)
    {
      setCollaborations1(Collections.EMPTY_LIST);
    }
    // opposite role: partition1 this role: contents
    if (_partition1.size() != 0)
    {
      setPartitions1(Collections.EMPTY_LIST);
    }
    // opposite role: behavior this role: context
    if (_behavior.size() != 0)
    {
      setBehaviors(Collections.EMPTY_LIST);
    }
    // opposite role: stereotype this role: extendedElement
    if (_stereotype != null )
    {
      setStereotype(null);
    }
    // opposite role: templateParameter2 this role: modelElement2
    if (_templateParameter2.size() != 0)
    {
      setTemplateParameters2(Collections.EMPTY_LIST);
    }
    // opposite role: elementResidence this role: resident
    if (_elementResidence.size() != 0)
    {
      setElementResidences(Collections.EMPTY_LIST);
    }
    // opposite role: comment this role: annotatedElement
    if (_comment.size() != 0)
    {
      setComments(Collections.EMPTY_LIST);
    }
    // opposite role: binding this role: argument
    if (_binding.size() != 0)
    {
      setBindings(Collections.EMPTY_LIST);
    }
    // opposite role: templateParameter3 this role: defaultElement
    if (_templateParameter3.size() != 0)
    {
      setTemplateParameters3(Collections.EMPTY_LIST);
    }
    // opposite role: sourceFlow this role: source
    if (_sourceFlow.size() != 0)
    {
      setSourceFlows(Collections.EMPTY_LIST);
    }
    // opposite role: targetFlow this role: target
    if (_targetFlow.size() != 0)
    {
      setTargetFlows(Collections.EMPTY_LIST);
    }
    // opposite role: templateParameter this role: modelElement
    if (_templateParameter.size() != 0)
    {
      scheduledForRemove.addAll(_templateParameter);
      setTemplateParameters(Collections.EMPTY_LIST);
    }
    // opposite role: presentation this role: subject
    if (_presentation.size() != 0)
    {
      setPresentations(Collections.EMPTY_LIST);
    }
    // opposite role: supplierDependency this role: supplier
    if (_supplierDependency.size() != 0)
    {
      setSupplierDependencies(Collections.EMPTY_LIST);
    }
    // opposite role: constraint this role: constrainedElement
    if (_constraint.size() != 0)
    {
      setConstraints(Collections.EMPTY_LIST);
    }
    // opposite role: taggedValue this role: modelElement
    if (_taggedValue.size() != 0)
    {
      scheduledForRemove.addAll(_taggedValue);
      setTaggedValues(Collections.EMPTY_LIST);
    }
    // opposite role: clientDependency this role: client
    if (_clientDependency.size() != 0)
    {
      setClientDependencies(Collections.EMPTY_LIST);
    }
    // opposite role: namespace this role: ownedElement
    if (_namespace != null )
    {
      setNamespace(null);
    }
    super.cleanup(scheduledForRemove);
  }

  public String getUMLClassName()
  {
    return "ModelElement";
  }

  // Reflective API

  public Object reflectiveGetValue(String feature)
  {
    if ("isSpecification".equals(feature))
    {
      return new Boolean(isSpecification());
    }
    if ("visibility".equals(feature))
    {
      return getVisibility();
    }
    if ("name".equals(feature))
    {
      return getName();
    }
    if ("elementImport2".equals(feature))
    {
      return getElementImports2();
    }
    if ("classifierRole1".equals(feature))
    {
      return getClassifierRoles1();
    }
    if ("collaboration1".equals(feature))
    {
      return getCollaborations1();
    }
    if ("partition1".equals(feature))
    {
      return getPartitions1();
    }
    if ("behavior".equals(feature))
    {
      return getBehaviors();
    }
    if ("stereotype".equals(feature))
    {
      return getStereotype();
    }
    if ("templateParameter2".equals(feature))
    {
      return getTemplateParameters2();
    }
    if ("elementResidence".equals(feature))
    {
      return getElementResidences();
    }
    if ("comment".equals(feature))
    {
      return getComments();
    }
    if ("binding".equals(feature))
    {
      return getBindings();
    }
    if ("templateParameter3".equals(feature))
    {
      return getTemplateParameters3();
    }
    if ("sourceFlow".equals(feature))
    {
      return getSourceFlows();
    }
    if ("targetFlow".equals(feature))
    {
      return getTargetFlows();
    }
    if ("templateParameter".equals(feature))
    {
      return getTemplateParameters();
    }
    if ("presentation".equals(feature))
    {
      return getPresentations();
    }
    if ("supplierDependency".equals(feature))
    {
      return getSupplierDependencies();
    }
    if ("constraint".equals(feature))
    {
      return getConstraints();
    }
    if ("taggedValue".equals(feature))
    {
      return getTaggedValues();
    }
    if ("clientDependency".equals(feature))
    {
      return getClientDependencies();
    }
    if ("namespace".equals(feature))
    {
      return getNamespace();
    }

    return super.reflectiveGetValue(feature);
  }

  public void reflectiveSetValue(String feature, Object obj)
  {
    if ("isSpecification".equals(feature))
    {
      setSpecification(((Boolean)obj).booleanValue());
      return;
    }
    if ("visibility".equals(feature))
    {
      setVisibility((MVisibilityKind)obj);
      return;
    }
    if ("name".equals(feature))
    {
      setName((String)obj);
      return;
    }
    if ("elementImport2".equals(feature))
    {
      setElementImports2((Collection)obj);
      return;
    }
    if ("classifierRole1".equals(feature))
    {
      setClassifierRoles1((Collection)obj);
      return;
    }
    if ("collaboration1".equals(feature))
    {
      setCollaborations1((Collection)obj);
      return;
    }
    if ("partition1".equals(feature))
    {
      setPartitions1((Collection)obj);
      return;
    }
    if ("behavior".equals(feature))
    {
      setBehaviors((Collection)obj);
      return;
    }
    if ("stereotype".equals(feature))
    {
      setStereotype((MStereotype)obj);
      return;
    }
    if ("templateParameter2".equals(feature))
    {
      setTemplateParameters2((Collection)obj);
      return;
    }
    if ("elementResidence".equals(feature))
    {
      setElementResidences((Collection)obj);
      return;
    }
    if ("comment".equals(feature))
    {
      setComments((Collection)obj);
      return;
    }
    if ("binding".equals(feature))
    {
      setBindings((Collection)obj);
      return;
    }
    if ("templateParameter3".equals(feature))
    {
      setTemplateParameters3((Collection)obj);
      return;
    }
    if ("sourceFlow".equals(feature))
    {
      setSourceFlows((Collection)obj);
      return;
    }
    if ("targetFlow".equals(feature))
    {
      setTargetFlows((Collection)obj);
      return;
    }
    if ("templateParameter".equals(feature))
    {
      setTemplateParameters((List)obj);
      return;
    }
    if ("presentation".equals(feature))
    {
      setPresentations((Collection)obj);
      return;
    }
    if ("supplierDependency".equals(feature))
    {
      setSupplierDependencies((Collection)obj);
      return;
    }
    if ("constraint".equals(feature))
    {
      setConstraints((Collection)obj);
      return;
    }
    if ("taggedValue".equals(feature))
    {
      setTaggedValues((Collection)obj);
      return;
    }
    if ("clientDependency".equals(feature))
    {
      setClientDependencies((Collection)obj);
      return;
    }
    if ("namespace".equals(feature))
    {
      setNamespace((MNamespace)obj);
      return;
    }

    super.reflectiveSetValue(feature, obj);
  }

  public void reflectiveAddValue(String feature, Object obj)
  {
    if ("elementImport2".equals(feature))
    {
      addElementImport2((MElementImport)obj);
      return;
    }
    if ("classifierRole1".equals(feature))
    {
      addClassifierRole1((MClassifierRole)obj);
      return;
    }
    if ("collaboration1".equals(feature))
    {
      addCollaboration1((MCollaboration)obj);
      return;
    }
    if ("partition1".equals(feature))
    {
      addPartition1((MPartition)obj);
      return;
    }
    if ("behavior".equals(feature))
    {
      addBehavior((MStateMachine)obj);
      return;
    }
    if ("templateParameter2".equals(feature))
    {
      addTemplateParameter2((MTemplateParameter)obj);
      return;
    }
    if ("elementResidence".equals(feature))
    {
      addElementResidence((MElementResidence)obj);
      return;
    }
    if ("comment".equals(feature))
    {
      addComment((MComment)obj);
      return;
    }
    if ("binding".equals(feature))
    {
      addBinding((MBinding)obj);
      return;
    }
    if ("templateParameter3".equals(feature))
    {
      addTemplateParameter3((MTemplateParameter)obj);
      return;
    }
    if ("sourceFlow".equals(feature))
    {
      addSourceFlow((MFlow)obj);
      return;
    }
    if ("targetFlow".equals(feature))
    {
      addTargetFlow((MFlow)obj);
      return;
    }
    if ("templateParameter".equals(feature))
    {
      addTemplateParameter((MTemplateParameter)obj);
      return;
    }
    if ("presentation".equals(feature))
    {
      addPresentation((MPresentationElement)obj);
      return;
    }
    if ("supplierDependency".equals(feature))
    {
      addSupplierDependency((MDependency)obj);
      return;
    }
    if ("constraint".equals(feature))
    {
      addConstraint((MConstraint)obj);
      return;
    }
    if ("taggedValue".equals(feature))
    {
      addTaggedValue((MTaggedValue)obj);
      return;
    }
    if ("clientDependency".equals(feature))
    {
      addClientDependency((MDependency)obj);
      return;
    }

    super.reflectiveAddValue(feature, obj);
  }

  public void reflectiveRemoveValue(String feature, Object obj)
  {
    if ("elementImport2".equals(feature))
    {
      removeElementImport2((MElementImport)obj);
      return;
    }
    if ("classifierRole1".equals(feature))
    {
      removeClassifierRole1((MClassifierRole)obj);
      return;
    }
    if ("collaboration1".equals(feature))
    {
      removeCollaboration1((MCollaboration)obj);
      return;
    }
    if ("partition1".equals(feature))
    {
      removePartition1((MPartition)obj);
      return;
    }
    if ("behavior".equals(feature))
    {
      removeBehavior((MStateMachine)obj);
      return;
    }
    if ("templateParameter2".equals(feature))
    {
      removeTemplateParameter2((MTemplateParameter)obj);
      return;
    }
    if ("elementResidence".equals(feature))
    {
      removeElementResidence((MElementResidence)obj);
      return;
    }
    if ("comment".equals(feature))
    {
      removeComment((MComment)obj);
      return;
    }
    if ("binding".equals(feature))
    {
      removeBinding((MBinding)obj);
      return;
    }
    if ("templateParameter3".equals(feature))
    {
      removeTemplateParameter3((MTemplateParameter)obj);
      return;
    }
    if ("sourceFlow".equals(feature))
    {
      removeSourceFlow((MFlow)obj);
      return;
    }
    if ("targetFlow".equals(feature))
    {
      removeTargetFlow((MFlow)obj);
      return;
    }
    if ("templateParameter".equals(feature))
    {
      removeTemplateParameter((MTemplateParameter)obj);
      return;
    }
    if ("presentation".equals(feature))
    {
      removePresentation((MPresentationElement)obj);
      return;
    }
    if ("supplierDependency".equals(feature))
    {
      removeSupplierDependency((MDependency)obj);
      return;
    }
    if ("constraint".equals(feature))
    {
      removeConstraint((MConstraint)obj);
      return;
    }
    if ("taggedValue".equals(feature))
    {
      removeTaggedValue((MTaggedValue)obj);
      return;
    }
    if ("clientDependency".equals(feature))
    {
      removeClientDependency((MDependency)obj);
      return;
    }

    super.reflectiveRemoveValue(feature, obj);
  }

  public Object reflectiveGetValue(String feature, int pos)
  {
    if ("templateParameter".equals(feature))
    {
      return getTemplateParameter(pos);
    }

    return super.reflectiveGetValue(feature, pos);
  }

  public void reflectiveSetValue(String feature, int pos, Object obj)
  {
    if ("templateParameter".equals(feature))
    {
      setTemplateParameter(pos, (MTemplateParameter)obj);
      return;
    }

    super.reflectiveSetValue(feature, pos, obj);
  }

  public void reflectiveAddValue(String feature, int pos, Object obj)
  {
    if ("templateParameter".equals(feature))
    {
      addTemplateParameter(pos, (MTemplateParameter)obj);
      return;
    }

    super.reflectiveAddValue(feature, pos, obj);
  }

  public void reflectiveRemoveValue(String feature, int pos)
  {
    if ("templateParameter".equals(feature))
    {
      removeTemplateParameter(pos);
      return;
    }

    super.reflectiveRemoveValue(feature, pos);
  }
  public Collection getModelElementContents()
  {
    Collection ret = super.getModelElementContents();
    ret.addAll(getTemplateParameters());
    ret.addAll(getTaggedValues());
    return ret;
  }
  public ru.novosoft.uml.model_management.MModel getModel()
  {
    if(this instanceof ru.novosoft.uml.model_management.MModel)
    {
      return (ru.novosoft.uml.model_management.MModel)this;
    }

    ru.novosoft.uml.foundation.core.MModelElement me = getModelElementContainer();
    if(me == null)
    {
      return null;
    }

    return me.getModel();
  }

  private ru.novosoft.uml.foundation.extension_mechanisms.MTaggedValue lookUpTaggedValue(String key)
  {
    Iterator i = getTaggedValues().iterator();
    while(i.hasNext())
    {
      MTaggedValue tv = (MTaggedValue)i.next();
      if(tv.getTag().equals(key))
      {
        return tv;
      }
    }
    return null;
  }

  public String getTaggedValue(String key)
  {
    return getTaggedValue(key, null);
  }

  public String getTaggedValue(String key, String defValue)
  {
    ru.novosoft.uml.foundation.extension_mechanisms.MTaggedValue tv = lookUpTaggedValue(key);
    if(tv == null)
      return defValue;
    return tv.getValue();
  }

  public void setTaggedValue(String key, String value)
  {
    ru.novosoft.uml.foundation.extension_mechanisms.MTaggedValue tv = lookUpTaggedValue(key);
    if(tv == null)
    {
      tv = new ru.novosoft.uml.foundation.extension_mechanisms.MTaggedValueImpl();
      tv.setTag(key);
      tv.setValue(value);
      addTaggedValue(tv);
    }
    else
    {
      tv.setValue(value);
    }
  }

  /** is a template */
  public boolean isTemplate()
  {
    return getTemplateParameters().size() > 0;
  } 

  /** is an instantiation of template */
  public boolean isInstantiation()
  {
    Iterator i = getClientDependencies().iterator();
    while(i.hasNext())
    {
      if(i.next() instanceof ru.novosoft.uml.foundation.core.MBinding)
      {
        return true;
      }
    }
    return false;
  } 

public void removeTaggedValue(String key)
{
  ru.novosoft.uml.foundation.extension_mechanisms.MTaggedValue tv = lookUpTaggedValue(key);
  if(null != tv)
  {
    removeTaggedValue(tv);
  }
}

public String toString()
{
  String s = getName();
  if (null != s)
  {
    return getUMLClassName() + ": " + s;
  }

  return "anon " + getUMLClassName();
}
}
