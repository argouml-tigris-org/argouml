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

// 28 Mar 2002: Jeremy Bennett (mail@jeremybennett.com). Completely rewritten
// to support proper UML extension points.


package org.argouml.uml.ui.behavior.use_cases;

import java.util.*;
import java.awt.*;

import javax.swing.*;

import ru.novosoft.uml.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.behavior.use_cases.*;

import org.argouml.ui.*;
import org.argouml.uml.ui.UMLListMenuItem;
import org.argouml.uml.ui.UMLModelElementListLinkModel;
import org.argouml.uml.ui.UMLUserInterfaceContainer;
import org.apache.commons.logging.Log;
import org.argouml.kernel.*;


/**
 * <p>A list model for extension points on extend relationship and use case
 *   property panels.</p> 
 *
 * <p>Supports the full menu (Open, Add, Delete, Move Up, Move Down).</p>
 *
 * <p>In addition, when the list appears on an extend relationship, a "Link"
 *   menu entry is provided to link to existing extension points.</p>
 *
 * <p> Provides its own formatElement routine, to use the location rather than
 *   the name of the extension point.</p>
 */

public class UMLExtensionPointListModel extends UMLModelElementListLinkModel  {
    protected static Log logger = org.apache.commons.logging.LogFactory.getLog(UMLExtensionPointListModel.class);

    /**
     * <p>The default text when there is no location for the
     * extensionPoint.</p>
     */
 
    final private static String _nullLocation = "(anon)";


    /**
     * <p>Create a new list model.<p>
     *
     * <p>Implementation is just an invocation of the parent constructor,
     *   passing in the class of interest.</p>
     *
     * @param container  The container for this list&mdash;the use case
     *                   or extend property panel.
     *
     * @param showNone   True if an empty list is represented by the keyword
     *                   "none"
     *
     * @param useLink    <code>true</code> if the "Link" menu entry should be
     *                   shown (for extend relationships), <code>false</code>
     *                   otherwise (for use cases).
     */

    public UMLExtensionPointListModel(UMLUserInterfaceContainer container,
                                      boolean showNone, boolean useLink) {

        super(container, MExtensionPoint.class, showNone, useLink);
    }


    /**
     * <p>Compute the size of the list model. This method must be provided to
     *   override the abstract method in the parent.</p>
     *
     * @return the number of elements the list model (0 if there are none).
     */

