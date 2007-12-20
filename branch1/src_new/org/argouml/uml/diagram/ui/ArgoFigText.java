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

package org.argouml.uml.diagram.ui;

import java.awt.Font;
import java.beans.PropertyChangeEvent;

import javax.management.ListenerNotFoundException;
import javax.management.MBeanNotificationInfo;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import javax.management.NotificationEmitter;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;

import org.argouml.application.events.ArgoDiagramAppearanceEvent;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.uml.diagram.UMLMutableGraphSupport;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Layer;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.graph.GraphModel;
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

    public ArgoFigText(int x, int y, int w, int h) {
        super(x, y, w, h);
    }

    public ArgoFigText(int x, int y, int w, int h, boolean expandOnly) {
        super(x, y, w, h, expandOnly);
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
     * @see org.argouml.uml.diagram.ui.ArgoFig#setProject(org.argouml.kernel.Project)
     */
    public void setProject(Project project) {
        throw new UnsupportedOperationException();
    }
    
    public Project getProject() {
        return ArgoFigUtil.getProject(this);
    }

    /**
     * Handles diagram font changing.
     * @param e the event
     * @see org.argouml.application.events.ArgoDiagramAppearanceEventListener#diagramFontChanged(org.argouml.application.events.ArgoDiagramAppearanceEvent)
     */
    public void diagramFontChanged(ArgoDiagramAppearanceEvent e) {
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
        int style = getFigFontStyle();
        Project p = getProject();
        if (p != null) {
            Font f = getProject().getProjectSettings().getFont(style);
            setFont(f);
        }
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

    @Override
    public void setOwner(Object own) {
        super.setOwner(own);
        updateListeners(null, own);
    }
    
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

}
