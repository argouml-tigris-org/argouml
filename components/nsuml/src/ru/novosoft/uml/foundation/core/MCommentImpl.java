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

public class MCommentImpl extends MModelElementImpl implements MComment
{
  // ------------ code for class Comment -----------------
  // generating attributes
  // generating associations
  // opposite role: annotatedElement this role: comment
  private final static Method _annotatedElement_setMethod = getMethod1(MCommentImpl.class, "setAnnotatedElements", Collection.class);
  private final static Method _annotatedElement_addMethod = getMethod1(MCommentImpl.class, "addAnnotatedElement", MModelElement.class);
  private final static Method _annotatedElement_removeMethod = getMethod1(MCommentImpl.class, "removeAnnotatedElement", MModelElement.class);
  Collection _annotatedElement = Collections.EMPTY_LIST;
  Collection _annotatedElement_ucopy = Collections.EMPTY_LIST;
  public final Collection getAnnotatedElements()
  {
    checkExists();
    if (null == _annotatedElement_ucopy)
    {
      _annotatedElement_ucopy = ucopy(_annotatedElement);
    }
    return _annotatedElement_ucopy;
  }
  public final void setAnnotatedElements(Collection __arg)
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
        old = getAnnotatedElements();
      }
      _annotatedElement_ucopy = null;
      Collection __added = bagdiff(__arg,_annotatedElement);
      Collection __removed = bagdiff(_annotatedElement, __arg);
      Iterator iter1 = __removed.iterator();
      while (iter1.hasNext())
      {
        MModelElement o = (MModelElement)iter1.next();
        o.internalUnrefByComment(this);
      }
      Iterator iter2 = __added.iterator();
      while (iter2.hasNext())
      {
        MModelElement o = (MModelElement)iter2.next();
        o.internalRefByComment(this);
      }
      _annotatedElement = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_annotatedElement_setMethod, old, getAnnotatedElements());
      }
      if (sendEvent)
      {
        fireBagSet("annotatedElement", old, getAnnotatedElements());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addAnnotatedElement(MModelElement __arg)
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
        old = getAnnotatedElements();
      }
      if (null != _annotatedElement_ucopy)
      {
        _annotatedElement = new ArrayList(_annotatedElement);
        _annotatedElement_ucopy = null;
      }
      __arg.internalRefByComment(this);
      _annotatedElement.add(__arg);
      logBagAdd(_annotatedElement_addMethod, _annotatedElement_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("annotatedElement", old, getAnnotatedElements(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeAnnotatedElement(MModelElement __arg)
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
        old = getAnnotatedElements();
      }
      if (null != _annotatedElement_ucopy)
      {
        _annotatedElement = new ArrayList(_annotatedElement);
        _annotatedElement_ucopy = null;
      }
      if (!_annotatedElement.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByComment(this);
      logBagRemove(_annotatedElement_removeMethod, _annotatedElement_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("annotatedElement", old, getAnnotatedElements(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByAnnotatedElement(MModelElement __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getAnnotatedElements();
    }
    if (null != _annotatedElement_ucopy)
    {
      _annotatedElement = new ArrayList(_annotatedElement);
      _annotatedElement_ucopy = null;
    }
    _annotatedElement.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("annotatedElement", old, getAnnotatedElements(), __arg);
    }
  }
  public final void internalUnrefByAnnotatedElement(MModelElement __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getAnnotatedElements();
    }
    if (null != _annotatedElement_ucopy)
    {
      _annotatedElement = new ArrayList(_annotatedElement);
      _annotatedElement_ucopy = null;
    }
    _annotatedElement.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("annotatedElement", old, getAnnotatedElements(), __arg);
    }
  }
  protected void cleanup(Collection scheduledForRemove)
  {
    // opposite role: annotatedElement this role: comment
    if (_annotatedElement.size() != 0)
    {
      setAnnotatedElements(Collections.EMPTY_LIST);
    }
    super.cleanup(scheduledForRemove);
  }

  public String getUMLClassName()
  {
    return "Comment";
  }

  // Reflective API

  public Object reflectiveGetValue(String feature)
  {
    if ("annotatedElement".equals(feature))
    {
      return getAnnotatedElements();
    }

    return super.reflectiveGetValue(feature);
  }

  public void reflectiveSetValue(String feature, Object obj)
  {
    if ("annotatedElement".equals(feature))
    {
      setAnnotatedElements((Collection)obj);
      return;
    }

    super.reflectiveSetValue(feature, obj);
  }

  public void reflectiveAddValue(String feature, Object obj)
  {
    if ("annotatedElement".equals(feature))
    {
      addAnnotatedElement((MModelElement)obj);
      return;
    }

    super.reflectiveAddValue(feature, obj);
  }

  public void reflectiveRemoveValue(String feature, Object obj)
  {
    if ("annotatedElement".equals(feature))
    {
      removeAnnotatedElement((MModelElement)obj);
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
