// $Id: PropPanelSimpleState.java 11266 2006-10-01 08:01:23Z linus $
// Copyright (c) 1996-2006 The Regents of the University of California. All
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

import org.argouml.i18n.Translator;
import org.argouml.util.ConfigLoader;
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
        this("Simple State", lookupIcon("SimpleState"),
                ConfigLoader.getTabPropsOrientation());
    }

    /**
     * Construct a new property panel for a Simple State with the given 
     * attributes.
     *
     * @param name the name of the properties panel, shown at the top
     * @param icon the icon shown at the top
     * @param orientation the orientation of the panel
     */
    public PropPanelSimpleState(String name, ImageIcon icon,
            Orientation orientation) {
        super(name, icon, orientation);

        addField(Translator.localize("label.name"),
                getNameTextField());
        addField(Translator.localize("label.container"),
                getContainerScroll());
        addField(Translator.localize("label.entry"),
                getEntryScroll());
        addField(Translator.localize("label.exit"),
                getExitScroll());
        addField(Translator.localize("label.do-activity"),
                getDoScroll());
        addField(Translator.localize("label.deferrable"),
                getDeferrableEventsScroll());

        addSeparator();

        addField(Translator.localize("label.incoming"),
                getIncomingScroll());
        addField(Translator.localize("label.outgoing"),
                getOutgoingScroll());
        addField(Translator.localize("label.internal-transitions"),
                getInternalTransitionsScroll());

    }

} /* end class PropPanelSimpleState */

