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

package org.argouml.application.helpers;

import java.util.Hashtable;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.UIManager;

import org.apache.log4j.Logger;
import org.argouml.model.DataTypesHelper;
import org.argouml.model.Model;
import org.argouml.model.ModelFacade;
import org.argouml.uml.util.namespace.StringNamespace;
import org.tigris.gef.util.ResourceLoader;

/**
 * Wrapper around org.tigris.gef.util.ResourceLoader.<p>
 *
 * Necessary since ArgoUML needs some extra init.
 *
 * @since Nov 24, 2002
 * @author jaap.branderhorst@xs4all.nl @stereotype singleton
 */
public final class ResourceLoaderWrapper {

    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(ResourceLoaderWrapper.class);

    private static ImageIcon actionStateIcon;
    private static ImageIcon stateIcon;
    private static ImageIcon initialStateIcon;
    private static ImageIcon deepIcon;
    private static ImageIcon shallowIcon;
    private static ImageIcon forkIcon;
    private static ImageIcon joinIcon;
    private static ImageIcon branchIcon;
    private static ImageIcon junctionIcon;
    private static ImageIcon finalStateIcon;
    private static ImageIcon realizeIcon;
    private static ImageIcon signalIcon;
    private static ImageIcon commentIcon;
    private static ImageIcon callStateIcon;
    private static ImageIcon objectFlowStateIcon;
    private static ImageIcon subactivityStateIcon;


    private Hashtable iconCache = new Hashtable();

    /**
     * Singleton implementation.
     */
    private static ResourceLoaderWrapper instance = new ResourceLoaderWrapper();


    /**
     * Returns the singleton instance.
     *
     * @return ResourceLoaderWrapper
     */
    public static ResourceLoaderWrapper getInstance() {
        return instance;
    }

    /**
     * Constructor for ResourceLoaderWrapper.
     */
    private ResourceLoaderWrapper() {
        initResourceLoader();
    }

    /**
     * Calculate the path to a look and feel object.
     *
     * @param classname
     *            The look and feel classname
     * @param element
     *            The en part of the path.
     * @return the complete path.
     */
    private static String lookAndFeelPath(String classname, String element) {
        return "/org/argouml/Images/plaf/"
            + classname.replace('.', '/')
            + "/toolbarButtonGraphics/"
            + element;
    }

    /**
     * Initializes the resourceloader.
     *
     * LookupIconResource checks if there are locations and extensions known.
     * If there are none, this method is called to initialize the resource
     * loader. Originally, this method was placed within Main but this coupled
     * Main and the resourceLoader too much.
     */
    private static void initResourceLoader() {
	String lookAndFeelClassName;
	if ("true".equals(System.getProperty("force.nativelaf", "false"))) {
	    lookAndFeelClassName = UIManager.getSystemLookAndFeelClassName();
	} else {
	    lookAndFeelClassName = "javax.swing.plaf.metal.MetalLookAndFeel";
	}
	String lookAndFeelGeneralImagePath =
	    lookAndFeelPath(lookAndFeelClassName, "general");
	String lookAndFeelNavigationImagePath =
	    lookAndFeelPath(lookAndFeelClassName, "navigation");
	String lookAndFeelDiagramImagePath =
	    lookAndFeelPath(lookAndFeelClassName, "argouml/diagrams");
	String lookAndFeelElementImagePath =
	    lookAndFeelPath(lookAndFeelClassName, "argouml/elements");
	String lookAndFeelArgoUmlImagePath =
	    lookAndFeelPath(lookAndFeelClassName, "argouml");
	ResourceLoader.addResourceExtension("gif");
	ResourceLoader.addResourceLocation(lookAndFeelGeneralImagePath);
	ResourceLoader.addResourceLocation(lookAndFeelNavigationImagePath);
	ResourceLoader.addResourceLocation(lookAndFeelDiagramImagePath);
	ResourceLoader.addResourceLocation(lookAndFeelElementImagePath);
	ResourceLoader.addResourceLocation(lookAndFeelArgoUmlImagePath);
	ResourceLoader.addResourceLocation("/org/argouml/Images");
	ResourceLoader.addResourceLocation("/org/tigris/gef/Images");
    
	actionStateIcon = ResourceLoader.lookupIconResource("ActionState");
        stateIcon = ResourceLoader.lookupIconResource("State");
        initialStateIcon = ResourceLoader.lookupIconResource("Initial");
        deepIcon = ResourceLoader.lookupIconResource("DeepHistory");
        shallowIcon = ResourceLoader.lookupIconResource("ShallowHistory");
        forkIcon = ResourceLoader.lookupIconResource("Fork");
        joinIcon = ResourceLoader.lookupIconResource("Join");
        branchIcon = ResourceLoader.lookupIconResource("Choice");
        junctionIcon = ResourceLoader.lookupIconResource("Junction");
        finalStateIcon = ResourceLoader.lookupIconResource("FinalState");
        realizeIcon = ResourceLoader.lookupIconResource("Realization");
        signalIcon = ResourceLoader.lookupIconResource("SignalSending");
        commentIcon = ResourceLoader.lookupIconResource("Note");
        callStateIcon = ResourceLoader.lookupIconResource("CallState");
        objectFlowStateIcon = 
            ResourceLoader.lookupIconResource("ObjectFlowState");
        subactivityStateIcon = 
            ResourceLoader.lookupIconResource("SubactivityState");
    }

