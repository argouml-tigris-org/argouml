// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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

package org.argouml.language.cpp.reveng;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import org.apache.log4j.Logger;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.model.UUIDManager;

/**
 * Implementation of the <code>Modeler</code> interface. This facade
 * implements part and delegates the rest of the implementation that transforms
 * the parsed information from a C++ translation unit into UML model elements
 * and updating the model with it.
 *
 * @author euluis
 * @since 0.19.3
 */
public class ModelerImpl implements Modeler {
    /**
     * The context stack keeps track of the current parsing context in a stack
     * wise manner.
     */
    private Stack contextStack = new Stack();

    /**
     * The access specifier applicable to the parsing context. May be null if
     * not within a classifier.
     */
    private Object contextAccessSpecifier;

    /**
     * Counts the member declaration level.
     */
    private int memberDeclarationCount;

    /**
     * Counts the compound statement level.
     */
    private int compoundStatementCount;

    private boolean ignoreableFunctionDefinition;

    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(ModelerImpl.class);

    /**
     * @see org.argouml.language.cpp.reveng.Modeler#beginTranslationUnit()
     */
    public void beginTranslationUnit() {
        // for now we don't need to do anything here
    }

    /**
     * @see org.argouml.language.cpp.reveng.Modeler#endTranslationUnit()
     */
    public void endTranslationUnit() {
        // for now we don't need to do anything here
    }

    /**
     * @see org.argouml.language.cpp.reveng.Modeler#enterNamespaceScope(java.lang.String)
     */
    public void enterNamespaceScope(String nsName) {
        if (!ignore()) {
            Object parentNs = getCurrentNamespace();
            Object ns = findNamespace(nsName, parentNs);
            if (ns == null) {
                ns =
		    Model.getModelManagementFactory().buildPackage(
			    nsName,
			    UUIDManager.getInstance().getNewUUID());
                Model.getCoreHelper().setNamespace(ns, parentNs);
            }
            contextStack.push(ns);
        }
    }

    /**
     * Get the current namespace from the {@link #contextStack contextStack}or
     * the model.
     *
     * @return the parent namespace
     */
    private Object getCurrentNamespace() {
        Object parentNs = null;
        if (contextStack.isEmpty()) {
            parentNs = getModel();
        } else {
            parentNs = contextStack.peek();
            assert Model.getFacade().isANamespace(parentNs);
        }
        return parentNs;
    }

    /**
     * Find the namespace with the given name which parent is
     * <code>parentNs</code>.
     *
     * @param nsName namespace name
     * @param parentNs the parent namespace of the namespace to get
     * @return the namespace if it exists, <code>null</code> otherwise.
     */
    private static Object findNamespace(String nsName, Object parentNs) {
        Collection nss =
	    Model.getModelManagementHelper().getAllNamespaces(getModel());
        Iterator it = nss.iterator();
        Object ns = null;
        while (it.hasNext()) {
            Object tmpNs = it.next();
            if (nsName.equals(Model.getFacade().getName(tmpNs))) {
                // NOTE: equality by reference may be deceiving if the
                // implementation uses proxies - not likely that different
                // proxies are used, so at least we should be comparing the
                // references of the same proxy object!
                if (Model.getFacade().getNamespace(tmpNs) == parentNs) {
                    ns = tmpNs;
                    break;
                }
            }
        }
        return ns;
    }

    /**
     * @return the model
     */
    private static Object getModel() {
        return ProjectManager.getManager().getCurrentProject().getModel();
    }

    /**
     * @see org.argouml.language.cpp.reveng.Modeler#exitNamespaceScope()
     */
    public void exitNamespaceScope() {
        if (!ignore()) {
            Object ns = contextStack.pop();
            assert Model.getFacade().isANamespace(ns) : "The poped context (\""
                + ns + "\") isn't a namespace!";
        }
    }

    /**
     * @see org.argouml.language.cpp.reveng.Modeler#makeNamespaceAlias(java.lang.String,
     *      java.lang.String)
     */
    public void makeNamespaceAlias(String ns, String alias) {
        // TODO: implement after defining the way this is supposed to be
        // modeled
    }

