// Copyright (c) 1996-2002 The Regents of the University of California. All
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

// $header$
package org.argouml.application.helpers;

import java.util.Hashtable;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.UIManager;

import org.tigris.gef.util.ResourceLoader;

import ru.novosoft.uml.behavior.common_behavior.MSignal;
import ru.novosoft.uml.behavior.state_machines.MPseudostate;
import ru.novosoft.uml.foundation.core.MAbstraction;
import ru.novosoft.uml.foundation.core.MComment;
import ru.novosoft.uml.foundation.data_types.MPseudostateKind;

/**
 * Wrapper around org.tigris.gef.util.ResourceLoader. Necessary since ArgoUML needs
 * some extra init
 * @since Nov 24, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public final class ResourceLoaderWrapper {

    protected ImageIcon _ActionStateIcon = ResourceLoader.lookupIconResource("ActionState");
    protected ImageIcon _StateIcon = ResourceLoader.lookupIconResource("State");
    protected ImageIcon _InitialStateIcon = ResourceLoader.lookupIconResource("Initial");
    protected ImageIcon _DeepIcon = ResourceLoader.lookupIconResource("DeepHistory");
    protected ImageIcon _ShallowIcon = ResourceLoader.lookupIconResource("ShallowHistory");
    protected ImageIcon _ForkIcon = ResourceLoader.lookupIconResource("Fork");
    protected ImageIcon _JoinIcon = ResourceLoader.lookupIconResource("Join");
    protected ImageIcon _BranchIcon = ResourceLoader.lookupIconResource("Branch");
    protected ImageIcon _FinalStateIcon = ResourceLoader.lookupIconResource("FinalState");
    protected ImageIcon _RealizeIcon = ResourceLoader.lookupIconResource("Realization");
    protected ImageIcon _SignalIcon = ResourceLoader.lookupIconResource("SignalSending");
    protected ImageIcon _CommentIcon = ResourceLoader.lookupIconResource("Note");

    protected Hashtable _iconCache = new Hashtable();

    /**
     * Singleton implementation
     */
    private static ResourceLoaderWrapper _instance;

    /**
     * Returns the singleton instance
     * @return ResourceLoaderWrapper
     */
    public static ResourceLoaderWrapper getResourceLoaderWrapper() {
        if (_instance == null) {
            _instance = new ResourceLoaderWrapper();
        }
        return _instance;
    }

    /**
     * Constructor for ResourceLoaderWrapper.
     */
    public ResourceLoaderWrapper() {
        super();
        initResourceLoader();
    }

    /**
     * Initializes the resourceloader. LookupIconResource checks if there are locations 
     * and extensions known. If there are none, this method is called to initialize
     * the resource loader. Originally, this method was placed within Main but
     * this coupled Main and the resourceLoader to much.
     */
    private void initResourceLoader() {
        String lookAndFeelClassName;
        if ("true".equals(System.getProperty("force.nativelaf", "false"))) {
            lookAndFeelClassName = UIManager.getSystemLookAndFeelClassName();
        } else {
            lookAndFeelClassName = "javax.swing.plaf.metal.MetalLookAndFeel";
        }
        String lookAndFeelGeneralImagePath = "/org/argouml/Images/plaf/" + lookAndFeelClassName.replace('.', '/') + "/toolbarButtonGraphics/general";
        String lookAndFeelNavigationImagePath = "/org/argouml/Images/plaf/" + lookAndFeelClassName.replace('.', '/') + "/toolbarButtonGraphics/navigation";
        String lookAndFeelDiagramImagePath = "/org/argouml/Images/plaf/" + lookAndFeelClassName.replace('.', '/') + "/toolbarButtonGraphics/argouml/diagrams";
        String lookAndFeelElementImagePath = "/org/argouml/Images/plaf/" + lookAndFeelClassName.replace('.', '/') + "/toolbarButtonGraphics/argouml/elements";
        String lookAndFeelArgoUmlImagePath = "/org/argouml/Images/plaf/" + lookAndFeelClassName.replace('.', '/') + "/toolbarButtonGraphics/argouml";
        ResourceLoader.addResourceExtension("gif");
        ResourceLoader.addResourceLocation(lookAndFeelGeneralImagePath);
        ResourceLoader.addResourceLocation(lookAndFeelNavigationImagePath);
        ResourceLoader.addResourceLocation(lookAndFeelDiagramImagePath);
        ResourceLoader.addResourceLocation(lookAndFeelElementImagePath);
        ResourceLoader.addResourceLocation(lookAndFeelArgoUmlImagePath);
        ResourceLoader.addResourceLocation("/org/argouml/Images");
        ResourceLoader.addResourceLocation("/org/tigris/gef/Images");
    }

    /**
     * Wrapped method
     * @param extension
     */
    public void addResourceExtension(String extension) {
        ResourceLoader.addResourceExtension(extension);
    }

    /**
     * Wrapped method
     * @param location
     */
    public void addResourceLocation(String location) {
        ResourceLoader.addResourceLocation(location);
    }

    /**
     * Wrapped method
     * @param extension
     * @return boolean
     */
    public boolean containsExtension(String extension) {
        return ResourceLoader.containsExtension(extension);
    }

    /**
     * Wrapped method
     * @param location
     * @return boolean
     */
    public boolean containsLocation(String location) {
        return ResourceLoader.containsLocation(location);
    }

    /**
     * Wrapped method
     * @param resource
     * @return boolean
     */
    public boolean isInCache(String resource) {
        return ResourceLoader.isInCache(resource);
    }

    /**
     * Wrapped method
     * @param resource
     * @return ImageIcon
     */
    public ImageIcon lookupIconResource(String resource) {
        return ResourceLoader.lookupIconResource(resource);
    }

    /**
     * Wrapped method
     * @param resource
     * @param loader
     * @return ImageIcon
     */
    public ImageIcon lookupIconResource(String resource, ClassLoader loader) {
        return ResourceLoader.lookupIconResource(resource, loader);
    }

    /**
     * Wrapped method
     * @param resource
     * @param desc
     * @return ImageIcon
     */
    public ImageIcon lookupIconResource(String resource, String desc) {
        return ResourceLoader.lookupIconResource(resource, desc);
    }

    /**
     * Wrapped method
     * @param resource
     * @param desc
     * @param loader
     * @return ImageIcon
     */
    public ImageIcon lookupIconResource(String resource, String desc, ClassLoader loader) {
        return ResourceLoader.lookupIconResource(resource, desc, loader);
    }

    /** 
     * Wrapped method
     * @param extension
     */
    public void removeResourceExtension(String extension) {
        ResourceLoader.removeResourceExtension(extension);
    }

    /**
     * Wrapped method
     * @param location
     */
    public void removeResourceLocation(String location) {
        ResourceLoader.removeResourceExtension(location);
    }

    public Icon lookupIcon(Object value) {
        Icon icon = (Icon) _iconCache.get(value.getClass());

        if (value instanceof MPseudostate) {
            MPseudostate ps = (MPseudostate) value;
            MPseudostateKind kind = ps.getKind();
            if (MPseudostateKind.INITIAL.equals(kind))
                icon = _InitialStateIcon;
            if (MPseudostateKind.DEEP_HISTORY.equals(kind))
                icon = _DeepIcon;
            if (MPseudostateKind.SHALLOW_HISTORY.equals(kind))
                icon = _ShallowIcon;
            if (MPseudostateKind.FORK.equals(kind))
                icon = _ForkIcon;
            if (MPseudostateKind.JOIN.equals(kind))
                icon = _JoinIcon;
            if (MPseudostateKind.BRANCH.equals(kind))
                icon = _BranchIcon;
            //if (MPseudostateKind.FINAL.equals(kind)) icon = _FinalStateIcon;
        }
        if (value instanceof MAbstraction) {
            icon = _RealizeIcon;
        }
        // needs more work: sending and receiving icons
        if (value instanceof MSignal) {
            icon = _SignalIcon;
        }

        if (value instanceof MComment) {
            icon = _CommentIcon;
        }

        if (icon == null) {
            String clsPackName = value.getClass().getName();
            if (clsPackName.startsWith("org") || clsPackName.startsWith("ru")) {
                String cName = clsPackName.substring(clsPackName.lastIndexOf(".") + 1);
                // special case "UML*" e.g. UMLClassDiagram
                if (cName.startsWith("UML"))
                    cName = cName.substring(3);
                if (cName.startsWith("M"))
                    cName = cName.substring(1);
                if (cName.endsWith("Impl"))
                    cName = cName.substring(0, cName.length() - 4);
                icon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource(cName);
                if (icon != null)
                    _iconCache.put(value.getClass(), icon);
            }
        }
        return icon;
    }
}
