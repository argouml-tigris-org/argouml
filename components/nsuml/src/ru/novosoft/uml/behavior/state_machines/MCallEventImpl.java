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

public class MCallEventImpl extends MEventImpl implements MCallEvent
{
  // ------------ code for class CallEvent -----------------
  // generating attributes
  // generating associations
  // opposite role: operation this role: occurrence
  private final static Method _operation_setMethod = getMethod1(MCallEventImpl.class, "setOperation", MOperation.class);
  MOperation _operation;
  public final MOperation getOperation()
  {
    checkExists();
    return _operation;
  }
  public final void setOperation(MOperation __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      MOperation __saved = _operation;
      if (_operation != __arg)
      {
        if (__saved != null)
        {
          __saved.internalUnrefByOccurrence(this);
        }
        if (__arg != null)
        {
          __arg.internalRefByOccurrence(this);
        }
        logRefSet(_operation_setMethod, __saved, __arg);
        fireRefSet("operation", __saved, __arg);
        _operation = __arg;
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByOperation(MOperation __arg)
  {
    MOperation __saved = _operation;
    if (_operation != null)
    {
      _operation.removeOccurrence(this);
    }
    fireRefSet("operation", __saved, __arg);
    _operation = __arg;
  }
  public final void internalUnrefByOperation(MOperation __arg)
  {
    fireRefSet("operation", _operation, null);
    _operation = null;
  }
  protected void cleanup(Collection scheduledForRemove)
  {
    // opposite role: operation this role: occurrence
    if (_operation != null )
    {
      setOperation(null);
    }
    super.cleanup(scheduledForRemove);
  }

  public String getUMLClassName()
  {
    return "CallEvent";
  }

  // Reflective API

  public Object reflectiveGetValue(String feature)
  {
    if ("operation".equals(feature))
    {
      return getOperation();
    }

    return super.reflectiveGetValue(feature);
  }

  public void reflectiveSetValue(String feature, Object obj)
  {
    if ("operation".equals(feature))
    {
      setOperation((MOperation)obj);
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
