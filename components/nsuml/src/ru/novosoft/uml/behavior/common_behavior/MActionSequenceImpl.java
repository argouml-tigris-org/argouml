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

public class MActionSequenceImpl extends MActionImpl implements MActionSequence
{
  // ------------ code for class ActionSequence -----------------
  // generating attributes
  // generating associations
  // opposite role: action this role: actionSequence
  private final static Method _action_setMethod = getMethod1(MActionSequenceImpl.class, "setActions", List.class);
  private final static Method _action_removeMethod = getMethod1(MActionSequenceImpl.class, "removeAction", int.class);
  private final static Method _action_addMethod = getMethod2(MActionSequenceImpl.class, "addAction", int.class, MAction.class);
  private final static Method _action_listSetMethod = getMethod2(MActionSequenceImpl.class, "setAction", int.class, MAction.class);
  List _action = Collections.EMPTY_LIST;
  List _action_ucopy = Collections.EMPTY_LIST;
  public final List getActions()
  {
    checkExists();
    if (null == _action_ucopy)
    {
      _action_ucopy = ucopy(_action);
    }
    return _action_ucopy;
  }
  public final void setActions(List __arg)
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
      List old = null;
      if (sendEvent || logForUndo)
      {
        old = getActions();
      }
      _action_ucopy = null;
      Collection __added = bagdiff(__arg,_action);
      Collection __removed = bagdiff(_action, __arg);
      Iterator iter1 = __removed.iterator();
      while (iter1.hasNext())
      {
        MAction o = (MAction)iter1.next();
        o.internalUnrefByActionSequence(this);
      }
      Iterator iter2 = __added.iterator();
      while (iter2.hasNext())
      {
        MAction o = (MAction)iter2.next();
        o.internalRefByActionSequence(this);
      }
      _action = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_action_setMethod, old, getActions());
      }
      if (sendEvent)
      {
        fireListSet("action", old, getActions());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addAction(MAction __arg)
  {
    addAction(_action.size(), __arg);
  }
  public final void removeAction(MAction __arg)
  {
    if (__arg == null)
    {
      throw new NullPointerException();
    }
    int __pos = _action.indexOf(__arg);
    removeAction(__pos);
  }
  public final void addAction(int __pos, MAction __arg)
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
      List old = null;
      if (sendEvent)
      {
        old = getActions();
      }
      if (null != _action_ucopy)
      {
        _action = new ArrayList(_action);
        _action_ucopy = null;
      }
      _action.add(__pos, __arg);
      __arg.internalRefByActionSequence(this);
      logListAdd(_action_addMethod, _action_removeMethod, __arg, __pos);
      if (sendEvent)
      {
        fireListAdd("action", old, getActions(), __arg, __pos);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeAction(int __pos)
  {
    operationStarted();
    try
    {
      checkExists();
      final boolean sendEvent = needEvent();
      List old = null;
      if (sendEvent)
      {
        old = getActions();
      }
      if (null != _action_ucopy)
      {
        _action = new ArrayList(_action);
        _action_ucopy = null;
      }
      MAction __arg = (MAction)_action.remove(__pos);
      __arg.internalUnrefByActionSequence(this);
      logListRemove(_action_removeMethod, _action_addMethod, __arg, __pos);
      if (sendEvent)
      {
        fireListRemove("action", old, getActions(), __arg, __pos);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void setAction(int __pos, MAction __arg)
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
      List old = null;
      if (sendEvent)
      {
        old = getActions();
      }
      if (null != _action_ucopy)
      {
        _action = new ArrayList(_action);
        _action_ucopy = null;
      }
      MAction __old = (MAction)_action.get(__pos);
      __old.internalUnrefByActionSequence(this);
      __arg.internalRefByActionSequence(this);
      _action.set(__pos,__arg);
      logListSet(_action_listSetMethod, __old, __arg, __pos);
      if (sendEvent)
      {
        fireListItemSet("action", old, getActions(), __old, __arg, __pos);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final MAction getAction(int __pos)
  {
    checkExists();
    return (MAction)_action.get(__pos);
  }
  protected void cleanup(Collection scheduledForRemove)
  {
    // opposite role: action this role: actionSequence
    if (_action.size() != 0)
    {
      scheduledForRemove.addAll(_action);
      setActions(Collections.EMPTY_LIST);
    }
    super.cleanup(scheduledForRemove);
  }

  public String getUMLClassName()
  {
    return "ActionSequence";
  }

  // Reflective API

  public Object reflectiveGetValue(String feature)
  {
    if ("action".equals(feature))
    {
      return getActions();
    }

    return super.reflectiveGetValue(feature);
  }

  public void reflectiveSetValue(String feature, Object obj)
  {
    if ("action".equals(feature))
    {
      setActions((List)obj);
      return;
    }

    super.reflectiveSetValue(feature, obj);
  }

  public void reflectiveAddValue(String feature, Object obj)
  {
    if ("action".equals(feature))
    {
      addAction((MAction)obj);
      return;
    }

    super.reflectiveAddValue(feature, obj);
  }

  public void reflectiveRemoveValue(String feature, Object obj)
  {
    if ("action".equals(feature))
    {
      removeAction((MAction)obj);
      return;
    }

    super.reflectiveRemoveValue(feature, obj);
  }

  public Object reflectiveGetValue(String feature, int pos)
  {
    if ("action".equals(feature))
    {
      return getAction(pos);
    }

    return super.reflectiveGetValue(feature, pos);
  }

  public void reflectiveSetValue(String feature, int pos, Object obj)
  {
    if ("action".equals(feature))
    {
      setAction(pos, (MAction)obj);
      return;
    }

    super.reflectiveSetValue(feature, pos, obj);
  }

  public void reflectiveAddValue(String feature, int pos, Object obj)
  {
    if ("action".equals(feature))
    {
      addAction(pos, (MAction)obj);
      return;
    }

    super.reflectiveAddValue(feature, pos, obj);
  }

  public void reflectiveRemoveValue(String feature, int pos)
  {
    if ("action".equals(feature))
    {
      removeAction(pos);
      return;
    }

    super.reflectiveRemoveValue(feature, pos);
  }
  public Collection getModelElementContents()
  {
    Collection ret = super.getModelElementContents();
    ret.addAll(getActions());
    return ret;
  }
}
