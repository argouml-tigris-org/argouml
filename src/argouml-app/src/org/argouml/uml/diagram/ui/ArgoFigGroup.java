/* $Id$
 *******************************************************************************
 * Copyright (c) 2010-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Tom Morris
 *    Bob Tarling
 *******************************************************************************
 *
 * Some portions of this file were previously release using the BSD License:
 */
// $Id$
// Copyright (c) 2007-2009 The Regents of the University of California. All
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

package org.argouml.uml.diagram.ui;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.argouml.kernel.Project;
import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigGroup;

/**
 * A Fig which contains other Figs.  ArgoUMLs version of GEF's FigGroup. <p>
 *
 * It implements the additional methods of the ArgoFig interface.
 *
 * @author Tom Morris <tfmorris@gmail.com>
 */
public abstract class ArgoFigGroup extends FigGroup implements ArgoFig {

    private static final Logger LOG =
        Logger.getLogger(ArgoFigGroup.class.getName());

    // TODO: Make this final asap.
    private DiagramSettings settings;

    /**
     * Construct an empty FigGroup with the given DiagramSettings.
     * object.
     * @param owner owning UML element
     * @param renderSettings render settings to use
     */
    public ArgoFigGroup(Object owner, DiagramSettings renderSettings) {
        super();
        super.setOwner(owner);
        settings = renderSettings;
    }

    /**
     * This optional method is not implemented.  It will throw an
     * {@link UnsupportedOperationException} if used.  Figs are
     * added to a GraphModel which is, in turn, owned by a project.<p>
     *
     * @param project the project
     * @deprecated
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    public void setProject(Project project) {
        throw new UnsupportedOperationException();
    }

    /**
     * @deprecated for 0.27.2 by tfmorris.  Implementations should have all
     * the information that they require in the DiagramSettings object.
     *
     * @return the owning project
     * @see org.argouml.uml.diagram.ui.ArgoFig#getProject()
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    public Project getProject() {
        return ArgoFigUtil.getProject(this);
    }

    public void renderingChanged() {
        // Get all our sub Figs and hit them with the big stick too
        for (Fig fig : (List<Fig>) getFigs()) {
            if (fig instanceof ArgoFig) {
                ((ArgoFig) fig).renderingChanged();
            } else {
                LOG.log(Level.FINE, "Found non-Argo fig nested");
            }
        }
    }


    public DiagramSettings getSettings() {
        // TODO: This is a temporary crutch to use until all Figs are updated
        // to use the constructor that accepts a DiagramSettings object
        if (settings == null) {
            Project p = getProject();
            if (p != null) {
                return p.getProjectSettings().getDefaultDiagramSettings();
            }
        }
        return settings;
    }

    /**
     * @deprecated for 0.29.3 by Bob Tarling. The diagram settings are
     * provided to the constructor and then should never change.
     * When this method is removed the "settings" variable can become final.
     */
    public void setSettings(DiagramSettings renderSettings) {
        settings = renderSettings;
        renderingChanged();
    }

    /**
     * Setting the owner of the Fig must be done in the constructor and not
     * changed afterwards for all ArgoUML figs.
     *
     * @param owner owning UML element
     * @throws UnsupportedOperationException
     * @deprecated for 0.27.3 by tfmorris. Set owner in constructor. This method
     *             is implemented in GEF, so we'll leave this implementation
     *             here to block any attempts to use it within ArgoUML.
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    public void setOwner(Object owner) {
        if (owner != getOwner()) {
            throw new UnsupportedOperationException(
                    "Owner must be set in constructor and left unchanged");
        }
    }

}
