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

public class MUseCaseImpl extends MClassifierImpl implements MUseCase
{
  // ------------ code for class UseCase -----------------
  // generating attributes
  // generating associations
  // opposite role: extensionPoint this role: useCase
  private final static Method _extensionPoint_setMethod = getMethod1(MUseCaseImpl.class, "setExtensionPoints", Collection.class);
  private final static Method _extensionPoint_addMethod = getMethod1(MUseCaseImpl.class, "addExtensionPoint", MExtensionPoint.class);
  private final static Method _extensionPoint_removeMethod = getMethod1(MUseCaseImpl.class, "removeExtensionPoint", MExtensionPoint.class);
  Collection _extensionPoint = Collections.EMPTY_LIST;
  Collection _extensionPoint_ucopy = Collections.EMPTY_LIST;
  public final Collection getExtensionPoints()
  {
    checkExists();
    if (null == _extensionPoint_ucopy)
    {
      _extensionPoint_ucopy = ucopy(_extensionPoint);
    }
    return _extensionPoint_ucopy;
  }
  public final void setExtensionPoints(Collection __arg)
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
        old = getExtensionPoints();
      }
      _extensionPoint_ucopy = null;
      Collection __added = bagdiff(__arg,_extensionPoint);
      Collection __removed = bagdiff(_extensionPoint, __arg);
      Iterator iter1 = __removed.iterator();
      while (iter1.hasNext())
      {
        MExtensionPoint o = (MExtensionPoint)iter1.next();
        o.internalUnrefByUseCase(this);
      }
      Iterator iter2 = __added.iterator();
      while (iter2.hasNext())
      {
        MExtensionPoint o = (MExtensionPoint)iter2.next();
        o.internalRefByUseCase(this);
      }
      _extensionPoint = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_extensionPoint_setMethod, old, getExtensionPoints());
      }
      if (sendEvent)
      {
        fireBagSet("extensionPoint", old, getExtensionPoints());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addExtensionPoint(MExtensionPoint __arg)
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
        old = getExtensionPoints();
      }
      if (null != _extensionPoint_ucopy)
      {
        _extensionPoint = new ArrayList(_extensionPoint);
        _extensionPoint_ucopy = null;
      }
      __arg.internalRefByUseCase(this);
      _extensionPoint.add(__arg);
      logBagAdd(_extensionPoint_addMethod, _extensionPoint_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("extensionPoint", old, getExtensionPoints(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeExtensionPoint(MExtensionPoint __arg)
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
        old = getExtensionPoints();
      }
      if (null != _extensionPoint_ucopy)
      {
        _extensionPoint = new ArrayList(_extensionPoint);
        _extensionPoint_ucopy = null;
      }
      if (!_extensionPoint.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByUseCase(this);
      logBagRemove(_extensionPoint_removeMethod, _extensionPoint_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("extensionPoint", old, getExtensionPoints(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByExtensionPoint(MExtensionPoint __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getExtensionPoints();
    }
    if (null != _extensionPoint_ucopy)
    {
      _extensionPoint = new ArrayList(_extensionPoint);
      _extensionPoint_ucopy = null;
    }
    _extensionPoint.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("extensionPoint", old, getExtensionPoints(), __arg);
    }
  }
  public final void internalUnrefByExtensionPoint(MExtensionPoint __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getExtensionPoints();
    }
    if (null != _extensionPoint_ucopy)
    {
      _extensionPoint = new ArrayList(_extensionPoint);
      _extensionPoint_ucopy = null;
    }
    _extensionPoint.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("extensionPoint", old, getExtensionPoints(), __arg);
    }
  }
  // opposite role: include2 this role: base
  private final static Method _include2_setMethod = getMethod1(MUseCaseImpl.class, "setIncludes2", Collection.class);
  private final static Method _include2_addMethod = getMethod1(MUseCaseImpl.class, "addInclude2", MInclude.class);
  private final static Method _include2_removeMethod = getMethod1(MUseCaseImpl.class, "removeInclude2", MInclude.class);
  Collection _include2 = Collections.EMPTY_LIST;
  Collection _include2_ucopy = Collections.EMPTY_LIST;
  public final Collection getIncludes2()
  {
    checkExists();
    if (null == _include2_ucopy)
    {
      _include2_ucopy = ucopy(_include2);
    }
    return _include2_ucopy;
  }
  public final void setIncludes2(Collection __arg)
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
        old = getIncludes2();
      }
      _include2_ucopy = null;
      Collection __added = bagdiff(__arg,_include2);
      Collection __removed = bagdiff(_include2, __arg);
      Iterator iter3 = __removed.iterator();
      while (iter3.hasNext())
      {
        MInclude o = (MInclude)iter3.next();
        o.internalUnrefByBase(this);
      }
      Iterator iter4 = __added.iterator();
      while (iter4.hasNext())
      {
        MInclude o = (MInclude)iter4.next();
        o.internalRefByBase(this);
      }
      _include2 = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_include2_setMethod, old, getIncludes2());
      }
      if (sendEvent)
      {
        fireBagSet("include2", old, getIncludes2());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addInclude2(MInclude __arg)
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
        old = getIncludes2();
      }
      if (null != _include2_ucopy)
      {
        _include2 = new ArrayList(_include2);
        _include2_ucopy = null;
      }
      __arg.internalRefByBase(this);
      _include2.add(__arg);
      logBagAdd(_include2_addMethod, _include2_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("include2", old, getIncludes2(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeInclude2(MInclude __arg)
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
        old = getIncludes2();
      }
      if (null != _include2_ucopy)
      {
        _include2 = new ArrayList(_include2);
        _include2_ucopy = null;
      }
      if (!_include2.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByBase(this);
      logBagRemove(_include2_removeMethod, _include2_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("include2", old, getIncludes2(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByInclude2(MInclude __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getIncludes2();
    }
    if (null != _include2_ucopy)
    {
      _include2 = new ArrayList(_include2);
      _include2_ucopy = null;
    }
    _include2.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("include2", old, getIncludes2(), __arg);
    }
  }
  public final void internalUnrefByInclude2(MInclude __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getIncludes2();
    }
    if (null != _include2_ucopy)
    {
      _include2 = new ArrayList(_include2);
      _include2_ucopy = null;
    }
    _include2.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("include2", old, getIncludes2(), __arg);
    }
  }
  // opposite role: include this role: addition
  private final static Method _include_setMethod = getMethod1(MUseCaseImpl.class, "setIncludes", Collection.class);
  private final static Method _include_addMethod = getMethod1(MUseCaseImpl.class, "addInclude", MInclude.class);
  private final static Method _include_removeMethod = getMethod1(MUseCaseImpl.class, "removeInclude", MInclude.class);
  Collection _include = Collections.EMPTY_LIST;
  Collection _include_ucopy = Collections.EMPTY_LIST;
  public final Collection getIncludes()
  {
    checkExists();
    if (null == _include_ucopy)
    {
      _include_ucopy = ucopy(_include);
    }
    return _include_ucopy;
  }
  public final void setIncludes(Collection __arg)
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
        old = getIncludes();
      }
      _include_ucopy = null;
      Collection __added = bagdiff(__arg,_include);
      Collection __removed = bagdiff(_include, __arg);
      Iterator iter5 = __removed.iterator();
      while (iter5.hasNext())
      {
        MInclude o = (MInclude)iter5.next();
        o.internalUnrefByAddition(this);
      }
      Iterator iter6 = __added.iterator();
      while (iter6.hasNext())
      {
        MInclude o = (MInclude)iter6.next();
        o.internalRefByAddition(this);
      }
      _include = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_include_setMethod, old, getIncludes());
      }
      if (sendEvent)
      {
        fireBagSet("include", old, getIncludes());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addInclude(MInclude __arg)
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
        old = getIncludes();
      }
      if (null != _include_ucopy)
      {
        _include = new ArrayList(_include);
        _include_ucopy = null;
      }
      __arg.internalRefByAddition(this);
      _include.add(__arg);
      logBagAdd(_include_addMethod, _include_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("include", old, getIncludes(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeInclude(MInclude __arg)
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
        old = getIncludes();
      }
      if (null != _include_ucopy)
      {
        _include = new ArrayList(_include);
        _include_ucopy = null;
      }
      if (!_include.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByAddition(this);
      logBagRemove(_include_removeMethod, _include_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("include", old, getIncludes(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByInclude(MInclude __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getIncludes();
    }
    if (null != _include_ucopy)
    {
      _include = new ArrayList(_include);
      _include_ucopy = null;
    }
    _include.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("include", old, getIncludes(), __arg);
    }
  }
  public final void internalUnrefByInclude(MInclude __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getIncludes();
    }
    if (null != _include_ucopy)
    {
      _include = new ArrayList(_include);
      _include_ucopy = null;
    }
    _include.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("include", old, getIncludes(), __arg);
    }
  }
  // opposite role: extend this role: extension
  private final static Method _extend_setMethod = getMethod1(MUseCaseImpl.class, "setExtends", Collection.class);
  private final static Method _extend_addMethod = getMethod1(MUseCaseImpl.class, "addExtend", MExtend.class);
  private final static Method _extend_removeMethod = getMethod1(MUseCaseImpl.class, "removeExtend", MExtend.class);
  Collection _extend = Collections.EMPTY_LIST;
  Collection _extend_ucopy = Collections.EMPTY_LIST;
  public final Collection getExtends()
  {
    checkExists();
    if (null == _extend_ucopy)
    {
      _extend_ucopy = ucopy(_extend);
    }
    return _extend_ucopy;
  }
  public final void setExtends(Collection __arg)
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
        old = getExtends();
      }
      _extend_ucopy = null;
      Collection __added = bagdiff(__arg,_extend);
      Collection __removed = bagdiff(_extend, __arg);
      Iterator iter7 = __removed.iterator();
      while (iter7.hasNext())
      {
        MExtend o = (MExtend)iter7.next();
        o.internalUnrefByExtension(this);
      }
      Iterator iter8 = __added.iterator();
      while (iter8.hasNext())
      {
        MExtend o = (MExtend)iter8.next();
        o.internalRefByExtension(this);
      }
      _extend = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_extend_setMethod, old, getExtends());
      }
      if (sendEvent)
      {
        fireBagSet("extend", old, getExtends());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addExtend(MExtend __arg)
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
        old = getExtends();
      }
      if (null != _extend_ucopy)
      {
        _extend = new ArrayList(_extend);
        _extend_ucopy = null;
      }
      __arg.internalRefByExtension(this);
      _extend.add(__arg);
      logBagAdd(_extend_addMethod, _extend_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("extend", old, getExtends(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeExtend(MExtend __arg)
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
        old = getExtends();
      }
      if (null != _extend_ucopy)
      {
        _extend = new ArrayList(_extend);
        _extend_ucopy = null;
      }
      if (!_extend.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByExtension(this);
      logBagRemove(_extend_removeMethod, _extend_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("extend", old, getExtends(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByExtend(MExtend __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getExtends();
    }
    if (null != _extend_ucopy)
    {
      _extend = new ArrayList(_extend);
      _extend_ucopy = null;
    }
    _extend.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("extend", old, getExtends(), __arg);
    }
  }
  public final void internalUnrefByExtend(MExtend __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getExtends();
    }
    if (null != _extend_ucopy)
    {
      _extend = new ArrayList(_extend);
      _extend_ucopy = null;
    }
    _extend.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("extend", old, getExtends(), __arg);
    }
  }
  // opposite role: extend2 this role: base
  private final static Method _extend2_setMethod = getMethod1(MUseCaseImpl.class, "setExtends2", Collection.class);
  private final static Method _extend2_addMethod = getMethod1(MUseCaseImpl.class, "addExtend2", MExtend.class);
  private final static Method _extend2_removeMethod = getMethod1(MUseCaseImpl.class, "removeExtend2", MExtend.class);
  Collection _extend2 = Collections.EMPTY_LIST;
  Collection _extend2_ucopy = Collections.EMPTY_LIST;
  public final Collection getExtends2()
  {
    checkExists();
    if (null == _extend2_ucopy)
    {
      _extend2_ucopy = ucopy(_extend2);
    }
    return _extend2_ucopy;
  }
  public final void setExtends2(Collection __arg)
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
        old = getExtends2();
      }
      _extend2_ucopy = null;
      Collection __added = bagdiff(__arg,_extend2);
      Collection __removed = bagdiff(_extend2, __arg);
      Iterator iter9 = __removed.iterator();
      while (iter9.hasNext())
      {
        MExtend o = (MExtend)iter9.next();
        o.internalUnrefByBase(this);
      }
      Iterator iter10 = __added.iterator();
      while (iter10.hasNext())
      {
        MExtend o = (MExtend)iter10.next();
        o.internalRefByBase(this);
      }
      _extend2 = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_extend2_setMethod, old, getExtends2());
      }
      if (sendEvent)
      {
        fireBagSet("extend2", old, getExtends2());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addExtend2(MExtend __arg)
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
        old = getExtends2();
      }
      if (null != _extend2_ucopy)
      {
        _extend2 = new ArrayList(_extend2);
        _extend2_ucopy = null;
      }
      __arg.internalRefByBase(this);
      _extend2.add(__arg);
      logBagAdd(_extend2_addMethod, _extend2_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("extend2", old, getExtends2(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeExtend2(MExtend __arg)
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
        old = getExtends2();
      }
      if (null != _extend2_ucopy)
      {
        _extend2 = new ArrayList(_extend2);
        _extend2_ucopy = null;
      }
      if (!_extend2.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByBase(this);
      logBagRemove(_extend2_removeMethod, _extend2_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("extend2", old, getExtends2(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByExtend2(MExtend __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getExtends2();
    }
    if (null != _extend2_ucopy)
    {
      _extend2 = new ArrayList(_extend2);
      _extend2_ucopy = null;
    }
    _extend2.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("extend2", old, getExtends2(), __arg);
    }
  }
  public final void internalUnrefByExtend2(MExtend __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getExtends2();
    }
    if (null != _extend2_ucopy)
    {
      _extend2 = new ArrayList(_extend2);
      _extend2_ucopy = null;
    }
    _extend2.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("extend2", old, getExtends2(), __arg);
    }
  }
  protected void cleanup(Collection scheduledForRemove)
  {
    // opposite role: extensionPoint this role: useCase
    if (_extensionPoint.size() != 0)
    {
      setExtensionPoints(Collections.EMPTY_LIST);
    }
    // opposite role: include2 this role: base
    if (_include2.size() != 0)
    {
      setIncludes2(Collections.EMPTY_LIST);
    }
    // opposite role: include this role: addition
    if (_include.size() != 0)
    {
      setIncludes(Collections.EMPTY_LIST);
    }
    // opposite role: extend this role: extension
    if (_extend.size() != 0)
    {
      setExtends(Collections.EMPTY_LIST);
    }
    // opposite role: extend2 this role: base
    if (_extend2.size() != 0)
    {
      setExtends2(Collections.EMPTY_LIST);
    }
    super.cleanup(scheduledForRemove);
  }

  public String getUMLClassName()
  {
    return "UseCase";
  }

  // Reflective API

  public Object reflectiveGetValue(String feature)
  {
    if ("extensionPoint".equals(feature))
    {
      return getExtensionPoints();
    }
    if ("include2".equals(feature))
    {
      return getIncludes2();
    }
    if ("include".equals(feature))
    {
      return getIncludes();
    }
    if ("extend".equals(feature))
    {
      return getExtends();
    }
    if ("extend2".equals(feature))
    {
      return getExtends2();
    }

    return super.reflectiveGetValue(feature);
  }

  public void reflectiveSetValue(String feature, Object obj)
  {
    if ("extensionPoint".equals(feature))
    {
      setExtensionPoints((Collection)obj);
      return;
    }
    if ("include2".equals(feature))
    {
      setIncludes2((Collection)obj);
      return;
    }
    if ("include".equals(feature))
    {
      setIncludes((Collection)obj);
      return;
    }
    if ("extend".equals(feature))
    {
      setExtends((Collection)obj);
      return;
    }
    if ("extend2".equals(feature))
    {
      setExtends2((Collection)obj);
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
    if ("include2".equals(feature))
    {
      addInclude2((MInclude)obj);
      return;
    }
    if ("include".equals(feature))
    {
      addInclude((MInclude)obj);
      return;
    }
    if ("extend".equals(feature))
    {
      addExtend((MExtend)obj);
      return;
    }
    if ("extend2".equals(feature))
    {
      addExtend2((MExtend)obj);
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
    if ("include2".equals(feature))
    {
      removeInclude2((MInclude)obj);
      return;
    }
    if ("include".equals(feature))
    {
      removeInclude((MInclude)obj);
      return;
    }
    if ("extend".equals(feature))
    {
      removeExtend((MExtend)obj);
      return;
    }
    if ("extend2".equals(feature))
    {
      removeExtend2((MExtend)obj);
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
