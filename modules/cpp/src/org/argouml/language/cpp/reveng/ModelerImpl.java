// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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
import java.util.List;
import java.util.Stack;

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
 * @version 0.00
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
     * @see org.argouml.language.cpp.reveng.Modeler#beginTranslationUnit()
     */
    public void beginTranslationUnit() {
        // TODO: Auto-generated method stub

    }

    /**
     * @see org.argouml.language.cpp.reveng.Modeler#endTranslationUnit()
     */
    public void endTranslationUnit() {
        // TODO: Auto-generated method stub

    }

    /**
     * @see org.argouml.language.cpp.reveng.Modeler#enterNamespaceScope(java.lang.String)
     */
    public void enterNamespaceScope(String nsName) {
        Object ns = null;
        if (!namespaceExists(nsName)) {
            ns = Model.getModelManagementFactory().buildPackage(nsName,
                    UUIDManager.getInstance().getNewUUID());
            Model.getCoreHelper().setNamespace(ns, getModel());
        }
        contextStack.push(ns);
    }

    /**
     * @return the model
     */
    private Object getModel() {
        return ProjectManager.getManager().getCurrentProject().getModel();
    }

    /**
     * Check if the provided namespace already exists.
     * 
     * @param ns
     * @return true if the namespace already exists in the model
     */
    private boolean namespaceExists(String ns) {
        Collection nss = Model.getModelManagementHelper().getAllNamespaces(
                getModel());
        Iterator it = nss.iterator();
        Object pack = null;
        while (it.hasNext()) {
            if (ns.equals(Model.getFacade().getName(it.next()))) {
                return true;
            }
        }
        return false;
    }

    /**
     * @see org.argouml.language.cpp.reveng.Modeler#exitNamespaceScope()
     */
    public void exitNamespaceScope() {
        Object ns = contextStack.pop();
        assert Model.getFacade().isANamespace(ns) : "The poped context (\""
                + ns + "\") isn't a namespace!";
    }

    /**
     * @see org.argouml.language.cpp.reveng.Modeler#makeNamespaceAlias(java.lang.String,
     *      java.lang.String)
     */
    public void makeNamespaceAlias(String ns, String alias) {
        // TODO: Auto-generated method stub

    }

    /**
     * @see org.argouml.language.cpp.reveng.Modeler#beginClassDefinition(java.lang.String,
     *      java.lang.String)
     */
    public void beginClassDefinition(String oType, String identifier) {
        // a class is defined, so, create the class in the model...
        Object cls = Model.getCoreFactory().buildClass(identifier,
                contextStack.peek());
        contextStack.push(cls);
    }

    /**
     * @see org.argouml.language.cpp.reveng.Modeler#endClassDefinition()
     */
    public void endClassDefinition() {
        Object cls = contextStack.pop();
        assert Model.getFacade().isAClass(cls) : "The poped context (\"" + cls
                + "\") isn't a class!";
    }

    /**
     * @see org.argouml.language.cpp.reveng.Modeler#accessSpecifier(java.lang.String)
     */
    public void accessSpecifier(String accessSpec) {
        // TODO: Auto-generated method stub

    }

    /**
     * @see org.argouml.language.cpp.reveng.Modeler#beginFunctionDeclaration()
     */
    public void beginFunctionDeclaration() {
        // The default return type is "void", but, this must be substituted
        // with the actual parsed value. If possible it wouldn't be set here.
        Object returnType = ProjectManager.getManager().getCurrentProject()
                .findType("void");
        Object oper = buildOperation(contextStack.peek(), returnType);
        contextStack.push(oper);
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
    private Collection getPropertyChangeListeners(Object me) {
        return ProjectManager.getManager().getCurrentProject()
                .findFigsForMember(me);
    }

    /**
     * @see org.argouml.language.cpp.reveng.Modeler#endFunctionDeclaration()
     */
    public void endFunctionDeclaration() {
        Object oper = contextStack.pop();
        assert Model.getFacade().isAOperation(oper) : "The poped context (\""
                + oper + "\") isn't an operation!";
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
        StringBuffer stsString = new StringBuffer();
        Iterator i = sts.iterator();
        while (i.hasNext())
            stsString.append(i.next().toString()).append(" ");
        Object theType = ProjectManager.getManager().getCurrentProject()
                .findType(stsString.toString(), true);
        // now, depending on the context, this might be the return type of a
        // function declaration or an attribute of a class or a variable
        // declaration; of course, this is rather incomplete(!)
        if (Model.getFacade().isAOperation(contextStack.peek())) {
            // set the operation return type
            Object rv = Model.getCoreHelper().getReturnParameter(
                    contextStack.peek());
            Model.getCoreHelper().setType(rv, theType);

        } else if (Model.getFacade().isAClass(contextStack.peek())) {
            Object attr = Model.getCoreFactory().buildAttribute(
                    contextStack.peek(), getModel(), theType,
                    getPropertyChangeListeners(contextStack.peek()));
            contextStack.push(attr);
        }
    }

    /**
     * @see org.argouml.language.cpp.reveng.Modeler#directDeclarator(java.lang.String)
     */
    public void directDeclarator(String id) {
        Model.getCoreHelper().setName(contextStack.peek(), id);
        if (Model.getFacade().isAAttribute(contextStack.peek()))
            contextStack.pop();
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
        // TODO: Auto-generated method stub

    }

    /**
     * @see org.argouml.language.cpp.reveng.Modeler#endFunctionDefinition()
     */
    public void endFunctionDefinition() {
        // TODO: Auto-generated method stub

    }

    /**
     * @see org.argouml.language.cpp.reveng.Modeler#functionDirectDeclarator(java.lang.String)
     */
    public void functionDirectDeclarator(String identifier) {
        // TODO: Auto-generated method stub

    }
}