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

public class MStimulusImpl extends MModelElementImpl implements MStimulus
{
  // ------------ code for class Stimulus -----------------
  // generating attributes
  // generating associations
  // opposite role: dispatchAction this role: stimulus
  private final static Method _dispatchAction_setMethod = getMethod1(MStimulusImpl.class, "setDispatchAction", MAction.class);
  MAction _dispatchAction;
  public final MAction getDispatchAction()
  {
    checkExists();
    return _dispatchAction;
  }
  public final void setDispatchAction(MAction __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      MAction __saved = _dispatchAction;
      if (_dispatchAction != __arg)
      {
        if (__saved != null)
        {
          __saved.internalUnrefByStimulus(this);
        }
        if (__arg != null)
        {
          __arg.internalRefByStimulus(this);
        }
        logRefSet(_dispatchAction_setMethod, __saved, __arg);
        fireRefSet("dispatchAction", __saved, __arg);
        _dispatchAction = __arg;
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByDispatchAction(MAction __arg)
  {
    MAction __saved = _dispatchAction;
    if (_dispatchAction != null)
    {
      _dispatchAction.removeStimulus(this);
    }
    fireRefSet("dispatchAction", __saved, __arg);
    _dispatchAction = __arg;
  }
  public final void internalUnrefByDispatchAction(MAction __arg)
  {
    fireRefSet("dispatchAction", _dispatchAction, null);
    _dispatchAction = null;
  }
  // opposite role: communicationLink this role: stimulus
  private final static Method _communicationLink_setMethod = getMethod1(MStimulusImpl.class, "setCommunicationLink", MLink.class);
  MLink _communicationLink;
  public final MLink getCommunicationLink()
  {
    checkExists();
    return _communicationLink;
  }
  public final void setCommunicationLink(MLink __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      MLink __saved = _communicationLink;
      if (_communicationLink != __arg)
      {
        if (__saved != null)
        {
          __saved.internalUnrefByStimulus(this);
        }
        if (__arg != null)
        {
          __arg.internalRefByStimulus(this);
        }
        logRefSet(_communicationLink_setMethod, __saved, __arg);
        fireRefSet("communicationLink", __saved, __arg);
        _communicationLink = __arg;
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByCommunicationLink(MLink __arg)
  {
    MLink __saved = _communicationLink;
    if (_communicationLink != null)
    {
      _communicationLink.removeStimulus(this);
    }
    fireRefSet("communicationLink", __saved, __arg);
    _communicationLink = __arg;
  }
  public final void internalUnrefByCommunicationLink(MLink __arg)
  {
    fireRefSet("communicationLink", _communicationLink, null);
    _communicationLink = null;
  }
  // opposite role: receiver this role: stimulus2
  private final static Method _receiver_setMethod = getMethod1(MStimulusImpl.class, "setReceiver", MInstance.class);
  MInstance _receiver;
  public final MInstance getReceiver()
  {
    checkExists();
    return _receiver;
  }
  public final void setReceiver(MInstance __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      MInstance __saved = _receiver;
      if (_receiver != __arg)
      {
        if (__saved != null)
        {
          __saved.internalUnrefByStimulus2(this);
        }
        if (__arg != null)
        {
          __arg.internalRefByStimulus2(this);
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
  public final void internalRefByReceiver(MInstance __arg)
  {
    MInstance __saved = _receiver;
    if (_receiver != null)
    {
      _receiver.removeStimulus2(this);
    }
    fireRefSet("receiver", __saved, __arg);
    _receiver = __arg;
  }
  public final void internalUnrefByReceiver(MInstance __arg)
  {
    fireRefSet("receiver", _receiver, null);
    _receiver = null;
  }
  // opposite role: sender this role: stimulus3
  private final static Method _sender_setMethod = getMethod1(MStimulusImpl.class, "setSender", MInstance.class);
  MInstance _sender;
  public final MInstance getSender()
  {
    checkExists();
    return _sender;
  }
  public final void setSender(MInstance __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      MInstance __saved = _sender;
      if (_sender != __arg)
      {
        if (__saved != null)
        {
          __saved.internalUnrefByStimulus3(this);
        }
        if (__arg != null)
        {
          __arg.internalRefByStimulus3(this);
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
  public final void internalRefBySender(MInstance __arg)
  {
    MInstance __saved = _sender;
    if (_sender != null)
    {
      _sender.removeStimulus3(this);
    }
    fireRefSet("sender", __saved, __arg);
    _sender = __arg;
  }
  public final void internalUnrefBySender(MInstance __arg)
  {
    fireRefSet("sender", _sender, null);
    _sender = null;
  }
  // opposite role: argument this role: stimulus1
  private final static Method _argument_setMethod = getMethod1(MStimulusImpl.class, "setArguments", Collection.class);
  private final static Method _argument_addMethod = getMethod1(MStimulusImpl.class, "addArgument", MInstance.class);
  private final static Method _argument_removeMethod = getMethod1(MStimulusImpl.class, "removeArgument", MInstance.class);
  Collection _argument = Collections.EMPTY_LIST;
  Collection _argument_ucopy = Collections.EMPTY_LIST;
  public final Collection getArguments()
  {
    checkExists();
    if (null == _argument_ucopy)
    {
      _argument_ucopy = ucopy(_argument);
    }
    return _argument_ucopy;
  }
  public final void setArguments(Collection __arg)
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
        old = getArguments();
      }
      _argument_ucopy = null;
      Collection __added = bagdiff(__arg,_argument);
      Collection __removed = bagdiff(_argument, __arg);
      Iterator iter1 = __removed.iterator();
      while (iter1.hasNext())
      {
        MInstance o = (MInstance)iter1.next();
        o.internalUnrefByStimulus1(this);
      }
      Iterator iter2 = __added.iterator();
      while (iter2.hasNext())
      {
        MInstance o = (MInstance)iter2.next();
        o.internalRefByStimulus1(this);
      }
      _argument = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_argument_setMethod, old, getArguments());
      }
      if (sendEvent)
      {
        fireBagSet("argument", old, getArguments());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addArgument(MInstance __arg)
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
        old = getArguments();
      }
      if (null != _argument_ucopy)
      {
        _argument = new ArrayList(_argument);
        _argument_ucopy = null;
      }
      __arg.internalRefByStimulus1(this);
      _argument.add(__arg);
      logBagAdd(_argument_addMethod, _argument_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("argument", old, getArguments(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeArgument(MInstance __arg)
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
        old = getArguments();
      }
      if (null != _argument_ucopy)
      {
        _argument = new ArrayList(_argument);
        _argument_ucopy = null;
      }
      if (!_argument.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByStimulus1(this);
      logBagRemove(_argument_removeMethod, _argument_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("argument", old, getArguments(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByArgument(MInstance __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getArguments();
    }
    if (null != _argument_ucopy)
    {
      _argument = new ArrayList(_argument);
      _argument_ucopy = null;
    }
    _argument.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("argument", old, getArguments(), __arg);
    }
  }
  public final void internalUnrefByArgument(MInstance __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getArguments();
    }
    if (null != _argument_ucopy)
    {
      _argument = new ArrayList(_argument);
      _argument_ucopy = null;
    }
    _argument.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("argument", old, getArguments(), __arg);
    }
  }
  protected void cleanup(Collection scheduledForRemove)
  {
    // opposite role: dispatchAction this role: stimulus
    if (_dispatchAction != null )
    {
      setDispatchAction(null);
    }
    // opposite role: communicationLink this role: stimulus
    if (_communicationLink != null )
    {
      setCommunicationLink(null);
    }
    // opposite role: receiver this role: stimulus2
    if (_receiver != null )
    {
      setReceiver(null);
    }
    // opposite role: sender this role: stimulus3
    if (_sender != null )
    {
      setSender(null);
    }
    // opposite role: argument this role: stimulus1
    if (_argument.size() != 0)
    {
      setArguments(Collections.EMPTY_LIST);
    }
    super.cleanup(scheduledForRemove);
  }

  public String getUMLClassName()
  {
    return "Stimulus";
  }

  // Reflective API

  public Object reflectiveGetValue(String feature)
  {
    if ("dispatchAction".equals(feature))
    {
      return getDispatchAction();
    }
    if ("communicationLink".equals(feature))
    {
      return getCommunicationLink();
    }
    if ("receiver".equals(feature))
    {
      return getReceiver();
    }
    if ("sender".equals(feature))
    {
      return getSender();
    }
    if ("argument".equals(feature))
    {
      return getArguments();
    }

    return super.reflectiveGetValue(feature);
  }

  public void reflectiveSetValue(String feature, Object obj)
  {
    if ("dispatchAction".equals(feature))
    {
      setDispatchAction((MAction)obj);
      return;
    }
    if ("communicationLink".equals(feature))
    {
      setCommunicationLink((MLink)obj);
      return;
    }
    if ("receiver".equals(feature))
    {
      setReceiver((MInstance)obj);
      return;
    }
    if ("sender".equals(feature))
    {
      setSender((MInstance)obj);
      return;
    }
    if ("argument".equals(feature))
    {
      setArguments((Collection)obj);
      return;
    }

    super.reflectiveSetValue(feature, obj);
  }

  public void reflectiveAddValue(String feature, Object obj)
  {
    if ("argument".equals(feature))
    {
      addArgument((MInstance)obj);
      return;
    }

    super.reflectiveAddValue(feature, obj);
  }

  public void reflectiveRemoveValue(String feature, Object obj)
  {
    if ("argument".equals(feature))
    {
      removeArgument((MInstance)obj);
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
