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

package org.argouml.application.api;

import org.argouml.uml.ui.PropPanel;

/**
 * An interface which identifies an ArgoUML plug-in property panel.
 * Plug-ins are replacements or additions to standard Argo classes.
 *
 * PluggablePropertyPanel should expect to be passed a
 * {@link QuadrantPanel} as the first argument in the inContext call.
 * The plugin can use getQuadrant to determine which panel is requesting
 * plugins.
 *
 * @author Thierry Lach
 * @since ARGO0.9.4
 * @deprecated by Linus Tolke (0.21.1 March 2006).
 *         Call registration in the Details Tabs subsystem from
 *         {@link org.argouml.moduleloader.ModuleInterface#enable()}.
 *         The needed registration is not currently available. Add it first!
 *         See {@link org.argouml.uml.ui.TabProps#moduleLoaded(
 *         ArgoModuleEvent event)}
 */
public interface PluggablePropertyPanel extends Pluggable {

    /**
     * Returns the <code>Class</code> that the panel handles.
     *
     * @return the <code>Class</code>.
     */
    Class getClassForPanel();

    /**
     * Returns an instance of the property panel.
     *
     * @return the <code>PropPanel</code>.
     */
    PropPanel getPropertyPanel();

} /* End interface PluggablePropertyPanel */
