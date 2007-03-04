// $Id$
// Copyright (c) 2003-2007 The Regents of the University of California. All
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

package org.argouml.uml.reveng.java;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.argouml.application.api.Argo;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.CoreFactory;
import org.argouml.model.Facade;
import org.argouml.model.Model;
import org.argouml.ocl.OCLUtil;
import org.argouml.uml.reveng.DiagramInterface;
import org.argouml.uml.reveng.ImportCommon;
import org.argouml.uml.reveng.ImportInterface;
import org.argouml.uml.reveng.ImportSettings;

/**
 * Modeller maps Java source code(parsed/recognised by ANTLR) to UML model
 * elements, it applies some of the semantics in JSR26.
 *
 * @author Marcus Andersson
 */
public class Modeller {
    /**
     * Logger.<p>
     */
    private static final Logger LOG = Logger.getLogger(Modeller.class);

    /**
     * Current working model.
     */
    private Object model;

    // TODO: This can be removed when we are sure there are no external users
    // of the functionality.  Diagram creation has moved to ImportCommon where
    // a single implementation is shared by all importers. - tfm 20061209
    private DiagramInterface diagram;

    /**
     * Current import settings.
     */
    private ImportCommon importSession;

    /**
     * The package which the currentClassifier belongs to.
     */
    private Object currentPackage;

    /**
     * Last package name used in addPackage().
     * It is null for classes wich are not packaged.
     * Used in popClassifier() to create diagram for that
     * packaget.
     */
    private String currentPackageName;

    /**
     * Keeps the data that varies during parsing.
     */
    private ParseState parseState;

    /**
     * Stack up the state when descending inner classes.
     */
    private Stack parseStateStack;

    /**
     * Only attributes will be generated.
     */
    private boolean noAssociations;

    /**
     * Arrays will be modelled as unique datatypes.
     */
    private boolean arraysAsDatatype;

    /**
     * The name of the file being parsed.
     */
    private String fileName;

    /**
     * Arbitrary attributes.
     */
    private Hashtable attributes = new Hashtable();

    /**
     * Vector of parsed method calls.
     */
    private Vector methodCalls = new Vector();

    /**
     * HashMap of parsed local variables.
     */
    private Hashtable localVariables = new Hashtable();

    /**
     * New model elements that were created during this
     * reverse engineering session.
     */
    private Collection newElements;
    
    /**
     * Create a new modeller which will do diagram creation.
     * 
     * @param m
     *            The model to work with.
     * @param diag
     *            the interface to the diagram to add nodes and edges to
     * @param imp
     *            The current Import session.
     * @param noAss
     *            whether associations are modelled as attributes
     * @param arraysAsDT
     *            whether darrays are modelled as dataypes
     * @param fName
     *            the current file name
     * @deprecated for 0.23.4 by tfmorris - use variant without diagram
     *             interface
     *      {@link #Modeller(Object, ImportCommon, boolean, boolean, String)}.
     *             NOTE: This really is private to the Java RE module, but it
     *             has also been used by other bundled importers, so it's
     *             possible it was used by others outside the project as well.
     */
    public Modeller(Object m,
		    DiagramInterface diag,
		    ImportCommon imp,
		    boolean noAss,
		    boolean arraysAsDT,
		    String fName) {
	model = m;
	noAssociations = noAss;
	arraysAsDatatype = arraysAsDT;
	importSession = imp;
	currentPackage = this.model;
	parseState = new ParseState(this.model, getPackage("java.lang"));
	parseStateStack = new Stack();
	diagram = diag;
        fileName = fName;
        newElements = new ArrayList();
    }

    /**
     * Create a new modeller.
     *
     * @param theModel The model to work with.
     * @param settings the settings to use for this import
     * @param theFileName the current file name
     */
    public Modeller(Object theModel, ImportSettings settings, 
            String theFileName) {
        this(theModel, null, null, settings.isAttributeSelected(), settings
                .isDatatypeSelected(), theFileName);
    }
    
    /**
     * @param key the key of the attribute to get
     * @return the value of the attribute
     */
    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    /**
     * @param key the key of the attribute
     * @param value the value for the attribute
     */
    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    /**
     * This is a mapping from a Java compilation Unit -> a UML component.
     * Classes are resident in a component.
     * Imports are relationships between components and other classes
     * / packages.<p>
     *
     * See JSR 26.<p>
     *
     * Adding components is a little messy since there are 2 cases:
     *
     * <ol>
     * <li>source file has package statement, will be added several times
     *     since lookup in addComponent() only looks in the model since the
     *     package namespace is not yet known.
     *
     * <li>source file has not package statement: component is added
     *     to the model namespace. the is no package statement so the
     *     lookup will always work.
     *
     * </ol>
     * Therefore in the case of (1), we need to delete duplicate components
     * in the addPackage() method.<p>
     *
     * In either case we need to add a package since we don't know in advance
     * if there will be a package statement.<p>
     */
    public void addComponent() {

        // try and find the component in the current package
        // to cope with repeated imports
        // [this will never work if a package statmeent exists:
        // because the package statement is parsed after the component is
        // identified]
        Object component = Model.getFacade().lookupIn(currentPackage, fileName);

        if (component == null) {

            // remove the java specific ending (per JSR 26).
            // BUT we can't do this because then the component will be confused
            // with its class with the same name when invoking
            // Model.getFacade().lookupIn(Object,String)
            /*
	      if(fileName.endsWith(".java"))
	      fileName = fileName.substring(0, fileName.length()-5);
            */

            component = Model.getCoreFactory().createComponent();
            Model.getCoreHelper().setName(component, fileName);
            newElements.add(component);
        }

        parseState.addComponent(component);

        // set the namespace of the component, in the event
        // that the source file does not have a package stmt
        Model.getCoreHelper().setNamespace(parseState.getComponent(), model);
    }

    /**
     * Called from the parser when a package clause is found.
     *
     * @param name The name of the package.
     */
    public void addPackage(String name) {
	// Add a package figure for this package to the owner's class
	// diagram, if it's not in the diagram yet. I do this for all
	// the class diagrams up to the top level, since I need
	// diagrams for all the packages.
	String ownerPackageName, currentName = name;
        ownerPackageName = getPackageName(currentName);
	while (!"".equals(ownerPackageName)) {
	    if (diagram != null
	            && importSession != null
	            && importSession.isCreateDiagramsSelected()
	            && diagram.isDiagramInProject(ownerPackageName)) {

	        diagram.selectClassDiagram(getPackage(ownerPackageName),
	                ownerPackageName);
	        diagram.addPackage(getPackage(currentName));

	    }
	    currentName = ownerPackageName;
            ownerPackageName = getPackageName(currentName);
	}
	// Save src_path in the upper package
        // TODO: Rework this so that we don't need importSession here.
        // perhaps move to the common import code. - tfm
	Object mPackage = getPackage(currentName);
	if (importSession != null && importSession.getSrcPath() != null
	    && Model.getFacade().getTaggedValue(mPackage,
                        ImportInterface.SOURCE_PATH_TAG) == null) {
            Model.getCoreHelper()
                    .setTaggedValue(mPackage, ImportInterface.SOURCE_PATH_TAG,
                            importSession.getSrcPath());
	}

	// Find or create a Package model element for this package.
	mPackage = getPackage(name);
        currentPackageName = name;

	// Set the current package for the following source code.
	currentPackage = mPackage;
	parseState.addPackageContext(mPackage);

        // Delay diagram creation until any classifier (class or
        // interface) will be found

        //set the namespace of the component
        // check to see if there is already a component defined:
        Object component = Model.getFacade().lookupIn(currentPackage, fileName);

        if (component == null) {

            // set the namespace of the component
            Model.getCoreHelper().setNamespace(
                    parseState.getComponent(),
                    currentPackage);
        } else {

            // a component already exists,
            // so delete the latest one(the duplicate)
            Object oldComponent = parseState.getComponent();
            Model.getUmlFactory().delete(oldComponent);
            newElements.remove(oldComponent);
        // change the parse state to the existing one.
            parseState.addComponent(component);
        }
    }

