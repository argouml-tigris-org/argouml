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

package org.argouml.application.helpers;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.UIManager;

import org.apache.log4j.Logger;
import org.argouml.i18n.Translator;
import org.argouml.model.DataTypesHelper;
import org.argouml.model.InvalidElementException;
import org.argouml.model.Model;


/**
 * Wrapper around org.tigris.gef.util.ResourceLoader.<p>
 *
 * Necessary since ArgoUML needs some extra init.
 *
 * @since Nov 24, 2002
 * @author jaap.branderhorst@xs4all.nl 
 * @stereotype singleton
 */
public final class ResourceLoaderWrapper {

    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(ResourceLoaderWrapper.class);

    private static ImageIcon initialStateIcon;
    private static ImageIcon deepIcon;
    private static ImageIcon shallowIcon;
    private static ImageIcon forkIcon;
    private static ImageIcon joinIcon;
    private static ImageIcon branchIcon;
    private static ImageIcon junctionIcon;
    private static ImageIcon realizeIcon;
    private static ImageIcon signalIcon;
    private static ImageIcon commentIcon;

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
        ResourceLoader.addResourceExtension("png");
	ResourceLoader.addResourceLocation(lookAndFeelGeneralImagePath);
	ResourceLoader.addResourceLocation(lookAndFeelNavigationImagePath);
	ResourceLoader.addResourceLocation(lookAndFeelDiagramImagePath);
	ResourceLoader.addResourceLocation(lookAndFeelElementImagePath);
	ResourceLoader.addResourceLocation(lookAndFeelArgoUmlImagePath);
	ResourceLoader.addResourceLocation("/org/argouml/Images");
	ResourceLoader.addResourceLocation("/org/tigris/gef/Images");

        // Initialze GEF's version of the loader too
        // TODO: We should probably be passing icons that we loaded ourselves
        // but there doesn't seem to be a way to do that with GEF - tfm
        org.tigris.gef.util.ResourceLoader.addResourceExtension("gif");
        org.tigris.gef.util.ResourceLoader.addResourceExtension("png");
        org.tigris.gef.util.ResourceLoader
                .addResourceLocation(lookAndFeelGeneralImagePath);
        org.tigris.gef.util.ResourceLoader
                .addResourceLocation(lookAndFeelNavigationImagePath);
        org.tigris.gef.util.ResourceLoader
                .addResourceLocation(lookAndFeelDiagramImagePath);
        org.tigris.gef.util.ResourceLoader
                .addResourceLocation(lookAndFeelElementImagePath);
        org.tigris.gef.util.ResourceLoader
                .addResourceLocation(lookAndFeelArgoUmlImagePath);
        org.tigris.gef.util.ResourceLoader
                .addResourceLocation("/org/argouml/Images");
        org.tigris.gef.util.ResourceLoader
                .addResourceLocation("/org/tigris/gef/Images");
        
