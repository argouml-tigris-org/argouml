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


// File: UMLStereotypeComboBox.java
// Classes: UMLStereotypeComboBox
// Original Author: Curt Arnold
// $Id$

// 26 Apr 2002: Jeremy Bennett (mail@jeremybennett.com). Added third party
// event listener to ensure name changes to stereotypes are picked up.


package org.argouml.uml.ui;

import org.argouml.uml.*;

import javax.swing.*;

import java.awt.event.*;
import java.awt.*;
import java.util.*;

import ru.novosoft.uml.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.model_management.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;


/**
 * <p>Implements a combo box for stereotypes.</p>
 *
 * <p>The class polls the model and profile for appropriate stereotypes for the
 *   target object.  A context popup menu allows for new stereotypes to be
 *   created and existing stereotypes to be deleted.</p>
 *
 * @author Curt Arnold
 *
 * @author Jeremy Bennett (mail@jeremybennett.com), 26 Apr 2002. Added third
 *         party listener, to ensure name change events are tracked.
 */

public class UMLStereotypeComboBox extends UMLComboBox {

    /**
     * <p>Constructor for the box.</p>
     *
     * <p>Creates a model ({@link UMLComboBoxModel} and invokes the superclass
     *   with that. Then sets a third party listener.</p>
     *
     * @param container  The container (invariably a {@link PropPanel}) that
     *                   contains this box.
     */

    public UMLStereotypeComboBox(UMLUserInterfaceContainer container) {

        super(new UMLComboBoxModel(container, "isAcceptibleStereotype",
                                   "stereotype", "getStereotype",
                                   "setStereotype", true, MStereotype.class,
                                   true));

        // Only add a listener if we have a prop panel

        if (container instanceof PropPanel) {
            Object [] eventsToWatch = { MStereotype.class, "name" };
            ((PropPanel) container).addThirdPartyEventListening(eventsToWatch);
        }
    }

}  /* End of UMLStereotypeComboBox.java */
