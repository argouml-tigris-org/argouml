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

// $header$
package org.argouml.uml.ui;

import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * @since Oct 2, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public abstract class UMLList2 extends JList implements TargetChangedListener, ListSelectionListener {

    /**
     * Constructor for UMLList2.
     * @param dataModel
     * @param showIcon true if an icon should be shown representing the
     * modelelement in each cell.
     */
    public UMLList2(UMLModelElementListModel2 dataModel, boolean showIcon) {
        super(dataModel);
        setDoubleBuffered(true);
        getSelectionModel().addListSelectionListener(this);
        setCellRenderer(new UMLListCellRenderer2(showIcon));
    }

    public UMLList2(UMLModelElementListModel2 dataModel) {
        this(dataModel, false);
    }

	/**
	 * Constructor for UMLList2. Used by subclasses that want to add their own
	 * renderer to the list.
	 * @param dataModel
	 * @param renderer
	 */
    protected UMLList2(UMLModelElementListModel2 dataModel, ListCellRenderer renderer) {
        super(dataModel);
        setDoubleBuffered(true);
        getSelectionModel().addListSelectionListener(this);
        if (renderer != null)
            setCellRenderer(renderer);
    }    

    /**
     * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
     */
    public void valueChanged(ListSelectionEvent e) {
        if (e.getFirstIndex() >= 0) {
            doIt(e);
        }
    }

    /**
     * The 'body' of the valueChanged method. Is only called if there is
     * actually a selection made.
     * @param event
     */
    protected abstract void doIt(ListSelectionEvent e);

    /**
     * Getter for the target. First approach to get rid of the container.
     * @return Object
     */
    public Object getTarget() {
        return ((UMLModelElementListModel2) getModel()).getTarget();
    }

    /**
     * @see org.argouml.uml.ui.TargetChangedListener#targetChanged(java.lang.Object)
     */
    public void targetChanged(Object newTarget) {
        ((UMLModelElementListModel2) getModel()).targetChanged(newTarget);
    }

    /**
     * @see org.argouml.uml.ui.TargetChangedListener#targetReasserted(java.lang.Object)
     */
    public void targetReasserted(Object newTarget) {
        ((UMLModelElementListModel2) getModel()).targetReasserted(newTarget);
    }

}
