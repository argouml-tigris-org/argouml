// $Id$
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

package org.argouml.xml.argo;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.log4j.Logger;
import org.argouml.kernel.Project;
import org.argouml.xml.todo.TodoParser;
import org.xml.sax.SAXException;

/**
 * The file persister for the Todo members.
 * @author Bob Tarling
 */
public class TodoListMemberFilePersister extends MemberFilePersister {
    
    private URL url;

    private static final Logger LOG =
        Logger.getLogger(TodoListMemberFilePersister.class);
    
    /**
     * The constructor.
     * 
     * @param theUrl the location where the diagram members 
     *               are stored persistently
     * @param theProject the project to persist
     * @throws SAXException when SAX finds a problem
     */
    public TodoListMemberFilePersister(URL theUrl, Project theProject)
            throws SAXException {
        url = theUrl;
    }
    
    /**
     * Load the todo member.
     * Throws a SAXException on any parsing error.
     *
     * @see org.argouml.xml.argo.MemberFilePersister#load(java.util.Map)
     */
    public void load(int instance) throws SAXException {
        try {
            InputStream inputStream =
                    new XmlInputStream(url.openStream(), "todo");
            TodoParser parser = new TodoParser();
            parser.readTodoList(inputStream, true);
        } catch (IOException e) {
            LOG.error("IOException caught", e);
            throw new SAXException(e);
        }
    }
}