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

public class MNamespaceImpl extends MModelElementImpl implements MNamespace
{
  // ------------ code for class Namespace -----------------
  // generating attributes
  // generating associations
  // opposite role: ownedElement this role: namespace
  private final static Method _ownedElement_setMethod = getMethod1(MNamespaceImpl.class, "setOwnedElements", Collection.class);
  private final static Method _ownedElement_addMethod = getMethod1(MNamespaceImpl.class, "addOwnedElement", MModelElement.class);
  private final static Method _ownedElement_removeMethod = getMethod1(MNamespaceImpl.class, "removeOwnedElement", MModelElement.class);
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
      Iterator iter1 = __removed.iterator();
      while (iter1.hasNext())
      {
        MModelElement o = (MModelElement)iter1.next();
        o.internalUnrefByNamespace(this);
      }
      Iterator iter2 = __added.iterator();
      while (iter2.hasNext())
      {
        MModelElement o = (MModelElement)iter2.next();
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
    return "Namespace";
  }

  // Reflective API

  public Object reflectiveGetValue(String feature)
  {
    if ("ownedElement".equals(feature))
    {
      return getOwnedElements();
    }

    return super.reflectiveGetValue(feature);
  }

  public void reflectiveSetValue(String feature, Object obj)
  {
    if ("ownedElement".equals(feature))
    {
      setOwnedElements((Collection)obj);
      return;
    }

    super.reflectiveSetValue(feature, obj);
  }

  public void reflectiveAddValue(String feature, Object obj)
  {
    if ("ownedElement".equals(feature))
    {
      addOwnedElement((MModelElement)obj);
      return;
    }

    super.reflectiveAddValue(feature, obj);
  }

  public void reflectiveRemoveValue(String feature, Object obj)
  {
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
