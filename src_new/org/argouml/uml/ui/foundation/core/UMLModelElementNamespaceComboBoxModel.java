// $Id$
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

package org.argouml.uml.ui.foundation.core;

import org.argouml.model.uml.UmlModelEventPump;
import org.argouml.model.uml.foundation.core.CoreHelper;
import org.argouml.uml.ui.UMLComboBoxModel2;

import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.foundation.core.MNamespace;
import ru.novosoft.uml.MBase;

import java.util.Collection;
import java.util.ArrayList;
import javax.swing.SwingUtilities;

/**
 * A model for a namespace combo box, a singleton.
 *
 * <p>the namespace combobox needs to be a singleton because it has a worker
 * thread that needs to be
 * updated by any propanel when a target is changed.
 * the namespace combobox has this optimisation because it is a performance
 * bottleneck with models of size > 100 classifiers.
 *
 * <p>Maybe in the future the bottleneck will disappear, then this class
 * can be simplefied back to its original implementation.
 *
 * $Id$
 *
 * @since Oct 10, 2002
 * @author jaap.branderhorst@xs4all.nl, alexb
 */
public class UMLModelElementNamespaceComboBoxModel extends UMLComboBoxModel2 {
    
    private static UMLModelElementNamespaceComboBoxModel theInstance;
    
    private NamespaceListBuilderThread theBuilderThread;
    
    /**
     * singleton getter method
     */
    public static UMLModelElementNamespaceComboBoxModel getInstance() {
        
        if (theInstance == null) {
            
            theInstance = new UMLModelElementNamespaceComboBoxModel();
            return theInstance;
        }
        else
            return theInstance;
    }
    
    /**
     * Constructor for UMLModelElementNamespaceComboBoxModel.
     */
    private UMLModelElementNamespaceComboBoxModel() {
        super("namespace", false);
        UmlModelEventPump.getPump().addClassModelEventListener(this, MNamespace.class, "ownedElement");
        
        theBuilderThread =
	    new NamespaceListBuilderThread("namespace combobox worker");
        theBuilderThread.start();
    }
    
    /**
     * @see org.argouml.uml.ui.UMLComboBoxModel2#isValidElement(ru.novosoft.uml.MBase)
     */
    protected boolean isValidElement(Object o) {
        return o instanceof MNamespace && CoreHelper.getHelper().isValidNamespace((MModelElement) getTarget(), (MNamespace) o);
    }
    
    /**
     * this doesn't actually build the list, it only display "please wait..."
     * in the dropdown list to tell the user the list is being built.
     *
     * <p> the list is now built in the worker thread.
     *
     * @see org.argouml.uml.ui.UMLComboBoxModel2#buildModelList()
     */
    protected void buildModelList() {
        
        Collection building = new ArrayList();
        building.add(new String("please wait ..."));
        setElements(building);
    }
    
    /**
     * @see org.argouml.uml.ui.UMLComboBoxModel2#getSelectedModelElement()
     */
    protected Object getSelectedModelElement() {
        if (getTarget() != null) {
            return ((MModelElement) getTarget()).getNamespace();
        }
        return null;
    }
    
    /**
     * overridden setTarget() to maintain consistent model state whilst using
     * the worker thread.
     *
     * if the target has been changed we don't want to continue calculating the
     * namespaces for the old target!
     *
     */
    protected void setTarget(Object target) {
        
        if (theBuilderThread != null && theBuilderThread.isAlive()) {
            try {
                theBuilderThread.interrupt();
            } catch (SecurityException seIgnore) { }
        }
        
        if (_target instanceof MBase) {
            UmlModelEventPump.getPump().removeModelEventListener(this,
								 (MBase) _target,
								 _propertySetName);
        }
        
        if (target instanceof MBase) {
            _target = target;
            UmlModelEventPump.getPump().addModelEventListener(this,
							      (MBase) _target,
							      _propertySetName);
        } else {
            target = null;
        }
        _fireListEvents = false;
        removeAllElements();
        _fireListEvents = true;
        if (target != null) {
            buildModelList();
            // calculate the new namespaces.
            theBuilderThread.build();
        }
        
       
    }
    
    /**
     * worker thread to free the swing gui whilst namespaces are calculated.
     * we only ever want one of this thread
     */
    class NamespaceListBuilderThread extends Thread {
        
        public NamespaceListBuilderThread(String name) {
            super(name);
            // let argo exit even if this is still running.
            setDaemon(true);
        }
        
        public void run() {
            
            synchronized (this) {
                while (true) {
                    
                    try {
                        // wait for the next change of target
                        wait();
                        // build the namespace list.
                        buildList();
                    }
                    catch (InterruptedException ie) { /** no action needed */ }
                }
            }
        }
        
        /**
         * notifies this thread that it can build a new list.
         */
        public synchronized void build() {
            
            notify();
        }
        
        /**
         * does the time consuming work on this thread,
         * then updates the gui on the event dispatching thread.
         *
         * <p> does not update the gui if the target was changed again..
         */
        private void buildList() {
            
            Object originalTarget = getTarget();
            
            final Collection namespaces =
		CoreHelper.getHelper().
		    getAllPossibleNamespaces((MModelElement) originalTarget);
            
            // don't update the gui if the target has changed!
            if (originalTarget != getTarget()) {
                return;
            }
            
            SwingUtilities.invokeLater(new Runnable()
				       {
					   public void run() {
                    
					       setElements(namespaces);
					       setSelectedItem(getSelectedModelElement());
					   }
				       } );
        }
    }
}
