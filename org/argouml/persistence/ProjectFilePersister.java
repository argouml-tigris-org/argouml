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
package org.argouml.persistence;

import java.io.File;
import java.net.URL;

import javax.swing.JProgressBar;

import org.argouml.kernel.Project;

/**
 * To persist a project to and from file storage.
 *
 * @author Bob Tarling
 */
public interface ProjectFilePersister {

    /**
     * @param project the project to save
     * @param file The file to write.
     * @throws SaveException if anything goes wrong.
     */
    public void save(Project project, File file) throws SaveException;

    /**
     * @param file the file of the project to load
     * @param progressBar the progress bar givin by the GUI to indicate progress.
     * @return the Project
     * 
     * @throws OpenException when we fail to open from this url
     */
    public Project doLoad(File file, JProgressBar progressBar) throws OpenException;

    /**
     * @param url the url of the project to load
     * @param progressBar the progress bar givin by the GUI to indicate progress.
     * @return the Project
     * @throws OpenException when we fail to open from this url
     */
    public Project doLoad(URL url, JProgressBar progressBar) throws OpenException;

}
