/* $Id$
 *******************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    bobtarling
 *******************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2005-2007 The Regents of the University of California. All
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

package org.argouml.model;

import java.util.Collection;

/**
 * The interface for the helper for ActivityGraphs.<p>
 *
 * Created from the old ActivityGraphsHelper.
 */
public interface ActivityGraphsHelper {
    /**
     * Finds the Classifier to which a given ObjectFlowState
     * refers by its given name. This function may be used for when the user
     * types the name of a classifier in the diagram, in an ObjectFlowState.
     *
     * @author MVW
     * @param ofs the given ObjectFlowState
     * @param s   the given String that represents
     *            the name of the "type" Classifier
     * @return    the found classifier or null
     */
    Object findClassifierByName(Object ofs, String s);

    /**
     * Find a state of a Classifier by its name.
     * This routine is used to make the connection between
     * a ClassifierInState and its State.
     *
     * @author mvw
     * @param c the Classifier. If this is not a Classifier, then
     *          IllegalArgumentException is thrown.
     * @param s the string that represents the name of
     *          the state we are looking for. If "" or null, then
     *          null is returned straight away.
     * @return  the State (as Object) or null, if not found.
     */
    Object findStateByName(Object c, String s);

    /**
     * Returns true if an activitygraph may be added to the given
     * context. To decouple ArgoUML as much as possible from the
     * model implementation, the parameter of the method is of
     * type Object.<p>
     *
     * An ActivityGraph specifies the dynamics of<ol>
     * <li> a Package, or
     * <li> a Classifier (including UseCase), or
     * <li> a BehavioralFeature.
     * </ol>
     *
     * @param context the given context
     * @return boolean true if an activitygraph may be added
     */
    boolean isAddingActivityGraphAllowed(Object context);

    /**
     * @author mvw
     * @param classifierInState the classifierInState
     * @param state the state that will be linked
     */
    void addInState(Object classifierInState, Object state);
    
    
    /**
     * Replace the complete collection of states by the given new one. 
     * This function only modifies the model for added or removed states.
     * 
     * @param classifierInState the ClassifierInState to be altered
     * @param newStates the collection of states
     */
    void setInStates(Object classifierInState, Collection newStates);
    
    /**
     * Replace the complete collection of the partition contents 
     * by the given new one. 
     * This function only modifies the model for added or removed modelelements.
     * 
     * @param partition the partition to be altered
     * @param newContents the new contents of the partition (modelelements)
     */
    void setContents(Object partition, Collection newContents);
    
    /**
     * Add a model element to a partition
     * @param partition the partition to contain the model element
     * @param modeElement the model element to place in the partition
     */
    void addContent(Object partition, Object modeElement);
    
    /**
     * Remove a model element from a partition
     * @param partition the partition currently containing the model element
     * @param modeElement the model element to be removed from the partition
     */
    void removeContent(Object partition, Object modeElement);

    /**
     * Set the isSynch attribute of an ObjectFlowState.
     * 
     * @param objectFlowState
     *            the element for which to set the attribute
     * @param isSynch
     *            true if this ObjectFlowState is a synch state.
     */
    void setSynch(Object objectFlowState, boolean isSynch);

    /**
     * Add a Parameter to an ObjectFlowState.
     * 
     * @param objectFlowState
     *            the ObjectFlowState
     * @param parameter
     *            the Parameter
     */
    void addParameter(Object objectFlowState, Object parameter);

    /**
     * Remove Parameter from an ObjectFlowState.
     * 
     * @param objectFlowState
     *            the ObjectFlowState
     * @param parameter
     *            the Parameter to remove
     */
    void removeParameter(Object objectFlowState, Object parameter);

    /**
     * @param objectFlowState
     *            the ObjectFlowState.
     * @param parameters
     *            the collection of Parameters. Pass
     *            {@link java.util.Collections#EMPTY_SET} if there are no
     *            Parameters.
     */
    void setParameters(Object objectFlowState, Collection parameters);
}
