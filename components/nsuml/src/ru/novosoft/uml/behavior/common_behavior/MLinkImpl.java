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

package ru.novosoft.uml.behavior.common_behavior;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import ru.novosoft.uml.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;
import ru.novosoft.uml.behavior.collaborations.*;
import ru.novosoft.uml.behavior.use_cases.*;
import ru.novosoft.uml.behavior.state_machines.*;
import ru.novosoft.uml.behavior.activity_graphs.*;
import ru.novosoft.uml.model_management.*;

import java.lang.reflect.Method;

public class MLinkImpl extends MModelElementImpl implements MLink
{
  // ------------ code for class Link -----------------
  // generating attributes
  // generating associations
  // opposite role: stimulus this role: communicationLink
  private final static Method _stimulus_setMethod = getMethod1(MLinkImpl.class, "setStimuli", Collection.class);
  private final static Method _stimulus_addMethod = getMethod1(MLinkImpl.class, "addStimulus", MStimulus.class);
  private final static Method _stimulus_removeMethod = getMethod1(MLinkImpl.class, "removeStimulus", MStimulus.class);
  Collection _stimulus = Collections.EMPTY_LIST;
  Collection _stimulus_ucopy = Collections.EMPTY_LIST;
  public final Collection getStimuli()
  {
    checkExists();
    if (null == _stimulus_ucopy)
    {
      _stimulus_ucopy = ucopy(_stimulus);
    }
    return _stimulus_ucopy;
  }
  public final void setStimuli(Collection __arg)
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
        old = getStimuli();
      }
      _stimulus_ucopy = null;
      Collection __added = bagdiff(__arg,_stimulus);
      Collection __removed = bagdiff(_stimulus, __arg);
      Iterator iter1 = __removed.iterator();
      while (iter1.hasNext())
      {
        MStimulus o = (MStimulus)iter1.next();
        o.internalUnrefByCommunicationLink(this);
      }
      Iterator iter2 = __added.iterator();
      while (iter2.hasNext())
      {
        MStimulus o = (MStimulus)iter2.next();
        o.internalRefByCommunicationLink(this);
      }
      _stimulus = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_stimulus_setMethod, old, getStimuli());
      }
      if (sendEvent)
      {
        fireBagSet("stimulus", old, getStimuli());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addStimulus(MStimulus __arg)
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
        old = getStimuli();
      }
      if (null != _stimulus_ucopy)
      {
        _stimulus = new ArrayList(_stimulus);
        _stimulus_ucopy = null;
      }
      __arg.internalRefByCommunicationLink(this);
      _stimulus.add(__arg);
      logBagAdd(_stimulus_addMethod, _stimulus_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("stimulus", old, getStimuli(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeStimulus(MStimulus __arg)
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
        old = getStimuli();
      }
      if (null != _stimulus_ucopy)
      {
        _stimulus = new ArrayList(_stimulus);
        _stimulus_ucopy = null;
      }
      if (!_stimulus.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByCommunicationLink(this);
      logBagRemove(_stimulus_removeMethod, _stimulus_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("stimulus", old, getStimuli(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByStimulus(MStimulus __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getStimuli();
    }
    if (null != _stimulus_ucopy)
    {
      _stimulus = new ArrayList(_stimulus);
      _stimulus_ucopy = null;
    }
    _stimulus.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("stimulus", old, getStimuli(), __arg);
    }
  }
  public final void internalUnrefByStimulus(MStimulus __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getStimuli();
    }
    if (null != _stimulus_ucopy)
    {
      _stimulus = new ArrayList(_stimulus);
      _stimulus_ucopy = null;
    }
    _stimulus.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("stimulus", old, getStimuli(), __arg);
    }
  }
  // opposite role: connection this role: link
  private final static Method _connection_setMethod = getMethod1(MLinkImpl.class, "setConnections", Collection.class);
  private final static Method _connection_addMethod = getMethod1(MLinkImpl.class, "addConnection", MLinkEnd.class);
  private final static Method _connection_removeMethod = getMethod1(MLinkImpl.class, "removeConnection", MLinkEnd.class);
  Collection _connection = Collections.EMPTY_LIST;
  Collection _connection_ucopy = Collections.EMPTY_LIST;
  public final Collection getConnections()
  {
    checkExists();
    if (null == _connection_ucopy)
    {
      _connection_ucopy = ucopy(_connection);
    }
    return _connection_ucopy;
  }
  public final void setConnections(Collection __arg)
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
        old = getConnections();
      }
      _connection_ucopy = null;
      Collection __added = bagdiff(__arg,_connection);
      Collection __removed = bagdiff(_connection, __arg);
      Iterator iter3 = __removed.iterator();
      while (iter3.hasNext())
      {
        MLinkEnd o = (MLinkEnd)iter3.next();
        o.internalUnrefByLink(this);
      }
      Iterator iter4 = __added.iterator();
      while (iter4.hasNext())
      {
        MLinkEnd o = (MLinkEnd)iter4.next();
        o.internalRefByLink(this);
      }
      _connection = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_connection_setMethod, old, getConnections());
      }
      if (sendEvent)
      {
        fireBagSet("connection", old, getConnections());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addConnection(MLinkEnd __arg)
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
        old = getConnections();
      }
      if (null != _connection_ucopy)
      {
        _connection = new ArrayList(_connection);
        _connection_ucopy = null;
      }
      __arg.internalRefByLink(this);
      _connection.add(__arg);
      logBagAdd(_connection_addMethod, _connection_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("connection", old, getConnections(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeConnection(MLinkEnd __arg)
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
        old = getConnections();
      }
      if (null != _connection_ucopy)
      {
        _connection = new ArrayList(_connection);
        _connection_ucopy = null;
      }
      if (!_connection.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByLink(this);
      logBagRemove(_connection_removeMethod, _connection_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("connection", old, getConnections(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByConnection(MLinkEnd __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getConnections();
    }
    if (null != _connection_ucopy)
    {
      _connection = new ArrayList(_connection);
      _connection_ucopy = null;
    }
    _connection.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("connection", old, getConnections(), __arg);
    }
  }
  public final void internalUnrefByConnection(MLinkEnd __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getConnections();
    }
    if (null != _connection_ucopy)
    {
      _connection = new ArrayList(_connection);
      _connection_ucopy = null;
    }
    _connection.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("connection", old, getConnections(), __arg);
    }
  }
  // opposite role: association this role: link
  private final static Method _association_setMethod = getMethod1(MLinkImpl.class, "setAssociation", MAssociation.class);
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
      MAssociation __saved = _association;
      if (_association != __arg)
      {
        if (__saved != null)
        {
          __saved.internalUnrefByLink(this);
        }
        if (__arg != null)
        {
          __arg.internalRefByLink(this);
        }
        logRefSet(_association_setMethod, __saved, __arg);
        fireRefSet("association", __saved, __arg);
        _association = __arg;
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
      _association.removeLink(this);
    }
    fireRefSet("association", __saved, __arg);
    _association = __arg;
  }
  public final void internalUnrefByAssociation(MAssociation __arg)
  {
    fireRefSet("association", _association, null);
    _association = null;
  }
  protected void cleanup(Collection scheduledForRemove)
  {
    // opposite role: stimulus this role: communicationLink
    if (_stimulus.size() != 0)
    {
      setStimuli(Collections.EMPTY_LIST);
    }
    // opposite role: connection this role: link
    if (_connection.size() != 0)
    {
      scheduledForRemove.addAll(_connection);
      setConnections(Collections.EMPTY_LIST);
    }
    // opposite role: association this role: link
    if (_association != null )
    {
      setAssociation(null);
    }
    super.cleanup(scheduledForRemove);
  }

  public String getUMLClassName()
  {
    return "Link";
  }

  // Reflective API

  public Object reflectiveGetValue(String feature)
  {
    if ("stimulus".equals(feature))
    {
      return getStimuli();
    }
    if ("connection".equals(feature))
    {
      return getConnections();
    }
    if ("association".equals(feature))
    {
      return getAssociation();
    }

    return super.reflectiveGetValue(feature);
  }

  public void reflectiveSetValue(String feature, Object obj)
  {
    if ("stimulus".equals(feature))
    {
      setStimuli((Collection)obj);
      return;
    }
    if ("connection".equals(feature))
    {
      setConnections((Collection)obj);
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
    if ("stimulus".equals(feature))
    {
      addStimulus((MStimulus)obj);
      return;
    }
    if ("connection".equals(feature))
    {
      addConnection((MLinkEnd)obj);
      return;
    }

    super.reflectiveAddValue(feature, obj);
  }

  public void reflectiveRemoveValue(String feature, Object obj)
  {
    if ("stimulus".equals(feature))
    {
      removeStimulus((MStimulus)obj);
      return;
    }
    if ("connection".equals(feature))
    {
      removeConnection((MLinkEnd)obj);
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
    ret.addAll(getConnections());
    return ret;
  }
}
