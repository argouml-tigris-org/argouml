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

public class MCompositeStateImpl extends MStateImpl implements MCompositeState
{
  // ------------ code for class CompositeState -----------------
  // generating attributes
  // attribute: isConcurent
  private final static Method _isConcurent_setMethod = getMethod1(MCompositeStateImpl.class, "setConcurent", boolean.class);
  boolean _isConcurent;
  public final boolean isConcurent()
  {
    checkExists();
    return _isConcurent;
  }
  public final void setConcurent(boolean __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      logAttrSet(_isConcurent_setMethod, _isConcurent, __arg);
      fireAttrSet("isConcurent", _isConcurent, __arg);
      _isConcurent = __arg;
    }
    finally
    {
      operationFinished();
    }
  }
  // generating associations
  // opposite role: subvertex this role: container
  private final static Method _subvertex_setMethod = getMethod1(MCompositeStateImpl.class, "setSubvertices", Collection.class);
  private final static Method _subvertex_addMethod = getMethod1(MCompositeStateImpl.class, "addSubvertex", MStateVertex.class);
  private final static Method _subvertex_removeMethod = getMethod1(MCompositeStateImpl.class, "removeSubvertex", MStateVertex.class);
  Collection _subvertex = Collections.EMPTY_LIST;
  Collection _subvertex_ucopy = Collections.EMPTY_LIST;
  public final Collection getSubvertices()
  {
    checkExists();
    if (null == _subvertex_ucopy)
    {
      _subvertex_ucopy = ucopy(_subvertex);
    }
    return _subvertex_ucopy;
  }
  public final void setSubvertices(Collection __arg)
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
        old = getSubvertices();
      }
      _subvertex_ucopy = null;
      Collection __added = bagdiff(__arg,_subvertex);
      Collection __removed = bagdiff(_subvertex, __arg);
      Iterator iter1 = __removed.iterator();
      while (iter1.hasNext())
      {
        MStateVertex o = (MStateVertex)iter1.next();
        o.internalUnrefByContainer(this);
      }
      Iterator iter2 = __added.iterator();
      while (iter2.hasNext())
      {
        MStateVertex o = (MStateVertex)iter2.next();
        o.internalRefByContainer(this);
      }
      _subvertex = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_subvertex_setMethod, old, getSubvertices());
      }
      if (sendEvent)
      {
        fireBagSet("subvertex", old, getSubvertices());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addSubvertex(MStateVertex __arg)
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
        old = getSubvertices();
      }
      if (null != _subvertex_ucopy)
      {
        _subvertex = new ArrayList(_subvertex);
        _subvertex_ucopy = null;
      }
      __arg.internalRefByContainer(this);
      _subvertex.add(__arg);
      logBagAdd(_subvertex_addMethod, _subvertex_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("subvertex", old, getSubvertices(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeSubvertex(MStateVertex __arg)
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
        old = getSubvertices();
      }
      if (null != _subvertex_ucopy)
      {
        _subvertex = new ArrayList(_subvertex);
        _subvertex_ucopy = null;
      }
      if (!_subvertex.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByContainer(this);
      logBagRemove(_subvertex_removeMethod, _subvertex_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("subvertex", old, getSubvertices(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefBySubvertex(MStateVertex __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getSubvertices();
    }
    if (null != _subvertex_ucopy)
    {
      _subvertex = new ArrayList(_subvertex);
      _subvertex_ucopy = null;
    }
    _subvertex.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("subvertex", old, getSubvertices(), __arg);
    }
  }
  public final void internalUnrefBySubvertex(MStateVertex __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getSubvertices();
    }
    if (null != _subvertex_ucopy)
    {
      _subvertex = new ArrayList(_subvertex);
      _subvertex_ucopy = null;
    }
    _subvertex.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("subvertex", old, getSubvertices(), __arg);
    }
  }
  protected void cleanup(Collection scheduledForRemove)
  {
    // opposite role: subvertex this role: container
    if (_subvertex.size() != 0)
    {
      scheduledForRemove.addAll(_subvertex);
      setSubvertices(Collections.EMPTY_LIST);
    }
    super.cleanup(scheduledForRemove);
  }

  public String getUMLClassName()
  {
    return "CompositeState";
  }

  // Reflective API

  public Object reflectiveGetValue(String feature)
  {
    if ("isConcurent".equals(feature))
    {
      return new Boolean(isConcurent());
    }
    if ("subvertex".equals(feature))
    {
      return getSubvertices();
    }

    return super.reflectiveGetValue(feature);
  }

  public void reflectiveSetValue(String feature, Object obj)
  {
    if ("isConcurent".equals(feature))
    {
      setConcurent(((Boolean)obj).booleanValue());
      return;
    }
    if ("subvertex".equals(feature))
    {
      setSubvertices((Collection)obj);
      return;
    }

    super.reflectiveSetValue(feature, obj);
  }

  public void reflectiveAddValue(String feature, Object obj)
  {
    if ("subvertex".equals(feature))
    {
      addSubvertex((MStateVertex)obj);
      return;
    }

    super.reflectiveAddValue(feature, obj);
  }

  public void reflectiveRemoveValue(String feature, Object obj)
  {
    if ("subvertex".equals(feature))
    {
      removeSubvertex((MStateVertex)obj);
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
    ret.addAll(getSubvertices());
    return ret;
  }
}
