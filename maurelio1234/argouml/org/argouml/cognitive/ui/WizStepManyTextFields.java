// $Id: WizStepManyTextFields.java 12950 2007-07-01 08:10:04Z tfmorris $
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

package org.argouml.cognitive.ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import org.argouml.cognitive.critics.Wizard;
import org.argouml.swingext.SpacerPanel;


/**
 * A non-modal wizard step that shows instructions and prompts
 * the user to enter a series of strings in textfields.
 *
 * @see org.argouml.cognitive.Critic
 * @see org.argouml.cognitive.critics.Wizard
 */

public class WizStepManyTextFields extends WizStep {
    private JTextArea instructions = new JTextArea();
    private List<JTextField> fields = new ArrayList<JTextField>();

    /**
     * The constructor.
     *
     * @param w the wizard
     * @param instr  the instructions
     * @param strings the strings
     */
    public WizStepManyTextFields(Wizard w, String instr, List strings) {
	// store wizard?
	instructions.setText(instr);
	instructions.setWrapStyleWord(true);
        instructions.setLineWrap(true);
	instructions.setEditable(false);
	instructions.setBorder(null);
	instructions.setBackground(getMainPanel().getBackground());


	getMainPanel().setBorder(new EtchedBorder());

	GridBagLayout gb = new GridBagLayout();
	getMainPanel().setLayout(gb);

	GridBagConstraints c = new GridBagConstraints();
	c.ipadx = 3; c.ipady = 3;
	c.weightx = 0.0; c.weighty = 0.0;
	c.anchor = GridBagConstraints.EAST;

	JLabel image = new JLabel("");
	//image.setMargin(new Insets(0, 0, 0, 0));
	image.setIcon(getWizardIcon());
	image.setBorder(null);
	c.gridx = 0;
	c.gridheight = GridBagConstraints.REMAINDER;
	c.gridy = 0;
	c.anchor = GridBagConstraints.NORTH;
	gb.setConstraints(image, c);
	getMainPanel().add(image);

	c.weightx = 0.0;
	c.gridx = 2;
	c.gridheight = 1;
	c.gridwidth = 3;
	c.gridy = 0;
	c.fill = GridBagConstraints.NONE;
	gb.setConstraints(instructions, c);
	getMainPanel().add(instructions);

	c.gridx = 1;
	c.gridy = 1;
	c.weightx = 0.0;
	c.gridwidth = 1;
	c.fill = GridBagConstraints.NONE;
	SpacerPanel spacer = new SpacerPanel();
	gb.setConstraints(spacer, c);
	getMainPanel().add(spacer);

	c.gridx = 2;
	c.weightx = 1.0;
	c.anchor = GridBagConstraints.WEST;
	c.gridwidth = 1;
	int size = strings.size();
	for (int i = 0; i < size; i++) {
	    c.gridy = 2 + i;
	    String s = (String) strings.get(i);
	    JTextField tf = new JTextField(s, 50);
	    tf.setMinimumSize(new Dimension(200, 20));
	    tf.getDocument().addDocumentListener(this);
	    fields.add(tf);
	    gb.setConstraints(tf, c);
	    getMainPanel().add(tf);
	}

	c.gridx = 1;
	c.gridy = 3 + strings.size();
	c.weightx = 0.0;
	c.gridwidth = 1;
	c.fill = GridBagConstraints.NONE;
	SpacerPanel spacer2 = new SpacerPanel();
	gb.setConstraints(spacer2, c);
	getMainPanel().add(spacer2);

    }

    /**
     * @return the strings
     * @deprecated for 0.25.4 by tfmorris.  Use {@link #getStringList()}.
     */
    @Deprecated
    public Vector<String> getStrings() {
        return new Vector<String>(getStringList());
    }

    /**
     * @return the strings
     */
    public List<String> getStringList() {
        List<String> result = new ArrayList<String>(fields.size());
        for (JTextField tf : fields) {
            result.add(tf.getText());
        }
        return result;
    }
    
    /**
     * The UID.
     */
    private static final long serialVersionUID = -5154002407806917092L;
} /* end class WizStepManyTextFields */



