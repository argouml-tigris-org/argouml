// $Id$
// Copyright (c) 2003 The Regents of the University of California. All
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

/*
 * PopupToolBox.java
 *
 * Created on 23 February 2003, 09:59
 */

package org.argouml.swingext;

import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.Action;
import javax.swing.JButton;

/**
 *
 * @author  Bob Tarling
 */
public class PopupToolBox extends Toolbox {

    ArrayList _actions = new ArrayList();
    MouseListener _mouseListener;

    /** Creates a new instance of PopupToolBox */
    public PopupToolBox(int rows, int cols) {
        super(rows, cols);
    }

    public JButton add(Action action) {
        JButton button = super.add(action);
        _actions.add(action);
        
        return button;
    }

    public void setButtonMouseListener(MouseListener mouseListener) {
        _mouseListener = mouseListener;
    }

    /**
     * Occasionally the ToolBox gets in a state where a button
     * shows rollover status at the wrong time.
     * The only way to get around this is to rebuild the ToolBox.
     */
    public void rebuild() {
        super.removeAll();
        Iterator it = _actions.iterator();
        while (it.hasNext()) {
            Action a = (Action) it.next();
            JButton button = super.add(a);
            if (_mouseListener != null) {
                button.addMouseListener(_mouseListener);
            }
        }
    }
}
