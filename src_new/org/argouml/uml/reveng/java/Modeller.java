// $Id$
// Copyright (c) 2003 The Regents of the University of California. All
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

// File: Modeller.java
// Classes: Modeller
// Original Author: Marcus Andersson andersson@users.sourceforge.net

package org.argouml.uml.reveng.java;

import java.util.Collection;
import java.util.Iterator;
import java.util.Stack;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.ModelFacade;
import org.argouml.model.uml.UmlFactory;
import org.argouml.model.uml.UmlHelper;
import org.argouml.model.uml.UmlModelEventPump;
import org.argouml.model.uml.foundation.core.CoreFactory;
import org.argouml.ocl.OCLUtil;
import org.argouml.uml.MMUtil;
import org.argouml.uml.reveng.DiagramInterface;
import org.argouml.uml.reveng.Import;
import org.tigris.gef.base.Globals;

/**
 * Modeller maps Java source code(parsed/recognised by ANTLR) to UML model
 * elements, it applies some of the semantics in JSR26.
 */
public class Modeller
{
    protected static Logger cat = Logger.getLogger(Modeller.class);
    /** Current working model. */
    private Object model;

    private DiagramInterface _diagram;
    
    /** Current import session */
    private Import _import;

    /** The package which the currentClassifier belongs to. */
    private Object currentPackage;
    
    /** Last package name used in addPackage().
     * It is null for classes wich are not packaged.
     * Used in popClassifier() to create diagram for that
     * packaget. 
     */
    private String currentPackageName;

    /** Keeps the data that varies during parsing. */
    private ParseState parseState;

    /** Stack up the state when descending inner classes. */
    private Stack parseStateStack;

    /** Only attributes will be generated. */
    private boolean noAssociations;

    /** Arrays will be modelled as unique datatypes. */
    private boolean arraysAsDatatype;

    /** the name of the file being parsed */
    private String fileName;
    /**
       Create a new modeller.

       @param model The model to work with.
    */
    public Modeller(Object model,
		    DiagramInterface diagram,
		    Import _import,
		    boolean noAssociations,
		    boolean arraysAsDatatype,
		    String fileName)
    {
	this.model = model;
	this.noAssociations = noAssociations;
	this.arraysAsDatatype = arraysAsDatatype;
	this._import = _import;
	currentPackage = this.model;
	parseState = new ParseState(this.model, getPackage("java.lang"));
	parseStateStack = new Stack();
	_diagram = diagram;
        this.fileName = fileName;
    }

    /**
     * Get the current diagram.
     *
     * @return a interface to the current diagram.
     */
    private DiagramInterface getDiagram() {
	return _diagram;
    }

    /**
     * This is a mapping from a Java compilation Unit -> a UML component.
     * Classes are resident in a component.
     * Imports are relationships between components and other classes
     * / packages.
     * <p>See JSR 26.
     */
    public void addComponent() {
        
        // try and find the component in the current package
        // to cope with repeated imports
        Object component = ModelFacade.lookupIn(currentPackage, fileName);
        
        if (component == null) {
        
            // remove the java specific ending (per JSR 26).
            // BUT we can't do this because then the component will be confused
            // with its class with the same name when invoking
            // ModelFacade.lookupIn(Object,String)
            /*
	      if(fileName.endsWith(".java"))
	      fileName = fileName.substring(0,
	      fileName.length()-5);
            */
            
            component = UmlFactory.getFactory().getCore().createComponent();
            ModelFacade.setName(component, fileName);
        }
        
        parseState.addComponent(component);
        
        // set the namespace of the component, in the event
        // that the source file does not have a package stmt
        ModelFacade.setNamespace(parseState.getComponent(), model);
    }
    
    /**
       Called from the parser when a package clause is found.

       @param name The name of the package.
    */
    public void addPackage(String name)
    {
	// Add a package figure for this package to the owners class
	// diagram, if it's not in the diagram yet. I do this for all
	// the class diagrams up to the top level, since I need
	// diagrams for all the packages.
	String ownerPackageName, currentName = name;
	while ( !"".equals(ownerPackageName = getPackageName(currentName))) {
	    if (getDiagram() != null && _import.isCreateDiagramsChecked() && getDiagram().isDiagramInProject(ownerPackageName)) {
                getDiagram().selectClassDiagram( getPackage(ownerPackageName),
						 ownerPackageName);
                getDiagram().addPackage(getPackage(currentName));
            }
	    currentName = ownerPackageName;
	}
	// Save src_path in the upper package
	Object mPackage = getPackage(currentName);
	if (_import.getSrcPath() != null
	    && ModelFacade.getTaggedValue(mPackage, "src_path") == null)
	    ModelFacade.setTaggedValue(mPackage, "src_path",
				       _import.getSrcPath());
		
	// Find or create a MPackage NSUML object for this package.
	mPackage = getPackage(name);
        currentPackageName = name;

	// Set the current package for the following source code.
	currentPackage = mPackage;
	parseState.addPackageContext(mPackage);

        // Delay diagram creation until any classifier (class or
        // interface) will be found
        
        // set the namespace of the component
        ModelFacade.setNamespace(parseState.getComponent(), currentPackage);
    }

