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

public class MComponentImpl extends MClassifierImpl implements MComponent
{
  // ------------ code for class Component -----------------
  // generating attributes
  // generating associations
  // opposite role: residentElement this role: implementationLocation
  private final static Method _residentElement_setMethod = getMethod1(MComponentImpl.class, "setResidentElements", Collection.class);
  private final static Method _residentElement_addMethod = getMethod1(MComponentImpl.class, "addResidentElement", MElementResidence.class);
  private final static Method _residentElement_removeMethod = getMethod1(MComponentImpl.class, "removeResidentElement", MElementResidence.class);
  Collection _residentElement = Collections.EMPTY_LIST;
  Collection _residentElement_ucopy = Collections.EMPTY_LIST;
  public final Collection getResidentElements()
  {
    checkExists();
    if (null == _residentElement_ucopy)
    {
      _residentElement_ucopy = ucopy(_residentElement);
    }
    return _residentElement_ucopy;
  }
  public final void setResidentElements(Collection __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      if (__arg == null)
      {
        throw new NullPointerException();
      }
      final boolean sendEvent = needEvent();
      final boolean logForUndo = needUndo();
      Collection old = null;
      if (sendEvent || logForUndo)
      {
        old = getResidentElements();
      }
      _residentElement_ucopy = null;
      Collection __added = bagdiff(__arg,_residentElement);
      Collection __removed = bagdiff(_residentElement, __arg);
      Iterator iter1 = __removed.iterator();
      while (iter1.hasNext())
      {
        MElementResidence o = (MElementResidence)iter1.next();
        o.internalUnrefByImplementationLocation(this);
      }
      Iterator iter2 = __added.iterator();
      while (iter2.hasNext())
      {
        MElementResidence o = (MElementResidence)iter2.next();
        o.internalRefByImplementationLocation(this);
      }
      _residentElement = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_residentElement_setMethod, old, getResidentElements());
      }
      if (sendEvent)
      {
        fireBagSet("residentElement", old, getResidentElements());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addResidentElement(MElementResidence __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      if (__arg == null)
      {
        throw new NullPointerException();
      }
      final boolean sendEvent = needEvent();
      Collection old = null;
      if (sendEvent)
      {
        old = getResidentElements();
      }
      if (null != _residentElement_ucopy)
      {
        _residentElement = new ArrayList(_residentElement);
        _residentElement_ucopy = null;
      }
      __arg.internalRefByImplementationLocation(this);
      _residentElement.add(__arg);
      logBagAdd(_residentElement_addMethod, _residentElement_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("residentElement", old, getResidentElements(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeResidentElement(MElementResidence __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      if (__arg == null)
      {
        throw new NullPointerException();
      }
      final boolean sendEvent = needEvent();
      Collection old = null;
      if (sendEvent)
      {
        old = getResidentElements();
      }
      if (null != _residentElement_ucopy)
      {
        _residentElement = new ArrayList(_residentElement);
        _residentElement_ucopy = null;
      }
      if (!_residentElement.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByImplementationLocation(this);
      logBagRemove(_residentElement_removeMethod, _residentElement_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("residentElement", old, getResidentElements(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByResidentElement(MElementResidence __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getResidentElements();
    }
    if (null != _residentElement_ucopy)
    {
      _residentElement = new ArrayList(_residentElement);
      _residentElement_ucopy = null;
    }
    _residentElement.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("residentElement", old, getResidentElements(), __arg);
    }
  }
  public final void internalUnrefByResidentElement(MElementResidence __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getResidentElements();
    }
    if (null != _residentElement_ucopy)
    {
      _residentElement = new ArrayList(_residentElement);
      _residentElement_ucopy = null;
    }
    _residentElement.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("residentElement", old, getResidentElements(), __arg);
    }
  }
  // opposite role: deploymentLocation this role: resident
  private final static Method _deploymentLocation_setMethod = getMethod1(MComponentImpl.class, "setDeploymentLocations", Collection.class);
  private final static Method _deploymentLocation_addMethod = getMethod1(MComponentImpl.class, "addDeploymentLocation", MNode.class);
  private final static Method _deploymentLocation_removeMethod = getMethod1(MComponentImpl.class, "removeDeploymentLocation", MNode.class);
  Collection _deploymentLocation = Collections.EMPTY_LIST;
  Collection _deploymentLocation_ucopy = Collections.EMPTY_LIST;
  public final Collection getDeploymentLocations()
  {
    checkExists();
    if (null == _deploymentLocation_ucopy)
    {
      _deploymentLocation_ucopy = ucopy(_deploymentLocation);
    }
    return _deploymentLocation_ucopy;
  }
  public final void setDeploymentLocations(Collection __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      if (__arg == null)
      {
        throw new NullPointerException();
      }
      final boolean sendEvent = needEvent();
      final boolean logForUndo = needUndo();
      Collection old = null;
      if (sendEvent || logForUndo)
      {
        old = getDeploymentLocations();
      }
      _deploymentLocation_ucopy = null;
      Collection __added = bagdiff(__arg,_deploymentLocation);
      Collection __removed = bagdiff(_deploymentLocation, __arg);
      Iterator iter3 = __removed.iterator();
      while (iter3.hasNext())
      {
        MNode o = (MNode)iter3.next();
        o.internalUnrefByResident(this);
      }
      Iterator iter4 = __added.iterator();
      while (iter4.hasNext())
      {
        MNode o = (MNode)iter4.next();
        o.internalRefByResident(this);
      }
      _deploymentLocation = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_deploymentLocation_setMethod, old, getDeploymentLocations());
      }
      if (sendEvent)
      {
        fireBagSet("deploymentLocation", old, getDeploymentLocations());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addDeploymentLocation(MNode __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      if (__arg == null)
      {
        throw new NullPointerException();
      }
      final boolean sendEvent = needEvent();
      Collection old = null;
      if (sendEvent)
      {
        old = getDeploymentLocations();
      }
      if (null != _deploymentLocation_ucopy)
      {
        _deploymentLocation = new ArrayList(_deploymentLocation);
        _deploymentLocation_ucopy = null;
      }
      __arg.internalRefByResident(this);
      _deploymentLocation.add(__arg);
      logBagAdd(_deploymentLocation_addMethod, _deploymentLocation_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("deploymentLocation", old, getDeploymentLocations(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeDeploymentLocation(MNode __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      if (__arg == null)
      {
        throw new NullPointerException();
      }
      final boolean sendEvent = needEvent();
      Collection old = null;
      if (sendEvent)
      {
        old = getDeploymentLocations();
      }
      if (null != _deploymentLocation_ucopy)
      {
        _deploymentLocation = new ArrayList(_deploymentLocation);
        _deploymentLocation_ucopy = null;
      }
      if (!_deploymentLocation.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByResident(this);
      logBagRemove(_deploymentLocation_removeMethod, _deploymentLocation_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("deploymentLocation", old, getDeploymentLocations(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByDeploymentLocation(MNode __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getDeploymentLocations();
    }
    if (null != _deploymentLocation_ucopy)
    {
      _deploymentLocation = new ArrayList(_deploymentLocation);
      _deploymentLocation_ucopy = null;
    }
    _deploymentLocation.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("deploymentLocation", old, getDeploymentLocations(), __arg);
    }
  }
  public final void internalUnrefByDeploymentLocation(MNode __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getDeploymentLocations();
    }
    if (null != _deploymentLocation_ucopy)
    {
      _deploymentLocation = new ArrayList(_deploymentLocation);
      _deploymentLocation_ucopy = null;
    }
    _deploymentLocation.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("deploymentLocation", old, getDeploymentLocations(), __arg);
    }
  }
  protected void cleanup(Collection scheduledForRemove)
  {
    // opposite role: residentElement this role: implementationLocation
    if (_residentElement.size() != 0)
    {
      setResidentElements(Collections.EMPTY_LIST);
    }
    // opposite role: deploymentLocation this role: resident
    if (_deploymentLocation.size() != 0)
    {
      setDeploymentLocations(Collections.EMPTY_LIST);
    }
    super.cleanup(scheduledForRemove);
  }

  public String getUMLClassName()
  {
    return "Component";
  }

  // Reflective API

  public Object reflectiveGetValue(String feature)
  {
    if ("residentElement".equals(feature))
    {
      return getResidentElements();
    }
    if ("deploymentLocation".equals(feature))
    {
      return getDeploymentLocations();
    }

    return super.reflectiveGetValue(feature);
  }

  public void reflectiveSetValue(String feature, Object obj)
  {
    if ("residentElement".equals(feature))
    {
      setResidentElements((Collection)obj);
      return;
    }
    if ("deploymentLocation".equals(feature))
    {
      setDeploymentLocations((Collection)obj);
      return;
    }

    super.reflectiveSetValue(feature, obj);
  }

  public void reflectiveAddValue(String feature, Object obj)
  {
    if ("residentElement".equals(feature))
    {
      addResidentElement((MElementResidence)obj);
      return;
    }
    if ("deploymentLocation".equals(feature))
    {
      addDeploymentLocation((MNode)obj);
      return;
    }

    super.reflectiveAddValue(feature, obj);
  }

  public void reflectiveRemoveValue(String feature, Object obj)
  {
    if ("residentElement".equals(feature))
    {
      removeResidentElement((MElementResidence)obj);
      return;
    }
    if ("deploymentLocation".equals(feature))
    {
      removeDeploymentLocation((MNode)obj);
      return;
    }

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
