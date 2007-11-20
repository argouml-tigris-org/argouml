// $Id$
// Copyright (c) 1996-2007 The Regents of the University of California. All
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

package org.argouml.uml.ui.foundation.core;

import javax.swing.ImageIcon;

import org.tigris.swidgets.Orientation;

/**
 * Added this class to give as much information to the user as possible
 * if the lookup mechanisme for proppanels fails.
 *
 * @since Oct 12, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public class PropPanelRelationship extends PropPanelModelElement {

    /**
     * Construct a property panel for a Relationship with the given name and
     * icon.
     * 
     * @param name The name of the panel to be shown at the top.
     * @param icon The icon to be shown next to the name.
     */
    public PropPanelRelationship(String name, ImageIcon icon) {
        super(name, icon);
    }
    
    /**
     * The constructor.
     *
     * @param name The name of the panel to be shown at the top.
     * @param orientation The orientation of the panel.
     * @param icon The icon to be shown next to the name.
     * @deprecated for 0.25.4 by tfmorris. Use
     *             {@link #PropPanelRelationship(String, ImageIcon)} and
     *             setOrientation() after instantiation.
     */
    @Deprecated
    public PropPanelRelationship(String name, ImageIcon icon,
            Orientation orientation) {
        super(name, icon, orientation);
    }

    /**
     * Constructor for PropPanelRelationship.
     */
    public PropPanelRelationship() {
        super("label.relationship", lookupIcon("Relationship"));
    }

    /**
     * The constructor.
     *
     * @param name the name of the panel to be shown at the top
     * @param orientation the orientation of the panel
     * @deprecated for 0.25.4 by tfmorris. Use
     *             {@link #PropPanelRelationship(String, ImageIcon)} and
     *             setOrientation() after instantiation.
     */
    @Deprecated
    public PropPanelRelationship(String name, Orientation orientation) {
        super(name, orientation);
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = -1610200799419501588L;
}
