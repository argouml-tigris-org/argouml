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

// 25 Mar 2002: Jeremy Bennett (mail@jeremybennett.com). Tidied up layout, to
// facilitate comparison with UMLGeneralizationListModel.java. Made
// getSpecializations public. Simplified getModelElement to remove duplicated
// checking.


package org.argouml.uml.ui;

import ru.novosoft.uml.*;
import javax.swing.*;
import ru.novosoft.uml.foundation.core.*;
import java.util.*;
import java.awt.*;


/**
 * <p>A concrete class to provide the list of model elements that are
 *   specializations of some other element.</p>
 *
 * <p>This list should support the full set of "Open", "Add", "Delete", "Move
 *   Up" and "Move Down" in its context sensitive menu.</p>
 *
 * <p>Where there is no entry, the default text is "null".</p>
 */

public class UMLSpecializationListModel extends UMLModelElementListModel  {

    /**
     * <p>The default text when there is no entry.</p>
     */
 
    private final static String _nullLabel = "(anon)";
    

    /**
     * <p>Create a new specialization list model.</p>
     *
     * <p>This implementation just invokes the parent constructor directly.</p>
     *
     * @param container  the graphics object containing this list (typically
     *                   some child of {@link PropPanel}, providing access to
     *                   the target GeneralizableElement.
     *
     * @param property   a string that specifies the name of an event that
     *                   should force a refresh of the list model.  A
     *                   <code>null</code> value will cause all events to
     *                   trigger a refresh.  
     *
     * @param showNone   if <code>true</code>, an element labelled "none" will
     *                   be shown where there are no actual entries in the
     *                   list.
     */

    public UMLSpecializationListModel(UMLUserInterfaceContainer container,
                                      String property, boolean showNone) { 

        super(container,property,showNone);
    }
    

    /**
     * <p>The method to recalculate the number of elements in the list. Must
     *   be provided to override the abstract method in the parent class.</p>
     *
     * @return  the number of elements in the list (zero if empty).
     */

    protected int recalcModelElementSize() {

        int        size            = 0;
        Collection specializations = getSpecializations();

        if(specializations != null) {
            size = specializations.size();
        }

        return size;
    }
    
    /**
     * <p>The method to get the GeneralizableElement at a particular index in
     *   the list. Must be provided to override the abstract method in the
     *   parent class.</p>
     *
     * <p>This version is simplified, relying on the {@link #elementAtUtil} to
     *   pick up all odd cases.</p>
     *
     * @param index  the index of the desired element.
     *
     * @return       the element at the given index.
     */

    protected MModelElement getModelElementAt(int index) {
        return elementAtUtil(getSpecializations(), index,
                             MGeneralization.class);
    }
            
        
    /**
     * <p>A utility to construct the list of specializations to display. This
     *   is only used within this class.</p>
     *
     * <p>Gets the target, which should be a generalizableElement. Picks out
     *   the specializations from it.</p>
     *
     * @return  a {@link Collection} of generlizableElements or
     *          <code>null</code> if there are none.
     */

    private Collection getSpecializations() {

        Collection specializations = null;
        Object     target          = getTarget();

        if(target instanceof MGeneralizableElement) {
            MGeneralizableElement genElement = (MGeneralizableElement) target;

            specializations = genElement.getSpecializations();
        }

        return specializations;
    }
    

    /**
     * <p>Format a given model element.</p>
     *
     * <p>If there is no element, use the default text ("(anon)"). If there is
     *   use the parent formatElement on the GeneralizableElement attached as
     *   child to the Generalization , which will ultimately invoke the format
     *   element method of {@link PropPanel}.</p>
     *
     * <p>In this current implementation, more rigorously checks it is
     *   formatting a generalization.</p>
     *
     * @param element  the model element to format
     *
     * @return an object (typically a string) representing the element.
     */

    public Object formatElement(MModelElement element) {

        Object value = _nullLabel;

        if ((element != null) && (element instanceof MGeneralization)) {
            MGeneralization       gen    = (MGeneralization) element;
            MGeneralizableElement target = gen.getChild();

            if(target != null) {
                value = super.formatElement(target);
            }
        }
        else {
            if (element != null) {
                System.out.println("UMLSpecializationListModel." +
                                   "formatElement(): Can't format " +
                                   element.getClass().toString());
            }
        }

        return value;
    }
            