    /**
     * Called from the parser when an import clause is found.
     *
     * @param name The name of the import. Can end with a '*'.
     */
    public void addImport(String name) {
        addImport(name, false);
    }
    
    /**
     * Called from the parser when an import clause is found.
     *
     * @param name The name of the import. Can end with a '*'.
     * @param forceIt Force addition by creating all that's missing.
     */
    void addImport(String name, boolean forceIt) {
        // only do imports on the 2nd pass.
        Object level = this.getAttribute("level");
        if (level != null) {
            if (level.equals(new Integer(0))) {
                return;
            }
        }

	String packageName = getPackageName(name);
	String classifierName = getClassifierName(name);
	Object mPackage = getPackage(packageName);

        // import on demand
	if (classifierName.equals("*")) {
	    parseState.addPackageContext(mPackage);
            Object perm = null;

            // try find an existing permission
            Iterator dependenciesIt =
                Model.getCoreHelper()
		    .getDependencies(mPackage, parseState.getComponent())
		        .iterator();
            while (dependenciesIt.hasNext()) {

                Object dependency = dependenciesIt.next();
                if (Model.getFacade().isAPermission(dependency)) {

                    perm = dependency;
                    break;
                }
            }

            // if no existing permission was found.
            if (perm == null) {
		perm =
		    Model.getCoreFactory()
		        .buildPermission(parseState.getComponent(), mPackage);
		String newName =
		    Model.getFacade().getName(parseState.getComponent())
		    + " -> "
		    + packageName;
		Model.getCoreHelper().setName(perm, newName);
            }
	}
        // single type import
	else {
            Object mClassifier = null;
	    try {
		mClassifier =
		    (new PackageContext(null, mPackage)).get(classifierName);
            } catch (ClassifierNotFoundException e) {
                if (forceIt && classifierName != null && mPackage != null) {
                    // we must guess if it's a class or an interface, so: class
                    LOG.info("Modeller.java: " 
                            + "forced creation of unknown classifier "
                            + classifierName);
                    mClassifier = Model.getCoreFactory().buildClass(
                            classifierName, mPackage);
                } else {
                    warnClassifierNotFound(classifierName, e,
                            "an imported classifier");
                }
            }
            if (mClassifier != null) {
		parseState.addClassifierContext(mClassifier);

                // try find an existing permission
                Iterator dependenciesIt =
		    Model.getCoreHelper()
                        .getDependencies(mClassifier,
					 parseState.getComponent())
                            .iterator();
                Object perm = null;
                while (dependenciesIt.hasNext()) {

                    Object dependency = dependenciesIt.next();
                    if (Model.getFacade().isAPermission(dependency)) {

                        perm = dependency;
                        break;
                    }
                }

                // if no existing permission was found.
                if (perm == null) {
                    perm =
			Model.getCoreFactory()
			    .buildPermission(parseState.getComponent(),
					     mClassifier);
		    String newName =
			Model.getFacade().getName(parseState.getComponent())
			+ " -> "
			+ Model.getFacade().getName(mClassifier);
                    Model.getCoreHelper().setName(perm, newName);
                }
	    }


	}
    }

    /**
     * Called from the parser when a class declaration is found.
     *
     * @param name The name of the class.
     * @param modifiers A sequence of class modifiers.
     * @param superclassName Zero or one string with the name of the
     *        superclass. Can be fully qualified or
     *        just a simple class name.
     * @param interfaces Zero or more strings with the names of implemented
     *        interfaces. Can be fully qualified or just a
     *        simple interface name.
     * @param javadoc The javadoc comment. null or "" if no comment available.
     */
    public void addClass(String name,
                         short modifiers,
                         String superclassName,
                         Vector interfaces,
                         String javadoc) {
        addClass(name, modifiers, superclassName, interfaces, javadoc);
    }

    /**
     * Called from the parser when a class declaration is found.
     *
     * @param name The name of the class.
     * @param modifiers A sequence of class modifiers.
     * @param superclassName Zero or one string with the name of the
     *        superclass. Can be fully qualified or
     *        just a simple class name.
     * @param interfaces Zero or more strings with the names of implemented
     *        interfaces. Can be fully qualified or just a
     *        simple interface name.
     * @param javadoc The javadoc comment. null or "" if no comment available.
     * @param forceIt Force addition by creating all that's missing.
     */
    void addClass(String name,
                         short modifiers,
                         String superclassName,
                         Vector interfaces,
                         String javadoc,
                         boolean forceIt) {
        Object mClass =
	    addClassifier(Model.getCoreFactory().createClass(),
			  name, modifiers, javadoc);

        Model.getCoreHelper().setAbstract(
                mClass,
                (modifiers & JavaRecognizer.ACC_ABSTRACT) > 0);
        Model.getCoreHelper().setLeaf(
                mClass,
                (modifiers & JavaRecognizer.ACC_FINAL) > 0);
        Model.getCoreHelper().setRoot(mClass, false);

        // only do generalizations and realizations on the 2nd pass.
        Object level = this.getAttribute("level");
        if (level != null) {
            if (level.equals(new Integer(0))) {
                return;
            }
        }

	if (superclassName != null) {
            Object parentClass = null;
	    try {
		parentClass =
		    getContext(superclassName)
		        .get(getClassifierName(superclassName));
		getGeneralization(currentPackage, parentClass, mClass);
	    } catch (ClassifierNotFoundException e) {
	        if (forceIt && superclassName != null && model != null) {
	            LOG.info("Modeller.java: forced creation of unknown class "
	                    + superclassName);
	            String packageName = getPackageName(superclassName);
	            String classifierName = getClassifierName(superclassName);
	            Object mPackage = (packageName.length() > 0) 
                            ? getPackage(packageName)
	                    : model;
	            parentClass = Model.getCoreFactory().buildClass(
	                    classifierName, mPackage);
	            getGeneralization(currentPackage, parentClass, mClass);
	        } else {
	            warnClassifierNotFound(superclassName, e,
	                    "a generalization");
	        }
	    }
	}

	if (interfaces != null) {
	    for (Iterator i = interfaces.iterator(); i.hasNext();) {
		String interfaceName = (String) i.next();
                Object mInterface = null;
		try {
                    mInterface =
			getContext(interfaceName)
			    .getInterface(getClassifierName(interfaceName));
                } catch (ClassifierNotFoundException e) {
                    if (forceIt && interfaceName != null && model != null) {
                        LOG.info("Modeller.java: " 
                                + "forced creation of unknown interface "
                                + interfaceName);
                        String packageName = getPackageName(interfaceName);
                        String classifierName =
                                getClassifierName(interfaceName);
                        Object mPackage = 
                            (packageName.length() > 0) 
                                        ? getPackage(packageName)
                                        : model;
                        mInterface =
                                Model.getCoreFactory().buildInterface(
                                        classifierName, mPackage);
                    } else {
                        warnClassifierNotFound(interfaceName, e,
                                "an abstraction");
                    }
                }
                // TODO: This should use the Model API's buildAbstraction - tfm
                if (mInterface != null) {
		    Object mAbstraction =
			getAbstraction(mInterface, mClass);
		    if (Model.getFacade().getSuppliers(mAbstraction).size()
		            == 0) {
			Model.getCoreHelper().addSupplier(
			        mAbstraction,
			        mInterface);
			Model.getCoreHelper().addClient(mAbstraction, mClass);
		    }
		    Model.getCoreHelper().setNamespace(
		            mAbstraction,
		            currentPackage);
		    Model.getCoreHelper().addStereotype(
		            mAbstraction,
		            getStereotype(CoreFactory.REALIZE_STEREOTYPE));
		}
	    }
	}
    }

