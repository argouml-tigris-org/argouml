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

public class MStereotypeImpl extends MGeneralizableElementImpl implements MStereotype
{
  // ------------ code for class Stereotype -----------------
  // generating attributes
  // attribute: baseClass
  private final static Method _baseClass_setMethod = getMethod1(MStereotypeImpl.class, "setBaseClass", String.class);
  String _baseClass;
  public final String getBaseClass()
  {
    checkExists();
    return _baseClass;
  }
  public final void setBaseClass(String __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      logAttrSet(_baseClass_setMethod, _baseClass, __arg);
      fireAttrSet("baseClass", _baseClass, __arg);
      _baseClass = __arg;
    }
    finally
    {
      operationFinished();
    }
  }
  // attribute: icon
  private final static Method _icon_setMethod = getMethod1(MStereotypeImpl.class, "setIcon", String.class);
  String _icon;
  public final String getIcon()
  {
    checkExists();
    return _icon;
  }
  public final void setIcon(String __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      logAttrSet(_icon_setMethod, _icon, __arg);
      fireAttrSet("icon", _icon, __arg);
      _icon = __arg;
    }
    finally
    {
      operationFinished();
    }
  }
  // generating associations
  // opposite role: stereotypeConstraint this role: constrainedElement2
  private final static Method _stereotypeConstraint_setMethod = getMethod1(MStereotypeImpl.class, "setStereotypeConstraints", Collection.class);
  private final static Method _stereotypeConstraint_addMethod = getMethod1(MStereotypeImpl.class, "addStereotypeConstraint", MConstraint.class);
  private final static Method _stereotypeConstraint_removeMethod = getMethod1(MStereotypeImpl.class, "removeStereotypeConstraint", MConstraint.class);
  Collection _stereotypeConstraint = Collections.EMPTY_LIST;
  Collection _stereotypeConstraint_ucopy = Collections.EMPTY_LIST;
  public final Collection getStereotypeConstraints()
  {
    checkExists();
    if (null == _stereotypeConstraint_ucopy)
    {
      _stereotypeConstraint_ucopy = ucopy(_stereotypeConstraint);
    }
    return _stereotypeConstraint_ucopy;
  }
  public final void setStereotypeConstraints(Collection __arg)
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
        old = getStereotypeConstraints();
      }
      _stereotypeConstraint_ucopy = null;
      Collection __added = bagdiff(__arg,_stereotypeConstraint);
      Collection __removed = bagdiff(_stereotypeConstraint, __arg);
      Iterator iter1 = __removed.iterator();
      while (iter1.hasNext())
      {
        MConstraint o = (MConstraint)iter1.next();
        o.internalUnrefByConstrainedElement2(this);
      }
      Iterator iter2 = __added.iterator();
      while (iter2.hasNext())
      {
        MConstraint o = (MConstraint)iter2.next();
        o.internalRefByConstrainedElement2(this);
      }
      _stereotypeConstraint = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_stereotypeConstraint_setMethod, old, getStereotypeConstraints());
      }
      if (sendEvent)
      {
        fireBagSet("stereotypeConstraint", old, getStereotypeConstraints());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addStereotypeConstraint(MConstraint __arg)
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
        old = getStereotypeConstraints();
      }
      if (null != _stereotypeConstraint_ucopy)
      {
        _stereotypeConstraint = new ArrayList(_stereotypeConstraint);
        _stereotypeConstraint_ucopy = null;
      }
      __arg.internalRefByConstrainedElement2(this);
      _stereotypeConstraint.add(__arg);
      logBagAdd(_stereotypeConstraint_addMethod, _stereotypeConstraint_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("stereotypeConstraint", old, getStereotypeConstraints(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeStereotypeConstraint(MConstraint __arg)
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
        old = getStereotypeConstraints();
      }
      if (null != _stereotypeConstraint_ucopy)
      {
        _stereotypeConstraint = new ArrayList(_stereotypeConstraint);
        _stereotypeConstraint_ucopy = null;
      }
      if (!_stereotypeConstraint.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByConstrainedElement2(this);
      logBagRemove(_stereotypeConstraint_removeMethod, _stereotypeConstraint_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("stereotypeConstraint", old, getStereotypeConstraints(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByStereotypeConstraint(MConstraint __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getStereotypeConstraints();
    }
    if (null != _stereotypeConstraint_ucopy)
    {
      _stereotypeConstraint = new ArrayList(_stereotypeConstraint);
      _stereotypeConstraint_ucopy = null;
    }
    _stereotypeConstraint.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("stereotypeConstraint", old, getStereotypeConstraints(), __arg);
    }
  }
  public final void internalUnrefByStereotypeConstraint(MConstraint __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getStereotypeConstraints();
    }
    if (null != _stereotypeConstraint_ucopy)
    {
      _stereotypeConstraint = new ArrayList(_stereotypeConstraint);
      _stereotypeConstraint_ucopy = null;
    }
    _stereotypeConstraint.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("stereotypeConstraint", old, getStereotypeConstraints(), __arg);
    }
  }
  // opposite role: extendedElement this role: stereotype
  private final static Method _extendedElement_setMethod = getMethod1(MStereotypeImpl.class, "setExtendedElements", Collection.class);
  private final static Method _extendedElement_addMethod = getMethod1(MStereotypeImpl.class, "addExtendedElement", MModelElement.class);
  private final static Method _extendedElement_removeMethod = getMethod1(MStereotypeImpl.class, "removeExtendedElement", MModelElement.class);
  Collection _extendedElement = Collections.EMPTY_LIST;
  Collection _extendedElement_ucopy = Collections.EMPTY_LIST;
  public final Collection getExtendedElements()
  {
    checkExists();
    if (null == _extendedElement_ucopy)
    {
      _extendedElement_ucopy = ucopy(_extendedElement);
    }
    return _extendedElement_ucopy;
  }
  public final void setExtendedElements(Collection __arg)
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
        old = getExtendedElements();
      }
      _extendedElement_ucopy = null;
      Collection __added = bagdiff(__arg,_extendedElement);
      Collection __removed = bagdiff(_extendedElement, __arg);
      Iterator iter3 = __removed.iterator();
      while (iter3.hasNext())
      {
        MModelElement o = (MModelElement)iter3.next();
        o.internalUnrefByStereotype(this);
      }
      Iterator iter4 = __added.iterator();
      while (iter4.hasNext())
      {
        MModelElement o = (MModelElement)iter4.next();
        o.internalRefByStereotype(this);
      }
      _extendedElement = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_extendedElement_setMethod, old, getExtendedElements());
      }
      if (sendEvent)
      {
        fireBagSet("extendedElement", old, getExtendedElements());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addExtendedElement(MModelElement __arg)
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
        old = getExtendedElements();
      }
      if (null != _extendedElement_ucopy)
      {
        _extendedElement = new ArrayList(_extendedElement);
        _extendedElement_ucopy = null;
      }
      __arg.internalRefByStereotype(this);
      _extendedElement.add(__arg);
      logBagAdd(_extendedElement_addMethod, _extendedElement_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("extendedElement", old, getExtendedElements(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeExtendedElement(MModelElement __arg)
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
        old = getExtendedElements();
      }
      if (null != _extendedElement_ucopy)
      {
        _extendedElement = new ArrayList(_extendedElement);
        _extendedElement_ucopy = null;
      }
      if (!_extendedElement.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByStereotype(this);
      logBagRemove(_extendedElement_removeMethod, _extendedElement_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("extendedElement", old, getExtendedElements(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByExtendedElement(MModelElement __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getExtendedElements();
    }
    if (null != _extendedElement_ucopy)
    {
      _extendedElement = new ArrayList(_extendedElement);
      _extendedElement_ucopy = null;
    }
    _extendedElement.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("extendedElement", old, getExtendedElements(), __arg);
    }
  }
  public final void internalUnrefByExtendedElement(MModelElement __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getExtendedElements();
    }
    if (null != _extendedElement_ucopy)
    {
      _extendedElement = new ArrayList(_extendedElement);
      _extendedElement_ucopy = null;
    }
    _extendedElement.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("extendedElement", old, getExtendedElements(), __arg);
    }
  }
  // opposite role: requiredTag this role: stereotype
  private final static Method _requiredTag_setMethod = getMethod1(MStereotypeImpl.class, "setRequiredTags", Collection.class);
  private final static Method _requiredTag_addMethod = getMethod1(MStereotypeImpl.class, "addRequiredTag", MTaggedValue.class);
  private final static Method _requiredTag_removeMethod = getMethod1(MStereotypeImpl.class, "removeRequiredTag", MTaggedValue.class);
  Collection _requiredTag = Collections.EMPTY_LIST;
  Collection _requiredTag_ucopy = Collections.EMPTY_LIST;
  public final Collection getRequiredTags()
  {
    checkExists();
    if (null == _requiredTag_ucopy)
    {
      _requiredTag_ucopy = ucopy(_requiredTag);
    }
    return _requiredTag_ucopy;
  }
  public final void setRequiredTags(Collection __arg)
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
        old = getRequiredTags();
      }
      _requiredTag_ucopy = null;
      Collection __added = bagdiff(__arg,_requiredTag);
      Collection __removed = bagdiff(_requiredTag, __arg);
      Iterator iter5 = __removed.iterator();
      while (iter5.hasNext())
      {
        MTaggedValue o = (MTaggedValue)iter5.next();
        o.internalUnrefByStereotype(this);
      }
      Iterator iter6 = __added.iterator();
      while (iter6.hasNext())
      {
        MTaggedValue o = (MTaggedValue)iter6.next();
        o.internalRefByStereotype(this);
      }
      _requiredTag = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_requiredTag_setMethod, old, getRequiredTags());
      }
      if (sendEvent)
      {
        fireBagSet("requiredTag", old, getRequiredTags());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addRequiredTag(MTaggedValue __arg)
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
        old = getRequiredTags();
      }
      if (null != _requiredTag_ucopy)
      {
        _requiredTag = new ArrayList(_requiredTag);
        _requiredTag_ucopy = null;
      }
      __arg.internalRefByStereotype(this);
      _requiredTag.add(__arg);
      logBagAdd(_requiredTag_addMethod, _requiredTag_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("requiredTag", old, getRequiredTags(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeRequiredTag(MTaggedValue __arg)
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
        old = getRequiredTags();
      }
      if (null != _requiredTag_ucopy)
      {
        _requiredTag = new ArrayList(_requiredTag);
        _requiredTag_ucopy = null;
      }
      if (!_requiredTag.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByStereotype(this);
      logBagRemove(_requiredTag_removeMethod, _requiredTag_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("requiredTag", old, getRequiredTags(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByRequiredTag(MTaggedValue __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getRequiredTags();
    }
    if (null != _requiredTag_ucopy)
    {
      _requiredTag = new ArrayList(_requiredTag);
      _requiredTag_ucopy = null;
    }
    _requiredTag.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("requiredTag", old, getRequiredTags(), __arg);
    }
  }
  public final void internalUnrefByRequiredTag(MTaggedValue __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getRequiredTags();
    }
    if (null != _requiredTag_ucopy)
    {
      _requiredTag = new ArrayList(_requiredTag);
      _requiredTag_ucopy = null;
    }
    _requiredTag.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("requiredTag", old, getRequiredTags(), __arg);
    }
  }
  protected void cleanup(Collection scheduledForRemove)
  {
    // opposite role: stereotypeConstraint this role: constrainedElement2
    if (_stereotypeConstraint.size() != 0)
    {
      scheduledForRemove.addAll(_stereotypeConstraint);
      setStereotypeConstraints(Collections.EMPTY_LIST);
    }
    // opposite role: extendedElement this role: stereotype
    if (_extendedElement.size() != 0)
    {
      setExtendedElements(Collections.EMPTY_LIST);
    }
    // opposite role: requiredTag this role: stereotype
    if (_requiredTag.size() != 0)
    {
      scheduledForRemove.addAll(_requiredTag);
      setRequiredTags(Collections.EMPTY_LIST);
    }
    super.cleanup(scheduledForRemove);
  }

  public String getUMLClassName()
  {
    return "Stereotype";
  }

  // Reflective API

  public Object reflectiveGetValue(String feature)
  {
    if ("baseClass".equals(feature))
    {
      return getBaseClass();
    }
    if ("icon".equals(feature))
    {
      return getIcon();
    }
    if ("stereotypeConstraint".equals(feature))
    {
      return getStereotypeConstraints();
    }
    if ("extendedElement".equals(feature))
    {
      return getExtendedElements();
    }
    if ("requiredTag".equals(feature))
    {
      return getRequiredTags();
    }

    return super.reflectiveGetValue(feature);
  }

  public void reflectiveSetValue(String feature, Object obj)
  {
    if ("baseClass".equals(feature))
    {
      setBaseClass((String)obj);
      return;
    }
    if ("icon".equals(feature))
    {
      setIcon((String)obj);
      return;
    }
    if ("stereotypeConstraint".equals(feature))
    {
      setStereotypeConstraints((Collection)obj);
      return;
    }
    if ("extendedElement".equals(feature))
    {
      setExtendedElements((Collection)obj);
      return;
    }
    if ("requiredTag".equals(feature))
    {
      setRequiredTags((Collection)obj);
      return;
    }

    super.reflectiveSetValue(feature, obj);
  }

  public void reflectiveAddValue(String feature, Object obj)
  {
    if ("stereotypeConstraint".equals(feature))
    {
      addStereotypeConstraint((MConstraint)obj);
      return;
    }
    if ("extendedElement".equals(feature))
    {
      addExtendedElement((MModelElement)obj);
      return;
    }
    if ("requiredTag".equals(feature))
    {
      addRequiredTag((MTaggedValue)obj);
      return;
    }

    super.reflectiveAddValue(feature, obj);
  }

  public void reflectiveRemoveValue(String feature, Object obj)
  {
    if ("stereotypeConstraint".equals(feature))
    {
      removeStereotypeConstraint((MConstraint)obj);
      return;
    }
    if ("extendedElement".equals(feature))
    {
      removeExtendedElement((MModelElement)obj);
      return;
    }
    if ("requiredTag".equals(feature))
    {
      removeRequiredTag((MTaggedValue)obj);
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
    ret.addAll(getStereotypeConstraints());
    ret.addAll(getRequiredTags());
    return ret;
  }
}