    /**
     * @see org.argouml.language.cpp.reveng.Modeler#beginClassDefinition(java.lang.String,
     *      java.lang.String)
     */
    public void beginClassDefinition(String oType, String identifier) {
        if (!ignore()) {
            // a class is defined, so, check if it exists in the model or
            // create it...
            Object ns = getCurrentNamespace();
            // FIXME: we're not checking for oType here! Is it needed? Is it
            // possible to have struct X and class X in the same namespace in
            // C++?
            Object cls = findClass(identifier, ns);
            if (cls == null) {
                cls = Model.getCoreFactory().buildClass(identifier, ns);
            }
            contextStack.push(cls);
            if (CPPvariables.OT_CLASS.equals(oType)) {
                // the default visibility for a C++ class
                contextAccessSpecifier = Model.getVisibilityKind().getPrivate();
            } else if (CPPvariables.OT_STRUCT.equals(oType)) {
                contextAccessSpecifier = Model.getVisibilityKind().getPublic();
                Object classSpecifierTV =
		    Model.getExtensionMechanismsFactory()
                        .buildTaggedValue(ProfileCpp.TV_NAME_CLASS_SPECIFIER,
                            "struct");
                Model.getCoreHelper().addTaggedValue(cls, classSpecifierTV);
            } else if (CPPvariables.OT_UNION.equals(oType)) {
                // TODO: implement union specifics.
                ;
            } else {
                assert false
                : "Not expecting any other oType than class, struct and "
                    + "union!";
            }
        }
    }

    /**
     * Find a class within the given namespace that has the given identifier.
     *
     * @param identifier the class identifier
     * @param ns namespace to look in
     * @return the class if found, null otherwise
     */
    private static Object findClass(String identifier, Object ns) {
        Collection classes = Model.getCoreHelper().getAllClasses(ns);
        Iterator it = classes.iterator();
        while (it.hasNext()) {
            Object candidateClass = it.next();
            if (Model.getFacade().getName(candidateClass).equals(identifier)) {
                return candidateClass;
            }
        }
        return null;
    }

    /**
     * @see org.argouml.language.cpp.reveng.Modeler#endClassDefinition()
     */
    public void endClassDefinition() {
        if (!ignore()) {
            Object cls = contextStack.pop();
            assert Model.getFacade().isAClass(cls) : "The poped context (\""
                + cls + "\") isn't a class!";
            contextAccessSpecifier = null;
        }
    }

    /**
     * FIXME: I think that with nested classes having only one access specifier
     * won't work. This must be implemented in a stack scheme, where the
     * constructs that can work with access specifiers will need to manage the
     * stack.
     *
     * @see org.argouml.language.cpp.reveng.Modeler#accessSpecifier(java.lang.String)
     */
    public void accessSpecifier(String accessSpec) {
        if (!ignore()) {
            if ("public".equals(accessSpec)) {
                contextAccessSpecifier = Model.getVisibilityKind().getPublic();
            } else if ("protected".equals(accessSpec)) {
                contextAccessSpecifier =
		    Model.getVisibilityKind().getProtected();
            } else if ("private".equals(accessSpec)) {
                contextAccessSpecifier = Model.getVisibilityKind().getPrivate();
            } else {
                assert false : "Unknown C++ access specifier: " + accessSpec;
            }
        }
    }

    /**
     * @see org.argouml.language.cpp.reveng.Modeler#beginFunctionDeclaration()
     */
    public void beginFunctionDeclaration() {
        if (!ignore()) {
            // The default return type is "void", but, this must be substituted
            // with the actual parsed value. If possible it wouldn't be set
            // here.
            Object returnType = getVoid();
            Object oper = buildOperation(contextStack.peek(), returnType);
            if (contextAccessSpecifier != null) {
                Model.getCoreHelper().setVisibility(oper,
                    contextAccessSpecifier);
	    }
            contextStack.push(oper);
        }
    }

    /**
     * @return the void DataType
     */
    private static Object getVoid() {
        return ProjectManager.getManager().getCurrentProject().findType("void");
    }

    /**
     * Create a operation in the given model element.
     *
     * @param me the model element for which to build the operation
     * @param returnType the operation return type
     * @return the operation
     */
    private Object buildOperation(Object me, Object returnType) {
        Collection propertyChangeListeners = getPropertyChangeListeners(me);
        return Model.getCoreFactory().buildOperation(me, getModel(),
            returnType, propertyChangeListeners);
    }

