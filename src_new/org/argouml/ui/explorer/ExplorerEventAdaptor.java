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

package org.argouml.ui.explorer;


import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.argouml.kernel.ProjectManager;
import org.argouml.application.api.Notation;
import org.argouml.application.api.Configuration;
import org.argouml.model.uml.ExplorerNSUMLEventAdaptor;

/**
 * All events going to the Explorer must pass through here first!
 *
 * <p>Most will come from the uml model via ExplorerNSUMLEventAdaptor
 *
 * @since 0.15.2, Created on 16 September 2003, 23:13
 * @author  alexb
 */
public class ExplorerEventAdaptor 
implements PropertyChangeListener{
    
    private static ExplorerEventAdaptor instance;
    
    /**
     * the tree model to update
     */
    private TreeModelUMLEventListener treeModel;
    
    public static ExplorerEventAdaptor getInstance(){
        
        if(instance == null)
            return instance = new ExplorerEventAdaptor();
        else
            return instance;
    }
    
    /** Creates a new instance of ExplorerUMLEventAdaptor */
    private ExplorerEventAdaptor() {
        
        Configuration.addListener(Notation.KEY_USE_GUILLEMOTS, this);
        Configuration.addListener(Notation.KEY_SHOW_STEREOTYPES, this);
        ProjectManager.getManager().addPropertyChangeListener(this);
        ExplorerNSUMLEventAdaptor.getInstance().addPropertyChangeListener(this);
    }
    
    /**
     * forwards this event to the tree model.
     */
    public void structureChanged(){
        treeModel.structureChanged();
    }
    
    /**
     * forwards this event to the tree model.
     */
    public void modelElementRemoved(Object source){
        treeModel.modelElementRemoved(source);
    }
    
    /**
     * forwards this event to the tree model.
     */
    public void modelElementAdded(Object source){
        treeModel.modelElementAdded(source);
    }
    
    /**
     * forwards this event to the tree model.
     */
    public void modelElementChanged(Object source){
        treeModel.modelElementChanged(source);
    }
    
    /**
     * sets the tree model that will receive events.
     */
    public void setTreeModelUMLEventListener(TreeModelUMLEventListener newTreeModel){
        treeModel = newTreeModel;
    }
    
    /**
     * Listens to events coming from the project manager, config manager, and
     * uml model, passes those events on to the explorer model.
     *
     *  @since ARGO0.11.2
     */
    public void propertyChange(java.beans.PropertyChangeEvent pce) {
        
        // project events
        if (pce.getPropertyName()
                .equals(ProjectManager.CURRENT_PROJECT_PROPERTY_NAME)) 
	{
            treeModel.structureChanged();
            return;
        }
        
        // notation events
        if (Notation.KEY_USE_GUILLEMOTS.isChangedProperty(pce)
            || Notation.KEY_SHOW_STEREOTYPES.isChangedProperty(pce)) {
            treeModel.structureChanged();
        }
        
        // uml model events
        if (pce.getPropertyName()
                .equals("umlModelStructureChanged")) {
            treeModel.structureChanged();
        }
        
        if (pce.getPropertyName()
                .equals("modelElementRemoved")) {
            treeModel.modelElementRemoved(pce.getNewValue());
        }
        
        if (pce.getPropertyName()
                .equals("modelElementAdded")) {
            treeModel.modelElementAdded(pce.getNewValue());
        }
        
        if (pce.getPropertyName()
                .equals("modelElementChanged")) {
            treeModel.modelElementChanged(pce.getNewValue());
        }
    }
    
}
