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

package ru.novosoft.uml;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;
import ru.novosoft.uml.behavior.common_behavior.*;
import ru.novosoft.uml.behavior.collaborations.*;
import ru.novosoft.uml.behavior.use_cases.*;
import ru.novosoft.uml.behavior.state_machines.*;
import ru.novosoft.uml.behavior.activity_graphs.*;
import ru.novosoft.uml.model_management.*;

import java.lang.reflect.Method;

public class MExtensionImpl extends MBaseImpl implements MExtension
{
  // ------------ code for class Extension -----------------
  // generating attributes
  // attribute: extenderID
  private final static Method _extenderID_setMethod = getMethod1(MExtensionImpl.class, "setExtenderID", String.class);
  String _extenderID;
  public final String getExtenderID()
  {
    checkExists();
    return _extenderID;
  }
  public final void setExtenderID(String __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      logAttrSet(_extenderID_setMethod, _extenderID, __arg);
      fireAttrSet("extenderID", _extenderID, __arg);
      _extenderID = __arg;
    }
    finally
    {
      operationFinished();
    }
  }
  // attribute: extender
  private final static Method _extender_setMethod = getMethod1(MExtensionImpl.class, "setExtender", String.class);
  String _extender;
  public final String getExtender()
  {
    checkExists();
    return _extender;
  }
  public final void setExtender(String __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      logAttrSet(_extender_setMethod, _extender, __arg);
      fireAttrSet("extender", _extender, __arg);
      _extender = __arg;
    }
    finally
    {
      operationFinished();
    }
  }
  // generating associations
  // opposite role: baseElement this role: extension
  private final static Method _baseElement_setMethod = getMethod1(MExtensionImpl.class, "setBaseElement", MBase.class);
  MBase _baseElement;
  public final MBase getBaseElement()
  {
    checkExists();
    return _baseElement;
  }
  public final void setBaseElement(MBase __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      MBase __saved = _baseElement;
      if (_baseElement != __arg)
      {
        if (__saved != null)
        {
          __saved.internalUnrefByExtension(this);
        }
        if (__arg != null)
        {
          __arg.internalRefByExtension(this);
        }
        logRefSet(_baseElement_setMethod, __saved, __arg);
        fireRefSet("baseElement", __saved, __arg);
        _baseElement = __arg;
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByBaseElement(MBase __arg)
  {
    MBase __saved = _baseElement;
    if (_baseElement != null)
    {
      _baseElement.removeExtension(this);
    }
    fireRefSet("baseElement", __saved, __arg);
    _baseElement = __arg;
  }
  public final void internalUnrefByBaseElement(MBase __arg)
  {
    fireRefSet("baseElement", _baseElement, null);
    _baseElement = null;
  }
  protected void cleanup(Collection scheduledForRemove)
  {
    // opposite role: baseElement this role: extension
    if (_baseElement != null )
    {
      setBaseElement(null);
    }
    super.cleanup(scheduledForRemove);
  }

  public String getUMLClassName()
  {
    return "Extension";
  }

  // Reflective API

  public Object reflectiveGetValue(String feature)
  {
    if ("extenderID".equals(feature))
    {
      return getExtenderID();
    }
    if ("extender".equals(feature))
    {
      return getExtender();
    }
    if ("baseElement".equals(feature))
    {
      return getBaseElement();
    }

    return super.reflectiveGetValue(feature);
  }

  public void reflectiveSetValue(String feature, Object obj)
  {
    if ("extenderID".equals(feature))
    {
      setExtenderID((String)obj);
      return;
    }
    if ("extender".equals(feature))
    {
      setExtender((String)obj);
      return;
    }
    if ("baseElement".equals(feature))
    {
      setBaseElement((MBase)obj);
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

  // Value support

  Object _value = null;
  public Object getValue()
  {
    return _value;
  }

  public void setValue(Object value)
  {
    if (null != _value)
    {
      throw new IllegalArgumentException("Value already assigned");
    }

    _value = value;
  }
}
