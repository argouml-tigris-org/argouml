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

package ru.novosoft.uml.behavior.state_machines;

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
import ru.novosoft.uml.behavior.activity_graphs.*;
import ru.novosoft.uml.model_management.*;

import java.lang.reflect.Method;

public abstract class MStateVertexImpl extends MModelElementImpl implements MStateVertex
{
  // ------------ code for class StateVertex -----------------
  // generating attributes
  // generating associations
  // opposite role: incoming this role: target
  private final static Method _incoming_setMethod = getMethod1(MStateVertexImpl.class, "setIncomings", Collection.class);
  private final static Method _incoming_addMethod = getMethod1(MStateVertexImpl.class, "addIncoming", MTransition.class);
  private final static Method _incoming_removeMethod = getMethod1(MStateVertexImpl.class, "removeIncoming", MTransition.class);
  Collection _incoming = Collections.EMPTY_LIST;
  Collection _incoming_ucopy = Collections.EMPTY_LIST;
  public final Collection getIncomings()
  {
    checkExists();
    if (null == _incoming_ucopy)
    {
      _incoming_ucopy = ucopy(_incoming);
    }
    return _incoming_ucopy;
  }
  public final void setIncomings(Collection __arg)
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
        old = getIncomings();
      }
      _incoming_ucopy = null;
      Collection __added = bagdiff(__arg,_incoming);
      Collection __removed = bagdiff(_incoming, __arg);
      Iterator iter1 = __removed.iterator();
      while (iter1.hasNext())
      {
        MTransition o = (MTransition)iter1.next();
        o.internalUnrefByTarget(this);
      }
      Iterator iter2 = __added.iterator();
      while (iter2.hasNext())
      {
        MTransition o = (MTransition)iter2.next();
        o.internalRefByTarget(this);
      }
      _incoming = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_incoming_setMethod, old, getIncomings());
      }
      if (sendEvent)
      {
        fireBagSet("incoming", old, getIncomings());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addIncoming(MTransition __arg)
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
        old = getIncomings();
      }
      if (null != _incoming_ucopy)
      {
        _incoming = new ArrayList(_incoming);
        _incoming_ucopy = null;
      }
      __arg.internalRefByTarget(this);
      _incoming.add(__arg);
      logBagAdd(_incoming_addMethod, _incoming_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("incoming", old, getIncomings(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeIncoming(MTransition __arg)
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
        old = getIncomings();
      }
      if (null != _incoming_ucopy)
      {
        _incoming = new ArrayList(_incoming);
        _incoming_ucopy = null;
      }
      if (!_incoming.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByTarget(this);
      logBagRemove(_incoming_removeMethod, _incoming_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("incoming", old, getIncomings(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByIncoming(MTransition __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getIncomings();
    }
    if (null != _incoming_ucopy)
    {
      _incoming = new ArrayList(_incoming);
      _incoming_ucopy = null;
    }
    _incoming.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("incoming", old, getIncomings(), __arg);
    }
  }
  public final void internalUnrefByIncoming(MTransition __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getIncomings();
    }
    if (null != _incoming_ucopy)
    {
      _incoming = new ArrayList(_incoming);
      _incoming_ucopy = null;
    }
    _incoming.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("incoming", old, getIncomings(), __arg);
    }
  }
  // opposite role: outgoing this role: source
  private final static Method _outgoing_setMethod = getMethod1(MStateVertexImpl.class, "setOutgoings", Collection.class);
  private final static Method _outgoing_addMethod = getMethod1(MStateVertexImpl.class, "addOutgoing", MTransition.class);
  private final static Method _outgoing_removeMethod = getMethod1(MStateVertexImpl.class, "removeOutgoing", MTransition.class);
  Collection _outgoing = Collections.EMPTY_LIST;
  Collection _outgoing_ucopy = Collections.EMPTY_LIST;
  public final Collection getOutgoings()
  {
    checkExists();
    if (null == _outgoing_ucopy)
    {
      _outgoing_ucopy = ucopy(_outgoing);
    }
    return _outgoing_ucopy;
  }
  public final void setOutgoings(Collection __arg)
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
        old = getOutgoings();
      }
      _outgoing_ucopy = null;
      Collection __added = bagdiff(__arg,_outgoing);
      Collection __removed = bagdiff(_outgoing, __arg);
      Iterator iter3 = __removed.iterator();
      while (iter3.hasNext())
      {
        MTransition o = (MTransition)iter3.next();
        o.internalUnrefBySource(this);
      }
      Iterator iter4 = __added.iterator();
      while (iter4.hasNext())
      {
        MTransition o = (MTransition)iter4.next();
        o.internalRefBySource(this);
      }
      _outgoing = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_outgoing_setMethod, old, getOutgoings());
      }
      if (sendEvent)
      {
        fireBagSet("outgoing", old, getOutgoings());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addOutgoing(MTransition __arg)
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
        old = getOutgoings();
      }
      if (null != _outgoing_ucopy)
      {
        _outgoing = new ArrayList(_outgoing);
        _outgoing_ucopy = null;
      }
      __arg.internalRefBySource(this);
      _outgoing.add(__arg);
      logBagAdd(_outgoing_addMethod, _outgoing_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("outgoing", old, getOutgoings(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeOutgoing(MTransition __arg)
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
        old = getOutgoings();
      }
      if (null != _outgoing_ucopy)
      {
        _outgoing = new ArrayList(_outgoing);
        _outgoing_ucopy = null;
      }
      if (!_outgoing.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefBySource(this);
      logBagRemove(_outgoing_removeMethod, _outgoing_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("outgoing", old, getOutgoings(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByOutgoing(MTransition __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getOutgoings();
    }
    if (null != _outgoing_ucopy)
    {
      _outgoing = new ArrayList(_outgoing);
      _outgoing_ucopy = null;
    }
    _outgoing.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("outgoing", old, getOutgoings(), __arg);
    }
  }
  public final void internalUnrefByOutgoing(MTransition __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getOutgoings();
    }
    if (null != _outgoing_ucopy)
    {
      _outgoing = new ArrayList(_outgoing);
      _outgoing_ucopy = null;
    }
    _outgoing.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("outgoing", old, getOutgoings(), __arg);
    }
  }
  // opposite role: container this role: subvertex
  private final static Method _container_setMethod = getMethod1(MStateVertexImpl.class, "setContainer", MCompositeState.class);
  MCompositeState _container;
  public final MCompositeState getContainer()
  {
    checkExists();
    return _container;
  }
  public final void setContainer(MCompositeState __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      MCompositeState __saved = _container;
      if (_container != __arg)
      {
        if (__saved != null)
        {
          __saved.internalUnrefBySubvertex(this);
        }
        if (__arg != null)
        {
          __arg.internalRefBySubvertex(this);
        }
        logRefSet(_container_setMethod, __saved, __arg);
        fireRefSet("container", __saved, __arg);
        _container = __arg;
        setModelElementContainer(_container, "container");
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByContainer(MCompositeState __arg)
  {
    MCompositeState __saved = _container;
    if (_container != null)
    {
      _container.removeSubvertex(this);
    }
    fireRefSet("container", __saved, __arg);
    _container = __arg;
    setModelElementContainer(_container, "container");
  }
  public final void internalUnrefByContainer(MCompositeState __arg)
  {
    fireRefSet("container", _container, null);
    _container = null;
    setModelElementContainer(null, null);
  }
  protected void cleanup(Collection scheduledForRemove)
  {
    // opposite role: incoming this role: target
    if (_incoming.size() != 0)
    {
      setIncomings(Collections.EMPTY_LIST);
    }
    // opposite role: outgoing this role: source
    if (_outgoing.size() != 0)
    {
      setOutgoings(Collections.EMPTY_LIST);
    }
    // opposite role: container this role: subvertex
    if (_container != null )
    {
      setContainer(null);
    }
    super.cleanup(scheduledForRemove);
  }

  public String getUMLClassName()
  {
    return "StateVertex";
  }

  // Reflective API

  public Object reflectiveGetValue(String feature)
  {
    if ("incoming".equals(feature))
    {
      return getIncomings();
    }
    if ("outgoing".equals(feature))
    {
      return getOutgoings();
    }
    if ("container".equals(feature))
    {
      return getContainer();
    }

    return super.reflectiveGetValue(feature);
  }

  public void reflectiveSetValue(String feature, Object obj)
  {
    if ("incoming".equals(feature))
    {
      setIncomings((Collection)obj);
      return;
    }
    if ("outgoing".equals(feature))
    {
      setOutgoings((Collection)obj);
      return;
    }
    if ("container".equals(feature))
    {
      setContainer((MCompositeState)obj);
      return;
    }

    super.reflectiveSetValue(feature, obj);
  }

  public void reflectiveAddValue(String feature, Object obj)
  {
    if ("incoming".equals(feature))
    {
      addIncoming((MTransition)obj);
      return;
    }
    if ("outgoing".equals(feature))
    {
      addOutgoing((MTransition)obj);
      return;
    }

    super.reflectiveAddValue(feature, obj);
  }

  public void reflectiveRemoveValue(String feature, Object obj)
  {
    if ("incoming".equals(feature))
    {
      removeIncoming((MTransition)obj);
      return;
    }
    if ("outgoing".equals(feature))
    {
      removeOutgoing((MTransition)obj);
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
