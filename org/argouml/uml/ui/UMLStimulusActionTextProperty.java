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

package org.argouml.uml.ui;
import org.argouml.model.ModelFacade;

import ru.novosoft.uml.MElementEvent;
//import ru.novosoft.uml.behavior.common_behavior.MStimulus;

/**
 * TODO: this class should be moved to package
 * org.argouml.uml.ui.behavior.common_behavior
 */
public class UMLStimulusActionTextProperty  {
    
   
    protected String _propertyName;

    public UMLStimulusActionTextProperty(String propertyName) {
        _propertyName = propertyName;
    }
    
    
    public void setProperty(UMLUserInterfaceContainer container, String newValue) {
	Object/*MStimulus*/  stimulus = container.getTarget();
	if (stimulus != null) {

	    String oldValue = getProperty(container);
	    //
	    //  if one or the other is null or they are not equal
	    if (newValue == null || oldValue == null || !newValue.equals(oldValue)) {
		//
		//  as long as they aren't both null 
		//   (or a really rare identical string pointer)
		if (newValue != oldValue) {
		    // Object[] args = { newValue };
		    Object action = ModelFacade.getDispatchAction(stimulus);
		    ModelFacade.setName(action, newValue);
		    // to rupdate the diagram set the stimulus name again
		    String dummyStr =  new String(ModelFacade.getName(stimulus));
		    ModelFacade.setName(stimulus, dummyStr);
		    
		 
		    
		}
	    }		
	}       
    }
    
    public String getProperty(UMLUserInterfaceContainer container) {
        String value = null;       
	Object/*MStimulus*/ stimulus = container.getTarget();
	if (stimulus != null) {
	    Object action = ModelFacade.getDispatchAction(stimulus);
	    if (action != null) value = ModelFacade.getName(action);
	}
        return value;
    }
    
    boolean isAffected(MElementEvent event) {
	String sourceName = event.getName();	
        if (_propertyName == null || sourceName == null || sourceName.equals(_propertyName))
            return true;
        return false;
    }
    
    void targetChanged() {
    }
}

