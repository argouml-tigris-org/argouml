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

public abstract class MPresentationElementImpl extends MElementImpl implements MPresentationElement
{
  // ------------ code for class PresentationElement -----------------
  // generating attributes
  // generating associations
  // opposite role: subject this role: presentation
  private final static Method _subject_setMethod = getMethod1(MPresentationElementImpl.class, "setSubjects", Collection.class);
  private final static Method _subject_addMethod = getMethod1(MPresentationElementImpl.class, "addSubject", MModelElement.class);
  private final static Method _subject_removeMethod = getMethod1(MPresentationElementImpl.class, "removeSubject", MModelElement.class);
  Collection _subject = Collections.EMPTY_LIST;
  Collection _subject_ucopy = Collections.EMPTY_LIST;
  public final Collection getSubjects()
  {
    checkExists();
    if (null == _subject_ucopy)
    {
      _subject_ucopy = ucopy(_subject);
    }
    return _subject_ucopy;
  }
  public final void setSubjects(Collection __arg)
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
        old = getSubjects();
      }
      _subject_ucopy = null;
      Collection __added = bagdiff(__arg,_subject);
      Collection __removed = bagdiff(_subject, __arg);
      Iterator iter1 = __removed.iterator();
      while (iter1.hasNext())
      {
        MModelElement o = (MModelElement)iter1.next();
        o.internalUnrefByPresentation(this);
      }
      Iterator iter2 = __added.iterator();
      while (iter2.hasNext())
      {
        MModelElement o = (MModelElement)iter2.next();
        o.internalRefByPresentation(this);
      }
      _subject = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_subject_setMethod, old, getSubjects());
      }
      if (sendEvent)
      {
        fireBagSet("subject", old, getSubjects());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addSubject(MModelElement __arg)
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
        old = getSubjects();
      }
      if (null != _subject_ucopy)
      {
        _subject = new ArrayList(_subject);
        _subject_ucopy = null;
      }
      __arg.internalRefByPresentation(this);
      _subject.add(__arg);
      logBagAdd(_subject_addMethod, _subject_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("subject", old, getSubjects(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeSubject(MModelElement __arg)
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
        old = getSubjects();
      }
      if (null != _subject_ucopy)
      {
        _subject = new ArrayList(_subject);
        _subject_ucopy = null;
      }
      if (!_subject.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByPresentation(this);
      logBagRemove(_subject_removeMethod, _subject_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("subject", old, getSubjects(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefBySubject(MModelElement __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getSubjects();
    }
    if (null != _subject_ucopy)
    {
      _subject = new ArrayList(_subject);
      _subject_ucopy = null;
    }
    _subject.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("subject", old, getSubjects(), __arg);
    }
  }
  public final void internalUnrefBySubject(MModelElement __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getSubjects();
    }
    if (null != _subject_ucopy)
    {
      _subject = new ArrayList(_subject);
      _subject_ucopy = null;
    }
    _subject.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("subject", old, getSubjects(), __arg);
    }
  }
  protected void cleanup(Collection scheduledForRemove)
  {
    // opposite role: subject this role: presentation
    if (_subject.size() != 0)
    {
      setSubjects(Collections.EMPTY_LIST);
    }
    super.cleanup(scheduledForRemove);
  }

  public String getUMLClassName()
  {
    return "PresentationElement";
  }

  // Reflective API

  public Object reflectiveGetValue(String feature)
  {
    if ("subject".equals(feature))
    {
      return getSubjects();
    }

    return super.reflectiveGetValue(feature);
  }

  public void reflectiveSetValue(String feature, Object obj)
  {
    if ("subject".equals(feature))
    {
      setSubjects((Collection)obj);
      return;
    }

    super.reflectiveSetValue(feature, obj);
  }

  public void reflectiveAddValue(String feature, Object obj)
  {
    if ("subject".equals(feature))
    {
      addSubject((MModelElement)obj);
      return;
    }

    super.reflectiveAddValue(feature, obj);
  }

  public void reflectiveRemoveValue(String feature, Object obj)
  {
    if ("subject".equals(feature))
    {
      removeSubject((MModelElement)obj);
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