    /**
     * Called from the parser when an anonymous inner class is found.
     *
     * @param type The type of this anonymous class.
     */
    public void addAnonymousClass(String type) {
        addAnonymousClass(type, false);
    }
    
    /**
     * Called from the parser when an anonymous inner class is found.
     *
     * @param type The type of this anonymous class.
     * @param forceIt Force addition by creating all that's missing.
     */
    void addAnonymousClass(String type, boolean forceIt) {
        String name = parseState.anonymousClass();
        try {
            Object mClassifier = getContext(type).get(getClassifierName(type));
            Vector interfaces = new Vector();
            if (Model.getFacade().isAInterface(mClassifier)) {
                interfaces.add(type);
            }
            addClass(name,
		     (short) 0,
		     Model.getFacade().isAClass(mClassifier) ? type : null,
		     interfaces,
		     "",
                     forceIt);
        } catch (ClassifierNotFoundException e) {
            // Must add it anyway, or the class popping will mismatch.
            addClass(name, (short) 0, null, new Vector(), "", forceIt);
            LOG.info("Modeller.java: an anonymous class was created "
		     + "although it could not be found in the classpath.");
        }
    }

    /**
     * Called from the parser when an interface declaration is found.
     *
     * @param name The name of the interface.
     * @param modifiers A sequence of interface modifiers.
     * @param interfaces Zero or more strings with the names of extended
     * interfaces. Can be fully qualified or just a simple interface name.
     * @param javadoc The javadoc comment. "" if no comment available.
    */
    public void addInterface(String name,
                             short modifiers,
                             Vector interfaces,
                             String javadoc) {
        addInterface(name, modifiers, interfaces, javadoc);
    }
    
    /**
     * Called from the parser when an interface declaration is found.
     *
     * @param name The name of the interface.
     * @param modifiers A sequence of interface modifiers.
     * @param interfaces Zero or more strings with the names of extended
     * interfaces. Can be fully qualified or just a simple interface name.
     * @param javadoc The javadoc comment. "" if no comment available.
     * @param forceIt Force addition by creating all that's missing.
     */
    void addInterface(String name,
                             short modifiers,
                             Vector interfaces,
                             String javadoc,
                             boolean forceIt) {
        Object mInterface =
	    addClassifier(Model.getCoreFactory().createInterface(),
			  name,
			  modifiers,
			  javadoc);

        // only do generalizations and realizations on the 2nd pass.
        Object level = this.getAttribute("level");
        if (level != null) {
            if (level.equals(new Integer(0))) {
                return;
            }
        }

        for (Iterator i = interfaces.iterator(); i.hasNext();) {
            String interfaceName = (String) i.next();
            Object parentInterface = null;
            try {
                parentInterface =
		    getContext(interfaceName)
		        .getInterface(getClassifierName(interfaceName));
                getGeneralization(currentPackage, parentInterface, mInterface);
            } catch (ClassifierNotFoundException e) {
                if (forceIt && interfaceName != null && model != null) {
                    LOG.info("Modeller.java: " 
                            + "forced creation of unknown interface "
                            + interfaceName);
                    String packageName = getPackageName(interfaceName);
                    String classifierName = getClassifierName(interfaceName);
                    Object mPackage = (packageName.length() > 0) 
                            ? getPackage(packageName)
                            : model;
                    parentInterface = Model.getCoreFactory().buildInterface(
                            classifierName, mPackage);
                    getGeneralization(currentPackage, parentInterface,
                            mInterface);
                } else {
                    warnClassifierNotFound(interfaceName, e, 
                            "a generalization");
                }
            }
        }
    }

    /**
       Common code used by addClass and addInterface.

       @param newClassifier Supply one if none is found in the model.
       @param name Name of the classifier.
       @param modifiers String of modifiers.
       @param javadoc The javadoc comment. null or "" if no comment available.
       @return The newly created/found classifier.
    */
    private Object addClassifier(Object newClassifier,
                                 String name,
                                 short modifiers,
                                 String javadoc) {
        Object mClassifier;
        Object mNamespace;

        if (parseState.getClassifier() != null) {
            // the new classifier is a java inner class
            mClassifier =
        	Model.getFacade().lookupIn(parseState.getClassifier(), name);
            mNamespace = parseState.getClassifier();
        } else {
            // the new classifier is a top level java class
            parseState.outerClassifier();
            mClassifier = Model.getFacade().lookupIn(currentPackage, name);
            mNamespace = currentPackage;
        }


        if (mClassifier == null) {
            // if the classifier could not be found in the model
            if (LOG.isInfoEnabled()) {
                LOG.info("Created new classifier for " + name);
            }
            mClassifier = newClassifier;
            Model.getCoreHelper().setName(mClassifier, name);
            Model.getCoreHelper().setNamespace(mClassifier, mNamespace);
            newElements.add(mClassifier);
        } else {
            // it was found and we delete any existing tagged values.
            if (LOG.isInfoEnabled()) {
                LOG.info("Found existing classifier for " + name);
            }
            // TODO: Rewrite existing elements instead? - tfm
            cleanModelElement(mClassifier);
        }

        parseState.innerClassifier(mClassifier);

        // set up the component residency (only for top level classes)
        if (parseState.getClassifier() == null) {
            // set the clasifier to be a resident in its component:
            // (before we push a new parse state on the stack)

            // This test is carried over from a previous implementation,
            // but I'm not sure why it would already be set - tfm
            if (Model.getFacade().getElementResidences(mClassifier).isEmpty()) {
                Object resident = Model.getCoreFactory()
                        .createElementResidence();
                Model.getCoreHelper().setResident(resident, mClassifier);
                Model.getCoreHelper().setContainer(resident,
                        parseState.getComponent());
            }
        }

        // change the parse state to a classifier parse state
        parseStateStack.push(parseState);
        parseState = new ParseState(parseState, mClassifier, currentPackage);

        setVisibility(mClassifier, modifiers);
        
        // Add classifier documentation tags during first (or only) pass only
        Object level = this.getAttribute("level");
        if (level != null) {
            if (((Integer) level).intValue() <= 1) {
                addDocumentationTag(mClassifier, javadoc);
            }
        }

        return mClassifier;
    }

    /**
       Called from the parser when a classifier is completely parsed.
    */
    public void popClassifier() {
        // now create diagram if it doesn't exists in project
	if (diagram != null 
                && importSession != null 
                && importSession.isCreateDiagramsSelected()) {

	    if (currentPackageName != null) {
	        diagram.selectClassDiagram(currentPackage,
	                currentPackageName);
	    }
	    // The class is in a source file
	    // with no package declaration
	    else {
	        // create new diagram in root for classifier without package
	        diagram.createRootClassDiagram();
	    }
	    // add the current classifier to the diagram.
	    Object classifier = parseState.getClassifier();
	    if (Model.getFacade().isAInterface(classifier)) {
	        diagram.addInterface(classifier,
	                importSession.isMinimizeFigsSelected());
	    } else if (Model.getFacade().isAClass(classifier)) {
	        diagram.addClass(classifier,
	                importSession.isMinimizeFigsSelected());
	    }
	}

        // Remove operations and attributes not in source
        parseState.removeObsoleteFeatures();

        // Remove inner classes not in source
        parseState.removeObsoleteInnerClasses();

        parseState = (ParseState) parseStateStack.pop();
    }

