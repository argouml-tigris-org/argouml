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

public class MInstanceImpl extends MModelElementImpl implements MInstance
{
  // ------------ code for class Instance -----------------
  // generating attributes
  // generating associations
  // opposite role: stimulus2 this role: receiver
  private final static Method _stimulus2_setMethod = getMethod1(MInstanceImpl.class, "setStimuli2", Collection.class);
  private final static Method _stimulus2_addMethod = getMethod1(MInstanceImpl.class, "addStimulus2", MStimulus.class);
  private final static Method _stimulus2_removeMethod = getMethod1(MInstanceImpl.class, "removeStimulus2", MStimulus.class);
  Collection _stimulus2 = Collections.EMPTY_LIST;
  Collection _stimulus2_ucopy = Collections.EMPTY_LIST;
  public final Collection getStimuli2()
  {
    checkExists();
    if (null == _stimulus2_ucopy)
    {
      _stimulus2_ucopy = ucopy(_stimulus2);
    }
    return _stimulus2_ucopy;
  }
  public final void setStimuli2(Collection __arg)
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
        old = getStimuli2();
      }
      _stimulus2_ucopy = null;
      Collection __added = bagdiff(__arg,_stimulus2);
      Collection __removed = bagdiff(_stimulus2, __arg);
      Iterator iter1 = __removed.iterator();
      while (iter1.hasNext())
      {
        MStimulus o = (MStimulus)iter1.next();
        o.internalUnrefByReceiver(this);
      }
      Iterator iter2 = __added.iterator();
      while (iter2.hasNext())
      {
        MStimulus o = (MStimulus)iter2.next();
        o.internalRefByReceiver(this);
      }
      _stimulus2 = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_stimulus2_setMethod, old, getStimuli2());
      }
      if (sendEvent)
      {
        fireBagSet("stimulus2", old, getStimuli2());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addStimulus2(MStimulus __arg)
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
        old = getStimuli2();
      }
      if (null != _stimulus2_ucopy)
      {
        _stimulus2 = new ArrayList(_stimulus2);
        _stimulus2_ucopy = null;
      }
      __arg.internalRefByReceiver(this);
      _stimulus2.add(__arg);
      logBagAdd(_stimulus2_addMethod, _stimulus2_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("stimulus2", old, getStimuli2(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeStimulus2(MStimulus __arg)
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
        old = getStimuli2();
      }
      if (null != _stimulus2_ucopy)
      {
        _stimulus2 = new ArrayList(_stimulus2);
        _stimulus2_ucopy = null;
      }
      if (!_stimulus2.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByReceiver(this);
      logBagRemove(_stimulus2_removeMethod, _stimulus2_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("stimulus2", old, getStimuli2(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByStimulus2(MStimulus __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getStimuli2();
    }
    if (null != _stimulus2_ucopy)
    {
      _stimulus2 = new ArrayList(_stimulus2);
      _stimulus2_ucopy = null;
    }
    _stimulus2.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("stimulus2", old, getStimuli2(), __arg);
    }
  }
  public final void internalUnrefByStimulus2(MStimulus __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getStimuli2();
    }
    if (null != _stimulus2_ucopy)
    {
      _stimulus2 = new ArrayList(_stimulus2);
      _stimulus2_ucopy = null;
    }
    _stimulus2.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("stimulus2", old, getStimuli2(), __arg);
    }
  }
  // opposite role: componentInstance this role: resident
  private final static Method _componentInstance_setMethod = getMethod1(MInstanceImpl.class, "setComponentInstance", MComponentInstance.class);
  MComponentInstance _componentInstance;
  public final MComponentInstance getComponentInstance()
  {
    checkExists();
    return _componentInstance;
  }
  public final void setComponentInstance(MComponentInstance __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      MComponentInstance __saved = _componentInstance;
      if (_componentInstance != __arg)
      {
        if (__saved != null)
        {
          __saved.internalUnrefByResident(this);
        }
        if (__arg != null)
        {
          __arg.internalRefByResident(this);
        }
        logRefSet(_componentInstance_setMethod, __saved, __arg);
        fireRefSet("componentInstance", __saved, __arg);
        _componentInstance = __arg;
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByComponentInstance(MComponentInstance __arg)
  {
    MComponentInstance __saved = _componentInstance;
    if (_componentInstance != null)
    {
      _componentInstance.removeResident(this);
    }
    fireRefSet("componentInstance", __saved, __arg);
    _componentInstance = __arg;
  }
  public final void internalUnrefByComponentInstance(MComponentInstance __arg)
  {
    fireRefSet("componentInstance", _componentInstance, null);
    _componentInstance = null;
  }
  // opposite role: stimulus3 this role: sender
  private final static Method _stimulus3_setMethod = getMethod1(MInstanceImpl.class, "setStimuli3", Collection.class);
  private final static Method _stimulus3_addMethod = getMethod1(MInstanceImpl.class, "addStimulus3", MStimulus.class);
  private final static Method _stimulus3_removeMethod = getMethod1(MInstanceImpl.class, "removeStimulus3", MStimulus.class);
  Collection _stimulus3 = Collections.EMPTY_LIST;
  Collection _stimulus3_ucopy = Collections.EMPTY_LIST;
  public final Collection getStimuli3()
  {
    checkExists();
    if (null == _stimulus3_ucopy)
    {
      _stimulus3_ucopy = ucopy(_stimulus3);
    }
    return _stimulus3_ucopy;
  }
  public final void setStimuli3(Collection __arg)
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
        old = getStimuli3();
      }
      _stimulus3_ucopy = null;
      Collection __added = bagdiff(__arg,_stimulus3);
      Collection __removed = bagdiff(_stimulus3, __arg);
      Iterator iter3 = __removed.iterator();
      while (iter3.hasNext())
      {
        MStimulus o = (MStimulus)iter3.next();
        o.internalUnrefBySender(this);
      }
      Iterator iter4 = __added.iterator();
      while (iter4.hasNext())
      {
        MStimulus o = (MStimulus)iter4.next();
        o.internalRefBySender(this);
      }
      _stimulus3 = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_stimulus3_setMethod, old, getStimuli3());
      }
      if (sendEvent)
      {
        fireBagSet("stimulus3", old, getStimuli3());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addStimulus3(MStimulus __arg)
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
        old = getStimuli3();
      }
      if (null != _stimulus3_ucopy)
      {
        _stimulus3 = new ArrayList(_stimulus3);
        _stimulus3_ucopy = null;
      }
      __arg.internalRefBySender(this);
      _stimulus3.add(__arg);
      logBagAdd(_stimulus3_addMethod, _stimulus3_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("stimulus3", old, getStimuli3(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeStimulus3(MStimulus __arg)
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
        old = getStimuli3();
      }
      if (null != _stimulus3_ucopy)
      {
        _stimulus3 = new ArrayList(_stimulus3);
        _stimulus3_ucopy = null;
      }
      if (!_stimulus3.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefBySender(this);
      logBagRemove(_stimulus3_removeMethod, _stimulus3_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("stimulus3", old, getStimuli3(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByStimulus3(MStimulus __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getStimuli3();
    }
    if (null != _stimulus3_ucopy)
    {
      _stimulus3 = new ArrayList(_stimulus3);
      _stimulus3_ucopy = null;
    }
    _stimulus3.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("stimulus3", old, getStimuli3(), __arg);
    }
  }
  public final void internalUnrefByStimulus3(MStimulus __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getStimuli3();
    }
    if (null != _stimulus3_ucopy)
    {
      _stimulus3 = new ArrayList(_stimulus3);
      _stimulus3_ucopy = null;
    }
    _stimulus3.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("stimulus3", old, getStimuli3(), __arg);
    }
  }
  // opposite role: stimulus1 this role: argument
  private final static Method _stimulus1_setMethod = getMethod1(MInstanceImpl.class, "setStimuli1", Collection.class);
  private final static Method _stimulus1_addMethod = getMethod1(MInstanceImpl.class, "addStimulus1", MStimulus.class);
  private final static Method _stimulus1_removeMethod = getMethod1(MInstanceImpl.class, "removeStimulus1", MStimulus.class);
  Collection _stimulus1 = Collections.EMPTY_LIST;
  Collection _stimulus1_ucopy = Collections.EMPTY_LIST;
  public final Collection getStimuli1()
  {
    checkExists();
    if (null == _stimulus1_ucopy)
    {
      _stimulus1_ucopy = ucopy(_stimulus1);
    }
    return _stimulus1_ucopy;
  }
  public final void setStimuli1(Collection __arg)
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
        old = getStimuli1();
      }
      _stimulus1_ucopy = null;
      Collection __added = bagdiff(__arg,_stimulus1);
      Collection __removed = bagdiff(_stimulus1, __arg);
      Iterator iter5 = __removed.iterator();
      while (iter5.hasNext())
      {
        MStimulus o = (MStimulus)iter5.next();
        o.internalUnrefByArgument(this);
      }
      Iterator iter6 = __added.iterator();
      while (iter6.hasNext())
      {
        MStimulus o = (MStimulus)iter6.next();
        o.internalRefByArgument(this);
      }
      _stimulus1 = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_stimulus1_setMethod, old, getStimuli1());
      }
      if (sendEvent)
      {
        fireBagSet("stimulus1", old, getStimuli1());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addStimulus1(MStimulus __arg)
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
        old = getStimuli1();
      }
      if (null != _stimulus1_ucopy)
      {
        _stimulus1 = new ArrayList(_stimulus1);
        _stimulus1_ucopy = null;
      }
      __arg.internalRefByArgument(this);
      _stimulus1.add(__arg);
      logBagAdd(_stimulus1_addMethod, _stimulus1_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("stimulus1", old, getStimuli1(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeStimulus1(MStimulus __arg)
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
        old = getStimuli1();
      }
      if (null != _stimulus1_ucopy)
      {
        _stimulus1 = new ArrayList(_stimulus1);
        _stimulus1_ucopy = null;
      }
      if (!_stimulus1.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByArgument(this);
      logBagRemove(_stimulus1_removeMethod, _stimulus1_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("stimulus1", old, getStimuli1(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByStimulus1(MStimulus __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getStimuli1();
    }
    if (null != _stimulus1_ucopy)
    {
      _stimulus1 = new ArrayList(_stimulus1);
      _stimulus1_ucopy = null;
    }
    _stimulus1.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("stimulus1", old, getStimuli1(), __arg);
    }
  }
  public final void internalUnrefByStimulus1(MStimulus __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getStimuli1();
    }
    if (null != _stimulus1_ucopy)
    {
      _stimulus1 = new ArrayList(_stimulus1);
      _stimulus1_ucopy = null;
    }
    _stimulus1.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("stimulus1", old, getStimuli1(), __arg);
    }
  }
  // opposite role: slot this role: instance
  private final static Method _slot_setMethod = getMethod1(MInstanceImpl.class, "setSlots", Collection.class);
  private final static Method _slot_addMethod = getMethod1(MInstanceImpl.class, "addSlot", MAttributeLink.class);
  private final static Method _slot_removeMethod = getMethod1(MInstanceImpl.class, "removeSlot", MAttributeLink.class);
  Collection _slot = Collections.EMPTY_LIST;
  Collection _slot_ucopy = Collections.EMPTY_LIST;
  public final Collection getSlots()
  {
    checkExists();
    if (null == _slot_ucopy)
    {
      _slot_ucopy = ucopy(_slot);
    }
    return _slot_ucopy;
  }
  public final void setSlots(Collection __arg)
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
        old = getSlots();
      }
      _slot_ucopy = null;
      Collection __added = bagdiff(__arg,_slot);
      Collection __removed = bagdiff(_slot, __arg);
      Iterator iter7 = __removed.iterator();
      while (iter7.hasNext())
      {
        MAttributeLink o = (MAttributeLink)iter7.next();
        o.internalUnrefByInstance(this);
      }
      Iterator iter8 = __added.iterator();
      while (iter8.hasNext())
      {
        MAttributeLink o = (MAttributeLink)iter8.next();
        o.internalRefByInstance(this);
      }
      _slot = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_slot_setMethod, old, getSlots());
      }
      if (sendEvent)
      {
        fireBagSet("slot", old, getSlots());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addSlot(MAttributeLink __arg)
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
        old = getSlots();
      }
      if (null != _slot_ucopy)
      {
        _slot = new ArrayList(_slot);
        _slot_ucopy = null;
      }
      __arg.internalRefByInstance(this);
      _slot.add(__arg);
      logBagAdd(_slot_addMethod, _slot_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("slot", old, getSlots(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeSlot(MAttributeLink __arg)
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
        old = getSlots();
      }
      if (null != _slot_ucopy)
      {
        _slot = new ArrayList(_slot);
        _slot_ucopy = null;
      }
      if (!_slot.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByInstance(this);
      logBagRemove(_slot_removeMethod, _slot_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("slot", old, getSlots(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefBySlot(MAttributeLink __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getSlots();
    }
    if (null != _slot_ucopy)
    {
      _slot = new ArrayList(_slot);
      _slot_ucopy = null;
    }
    _slot.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("slot", old, getSlots(), __arg);
    }
  }
  public final void internalUnrefBySlot(MAttributeLink __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getSlots();
    }
    if (null != _slot_ucopy)
    {
      _slot = new ArrayList(_slot);
      _slot_ucopy = null;
    }
    _slot.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("slot", old, getSlots(), __arg);
    }
  }
  // opposite role: linkEnd this role: instance
  private final static Method _linkEnd_setMethod = getMethod1(MInstanceImpl.class, "setLinkEnds", Collection.class);
  private final static Method _linkEnd_addMethod = getMethod1(MInstanceImpl.class, "addLinkEnd", MLinkEnd.class);
  private final static Method _linkEnd_removeMethod = getMethod1(MInstanceImpl.class, "removeLinkEnd", MLinkEnd.class);
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
      Iterator iter9 = __removed.iterator();
      while (iter9.hasNext())
      {
        MLinkEnd o = (MLinkEnd)iter9.next();
        o.internalUnrefByInstance(this);
      }
      Iterator iter10 = __added.iterator();
      while (iter10.hasNext())
      {
        MLinkEnd o = (MLinkEnd)iter10.next();
        o.internalRefByInstance(this);
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
      __arg.internalRefByInstance(this);
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
      __arg.internalUnrefByInstance(this);
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
  // opposite role: attributeLink this role: value
  private final static Method _attributeLink_setMethod = getMethod1(MInstanceImpl.class, "setAttributeLinks", Collection.class);
  private final static Method _attributeLink_addMethod = getMethod1(MInstanceImpl.class, "addAttributeLink", MAttributeLink.class);
  private final static Method _attributeLink_removeMethod = getMethod1(MInstanceImpl.class, "removeAttributeLink", MAttributeLink.class);
  Collection _attributeLink = Collections.EMPTY_LIST;
  Collection _attributeLink_ucopy = Collections.EMPTY_LIST;
  public final Collection getAttributeLinks()
  {
    checkExists();
    if (null == _attributeLink_ucopy)
    {
      _attributeLink_ucopy = ucopy(_attributeLink);
    }
    return _attributeLink_ucopy;
  }
  public final void setAttributeLinks(Collection __arg)
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
        old = getAttributeLinks();
      }
      _attributeLink_ucopy = null;
      Collection __added = bagdiff(__arg,_attributeLink);
      Collection __removed = bagdiff(_attributeLink, __arg);
      Iterator iter11 = __removed.iterator();
      while (iter11.hasNext())
      {
        MAttributeLink o = (MAttributeLink)iter11.next();
        o.internalUnrefByValue(this);
      }
      Iterator iter12 = __added.iterator();
      while (iter12.hasNext())
      {
        MAttributeLink o = (MAttributeLink)iter12.next();
        o.internalRefByValue(this);
      }
      _attributeLink = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_attributeLink_setMethod, old, getAttributeLinks());
      }
      if (sendEvent)
      {
        fireBagSet("attributeLink", old, getAttributeLinks());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addAttributeLink(MAttributeLink __arg)
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
        old = getAttributeLinks();
      }
      if (null != _attributeLink_ucopy)
      {
        _attributeLink = new ArrayList(_attributeLink);
        _attributeLink_ucopy = null;
      }
      __arg.internalRefByValue(this);
      _attributeLink.add(__arg);
      logBagAdd(_attributeLink_addMethod, _attributeLink_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("attributeLink", old, getAttributeLinks(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeAttributeLink(MAttributeLink __arg)
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
        old = getAttributeLinks();
      }
      if (null != _attributeLink_ucopy)
      {
        _attributeLink = new ArrayList(_attributeLink);
        _attributeLink_ucopy = null;
      }
      if (!_attributeLink.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByValue(this);
      logBagRemove(_attributeLink_removeMethod, _attributeLink_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("attributeLink", old, getAttributeLinks(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByAttributeLink(MAttributeLink __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getAttributeLinks();
    }
    if (null != _attributeLink_ucopy)
    {
      _attributeLink = new ArrayList(_attributeLink);
      _attributeLink_ucopy = null;
    }
    _attributeLink.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("attributeLink", old, getAttributeLinks(), __arg);
    }
  }
  public final void internalUnrefByAttributeLink(MAttributeLink __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getAttributeLinks();
    }
    if (null != _attributeLink_ucopy)
    {
      _attributeLink = new ArrayList(_attributeLink);
      _attributeLink_ucopy = null;
    }
    _attributeLink.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("attributeLink", old, getAttributeLinks(), __arg);
    }
  }
  // opposite role: classifier this role: instance
  private final static Method _classifier_setMethod = getMethod1(MInstanceImpl.class, "setClassifiers", Collection.class);
  private final static Method _classifier_addMethod = getMethod1(MInstanceImpl.class, "addClassifier", MClassifier.class);
  private final static Method _classifier_removeMethod = getMethod1(MInstanceImpl.class, "removeClassifier", MClassifier.class);
  Collection _classifier = Collections.EMPTY_LIST;
  Collection _classifier_ucopy = Collections.EMPTY_LIST;
  public final Collection getClassifiers()
  {
    checkExists();
    if (null == _classifier_ucopy)
    {
      _classifier_ucopy = ucopy(_classifier);
    }
    return _classifier_ucopy;
  }
  public final void setClassifiers(Collection __arg)
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
        old = getClassifiers();
      }
      _classifier_ucopy = null;
      Collection __added = bagdiff(__arg,_classifier);
      Collection __removed = bagdiff(_classifier, __arg);
      Iterator iter13 = __removed.iterator();
      while (iter13.hasNext())
      {
        MClassifier o = (MClassifier)iter13.next();
        o.internalUnrefByInstance(this);
      }
      Iterator iter14 = __added.iterator();
      while (iter14.hasNext())
      {
        MClassifier o = (MClassifier)iter14.next();
        o.internalRefByInstance(this);
      }
      _classifier = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_classifier_setMethod, old, getClassifiers());
      }
      if (sendEvent)
      {
        fireBagSet("classifier", old, getClassifiers());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addClassifier(MClassifier __arg)
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
        old = getClassifiers();
      }
      if (null != _classifier_ucopy)
      {
        _classifier = new ArrayList(_classifier);
        _classifier_ucopy = null;
      }
      __arg.internalRefByInstance(this);
      _classifier.add(__arg);
      logBagAdd(_classifier_addMethod, _classifier_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("classifier", old, getClassifiers(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeClassifier(MClassifier __arg)
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
        old = getClassifiers();
      }
      if (null != _classifier_ucopy)
      {
        _classifier = new ArrayList(_classifier);
        _classifier_ucopy = null;
      }
      if (!_classifier.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByInstance(this);
      logBagRemove(_classifier_removeMethod, _classifier_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("classifier", old, getClassifiers(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByClassifier(MClassifier __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getClassifiers();
    }
    if (null != _classifier_ucopy)
    {
      _classifier = new ArrayList(_classifier);
      _classifier_ucopy = null;
    }
    _classifier.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("classifier", old, getClassifiers(), __arg);
    }
  }
  public final void internalUnrefByClassifier(MClassifier __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getClassifiers();
    }
    if (null != _classifier_ucopy)
    {
      _classifier = new ArrayList(_classifier);
      _classifier_ucopy = null;
    }
    _classifier.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("classifier", old, getClassifiers(), __arg);
    }
  }
  protected void cleanup(Collection scheduledForRemove)
  {
    // opposite role: stimulus2 this role: receiver
    if (_stimulus2.size() != 0)
    {
      setStimuli2(Collections.EMPTY_LIST);
    }
    // opposite role: componentInstance this role: resident
    if (_componentInstance != null )
    {
      setComponentInstance(null);
    }
    // opposite role: stimulus3 this role: sender
    if (_stimulus3.size() != 0)
    {
      setStimuli3(Collections.EMPTY_LIST);
    }
    // opposite role: stimulus1 this role: argument
    if (_stimulus1.size() != 0)
    {
      setStimuli1(Collections.EMPTY_LIST);
    }
    // opposite role: slot this role: instance
    if (_slot.size() != 0)
    {
      scheduledForRemove.addAll(_slot);
      setSlots(Collections.EMPTY_LIST);
    }
    // opposite role: linkEnd this role: instance
    if (_linkEnd.size() != 0)
    {
      setLinkEnds(Collections.EMPTY_LIST);
    }
    // opposite role: attributeLink this role: value
    if (_attributeLink.size() != 0)
    {
      setAttributeLinks(Collections.EMPTY_LIST);
    }
    // opposite role: classifier this role: instance
    if (_classifier.size() != 0)
    {
      setClassifiers(Collections.EMPTY_LIST);
    }
    super.cleanup(scheduledForRemove);
  }

  public String getUMLClassName()
  {
    return "Instance";
  }

  // Reflective API

  public Object reflectiveGetValue(String feature)
  {
    if ("stimulus2".equals(feature))
    {
      return getStimuli2();
    }
    if ("componentInstance".equals(feature))
    {
      return getComponentInstance();
    }
    if ("stimulus3".equals(feature))
    {
      return getStimuli3();
    }
    if ("stimulus1".equals(feature))
    {
      return getStimuli1();
    }
    if ("slot".equals(feature))
    {
      return getSlots();
    }
    if ("linkEnd".equals(feature))
    {
      return getLinkEnds();
    }
    if ("attributeLink".equals(feature))
    {
      return getAttributeLinks();
    }
    if ("classifier".equals(feature))
    {
      return getClassifiers();
    }

    return super.reflectiveGetValue(feature);
  }

  public void reflectiveSetValue(String feature, Object obj)
  {
    if ("stimulus2".equals(feature))
    {
      setStimuli2((Collection)obj);
      return;
    }
    if ("componentInstance".equals(feature))
    {
      setComponentInstance((MComponentInstance)obj);
      return;
    }
    if ("stimulus3".equals(feature))
    {
      setStimuli3((Collection)obj);
      return;
    }
    if ("stimulus1".equals(feature))
    {
      setStimuli1((Collection)obj);
      return;
    }
    if ("slot".equals(feature))
    {
      setSlots((Collection)obj);
      return;
    }
    if ("linkEnd".equals(feature))
    {
      setLinkEnds((Collection)obj);
      return;
    }
    if ("attributeLink".equals(feature))
    {
      setAttributeLinks((Collection)obj);
      return;
    }
    if ("classifier".equals(feature))
    {
      setClassifiers((Collection)obj);
      return;
    }

    super.reflectiveSetValue(feature, obj);
  }

  public void reflectiveAddValue(String feature, Object obj)
  {
    if ("stimulus2".equals(feature))
    {
      addStimulus2((MStimulus)obj);
      return;
    }
    if ("stimulus3".equals(feature))
    {
      addStimulus3((MStimulus)obj);
      return;
    }
    if ("stimulus1".equals(feature))
    {
      addStimulus1((MStimulus)obj);
      return;
    }
    if ("slot".equals(feature))
    {
      addSlot((MAttributeLink)obj);
      return;
    }
    if ("linkEnd".equals(feature))
    {
      addLinkEnd((MLinkEnd)obj);
      return;
    }
    if ("attributeLink".equals(feature))
    {
      addAttributeLink((MAttributeLink)obj);
      return;
    }
    if ("classifier".equals(feature))
    {
      addClassifier((MClassifier)obj);
      return;
    }

    super.reflectiveAddValue(feature, obj);
  }

  public void reflectiveRemoveValue(String feature, Object obj)
  {
    if ("stimulus2".equals(feature))
    {
      removeStimulus2((MStimulus)obj);
      return;
    }
    if ("stimulus3".equals(feature))
    {
      removeStimulus3((MStimulus)obj);
      return;
    }
    if ("stimulus1".equals(feature))
    {
      removeStimulus1((MStimulus)obj);
      return;
    }
    if ("slot".equals(feature))
    {
      removeSlot((MAttributeLink)obj);
      return;
    }
    if ("linkEnd".equals(feature))
    {
      removeLinkEnd((MLinkEnd)obj);
      return;
    }
    if ("attributeLink".equals(feature))
    {
      removeAttributeLink((MAttributeLink)obj);
      return;
    }
    if ("classifier".equals(feature))
    {
      removeClassifier((MClassifier)obj);
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
    ret.addAll(getSlots());
    return ret;
  }
}
