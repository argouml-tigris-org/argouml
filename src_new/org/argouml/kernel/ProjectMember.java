// $Id$
// Copyright (c) 1996-2004 The Regents of the University of California. All
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

public abstract class ProjectMember {

    ////////////////////////////////////////////////////////////////
    // instance varables

    private String name;
    private Project project = null;

    ////////////////////////////////////////////////////////////////
    // constructors

    public ProjectMember(String name, Project theProject) {
        project = theProject;
        setName(name);
    }


    ////////////////////////////////////////////////////////////////
    // accessors


    /**
     * In contrast to {@link #getName} returns the member's name without the
     * prepended name of the project. This is the name that
     * {@link Project#findMemberByName} goes by.
     *
     * @author Steffen Zschaler
     *
     * @return the member's name without the prepended name of the project
     */
    public String getPlainName() {
        String s = name;
        
        if (s != null) {
            if (!s.endsWith (getFileExtension())) {
        	s += getFileExtension();
            }
        }
        
        return s;
    }

    /**
     * In contrast to {@link #getPlainName} returns the member's name
     * including the project's base name. The project's base name is
     * prepended followed by an underscore '_'.
     *
     * @return the member's name including the project's base name
     */
    public String getName() {
        if (name == null)
            return null;
        
        String s = project.getBaseName();
        
        if (name.length() > 0)
            s += "_" + name;
        
        if (!s.endsWith(getFileExtension()))
            s += getFileExtension();
        
        return s;
    }
  
    public void setName(String s) { 
        name = s;
        
        if (name == null) {
            return;
        }
        
        if (name.startsWith (project.getBaseName())) {
            name = name.substring (project.getBaseName().length());
            int i = 0;
            for (; i < name.length(); i++) {
            	if (name.charAt(i) != '_') {
            	    break;
                }
            }
            if (i > 0) {
                name = name.substring(i);
            }
        }
        
        if (name.endsWith(getFileExtension())) {
            name = 
                name.substring(0,
                        name.length() - getFileExtension().length());
        }
    }

    public Project getProject() { return project; }

    public abstract String getType();
    public abstract String getFileExtension();

    ////////////////////////////////////////////////////////////////
    // actions

    /**
     * Save the projectmember as XML to the given writer.
     * @param writer The Writer to which appen the save.
     * @param indent The offset to which to indent the XML
     * @throws Exception
     */
    public abstract void save(Writer writer, Integer indent) throws SaveException;

    /**
     * Send an existing file of XML to the PrintWriter.
     * @param writer the PrintWriter.
     * @param file the File
     * @param indent How far to indent in the writer.
     * @throws SaveException on any errors.
     */
    protected void addXmlFileToWriter(PrintWriter writer, File file, int indent) 
            throws SaveException {
        try {
            String padding =
                "                                          ".substring(0,indent);
            BufferedReader reader = 
                new BufferedReader(new FileReader(file));
            
            // Skip the <?xml... first line
            String line = reader.readLine();
            while (line != null && (line.startsWith("<?xml ") || line.startsWith("<!DOCTYPE "))) {
                line = reader.readLine();
            }
            
            while (line != null) {
                (writer).print(padding);
                (writer).println(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (FileNotFoundException e) {
            throw new SaveException(e);
        } catch (IOException e) {
            throw new SaveException(e);
        }
    }
    
    public void remove() {
        name = null;
        project = null;
    }
} /* end class ProjectMember */
