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

package ru.novosoft.uml.behavior.use_cases;

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
import ru.novosoft.uml.behavior.state_machines.*;
import ru.novosoft.uml.behavior.activity_graphs.*;
import ru.novosoft.uml.model_management.*;

import java.lang.reflect.Method;

public class MExtendImpl extends MRelationshipImpl implements MExtend
{
  // ------------ code for class Extend -----------------
  // generating attributes
  // attribute: condition
  private final static Method _condition_setMethod = getMethod1(MExtendImpl.class, "setCondition", MBooleanExpression.class);
  MBooleanExpression _condition;
  public final MBooleanExpression getCondition()
  {
    checkExists();
    return _condition;
  }
  public final void setCondition(MBooleanExpression __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      logAttrSet(_condition_setMethod, _condition, __arg);
      fireAttrSet("condition", _condition, __arg);
      _condition = __arg;
    }
    finally
    {
      operationFinished();
    }
  }
  // generating associations
  // opposite role: extensionPoint this role: extend
  private final static Method _extensionPoint_setMethod = getMethod1(MExtendImpl.class, "setExtensionPoints", List.class);
  private final static Method _extensionPoint_removeMethod = getMethod1(MExtendImpl.class, "removeExtensionPoint", int.class);
  private final static Method _extensionPoint_addMethod = getMethod2(MExtendImpl.class, "addExtensionPoint", int.class, MExtensionPoint.class);
  private final static Method _extensionPoint_listSetMethod = getMethod2(MExtendImpl.class, "setExtensionPoint", int.class, MExtensionPoint.class);
  List _extensionPoint = Collections.EMPTY_LIST;
  List _extensionPoint_ucopy = Collections.EMPTY_LIST;
  public final List getExtensionPoints()
  {
    checkExists();
    if (null == _extensionPoint_ucopy)
    {
      _extensionPoint_ucopy = ucopy(_extensionPoint);
    }
    return _extensionPoint_ucopy;
  }
  public final void setExtensionPoints(List __arg)
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
        old = getExtensionPoints();
      }
      _extensionPoint_ucopy = null;
      Collection __added = bagdiff(__arg,_extensionPoint);
      Collection __removed = bagdiff(_extensionPoint, __arg);
      Iterator iter1 = __removed.iterator();
      while (iter1.hasNext())
      {
        MExtensionPoint o = (MExtensionPoint)iter1.next();
        o.internalUnrefByExtend(this);
      }
      Iterator iter2 = __added.iterator();
      while (iter2.hasNext())
      {
        MExtensionPoint o = (MExtensionPoint)iter2.next();
        o.internalRefByExtend(this);
      }
      _extensionPoint = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_extensionPoint_setMethod, old, getExtensionPoints());
      }
      if (sendEvent)
      {
        fireListSet("extensionPoint", old, getExtensionPoints());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addExtensionPoint(MExtensionPoint __arg)
  {
    addExtensionPoint(_extensionPoint.size(), __arg);
  }
  public final void removeExtensionPoint(MExtensionPoint __arg)
  {
    if (__arg == null)
    {
      throw new NullPointerException();
    }
    int __pos = _extensionPoint.indexOf(__arg);
    removeExtensionPoint(__pos);
  }
  public final void addExtensionPoint(int __pos, MExtensionPoint __arg)
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
        old = getExtensionPoints();
      }
      if (null != _extensionPoint_ucopy)
      {
        _extensionPoint = new ArrayList(_extensionPoint);
        _extensionPoint_ucopy = null;
      }
      _extensionPoint.add(__pos, __arg);
      __arg.internalRefByExtend(this);
      logListAdd(_extensionPoint_addMethod, _extensionPoint_removeMethod, __arg, __pos);
      if (sendEvent)
      {
        fireListAdd("extensionPoint", old, getExtensionPoints(), __arg, __pos);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeExtensionPoint(int __pos)
  {
    operationStarted();
    try
    {
      checkExists();
      final boolean sendEvent = needEvent();
      List old = null;
      if (sendEvent)
      {
        old = getExtensionPoints();
      }
      if (null != _extensionPoint_ucopy)
      {
        _extensionPoint = new ArrayList(_extensionPoint);
        _extensionPoint_ucopy = null;
      }
      MExtensionPoint __arg = (MExtensionPoint)_extensionPoint.remove(__pos);
      __arg.internalUnrefByExtend(this);
      logListRemove(_extensionPoint_removeMethod, _extensionPoint_addMethod, __arg, __pos);
      if (sendEvent)
      {
        fireListRemove("extensionPoint", old, getExtensionPoints(), __arg, __pos);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void setExtensionPoint(int __pos, MExtensionPoint __arg)
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
        old = getExtensionPoints();
      }
      if (null != _extensionPoint_ucopy)
      {
        _extensionPoint = new ArrayList(_extensionPoint);
        _extensionPoint_ucopy = null;
      }
      MExtensionPoint __old = (MExtensionPoint)_extensionPoint.get(__pos);
      __old.internalUnrefByExtend(this);
      __arg.internalRefByExtend(this);
      _extensionPoint.set(__pos,__arg);
      logListSet(_extensionPoint_listSetMethod, __old, __arg, __pos);
      if (sendEvent)
      {
        fireListItemSet("extensionPoint", old, getExtensionPoints(), __old, __arg, __pos);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final MExtensionPoint getExtensionPoint(int __pos)
  {
    checkExists();
    return (MExtensionPoint)_extensionPoint.get(__pos);
  }
  // opposite role: extension this role: extend
  private final static Method _extension_setMethod = getMethod1(MExtendImpl.class, "setExtension", MUseCase.class);
  MUseCase _extension;
  public final MUseCase getExtension()
  {
    checkExists();
    return _extension;
  }
  public final void setExtension(MUseCase __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      MUseCase __saved = _extension;
      if (_extension != __arg)
      {
        if (__saved != null)
        {
          __saved.internalUnrefByExtend(this);
        }
        if (__arg != null)
        {
          __arg.internalRefByExtend(this);
        }
        logRefSet(_extension_setMethod, __saved, __arg);
        fireRefSet("extension", __saved, __arg);
        _extension = __arg;
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByExtension(MUseCase __arg)
  {
    MUseCase __saved = _extension;
    if (_extension != null)
    {
      _extension.removeExtend(this);
    }
    fireRefSet("extension", __saved, __arg);
    _extension = __arg;
  }
  public final void internalUnrefByExtension(MUseCase __arg)
  {
    fireRefSet("extension", _extension, null);
    _extension = null;
  }
  // opposite role: base this role: extend2
  private final static Method _base_setMethod = getMethod1(MExtendImpl.class, "setBase", MUseCase.class);
  MUseCase _base;
  public final MUseCase getBase()
  {
    checkExists();
    return _base;
  }
  public final void setBase(MUseCase __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      MUseCase __saved = _base;
      if (_base != __arg)
      {
        if (__saved != null)
        {
          __saved.internalUnrefByExtend2(this);
        }
        if (__arg != null)
        {
          __arg.internalRefByExtend2(this);
        }
        logRefSet(_base_setMethod, __saved, __arg);
        fireRefSet("base", __saved, __arg);
        _base = __arg;
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByBase(MUseCase __arg)
  {
    MUseCase __saved = _base;
    if (_base != null)
    {
      _base.removeExtend2(this);
    }
    fireRefSet("base", __saved, __arg);
    _base = __arg;
  }
  public final void internalUnrefByBase(MUseCase __arg)
  {
    fireRefSet("base", _base, null);
    _base = null;
  }
  protected void cleanup(Collection scheduledForRemove)
  {
    // opposite role: extensionPoint this role: extend
    if (_extensionPoint.size() != 0)
    {
      setExtensionPoints(Collections.EMPTY_LIST);
    }
    // opposite role: extension this role: extend
    if (_extension != null )
    {
      setExtension(null);
    }
    // opposite role: base this role: extend2
    if (_base != null )
    {
      setBase(null);
    }
    super.cleanup(scheduledForRemove);
  }

  public String getUMLClassName()
  {
    return "Extend";
  }

  // Reflective API

  public Object reflectiveGetValue(String feature)
  {
    if ("condition".equals(feature))
    {
      return getCondition();
    }
    if ("extensionPoint".equals(feature))
    {
      return getExtensionPoints();
    }
    if ("extension".equals(feature))
    {
      return getExtension();
    }
    if ("base".equals(feature))
    {
      return getBase();
    }

    return super.reflectiveGetValue(feature);
  }

  public void reflectiveSetValue(String feature, Object obj)
  {
    if ("condition".equals(feature))
    {
      setCondition((MBooleanExpression)obj);
      return;
    }
    if ("extensionPoint".equals(feature))
    {
      setExtensionPoints((List)obj);
      return;
    }
    if ("extension".equals(feature))
    {
      setExtension((MUseCase)obj);
      return;
    }
    if ("base".equals(feature))
    {
      setBase((MUseCase)obj);
      return;
    }

    super.reflectiveSetValue(feature, obj);
  }

  public void reflectiveAddValue(String feature, Object obj)
  {
    if ("extensionPoint".equals(feature))
    {
      addExtensionPoint((MExtensionPoint)obj);
      return;
    }

    super.reflectiveAddValue(feature, obj);
  }

  public void reflectiveRemoveValue(String feature, Object obj)
  {
    if ("extensionPoint".equals(feature))
    {
      removeExtensionPoint((MExtensionPoint)obj);
      return;
    }

    super.reflectiveRemoveValue(feature, obj);
  }

  public Object reflectiveGetValue(String feature, int pos)
  {
    if ("extensionPoint".equals(feature))
    {
      return getExtensionPoint(pos);
    }

    return super.reflectiveGetValue(feature, pos);
  }

  public void reflectiveSetValue(String feature, int pos, Object obj)
  {
    if ("extensionPoint".equals(feature))
    {
      setExtensionPoint(pos, (MExtensionPoint)obj);
      return;
    }

    super.reflectiveSetValue(feature, pos, obj);
  }

  public void reflectiveAddValue(String feature, int pos, Object obj)
  {
    if ("extensionPoint".equals(feature))
    {
      addExtensionPoint(pos, (MExtensionPoint)obj);
      return;
    }

    super.reflectiveAddValue(feature, pos, obj);
  }

  public void reflectiveRemoveValue(String feature, int pos)
  {
    if ("extensionPoint".equals(feature))
    {
      removeExtensionPoint(pos);
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
