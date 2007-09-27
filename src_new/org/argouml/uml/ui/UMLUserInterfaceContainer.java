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


package org.argouml.uml.ui;
import java.util.Iterator;

import org.argouml.uml.profile.ProfileConfiguration;

/**
 * Interface supported by any container of UML user interface components.
 * This interface allows UML user interface components to determine
 * the currently selected target and profile and allows the control
 * to request a navigation.  Implemented by PropPanel.
 *
 * @author Curt Arnold
 * @see PropPanel
 */
public interface UMLUserInterfaceContainer {

    /**
     * @return the current target for the container, may be null
     */
    public Object getTarget();

    /**
     * @return the current target for the container if the target is a
     *      ModelElement, otherwise null
     */
    public Object getModelElement();

    /**
     * Return the current profile.
     * <p>
     * NOTE: An incompatible change was made to this API for 0.25.4. The return
     * type was changed from Profile to ProfileConfiguration.
     * 
     * @return the current profile, may not be null
     */
    public ProfileConfiguration getProfile();

    /**
     * Formats the specified model element.  Typically, deferred to the
     * profile.
     *
     * @param element the given ModelElement
     * @return the formatted string
     */
    public String formatElement(Object element);

    /**
     * Formats a collection of model elements.  Typically, deferred to the
     * profile.
     *
     * @param iter an iterator into the collection of modelelements
     * @return the formatted string
     */
    public String formatCollection(Iterator iter);

    /**
     * Formats the model element as a namespace.
     *
     * @param ns the namespace
     * @return the formatted string
     */
    public String formatNamespace(Object ns);

}
