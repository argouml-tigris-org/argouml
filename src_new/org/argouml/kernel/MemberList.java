// $Id$
// Copyright (c) 2004 The Regents of the University of California. All
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

package org.argouml.kernel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.apache.log4j.Logger;
import org.argouml.uml.ProjectMemberModel;
import org.argouml.uml.cognitive.ProjectMemberTodoList;
import org.argouml.uml.diagram.ProjectMemberDiagram;
import org.tigris.gef.base.Diagram;

/**
 * @author Bob Tarling
 */
public class MemberList implements List {
    
    /** logger */
    private static final Logger LOG = Logger.getLogger(MemberList.class);

    private AbstractProjectMember model;
    private ArrayList members = new ArrayList(10);
    private AbstractProjectMember todoList;

    /**
     * The constructor.
     */
    public MemberList() {
        LOG.info("Creating a member list");
    }
    
    /**
     * @see java.util.Collection#add(java.lang.Object)
     */
    public boolean add(Object member) {

        if (member instanceof ProjectMemberModel) {
            // Always put the model at the top
            model = (AbstractProjectMember) member;
            return true;
        } else if (member instanceof ProjectMemberTodoList) {
            // otherwise add the diagram at the start
            setTodoList((AbstractProjectMember) member);
            return true;
        } else if (member instanceof ProjectMemberDiagram) {
            // otherwise add the diagram at the start
            return members.add(member);
        }
        return false;
    }

    /**
     * @see java.util.Collection#remove(java.lang.Object)
     */
    public boolean remove(Object member) {
        LOG.info("Removing a member");
        if (member instanceof Diagram) {
            return removeDiagram((Diagram) member);
        }
        ((AbstractProjectMember) member).remove();
        if (model == member) {
            model = null;
            return true;
        } else if (todoList == member) {
            LOG.info("Removing todo list");
            setTodoList(null);
            return true;
        } else {
            return members.remove(member);
        }
    }
    
    /**
     * @see java.util.Collection#iterator()
     */
    public Iterator iterator() {
        ArrayList temp = new ArrayList(size());
        if (model != null) {
            temp.add(model);
        }
        temp.addAll(members);
        if (todoList != null) {
            temp.add(todoList);
        }
        return temp.iterator();
    }

    /**
     * @see java.util.List#listIterator()
     */
    public ListIterator listIterator() {
        ArrayList temp = new ArrayList(size());
        if (model != null) {
            temp.add(model);
        }
        temp.addAll(members);
        if (todoList != null) {
            temp.add(todoList);
        }
        return temp.listIterator();
    }

    /**
     * @see java.util.List#listIterator(int)
     */
    public ListIterator listIterator(int arg0) {
        ArrayList temp = new ArrayList(size());
        if (model != null) {
            temp.add(model);
        }
        temp.addAll(members);
        if (todoList != null) {
            temp.add(todoList);
        }
        return temp.listIterator(arg0);
    }
    
    private boolean removeDiagram(Diagram d) {
        Iterator it = members.iterator();
        while (it.hasNext()) {
            Object obj = it.next();
            ProjectMemberDiagram pmd = (ProjectMemberDiagram) obj;
            if (pmd.getDiagram() == d) {
                pmd.remove();
                members.remove(pmd);
                return true;
            }
        }
        return false;
    }

    
    
    /**
     * @see java.util.Collection#size()
     */
    public int size() {
        int size = members.size();
        if (model != null) ++size;
        if (todoList != null) ++size;
        return size;
    }
    
    /**
     * @param name the name of the member to be found
     * @return the member
     */
    public ProjectMember findDiagramMemberByName(String name) {
        for (int i = 0; i < members.size(); i++) {
            ProjectMember pm = (ProjectMember) members.get(i);
            if (name.equals(pm.getPlainName()))
                return pm;
        }
        return null;
    }
    
