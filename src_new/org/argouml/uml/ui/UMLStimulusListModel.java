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
import ru.novosoft.uml.*;
import javax.swing.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.behavior.common_behavior.MStimulus;
import ru.novosoft.uml.behavior.common_behavior.MObject;
import java.util.*;
import java.awt.*;

public class UMLStimulusListModel extends UMLModelElementListModel  {

    private final static String _nullLabel = "(null)";
    // list of sent or received stimuli
    private String stimulusType;

    public UMLStimulusListModel(UMLUserInterfaceContainer container,String property,boolean showNone, String stimulusType) {
	super(container,property,showNone);
	this.stimulusType = stimulusType;
       
    }
    
    public void open(int index) {
        MModelElement stimulus = getModelElementAt(index);
        if(stimulus != null) {
            navigateTo(stimulus);
        }
    }
    

    protected int recalcModelElementSize() {
        int size = 0;
        Collection stimuli = getStimuli();
        if(stimuli != null) {
            size = stimuli.size();
        }
        return size;
    }

    protected MModelElement getModelElementAt(int index) {
        MModelElement elem = null;
        Collection stimuli = getStimuli();
        if(stimuli != null) {
            elem = elementAtUtil(stimuli,index,MStimulus.class);
        }
        return elem;
    }

    private Collection getStimuli() {
	MObject obj = (MObject) getTarget();
	Collection stimuli = null;
        if (stimulusType.equals("sent") ) {
	    stimuli = obj.getStimuli3();
	} else if (stimulusType.equals("received") ) {
	    stimuli = obj.getStimuli2();
  	}
	return stimuli;
		   
    }
    
    

    
	/**
	 * @see org.argouml.uml.ui.UMLModelElementListModel#buildPopup(JPopupMenu, int)
	 */
	public boolean buildPopup(JPopupMenu popup, int index) {
		return false;
	}

}




