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

package org.argouml.uml.ui.behavior.state_machines;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;

import org.argouml.i18n.Translator;
import org.argouml.uml.ui.UMLComboBox2;
import org.argouml.uml.ui.UMLComboBoxNavigator;
import org.argouml.uml.ui.UMLMutableLinkedList;
import org.tigris.swidgets.Orientation;

/**
 * @since Dec 15, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public class PropPanelSubmachineState extends PropPanelCompositeState {

    /**
     * The serial version.
     */
    private static final long serialVersionUID = 2384673708664550264L;

    /**
     * Construct a property panel for SubmachineState elements with the given
     * params.
     *
     * @param name
     *            the name of the properties panel
     * @param icon
     *            the icon to be shown next to the name
     * @param orientation
     *            the orientation of the panel
     * @deprecated for 0.25.4 by tfmorris. Use
     *             {@link #PropPanelCompositeState(String, ImageIcon)} and
     *             setOrientation() after instantiation.
     */
    @Deprecated
    public PropPanelSubmachineState(final String name, final ImageIcon icon,
            final Orientation orientation) {
        super(name, icon);
        setOrientation(orientation);
        initialize();
    }

    /**
     * Construct a property panel for SubmachineState elements with the given
     * params.
     *
     * @param name
     *            the name of the properties panel
     * @param icon
     *            the icon to be shown next to the name
     */
    public PropPanelSubmachineState(final String name, final ImageIcon icon) {
        super(name, icon);
        // TODO: Are these constructors organized correctly?  We aren't
        // providing our own initialize(), so all the work done in the default
        // constructor will be skipped for 
        // our subclasses (PropPanelSubactivityState) - tfm - 20071119
        initialize();
    }
    
    /**
     * Construct a default property panel SubmachineState elements.
     */
    public PropPanelSubmachineState() {
        super("label.submachinestate", lookupIcon("SubmachineState"));
        addField("label.name", getNameTextField());
        addField("label.container", getContainerScroll());
        final JComboBox submachineBox = new UMLComboBox2(
                new UMLSubmachineStateComboBoxModel(),
                ActionSetSubmachineStateSubmachine.getInstance());
        addField("label.submachine",
                new UMLComboBoxNavigator(Translator.localize(
                        "tooltip.nav-submachine"), submachineBox));
        addField("label.entry", getEntryScroll());
        addField("label.exit", getExitScroll());
        addField("label.do-activity", getDoScroll());

        addSeparator();

        addField("label.incoming", getIncomingScroll());
        addField("label.outgoing", getOutgoingScroll());
        addField("label.internal-transitions", getInternalTransitionsScroll());

        addSeparator();

        addField("label.subvertex",
                new JScrollPane(new UMLMutableLinkedList(
                        new UMLCompositeStateSubvertexListModel(), null,
                        ActionNewStubState.getInstance())));
    }

    /*
     * @see org.argouml.uml.ui.behavior.state_machines.PropPanelStateVertex#addExtraButtons()
     */
    @Override
    protected void addExtraButtons() {
        // Intentionally do nothing.
    }

    /*
     * @see org.argouml.uml.ui.behavior.state_machines.PropPanelCompositeState#updateExtraButtons()
     */
    @Override
    protected void updateExtraButtons() {
        // Intentionally do nothing.
    }


}
