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

public class MCollaborationImpl extends MGeneralizableElementImpl implements MCollaboration
{
  // ------------ code for class Collaboration -----------------
  // generating attributes
  // generating associations
  // opposite role: representedOperation this role: collaboration
  private final static Method _representedOperation_setMethod = getMethod1(MCollaborationImpl.class, "setRepresentedOperation", MOperation.class);
  MOperation _representedOperation;
  public final MOperation getRepresentedOperation()
  {
    checkExists();
    return _representedOperation;
  }
  public final void setRepresentedOperation(MOperation __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      MOperation __saved = _representedOperation;
      if (_representedOperation != __arg)
      {
        if (__saved != null)
        {
          __saved.internalUnrefByCollaboration(this);
        }
        if (__arg != null)
        {
          __arg.internalRefByCollaboration(this);
        }
        logRefSet(_representedOperation_setMethod, __saved, __arg);
        fireRefSet("representedOperation", __saved, __arg);
        _representedOperation = __arg;
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByRepresentedOperation(MOperation __arg)
  {
    MOperation __saved = _representedOperation;
    if (_representedOperation != null)
    {
      _representedOperation.removeCollaboration(this);
    }
    fireRefSet("representedOperation", __saved, __arg);
    _representedOperation = __arg;
  }
  public final void internalUnrefByRepresentedOperation(MOperation __arg)
  {
    fireRefSet("representedOperation", _representedOperation, null);
    _representedOperation = null;
  }
  // opposite role: representedClassifier this role: collaboration
  private final static Method _representedClassifier_setMethod = getMethod1(MCollaborationImpl.class, "setRepresentedClassifier", MClassifier.class);
  MClassifier _representedClassifier;
  public final MClassifier getRepresentedClassifier()
  {
    checkExists();
    return _representedClassifier;
  }
  public final void setRepresentedClassifier(MClassifier __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      MClassifier __saved = _representedClassifier;
      if (_representedClassifier != __arg)
      {
        if (__saved != null)
        {
          __saved.internalUnrefByCollaboration(this);
        }
        if (__arg != null)
        {
          __arg.internalRefByCollaboration(this);
        }
        logRefSet(_representedClassifier_setMethod, __saved, __arg);
        fireRefSet("representedClassifier", __saved, __arg);
        _representedClassifier = __arg;
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByRepresentedClassifier(MClassifier __arg)
  {
    MClassifier __saved = _representedClassifier;
    if (_representedClassifier != null)
    {
      _representedClassifier.removeCollaboration(this);
    }
    fireRefSet("representedClassifier", __saved, __arg);
    _representedClassifier = __arg;
  }
  public final void internalUnrefByRepresentedClassifier(MClassifier __arg)
  {
    fireRefSet("representedClassifier", _representedClassifier, null);
    _representedClassifier = null;
  }
  // opposite role: constrainingElement this role: collaboration1
  private final static Method _constrainingElement_setMethod = getMethod1(MCollaborationImpl.class, "setConstrainingElements", Collection.class);
  private final static Method _constrainingElement_addMethod = getMethod1(MCollaborationImpl.class, "addConstrainingElement", MModelElement.class);
  private final static Method _constrainingElement_removeMethod = getMethod1(MCollaborationImpl.class, "removeConstrainingElement", MModelElement.class);
  Collection _constrainingElement = Collections.EMPTY_LIST;
  Collection _constrainingElement_ucopy = Collections.EMPTY_LIST;
  public final Collection getConstrainingElements()
  {
    checkExists();
    if (null == _constrainingElement_ucopy)
    {
      _constrainingElement_ucopy = ucopy(_constrainingElement);
    }
    return _constrainingElement_ucopy;
  }
  public final void setConstrainingElements(Collection __arg)
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
        old = getConstrainingElements();
      }
      _constrainingElement_ucopy = null;
      Collection __added = bagdiff(__arg,_constrainingElement);
      Collection __removed = bagdiff(_constrainingElement, __arg);
      Iterator iter1 = __removed.iterator();
      while (iter1.hasNext())
      {
        MModelElement o = (MModelElement)iter1.next();
        o.internalUnrefByCollaboration1(this);
      }
      Iterator iter2 = __added.iterator();
      while (iter2.hasNext())
      {
        MModelElement o = (MModelElement)iter2.next();
        o.internalRefByCollaboration1(this);
      }
      _constrainingElement = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_constrainingElement_setMethod, old, getConstrainingElements());
      }
      if (sendEvent)
      {
        fireBagSet("constrainingElement", old, getConstrainingElements());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addConstrainingElement(MModelElement __arg)
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
        old = getConstrainingElements();
      }
      if (null != _constrainingElement_ucopy)
      {
        _constrainingElement = new ArrayList(_constrainingElement);
        _constrainingElement_ucopy = null;
      }
      __arg.internalRefByCollaboration1(this);
      _constrainingElement.add(__arg);
      logBagAdd(_constrainingElement_addMethod, _constrainingElement_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("constrainingElement", old, getConstrainingElements(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeConstrainingElement(MModelElement __arg)
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
        old = getConstrainingElements();
      }
      if (null != _constrainingElement_ucopy)
      {
        _constrainingElement = new ArrayList(_constrainingElement);
        _constrainingElement_ucopy = null;
      }
      if (!_constrainingElement.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByCollaboration1(this);
      logBagRemove(_constrainingElement_removeMethod, _constrainingElement_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("constrainingElement", old, getConstrainingElements(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByConstrainingElement(MModelElement __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getConstrainingElements();
    }
    if (null != _constrainingElement_ucopy)
    {
      _constrainingElement = new ArrayList(_constrainingElement);
      _constrainingElement_ucopy = null;
    }
    _constrainingElement.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("constrainingElement", old, getConstrainingElements(), __arg);
    }
  }
  public final void internalUnrefByConstrainingElement(MModelElement __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getConstrainingElements();
    }
    if (null != _constrainingElement_ucopy)
    {
      _constrainingElement = new ArrayList(_constrainingElement);
      _constrainingElement_ucopy = null;
    }
    _constrainingElement.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("constrainingElement", old, getConstrainingElements(), __arg);
    }
  }
  // opposite role: interaction this role: context
  private final static Method _interaction_setMethod = getMethod1(MCollaborationImpl.class, "setInteractions", Collection.class);
  private final static Method _interaction_addMethod = getMethod1(MCollaborationImpl.class, "addInteraction", MInteraction.class);
  private final static Method _interaction_removeMethod = getMethod1(MCollaborationImpl.class, "removeInteraction", MInteraction.class);
  Collection _interaction = Collections.EMPTY_LIST;
  Collection _interaction_ucopy = Collections.EMPTY_LIST;
  public final Collection getInteractions()
  {
    checkExists();
    if (null == _interaction_ucopy)
    {
      _interaction_ucopy = ucopy(_interaction);
    }
    return _interaction_ucopy;
  }
  public final void setInteractions(Collection __arg)
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
        old = getInteractions();
      }
      _interaction_ucopy = null;
      Collection __added = bagdiff(__arg,_interaction);
      Collection __removed = bagdiff(_interaction, __arg);
      Iterator iter3 = __removed.iterator();
      while (iter3.hasNext())
      {
        MInteraction o = (MInteraction)iter3.next();
        o.internalUnrefByContext(this);
      }
      Iterator iter4 = __added.iterator();
      while (iter4.hasNext())
      {
        MInteraction o = (MInteraction)iter4.next();
        o.internalRefByContext(this);
      }
      _interaction = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_interaction_setMethod, old, getInteractions());
      }
      if (sendEvent)
      {
        fireBagSet("interaction", old, getInteractions());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addInteraction(MInteraction __arg)
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
        old = getInteractions();
      }
      if (null != _interaction_ucopy)
      {
        _interaction = new ArrayList(_interaction);
        _interaction_ucopy = null;
      }
      __arg.internalRefByContext(this);
      _interaction.add(__arg);
      logBagAdd(_interaction_addMethod, _interaction_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("interaction", old, getInteractions(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeInteraction(MInteraction __arg)
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
        old = getInteractions();
      }
      if (null != _interaction_ucopy)
      {
        _interaction = new ArrayList(_interaction);
        _interaction_ucopy = null;
      }
      if (!_interaction.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByContext(this);
      logBagRemove(_interaction_removeMethod, _interaction_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("interaction", old, getInteractions(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByInteraction(MInteraction __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getInteractions();
    }
    if (null != _interaction_ucopy)
    {
      _interaction = new ArrayList(_interaction);
      _interaction_ucopy = null;
    }
    _interaction.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("interaction", old, getInteractions(), __arg);
    }
  }
  public final void internalUnrefByInteraction(MInteraction __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getInteractions();
    }
    if (null != _interaction_ucopy)
    {
      _interaction = new ArrayList(_interaction);
      _interaction_ucopy = null;
    }
    _interaction.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("interaction", old, getInteractions(), __arg);
    }
  }
  // ------------ code for class Namespace -----------------
  // generating attributes
  // generating associations
  // opposite role: ownedElement this role: namespace
  private final static Method _ownedElement_setMethod = getMethod1(MCollaborationImpl.class, "setOwnedElements", Collection.class);
  private final static Method _ownedElement_addMethod = getMethod1(MCollaborationImpl.class, "addOwnedElement", MModelElement.class);
  private final static Method _ownedElement_removeMethod = getMethod1(MCollaborationImpl.class, "removeOwnedElement", MModelElement.class);
  Collection _ownedElement = Collections.EMPTY_LIST;
  Collection _ownedElement_ucopy = Collections.EMPTY_LIST;
  public final Collection getOwnedElements()
  {
    checkExists();
    if (null == _ownedElement_ucopy)
    {
      _ownedElement_ucopy = ucopy(_ownedElement);
    }
    return _ownedElement_ucopy;
  }
  public final void setOwnedElements(Collection __arg)
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
        old = getOwnedElements();
      }
      _ownedElement_ucopy = null;
      Collection __added = bagdiff(__arg,_ownedElement);
      Collection __removed = bagdiff(_ownedElement, __arg);
      Iterator iter5 = __removed.iterator();
      while (iter5.hasNext())
      {
        MModelElement o = (MModelElement)iter5.next();
        o.internalUnrefByNamespace(this);
      }
      Iterator iter6 = __added.iterator();
      while (iter6.hasNext())
      {
        MModelElement o = (MModelElement)iter6.next();
        o.internalRefByNamespace(this);
      }
      _ownedElement = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_ownedElement_setMethod, old, getOwnedElements());
      }
      if (sendEvent)
      {
        fireBagSet("ownedElement", old, getOwnedElements());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addOwnedElement(MModelElement __arg)
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
        old = getOwnedElements();
      }
      if (null != _ownedElement_ucopy)
      {
        _ownedElement = new ArrayList(_ownedElement);
        _ownedElement_ucopy = null;
      }
      __arg.internalRefByNamespace(this);
      _ownedElement.add(__arg);
      logBagAdd(_ownedElement_addMethod, _ownedElement_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("ownedElement", old, getOwnedElements(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeOwnedElement(MModelElement __arg)
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
        old = getOwnedElements();
      }
      if (null != _ownedElement_ucopy)
      {
        _ownedElement = new ArrayList(_ownedElement);
        _ownedElement_ucopy = null;
      }
      if (!_ownedElement.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByNamespace(this);
      logBagRemove(_ownedElement_removeMethod, _ownedElement_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("ownedElement", old, getOwnedElements(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByOwnedElement(MModelElement __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getOwnedElements();
    }
    if (null != _ownedElement_ucopy)
    {
      _ownedElement = new ArrayList(_ownedElement);
      _ownedElement_ucopy = null;
    }
    _ownedElement.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("ownedElement", old, getOwnedElements(), __arg);
    }
  }
  public final void internalUnrefByOwnedElement(MModelElement __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getOwnedElements();
    }
    if (null != _ownedElement_ucopy)
    {
      _ownedElement = new ArrayList(_ownedElement);
      _ownedElement_ucopy = null;
    }
    _ownedElement.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("ownedElement", old, getOwnedElements(), __arg);
    }
  }
  protected void cleanup(Collection scheduledForRemove)
  {
    // opposite role: representedOperation this role: collaboration
    if (_representedOperation != null )
    {
      setRepresentedOperation(null);
    }
    // opposite role: representedClassifier this role: collaboration
    if (_representedClassifier != null )
    {
      setRepresentedClassifier(null);
    }
    // opposite role: constrainingElement this role: collaboration1
    if (_constrainingElement.size() != 0)
    {
      setConstrainingElements(Collections.EMPTY_LIST);
    }
    // opposite role: interaction this role: context
    if (_interaction.size() != 0)
    {
      scheduledForRemove.addAll(_interaction);
      setInteractions(Collections.EMPTY_LIST);
    }
    // opposite role: ownedElement this role: namespace
    if (_ownedElement.size() != 0)
    {
      scheduledForRemove.addAll(_ownedElement);
      setOwnedElements(Collections.EMPTY_LIST);
    }
    super.cleanup(scheduledForRemove);
  }

  public String getUMLClassName()
  {
    return "Collaboration";
  }

  // Reflective API

  public Object reflectiveGetValue(String feature)
  {
    if ("representedOperation".equals(feature))
    {
      return getRepresentedOperation();
    }
    if ("representedClassifier".equals(feature))
    {
      return getRepresentedClassifier();
    }
    if ("constrainingElement".equals(feature))
    {
      return getConstrainingElements();
    }
    if ("interaction".equals(feature))
    {
      return getInteractions();
    }
    if ("ownedElement".equals(feature))
    {
      return getOwnedElements();
    }

    return super.reflectiveGetValue(feature);
  }

  public void reflectiveSetValue(String feature, Object obj)
  {
    if ("representedOperation".equals(feature))
    {
      setRepresentedOperation((MOperation)obj);
      return;
    }
    if ("representedClassifier".equals(feature))
    {
      setRepresentedClassifier((MClassifier)obj);
      return;
    }
    if ("constrainingElement".equals(feature))
    {
      setConstrainingElements((Collection)obj);
      return;
    }
    if ("interaction".equals(feature))
    {
      setInteractions((Collection)obj);
      return;
    }
    if ("ownedElement".equals(feature))
    {
      setOwnedElements((Collection)obj);
      return;
    }

    super.reflectiveSetValue(feature, obj);
  }

  public void reflectiveAddValue(String feature, Object obj)
  {
    if ("constrainingElement".equals(feature))
    {
      addConstrainingElement((MModelElement)obj);
      return;
    }
    if ("interaction".equals(feature))
    {
      addInteraction((MInteraction)obj);
      return;
    }
    if ("ownedElement".equals(feature))
    {
      addOwnedElement((MModelElement)obj);
      return;
    }

    super.reflectiveAddValue(feature, obj);
  }

  public void reflectiveRemoveValue(String feature, Object obj)
  {
    if ("constrainingElement".equals(feature))
    {
      removeConstrainingElement((MModelElement)obj);
      return;
    }
    if ("interaction".equals(feature))
    {
      removeInteraction((MInteraction)obj);
      return;
    }
    if ("ownedElement".equals(feature))
    {
      removeOwnedElement((MModelElement)obj);
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
    ret.addAll(getInteractions());
    ret.addAll(getOwnedElements());
    return ret;
  }
  public ru.novosoft.uml.foundation.core.MModelElement lookup(String name)
  {
    int idx = name.indexOf("::");

    if (idx != -1)
    {
       String nm;
       nm = name.substring(0, idx);
       ru.novosoft.uml.foundation.core.MModelElement e = lookup(nm);
       if(e == null || !(e instanceof ru.novosoft.uml.foundation.core.MNamespace))
       {
         return null;
       } 
       ru.novosoft.uml.foundation.core.MNamespace ns = (ru.novosoft.uml.foundation.core.MNamespace)e;
       nm = name.substring(idx+2);
       return ns.lookup(nm); 
    }
    Iterator i = getOwnedElements().iterator();
    while(i.hasNext())
    {
      ru.novosoft.uml.foundation.core.MModelElement e = (ru.novosoft.uml.foundation.core.MModelElement)i.next();
      if(name.equals(e.getName()))
      {
        return e;
      }
    }
    return null;
  }
}
