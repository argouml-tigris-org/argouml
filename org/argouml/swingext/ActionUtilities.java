/*
 * ActionUtilities.java
 */
package org.argouml.swingext;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;

import javax.swing.JPopupMenu;

/**
 * A collection of utility methods for Swing Actions.
 *
 * @author Eugenio Alvarez
 */
public class ActionUtilities {

  private ActionUtilities() {
      throw new UnsupportedOperationException("ActionUtilities is just a container for static methods");
  }
  /**
   * Intended for use inside an <code>actionPerformed</code> method eg:
   * <pre>
   *     public void actionPerformed(ActionEvent ae) {
   *         Container appRoot = ActionUtilities.getActionRoot(ae);
   *     }
   * </pre>
   * Returns the root object, usually a <code>JFrame, JDialog or JApplet</code> that is the owner
   * of the source event source object (JMenu, JMenuItem, JPopupMenu etc).
   */
  public static Container getActionRoot(ActionEvent ae) {
    return ActionUtilities.getActionRoot(ae.getSource());
  } // getActionRoot()

  /**
   * Intended for use inside an <code>actionPerformed</code> method eg:
   * <pre>
   *     public void actionPerformed(ActionEvent e) {
   *         Container appRoot = ActionUtilities.getActionRoot(e.getSource());
   *     }
   * </pre>
   * @return the root object, usually a JFrame, JDialog or JApplet
   *	     that is the owner of the source event source object 
   *         (JMenu, JMenuItem, JPopupMenu etc).
   *         null if none is found.
   */
  public static Container getActionRoot(Object source) {
      Container container = null;
      if (source instanceof Component) {
        Component component = (Component)source;
        container = ActionUtilities.getContainer(component);
        if (container == null) {
          if (source instanceof Container) {
            return (Container)source;
          } // end if
          return null;
        } // end if
        while(ActionUtilities.getContainer(container) != null) {
          container = ActionUtilities.getContainer(container);
        } // end while
      } // end if
      return container;
  } // end getActionRoot()

  /**
   * Helper method to find the <code>Container</code> of <code>Component</code>.
   */
  private static Container getContainer(Component source) {
      Container container = source.getParent();
      if (container != null) {
        return container;
      }
      if (source instanceof JPopupMenu) {
        JPopupMenu jPopupMenu = (JPopupMenu)source;
        Component component = jPopupMenu.getInvoker();
        if (component instanceof Container) {
            container = (Container)component;
        } // end if
      } // end if
      return container;
  } // end getContainer()

} // end class ActionUtilities


