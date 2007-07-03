// $Id: PropPanelArgument.java 11189 2006-09-17 16:26:40Z mvw $
// Copyright (c) 2003-2006 The Regents of the University of California. All
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

package org.argouml.uml.ui.behavior.common_behavior;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.argouml.i18n.Translator;
import org.argouml.ui.LookAndFeelMgr;
import org.argouml.uml.ui.ActionNavigateAction;
import org.argouml.uml.ui.UMLExpressionBodyField;
import org.argouml.uml.ui.UMLExpressionExpressionModel;
import org.argouml.uml.ui.UMLExpressionLanguageField;
import org.argouml.uml.ui.UMLExpressionModel2;
import org.argouml.uml.ui.foundation.core.PropPanelModelElement;
import org.argouml.util.ConfigLoader;

/**
 *
 * @since aug 10, 2003
 * @author Decki, Endi, Yayan. Polytechnic of Bandung Indonesia, Computer
 *         Engineering Departement
 */
public class PropPanelArgument extends PropPanelModelElement {

    // //////////////////////////////////////////////////////////////
    // contructors
    /**
     * Constructor.
     */
    public PropPanelArgument() {

        super("Argument", ConfigLoader.getTabPropsOrientation());

        addField(Translator.localize("label.name"), getNameTextField());

        UMLExpressionModel2 expressionModel =
            new UMLExpressionExpressionModel(
                this, "expression");
        JTextArea ebf = new UMLExpressionBodyField(expressionModel, true);
        ebf.setFont(LookAndFeelMgr.getInstance().getStandardFont());
        ebf.setRows(3); // make it take up all remaining height
        addField(Translator.localize("label.value"),
                new JScrollPane(ebf));
        addField(Translator.localize("label.language"),
                new UMLExpressionLanguageField(expressionModel, true));

        addAction(new ActionNavigateAction());
        addAction(getDeleteAction());

    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = 6737211630130267264L;
}
