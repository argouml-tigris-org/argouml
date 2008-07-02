// $Id$
// Copyright (c) 1996-2007 The Regents of the University of California. All
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

package org.argouml.uml.ui.behavior.state_machines;

import javax.swing.ImageIcon;

import org.tigris.swidgets.Orientation;

/**
 * The properties panel for a State.
 *
 * @author 5heyden
 */
public class PropPanelSimpleState extends AbstractPropPanelState {

    /**
     * The serial version.
     */
    private static final long serialVersionUID = 7072535148338954868L;

    /**
     * Construct a new default property panel for a Simple State.
     */
    public PropPanelSimpleState() {
        this("label.simple.state", lookupIcon("SimpleState"));
    }

    /**
     * Construct a new property panel for a Simple State with the given 
     * attributes.
     *
     * @param name the name of the properties panel, shown at the top
     * @param icon the icon shown at the top
     * @param orientation the orientation of the panel
     * @deprecated for 0.25.4 by tfmorris. Use
     *             {@link #PropPanelSimpleState(String, ImageIcon)} and
     *             setOrientation() after instantiation.
     */
    @Deprecated
    public PropPanelSimpleState(String name, ImageIcon icon,
            Orientation orientation) {
        this(name, icon);
        setOrientation(orientation);
    }
    
    /**
     * Construct a new property panel for a Simple State with the given
     * attributes.
     * 
     * @param name the name of the properties panel, shown at the top
     * @param icon the icon shown at the top
     */
    private PropPanelSimpleState(String name, ImageIcon icon) {
        super(name, icon);

        addField("label.name", getNameTextField());
        addField("label.container", getContainerScroll());
        addField("label.entry", getEntryScroll());
        addField("label.exit", getExitScroll());
        addField("label.do-activity", getDoScroll());
        addField("label.deferrable", getDeferrableEventsScroll());

        addSeparator();

        addField("label.incoming", getIncomingScroll());
        addField("label.outgoing", getOutgoingScroll());
        addField("label.internal-transitions", getInternalTransitionsScroll());

    }

}

