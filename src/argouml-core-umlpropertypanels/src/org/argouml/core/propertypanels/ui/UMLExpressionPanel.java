/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    mvw
 *    Thomas Neustupny
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2008-2009 The Regents of the University of California. All
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

package org.argouml.core.propertypanels.ui;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.tigris.swidgets.LabelledLayout;


/**
 * The panel that shows an Expression for an other UML element.
 * There are 8 kinds of Expressions defined in the UML 1.4.2.
 *
 * @author penyaskito
 */
class UMLExpressionPanel extends JPanel
	implements ChangeListener {

    private static final Logger LOG =
        Logger.getLogger(UMLExpressionPanel.class.getName());

    private final UMLExpressionModel model;
    private final UMLExpressionLanguageField languageField;
    private final UMLExpressionBodyField bodyField;

    public UMLExpressionPanel(UMLExpressionModel model, String title) {

        super(new LabelledLayout());
        LOG.log(Level.FINE, ">>New Expression panel created");

        TitledBorder border = new TitledBorder(title);
        this.setBorder(border);

        this.model = model;
        this.languageField = new UMLExpressionLanguageField(model,
                false);
        this.bodyField = new UMLExpressionBodyField(
                model, true);

        boolean isReadOnly = !languageField.isEditable();
        if (isReadOnly) {
            bodyField.setEditable(false);
        } else {
            add(languageField);
        }
        add(new JScrollPane(bodyField));

        model.addChangeListener(this);
    }

    @Override
    public void removeNotify() {
	model.removeChangeListener(this);
	super.removeNotify();
    }

    public void stateChanged(ChangeEvent e) {
        LOG.log(Level.FINE, ">>Values shown on panel are changed");
	bodyField.update();
	languageField.update();
    }
}
