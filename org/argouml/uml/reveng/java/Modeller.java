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

import org.argouml.ui.*;
import org.apache.log4j.Category;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.ModelFacade;
import org.argouml.model.uml.UmlFactory;
import org.argouml.model.uml.UmlModelEventPump;
import org.argouml.model.uml.foundation.core.CoreFactory;
import org.argouml.model.uml.foundation.core.CoreHelper;
import org.argouml.ocl.OCLUtil;
import org.argouml.uml.*;
import org.argouml.uml.reveng.*;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;

/** This class receives calls from the parser and builds the UML
 * model. Currently needs work. Class diagrams
 * are stacked on top of each other requiring
 * user to manually rearrange artifacts in diagram.
 *
 * This problem has been noted in Poseidon SE as
 * well.
 *
 *The arrange layout/automatic should be automatically invoked.
 */
public class Modeller
{
        protected static Category cat = Category.getInstance(Modeller.class);
    /** Current working model. */
    private Object model;

    private DiagramInterface _diagram;

    /** The package which the currentClassifier belongs to. */
    private Object currentPackage;

    /** Keeps the data that varies during parsing. */
    private ParseState parseState;

    /** Stack up the state when descending inner classes. */
    private Stack parseStateStack;

    /** Only attributes will be generated. */
    private boolean noAssociations;

    /** Arrays will be modelled as unique datatypes. */
    private boolean arraysAsDatatype;

    /** Pointer to exception caught during parsing, null if no
        exception caught.*/
    private Exception _exception;