    /**
     * Called from the parser when an operation is
     * found.
     *
     * @param modifiers A sequence of operation modifiers.
     * @param returnType The return type of the operation.
     * @param name The name of the operation as a string
     * @param parameters A number of vectors, each representing a
     *
     * parameter.
     * @param javadoc The javadoc comment. null or "" if no comment available.
     * @return The operation.
     */
    public Object addOperation (short modifiers,
                                String returnType,
                                String name,
                                Vector parameters,
                                String javadoc) {
        return addOperation(modifiers, returnType, name, parameters, javadoc);
    }
    
    /**
     * Called from the parser when an operation is
     * found.
     *
     * @param modifiers A sequence of operation modifiers.
     * @param returnType The return type of the operation.
     * @param name The name of the operation as a string
     * @param parameters A number of vectors, each representing a
     *
     * parameter.
     * @param javadoc The javadoc comment. null or "" if no comment available.
     * @param forceIt Force addition by creating all that's missing.
     * @return The operation.
     */
    Object addOperation (short modifiers,
                                String returnType,
                                String name,
                                Vector parameters,
                                String javadoc,
                                boolean forceIt) {
	Object mOperation = getOperation(name);
	parseState.feature(mOperation);

	Model.getCoreHelper().setAbstract(mOperation,
				(modifiers & JavaRecognizer.ACC_ABSTRACT) > 0);
	Model.getCoreHelper().setLeaf(mOperation,
			    (modifiers & JavaRecognizer.ACC_FINAL) > 0);
	Model.getCoreHelper().setRoot(mOperation, false);
	setOwnerScope(mOperation, modifiers);
	setVisibility(mOperation, modifiers);
	if ((modifiers & JavaRecognizer.ACC_SYNCHRONIZED) > 0) {
	    Model.getCoreHelper().setConcurrency(mOperation,
	            Model.getConcurrencyKind().getGuarded());
	} else if (Model.getFacade().getConcurrency(mOperation)
		   == Model.getConcurrencyKind().getGuarded()) {
	    Model.getCoreHelper().setConcurrency(mOperation,
	            Model.getConcurrencyKind().getSequential());
	}

        Collection c = new ArrayList(Model.getFacade()
                .getParameters(mOperation));
	for (Iterator i = c.iterator(); i.hasNext();) {
	    Model.getCoreHelper().removeParameter(mOperation, i.next());
	}

	Object mParameter;
	String typeName;
	Object mClassifier = null;

	if (returnType == null
            || ("void".equals(returnType)
                && name.equals(Model.getFacade().getName(parseState
                        .getClassifier())))) {
	    // Constructor
	    Model.getCoreHelper().addStereotype(mOperation,
                getStereotype(mOperation, "create", "BehavioralFeature"));
	} else {
	    try {
		mClassifier =
		    getContext(returnType).get(getClassifierName(returnType));
            } catch (ClassifierNotFoundException e) {
                if (forceIt && returnType != null && model != null) {
                    LOG.info("Modeller.java: " 
                            + "forced creation of unknown classifier "
                            + returnType);
                    String packageName = getPackageName(returnType);
                    String classifierName = getClassifierName(returnType);
                    Object mPackage =
                            (packageName.length() > 0) ? getPackage(packageName)
                                    : model;
                    mClassifier = Model.getCoreFactory().buildClass(
                            classifierName, mPackage);
                } else {
                    warnClassifierNotFound(returnType, e,
                            "operation return type");
                }
            }
            if (mClassifier != null) {
		Object mdl = ProjectManager.getManager()
		    .getCurrentProject().getModel();
		Object voidType = ProjectManager.getManager()
		    .getCurrentProject().findType("void");
		mParameter = Model.getCoreFactory().buildParameter(
		        mOperation, mdl, voidType);
                Model.getCoreHelper().setName(mParameter, "return");
                Model.getCoreHelper().setKind(
                        mParameter,
                        Model.getDirectionKind().getReturnParameter());

                Model.getCoreHelper().setType(mParameter, mClassifier);
	    }
	}

	for (Iterator i = parameters.iterator(); i.hasNext();) {
	    Vector parameter = (Vector) i.next();
	    typeName = (String) parameter.elementAt(1);
            mClassifier = null;
	    try {
                mClassifier =
		    getContext(typeName).get(getClassifierName(typeName));
            } catch (ClassifierNotFoundException e) {
                if (forceIt && typeName != null && model != null) {
                    LOG.info("Modeller.java: " 
                            + "forced creation of unknown classifier "
                            + typeName);
                    String packageName = getPackageName(typeName);
                    String classifierName = getClassifierName(typeName);
                    Object mPackage =
                            (packageName.length() > 0) ? getPackage(packageName)
                                    : model;
                    mClassifier = Model.getCoreFactory().buildClass(
                            classifierName, mPackage);
                } else {
                    warnClassifierNotFound(typeName, e,
                            "operation params");
                }
            }
            if (mClassifier != null) {
                Object mdl = ProjectManager.getManager()
                    .getCurrentProject().getModel();
                Object voidType = ProjectManager.getManager()
                    .getCurrentProject().findType("void");
                mParameter = Model.getCoreFactory().buildParameter(
                        mOperation, mdl, voidType);
		Model.getCoreHelper().setName(mParameter,
				    (String) parameter.elementAt(2));
		Model.getCoreHelper().setKind(mParameter,
                        Model.getDirectionKind().getInParameter());
                if (Model.getFacade().isAClassifier(mClassifier)) {
                    Model.getCoreHelper().setType(mParameter, mClassifier);
                } else {
                    // the type resolution failed to find a valid classifier.
                    LOG.warn("Modeller.java: a valid type for a parameter "
			     + "could not be resolved:\n "
			     + "In file: " + fileName + ", for operation: "
			     + Model.getFacade().getName(mOperation)
			     + ", for parameter: "
			     + Model.getFacade().getName(mParameter));
                }
	    }
	}

	addDocumentationTag (mOperation, javadoc);

	return mOperation;
    }


    /**
     * Warn user that information available in input source will not be
     * reflected accurately in the model.
     * 
     * @param name
     *            name of the classifiere which wasn't found
     * @param e
     *            the exception -
     * @param operation -
     *            a string indicating what type of operation was being attempted
     */
    private void warnClassifierNotFound(String name, Throwable e,
            String operation) {
        // TODO: The user will likely never see this error.  It
        // needs to be more visible. - tfm
        LOG.info("Modeller.java: a classifier (" + name
                + ") that was in the source "
                + "file could not be generated in the model " 
                + "(while generating " + operation + ").");
    }

