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

public class MInteractionImpl extends MModelElementImpl implements MInteraction
{
  // ------------ code for class Interaction -----------------
  // generating attributes
  // generating associations
  // opposite role: context this role: interaction
  private final static Method _context_setMethod = getMethod1(MInteractionImpl.class, "setContext", MCollaboration.class);
  MCollaboration _context;
  public final MCollaboration getContext()
  {
    checkExists();
    return _context;
  }
  public final void setContext(MCollaboration __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      MCollaboration __saved = _context;
      if (_context != __arg)
      {
        if (__saved != null)
        {
          __saved.internalUnrefByInteraction(this);
        }
        if (__arg != null)
        {
          __arg.internalRefByInteraction(this);
        }
        logRefSet(_context_setMethod, __saved, __arg);
        fireRefSet("context", __saved, __arg);
        _context = __arg;
        setModelElementContainer(_context, "context");
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByContext(MCollaboration __arg)
  {
    MCollaboration __saved = _context;
    if (_context != null)
    {
      _context.removeInteraction(this);
    }
    fireRefSet("context", __saved, __arg);
    _context = __arg;
    setModelElementContainer(_context, "context");
  }
  public final void internalUnrefByContext(MCollaboration __arg)
  {
    fireRefSet("context", _context, null);
    _context = null;
    setModelElementContainer(null, null);
  }
  // opposite role: message this role: interaction
  private final static Method _message_setMethod = getMethod1(MInteractionImpl.class, "setMessages", Collection.class);
  private final static Method _message_addMethod = getMethod1(MInteractionImpl.class, "addMessage", MMessage.class);
  private final static Method _message_removeMethod = getMethod1(MInteractionImpl.class, "removeMessage", MMessage.class);
  Collection _message = Collections.EMPTY_LIST;
  Collection _message_ucopy = Collections.EMPTY_LIST;
  public final Collection getMessages()
  {
    checkExists();
    if (null == _message_ucopy)
    {
      _message_ucopy = ucopy(_message);
    }
    return _message_ucopy;
  }
  public final void setMessages(Collection __arg)
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
        old = getMessages();
      }
      _message_ucopy = null;
      Collection __added = bagdiff(__arg,_message);
      Collection __removed = bagdiff(_message, __arg);
      Iterator iter1 = __removed.iterator();
      while (iter1.hasNext())
      {
        MMessage o = (MMessage)iter1.next();
        o.internalUnrefByInteraction(this);
      }
      Iterator iter2 = __added.iterator();
      while (iter2.hasNext())
      {
        MMessage o = (MMessage)iter2.next();
        o.internalRefByInteraction(this);
      }
      _message = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_message_setMethod, old, getMessages());
      }
      if (sendEvent)
      {
        fireBagSet("message", old, getMessages());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addMessage(MMessage __arg)
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
        old = getMessages();
      }
      if (null != _message_ucopy)
      {
        _message = new ArrayList(_message);
        _message_ucopy = null;
      }
      __arg.internalRefByInteraction(this);
      _message.add(__arg);
      logBagAdd(_message_addMethod, _message_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("message", old, getMessages(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeMessage(MMessage __arg)
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
        old = getMessages();
      }
      if (null != _message_ucopy)
      {
        _message = new ArrayList(_message);
        _message_ucopy = null;
      }
      if (!_message.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByInteraction(this);
      logBagRemove(_message_removeMethod, _message_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("message", old, getMessages(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByMessage(MMessage __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getMessages();
    }
    if (null != _message_ucopy)
    {
      _message = new ArrayList(_message);
      _message_ucopy = null;
    }
    _message.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("message", old, getMessages(), __arg);
    }
  }
  public final void internalUnrefByMessage(MMessage __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getMessages();
    }
    if (null != _message_ucopy)
    {
      _message = new ArrayList(_message);
      _message_ucopy = null;
    }
    _message.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("message", old, getMessages(), __arg);
    }
  }
  protected void cleanup(Collection scheduledForRemove)
  {
    // opposite role: context this role: interaction
    if (_context != null )
    {
      setContext(null);
    }
    // opposite role: message this role: interaction
    if (_message.size() != 0)
    {
      scheduledForRemove.addAll(_message);
      setMessages(Collections.EMPTY_LIST);
    }
    super.cleanup(scheduledForRemove);
  }

  public String getUMLClassName()
  {
    return "Interaction";
  }

  // Reflective API

  public Object reflectiveGetValue(String feature)
  {
    if ("context".equals(feature))
    {
      return getContext();
    }
    if ("message".equals(feature))
    {
      return getMessages();
    }

    return super.reflectiveGetValue(feature);
  }

  public void reflectiveSetValue(String feature, Object obj)
  {
    if ("context".equals(feature))
    {
      setContext((MCollaboration)obj);
      return;
    }
    if ("message".equals(feature))
    {
      setMessages((Collection)obj);
      return;
    }

    super.reflectiveSetValue(feature, obj);
  }

  public void reflectiveAddValue(String feature, Object obj)
  {
    if ("message".equals(feature))
    {
      addMessage((MMessage)obj);
      return;
    }

    super.reflectiveAddValue(feature, obj);
  }

  public void reflectiveRemoveValue(String feature, Object obj)
  {
    if ("message".equals(feature))
    {
      removeMessage((MMessage)obj);
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
    ret.addAll(getMessages());
    return ret;
  }
}
