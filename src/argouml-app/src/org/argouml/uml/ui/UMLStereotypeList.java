/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2011 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Linus Tolke
 *    Laurant Braud
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 1996-2007 The Regents of the University of California. All
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

// $header$
package org.argouml.uml.ui;

import java.awt.Color;

import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;

import org.argouml.ui.LookAndFeelMgr;
import org.argouml.ui.targetmanager.TargetListener;
import org.argouml.ui.targetmanager.TargettableModelView;

/**
 * A JList for display stereotypes to the user.
 * 
 * This implements 'jump' behaviour. As soon as the user
 * doubleclicks on an element in the list, that element is selected in
 * argouml. <p>
 *
 * Also, it allows showing an icon with the text items in the list.<p>
 *
 * And, in case the listed item has no name, a default name is generated.
 *
 * @since Oct 2, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
class UMLStereotypeList extends JList implements TargettableModelView {


    /**
     * Constructor for UMLLinkedList.
     *
     * @param dataModel the data model
     * @param showIcon true if an icon should be shown
     * @param showPath true if containment path should be shown
     */
    public UMLStereotypeList(ListModel dataModel, boolean showIcon, boolean showPath) {
        super(dataModel);
        //
        ListCellRenderer renderer = new UMLLinkedListCellRenderer(showIcon, showPath);
        setDoubleBuffered(true);
        if (renderer != null) {
            setCellRenderer(renderer);
        }
        setFont(LookAndFeelMgr.getInstance().getStandardFont());
        
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // TODO: Can we find a better way to do this than hard coding colour?
        setForeground(Color.blue);
        setSelectionForeground(Color.blue.darker());
        
        UMLLinkMouseListener mouseListener = new UMLLinkMouseListener(this);
        addMouseListener(mouseListener);
    }
    

    /**
     * The constructor.
     *
     * @param dataModel the data model
     */
    public UMLStereotypeList(ListModel dataModel) {
        this(dataModel, true);
    }

    /**
     * Constructor for UMLLinkedList.
     *
     * @param dataModel the data model
     * @param showIcon true if an icon should be shown
     */
    public UMLStereotypeList(ListModel dataModel,
            boolean showIcon) {
        this(dataModel, showIcon, true);
    }
    
    /**
     * Getter for the target. First approach to get rid of the container.
     * @return Object
     */
    public Object getTarget() {
        
        return ((UMLStereotypeListModel) getModel()).getTarget();
    }
    
    /*
     * @see TargettableModelView#getTargettableModel()
     */
    public TargetListener getTargettableModel() {
        return (TargetListener) getModel();
    }
}
