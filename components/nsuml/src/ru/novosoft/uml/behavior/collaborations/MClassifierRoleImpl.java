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

package ru.novosoft.uml.behavior.collaborations;

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
import ru.novosoft.uml.behavior.use_cases.*;
import ru.novosoft.uml.behavior.state_machines.*;
import ru.novosoft.uml.behavior.activity_graphs.*;
import ru.novosoft.uml.model_management.*;

import java.lang.reflect.Method;

public class MClassifierRoleImpl extends MClassifierImpl implements MClassifierRole
{
  // ------------ code for class ClassifierRole -----------------
  // generating attributes
  // attribute: multiplicity
  private final static Method _multiplicity_setMethod = getMethod1(MClassifierRoleImpl.class, "setMultiplicity", MMultiplicity.class);
  MMultiplicity _multiplicity;
  public final MMultiplicity getMultiplicity()
  {
    checkExists();
    return _multiplicity;
  }
  public final void setMultiplicity(MMultiplicity __arg)
  {
    operationStarted();
    try
    {
      checkExists();
      logAttrSet(_multiplicity_setMethod, _multiplicity, __arg);
      fireAttrSet("multiplicity", _multiplicity, __arg);
      _multiplicity = __arg;
    }
    finally
    {
      operationFinished();
    }
  }
  // generating associations
  // opposite role: availableContents this role: classifierRole1
  private final static Method _availableContents_setMethod = getMethod1(MClassifierRoleImpl.class, "setAvailableContentses", Collection.class);
  private final static Method _availableContents_addMethod = getMethod1(MClassifierRoleImpl.class, "addAvailableContents", MModelElement.class);
  private final static Method _availableContents_removeMethod = getMethod1(MClassifierRoleImpl.class, "removeAvailableContents", MModelElement.class);
  Collection _availableContents = Collections.EMPTY_LIST;
  Collection _availableContents_ucopy = Collections.EMPTY_LIST;
  public final Collection getAvailableContentses()
  {
    checkExists();
    if (null == _availableContents_ucopy)
    {
      _availableContents_ucopy = ucopy(_availableContents);
    }
    return _availableContents_ucopy;
  }
  public final void setAvailableContentses(Collection __arg)
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
        old = getAvailableContentses();
      }
      _availableContents_ucopy = null;
      Collection __added = bagdiff(__arg,_availableContents);
      Collection __removed = bagdiff(_availableContents, __arg);
      Iterator iter1 = __removed.iterator();
      while (iter1.hasNext())
      {
        MModelElement o = (MModelElement)iter1.next();
        o.internalUnrefByClassifierRole1(this);
      }
      Iterator iter2 = __added.iterator();
      while (iter2.hasNext())
      {
        MModelElement o = (MModelElement)iter2.next();
        o.internalRefByClassifierRole1(this);
      }
      _availableContents = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_availableContents_setMethod, old, getAvailableContentses());
      }
      if (sendEvent)
      {
        fireBagSet("availableContents", old, getAvailableContentses());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addAvailableContents(MModelElement __arg)
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
        old = getAvailableContentses();
      }
      if (null != _availableContents_ucopy)
      {
        _availableContents = new ArrayList(_availableContents);
        _availableContents_ucopy = null;
      }
      __arg.internalRefByClassifierRole1(this);
      _availableContents.add(__arg);
      logBagAdd(_availableContents_addMethod, _availableContents_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("availableContents", old, getAvailableContentses(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeAvailableContents(MModelElement __arg)
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
        old = getAvailableContentses();
      }
      if (null != _availableContents_ucopy)
      {
        _availableContents = new ArrayList(_availableContents);
        _availableContents_ucopy = null;
      }
      if (!_availableContents.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByClassifierRole1(this);
      logBagRemove(_availableContents_removeMethod, _availableContents_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("availableContents", old, getAvailableContentses(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByAvailableContents(MModelElement __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getAvailableContentses();
    }
    if (null != _availableContents_ucopy)
    {
      _availableContents = new ArrayList(_availableContents);
      _availableContents_ucopy = null;
    }
    _availableContents.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("availableContents", old, getAvailableContentses(), __arg);
    }
  }
  public final void internalUnrefByAvailableContents(MModelElement __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getAvailableContentses();
    }
    if (null != _availableContents_ucopy)
    {
      _availableContents = new ArrayList(_availableContents);
      _availableContents_ucopy = null;
    }
    _availableContents.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("availableContents", old, getAvailableContentses(), __arg);
    }
  }
  // opposite role: message1 this role: receiver
  private final static Method _message1_setMethod = getMethod1(MClassifierRoleImpl.class, "setMessages1", Collection.class);
  private final static Method _message1_addMethod = getMethod1(MClassifierRoleImpl.class, "addMessage1", MMessage.class);
  private final static Method _message1_removeMethod = getMethod1(MClassifierRoleImpl.class, "removeMessage1", MMessage.class);
  Collection _message1 = Collections.EMPTY_LIST;
  Collection _message1_ucopy = Collections.EMPTY_LIST;
  public final Collection getMessages1()
  {
    checkExists();
    if (null == _message1_ucopy)
    {
      _message1_ucopy = ucopy(_message1);
    }
    return _message1_ucopy;
  }
  public final void setMessages1(Collection __arg)
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
        old = getMessages1();
      }
      _message1_ucopy = null;
      Collection __added = bagdiff(__arg,_message1);
      Collection __removed = bagdiff(_message1, __arg);
      Iterator iter3 = __removed.iterator();
      while (iter3.hasNext())
      {
        MMessage o = (MMessage)iter3.next();
        o.internalUnrefByReceiver(this);
      }
      Iterator iter4 = __added.iterator();
      while (iter4.hasNext())
      {
        MMessage o = (MMessage)iter4.next();
        o.internalRefByReceiver(this);
      }
      _message1 = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_message1_setMethod, old, getMessages1());
      }
      if (sendEvent)
      {
        fireBagSet("message1", old, getMessages1());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addMessage1(MMessage __arg)
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
        old = getMessages1();
      }
      if (null != _message1_ucopy)
      {
        _message1 = new ArrayList(_message1);
        _message1_ucopy = null;
      }
      __arg.internalRefByReceiver(this);
      _message1.add(__arg);
      logBagAdd(_message1_addMethod, _message1_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("message1", old, getMessages1(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeMessage1(MMessage __arg)
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
        old = getMessages1();
      }
      if (null != _message1_ucopy)
      {
        _message1 = new ArrayList(_message1);
        _message1_ucopy = null;
      }
      if (!_message1.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByReceiver(this);
      logBagRemove(_message1_removeMethod, _message1_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("message1", old, getMessages1(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByMessage1(MMessage __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getMessages1();
    }
    if (null != _message1_ucopy)
    {
      _message1 = new ArrayList(_message1);
      _message1_ucopy = null;
    }
    _message1.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("message1", old, getMessages1(), __arg);
    }
  }
  public final void internalUnrefByMessage1(MMessage __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getMessages1();
    }
    if (null != _message1_ucopy)
    {
      _message1 = new ArrayList(_message1);
      _message1_ucopy = null;
    }
    _message1.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("message1", old, getMessages1(), __arg);
    }
  }
  // opposite role: message2 this role: sender
  private final static Method _message2_setMethod = getMethod1(MClassifierRoleImpl.class, "setMessages2", Collection.class);
  private final static Method _message2_addMethod = getMethod1(MClassifierRoleImpl.class, "addMessage2", MMessage.class);
  private final static Method _message2_removeMethod = getMethod1(MClassifierRoleImpl.class, "removeMessage2", MMessage.class);
  Collection _message2 = Collections.EMPTY_LIST;
  Collection _message2_ucopy = Collections.EMPTY_LIST;
  public final Collection getMessages2()
  {
    checkExists();
    if (null == _message2_ucopy)
    {
      _message2_ucopy = ucopy(_message2);
    }
    return _message2_ucopy;
  }
  public final void setMessages2(Collection __arg)
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
        old = getMessages2();
      }
      _message2_ucopy = null;
      Collection __added = bagdiff(__arg,_message2);
      Collection __removed = bagdiff(_message2, __arg);
      Iterator iter5 = __removed.iterator();
      while (iter5.hasNext())
      {
        MMessage o = (MMessage)iter5.next();
        o.internalUnrefBySender(this);
      }
      Iterator iter6 = __added.iterator();
      while (iter6.hasNext())
      {
        MMessage o = (MMessage)iter6.next();
        o.internalRefBySender(this);
      }
      _message2 = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_message2_setMethod, old, getMessages2());
      }
      if (sendEvent)
      {
        fireBagSet("message2", old, getMessages2());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addMessage2(MMessage __arg)
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
        old = getMessages2();
      }
      if (null != _message2_ucopy)
      {
        _message2 = new ArrayList(_message2);
        _message2_ucopy = null;
      }
      __arg.internalRefBySender(this);
      _message2.add(__arg);
      logBagAdd(_message2_addMethod, _message2_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("message2", old, getMessages2(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeMessage2(MMessage __arg)
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
        old = getMessages2();
      }
      if (null != _message2_ucopy)
      {
        _message2 = new ArrayList(_message2);
        _message2_ucopy = null;
      }
      if (!_message2.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefBySender(this);
      logBagRemove(_message2_removeMethod, _message2_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("message2", old, getMessages2(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByMessage2(MMessage __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getMessages2();
    }
    if (null != _message2_ucopy)
    {
      _message2 = new ArrayList(_message2);
      _message2_ucopy = null;
    }
    _message2.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("message2", old, getMessages2(), __arg);
    }
  }
  public final void internalUnrefByMessage2(MMessage __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getMessages2();
    }
    if (null != _message2_ucopy)
    {
      _message2 = new ArrayList(_message2);
      _message2_ucopy = null;
    }
    _message2.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("message2", old, getMessages2(), __arg);
    }
  }
  // opposite role: availableFeature this role: classifierRole
  private final static Method _availableFeature_setMethod = getMethod1(MClassifierRoleImpl.class, "setAvailableFeatures", Collection.class);
  private final static Method _availableFeature_addMethod = getMethod1(MClassifierRoleImpl.class, "addAvailableFeature", MFeature.class);
  private final static Method _availableFeature_removeMethod = getMethod1(MClassifierRoleImpl.class, "removeAvailableFeature", MFeature.class);
  Collection _availableFeature = Collections.EMPTY_LIST;
  Collection _availableFeature_ucopy = Collections.EMPTY_LIST;
  public final Collection getAvailableFeatures()
  {
    checkExists();
    if (null == _availableFeature_ucopy)
    {
      _availableFeature_ucopy = ucopy(_availableFeature);
    }
    return _availableFeature_ucopy;
  }
  public final void setAvailableFeatures(Collection __arg)
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
        old = getAvailableFeatures();
      }
      _availableFeature_ucopy = null;
      Collection __added = bagdiff(__arg,_availableFeature);
      Collection __removed = bagdiff(_availableFeature, __arg);
      Iterator iter7 = __removed.iterator();
      while (iter7.hasNext())
      {
        MFeature o = (MFeature)iter7.next();
        o.internalUnrefByClassifierRole(this);
      }
      Iterator iter8 = __added.iterator();
      while (iter8.hasNext())
      {
        MFeature o = (MFeature)iter8.next();
        o.internalRefByClassifierRole(this);
      }
      _availableFeature = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_availableFeature_setMethod, old, getAvailableFeatures());
      }
      if (sendEvent)
      {
        fireBagSet("availableFeature", old, getAvailableFeatures());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addAvailableFeature(MFeature __arg)
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
        old = getAvailableFeatures();
      }
      if (null != _availableFeature_ucopy)
      {
        _availableFeature = new ArrayList(_availableFeature);
        _availableFeature_ucopy = null;
      }
      __arg.internalRefByClassifierRole(this);
      _availableFeature.add(__arg);
      logBagAdd(_availableFeature_addMethod, _availableFeature_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("availableFeature", old, getAvailableFeatures(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeAvailableFeature(MFeature __arg)
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
        old = getAvailableFeatures();
      }
      if (null != _availableFeature_ucopy)
      {
        _availableFeature = new ArrayList(_availableFeature);
        _availableFeature_ucopy = null;
      }
      if (!_availableFeature.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByClassifierRole(this);
      logBagRemove(_availableFeature_removeMethod, _availableFeature_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("availableFeature", old, getAvailableFeatures(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByAvailableFeature(MFeature __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getAvailableFeatures();
    }
    if (null != _availableFeature_ucopy)
    {
      _availableFeature = new ArrayList(_availableFeature);
      _availableFeature_ucopy = null;
    }
    _availableFeature.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("availableFeature", old, getAvailableFeatures(), __arg);
    }
  }
  public final void internalUnrefByAvailableFeature(MFeature __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getAvailableFeatures();
    }
    if (null != _availableFeature_ucopy)
    {
      _availableFeature = new ArrayList(_availableFeature);
      _availableFeature_ucopy = null;
    }
    _availableFeature.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("availableFeature", old, getAvailableFeatures(), __arg);
    }
  }
  // opposite role: base this role: classifierRole
  private final static Method _base_setMethod = getMethod1(MClassifierRoleImpl.class, "setBases", Collection.class);
  private final static Method _base_addMethod = getMethod1(MClassifierRoleImpl.class, "addBase", MClassifier.class);
  private final static Method _base_removeMethod = getMethod1(MClassifierRoleImpl.class, "removeBase", MClassifier.class);
  Collection _base = Collections.EMPTY_LIST;
  Collection _base_ucopy = Collections.EMPTY_LIST;
  public final Collection getBases()
  {
    checkExists();
    if (null == _base_ucopy)
    {
      _base_ucopy = ucopy(_base);
    }
    return _base_ucopy;
  }
  public final void setBases(Collection __arg)
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
        old = getBases();
      }
      _base_ucopy = null;
      Collection __added = bagdiff(__arg,_base);
      Collection __removed = bagdiff(_base, __arg);
      Iterator iter9 = __removed.iterator();
      while (iter9.hasNext())
      {
        MClassifier o = (MClassifier)iter9.next();
        o.internalUnrefByClassifierRole(this);
      }
      Iterator iter10 = __added.iterator();
      while (iter10.hasNext())
      {
        MClassifier o = (MClassifier)iter10.next();
        o.internalRefByClassifierRole(this);
      }
      _base = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_base_setMethod, old, getBases());
      }
      if (sendEvent)
      {
        fireBagSet("base", old, getBases());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addBase(MClassifier __arg)
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
        old = getBases();
      }
      if (null != _base_ucopy)
      {
        _base = new ArrayList(_base);
        _base_ucopy = null;
      }
      __arg.internalRefByClassifierRole(this);
      _base.add(__arg);
      logBagAdd(_base_addMethod, _base_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("base", old, getBases(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeBase(MClassifier __arg)
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
        old = getBases();
      }
      if (null != _base_ucopy)
      {
        _base = new ArrayList(_base);
        _base_ucopy = null;
      }
      if (!_base.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByClassifierRole(this);
      logBagRemove(_base_removeMethod, _base_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("base", old, getBases(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByBase(MClassifier __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getBases();
    }
    if (null != _base_ucopy)
    {
      _base = new ArrayList(_base);
      _base_ucopy = null;
    }
    _base.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("base", old, getBases(), __arg);
    }
  }
  public final void internalUnrefByBase(MClassifier __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getBases();
    }
    if (null != _base_ucopy)
    {
      _base = new ArrayList(_base);
      _base_ucopy = null;
    }
    _base.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("base", old, getBases(), __arg);
    }
  }
  protected void cleanup(Collection scheduledForRemove)
  {
    // opposite role: availableContents this role: classifierRole1
    if (_availableContents.size() != 0)
    {
      setAvailableContentses(Collections.EMPTY_LIST);
    }
    // opposite role: message1 this role: receiver
    if (_message1.size() != 0)
    {
      setMessages1(Collections.EMPTY_LIST);
    }
    // opposite role: message2 this role: sender
    if (_message2.size() != 0)
    {
      setMessages2(Collections.EMPTY_LIST);
    }
    // opposite role: availableFeature this role: classifierRole
    if (_availableFeature.size() != 0)
    {
      setAvailableFeatures(Collections.EMPTY_LIST);
    }
    // opposite role: base this role: classifierRole
    if (_base.size() != 0)
    {
      setBases(Collections.EMPTY_LIST);
    }
    super.cleanup(scheduledForRemove);
  }

  public String getUMLClassName()
  {
    return "ClassifierRole";
  }

  // Reflective API

  public Object reflectiveGetValue(String feature)
  {
    if ("multiplicity".equals(feature))
    {
      return getMultiplicity();
    }
    if ("availableContents".equals(feature))
    {
      return getAvailableContentses();
    }
    if ("message1".equals(feature))
    {
      return getMessages1();
    }
    if ("message2".equals(feature))
    {
      return getMessages2();
    }
    if ("availableFeature".equals(feature))
    {
      return getAvailableFeatures();
    }
    if ("base".equals(feature))
    {
      return getBases();
    }

    return super.reflectiveGetValue(feature);
  }

  public void reflectiveSetValue(String feature, Object obj)
  {
    if ("multiplicity".equals(feature))
    {
      setMultiplicity((MMultiplicity)obj);
      return;
    }
    if ("availableContents".equals(feature))
    {
      setAvailableContentses((Collection)obj);
      return;
    }
    if ("message1".equals(feature))
    {
      setMessages1((Collection)obj);
      return;
    }
    if ("message2".equals(feature))
    {
      setMessages2((Collection)obj);
      return;
    }
    if ("availableFeature".equals(feature))
    {
      setAvailableFeatures((Collection)obj);
      return;
    }
    if ("base".equals(feature))
    {
      setBases((Collection)obj);
      return;
    }

    super.reflectiveSetValue(feature, obj);
  }

  public void reflectiveAddValue(String feature, Object obj)
  {
    if ("availableContents".equals(feature))
    {
      addAvailableContents((MModelElement)obj);
      return;
    }
    if ("message1".equals(feature))
    {
      addMessage1((MMessage)obj);
      return;
    }
    if ("message2".equals(feature))
    {
      addMessage2((MMessage)obj);
      return;
    }
    if ("availableFeature".equals(feature))
    {
      addAvailableFeature((MFeature)obj);
      return;
    }
    if ("base".equals(feature))
    {
      addBase((MClassifier)obj);
      return;
    }

    super.reflectiveAddValue(feature, obj);
  }

  public void reflectiveRemoveValue(String feature, Object obj)
  {
    if ("availableContents".equals(feature))
    {
      removeAvailableContents((MModelElement)obj);
      return;
    }
    if ("message1".equals(feature))
    {
      removeMessage1((MMessage)obj);
      return;
    }
    if ("message2".equals(feature))
    {
      removeMessage2((MMessage)obj);
      return;
    }
    if ("availableFeature".equals(feature))
    {
      removeAvailableFeature((MFeature)obj);
      return;
    }
    if ("base".equals(feature))
    {
      removeBase((MClassifier)obj);
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
