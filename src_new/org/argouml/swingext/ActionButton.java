/*
 * ActionButton.java
 *
 * Created on 15 October 2002, 22:52
 */

package org.argouml.swingext;

import javax.swing.*;
import java.awt.event.*;
import java.lang.UnsupportedOperationException;
import java.beans.*;

/**
 *
 * @author  administrator
 */
public class ActionButton extends javax.swing.JButton {
    
    private Action action;
    private PropertyChangeListener actionPropertyChangeListener;

    /** Creates a new instance of ActionButton */
    public ActionButton(Action a) {
        super();
        if (a == null) throw new IllegalArgumentException();
        
        action = a;
	Boolean hide = (Boolean)getClientProperty("hideActionText");
	if (hide == null || hide!=Boolean.TRUE) {
	    setText((String)a.getValue(Action.NAME));
        }
	setIcon((Icon)a.getValue(Action.SMALL_ICON));
	setEnabled(a.isEnabled());
 	setToolTipText((String)a.getValue(Action.SHORT_DESCRIPTION));	
        if (!isListener(ActionListener.class, action)) {
            addActionListener(action);
        }
        // Reverse linkage:
        actionPropertyChangeListener = createActionPropertyChangeListener(action);
        action.addPropertyChangeListener(actionPropertyChangeListener);
        //firePropertyChange("action", oldValue, action2);
    }
    
    private boolean isListener(Class c, ActionListener a) {
	boolean isListener = false;
	Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==c && listeners[i+1]==a) {
		    isListener=true;
	    }
	}
	return isListener;
    }

    
    public Action getAction() {
	return action;
    }

    public void setAction(Action a) {
        throw new UnsupportedOperationException();
    }
}
