// $Id$
// Copyright (c) 1996-99 The Regents of the University of California. All
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

package org.argouml.uml.diagram.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemListener;

import javax.swing.text.Document;

import org.apache.log4j.Logger;
import org.argouml.ui.SpacerPanel;
import org.argouml.ui.StylePanelFig;
import org.tigris.gef.ui.ColorRenderer;

/**
 * The style Panel for FigEdgeModelElement.
 *
 */
public class SPFigEdgeModelElement extends StylePanelFig implements
        ItemListener {

    private static final Logger LOG = 
        Logger.getLogger(SPFigEdgeModelElement.class);

    private SpacerPanel spacer = new SpacerPanel();

    private SpacerPanel spacer2 = new SpacerPanel();

    private SpacerPanel spacer3 = new SpacerPanel();

    /**
     * The constructor.
     * 
     */
    public SPFigEdgeModelElement() {
        super("Edge Appearance");
        initChoices();
        GridBagLayout gb = (GridBagLayout) getLayout();
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.ipadx = 0;
        c.ipady = 0;

        Document bboxDoc = getBBoxField().getDocument();
        bboxDoc.addDocumentListener(this);
        getLineField().addItemListener(this);
        //_dashedField.addItemListener(this);

        getLineField().setRenderer(new ColorRenderer());
        //_dashedField.setRenderer(DashRenderer.SINGLETON);

        c.gridx = 0;
        c.gridwidth = 1;
        c.gridy = 1;
        c.weightx = 0.0;
        gb.setConstraints(getBBoxLabel(), c);
        add(getBBoxLabel());
        c.gridy = 2;
        gb.setConstraints(getLineLabel(), c);
        add(getLineLabel());
        //c.gridy = 3;
        //gb.setConstraints(_dashedLabel, c);
        //add(_dashedLabel);

        c.weightx = 1.0;
        c.gridx = 1;
        //c.gridwidth = GridBagConstraints.REMAINDER;
        c.gridy = 1;
        gb.setConstraints(getBBoxField(), c);
        add(getBBoxField());
        c.gridy = 2;
        gb.setConstraints(getLineField(), c);
        add(getLineField());
        //c.gridy = 3;
        //gb.setConstraints(_dashedField, c);
        //add(_dashedField);

        c.weightx = 0.0;
        c.gridx = 2;
        c.gridy = 1;
        gb.setConstraints(spacer, c);
        add(spacer);

        c.gridx = 3;
        c.gridy = 10;
        gb.setConstraints(spacer2, c);
        add(spacer2);

        c.weightx = 1.0;
        c.gridx = 4;
        c.gridy = 10;
        gb.setConstraints(spacer3, c);
        add(spacer3);
    }

} /* end class SPFigEdgeModelElement */

