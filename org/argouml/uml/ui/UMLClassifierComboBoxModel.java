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

// File: UMLClassifierComboBoxModel.java
// Classes: UMLClassifierComboBoxModel
// Original Author: 
// $Id$

// 23 Apr 2002: Jeremy Bennett (mail@jeremybennett.com). Layout tidied up and
// mods made following bug fixing in UMLComboBoxModel. getModel() and
// setModel() are no longer available in the parent. _noArgs made a local
// variable.


package org.argouml.uml.ui;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.argouml.model.ModelFacade;
import org.argouml.uml.Profile;

import ru.novosoft.uml.MElementEvent;
import ru.novosoft.uml.behavior.common_behavior.MInstance;
import ru.novosoft.uml.foundation.core.MClassifier;
import ru.novosoft.uml.model_management.MModel;

/**
 * @deprecated as of ArgoUml 0.13.5 (10-may-2003),
 *             replaced by ?,
 *             this class is part of the 'old'(pre 0.13.*) implementation of proppanels
 *             that used reflection a lot.
 */
public class UMLClassifierComboBoxModel extends UMLComboBoxModel  {
    protected static Logger cat = 
        Logger.getLogger(UMLClassifierComboBoxModel.class);

   

    /**
     *   This method creates a UMLComboBoxModel
     *
     *    @param container container that provides access to target, formatting etc
     *    @param filter name of method on container that takes a MModelElement
     *         true if element should be in list, may be null
     *    @param property name of event that would indicate that the value has changed
     *    @param getMethod name of method on container to get value
     *    @param putMethod name of method on container to set value
     *    @param allowVoid allows an entry in the list
     *    @param elementType base type for all elements
     */
    public UMLClassifierComboBoxModel(UMLUserInterfaceContainer container,
				      String filter, String property, String getMethod,
				      String setMethod, boolean allowVoid, Class elementType,
				      boolean addElementsFromProfileModel) {
	
	super (container, filter, property, getMethod, setMethod, allowVoid, elementType,
	       addElementsFromProfileModel);

       
    }


    private void makeSelection(Object model, Object/*MClassifier*/ selClass) {
	getSet().clear();
	Profile profile = getContainer().getProfile();
	if (allowVoid()) {
	    getSet().add(new UMLComboBoxEntry(null, profile, false));
	}
	if (model != null) {
	    collectElements((MModel)model, profile, false);
	}
                 
	if (addElementsFromProfileModel()) {
	    Object/*MModel*/ profileModel = profile.getProfileModel();
	    if (profileModel != null) {
		collectElements(profileModel, profile, true);
	    }
	}
		       
	//
	//   scan for name collisions
	//

	Iterator iter = getSet().iterator();
	String before = null;
	UMLComboBoxEntry currentEntry = null;
	String currentStr = null;
	UMLComboBoxEntry afterEntry = null;
	String after = null;
	    
	while (iter.hasNext()) {
	    before = currentStr;
	    currentEntry = afterEntry;
	    currentStr = after;
	    afterEntry = (UMLComboBoxEntry) iter.next();
	    after = afterEntry.getShortName();
	    if (currentEntry != null) currentEntry.checkCollision(before, after);
	}
	    
	if (afterEntry != null) afterEntry.checkCollision(currentStr, null);		   
	    
	// fireContentsChanged(this,0,getSet().size());	  

	//
	//   get current value
	//                          
	Iterator it = getSet().iterator();
	UMLComboBoxEntry entry;
	while (it.hasNext()) {
	    entry = (UMLComboBoxEntry) it.next();
	    if (!entry.isPhantom() && entry.getElement(model) == selClass) {
		Object/*MModelElement*/ elem = entry.getElement(model);
		String name = null;
		if (elem !=  null ) name = ModelFacade.getName(elem);
		cat.debug("setSelectedItem");
		setSelectedItem( entry);
			    
	    }
	}
	fireContentsChanged(this, 0, getSet().size());	
    }



   
    public void roleAdded(final MElementEvent event) {
	Iterator it = null;
        String eventName = event.getName();
        if (eventName != null && eventName.equals("classifier")) {
	    Object model = ModelFacade.getModel(event.getSource());
	    if (ModelFacade.isAInstance(event.getSource()) ) {
		
		Object/*MInstance*/ instance = (MInstance) event.getSource();	    	    
		Collection col = ModelFacade.getClassifiers(instance);
		if (col != null && col.size() > 0) {
		    it  = col.iterator();		  
		    Object/*MClassifier*/ cls = it.next();

		    makeSelection(model, cls);
		}
	    }	    
	}
    }

   
     
   

    public void targetChanged() {
        Object target = getContainer().getTarget();	
        if (ModelFacade.isAModelElement(target)) {

            Object/*MModelElement*/ element = target;	 	   
            Object/*MModel*/ model = ModelFacade.getModel(element);
	    try {		
                Object[] _noArgs = {};
                Object current = getGetMethod().invoke(getContainer(), _noArgs);
		makeSelection( model , (MClassifier) current);
		
	    }
	    catch (InvocationTargetException ex) {
                cat.error(ex.getTargetException() + " is IncovationTargetException in UMLClassifierComboBoxModel", ex);
		setSelectedItem(null);
	    }
            catch (Exception e) {
                cat.error("Exception in targetchanged", e);
                setSelectedItem(null);
            }
	}
    }


  

    /**
     * @see ru.novosoft.uml.MElementListener#propertySet(MElementEvent)
     */
    public void propertySet(MElementEvent e) {
	super.propertySet(e);
    }

}