    /**
     * Called from the parser to add a method body to an operation. (An
     * operation will have exactly one Java body.)
     * 
     * @param op
     *            An operation.
     * @param body
     *            A method body.
     */
    public void addBodyToOperation(Object op, String body) {
        if (op == null || !Model.getFacade().isAOperation(op)) {
//            LOG.warn("adding body failed: no operation!");
            return;
        }
        if (body == null || body.length() == 0) {
            return;
        }

        Object method = getMethod(Model.getFacade().getName(op));
        parseState.feature(method);
        Model.getCoreHelper().setBody(method,
			    Model.getDataTypesFactory()
			    	.createProcedureExpression("Java",
			    	        		   body));
	// Add the method to it's specification.
        Model.getCoreHelper().addMethod(op, method);

        // Add this method as a feature to the classifier that owns
        // the operation.
        Model.getCoreHelper().addFeature(Model.getFacade().getOwner(op),
                method);
    }

    /**
     * Called from the parser when an attribute is found.
     *
     * @param modifiers A sequence of attribute modifiers.
     * @param typeSpec The attribute's type.
     * @param name The name of the attribute.
     * @param initializer The initial value of the attribute.
     * @param javadoc The javadoc comment. null or "" if no comment available.
     */
    public void addAttribute (short modifiers,
                              String typeSpec,
                              String name,
                              String initializer,
                              String javadoc) {
        addAttribute(modifiers, typeSpec, name, initializer, javadoc);
    }
    
    /**
     * Called from the parser when an attribute is found.
     *
     * @param modifiers A sequence of attribute modifiers.
     * @param typeSpec The attribute's type.
     * @param name The name of the attribute.
     * @param initializer The initial value of the attribute.
     * @param javadoc The javadoc comment. null or "" if no comment available.
     * @param forceIt Force addition by creating all that's missing.
     */
    void addAttribute (short modifiers,
                              String typeSpec,
                              String name,
                              String initializer,
                              String javadoc,
                              boolean forceIt) {
	String multiplicity = null;

	if (!arraysAsDatatype && typeSpec.indexOf('[') != -1) {
	    typeSpec = typeSpec.substring(0, typeSpec.indexOf('['));
	    multiplicity = "1_N";
	} else {
	    multiplicity = "1_1";
	}

	// the attribute type
	Object mClassifier = null;
	try {
	    // get the attribute type
	    mClassifier = getContext(typeSpec).get(getClassifierName(typeSpec));
	} catch (ClassifierNotFoundException e) {
            if (forceIt && typeSpec != null && model != null) {
                LOG.info("Modeller.java: forced creation of unknown classifier "
                    + typeSpec);
                String packageName = getPackageName(typeSpec);
                String classifierName = getClassifierName(typeSpec);
                Object mPackage = (packageName.length() > 0) 
                        ? getPackage(packageName)
                        : model;
                mClassifier =
                    Model.getCoreFactory().buildClass(classifierName, mPackage);
            } else {
                warnClassifierNotFound(typeSpec, e,
                    "an attribute");
            }
        }
        if (mClassifier == null) {
	    return;
	}

	// if we want to create a UML attribute:
	if (noAssociations
	    || Model.getFacade().isADataType(mClassifier)
	    || (Model.getFacade().getNamespace(mClassifier)
		== getPackage("java.lang"))) {

            Object mAttribute = parseState.getAttribute(name);
            if (mAttribute == null) {
                mAttribute = buildAttribute(parseState.getClassifier(), name);
            }
            parseState.feature(mAttribute);

            setOwnerScope(mAttribute, modifiers);
            setVisibility(mAttribute, modifiers);
            Model.getCoreHelper().setMultiplicity(mAttribute, multiplicity);

            if (Model.getFacade().isAClassifier(mClassifier)) {
                Model.getCoreHelper().setType(mAttribute, mClassifier);
            } else {
                // the type resolution failed to find a valid classifier.
                LOG.warn("Modeller.java: a valid type for a parameter "
			 + "could not be resolved:\n "
			 + "In file: " + fileName + ", for attribute: "
			 + Model.getFacade().getName(mAttribute));
            }

            // Set the initial value for the attribute.
            if (initializer != null) {

                // we must remove line endings and tabs from the intializer
                // strings, otherwise the classes will display horribly.
                initializer = initializer.replace('\n', ' ');
                initializer = initializer.replace('\t', ' ');

		Object newInitialValue =
		    Model.getDataTypesFactory()
		        .createExpression("Java",
					  initializer);
                Model.getCoreHelper().setInitialValue(
                        mAttribute,
                        newInitialValue);
            }

            if ((modifiers & JavaRecognizer.ACC_FINAL) > 0) {
                Model.getCoreHelper().setChangeable(mAttribute, false);
            } else if (!Model.getFacade().isChangeable(mAttribute)) {
                Model.getCoreHelper().setChangeable(mAttribute, true);
            }
            addDocumentationTag(mAttribute, javadoc);
        }
        // we want to create a UML association from the java attribute
        else {

            Object mAssociationEnd = getAssociationEnd(name, mClassifier);
            setTargetScope(mAssociationEnd, modifiers);
            setVisibility(mAssociationEnd, modifiers);
            Model.getCoreHelper().setMultiplicity(
                    mAssociationEnd,
                    multiplicity);
            Model.getCoreHelper().setType(mAssociationEnd, mClassifier);
            Model.getCoreHelper().setName(mAssociationEnd, name);
            if ((modifiers & JavaRecognizer.ACC_FINAL) > 0) {
                Model.getCoreHelper().setChangeable(mAssociationEnd, false);
            }
            Model.getCoreHelper().setNavigable(mAssociationEnd, true);
            addDocumentationTag(mAssociationEnd, javadoc);
	}
    }

    /**
       Find a generalization in the model. If it does not exist, a
       new generalization is created.

       @param mPackage Look in this package.
       @param parent The superclass.
       @param child The subclass.
       @return The generalization found or created.
    */
    private Object getGeneralization(Object mPackage,
                                     Object parent,
                                     Object child) {
        String name = Model.getFacade().getName(child) + " -> "
            + Model.getFacade().getName(parent);
        Object mGeneralization = null;
        mGeneralization = Model.getFacade().getGeneralization(child, parent);
        if (mGeneralization == null) {
            mGeneralization =
		Model.getCoreFactory().buildGeneralization(child, parent,
							     name);
        }
        if (mGeneralization != null) {
            Model.getCoreHelper().setNamespace(mGeneralization, mPackage);
        }
        return mGeneralization;
    }

    /**
     * Find an abstraction<<realize>> in the model. If it does not
     * exist, a new abstraction is created.
     *
     * @param parent The superclass.
     * @param child The subclass.
     * @return The abstraction found or created.
     */
    private Object getAbstraction(Object parent,
                                  Object child) {
        String name = Model.getFacade().getName(child) + " -> "
            + Model.getFacade().getName(parent);
        Object mAbstraction = null;
        for (Iterator i =
                Model.getFacade().getClientDependencies(child).iterator();
	     i.hasNext();) {
            mAbstraction = i.next();
            Collection c = Model.getFacade().getSuppliers(mAbstraction);
            if (c == null || c.size() == 0) {
                Model.getCoreHelper()
                	.removeClientDependency(child, mAbstraction);
            } else {
                if (parent != c.toArray()[0]) {
                    mAbstraction = null;
                } else {
                    break;
                }
            }
        }

        if (mAbstraction == null) {
            mAbstraction = Model.getCoreFactory().buildAbstraction(
                   name,
                   parent,
                   child);
        }
        return mAbstraction;
    }

