// $Id$
// Copyright (c) 2007 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

import java.net.URI;

/**
 * Factory for creating new Project instances.
 * <p>
 * Consumers who need an instance of a Project
 * should use this factory to get an instance which implements the
 * Project interface. This allows us to decouple the specification from its
 * implementation and break the dependency cycle that would otherwise exist
 * between Projects and ArgoDiagrams.
 * 
 * @author Tom Morris <tfmorris@gmail.com>
 * @stereotype singleton
 */
public class ProjectFactory {

    private ProjectFactory() {
        super();
    }

    private static final ProjectFactory INSTANCE = new ProjectFactory();

    /**
     * Create a new empty project.
     * 
     * @return a new initialised project
     */
    public Project createProject() {
        return new ProjectImpl();
    }

    /**
     * Create a project with the given URI as its location
     * @param uri the URI to use as the name/location of the project
     * @return a newly created project
     */
    public Project createProject(URI uri) {
        return new ProjectImpl(uri);
    }

    public static ProjectFactory getInstance() {
        return INSTANCE;
    }
}
