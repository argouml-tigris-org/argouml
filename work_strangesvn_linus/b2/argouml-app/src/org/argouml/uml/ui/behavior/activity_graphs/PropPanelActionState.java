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

package org.argouml.uml.ui.behavior.activity_graphs;

import javax.swing.ImageIcon;

import org.argouml.i18n.Translator;
import org.argouml.uml.ui.behavior.state_machines.AbstractPropPanelState;
import org.tigris.swidgets.Orientation;

/**
 * User interface panel shown at the bottom of the screen that allows the user
 * to edit the properties of the selected UML model element.
 */
public class PropPanelActionState extends AbstractPropPanelState {

    /**
     * The serial version.
     */
    private static final long serialVersionUID = 4936258091606712050L;

    /**
     * Construct a default property panel for an Action State.
     */
    public PropPanelActionState() {
        this("label.action-state", lookupIcon("ActionState"));
    }

    /**
     * Construct a property panel for an Action State with the given params.
     *
     * @param name the name of the properties panel
     * @param icon the icon to be shown next to the name
     * @param orientation the orientation of the panel
     * @deprecated for 0.25.4 by tfmorris. Use
     *             {@link #PropPanelActionState(String, ImageIcon)} and
     *             setOrientation() after instantiation.
     */
    @Deprecated
    public PropPanelActionState(String name, ImageIcon icon,
            Orientation orientation) {
        this(name, icon);
        setOrientation(orientation);
    }
    
    /**
     * Construct a property panel for an Action State with the given params.
     *
     * @param name the name of the properties panel
     * @param icon the icon to be shown next to the name
     */
    public PropPanelActionState(String name, ImageIcon icon) {
        super(name, icon);

        addField(Translator.localize("label.name"), getNameTextField());
        addField(Translator.localize("label.container"), getContainerScroll());
        addField(Translator.localize("label.entry"), getEntryScroll());

        addField(Translator.localize("label.deferrable"),
                getDeferrableEventsScroll());

        addSeparator();

        addField(Translator.localize("label.incoming"), getIncomingScroll());
        addField(Translator.localize("label.outgoing"), getOutgoingScroll());

    }

}
