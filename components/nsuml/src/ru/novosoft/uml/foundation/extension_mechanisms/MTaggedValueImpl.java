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

package ru.novosoft.uml.foundation.extension_mechanisms;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import ru.novosoft.uml.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.behavior.common_behavior.*;
import ru.novosoft.uml.behavior.collaborations.*;
import ru.novosoft.uml.behavior.use_cases.*;
import ru.novosoft.uml.behavior.state_machines.*;
import ru.novosoft.uml.behavior.activity_graphs.*;
import ru.novosoft.uml.model_management.*;

import java.lang.reflect.Method;

public class MTaggedValueImpl extends MElementImpl implements MTaggedValue
{
  // ------------ code for class TaggedValue -----------------
  // generating attributes
  // attribute: value
  private final static Method _value_setMethod = getMethod1(MTaggedValueImpl.class, "setValue", String.class);
  String _value;
  public final String getValue()
  {
    checkExists();
    return _value;
  }
  public final void setValue(String __arg)
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
  // attribute: tag
  private final static Method _tag_setMethod = getMethod1(MTaggedValueImpl.class, "setTag", String.class);
  String _tag;
  public final String getTag()
  {
    checkExists();
    return _tag;
  }
  public final void setTag(String __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      logAttrSet(_tag_setMethod, _tag, __arg);
      fireAttrSet("tag", _tag, __arg);
      _tag = __arg;
    }
    finally
    {
      operationFinished();
    }
  }
  // generating associations
  // opposite role: stereotype this role: requiredTag
  private final static Method _stereotype_setMethod = getMethod1(MTaggedValueImpl.class, "setStereotype", MStereotype.class);
  MStereotype _stereotype;
  public final MStereotype getStereotype()
  {
    checkExists();
    return _stereotype;
  }
  public final void setStereotype(MStereotype __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      MStereotype __saved = _stereotype;
      if (_stereotype != __arg)
      {
        if (__saved != null)
        {
          __saved.internalUnrefByRequiredTag(this);
        }
        if (__arg != null)
        {
          __arg.internalRefByRequiredTag(this);
        }
        logRefSet(_stereotype_setMethod, __saved, __arg);
        fireRefSet("stereotype", __saved, __arg);
        _stereotype = __arg;
        setModelElementContainer(_stereotype, "stereotype");
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByStereotype(MStereotype __arg)
  {
    MStereotype __saved = _stereotype;
    if (_stereotype != null)
    {
      _stereotype.removeRequiredTag(this);
    }
    fireRefSet("stereotype", __saved, __arg);
    _stereotype = __arg;
    setModelElementContainer(_stereotype, "stereotype");
  }
  public final void internalUnrefByStereotype(MStereotype __arg)
  {
    fireRefSet("stereotype", _stereotype, null);
    _stereotype = null;
    setModelElementContainer(null, null);
  }
  // opposite role: modelElement this role: taggedValue
  private final static Method _modelElement_setMethod = getMethod1(MTaggedValueImpl.class, "setModelElement", MModelElement.class);
  MModelElement _modelElement;
  public final MModelElement getModelElement()
  {
    checkExists();
    return _modelElement;
  }
  public final void setModelElement(MModelElement __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      MModelElement __saved = _modelElement;
      if (_modelElement != __arg)
      {
        if (__saved != null)
        {
          __saved.internalUnrefByTaggedValue(this);
        }
        if (__arg != null)
        {
          __arg.internalRefByTaggedValue(this);
        }
        logRefSet(_modelElement_setMethod, __saved, __arg);
        fireRefSet("modelElement", __saved, __arg);
        _modelElement = __arg;
        setModelElementContainer(_modelElement, "modelElement");
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByModelElement(MModelElement __arg)
  {
    MModelElement __saved = _modelElement;
    if (_modelElement != null)
    {
      _modelElement.removeTaggedValue(this);
    }
    fireRefSet("modelElement", __saved, __arg);
    _modelElement = __arg;
    setModelElementContainer(_modelElement, "modelElement");
  }
  public final void internalUnrefByModelElement(MModelElement __arg)
  {
    fireRefSet("modelElement", _modelElement, null);
    _modelElement = null;
    setModelElementContainer(null, null);
  }
  protected void cleanup(Collection scheduledForRemove)
  {
    // opposite role: stereotype this role: requiredTag
    if (_stereotype != null )
    {
      setStereotype(null);
    }
    // opposite role: modelElement this role: taggedValue
    if (_modelElement != null )
    {
      setModelElement(null);
    }
    super.cleanup(scheduledForRemove);
  }

  public String getUMLClassName()
  {
    return "TaggedValue";
  }

  // Reflective API

  public Object reflectiveGetValue(String feature)
  {
    if ("value".equals(feature))
    {
      return getValue();
    }
    if ("tag".equals(feature))
    {
      return getTag();
    }
    if ("stereotype".equals(feature))
    {
      return getStereotype();
    }
    if ("modelElement".equals(feature))
    {
      return getModelElement();
    }

    return super.reflectiveGetValue(feature);
  }

  public void reflectiveSetValue(String feature, Object obj)
  {
    if ("value".equals(feature))
    {
      setValue((String)obj);
      return;
    }
    if ("tag".equals(feature))
    {
      setTag((String)obj);
      return;
    }
    if ("stereotype".equals(feature))
    {
      setStereotype((MStereotype)obj);
      return;
    }
    if ("modelElement".equals(feature))
    {
      setModelElement((MModelElement)obj);
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
public String toString()
{
  return "TaggedValue: \'"+ getTag() + "\' = \'" + getValue()+"\'";
}

}