    /**
     * Retrieve the property change listeners for the given model element.
     *
     * @param me the model element
     * @return property change listeners for me
     */
    private static Collection getPropertyChangeListeners(Object me) {
        return ProjectManager.getManager().getCurrentProject()
                .findFigsForMember(me);
    }

    /**
     * @see org.argouml.language.cpp.reveng.Modeler#endFunctionDeclaration()
     */
    public void endFunctionDeclaration() {
        if (!ignore()) {
            Object oper = contextStack.pop();
            assert Model.getFacade().isAOperation(oper) : ""
                + "The poped context (\"" + oper + "\") isn't an operation!";
            removeOperationIfDuplicate(oper);
        }
    }

    /**
     * Check if the given operation is a duplicate of other already existing
     * operation and if so remove it.
     *
     * @param oper the operation to be checked
     */
    private void removeOperationIfDuplicate(Object oper) {
        // in the current
        Object parent = contextStack.peek();
        Collection opers = Model.getFacade().getOperations(parent);

        Iterator it = opers.iterator();
        while (it.hasNext()) {
            Object possibleDuplicateOper = it.next();
            if (oper != possibleDuplicateOper
                && Model.getFacade().getName(oper).equals(
                    Model.getFacade().getName(possibleDuplicateOper))) {
                if (equalParameters(oper, possibleDuplicateOper)) {
                    Model.getCoreHelper().removeFeature(parent, oper);
                }
            }
        }
    }

    /**
     * Compare the parameters of two operations.
     *
     * @param oper1 left hand side operation
     * @param oper2 right hand side operation
     * @return true if the parameters are equal - the same types given in the
     *         same order
     */
    private boolean equalParameters(Object oper1, Object oper2) {
        // TODO: implementation.
        return true;
    }

    /**
     * @see org.argouml.language.cpp.reveng.Modeler#declarationSpecifiers(java.util.List)
     */
    public void declarationSpecifiers(List declSpecs) {
        // TODO: Auto-generated method stub

    }

    /**
     * @see org.argouml.language.cpp.reveng.Modeler#simpleTypeSpecifier(java.util.List)
     */
    public void simpleTypeSpecifier(List sts) {
        if (!ignore()) {
            StringBuffer stsString = new StringBuffer();
            Iterator i = sts.iterator();
            while (i.hasNext()) {
                stsString.append(i.next().toString()).append(" ");
	    }
            Object theType = findOrCreateType(stsString.toString().trim());
            // now, depending on the context, this might be the return type of a
            // function declaration or an attribute of a class or a variable
            // declaration; of course, this is rather incomplete(!)
            if (Model.getFacade().isAOperation(contextStack.peek())) {
                // set the operation return type
                Object rv =
		    Model.getCoreHelper().getReturnParameter(
			contextStack.peek());
                Model.getCoreHelper().setType(rv, theType);

            } else if (Model.getFacade().isAClass(contextStack.peek())) {
                Object attr =
		    Model.getCoreFactory().buildAttribute(
			    contextStack.peek(), getModel(), theType,
			    getPropertyChangeListeners(contextStack.peek()));
                if (contextAccessSpecifier != null) {
                    Model.getCoreHelper().setVisibility(attr,
                        contextAccessSpecifier);
		}
                contextStack.push(attr);
            } else if (Model.getFacade().isAParameter(contextStack.peek())) {
                Model.getCoreHelper().setType(contextStack.peek(), theType);
            }
        }
    }

    /**
     * Finds or creates a type with the given name. This method
     * delegates the call to ArgoUML helper method, but, first takes
     * care of C++ specific issues, such as pointer and reference
     * stripping and buit-in types which shouldn't be created as
     * classes (the way ArgoUML does), but, as DataType.
     *
     * @param typeName the name of the type
     * @return A model element that represents the given type
     */
    private Object findOrCreateType(String typeName) {
        Object theType = null;
        List taggedValues = new LinkedList();
        String strippedTypeName = processPtrOperators(typeName, taggedValues);
        if (ProfileCpp.isBuiltIn(typeName)) {
            theType = ProfileCpp.getBuiltIn(typeName);
        } else {
            theType =
		ProjectManager.getManager().getCurrentProject().findType(
			typeName.toString(), true);
        }
        return theType;
    }

