package org.argouml.swingext;

import javax.swing.JComponent;
import java.awt.*;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author Bob Tarling
 * @version 1.0
 */

public class BorderSplitPane extends JComponent {
    final public static String NORTH = "North";
    final public static String SOUTH = "South";
    final public static String EAST = "East";
    final public static String WEST = "West";
    final public static String CENTER = "Center";
    final public static String SOUTHWEST = "SouthWest";
    final public static String SOUTHEAST = "SouthEast";
    final public static String NORTHWEST = "NorthWest";
    final public static String NORTHEAST = "NorthEast";

    private MultipleSplitPane outerSplitPane;
    private MultipleSplitPane topSplitPane;
    private MultipleSplitPane middleSplitPane;
    private MultipleSplitPane bottomSplitPane;

    public BorderSplitPane() {
        outerSplitPane = new MultipleSplitPane(3, MultipleSplitPane.VERTICAL_SPLIT);
        setLayout(new BorderLayout());
        super.add(outerSplitPane, BorderLayout.CENTER);

        topSplitPane = new MultipleSplitPane(3);
        middleSplitPane = new MultipleSplitPane(3);
        bottomSplitPane = new MultipleSplitPane(3);

        outerSplitPane.add(middleSplitPane, "100", 1);
    }

    public Component add(Component comp) {
        middleSplitPane.add(comp, "100", 1);
        return comp;
    }

    public void add(Component comp, Object constraints) {
        if (constraints.toString().equals(CENTER)) {
            add(comp);
        }
        else {
            if (constraints.toString().equals(NORTH)) {
                topSplitPane.add(comp, "100", 1);
                if (topSplitPane.getParent() != outerSplitPane) outerSplitPane.add(topSplitPane, 0);
            }
            else if (constraints.toString().equals(NORTHWEST)) {
                topSplitPane.add(comp, 0);
                if (topSplitPane.getParent() != outerSplitPane) outerSplitPane.add(topSplitPane, 0);
            }
            else if (constraints.toString().equals(NORTHEAST)) {
                topSplitPane.add(comp, 2);
                if (topSplitPane.getParent() != outerSplitPane) outerSplitPane.add(topSplitPane, 0);
            }
            else if (constraints.toString().equals(SOUTH)) {
                bottomSplitPane.add(comp, "100", 1);
                if (bottomSplitPane.getParent() != outerSplitPane) outerSplitPane.add(bottomSplitPane, 2);
            }
            else if (constraints.toString().equals(SOUTHWEST)) {
                bottomSplitPane.add(comp, 0);
                if (bottomSplitPane.getParent() != outerSplitPane) outerSplitPane.add(bottomSplitPane, 2);
            }
            else if (constraints.toString().equals(SOUTHEAST)) {
                bottomSplitPane.add(comp, 2);
                if (bottomSplitPane.getParent() != outerSplitPane) outerSplitPane.add(bottomSplitPane, 2);
            }
            else if (constraints.toString().equals(WEST)) {
                middleSplitPane.add(comp, 0);
            }
            else if(constraints.toString().equals(EAST)) {
                middleSplitPane.add(comp, 2);
            }
        }
    }

    public Component add(Component comp, int index) {
        return add(comp);
    }


    public void add(Component comp, Object constraints, int index) {
        add(comp, constraints);
    }
}
