// $Id:GoalsDialog.java 12950 2007-07-01 08:10:04Z tfmorris $
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

package org.argouml.cognitive.ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Hashtable;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.argouml.cognitive.Designer;
import org.argouml.cognitive.Goal;
import org.argouml.cognitive.GoalModel;
import org.argouml.cognitive.Translator;
import org.argouml.util.ArgoDialog;


/**
 * The dialog to set the user's goals.
 *
 */
public class GoalsDialog extends ArgoDialog implements ChangeListener {

    private static final int DIALOG_WIDTH = 320;
    private static final int DIALOG_HEIGHT = 400;


    private JPanel mainPanel = new JPanel();

    private Hashtable<JSlider, Goal> slidersToGoals = 
        new Hashtable<JSlider, Goal>();

    private Hashtable<JSlider, JLabel> slidersToDigits = 
        new Hashtable<JSlider, JLabel>();

    /**
     * The constructor.
     */
    public GoalsDialog() {
	super(Translator.localize("dialog.title.design-goals"), false);

	initMainPanel();

	JScrollPane scroll = new JScrollPane(mainPanel);
	scroll.setPreferredSize(new Dimension(DIALOG_WIDTH, DIALOG_HEIGHT));

	setContent(scroll);
    }


    private void initMainPanel() {
	GoalModel gm = Designer.theDesigner().getGoalModel();
	List<Goal> goals = gm.getGoalList();

	GridBagLayout gb = new GridBagLayout();
	mainPanel.setLayout(gb);
	mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

	GridBagConstraints c = new GridBagConstraints();
	c.fill = GridBagConstraints.BOTH;
	c.weightx = 1.0;
	c.weighty = 0.0;
	c.ipadx = 3; c.ipady = 3;



	//     c.gridy = 0;
	//     c.gridx = 0;
	//     JLabel priLabel = new JLabel("Priority:");
	//     gb.setConstraints(priLabel, c);
	//     _mainPanel.add(priLabel);

	//     c.gridy = 0;
	//     c.gridx = 1;
	//     JLabel offLabel = new JLabel("Off");
	//     gb.setConstraints(offLabel, c);
	//     _mainPanel.add(offLabel);

	//     c.gridy = 0;
	//     c.gridx = 2;
	//     JLabel lowLabel = new JLabel("Low");
	//     gb.setConstraints(lowLabel, c);
	//     _mainPanel.add(lowLabel);

	//     c.gridy = 0;
	//     c.gridx = 3;
	//     JLabel twoLabel = new JLabel("ad");
	//     gb.setConstraints(twoLabel, c);
	//     _mainPanel.add(twoLabel);

	//     c.gridy = 0;
	//     c.gridx = 4;
	//     JLabel threeLabel = new JLabel("asd");
	//     gb.setConstraints(threeLabel, c);
	//     _mainPanel.add(threeLabel);

	//     c.gridy = 0;
	//     c.gridx = 5;
	//     JLabel fourLabel = new JLabel("asd");
	//     gb.setConstraints(fourLabel, c);
	//     _mainPanel.add(fourLabel);

	//     c.gridy = 0;
	//     c.gridx = 6;
	//     JLabel highLabel = new JLabel("High");
	//     gb.setConstraints(highLabel, c);
	//     _mainPanel.add(highLabel);


	c.gridy = 1;
        for (Goal goal : goals) {
	    JLabel decLabel = new JLabel(goal.getName());
	    JLabel valueLabel = new JLabel("    " + goal.getPriority());
	    JSlider decSlide =
                new JSlider(SwingConstants.HORIZONTAL,
                            0, 5, goal.getPriority());
	    decSlide.setPaintTicks(true);
	    decSlide.setPaintLabels(true);
	    decSlide.addChangeListener(this);
	    Dimension origSize = decSlide.getPreferredSize();
	    Dimension smallSize =
		new Dimension(origSize.width / 2, origSize.height);
	    decSlide.setSize(smallSize);
	    decSlide.setPreferredSize(smallSize);

	    slidersToGoals.put(decSlide, goal);
	    slidersToDigits.put(decSlide, valueLabel);

	    c.gridx = 0;
	    c.gridwidth = 1;
	    c.weightx = 0.0;
	    c.ipadx = 3;
	    gb.setConstraints(decLabel, c);
	    mainPanel.add(decLabel);

	    c.gridx = 1;
	    c.gridwidth = 1;
	    c.weightx = 0.0;
	    c.ipadx = 0;
	    gb.setConstraints(valueLabel, c);
	    mainPanel.add(valueLabel);

	    c.gridx = 2;
	    c.gridwidth = 6;
	    c.weightx = 1.0;
	    gb.setConstraints(decSlide, c);
	    mainPanel.add(decSlide);

	    c.gridy++;
	}
    }

    /*
     * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
     */
    public void stateChanged(ChangeEvent ce) {
	JSlider srcSlider = (JSlider) ce.getSource();
	Goal goal = slidersToGoals.get(srcSlider);
	JLabel valLab = slidersToDigits.get(srcSlider);
	int pri = srcSlider.getValue();
	goal.setPriority(pri);
	if (pri == 0) {
	    valLab.setText(Translator.localize("label.off"));
	} else {
	    valLab.setText("    " + pri);
	}
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = -1871200638199122363L;
}