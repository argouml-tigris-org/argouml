// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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

package org.argouml.uml.diagram.sequence.ui;

import java.beans.PropertyVetoException;
import java.util.Hashtable;

import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.ui.CmdSetMode;
import org.argouml.uml.diagram.sequence.SequenceDiagramGraphModel;
import org.argouml.uml.diagram.ui.RadioAction;
import org.argouml.uml.diagram.ui.UMLDiagram;

/**
 * The diagram for sequence diagrams.<p>
 *
 * Totally rewritten for release 0.16.<p>
 *
 * @author jaap.branderhorst@xs4all.nl Aug 3, 2003
 * @author 5eichler@informatik.uni-hamburg.de originally.
 */
public class UMLSequenceDiagram extends UMLDiagram {

    private static final long serialVersionUID = 4143700589122465301L;
    
    private Object[] actions;
    static final String SEQUENCE_CONTRACT_BUTTON = "button.sequence-contract";
    static final String SEQUENCE_EXPAND_BUTTON = "button.sequence-expand";

    /**
     * Constructs a new sequence diagram with a default name and NO namespace.
     * namespaces are used to determine the 'owner' of the diagram for diagrams
     * but that's plain misuse.
     */
    public UMLSequenceDiagram() {
        super();
        // Dirty hack to remove the trash the Diagram constructor leaves
        SequenceDiagramGraphModel gm =
            new SequenceDiagramGraphModel();
        SequenceDiagramLayer lay =
            new SequenceDiagramLayer(this.getName(), gm);
        SequenceDiagramRenderer rend = new SequenceDiagramRenderer();
        lay.setGraphEdgeRenderer(rend);
        lay.setGraphNodeRenderer(rend);
        setLayer(lay);
    }

    /**
     * The constructor.
     *
     * @param collaboration the collaboration
     */
    public UMLSequenceDiagram(Object collaboration) {
        this();
        try {
            setName(getNewDiagramName());
        } catch (PropertyVetoException pve) {
        }
        ((SequenceDiagramGraphModel) getGraphModel())
	    .setCollaboration(collaboration);
        setNamespace(collaboration); //See issue 3373.
    }

    /**
     * Returns the owner of this diagram. 
     *
     * @see org.argouml.uml.diagram.ui.UMLDiagram#getOwner()
     */
    public Object getOwner() {
        return getNamespace();
    }

    /**
     * Creates a new diagramname.
     *
     * @return a new unique name.
     */
    protected String getNewDiagramName() {
        String name = getLabelName() + " " + getNextDiagramSerial();
        if (!(ProjectManager.getManager().getCurrentProject()
	      .isValidDiagramName(name))) {
            name = getNewDiagramName();
        }
        return name;
    }

    /*
     * @see org.argouml.uml.diagram.ui.UMLDiagram#getLabelName()
     */
    public String getLabelName() {
        return Translator.localize("label.sequence-diagram");
    }

    /**
     * Must return an array of actions via which the model can be
     * manipulated. To use the 'nested actions' feature (like the
     * different association types on UMLClassDiagram) these nested
     * actions must be in an array of their own.<p>
     *
     * In case of the sequence diagram this method must return the
     * following actions:<ul>
     * <li>Action to create an object
     * <li>Action to add a procedural link
     * <li>Action to add a create link
     * <li>Action to add a asynchronous link
     * <li>Action to add a synchronous link
     * <li>Action to add a return link
     * </ul>
     *
     * @see org.argouml.uml.diagram.ui.UMLDiagram#getUmlActions()
     */
    protected Object[] getUmlActions() {
        if (actions == null) {
            actions = new Object[7];
            actions[0] =
                new ActionAddClassifierRole();
            int offset = 1;

            Object[][] actionList = {
                {Model.getMetaTypes().getCallAction(),
                    "button.new-callaction", },
                {Model.getMetaTypes().getReturnAction(),
                    "button.new-returnaction", },
                {Model.getMetaTypes().getCreateAction(),
                    "button.new-createaction", },
                {Model.getMetaTypes().getDestroyAction(),
                    "button.new-destroyaction", },
            };

	    for (int i = 0; i < actionList.length; i++) {
		Hashtable args = new Hashtable();
                args.put("edgeClass", Model.getMetaTypes().getMessage());
		args.put("action", actionList[i][0]);
                args.put("actionName",
			 ResourceLoaderWrapper
			 .getImageBinding((String) actionList[i][1]));
		actions[i + offset] =
                    new RadioAction(new CmdSetMode(ModeCreateMessage.class,
                    args,
						   (String) actionList[i][1]));
	    }
            Hashtable args = new Hashtable();
            args.put("name", SEQUENCE_EXPAND_BUTTON);
            actions[5] =
		new RadioAction(new CmdSetMode(ModeChangeHeight.class,
					       args,
					       SEQUENCE_EXPAND_BUTTON));
            args = new Hashtable();
            args.put("name", SEQUENCE_CONTRACT_BUTTON);
            actions[6] =
		new RadioAction(new CmdSetMode(ModeChangeHeight.class,
					       args,
					       SEQUENCE_CONTRACT_BUTTON));
        }
        return actions;
    }

    /*
     * @see org.argouml.uml.diagram.ui.UMLDiagram#getNamespace()
     */
    public Object getNamespace() {
        return ((SequenceDiagramGraphModel) getGraphModel()).getCollaboration();
    }

    /*
     * @see org.argouml.uml.diagram.ui.UMLDiagram#setNamespace(java.lang.Object)
     */
    public void setNamespace(Object ns) {
        ((SequenceDiagramGraphModel) getGraphModel()).setCollaboration(ns);
        super.setNamespace(ns);
    }

    /**
     * Method called by Project.removeDiagram to clean up the mess in
     * this diagram when the diagram is removed.
     */
    public void cleanUp() {
/*
        ProjectManager.getManager().getCurrentProject().moveToTrash(collab);
*/
    }

    /*
     * @see org.argouml.uml.diagram.ui.UMLDiagram#isRelocationAllowed(
     *         java.lang.Object)
     */
    public boolean isRelocationAllowed(Object base)  {
    	return false;
	/* TODO: We may return the following when the
	 * relocate() has been implemented.
	 */
//    	Model.getFacade().isAClassifier(base)
//        	|| Model.getFacade().isAOperation(base);
    }

    /*
     * @see org.argouml.uml.diagram.ui.UMLDiagram#relocate(java.lang.Object)
     */
    public boolean relocate(Object base) {
        return false;
    }

} /* end class UMLSequenceDiagram */
