// Copyright (c) 1996-2001 The Regents of the University of California. All
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

package org.argouml.language.ui;
import org.argouml.application.api.*;
import org.argouml.application.events.*;

import java.awt.*;
import java.util.*;
import javax.swing.*;

/**
 *   This class provides a self-updating notation combo box.
 *
 *   @author Thierry Lach
 *   @since 0.9.4
 */
public class NotationComboBox extends JComboBox
implements ArgoNotationEventListener {

    private static NotationComboBox SINGLETON = null;

    public static NotationComboBox getInstance() {
        // Only instantiate when we need it.
	if (SINGLETON == null) SINGLETON = new NotationComboBox();
        return SINGLETON;
    }

    public NotationComboBox() {
        super();
	setEditable(false);
	setMaximumRowCount(6);

	Dimension d = getPreferredSize();
	d.width = 200;
	setMaximumSize(d);

        ArgoEventPump.addListener(ArgoEventTypes.ANY_NOTATION_EVENT, this);
	refresh();
    }

    public void notationChanged(ArgoNotationEvent event) {
        //Notation.cat.debug("NotationComboBox.notationChanged(" + event + ")");
        //Argo.log.info("NotationComboBox.notationChanged(" + event + ")");
        refresh();
    }
    public void notationAdded(ArgoNotationEvent event) {
        //Argo.log.info("NotationComboBox.notationAdded(" + event + ")");
        refresh();
	}
    public void notationRemoved(ArgoNotationEvent event) { }
    public void notationProviderAdded(ArgoNotationEvent event) { }
    public void notationProviderRemoved(ArgoNotationEvent event) { }

    public void refresh() {
        if (Configuration.getBoolean(Notation.KEY_UML_NOTATION_ONLY, false)) {
	    setVisible(false);
	}
	else {
	    removeAllItems();
            ListIterator iterator = Notation.getAvailableNotations().listIterator();
            while (iterator.hasNext()) {
	        try {
                    NotationName nn = (NotationName)iterator.next();
                    addItem(nn);
	        }
	        catch (Exception e) {
	            Argo.log.error ("Unexpected exception", e);
	        }
	    }
	    setVisible(true);
	}
	invalidate();
    }
}
