// Copyright (c) 1996-99 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

// File: DisplayTextTree.java
// Classes: DisplayTextTree
// Original Author:
// $Id$

// 26 Apr 2002: Jeremy Bennett (mail@jeremybennett.com). Patch to give a better
// naming for extension points in convertValueToText.


package org.argouml.ui;

import java.util.*;
import java.beans.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import javax.swing.plaf.basic.*;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.*;
import ru.novosoft.uml.behavior.state_machines.*;
import ru.novosoft.uml.behavior.use_cases.*;
import ru.novosoft.uml.foundation.extension_mechanisms.MTaggedValue;
import ru.novosoft.uml.foundation.extension_mechanisms.MStereotype;

import org.tigris.gef.base.*;

import org.apache.log4j.Category;
import org.argouml.application.api.Configuration;
import org.argouml.application.api.Notation;
import org.argouml.kernel.*;
import org.argouml.model.uml.UmlModelEventPump;
import org.argouml.cognitive.*;
import org.argouml.uml.generator.*;
import org.argouml.uml.ui.*;

public class DisplayTextTree extends JTree
implements MElementListener {
    
    protected static Category cat = Category.getInstance(DisplayTextTree.class);

  Hashtable _expandedPathsInModel = new Hashtable();
  boolean _reexpanding = false;

  public DisplayTextTree() {
    setCellRenderer(new UMLTreeCellRenderer());
    putClientProperty("JTree.lineStyle", "Angled");
    //setEditable(true);
  }

  public String convertValueToText(Object value, boolean selected,
				   boolean expanded, boolean leaf, int row,
				   boolean hasFocus) {
    if (value == null) return "(null)";
    if (value instanceof ToDoItem) {
      return ((ToDoItem)value).getHeadline();
    }
    if (value instanceof ToDoList) {
	return "ToDoList";
    }
    if (value instanceof MTaggedValue) {
        String tagName = ((MTaggedValue)value).getTag();
        if (tagName == null || tagName.equals("")) tagName = "(anon)";
        return("1-" + tagName);
    }
    if ((value instanceof MElement)&&(!(value instanceof MTaggedValue))) {
      // original
      MElement e = (MElement) value;
      String ocl = "";
      if (e instanceof MModelElement)
	ocl = ((MModelElement)e).getUMLClassName();
        String name = ((MModelElement)e).getName();
      if (e instanceof MTransition) {
		  name = GeneratorDisplay.Generate((MTransition)e);
      }
      if (e instanceof MExtensionPoint) {  // Jeremy Bennett patch
          name = GeneratorDisplay.Generate((MExtensionPoint) e);
      }
      if (name == null || name.equals("")) name = "(anon " + ocl + ")";

      // Look for stereotype
      if (Configuration.getBoolean(Notation.KEY_SHOW_STEREOTYPES, false)) {
          if (e instanceof MModelElement) {
              MStereotype st = ((MModelElement)e).getStereotype();
	      if (st != null) {
		  name += " " + GeneratorDisplay.Generate(st);
	      }
          }
      }
      return name;
    }
    if (value instanceof Diagram) {
      return ((Diagram)value).getName();
    }
    return value.toString();
  }


  protected Vector getExpandedPaths() {
    TreeModel tm = getModel();
    Vector res = (Vector) _expandedPathsInModel.get(tm);
    if (res == null) {
      res = new Vector();
      _expandedPathsInModel.put(tm, res);
    }
    return res;
  }

  /**
   * Tree MModel Expansion notification.
   *
   * @param e  a Tree node insertion event
   */
  public void fireTreeExpanded(TreePath path) {
    super.fireTreeExpanded(path);
    if (_reexpanding) return;
    if (path == null || _expandedPathsInModel == null) return;
    Vector expanded = getExpandedPaths();
    expanded.removeElement(path);
    expanded.addElement(path);
    addListenerToPath(path);
  }

  protected void addListenerToPath(TreePath path) {
    Object node = path.getLastPathComponent();
    addListenerToNode(node);
  }

  protected void addListenerToNode(Object node) {
    if (node instanceof MBase) {
        // UmlModelEventPump.getPump().removeModelEventListener(this, ((MBase)node));
        UmlModelEventPump.getPump().addModelEventListener(this, ((MBase)node)); 
    }
    TreeModel tm = getModel();
    int childCount = tm.getChildCount(node);
    for (int i = 0; i < childCount; i++) {
      Object child = tm.getChild(node, i);
      if (child instanceof MBase) {
            UmlModelEventPump.getPump().removeModelEventListener(this, ((MBase)child));
            UmlModelEventPump.getPump().addModelEventListener(this, ((MBase)child));
       
      }
    }
  }

  public void fireTreeCollapsed(TreePath path) {
    super.fireTreeCollapsed(path);
    if (path == null || _expandedPathsInModel == null) return;
    Vector expanded = getExpandedPaths();
    expanded.removeElement(path);
  }


  public void setModel(TreeModel newModel) {
    super.setModel(newModel);
    Object r = newModel.getRoot();
    if (r instanceof MBase) {
      // UmlModelEventPump.getPump().removeModelEventListener(this, (MBase)r);
      UmlModelEventPump.getPump().addModelEventListener(this, (MBase)r);
    }

    int childCount = newModel.getChildCount(r);
    for (int i = 0; i < childCount; i++) {
      Object child = newModel.getChild(r, i);
      if (child instanceof MBase) {
	// UmlModelEventPump.getPump().removeModelEventListener(this, (MBase)child);
        UmlModelEventPump.getPump().addModelEventListener(this, (MBase)child);
      }
    }
    reexpand();
  }

	




  public static final int DEPTH_LIMIT = 10;
  public static final int CHANGE = 1;
  public static final int ADD = 2;
  public static final int REMOVE = 3;
  //public static Object path[] = new Object[DEPTH_LIMIT];

  public void forceUpdate() {
    Object rootArray[] = new Object[1];
    rootArray[0] = getModel().getRoot();
    Object noChildren[] = null;
    int noIndexes[] = null;
    TreeModelEvent tme = new TreeModelEvent(this, new TreePath(rootArray));
    treeModelListener.treeStructureChanged(tme);
    TreeModel tm = getModel();
    if (tm instanceof NavPerspective) {
      NavPerspective np = (NavPerspective) tm;
      np.fireTreeStructureChanged(this, rootArray, noIndexes, noChildren);
    }
    reexpand();
  }

//   public void forceUpdate_old() {
//     int n = 0;
//     ProjectBrowser pb = ProjectBrowser.TheInstance;
//     Vector pers = pb.getNavPane().getPerspectives();
//     NavPerspective curPerspective = pb.getNavPane().getCurPerspective();
//     if (curPerspective == null) return;
//     n = (pers.indexOf(curPerspective) + 1) % pers.size();
//     NavPerspective otherPerspective = (NavPerspective) pers.elementAt(n);
//     pb.getNavPane().setCurPerspective(otherPerspective);
//     pb.getNavPane().setCurPerspective(curPerspective);
//   }

  public void reexpand() {
    if (_expandedPathsInModel == null) return;
    _reexpanding = true;
    Object[] path2 = new Object[1];
    path2[0] = getModel().getRoot();
    TreeModelEvent tme = new TreeModelEvent(this, path2, null, null);
    treeModelListener.treeStructureChanged(tme);
    treeDidChange();

    java.util.Enumeration enum = getExpandedPaths().elements();
    while (enum.hasMoreElements()) {
      TreePath path = (TreePath) enum.nextElement();
      tme = new TreeModelEvent(this, path, null, null);
      treeModelListener.treeStructureChanged(tme);
      expandPath(path);
      // addListenerToPath(path);
    }
    _reexpanding = false;

  }

	public void propertySet(MElementEvent mee) {
	}
	public void listRoleItemSet(MElementEvent mee) {
	}
	public void recovered(MElementEvent mee) {
	}
	public void removed(MElementEvent mee) {
	}
	public void roleAdded(MElementEvent mee) {
	}
	public void roleRemoved(MElementEvent mee) {
	}


} /* end class DisplayTextTree */
