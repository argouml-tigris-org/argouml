package org.argouml.swingext;

import javax.swing.*;
import java.awt.*;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author Bob Tarling
 * @version 1.0
 */

public class MultipleSplitPane extends JComponent {
    final static public int HORIZONTAL_SPLIT = Orientation.HORIZONTAL;
    final static public int VERTICAL_SPLIT = Orientation.VERTICAL;

    private Splitter[] splitterArray;

    public MultipleSplitPane(Component componentArray[]) {
        this(componentArray.length);
    }

    public MultipleSplitPane(Component componentArray[], int orientation) {
        this(componentArray.length, orientation);
    }

    public MultipleSplitPane(int paneCount) {
        this(paneCount, HORIZONTAL_SPLIT);
    }

    public MultipleSplitPane(int paneCount, int orientation) {
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
