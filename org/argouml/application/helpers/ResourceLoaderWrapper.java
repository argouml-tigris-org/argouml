// $Id$
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

import org.argouml.model.ModelFacade;
import org.argouml.model.uml.UmlHelper;
import org.tigris.gef.util.ResourceLoader;

/**
 * Wrapper around org.tigris.gef.util.ResourceLoader. 
 * 
 * Necessary since ArgoUML needs some extra init
 * @since Nov 24, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public final class ResourceLoaderWrapper {
    static {
        initResourceLoader();
    }

    protected static ImageIcon _ActionStateIcon =
	 ResourceLoader.lookupIconResource("ActionState");
    protected static ImageIcon _StateIcon =
	 ResourceLoader.lookupIconResource("State");
    protected static ImageIcon _InitialStateIcon =
	 ResourceLoader.lookupIconResource("Initial");
    protected static ImageIcon _DeepIcon =
	 ResourceLoader.lookupIconResource("DeepHistory");
    protected static ImageIcon _ShallowIcon =
	 ResourceLoader.lookupIconResource("ShallowHistory");
    protected static ImageIcon _ForkIcon =
	 ResourceLoader.lookupIconResource("Fork");
    protected static ImageIcon _JoinIcon =
	 ResourceLoader.lookupIconResource("Join");
    protected static ImageIcon _BranchIcon =
	 ResourceLoader.lookupIconResource("Branch");
    protected static ImageIcon _FinalStateIcon =
	 ResourceLoader.lookupIconResource("FinalState");
    protected static ImageIcon _RealizeIcon =
	 ResourceLoader.lookupIconResource("Realization");
    protected static ImageIcon _SignalIcon =
	 ResourceLoader.lookupIconResource("SignalSending");
    protected static ImageIcon _CommentIcon =
	 ResourceLoader.lookupIconResource("Note");

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
     * Calculate the path to a look and feel object.
     *
     * @param classname The look and feel classname
     * @param element The en part of the path.
     * @returns the complete path.
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
     * If there are none, this method is called to initialize
     * the resource loader. Originally, this method was placed within Main but
     * this coupled Main and the resourceLoader to much.
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
    public ImageIcon lookupIconResource(String resource, String desc, 
					ClassLoader loader) 
    {
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
        Icon icon = null;
        if (value != null) {
        
	    icon = (Icon) _iconCache.get(value.getClass());

            if (ModelFacade.isAPseudostate(value) ) {
                
		Object kind = ModelFacade.getKind(value);
                if (UmlHelper.getHelper().getDataTypes()
                    .equalsINITIALKind(kind))
		    icon = _InitialStateIcon;
		if (UmlHelper.getHelper().getDataTypes()
                    .equalsDEEP_HISTORYKind(kind))
		    icon = _DeepIcon;
		if (UmlHelper.getHelper().getDataTypes()
                    .equalsSHALLOW_HISTORYKind(kind))
		    icon = _ShallowIcon;
		if (UmlHelper.getHelper().getDataTypes()
                    .equalsFORKKind(kind))
		    icon = _ForkIcon;
		if (UmlHelper.getHelper().getDataTypes()
                    .equalsJOINKind(kind))
		    icon = _JoinIcon;
		if (UmlHelper.getHelper().getDataTypes()
                    .equalsBRANCHKind(kind))
		    icon = _BranchIcon;
		// if (MPseudostateKind.FINAL.equals(kind)) 
		// icon = _FinalStateIcon;
	    }
	    if (ModelFacade.isAAbstraction(value)) {
		icon = _RealizeIcon;
	    }
	    // needs more work: sending and receiving icons
	    if (ModelFacade.isASignal(value)) {
		icon = _SignalIcon;
	    }

	    if (ModelFacade.isAComment(value)) {
		icon = _CommentIcon;
	    }

	    if (icon == null) {
		String clsPackName = value.getClass().getName();
		if (clsPackName.startsWith("org")
		    || clsPackName.startsWith("ru")) 
		{
		    String cName =
			clsPackName.substring(clsPackName.lastIndexOf(".")
					      + 1);
		    // special case "UML*" e.g. UMLClassDiagram
		    if (cName.startsWith("UML"))
			cName = cName.substring(3);
		    if (cName.startsWith("M"))
			cName = cName.substring(1);
		    if (cName.endsWith("Impl"))
			cName = cName.substring(0, cName.length() - 4);
		    icon = ResourceLoaderWrapper.getResourceLoaderWrapper()
			.lookupIconResource(cName);
		    if (icon != null)
			_iconCache.put(value.getClass(), icon);
		}
	    }
        }
        return icon;
        
    }
}
