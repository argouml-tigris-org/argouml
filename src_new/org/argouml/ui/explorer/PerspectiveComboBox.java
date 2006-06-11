// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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

package org.argouml.ui.explorer;

import javax.swing.JComboBox;

/**
 * Listens to updates from the Perspective manager. This class should be
 * refactored so that this functionality is done via the combobox model.
 *
 * @author  alexb
 * @since 0.15.2
 */
public class PerspectiveComboBox
    extends JComboBox
    implements PerspectiveManagerListener {

    /** Creates a new instance of PerspectiveCombobox */
    public PerspectiveComboBox() {
        /* The default nr of rows is 8, 
         * but since we have 9 perspectives by default now, 
         * setting to 9 is nicer. */
        this.setMaximumRowCount(9);
        PerspectiveManager.getInstance().addListener(this);
    }

    /**
     * @see org.argouml.ui.explorer.PerspectiveManagerListener#addPerspective(java.lang.Object)
     */
    public void addPerspective(Object perspective) {
        addItem(perspective);
    }

    /**
     * @see org.argouml.ui.explorer.PerspectiveManagerListener#removePerspective(java.lang.Object)
     */
    public void removePerspective(Object perspective) {
        removeItem(perspective);
    }
}
