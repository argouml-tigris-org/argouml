/*
 * DropDownIcon.java
 *
 * Created on 25 February 2003, 22:12
 */

package org.argouml.swingext;

import javax.swing.ImageIcon;

/**
 *
 * @author  Administrator
 */
public class DropDownIcon extends DecoratedIcon {

    private static final int[][] standardBuffer = {
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 2, 2, 2, 2, 2, 2, 2, 2, 0, 0},
        {0, 0, 1, 1, 1, 1, 1, 1, 3, 3, 0},
        {0, 0, 0, 1, 1, 1, 1, 3, 3, 0, 0},
        {0, 0, 0, 0, 1, 1, 3, 3, 0, 0, 0},
        {0, 0, 0, 0, 0, 3, 3, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
    };


    /** Creates a new instance of DropDownIcon */
    public DropDownIcon(ImageIcon imageIcon) {
        super(imageIcon);
        init(standardBuffer);
    }

    /** Creates a new instance of DropDownIcon */
    public DropDownIcon(ImageIcon imageIcon, boolean showSplitter) {
        super(imageIcon, showSplitter);
        init(standardBuffer);
    }
}