    /**
       Find a package in the model. If it does not exist, a new
       package is created.

       @param name The name of the package.
       @return The package found or created.
    */
    private Object getPackage(String name) {
	Object mPackage = searchPackageInModel(name);
	if (mPackage == null) {
	    mPackage =
		Model.getModelManagementFactory()
		    .buildPackage(getRelativePackageName(name), name);
	    Model.getCoreHelper().setNamespace(mPackage, model);

	    // Find the owner for this package.
	    if ("".equals(getPackageName(name))) {
		Model.getCoreHelper().addOwnedElement(model, mPackage);
	    } else {
		Model.getCoreHelper().addOwnedElement(
		        getPackage(getPackageName(name)),
		        mPackage);
	    }
	}
	return mPackage;
    }

    /**
     * Search recursivly for nested packages in the model. So if you
     * pass a package org.argouml.kernel , this method searches for a package
     * kernel, that is owned by a package argouml, which is owned by a
     * package org. This method is required to nest the parsed packages.
     *
     * @param name The fully qualified package name of the package we
     * are searching for.
     * @return The found package or null, if it is not in the model.
     */
    private Object searchPackageInModel(String name) {
	if ("".equals(getPackageName(name))) {
	    return Model.getFacade().lookupIn(model, name);
	}
        Object owner = searchPackageInModel(getPackageName(name));
        return owner == null
            ? null
            : Model.getFacade().lookupIn(owner, getRelativePackageName(name));
    }

    /**
       Find an operation in the currentClassifier. If the operation is
       not found, a new is created.

       @param name The name of the operation.
       @return The operation found or created.
    */
    private Object getOperation(String name) {
        Object mOperation = parseState.getOperation(name);
        if (mOperation != null) {
            LOG.info("Getting the existing operation " + name);
        } else {
            LOG.info("Creating a new operation " + name);
            Object cls = parseState.getClassifier();
            Object mdl = ProjectManager.getManager()
                .getCurrentProject().getModel();
            Object voidType = ProjectManager.getManager()
                .getCurrentProject().findType("void");
            mOperation =
        		Model.getCoreFactory().buildOperation(
                        cls, mdl, voidType, name);
//            Iterator it2 =
//		  ProjectManager.getManager().getCurrentProject()
//                .findFigsForMember(parseState.getClassifier()).iterator();
//            while (it2.hasNext()) {
//                Object listener = it2.next();
//                // UmlModelEventPump.getPump()
//                //     .removeModelEventListener(listener,
//                // mOperation);
//                UmlModelEventPump.getPump().addModelEventListener(listener,
//								  mOperation);
//                // UmlModelEventPump.getPump()
//                //     .removeModelEventListener(listener,
//                // mOperation.getParameter(0));
//                UmlModelEventPump.getPump()
//		    .addModelEventListener(listener,
//				Model.getFacade().getParameter(mOperation,
//								    0));
//            }
        }
        return mOperation;
    }

    /**
       Find an operation in the currentClassifier. If the operation is
       not found, a new is created.

       @param name The name of the method.
       @return The method found or created.
    */
    private Object getMethod(String name) {
        Object method = parseState.getMethod(name);
        if (method != null) {
            LOG.info("Getting the existing method " + name);
        } else {
            LOG.info("Creating a new method " + name);
            method = Model.getCoreFactory().buildMethod(name);
            Model.getCoreHelper().addFeature(
                    parseState.getClassifier(),
                    method);
        }
        return method;
    }

    /**
       Find an attribute in the currentClassifier. If the attribute is
       not found, a new one is created.

       @param name The name of the attribute.
       @return The attribute found or created.
    */
    private Object buildAttribute(Object classifier, String name) {
        Project project = ProjectManager.getManager().getCurrentProject();
        Object intType = project.findType("int");
        Object myModel = project.getModel();
        Object mAttribute = Model.getCoreFactory().buildAttribute(classifier,
                myModel, intType);
        Model.getCoreHelper().setName(mAttribute, name);
        return mAttribute;
    }

    /**
       Find an associationEnd from the currentClassifier to the type
       specified. If not found, a new is created.

       @param name The name of the attribute.
       @param mClassifier Where the association ends.
       @return The attribute found or created.
    */
    private Object getAssociationEnd(String name, Object mClassifier) {
        Object mAssociationEnd = null;
        for (Iterator i = Model.getFacade().getAssociationEnds(mClassifier)
                .iterator(); i.hasNext();) {
            Object ae = i.next();
            if (name.equals(Model.getFacade().getName(ae))
                && Model.getFacade().getType(
                        Model.getFacade().getOppositeEnd(ae))
                    == parseState.getClassifier()) {
                mAssociationEnd = ae;
            }
        }
        if (mAssociationEnd == null && !noAssociations) {
            String newName =
                Model.getFacade().getName(parseState.getClassifier())
                    + " -> " + Model.getFacade().getName(mClassifier);

            Object mAssociation = buildDirectedAssociation(
                        newName, parseState.getClassifier(), mClassifier);
            mAssociationEnd =
                Model.getFacade().getAssociationEnd(
                        mClassifier,
                        mAssociation);
        }
        return mAssociationEnd;
    }

    public static Object buildDirectedAssociation(
	    String name,
	    Object sourceClassifier, 
	    Object destClassifier) {
        return Model.getCoreFactory().buildAssociation(
                destClassifier, true, sourceClassifier, false, 
                name);
    }
    
    /**
       Get the stereotype with a specific name.

       @param name The name of the stereotype.
       @return The stereotype.
    */
    private Object getStereotype(String name) {
        LOG.info("Trying to find a stereotype of name <<" + name + ">>");
        // Is this line really safe wouldn't it just return the first
        // model element of the same name whether or not it is a stereotype
        Object stereotype = Model.getFacade().lookupIn(model, name);

        if (stereotype == null) {
            LOG.info("Couldn't find so creating it");
            return
                Model.getExtensionMechanismsFactory()
                    .buildStereotype(name, model);
        }

        if (!Model.getFacade().isAStereotype(stereotype)) {
            // and so this piece of code may create an existing stereotype
            // in error.
            LOG.info("Found something that isn't a stereotype so creating it");
            return
                Model.getExtensionMechanismsFactory()
                    .buildStereotype(name, model);
        }

        LOG.info("Found it");
        return stereotype;
    }

    /**
     * Find the first suitable stereotype with baseclass for a given object.
     *
     * @param me
     * @param name
     * @param baseClass
     * @return the stereotype if found
     *
     * @throws IllegalArgumentException if the desired stereotypes for
     * the modelelement and baseclass was not found and could not be created.
     * No stereotype is created.
     */
    private Object getStereotype(Object me, String name, String baseClass) {
        Collection models =
            ProjectManager.getManager().getCurrentProject().getModels();
        Collection stereos =
                Model.getExtensionMechanismsHelper().getAllPossibleStereotypes(
                        models, me);
        Object stereotype =  null;
        if (stereos != null && stereos.size() > 0) {
            Iterator iter = stereos.iterator();
            while (iter.hasNext()) {
                stereotype = iter.next();
                if (Model.getExtensionMechanismsHelper()
                        .isStereotypeInh(stereotype, name, baseClass)) {
                    LOG.info("Returning the existing stereotype of <<"
                            + Model.getFacade().getName(stereotype) + ">>");
                    return stereotype;
                }
            }
        }
        // Instead of failing, this should create any stereotypes that it
        // requires.  Most likely cause of failure is that the stereotype isn't
        // included in the profile that is being used. - tfm 20060224
        stereotype = getStereotype(name);
        if (stereotype != null) {
            Model.getExtensionMechanismsHelper().addBaseClass(stereotype, me);
            return stereotype;
        }
        // This should never happen then:
        throw new IllegalArgumentException("Could not find "
            + "a suitable stereotype for " + Model.getFacade().getName(me)
            + " -  stereotype: <<" + name
            + ">> base: " + baseClass);
    }