    /**
       Called from the parser when an import clause is found.

       @param name The name of the import. Can end with a '*'.
    */
    public void addImport(String name)
    {
	String packageName = getPackageName(name);
	String classifierName = getClassifierName(name);
	Object mPackage = getPackage(packageName);

        // import on demand
	if (classifierName.equals("*")) {
	    parseState.addPackageContext(mPackage);
            Object perm = null;
            
            // try find an existing permission
            Iterator dependenciesIt =
                UmlHelper.getHelper().getCore()
		.getDependencies(mPackage, parseState.getComponent())
		.iterator();
            while (dependenciesIt.hasNext()) {
                
                Object dependency = dependenciesIt.next();
                if (ModelFacade.isAPermission(dependency)) {
                    
                    perm = dependency;
                    break;
                }
            }
            
            // if no existing permission was found.
            if (perm == null) {
		perm =
		    UmlFactory.getFactory().getCore().buildPermission(parseState.getComponent(),
								      mPackage);
		ModelFacade.setName(perm,
				    ModelFacade.getName(parseState.getComponent()) +
				    " -> " +
				    packageName);
            }
	}
        // single type import
	else {
            Object mClassifier = null;
	    try {
		mClassifier =
		    (new PackageContext(null, mPackage)).get(classifierName);
		parseState.addClassifierContext(mClassifier);
                Object perm = null;
                
                // try find an existing permission
                Iterator dependenciesIt =
		    UmlHelper.getHelper().getCore()
                    .getDependencies(mClassifier, parseState.getComponent())
                    .iterator();
                while (dependenciesIt.hasNext()) {
                    
                    Object dependency = dependenciesIt.next();
                    if (ModelFacade.isAPermission(dependency)) {
                        
                        perm = dependency;
                        break;
                    }
                }
                
                // if no existing permission was found.
                if (perm == null) {
                    perm =
			UmlFactory.getFactory().getCore().buildPermission(parseState.getComponent(),
									  mClassifier);
                    ModelFacade.setName(perm,
					ModelFacade.getName(parseState.getComponent()) +
					" -> " +
					ModelFacade.getName(mClassifier));
                }
	    }
	    catch (ClassifierNotFoundException e) {
		// Currently if a classifier cannot be found in the
                // model/classpath then information will be lost from
                // source files, because the classifier cannot be
                // created on the fly.
                cat.warn("Modeller.java: a classifier that was in the source" +
                         " file could not be generated in the model " +
                         "(to generate an imported classifier)- information lost\n" +
                         "\t" + e);
	    }
            
            
	}
    }

    /**
       Called from the parser when a class declaration is found.

       @param name The name of the class.
       @param modifiers A sequence of class modifiers.
       @param superclass Zero or one string with the name of the
       superclass. Can be fully qualified or
       just a simple class name.
       @param interfaces Zero or more strings with the names of implemented
       interfaces. Can be fully qualified or just a
       simple interface name.
       @param javadoc The javadoc comment. null or "" if no comment available.
    */
    public void addClass(String name,
                         short modifiers,
                         String superclassName,
                         Vector interfaces,
                         String javadoc)
    {
        Object mClass =
	    addClassifier(UmlFactory.getFactory().getCore().createClass(), name, modifiers, javadoc);

        ModelFacade.setAbstract(mClass,
				(modifiers & JavaRecognizer.ACC_ABSTRACT) > 0);
        ModelFacade.setLeaf(mClass, (modifiers & JavaRecognizer.ACC_FINAL) > 0);
        ModelFacade.setRoot(mClass, false);


	if (superclassName != null) {
	    try {
		Object parentClass =
		    getContext(superclassName).get(getClassifierName(superclassName));
		getGeneralization(currentPackage, parentClass, mClass);
	    }
	    catch (ClassifierNotFoundException e) {
		// Currently if a classifier cannot be found in the
		// model/classpath then information will be lost from
		// source files, because the classifier cannot be
		// created on the fly.
		cat.warn("Modeller.java: a classifier that was in the source" +
                         " file could not be generated in the model " +
                         "(to generate a generalization)- information lost\n" +
                         "\t" + e);
	    }
	}

	for (Iterator i = interfaces.iterator(); i.hasNext(); ) {
	    String interfaceName = (String) i.next();
	    try {
		Object mInterface =
		    getContext(interfaceName).getInterface(getClassifierName(interfaceName));
		Object mAbstraction =
		    getAbstraction(currentPackage, mInterface, mClass);
		if (ModelFacade.getSuppliers(mAbstraction).size() == 0) {
		    ModelFacade.addSupplier(mAbstraction, mInterface);
		    ModelFacade.addClient(mAbstraction, mClass);
		}
		ModelFacade.setNamespace(mAbstraction, currentPackage);
		ModelFacade.setStereotype(mAbstraction,
					  getStereotype("realize"));
	    }
	    catch (ClassifierNotFoundException e) {
		// Currently if a classifier cannot be found in the
		// model/classpath then information will be lost from
		// source files, because the classifier cannot be
		// created on the fly.
		cat.warn("Modeller.java: a classifier that was in the source" +
                         " file could not be generated in the model " +
                         "(to generate a abstraction)- information lost\n" +
                         "\t" + e);
	    }
	}

    }