    /**
     * Process a type specification by stripping pointer operators
     * from the type name and processing them to tagged values that
     * are added to the provide list.
     *
     * @param typeName unprocessed C++ type name
     * @param taggedValues list of tagged values where any processing result is
     *            added to
     * @return the stripped type name
     */
    private String processPtrOperators(String typeName, List taggedValues) {
        // TODO: implement
        return typeName;
    }

    /**
     * @see org.argouml.language.cpp.reveng.Modeler#directDeclarator(java.lang.String)
     */
    public void directDeclarator(String id) {
        if (!ignore()) {
            Model.getCoreHelper().setName(contextStack.peek(), id);
            if (Model.getFacade().isAAttribute(contextStack.peek())) {
                Object attr = contextStack.pop();
                removeAttributeIfDuplicate(attr);
            }
        }
    }

    /**
     * Check if the given attribute is a duplicate of other already existing
     * attribute and if so remove it.
     *
     * FIXME: this method is very similar to the {@link
     * #removeOperationIfDuplicate(Object) removeOperationIfDuplicate}
     * method. Both are related to the removal of a Feature from a
     * Classifier if the Feature is duplicated in the Classifier. I
     * think this may be refactored in the future...
     *
     * @param attr the attribute to be checked and possibly removed
     */
    private void removeAttributeIfDuplicate(Object attr) {
        Object parent = contextStack.peek();
        Collection attrs = Model.getFacade().getAttributes(parent);

        Iterator it = attrs.iterator();
        while (it.hasNext()) {
            Object possibleDuplicateAttr = it.next();
            if (attr != possibleDuplicateAttr
                && Model.getFacade().getName(attr).equals(
                    Model.getFacade().getName(possibleDuplicateAttr))) {
                Model.getCoreHelper().removeFeature(parent, attr);
            }
        }
    }

    /**
     * @see org.argouml.language.cpp.reveng.Modeler#storageClassSpecifier(java.lang.String)
     */
    public void storageClassSpecifier(String storageClassSpec) {
        // TODO: Auto-generated method stub
    }

    /**
     * @see org.argouml.language.cpp.reveng.Modeler#typeQualifier(java.lang.String)
     */
    public void typeQualifier(String typeQualifier) {
        // TODO: Auto-generated method stub

    }

    /**
     * @see org.argouml.language.cpp.reveng.Modeler#beginFunctionDefinition()
     */
    public void beginFunctionDefinition() {
        if (!ignore()) {
            if (isMemberDeclaration()) {
                beginFunctionDeclaration();
            } else {
                // TODO: here we should set the method of the corresponding
                // operation, if it exists, or create a global operation and
                // set the corresponding method
                ignoreableFunctionDefinition = true;
            }
        }
    }

    /**
     * @see org.argouml.language.cpp.reveng.Modeler#endFunctionDefinition()
     */
    public void endFunctionDefinition() {
        if (!ignore()) {
            if (isMemberDeclaration()) {
                endFunctionDeclaration();
            } else {
                // TODO: here we should set the method of the corresponding
                // operation, if it exists, or create a global operation and
                // set the corresponding method
                ignoreableFunctionDefinition = false;
            }
        }
    }

    /**
     * @return true if this call occurs within a member declaration
     */
    private boolean isMemberDeclaration() {
        return memberDeclarationCount > 0;
    }

    /**
     * @see org.argouml.language.cpp.reveng.Modeler#functionDirectDeclarator(java.lang.String)
     */
    public void functionDirectDeclarator(String identifier) {
        if (!ignore()) {
            assert Model.getFacade().isAOperation(contextStack.peek());
            Model.getCoreHelper().setName(contextStack.peek(), identifier);
        }
    }

    /**
     * @return true if the call should be ignored
     */
    private boolean ignore() {
        return compoundStatementCount > 0 || ignoreableFunctionDefinition;
    }

