// $Id: eclipse-argo-codetemplates.xml 11347 2006-10-26 22:37:44Z linus $
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

import javax.swing.ImageIcon;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.argouml.cognitive.Critic;
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
    private JTextArea  description;
    private JTextField supportedDecision;
    private JTextField knowledgeType;
    
    /**
     * Constructor 
     */
    public PropPanelCritic() {
        super("", (ImageIcon)null);
                
        criticClass = new JTextField();
        addField("label.class", criticClass);
        criticClass.setEditable(false);
        
        name = new JTextField();
        addField("label.name", name);
        name.setEditable(false);
        
        headline = new JTextField();
        addField("label.headline", headline);
        headline.setEditable(false);
        
        priority = new JTextField();
        addField("label.priority", priority);
        priority.setEditable(false);
        
        description = new JTextArea(5, 30);        
        addField("label.description", description);
        description.setEditable(false);
        description.setLineWrap(true);
        
        supportedDecision = new JTextField();
        addField("label.decision", supportedDecision);
        supportedDecision.setEditable(false);        

        knowledgeType = new JTextField();
        addField("label.knowledge_types", knowledgeType);
        knowledgeType.setEditable(false);        
    }


    /**
     * @see org.argouml.uml.ui.PropPanel#setTarget(java.lang.Object)
     */
    public void setTarget(Object t) {
        super.setTarget(t);
        
        criticClass.setText(getTarget().getClass().getCanonicalName());
        
        Critic c = (Critic) getTarget();
        name.setText(c.getCriticName());
        headline.setText(c.getHeadline());
        description.setText(c.getDescriptionTemplate());
        supportedDecision.setText(""+c.getSupportedDecisions());
        
        priority.setText(""+c.getPriority());
        knowledgeType.setText("" + c.getKnowledgeTypes());
    }
}
