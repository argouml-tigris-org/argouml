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

// File: UMLIncludeListModel.java
// Classes: UMLIncludeListModel
// Original Author: not known
// $$

// 26 Mar 2002: Jeremy Bennett (mail@jeremybennett.com). Tidied up, as part of
// getting the include stuff to work. add() method tidied up to put the new
// relationship at the correct place in the list. buildPopup removed, since the
// parent implementation is fine.

// 3 Apr 2002: Jeremy Bennett (mail@jeremybennett.com). There is a bug in
// NSUML, where the "include" and "include2" associations of a use case are
// back to front, i.e "include" is used as the opposite end of "addition" to
// point to an including use case, rather than an included use case. Fixed
// within the include relationship, rather than the use case, by reversing the
// use of access functions for the "base" and "addition" associations.

// 3 May 2002: Jeremy Bennett (mail@jeremybennett.com). Extended to mark the
// project as needing saving if an include relationship is added, deleted,
// changed or moved.


package org.argouml.uml.ui.behavior.use_cases;

import java.util.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import javax.swing.*;

import ru.novosoft.uml.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.behavior.use_cases.*;

import org.argouml.ui.*;
import org.argouml.uml.ui.ActionRemoveFromModel;
import org.argouml.uml.ui.UMLAddDialog;
import org.argouml.uml.ui.UMLListMenuItem;
import org.argouml.uml.ui.UMLModelElementListModel;
import org.argouml.uml.ui.UMLUserInterfaceContainer;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.graph.MutableGraphModel;
import org.tigris.gef.presentation.Fig;
import org.apache.log4j.Category;
import org.argouml.application.api.Argo;
import org.argouml.kernel.*;
import org.argouml.model.uml.behavioralelements.usecases.UseCasesFactory;
import org.argouml.model.uml.behavioralelements.usecases.UseCasesHelper;


/**
 * <p>A list model for the include relationship on use case property
 *   panels.</p>
 *
 * <p>Supports the full menu (Open, Add, Delete, Move Up, Move Down). Provides
 *   its own formatElement routine, to use the name of the base use case (where
 *   the container is a use case) or the extension use case (where the
 *   container is an extension point, rather than name of the extend
 *   relationship itself.</p>
 *
 * <p><em>Note</em>. There is a bug in NSUML, where the "include" and
 *   "include2" associations of a use case are back to front, i.e "include" is
 *   used as the opposite end of "addition" to point to an including use case,
 *   rather than an included use case. Fixed within the include relationship,
 *   rather than the use case, by reversing the use of access functions for the
 *   "base" and "addition" associations in the code.</p>
 */

public class UMLIncludeListModel extends UMLModelElementListModel  {
    protected static Category cat = Category.getInstance(UMLIncludeListModel.class);

    /**
     * <p>The default text when there is no addition class for the include
     *   relationship.</p>
     */
 
    private final static String _nullLabel = "(anon)";
    

    /**
     * <p>Create a new list model.<p>
     *
     * <p>Implementation is just an invocation of the parent constructor.</p>
     *
     * @param container  The container for this list - the use case property
     *                   panel.
     *
     * @param property   The name associated with the NSUML {@link
     *                   MElementEvent} that we are tracking or
     *                   <code>null</code> if we track them all. We 
     *                   probably want to just track the "include" event.
     *
     * @param showNone   True if an empty list is represented by the keyword
     *                   "none"
     */

    public UMLIncludeListModel(UMLUserInterfaceContainer container,
                               String property,boolean showNone) {
        super(container,property,showNone);
    }
    
    /**
     * <p>Compute the size of the list model. This method must be provided to
     *   override the abstract method in the parent.</p>
     *
     * @return the number of elements the list model (0 if there are none).
     */

    protected int recalcModelElementSize() {
        int        size     = 0;
        Collection includes = getIncludes();

        if(includes != null) {
            size = includes.size();
        }

        return size;
    }
    
    /**
     * <p>Get the element at a given offset in the model This method must be
     *   provided to override the abstract method in the parent.</p>
     *
     * <p>The implementation makes use of the {@link #elementAtUtil} method,
     *   which takes care of all unusual cases.</p>
     *
     * @param   the index of the desired element.
     *
     * @return  the element at that index if there is one, otherwise
     *          <code>null</code>.
     */