    /**
     * <p>The action that occurs with the "Add" pop up.</p>
     *
     * <p>Create a new generalization after the given index in the list. Set
     *   the parent to the current target and take the namespace from the
     *   parent. Then navigate to this new generalization.</p>
     *
     * <p>It would be nice to use the routines in MMUtil to do this properly,
     *   but at present they only work if both child and parent are defined,
     *   and also do not set the corresponding relationships in the parent and
     *   child (or does NSUML do this for you anyway...?).</p>
     *
     * @param index  the index in the list after which the new generalization
     *               should be added.
     */

    public void add(int index) {

        // Give up if the target isn't a use case or if it doesn't have a
        // namespace.

        Object target = getTarget();

        if (!(target instanceof MGeneralizableElement)) {
            return;
        }

        MGeneralizableElement genElem = (MGeneralizableElement) target;
        MNamespace            ns      = genElem.getNamespace();

        if (ns == null) {
            return;
        }

        // Get the new generalization from the factory, add it to the namespace
        // and link it to the generalizable element. Note that we have nothing
        // for the other end at present.

        MGeneralization newGen = ns.getFactory().createGeneralization();

        ns.addOwnedElement(newGen);

        // Now place the reference in the correct place list of generalizations
        // for the parent. NSUML will automatically set the other end for us.

        if(index == getModelElementSize()) {    
                genElem.addSpecialization(newGen);
        }
        else {
            genElem.setSpecializations(addAtUtil(genElem.getSpecializations(),
                                                 newGen, index));
        }

        // Tell Swing and then navigate there

        fireIntervalAdded(this, index, index);
        navigateTo(newGen);
    }
    

    /**
     * <p>The action that occurs with the "Delete" pop up.</p>
     *
     * <p>Delete the generalization at the given index in the list from the
     *   model. This is done by setting both ends (parent and child) to
     *   null. Garbage collection will do the rest.</p>
     *
     * <p><em>Note</em>. We don't actually need to check the target PropPanel
     *   is a generalisable element&mdash;given a generalization we can delete
     *   it.</p>
     *
     * @param index  the index in the list of the  generalization to be
     *               deleted.
     */

    public void delete(int index) {

        MModelElement modElem = getModelElementAt(index);

        // Only do this for a generalization

        if (!(modElem instanceof MGeneralization)) {
            return;
        }

        // Get the generalization and its two ends and namespace

        MGeneralization gen = (MGeneralization) modElem;

        MGeneralizableElement parent = gen.getParent();
        MGeneralizableElement child  = gen.getChild();
        MNamespace            ns     = gen.getNamespace();

        // Remove the parent end of the relationship. Note we do not need to do
        // anything about the generalization's child attribute - it will have
        // been done by the removeSpecialization

        if (parent != null) {
            parent.removeSpecialization(gen);
        }
            
        // Remove the child end of the relationship. Note we do not need to do
        // anything about the generalization's parent attribute - it will have
        // been done by the removeGeneralization

        if (child != null) {
            child.removeGeneralization(gen);
        }
            
        // Finally remove from the namespace

        if (ns != null) {
            ns.removeOwnedElement(gen);
        }

        // Tell Swing this entry has gone

        fireIntervalRemoved(this,index,index);
    }

    
    /**
     * <p>The action that occurs with the "MoveUp" pop up.</p>
     *
     * <p>Move the generalization at the given index in the list up one (unless
     *   it is already at the top).</p>
     *
     * @param index  the index in the list of the generalization to move up.
     */

    public void moveUp(int index) {

        Object target = getTarget();

        if(target instanceof MGeneralizableElement) {
            MGeneralizableElement genElem = (MGeneralizableElement) target;

            genElem.setGeneralizations(
                moveUpUtil(genElem.getGeneralizations(), index));

            // Tell Swing

            fireContentsChanged(this,index-1,index);
        }
    }
    

    /**
     * <p>The action that occurs with the "MoveDown" pop up.</p>
     *
     * <p>Move the generalization at the given index in the list down one
     *   (unless it is already at the bottom).</p>
     *
     * @param index  the index in the list of the generalization to move down.
     */

    public void moveDown(int index) {

        Object target = getTarget();

        if(target instanceof MGeneralizableElement) {
            MGeneralizableElement genElem = (MGeneralizableElement) target;

            genElem.setGeneralizations(
                moveDownUtil(genElem.getGeneralizations(), index));

            // Tell Swing

            fireContentsChanged(this,index,index+1);
        }
    }
    

} /* End of class UMLSpecializationListModel */
