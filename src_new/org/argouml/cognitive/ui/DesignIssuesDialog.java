// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.argouml.cognitive.Decision;
import org.argouml.cognitive.DecisionModel;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.Translator;
import org.argouml.ui.ArgoDialog;


/** A dialog to set the priorities for decisions. These will be evaluated
 *  against the critics, so that the user will only see todo items which match
 *  the priorities for a certain decision.
 */
public class DesignIssuesDialog extends ArgoDialog implements ChangeListener {

    ////////////////////////////////////////////////////////////////
    // constants
    ////////////////////////////////////////////////////////////////
    // instance variables
    private JPanel  mainPanel = new JPanel();
    private Hashtable slidersToDecisions = new Hashtable();
    private Hashtable slidersToDigits = new Hashtable();

    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     * The constructor.
     */
    public DesignIssuesDialog() {
        super(Translator.localize("dialog.title.design-issues"), false);

        final int width = 320;
        final int height = 400;

        initMainPanel();

        JScrollPane scroll = new JScrollPane(mainPanel);
        scroll.setPreferredSize(new Dimension(width, height));

        setContent(scroll);
    }


    private void initMainPanel() {
        DecisionModel dm = Designer.theDesigner().getDecisionModel();
        Vector decs = dm.getDecisions();

        GridBagLayout gb = new GridBagLayout();
        mainPanel.setLayout(gb);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 0.0;
        c.ipadx = 3; c.ipady = 3;


        c.gridy = 0;
        c.gridx = 0;
        c.gridwidth = 1;
        JLabel decTitleLabel = new JLabel(
            Translator.localize("label.decision"));
        gb.setConstraints(decTitleLabel, c);
        mainPanel.add(decTitleLabel);

        c.gridy = 0;
        c.gridx = 2;
        c.gridwidth = 8;
        JLabel priLabel = new JLabel(
            Translator.localize("label.decision-priority"));
        gb.setConstraints(priLabel, c);
        mainPanel.add(priLabel);

        c.gridy = 1;
        c.gridx = 2;
        c.gridwidth = 2;
        JLabel offLabel = new JLabel(Translator.localize("label.off"));
        gb.setConstraints(offLabel, c);
        mainPanel.add(offLabel);

        c.gridy = 1;
        c.gridx = 4;
        c.gridwidth = 2;
        JLabel lowLabel = new JLabel(Translator.localize("label.low"));
        gb.setConstraints(lowLabel, c);
        mainPanel.add(lowLabel);

        c.gridy = 1;
        c.gridx = 6;
        c.gridwidth = 2;
        JLabel mediumLabel = new JLabel(Translator.localize("label.medium"));
        gb.setConstraints(mediumLabel, c);
        mainPanel.add(mediumLabel);

        c.gridy = 1;
        c.gridx = 8;
        c.gridwidth = 2;
        JLabel highLabel = new JLabel(Translator.localize("label.high"));
        gb.setConstraints(highLabel, c);
        mainPanel.add(highLabel);


        c.gridy = 2;
        Enumeration elems = decs.elements();
        while (elems.hasMoreElements()) {
            Decision d = (Decision) elems.nextElement();
            JLabel decLabel = new JLabel(d.getName());
            JLabel valueLabel = new JLabel(getValueText(d.getPriority()));
            JSlider decSlide =
                new JSlider(SwingConstants.HORIZONTAL,
                            1, 4, (d.getPriority() == 0 ? 4 : d.getPriority()));

            decSlide.setInverted(true);
            decSlide.setMajorTickSpacing(1);
            decSlide.setPaintTicks(true);
            decSlide.setSnapToTicks(true);
            // decSlide.setPaintLabels(true);
            decSlide.addChangeListener(this);
            Dimension origSize = decSlide.getPreferredSize();
            Dimension smallSize =
                new Dimension(origSize.width / 2, origSize.height);
            decSlide.setSize(smallSize);
            decSlide.setPreferredSize(smallSize);

            slidersToDecisions.put(decSlide, d);
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
            c.gridwidth = 8;
            c.weightx = 1.0;
            gb.setConstraints(decSlide, c);
            mainPanel.add(decSlide);

            c.gridy++;
        }
    }

    ////////////////////////////////////////////////////////////////
    // event handlers

    /**
     * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
     */
    public void stateChanged(ChangeEvent ce) {
        JSlider srcSlider = (JSlider) ce.getSource();
        Decision d = (Decision) slidersToDecisions.get(srcSlider);
        JLabel valLab = (JLabel) slidersToDigits.get(srcSlider);
        int pri = srcSlider.getValue();
        d.setPriority((pri == 4) ? 0 : pri);
        valLab.setText(getValueText(pri));
    }

    private String getValueText(int priority) {
        String label = "";
        switch(priority) {
        case 1:
	    label = "    1";
	    break;
        case 2:
	    label = "    2";
	    break;
        case 3:
	    label = "    3";
	    break;
        case 0:
        case 4:
	    label = Translator.localize("label.off");
	    break;
        }
        return label;
    }

} /* end class DesignIssuesDialog */



////////////////////////////////////////////////////////////////
