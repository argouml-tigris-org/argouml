// Copyright (c) 1996-99 The Regents of the University of California. All
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
 * ToolBox.java
 *
 * Created on 09 February 2003, 14:36
 * @author Bob Tarling
 */

package org.argouml.swingext;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPopupMenu;

import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.FocusEvent;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * A button which produces a dropdown toolbox designed to contain
 * further buttons.
 *
 * @author  Bob Tarling
 */
public class UglyToolBox extends JButton {
    int _rows, _cols;
    JButton _target;
    ArrayList _actions = new ArrayList();
    
    /** Creates a new instance of ToolBox */
    public UglyToolBox(Icon icon, JButton target, int rows, int cols) {
        super(icon);
        _target = target;
        _rows = rows;
        _cols = cols;
        _actions = new ArrayList();
        
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                final JPopupMenu popup = new JPopupMenu();
                Toolbar dropDownToolBox = new Toolbar();
                dropDownToolBox.setLayout(new GridLayout(_rows,_cols));
                Iterator it = _actions.iterator();
                while(it.hasNext()) {
                    final Action a = (Action)it.next();
                    JButton button = dropDownToolBox.add(a);
                    button.addMouseListener(new MouseAdapter() {
                        public void mouseClicked(MouseEvent e) {
                            popup.setVisible(false);
                            _target.setAction(a);
                        }
                    });
                }

                popup.add(dropDownToolBox);
                popup.show(_target, 0, (int)_target.getPreferredSize().getHeight());
            }
        });
    }

    public void add(Action a) {
        _actions.add(a);
    }
}
