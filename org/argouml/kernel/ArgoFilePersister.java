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

import java.io.File;
import java.net.URL;

import org.apache.log4j.Logger;

/**
 * To persist to and from argo (xml file) storage.
 * 
 * @author Bob Tarling
 */
public class ArgoFilePersister extends AbstractFilePersister {
    
    private static final Logger LOG = 
        Logger.getLogger(ArgoFilePersister.class);
    
    /**
     * The constructor.
     * Sets the extensionname and the description. 
     */
    public ArgoFilePersister() {
        extension = "argo";
        desc = "Argo project file";
    }
    
    /**
     * @see org.argouml.kernel.ProjectFilePersister#save(
     * org.argouml.kernel.Project, java.io.File)
     */
    public void save(Project project, File file) throws SaveException {
        throw new SaveException("Save as XMI has not yet been implemented");
    }
    
    /**
     * @see org.argouml.kernel.ProjectFilePersister#loadProject(java.net.URL)
     */
    public Project loadProject(URL url) throws OpenException {
        throw new OpenException("Open argo has not yet been implemented");
    }
}
