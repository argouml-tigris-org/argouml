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

package org.argouml.application.helpers;
import java.util.Vector;

import javax.swing.JMenuItem;

import org.argouml.application.api.PluggableDiagram;
import org.argouml.i18n.Translator;
import org.argouml.ui.ArgoDiagram;

/**
 * Helper object for Diagrams
 *
 * @author Thomas Neustupny
 * @since 0.9.5
 */
public abstract class DiagramHelper extends ArgoDiagram
    implements PluggableDiagram {

    /**
     * Default localization key for diagrams
     */
    public static final String DIAGRAM_BUNDLE = "DiagramType";

    /**
     * String naming the resource bundle to use for localization.
     */
    private String bundle = "";

    /**
     * The constructor.
     *
     */
    public DiagramHelper() {
        bundle = getDiagramResourceBundleKey();
    }

    /**
     * @see org.argouml.application.api.ArgoModule#setModuleEnabled(boolean)
     */
    public void setModuleEnabled(boolean v) { }

    /**
     * @see org.argouml.application.api.ArgoModule#initializeModule()
     */
    public boolean initializeModule() { return true; }

    /**
     * @see org.argouml.application.api.Pluggable#inContext(java.lang.Object[])
     */
    public boolean inContext(Object[] o) { return true; }

    /**
     * @see org.argouml.application.api.ArgoModule#isModuleEnabled()
     */
    public boolean isModuleEnabled() { return true; }

    /**
     * @see org.argouml.application.api.ArgoModule#getModulePopUpActions(
     * java.util.Vector, java.lang.Object)
     */
    public Vector getModulePopUpActions(Vector v, Object o) { return null; }

    /**
     * @see org.argouml.application.api.ArgoModule#shutdownModule()
     */
    public boolean shutdownModule() { return true; }

    /**
     * @see org.argouml.application.api.PluggableDiagram#getDiagramMenuItem()
     */
    public JMenuItem getDiagramMenuItem() {
	return new JMenuItem(Translator.localize("menu.item.diagram-type"));
    } // add icon if desired

    /**
     * @return the default localization key for diagrams
     */
    public String getDiagramResourceBundleKey() {
        return DIAGRAM_BUNDLE;
    }
}