    /**
       Called from the parser when an anonymous inner class is found.

       @param type The type of this anonymous class.
    */
    public void addAnonymousClass(String type)
    {
        String name = parseState.anonymousClass();
        try {
            Object mClassifier = getContext(type).get(getClassifierName(type));
            Vector interfaces = new Vector();
            if (ModelFacade.isAInterface(mClassifier)) {
                interfaces.add(type);
            }
            addClass(name,
		     (short) 0,
		     ModelFacade.isAClass(mClassifier) ? type : null,
		     interfaces,
		     "");
        }
        catch (ClassifierNotFoundException e) {
            // Must add it anyway, or the class poping will mismatch.
            addClass(name, (short) 0, null, new Vector(), "");
        }
    }

    /**
       Called from the parser when an interface declaration is found.

       @param name The name of the interface.
       @param modifiers A sequence of interface modifiers.
       @param interfaces Zero or more strings with the names of extended
       interfaces. Can be fully qualified or just a
       simple interface name.
       @param javadoc The javadoc comment. "" if no comment available.
    */
    public void addInterface(String name,
                             short modifiers,
                             Vector interfaces,
                             String javadoc)
    {
        Object mInterface =
	    addClassifier(UmlFactory.getFactory().getCore().createInterface(),
			  name,
			  modifiers,
			  javadoc);
        for (Iterator i = interfaces.iterator(); i.hasNext(); ) {
            String interfaceName = (String) i.next();
            try {
                Object parentInterface =
		    getContext(interfaceName).getInterface(getClassifierName(interfaceName));
                getGeneralization(currentPackage, parentInterface, mInterface);
            }
            catch (ClassifierNotFoundException e) {
		// Currently if a classifier cannot be found in the
                // model/classpath then information will be lost from
                // source files, because the classifier cannot be
                // created on the fly.
                cat.warn("Modeller.java: a classifier that was in the source" +
                         " file could not be generated in the model " +
                         "(to generate a generalization)- information lost\n" +
                         "\t" + e);
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
                                 String javadoc)
    {
	Object mClassifier;
	Object mNamespace;

        // the new classifier is a java inner class
	if (parseState.getClassifier() != null) {
	    mClassifier =
		ModelFacade.lookupIn(parseState.getClassifier(), name);
	    mNamespace = parseState.getClassifier();
	}
        // the new classifier is a top level java class
	else {
	    parseState.outerClassifier();
	    mClassifier = ModelFacade.lookupIn(currentPackage, name);
	    mNamespace = currentPackage;
	}
        // if the classifier could not be could in the model
	if (mClassifier == null) {
	    mClassifier = newClassifier;
	    ModelFacade.setName(mClassifier, name);
	    ModelFacade.setNamespace(mClassifier, mNamespace);
	}
        // it was found and we delete andy existing tagged values.
	else {
	    cleanModelElement(mClassifier);
	}
	parseState.innerClassifier(mClassifier);
        
        // set up the component residency (only for top level classes)
        if (parseState.getClassifier() == null) {
            // set the clasifier to be a resident in its component:
            // (before we push a new parse state on the stack)
            Object residentDep = null;
            
            // try find an existing residency
            Iterator dependenciesIt =
		UmlHelper.getHelper().getCore()
                .getDependencies(mClassifier, parseState.getComponent())
		.iterator();
            while (dependenciesIt.hasNext()) {
                
                Object dependency = dependenciesIt.next();
                residentDep = dependency;
                break;
            }
            
            // if no existing residency was found.
            if (residentDep == null) {
                
                // this doesn't work because of a bug in NSUML (the
                // ElementResidence association class is never saved
                // to the xmi).
                // //UmlHelper.getHelper().getCore().setResident(parseState.getComponent(),mClassifier);
                
                // therefore temporarily use a non-standard hack:
                //if (parseState.getComponent() == null) addComponent();
                residentDep = CoreFactory.getFactory()
		    .buildDependency(parseState.getComponent(), mClassifier);
                UmlFactory.getFactory().getExtensionMechanisms()
		    .buildStereotype(
				     residentDep,
				     "resident",
				     model);
                ModelFacade.setName(residentDep,
				    ModelFacade.getName(parseState.getComponent()) +
				    " -(location of)-> " +
				    ModelFacade.getName(mClassifier));
            }
        }
        
        // change the parse state to a classifier parse state
        parseStateStack.push(parseState);
        parseState = new ParseState(parseState, mClassifier, currentPackage);


	setVisibility(mClassifier, modifiers);

	/*
	 * Changed 2001-10-05 STEFFEN ZSCHALER
	 *
	 * Was (space added below!):
	 *
	 if((javadoc == null) || "".equals(javadoc)) {
	 javadoc = "/** * /";
	 }
	 getTaggedValue(mClassifier, "documentation").setValue(javadoc);
	 *
	 */

	addDocumentationTag (mClassifier, javadoc);
  
        
	return mClassifier;
    }

    /**
       Called from the parser when a classifier is completely parsed.
    */
    public void popClassifier()
    {
        // now create diagram if it doesn't exists in project
	if (_import.isCreateDiagramsChecked()) {
	    if (getDiagram() == null) {
		_diagram = new DiagramInterface(Globals.curEditor());
		if (currentPackageName != null
		    && !currentPackageName.trim().equals(""))
		{
		    // create new diagram or select existing diagram for package
		    _diagram.createOrSelectClassDiagram(currentPackage,
							currentPackageName);
		} else {
		    // create new diagram in root for classifier without package
		    _diagram.createRootClassDiagram();
		}
				
	    } else {
		if (currentPackageName != null) {
		    getDiagram().selectClassDiagram(currentPackage,
						    currentPackageName);
		}
		// the DiagramInterface is instantiated already
		// but the class is in a source file
		// with no package declaration
		else {
		    // create new diagram in root for classifier without package
		    _diagram.createRootClassDiagram();
		}
	    }
	}
        // add the current classifier to the diagram.
        Object classifier = parseState.getClassifier();
        if (ModelFacade.isAInterface(classifier)) {
            if (getDiagram() != null && _import.isCreateDiagramsChecked())
		_diagram.addInterface(classifier,
				      _import.isMinimiseFigsChecked());
        } else {
            if (ModelFacade.isAClass(classifier)) {
                if (getDiagram() != null && _import.isCreateDiagramsChecked())
		    _diagram.addClass(classifier,
				      _import.isMinimiseFigsChecked());
            }
        }

        // Remove operations and attributes not in source
        parseState.removeObsoleteFeatures();

        // Remove inner classes not in source
        parseState.removeObsoleteInnerClasses();

        parseState = (ParseState) parseStateStack.pop();
    }

    /** Called from the parser when an operation is
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
	Object mOperation = getOperation(name);
	parseState.feature(mOperation);

	ModelFacade.setAbstract(mOperation,
				(modifiers & JavaRecognizer.ACC_ABSTRACT) > 0);
	ModelFacade.setLeaf(mOperation,
			    (modifiers & JavaRecognizer.ACC_FINAL) > 0);
	ModelFacade.setRoot(mOperation, false);
	setOwnerScope(mOperation, modifiers);
	setVisibility(mOperation, modifiers);
	if ((modifiers & JavaRecognizer.ACC_SYNCHRONIZED) > 0) {
	    ModelFacade.setConcurrency(mOperation, ModelFacade.GUARDED);
	} else if (ModelFacade.getConcurrency(mOperation) == ModelFacade.GUARDED_CONCURRENCYKIND) {
	    ModelFacade.setConcurrency(mOperation, ModelFacade.SEQUENTIAL);
	}

	for (Iterator i = ModelFacade.getParameters(mOperation).iterator(); i.hasNext(); )
	{
	    ModelFacade.removeParameter(mOperation, i.next());
	}

	Object mParameter;
	String typeName;
	Object mPackage;
	Object mClassifier;

	if (returnType == null) {
	    // Constructor
	    ModelFacade.setStereotype(mOperation, getStereotype("create"));
	}
	else {
	    try {
		mClassifier =
		    getContext(returnType).get(getClassifierName(returnType));
                
		mParameter =
		    UmlFactory.getFactory().getCore().buildParameter(mOperation);
		ModelFacade.setName(mParameter, "return");
		ModelFacade.setKindToReturn(mParameter);

                ModelFacade.setType(mParameter, mClassifier);
	    }
	    catch (ClassifierNotFoundException e) {
		// Currently if a classifier cannot be found in the
                // model/classpath then information will be lost from
                // source files, because the classifier cannot be
                // created on the fly.
                cat.warn("Modeller.java: a classifier that was in the source " +
                         "file could not be generated in the model " +
                         "(for generating operation return type)- information lost\n" +
                         "\t" + e);
	    }
	}

	for (Iterator i = parameters.iterator(); i.hasNext(); ) {
	    Vector parameter = (Vector) i.next();
	    typeName = (String) parameter.elementAt(1);
	    try {
                mClassifier =
		    getContext(typeName).get(getClassifierName(typeName));
                mParameter =
		    UmlFactory.getFactory().getCore().buildParameter(mOperation);
		ModelFacade.setName(mParameter, (String) parameter.elementAt(2));
		ModelFacade.setKindToIn(mParameter);
		ModelFacade.setType(mParameter, mClassifier);
	    }
	    catch (ClassifierNotFoundException e) {
		// Currently if a classifier cannot be found in the
                // model/classpath then information will be lost from
                // source files, because the classifier cannot be
                // created on the fly.
                cat.warn("Modeller.java: a classifier that was in the source " +
                         "file could not be generated in the model " +
                         "(for generating operation params)- information lost\n" +
                         "\t" + e);
	    }
	}


	/*
	 * Changed 2001-10-05 STEFFEN ZSCHALER.
	 *
	 * Was (space added below!):
	 *
	 if((javadoc == null) || "".equals(javadoc)) {
	 javadoc = "/** * /";
	 }
	 getTaggedValue(mOperation, "documentation").setValue(javadoc);
	 *
	 * Moved to end of method 2001-11-05 to allow addDocumentationTag to
	 * access as much information as possible
	 */

	addDocumentationTag (mOperation, javadoc);

	return mOperation;
    }

    /**
     * Called from the parser to add a method body to an operation.
     * (An operation will have exactly one Java body.)
     *
     * @param op An operation.
     * @param body A method body.
     */
    public void addBodyToOperation(Object op, String body)
    {
        if (op == null || !ModelFacade.isAOperation(op)) {
            cat.warn("adding body failed: no operation!");
            return;
        }
        if (body == null || body.length() == 0)
            return;

        Object method = getMethod(ModelFacade.getName(op));
        parseState.feature(method);
        ModelFacade.setBody(method,
			    UmlFactory.getFactory().getDataTypes()
			    .createProcedureExpression("Java",
						       body));
	// Add the method to it's specification.
        ModelFacade.addMethod(op, method);

        // Add this method as a feature to the classifier that owns
        // the operation.
        ModelFacade.addFeature(ModelFacade.getOwner(op), method);
    }

    /**
       Called from the parser when an attribute is found.

       @param modifiers A sequence of attribute modifiers.
       @param typeSpec The attribute's type.
       @param variable The name of the attribute.
       @param initializer The initial value of the attribute.
       @param javadoc The javadoc comment. null or "" if no comment available.
    */
    public void addAttribute (short modifiers,
                              String typeSpec,
                              String name,
                              String initializer,
                              String javadoc) {
	String multiplicity = null;

	if (!arraysAsDatatype && typeSpec.indexOf('[') != -1) {
	    typeSpec = typeSpec.substring(0, typeSpec.indexOf('['));
	    multiplicity = "1_N";
	} else
	    multiplicity = "1_1";
      
	// the attribute type
	Object mClassifier = null;
	try {
	    // get the attribute type
	    mClassifier = getContext(typeSpec).get(getClassifierName(typeSpec));
	} catch (ClassifierNotFoundException e) {
	    // Currently if a classifier cannot be found in the
	    // model/classpath then information will be lost from
	    // source files, because the classifier cannot be
	    // created on the fly.
	    cat.warn("Modeller.java: a classifier that was in the source " +
		     "file could not be generated in the model " +
		     "(for generating an attribute)- information lost\n" +
		     "\t" + e);
                
	    // if we can't find the attribute type then
	    // we can't add the attribute.
	    return;
	}
          
	// if we want to create a UML attribute:
	if (noAssociations ||
	    ModelFacade.isADataType(mClassifier) ||
	    ModelFacade.getNamespace(mClassifier) == getPackage("java.lang")) {
      
            Object mAttribute = getAttribute(name, initializer, mClassifier);

            parseState.feature(mAttribute);

            setOwnerScope(mAttribute, modifiers);
            setVisibility(mAttribute, modifiers);
            ModelFacade.setMultiplicity(mAttribute, multiplicity);
            ModelFacade.setType(mAttribute, mClassifier);

            // Set the initial value for the attribute.
            if (initializer != null) {
                ModelFacade.setInitialValue(mAttribute,
					    UmlFactory.getFactory().getDataTypes()
					    .createExpression("Java",
							      initializer));
            }

            if ((modifiers & JavaRecognizer.ACC_FINAL) > 0) {
                ModelFacade.setChangeable(mAttribute, false);
            }
            else if (!ModelFacade.isChangeable(mAttribute)) {
                ModelFacade.setChangeable(mAttribute, true);
            }
            addDocumentationTag(mAttribute, javadoc);
        }
        // we want to create a UML association from the java attribute
        else {
            
            Object mAssociationEnd = getAssociationEnd(name, mClassifier);
            setTargetScope(mAssociationEnd, modifiers);
            setVisibility(mAssociationEnd, modifiers);
            ModelFacade.setMultiplicity(mAssociationEnd, multiplicity);
            ModelFacade.setType(mAssociationEnd, mClassifier);
            ModelFacade.setName(mAssociationEnd, name);
            if ((modifiers & JavaRecognizer.ACC_FINAL) > 0) {
                ModelFacade.setChangeable(mAssociationEnd, false);
            }
            ModelFacade.setNavigable(mAssociationEnd, true);
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
                                     Object child)
    {
        String name =
	    ModelFacade.getName(child) + " -> " + ModelFacade.getName(parent);
        Object mGeneralization = null;
        mGeneralization = ModelFacade.getGeneralization(child, parent);
        if (mGeneralization == null) {
            mGeneralization =
		CoreFactory.getFactory().buildGeneralization(child, parent,
							     name);
        }
        if (mGeneralization != null) {
            ModelFacade.setNamespace(mGeneralization, mPackage);
        }
        return mGeneralization;
    }

    /**
       Find an abstraction<<realize>> in the model. If it does not
       exist, a new abstraction is created.

       @param mPackage Look in this package.
       @param parent The superclass.
       @param child The subclass.
       @return The abstraction found or created.
    */
    private Object getAbstraction(Object mPackage,
                                  Object parent,
                                  Object child)
    {
        String name =
	    ModelFacade.getName(child) + " -> " + ModelFacade.getName(parent);
        Object mAbstraction = null;
        for (Iterator i = ModelFacade.getClientDependencies(child).iterator();
	     i.hasNext(); )
	{
            mAbstraction = i.next();
            Collection c = ModelFacade.getSuppliers(mAbstraction);
            if (c == null || c.size() == 0) {
                ModelFacade.removeClientDependency(child, mAbstraction);
            }
            else {
                if (parent != c.toArray()[0]) {
                    mAbstraction = null;
                }
                else {
                    break;
                }
            }
        }

        if (mAbstraction == null) {
            mAbstraction =
		UmlFactory.getFactory().getCore().buildAbstraction(name);
        }
        return mAbstraction;
    }

    /**
       Find a package in the model. If it does not exist, a new
       package is created.

       @param name The name of the package.
       @return The package found or created.
    */
    private Object getPackage(String name)
    {
	Object mPackage = searchPackageInModel(name);
	if (mPackage == null) {
	    mPackage =
		UmlFactory.getFactory().getModelManagement().buildPackage(getRelativePackageName(name),
									  name);
	    ModelFacade.setNamespace(mPackage, model);

	    // Find the owner for this package.
	    if ("".equals(getPackageName(name))) {
		ModelFacade.addOwnedElement(model, mPackage);
	    }
	    else {
		ModelFacade.addOwnedElement(getPackage(getPackageName(name)),
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
	    return ModelFacade.lookupIn(model, name);
	} else {
	    Object owner = searchPackageInModel(getPackageName(name));
	    return owner == null
		? null
		: ModelFacade.lookupIn(owner, getRelativePackageName(name));
	}
    }

    /**
       Find an operation in the currentClassifier. If the operation is
       not found, a new is created.

       @param name The name of the operation.
       @return The operation found or created.
    */
    private Object getOperation(String name)
    {
        Object mOperation = parseState.getOperation(name);
        if (mOperation == null) {
            mOperation =
		UmlFactory.getFactory().getCore().buildOperation(parseState.getClassifier(), name);
            Iterator it2 =
		ProjectManager.getManager().getCurrentProject().findFigsForMember(parseState.getClassifier()).iterator();
            while (it2.hasNext()) {
                Object listener = it2.next();
                // UmlModelEventPump.getPump().removeModelEventListener(listener,
                // mOperation);
                UmlModelEventPump.getPump().addModelEventListener(listener,
								  mOperation);
                // UmlModelEventPump.getPump().removeModelEventListener(listener,
                // mOperation.getParameter(0));
                UmlModelEventPump.getPump()
		    .addModelEventListener(listener,
					   ModelFacade.getParameter(mOperation,
								    0));
            }
        }
        return mOperation;
    }

    /**
       Find an operation in the currentClassifier. If the operation is
       not found, a new is created.

       @param name The name of the method.
       @return The method found or created.
    */
    private Object getMethod(String name)
    {
        Object mMethod = parseState.getMethod(name);
        if (mMethod == null) {
            mMethod = UmlFactory.getFactory().getCore().buildMethod(name);
            ModelFacade.addFeature(parseState.getClassifier(), mMethod);
        }
        return mMethod;
    }

    /**
       Find an attribute in the currentClassifier. If the attribute is
       not found, a new is created.

       @param name The name of the attribute.
       @param initializer The initializer code.
       @param mClassifier The type, used when checking for existing
       association.
       @return The attribute found or created.
    */
    private Object getAttribute(String name,
                                String initializer,
                                Object mClassifier)
    {
        Object mAttribute = parseState.getFeature(name);
        if (mAttribute == null) {
            mAttribute = UmlFactory.getFactory().getCore().buildAttribute(name);
            ModelFacade.addFeature(parseState.getClassifier(), mAttribute);
        }
        return mAttribute;
    }

    /**
       Find an associationEnd from the currentClassifier to the type
       specified. If not found, a new is created.

       @param name The name of the attribute.
       @param mClassifier Where the association ends.
       @return The attribute found or created.
    */
    private Object getAssociationEnd(String name,
                                     Object mClassifier)
    {
        Object mAssociationEnd = null;
        for (Iterator i = ModelFacade.getAssociationEnds(mClassifier).iterator(); i.hasNext(); ) {
            Object ae = i.next();
            if (name.equals(ModelFacade.getName(ae))) {
                mAssociationEnd = ae;
            }
        }
        if (mAssociationEnd == null && !noAssociations) {
            Object mAssociation = CoreFactory.getFactory()
		.buildAssociation(mClassifier,
				  true,
				  parseState.getClassifier(),
				  false,
				  ModelFacade.getName(parseState.getClassifier())
				  + " -> "
				  + ModelFacade.getName(mClassifier));
            mAssociationEnd =
		ModelFacade.getAssociationEnd(mClassifier, mAssociation);
        }
        return mAssociationEnd;
    }

    /**
       Get the stereotype with a specific name.

       @param name The name of the stereotype.
       @return The stereotype.
    */
    private Object getStereotype(String name)
    {
	Object mStereotype = ModelFacade.lookupIn(model, name);

	if (mStereotype == null || !ModelFacade.isAStereotype(mStereotype)) {
	    mStereotype =
		UmlFactory.getFactory().getExtensionMechanisms()
		.buildStereotype(name, model);
	}

	return mStereotype;
    }

    /**
       Return the tagged value with a specific tag.

       @param element The tagged value belongs to this.
       @param name The tag.
       @return The found tag. A new is created if not found.
    */
    private Object getTaggedValue(Object element, String name)
    {
        Object tv = ModelFacade.getTaggedValue(element, name);
        if (tv == null) {
            ModelFacade.setTaggedValue(element, name, "");
            tv = ModelFacade.getTaggedValue(element, name);
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
        for (Iterator i = ModelFacade.getTaggedValues(element); i.hasNext(); ) {
            Object tv = i.next();
            if (ModelFacade.getValueOfTag(tv).equals(MMUtil.GENERATED_TAG)) {
                UmlFactory.getFactory().delete(tv);
            }
        }
    }

    /**
       Get the package name from a fully specified classifier name.

       @param name A fully specified classifier name.
       @return The package name.
    */
    private String getPackageName(String name)
    {
	int lastDot = name.lastIndexOf('.');
	if (lastDot == -1) {
	    return "";
	}
	else {
	    return name.substring(0, lastDot);
	}
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
    private String getClassifierName(String name)
    {
	int lastDot = name.lastIndexOf('.');
	if (lastDot == -1) {
	    return name;
	}
	else {
	    return name.substring(lastDot + 1);
	}
    }

    /**
       Set the visibility for a model element.

       @param element The model element.
       @param modifiers A sequence of modifiers which may contain
       'private', 'protected' or 'public'.
    */
    private void setVisibility(Object element,
                               short modifiers)
    {
	if ((modifiers & JavaRecognizer.ACC_STATIC) > 0) {
            ModelFacade.setTaggedValue(element, "src_modifiers", "static");
	}
	if ((modifiers & JavaRecognizer.ACC_PRIVATE) > 0) {
	    ModelFacade.setVisibility(element, ModelFacade.ACC_PRIVATE);
	}
	else if ((modifiers & JavaRecognizer.ACC_PROTECTED) > 0) {
	    ModelFacade.setVisibility(element, ModelFacade.ACC_PROTECTED);
	}
	else if ((modifiers & JavaRecognizer.ACC_PUBLIC) > 0) {
	    ModelFacade.setVisibility(element, ModelFacade.ACC_PUBLIC);
	} else {
            ModelFacade.setTaggedValue(element, "src_visibility", "default");
	}
    }

    /**
       Set the owner scope for a feature.

       @param feature The feature.
       @param modifiers A sequence of modifiers which may contain
       'static'.
    */
    private void setOwnerScope(Object feature, short modifiers)
    {
        if ((modifiers & JavaRecognizer.ACC_STATIC) > 0) {
            ModelFacade.setOwnerScope(feature, ModelFacade.CLASSIFIER_SCOPE);
        }
        else {
            ModelFacade.setOwnerScope(feature, ModelFacade.INSTANCE_SCOPE);
        }
    }

    /**
       Set the target scope for an association end.

       @param mAssociationEnd The end.
       @param modifiers A sequence of modifiers which may contain
       'static'.
    */
    private void setTargetScope(Object mAssociationEnd, short modifiers)
    {
        if ((modifiers & JavaRecognizer.ACC_STATIC) > 0) {
            ModelFacade.setTargetScope(mAssociationEnd,
				       ModelFacade.CLASSIFIER_SCOPE);
        }
        else {
            ModelFacade.setTargetScope(mAssociationEnd,
				       ModelFacade.INSTANCE_SCOPE);
        }
    }

    /**
       Get the context for a classifier name that may or may not be
       fully qualified.

       @param name The classifier name.
    */
    private Context getContext(String name)
    {
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
     * @sTagName the name of the javadoc tag
     * @sTagData the contents of the javadoc tag
     */
    private void addJavadocTagContents (Object me,
					String sTagName,
					String sTagData) {
	if ((sTagName.equals ("invariant")) ||
	    (sTagName.equals ("pre-condition")) ||
	    (sTagName.equals ("post-condition"))) {

	    // add as OCL constraint
	    String sContext = OCLUtil.getContextString(me);
	    String name = sTagData.substring(0, sTagData.indexOf(':'));
	    String body = null;
	    if (sTagName.equals ("invariant")) {
		// add as invariant constraint Note that no checking
		// of constraint syntax is performed... BAD!
		body = sContext + " inv " + sTagData;
	    }
	    else if (sTagName.equals ("pre-condition")) {
		body = sContext + " pre " + sTagData;
	    }
	    else {
		body = sContext + " post " + sTagData;
	    }
	    Object bexpr =
		UmlFactory.getFactory().getDataTypes()
		.createBooleanExpression("OCL", body);
	    Object mc =
		UmlFactory.getFactory().getCore().buildConstraint(name, bexpr);
	    ModelFacade.addConstraint(me, mc);
	    if (ModelFacade.getNamespace(me) != null) {
		// Apparently namespace management is not supported
		// for all model elements. As this does not seem to
		// cause problems, I'll just leave it at that for the
		// moment...
		ModelFacade.addOwnedElement(ModelFacade.getNamespace(me), mc);
	    }
	}
	else {
	    ModelFacade.setValueOfTag(getTaggedValue(me, sTagName), sTagData);
	}
    }

    /**
     * Add the javadocs as a tagged value 'documentation' to the model
     * element. All comment delimiters are removed prior to adding the
     * comment.
     *
     * Added 2001-10-05 STEFFEN ZSCHALER.
     *
     * @param me the model element to which to add the documentation
     * @param sJavaDocs the documentation comment to add ("" or null
     * if no java docs)
     */
    private void addDocumentationTag (Object modelElement,
				      String sJavaDocs) {
	if ((sJavaDocs != null) &&
	    (sJavaDocs.trim().length() >= 5 )) {
	    cat.debug ("Modeller.addDocumentationTag: sJavaDocs = \""
		       + sJavaDocs
		       + "\"");

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
		    if (!fHadAsterisk) {
			// forget every white space before the first asterisk
			nStartPos++;
			break;
		    }

		default:
		    // normal comment text or standard tag

		    // check ahead for tag
		    int j = nStartPos;

		    while ((j < sJavaDocs.length()) &&
			   ((sJavaDocs.charAt (j) == ' ') ||
			    (sJavaDocs.charAt (j) == '\t'))) {
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
			    }
			    else {
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

			    cat.debug (
				       "Modeller.addDocumentationTag (starting tag): " +
				       "current tag name: " + sCurrentTagName);

			    int nTemp1 = sJavaDocs.indexOf ('\n', ++nTemp);
			    if (nTemp1 == -1) {
				nTemp1 = sJavaDocs.length();
			    }
			    else {
				nTemp1++;
			    }

			    sCurrentTagData =
				sJavaDocs.substring (nTemp, nTemp1);
			    cat.debug (
				       "Modeller.addDocumentationTag (starting tag): " +
				       "current tag data: " + sCurrentTagData);

			    nStartPos = nTemp1;
			}
			else {
			    // continue standard tag or comment text
			    int nTemp = sJavaDocs.indexOf ('\n', nStartPos);
			    if (nTemp == -1) {
				nTemp = sJavaDocs.length();
			    }
			    else {
				nTemp++;
			    }

			    if (sCurrentTagName != null) {
				sbPureDocs.append(sJavaDocs.substring(nStartPos,
								      nTemp));
				cat.debug (
					   "Modeller.addDocumentationTag (continuing tag): nTemp = " 
					   + nTemp
					   + ", nStartPos = " 
					   + nStartPos);
				sCurrentTagData +=
				    " " +
				    sJavaDocs.substring (nStartPos, nTemp);
				cat.debug (
					   "Modeller.addDocumentationTag (continuing tag): tag data = " +
					   sCurrentTagData);
			    }
			    else {
				cat.debug ("Modeller.addDocumentationTag: nTemp = "
					   + nTemp
					   + ", nStartPos = "
					   + nStartPos);
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
	    cat.debug(sJavaDocs);
	    /*
	     * After this, we have the documentation text, but
	     * unfortunately, there's still a trailing '/' left. If
	     * this is even the only thing on it's line, we want to
	     * remove the complete line, otherwise we remove just the
	     * '/'.
	     *
	     * This will be either at the end of the actual comment
	     * text or at the end of the last tag.
	     */

	    if (sCurrentTagName != null) {
		// handle last tag...
		sCurrentTagData = 
		    sCurrentTagData.substring(0,
					      sCurrentTagData.lastIndexOf('/') - 1
					      );

		if ( sCurrentTagData.length() > 0 &&
		     sCurrentTagData.charAt (sCurrentTagData.length() - 1) == '\n') {
		    sCurrentTagData = 
			sCurrentTagData.substring(0,
						  sCurrentTagData.length() - 1
						  );
		}

		// store tag
		addJavadocTagContents (modelElement, sCurrentTagName,
				       sCurrentTagData);
	    }
	    else {
		sJavaDocs =
		    sJavaDocs.substring (0, sJavaDocs.lastIndexOf ('/') - 1);

		if (sJavaDocs.length() > 0) {
		    if (sJavaDocs.charAt (sJavaDocs.length() - 1) == '\n') {
			sJavaDocs =
			    sJavaDocs.substring (0, sJavaDocs.length() - 1);
		    }
		}
	    }
	    if (sJavaDocs.endsWith("/"))
		sJavaDocs = sJavaDocs.substring(0, sJavaDocs.length() - 1);

	    // Do special things:

	    // Now store documentation text
	    ModelFacade.setValueOfTag(getTaggedValue(modelElement,
						     "documentation"),
				      sJavaDocs);

	    // If there is a tagged value named stereotype, make it a real
	    // stereotype
	    String stereo =
		ModelFacade.getValueOfTag(ModelFacade.getTaggedValue(modelElement, "stereotype"));
	    if (stereo != null && stereo.length() > 0) {
		ModelFacade.setStereotype(modelElement, getStereotype(stereo));
	    }
	}
    }

    public void addCall(String method, String obj) {
	if (obj.equals(""))
	    cat.debug("Add call to method " + method);
	else
	    cat.debug("Add call to method " + method + " in " + obj);
    }

}
