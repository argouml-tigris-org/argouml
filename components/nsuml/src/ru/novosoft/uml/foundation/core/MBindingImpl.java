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

public class MBindingImpl extends MDependencyImpl implements MBinding
{
  // ------------ code for class Binding -----------------
  // generating attributes
  // generating associations
  // opposite role: argument this role: binding
  private final static Method _argument_setMethod = getMethod1(MBindingImpl.class, "setArguments", List.class);
  private final static Method _argument_removeMethod = getMethod1(MBindingImpl.class, "removeArgument", int.class);
  private final static Method _argument_addMethod = getMethod2(MBindingImpl.class, "addArgument", int.class, MModelElement.class);
  private final static Method _argument_listSetMethod = getMethod2(MBindingImpl.class, "setArgument", int.class, MModelElement.class);
  List _argument = Collections.EMPTY_LIST;
  List _argument_ucopy = Collections.EMPTY_LIST;
  public final List getArguments()
  {
    checkExists();
    if (null == _argument_ucopy)
    {
      _argument_ucopy = ucopy(_argument);
    }
    return _argument_ucopy;
  }
  public final void setArguments(List __arg)
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
        old = getArguments();
      }
      _argument_ucopy = null;
      Collection __added = bagdiff(__arg,_argument);
      Collection __removed = bagdiff(_argument, __arg);
      Iterator iter1 = __removed.iterator();
      while (iter1.hasNext())
      {
        MModelElement o = (MModelElement)iter1.next();
        o.internalUnrefByBinding(this);
      }
      Iterator iter2 = __added.iterator();
      while (iter2.hasNext())
      {
        MModelElement o = (MModelElement)iter2.next();
        o.internalRefByBinding(this);
      }
      _argument = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_argument_setMethod, old, getArguments());
      }
      if (sendEvent)
      {
        fireListSet("argument", old, getArguments());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addArgument(MModelElement __arg)
  {
    addArgument(_argument.size(), __arg);
  }
  public final void removeArgument(MModelElement __arg)
  {
    if (__arg == null)
    {
      throw new NullPointerException();
    }
    int __pos = _argument.indexOf(__arg);
    removeArgument(__pos);
  }
  public final void addArgument(int __pos, MModelElement __arg)
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
        old = getArguments();
      }
      if (null != _argument_ucopy)
      {
        _argument = new ArrayList(_argument);
        _argument_ucopy = null;
      }
      _argument.add(__pos, __arg);
      __arg.internalRefByBinding(this);
      logListAdd(_argument_addMethod, _argument_removeMethod, __arg, __pos);
      if (sendEvent)
      {
        fireListAdd("argument", old, getArguments(), __arg, __pos);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeArgument(int __pos)
  {
    operationStarted();
    try
    {
      checkExists();
      final boolean sendEvent = needEvent();
      List old = null;
      if (sendEvent)
      {
        old = getArguments();
      }
      if (null != _argument_ucopy)
      {
        _argument = new ArrayList(_argument);
        _argument_ucopy = null;
      }
      MModelElement __arg = (MModelElement)_argument.remove(__pos);
      __arg.internalUnrefByBinding(this);
      logListRemove(_argument_removeMethod, _argument_addMethod, __arg, __pos);
      if (sendEvent)
      {
        fireListRemove("argument", old, getArguments(), __arg, __pos);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void setArgument(int __pos, MModelElement __arg)
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
        old = getArguments();
      }
      if (null != _argument_ucopy)
      {
        _argument = new ArrayList(_argument);
        _argument_ucopy = null;
      }
      MModelElement __old = (MModelElement)_argument.get(__pos);
      __old.internalUnrefByBinding(this);
      __arg.internalRefByBinding(this);
      _argument.set(__pos,__arg);
      logListSet(_argument_listSetMethod, __old, __arg, __pos);
      if (sendEvent)
      {
        fireListItemSet("argument", old, getArguments(), __old, __arg, __pos);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final MModelElement getArgument(int __pos)
  {
    checkExists();
    return (MModelElement)_argument.get(__pos);
  }
  protected void cleanup(Collection scheduledForRemove)
  {
    // opposite role: argument this role: binding
    if (_argument.size() != 0)
    {
      setArguments(Collections.EMPTY_LIST);
    }
    super.cleanup(scheduledForRemove);
  }

  public String getUMLClassName()
  {
    return "Binding";
  }

  // Reflective API

  public Object reflectiveGetValue(String feature)
  {
    if ("argument".equals(feature))
    {
      return getArguments();
    }

    return super.reflectiveGetValue(feature);
  }

  public void reflectiveSetValue(String feature, Object obj)
  {
    if ("argument".equals(feature))
    {
      setArguments((List)obj);
      return;
    }

    super.reflectiveSetValue(feature, obj);
  }

  public void reflectiveAddValue(String feature, Object obj)
  {
    if ("argument".equals(feature))
    {
      addArgument((MModelElement)obj);
      return;
    }

    super.reflectiveAddValue(feature, obj);
  }

  public void reflectiveRemoveValue(String feature, Object obj)
  {
    if ("argument".equals(feature))
    {
      removeArgument((MModelElement)obj);
      return;
    }

    super.reflectiveRemoveValue(feature, obj);
  }

  public Object reflectiveGetValue(String feature, int pos)
  {
    if ("argument".equals(feature))
    {
      return getArgument(pos);
    }

    return super.reflectiveGetValue(feature, pos);
  }

  public void reflectiveSetValue(String feature, int pos, Object obj)
  {
    if ("argument".equals(feature))
    {
      setArgument(pos, (MModelElement)obj);
      return;
    }

    super.reflectiveSetValue(feature, pos, obj);
  }

  public void reflectiveAddValue(String feature, int pos, Object obj)
  {
    if ("argument".equals(feature))
    {
      addArgument(pos, (MModelElement)obj);
      return;
    }

    super.reflectiveAddValue(feature, pos, obj);
  }

  public void reflectiveRemoveValue(String feature, int pos)
  {
    if ("argument".equals(feature))
    {
      removeArgument(pos);
      return;
    }

    super.reflectiveRemoveValue(feature, pos);
  }
  public Collection getModelElementContents()
  {
    Collection ret = super.getModelElementContents();
    return ret;
  }
}
