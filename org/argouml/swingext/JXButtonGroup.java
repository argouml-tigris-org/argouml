// $Id$
// Copyright (c) 2006 The Regents of the University of California. All
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

package org.argouml.swingext;

import javax.swing.*;
import java.util.*;
import java.util.List;
import java.awt.*;

/**
 * @author Alexander Potochkin
 * https://swinghelper.dev.java.net/
 */
class JXButtonGroup extends ButtonGroup {
    /**
     * The UID
     */
    private static final long serialVersionUID = 68759405663671804L;
    private List buttonList = new ArrayList();
    private ButtonModel selection;

    public JXButtonGroup() {
    }

    public void setSelected(ButtonModel m, boolean b) {
        if (m == null || m.isSelected() == b) {
            return;
        }
        if (b && m != selection) {
            ButtonModel oldSelection = selection;
            selection = m;
            if (oldSelection != null) {
                oldSelection.setSelected(false);
            }
            m.setSelected(true);
        }
    }
    
    public void add(JRadioButton b) {
        if (b == null) {
            return;
        }
        buttonList.add(b);

        if (b.isSelected()) {
            if (selection == null) {
                selection = b.getModel();
            } else {
                b.setSelected(false);
            }
        }

        b.getModel().setGroup(this);
    }

    public void remove(JRadioButton b) {
        if (b == null) {
            return;
        }
        buttonList.remove(b);
        if (b.getModel() == selection) {
            selection = null;
        }
        b.getModel().setGroup(null);
    }

    public void removeAll() {
        buttonList.clear();
        selection = null;
    }

    public void clearSelection() {
        if (selection != null) {
            ButtonModel oldSelection = selection;
            selection = null;
            oldSelection.setSelected(false);
        }
    }

    public boolean contains(Component c) {
        return buttonList.contains(c);
    }

    public ButtonModel getSelection() {
        return selection;
    }

    public boolean isSelected(ButtonModel m) {
        return (m == selection);
    }

    public int getButtonCount() {
        return buttonList.size();
    }

    public JRadioButton getButton(int i) {
        return (JRadioButton) buttonList.get(i);
    }

    public List getButtons() {
        return buttonList;
    }
}
