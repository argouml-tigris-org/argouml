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

import java.awt.event.*;
import java.beans.PropertyVetoException;
import javax.swing.*;
import javax.swing.event.*;

import org.apache.log4j.Category;
import org.argouml.ui.ProjectBrowser;
import java.lang.reflect.*;

import ru.novosoft.uml.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;

public class UMLRadioButton extends JRadioButton implements ItemListener, 
                                                            UMLUserInterfaceComponent {
    protected static Category cat = Category.getInstance(UMLRadioButton.class);
                                                                
        
	private class BooleanSetter implements Runnable {
		JRadioButton _field = null;
		boolean _newValue = false;
		public BooleanSetter(JRadioButton field, boolean newValue) {
			_field = field;
			_newValue = newValue;
		}
		/**
         * @see java.lang.Runnable#run()
         */
        public void run() {
        	_field.setSelected(_newValue);
        }
	}
			
    private UMLUserInterfaceContainer _container;
    private UMLBooleanProperty _property;
    private ButtonGroup _group = null;
    
    /** Creates new BooleanChangeListener */
    public UMLRadioButton(String label,UMLUserInterfaceContainer container,
                          UMLBooleanProperty property) {
        super(label);
        _container = container;
        _property = property;
        addItemListener(this);
        update();
    }
    
    public UMLRadioButton(String label,UMLUserInterfaceContainer container,
                          UMLBooleanProperty property, boolean select) {
        super(label, select);
        _container = container;
        _property = property;
        addItemListener(this);
        update();
    }
    public void itemStateChanged(final ItemEvent event) {
		cat.debug(getAccessibleContext().getAccessibleName()+" itemStateChanged "+event.getStateChange());
		try {
        	_property.setProperty(_container.getTarget(),event.getStateChange() == ItemEvent.SELECTED);
		}
		catch (PropertyVetoException ve) {
			ProjectBrowser.TheInstance.getStatusBar().showStatus(ve.getMessage());
			setSelected(_property.getProperty(_container.getTarget()));
    	}
        // yes we should update
        update();
    }

    public void targetChanged() {
        update();
    }

    public void targetReasserted() {
    }
    
    public void roleAdded(final MElementEvent p1) {
    }
    public void recovered(final MElementEvent p1) {
    }
    public void roleRemoved(final MElementEvent p1) {
    }
    public void listRoleItemSet(final MElementEvent p1) {
    }
    public void removed(final MElementEvent p1) {
    }
    public void propertySet(final MElementEvent event) {
      if(_property.isAffected(event))
	  update();
    }
    
/** update the radio button selection on a target change to reflect the 
 *  attribute's visibility. newState is what was saved with the collection,
 *  sel generally reflects the vis. of the previously displayed attribute or the
 *  direction kind of a parameter. Each change makes a number of passes through 
 *  update, 1 pass for each radio button in the property panel. Therefore, if 
 *  sel and newState are different, and newState is true, we set the currently 
 *  selected radio button to reflect the visibility for attributes, or the 
 *  direction kind for parameters. 
 *      Modified psager@tigris.org Sept. 01, 2001
 */    
    private void update() {
        Object target = _container.getTarget();
        boolean sel = isSelected();
        boolean newState = _property.getProperty(_container.getTarget());
        if (newState && sel != newState){
        	
        	SwingUtilities.invokeLater(new BooleanSetter(this, true));
        }
    }  //...end of update()...

        

}   //...end of class...
