// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ResolvedCritic;
import org.argouml.cognitive.ToDoItem;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectMember;
import org.argouml.ocl.OCLExpander;
import org.argouml.persistence.ResolvedCriticXMLHelper;
import org.argouml.persistence.SaveException;
import org.argouml.persistence.ToDoItemXMLHelper;
import org.tigris.gef.ocl.ExpansionException;
import org.tigris.gef.ocl.TemplateReader;


/**
 * Helper class to act as a project member on behalf of the todo list.
 * It helps the todo list get loaded and saved together with the rest
 * of the project.
 *
 * @author	Michael Stockman
 */
public class ProjectMemberTodoList extends ProjectMember {
    
    private static final Logger LOG =
        Logger.getLogger(ProjectMemberTodoList.class);

    private static final String TO_DO_TEE = "/org/argouml/persistence/todo.tee";
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

    /**
     * @see org.argouml.kernel.ProjectMember#getType()
     */
    public String getType() {
        return "todo";
    }

    /**
     * @see org.argouml.kernel.ProjectMember#getFileExtension()
     */
    public String getFileExtension() {
        return TO_DO_EXT;
    }

    /**
     * @return a vector containing the to do list
     */
    public Vector getToDoList() {
        Vector in, out;
        ToDoItem tdi;
        Designer dsgr;
        int i;
        
        dsgr = Designer.theDesigner();
        in = dsgr.getToDoList().getToDoItems();
        out = new Vector();
        for (i = 0; i < in.size(); i++) {
            try {
            	tdi = (ToDoItem) in.elementAt(i);
            	if (tdi == null) {
                    continue;
                }
            } catch (ClassCastException e) {
                continue;
            }
        
            if (tdi.getPoster() instanceof Designer) {
                out.addElement(new ToDoItemXMLHelper(tdi));
            }
        }
        return out;
    }

    /**
     * @return Vector conaining the resolved critics list
     */
    public Vector getResolvedCriticsList() {
    	Vector in, out;
    	ResolvedCritic rci;
    	Designer dsgr;
    	int i;
    
    	dsgr = Designer.theDesigner();
    	in = dsgr.getToDoList().getResolvedItems();
    	out = new Vector();
    	for (i = 0; i < in.size(); i++) {
    	    try {
        	rci = (ResolvedCritic) in.elementAt(i);
        	if (rci == null) {
        		    continue;
                }
    	    }
    	    catch (ClassCastException e) {
        		continue;
    	    }
    	    out.addElement(new ResolvedCriticXMLHelper(rci));
    	}
    	return out;
    }

//    /**
//     * @param is an InputStream
//     * @throws OpenException on any error
//     */
//    public void load(InputStream is) throws OpenException {
//        try {
//            TodoParser parser = new TodoParser();
//            parser.readTodoList(is, true);
//        } catch (SAXException e) {
//            throw new OpenException(e);
//        }
//    }

    /**
     * 
     * Throws InvalidArgumentException if no writer specified.
     *
     * @see org.argouml.kernel.ProjectMember#save(java.io.Writer, 
     * java.lang.Integer)
     */
    public void save(Writer writer, Integer indent) throws SaveException {
        LOG.info("Saving todo list");

        if (writer == null) {
            throw new IllegalArgumentException(
                    "No writer specified to save todo list");
        }
        
        OCLExpander expander;
        try {
            expander = new OCLExpander(TemplateReader.getInstance()
                    .read(TO_DO_TEE));
        } catch (FileNotFoundException e) {
            throw new SaveException(e);
        }
        
        if (indent == null) {
            try {
                expander.expand(writer, this);
            } catch (ExpansionException e) {
                throw new SaveException(e);
            }
        } else {
            try {
                File tempFile = File.createTempFile("todo", null);
                tempFile.deleteOnExit();
                FileWriter w = new FileWriter(tempFile);
                expander.expand(w, this);
                w.close();
                addXmlFileToWriter(
                        (PrintWriter) writer,
                        tempFile,
                        indent.intValue());
            } catch (ExpansionException e) {
                throw new SaveException(e);
            } catch (IOException e) {
                throw new SaveException(e);
            }
        }
        
        LOG.debug("Done saving TO DO LIST!!!");
    }
}

