// $Id$
// Copyright (c) 1996-2002 The Regents of the University of California. All
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

import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JScrollPane;

import org.argouml.model.uml.foundation.core.CoreFactory;
import org.argouml.model.uml.modelmanagement.ModelManagementFactory;
import org.argouml.swingext.Orientation;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.UMLLinkedList;

import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.foundation.core.MNamespace;


public abstract class PropPanelNamespace extends PropPanelModelElement {

    private JScrollPane _ownedElementsScroll;
    
    private static UMLNamespaceOwnedElementListModel ownedElementListModel = new UMLNamespaceOwnedElementListModel();

    ////////////////////////////////////////////////////////////////
    // contructors
    /**
     *    @deprecated As of ArgoUml version 0.13.5 (10-May-2003),
     *                {@link #PropPanelNamespace(String title, ImageIcon icon, Orientation orientation)}
     *                I propose to remove this by version 0.15 (Bob Tarling)
     */
    public PropPanelNamespace(String panelName, ImageIcon icon, int columns) {
        super(panelName, icon, columns);
    }
    
    public PropPanelNamespace(String panelName, ImageIcon icon, Orientation orientation) {
        super(panelName, icon, orientation);
    }
    
    public PropPanelNamespace(String title, Orientation orientation) {
    	super(title, orientation);
    }

    /**
     *    @deprecated As of ArgoUml version 0.13.5 (10-May-2003),
     *                {@link #PropPanelNamespace(String title, ImageIcon icon, Orientation orientation)}
     *                I propose to remove this by version 0.15 (Bob Tarling)
     */
    public PropPanelNamespace(String panelName, int columns) {
        this(panelName, null, columns);
    }
    
    public void addClass() {
        Object target = getTarget();
        if (target instanceof MNamespace) {
            MNamespace ns = (MNamespace) target;
            MModelElement ownedElem = CoreFactory.getFactory().buildClass();
            ns.addOwnedElement(ownedElem);
            TargetManager.getInstance().setTarget(ownedElem);
        }
    }

    public void addInterface() {
        Object target = getTarget();
        if (target instanceof MNamespace) {
            MNamespace ns = (MNamespace) target;
            MModelElement ownedElem = CoreFactory.getFactory().createInterface();
            ns.addOwnedElement(ownedElem);
            TargetManager.getInstance().setTarget(ownedElem);
        }
    }

    public void addPackage() {
        Object target = getTarget();
        if (target instanceof MNamespace) {
            MNamespace ns = (MNamespace) target;
            MModelElement ownedElem = ModelManagementFactory.getFactory().createPackage();
            ns.addOwnedElement(ownedElem);
            TargetManager.getInstance().setTarget(ownedElem);
        }
    }
    
    

    /**
     * Returns the ownedElementsScroll.
     * @return JScrollPane
     */
    public JScrollPane getOwnedElementsScroll() {
        if (_ownedElementsScroll == null) {
	    JList ownedElementsList  = new UMLLinkedList(ownedElementListModel);
            _ownedElementsScroll = new JScrollPane(ownedElementsList); 
        }
        return _ownedElementsScroll;
        
    }

} /* end class PropPanelClass */