    /**
     * @see java.util.Collection#contains(java.lang.Object)
     */
    public boolean contains(Object member) {
        if (todoList == member) {
            return true;
        }
        if (model == member) {
            return true;
        }
        return members.contains(member);
    }
    
    /**
     * @see java.util.Collection#clear()
     */
    public void clear() {
        LOG.info("Clearing members");
        if (model != null) {
            model.remove();
        }
        if (todoList != null) {
            todoList.remove();
        }
        Iterator membersIt = members.iterator();
        while (membersIt.hasNext()) {
            ((AbstractProjectMember) membersIt.next()).remove();
        }
        members.clear();
    }
    
    /**
     * @param type the type of the member
     * @return the member of the project
     */
    public ProjectMember getMember(Class type) {
        if (type == ProjectMemberModel.class) {
            return model;
        }
        if (type == ProjectMemberTodoList.class) {
            return todoList;
        }
        throw new IllegalArgumentException(
            "There is no single instance of a " + type.getName() + " member");
    }
    
    /**
     * @see java.util.List#get(int)
     */
    public Object get(int i) {
        if (model != null) {
            if (i == 0) {
                return model;
            }
            --i;
        }
        
        if (i == members.size()) {
            return todoList;
        }
        
        return (ProjectMember) members.get(i);
    }

    /**
     * @see java.util.List#isEmpty()
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * @see java.util.List#toArray()
     */
    public Object[] toArray() {
        Object[] temp = new Object[size()];
        int pos = 0;
        if (model != null) {
            temp[pos++] = model;
        }
        for (int i = 0; i < members.size(); ++i) {
            temp[pos++] = members.get(i);
        }
        if (todoList != null) {
            temp[pos++] = todoList;
        }
        return temp;
    }

    private void setTodoList(AbstractProjectMember member) {
        LOG.info("Setting todoList to " + member);
        todoList = member;
    }
    
    /**
     * @see java.util.List#toArray(java.lang.Object[])
     */
    public Object[] toArray(Object[] arg0) {
        throw new UnsupportedOperationException();
    }

    /**
     * @see java.util.List#containsAll(java.util.Collection)
     */
    public boolean containsAll(Collection arg0) {
        throw new UnsupportedOperationException();
    }

    /**
     * @see java.util.List#addAll(java.util.Collection)
     */
    public boolean addAll(Collection arg0) {
        throw new UnsupportedOperationException();
    }

    /**
     * @see java.util.List#addAll(int, java.util.Collection)
     */
    public boolean addAll(int arg0, Collection arg1) {
        throw new UnsupportedOperationException();
    }

    /**
     * @see java.util.List#removeAll(java.util.Collection)
     */
    public boolean removeAll(Collection arg0) {
        throw new UnsupportedOperationException();
    }

    /**
     * @see java.util.List#retainAll(java.util.Collection)
     */
    public boolean retainAll(Collection arg0) {
        throw new UnsupportedOperationException();
    }

    /**
     * @see java.util.List#set(int, java.lang.Object)
     */
    public Object set(int arg0, Object arg1) {
        throw new UnsupportedOperationException();
    }

    /**
     * @see java.util.List#add(int, java.lang.Object)
     */
    public void add(int arg0, Object arg1) {
        throw new UnsupportedOperationException();
    }

    /**
     * @see java.util.List#remove(int)
     */
    public Object remove(int arg0) {
        throw new UnsupportedOperationException();
    }

    /**
     * @see java.util.List#indexOf(java.lang.Object)
     */
    public int indexOf(Object arg0) {
        throw new UnsupportedOperationException();
    }

    /**
     * @see java.util.List#lastIndexOf(java.lang.Object)
     */
    public int lastIndexOf(Object arg0) {
        throw new UnsupportedOperationException();
    }


    /**
     * @see java.util.List#subList(int, int)
     */
    public List subList(int arg0, int arg1) {
        throw new UnsupportedOperationException();
    }
}

