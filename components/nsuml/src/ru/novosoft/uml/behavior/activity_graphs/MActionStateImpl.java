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

public class MActionStateImpl extends MSimpleStateImpl implements MActionState
{
  // ------------ code for class ActionState -----------------
  // generating attributes
  // attribute: dynamicMultiplicity
  private final static Method _dynamicMultiplicity_setMethod = getMethod1(MActionStateImpl.class, "setDynamicMultiplicity", MMultiplicity.class);
  MMultiplicity _dynamicMultiplicity;
  public final MMultiplicity getDynamicMultiplicity()
  {
    checkExists();
    return _dynamicMultiplicity;
  }
  public final void setDynamicMultiplicity(MMultiplicity __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      logAttrSet(_dynamicMultiplicity_setMethod, _dynamicMultiplicity, __arg);
      fireAttrSet("dynamicMultiplicity", _dynamicMultiplicity, __arg);
      _dynamicMultiplicity = __arg;
    }
    finally
    {
      operationFinished();
    }
  }
  // attribute: dynamicArguments
  private final static Method _dynamicArguments_setMethod = getMethod1(MActionStateImpl.class, "setDynamicArguments", MArgListsExpression.class);
  MArgListsExpression _dynamicArguments;
  public final MArgListsExpression getDynamicArguments()
  {
    checkExists();
    return _dynamicArguments;
  }
  public final void setDynamicArguments(MArgListsExpression __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      logAttrSet(_dynamicArguments_setMethod, _dynamicArguments, __arg);
      fireAttrSet("dynamicArguments", _dynamicArguments, __arg);
      _dynamicArguments = __arg;
    }
    finally
    {
      operationFinished();
    }
  }
  // attribute: isDynamic
  private final static Method _isDynamic_setMethod = getMethod1(MActionStateImpl.class, "setDynamic", boolean.class);
  boolean _isDynamic;
  public final boolean isDynamic()
  {
    checkExists();
    return _isDynamic;
  }
  public final void setDynamic(boolean __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      logAttrSet(_isDynamic_setMethod, _isDynamic, __arg);
      fireAttrSet("isDynamic", _isDynamic, __arg);
      _isDynamic = __arg;
    }
    finally
    {
      operationFinished();
    }
  }
  // generating associations
  protected void cleanup(Collection scheduledForRemove)
  {
    super.cleanup(scheduledForRemove);
  }

  public String getUMLClassName()
  {
    return "ActionState";
  }

  // Reflective API

  public Object reflectiveGetValue(String feature)
  {
    if ("dynamicMultiplicity".equals(feature))
    {
      return getDynamicMultiplicity();
    }
    if ("dynamicArguments".equals(feature))
    {
      return getDynamicArguments();
    }
    if ("isDynamic".equals(feature))
    {
      return new Boolean(isDynamic());
    }

    return super.reflectiveGetValue(feature);
  }

  public void reflectiveSetValue(String feature, Object obj)
  {
    if ("dynamicMultiplicity".equals(feature))
    {
      setDynamicMultiplicity((MMultiplicity)obj);
      return;
    }
    if ("dynamicArguments".equals(feature))
    {
      setDynamicArguments((MArgListsExpression)obj);
      return;
    }
    if ("isDynamic".equals(feature))
    {
      setDynamic(((Boolean)obj).booleanValue());
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
