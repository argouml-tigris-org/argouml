// $Id$

/*
  JavaRE - Code generation and reverse engineering for UML and Java
  Copyright (C) 2000 Marcus Andersson andersson@users.sourceforge.net
  
  This library is free software; you can redistribute it and/or modify
  it under the terms of the GNU Lesser General Public License as
  published by the Free Software Foundation; either version 2.1 of the
  License, or (at your option) any later version.

  This library is distributed in the hope that it will be useful, but
  WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
  Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General Public
  License along with this library; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
  USA 

*/

package org.argouml.uml.reveng.java;

import java.util.*;
import org.argouml.uml.reveng.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;
import ru.novosoft.uml.model_management.*;
import ru.novosoft.uml.foundation.data_types.*;

/**
   This class receives calls from the parser and builds the UML
   model.
*/
public class Modeller 
{ 
    /** Current working model. */
    private MModel model;

    private DiagramInterface _diagram;
    
    /** The package which the currentClassifier belongs to. */
    private MPackage currentPackage;

    /** Keeps the data that varies during parsing. */
    private ParseState parseState;

    /** Stack up the state when descending inner classes. */
    private Stack parseStateStack;

    /**
       Create a new modeller.
       
       @param model The model to work with.
    */
    public Modeller(MModel model, DiagramInterface diagram)
    {
	this.model = model;
	currentPackage = model;
	parseState = new ParseState(model, getPackage("java.lang"));
	parseStateStack = new Stack();
	_diagram = diagram;
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
       Called from the parser when a package clause is found.
       
       @param name The name of the package.
    */
    public void addPackage(String name)
    {
	MPackage mPackage = getPackage(name);
	currentPackage = mPackage;
	parseState.addPackageContext(mPackage);
    }

    /**
       Called from the parser when an import clause is found.

       @param name The name of the import. Can end with a '*'.
    */
    public void addImport(String name)
    {
	String packageName = getPackageName(name);
	String classifierName = getClassifierName(name);
	MPackage mPackage = getPackage(packageName);
	
	if(classifierName.equals("*")) {
	    parseState.addPackageContext(mPackage);
	}
	else {
	    MClassifier mClassifier = 
		(new PackageContext(null, mPackage)).get(classifierName);
	    parseState.addClassifierContext(mClassifier);
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
	MClass mClass = (MClass)addClassifier(new MClassImpl(),
					      name, modifiers, javadoc);

	mClass.setAbstract((modifiers & JavaRecognizer.ACC_ABSTRACT) > 0);
	mClass.setLeaf((modifiers & JavaRecognizer.ACC_FINAL) > 0);
	mClass.setRoot(false);

	if(superclassName != null) {
	    MPackage mPackage = getPackage(getPackageName(superclassName));
	    MClass parentClass = (MClass)
		(new PackageContext(parseState.getContext(), 
					mPackage)).
		get(getClassifierName(superclassName));

	    MGeneralization mGeneralization = 
		getGeneralization(currentPackage, parentClass, mClass);
	    mGeneralization.setParent(parentClass);
	    mGeneralization.setChild(mClass);
	    mGeneralization.setNamespace(currentPackage);
	}

	for(Iterator i = interfaces.iterator(); i.hasNext(); ) {
	    String interfaceName = (String)i.next();
	    MPackage mPackage = getPackage(getPackageName(interfaceName));
	    MInterface mInterface =
		(new PackageContext(parseState.getContext(), 
					mPackage)).
		getInterface(getClassifierName(interfaceName));

	    MAbstraction mAbstraction = 
		getAbstraction(currentPackage, mInterface, mClass);
	    if(mAbstraction.getSuppliers().size() == 0) {
		mAbstraction.addSupplier(mInterface);
		mAbstraction.addClient(mClass);
	    }
	    mAbstraction.setNamespace(currentPackage);
	    mAbstraction.setStereotype(getStereotype("realize"));
	}	    
    }

    /**
       Called from the parser when an anonymous inner class is found.

       @param type The type of this anonymous class.
    */
    public void addAnonymousClass(String type)
    {
	String name = parseState.anonymousClass();
	MPackage mPackage = getPackage(getPackageName(type));
	MClassifier mClassifier =
	    (new PackageContext(parseState.getContext(), mPackage)).
	    get(getClassifierName(type));
	Vector superclass = new Vector();
	Vector interfaces = new Vector();
	if(mClassifier instanceof MInterface) {
	    interfaces.add(type);
	}
	addClass(name, (short)0, mClassifier instanceof MClass ? type : null, interfaces, "");
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
	MInterface mInterface = (MInterface)addClassifier(new MInterfaceImpl(),
							  name,
							  modifiers,
							  javadoc);

	for(Iterator i = interfaces.iterator(); i.hasNext(); ) {
	    String interfaceName = (String)i.next();
	    MPackage mPackage = getPackage(getPackageName(interfaceName));
	    MInterface parentInterface =
		(new PackageContext(parseState.getContext(), 
					mPackage)).
		getInterface(getClassifierName(interfaceName));

	    MGeneralization mGeneralization = 
		getGeneralization(currentPackage, parentInterface, mInterface);
	    mGeneralization.setParent(parentInterface);
	    mGeneralization.setChild(mInterface);
	    mGeneralization.setNamespace(currentPackage);
	}
    }

    /**
       Common code used by addClass and addInterface.
       
       @param newClassifier Supply one if none is found in the model.
       @param name Name of the classifier.
       @param modifiers String of modifiers.
       @param javadoc The javadoc comment. null or "" if no comment available.
       @returns The newly created/found classifier.
     */
    private MClassifier addClassifier(MClassifier newClassifier,
				      String name,
                                      short modifiers,
                                      String javadoc)
    {
	MClassifier mClassifier;
	MNamespace mNamespace;

	if(parseState.getClassifier() != null) {
	    // Inner classes
	    mClassifier = (MClassifier)parseState.getClassifier().lookup(name);
	    mNamespace = parseState.getClassifier();
	}
	else {
	    parseState.outerClassifier();
	    mClassifier = (MClassifier)currentPackage.lookup(name);
	    mNamespace = currentPackage;
	}
	if(mClassifier == null) {
	    mClassifier = newClassifier;
	    mClassifier.setName(name);
	    mClassifier.setNamespace(mNamespace);
	}
	parseState.innerClassifier(mClassifier);
	parseStateStack.push(parseState);
	parseState = new ParseState(parseState, mClassifier, currentPackage);

	setVisibility(mClassifier, modifiers);

	if((javadoc == null) || "".equals(javadoc)) {
	    javadoc = "/** */";
	}
	getTaggedValue(mClassifier, "javadocs").setValue(javadoc);

	return mClassifier;
    }	

    /**
       Called from the parser when a classifier is completely parsed.
    */
    public void popClassifier()
    {
	// add the current classifier to the diagram.
	MClassifier classifier = parseState.getClassifier();
	if(classifier instanceof MInterface) {
	    getDiagram().addInterface((MInterface)classifier);
	} else {
	    if(classifier instanceof MClass) {
		getDiagram().addClass((MClass)classifier);
	    }
	}
	
	// Remove operations and attributes not in source
	parseState.removeObsoleteFeatures();

	// Remove inner classes not in source
	parseState.removeObsoleteInnerClasses();

	parseState = (ParseState)parseStateStack.pop();
    }

    /**
       Called from the parser when an operation is found.

       @param modifiers A sequence of operation modifiers.
       @param returnType The return type of the operation.
       @param name The name of the operation as a string
       @param parameters A number of vectors, each representing a 
                         parameter.
       @param javadoc The javadoc comment. null or "" if no comment available.
    */
    public void addOperation(short modifiers,
                             String returnType,
			     String name,
                             Vector parameters,
                             String javadoc)
    {
	MOperation mOperation = getOperation(name);
	parseState.feature(mOperation);

	if((javadoc == null) || "".equals(javadoc)) {
	    javadoc = "/** */";
	}
	getTaggedValue(mOperation, "javadocs").setValue(javadoc);

	mOperation.setAbstract((modifiers & JavaRecognizer.ACC_ABSTRACT) > 0);
	mOperation.setLeaf((modifiers & JavaRecognizer.ACC_FINAL) > 0);
	mOperation.setRoot(false);
	setScope(mOperation, modifiers);
	setVisibility(mOperation, modifiers);
	if((modifiers & JavaRecognizer.ACC_SYNCHRONIZED) > 0) {
	    mOperation.setConcurrency(MCallConcurrencyKind.GUARDED);
	} else if(mOperation.getConcurrency() == 
		  MCallConcurrencyKind.GUARDED) {
	    mOperation.setConcurrency(MCallConcurrencyKind.SEQUENTIAL);
	}	    


	for(Iterator i = mOperation.getParameters().iterator(); 
	    i.hasNext(); ) {
	    mOperation.removeParameter((MParameter)i.next());
	}
	
	MParameter mParameter;
	String typeName;
	MPackage mPackage;
	MClassifier mClassifier;

	if(returnType == null) {
	    // Constructor
	}
	else {
	    mParameter = new MParameterImpl();
	    mParameter.setName("return");
	    mParameter.setKind(MParameterDirectionKind.RETURN);
	    mOperation.addParameter(mParameter);
	    
	    typeName = returnType;
	    mPackage = getPackage(getPackageName(typeName));
	    mClassifier = 
		(new PackageContext(parseState.getContext(), 
					mPackage)).
		get(getClassifierName(typeName));
	    mParameter.setType(mClassifier);
	}

	for(Iterator i=parameters.iterator(); i.hasNext(); ) {
	    Vector parameter = (Vector)i.next();
	    mParameter = new MParameterImpl();
	    mParameter.setName((String)parameter.elementAt(2));
	    mParameter.setKind(MParameterDirectionKind.IN);
	    mOperation.addParameter(mParameter);
	    
	    typeName = (String)parameter.elementAt(1);
	    mPackage = getPackage(getPackageName(typeName));
	    mClassifier = 
		(new PackageContext(parseState.getContext(),
					mPackage)).
		get(getClassifierName(typeName));

	    mParameter.setType(mClassifier);
	}
    }

    /**
       Called from the parser when an attribute is found.
       
       @param modifiers A sequence of attribute modifiers.
       @param typeSpec The attribute's type.
       @param variable The name of the attribute.
       @param initializer The initial value of the attribute.
       @param javadoc The javadoc comment. null or "" if no comment available.
    */
    public void addAttribute(short modifiers,
                             String typeSpec,
                             String variable,
			     String initializer,
                             String javadoc)
    {
	MAttribute mAttribute = getAttribute((String)variable);
	parseState.feature(mAttribute);

	if((javadoc==null) || "".equals(javadoc)) {
	    javadoc = "/** */";
	}
	getTaggedValue(mAttribute, "javadocs").setValue(javadoc);

	setScope(mAttribute, modifiers);
	setVisibility(mAttribute, modifiers);
	mAttribute.setMultiplicity(MMultiplicity.M1_1);

	MPackage mPackage = getPackage(getPackageName(typeSpec));
	MClassifier mClassifier = 
	    (new PackageContext(parseState.getContext(), mPackage)).
	    get(getClassifierName(typeSpec));
	mAttribute.setType(mClassifier);

	// Set the initial value for the attribute.
	if(initializer != null) {
	    mAttribute.setInitialValue(new MExpression("Java", initializer));
	}

	if((modifiers & JavaRecognizer.ACC_FINAL) > 0) {
	    mAttribute.setChangeability(MChangeableKind.FROZEN);
	}
	else if(mAttribute.getChangeability() == MChangeableKind.FROZEN) {
	    mAttribute.setChangeability(MChangeableKind.CHANGEABLE);
	}
    }

    /**
       Find a generalization in the model. If it does not exist, a
       new generalization is created.
       
       @param mPackage Look in this package.
       @param parent The superclass.
       @param child The subclass.
       @returns The generalization found or created.
    */
    private MGeneralization getGeneralization(MPackage mPackage,
                                              MClassifier parent,
                                              MClassifier child)
    {
	String name = parent.getName() + "<-" + child.getName();
	MGeneralization mGeneralization = null;
	Collection generalizations = child.getGeneralizations();
	for(Iterator i = generalizations.iterator(); i.hasNext(); ) {
	    mGeneralization = (MGeneralization)i.next();
	    if(parent != mGeneralization.getParent()) {
		mGeneralization = null;
	    }
	}

	if(mGeneralization == null) {
	    mGeneralization = new MGeneralizationImpl();
	    mGeneralization.setName(name);
	}
	return mGeneralization;
    }

    /**
       Find an abstraction<<realize>> in the model. If it does not
       exist, a new abstraction is created.

       @param mPackage Look in this package.
       @param parent The superclass.
       @param child The subclass.
       @returns The abstraction found or created.
    */
    private MAbstraction getAbstraction(MPackage mPackage,
                                        MInterface parent,
                                        MClass child)
    {
	String name = parent.getName() + "<-" + child.getName();
	MAbstraction mAbstraction = null;
	Collection abstractions = child.getClientDependencies();
	for(Iterator i = abstractions.iterator(); i.hasNext(); ) {
	    mAbstraction = (MAbstraction)i.next();
	    if(parent != mAbstraction.getSuppliers().toArray()[0]) {
		mAbstraction = null;
	    }
	    else {
		break;
	    }
	}

	if(mAbstraction == null) {
	    mAbstraction = new MAbstractionImpl();
	    mAbstraction.setName(name);
	}
	return mAbstraction;
    }

    /**
       Find a package in the model. If it does not exist, a new
       package is created.

       @param name The name of the package.
       @returns The package found or created.
    */
    private MPackage getPackage(String name)
    {
	if(name.equals("")) {
	    // This is just a placeholder package. Nothing will be
	    // added to it.
	    return new MPackageImpl();
	}
	else {
	    MPackage mPackage = (MPackage)model.lookup(name);
	    if(mPackage == null) {
		mPackage = new MPackageImpl();
		mPackage.setName(name);
		mPackage.setNamespace(model);
	    }
	    if(mPackage.getUUID() == null) {
		mPackage.setUUID(name);
	    }		
	    return mPackage;
	}
    }

    /**
       Find an operation in the currentClassifier. If the operation is
       not found, a new is created.

       @param name The name of the operation.
       @returns The operation found or created.  
    */
    private MOperation getOperation(String name)
    {
	MOperation mOperation = (MOperation)parseState.getFeature(name);

	if(mOperation == null) {
	    mOperation = new MOperationImpl();
	    mOperation.setName(name);
	    parseState.getClassifier().addFeature(mOperation);
	}
	return mOperation;
    }

    /**
       Find an attribute in the currentClassifier. If the attribute is
       not found, a new is created.

       @param name The name of the attribute.
       @returns The attribute found or created.  
    */
    private MAttribute getAttribute(String name)
    {
	MAttribute mAttribute = (MAttribute)parseState.getFeature(name);

	if(mAttribute == null) {
	    mAttribute = new MAttributeImpl();
	    mAttribute.setName(name);
	    parseState.getClassifier().addFeature(mAttribute);
	}
	return mAttribute;
    }

    /**
       Get the stereotype with a specific name.
       
       @param name The name of the stereotype.
       @returns The stereotype.
    */
    private MStereotype getStereotype(String name)
    {
	MStereotype mStereotype = (MStereotype)model.lookup(name);
	
	if(mStereotype == null) {
	    mStereotype = new MStereotypeImpl();
	    mStereotype.setName(name);
	    mStereotype.setNamespace(model);
	}
	
	return mStereotype;
    }

    /**
       Return the tagged value with a specific tag.

       @param element The tagged value belongs to this.
       @param name The tag.
       @returns The found tag. A new is created if not found.
     */
    private MTaggedValue getTaggedValue(MModelElement element,
                                        String name)
    {
	for(Iterator i = element.getTaggedValues().iterator(); i.hasNext(); ) {
	    MTaggedValue tv = (MTaggedValue)i.next();
	    if(tv.getTag().equals(name)) {
		return tv;
	    }
	}
	MTaggedValue tv = new MTaggedValueImpl();
	tv.setModelElement(element);
	tv.setTag(name);
	return tv;
    }

    /**
       Get the package name from a fully specified classifier name.
       
       @param name A fully specified classifier name.
       @returns The package name.
    */
    private String getPackageName(String name)
    {
	int lastDot = name.lastIndexOf('.');
	if(lastDot == -1) {
	    return "";
	}
	else {
	    return name.substring(0, lastDot);
	}
    }

    /**
       Get the classifier name from a fully specified classifier name.
       
       @param name A fully specified classifier name.
       @returns The classifier name.
    */
    private String getClassifierName(String name)
    {
	int lastDot = name.lastIndexOf('.');
	if(lastDot == -1) {
	    return name;
	}
	else {
	    return name.substring(lastDot+1);
	}
    }

    /**
       Set the visibility for a model element.
       
       @param element The model element.
       @param modifiers A sequence of modifiers which may contain 
                        'private', 'protected' or 'public'.
    */
    private void setVisibility(MModelElement element,
                               short modifiers)
    {
	if((modifiers & JavaRecognizer.ACC_PRIVATE) > 0) {
	    element.setVisibility(MVisibilityKind.PRIVATE);
	}
	else if((modifiers & JavaRecognizer.ACC_PROTECTED) > 0) {
	    element.setVisibility(MVisibilityKind.PROTECTED);
	}
	else if((modifiers & JavaRecognizer.ACC_PUBLIC) > 0) {
	    element.setVisibility(MVisibilityKind.PUBLIC);
	}
    }

    /**
       Set the scope for a feature.
       
       @param feature The feature.
       @param modifiers A sequence of modifiers which may contain 
                        'static'.
    */
    private void setScope(MFeature feature,
                          short modifiers)
    {
	if((modifiers & JavaRecognizer.ACC_STATIC) > 0) {
	    feature.setOwnerScope(MScopeKind.CLASSIFIER);
	}
	else {
	    feature.setOwnerScope(MScopeKind.INSTANCE);
	}
    }
}
