// $Id$
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

package org.argouml.model.uml;


import ru.novosoft.uml.MElementEvent;
import ru.novosoft.uml.MElementListener;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
/**
 *
 * @author  alexb
 */
public class ExplorerNSUMLEventAdaptor
extends PropertyChangeSupport
implements MElementListener{
    
    private static ExplorerNSUMLEventAdaptor instance;
    
    public static ExplorerNSUMLEventAdaptor getInstance(){
        
        if(instance == null)
            return instance = new ExplorerNSUMLEventAdaptor();
        else
            return instance;
    }
    
    /** Creates a new instance of ExplorerUMLEventAdaptor */
    private ExplorerNSUMLEventAdaptor() {
        // PropertyChangeSupport needs a source object
        super(UmlModelEventPump.getPump());
    }
    
    /**
     * If a element changes, this will be catched by this method and reflected
     * in the tree.
     * @see
     * ru.novosoft.uml.MElementListener#listRoleItemSet(ru.novosoft.uml.MElementEvent)
     */
    public void listRoleItemSet(MElementEvent e) {
        if (e.getAddedValue() != null
            || e.getRemovedValue() != null
            || (e.getNewValue() != null
                && !e.getNewValue().equals(e.getOldValue()))) {
            firePropertyChanged("umlModelStructureChanged", e.getSource());
        }

    }

    /**
     * If a element changes, this will be catched by this method and reflected
     * in the tree.
     * @see
     * ru.novosoft.uml.MElementListener#propertySet(ru.novosoft.uml.MElementEvent)
     */
    public void propertySet(MElementEvent e) {
        if (e.getAddedValue() != null
            || e.getRemovedValue() != null
            || (e.getNewValue() != null
                && !e.getNewValue().equals(e.getOldValue()))) {
            if ("namespace".equals(e.getName()))
            firePropertyChanged("structureChanged", e.getSource());
            else
            firePropertyChanged("modelElementChanged", e.getSource());
        }

    }

    /**
     * If a element changes, this will be catched by this method and reflected
     * in the tree.
     * @see
     * ru.novosoft.uml.MElementListener#recovered(ru.novosoft.uml.MElementEvent)
     */
    public void recovered(MElementEvent e) {
        if (e.getAddedValue() != null
            || e.getRemovedValue() != null
            || (e.getNewValue() != null
                && !e.getNewValue().equals(e.getOldValue()))) {
            firePropertyChanged("modelElementAdded", e.getSource());
        }

    }

    /**
     * If a element changes, this will be catched by this method and reflected
     * in the tree.
     * @see
     * ru.novosoft.uml.MElementListener#removed(ru.novosoft.uml.MElementEvent)
     */
    public void removed(MElementEvent e) {
        if (e.getAddedValue() != null
            || e.getRemovedValue() != null
            || (e.getNewValue() != null
                && !e.getNewValue().equals(e.getOldValue()))) {
            firePropertyChanged("modelElementRemoved", e.getSource());
        }

    }

    /**
     * If a element changes, this will be catched by this method and reflected
     * in the tree.
     * @see
     * ru.novosoft.uml.MElementListener#roleAdded(ru.novosoft.uml.MElementEvent)
     */
    public void roleAdded(MElementEvent e) {
        if (e.getAddedValue() != null
            || e.getRemovedValue() != null
            || (e.getNewValue() != null
                && !e.getNewValue().equals(e.getOldValue()))) {
            firePropertyChanged("modelElementAdded", e.getSource());
        }

    }

    /**
     * If a element changes, this will be catched by this method and reflected
     * in the tree.
     * @see
     * ru.novosoft.uml.MElementListener#roleRemoved(ru.novosoft.uml.MElementEvent)
     */
    public void roleRemoved(MElementEvent e) {
        if (e.getAddedValue() != null
            || e.getRemovedValue() != null
            || (e.getNewValue() != null
                && !e.getNewValue().equals(e.getOldValue()))) {
            firePropertyChanged("modelElementRemoved", e.getSource());
        }

    }
    
    // ------- property change events ----------

    /**
     * source of the model element changed translates to the new value.
     */
    private void firePropertyChanged(String propertyName,
				     Object source) 
    {
        PropertyChangeEvent pce = new PropertyChangeEvent(
                            this,
                            propertyName,
                            null,
                            source);

        this.firePropertyChange(pce);
    }
}
