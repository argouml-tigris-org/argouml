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

public class MElementResidenceImpl extends MBaseImpl implements MElementResidence
{
  // ------------ code for class ElementResidence -----------------
  // generating attributes
  // attribute: visibility
  private final static Method _visibility_setMethod = getMethod1(MElementResidenceImpl.class, "setVisibility", MVisibilityKind.class);
  MVisibilityKind _visibility;
  public final MVisibilityKind getVisibility()
  {
    checkExists();
    return _visibility;
  }
  public final void setVisibility(MVisibilityKind __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      logAttrSet(_visibility_setMethod, _visibility, __arg);
      fireAttrSet("visibility", _visibility, __arg);
      _visibility = __arg;
    }
    finally
    {
      operationFinished();
    }
  }
  // generating associations
  // opposite role: resident this role: elementResidence
  private final static Method _resident_setMethod = getMethod1(MElementResidenceImpl.class, "setResident", MModelElement.class);
  MModelElement _resident;
  public final MModelElement getResident()
  {
    checkExists();
    return _resident;
  }
  public final void setResident(MModelElement __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      MModelElement __saved = _resident;
      if (_resident != __arg)
      {
        if (__saved != null)
        {
          __saved.internalUnrefByElementResidence(this);
        }
        if (__arg != null)
        {
          __arg.internalRefByElementResidence(this);
        }
        logRefSet(_resident_setMethod, __saved, __arg);
        fireRefSet("resident", __saved, __arg);
        _resident = __arg;
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByResident(MModelElement __arg)
  {
    MModelElement __saved = _resident;
    if (_resident != null)
    {
      _resident.removeElementResidence(this);
    }
    fireRefSet("resident", __saved, __arg);
    _resident = __arg;
  }
  public final void internalUnrefByResident(MModelElement __arg)
  {
    fireRefSet("resident", _resident, null);
    _resident = null;
  }
  // opposite role: implementationLocation this role: residentElement
  private final static Method _implementationLocation_setMethod = getMethod1(MElementResidenceImpl.class, "setImplementationLocation", MComponent.class);
  MComponent _implementationLocation;
  public final MComponent getImplementationLocation()
  {
    checkExists();
    return _implementationLocation;
  }
  public final void setImplementationLocation(MComponent __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      MComponent __saved = _implementationLocation;
      if (_implementationLocation != __arg)
      {
        if (__saved != null)
        {
          __saved.internalUnrefByResidentElement(this);
        }
        if (__arg != null)
        {
          __arg.internalRefByResidentElement(this);
        }
        logRefSet(_implementationLocation_setMethod, __saved, __arg);
        fireRefSet("implementationLocation", __saved, __arg);
        _implementationLocation = __arg;
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByImplementationLocation(MComponent __arg)
  {
    MComponent __saved = _implementationLocation;
    if (_implementationLocation != null)
    {
      _implementationLocation.removeResidentElement(this);
    }
    fireRefSet("implementationLocation", __saved, __arg);
    _implementationLocation = __arg;
  }
  public final void internalUnrefByImplementationLocation(MComponent __arg)
  {
    fireRefSet("implementationLocation", _implementationLocation, null);
    _implementationLocation = null;
  }
  protected void cleanup(Collection scheduledForRemove)
  {
    // opposite role: resident this role: elementResidence
    if (_resident != null )
    {
      setResident(null);
    }
    // opposite role: implementationLocation this role: residentElement
    if (_implementationLocation != null )
    {
      setImplementationLocation(null);
    }
    super.cleanup(scheduledForRemove);
  }

  public String getUMLClassName()
  {
    return "ElementResidence";
  }

  // Reflective API

  public Object reflectiveGetValue(String feature)
  {
    if ("visibility".equals(feature))
    {
      return getVisibility();
    }
    if ("resident".equals(feature))
    {
      return getResident();
    }
    if ("implementationLocation".equals(feature))
    {
      return getImplementationLocation();
    }

    return super.reflectiveGetValue(feature);
  }

  public void reflectiveSetValue(String feature, Object obj)
  {
    if ("visibility".equals(feature))
    {
      setVisibility((MVisibilityKind)obj);
      return;
    }
    if ("resident".equals(feature))
    {
      setResident((MModelElement)obj);
      return;
    }
    if ("implementationLocation".equals(feature))
    {
      setImplementationLocation((MComponent)obj);
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
