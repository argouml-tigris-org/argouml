// $Id: FigInspectorPanel.java 11132 2006-09-06 23:29:12Z bobtarling $
// Copyright (c) 2006 The Regents of the University of California. All
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

package org.argouml.model.mdr;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import org.argouml.model.Model;

/**
 * The event pump inspector displays the contents of the event
 * pump. There is no event mechanism to refresh this panel. The
 * user must press the refresh button to see the latest state.
 * @author Bob Tarling
 */
public final class EventPumpInspectorPanel extends JPanel {

    /**
     * The UID.
     */
    private static final long serialVersionUID = -7217414480003857977L;

    /**
     * The instance.
     */
    private static final EventPumpInspectorPanel INSTANCE =
        new EventPumpInspectorPanel();

    /**
     * @return The instance.
     */
    public static EventPumpInspectorPanel getInstance() {
        return INSTANCE;
    }

    private JScrollPane scroller;
    
    /**
     * Constructor.
     */
    private EventPumpInspectorPanel() {
        setLayout(new BorderLayout());
        this.add(new JButton(new RefreshAction()), BorderLayout.NORTH);
    }
    
    private class RefreshAction extends AbstractAction {
    	
        /**
         * UID
         */
        private static final long serialVersionUID = -2527310696451597274L;

        RefreshAction() {
            super("Refresh");
        }

        public void actionPerformed(ActionEvent arg0) {
            JTree tree = new JTree(getTree());
            tree.expandRow(0);
            if (scroller != null) {
                remove(scroller);
            }
            scroller = new JScrollPane(tree);
            add(scroller);
            invalidate();
            validate();
        }
    }
    
    
    /**
     * Getter provided for the dev module. This supplies a tree
     * model of registered listeners.
     * @return a root tree node
     */
    TreeNode getTree() {
        ModelEventPumpMDRImpl mep = (ModelEventPumpMDRImpl) Model.getPump();
        Registry<PropertyChangeListener> elements = mep.getElements();
        DefaultMutableTreeNode root =
            new DefaultMutableTreeNode("registry");
        for (Iterator it = elements.registry.entrySet().iterator(); 
                it.hasNext(); ) {
            Map.Entry entry = (Map.Entry) it.next();
            String item = entry.getKey().toString();
            DefaultMutableTreeNode modelElementNode =
                new DefaultMutableTreeNode(getDescr(item));
            root.add(modelElementNode);
            Map propertyMap = (Map) entry.getValue();
            for (Iterator propertyIterator = propertyMap.entrySet().iterator(); 
                    propertyIterator.hasNext();) {
                Map.Entry propertyEntry = (Map.Entry) propertyIterator.next();
                DefaultMutableTreeNode propertyNode =
                    new DefaultMutableTreeNode(propertyEntry.getKey());
                modelElementNode.add(propertyNode);

                List listenerList = (List) propertyEntry.getValue();
                for (Iterator listIt = listenerList.iterator();
                        listIt.hasNext(); ) {
                    Object listener = listIt.next();
                    DefaultMutableTreeNode listenerNode =
                        new DefaultMutableTreeNode(
                                listener.getClass().getName());
                    propertyNode.add(listenerNode);
                }
            }
        }

        return root;
    }

    private String getDescr(String mofId) {
        ModelEventPumpMDRImpl mep = (ModelEventPumpMDRImpl) Model.getPump();
        Object modelElement = mep.getByMofId(mofId);
        String descr = Model.getFacade().getName(modelElement);
        if (descr != null && descr.trim().length() != 0) {
            return "\"" + descr + "\" - " + modelElement.toString();
        } else {
            return modelElement.toString();
        }
    }
}
