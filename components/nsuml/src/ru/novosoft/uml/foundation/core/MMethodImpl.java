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

public class MMethodImpl extends MBehavioralFeatureImpl implements MMethod
{
  // ------------ code for class Method -----------------
  // generating attributes
  // attribute: body
  private final static Method _body_setMethod = getMethod1(MMethodImpl.class, "setBody", MProcedureExpression.class);
  MProcedureExpression _body;
  public final MProcedureExpression getBody()
  {
    checkExists();
    return _body;
  }
  public final void setBody(MProcedureExpression __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      logAttrSet(_body_setMethod, _body, __arg);
      fireAttrSet("body", _body, __arg);
      _body = __arg;
    }
    finally
    {
      operationFinished();
    }
  }
  // generating associations
  // opposite role: specification this role: method
  private final static Method _specification_setMethod = getMethod1(MMethodImpl.class, "setSpecification", MOperation.class);
  MOperation _specification;
  public final MOperation getSpecification()
  {
    checkExists();
    return _specification;
  }
  public final void setSpecification(MOperation __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      MOperation __saved = _specification;
      if (_specification != __arg)
      {
        if (__saved != null)
        {
          __saved.internalUnrefByMethod(this);
        }
        if (__arg != null)
        {
          __arg.internalRefByMethod(this);
        }
        logRefSet(_specification_setMethod, __saved, __arg);
        fireRefSet("specification", __saved, __arg);
        _specification = __arg;
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefBySpecification(MOperation __arg)
  {
    MOperation __saved = _specification;
    if (_specification != null)
    {
      _specification.removeMethod(this);
    }
    fireRefSet("specification", __saved, __arg);
    _specification = __arg;
  }
  public final void internalUnrefBySpecification(MOperation __arg)
  {
    fireRefSet("specification", _specification, null);
    _specification = null;
  }
  protected void cleanup(Collection scheduledForRemove)
  {
    // opposite role: specification this role: method
    if (_specification != null )
    {
      setSpecification(null);
    }
    super.cleanup(scheduledForRemove);
  }

  public String getUMLClassName()
  {
    return "Method";
  }

  // Reflective API

  public Object reflectiveGetValue(String feature)
  {
    if ("body".equals(feature))
    {
      return getBody();
    }
    if ("specification".equals(feature))
    {
      return getSpecification();
    }

    return super.reflectiveGetValue(feature);
  }

  public void reflectiveSetValue(String feature, Object obj)
  {
    if ("body".equals(feature))
    {
      setBody((MProcedureExpression)obj);
      return;
    }
    if ("specification".equals(feature))
    {
      setSpecification((MOperation)obj);
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
