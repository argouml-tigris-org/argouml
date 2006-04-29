// $Id$
// Copyright (c) 2006 The Regents of the University of California. All
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

package org.tigris.gef.undo;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public final class UndoLogPanel extends JScrollPane {

    /**
     * The UID.
     */
    private static final long serialVersionUID = -3483889053389473380L;

    private JPanel list;

    /**
     * The instance.
     */
    private static final UndoLogPanel INSTANCE = new UndoLogPanel();

    /**
     * @return The instance.
     */
    public static UndoLogPanel getInstance() {
        return INSTANCE;
    }

    /**
     * Constructor.
     */
    private UndoLogPanel() {
        list = new JPanel(new GridLayout(0, 1));
        JPanel listContainer = new JPanel(new BorderLayout());
        listContainer.add(BorderLayout.NORTH, list);
        this.setViewportView(listContainer);
    }

    void addMemento(Memento memento) {
        list.add(new JLabel(memento.toString()));
        doLayout();
        validate();
        if (getVerticalScrollBar() != null) {
            int maxScroll = getVerticalScrollBar().getMaximum();
            getVerticalScrollBar().setValue(maxScroll + 1);
        }
    }

    void removeMemento(Memento memento) {
        list.remove(list.getComponentCount() - 1);
        doLayout();
        validate();
    }
}
