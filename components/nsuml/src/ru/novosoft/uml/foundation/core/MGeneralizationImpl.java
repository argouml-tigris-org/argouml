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

public class MGeneralizationImpl extends MRelationshipImpl implements MGeneralization
{
  // ------------ code for class Generalization -----------------
  // generating attributes
  // attribute: discriminator
  private final static Method _discriminator_setMethod = getMethod1(MGeneralizationImpl.class, "setDiscriminator", String.class);
  String _discriminator;
  public final String getDiscriminator()
  {
    checkExists();
    return _discriminator;
  }
  public final void setDiscriminator(String __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      logAttrSet(_discriminator_setMethod, _discriminator, __arg);
      fireAttrSet("discriminator", _discriminator, __arg);
      _discriminator = __arg;
    }
    finally
    {
      operationFinished();
    }
  }
  // generating associations
  // opposite role: powertype this role: powertypeRange
  private final static Method _powertype_setMethod = getMethod1(MGeneralizationImpl.class, "setPowertype", MClassifier.class);
  MClassifier _powertype;
  public final MClassifier getPowertype()
  {
    checkExists();
    return _powertype;
  }
  public final void setPowertype(MClassifier __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      MClassifier __saved = _powertype;
      if (_powertype != __arg)
      {
        if (__saved != null)
        {
          __saved.internalUnrefByPowertypeRange(this);
        }
        if (__arg != null)
        {
          __arg.internalRefByPowertypeRange(this);
        }
        logRefSet(_powertype_setMethod, __saved, __arg);
        fireRefSet("powertype", __saved, __arg);
        _powertype = __arg;
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByPowertype(MClassifier __arg)
  {
    MClassifier __saved = _powertype;
    if (_powertype != null)
    {
      _powertype.removePowertypeRange(this);
    }
    fireRefSet("powertype", __saved, __arg);
    _powertype = __arg;
  }
  public final void internalUnrefByPowertype(MClassifier __arg)
  {
    fireRefSet("powertype", _powertype, null);
    _powertype = null;
  }
  // opposite role: parent this role: specialization
  private final static Method _parent_setMethod = getMethod1(MGeneralizationImpl.class, "setParent", MGeneralizableElement.class);
  MGeneralizableElement _parent;
  public final MGeneralizableElement getParent()
  {
    checkExists();
    return _parent;
  }
  public final void setParent(MGeneralizableElement __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      MGeneralizableElement __saved = _parent;
      if (_parent != __arg)
      {
        if (__saved != null)
        {
          __saved.internalUnrefBySpecialization(this);
        }
        if (__arg != null)
        {
          __arg.internalRefBySpecialization(this);
        }
        logRefSet(_parent_setMethod, __saved, __arg);
        fireRefSet("parent", __saved, __arg);
        _parent = __arg;
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByParent(MGeneralizableElement __arg)
  {
    MGeneralizableElement __saved = _parent;
    if (_parent != null)
    {
      _parent.removeSpecialization(this);
    }
    fireRefSet("parent", __saved, __arg);
    _parent = __arg;
  }
  public final void internalUnrefByParent(MGeneralizableElement __arg)
  {
    fireRefSet("parent", _parent, null);
    _parent = null;
  }
  // opposite role: child this role: generalization
  private final static Method _child_setMethod = getMethod1(MGeneralizationImpl.class, "setChild", MGeneralizableElement.class);
  MGeneralizableElement _child;
  public final MGeneralizableElement getChild()
  {
    checkExists();
    return _child;
  }
  public final void setChild(MGeneralizableElement __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      MGeneralizableElement __saved = _child;
      if (_child != __arg)
      {
        if (__saved != null)
        {
          __saved.internalUnrefByGeneralization(this);
        }
        if (__arg != null)
        {
          __arg.internalRefByGeneralization(this);
        }
        logRefSet(_child_setMethod, __saved, __arg);
        fireRefSet("child", __saved, __arg);
        _child = __arg;
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByChild(MGeneralizableElement __arg)
  {
    MGeneralizableElement __saved = _child;
    if (_child != null)
    {
      _child.removeGeneralization(this);
    }
    fireRefSet("child", __saved, __arg);
    _child = __arg;
  }
  public final void internalUnrefByChild(MGeneralizableElement __arg)
  {
    fireRefSet("child", _child, null);
    _child = null;
  }
  protected void cleanup(Collection scheduledForRemove)
  {
    // opposite role: powertype this role: powertypeRange
    if (_powertype != null )
    {
      setPowertype(null);
    }
    // opposite role: parent this role: specialization
    if (_parent != null )
    {
      setParent(null);
    }
    // opposite role: child this role: generalization
    if (_child != null )
    {
      setChild(null);
    }
    super.cleanup(scheduledForRemove);
  }

  public String getUMLClassName()
  {
    return "Generalization";
  }

  // Reflective API

  public Object reflectiveGetValue(String feature)
  {
    if ("discriminator".equals(feature))
    {
      return getDiscriminator();
    }
    if ("powertype".equals(feature))
    {
      return getPowertype();
    }
    if ("parent".equals(feature))
    {
      return getParent();
    }
    if ("child".equals(feature))
    {
      return getChild();
    }

    return super.reflectiveGetValue(feature);
  }

  public void reflectiveSetValue(String feature, Object obj)
  {
    if ("discriminator".equals(feature))
    {
      setDiscriminator((String)obj);
      return;
    }
    if ("powertype".equals(feature))
    {
      setPowertype((MClassifier)obj);
      return;
    }
    if ("parent".equals(feature))
    {
      setParent((MGeneralizableElement)obj);
      return;
    }
    if ("child".equals(feature))
    {
      setChild((MGeneralizableElement)obj);
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