    /**
     * @see ResourceLoader#addResourceExtension(String)
     */
    public static void addResourceExtension(String extension) {
	ResourceLoader.addResourceExtension(extension);
    }

    /**
     * @see ResourceLoader#addResourceLocation(String)
     */
    public static void addResourceLocation(String location) {
	ResourceLoader.addResourceLocation(location);
    }

    /**
     * @see ResourceLoader#containsExtension(String)
     */
    public static boolean containsExtension(String extension) {
	return ResourceLoader.containsExtension(extension);
    }

    /**
     * @see ResourceLoader#containsLocation(String)
     */
    public static boolean containsLocation(String location) {
	return ResourceLoader.containsLocation(location);
    }

    /**
     * @see ResourceLoader#isInCache(String)
     */
    public static boolean isInCache(String resource) {
	return ResourceLoader.isInCache(resource);
    }

    /**
     * @see ResourceLoader#lookupIconResource(String)
     */
    public static ImageIcon lookupIconResource(String resource) {
	return ResourceLoader.lookupIconResource(resource);
    }

    /**
     * @see ResourceLoader#lookupIconResource(String, ClassLoader)
     */
    public static ImageIcon lookupIconResource(
					       String resource,
					       ClassLoader loader) {
	return ResourceLoader.lookupIconResource(resource, loader);
    }

    /**
     * @see ResourceLoader#lookupIconResource(String, String)
     */
    public static ImageIcon lookupIconResource(String resource, String desc) {
	return ResourceLoader.lookupIconResource(resource, desc);
    }

    /**
     * @see ResourceLoader#lookupIconResource(String, String, ClassLoader)
     */
    public static ImageIcon lookupIconResource(
					       String resource,
					       String desc,
					       ClassLoader loader) {
	return ResourceLoader.lookupIconResource(resource, desc, loader);
    }

    /**
     * @see ResourceLoader#removeResourceExtension(String)
     */
    public static void removeResourceExtension(String extension) {
	ResourceLoader.removeResourceExtension(extension);
    }

    /**
     * @see ResourceLoader#removeResourceLocation(String)
     */
    public static void removeResourceLocation(String location) {
	ResourceLoader.removeResourceExtension(location);
    }

    /**
     * Find the Icon for a given model element.
     *
     * @return The Icon.
     * @param value The model element.
     *
     * TODO: This should not use string matching on classnames to do this
     *       since this means that we have knowledge about how the model
     *       elements are implemented outside of the Model component.
     */
    public Icon lookupIcon(Object value) {
        if (value == null) {
            throw new IllegalArgumentException(
                    "Attempted to get an icon given a null key");
        }

	Icon icon = (Icon) iconCache.get(value.getClass());

        if (ModelFacade.isAPseudostate(value)) {

            Object kind = ModelFacade.getKind(value);
            DataTypesHelper helper = Model.getUmlHelper().getDataTypes();
            if (helper.equalsINITIALKind(kind)) {
                icon = initialStateIcon;
            }
            if (helper.equalsDeepHistoryKind(kind)) {
                icon = deepIcon;
            }
            if (helper.equalsShallowHistoryKind(kind)) {
                icon = shallowIcon;
            }
            if (helper.equalsFORKKind(kind)) {
                icon = forkIcon;
            }
            if (helper.equalsJOINKind(kind)) {
                icon = joinIcon;
            }
            if (helper.equalsBRANCHKind(kind)) {
                icon = branchIcon;
            }
            if (helper.equalsJUNCTIONKind(kind)) {
                icon = junctionIcon;
            }
            // if (MPseudostateKind.FINAL.equals(kind))
            // icon = _FinalStateIcon;
        }

        if (ModelFacade.isAAbstraction(value)) {
            icon = realizeIcon;
        }
        // needs more work: sending and receiving icons
        if (ModelFacade.isASignal(value)) {
            icon = signalIcon;
        }

        if (ModelFacade.isAComment(value)) {
            icon = commentIcon;
        }

        if (icon == null) {

            StringNamespace sns =
                (StringNamespace) StringNamespace.parse(value.getClass());
            StringNamespace org =
                new StringNamespace(new String[] {"org"});
            StringNamespace ru = new StringNamespace(new String[] {"ru"});

            String cName = sns.popNamespaceElement().toString();
            if (ru.equals(sns.getCommonNamespace(ru))
                    || org.equals(sns.getCommonNamespace(org))) {

                if (cName.startsWith("UML")) {
                    cName = cName.substring(3);
                }
                if (cName.startsWith("M")) {
                    cName = cName.substring(1);
                }
                if (cName.endsWith("Impl")) {
                    cName = cName.substring(0, cName.length() - 4);
                }
            }
            icon = lookupIconResource(cName);
            if (icon == null) {
                LOG.warn("Can't find icon for " + cName);
            } else {
                synchronized (iconCache) {
                    iconCache.put(value.getClass(), icon);
                }
            }

        }
	return icon;
    }
}