    protected int recalcModelElementSize() {
        int        size            = 0;
        Collection extensionPoints = getExtensionPoints();

        if(extensionPoints != null) {
            size = extensionPoints.size();
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

        return elementAtUtil(getExtensionPoints(), index,
                             MExtensionPoint.class);
    }
            
        
    /**
     * <p>A private utility to get the list of extensionPoints relationships
     *   for this extends relationship or use case.</p>
     *
     * <p>We use the approrpiate call depending on whether our container is a
     *   use case or and extend relationship.</p>
     *
     * @return  the list of extension points for this use case.
     */

    private Collection getExtensionPoints() {

        Collection extensionPoints = null;
        Object     target          = getTarget();

        // If we are a use case, it is just the extension points for that use
        // case. If we are an Extend relationship, it is the extension points
        // of that relationship.

        if (target instanceof MUseCase) {
            MUseCase useCase = (MUseCase) target;

            extensionPoints = useCase.getExtensionPoints();
        }
        else if (target instanceof MExtend) {
            MExtend extend   = (MExtend) target;
            
            extensionPoints = extend.getExtensionPoints();
        }

        return extensionPoints;
    }
    

    /**
     * <p>Format a given model element.</p>
     *
     * <p>If there is no location (or it is the empty string), use the default
     *   text ("(anon)").
     * <p>If there is use "name: location", with "name:" omitted if
     *   undefined.</p> 
     *
     * <p>In this current implementation, more rigorously checks it is
     *   formatting an extensionPoint relationship. We can't just use the
     *   parent formatter, since that just takes the name. Instead we use the
     *   notation generator, which knows what to do with an extension
     *   point.</p>
     *
     * @param element  the model element to format
     *
     * @return an object (typically a string) representing the element.
     */

    public Object formatElement(MModelElement element) {
    	return element.getName();
    }


    /**
     * <p>Implement the "link" function of the pop up menu.</p>
     *
     * <p>The sub-index identifies an existing {@link MExtensionPoint}, which
     *   we add as an extension point for this extend relationship at the
     *   position identified by main-index. Then navigate to the
     *   extensionPoint.</p>
     *
     * <p>This should only be invoked if we are a list in an extend
     *   relationship.</p>
     *
     * <p> Only invoked when in an extend relationship property panel.</p>
     *
     * @param index     Offset in the main list of extension points for this
     *                  at which the pop-up was invoked.
     *
     * @param subEntry  The extension point in the sub-menu on which we were
     *                  invoked.
     */

    public void link(int index, MModelElement subEntry) {

        // Give up if the target isn't an extend relationship or if it doesn't
        // have a namespace.

        Object target = getTarget();

        if (!(target instanceof MExtend)) {
            return;
        }

        MExtend    extend = (MExtend) target;
        MNamespace ns     = extend.getNamespace();

        if (ns == null) {
            return;
        }

        // Link in to the extension point. Note that (unlike the "Add" menu) we
        // don't set anything in the extension point.

        MExtensionPoint extensionPoint = (MExtensionPoint) subEntry;

        // Put the extension point in the list of extension points of the
        // extend relationship in the correct place. This will automatically
        // add the reverse link in NSUML.

        if(index == getModelElementSize()) {
            extend.addExtensionPoint(extensionPoint);
        }
        else {
            extend.setExtensionPoints(addAtUtil(extend.getExtensionPoints(),
                                                extensionPoint,
                                                index));
        }

        // Having added an extension point, mark as needing saving

        Project p = ProjectBrowser.TheInstance.getProject();
        p.setNeedsSave(true);

        // Advise Swing that we have added something at this index and
        // navigate there.

        fireIntervalAdded(this,index,index);
        navigateTo(extensionPoint);
    }
    

    /**
     * <p>Implement the "add" function of the pop up menu.</p>
     *
     * <p>Create a new {@link MExtensionPoint} in the same namespace as the
     *   target (do nothing if it doesn't have a namespace). Then navigate to
     *   the extensionPoint. Uses the NSUML Factory class.</p>
     *
     * <p>There are two variants of the code, according as whether the target
     *   is an extend relationship or use case.</p>
     *
     * @param index  Offset in the list of the element at which the pop-up was
     *               invoked.
     */

    public void add(int index) {

        // Give up if the target isn't an extend relationship or use case or if
        // it doesn't have a namespace.

        Object target = getTarget();

        if (!(target instanceof MExtend) && !(target instanceof MUseCase)) {
            return;
        }

        MNamespace ns = ((MModelElement) target).getNamespace();

        if (ns == null) {
            return;
        }

        // Get the new extensionPoint relationship from the factory and add it
        // to the namespace.

        MExtensionPoint newExtensionPoint =
            ns.getFactory().createExtensionPoint();
        ns.addOwnedElement(newExtensionPoint);

        // Now deal with the two cases of an Extend relationship and a UseCase
        // relationship. In the former case, we can potentially also identify
        // the owning use case of the extension point (our base)

        if (target instanceof MExtend) {
            MExtend extend = (MExtend) target;

            // If we have a base use case, make it the owning use case of this
            // extension point. NSUML will add it to the end of the list in the
            // Use Case for us. We can always reorder from the use case.

            MUseCase base = extend.getBase();

            if (base != null) {
                newExtensionPoint.setUseCase(base);
            }

            // Put the extension point in the list of extension points of the
            // extend relationship in the correct place. This will
            // automatically add the reverse link in NSUML.

            if(index == getModelElementSize()) {
                extend.addExtensionPoint(newExtensionPoint);
            }
            else {
                extend.setExtensionPoints(
                    addAtUtil(extend.getExtensionPoints(), newExtensionPoint,
                              index));
            }
        }
        else {

            // Must be a useCase

            MUseCase useCase = (MUseCase) target;

            // Now put it in the list of extensionPoints relationships of
            // the use case in the correct place. This is an unordered
            // relationship, so the position is purely aesthetic. NSUML will
            // set up the reverse link for us.

            if(index == getModelElementSize()) {    
                useCase.addExtensionPoint(newExtensionPoint);
            }
            else {
                useCase.setExtensionPoints(
                    addAtUtil(useCase.getExtensionPoints(), newExtensionPoint,
                              index));
            }
        }

        // Having added an extension point, mark as needing saving

        Project p = ProjectBrowser.TheInstance.getProject();
        p.setNeedsSave(true);

        // Advise Swing that we have added something at this index and
        // navigate there.

        fireIntervalAdded(this,index,index);
        navigateTo(newExtensionPoint);
    }
    

    /**
     * <p>Implement the "delete" function of the pop up menu. Delete the
     *   element at the given index.</p>
     *
     * <p>Find the use case at one end and the extend relationships at the
     *   other. Delete their references to this extensionPoint
     *   relationship. Also remove from the namespace.</p>
     *
     * <p><em>Note</em>. We don't actually need to check the target PropPanel
     *   is an extend relationship or use case&mdash;given an extensionPoint
     *   relationship we can delete it.</p>
     *
     * @param index  Offset in the list of the element at which the pop-up was
     *               invoked and which is to be deleted.
     */

    public void delete(int index) {

        // Only do this if it really is an extensionPoints relationship

        MModelElement modElem = getModelElementAt(index);

        if (!(modElem instanceof MExtensionPoint)) {
            return ;
        }

        // Get the extensionPoint relationship and its two ends and namespace.

        MExtensionPoint extensionPoint = (MExtensionPoint) modElem;

        MUseCase   useCase   = extensionPoint.getUseCase();
        Collection extendSet = extensionPoint.getExtends();
        MNamespace ns        = extensionPoint.getNamespace();

        // Remove the use case end of the relationship. Note we do not need to
        // do anything about the extensionPoint relationship's use case
        // attribute - it will have been done by the removeExtensionPoint.

        if (useCase != null) {
            useCase.removeExtensionPoint(extensionPoint);
        }

        // Remove each of the extend relationships. Note we do not need to do
        // anything about the extensionPoint extend attribute - it will have
        // been done by the removeExtensionPoint.

        Iterator iter = extendSet.iterator();

        while(iter.hasNext()) {
            MExtend extend = (MExtend) (iter.next());

            extend.removeExtensionPoint(extensionPoint);
        }

        // Finally remove from the namespace

        if (ns != null) {
            ns.removeOwnedElement(extensionPoint);
        }

        // Having removed an extension point, mark as needing saving

        Project p = ProjectBrowser.TheInstance.getProject();
        p.setNeedsSave(true);

        // Tell Swing this entry has gone
        
        fireIntervalRemoved(this,index,index);
    }


    /**
     * <p>Implement the action that occurs with the "MoveUp" pop up.</p>
     *
     * <p>Move the extensionPoint at the given index in the list up one
     *   (unless it is already at the top). Since we use {@link #moveUpUtil}
     *   there is no need to test for unusual cases.</p>
     *
     * @param index  the index in the list of extensionPoints to move up.
     */

    public void moveUp(int index) {

        // Only do this if the target is an extend relationship or use case

        Object target = getTarget();

        if (!(target instanceof MExtend) && !(target instanceof MUseCase)) {
            return;
        }

        // Deal separately with moving extend relationships and use cases

        if (target instanceof MExtend) {
            MExtend extend = (MExtend) target;
            extend.setExtensionPoints(moveUpUtil(extend.getExtensionPoints(),
                                                 index));
        }
        else {
            MUseCase useCase = (MUseCase) target;
            useCase.setExtensionPoints(moveUpUtil(useCase.getExtensionPoints(),
                                                  index));
        }

        // Having moved an extension point, mark as needing saving

        Project p = ProjectBrowser.TheInstance.getProject();
        p.setNeedsSave(true);

        // Tell Swing

        fireContentsChanged(this, index - 1, index);
    }
    

    /**
     * <p>Implement the action that occurs with the "MoveDown" pop up.</p>
     *
     * <p>Move the extensionPoint at the given index in the list down one
     * (unless it is already at the bottom). Since we use {@link #moveUpUtil}
     * there is no need to test for unusual cases.</p>
     *
     * @param index  the index in the list of the extensionPoint to move down.
     */

    public void moveDown(int index) {

        // Only do this if the target is an extend relationship or use case

        Object target = getTarget();

        if (!(target instanceof MExtend) && !(target instanceof MUseCase)) {
            return ;
        }

        // Deal separately with moving extend relationships and use cases

        if (target instanceof MExtend) {
            MExtend extend = (MExtend) target;
            extend.setExtensionPoints(moveDownUtil(extend.getExtensionPoints(),
                                                   index));
        }
        else {
            MUseCase useCase = (MUseCase) target;
            useCase.setExtensionPoints(
                moveDownUtil(useCase.getExtensionPoints(), index));
        }

        // Having moved an extension point, mark as needing saving

        Project p = ProjectBrowser.TheInstance.getProject();
        p.setNeedsSave(true);

        // Tell Swing

        fireContentsChanged(this,index,index+1);
    }
    


    /**
     * <p>The routine to determine if a "Link" sub-menu entry is valid.</p>
     *
     * <p>We should only be invoked when the target is an extend
     *   relationship.</p>
     *
     * <p>The routine should return one of the three predefined constants,
     *   {#SEMANTIC_ENTRY}, {#SYNTACTIC_ENTRY} or {#INVALID_ENTRY}, according
     *   to whether the given model element is valid i) semantically and
     *   syntactically , ii) only syntactically or iii) not valid at all.</p>
     *
     * <p>Semantically valid entries are extension points whose owning use case
     *   is <code>null</code> or the same as the base use case of the extend
     *   relationship or any extension point in the case where the extend
     *   relationship has no base use case defined. Syntactically valid entries
     *   are all other extension points.</p>
     *
     * @param me  The {@link MModelElement} to test.
     *
     * @return    {#SEMANTIC_ENTRY} if the element is valid both semantically
     *            and syntactically, {#SYNTACTIC_ENTRY} if the element is valid
     *            only syntactically, and {#INVALID_ENTRY} if the element is
     *            not valid. The effect of returning any other value is
     *            undefined.  */

    protected int isAcceptable(MModelElement me) {
        Object target = getTarget();

        // Give up if we are not an extend relationship

        if (!(target instanceof MExtend)) {
            return INVALID_ENTRY;
        }

        MExtend         extend         = (MExtend) target;

        // Give up if we are syntactically invalid

        if (!(me instanceof MExtensionPoint)) {
            return INVALID_ENTRY;
        }

        MExtensionPoint extensionPoint = (MExtensionPoint) me;

        // Give up if we are already an extension point for this extend
        // relationship

        if (extend.getExtensionPoints().contains(me)) {
            return INVALID_ENTRY;
        }

        // Are we semantically valid as well?

        MUseCase  base    = extend.getBase();
        MUseCase  useCase = extensionPoint.getUseCase();

        if ((base == null) ||
            (useCase == null) ||
            (base == useCase)) {
            return SEMANTIC_ENTRY;
        }

        // Not semantically valid, only syntactically

        return SYNTACTIC_ENTRY;
    }

	/**
	 * @see org.argouml.uml.ui.UMLModelElementListModel#buildPopup(JPopupMenu, int)
	 */
	public boolean buildPopup(JPopupMenu popup, int index) {
		UMLUserInterfaceContainer container = getContainer();
        Object                    target    = getTarget();

        // Each entry in turn. First "Open"

        UMLListMenuItem open =
            new UMLListMenuItem(container.localize("Open"), this,
                                "open", index);

		
        if (getModelElementSize() <= 0) {
            open.setEnabled(false);
        }
        
		
		
        popup.add(open);

        // "Add"

        UMLListMenuItem add =
            new UMLListMenuItem(container.localize("New"), this,
                                "add", index);

        if ((_upper >= 0) && (getModelElementSize() >= _upper)) {
            add.setEnabled(false);
        }

        popup.add(add);

        // "Link". Create a new sub-menu if the flag is set.

        if (useLink()) {
            JMenu link = new JMenu(container.localize("Link"));

            // Grey out if we didn't have anything on the sub-popup

            if (!buildSubPopup(link, index)) {
                link.setEnabled(false);
            }

            popup.add(link);
        }

        // "Delete"

        UMLListMenuItem delete =
            new UMLListMenuItem(container.localize("Delete"), this,
                                "delete", index);

        if (getModelElementSize() <= 0) {
            delete.setEnabled(false);
        }

        popup.add(delete);

        

        

        return true;
	}

} /* End of class UMLExtensionPointListModel */
