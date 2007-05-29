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

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.argouml.i18n.Translator;
import org.argouml.uml.ui.UMLExpressionBodyField;
import org.argouml.uml.ui.UMLExpressionLanguageField;
import org.argouml.uml.ui.UMLExpressionModel2;
import org.argouml.uml.ui.UMLTimeExpressionModel;
import org.argouml.util.ConfigLoader;

/**
 * The properties panel for a TimeEvent.
 *
 * @author oliver.heyden
 */
public class PropPanelTimeEvent extends PropPanelEvent {

    /**
     * The constructor.
     */
    public PropPanelTimeEvent() {
        super("Time event", lookupIcon("TimeEvent"), ConfigLoader
                .getTabPropsOrientation());
    }

    /*
     * @see org.argouml.uml.ui.behavior.state_machines.PropPanelEvent#initialize()
     */
    public void initialize() {
        super.initialize();

        UMLExpressionModel2 whenModel = new UMLTimeExpressionModel(
                this, "when");
        JPanel whenPanel = createBorderPanel(Translator
                .localize("label.when"));
        whenPanel.add(new JScrollPane(new UMLExpressionBodyField(
                whenModel, true)));
        whenPanel.add(new UMLExpressionLanguageField(whenModel,
                false));
        add(whenPanel);
        
        addAction(getDeleteAction());
    }

}
