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

public class MSignalImpl extends MClassifierImpl implements MSignal
{
  // ------------ code for class Signal -----------------
  // generating attributes
  // generating associations
  // opposite role: context this role: raisedSignal
  private final static Method _context_setMethod = getMethod1(MSignalImpl.class, "setContexts", Collection.class);
  private final static Method _context_addMethod = getMethod1(MSignalImpl.class, "addContext", MBehavioralFeature.class);
  private final static Method _context_removeMethod = getMethod1(MSignalImpl.class, "removeContext", MBehavioralFeature.class);
  Collection _context = Collections.EMPTY_LIST;
  Collection _context_ucopy = Collections.EMPTY_LIST;
  public final Collection getContexts()
  {
    checkExists();
    if (null == _context_ucopy)
    {
      _context_ucopy = ucopy(_context);
    }
    return _context_ucopy;
  }
  public final void setContexts(Collection __arg)
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
        old = getContexts();
      }
      _context_ucopy = null;
      Collection __added = bagdiff(__arg,_context);
      Collection __removed = bagdiff(_context, __arg);
      Iterator iter1 = __removed.iterator();
      while (iter1.hasNext())
      {
        MBehavioralFeature o = (MBehavioralFeature)iter1.next();
        o.internalUnrefByRaisedSignal(this);
      }
      Iterator iter2 = __added.iterator();
      while (iter2.hasNext())
      {
        MBehavioralFeature o = (MBehavioralFeature)iter2.next();
        o.internalRefByRaisedSignal(this);
      }
      _context = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_context_setMethod, old, getContexts());
      }
      if (sendEvent)
      {
        fireBagSet("context", old, getContexts());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addContext(MBehavioralFeature __arg)
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
        old = getContexts();
      }
      if (null != _context_ucopy)
      {
        _context = new ArrayList(_context);
        _context_ucopy = null;
      }
      __arg.internalRefByRaisedSignal(this);
      _context.add(__arg);
      logBagAdd(_context_addMethod, _context_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("context", old, getContexts(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeContext(MBehavioralFeature __arg)
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
        old = getContexts();
      }
      if (null != _context_ucopy)
      {
        _context = new ArrayList(_context);
        _context_ucopy = null;
      }
      if (!_context.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByRaisedSignal(this);
      logBagRemove(_context_removeMethod, _context_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("context", old, getContexts(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByContext(MBehavioralFeature __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getContexts();
    }
    if (null != _context_ucopy)
    {
      _context = new ArrayList(_context);
      _context_ucopy = null;
    }
    _context.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("context", old, getContexts(), __arg);
    }
  }
  public final void internalUnrefByContext(MBehavioralFeature __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getContexts();
    }
    if (null != _context_ucopy)
    {
      _context = new ArrayList(_context);
      _context_ucopy = null;
    }
    _context.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("context", old, getContexts(), __arg);
    }
  }
  // opposite role: reception this role: signal
  private final static Method _reception_setMethod = getMethod1(MSignalImpl.class, "setReceptions", Collection.class);
  private final static Method _reception_addMethod = getMethod1(MSignalImpl.class, "addReception", MReception.class);
  private final static Method _reception_removeMethod = getMethod1(MSignalImpl.class, "removeReception", MReception.class);
  Collection _reception = Collections.EMPTY_LIST;
  Collection _reception_ucopy = Collections.EMPTY_LIST;
  public final Collection getReceptions()
  {
    checkExists();
    if (null == _reception_ucopy)
    {
      _reception_ucopy = ucopy(_reception);
    }
    return _reception_ucopy;
  }
  public final void setReceptions(Collection __arg)
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
        old = getReceptions();
      }
      _reception_ucopy = null;
      Collection __added = bagdiff(__arg,_reception);
      Collection __removed = bagdiff(_reception, __arg);
      Iterator iter3 = __removed.iterator();
      while (iter3.hasNext())
      {
        MReception o = (MReception)iter3.next();
        o.internalUnrefBySignal(this);
      }
      Iterator iter4 = __added.iterator();
      while (iter4.hasNext())
      {
        MReception o = (MReception)iter4.next();
        o.internalRefBySignal(this);
      }
      _reception = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_reception_setMethod, old, getReceptions());
      }
      if (sendEvent)
      {
        fireBagSet("reception", old, getReceptions());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addReception(MReception __arg)
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
        old = getReceptions();
      }
      if (null != _reception_ucopy)
      {
        _reception = new ArrayList(_reception);
        _reception_ucopy = null;
      }
      __arg.internalRefBySignal(this);
      _reception.add(__arg);
      logBagAdd(_reception_addMethod, _reception_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("reception", old, getReceptions(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeReception(MReception __arg)
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
        old = getReceptions();
      }
      if (null != _reception_ucopy)
      {
        _reception = new ArrayList(_reception);
        _reception_ucopy = null;
      }
      if (!_reception.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefBySignal(this);
      logBagRemove(_reception_removeMethod, _reception_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("reception", old, getReceptions(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByReception(MReception __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getReceptions();
    }
    if (null != _reception_ucopy)
    {
      _reception = new ArrayList(_reception);
      _reception_ucopy = null;
    }
    _reception.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("reception", old, getReceptions(), __arg);
    }
  }
  public final void internalUnrefByReception(MReception __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getReceptions();
    }
    if (null != _reception_ucopy)
    {
      _reception = new ArrayList(_reception);
      _reception_ucopy = null;
    }
    _reception.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("reception", old, getReceptions(), __arg);
    }
  }
  // opposite role: occurrence this role: signal
  private final static Method _occurrence_setMethod = getMethod1(MSignalImpl.class, "setOccurrences", Collection.class);
  private final static Method _occurrence_addMethod = getMethod1(MSignalImpl.class, "addOccurrence", MSignalEvent.class);
  private final static Method _occurrence_removeMethod = getMethod1(MSignalImpl.class, "removeOccurrence", MSignalEvent.class);
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
        MSignalEvent o = (MSignalEvent)iter5.next();
        o.internalUnrefBySignal(this);
      }
      Iterator iter6 = __added.iterator();
      while (iter6.hasNext())
      {
        MSignalEvent o = (MSignalEvent)iter6.next();
        o.internalRefBySignal(this);
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
  public final void addOccurrence(MSignalEvent __arg)
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
      __arg.internalRefBySignal(this);
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
  public final void removeOccurrence(MSignalEvent __arg)
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
      __arg.internalUnrefBySignal(this);
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
  public final void internalRefByOccurrence(MSignalEvent __arg)
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
  public final void internalUnrefByOccurrence(MSignalEvent __arg)
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
  // opposite role: sendAction this role: signal
  private final static Method _sendAction_setMethod = getMethod1(MSignalImpl.class, "setSendActions", Collection.class);
  private final static Method _sendAction_addMethod = getMethod1(MSignalImpl.class, "addSendAction", MSendAction.class);
  private final static Method _sendAction_removeMethod = getMethod1(MSignalImpl.class, "removeSendAction", MSendAction.class);
  Collection _sendAction = Collections.EMPTY_LIST;
  Collection _sendAction_ucopy = Collections.EMPTY_LIST;
  public final Collection getSendActions()
  {
    checkExists();
    if (null == _sendAction_ucopy)
    {
      _sendAction_ucopy = ucopy(_sendAction);
    }
    return _sendAction_ucopy;
  }
  public final void setSendActions(Collection __arg)
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
        old = getSendActions();
      }
      _sendAction_ucopy = null;
      Collection __added = bagdiff(__arg,_sendAction);
      Collection __removed = bagdiff(_sendAction, __arg);
      Iterator iter7 = __removed.iterator();
      while (iter7.hasNext())
      {
        MSendAction o = (MSendAction)iter7.next();
        o.internalUnrefBySignal(this);
      }
      Iterator iter8 = __added.iterator();
      while (iter8.hasNext())
      {
        MSendAction o = (MSendAction)iter8.next();
        o.internalRefBySignal(this);
      }
      _sendAction = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_sendAction_setMethod, old, getSendActions());
      }
      if (sendEvent)
      {
        fireBagSet("sendAction", old, getSendActions());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addSendAction(MSendAction __arg)
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
        old = getSendActions();
      }
      if (null != _sendAction_ucopy)
      {
        _sendAction = new ArrayList(_sendAction);
        _sendAction_ucopy = null;
      }
      __arg.internalRefBySignal(this);
      _sendAction.add(__arg);
      logBagAdd(_sendAction_addMethod, _sendAction_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("sendAction", old, getSendActions(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeSendAction(MSendAction __arg)
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
        old = getSendActions();
      }
      if (null != _sendAction_ucopy)
      {
        _sendAction = new ArrayList(_sendAction);
        _sendAction_ucopy = null;
      }
      if (!_sendAction.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefBySignal(this);
      logBagRemove(_sendAction_removeMethod, _sendAction_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("sendAction", old, getSendActions(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefBySendAction(MSendAction __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getSendActions();
    }
    if (null != _sendAction_ucopy)
    {
      _sendAction = new ArrayList(_sendAction);
      _sendAction_ucopy = null;
    }
    _sendAction.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("sendAction", old, getSendActions(), __arg);
    }
  }
  public final void internalUnrefBySendAction(MSendAction __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getSendActions();
    }
    if (null != _sendAction_ucopy)
    {
      _sendAction = new ArrayList(_sendAction);
      _sendAction_ucopy = null;
    }
    _sendAction.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("sendAction", old, getSendActions(), __arg);
    }
  }
  protected void cleanup(Collection scheduledForRemove)
  {
    // opposite role: context this role: raisedSignal
    if (_context.size() != 0)
    {
      setContexts(Collections.EMPTY_LIST);
    }
    // opposite role: reception this role: signal
    if (_reception.size() != 0)
    {
      setReceptions(Collections.EMPTY_LIST);
    }
    // opposite role: occurrence this role: signal
    if (_occurrence.size() != 0)
    {
      setOccurrences(Collections.EMPTY_LIST);
    }
    // opposite role: sendAction this role: signal
    if (_sendAction.size() != 0)
    {
      setSendActions(Collections.EMPTY_LIST);
    }
    super.cleanup(scheduledForRemove);
  }

  public String getUMLClassName()
  {
    return "Signal";
  }

  // Reflective API

  public Object reflectiveGetValue(String feature)
  {
    if ("context".equals(feature))
    {
      return getContexts();
    }
    if ("reception".equals(feature))
    {
      return getReceptions();
    }
    if ("occurrence".equals(feature))
    {
      return getOccurrences();
    }
    if ("sendAction".equals(feature))
    {
      return getSendActions();
    }

    return super.reflectiveGetValue(feature);
  }

  public void reflectiveSetValue(String feature, Object obj)
  {
    if ("context".equals(feature))
    {
      setContexts((Collection)obj);
      return;
    }
    if ("reception".equals(feature))
    {
      setReceptions((Collection)obj);
      return;
    }
    if ("occurrence".equals(feature))
    {
      setOccurrences((Collection)obj);
      return;
    }
    if ("sendAction".equals(feature))
    {
      setSendActions((Collection)obj);
      return;
    }

    super.reflectiveSetValue(feature, obj);
  }

  public void reflectiveAddValue(String feature, Object obj)
  {
    if ("context".equals(feature))
    {
      addContext((MBehavioralFeature)obj);
      return;
    }
    if ("reception".equals(feature))
    {
      addReception((MReception)obj);
      return;
    }
    if ("occurrence".equals(feature))
    {
      addOccurrence((MSignalEvent)obj);
      return;
    }
    if ("sendAction".equals(feature))
    {
      addSendAction((MSendAction)obj);
      return;
    }

    super.reflectiveAddValue(feature, obj);
  }

  public void reflectiveRemoveValue(String feature, Object obj)
  {
    if ("context".equals(feature))
    {
      removeContext((MBehavioralFeature)obj);
      return;
    }
    if ("reception".equals(feature))
    {
      removeReception((MReception)obj);
      return;
    }
    if ("occurrence".equals(feature))
    {
      removeOccurrence((MSignalEvent)obj);
      return;
    }
    if ("sendAction".equals(feature))
    {
      removeSendAction((MSendAction)obj);
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
