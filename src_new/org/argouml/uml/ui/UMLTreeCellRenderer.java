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

package org.argouml.uml.ui;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.apache.log4j.Category;
import org.argouml.application.helpers.ResourceLoaderWrapper;

import ru.novosoft.uml.foundation.core.MComment;
import ru.novosoft.uml.foundation.core.MModelElement;

/**
 * UMTreeCellRenderer determines how the entries in the Navigationpane
 * and ToDoList will be represented graphically.
 *
 * <p>In particular it makes decisions about the icons
 *  to use in order to display a Navigationpane artifact depending on the kind
 *  of object to be displayed.
 */
public class UMLTreeCellRenderer extends DefaultTreeCellRenderer {
    
    protected static Category cat =
    Category.getInstance(UMLTreeCellRenderer.class);
    
    ////////////////////////////////////////////////////////////////
    // TreeCellRenderer implementation
    
    public Component getTreeCellRendererComponent(JTree tree,
                                                  Object value,
                                                  boolean sel, 
                                                  boolean expanded, 
                                                  boolean leaf, 
                                                  int row, 
                                                  boolean hasFocus) {
        
        Component r = super.getTreeCellRendererComponent(tree, 
                                                         value, 
                                                         sel, 
                                                         expanded, 
                                                         leaf, 
                                                         row, 
                                                         hasFocus);
        
        if (r instanceof JLabel) {
            JLabel lab = (JLabel) r;
            Icon icon = ResourceLoaderWrapper.getResourceLoaderWrapper()
                                             .lookupIcon(value);
            if (icon != null)
                lab.setIcon(icon);
            else
                cat.warn("UMLTreeCellRenderer: using default Icon");
            
            // setting the tooltip
            String tip;
            if (value instanceof MModelElement)
                tip = ((MModelElement) value).getUMLClassName() + ": " + 
                            ((MModelElement) value).getName() + " ";
            else
                tip = (value == null) ? "null " : value.toString() + " ";
                
                lab.setToolTipText(tip);
                //tree.setToolTipText(tip);
        }
        return r;
    }
    
} /* end class UMLTreeCellRenderer */
