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

package ru.novosoft.uml.behavior.activity_graphs;

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
import ru.novosoft.uml.behavior.state_machines.*;
import ru.novosoft.uml.model_management.*;

import java.lang.reflect.Method;

public class MClassifierInStateImpl extends MClassifierImpl implements MClassifierInState
{
  // ------------ code for class ClassifierInState -----------------
  // generating attributes
  // generating associations
  // opposite role: type this role: classifierInState
  private final static Method _type_setMethod = getMethod1(MClassifierInStateImpl.class, "setType", MClassifier.class);
  MClassifier _type;
  public final MClassifier getType()
  {
    checkExists();
    return _type;
  }
  public final void setType(MClassifier __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      MClassifier __saved = _type;
      if (_type != __arg)
      {
        if (__saved != null)
        {
          __saved.internalUnrefByClassifierInState(this);
        }
        if (__arg != null)
        {
          __arg.internalRefByClassifierInState(this);
        }
        logRefSet(_type_setMethod, __saved, __arg);
        fireRefSet("type", __saved, __arg);
        _type = __arg;
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByType(MClassifier __arg)
  {
    MClassifier __saved = _type;
    if (_type != null)
    {
      _type.removeClassifierInState(this);
    }
    fireRefSet("type", __saved, __arg);
    _type = __arg;
  }
  public final void internalUnrefByType(MClassifier __arg)
  {
    fireRefSet("type", _type, null);
    _type = null;
  }
  // opposite role: inState this role: classifierInState
  private final static Method _inState_setMethod = getMethod1(MClassifierInStateImpl.class, "setInStates", Collection.class);
  private final static Method _inState_addMethod = getMethod1(MClassifierInStateImpl.class, "addInState", MState.class);
  private final static Method _inState_removeMethod = getMethod1(MClassifierInStateImpl.class, "removeInState", MState.class);
  Collection _inState = Collections.EMPTY_LIST;
  Collection _inState_ucopy = Collections.EMPTY_LIST;
  public final Collection getInStates()
  {
    checkExists();
    if (null == _inState_ucopy)
    {
      _inState_ucopy = ucopy(_inState);
    }
    return _inState_ucopy;
  }
  public final void setInStates(Collection __arg)
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
        old = getInStates();
      }
      _inState_ucopy = null;
      Collection __added = bagdiff(__arg,_inState);
      Collection __removed = bagdiff(_inState, __arg);
      Iterator iter1 = __removed.iterator();
      while (iter1.hasNext())
      {
        MState o = (MState)iter1.next();
        o.internalUnrefByClassifierInState(this);
      }
      Iterator iter2 = __added.iterator();
      while (iter2.hasNext())
      {
        MState o = (MState)iter2.next();
        o.internalRefByClassifierInState(this);
      }
      _inState = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_inState_setMethod, old, getInStates());
      }
      if (sendEvent)
      {
        fireBagSet("inState", old, getInStates());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addInState(MState __arg)
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
        old = getInStates();
      }
      if (null != _inState_ucopy)
      {
        _inState = new ArrayList(_inState);
        _inState_ucopy = null;
      }
      __arg.internalRefByClassifierInState(this);
      _inState.add(__arg);
      logBagAdd(_inState_addMethod, _inState_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("inState", old, getInStates(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeInState(MState __arg)
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
        old = getInStates();
      }
      if (null != _inState_ucopy)
      {
        _inState = new ArrayList(_inState);
        _inState_ucopy = null;
      }
      if (!_inState.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByClassifierInState(this);
      logBagRemove(_inState_removeMethod, _inState_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("inState", old, getInStates(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByInState(MState __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getInStates();
    }
    if (null != _inState_ucopy)
    {
      _inState = new ArrayList(_inState);
      _inState_ucopy = null;
    }
    _inState.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("inState", old, getInStates(), __arg);
    }
  }
  public final void internalUnrefByInState(MState __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getInStates();
    }
    if (null != _inState_ucopy)
    {
      _inState = new ArrayList(_inState);
      _inState_ucopy = null;
    }
    _inState.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("inState", old, getInStates(), __arg);
    }
  }
  protected void cleanup(Collection scheduledForRemove)
  {
    // opposite role: type this role: classifierInState
    if (_type != null )
    {
      setType(null);
    }
    // opposite role: inState this role: classifierInState
    if (_inState.size() != 0)
    {
      setInStates(Collections.EMPTY_LIST);
    }
    super.cleanup(scheduledForRemove);
  }

  public String getUMLClassName()
  {
    return "ClassifierInState";
  }

  // Reflective API

  public Object reflectiveGetValue(String feature)
  {
    if ("type".equals(feature))
    {
      return getType();
    }
    if ("inState".equals(feature))
    {
      return getInStates();
    }

    return super.reflectiveGetValue(feature);
  }

  public void reflectiveSetValue(String feature, Object obj)
  {
    if ("type".equals(feature))
    {
      setType((MClassifier)obj);
      return;
    }
    if ("inState".equals(feature))
    {
      setInStates((Collection)obj);
      return;
    }

    super.reflectiveSetValue(feature, obj);
  }

  public void reflectiveAddValue(String feature, Object obj)
  {
    if ("inState".equals(feature))
    {
      addInState((MState)obj);
      return;
    }

    super.reflectiveAddValue(feature, obj);
  }

  public void reflectiveRemoveValue(String feature, Object obj)
  {
    if ("inState".equals(feature))
    {
      removeInState((MState)obj);
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
