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

public class MArgumentImpl extends MModelElementImpl implements MArgument
{
  // ------------ code for class Argument -----------------
  // generating attributes
  // attribute: value
  private final static Method _value_setMethod = getMethod1(MArgumentImpl.class, "setValue", MExpression.class);
  MExpression _value;
  public final MExpression getValue()
  {
    checkExists();
    return _value;
  }
  public final void setValue(MExpression __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      logAttrSet(_value_setMethod, _value, __arg);
      fireAttrSet("value", _value, __arg);
      _value = __arg;
    }
    finally
    {
      operationFinished();
    }
  }
  // generating associations
  // opposite role: action this role: actualArgument
  MAction _action;
  public final MAction getAction()
  {
    checkExists();
    return _action;
  }
  public final void setAction(MAction __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      if (_action != __arg)
      {
        if (_action != null)
        {
          _action.removeActualArgument(this);
        }
        if (__arg != null)
        {
          __arg.addActualArgument(this);
        }
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByAction(MAction __arg)
  {
    MAction __saved = _action;
    if (_action != null)
    {
      _action.removeActualArgument(this);
    }
    fireRefSet("action", __saved, __arg);
    _action = __arg;
    setModelElementContainer(_action, "action");
  }
  public final void internalUnrefByAction(MAction __arg)
  {
    fireRefSet("action", _action, null);
    _action = null;
    setModelElementContainer(null, null);
  }
  protected void cleanup(Collection scheduledForRemove)
  {
    // opposite role: action this role: actualArgument
    if (_action != null )
    {
      setAction(null);
    }
    super.cleanup(scheduledForRemove);
  }

  public String getUMLClassName()
  {
    return "Argument";
  }

  // Reflective API

  public Object reflectiveGetValue(String feature)
  {
    if ("value".equals(feature))
    {
      return getValue();
    }
    if ("action".equals(feature))
    {
      return getAction();
    }

    return super.reflectiveGetValue(feature);
  }

  public void reflectiveSetValue(String feature, Object obj)
  {
    if ("value".equals(feature))
    {
      setValue((MExpression)obj);
      return;
    }
    if ("action".equals(feature))
    {
      setAction((MAction)obj);
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
