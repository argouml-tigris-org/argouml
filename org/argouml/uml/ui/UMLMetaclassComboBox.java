// $Id$
// Copyright (c) 1996-2004 The Regents of the University of California. All
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
import javax.swing.*;

import org.apache.log4j.Logger;

import java.awt.event.*;
import java.lang.reflect.*;
import ru.novosoft.uml.MElementEvent;

/**
 * @deprecated as of ArgoUml 0.13.5 (10-may-2003),
 * TODO: What is this replaced by?
 * this class is part of the 'old'(pre 0.13.*) implementation of proppanels
 * that used reflection a lot.
 */
public class UMLMetaclassComboBox 
    extends JComboBox 
    implements UMLUserInterfaceComponent, ItemListener 
{

    private static final Logger LOG = 
        Logger.getLogger(UMLMetaclassComboBox.class);

    private String[] metaclasses = {
	"ModelElement",
	"Classifier",
	"Class",
	"Interface",
	"DataType",
	"Exception",
	"Signal",
	
	"Association",
	"AssociationEnd",
	"Attribute",
	"Operation",
	"Generalization",
	"Flow",
	"Usage",
	"BehavioralFeature",
	
	"CallEvent",
	"Abstraction",
	"Component",
	"Package",
	"Constraint",
	"Comment",
	"ObjectFlowState",
	
	"Model",
	"Subsystem",
	"Collaboration",
	"Permission",
	"Actor",
	"Node",
	"NodeInstance",
	"Link"
    };

    private Method theGetMethod;
    private Method theSetMethod;
    private String theProperty;
    private UMLUserInterfaceContainer theContainer;
    private Object[] noArgs = {};

    /**
     * The constructor.
     * 
     * @param container the container
     * @param property the property
     * @param getMethod the getmethod
     * @param setMethod the setmethod
     */
    public UMLMetaclassComboBox(UMLUserInterfaceContainer container,
				String property,
				String getMethod, String setMethod) {
	setModel(new DefaultComboBoxModel(metaclasses));
	theContainer = container;
	theProperty = property;
	addItemListener(this);
	try {
	    theGetMethod = container.getClass().getMethod(getMethod,
							new Class[] { 
							});
	}
	catch (Exception e) {
	    LOG.error("Error in UMLMetaclassComboBox:" + getMethod, e);
	}
	try {
	    theSetMethod = container.getClass()
	        .getMethod(setMethod, new Class[] {
	            String.class 
	        });
	}
	catch (Exception e) {
	    LOG.error("Error in UMLMetaclassComboBox:" + setMethod, e);
	}
    }

    /**
     * @see ru.novosoft.uml.MElementListener#propertySet(ru.novosoft.uml.MElementEvent)
     */
    public void propertySet(MElementEvent e) {
	String eventName = e.getName();
	if (eventName == null
	    || theProperty == null
	    || eventName.equals(theProperty)) {

	    update();

	}
    }

    /**
     * @see ru.novosoft.uml.MElementListener#roleAdded(ru.novosoft.uml.MElementEvent)
     */
    public void roleAdded(MElementEvent e) {
    }

    /**
     * @see ru.novosoft.uml.MElementListener#roleRemoved(ru.novosoft.uml.MElementEvent)
     */
    public void roleRemoved(MElementEvent e) {
    }

    /**
     * @see ru.novosoft.uml.MElementListener#listRoleItemSet(ru.novosoft.uml.MElementEvent)
     */
    public void listRoleItemSet(MElementEvent e) {
    }

    /**
     * @see ru.novosoft.uml.MElementListener#removed(ru.novosoft.uml.MElementEvent)
     */
    public void removed(MElementEvent e) {
    }

    /**
     * @see ru.novosoft.uml.MElementListener#recovered(ru.novosoft.uml.MElementEvent)
     */
    public void recovered(MElementEvent e) {
    }

    /**
     * @see org.argouml.uml.ui.UMLUserInterfaceComponent#targetChanged()
     */
    public void targetChanged() {
	update();
    }

    /**
     * @see org.argouml.uml.ui.UMLUserInterfaceComponent#targetReasserted()
     */
    public void targetReasserted() {
    }

    private void update() {
	String meta = "ModelElement";
	try {
	    meta = (String) theGetMethod.invoke(theContainer, noArgs);
	}
	catch (Exception e) {
	}
	ComboBoxModel model = getModel();
	Object element;
	int size =  model.getSize();
	int i = 0;
	for (i = 0; i < size; i++) {
	    element = model.getElementAt(i);
	    if (element.equals(meta)) {
		model.setSelectedItem(element);
		break;
	    }
	}
	if (i == size) {
	    model.setSelectedItem(model.getElementAt(0));
	}
    }

    /**
     * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
     */
    public void itemStateChanged(ItemEvent event) {
	if (event.getStateChange() == ItemEvent.SELECTED) {
	    Object selectedItem = event.getItem();
	    try {
		theSetMethod.invoke(theContainer, new Object[] {
		    selectedItem.toString() 
		});
	    }
	    catch (Exception e) {
	    }
	}
    }
}
