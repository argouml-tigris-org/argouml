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

public abstract class MStructuralFeatureImpl extends MFeatureImpl implements MStructuralFeature
{
  // ------------ code for class StructuralFeature -----------------
  // generating attributes
  // attribute: targetScope
  private final static Method _targetScope_setMethod = getMethod1(MStructuralFeatureImpl.class, "setTargetScope", MScopeKind.class);
  MScopeKind _targetScope;
  public final MScopeKind getTargetScope()
  {
    checkExists();
    return _targetScope;
  }
  public final void setTargetScope(MScopeKind __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      logAttrSet(_targetScope_setMethod, _targetScope, __arg);
      fireAttrSet("targetScope", _targetScope, __arg);
      _targetScope = __arg;
    }
    finally
    {
      operationFinished();
    }
  }
  // attribute: changeability
  private final static Method _changeability_setMethod = getMethod1(MStructuralFeatureImpl.class, "setChangeability", MChangeableKind.class);
  MChangeableKind _changeability;
  public final MChangeableKind getChangeability()
  {
    checkExists();
    return _changeability;
  }
  public final void setChangeability(MChangeableKind __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      logAttrSet(_changeability_setMethod, _changeability, __arg);
      fireAttrSet("changeability", _changeability, __arg);
      _changeability = __arg;
    }
    finally
    {
      operationFinished();
    }
  }
  // attribute: multiplicity
  private final static Method _multiplicity_setMethod = getMethod1(MStructuralFeatureImpl.class, "setMultiplicity", MMultiplicity.class);
  MMultiplicity _multiplicity;
  public final MMultiplicity getMultiplicity()
  {
    checkExists();
    return _multiplicity;
  }
  public final void setMultiplicity(MMultiplicity __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      logAttrSet(_multiplicity_setMethod, _multiplicity, __arg);
      fireAttrSet("multiplicity", _multiplicity, __arg);
      _multiplicity = __arg;
    }
    finally
    {
      operationFinished();
    }
  }
  // generating associations
  // opposite role: type this role: structuralFeature
  private final static Method _type_setMethod = getMethod1(MStructuralFeatureImpl.class, "setType", MClassifier.class);
  MClassifier _type;
  public final MClassifier getType()
  {
    checkExists();
    return _type;
  }
  public final void setType(MClassifier __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      MClassifier __saved = _type;
      if (_type != __arg)
      {
        if (__saved != null)
        {
          __saved.internalUnrefByStructuralFeature(this);
        }
        if (__arg != null)
        {
          __arg.internalRefByStructuralFeature(this);
        }
        logRefSet(_type_setMethod, __saved, __arg);
        fireRefSet("type", __saved, __arg);
        _type = __arg;
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByType(MClassifier __arg)
  {
    MClassifier __saved = _type;
    if (_type != null)
    {
      _type.removeStructuralFeature(this);
    }
    fireRefSet("type", __saved, __arg);
    _type = __arg;
  }
  public final void internalUnrefByType(MClassifier __arg)
  {
    fireRefSet("type", _type, null);
    _type = null;
  }
  protected void cleanup(Collection scheduledForRemove)
  {
    // opposite role: type this role: structuralFeature
    if (_type != null )
    {
      setType(null);
    }
    super.cleanup(scheduledForRemove);
  }

  public String getUMLClassName()
  {
    return "StructuralFeature";
  }

  // Reflective API

  public Object reflectiveGetValue(String feature)
  {
    if ("targetScope".equals(feature))
    {
      return getTargetScope();
    }
    if ("changeability".equals(feature))
    {
      return getChangeability();
    }
    if ("multiplicity".equals(feature))
    {
      return getMultiplicity();
    }
    if ("type".equals(feature))
    {
      return getType();
    }

    return super.reflectiveGetValue(feature);
  }

  public void reflectiveSetValue(String feature, Object obj)
  {
    if ("targetScope".equals(feature))
    {
      setTargetScope((MScopeKind)obj);
      return;
    }
    if ("changeability".equals(feature))
    {
      setChangeability((MChangeableKind)obj);
      return;
    }
    if ("multiplicity".equals(feature))
    {
      setMultiplicity((MMultiplicity)obj);
      return;
    }
    if ("type".equals(feature))
    {
      setType((MClassifier)obj);
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
