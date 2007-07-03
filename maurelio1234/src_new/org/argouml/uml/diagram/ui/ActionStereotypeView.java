// $Id: eclipse-argo-codetemplates.xml 11347 2006-10-26 22:37:44Z linus $
// Copyright (c) 2007 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

package org.argouml.uml.diagram.ui;

/**
 * The UML 2.0 Profile specification allows three visualizations for the 
 * stereotypes applied on a model element:
 * 
 * <ul>
 * <li>The stereotype name in guilmets (<< >>'s)</li>
 * <li>An icon replacing the default visualization</li>
 * <li>A small icon flying over the default visualization</li>
 * </ul>
 * 
 * An popup submenu is provided in order to allow the user switching between
 * these modes. This class keeps the code that should be commonly shared
 * among all the visualization options.
 * 
 * @see FigNodeModelElement#getPopUpActions(java.awt.event.MouseEvent)
 *  
 * @author maurelio1234
 */
public abstract class ActionStereotypeView extends AbstractActionRadioMenuItem {

    private FigNodeModelElement targetNode;
    private int selectedStereotypeView;
    
    /**
     * The default constructor for this class
     * 
     * @param node the selected node 
     * @param key  the internationalization key used to label this action
     * @param stereotypeView the stereotype view that is activated when this 
     * 		action is triggered
     */
    public ActionStereotypeView(FigNodeModelElement node, String key,
	    int stereotypeView) {
	super(key, false);

	this.targetNode = node;
	this.selectedStereotypeView = stereotypeView;
	updateSelection();
    }
    
    private void updateSelection() {
	putValue("SELECTED", Boolean
		.valueOf(targetNode.getStereotypeView() 
				== selectedStereotypeView));
    }

    /**
     * @see org.argouml.uml.diagram.ui.AbstractActionRadioMenuItem#toggleValueOfTarget(java.lang.Object)
     */
    void toggleValueOfTarget(Object t) {
	targetNode.setStereotypeView(selectedStereotypeView);
	updateSelection();
    }

    /**
     * @see org.argouml.uml.diagram.ui.AbstractActionRadioMenuItem#valueOfTarget(java.lang.Object)
     */
    Object valueOfTarget(Object t) {
	if (t instanceof FigNodeModelElement) {
	    return new Integer(((FigNodeModelElement) t).getStereotypeView());
	} else {
	    return t;
	}
    }

}