    protected MModelElement getModelElementAt(int index) {

        return elementAtUtil(getIncludes(), index, MInclude.class);
    }
            
        
    /**
     * <p>A private utility to get the list of extends relationships for this
     *   use case.</p>
     *
     * <p><em>Note</em>. There is a bug in NSUML, where the "include" and
     *   "include2" associations of a use case are back to front, i.e "include"
     *   is used as the opposite end of "addition" to point to an including use
     *   case, rather than an included use case.  Fixed within the include
     *   relationship, rather than the use case, by reversing the use of access
     *   functions for the "base" and "addition" associations in the code.</p>
     *
     * @return  the list of includes relationships for this use case.
     */

    private Collection getIncludes() {

        Collection includes = null;
        Object     target   = getTarget();

        if(target instanceof MUseCase) {
            MUseCase useCase = (MUseCase) target;

            includes = useCase.getIncludes();
        }

        return includes;
    }
    
    
    /**
     * <p>Format a given model element.</p>
     *
     * <p>If there is no addition use case, use the default text
     *   ("(anon)"). Otherwise use the parent formatElement on the use case
     *   attached as addition to the extend relationship , which will
     *   ultimately invoke the format element method of {@link PropPanel}.</p>
     *
     * <p>In this current implementation, more rigorously checks it is
     *   formatting an extend relationship.</p>
     *
     * <p><em>Note</em>. There is a bug in NSUML, where the "include" and
     *  "include2" associations of a use case are back to front, i.e "include"
     *   is used as the opposite end of "addition" to point to an including
     *   use case, rather than an included use case. Fixed within the include
     *   relationship, rather than the use case, by reversing the meaning of
     *   the "base" and "association" associations in the code.</p>
     *
     * @param element  the model element to format
     *
     * @return an object (typically a string) representing the element.
     */

    public Object formatElement(MModelElement element) {

        Object value = _nullLabel;

        // Note that we cope with the NSUML bug by using the getBase() accessor
        // rather than the getAddition() accessor to reverse the problem.

        if (element instanceof MInclude) {
            MInclude include = (MInclude) element;
            MUseCase target  = include.getBase();

            if(target != null) {
                value = super.formatElement(target);
            }
        }
        else {
            if (element != null) {
                cat.warn("UMLIncludeListModel." +
                                   "formatElement(): Can't format " +
                                   element.getClass().toString());
            }
        }

        return value;
    }


    /**
     * <p>Implement the "add" function of the pop up menu.</p>
     *
     * <p>Pops up the UMLAddDialog. The user can include existing usecases to 
     * this model's target.
     *
     * <p><em>Note</em>. There is a bug in NSUML, where the "include" and
     *   "include2" associations of a use case are back to front, i.e "include"
     *   is used as the opposite end of "addition" to point to an including use
     *   case, rather than an included use case.  Fixed within the include
     *   relationship, rather than the use case, by reversing the use of access
     *   functions for the "base" and "addition" associations in the code.</p>
     *
     * @param index  Offset in the list of the element at which the pop-up was
     *               invoked.
     */

