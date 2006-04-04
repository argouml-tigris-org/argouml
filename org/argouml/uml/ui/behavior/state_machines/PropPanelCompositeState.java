// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JScrollPane;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.ui.ActionAddConcurrentRegion;
import org.argouml.uml.diagram.ui.ActionDeleteConcurrentRegion;
import org.argouml.util.ConfigLoader;
import org.tigris.swidgets.Orientation;

/**
 * The properties panel for a Composite State.
 *
 * @author 5heyden
 */
public class PropPanelCompositeState extends AbstractPropPanelState {

    private JList subverticesList = null;
    private Action addConcurrentRegion;
    private Action deleteConcurrentRegion;

    /**
     * Constructor for PropPanelCompositeState.
     * @param name the name of the properties panel
     * @param icon the icon to be shown next to the name
     * @param orientation the orientation of the panel
     */
    public PropPanelCompositeState(String name, ImageIcon icon,
            Orientation orientation) {
        super(name, icon, orientation);
        initialize();
    }

    /**
     * The constructor.
     *
     */
    public PropPanelCompositeState() {
        super("Composite State", lookupIcon("CompositeState"),
                ConfigLoader.getTabPropsOrientation());
        initialize();

        addField(Translator.localize("label.name"),
                getNameTextField());
        addField(Translator.localize("label.stereotype"),
                getStereotypeSelector());
        addField(Translator.localize("label.container"),
                getContainerScroll());
        /*addField(Translator.localize("label.modifiers"),
                new UMLCompositeStateConcurrentCheckBox());*/
        addField(Translator.localize("label.entry"),
                getEntryScroll());
        addField(Translator.localize("label.exit"),
                getExitScroll());
        addField(Translator.localize("label.do-activity"),
                getDoScroll());

        addSeperator();

        addField(Translator.localize("label.incoming"),
                getIncomingScroll());
        addField(Translator.localize("label.outgoing"),
                getOutgoingScroll());
        addField(Translator.localize("label.internal-transitions"),
                getInternalTransitionsScroll());

        addSeperator();

        addField(Translator.localize("label.subvertex"),
                new JScrollPane(subverticesList));
    }

    /**
     * @see org.argouml.uml.ui.behavior.state_machines.PropPanelStateVertex#addExtraButtons()
     */
    protected void addExtraButtons() {
        super.addExtraButtons();
        addConcurrentRegion = new ActionAddConcurrentRegion();
        addAction(addConcurrentRegion);
        deleteConcurrentRegion = new ActionDeleteConcurrentRegion();
        addAction(deleteConcurrentRegion);
    }

    /**
     * Initialize the panel with its specific fields, in casu
     * the substate vertex list.
     */
    protected void initialize() {
	subverticesList =
	    new UMLCompositeStateSubvertexList(
	            new UMLCompositeStateSubvertexListModel());
    }

    /**
     * @see org.argouml.uml.ui.PropPanel#setTarget(java.lang.Object)
     */
    public void setTarget(Object t) {
        super.setTarget(t);
        addConcurrentRegion.setEnabled(addConcurrentRegion.isEnabled());
        deleteConcurrentRegion.setEnabled(deleteConcurrentRegion.isEnabled());
        Object target = TargetManager.getInstance().getModelTarget();
        if (Model.getFacade().isAConcurrentRegion(target)) {
            getTitleLabel().setText("Concurrent Region");
        } else if (Model.getFacade().isConcurrent(target)) {
            getTitleLabel().setText("Concurrent Composite State");
        } else {
            getTitleLabel().setText("Composite State");
        }
     }

} /* end class PropPanelCompositeState */



