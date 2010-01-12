/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    tfmorris
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 1996-2009 The Regents of the University of California. All
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

package org.argouml.persistence;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;

import org.argouml.application.api.Argo;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectMember;
import org.xml.sax.InputSource;

/**
 * A base class file persister for project members.
 * @author Bob Tarling
 */
abstract class MemberFilePersister {
    /**
     * Load a project member from an InputStream.
     *
     * @param project the project to persist
     * @param inputStream the inputStream to parse to load the member.
     * @throws OpenException on any parsing errors.
     */
    public abstract void load(Project project, InputStream inputStream)
        throws OpenException;

    /**
     * Load a project member from a URL.
     *
     * @param project the project to persist
     * @param url the URL to open and parse to load the member.
     * @throws OpenException on any parsing errors.
     */
    public abstract void load(Project project, URL url)
        throws OpenException;

    /**
     * Load a project member from a SAX InputSource.
     *
     * @param project the project to persist
     * @param inputSource the InputSource to load from
     * @throws OpenException on any parsing errors.
     * @since 0.29.1
     */
    public abstract void load(Project project, InputSource inputSource)
        throws OpenException;
    
    
    /**
     * Gets the tag name which is the root tag for this member.
     * @return tag name.
     */
    public abstract String getMainTag();


    /**
     * Save the project member as XML to the given output stream.
     * 
     * @param member
     *            The project member to save.
     * @param stream
     *            The OutputStream to write the contents to.
     * @throws SaveException
     *             if the save fails
     * @since 0.25.4
     */
    public abstract void save(
            ProjectMember member,
            OutputStream stream) throws SaveException;
    
    
    /**
     * Send an existing file of XML to the PrintWriter.
     * @param writer the PrintWriter.
     * @param file the File
     * @throws SaveException on any errors.
     */
    protected void addXmlFileToWriter(PrintWriter writer, File file)
        throws SaveException {
        try {
            BufferedReader reader =
                new BufferedReader(
                        new InputStreamReader(
                                new FileInputStream(file), 
                                Argo.getEncoding()));

            // Skip the <?xml... first line
            String line = reader.readLine();
            while (line != null && (line.startsWith("<?xml ")
                    || line.startsWith("<!DOCTYPE "))) {
                line = reader.readLine();
            }

            while (line != null) {
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

}
