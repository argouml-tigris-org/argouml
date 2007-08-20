// $Id:PropPanelCallState.java 10352 2006-04-08 22:36:07Z tfmorris $
// Copyright (c) 2003-2006 The Regents of the University of California. All
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

package org.argouml.uml.ui.behavior.activity_graphs;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JScrollPane;

import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.uml.ui.behavior.state_machines.AbstractPropPanelState;
import org.argouml.uml.ui.behavior.state_machines.UMLStateEntryListModel;
import org.argouml.util.ConfigLoader;
import org.tigris.swidgets.Orientation;

/**
 * The properties panel for a CallState.
 *
 * @author mkl
 */
public class PropPanelCallState extends AbstractPropPanelState {

    private JScrollPane callActionEntryScroll;
    private JList callActionEntryList;

    /**
     * The constructor.
     *
     */
    public PropPanelCallState() {
        this("CallState", lookupIcon("CallState"), ConfigLoader
                .getTabPropsOrientation());
    }

    /**
     * @param name the name of the properties panel
     * @param icon the icon to be shown next to the name
     * @param orientation the orientation of the panel
     */
    public PropPanelCallState(String name, ImageIcon icon,
            Orientation orientation) {

        super(name, icon, orientation);

        callActionEntryList =
            new UMLCallStateEntryList(
                new UMLStateEntryListModel());
        callActionEntryList.setVisibleRowCount(1);
        callActionEntryScroll = new JScrollPane(callActionEntryList);

        addField(Translator.localize("label.name"),
                getNameTextField());
        addField(Translator.localize("label.container"),
                getContainerScroll());
        addField(Translator.localize("label.entry"),
                getCallActionEntryScroll());

        addField(Translator.localize("label.deferrable"),
                getDeferrableEventsScroll());

        addSeparator();

        addField(Translator.localize("label.incoming"),
                getIncomingScroll());
        addField(Translator.localize("label.outgoing"),
                getOutgoingScroll());
    }

    /**
     * Let's add a buttun to create a CallAction.
     *
     * @see org.argouml.uml.ui.behavior.state_machines.PropPanelStateVertex#addExtraButtons()
     */
    protected void addExtraButtons() {
        Action a = new ActionNewEntryCallAction();
        a.putValue(Action.SHORT_DESCRIPTION,
                Translator.localize("button.new-callaction"));
        Icon icon = ResourceLoaderWrapper.lookupIcon("CallAction");
        a.putValue(Action.SMALL_ICON, icon);
        addAction(a);
    }

    /**
     * @return Returns the entryScroll.
     */
    protected JScrollPane getCallActionEntryScroll() {
        return callActionEntryScroll;
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = -8830997687737785261L;
}



