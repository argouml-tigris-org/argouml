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

public abstract class MBaseImpl implements MBase, java.io.Serializable
{
  // ------------ code for class Base -----------------
  // generating attributes
  // generating associations
  // opposite role: extension this role: baseElement
  private final static Method _extension_setMethod = getMethod1(MBaseImpl.class, "setExtensions", Collection.class);
  private final static Method _extension_addMethod = getMethod1(MBaseImpl.class, "addExtension", MExtension.class);
  private final static Method _extension_removeMethod = getMethod1(MBaseImpl.class, "removeExtension", MExtension.class);
  Collection _extension = Collections.EMPTY_LIST;
  Collection _extension_ucopy = Collections.EMPTY_LIST;
  public final Collection getExtensions()
  {
    checkExists();
    if (null == _extension_ucopy)
    {
      _extension_ucopy = ucopy(_extension);
    }
    return _extension_ucopy;
  }
  public final void setExtensions(Collection __arg)
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
        old = getExtensions();
      }
      _extension_ucopy = null;
      Collection __added = bagdiff(__arg,_extension);
      Collection __removed = bagdiff(_extension, __arg);
      Iterator iter1 = __removed.iterator();
      while (iter1.hasNext())
      {
        MExtension o = (MExtension)iter1.next();
        o.internalUnrefByBaseElement(this);
      }
      Iterator iter2 = __added.iterator();
      while (iter2.hasNext())
      {
        MExtension o = (MExtension)iter2.next();
        o.internalRefByBaseElement(this);
      }
      _extension = new ArrayList(__arg);
      if (logForUndo)
      {
        logBagSet(_extension_setMethod, old, getExtensions());
      }
      if (sendEvent)
      {
        fireBagSet("extension", old, getExtensions());
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void addExtension(MExtension __arg)
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
        old = getExtensions();
      }
      if (null != _extension_ucopy)
      {
        _extension = new ArrayList(_extension);
        _extension_ucopy = null;
      }
      __arg.internalRefByBaseElement(this);
      _extension.add(__arg);
      logBagAdd(_extension_addMethod, _extension_removeMethod, __arg);
      if (sendEvent)
      {
        fireBagAdd("extension", old, getExtensions(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void removeExtension(MExtension __arg)
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
        old = getExtensions();
      }
      if (null != _extension_ucopy)
      {
        _extension = new ArrayList(_extension);
        _extension_ucopy = null;
      }
      if (!_extension.remove(__arg))
      {
        throw new RuntimeException("removing not added object");
      }
      __arg.internalUnrefByBaseElement(this);
      logBagRemove(_extension_removeMethod, _extension_addMethod, __arg);
      if (sendEvent)
      {
        fireBagRemove("extension", old, getExtensions(), __arg);
      }
    }
    finally
    {
      operationFinished();
    }
  }
  public final void internalRefByExtension(MExtension __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getExtensions();
    }
    if (null != _extension_ucopy)
    {
      _extension = new ArrayList(_extension);
      _extension_ucopy = null;
    }
    _extension.add(__arg);
    if (sendEvent)
    {
      fireBagAdd("extension", old, getExtensions(), __arg);
    }
  }
  public final void internalUnrefByExtension(MExtension __arg)
  {
    final boolean sendEvent = needEvent();
    Collection old = null;
    if (sendEvent)
    {
      old = getExtensions();
    }
    if (null != _extension_ucopy)
    {
      _extension = new ArrayList(_extension);
      _extension_ucopy = null;
    }
    _extension.remove(__arg);
    if (sendEvent)
    {
      fireBagRemove("extension", old, getExtensions(), __arg);
    }
  }
  protected void cleanup(Collection scheduledForRemove)
  {
    // opposite role: extension this role: baseElement
    if (_extension.size() != 0)
    {
      setExtensions(Collections.EMPTY_LIST);
    }
  }

  public String getUMLClassName()
  {
    return "Base";
  }

  // Reflective API

  public Object reflectiveGetValue(String feature)
  {
    if ("extension".equals(feature))
    {
      return getExtensions();
    }

    throw new IllegalArgumentException();
  }

  public void reflectiveSetValue(String feature, Object obj)
  {
    if ("extension".equals(feature))
    {
      setExtensions((Collection)obj);
      return;
    }

    throw new IllegalArgumentException();
  }

  public void reflectiveAddValue(String feature, Object obj)
  {
    if ("extension".equals(feature))
    {
      addExtension((MExtension)obj);
      return;
    }

    throw new IllegalArgumentException();
  }

  public void reflectiveRemoveValue(String feature, Object obj)
  {
    if ("extension".equals(feature))
    {
      removeExtension((MExtension)obj);
      return;
    }

    throw new IllegalArgumentException();
  }

  public Object reflectiveGetValue(String feature, int pos)
  {

    throw new IllegalArgumentException();
  }

  public void reflectiveSetValue(String feature, int pos, Object obj)
  {

    throw new IllegalArgumentException();
  }

  public void reflectiveAddValue(String feature, int pos, Object obj)
  {

    throw new IllegalArgumentException();
  }

  public void reflectiveRemoveValue(String feature, int pos)
  {

    throw new IllegalArgumentException();
  }
  public Collection getModelElementContents()
  {
    Collection ret = new ArrayList();
    return ret;
  }
  public MFactory getFactory()
  {
    return MFactoryImpl.getFactory();
  }

  protected MBaseImpl()
  {
    logCreate();
  }

  // UUID support

  String _uuid = null;
  public String getUUID()
  {
    return _uuid;
  }

  public void setUUID(String uuid)
  {
    if (null != _uuid)
    {
      throw new IllegalArgumentException("UUID already assigned");
    }

    _uuid = uuid;
  }

  private boolean removed = false;
  /** check if object was removed */
  public boolean isRemoved()
  {
    return removed;
  }
  
  /** remove element from meta-model */
  public final void remove()
  {
    checkExists();
    ArrayList scheduledForRemove = new ArrayList();
    operationStarted();
    // first package of events (unreferencing othe objects)
    try
    {
      cleanup(scheduledForRemove);
    }
    finally
    {
      operationFinished();
    }
    // second package of events (destroying this object)
    internalRedoRemove();
    logRemove();
    // removing owned objects
    Iterator i = scheduledForRemove.iterator();
    while(i.hasNext())
    {
      MBase e = (MBase)i.next();
      e.remove();
    }
  }

  public void internalUndoRemove()
  {
    operationStarted();
    try
    {
      removed = false;
      fireRecovered();
    }
    finally
    {
      operationFinished();
    }
  }

  public void internalRedoRemove()
  {
    operationStarted();
    try
    {
      removed = true;
      fireRemoved();
    }
    finally
    {
      operationFinished();
    }
  }

  protected static Collection bagdiff(Collection a, Collection b)
  {
    ArrayList rc = new ArrayList(a);
    Iterator i = b.iterator();
    while(i.hasNext())
    {
      rc.remove(i.next());
    }
    return rc;
  }

  protected static Collection scopy(Collection col)
  {
    return new ArrayList(col);
  }

  protected static List scopy(List col)
  {
    return new ArrayList(col);
  }

  protected static Collection ucopy(Collection col)
  {
    return Collections.unmodifiableCollection(col);
  }

  protected static List ucopy(List col)
  {
    return Collections.unmodifiableList(col);
  }


  protected final void logAttrSet(Method setter, int vold, int vnew) 
  {
    if(MFactoryImpl.undoManager != null)
    {
      logAttrSet(setter, new Integer(vold), new Integer(vnew));
    }
  }
  protected final void logAttrSet(Method setter, boolean vold, boolean vnew)
  {
    if(MFactoryImpl.undoManager != null)
    {
      logAttrSet(setter, new Boolean(vold), new Boolean(vnew));
    }
  }
  protected final void logAttrSet(Method setter, Object vold, Object vnew)
  {
    if(MFactoryImpl.undoManager != null)
    {
      MFactoryImpl.enlistUndo(this, setter, new Object[]{vold}, setter, new Object[]{vnew});
    }
  }
  
  protected final void logRefSet(Method setter, Object vold, Object vnew)
  {
    if(MFactoryImpl.undoManager != null)
    {
      MFactoryImpl.enlistUndo(this, setter, new Object[]{vold}, setter, new Object[]{vnew});
    }
  }
  protected final void logBagSet(Method setter, Collection vold, Collection vnew)
  {
    if(MFactoryImpl.undoManager != null)
    {
      MFactoryImpl.enlistUndo(this, setter, new Object[]{vold}, setter, new Object[]{vnew});
    }
  }
  protected final void logBagAdd(Method adder, Method remover, Object vobj)
  {
    if(MFactoryImpl.undoManager != null)
    {
      MFactoryImpl.enlistUndo(this, remover, new Object[]{vobj}, adder, new Object[]{vobj});
    }
  }
  protected final void logBagRemove(Method remover, Method adder, Object vobj)
  {
    if(MFactoryImpl.undoManager != null)
    {
      MFactoryImpl.enlistUndo(this, adder, new Object[]{vobj}, remover, new Object[]{vobj});
    }
  }
  protected final void logListSet(Method setter, Object vold, Object vnew, int pos)
  {
    if(MFactoryImpl.undoManager != null)
    {
      MFactoryImpl.enlistUndo(this, setter, new Object[]{new Integer(pos), vold}, setter, new Object[]{new Integer(pos), vnew});
    }
  }
  protected final void logListAdd(Method adder, Method remover, Object vobj, int pos)
  {
    if(MFactoryImpl.undoManager != null)
    {
      MFactoryImpl.enlistUndo(this, remover, new Object[]{new Integer(pos)}, adder, new Object[]{new Integer(pos),vobj});
    }
  }
  protected final void logListRemove(Method remover, Method adder, Object vobj, int pos)
  {
    if(MFactoryImpl.undoManager != null)
    {
      MFactoryImpl.enlistUndo(this, adder, new Object[]{new Integer(pos),vobj}, remover, new Object[]{new Integer(pos)});
    }
  }

  private static Method remove_method = getMethod0(MBaseImpl.class, "internalRedoRemove");
  private static Method recover_method = getMethod0(MBaseImpl.class, "internalUndoRemove");
  private static final Object OBJ0[] = new Object[0];
  protected final void logRemove()
  {
    if(MFactoryImpl.undoManager != null)
    {
      MFactoryImpl.enlistUndo(this, recover_method, OBJ0, remove_method, OBJ0);
    }
  }
  protected final void logCreate()
  {
    if(MFactoryImpl.undoManager != null)
    {
      MFactoryImpl.enlistUndo(this, remove_method, OBJ0, recover_method, OBJ0);
    }
  }
  
  // event support
  ArrayList listeners = new ArrayList();
  List listeners_ucopy = null;

  /** add listener to meta-model element */
  public synchronized void addMElementListener(MElementListener l)
  {
    if(listeners_ucopy != null)
    {
      listeners_ucopy = null;
      listeners = new ArrayList(listeners);
    }
    listeners.add(l);
  }
  
  /** remove listener from meta-model element */
  public synchronized void removeMElementListener(MElementListener l)
  {
    if(listeners_ucopy != null)
    {
      listeners_ucopy = null;
      listeners = new ArrayList(listeners);
    }
    listeners.remove(l);
  }

  synchronized List getMElementListeners()
  {
    if (null == listeners_ucopy)
    {
      listeners_ucopy = ucopy(listeners);
    }

    return listeners_ucopy;
  }

  public final boolean needEvent()
  {
    return listeners.size()>0;
  }

  public final boolean needUndo()
  {
    return (MFactoryImpl.undoManager != null);
  }

  protected final void fireAttrSet(String attr, int vold, int vnew) 
  {
    //System.out.println("firing ...");
    if (MFactoryImpl.event_policy != MFactoryImpl.EVENT_POLICY_DISABLED)
    {
      fireAttrSet(attr, new Integer(vold), new Integer(vnew));
    }
  }
  protected final void fireAttrSet(String attr, boolean vold, boolean vnew)
  {
    //System.out.println("firing ...");
    if (MFactoryImpl.event_policy != MFactoryImpl.EVENT_POLICY_DISABLED)
    {
      fireAttrSet(attr, new Boolean(vold), new Boolean(vnew));
    }
  }
  
  protected final void fireAttrSet(String attr, Object vold, Object vnew)
  {
    //System.out.println("firing ...");
    if (MFactoryImpl.event_policy != MFactoryImpl.EVENT_POLICY_DISABLED)
    {
      fireEvent(new MElementEvent(this, attr, MElementEvent.ATTRIBUTE_SET, vold, vnew));
    }
  }

  protected final void fireRefSet(String role, Object vold, Object vnew)
  {
    //System.out.println("firing ...");
    if (MFactoryImpl.event_policy != MFactoryImpl.EVENT_POLICY_DISABLED)
    {
      fireEvent(new MElementEvent(this, role, MElementEvent.REFERENCE_SET, vold, vnew));
    }
  }

  protected final void fireBagSet(String role, Collection vold, Collection vnew)
  {
    //System.out.println("firing ...");
    if (MFactoryImpl.event_policy != MFactoryImpl.EVENT_POLICY_DISABLED)
    {
      fireEvent(new MElementEvent(this, role, MElementEvent.BAG_ROLE_SET, vold, vnew));
    }
  }
  protected final void fireBagAdd(String role, Collection vold, Collection vnew, Object obj)
  {
    //System.out.println("firing ...");
    if (MFactoryImpl.event_policy != MFactoryImpl.EVENT_POLICY_DISABLED)
    {
      fireEvent(new MElementEvent(this, role, MElementEvent.BAG_ROLE_ADDED, vold, vnew, obj, null, -1));
    }
  }
  protected final void fireBagRemove(String role, Collection vold, Collection vnew, Object obj)
  {
    //System.out.println("firing ...");
    if (MFactoryImpl.event_policy != MFactoryImpl.EVENT_POLICY_DISABLED)
    {
      fireEvent(new MElementEvent(this, role, MElementEvent.BAG_ROLE_REMOVED, vold, vnew, null, obj, -1));
    }
  }

  protected final void fireListSet(String role, List vold, List vnew)
  {
    //System.out.println("firing ...");
    if (MFactoryImpl.event_policy != MFactoryImpl.EVENT_POLICY_DISABLED)
    {
      fireEvent(new MElementEvent(this, role, MElementEvent.LIST_ROLE_SET, vold, vnew));
    }
  }
  
  protected final void fireListAdd(String role, List vold, List vnew, Object obj, int pos)
  {
    //System.out.println("firing ...");
    if (MFactoryImpl.event_policy != MFactoryImpl.EVENT_POLICY_DISABLED)
    {
      fireEvent(new MElementEvent(this, role, MElementEvent.LIST_ROLE_ADDED, vold, vnew, obj, null, pos));
    }
  }
  

  protected final void fireListRemove(String role, List vold, List vnew, Object obj, int pos)
  {
    //System.out.println("firing ...");
    if (MFactoryImpl.event_policy != MFactoryImpl.EVENT_POLICY_DISABLED)
    {
      fireEvent(new MElementEvent(this, role, MElementEvent.LIST_ROLE_REMOVED, vold, vnew, null, obj, pos));
    }
  }
  

  protected final void fireListItemSet(String role, List vold, List vnew, Object iold, Object inew, int pos)
  {
    //System.out.println("firing ...");
    if (MFactoryImpl.event_policy != MFactoryImpl.EVENT_POLICY_DISABLED)
    {
      fireEvent(new MElementEvent(this, role, MElementEvent.LIST_ROLE_ITEM_SET, vold, vnew, inew, iold, pos));
    }
  }
  
  /** element was removed from meta-model */
  private void fireRemoved()
  {
    //System.out.println("firing ...");
    if (MFactoryImpl.event_policy != MFactoryImpl.EVENT_POLICY_DISABLED)
    {
      fireEvent(new MElementEvent(this, MElementEvent.ELEMENT_REMOVED));
    }
  }
  private void fireRecovered()
  {
    //System.out.println("firing ...");
    if (MFactoryImpl.event_policy != MFactoryImpl.EVENT_POLICY_DISABLED)
    {
      fireEvent(new MElementEvent(this, MElementEvent.ELEMENT_RECOVERED));
    }
  }

  private static void fireEvent(MElementEvent evt)
  {
    MFactoryImpl.scheduleEvent(evt);
  }

  /** helper function */
  protected static void operationStarted() 
  {
    MFactoryImpl.operationStarted();
  }
  /** helper function */
  protected static void operationFinished()
  {
    MFactoryImpl.operationFinished();
  }

  /** helper function */
  protected final void checkExists()
  {
  }
  
  private static Method getMethod(Class c, String name, Class params[])
  {
    try
    {
      return c.getDeclaredMethod(name, params);
    }
    catch(Exception ex)
    {
      System.err.println("initalization of "+c.getName()+" failes ("+name+")");
      return null;
    }
  }
  
  protected static Method getMethod0(Class c, String name)
  {
    return getMethod(c, name, new Class[0]);
  }
  
  /** helper function */
  protected static Method getMethod1(Class c, String name, Class param)
  {
    return getMethod(c, name, new Class[]{param});
  }
  /** helper function */
  protected static Method getMethod2(Class c, String name, Class param1, Class param2)
  {
    return getMethod(c, name, new Class[]{param1, param2});
  }

  MModelElement modelElementContainer = null;
  String modelElementContainerRole = null;
  public MModelElement getModelElementContainer()
  {
    return modelElementContainer;
  }

  protected final void setModelElementContainer(MModelElement arg, String role)
  {
    if (null == arg)
    {
      modelElementContainer = null;
      modelElementContainerRole = null;
 
      return;
    }

    if ((null != modelElementContainerRole) && !modelElementContainerRole.equals(role))
    {
      new RuntimeException("Attempt to set more than one owner for model element.").printStackTrace(System.err);
    }

    modelElementContainer = arg;
    modelElementContainerRole = role;
  }
}
