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

public class MMessageImpl extends MModelElementImpl implements MMessage
{
  // ------------ code for class Message -----------------
  // generating attributes
  // generating associations
  // opposite role: action this role: message
  private final static Method _action_setMethod = getMethod1(MMessageImpl.class, "setAction", MAction.class);
  MAction _action;
  public final MAction getAction()
  {
    checkExists();
    return _action;
  }
  public final void setAction(MAction __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      MAction __saved = _action;
      if (_action != __arg)
      {
        if (__saved != null)
        {
          __saved.internalUnrefByMessage(this);
        }
        if (__arg != null)
        {
          __arg.internalRefByMessage(this);
        }
        logRefSet(_action_setMethod, __saved, __arg);
        fireRefSet("action", __saved, __arg);
        _action = __arg;
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByAction(MAction __arg)
  {
    MAction __saved = _action;
    if (_action != null)
    {
      _action.removeMessage(this);
    }
    fireRefSet("action", __saved, __arg);
    _action = __arg;
  }
  public final void internalUnrefByAction(MAction __arg)
  {
    fireRefSet("action", _action, null);
    _action = null;
  }
  // opposite role: communicationConnection this role: message
  private final static Method _communicationConnection_setMethod = getMethod1(MMessageImpl.class, "setCommunicationConnection", MAssociationRole.class);
  MAssociationRole _communicationConnection;
  public final MAssociationRole getCommunicationConnection()
  {
    checkExists();
    return _communicationConnection;
  }
  public final void setCommunicationConnection(MAssociationRole __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      MAssociationRole __saved = _communicationConnection;
      if (_communicationConnection != __arg)
      {
        if (__saved != null)
        {
          __saved.internalUnrefByMessage(this);
        }
        if (__arg != null)
        {
          __arg.internalRefByMessage(this);
        }
        logRefSet(_communicationConnection_setMethod, __saved, __arg);
        fireRefSet("communicationConnection", __saved, __arg);
        _communicationConnection = __arg;
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByCommunicationConnection(MAssociationRole __arg)
  {
    MAssociationRole __saved = _communicationConnection;
    if (_communicationConnection != null)
    {
      _communicationConnection.removeMessage(this);
    }
    fireRefSet("communicationConnection", __saved, __arg);
    _communicationConnection = __arg;
  }
  public final void internalUnrefByCommunicationConnection(MAssociationRole __arg)
  {
    fireRefSet("communicationConnection", _communicationConnection, null);
    _communicationConnection = null;
  }
  // opposite role: message3 this role: predecessor
  private final static Method _message3_setMethod = getMethod1(MMessageImpl.class, "setMessages3", Collection.class);
  private final static Method _message3_addMethod = getMethod1(MMessageImpl.class, "addMessage3", MMessage.class);
  private final static Method _message3_removeMethod = getMethod1(MMessageImpl.class, "removeMessage3", MMessage.class);
  Collection _message3 = Collections.EMPTY_LIST;
  Collection _message3_ucopy = Collections.EMPTY_LIST;
  public final Collection getMessages3()
  {
    checkExists();
    if (null == _message3_ucopy)
    {
      _message3_ucopy = ucopy(_message3);
    }
    return _message3_ucopy;
  }
  public final void setMessages3(Collection __arg)
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
        old = getMessages3();
      }
      _message3_ucopy = null;
      Collection __added = bagdiff(__arg,_message3);
      Collection __removed = bagdiff(_message3, __arg);
      Iterator iter1 = __removed.iterator();
      while (iter1.hasNext())
      {
        MMessage o = (MMessage)iter1.next();
        o.internalUnrefByPredecessor(this);
      }
      Iterator iter2 = __added.iterator();
      while (iter2.hasNext())
      {
        MMessage o = (MMessage)iter2.next();
        o.internalRefByPredecessor(this);
      }
      _message3 = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_message3_setMethod, old, getMessages3());
      }
      if (sendEvent)
      {
        fireBagSet("message3", old, getMessages3());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addMessage3(MMessage __arg)
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
        old = getMessages3();
      }
      if (null != _message3_ucopy)
      {
        _message3 = new ArrayList(_message3);
        _message3_ucopy = null;
      }
      __arg.internalRefByPredecessor(this);
      _message3.add(__arg);
      logBagAdd(_message3_addMethod, _message3_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("message3", old, getMessages3(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeMessage3(MMessage __arg)
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
        old = getMessages3();
      }
      if (null != _message3_ucopy)
      {
        _message3 = new ArrayList(_message3);
        _message3_ucopy = null;
      }
      if (!_message3.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByPredecessor(this);
      logBagRemove(_message3_removeMethod, _message3_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("message3", old, getMessages3(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByMessage3(MMessage __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getMessages3();
    }
    if (null != _message3_ucopy)
    {
      _message3 = new ArrayList(_message3);
      _message3_ucopy = null;
    }
    _message3.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("message3", old, getMessages3(), __arg);
    }
  }
  public final void internalUnrefByMessage3(MMessage __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getMessages3();
    }
    if (null != _message3_ucopy)
    {
      _message3 = new ArrayList(_message3);
      _message3_ucopy = null;
    }
    _message3.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("message3", old, getMessages3(), __arg);
    }
  }
  // opposite role: predecessor this role: message3
  private final static Method _predecessor_setMethod = getMethod1(MMessageImpl.class, "setPredecessors", Collection.class);
  private final static Method _predecessor_addMethod = getMethod1(MMessageImpl.class, "addPredecessor", MMessage.class);
  private final static Method _predecessor_removeMethod = getMethod1(MMessageImpl.class, "removePredecessor", MMessage.class);
  Collection _predecessor = Collections.EMPTY_LIST;
  Collection _predecessor_ucopy = Collections.EMPTY_LIST;
  public final Collection getPredecessors()
  {
    checkExists();
    if (null == _predecessor_ucopy)
    {
      _predecessor_ucopy = ucopy(_predecessor);
    }
    return _predecessor_ucopy;
  }
  public final void setPredecessors(Collection __arg)
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
        old = getPredecessors();
      }
      _predecessor_ucopy = null;
      Collection __added = bagdiff(__arg,_predecessor);
      Collection __removed = bagdiff(_predecessor, __arg);
      Iterator iter3 = __removed.iterator();
      while (iter3.hasNext())
      {
        MMessage o = (MMessage)iter3.next();
        o.internalUnrefByMessage3(this);
      }
      Iterator iter4 = __added.iterator();
      while (iter4.hasNext())
      {
        MMessage o = (MMessage)iter4.next();
        o.internalRefByMessage3(this);
      }
      _predecessor = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_predecessor_setMethod, old, getPredecessors());
      }
      if (sendEvent)
      {
        fireBagSet("predecessor", old, getPredecessors());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addPredecessor(MMessage __arg)
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
        old = getPredecessors();
      }
      if (null != _predecessor_ucopy)
      {
        _predecessor = new ArrayList(_predecessor);
        _predecessor_ucopy = null;
      }
      __arg.internalRefByMessage3(this);
      _predecessor.add(__arg);
      logBagAdd(_predecessor_addMethod, _predecessor_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("predecessor", old, getPredecessors(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removePredecessor(MMessage __arg)
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
        old = getPredecessors();
      }
      if (null != _predecessor_ucopy)
      {
        _predecessor = new ArrayList(_predecessor);
        _predecessor_ucopy = null;
      }
      if (!_predecessor.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByMessage3(this);
      logBagRemove(_predecessor_removeMethod, _predecessor_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("predecessor", old, getPredecessors(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByPredecessor(MMessage __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getPredecessors();
    }
    if (null != _predecessor_ucopy)
    {
      _predecessor = new ArrayList(_predecessor);
      _predecessor_ucopy = null;
    }
    _predecessor.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("predecessor", old, getPredecessors(), __arg);
    }
  }
  public final void internalUnrefByPredecessor(MMessage __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getPredecessors();
    }
    if (null != _predecessor_ucopy)
    {
      _predecessor = new ArrayList(_predecessor);
      _predecessor_ucopy = null;
    }
    _predecessor.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("predecessor", old, getPredecessors(), __arg);
    }
  }
  // opposite role: receiver this role: message1
  private final static Method _receiver_setMethod = getMethod1(MMessageImpl.class, "setReceiver", MClassifierRole.class);
  MClassifierRole _receiver;
  public final MClassifierRole getReceiver()
  {
    checkExists();
    return _receiver;
  }
  public final void setReceiver(MClassifierRole __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      MClassifierRole __saved = _receiver;
      if (_receiver != __arg)
      {
        if (__saved != null)
        {
          __saved.internalUnrefByMessage1(this);
        }
        if (__arg != null)
        {
          __arg.internalRefByMessage1(this);
        }
        logRefSet(_receiver_setMethod, __saved, __arg);
        fireRefSet("receiver", __saved, __arg);
        _receiver = __arg;
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByReceiver(MClassifierRole __arg)
  {
    MClassifierRole __saved = _receiver;
    if (_receiver != null)
    {
      _receiver.removeMessage1(this);
    }
    fireRefSet("receiver", __saved, __arg);
    _receiver = __arg;
  }
  public final void internalUnrefByReceiver(MClassifierRole __arg)
  {
    fireRefSet("receiver", _receiver, null);
    _receiver = null;
  }
  // opposite role: sender this role: message2
  private final static Method _sender_setMethod = getMethod1(MMessageImpl.class, "setSender", MClassifierRole.class);
  MClassifierRole _sender;
  public final MClassifierRole getSender()
  {
    checkExists();
    return _sender;
  }
  public final void setSender(MClassifierRole __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      MClassifierRole __saved = _sender;
      if (_sender != __arg)
      {
        if (__saved != null)
        {
          __saved.internalUnrefByMessage2(this);
        }
        if (__arg != null)
        {
          __arg.internalRefByMessage2(this);
        }
        logRefSet(_sender_setMethod, __saved, __arg);
        fireRefSet("sender", __saved, __arg);
        _sender = __arg;
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefBySender(MClassifierRole __arg)
  {
    MClassifierRole __saved = _sender;
    if (_sender != null)
    {
      _sender.removeMessage2(this);
    }
    fireRefSet("sender", __saved, __arg);
    _sender = __arg;
  }
  public final void internalUnrefBySender(MClassifierRole __arg)
  {
    fireRefSet("sender", _sender, null);
    _sender = null;
  }
  // opposite role: activator this role: message4
  private final static Method _activator_setMethod = getMethod1(MMessageImpl.class, "setActivator", MMessage.class);
  MMessage _activator;
  public final MMessage getActivator()
  {
    checkExists();
    return _activator;
  }
  public final void setActivator(MMessage __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      MMessage __saved = _activator;
      if (_activator != __arg)
      {
        if (__saved != null)
        {
          __saved.internalUnrefByMessage4(this);
        }
        if (__arg != null)
        {
          __arg.internalRefByMessage4(this);
        }
        logRefSet(_activator_setMethod, __saved, __arg);
        fireRefSet("activator", __saved, __arg);
        _activator = __arg;
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByActivator(MMessage __arg)
  {
    MMessage __saved = _activator;
    if (_activator != null)
    {
      _activator.removeMessage4(this);
    }
    fireRefSet("activator", __saved, __arg);
    _activator = __arg;
  }
  public final void internalUnrefByActivator(MMessage __arg)
  {
    fireRefSet("activator", _activator, null);
    _activator = null;
  }
  // opposite role: message4 this role: activator
  private final static Method _message4_setMethod = getMethod1(MMessageImpl.class, "setMessages4", Collection.class);
  private final static Method _message4_addMethod = getMethod1(MMessageImpl.class, "addMessage4", MMessage.class);
  private final static Method _message4_removeMethod = getMethod1(MMessageImpl.class, "removeMessage4", MMessage.class);
  Collection _message4 = Collections.EMPTY_LIST;
  Collection _message4_ucopy = Collections.EMPTY_LIST;
  public final Collection getMessages4()
  {
    checkExists();
    if (null == _message4_ucopy)
    {
      _message4_ucopy = ucopy(_message4);
    }
    return _message4_ucopy;
  }
  public final void setMessages4(Collection __arg)
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
        old = getMessages4();
      }
      _message4_ucopy = null;
      Collection __added = bagdiff(__arg,_message4);
      Collection __removed = bagdiff(_message4, __arg);
      Iterator iter5 = __removed.iterator();
      while (iter5.hasNext())
      {
        MMessage o = (MMessage)iter5.next();
        o.internalUnrefByActivator(this);
      }
      Iterator iter6 = __added.iterator();
      while (iter6.hasNext())
      {
        MMessage o = (MMessage)iter6.next();
        o.internalRefByActivator(this);
      }
      _message4 = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_message4_setMethod, old, getMessages4());
      }
      if (sendEvent)
      {
        fireBagSet("message4", old, getMessages4());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addMessage4(MMessage __arg)
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
        old = getMessages4();
      }
      if (null != _message4_ucopy)
      {
        _message4 = new ArrayList(_message4);
        _message4_ucopy = null;
      }
      __arg.internalRefByActivator(this);
      _message4.add(__arg);
      logBagAdd(_message4_addMethod, _message4_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("message4", old, getMessages4(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeMessage4(MMessage __arg)
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
        old = getMessages4();
      }
      if (null != _message4_ucopy)
      {
        _message4 = new ArrayList(_message4);
        _message4_ucopy = null;
      }
      if (!_message4.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByActivator(this);
      logBagRemove(_message4_removeMethod, _message4_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("message4", old, getMessages4(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByMessage4(MMessage __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getMessages4();
    }
    if (null != _message4_ucopy)
    {
      _message4 = new ArrayList(_message4);
      _message4_ucopy = null;
    }
    _message4.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("message4", old, getMessages4(), __arg);
    }
  }
  public final void internalUnrefByMessage4(MMessage __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getMessages4();
    }
    if (null != _message4_ucopy)
    {
      _message4 = new ArrayList(_message4);
      _message4_ucopy = null;
    }
    _message4.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("message4", old, getMessages4(), __arg);
    }
  }
  // opposite role: interaction this role: message
  private final static Method _interaction_setMethod = getMethod1(MMessageImpl.class, "setInteraction", MInteraction.class);
  MInteraction _interaction;
  public final MInteraction getInteraction()
  {
    checkExists();
    return _interaction;
  }
  public final void setInteraction(MInteraction __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      MInteraction __saved = _interaction;
      if (_interaction != __arg)
      {
        if (__saved != null)
        {
          __saved.internalUnrefByMessage(this);
        }
        if (__arg != null)
        {
          __arg.internalRefByMessage(this);
        }
        logRefSet(_interaction_setMethod, __saved, __arg);
        fireRefSet("interaction", __saved, __arg);
        _interaction = __arg;
        setModelElementContainer(_interaction, "interaction");
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByInteraction(MInteraction __arg)
  {
    MInteraction __saved = _interaction;
    if (_interaction != null)
    {
      _interaction.removeMessage(this);
    }
    fireRefSet("interaction", __saved, __arg);
    _interaction = __arg;
    setModelElementContainer(_interaction, "interaction");
  }
  public final void internalUnrefByInteraction(MInteraction __arg)
  {
    fireRefSet("interaction", _interaction, null);
    _interaction = null;
    setModelElementContainer(null, null);
  }
  protected void cleanup(Collection scheduledForRemove)
  {
    // opposite role: action this role: message
    if (_action != null )
    {
      setAction(null);
    }
    // opposite role: communicationConnection this role: message
    if (_communicationConnection != null )
    {
      setCommunicationConnection(null);
    }
    // opposite role: message3 this role: predecessor
    if (_message3.size() != 0)
    {
      setMessages3(Collections.EMPTY_LIST);
    }
    // opposite role: predecessor this role: message3
    if (_predecessor.size() != 0)
    {
      setPredecessors(Collections.EMPTY_LIST);
    }
    // opposite role: receiver this role: message1
    if (_receiver != null )
    {
      setReceiver(null);
    }
    // opposite role: sender this role: message2
    if (_sender != null )
    {
      setSender(null);
    }
    // opposite role: activator this role: message4
    if (_activator != null )
    {
      setActivator(null);
    }
    // opposite role: message4 this role: activator
    if (_message4.size() != 0)
    {
      setMessages4(Collections.EMPTY_LIST);
    }
    // opposite role: interaction this role: message
    if (_interaction != null )
    {
      setInteraction(null);
    }
    super.cleanup(scheduledForRemove);
  }

  public String getUMLClassName()
  {
    return "Message";
  }

  // Reflective API

  public Object reflectiveGetValue(String feature)
  {
    if ("action".equals(feature))
    {
      return getAction();
    }
    if ("communicationConnection".equals(feature))
    {
      return getCommunicationConnection();
    }
    if ("message3".equals(feature))
    {
      return getMessages3();
    }
    if ("predecessor".equals(feature))
    {
      return getPredecessors();
    }
    if ("receiver".equals(feature))
    {
      return getReceiver();
    }
    if ("sender".equals(feature))
    {
      return getSender();
    }
    if ("activator".equals(feature))
    {
      return getActivator();
    }
    if ("message4".equals(feature))
    {
      return getMessages4();
    }
    if ("interaction".equals(feature))
    {
      return getInteraction();
    }

    return super.reflectiveGetValue(feature);
  }

  public void reflectiveSetValue(String feature, Object obj)
  {
    if ("action".equals(feature))
    {
      setAction((MAction)obj);
      return;
    }
    if ("communicationConnection".equals(feature))
    {
      setCommunicationConnection((MAssociationRole)obj);
      return;
    }
    if ("message3".equals(feature))
    {
      setMessages3((Collection)obj);
      return;
    }
    if ("predecessor".equals(feature))
    {
      setPredecessors((Collection)obj);
      return;
    }
    if ("receiver".equals(feature))
    {
      setReceiver((MClassifierRole)obj);
      return;
    }
    if ("sender".equals(feature))
    {
      setSender((MClassifierRole)obj);
      return;
    }
    if ("activator".equals(feature))
    {
      setActivator((MMessage)obj);
      return;
    }
    if ("message4".equals(feature))
    {
      setMessages4((Collection)obj);
      return;
    }
    if ("interaction".equals(feature))
    {
      setInteraction((MInteraction)obj);
      return;
    }

    super.reflectiveSetValue(feature, obj);
  }

  public void reflectiveAddValue(String feature, Object obj)
  {
    if ("message3".equals(feature))
    {
      addMessage3((MMessage)obj);
      return;
    }
    if ("predecessor".equals(feature))
    {
      addPredecessor((MMessage)obj);
      return;
    }
    if ("message4".equals(feature))
    {
      addMessage4((MMessage)obj);
      return;
    }

    super.reflectiveAddValue(feature, obj);
  }

  public void reflectiveRemoveValue(String feature, Object obj)
  {
    if ("message3".equals(feature))
    {
      removeMessage3((MMessage)obj);
      return;
    }
    if ("predecessor".equals(feature))
    {
      removePredecessor((MMessage)obj);
      return;
    }
    if ("message4".equals(feature))
    {
      removeMessage4((MMessage)obj);
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
