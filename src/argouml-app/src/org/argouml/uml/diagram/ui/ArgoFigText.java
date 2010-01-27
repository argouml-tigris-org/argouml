/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    mvw
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

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

import java.awt.Font;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;

import javax.management.ListenerNotFoundException;
import javax.management.MBeanNotificationInfo;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import javax.management.NotificationEmitter;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;

import org.argouml.kernel.Project;
import org.argouml.model.Model;
import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.presentation.FigText;

/**
 * Primitive Fig for text.
 *
 * @author Michiel
 */
public class ArgoFigText extends FigText 
    implements NotificationEmitter, ArgoFig {

    private NotificationBroadcasterSupport notifier = 
        new NotificationBroadcasterSupport();

    private DiagramSettings settings;
    
    /**
     * Construct a text fig owned by the given UML element. <p>
     * 
     * Even if there is no owner, then you still have to use this constructor;
     * setting the owner parameter to null is acceptable.
     * 
     * @param owner owning model element or null
     * @param bounds rectangle describing bounds of figure
     * @param renderSettings render settings
     * @param expandOnly true if Fig should never shrink
     */
    public ArgoFigText(Object owner, Rectangle bounds,
            DiagramSettings renderSettings, boolean expandOnly) {        
        super(bounds.x, bounds.y, bounds.width, bounds.height, expandOnly);
        //setFontFamily("dialog"); /* TODO: Is this needed?*/

        // TODO: We don't currently have any settings that can change on a
        // per-fig basis, so we can just use the project/diagram defaults
//        settings = new DiagramSettings(renderSettings);
        settings = renderSettings;
        super.setFontFamily(settings.getFontName());
        super.setFontSize(settings.getFontSize());
        super.setFilled(false);
        super.setFillColor(null);
        super.setTextFilled(false);
        super.setTextFillColor(null);
        super.setTextColor(TEXT_COLOR);
        /* This makes the text not touch the text-border line: */
        super.setTopMargin(1);
        super.setBotMargin(1);
        super.setLeftMargin(1);
        super.setRightMargin(1);
        // Certain types of fixed text (e.g. a FigStereotype with a keyword)
        // may not have an owner
        if (owner != null) {
            super.setOwner(owner);
            Model.getPump().addModelEventListener(this, owner, "remove");
        }
    }
    
    /*
     * @see org.tigris.gef.presentation.Fig#deleteFromModel()
     */
    @Override
    public void deleteFromModel() {
        super.deleteFromModel();
        firePropChange("remove", null, null);
        notifier.sendNotification(new Notification("remove", this, 0));
    }

    /*
     * @see javax.management.NotificationEmitter#removeNotificationListener(javax.management.NotificationListener, javax.management.NotificationFilter, java.lang.Object)
     */
    public void removeNotificationListener(NotificationListener listener,
        NotificationFilter filter, Object handback) 
        throws ListenerNotFoundException {
        notifier.removeNotificationListener(listener, filter, handback);
    }

    /*
     * @see javax.management.NotificationBroadcaster#addNotificationListener(javax.management.NotificationListener, javax.management.NotificationFilter, java.lang.Object)
     */
    public void addNotificationListener(NotificationListener listener, 
        NotificationFilter filter, Object handback) 
        throws IllegalArgumentException {
        notifier.addNotificationListener(listener, filter, handback);
    }

    /*
     * @see javax.management.NotificationBroadcaster#getNotificationInfo()
     */
    public MBeanNotificationInfo[] getNotificationInfo() {
        return notifier.getNotificationInfo();
    }

    /*
     * @see javax.management.NotificationBroadcaster#removeNotificationListener(javax.management.NotificationListener)
     */
    public void removeNotificationListener(NotificationListener listener) 
        throws ListenerNotFoundException {
        notifier.removeNotificationListener(listener);
    }
    
    /**
     * This optional method is not implemented.  It will throw an
     * {@link UnsupportedOperationException} if used.  Figs are 
     * added to a GraphModel which is, in turn, owned by a project.
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
     * @return the owning project
     * @see org.argouml.uml.diagram.ui.ArgoFig#getProject()
     * @deprecated for 0.27.2 by tfmorris.  Implementations should have all
     * the information that they require in the DiagramSettings object.
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    public Project getProject() {
        return ArgoFigUtil.getProject(this);
    }

    public void renderingChanged() {
        updateFont();
        setBounds(getBounds());
        damage();        
    }

    /**
     * This function should, for all FigTexts, 
     * recalculate the font-style (plain, bold, italic, bold/italic),
     * and apply it by calling FigText.setFont().
     */
    protected void updateFont() {
        setFont(getSettings().getFont(getFigFontStyle()));
    }

    /**
     * Determines the font style based on the UML model. 
     * Overrule this in Figs that have to show bold or italic based on the 
     * UML model they represent. 
     * E.g. abstract classes show their name in italic.
     * 
     * @return the font style for the nameFig.
     */
    protected int getFigFontStyle() {
        return Font.PLAIN;
    }

    /**
     * Update listeners for a new owner. Obsolete since owner is not allow to
     * change.
     * 
     * @param oldOwner the old owner
     * @param newOwner the new owner
     * @deprecated for 0.27.3 by tfmorris. The owner must be specified in the
     *             constructor and never changed.
     */
    @Deprecated
    protected void updateListeners(Object oldOwner, Object newOwner) {
        if (oldOwner == newOwner) {
            return;
        }
        if (oldOwner != null) {
            Model.getPump().removeModelEventListener(this, oldOwner);
        }
        if (newOwner != null) {
            Model.getPump().addModelEventListener(this, newOwner, "remove");
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        super.propertyChange(pce);
        if ("remove".equals(pce.getPropertyName()) 
                && (pce.getSource() == getOwner())) {
            deleteFromModel();
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
