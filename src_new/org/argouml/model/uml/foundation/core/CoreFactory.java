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

package org.argouml.model.uml.foundation.core;

import org.argouml.model.uml.AbstractUmlModelFactory;
import org.argouml.model.uml.UmlFactory;

import ru.novosoft.uml.MFactory;
import ru.novosoft.uml.foundation.core.MAbstraction;
import ru.novosoft.uml.foundation.core.MAssociation;
import ru.novosoft.uml.foundation.core.MAssociationClass;
import ru.novosoft.uml.foundation.core.MAssociationEnd;
import ru.novosoft.uml.foundation.core.MAttribute;
import ru.novosoft.uml.foundation.core.MBinding;
import ru.novosoft.uml.foundation.core.MClass;
import ru.novosoft.uml.foundation.core.MClassifier;
import ru.novosoft.uml.foundation.core.MComment;
import ru.novosoft.uml.foundation.core.MComponent;
import ru.novosoft.uml.foundation.core.MConstraint;
import ru.novosoft.uml.foundation.core.MDataType;
import ru.novosoft.uml.foundation.core.MDependency;
import ru.novosoft.uml.foundation.core.MElementResidence;
import ru.novosoft.uml.foundation.core.MFeature;
import ru.novosoft.uml.foundation.core.MFlow;
import ru.novosoft.uml.foundation.core.MGeneralization;
import ru.novosoft.uml.foundation.core.MInterface;
import ru.novosoft.uml.foundation.core.MMethod;
import ru.novosoft.uml.foundation.core.MNamespace;
import ru.novosoft.uml.foundation.core.MNode;
import ru.novosoft.uml.foundation.core.MOperation;
import ru.novosoft.uml.foundation.core.MParameter;
import ru.novosoft.uml.foundation.core.MPermission;
import ru.novosoft.uml.foundation.core.MPresentationElement;
import ru.novosoft.uml.foundation.core.MRelationship;
import ru.novosoft.uml.foundation.core.MStructuralFeature;
import ru.novosoft.uml.foundation.core.MTemplateParameter;
import ru.novosoft.uml.foundation.core.MUsage;

/**
 * Factory to create UML classes for the UML
 * Foundation::Core package.
 *
 * Feature, StructuralFeature, and PresentationElement
 * do not have a create methods since
 * it is called an "abstract metaclass" in the
 * UML specifications.
 *
 * @since ARGO0.11.2
 * @author Thierry Lach
 */

public class CoreFactory extends AbstractUmlModelFactory {

    /** Singleton instance.
     */
    private static CoreFactory SINGLETON =
                   new CoreFactory();

    /** Singleton instance access method.
     */
    public static CoreFactory getFactory() {
        return SINGLETON;
    }

    /** Don't allow instantiation
     */
    private CoreFactory() {
    }

