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
 * MultipleSplitPane.java
 */
package org.argouml.swingext;

import javax.swing.*;
import java.awt.*;

/**
 * Similar to the standard java class <code>JSplitPane</code> but allows the number of panes to 
 * be set in the constructor.
 *
 * @author Bob Tarling
 */
public class MultipleSplitPane extends JComponent {
    final static public Orientation HORIZONTAL_SPLIT = Horizontal.getInstance();
    final static public Orientation VERTICAL_SPLIT = Vertical.getInstance();

    private Splitter[] splitterArray;

    public MultipleSplitPane(Component componentArray[]) {
        this(componentArray.length);
    }

    public MultipleSplitPane(Component componentArray[], Orientation orientation) {
        this(componentArray.length, orientation);
    }

    public MultipleSplitPane(int paneCount) {
        this(paneCount, HORIZONTAL_SPLIT);
    }

    public MultipleSplitPane(int paneCount, Orientation orientation) {
        this.setLayout(new SplitterLayout(orientation));
        int splitterCount = paneCount - 1;
        if (splitterCount >= 0) {
            splitterArray = new Splitter[paneCount - 1];
            for (int i = 0; i < splitterCount; ++i) {
                splitterArray[i] = new Splitter(orientation);
                add(splitterArray[i]);
            }
        }
        if (splitterCount > 1) {
            splitterArray[0].setQuickHide(Splitter.WEST);
            splitterArray[splitterCount - 1].setQuickHide(Splitter.EAST);
        }
    }
    public Component add(Component comp, int index) {
        if (!(comp instanceof Splitter)) {
            SplitterLayout splitterLayout = (SplitterLayout) getLayout();
            if (index > 0) {
                index = splitterLayout.getComponentPosition(splitterArray[index - 1]) + 1;
            }
        }
        if (index < this.getComponentCount() && !(this.getComponent(index) instanceof Splitter)) {
            super.remove(index);
        }
        return super.add(comp, index);
    }

    public void add(Component comp, Object constraints, int index) {
        if (!(comp instanceof Splitter)) {
            SplitterLayout splitterLayout = (SplitterLayout) getLayout();
            if (index > 0) {
                index = splitterLayout.getComponentPosition(splitterArray[index - 1]) + 1;
            }
        }
        if (index < this.getComponentCount() && !(this.getComponent(index) instanceof Splitter)) {
            super.remove(index);
        }
        super.add(comp, constraints, index);
    }

    public void remove(int index) {
        SplitterLayout splitterLayout = (SplitterLayout) getLayout();
        if (index >= splitterArray.length) {
            index = splitterLayout.getComponentPosition(splitterArray[index - 1]) + 1;
        }
        else {
            index = splitterLayout.getComponentPosition(splitterArray[index]) - 1;
        }
        if (index >= 0 && !(this.getComponent(index) instanceof Splitter)) super.remove(index);
    }
}
