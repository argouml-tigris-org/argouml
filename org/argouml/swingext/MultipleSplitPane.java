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
        int splitterCount = paneCount-1;
        if (splitterCount >= 0) {
            splitterArray = new Splitter[paneCount-1];
            for (int i=0; i<splitterCount; ++i) {
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
                index = splitterLayout.getComponentPosition(splitterArray[index-1]) + 1;
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
                index = splitterLayout.getComponentPosition(splitterArray[index-1]) + 1;
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
            index = splitterLayout.getComponentPosition(splitterArray[index-1]) + 1;
        }
        else {
            index = splitterLayout.getComponentPosition(splitterArray[index]) - 1;
        }
        if (index >= 0 && !(this.getComponent(index) instanceof Splitter)) super.remove(index);
    }
}
