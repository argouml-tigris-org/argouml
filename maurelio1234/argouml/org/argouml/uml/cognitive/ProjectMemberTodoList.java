// $Id: ProjectMemberTodoList.java 12950 2007-07-01 08:10:04Z tfmorris $
// Copyright (c) 1996-2006 The Regents of the University of California. All
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

package org.argouml.uml.cognitive;

import java.util.Vector;

import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ResolvedCritic;
import org.argouml.cognitive.ToDoItem;
import org.argouml.kernel.AbstractProjectMember;
import org.argouml.kernel.Project;
import org.argouml.persistence.ResolvedCriticXMLHelper;
import org.argouml.persistence.ToDoItemXMLHelper;

/**
 * Helper class to act as a project member on behalf of the todo list.
 * It helps the todo list get loaded and saved together with the rest
 * of the project.
 *
 * @author	Michael Stockman
 */
public class ProjectMemberTodoList extends AbstractProjectMember {

    private static final String TO_DO_EXT = ".todo";

    /**
     * The constructor.
     *
     * @param name the name
     * @param p the project
     */
    public ProjectMemberTodoList(String name, Project p) {
    	super(name, p);
    }

    /*
     * @see org.argouml.kernel.AbstractProjectMember#getType()
     */
    public String getType() {
        return "todo";
    }

    /*
     * @see org.argouml.kernel.AbstractProjectMember#getZipFileExtension()
     */
    @Override
    public String getZipFileExtension() {
        return TO_DO_EXT;
    }

    /**
     * @return a vector containing the to do list
     * Used by todo.tee
     */
    public Vector<ToDoItemXMLHelper> getToDoList() {
        Vector<ToDoItemXMLHelper> out = new Vector<ToDoItemXMLHelper>();
        Designer dsgr = Designer.theDesigner();
        for (ToDoItem tdi : dsgr.getToDoList().getToDoItemList()) {
            if (tdi != null && tdi.getPoster() instanceof Designer) {
                out.addElement(new ToDoItemXMLHelper(tdi));
            }
        }
        return out;
    }

    /**
     * @return Vector conaining the resolved critics list
     * Used by todo.tee
     */
    public Vector<ResolvedCriticXMLHelper> getResolvedCriticsList() {
        Vector<ResolvedCriticXMLHelper> out = 
            new Vector<ResolvedCriticXMLHelper>();
    	Designer dsgr = Designer.theDesigner();
    	for (ResolvedCritic rci : dsgr.getToDoList().getResolvedItems()) {
    	    if (rci != null) {
                out.addElement(new ResolvedCriticXMLHelper(rci));
            }
    	}
    	return out;
    }

    /**
     * There is not yet any repair task for the ToDo model but this is open to
     * implement as and when any problems areas are discovered.
     * 
     * {@inheritDoc}
     */
    public String repair() {
        return "";
    }

}