    /**
       Return the tagged value with a specific tag.

       @param element The tagged value belongs to this.
       @param name The tag.
       @return The found tag. A new is created if not found.
    */
    private Object getTaggedValue(Object element, String name) {
        Object tv = Model.getFacade().getTaggedValue(element, name);
        if (tv == null) {
            tv =
                    Model.getExtensionMechanismsFactory().buildTaggedValue(
                            name, "");
            Model.getExtensionMechanismsHelper().addTaggedValue(element, tv);
        }
        return tv;
    }

    /**
     * This classifier was earlier generated by reference but now it is
     * its time to be parsed so we clean out remnants.
     *
     * @param element that they are removed from
     */
    private void cleanModelElement(Object element) {
        for (Iterator i = Model.getFacade().getTaggedValues(element);
        	i.hasNext();) {
            Object tv = i.next();
            // TODO: This doesn't look right unless I don't understand it.
            // The TaggedValues are generated with a type/TagDefinition
            // of GENERATED_TAG and a value of "yes" but this is checking
            // for a value of GENERATED_TAG - tfm 20061106
            if (Model.getFacade().getValueOfTag(tv).equals(
			Facade.GENERATED_TAG)) {
                Model.getUmlFactory().delete(tv);
            }
        }
    }

    /**
       Get the package name from a fully specified classifier name.

       @param name A fully specified classifier name.
       @return The package name.
    */
    private String getPackageName(String name) {
	int lastDot = name.lastIndexOf('.');
	if (lastDot == -1) {
	    return "";
	}
        return name.substring(0, lastDot);
    }

    /**
     * Get the relative package name from a fully qualified
     * package name. So if the parameter is 'org.argouml.kernel'
     * the method is supposed to return 'kernel' (the package
     * kernel is in package 'org.argouml').
     *
     * @param packageName A fully qualified package name.
     * @return The relative package name.
     */
    private String getRelativePackageName(String packageName) {
	// Since the relative package name corresponds
	// to the classifier name of a fully qualified
	// classifier, we simply use this method.
	return getClassifierName(packageName);
    }

    /**
       Get the classifier name from a fully specified classifier name.

       @param name A fully specified classifier name.
       @return The classifier name.
    */
    private String getClassifierName(String name) {
	int lastDot = name.lastIndexOf('.');
	if (lastDot == -1) {
	    return name;
	}
        return name.substring(lastDot + 1);
    }

    /**
       Set the visibility for a model element.

       @param element The model element.
       @param modifiers A sequence of modifiers which may contain
       'private', 'protected' or 'public'.
    */
    private void setVisibility(Object element,
                               short modifiers) {
	if ((modifiers & JavaRecognizer.ACC_PRIVATE) > 0) {
	    Model.getCoreHelper().setVisibility(
	            element,
	            Model.getVisibilityKind().getPrivate());
	} else if ((modifiers & JavaRecognizer.ACC_PROTECTED) > 0) {
	    Model.getCoreHelper().setVisibility(
	            element,
	            Model.getVisibilityKind().getProtected());
	} else if ((modifiers & JavaRecognizer.ACC_PUBLIC) > 0) {
	    Model.getCoreHelper().setVisibility(
	            element,
	            Model.getVisibilityKind().getPublic());
	} else {
            // Default Java visibility is "package"
            Model.getCoreHelper().setVisibility(
                    element,
                    Model.getVisibilityKind().getPackage());
	}
    }

    /**
       Set the owner scope for a feature.

       @param feature The feature.
       @param modifiers A sequence of modifiers which may contain
       'static'.
    */
    private void setOwnerScope(Object feature, short modifiers) {
        if ((modifiers & JavaRecognizer.ACC_STATIC) > 0) {
            Model.getCoreHelper().setOwnerScope(feature,
                    Model.getScopeKind().getClassifier());
        } else {
            Model.getCoreHelper().setOwnerScope(feature,
                    Model.getScopeKind().getInstance());
        }
    }

    /**
       Set the target scope for an association end.

       @param mAssociationEnd The end.
       @param modifiers A sequence of modifiers which may contain
       'static'.
    */
    private void setTargetScope(Object mAssociationEnd, short modifiers) {
        if ((modifiers & JavaRecognizer.ACC_STATIC) > 0) {
            Model.getCoreHelper().setTargetScope(
                    mAssociationEnd,
                    Model.getScopeKind().getClassifier());
        } else {
            Model.getCoreHelper().setTargetScope(
                    mAssociationEnd,
                    Model.getScopeKind().getInstance());
        }
    }

    /**
       Get the context for a classifier name that may or may not be
       fully qualified.

       @param name The classifier name.
    */
    private Context getContext(String name) {
	Context context = parseState.getContext();
	String packageName = getPackageName(name);
	if (!"".equals(packageName)) {
	    context = new PackageContext(context, getPackage(packageName));
	}
	return context;
    }


    /**
     * Add the contents of a single standard javadoc tag to the model element.
     * Usually this will be added as a tagged value.
     *
     * This is called from {@link #addDocumentationTag} only.
     *
     * @param me the model element to add to
     * @param sTagName the name of the javadoc tag
     * @param sTagData the contents of the javadoc tag
     */
    private void addJavadocTagContents(Object me,
				       String sTagName,
				       String sTagData) {
        int colonPos = (sTagData != null) ? sTagData.indexOf(':') : -1;
        if (colonPos != -1 && (("invariant".equals(sTagName))
	    || ("pre-condition".equals(sTagName))
	    || ("post-condition".equals(sTagName)))) {

	    // add as OCL constraint
	    String sContext = OCLUtil.getContextString(me);
	    String name = sTagData.substring(0, colonPos);
	    String body = null;
	    if (sTagName.equals ("invariant")) {
		// add as invariant constraint Note that no checking
		// of constraint syntax is performed... BAD!
		body = sContext + " inv " + sTagData;
	    } else if (sTagName.equals ("pre-condition")) {
		body = sContext + " pre " + sTagData;
	    } else {
		body = sContext + " post " + sTagData;
	    }
	    Object bexpr =
		Model.getDataTypesFactory()
		    .createBooleanExpression("OCL", body);
	    Object mc =
		Model.getCoreFactory().buildConstraint(name, bexpr);
	    Model.getCoreHelper().addConstraint(me, mc);
	    if (Model.getFacade().getNamespace(me) != null) {
		// Apparently namespace management is not supported
		// for all model elements. As this does not seem to
		// cause problems, I'll just leave it at that for the
		// moment...
		Model.getCoreHelper().addOwnedElement(
		        Model.getFacade().getNamespace(me),
		        mc);
	    }
        } else {
            if ("stereotype".equals(sTagName)) {
                // multiple stereotype support:
                // make one stereotype tag from many stereotype tags
                Object tv = getTaggedValue(me, sTagName);
                if (tv != null) {
                    String sStereotype = Model.getFacade().getValueOfTag(tv);
                    if (sStereotype != null && sStereotype.length() > 0) {
                        sTagData = sStereotype + ',' + sTagData;
                    }
                }
                // now eliminate multiple entries in that comma separated list
                HashSet stSet = new HashSet();
                StringTokenizer st = new StringTokenizer(sTagData, ", ");
                while (st.hasMoreTokens()) {
                    stSet.add(st.nextToken().trim());
                }
                StringBuffer sb = new StringBuffer();
                Iterator iter = stSet.iterator();
                while (iter.hasNext()) {
                    if (sb.length() > 0) {
                        sb.append(',');
                    }
                    sb.append(iter.next());
                }
                sTagData = sb.toString();

            }
            
            Object tv = Model.getFacade().getTaggedValue(me, sTagName);
            if (tv == null) {
                Model.getExtensionMechanismsHelper().addTaggedValue(
                        me,
                        Model.getExtensionMechanismsFactory().buildTaggedValue(
                                sTagName, sTagData));
            } else {
                Model.getExtensionMechanismsHelper()
                        .setValueOfTag(tv, sTagData);
            }

	}
    }

