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

public abstract class MGeneralizableElementImpl extends MModelElementImpl implements MGeneralizableElement
{
  // ------------ code for class GeneralizableElement -----------------
  // generating attributes
  // attribute: isAbstract
  private final static Method _isAbstract_setMethod = getMethod1(MGeneralizableElementImpl.class, "setAbstract", boolean.class);
  boolean _isAbstract;
  public final boolean isAbstract()
  {
    checkExists();
    return _isAbstract;
  }
  public final void setAbstract(boolean __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      logAttrSet(_isAbstract_setMethod, _isAbstract, __arg);
      fireAttrSet("isAbstract", _isAbstract, __arg);
      _isAbstract = __arg;
    }
    finally
    {
      operationFinished();
    }
  }
  // attribute: isLeaf
  private final static Method _isLeaf_setMethod = getMethod1(MGeneralizableElementImpl.class, "setLeaf", boolean.class);
  boolean _isLeaf;
  public final boolean isLeaf()
  {
    checkExists();
    return _isLeaf;
  }
  public final void setLeaf(boolean __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      logAttrSet(_isLeaf_setMethod, _isLeaf, __arg);
      fireAttrSet("isLeaf", _isLeaf, __arg);
      _isLeaf = __arg;
    }
    finally
    {
      operationFinished();
    }
  }
  // attribute: isRoot
  private final static Method _isRoot_setMethod = getMethod1(MGeneralizableElementImpl.class, "setRoot", boolean.class);
  boolean _isRoot;
  public final boolean isRoot()
  {
    checkExists();
    return _isRoot;
  }
  public final void setRoot(boolean __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      logAttrSet(_isRoot_setMethod, _isRoot, __arg);
      fireAttrSet("isRoot", _isRoot, __arg);
      _isRoot = __arg;
    }
    finally
    {
      operationFinished();
    }
  }
  // generating associations
  // opposite role: specialization this role: parent
  private final static Method _specialization_setMethod = getMethod1(MGeneralizableElementImpl.class, "setSpecializations", Collection.class);
  private final static Method _specialization_addMethod = getMethod1(MGeneralizableElementImpl.class, "addSpecialization", MGeneralization.class);
  private final static Method _specialization_removeMethod = getMethod1(MGeneralizableElementImpl.class, "removeSpecialization", MGeneralization.class);
  Collection _specialization = Collections.EMPTY_LIST;
  Collection _specialization_ucopy = Collections.EMPTY_LIST;
  public final Collection getSpecializations()
  {
    checkExists();
    if (null == _specialization_ucopy)
    {
      _specialization_ucopy = ucopy(_specialization);
    }
    return _specialization_ucopy;
  }
  public final void setSpecializations(Collection __arg)
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
        old = getSpecializations();
      }
      _specialization_ucopy = null;
      Collection __added = bagdiff(__arg,_specialization);
      Collection __removed = bagdiff(_specialization, __arg);
      Iterator iter1 = __removed.iterator();
      while (iter1.hasNext())
      {
        MGeneralization o = (MGeneralization)iter1.next();
        o.internalUnrefByParent(this);
      }
      Iterator iter2 = __added.iterator();
      while (iter2.hasNext())
      {
        MGeneralization o = (MGeneralization)iter2.next();
        o.internalRefByParent(this);
      }
      _specialization = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_specialization_setMethod, old, getSpecializations());
      }
      if (sendEvent)
      {
        fireBagSet("specialization", old, getSpecializations());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addSpecialization(MGeneralization __arg)
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
        old = getSpecializations();
      }
      if (null != _specialization_ucopy)
      {
        _specialization = new ArrayList(_specialization);
        _specialization_ucopy = null;
      }
      __arg.internalRefByParent(this);
      _specialization.add(__arg);
      logBagAdd(_specialization_addMethod, _specialization_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("specialization", old, getSpecializations(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeSpecialization(MGeneralization __arg)
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
        old = getSpecializations();
      }
      if (null != _specialization_ucopy)
      {
        _specialization = new ArrayList(_specialization);
        _specialization_ucopy = null;
      }
      if (!_specialization.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByParent(this);
      logBagRemove(_specialization_removeMethod, _specialization_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("specialization", old, getSpecializations(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefBySpecialization(MGeneralization __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getSpecializations();
    }
    if (null != _specialization_ucopy)
    {
      _specialization = new ArrayList(_specialization);
      _specialization_ucopy = null;
    }
    _specialization.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("specialization", old, getSpecializations(), __arg);
    }
  }
  public final void internalUnrefBySpecialization(MGeneralization __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getSpecializations();
    }
    if (null != _specialization_ucopy)
    {
      _specialization = new ArrayList(_specialization);
      _specialization_ucopy = null;
    }
    _specialization.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("specialization", old, getSpecializations(), __arg);
    }
  }
  // opposite role: generalization this role: child
  private final static Method _generalization_setMethod = getMethod1(MGeneralizableElementImpl.class, "setGeneralizations", Collection.class);
  private final static Method _generalization_addMethod = getMethod1(MGeneralizableElementImpl.class, "addGeneralization", MGeneralization.class);
  private final static Method _generalization_removeMethod = getMethod1(MGeneralizableElementImpl.class, "removeGeneralization", MGeneralization.class);
  Collection _generalization = Collections.EMPTY_LIST;
  Collection _generalization_ucopy = Collections.EMPTY_LIST;
  public final Collection getGeneralizations()
  {
    checkExists();
    if (null == _generalization_ucopy)
    {
      _generalization_ucopy = ucopy(_generalization);
    }
    return _generalization_ucopy;
  }
  public final void setGeneralizations(Collection __arg)
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
        old = getGeneralizations();
      }
      _generalization_ucopy = null;
      Collection __added = bagdiff(__arg,_generalization);
      Collection __removed = bagdiff(_generalization, __arg);
      Iterator iter3 = __removed.iterator();
      while (iter3.hasNext())
      {
        MGeneralization o = (MGeneralization)iter3.next();
        o.internalUnrefByChild(this);
      }
      Iterator iter4 = __added.iterator();
      while (iter4.hasNext())
      {
        MGeneralization o = (MGeneralization)iter4.next();
        o.internalRefByChild(this);
      }
      _generalization = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_generalization_setMethod, old, getGeneralizations());
      }
      if (sendEvent)
      {
        fireBagSet("generalization", old, getGeneralizations());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addGeneralization(MGeneralization __arg)
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
        old = getGeneralizations();
      }
      if (null != _generalization_ucopy)
      {
        _generalization = new ArrayList(_generalization);
        _generalization_ucopy = null;
      }
      __arg.internalRefByChild(this);
      _generalization.add(__arg);
      logBagAdd(_generalization_addMethod, _generalization_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("generalization", old, getGeneralizations(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeGeneralization(MGeneralization __arg)
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
        old = getGeneralizations();
      }
      if (null != _generalization_ucopy)
      {
        _generalization = new ArrayList(_generalization);
        _generalization_ucopy = null;
      }
      if (!_generalization.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByChild(this);
      logBagRemove(_generalization_removeMethod, _generalization_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("generalization", old, getGeneralizations(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByGeneralization(MGeneralization __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getGeneralizations();
    }
    if (null != _generalization_ucopy)
    {
      _generalization = new ArrayList(_generalization);
      _generalization_ucopy = null;
    }
    _generalization.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("generalization", old, getGeneralizations(), __arg);
    }
  }
  public final void internalUnrefByGeneralization(MGeneralization __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getGeneralizations();
    }
    if (null != _generalization_ucopy)
    {
      _generalization = new ArrayList(_generalization);
      _generalization_ucopy = null;
    }
    _generalization.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("generalization", old, getGeneralizations(), __arg);
    }
  }
  protected void cleanup(Collection scheduledForRemove)
  {
    // opposite role: specialization this role: parent
    if (_specialization.size() != 0)
    {
      setSpecializations(Collections.EMPTY_LIST);
    }
    // opposite role: generalization this role: child
    if (_generalization.size() != 0)
    {
      setGeneralizations(Collections.EMPTY_LIST);
    }
    super.cleanup(scheduledForRemove);
  }

  public String getUMLClassName()
  {
    return "GeneralizableElement";
  }

  // Reflective API

  public Object reflectiveGetValue(String feature)
  {
    if ("isAbstract".equals(feature))
    {
      return new Boolean(isAbstract());
    }
    if ("isLeaf".equals(feature))
    {
      return new Boolean(isLeaf());
    }
    if ("isRoot".equals(feature))
    {
      return new Boolean(isRoot());
    }
    if ("specialization".equals(feature))
    {
      return getSpecializations();
    }
    if ("generalization".equals(feature))
    {
      return getGeneralizations();
    }

    return super.reflectiveGetValue(feature);
  }

  public void reflectiveSetValue(String feature, Object obj)
  {
    if ("isAbstract".equals(feature))
    {
      setAbstract(((Boolean)obj).booleanValue());
      return;
    }
    if ("isLeaf".equals(feature))
    {
      setLeaf(((Boolean)obj).booleanValue());
      return;
    }
    if ("isRoot".equals(feature))
    {
      setRoot(((Boolean)obj).booleanValue());
      return;
    }
    if ("specialization".equals(feature))
    {
      setSpecializations((Collection)obj);
      return;
    }
    if ("generalization".equals(feature))
    {
      setGeneralizations((Collection)obj);
      return;
    }

    super.reflectiveSetValue(feature, obj);
  }

  public void reflectiveAddValue(String feature, Object obj)
  {
    if ("specialization".equals(feature))
    {
      addSpecialization((MGeneralization)obj);
      return;
    }
    if ("generalization".equals(feature))
    {
      addGeneralization((MGeneralization)obj);
      return;
    }

    super.reflectiveAddValue(feature, obj);
  }

  public void reflectiveRemoveValue(String feature, Object obj)
  {
    if ("specialization".equals(feature))
    {
      removeSpecialization((MGeneralization)obj);
      return;
    }
    if ("generalization".equals(feature))
    {
      removeGeneralization((MGeneralization)obj);
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
  //public boolean isRoot()
  //{
  //  return getGeneralizations().size() == 0;
  //}

  //public boolean isLeaf()
  //{
  //  return getSpecializations().size() == 0;
  //}

  /** get parents */
  public List getChildren()
  {
    List rc = new ArrayList();
    Iterator i = getSpecializations().iterator();
    while(i.hasNext())
    {
      ru.novosoft.uml.foundation.core.MGeneralization g = (ru.novosoft.uml.foundation.core.MGeneralization)i.next();
      rc.add(g.getChild());
    }
    return rc;
  }

  /** get childs */
  public List getParents()
  {
    List rc = new ArrayList();
    Iterator i = getGeneralizations().iterator();
    while(i.hasNext())
    {
      ru.novosoft.uml.foundation.core.MGeneralization g = (ru.novosoft.uml.foundation.core.MGeneralization)i.next();
      rc.add(g.getParent());
    }
    return rc;
  }
}
