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

import javax.swing.JTextField;
import javax.swing.event.CaretListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import javax.swing.text.AbstractDocument.DefaultDocumentEvent;

import org.apache.commons.logging.Log;

import ru.novosoft.uml.MElementEvent;
import ru.novosoft.uml.MElementListener;

/**
 * A new model for a textproperty. This model does not use reflection to reach 
 * its goal and will perform better therefore. Furthermore, it only reacts to 
 * events that are meant for this model which improves maintainability and 
 * performance. 
 * TODO: make this model independant from proppanel by designing a mechanisme
 * that can replace thirthpartyeventlistener.
 * TODO: make this model independant from GUI components to pass on targetChanged
 * and targetreasserted events by designing a mechanisme that can extend 
 * UMLChangeDispatch so UMLUserInterfaceComponents don't have to be java.awt.components
 * anymore to get targetChanged events.
 * TODO: remove the dependance on the panel to get the target.
 * @since Oct 6, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public abstract class UMLPlainTextDocument extends PlainDocument
    implements UMLUserInterfaceComponent {
        
    public static Log logger = org.apache.commons.logging.LogFactory.getLog(UMLPlainTextDocument.class);
    
    /**
     * True if an event should be fired when the text of the document is changed
     */
    private boolean _firing = true;
    
    /**
     * The target of the propertypanel that's behind this property.
     */    
    private Object _target = null;


    private PropPanel _panel = null;
    
    /**
     * Constructor for UMLPlainTextDocument. This takes a panel to set the
     * thirdpartyeventlistener to the given list of events to listen to.
     * TODO: make an eventpump that's not in the need of a panel
     */
    public UMLPlainTextDocument(PropPanel panel, Object[] propertyList) {
        super();     
        _panel = panel; // only needed untill we have a working eventpump
        setTarget(panel.getTarget());
        panel.addThirdPartyEventListening(propertyList);  
    }

    /**
     * @see org.argouml.uml.ui.UMLUserInterfaceComponent#targetChanged()
     */
    public void targetChanged() {
        setTarget(_panel.getTarget());
        handleEvent();   
    }

    /**
     * @see org.argouml.uml.ui.UMLUserInterfaceComponent#targetReasserted()
     */
    public void targetReasserted() {
    }

    /**
     * @see ru.novosoft.uml.MElementListener#propertySet(ru.novosoft.uml.MElementEvent)
     */
    public void propertySet(MElementEvent e) {
        handleEvent();
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
     * Returns the target.
     * @return Object
     */
    public final Object getTarget() {
        return _target;
    }

    /**
     * Sets the target.
     * @param target The target to set
     */
    public final void setTarget(Object target) {
        _target = target;
        
    }

    /**
     * @see javax.swing.text.Document#insertString(int, java.lang.String, javax.swing.text.AttributeSet)
     */
    public void insertString(int offset, String str, AttributeSet a)
        throws BadLocationException {
         super.insertString(offset, str, a);
        if (isFiring()) {
            setProperty(getText(0, getLength()));
        }
       
    }

    /**
     * @see javax.swing.text.Document#remove(int, int)
     */
    public void remove(int offs, int len) throws BadLocationException {
        super.remove(offs, len);
        if (isFiring()) {
            setProperty(getText(0, getLength()));
        }      
    }
    
    protected abstract void setProperty(String text);
    
    protected abstract String getProperty();
    
    private final void setFiring(boolean firing) {
        _firing = firing;
    }
    
    private final boolean isFiring() {
        return _firing;
    }
    
    private final void handleEvent() {
        try {
            setFiring(false);
            remove(0, getLength());
            insertString(0, getProperty(), null);
        } catch (BadLocationException b) {
            logger.error("A BadLocationException happened\n" +
                "The string to set was: " +
                getProperty(), b);
        }
        finally {
            setFiring(true);
        }      
    }
    
    

}
