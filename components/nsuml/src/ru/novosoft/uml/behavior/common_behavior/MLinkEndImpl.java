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

public class MLinkEndImpl extends MModelElementImpl implements MLinkEnd
{
  // ------------ code for class LinkEnd -----------------
  // generating attributes
  // generating associations
  // opposite role: qualifiedValue this role: linkEnd
  private final static Method _qualifiedValue_setMethod = getMethod1(MLinkEndImpl.class, "setQualifiedValues", Collection.class);
  private final static Method _qualifiedValue_addMethod = getMethod1(MLinkEndImpl.class, "addQualifiedValue", MAttributeLink.class);
  private final static Method _qualifiedValue_removeMethod = getMethod1(MLinkEndImpl.class, "removeQualifiedValue", MAttributeLink.class);
  Collection _qualifiedValue = Collections.EMPTY_LIST;
  Collection _qualifiedValue_ucopy = Collections.EMPTY_LIST;
  public final Collection getQualifiedValues()
  {
    checkExists();
    if (null == _qualifiedValue_ucopy)
    {
      _qualifiedValue_ucopy = ucopy(_qualifiedValue);
    }
    return _qualifiedValue_ucopy;
  }
  public final void setQualifiedValues(Collection __arg)
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
        old = getQualifiedValues();
      }
      _qualifiedValue_ucopy = null;
      Collection __added = bagdiff(__arg,_qualifiedValue);
      Collection __removed = bagdiff(_qualifiedValue, __arg);
      Iterator iter1 = __removed.iterator();
      while (iter1.hasNext())
      {
        MAttributeLink o = (MAttributeLink)iter1.next();
        o.internalUnrefByLinkEnd(this);
      }
      Iterator iter2 = __added.iterator();
      while (iter2.hasNext())
      {
        MAttributeLink o = (MAttributeLink)iter2.next();
        o.internalRefByLinkEnd(this);
      }
      _qualifiedValue = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_qualifiedValue_setMethod, old, getQualifiedValues());
      }
      if (sendEvent)
      {
        fireBagSet("qualifiedValue", old, getQualifiedValues());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addQualifiedValue(MAttributeLink __arg)
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
        old = getQualifiedValues();
      }
      if (null != _qualifiedValue_ucopy)
      {
        _qualifiedValue = new ArrayList(_qualifiedValue);
        _qualifiedValue_ucopy = null;
      }
      __arg.internalRefByLinkEnd(this);
      _qualifiedValue.add(__arg);
      logBagAdd(_qualifiedValue_addMethod, _qualifiedValue_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("qualifiedValue", old, getQualifiedValues(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeQualifiedValue(MAttributeLink __arg)
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
        old = getQualifiedValues();
      }
      if (null != _qualifiedValue_ucopy)
      {
        _qualifiedValue = new ArrayList(_qualifiedValue);
        _qualifiedValue_ucopy = null;
      }
      if (!_qualifiedValue.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByLinkEnd(this);
      logBagRemove(_qualifiedValue_removeMethod, _qualifiedValue_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("qualifiedValue", old, getQualifiedValues(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByQualifiedValue(MAttributeLink __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getQualifiedValues();
    }
    if (null != _qualifiedValue_ucopy)
    {
      _qualifiedValue = new ArrayList(_qualifiedValue);
      _qualifiedValue_ucopy = null;
    }
    _qualifiedValue.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("qualifiedValue", old, getQualifiedValues(), __arg);
    }
  }
  public final void internalUnrefByQualifiedValue(MAttributeLink __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getQualifiedValues();
    }
    if (null != _qualifiedValue_ucopy)
    {
      _qualifiedValue = new ArrayList(_qualifiedValue);
      _qualifiedValue_ucopy = null;
    }
    _qualifiedValue.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("qualifiedValue", old, getQualifiedValues(), __arg);
    }
  }
  // opposite role: associationEnd this role: linkEnd
  private final static Method _associationEnd_setMethod = getMethod1(MLinkEndImpl.class, "setAssociationEnd", MAssociationEnd.class);
  MAssociationEnd _associationEnd;
  public final MAssociationEnd getAssociationEnd()
  {
    checkExists();
    return _associationEnd;
  }
  public final void setAssociationEnd(MAssociationEnd __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      MAssociationEnd __saved = _associationEnd;
      if (_associationEnd != __arg)
      {
        if (__saved != null)
        {
          __saved.internalUnrefByLinkEnd(this);
        }
        if (__arg != null)
        {
          __arg.internalRefByLinkEnd(this);
        }
        logRefSet(_associationEnd_setMethod, __saved, __arg);
        fireRefSet("associationEnd", __saved, __arg);
        _associationEnd = __arg;
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByAssociationEnd(MAssociationEnd __arg)
  {
    MAssociationEnd __saved = _associationEnd;
    if (_associationEnd != null)
    {
      _associationEnd.removeLinkEnd(this);
    }
    fireRefSet("associationEnd", __saved, __arg);
    _associationEnd = __arg;
  }
  public final void internalUnrefByAssociationEnd(MAssociationEnd __arg)
  {
    fireRefSet("associationEnd", _associationEnd, null);
    _associationEnd = null;
  }
  // opposite role: link this role: connection
  private final static Method _link_setMethod = getMethod1(MLinkEndImpl.class, "setLink", MLink.class);
  MLink _link;
  public final MLink getLink()
  {
    checkExists();
    return _link;
  }
  public final void setLink(MLink __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      MLink __saved = _link;
      if (_link != __arg)
      {
        if (__saved != null)
        {
          __saved.internalUnrefByConnection(this);
        }
        if (__arg != null)
        {
          __arg.internalRefByConnection(this);
        }
        logRefSet(_link_setMethod, __saved, __arg);
        fireRefSet("link", __saved, __arg);
        _link = __arg;
        setModelElementContainer(_link, "link");
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByLink(MLink __arg)
  {
    MLink __saved = _link;
    if (_link != null)
    {
      _link.removeConnection(this);
    }
    fireRefSet("link", __saved, __arg);
    _link = __arg;
    setModelElementContainer(_link, "link");
  }
  public final void internalUnrefByLink(MLink __arg)
  {
    fireRefSet("link", _link, null);
    _link = null;
    setModelElementContainer(null, null);
  }
  // opposite role: instance this role: linkEnd
  private final static Method _instance_setMethod = getMethod1(MLinkEndImpl.class, "setInstance", MInstance.class);
  MInstance _instance;
  public final MInstance getInstance()
  {
    checkExists();
    return _instance;
  }
  public final void setInstance(MInstance __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      MInstance __saved = _instance;
      if (_instance != __arg)
      {
        if (__saved != null)
        {
          __saved.internalUnrefByLinkEnd(this);
        }
        if (__arg != null)
        {
          __arg.internalRefByLinkEnd(this);
        }
        logRefSet(_instance_setMethod, __saved, __arg);
        fireRefSet("instance", __saved, __arg);
        _instance = __arg;
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByInstance(MInstance __arg)
  {
    MInstance __saved = _instance;
    if (_instance != null)
    {
      _instance.removeLinkEnd(this);
    }
    fireRefSet("instance", __saved, __arg);
    _instance = __arg;
  }
  public final void internalUnrefByInstance(MInstance __arg)
  {
    fireRefSet("instance", _instance, null);
    _instance = null;
  }
  protected void cleanup(Collection scheduledForRemove)
  {
    // opposite role: qualifiedValue this role: linkEnd
    if (_qualifiedValue.size() != 0)
    {
      scheduledForRemove.addAll(_qualifiedValue);
      setQualifiedValues(Collections.EMPTY_LIST);
    }
    // opposite role: associationEnd this role: linkEnd
    if (_associationEnd != null )
    {
      setAssociationEnd(null);
    }
    // opposite role: link this role: connection
    if (_link != null )
    {
      setLink(null);
    }
    // opposite role: instance this role: linkEnd
    if (_instance != null )
    {
      setInstance(null);
    }
    super.cleanup(scheduledForRemove);
  }

  public String getUMLClassName()
  {
    return "LinkEnd";
  }

  // Reflective API

  public Object reflectiveGetValue(String feature)
  {
    if ("qualifiedValue".equals(feature))
    {
      return getQualifiedValues();
    }
    if ("associationEnd".equals(feature))
    {
      return getAssociationEnd();
    }
    if ("link".equals(feature))
    {
      return getLink();
    }
    if ("instance".equals(feature))
    {
      return getInstance();
    }

    return super.reflectiveGetValue(feature);
  }

  public void reflectiveSetValue(String feature, Object obj)
  {
    if ("qualifiedValue".equals(feature))
    {
      setQualifiedValues((Collection)obj);
      return;
    }
    if ("associationEnd".equals(feature))
    {
      setAssociationEnd((MAssociationEnd)obj);
      return;
    }
    if ("link".equals(feature))
    {
      setLink((MLink)obj);
      return;
    }
    if ("instance".equals(feature))
    {
      setInstance((MInstance)obj);
      return;
    }

    super.reflectiveSetValue(feature, obj);
  }

  public void reflectiveAddValue(String feature, Object obj)
  {
    if ("qualifiedValue".equals(feature))
    {
      addQualifiedValue((MAttributeLink)obj);
      return;
    }

    super.reflectiveAddValue(feature, obj);
  }

  public void reflectiveRemoveValue(String feature, Object obj)
  {
    if ("qualifiedValue".equals(feature))
    {
      removeQualifiedValue((MAttributeLink)obj);
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
    ret.addAll(getQualifiedValues());
    return ret;
  }
}
