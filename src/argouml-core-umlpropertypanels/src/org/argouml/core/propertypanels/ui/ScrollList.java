
package org.argouml.core.propertypanels.ui;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ScrollPaneConstants;

/**
 * A scrollable list of items.<p>
 * This makes sure that there is no horizontal
 * scrollbar (which takes up too much screen real estate) and that sideways
 * scrolling can be achieved instead with arrow keys.
 * The component will automatically expand downward on mouse enter to
 * give the user a view of as many items as possible.
 * 
 * @author Bob Tarling
 */
interface ScrollList {
    JList getList();
}
