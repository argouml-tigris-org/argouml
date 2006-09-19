// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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

package org.argouml.ui;

import java.util.Hashtable;

import javax.swing.Action;

import org.argouml.i18n.Translator;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.model.Model;

/**
 * Command to create nodes with the appropriate modelelement. The modelelement
 * is initialized via the build methods on the uml factories.
 *
 * First we search for a buildMODELELEMENTNAME method without parameters.
 * If that is not found we use the createMODELELEMENTNAME method.
 *
 * @see org.argouml.model.Model
 * @see org.argouml.model.ActivityGraphsFactory
 * @see org.argouml.model.CollaborationsFactory
 * @see org.argouml.model.CommonBehaviorFactory
 * @see org.argouml.model.CoreFactory
 * @see org.argouml.model.DataTypesFactory
 * @see org.argouml.model.ExtensionMechanismsFactory
 * @see org.argouml.model.ModelManagementFactory
 * @see org.argouml.model.StateMachinesFactory
 * @see org.argouml.model.UseCasesFactory
 * @author jaap.branderhorst@xs4all.nl
 */
// TODO: This should be used for GEF 0.12.2
//public class CmdCreateNode extends CreateNodeAction {
public class CmdCreateNode extends org.tigris.gef.base.CmdCreateNode {
    
    private static final long serialVersionUID = 4813526025971574818L;

    /**
     * Constructor for CmdCreateNode.
     *
     * @param args a hastable of arguments
     * @param resource for localizing the name
     * @param name the to be localized tooltip name
     * @deprecated in 0.23.2 use CmdCreateNode(Object, String)
     */
    public CmdCreateNode(Hashtable args, String resource, String name) {
        super(args, resource, name);
        putToolTip(name);
    }

    /**
     * Constructor for CmdCreateNode.
     *
     * @param args a hastable of arguments
     * @param name the to be localized name of the command = tooltip name
     * @deprecated in 0.23.2 use CmdCreateNode(Object, String)
     */
    public CmdCreateNode(Hashtable args, String name) {
        super(args, ResourceLoaderWrapper.getImageBinding(name));
        putToolTip(name);
    }

    /**
     * Constructor for CmdCreateNode.
     *
     * @param nodeClass the class for which to create a node, and which
     *                  to create itself
     * @param resource for localizing the name
     * @param name the tooltip name
     * @deprecated in 0.23.2 use CmdCreateNode(Object, String)
     */
    public CmdCreateNode(Class nodeClass, String resource, String name) {
        super(nodeClass, resource, ResourceLoaderWrapper.getImageBinding(name));
        putToolTip(name);
    }

    /**
     * Constructor for CmdCreateNode.
     *
     * @param nodeClass the class for which to create a node, and which
     *                  to create itself
     * @param name the tooltip name
     */
    public CmdCreateNode(Object nodeClass, String name) {
        // TODO: This should be used for GEF 0.12.2
//        super((Class) nodeClass,
//                name,
//                ResourceLoaderWrapper.lookupIconResource(
//                        ResourceLoaderWrapper.getImageBinding(name)));
        super((Class) nodeClass, ResourceLoaderWrapper.getImageBinding(name));
        putToolTip(name);
    }

    /**
     * Constructor for CmdCreateNode.
     *
     * @param nodeClass the class for which to create a node, and which
     *                  to create itself
     * @param sticky the global sticky mode boolean allows the user
     *               to place several nodes rapidly (in succession)
     * @param resource for localizing the name
     * @param name the tooltip name
     */
    public CmdCreateNode(Class nodeClass, boolean sticky, String resource,
            String name) {
        super(nodeClass, sticky, resource,
                ResourceLoaderWrapper.getImageBinding(name));
        putToolTip(name);
    }

    /**
     * Constructor for CmdCreateNode.
     *
     * @param nodeClass the class for which to create a node, and which
     *                  to create itself
     * @param sticky the global sticky mode boolean allows the user
     *               to place several nodes rapidly (in succession)
     * @param name the tooltip name
     * @deprecated in 0.23.2 use CmdCreateNode(Object, String)
     */
    public CmdCreateNode(Object nodeClass, boolean sticky, String name) {
        super((Class) nodeClass, sticky,
                ResourceLoaderWrapper.getImageBinding(name));
        putToolTip(name);
    }

    /**
     * Delegate creation of the node to the uml model subsystem.
     *
     * @return an object which represents a particular UML
     *         Element.
     *
     * @see org.tigris.gef.graph.GraphFactory#makeNode()
     * @see org.tigris.gef.base.CmdCreateNode#makeNode()
     */
    public Object makeNode() {
        Object newNode =
            Model.getUmlFactory().buildNode(getArg("className"));
        return newNode;
    }

    /**
     * Adds tooltip text to the Action.
     *
     * @param name The key to localize as the name.
     */
    private void putToolTip(String name) {
        putValue(Action.SHORT_DESCRIPTION, Translator.localize(name));
    }
}
