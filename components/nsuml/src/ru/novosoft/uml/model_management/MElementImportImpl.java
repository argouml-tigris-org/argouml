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

package ru.novosoft.uml.model_management;

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
import ru.novosoft.uml.behavior.activity_graphs.*;

import java.lang.reflect.Method;

public class MElementImportImpl extends MBaseImpl implements MElementImport
{
  // ------------ code for class ElementImport -----------------
  // generating attributes
  // attribute: alias
  private final static Method _alias_setMethod = getMethod1(MElementImportImpl.class, "setAlias", String.class);
  String _alias;
  public final String getAlias()
  {
    checkExists();
    return _alias;
  }
  public final void setAlias(String __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      logAttrSet(_alias_setMethod, _alias, __arg);
      fireAttrSet("alias", _alias, __arg);
      _alias = __arg;
    }
    finally
    {
      operationFinished();
    }
  }
  // attribute: visibility
  private final static Method _visibility_setMethod = getMethod1(MElementImportImpl.class, "setVisibility", MVisibilityKind.class);
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
  // opposite role: modelElement this role: elementImport2
  private final static Method _modelElement_setMethod = getMethod1(MElementImportImpl.class, "setModelElement", MModelElement.class);
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
          __saved.internalUnrefByElementImport2(this);
        }
        if (__arg != null)
        {
          __arg.internalRefByElementImport2(this);
        }
        logRefSet(_modelElement_setMethod, __saved, __arg);
        fireRefSet("modelElement", __saved, __arg);
        _modelElement = __arg;
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
      _modelElement.removeElementImport2(this);
    }
    fireRefSet("modelElement", __saved, __arg);
    _modelElement = __arg;
  }
  public final void internalUnrefByModelElement(MModelElement __arg)
  {
    fireRefSet("modelElement", _modelElement, null);
    _modelElement = null;
  }
  // opposite role: package this role: elementImport
  private final static Method _package_setMethod = getMethod1(MElementImportImpl.class, "setPackage", MPackage.class);
  MPackage _package;
  public final MPackage getPackage()
  {
    checkExists();
    return _package;
  }
  public final void setPackage(MPackage __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      MPackage __saved = _package;
      if (_package != __arg)
      {
        if (__saved != null)
        {
          __saved.internalUnrefByElementImport(this);
        }
        if (__arg != null)
        {
          __arg.internalRefByElementImport(this);
        }
        logRefSet(_package_setMethod, __saved, __arg);
        fireRefSet("package", __saved, __arg);
        _package = __arg;
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByPackage(MPackage __arg)
  {
    MPackage __saved = _package;
    if (_package != null)
    {
      _package.removeElementImport(this);
    }
    fireRefSet("package", __saved, __arg);
    _package = __arg;
  }
  public final void internalUnrefByPackage(MPackage __arg)
  {
    fireRefSet("package", _package, null);
    _package = null;
  }
  protected void cleanup(Collection scheduledForRemove)
  {
    // opposite role: modelElement this role: elementImport2
    if (_modelElement != null )
    {
      setModelElement(null);
    }
    // opposite role: package this role: elementImport
    if (_package != null )
    {
      setPackage(null);
    }
    super.cleanup(scheduledForRemove);
  }

  public String getUMLClassName()
  {
    return "ElementImport";
  }

  // Reflective API

  public Object reflectiveGetValue(String feature)
  {
    if ("alias".equals(feature))
    {
      return getAlias();
    }
    if ("visibility".equals(feature))
    {
      return getVisibility();
    }
    if ("modelElement".equals(feature))
    {
      return getModelElement();
    }
    if ("package".equals(feature))
    {
      return getPackage();
    }

    return super.reflectiveGetValue(feature);
  }

  public void reflectiveSetValue(String feature, Object obj)
  {
    if ("alias".equals(feature))
    {
      setAlias((String)obj);
      return;
    }
    if ("visibility".equals(feature))
    {
      setVisibility((MVisibilityKind)obj);
      return;
    }
    if ("modelElement".equals(feature))
    {
      setModelElement((MModelElement)obj);
      return;
    }
    if ("package".equals(feature))
    {
      setPackage((MPackage)obj);
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