        initialStateIcon = ResourceLoader.lookupIconResource("Initial");
        deepIcon = ResourceLoader.lookupIconResource("DeepHistory");
        shallowIcon = ResourceLoader.lookupIconResource("ShallowHistory");
        forkIcon = ResourceLoader.lookupIconResource("Fork");
        joinIcon = ResourceLoader.lookupIconResource("Join");
        branchIcon = ResourceLoader.lookupIconResource("Choice");
        junctionIcon = ResourceLoader.lookupIconResource("Junction");
        realizeIcon = ResourceLoader.lookupIconResource("Realization");
        signalIcon = ResourceLoader.lookupIconResource("SignalSending");
        commentIcon = ResourceLoader.lookupIconResource("Note");
    }

    /**
     * Find the correct icon for a key.
     *
     * @param resource The name of the resource to look up.
     * @return The ImageIcon.
     */
    public static ImageIcon lookupIconResource(String resource) {
	return ResourceLoader.lookupIconResource(resource);
    }

    /**
     * Find the correct icon for a key.
     *
     * @param resource The name of the resource to look up.
     * @param desc The description for the icon.
     * @return The ImageIcon.
     */
    public static ImageIcon lookupIconResource(String resource, String desc) {
	return ResourceLoader.lookupIconResource(resource, desc);
    }

    /**
     * Look up the Icon for a key.
     *
     * @param key The key to find.
     * @return The found Icon.
     */
    public static Icon lookupIcon(String key) {
        return lookupIconResource(getImageBinding(key),
                		  Translator.localize(key));
    }

    /**
     * Find the Icon for a given model element.
     *
     * @return The Icon or <code>null</code> if there is no Icon.
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

        if (value instanceof String) {
            return null;
        }

        Icon icon = (Icon) iconCache.get(value.getClass());
        
        try {
            if (Model.getFacade().isAPseudostate(value)) {
                
                Object kind = Model.getFacade().getKind(value);
                DataTypesHelper helper = Model.getDataTypesHelper();
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
                if (helper.equalsCHOICEKind(kind)) {
                    icon = branchIcon;
                }
                if (helper.equalsJUNCTIONKind(kind)) {
                    icon = junctionIcon;
                }
                // if (MPseudostateKind.FINAL.equals(kind))
                // icon = _FinalStateIcon;
            }
            
            if (Model.getFacade().isAAbstraction(value)) {
                icon = realizeIcon;
            }
            // needs more work: sending and receiving icons
            if (Model.getFacade().isASignal(value)) {
                icon = signalIcon;
            }
            
            if (Model.getFacade().isAComment(value)) {
                icon = commentIcon;
            }
            
            if (icon == null) {
                
                String cName = Model.getMetaTypes().getName(value);
                
                icon = lookupIconResource(cName);
                if (icon == null) {
                    LOG.warn("Can't find icon for " + cName);
                } else {
                    synchronized (iconCache) {
                        iconCache.put(value.getClass(), icon);
                    }
                }
                
            }
        } catch (InvalidElementException e) {
            LOG.debug("Attempted to get icon for deleted element");
            return null;
        }
        return icon;
    }

    /**
     * Map to convert tokens into file names.
     */
    private static Map images = new HashMap();
    static {
        images.put("action.about-argouml", "AboutArgoUML");
        images.put("action.activity-diagram", "Activity Diagram");
        images.put("action.class-diagram", "Class Diagram");
        images.put("action.collaboration-diagram", "Collaboration Diagram");
        images.put("action.deployment-diagram", "Deployment Diagram");
        images.put("action.sequence-diagram", "Sequence Diagram");
        images.put("action.state-diagram", "State Diagram");
        images.put("action.usecase-diagram", "Use Case Diagram");
    }

    static {
        images.put("action.add-concurrent-region", "Add Concurrent Region");
        images.put("action.add-message", "Add Message");
        images.put("action.configure-perspectives", "ConfigurePerspectives");
        images.put("action.copy", "Copy");
        images.put("action.cut", "Cut");
        images.put("action.delete-concurrent-region", "DeleteConcurrentRegion");
        images.put("action.delete-from-model", "DeleteFromModel");
        images.put("action.find", "Find...");
        images.put("action.import-sources", "Import Sources...");
        images.put("action.more-info", "More Info...");
        images.put("action.navigate-back", "Navigate Back");
        images.put("action.navigate-forward", "Navigate Forward");
        images.put("action.new", "New");
        images.put("action.new-todo-item", "New To Do Item...");
        images.put("action.open-project", "Open Project...");
        images.put("action.page-setup", "Page Setup...");
        images.put("action.paste", "Paste");
        images.put("action.print", "Print...");
        images.put("action.remove-from-diagram", "Remove From Diagram");
        images.put("action.resolve-item", "Resolve Item...");
        images.put("action.save-project", "Save Project");
        images.put("action.save-project-as", "Save Project As...");
        images.put("action.settings", "Settings...");
        images.put("action.snooze-critic", "Snooze Critic");
        images.put("action.system-information", "System Information");
    }

    static {
        images.put("button.broom", "Broom");
        images.put("button.new-actionstate", "ActionState");
        images.put("button.new-actor", "Actor");
        images.put("button.new-aggregation", "Aggregation");
        images.put("button.new-association", "Association");
        images.put("button.new-associationclass", "AssociationClass");
        images.put("button.new-association-end", "AssociationEnd");
        images.put("button.new-associationrole", "AssociationRole");
        images.put("button.new-attribute", "New Attribute");
        images.put("button.new-callaction", "CallAction");
        images.put("button.new-callstate", "CallState");
        images.put("button.new-choice", "Choice");
        images.put("button.new-class", "Class");
        images.put("button.new-classifierrole", "ClassifierRole");
        images.put("button.new-commentlink", "CommentLink");
        images.put("button.new-component", "Component");
        images.put("button.new-componentinstance", "ComponentInstance");
        images.put("button.new-compositestate", "CompositeState");
        images.put("button.new-composition", "Composition");
        images.put("button.new-createaction", "CreateAction");
        images.put("button.new-datatype", "DataType");
        images.put("button.new-deephistory", "DeepHistory");
        images.put("button.new-dependency", "Dependency");
        images.put("button.new-destroyaction", "DestroyAction");
        images.put("button.new-enumeration", "Enumeration");
        images.put("button.new-extension-point", "New Extension Point");
        images.put("button.new-extend", "Extend");
    }

    static {
        images.put("button.new-finalstate", "FinalState");
        images.put("button.new-fork", "Fork");
        images.put("button.new-generalization", "Generalization");
        images.put("button.new-include", "Include");
        images.put("button.new-initial", "Initial");
    }

    static {
        images.put("button.new-inner-class", "Inner Class");
        images.put("button.new-interface", "Interface");
        images.put("button.new-join", "Join");
        images.put("button.new-junction", "Junction");
        images.put("button.new-link", "Link");
        images.put("button.new-node", "Node");
        images.put("button.new-nodeinstance", "NodeInstance");
        images.put("button.new-object", "Object");
        images.put("button.new-objectflowstate", "ObjectFlowState");
    }

    static {
        images.put("button.new-operation", "New Operation");
        images.put("button.new-package", "Package");
        images.put("button.new-parameter", "New Parameter");
        images.put("button.new-partition", "Partition");
        images.put("button.new-permission", "Permission");
        images.put("button.new-raised-signal", "New Raised Signal");
        images.put("button.new-reception", "New Reception");
        images.put("button.new-realization", "Realization");
        images.put("button.new-returnaction", "ReturnAction");
        images.put("button.new-sendaction", "SendAction");
        images.put("button.new-shallowhistory", "ShallowHistory");
        images.put("button.new-signal", "New Signal");
        images.put("button.new-simplestate", "SimpleState");
        images.put("button.new-stereotype", "Stereotype");
        images.put("button.new-stubstate", "StubState");
        images.put("button.new-subactivitystate", "SubactivityState");
        images.put("button.new-submachinestate", "SubmachineState");
        images.put("button.new-synchstate", "SynchState");
        images.put("button.new-tagdefinition", "TagDefinition");
        images.put("button.new-transition", "Transition");
        images.put("button.new-uniaggregation", "UniAggregation");
        images.put("button.new-uniassociation", "UniAssociation");
        images.put("button.new-unicomposition", "UniComposition");
        images.put("button.new-usage", "Usage");
        images.put("button.new-usecase", "UseCase");
    }

    static {
        images.put("button.select", "Select");
        images.put("button.sequence-expand", "SequenceExpand");
        images.put("button.sequence-contract", "SequenceContract");
    }

    /**
     * Convert the key to the image file name.
     *
     * @param name the new i18n key
     * @return the file name (base part only).
     */
    public static String getImageBinding(String name) {
        String found = (String) images.get(name);
        if (found == null) {
            return name;
        }
        return found;
    }
}
