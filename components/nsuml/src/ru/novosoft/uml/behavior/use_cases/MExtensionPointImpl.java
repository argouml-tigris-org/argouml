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

public class MExtensionPointImpl extends MModelElementImpl implements MExtensionPoint
{
  // ------------ code for class ExtensionPoint -----------------
  // generating attributes
  // attribute: location
  private final static Method _location_setMethod = getMethod1(MExtensionPointImpl.class, "setLocation", String.class);
  String _location;
  public final String getLocation()
  {
    checkExists();
    return _location;
  }
  public final void setLocation(String __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      logAttrSet(_location_setMethod, _location, __arg);
      fireAttrSet("location", _location, __arg);
      _location = __arg;
    }
    finally
    {
      operationFinished();
    }
  }
  // generating associations
  // opposite role: extend this role: extensionPoint
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
      Collection __added = bagdiff(__arg,_extend);
      Collection __removed = bagdiff(_extend, __arg);
      Iterator iter1 = __removed.iterator();
      while (iter1.hasNext())
      {
        MExtend o = (MExtend)iter1.next();
        o.removeExtensionPoint(this);
      }
      Iterator iter2 = __added.iterator();
      while (iter2.hasNext())
      {
        MExtend o = (MExtend)iter2.next();
        o.addExtensionPoint(this);
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
      __arg.addExtensionPoint(this);
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
      __arg.removeExtensionPoint(this);
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
  // opposite role: useCase this role: extensionPoint
  private final static Method _useCase_setMethod = getMethod1(MExtensionPointImpl.class, "setUseCase", MUseCase.class);
  MUseCase _useCase;
  public final MUseCase getUseCase()
  {
    checkExists();
    return _useCase;
  }
  public final void setUseCase(MUseCase __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      MUseCase __saved = _useCase;
      if (_useCase != __arg)
      {
        if (__saved != null)
        {
          __saved.internalUnrefByExtensionPoint(this);
        }
        if (__arg != null)
        {
          __arg.internalRefByExtensionPoint(this);
        }
        logRefSet(_useCase_setMethod, __saved, __arg);
        fireRefSet("useCase", __saved, __arg);
        _useCase = __arg;
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByUseCase(MUseCase __arg)
  {
    MUseCase __saved = _useCase;
    if (_useCase != null)
    {
      _useCase.removeExtensionPoint(this);
    }
    fireRefSet("useCase", __saved, __arg);
    _useCase = __arg;
  }
  public final void internalUnrefByUseCase(MUseCase __arg)
  {
    fireRefSet("useCase", _useCase, null);
    _useCase = null;
  }
  protected void cleanup(Collection scheduledForRemove)
  {
    // opposite role: extend this role: extensionPoint
    if (_extend.size() != 0)
    {
      setExtends(Collections.EMPTY_LIST);
    }
    // opposite role: useCase this role: extensionPoint
    if (_useCase != null )
    {
      setUseCase(null);
    }
    super.cleanup(scheduledForRemove);
  }

  public String getUMLClassName()
  {
    return "ExtensionPoint";
  }

  // Reflective API

  public Object reflectiveGetValue(String feature)
  {
    if ("location".equals(feature))
    {
      return getLocation();
    }
    if ("extend".equals(feature))
    {
      return getExtends();
    }
    if ("useCase".equals(feature))
    {
      return getUseCase();
    }

    return super.reflectiveGetValue(feature);
  }

  public void reflectiveSetValue(String feature, Object obj)
  {
    if ("location".equals(feature))
    {
      setLocation((String)obj);
      return;
    }
    if ("extend".equals(feature))
    {
      setExtends((Collection)obj);
      return;
    }
    if ("useCase".equals(feature))
    {
      setUseCase((MUseCase)obj);
      return;
    }

    super.reflectiveSetValue(feature, obj);
  }

  public void reflectiveAddValue(String feature, Object obj)
  {
    if ("extend".equals(feature))
    {
      addExtend((MExtend)obj);
      return;
    }

    super.reflectiveAddValue(feature, obj);
  }

  public void reflectiveRemoveValue(String feature, Object obj)
  {
    if ("extend".equals(feature))
    {
      removeExtend((MExtend)obj);
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