    /**
     * Add the javadocs as a tagged value 'documentation' to the model
     * element. All comment delimiters are removed prior to adding the
     * comment.
     *
     * Added 2001-10-05 STEFFEN ZSCHALER.
     *
     * @param modelElement the model element to which to add the documentation
     * @param sJavaDocs the documentation comment to add ("" or null
     * if no java docs)
     */
    private void addDocumentationTag(Object modelElement, String sJavaDocs) {
	if ((sJavaDocs != null)
	    && (sJavaDocs.trim().length() >= 5)) {
	    StringBuffer sbPureDocs = new StringBuffer(80);
	    String sCurrentTagName = null;
	    String sCurrentTagData = null;
	    int nStartPos = 3; // skip the leading /**
	    boolean fHadAsterisk = true;

	    while (nStartPos < sJavaDocs.length()) {
		switch (sJavaDocs.charAt (nStartPos)) {
		case '*':
		    fHadAsterisk = true;
		    nStartPos++;
		    break;
		case ' ':   // all white space, hope I didn't miss any ;-)
		case '\t':
		    // ignore white space before the first asterisk
		    if (!fHadAsterisk) {
			nStartPos++;
			break;
		    }
		default:
		    // normal comment text or standard tag
		    // check ahead for tag
		    int j = nStartPos;
		    while ((j < sJavaDocs.length())
			   && ((sJavaDocs.charAt (j) == ' ')
			       || (sJavaDocs.charAt (j) == '\t'))) {
			j++;
		    }
		    if (j < sJavaDocs.length()) {
			if (sJavaDocs.charAt (j) == '@') {
			    // if the last javadoc is on the last line
			    // no new line will be found, causing an
			    // indexoutofboundexception.
			    int lineEndPos = 0;
			    if (sJavaDocs.indexOf('\n', j) < 0) {
				lineEndPos = sJavaDocs.length() - 2;
			    } else {
				lineEndPos = sJavaDocs.indexOf('\n', j) + 1;
			    }
			    sbPureDocs.append(sJavaDocs.substring(j,
								  lineEndPos));
			    // start standard tag potentially add
			    // current tag to set of tagged values...
			    if (sCurrentTagName != null) {
				addJavadocTagContents (modelElement,
						       sCurrentTagName,
						       sCurrentTagData);
			    }
			    // open new tag
			    int nTemp = sJavaDocs.indexOf (' ', j + 1);
			    if (nTemp == -1) {
				nTemp = sJavaDocs.length() - 1;
			    }
			    sCurrentTagName = sJavaDocs.substring(j + 1,
								  nTemp);
			    int nTemp1 = sJavaDocs.indexOf ('\n', ++nTemp);
			    if (nTemp1 == -1) {
				nTemp1 = sJavaDocs.length();
			    } else {
				nTemp1++;
			    }
			    sCurrentTagData =
				sJavaDocs.substring (nTemp, nTemp1);
			    nStartPos = nTemp1;
			} else {
			    // continue standard tag or comment text
			    int nTemp = sJavaDocs.indexOf ('\n', nStartPos);
			    if (nTemp == -1) {
				nTemp = sJavaDocs.length();
			    } else {
				nTemp++;
			    }
			    if (sCurrentTagName != null) {
				sbPureDocs.append(sJavaDocs.substring(nStartPos,
								      nTemp));
				sCurrentTagData +=
				    " "
				    + sJavaDocs.substring (nStartPos, nTemp);
			    } else {
				sbPureDocs.append(sJavaDocs.substring(nStartPos,
								      nTemp));
			    }
			    nStartPos = nTemp;
			}
		    }
		    fHadAsterisk = false;
		}
	    }
            sJavaDocs = sbPureDocs.toString();

            /*
             * After this, we have the documentation text, but there's still a
             * trailing '/' left, either at the end of the actual comment text
             * or at the end of the last tag.
             */
            sJavaDocs = removeTrailingSlash(sJavaDocs);

            // handle last tag, if any (strip trailing slash there too)
	    if (sCurrentTagName != null) {
		sCurrentTagData = removeTrailingSlash(sCurrentTagData);
		addJavadocTagContents (modelElement, sCurrentTagName,
				       sCurrentTagData);
	    }

	    // Now store documentation text in a tagged value
	    Model.getExtensionMechanismsHelper().addTaggedValue(
                    modelElement,
                    Model.getExtensionMechanismsFactory().buildTaggedValue(
                            Argo.DOCUMENTATION_TAG, sJavaDocs));
	    addStereotypes(modelElement);
        }
    }


    /*
     * Remove a trailing slash, including the entire line if it's the only thing
     * on the line.
     */
    private String removeTrailingSlash(String s) {
        if (s.endsWith("\n/")) {
            return s.substring(0, s.length() - 2);
        } else  if (s.endsWith("/")) {
            return s.substring(0, s.length() - 1);
        } else {
            return s;
        }
    }

    /*
     * If there is a tagged value named 'stereotype', make it a real
     * stereotype and remove the tagged value.
     * We allow multiple instances of this tagged value
     * AND parse a single instance for multiple stereotypes
     */
    private void addStereotypes(Object modelElement) {
        Object tv = Model.getFacade()
                .getTaggedValue(modelElement, "stereotype");
        if (tv != null) {
            String stereo = Model.getFacade().getValueOfTag(tv);
            if (stereo != null && stereo.length() > 0) {
                StringTokenizer st = new StringTokenizer(stereo, ", ");
                while (st.hasMoreTokens()) {
                    Model.getCoreHelper().addStereotype(modelElement,
                            getStereotype(st.nextToken().trim()));
                }
            }
            Model.getUmlFactory().delete(tv);
        }
    }

    /**
     * Manage collection of parsed method calls. Used for reverse engineering of
     * interactions.
     */
    /**
     * Add a parsed method call to the collection of method calls.
     * @param methodName
     *            The method name called.
     */
    public void addCall(String methodName) {
        methodCalls.add(methodName);
    }

    /**
     * Get collection of method calls.
     * @return vector containing collected method calls
     */
    public synchronized Vector getMethodCalls() {
        return methodCalls;
    }

    /**
     * Clear collected method calls.
     */
    public void clearMethodCalls() {
        methodCalls.clear();
    }

    /**
     * Add a local variable declaration to the list of variables.
     *
     * @param type type of declared variable
     * @param name name of declared variable
     */
    public void addLocalVariableDeclaration(String type, String name) {
        localVariables.put(name, type);
    }

    /**
     * Return the collected set of local variable declarations.
     *
     * @return hashtable containing all local variable declarations.
     */
    public Hashtable getLocalVariableDeclarations() {
        return localVariables;
    }

    /**
     * Clear the set of local variable declarations.
     */
    public void clearLocalVariableDeclarations() {
        localVariables.clear();
    }
    
    /**
     * Get the elements which were created while reverse engineering this file.
     * 
     * @return the collection of elements
     */
    public Collection getNewElements() {
        return newElements;
    }
}
