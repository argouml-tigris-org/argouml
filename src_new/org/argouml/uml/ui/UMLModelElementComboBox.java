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


// File: UMLModelElementComboBox.java
// Classes: UMLModelElementComboBox
// Original Author: mail@jeremybennett.com
// $Id$

// 26 Apr 2002: Jeremy Bennett (mail@jeremybennett.com). Created to support
// UMLModelElementComboBoxModel.


package org.argouml.uml.ui;

import javax.swing.event.*;
import javax.swing.*;

import java.lang.reflect.*;
import java.awt.event.*;
import java.awt.*;

import ru.novosoft.uml.*;
import ru.novosoft.uml.foundation.core.*;


/**
 * <p>A combo box that can use {@link UMLModelElementComboBoxModel}.</p>
 *
 * <p>This is a normal {@link JComboBox}, but implements {@link
 *   UMLUserIntefaceComponent} and passes the events on to the model.</p>
 *
 * @author Jeremy Bennett (mail@jeremybennett.com), 26 Apr 2002.
 */


public class UMLModelElementComboBox
    extends    JComboBox
    implements UMLUserInterfaceComponent {


    ///////////////////////////////////////////////////////////////////////////
    //
    // Instance variables
    //
    ///////////////////////////////////////////////////////////////////////////

    /**
     * <p>Our underlying combo box model.</p>
     */

    private UMLModelElementComboBoxModel _model;


    ///////////////////////////////////////////////////////////////////////////
    //
    // Constructors
    //
    ///////////////////////////////////////////////////////////////////////////

    /**
     * <p>Build a new combo box for UML model elements.</p>
     *
     * <p>Uses the superclass constructor, then records the model in an
     *   instance variable, and adds an action listener for the model.</p>
     *
     * @param model  The underlying combo box model to use.
     */

    
    public UMLModelElementComboBox(UMLModelElementComboBoxModel model) {
        super(model);
        _model = model;
         addActionListener(_model);
    }


    ///////////////////////////////////////////////////////////////////////////
    //
    // Accessors
    //
    ///////////////////////////////////////////////////////////////////////////


    /**
     * <p>Change the underlying model.</p>
     *
     * <p>We need to take the action listener off the old model and add one to
     *   the new model. Then invoke the superclass method.</p>
     */

    public void setModel(ComboBoxModel newModel) {

        // Take off any old listener, and add a new one

        ComboBoxModel oldModel = getModel();

        if((oldModel != null) && (oldModel instanceof ActionListener)) {
            removeActionListener((ActionListener) oldModel);
        }

        if((newModel != null) && (newModel instanceof ActionListener)) {
            addActionListener((ActionListener) newModel);
        }

        // Let the superclass do the rest

        super.setModel(newModel);
    }


    ///////////////////////////////////////////////////////////////////////////
    //
    // Event Handlers
    //
    ///////////////////////////////////////////////////////////////////////////



    /**
     * <p>Pass the event on to the underlying model.</p>
     *
     * <p>Provided to comply with the {@link UMLUserInterfaceComponent}
     *   interface.</p>
     */

    public void targetChanged() {
        _model.targetChanged();
        updateUI();
    }


    /**
     * <p>Pass the event on to the underlying model.</p>
     *
     * <p>Provided to comply with the {@link UMLUserInterfaceComponent}
     *   interface.</p>
     */

    public void targetReasserted() {
        _model.targetReasserted();
        updateUI();
    }


    /**
     * <p>Pass the event on to the underlying model.</p>
     *
     * <p>Provided to comply with the {@link MElementListener} interface
     *   (superinterface of the {@link UMLUserInterfaceComponent})
     *   interface.</p>
     *
     * @param event  The NSUML event that trigged us.
     */

    public void roleAdded(final MElementEvent event) {
        _model.roleAdded(event);
        updateUI();
    }


    /**
     * <p>Pass the event on to the underlying model.</p>
     *
     * <p>Provided to comply with the {@link MElementListener} interface
     *   (superinterface of the {@link UMLUserInterfaceComponent})
     *   interface.</p>
     *
     * @param event  The NSUML event that trigged us.
     */

    public void recovered(final MElementEvent event) {
        _model.recovered(event);
        updateUI();
    }


    /**
     * <p>Pass the event on to the underlying model.</p>
     *
     * <p>Provided to comply with the {@link MElementListener} interface
     *   (superinterface of the {@link UMLUserInterfaceComponent})
     *   interface.</p>
     *
     * @param event  The NSUML event that trigged us.
     */

    public void roleRemoved(final MElementEvent event) {
        _model.roleRemoved(event);
        updateUI();
    }


    /**
     * <p>Pass the event on to the underlying model.</p>
     *
     * <p>Provided to comply with the {@link MElementListener} interface
     *   (superinterface of the {@link UMLUserInterfaceComponent})
     *   interface.</p>
     *
     * @param event  The NSUML event that trigged us.
     */

    public void listRoleItemSet(final MElementEvent event) {
        _model.listRoleItemSet(event);
        updateUI();
    }


    /**
     * <p>Pass the event on to the underlying model.</p>
     *
     * <p>Provided to comply with the {@link MElementListener} interface
     *   (superinterface of the {@link UMLUserInterfaceComponent})
     *   interface.</p>
     *
     * @param event  The NSUML event that trigged us.
     */

    public void removed(final MElementEvent event) {
        _model.removed(event);
        updateUI();
    }


    /**
     * <p>Pass the event on to the underlying model.</p>
     *
     * <p>Provided to comply with the {@link MElementListener} interface
     *   (superinterface of the {@link UMLUserInterfaceComponent})
     *   interface.</p>
     *
     * @param event  The NSUML event that trigged us.
     */

    public void propertySet(final MElementEvent event) {
        _model.propertySet(event);
        updateUI();
    }

} /* End of UMLModelElementComboBox */