    public void add(int index) {
    	Object target = getTarget();
    	if (target instanceof MUseCase) {
    		MUseCase usecase = (MUseCase)target;	
	    	Vector choices = new Vector();
	    	Vector selected = new Vector();
	    	choices.addAll(UseCasesHelper.getHelper().getAllUseCases());
	    	choices.remove(usecase);
	    	selected.addAll(UseCasesHelper.getHelper().getIncludedUseCases(usecase));
	    	UMLAddDialog dialog = new UMLAddDialog(choices, selected, Argo.localize("UMLMenu", "dialog.title.add-included-usecases"), true, true);
	    	int returnValue = dialog.showDialog(ProjectBrowser.TheInstance);
	    	if (returnValue == JOptionPane.OK_OPTION) {
	    		Iterator it = dialog.getSelected().iterator();
	    		while (it.hasNext()) {
	    			MUseCase includedusecase = (MUseCase)it.next();
	    			if (!selected.contains(includedusecase)) {
	    				ProjectBrowser pb = ProjectBrowser.TheInstance;
	    				ArgoDiagram diagram = pb.getActiveDiagram();
	    				Fig figclass = diagram.getLayer().presentationFor(usecase);
	    				Fig figeusecase = diagram.getLayer().presentationFor(includedusecase);
	    				if (figclass != null && figeusecase != null) {
	    					GraphModel gm = diagram.getGraphModel();
	    					if (gm instanceof MutableGraphModel) {
	    						((MutableGraphModel)gm).connect(usecase, includedusecase, MInclude.class);
	    					}
	    				} else {
	    					UseCasesFactory.getFactory().buildInclude(usecase, includedusecase);
	    				}
	    			}
	    		}
	    		it = selected.iterator();
	    		while (it.hasNext()) {
	    			MUseCase includedusecase = (MUseCase)it.next();
	    			if (!dialog.getSelected().contains(includedusecase)) {
	    				MInclude include = UseCasesHelper.getHelper().getIncludes(usecase, includedusecase);
			    		Object pt = ProjectBrowser.TheInstance.getTarget();
			    		ProjectBrowser.TheInstance.setTarget(include);
			    		ActionEvent event = new ActionEvent(this, 1, "delete");
			    		ActionRemoveFromModel.SINGLETON.actionPerformed(event);
			    		ProjectBrowser.TheInstance.setTarget(pt);
	    			}
	    		}
	    	}
    	}

    }
    
    /**
     * <p>Implement the "delete" function of the pop up menu. Delete the
     *   element at the given index.</p>
     *
     * <p>Find the use cases at each end (note that NSUML uses the name
     *   "include2" for the use case doing the included, since it is unnamed in
     *   the standard). Delete their references to this include
     *   relationship. Also remove from the namespace.</p>
     *
     * <p><em>Note</em>. We don't actually need to check the target PropPanel
     *   is a use case&mdash;given an include relationship we can delete
     *   it.</p>
     *
     * <p><em>Note</em>. There is a bug in NSUML, where the "include" and
     *   "include2" associations of a use case are back to front, i.e "include"
     *   is used as the opposite end of "addition" to point to an including use
     *   case, rather than an included use case.  Fixed within the include
     *   relationship, rather than the use case, by reversing the use of access
     *   functions for the "base" and "addition" associations in the code.</p>
     *
     * @param index  Offset in the list of the element at which the pop-up was
     *               invoked and which is to be deleted.
     */

    public void delete(int index) {
    	Object target = getTarget();
    	if (target instanceof MUseCase) {
    		MUseCase usecase = (MUseCase)target;
    		MUseCase includedusecase = (MUseCase)UMLModelElementListModel.elementAtUtil(UseCasesHelper.getHelper().getIncludedUseCases(usecase), index, null);
    		MInclude include = UseCasesHelper.getHelper().getIncludes(includedusecase, usecase);
    		Object pt = ProjectBrowser.TheInstance.getTarget();
    		ProjectBrowser.TheInstance.setTarget(include);
    		ActionEvent event = new ActionEvent(this, 1, "delete");
    		ActionRemoveFromModel.SINGLETON.actionPerformed(event);
    		ProjectBrowser.TheInstance.setTarget(pt);
    		fireIntervalRemoved(this,index,index);
    	}
    	/*

        // Only do this if it really is an includes relationship

        MModelElement modElem = getModelElementAt(index);

        if (!(modElem instanceof MInclude)) {
            return ;
        }

        MInclude  include   = (MInclude) modElem;

        // Note reversal of code using addition and base to handle NSUML bug

        MUseCase   addition = include.getBase();
        MUseCase   base     = include.getAddition();
        MNamespace ns       = include.getNamespace();

        // Remove the addition end of the relationship. Note we do not need to
        // do anything about the include relationship's addition attribute - it
        // will have been done by the removeInclude2.

        // Note there is a bug in NSUML. It has the include and include2
        // associations swapped over (include should be the other end of base,
        // not additon). We deal with this in the code by swapping accessor
        // fucntions for "base" and "addition".

        if (addition != null) {
            addition.removeInclude2(include);
        }

        // Remove the base end of the relationship (if there was one). Note we
        // do not need to do anything about the include relationship's base
        // attribute - it will have been done by the removeInclude.

        // Note there is a bug in NSUML. It has the include and include2
        // associations swapped over (include should be the other end of base,
        // not additon). We deal with this elsewhere by swapping "base" and
        // "addition"

        if(base != null) {
            base.removeInclude(include);
        }

        // Finally remove from the namespace

        if (ns != null) {
            ns.removeOwnedElement(include);
        }

        // Having removed an include relationship, mark as needing saving

        Project p = ProjectBrowser.TheInstance.getProject();
        p.setNeedsSave(true);

        // Tell Swing this entry has gone
        
        fireIntervalRemoved(this,index,index);
        */
    }


