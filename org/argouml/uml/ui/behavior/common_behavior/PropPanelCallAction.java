// $Id$
// Copyright (c) 1996-2002 The Regents of the University of California. All
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

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

import ru.novosoft.uml.behavior.common_behavior.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.model_management.*;

import org.argouml.application.api.*;
import org.argouml.uml.ui.*;
import org.argouml.uml.ui.foundation.core.*;

public class PropPanelCallAction extends PropPanelModelElement {

    ////////////////////////////////////////////////////////////////
    // contructors
    public PropPanelCallAction() {
        super("Action", _callActionIcon,2);

        Class mclass = MCallAction.class;

	addCaption(Argo.localize("UMLMenu", "label.name"),1,0,0);
        addField(nameField,1,0,0);

        UMLExpressionModel expressionModel = new UMLExpressionModel(this,MAction.class,"script",
            MActionExpression.class,"getScript","setScript");

        addCaption(Argo.localize("UMLMenu", "label.expression"),2,0,0);
        addField(new JScrollPane(new UMLExpressionBodyField(expressionModel,true)),2,0,0);

        addCaption(Argo.localize("UMLMenu", "label.language"),3,0,1);
        addField(new UMLExpressionLanguageField(expressionModel,true),3,0,0);

	new PropPanelButton(this,buttonPanel,_navBackIcon, Argo.localize("UMLMenu", "button.go-back"),"navigateBackAction","isNavigateBackEnabled");
	new PropPanelButton(this,buttonPanel,_navForwardIcon, Argo.localize("UMLMenu", "button.go-forward"),"navigateForwardAction","isNavigateForwardEnabled");
    }

} /* end class PropPanelCallAction */

