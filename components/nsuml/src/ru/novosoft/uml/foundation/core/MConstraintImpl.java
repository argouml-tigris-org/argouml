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

public class MConstraintImpl extends MModelElementImpl implements MConstraint
{
  // ------------ code for class Constraint -----------------
  // generating attributes
  // attribute: body
  private final static Method _body_setMethod = getMethod1(MConstraintImpl.class, "setBody", MBooleanExpression.class);
  MBooleanExpression _body;
  public final MBooleanExpression getBody()
  {
    checkExists();
    return _body;
  }
  public final void setBody(MBooleanExpression __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      logAttrSet(_body_setMethod, _body, __arg);
      fireAttrSet("body", _body, __arg);
      _body = __arg;
    }
    finally
    {
      operationFinished();
    }
  }
  // generating associations
  // opposite role: constrainedElement2 this role: stereotypeConstraint
  private final static Method _constrainedElement2_setMethod = getMethod1(MConstraintImpl.class, "setConstrainedElement2", MStereotype.class);
  MStereotype _constrainedElement2;
  public final MStereotype getConstrainedElement2()
  {
    checkExists();
    return _constrainedElement2;
  }
  public final void setConstrainedElement2(MStereotype __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      MStereotype __saved = _constrainedElement2;
      if (_constrainedElement2 != __arg)
      {
        if (__saved != null)
        {
          __saved.internalUnrefByStereotypeConstraint(this);
        }
        if (__arg != null)
        {
          __arg.internalRefByStereotypeConstraint(this);
        }
        logRefSet(_constrainedElement2_setMethod, __saved, __arg);
        fireRefSet("constrainedElement2", __saved, __arg);
        _constrainedElement2 = __arg;
        setModelElementContainer(_constrainedElement2, "constrainedElement2");
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByConstrainedElement2(MStereotype __arg)
  {
    MStereotype __saved = _constrainedElement2;
    if (_constrainedElement2 != null)
    {
      _constrainedElement2.removeStereotypeConstraint(this);
    }
    fireRefSet("constrainedElement2", __saved, __arg);
    _constrainedElement2 = __arg;
    setModelElementContainer(_constrainedElement2, "constrainedElement2");
  }
  public final void internalUnrefByConstrainedElement2(MStereotype __arg)
  {
    fireRefSet("constrainedElement2", _constrainedElement2, null);
    _constrainedElement2 = null;
    setModelElementContainer(null, null);
  }
  // opposite role: constrainedElement this role: constraint
  private final static Method _constrainedElement_setMethod = getMethod1(MConstraintImpl.class, "setConstrainedElements", List.class);
  private final static Method _constrainedElement_removeMethod = getMethod1(MConstraintImpl.class, "removeConstrainedElement", int.class);
  private final static Method _constrainedElement_addMethod = getMethod2(MConstraintImpl.class, "addConstrainedElement", int.class, MModelElement.class);
  private final static Method _constrainedElement_listSetMethod = getMethod2(MConstraintImpl.class, "setConstrainedElement", int.class, MModelElement.class);
  List _constrainedElement = Collections.EMPTY_LIST;
  List _constrainedElement_ucopy = Collections.EMPTY_LIST;
  public final List getConstrainedElements()
  {
    checkExists();
    if (null == _constrainedElement_ucopy)
    {
      _constrainedElement_ucopy = ucopy(_constrainedElement);
    }
    return _constrainedElement_ucopy;
  }
  public final void setConstrainedElements(List __arg)
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
      List old = null;
      if (sendEvent || logForUndo)
      {
        old = getConstrainedElements();
      }
      _constrainedElement_ucopy = null;
      Collection __added = bagdiff(__arg,_constrainedElement);
      Collection __removed = bagdiff(_constrainedElement, __arg);
      Iterator iter1 = __removed.iterator();
      while (iter1.hasNext())
      {
        MModelElement o = (MModelElement)iter1.next();
        o.internalUnrefByConstraint(this);
      }
      Iterator iter2 = __added.iterator();
      while (iter2.hasNext())
      {
        MModelElement o = (MModelElement)iter2.next();
        o.internalRefByConstraint(this);
      }
      _constrainedElement = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_constrainedElement_setMethod, old, getConstrainedElements());
      }
      if (sendEvent)
      {
        fireListSet("constrainedElement", old, getConstrainedElements());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addConstrainedElement(MModelElement __arg)
  {
    addConstrainedElement(_constrainedElement.size(), __arg);
  }
  public final void removeConstrainedElement(MModelElement __arg)
  {
    if (__arg == null)
    {
      throw new NullPointerException();
    }
    int __pos = _constrainedElement.indexOf(__arg);
    removeConstrainedElement(__pos);
  }
  public final void addConstrainedElement(int __pos, MModelElement __arg)
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
      List old = null;
      if (sendEvent)
      {
        old = getConstrainedElements();
      }
      if (null != _constrainedElement_ucopy)
      {
        _constrainedElement = new ArrayList(_constrainedElement);
        _constrainedElement_ucopy = null;
      }
      _constrainedElement.add(__pos, __arg);
      __arg.internalRefByConstraint(this);
      logListAdd(_constrainedElement_addMethod, _constrainedElement_removeMethod, __arg, __pos);
      if (sendEvent)
      {
        fireListAdd("constrainedElement", old, getConstrainedElements(), __arg, __pos);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeConstrainedElement(int __pos)
  {
    operationStarted();
    try
    {
      checkExists();
      final boolean sendEvent = needEvent();
      List old = null;
      if (sendEvent)
      {
        old = getConstrainedElements();
      }
      if (null != _constrainedElement_ucopy)
      {
        _constrainedElement = new ArrayList(_constrainedElement);
        _constrainedElement_ucopy = null;
      }
      MModelElement __arg = (MModelElement)_constrainedElement.remove(__pos);
      __arg.internalUnrefByConstraint(this);
      logListRemove(_constrainedElement_removeMethod, _constrainedElement_addMethod, __arg, __pos);
      if (sendEvent)
      {
        fireListRemove("constrainedElement", old, getConstrainedElements(), __arg, __pos);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void setConstrainedElement(int __pos, MModelElement __arg)
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
      List old = null;
      if (sendEvent)
      {
        old = getConstrainedElements();
      }
      if (null != _constrainedElement_ucopy)
      {
        _constrainedElement = new ArrayList(_constrainedElement);
        _constrainedElement_ucopy = null;
      }
      MModelElement __old = (MModelElement)_constrainedElement.get(__pos);
      __old.internalUnrefByConstraint(this);
      __arg.internalRefByConstraint(this);
      _constrainedElement.set(__pos,__arg);
      logListSet(_constrainedElement_listSetMethod, __old, __arg, __pos);
      if (sendEvent)
      {
        fireListItemSet("constrainedElement", old, getConstrainedElements(), __old, __arg, __pos);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final MModelElement getConstrainedElement(int __pos)
  {
    checkExists();
    return (MModelElement)_constrainedElement.get(__pos);
  }
  protected void cleanup(Collection scheduledForRemove)
  {
    // opposite role: constrainedElement2 this role: stereotypeConstraint
    if (_constrainedElement2 != null )
    {
      setConstrainedElement2(null);
    }
    // opposite role: constrainedElement this role: constraint
    if (_constrainedElement.size() != 0)
    {
      setConstrainedElements(Collections.EMPTY_LIST);
    }
    super.cleanup(scheduledForRemove);
  }

  public String getUMLClassName()
  {
    return "Constraint";
  }

  // Reflective API

  public Object reflectiveGetValue(String feature)
  {
    if ("body".equals(feature))
    {
      return getBody();
    }
    if ("constrainedElement2".equals(feature))
    {
      return getConstrainedElement2();
    }
    if ("constrainedElement".equals(feature))
    {
      return getConstrainedElements();
    }

    return super.reflectiveGetValue(feature);
  }

  public void reflectiveSetValue(String feature, Object obj)
  {
    if ("body".equals(feature))
    {
      setBody((MBooleanExpression)obj);
      return;
    }
    if ("constrainedElement2".equals(feature))
    {
      setConstrainedElement2((MStereotype)obj);
      return;
    }
    if ("constrainedElement".equals(feature))
    {
      setConstrainedElements((List)obj);
      return;
    }

    super.reflectiveSetValue(feature, obj);
  }

  public void reflectiveAddValue(String feature, Object obj)
  {
    if ("constrainedElement".equals(feature))
    {
      addConstrainedElement((MModelElement)obj);
      return;
    }

    super.reflectiveAddValue(feature, obj);
  }

  public void reflectiveRemoveValue(String feature, Object obj)
  {
    if ("constrainedElement".equals(feature))
    {
      removeConstrainedElement((MModelElement)obj);
      return;
    }

    super.reflectiveRemoveValue(feature, obj);
  }

  public Object reflectiveGetValue(String feature, int pos)
  {
    if ("constrainedElement".equals(feature))
    {
      return getConstrainedElement(pos);
    }

    return super.reflectiveGetValue(feature, pos);
  }

  public void reflectiveSetValue(String feature, int pos, Object obj)
  {
    if ("constrainedElement".equals(feature))
    {
      setConstrainedElement(pos, (MModelElement)obj);
      return;
    }

    super.reflectiveSetValue(feature, pos, obj);
  }

  public void reflectiveAddValue(String feature, int pos, Object obj)
  {
    if ("constrainedElement".equals(feature))
    {
      addConstrainedElement(pos, (MModelElement)obj);
      return;
    }

    super.reflectiveAddValue(feature, pos, obj);
  }

  public void reflectiveRemoveValue(String feature, int pos)
  {
    if ("constrainedElement".equals(feature))
    {
      removeConstrainedElement(pos);
      return;
    }

    super.reflectiveRemoveValue(feature, pos);
  }
  public Collection getModelElementContents()
  {
    Collection ret = super.getModelElementContents();
    return ret;
  }
}
