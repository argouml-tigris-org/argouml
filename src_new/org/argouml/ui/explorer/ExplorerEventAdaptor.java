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
 * @since Created on 16 September 2003, 23:13
 * @author  alexb
 */
public class ExplorerEventAdaptor 
implements PropertyChangeListener{
    
    private static ExplorerEventAdaptor instance;
    
    private TreeModelUMLEventListener tree;
    
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
    
    public void structureChanged(){
        tree.structureChanged();
    }
    
    public void modelElementRemoved(Object source){
        tree.modelElementRemoved(source);
    }
    
    public void modelElementAdded(Object source){
        tree.modelElementAdded(source);
    }
    
    public void modelElementChanged(Object source){
        tree.modelElementChanged(source);
    }
    
    public void setTreeModelUMLEventListener(TreeModelUMLEventListener newTree){
        tree = newTree;
    }
    
    /**
     * Listen for configuration changes that could require repaint
     *  of the navigator pane, calls forceUpdate(),
     * Listens for changes of the project fired by projectmanager.
     *
     *  @since ARGO0.11.2
     */
    public void propertyChange(java.beans.PropertyChangeEvent pce) {
        
        // project events
        if (pce.getPropertyName()
                .equals(ProjectManager.CURRENT_PROJECT_PROPERTY_NAME)) 
	{
            tree.structureChanged();
            return;
        }
        
        // notation events
        if (Notation.KEY_USE_GUILLEMOTS.isChangedProperty(pce)
            || Notation.KEY_SHOW_STEREOTYPES.isChangedProperty(pce)) {
            tree.structureChanged();
        }
        
        // uml model events
        if (pce.getPropertyName()
                .equals("umlModelStructureChanged")) {
            tree.structureChanged();
        }
        
        if (pce.getPropertyName()
                .equals("modelElementRemoved")) {
            tree.modelElementRemoved(pce.getNewValue());
        }
        
        if (pce.getPropertyName()
                .equals("modelElementAdded")) {
            tree.modelElementAdded(pce.getNewValue());
        }
        
        if (pce.getPropertyName()
                .equals("modelElementChanged")) {
            tree.modelElementChanged(pce.getNewValue());
        }
    }
    
}
