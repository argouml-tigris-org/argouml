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

public class MIncludeImpl extends MRelationshipImpl implements MInclude
{
  // ------------ code for class Include -----------------
  // generating attributes
  // generating associations
  // opposite role: base this role: include2
  private final static Method _base_setMethod = getMethod1(MIncludeImpl.class, "setBase", MUseCase.class);
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
          __saved.internalUnrefByInclude2(this);
        }
        if (__arg != null)
        {
          __arg.internalRefByInclude2(this);
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
      _base.removeInclude2(this);
    }
    fireRefSet("base", __saved, __arg);
    _base = __arg;
  }
  public final void internalUnrefByBase(MUseCase __arg)
  {
    fireRefSet("base", _base, null);
    _base = null;
  }
  // opposite role: addition this role: include
  private final static Method _addition_setMethod = getMethod1(MIncludeImpl.class, "setAddition", MUseCase.class);
  MUseCase _addition;
  public final MUseCase getAddition()
  {
    checkExists();
    return _addition;
  }
  public final void setAddition(MUseCase __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      MUseCase __saved = _addition;
      if (_addition != __arg)
      {
        if (__saved != null)
        {
          __saved.internalUnrefByInclude(this);
        }
        if (__arg != null)
        {
          __arg.internalRefByInclude(this);
        }
        logRefSet(_addition_setMethod, __saved, __arg);
        fireRefSet("addition", __saved, __arg);
        _addition = __arg;
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByAddition(MUseCase __arg)
  {
    MUseCase __saved = _addition;
    if (_addition != null)
    {
      _addition.removeInclude(this);
    }
    fireRefSet("addition", __saved, __arg);
    _addition = __arg;
  }
  public final void internalUnrefByAddition(MUseCase __arg)
  {
    fireRefSet("addition", _addition, null);
    _addition = null;
  }
  protected void cleanup(Collection scheduledForRemove)
  {
    // opposite role: base this role: include2
    if (_base != null )
    {
      setBase(null);
    }
    // opposite role: addition this role: include
    if (_addition != null )
    {
      setAddition(null);
    }
    super.cleanup(scheduledForRemove);
  }

  public String getUMLClassName()
  {
    return "Include";
  }

  // Reflective API

  public Object reflectiveGetValue(String feature)
  {
    if ("base".equals(feature))
    {
      return getBase();
    }
    if ("addition".equals(feature))
    {
      return getAddition();
    }

    return super.reflectiveGetValue(feature);
  }

  public void reflectiveSetValue(String feature, Object obj)
  {
    if ("base".equals(feature))
    {
      setBase((MUseCase)obj);
      return;
    }
    if ("addition".equals(feature))
    {
      setAddition((MUseCase)obj);
      return;
    }

    super.reflectiveSetValue(feature, obj);
  }

  public void reflectiveAddValue(String feature, Object obj)
  {

    super.reflectiveAddValue(feature, obj);
  }

  public void reflectiveRemoveValue(String feature, Object obj)
  {

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