    /**
     * @see org.argouml.language.cpp.reveng.Modeler#beginParameterDeclaration()
     */
    public void beginParameterDeclaration() {
        if (!ignore()) {
            Object oper = contextStack.peek();
            if (Model.getFacade().isAOperation(oper)) {
                // create a parameter within the operation
                Object param =
		    Model.getCoreFactory().buildParameter(
			    oper,
			    getModel(), getVoid(),
			    getPropertyChangeListeners(contextStack.peek()));
                // add the created parameter to the stack
                contextStack.push(param);
            }
        }
    }

    /**
     * @see org.argouml.language.cpp.reveng.Modeler#endParameterDeclaration()
     */
    public void endParameterDeclaration() {
        if (!ignore()) {
            // NOTE: this is different from the other endXxx() methods, since,
            // we may be called within the context of a function definition
            // without it being a member.
            Object param = contextStack.peek();
            if (Model.getFacade().isAParameter(param)) {
                contextStack.pop();
                // set the parameter kind according to the tagged value details
                // FIXME: this won't work when we have const reference
                // parameters!
                if (Model.getFacade().getTaggedValueValue(param,
                    ProfileCpp.TV_NAME_REFERENCE).equals("true")
                    || Model.getFacade().getTaggedValueValue(param,
                        ProfileCpp.TV_NAME_POINTER).equals("true")) {
                    Model.getCoreHelper().setKind(param,
                        Model.getDirectionKind().getInOutParameter());
                }
            }
        }
    }

    /**
     * @see org.argouml.language.cpp.reveng.Modeler#beginInitializer()
     */
    public void beginInitializer() {
        if (!ignore()) {
            Object context = contextStack.peek();
            if (Model.getFacade().isAOperation(context)) {
                // we don't really need to see what it is being initialized
                // to, for sure it is to 0 => abstract operation
                Model.getCoreHelper().setAbstract(context, true);
            }
        }
    }

    /**
     * @see org.argouml.language.cpp.reveng.Modeler#endInitializer()
     */
    public void endInitializer() {
        // do nothing
    }

    /**
     * @see org.argouml.language.cpp.reveng.Modeler#beginMemberDeclaration()
     */
    public void beginMemberDeclaration() {
        memberDeclarationCount++;
    }

    /**
     * @see org.argouml.language.cpp.reveng.Modeler#endMemberDeclaration()
     */
    public void endMemberDeclaration() {
        memberDeclarationCount--;
    }

    /**
     * @see org.argouml.language.cpp.reveng.Modeler#beginCompoundStatement()
     */
    public void beginCompoundStatement() {
        compoundStatementCount++;
    }

    /**
     * @see org.argouml.language.cpp.reveng.Modeler#endCompoundStatement()
     */
    public void endCompoundStatement() {
        compoundStatementCount--;
    }

    /**
     * @see org.argouml.language.cpp.reveng.Modeler#beginPtrOperator()
     */
    public void beginPtrOperator() {
        if (!ignore()) {
            Object ptrTV =
		Model.getExtensionMechanismsFactory()
                    .buildTaggedValue("dummy", "");
            contextStack.push(ptrTV);
        }
    }

    /**
     * @see org.argouml.language.cpp.reveng.Modeler#endPtrOperator()
     */
    public void endPtrOperator() {
        if (!ignore()) {
            Object ptrTV = contextStack.pop();
            assert Model.getFacade().isATaggedValue(ptrTV);
            Model.getCoreHelper().addTaggedValue(contextStack.peek(), ptrTV);
        }
    }

    /**
     * @see org.argouml.language.cpp.reveng.Modeler#ptrOperator(java.lang.String)
     */
    public void ptrOperator(String ptrSymbol) {
        if (!ignore()) {
            if (ptrSymbol.equals("&")) {
                // arrrg we must discard the tagged value created before cause
                // we can't change its name
                Object discardedTV = contextStack.pop();
                assert Model.getFacade().isATaggedValue(discardedTV);

                Object refTV =
		    Model.getExtensionMechanismsFactory()
                        .buildTaggedValue("reference", "true");
                contextStack.push(refTV);

            } else if (ptrSymbol.equals("*")) {
                // arrrg ditto!
                Object discardedTV = contextStack.pop();
                assert Model.getFacade().isATaggedValue(discardedTV);

                Object ptrTV =
		    Model.getExtensionMechanismsFactory()
                        .buildTaggedValue("pointer", "true");
                contextStack.push(ptrTV);
            } else {
                LOG.warn("unprocessed ptrSymbol: " + ptrSymbol);
            }
        }
    }

