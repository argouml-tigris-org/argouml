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

public class MSignalEventImpl extends MEventImpl implements MSignalEvent
{
  // ------------ code for class SignalEvent -----------------
  // generating attributes
  // generating associations
  // opposite role: signal this role: occurrence
  private final static Method _signal_setMethod = getMethod1(MSignalEventImpl.class, "setSignal", MSignal.class);
  MSignal _signal;
  public final MSignal getSignal()
  {
    checkExists();
    return _signal;
  }
  public final void setSignal(MSignal __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      MSignal __saved = _signal;
      if (_signal != __arg)
      {
        if (__saved != null)
        {
          __saved.internalUnrefByOccurrence(this);
        }
        if (__arg != null)
        {
          __arg.internalRefByOccurrence(this);
        }
        logRefSet(_signal_setMethod, __saved, __arg);
        fireRefSet("signal", __saved, __arg);
        _signal = __arg;
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefBySignal(MSignal __arg)
  {
    MSignal __saved = _signal;
    if (_signal != null)
    {
      _signal.removeOccurrence(this);
    }
    fireRefSet("signal", __saved, __arg);
    _signal = __arg;
  }
  public final void internalUnrefBySignal(MSignal __arg)
  {
    fireRefSet("signal", _signal, null);
    _signal = null;
  }
  protected void cleanup(Collection scheduledForRemove)
  {
    // opposite role: signal this role: occurrence
    if (_signal != null )
    {
      setSignal(null);
    }
    super.cleanup(scheduledForRemove);
  }

  public String getUMLClassName()
  {
    return "SignalEvent";
  }

  // Reflective API

  public Object reflectiveGetValue(String feature)
  {
    if ("signal".equals(feature))
    {
      return getSignal();
    }

    return super.reflectiveGetValue(feature);
  }

  public void reflectiveSetValue(String feature, Object obj)
  {
    if ("signal".equals(feature))
    {
      setSignal((MSignal)obj);
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