    /** Create an empty but initialized instance of a UML Abstraction.
     *  
     *  @return an initialized UML Abstraction instance.
     */
    public MAbstraction createAbstraction() {
        MAbstraction modelElement = MFactory.getDefaultFactory().createAbstraction();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UML Association.
     *  
     *  @return an initialized UML Association instance.
     */
    public MAssociation createAssociation() {
        MAssociation modelElement = MFactory.getDefaultFactory().createAssociation();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UML AssociationClass.
     *  
     *  @return an initialized UML AssociationClass instance.
     */
    public MAssociationClass createAssociationClass() {
        MAssociationClass modelElement = MFactory.getDefaultFactory().createAssociationClass();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UML AssociationEnd.
     *  
     *  @return an initialized UML AssociationEnd instance.
     */
    public MAssociationEnd createAssociationEnd() {
        MAssociationEnd modelElement = MFactory.getDefaultFactory().createAssociationEnd();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UML Attribute.
     *  
     *  @return an initialized UML Attribute instance.
     */
    public MAttribute createAttribute() {
        MAttribute modelElement = MFactory.getDefaultFactory().createAttribute();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UML Binding.
     *  
     *  @return an initialized UML Binding instance.
     */
    public MBinding createBinding() {
        MBinding modelElement = MFactory.getDefaultFactory().createBinding();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UML Class.
     *  
     *  @return an initialized UML Class instance.
     */
    public MClass createClass() {
        MClass modelElement = MFactory.getDefaultFactory().createClass();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UML Classifier.
     *  
     *  @return an initialized UML Classifier instance.
     */
    public MClassifier createClassifier() {
        MClassifier modelElement = MFactory.getDefaultFactory().createClassifier();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UML Comment.
     *  
     *  @return an initialized UML Comment instance.
     */
    public MComment createComment() {
        MComment modelElement = MFactory.getDefaultFactory().createComment();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UML Component.
     *  
     *  @return an initialized UML Component instance.
     */
    public MComponent createComponent() {
        MComponent modelElement = MFactory.getDefaultFactory().createComponent();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UML Constraint.
     *  
     *  @return an initialized UML Constraint instance.
     */
    public MConstraint createConstraint() {
        MConstraint modelElement = MFactory.getDefaultFactory().createConstraint();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UML DataType.
     *  
     *  @return an initialized UML DataType instance.
     */
    public MDataType createDataType() {
        MDataType modelElement = MFactory.getDefaultFactory().createDataType();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UML Dependency.
     *  
     *  @return an initialized UML Dependency instance.
     */
    public MDependency createDependency() {
        MDependency modelElement = MFactory.getDefaultFactory().createDependency();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UML ElementResidence.
     *  
     *  @return an initialized UML ElementResidence instance.
     */
    public MElementResidence createElementResidence() {
        MElementResidence modelElement = MFactory.getDefaultFactory().createElementResidence();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UML Flow.
     *  
     *  @return an initialized UML Flow instance.
     */
    public MFlow createFlow() {
        MFlow modelElement = MFactory.getDefaultFactory().createFlow();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UML Generalization.
     *  
     *  @return an initialized UML Generalization instance.
     */
    public MGeneralization createGeneralization() {
        MGeneralization modelElement = MFactory.getDefaultFactory().createGeneralization();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UML Interface.
     *  
     *  @return an initialized UML Interface instance.
     */
    public MInterface createInterface() {
        MInterface modelElement = MFactory.getDefaultFactory().createInterface();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UML Method.
     *  
     *  @return an initialized UML Method instance.
     */
    public MMethod createMethod() {
        MMethod modelElement = MFactory.getDefaultFactory().createMethod();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UML Namespace.
     *  
     *  @return an initialized UML Namespace instance.
     */
    public MNamespace createNamespace() {
        MNamespace modelElement = MFactory.getDefaultFactory().createNamespace();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UML Node.
     *  
     *  @return an initialized UML Node instance.
     */
    public MNode createNode() {
        MNode modelElement = MFactory.getDefaultFactory().createNode();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UML Operation.
     *  
     *  @return an initialized UML Operation instance.
     */
    public MOperation createOperation() {
        MOperation modelElement = MFactory.getDefaultFactory().createOperation();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UML Parameter.
     *  
     *  @return an initialized UML Parameter instance.
     */
    public MParameter createParameter() {
        MParameter modelElement = MFactory.getDefaultFactory().createParameter();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UML Permission.
     *  
     *  @return an initialized UML Permission instance.
     */
    public MPermission createPermission() {
        MPermission modelElement = MFactory.getDefaultFactory().createPermission();
	super.initialize(modelElement);
	return modelElement;
    }


    /** Create an empty but initialized instance of a UML Relationship.
     *  
     *  @return an initialized UML Relationship instance.
     */
    public MRelationship createRelationship() {
        MRelationship modelElement = MFactory.getDefaultFactory().createRelationship();
	super.initialize(modelElement);
	return modelElement;
    }


    /** Create an empty but initialized instance of a UML TemplateParameter.
     *  
     *  @return an initialized UML TemplateParameter instance.
     */
    public MTemplateParameter createTemplateParameter() {
        MTemplateParameter modelElement = MFactory.getDefaultFactory().createTemplateParameter();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UML Usage.
     *  
     *  @return an initialized UML Usage instance.
     */
    public MUsage createUsage() {
        MUsage modelElement = MFactory.getDefaultFactory().createUsage();
	super.initialize(modelElement);
	return modelElement;
    }
}

