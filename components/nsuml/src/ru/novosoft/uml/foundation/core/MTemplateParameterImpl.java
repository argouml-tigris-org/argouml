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

public class MTemplateParameterImpl extends MBaseImpl implements MTemplateParameter
{
  // ------------ code for class TemplateParameter -----------------
  // generating attributes
  // generating associations
  // opposite role: modelElement2 this role: templateParameter2
  private final static Method _modelElement2_setMethod = getMethod1(MTemplateParameterImpl.class, "setModelElement2", MModelElement.class);
  MModelElement _modelElement2;
  public final MModelElement getModelElement2()
  {
    checkExists();
    return _modelElement2;
  }
  public final void setModelElement2(MModelElement __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      MModelElement __saved = _modelElement2;
      if (_modelElement2 != __arg)
      {
        if (__saved != null)
        {
          __saved.internalUnrefByTemplateParameter2(this);
        }
        if (__arg != null)
        {
          __arg.internalRefByTemplateParameter2(this);
        }
        logRefSet(_modelElement2_setMethod, __saved, __arg);
        fireRefSet("modelElement2", __saved, __arg);
        _modelElement2 = __arg;
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByModelElement2(MModelElement __arg)
  {
    MModelElement __saved = _modelElement2;
    if (_modelElement2 != null)
    {
      _modelElement2.removeTemplateParameter2(this);
    }
    fireRefSet("modelElement2", __saved, __arg);
    _modelElement2 = __arg;
  }
  public final void internalUnrefByModelElement2(MModelElement __arg)
  {
    fireRefSet("modelElement2", _modelElement2, null);
    _modelElement2 = null;
  }
  // opposite role: defaultElement this role: templateParameter3
  private final static Method _defaultElement_setMethod = getMethod1(MTemplateParameterImpl.class, "setDefaultElement", MModelElement.class);
  MModelElement _defaultElement;
  public final MModelElement getDefaultElement()
  {
    checkExists();
    return _defaultElement;
  }
  public final void setDefaultElement(MModelElement __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      MModelElement __saved = _defaultElement;
      if (_defaultElement != __arg)
      {
        if (__saved != null)
        {
          __saved.internalUnrefByTemplateParameter3(this);
        }
        if (__arg != null)
        {
          __arg.internalRefByTemplateParameter3(this);
        }
        logRefSet(_defaultElement_setMethod, __saved, __arg);
        fireRefSet("defaultElement", __saved, __arg);
        _defaultElement = __arg;
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByDefaultElement(MModelElement __arg)
  {
    MModelElement __saved = _defaultElement;
    if (_defaultElement != null)
    {
      _defaultElement.removeTemplateParameter3(this);
    }
    fireRefSet("defaultElement", __saved, __arg);
    _defaultElement = __arg;
  }
  public final void internalUnrefByDefaultElement(MModelElement __arg)
  {
    fireRefSet("defaultElement", _defaultElement, null);
    _defaultElement = null;
  }
  // opposite role: modelElement this role: templateParameter
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
      if (_modelElement != __arg)
      {
        if (_modelElement != null)
        {
          _modelElement.removeTemplateParameter(this);
        }
        if (__arg != null)
        {
          __arg.addTemplateParameter(this);
        }
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
      _modelElement.removeTemplateParameter(this);
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
    // opposite role: modelElement2 this role: templateParameter2
    if (_modelElement2 != null )
    {
      setModelElement2(null);
    }
    // opposite role: defaultElement this role: templateParameter3
    if (_defaultElement != null )
    {
      setDefaultElement(null);
    }
    // opposite role: modelElement this role: templateParameter
    if (_modelElement != null )
    {
      setModelElement(null);
    }
    super.cleanup(scheduledForRemove);
  }

  public String getUMLClassName()
  {
    return "TemplateParameter";
  }

  // Reflective API

  public Object reflectiveGetValue(String feature)
  {
    if ("modelElement2".equals(feature))
    {
      return getModelElement2();
    }
    if ("defaultElement".equals(feature))
    {
      return getDefaultElement();
    }
    if ("modelElement".equals(feature))
    {
      return getModelElement();
    }

    return super.reflectiveGetValue(feature);
  }

  public void reflectiveSetValue(String feature, Object obj)
  {
    if ("modelElement2".equals(feature))
    {
      setModelElement2((MModelElement)obj);
      return;
    }
    if ("defaultElement".equals(feature))
    {
      setDefaultElement((MModelElement)obj);
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
}
