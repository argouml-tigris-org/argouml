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

// Copyright (c) 2006-2007 The Regents of the University of California. All
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

package org.argouml.dev;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.ui.targetmanager.TargetListener;
import org.argouml.ui.targetmanager.TargetManager;

/**
 * A debug panel for the TargetManager. It shows the actual Targets.
 *
 * @author Michiel
 */
public class TargetManagerPanel extends JPanel implements TargetListener {

    /**
     * The UID.
     */
    private static final long serialVersionUID = -4827194145585220207L;

    private DefaultListModel lm = new DefaultListModel();
    private JList lst;
    private String lastEvent;

    /**
     * The instance.
     */
    private static final TargetManagerPanel INSTANCE = new TargetManagerPanel();

    /**
     * @return the instance.
     */
    public static TargetManagerPanel getInstance() {
        return INSTANCE;
    }

    /**
     * The constructor.
     */
    public TargetManagerPanel() {
        setLayout(new BorderLayout());

        lst = new JList(lm);
        setTarget(TargetManager.getInstance().getTargets());
        add(new JScrollPane(lst), BorderLayout.CENTER);
        lst.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
        TargetManager.getInstance().addTargetListener(this);
        add(new JButton(new ClearAction("Clear")), BorderLayout.SOUTH);
    }

    class ClearAction extends AbstractAction {

        /**
         * @param name the name
         */
        public ClearAction(String name) {
            super(name);
        }

        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {
            lm.clear();
        }
    }

    /*
     * @see org.argouml.ui.targetmanager.TargetListener#targetAdded(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetAdded(TargetEvent e) {
        lastEvent = "targetAdded";
        setTarget(e.getNewTargets());
    }

    /*
     * @see org.argouml.ui.targetmanager.TargetListener#targetRemoved(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetRemoved(TargetEvent e) {
        lastEvent = "targetRemoved";
        setTarget(e.getNewTargets());
    }

    /*
     * @see org.argouml.ui.targetmanager.TargetListener#targetSet(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetSet(TargetEvent e) {
        lastEvent = "targetSet";
        setTarget(e.getNewTargets());
    }

    private void setTarget(Object[] t) {
        Collection c = new ArrayList(Arrays.asList(t));
        setTarget(c);
    }

    private void setTarget(Collection c) {
        lm.addElement("***Last event: " + lastEvent);
        Iterator i = c.iterator();
        while (i.hasNext()) {
            lm.addElement(i.next());
        }
    }

}