    /**
       Create a new modeller.

       @param model The model to work with.
    */
    public Modeller(Object model,
		    DiagramInterface diagram,
		    boolean noAssociations,
		    boolean arraysAsDatatype)
    {
	this.model = model;
	this.noAssociations = noAssociations;
	this.arraysAsDatatype = arraysAsDatatype;
	currentPackage = this.model;
	parseState = new ParseState(this.model, getPackage("java.lang"));
	parseStateStack = new Stack();
	_diagram = diagram;
	_exception = null;
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
       Get the exception that was caught during parsing.

       @return Exception, null if none was thrown.
    */
    public Exception getException() {
	return _exception;
    }

    /**
       Called from the parser when a package clause is found.

       @param name The name of the package.
    */
    public void addPackage(String name)
    {
	// Add a package figure for this package to the owners class diagram, if it's not
	// in the diagram yet. I do this for all the class diagrams up to the top level,
	// since I need diagrams for all the packages.
	String ownerPackageName, currentName = name;
	while( ! "".equals(ownerPackageName = getPackageName(currentName))) {
	    if(getDiagram() != null) getDiagram().selectClassDiagram( getPackage(ownerPackageName), ownerPackageName);
	    if(getDiagram() != null) getDiagram().addPackage(getPackage(currentName));
	    currentName = ownerPackageName;
	}

	// Find or create a MPackage NSUML object for this package.
	Object mPackage = getPackage(name);

	// Set the current package for the following source code.
	currentPackage = mPackage;
	parseState.addPackageContext(mPackage);

	// Select the class diagram for the current package,
	// so all succeeding objects are added to it.
	if(getDiagram() != null) getDiagram().selectClassDiagram(mPackage, name);
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

	if(classifierName.equals("*")) {
	    parseState.addPackageContext(mPackage);
	}
	else {
	    try {
		Object mClassifier = (new PackageContext(null, mPackage)).get(classifierName);
		parseState.addClassifierContext(mClassifier);
	    }
	    catch(ClassifierNotFoundException e) {
		// Postpone the problems to the actual class parsing.
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
        Object mClass = addClassifier(UmlFactory.getFactory().getCore().createClass(), name, modifiers, javadoc);

        ModelFacade.setAbstract(mClass,(modifiers & JavaRecognizer.ACC_ABSTRACT) > 0);
        ModelFacade.setLeaf(mClass,(modifiers & JavaRecognizer.ACC_FINAL) > 0);
        ModelFacade.setRoot(mClass,false);

        try {
            if(superclassName != null) {
                Object parentClass = getContext(superclassName).get(getClassifierName(superclassName));
                getGeneralization(currentPackage, parentClass, mClass);
            }

            for(Iterator i = interfaces.iterator(); i.hasNext(); ) {
                String interfaceName = (String)i.next();
                Object mInterface = getContext(interfaceName).getInterface(getClassifierName(interfaceName));
                Object mAbstraction = getAbstraction(currentPackage, mInterface, mClass);
                if(ModelFacade.getSuppliers(mAbstraction).size() == 0) {
                    ModelFacade.addSupplier(mAbstraction, mInterface);
                    ModelFacade.addClient(mAbstraction, mClass);
                }
                ModelFacade.setNamespace(mAbstraction,currentPackage);
                ModelFacade.setStereotype(mAbstraction,getStereotype("realize"));
            }
        }
        catch(ClassifierNotFoundException e) {
            _exception = e;
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
            if(ModelFacade.isAInterface(mClassifier)) {
                interfaces.add(type);
            }
            addClass(name, (short)0, ModelFacade.isAClass(mClassifier) ? type : null, interfaces, "");
        }
        catch(ClassifierNotFoundException e) {
            _exception = e;
            // Must add it anyway, or the class poping will mismatch.
            addClass(name, (short)0, null, new Vector(), "");
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
        Object mInterface = addClassifier(UmlFactory.getFactory().getCore().createInterface(), name, modifiers, javadoc);
        try {
            for(Iterator i = interfaces.iterator(); i.hasNext(); ) {
                String interfaceName = (String)i.next();
                Object parentInterface = getContext(interfaceName).getInterface(getClassifierName(interfaceName));
                getGeneralization(currentPackage, parentInterface, mInterface);
            }
        }
        catch(ClassifierNotFoundException e) {
            _exception = e;
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

	if(parseState.getClassifier() != null) {
	    // Inner classes
	    mClassifier = ModelFacade.lookupIn(parseState.getClassifier(),name);
	    mNamespace = parseState.getClassifier();
	}
	else {
	    parseState.outerClassifier();
	    mClassifier = ModelFacade.lookupIn(currentPackage,name);
	    mNamespace = currentPackage;
	}
	if(mClassifier == null) {
	    mClassifier = newClassifier;
	    ModelFacade.setName(mClassifier,name);
	    ModelFacade.setNamespace(mClassifier,mNamespace);
	}
	else {
	    cleanModelElement(mClassifier);
	}
	parseState.innerClassifier(mClassifier);
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
        // add the current classifier to the diagram.
        Object classifier = parseState.getClassifier();
        if(ModelFacade.isAInterface(classifier)) {
            if (getDiagram() != null) getDiagram().addInterface(classifier);
        } else {
            if(ModelFacade.isAClass(classifier)) {
                if (getDiagram() != null) getDiagram().addClass(classifier);
            }
        }

        // Remove operations and attributes not in source
        parseState.removeObsoleteFeatures();

        // Remove inner classes not in source
        parseState.removeObsoleteInnerClasses();

        parseState = (ParseState)parseStateStack.pop();
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

      ModelFacade.setAbstract(mOperation,(modifiers & JavaRecognizer.ACC_ABSTRACT) > 0);
      ModelFacade.setLeaf(mOperation,(modifiers & JavaRecognizer.ACC_FINAL) > 0);
      ModelFacade.setRoot(mOperation,false);
      setOwnerScope(mOperation, modifiers);
      setVisibility(mOperation, modifiers);
      if((modifiers & JavaRecognizer.ACC_SYNCHRONIZED) > 0) {
          ModelFacade.setConcurrency(mOperation, ModelFacade.GUARDED);
      } else if(ModelFacade.getConcurrency(mOperation) == ModelFacade.GUARDED) {
          ModelFacade.setConcurrency(mOperation, ModelFacade.SEQUENTIAL);
      }

      for(Iterator i = ModelFacade.getParameters(mOperation); i.hasNext(); ) {
          ModelFacade.removeParameter(mOperation, i.next());
      }

      try {
          Object mParameter;
          String typeName;
          Object mPackage;
          Object mClassifier;

          if(returnType == null) {
              // Constructor
              ModelFacade.setStereotype(mOperation,getStereotype("create"));
          }
          else {
              mParameter = UmlFactory.getFactory().getCore().buildParameter(mOperation);
              ModelFacade.setName(mParameter, "return");
              ModelFacade.setKindToReturn(mParameter);
              mClassifier = getContext(returnType).get(getClassifierName(returnType));
              ModelFacade.setType(mParameter, mClassifier);
          }

          for(Iterator i = parameters.iterator(); i.hasNext(); ) {
              Vector parameter = (Vector)i.next();
              mParameter = UmlFactory.getFactory().getCore().buildParameter(mOperation);
              ModelFacade.setName(mParameter, (String)parameter.elementAt(2));
              ModelFacade.setKindToIn(mParameter);
              typeName = (String)parameter.elementAt(1);
              mClassifier = getContext(typeName).get(getClassifierName(typeName));
              ModelFacade.setType(mParameter, mClassifier);
          }
      }
      catch(ClassifierNotFoundException e) {
          _exception = e;
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
        ModelFacade.setBody(method, UmlFactory.getFactory().getDataTypes().createProcedureExpression("Java", body));
        ModelFacade.addMethod(op,method);  // Add the method to it's specification.
        // Add this method as a feature to the classifier that owns the operation.
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
	    MMultiplicity multiplicity = MMultiplicity.M1_1;

      if(!arraysAsDatatype && typeSpec.indexOf('[') != -1) {
          typeSpec = typeSpec.substring(0, typeSpec.indexOf('['));
          multiplicity = MMultiplicity.M1_N;
      }

      try {
          MClassifier mClassifier =
        (MClassifier)(getContext(typeSpec).get(getClassifierName(typeSpec)));
          MAttribute mAttribute =
        (MAttribute)getAttribute(name, initializer, mClassifier);

          if(mAttribute != null) {
            parseState.feature(mAttribute);

            setOwnerScope(mAttribute, modifiers);
            setVisibility(mAttribute, modifiers);
            mAttribute.setMultiplicity(multiplicity);

            mAttribute.setType(mClassifier);

            // Set the initial value for the attribute.
            if(initializer != null) {
                mAttribute.setInitialValue(UmlFactory.getFactory().getDataTypes().createExpression("Java",
                             initializer));
            }

            if((modifiers & JavaRecognizer.ACC_FINAL) > 0) {
                mAttribute.setChangeability(MChangeableKind.FROZEN);
            }
            else if(mAttribute.getChangeability() ==
              MChangeableKind.FROZEN ||
              mAttribute.getChangeability() == null) {
                mAttribute.setChangeability(MChangeableKind.CHANGEABLE);
            }

            /*
             * Changed 2001-10-05 STEFFEN ZSCHALER
             *
             * Was (added space below!):
             *
            if((javadoc==null) || "".equals(javadoc)) {
                javadoc = "/** * /";
            }
            getTaggedValue(mAttribute, "documentation").setValue(javadoc);
             *
             * Moved to end of method 2001-11-05 to allow addDocumentationTag to access
             * as much information as possible
             */

            addDocumentationTag (mAttribute, javadoc);
          }
          else {
            MAssociationEnd mAssociationEnd =
                (MAssociationEnd)getAssociationEnd(name, mClassifier);

            setTargetScope(mAssociationEnd, modifiers);
            setVisibility(mAssociationEnd, modifiers);
            mAssociationEnd.setMultiplicity(multiplicity);

            mAssociationEnd.setType(mClassifier);
            mAssociationEnd.setName(name);

            if((modifiers & JavaRecognizer.ACC_FINAL) > 0) {
                mAssociationEnd.setChangeability(MChangeableKind.FROZEN);
            }
            else if(mAssociationEnd.getChangeability() ==
              MChangeableKind.FROZEN ||
              mAssociationEnd.getChangeability() == null) {
                mAssociationEnd
              .setChangeability(MChangeableKind.CHANGEABLE);
            }

            mAssociationEnd.setNavigable(true);

            /*
             * Changed 2001-10-05 STEFFEN ZSCHALER
             *
             * Was (added space below!):
             *
            if((javadoc==null) || "".equals(javadoc)) {
                javadoc = "/** * /";
            }
            getTaggedValue(mAssociationEnd, "documentation").setValue(javadoc);
             *
             * Moved to end of method 2001-11-05 to allow addDocumentationTag to access
             * as much information as possible
             */

            addDocumentationTag (mAssociationEnd, javadoc);
          }
      }
      catch(ClassifierNotFoundException e) {
          _exception = e;
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
        String name = ModelFacade.getName(parent) + "<-" + ModelFacade.getName(child);
        Object mGeneralization = null;
        mGeneralization = ModelFacade.getGeneralization(child, parent);
        if(mGeneralization == null) {
            mGeneralization = CoreFactory.getFactory().buildGeneralization(child, parent, name);
        }
        if(mGeneralization != null) {
            ModelFacade.setNamespace(mGeneralization,mPackage);
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
        String name = ModelFacade.getName(parent) + "<-" + ModelFacade.getName(child);
        Object mAbstraction = null;
        for(Iterator i = ModelFacade.getClientDependencies(child); i.hasNext(); ) {
            mAbstraction = i.next();
            Collection c = ModelFacade.getSuppliers(mAbstraction);
            if(c == null || c.size() == 0) {
                ModelFacade.removeClientDependency(child,mAbstraction);
            }
            else {
                if(parent != c.toArray()[0]) {
                    mAbstraction = null;
                }
                else {
                    break;
                }
            }
        }

        if(mAbstraction == null) {
            mAbstraction = UmlFactory.getFactory().getCore().buildAbstraction(name);
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
	if(mPackage == null) {
	    mPackage = UmlFactory.getFactory().getModelManagement().buildPackage(getRelativePackageName(name), name);
	    ModelFacade.setNamespace(mPackage, model);

	    // Find the owner for this package.
	    if("".equals(getPackageName(name))) {
		ModelFacade.addOwnedElement(model, mPackage);
	    }
	    else {
		ModelFacade.addOwnedElement(getPackage(getPackageName(name)), mPackage);
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
     * @param name The fully qualified package name of the package we are searching for.
     * @return The found package or null, if it is not in the model.
     */
    private Object searchPackageInModel(String name) {
	if("".equals(getPackageName(name))) {
	    return ModelFacade.lookupIn(model,name);
	} else {
	    Object owner = searchPackageInModel(getPackageName(name));
	    return owner == null ? null : ModelFacade.lookupIn(owner,getRelativePackageName(name));
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
        if(mOperation == null) {
            mOperation = UmlFactory.getFactory().getCore().buildOperation(parseState.getClassifier(),name);
            Iterator it2 = ProjectManager.getManager().getCurrentProject().findFigsForMember(parseState.getClassifier()).iterator();
            while (it2.hasNext()) {
                Object listener = it2.next();
                // UmlModelEventPump.getPump().removeModelEventListener(listener, mOperation);
                UmlModelEventPump.getPump().addModelEventListener(listener, mOperation);
                // UmlModelEventPump.getPump().removeModelEventListener(listener, mOperation.getParameter(0));
                UmlModelEventPump.getPump().addModelEventListener(listener, ModelFacade.getParameter(mOperation,0));
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
        if(mMethod == null) {
            mMethod = UmlFactory.getFactory().getCore().buildMethod(name);
            ModelFacade.addFeature(parseState.getClassifier(),mMethod);
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
        if(mAttribute == null &&
           (initializer != null ||
            (noAssociations &&
             getAssociationEnd(name, mClassifier) == null))) {
            mAttribute = UmlFactory.getFactory().getCore().buildAttribute(name);
            ModelFacade.addFeature(parseState.getClassifier(),mAttribute);
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
            if(name.equals(ModelFacade.getName(ae))) {
                mAssociationEnd = ae;
            }
        }
        if (mAssociationEnd == null && !noAssociations) {
            Object mAssociation = CoreFactory.getFactory()
                                             .buildAssociation(mClassifier,
                                                               true,
                                                               parseState.getClassifier(),
                                                               false,
                                                               ModelFacade.getName(parseState.getClassifier())+" -> "+ModelFacade.getName(mClassifier));
            mAssociationEnd = ModelFacade.getAssociationEnd(mClassifier, mAssociation);
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
	Object mStereotype = ModelFacade.lookupIn(model,name);

	if(mStereotype == null || !ModelFacade.isAStereotype(mStereotype)) {
	    mStereotype = UmlFactory.getFactory().getExtensionMechanisms().buildStereotype(name,model);
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
        Object tv = ModelFacade.getTaggedValue(element,name);
        if (tv == null) {
            tv = UmlFactory.getFactory().getExtensionMechanisms().buildTaggedValue(name,"");
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
        for(Iterator i = ModelFacade.getTaggedValues(element); i.hasNext(); ) {
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
	if(lastDot == -1) {
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
    private void setVisibility(Object element,
                               short modifiers)
    {
	if((modifiers & JavaRecognizer.ACC_STATIC) > 0) {
            ModelFacade.setTaggedValue(element,"src_modifiers", "static");
	}
	if((modifiers & JavaRecognizer.ACC_PRIVATE) > 0) {
	    ModelFacade.setVisibility(element,ModelFacade.ACC_PRIVATE);
	}
	else if((modifiers & JavaRecognizer.ACC_PROTECTED) > 0) {
	    ModelFacade.setVisibility(element,ModelFacade.ACC_PROTECTED);
	}
	else if((modifiers & JavaRecognizer.ACC_PUBLIC) > 0) {
	    ModelFacade.setVisibility(element,ModelFacade.ACC_PUBLIC);
	} else {
            ModelFacade.setTaggedValue(element,"src_visibility", "default");
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
        if((modifiers & JavaRecognizer.ACC_STATIC) > 0) {
            ModelFacade.setOwnerScope(feature,ModelFacade.CLASSIFIER);
        }
        else {
            ModelFacade.setOwnerScope(feature,ModelFacade.INSTANCE);
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
        if((modifiers & JavaRecognizer.ACC_STATIC) > 0) {
            ModelFacade.setTargetScope(mAssociationEnd,ModelFacade.CLASSIFIER);
        }
        else {
            ModelFacade.setTargetScope(mAssociationEnd,ModelFacade.INSTANCE);
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
	if(!"".equals(packageName)) {
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

      MConstraint mc = UmlFactory.getFactory().getCore().createConstraint();
      mc.setName (sTagData.substring (0, sTagData.indexOf (':')));

      if (sTagName.equals ("invariant")) {
        // add as invariant constraint
        // Note that no checking of constraint syntax is performed... BAD!
        mc.setBody (
            UmlFactory.getFactory().getDataTypes().createBooleanExpression (
              "OCL",
              sContext +
              " inv " + sTagData
            )
          );
      }
      else if (sTagName.equals ("pre-condition")) {
        mc.setBody (
            UmlFactory.getFactory().getDataTypes().createBooleanExpression (
              "OCL",
              sContext +
              " pre " + sTagData
            )
          );
      }
      else {
        mc.setBody (
            UmlFactory.getFactory().getDataTypes().createBooleanExpression (
              "OCL",
              sContext +
              " post " + sTagData
            )
          );
      }

      ModelFacade.addConstraint(me,mc);
      if (ModelFacade.getNamespace(me) != null) {
        // Apparently namespace management is not supported for all model
        // elements. As this does not seem to cause problems, I'll just
        // leave it at that for the moment...
        ModelFacade.addOwnedElement(ModelFacade.getNamespace(me),mc);
      }
    }
    else {
      ModelFacade.setValueOfTag(getTaggedValue(me,sTagName),sTagData);
    }
  }

  /**
   * Add the javadocs as a tagged value 'documentation' to the model element. All
   * comment delimiters are removed prior to adding the comment.
   *
   * Added 2001-10-05 STEFFEN ZSCHALER.
   *
   * @param me the model element to which to add the documentation
   * @param sJavaDocs the documentation comment to add ("" or null if no java docs)
   */
  private void addDocumentationTag (Object modelElement,
                                    String sJavaDocs) {
    if ((sJavaDocs != null) &&
        (sJavaDocs.trim().length() >=5 )) {
      cat.debug ("Modeller.addDocumentationTag: sJavaDocs = \"" + sJavaDocs + "\"");

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
            if (! fHadAsterisk) {
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
                sbPureDocs.append (sJavaDocs.substring (j, sJavaDocs.indexOf('\n',j) + 1));  // afl 05/02/03 fix Issue 1511
                // start standard tag
                // potentially add current tag to set of tagged values...
                if (sCurrentTagName != null) {
                  addJavadocTagContents (modelElement, sCurrentTagName, sCurrentTagData);
                }

                // open new tag

                int nTemp = sJavaDocs.indexOf (' ', j + 1);
                if (nTemp == -1) {
                  nTemp = sJavaDocs.length() - 1;
                }
                sCurrentTagName = sJavaDocs.substring (
                    j + 1,
                    nTemp
                  );

                cat.debug (
                    "Modeller.addDocumentationTag (starting tag): " +
                    "current tag name: " + sCurrentTagName
                );

                int nTemp1 = sJavaDocs.indexOf ('\n', ++nTemp);
                if (nTemp1 == -1) {
                  nTemp1 = sJavaDocs.length();
                }
                else {
                  nTemp1++;
                }

                sCurrentTagData = sJavaDocs.substring (nTemp, nTemp1);
                cat.debug (
                    "Modeller.addDocumentationTag (starting tag): "+
                    "current tag data: " + sCurrentTagData
                  );

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
                  sbPureDocs.append (sJavaDocs.substring (nStartPos, nTemp));  // afl 05/02/03 fixed Issue 1511
                  cat.debug (
                      "Modeller.addDocumentationTag (continuing tag): nTemp = " +
                      nTemp + ", nStartPos = " + nStartPos
                    );
                  sCurrentTagData +=
                      " " +
                      sJavaDocs.substring (nStartPos, nTemp);
                  cat.debug (
                     "Modeller.addDocumentationTag (continuing tag): tag data = " +
                      sCurrentTagData);
                }
                else {
                  cat.debug ("Modeller.addDocumentationTag: nTemp = " + nTemp + ", nStartPos = " + nStartPos);
                  sbPureDocs.append (sJavaDocs.substring (nStartPos, nTemp));
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
       * After this, we have the documentation text, but unfortunately, there's
       * still a trailing '/' left. If this is even the only thing on it's line,
       * we want to remove the complete line, otherwise we remove just the '/'.
       *
       * This will be either at the end of the actual comment text or at the end
       * of the last tag.
       */

      if (sCurrentTagName != null) {
        // handle last tag...
        sCurrentTagData = sCurrentTagData.substring (
            0,
            sCurrentTagData.lastIndexOf ('/') - 1
          );

        if ( sCurrentTagData.length() > 0 &&
             sCurrentTagData.charAt (sCurrentTagData.length() - 1) == '\n') {
          sCurrentTagData = sCurrentTagData.substring (
              0,
              sCurrentTagData.length() - 1
            );
        }

        // store tag
        addJavadocTagContents (modelElement, sCurrentTagName, sCurrentTagData);
      }
      else {
        sJavaDocs = sJavaDocs.substring (0, sJavaDocs.lastIndexOf ('/') - 1);

        if (sJavaDocs.length() > 0) {
          if (sJavaDocs.charAt (sJavaDocs.length() - 1) == '\n') {
            sJavaDocs = sJavaDocs.substring (0, sJavaDocs.length() - 1);
          }
        }
      }
      if (sJavaDocs.endsWith("/")) sJavaDocs = sJavaDocs.substring(0, sJavaDocs.length() - 1);

      // Do special things:

      // Now store documentation text
      ModelFacade.setValueOfTag(getTaggedValue(modelElement,"documentation"),sJavaDocs);

      // If there is a tagged value named stereotype, make it a real
      // stereotype
      String stereo = ModelFacade.getValueOfTag(ModelFacade.getTaggedValue(modelElement,"stereotype"));
      if (stereo != null && stereo.length() > 0) {
	  ModelFacade.setStereotype(modelElement,getStereotype(stereo));
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
