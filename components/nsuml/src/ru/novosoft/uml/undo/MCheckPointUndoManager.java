/* NovoSoft UML API for Java. Version 0.4.19
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
package ru.novosoft.uml.undo;

import ru.novosoft.uml.*;

//import com.sun.java.util.collections.*;
import java.util.*;

import java.util.TooManyListenersException;

/** This is a sample undo mamanager that implement checkpoint undo ideology.
  */

public class MCheckPointUndoManager
{
  static class MUndoManagerProxy implements MUndoManager
  {
    public void enlistUndo(MUndoableAction a)
    {
      if(undo_policy == UNDO_POLICY_ENABLED)
        MCheckPointUndoManager.enlistUndo(a);
    }
  }
  static final MUndoManagerProxy proxy = new MUndoManagerProxy();


  public static final int UNDO_POLICY_DISABLED = 0;
  public static final int UNDO_POLICY_ENABLED = 1;
  private static final int UNDO_POLICY_UNDOING = 2;
  private static final int UNDO_POLICY_REDOING = 3;
  static int undo_policy = UNDO_POLICY_DISABLED;
  public static void setUndoPolicy(int policy)
  { 
    if (undo_policy == UNDO_POLICY_DISABLED && policy == UNDO_POLICY_ENABLED)
    {
      try
      {
        MFactoryImpl.addUndoManager(proxy);
      }
      catch(TooManyListenersException ex)
      {
        throw new IllegalStateException("undo manager is already registered with factory");
      }
    }
    if (undo_policy == UNDO_POLICY_ENABLED && policy == UNDO_POLICY_DISABLED)
    {
      forget(getCheckPoint());
      MFactoryImpl.removeUndoManager(proxy);
    }
    undo_policy = policy;
  }
  public static int getUndoPolicy()
  { 
    return undo_policy;
  }

  static int currentCheckPointPosition;
  static boolean addingUndo; 
  static ArrayList undoStack = new ArrayList(1024);
  static
  {
    currentCheckPointPosition = 0;
    addingUndo = false;
    undoStack.add(new MCheckPoint(0));
  }

  /** get check point */
  public static MCheckPoint getCheckPoint()
  {
    addingUndo = false;
    if (undo_policy == UNDO_POLICY_DISABLED)
    {
      throw new IllegalStateException("undo support is disabled.");
    }
    
    MCheckPoint rc;
    if (currentCheckPointPosition == undoStack.size()-1)
    {
      rc = (MCheckPoint)undoStack.get(currentCheckPointPosition);
      //System.out.println("test got: "+currentCheckPointPosition);
    }
    else
    {
      currentCheckPointPosition = undoStack.size();
      rc = new MCheckPoint(currentCheckPointPosition);
      undoStack.add(rc);
      //System.out.println("test added: "+currentCheckPointPosition);
    }
    return rc;
  }

  /** undo changes so state is the same as were when the check point was got */
  public static void undo(MCheckPoint c)
  {
    if (undo_policy == UNDO_POLICY_DISABLED)
    {
      throw new IllegalStateException("undo support is disabled.");
    }
    addingUndo = false;
    setUndoPolicy(UNDO_POLICY_UNDOING);
    if (!c.canBeUndone())
    {
      throw new IllegalArgumentException();
    }
    for (int i = currentCheckPointPosition; i > c.stackPosition; i--)
    {
      Object o = undoStack.get(i);
      if (o instanceof MUndoableAction)
      {
        MUndoableAction u = (MUndoableAction)o;
        u.undo();
        //System.out.println("undoing "+i+" "+u);
      }
    }
    currentCheckPointPosition = c.stackPosition;
    setUndoPolicy(UNDO_POLICY_ENABLED);
  }

  /** undo changes so state is the same as were when the check point was got */
  public static void redo(MCheckPoint c)
  {
    addingUndo = false;
    if (undo_policy == UNDO_POLICY_DISABLED)
    {
      throw new IllegalStateException("undo support is disabled.");
    }
    setUndoPolicy(UNDO_POLICY_REDOING);
    if (!c.canBeRedone())
    {
      throw new IllegalArgumentException();
    }
    for (int i = currentCheckPointPosition+1; i < c.stackPosition; i++)
    {
      Object o = undoStack.get(i);
      if (o instanceof MUndoableAction)
      {
        MUndoableAction u = (MUndoableAction)o;
        u.redo();
        //System.out.println("redoing "+i+" "+u);
      }
    }
    currentCheckPointPosition = c.stackPosition;
    setUndoPolicy(UNDO_POLICY_ENABLED);
  }

  /** forget information about all changes done before this checkpoint */
  public static void forget(MCheckPoint c)
  {
    if (undo_policy == UNDO_POLICY_DISABLED)
    {
      throw new IllegalStateException("undo support is disabled.");
    }
    for (int i = currentCheckPointPosition-1; i >= 0; i--)
    {
      Object o = undoStack.get(i);
      if (o instanceof MCheckPoint)
      {
        MCheckPoint ic = (MCheckPoint)o;
        ic.valid = false;
      }
    }
    for (int i = currentCheckPointPosition-1; i >= 0; i--)
    {
      undoStack.remove(0);
    }
    currentCheckPointPosition = c.stackPosition;
  }
  

  static void enlistUndo(MUndoableAction u)
  {
    // clear redo stack
    if (!addingUndo && currentCheckPointPosition < undoStack.size()-1)
    {
      while (currentCheckPointPosition != undoStack.size()-1)
      {
        undoStack.remove(currentCheckPointPosition+1);
      }
    }
    undoStack.add(u);
    addingUndo = true;
  }

}