    /**
     * @see org.argouml.language.cpp.reveng.Modeler#ptrToMember(java.lang.String,
     *      java.lang.String)
     */
    public void ptrToMember(String scopedItem, String star) {
        // TODO: Auto-generated method stub
    }

    /**
     * Modeler for the base_specifier rule.
     */
    private BaseSpecifierModeler baseSpecifierModeler;

    /**
     * @see org.argouml.language.cpp.reveng.Modeler#beginBaseSpecifier()
     */
    public void beginBaseSpecifier() {
        if (!ignore()) {
            baseSpecifierModeler = new BaseSpecifierModeler();
	}
    }

    /**
     * @see org.argouml.language.cpp.reveng.Modeler#endBaseSpecifier()
     */
    public void endBaseSpecifier() {
        if (!ignore()) {
            baseSpecifierModeler.finish();
            baseSpecifierModeler = null;
        }
    }

    /**
     * @see org.argouml.language.cpp.reveng.Modeler#baseSpecifier(java.lang.String,
     *      boolean)
     */
    public void baseSpecifier(String identifier, boolean isVirtual) {
        if (!ignore()) {
            baseSpecifierModeler.baseSpecifier(identifier, isVirtual);
	}
    }

    /**
     * A class that models the base_specifier rule, mapping it to a UML
     * generalization.
     */
    private class BaseSpecifierModeler {
        private Object previousAccessSpecifier;

        private Object generalization;

        /**
         * The constructor of the BaseSpecifierModeler retrieves information
         * about the current parsing context, storing it to enable context aware
         * processing of the baseSpecifier call, and resetting the context to
         * its previous state after processing.
         */
        BaseSpecifierModeler() {
            previousAccessSpecifier = contextAccessSpecifier;
            contextAccessSpecifier = null;
        }

        /**
         *
         * @param identifier the base class identifier
         * @param isVirtual flags virtual inheritance
         */
        void baseSpecifier(String identifier, boolean isVirtual) {
            // create a generalization for the current class
            Object parent = findOrCreateType(identifier);
            generalization =
		findOrCreateGeneralization(parent, contextStack.peek());
            Model.getCoreHelper().setTaggedValue(generalization,
                ProfileCpp.TV_VIRTUAL_INHERITANCE, Boolean.toString(isVirtual));
        }

        /**
         * Finish processing the base specifier rule.
         */
        void finish() {
            // set the visibility of the generalization
            if (contextAccessSpecifier == null) { // default is private
                contextAccessSpecifier = Model.getVisibilityKind().getPrivate();
            }
            Model.getCoreHelper().setTaggedValue(generalization,
                ProfileCpp.TV_INHERITANCE_VISIBILITY,
                Model.getFacade().getName(contextAccessSpecifier));
            // finish the base specifier by setting the context to the
            // previous state
            contextAccessSpecifier = previousAccessSpecifier;
        }
    }

    /**
     * Find or create a Generalization between the given parent and child
     * Classifiers.
     *
     * @param parent the parent Classifier
     * @param child the child Classifier
     * @return the found Generalization model element if found, otherwise a
     *         newly created
     */
    private static Object findOrCreateGeneralization(Object parent,
            Object child) {
        Object generalization =
	    Model.getFacade().getGeneralization(child, parent);
        if (generalization == null) {
            generalization =
		Model.getCoreFactory().buildGeneralization(child, parent);
	}
        return generalization;
    }

    /**
     * Modeler for constructors.
     */
    private CtorModeler ctorModeler;

    /**
     * @see org.argouml.language.cpp.reveng.Modeler#beginCtorDefinition()
     */
    public void beginCtorDefinition() {
        if (!ignore()) {
            assert ctorModeler == null;
            ctorModeler = new CtorModeler();
        }
    }

