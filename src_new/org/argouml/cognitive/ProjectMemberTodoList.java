// Copyright (c) 1996-2002 The Regents of the University of California. All
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

package org.argouml.cognitive;

import java.io.InputStream;
import java.io.IOException;
import java.io.Writer;
import java.util.Hashtable;
import java.util.Vector;

import org.tigris.gef.ocl.TemplateReader;

import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ToDoItem;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectMember;
import org.argouml.ocl.OCLExpander;
import org.argouml.xml.todo.*;

/**
 * Helper class to act as a project member on behalf of the todo list.
 * It helps the todo list get loaded and saved together with the rest
 * of the project.
 *
 * @author	Michael Stockman
 */
public class ProjectMemberTodoList extends ProjectMember
{
	public static final String TODO_TEE = "/org/argouml/xml/dtd/todo.tee";
	public static final String TODO_EXT = ".todo";

	protected OCLExpander expander = null;

	public ProjectMemberTodoList(String name, Project p)
	{
		super(name, p);
	}

	public String getType()
	{
		return "todo";
	}

	public String getFileExtension()
	{
		return TODO_EXT;
	}

	public Vector getToDoList()
	{
		Vector in, out;
		ToDoItem tdi;
		Designer dsgr;
		int i;

		dsgr = Designer.theDesigner();
		in = dsgr.getToDoList().getToDoItems();
		out = new Vector();
		for (i = 0; i < in.size(); i++)
		{
			try
			{
				tdi = (ToDoItem) in.elementAt(i);
				if (tdi == null)
					continue;
			}
			catch (ClassCastException e)
			{
				continue;
			}

			if (tdi.getPoster() instanceof Designer)
				out.addElement(new ToDoItemXMLHelper(tdi));
		}
		return out;
	}

	public Vector getResolvedCriticsList()
	{
		Vector in, out;
		ResolvedCritic rci;
		Designer dsgr;
		int i;

		dsgr = Designer.theDesigner();
		in = dsgr.getToDoList().getResolvedItems();
		out = new Vector();
		for (i = 0; i < in.size(); i++)
		{
			try
			{
				rci = (ResolvedCritic) in.elementAt(i);
				if (rci == null)
					continue;
			}
			catch (ClassCastException e)
			{
				continue;
			}
			out.addElement(new ResolvedCriticXMLHelper(rci));
		}
		return out;
	}

	public void load(InputStream is) throws IOException, org.xml.sax.SAXException
	{
		TodoParser.SINGLETON.readTodoList(is, true);
	}

	public void load() throws IOException, org.xml.sax.SAXException
	{
		InputStream is = null;
		if (getURL() != null)
		{
			is = getURL().openStream();
			load(is);
		}
	}

	public void save(String path, boolean overwrite, Writer writer)
	{
		if (writer == null)
		{
			System.out.println("ProjectMemberTodoList.cognitive.argouml.org: No writer specified");
			return;
		}

		System.out.println("Saving TODO LIST " + path + getName() + " !!!");

		if (expander == null)
		{
			Hashtable templates = TemplateReader.readFile(TODO_TEE);
			expander = new OCLExpander(templates);
		}

		expander.expand(writer, this, "", "");

		System.out.println("Done saving TODO LIST!!!");
	}

	public void save(String path, boolean overwrite)
	{
		save(path, overwrite, null);
	}
}

