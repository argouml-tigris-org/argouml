// $Id$
// Copyright (c) 2004-2007 The Regents of the University of California. All
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
import org.argouml.uml.profile.ProfileConfiguration;
import org.tigris.gef.base.Diagram;

/**
 * List of ProjectMembers. <p>
 * 
 * The project members are grouped into 3 categories: 
 * model, diagrams and the todo item list. <p>
 *
 * The purpose of these categories is to make sure that members are read 
 * and written in the correct order. The model must be read before diagrams, 
 * diagrams must be read before todo items. <p>
 *
 * This implementation supports only 1 model member, 
 * multiple diagram members, and one todo list member.
 * 
 * @author Bob Tarling
 */
public class MemberList implements List {

    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(MemberList.class);

    private AbstractProjectMember model;

    private List<ProjectMemberDiagram> diagramMembers = 
        new ArrayList<ProjectMemberDiagram>(10);

    private AbstractProjectMember todoList;
    private AbstractProjectMember profileConfiguration;

    /**
     * The constructor.
     */
    public MemberList() {
        LOG.info("Creating a member list");
    }

    public synchronized boolean add(Object member) {

        if (member instanceof ProjectMemberModel) {
            // Always put the model at the top
            model = (AbstractProjectMember) member;
            return true;
        } else if (member instanceof ProjectMemberTodoList) {
            // otherwise add the diagram at the start
            setTodoList((AbstractProjectMember) member);
            return true;
        } else if (member instanceof ProfileConfiguration) {
            setProfileConfiguration((AbstractProjectMember) member);
            return true;
        } else if (member instanceof ProjectMemberDiagram) {
            // otherwise add the diagram at the start
            return diagramMembers.add((ProjectMemberDiagram) member);
        }
        return false;
    }

    public synchronized boolean remove(Object member) {
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
        } else if (profileConfiguration == member) {
            LOG.info("Removing profile configuration");
            setProfileConfiguration(null);
            return true;
        } else {
            final boolean removed = diagramMembers.remove(member);
            if (!removed) {
                LOG.warn("Failed to remove diagram member " + member);
            }
            return removed;
        }
    }

    public synchronized Iterator iterator() {
        return buildTempList().iterator();
    }

    public synchronized ListIterator listIterator() {
        return buildTempList().listIterator();
    }

    public synchronized ListIterator listIterator(int arg0) {
        return buildTempList().listIterator(arg0);
    }

    private List<ProjectMember> buildTempList() {
        List<ProjectMember> temp = 
            new ArrayList<ProjectMember>(size());
        if (model != null) {
            temp.add(model);
        }
        temp.addAll(diagramMembers);
        if (todoList != null) {
            temp.add(todoList);
        }
        if (profileConfiguration != null) {
            temp.add(profileConfiguration);
        }
        return temp;
    }
    
    private boolean removeDiagram(Diagram d) {
        for (ProjectMemberDiagram pmd : diagramMembers) {
            if (pmd.getDiagram() == d) {
                pmd.remove();
                diagramMembers.remove(pmd);
                return true;
            }
        }
        LOG.warn("Failed to remove diagram " + d);
        return false;
    }

    public synchronized int size() {
        int size = diagramMembers.size();
        if (model != null) {
            ++size;
        }
        if (todoList != null) {
            ++size;
        }
        if (profileConfiguration != null) {
            ++size;
        }
        return size;
    }

    public synchronized boolean contains(Object member) {
        if (todoList == member) {
            return true;
        }
        if (model == member) {
            return true;
        }
        if (profileConfiguration == member) {
            return true;
        }
        return diagramMembers.contains(member);
    }

    public synchronized void clear() {
        LOG.info("Clearing members");
        if (model != null) {
            model.remove();
        }
        if (todoList != null) {
            todoList.remove();
        }
        if (profileConfiguration != null) {
            profileConfiguration.remove();
        }
        Iterator membersIt = diagramMembers.iterator();
        while (membersIt.hasNext()) {
            ((AbstractProjectMember) membersIt.next()).remove();
        }
        diagramMembers.clear();
    }

    /**
     * @param type the type of the member
     * @return the member of the project
     */
    public synchronized ProjectMember getMember(Class type) {
        if (type == ProjectMemberModel.class) {
            return model;
        }
        if (type == ProjectMemberTodoList.class) {
            return todoList;
        }
        if (type == ProfileConfiguration.class) {
            return profileConfiguration;
        }
        throw new IllegalArgumentException(
            "There is no single instance of a " + type.getName() + " member");
    }

    /**
     * @param type the type of the member
     * @return the member of the project
     */
    public synchronized List getMembers(Class type) {
        if (type == ProjectMemberModel.class) {
            List<ProjectMember> temp = new ArrayList<ProjectMember>(1);
            temp.add(model);
            return temp;
        }
        if (type == ProjectMemberTodoList.class) {
            List<ProjectMember> temp = new ArrayList<ProjectMember>(1);
            temp.add(todoList);
            return temp;
        }
        if (type == ProjectMemberDiagram.class) {
            return diagramMembers;
        }
        if (type == ProfileConfiguration.class) {
            List<ProjectMember> temp = new ArrayList<ProjectMember>(1);
            temp.add(profileConfiguration);
            return temp;
        }
        throw new IllegalArgumentException(
            "There is no single instance of a " + type.getName() + " member");
    }

    public synchronized Object get(int i) {
        if (model != null) {
            if (i == 0) {
                return model;
            }
            --i;
        }

        if (i == diagramMembers.size()) {
            return todoList;
        }
        if (i == (diagramMembers.size() + 1)) {
            return profileConfiguration;
        }

        return diagramMembers.get(i);
    }

    public synchronized boolean isEmpty() {
        return size() == 0;
    }

    public synchronized Object[] toArray() {
        Object[] temp = new Object[size()];
        int pos = 0;
        if (model != null) {
            temp[pos++] = model;
        }
        for (ProjectMemberDiagram d : diagramMembers) {
            temp[pos++] = d;
        }
        if (todoList != null) {
            temp[pos++] = todoList;
        }
        if (profileConfiguration != null) {
            temp[pos++] = profileConfiguration;
        }
        return temp;
    }

    private void setTodoList(AbstractProjectMember member) {
        LOG.info("Setting todoList to " + member);
        todoList = member;
    }

    public Object[] toArray(Object[] arg0) {
        throw new UnsupportedOperationException();
    }

    public boolean containsAll(Collection arg0) {
        throw new UnsupportedOperationException();
    }

    public boolean addAll(Collection arg0) {
        throw new UnsupportedOperationException();
    }

    public boolean addAll(int arg0, Collection arg1) {
        throw new UnsupportedOperationException();
    }

    public boolean removeAll(Collection arg0) {
        throw new UnsupportedOperationException();
    }

    public boolean retainAll(Collection arg0) {
        throw new UnsupportedOperationException();
    }

    public Object set(int arg0, Object arg1) {
        throw new UnsupportedOperationException();
    }

    public void add(int arg0, Object arg1) {
        throw new UnsupportedOperationException();
    }

    public Object remove(int arg0) {
        throw new UnsupportedOperationException();
    }

    public int indexOf(Object arg0) {
        throw new UnsupportedOperationException();
    }

    public int lastIndexOf(Object arg0) {
        throw new UnsupportedOperationException();
    }

    public List subList(int arg0, int arg1) {
        throw new UnsupportedOperationException();
    }

    public AbstractProjectMember getProfileConfiguration() {
        return profileConfiguration;
    }

    public void setProfileConfiguration(AbstractProjectMember profileConfig) {
        profileConfiguration = profileConfig;
    }
}
