// $Id$
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

//$Id$

package org.argouml.application.api;

import java.util.List;

import org.argouml.kernel.Project;
import org.argouml.uml.reveng.DiagramInterface;

/**
 * @deprecated This extends a deprecated interface, so it is deprecated also.
 * @author Bogdan Pistol
 */
public interface PluggableImportEx extends PluggableImport {

    
    /**
     * One parseable object from the list will be parsed by this method.
     * Objects will be parsed in order defined by getList().
     * @param p - the current project
     * @param o - object to be parsed
     * @param diagram - current class diagram when Import was invoked
     * @param settings - these are common import settings
     * @throws Exception (all kinds)
     */
    void parseFile(Project p, Object o, DiagramInterface diagram,
            PluggableImportSettings settings) throws Exception;

    /**
     * Returns a list with objects that represent settings for this
     * import. These objects implement the PluggableImportTypes.* interfaces.
     * <p>
     * The caller must determine what interface an object is implementing
     * iterating the interfaces PluggableImportTypes.*
     * <p>
     * This is done this way to eliminate the need to use GUI elements.
     * The settings can easily be mapped into any GUI elements, this way
     * we are independent from the type of GUI.
     * @return the list of settings that are required by this particular import
     */
    List getSpecificImportSettings();
    
}
