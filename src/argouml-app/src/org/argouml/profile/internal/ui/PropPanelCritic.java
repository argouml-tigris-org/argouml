/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    tfmorris
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2008 The Regents of the University of California. All
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

package org.argouml.profile.internal.ui;

import java.util.Collection;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.argouml.cognitive.Critic;
import org.argouml.profile.internal.ocl.CrOCL;
import org.argouml.uml.ui.PropPanel;

/**
 * PropPanel for Critic
 * 
 * @author maurelio1234
 */
public class PropPanelCritic extends PropPanel {

    private JTextField criticClass;

    private JTextField name;

    private JTextField headline;

    private JTextField priority;

    private JTextArea description;

    private JTextArea ocl;
    
    private JLabel oclLabel;
    
    private JTextField supportedDecision;

    private JTextField knowledgeType;

    /**
     * Constructor
     */
    public PropPanelCritic(Object target) {
        super("", (ImageIcon) null);

        criticClass = new JTextField();
        addField("label.class", criticClass);
        criticClass.setEditable(false);

        name = new JTextField();
        addField("label.name", name);
        name.setEditable(false);

        headline = new JTextField();
        addField("label.headline", headline);
        headline.setEditable(false);

        description = new JTextArea(5, 30);
        addField("label.description", description);
        description.setEditable(false);
        description.setLineWrap(true);
        
        priority = new JTextField();
        addField("label.priority", priority);
        priority.setEditable(false);
        
        ocl = new JTextArea(5, 30);
        oclLabel = addField("label.ocl", ocl);
        ocl.setEditable(false);
        ocl.setLineWrap(true);
        
        supportedDecision = new JTextField();
        addField("label.decision", supportedDecision);
        supportedDecision.setEditable(false);

        knowledgeType = new JTextField();
        addField("label.knowledge_types", knowledgeType);
        knowledgeType.setEditable(false);
        
        setCritic((Critic) target);
    }

    /**
     * @param t the target
     * @see org.argouml.uml.ui.PropPanel#setTarget(java.lang.Object)
     */
    private void setCritic(Critic c) {
        final String targetType =
            c.getClass().getCanonicalName();
        
        criticClass.setText(targetType);

        name.setText(c.getCriticName());
        headline.setText(c.getHeadline());
        description.setText(c.getDescriptionTemplate());
        supportedDecision.setText("" + colToString(c.getSupportedDecisions()));
        if (c instanceof CrOCL) {
            oclLabel.setVisible(true);
            ocl.setVisible(true);
            ocl.setText(((CrOCL) c).getOCL());
        } else {
            oclLabel.setVisible(false);
            ocl.setVisible(false);
        }
        
        priority.setText("" + c.getPriority());
        knowledgeType.setText("" + colToString(c.getKnowledgeTypes()));
    }

    private String colToString(Collection set) {
        String r = "";
        int count = 0;
        for (Object obj : set) {
            if (count > 0) {
                r += ", ";
            }
            r += obj;
            ++count;
        }        
        return r;
    }
}