    /**
     * <p>Implement the action that occurs with the "MoveUp" pop up.</p>
     *
     * <p>Move the include relationship at the given index in the list up one
     *   (unless it is already at the top). Since we use {@link #moveUpUtil}
     *   there is no need to test for unusual cases.</p>
     *
     * <p><em>Note</em>. There is a bug in NSUML, where the "include" and
     *   "include2" associations of a use case are back to front, i.e "include"
     *   is used as the opposite end of "addition" to point to an including use
     *   case, rather than an included use case.  Fixed within the include
     *   relationship, rather than the use case, by reversing the use of access
     *   functions for the "base" and "addition" associations in the code.</p>
     *
     * @param index  the index in the list of the include relationship to move
     *               up.
     */

    public void moveUp(int index) {

        // Only do this if we are a use case

        Object target = getTarget();

        if (!(target instanceof MUseCase)) {
            return;
        }

        MUseCase useCase = (MUseCase) target;
        useCase.setIncludes(moveUpUtil(useCase.getIncludes(), index));

        // Having moved an include relationship, mark as needing saving

        Project p = ProjectBrowser.TheInstance.getProject();
        p.setNeedsSave(true);

        // Tell Swing

        fireContentsChanged(this, index - 1, index);
    }
    

    /**
     * <p>The action that occurs with the "MoveDown" pop up.</p>
     *
     * <p>Move the include relationship at the given index in the list down one
     *   (unless it is already at the bottom). Since we use {@link #moveUpUtil}
     *   there is no need to test for unusual cases.</p>
     *
     * <p><em>Note</em>. There is a bug in NSUML, where the "include" and
     *   "include2" associations of a use case are back to front, i.e "include"
     *   is used as the opposite end of "addition" to point to an including use
     *   case, rather than an included use case.  Fixed within the include
     *   relationship, rather than the use case, by reversing the use of access
     *   functions for the "base" and "addition" associations in the code.</p>
     *
     * @param index  the index in the list of the include relationship to move
     *               down.
     */

    public void moveDown(int index) {

        // Only do this if we are a use case

        Object target = getTarget();

        if (!(target instanceof MUseCase)) {
            return;
        }

        MUseCase useCase = (MUseCase) target;
        useCase.setIncludes(moveDownUtil(useCase.getIncludes(), index));

        // Having moved an include relationship, mark as needing saving

        Project p = ProjectBrowser.TheInstance.getProject();
        p.setNeedsSave(true);

        // Tell Swing

        fireContentsChanged(this,index,index+1);
    }
    
    /**
	 * @see org.argouml.uml.ui.UMLModelElementListModel#buildPopup(JPopupMenu, int)
	 */
	public boolean buildPopup(JPopupMenu popup, int index) {
		UMLUserInterfaceContainer container = getContainer();
        UMLListMenuItem open = new UMLListMenuItem(container.localize("Open"),this,"open",index);
        UMLListMenuItem delete = new UMLListMenuItem(container.localize("Delete"),this,"delete",index);
        if(getModelElementSize() <= 0) {
            open.setEnabled(false);
            delete.setEnabled(false);
        }

        popup.add(open);
        UMLListMenuItem add =new UMLListMenuItem(container.localize("Add"),this,"add",index);
        if(_upper >= 0 && getModelElementSize() >= _upper) {
            add.setEnabled(false);
        }
        popup.add(add);
        popup.add(delete);

        return true;
	}


} /* End of class UMLIncludeListModel */