    /**
     * Helper method that gets a stereotype for the given model
     * element with the given name.
     *
     * TODO: this might be a performance bottleneck. If so, caching of
     * stereotypes for model elements by their stereotypes names could fix it.
     *
     * @param modelElement The model element for which to look for the
     *            stereotype with the given name
     * @param stereotypeName The name of the stereotype.
     * @return the stereotype model element or null if not found.
     */
    private Object getStereotype(Object modelElement, String stereotypeName) {
        Object stereotype = null;
        Collection stereotypes =
	    Model
                .getExtensionMechanismsHelper()
                .getAllPossibleStereotypes(
                    ProjectManager.getManager().getCurrentProject().getModels(),
                    modelElement);
        for (Iterator it = stereotypes.iterator(); it.hasNext();) {
            Object candidateStereotype = it.next();
            if (Model.getFacade().getName(candidateStereotype).equals(
                stereotypeName)) {
                stereotype = candidateStereotype;
                break;
            }
        }
        return stereotype;
    }

    /**
     * @see org.argouml.language.cpp.reveng.Modeler#endCtorDefinition()
     */
    public void endCtorDefinition() {
        if (!ignore()) {
            ctorModeler.finish();
	}
    }

    /**
     * @see org.argouml.language.cpp.reveng.Modeler#qualifiedCtorId(java.lang.String)
     */
    public void qualifiedCtorId(String identifier) {
        if (!ignore()) {
            ctorModeler.qualifiedCtorId(identifier);
        }
    }

    /**
     * Base Modeler class for ctor and dtor modelers.
     */
    private abstract class XtorModeler {
        private Object xtor;

        /**
         * Initializes the XtorModeler with the stereotypeName, creates the
         * operation in the class and sets the stereotype with the given name.
         *
         * @param stereotypeName "create" for ctors and "destroy" for dtors.
         */
        XtorModeler(String stereotypeName) {
            // FIXME: this will fail when we try to process definitions made
            // outside of the class definition!
            assert Model.getFacade().isAClass(contextStack.peek());
            xtor = buildOperation(contextStack.peek(), getVoid());
            Model.getExtensionMechanismsHelper().addCopyStereotype(xtor,
                getStereotype(xtor, stereotypeName));
            contextStack.push(xtor);
        }

        /**
         * Checks if the given operation is the xtor (e.g. the modeled
         * constructor or destructor).
         *
         * @param oper the operation to check
         * @return true if the oper is the modeled xtor
         */
        boolean isTheXtor(Object oper) {
            return xtor == oper;
        }

        /**
         * Pops the xtor from the <code>contextStack</code>.
         */
        void finish() {
            Object poppedXtor = contextStack.pop();
            assert isTheXtor(poppedXtor);
            removeOperationIfDuplicate(xtor);
        }
    }

    /**
     * Modeler class for constructors.
     */
    private class CtorModeler extends XtorModeler {
        /**
         * Creates the ctor and puts it into the stack.
         */
        CtorModeler() {
            super("create");
        }

        /**
         * Sets the name of the ctor operation to the identifier.
         *
         * @param identifier
         */
        void qualifiedCtorId(String identifier) {
            assert isTheXtor(contextStack.peek());
            Model.getCoreHelper().setName(contextStack.peek(), identifier);
        }
    }

    private DtorModeler dtorModeler;

    /**
     * @see org.argouml.language.cpp.reveng.Modeler#beginDtorHead()
     */
    public void beginDtorHead() {
        dtorModeler = new DtorModeler();
    }

    /**
     * @see org.argouml.language.cpp.reveng.Modeler#endDtorHead()
     */
    public void endDtorHead() {
        dtorModeler.finish();
        dtorModeler = null;
    }

    /**
     * @see org.argouml.language.cpp.reveng.Modeler#dtorDeclarator(java.lang.String)
     */
    public void dtorDeclarator(String qualifiedId) {
        dtorModeler.declarator(qualifiedId);
    }

    /**
     * Modeler for destructor.
     */
    private class DtorModeler extends XtorModeler {

        /**
         * Ctor for the DtorModeler class, responsible for creating the
         * operation and setting up the <code>contextStack</code>
         * appropriately.
         */
        DtorModeler() {
            super("destroy");
        }

        /**
         * @param qualifiedId
         */
        void declarator(String qualifiedId) {
            assert isTheXtor(contextStack.peek());
            Model.getCoreHelper().setName(contextStack.peek(), qualifiedId);
        }
    }
}
