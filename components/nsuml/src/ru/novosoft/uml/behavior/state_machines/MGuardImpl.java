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

public class MGuardImpl extends MModelElementImpl implements MGuard
{
  // ------------ code for class Guard -----------------
  // generating attributes
  // attribute: expression
  private final static Method _expression_setMethod = getMethod1(MGuardImpl.class, "setExpression", MBooleanExpression.class);
  MBooleanExpression _expression;
  public final MBooleanExpression getExpression()
  {
    checkExists();
    return _expression;
  }
  public final void setExpression(MBooleanExpression __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      logAttrSet(_expression_setMethod, _expression, __arg);
      fireAttrSet("expression", _expression, __arg);
      _expression = __arg;
    }
    finally
    {
      operationFinished();
    }
  }
  // generating associations
  // opposite role: transition this role: guard
  private final static Method _transition_setMethod = getMethod1(MGuardImpl.class, "setTransition", MTransition.class);
  MTransition _transition;
  public final MTransition getTransition()
  {
    checkExists();
    return _transition;
  }
  public final void setTransition(MTransition __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      MTransition __saved = _transition;
      if (_transition != __arg)
      {
        if (__saved != null)
        {
          __saved.internalUnrefByGuard(this);
        }
        if (__arg != null)
        {
          __arg.internalRefByGuard(this);
        }
        logRefSet(_transition_setMethod, __saved, __arg);
        fireRefSet("transition", __saved, __arg);
        _transition = __arg;
        setModelElementContainer(_transition, "transition");
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByTransition(MTransition __arg)
  {
    MTransition __saved = _transition;
    if (__saved != null)
    {
      __saved.setGuard(null);
    }
    fireRefSet("transition", __saved, __arg);
    _transition = __arg;
    setModelElementContainer(_transition, "transition");
  }
  public final void internalUnrefByTransition(MTransition __arg)
  {
    fireRefSet("transition", _transition, null);
    _transition = null;
    setModelElementContainer(null, null);
  }
  protected void cleanup(Collection scheduledForRemove)
  {
    // opposite role: transition this role: guard
    if (_transition != null )
    {
      setTransition(null);
    }
    super.cleanup(scheduledForRemove);
  }

  public String getUMLClassName()
  {
    return "Guard";
  }

  // Reflective API

  public Object reflectiveGetValue(String feature)
  {
    if ("expression".equals(feature))
    {
      return getExpression();
    }
    if ("transition".equals(feature))
    {
      return getTransition();
    }

    return super.reflectiveGetValue(feature);
  }

  public void reflectiveSetValue(String feature, Object obj)
  {
    if ("expression".equals(feature))
    {
      setExpression((MBooleanExpression)obj);
      return;
    }
    if ("transition".equals(feature))
    {
      setTransition((MTransition)obj);
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
