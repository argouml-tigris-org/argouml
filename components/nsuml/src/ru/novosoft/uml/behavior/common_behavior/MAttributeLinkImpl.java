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

public class MAttributeLinkImpl extends MModelElementImpl implements MAttributeLink
{
  // ------------ code for class AttributeLink -----------------
  // generating attributes
  // generating associations
  // opposite role: linkEnd this role: qualifiedValue
  private final static Method _linkEnd_setMethod = getMethod1(MAttributeLinkImpl.class, "setLinkEnd", MLinkEnd.class);
  MLinkEnd _linkEnd;
  public final MLinkEnd getLinkEnd()
  {
    checkExists();
    return _linkEnd;
  }
  public final void setLinkEnd(MLinkEnd __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      MLinkEnd __saved = _linkEnd;
      if (_linkEnd != __arg)
      {
        if (__saved != null)
        {
          __saved.internalUnrefByQualifiedValue(this);
        }
        if (__arg != null)
        {
          __arg.internalRefByQualifiedValue(this);
        }
        logRefSet(_linkEnd_setMethod, __saved, __arg);
        fireRefSet("linkEnd", __saved, __arg);
        _linkEnd = __arg;
        setModelElementContainer(_linkEnd, "linkEnd");
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByLinkEnd(MLinkEnd __arg)
  {
    MLinkEnd __saved = _linkEnd;
    if (_linkEnd != null)
    {
      _linkEnd.removeQualifiedValue(this);
    }
    fireRefSet("linkEnd", __saved, __arg);
    _linkEnd = __arg;
    setModelElementContainer(_linkEnd, "linkEnd");
  }
  public final void internalUnrefByLinkEnd(MLinkEnd __arg)
  {
    fireRefSet("linkEnd", _linkEnd, null);
    _linkEnd = null;
    setModelElementContainer(null, null);
  }
  // opposite role: instance this role: slot
  private final static Method _instance_setMethod = getMethod1(MAttributeLinkImpl.class, "setInstance", MInstance.class);
  MInstance _instance;
  public final MInstance getInstance()
  {
    checkExists();
    return _instance;
  }
  public final void setInstance(MInstance __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      MInstance __saved = _instance;
      if (_instance != __arg)
      {
        if (__saved != null)
        {
          __saved.internalUnrefBySlot(this);
        }
        if (__arg != null)
        {
          __arg.internalRefBySlot(this);
        }
        logRefSet(_instance_setMethod, __saved, __arg);
        fireRefSet("instance", __saved, __arg);
        _instance = __arg;
        setModelElementContainer(_instance, "instance");
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByInstance(MInstance __arg)
  {
    MInstance __saved = _instance;
    if (_instance != null)
    {
      _instance.removeSlot(this);
    }
    fireRefSet("instance", __saved, __arg);
    _instance = __arg;
    setModelElementContainer(_instance, "instance");
  }
  public final void internalUnrefByInstance(MInstance __arg)
  {
    fireRefSet("instance", _instance, null);
    _instance = null;
    setModelElementContainer(null, null);
  }
  // opposite role: value this role: attributeLink
  private final static Method _value_setMethod = getMethod1(MAttributeLinkImpl.class, "setValue", MInstance.class);
  MInstance _value;
  public final MInstance getValue()
  {
    checkExists();
    return _value;
  }
  public final void setValue(MInstance __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      MInstance __saved = _value;
      if (_value != __arg)
      {
        if (__saved != null)
        {
          __saved.internalUnrefByAttributeLink(this);
        }
        if (__arg != null)
        {
          __arg.internalRefByAttributeLink(this);
        }
        logRefSet(_value_setMethod, __saved, __arg);
        fireRefSet("value", __saved, __arg);
        _value = __arg;
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByValue(MInstance __arg)
  {
    MInstance __saved = _value;
    if (_value != null)
    {
      _value.removeAttributeLink(this);
    }
    fireRefSet("value", __saved, __arg);
    _value = __arg;
  }
  public final void internalUnrefByValue(MInstance __arg)
  {
    fireRefSet("value", _value, null);
    _value = null;
  }
  // opposite role: attribute this role: attributeLink
  private final static Method _attribute_setMethod = getMethod1(MAttributeLinkImpl.class, "setAttribute", MAttribute.class);
  MAttribute _attribute;
  public final MAttribute getAttribute()
  {
    checkExists();
    return _attribute;
  }
  public final void setAttribute(MAttribute __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      MAttribute __saved = _attribute;
      if (_attribute != __arg)
      {
        if (__saved != null)
        {
          __saved.internalUnrefByAttributeLink(this);
        }
        if (__arg != null)
        {
          __arg.internalRefByAttributeLink(this);
        }
        logRefSet(_attribute_setMethod, __saved, __arg);
        fireRefSet("attribute", __saved, __arg);
        _attribute = __arg;
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByAttribute(MAttribute __arg)
  {
    MAttribute __saved = _attribute;
    if (_attribute != null)
    {
      _attribute.removeAttributeLink(this);
    }
    fireRefSet("attribute", __saved, __arg);
    _attribute = __arg;
  }
  public final void internalUnrefByAttribute(MAttribute __arg)
  {
    fireRefSet("attribute", _attribute, null);
    _attribute = null;
  }
  protected void cleanup(Collection scheduledForRemove)
  {
    // opposite role: linkEnd this role: qualifiedValue
    if (_linkEnd != null )
    {
      setLinkEnd(null);
    }
    // opposite role: instance this role: slot
    if (_instance != null )
    {
      setInstance(null);
    }
    // opposite role: value this role: attributeLink
    if (_value != null )
    {
      setValue(null);
    }
    // opposite role: attribute this role: attributeLink
    if (_attribute != null )
    {
      setAttribute(null);
    }
    super.cleanup(scheduledForRemove);
  }

  public String getUMLClassName()
  {
    return "AttributeLink";
  }

  // Reflective API

  public Object reflectiveGetValue(String feature)
  {
    if ("linkEnd".equals(feature))
    {
      return getLinkEnd();
    }
    if ("instance".equals(feature))
    {
      return getInstance();
    }
    if ("value".equals(feature))
    {
      return getValue();
    }
    if ("attribute".equals(feature))
    {
      return getAttribute();
    }

    return super.reflectiveGetValue(feature);
  }

  public void reflectiveSetValue(String feature, Object obj)
  {
    if ("linkEnd".equals(feature))
    {
      setLinkEnd((MLinkEnd)obj);
      return;
    }
    if ("instance".equals(feature))
    {
      setInstance((MInstance)obj);
      return;
    }
    if ("value".equals(feature))
    {
      setValue((MInstance)obj);
      return;
    }
    if ("attribute".equals(feature))
    {
      setAttribute((MAttribute)obj);
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
