// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.Action;

import org.apache.log4j.Logger;
import org.argouml.i18n.Translator;
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
public class CmdCreateNode extends org.tigris.gef.base.CmdCreateNode {
    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(CmdCreateNode.class);

    /**
     * The cache for once found creation methods.
     */
    private static Dictionary cache = new Hashtable();

    /**
     * List of factories to search for the methods in.
     */
    private static List factories = new Vector();
    static {
        factories.add(Model.getActivityGraphsFactory());
        factories.add(Model.getCollaborationsFactory());
        factories.add(Model.getCommonBehaviorFactory());
        factories.add(Model.getCoreFactory());
        factories.add(Model.getDataTypesFactory());
        factories.add(Model.getExtensionMechanismsFactory());
        factories.add(Model.getModelManagementFactory());
        factories.add(Model.getStateMachinesFactory());
        factories.add(Model.getUseCasesFactory());
    }

    /**
     * Prefix for the action key.
     */
    private static final String ACTION_PREFIX_KEY = "action.new";

    /**
     * Constructor for CmdCreateNode.
     *
     * @param args a hastable of arguments
     * @param resource for localizing the name
     * @param name the to be localized tooltip name
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
     */
    public CmdCreateNode(Hashtable args, String name) {
        super(args, name);
        putToolTip(name);
    }

    /**
     * Constructor for CmdCreateNode.
     *
     * @param nodeClass the class for which to create a node, and which
     *                  to create itself
     * @param resource for localizing the name
     * @param name the tooltip name
     */
    public CmdCreateNode(Class nodeClass, String resource, String name) {
        super(nodeClass, resource, name);
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
        super((Class) nodeClass, name);
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
        super(nodeClass, sticky, resource, name);
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
     */
    public CmdCreateNode(Object nodeClass, boolean sticky, String name) {
        super((Class) nodeClass, sticky, name);
        putToolTip(name);
    }

    /**
     * Creates a modelelement using the uml model factories. If it finds a
     * suitable match it will but the factory and method of this factory in a
     * cache, so that subsequent lookups do not have to iterate through all
     * methods again. If no match is found it will delegate to
     * <code>super.makeNode()</code>.
     *
     * @return an object which represents in most cases a particular UML
     *         Element.
     *
     * @see org.tigris.gef.graph.GraphFactory#makeNode()
     * @see org.tigris.gef.base.CmdCreateNode#makeNode()
     */
    public Object makeNode() {
        LOG.info("Trying to create a new node");
        // here we should implement usage of the factories
        // since i am kind of lazy i use reflection

        // factories

        try {
            Object[] cachedParams = (Object[]) cache
                    .get(_args.get("className"));
            if (cachedParams != null) {
                LOG.debug("Using method and factory from cache");
                return ((Method) cachedParams[1]).invoke(cachedParams[0],
                        new Object[] {});
            }
            Iterator it = factories.iterator();
            while (it.hasNext()) {
                Object factory = it.next();
                List createMethods =
                    Arrays.asList(factory.getClass().getMethods());
                Iterator it2 = createMethods.iterator();
                String classname = getCreateClassName();
                while (it2.hasNext()) {
                    Method method = (Method) it2.next();
                    String methodname = method.getName();
                    if (methodname.endsWith(classname)
                            && (methodname.substring(0, methodname
                                    .lastIndexOf(classname)).equals("build"))
                            && method.getParameterTypes().length == 0) {
                        LOG.debug("Using method: " + method);
                        Object[] params = new Object[] {
			    factory, method,
			};
                        cache.put(_args.get("className"), params);
                        return method.invoke(factory, new Object[] {});
                    }
                }
                it2 = createMethods.iterator();
                while (it2.hasNext()) {
                    Method method = (Method) it2.next();
                    String methodname = method.getName();
                    if (methodname.endsWith(classname)
                            && (methodname.substring(0,
			            methodname.lastIndexOf(classname))
				    .equals("create"))) {
                        LOG.debug("Using method: " + method);
                        Object[] params = new Object[] {
			    factory, method,
			};
                        cache.put(_args.get("className"), params);
                        return method.invoke(factory, new Object[] {});
                    }
                }

            }
        } catch (IllegalAccessException e2) {
            LOG.error(e2);
        } catch (InvocationTargetException e3) {
            LOG.error(e3);
        }
        LOG.debug("delegating to super.makeNode");
        return super.makeNode();
    }

    /**
     * Returns the name of the uml modelelement without impl, M
     * or the fullname.
     *
     * @return String
     */
    private String getCreateClassName() {
        String name = ((Class) _args.get("className")).getName();
        name = name.substring(name.lastIndexOf('.') + 2, name.length());
        if (name.endsWith("Impl")) {
            name = name.substring(0, name.lastIndexOf("Impl"));
        }
        return name;
    }

    /**
     * Adds tooltip text to the Action.
     *
     * @param name The key to localize as the name.
     */
    private void putToolTip(String name) {
        putValue(Action.SHORT_DESCRIPTION, Translator
                .localize(ACTION_PREFIX_KEY)
                + " " + Translator.localize(name));
    }
}
