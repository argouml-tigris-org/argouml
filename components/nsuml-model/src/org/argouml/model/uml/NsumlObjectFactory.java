//Copyright (c) 2003 The Regents of the University of California. All
//Rights Reserved. Permission to use, copy, modify, and distribute this
//software and its documentation without fee, and without a written
//agreement is hereby granted, provided that the above copyright notice
//and this paragraph appear in all copies.  This software program and
//documentation are copyrighted by The Regents of the University of
//California. The software program and documentation are supplied "AS
//IS", without any accompanying services from The Regents. The Regents
//does not warrant that the operation of the program will be
//uninterrupted or error-free. The end-user understands that the program
//was developed for research purposes and is advised not to rely
//exclusively on the program for any reason.  IN NO EVENT SHALL THE
//UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
//SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
//ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
//THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
//SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
//WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
//MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
//PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
//CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
//UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

package org.argouml.model.uml;

import java.lang.reflect.Method;
import java.util.Hashtable;

import org.argouml.api.InvalidObjectRequestException;
import org.argouml.api.model.uml.Uml;
import org.argouml.api.model.uml.UmlObjectFactory;

import ru.novosoft.uml.MFactory;
import ru.novosoft.uml.behavior.collaborations.*;
import ru.novosoft.uml.behavior.common_behavior.*;
import ru.novosoft.uml.behavior.state_machines.*;
import ru.novosoft.uml.behavior.use_cases.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;
import ru.novosoft.uml.model_management.*;

/**
 * Element creation object for the Model component in ArgoUML.<p>
 *
 * @author Thierry Lach
 */
public class NsumlObjectFactory implements UmlObjectFactory {

	private static Hashtable elements;

	static {
		MFactory factory = MFactory.getDefaultFactory();
		MFactorySupplement extra = new MFactorySupplement();
		elements = new Hashtable(80);
		elements.put(
			Uml.ABSTRACTION,
			new NsumlObjectInfo(
				factory,
				MAbstraction.class,
				"createAbstraction"));
		elements.put(
			Uml.ASSOCIATION,
			new NsumlObjectInfo(
				factory,
				MAssociation.class,
				"createAssociation"));
		elements.put(
			Uml.ASSOCIATION_ROLE,
			new NsumlObjectInfo(
				factory,
				MAssociationRole.class,
				"createAssociationRole"));
		elements.put(
			Uml.DEPENDENCY,
			new NsumlObjectInfo(
				factory,
				MDependency.class,
				"createDependency"));
		elements.put(
			Uml.EXTEND,
			new NsumlObjectInfo(factory, MExtend.class, "createExtend"));
		elements.put(
			Uml.GENERALIZATION,
			new NsumlObjectInfo(
				factory,
				MGeneralization.class,
				"createGeneralization"));
		elements.put(
			Uml.INCLUDE,
			new NsumlObjectInfo(factory, MInclude.class, "createInclude"));
		elements.put(
			Uml.LINK,
			new NsumlObjectInfo(factory, MLink.class, "createLink"));
		elements.put(
			Uml.PERMISSION,
			new NsumlObjectInfo(
				factory,
				MPermission.class,
				"createPermission"));
		elements.put(
			Uml.USAGE,
			new NsumlObjectInfo(factory, MUsage.class, "createUsage"));
		elements.put(
			Uml.TRANSITION,
			new NsumlObjectInfo(
				factory,
				MTransition.class,
				"createTransition"));
		elements.put(
			Uml.ACTOR,
			new NsumlObjectInfo(factory, MActor.class, "createActor"));
		elements.put(
			Uml.CLASS,
			new NsumlObjectInfo(factory, MClass.class, "createClass"));
		elements.put(
			Uml.CLASSIFIER,
			new NsumlObjectInfo(
				factory,
				MClassifier.class,
				"createClassifier"));
		elements.put(
			Uml.CLASSIFIER_ROLE,
			new NsumlObjectInfo(
				factory,
				MClassifierRole.class,
				"createClassifierRole"));
		elements.put(
			Uml.COMPONENT,
			new NsumlObjectInfo(factory, MComponent.class, "createComponent"));
		elements.put(
			Uml.COMPONENT_INSTANCE,
			new NsumlObjectInfo(
				factory,
				MComponentInstance.class,
				"createComponentInstance"));
		elements.put(
			Uml.INSTANCE,
			new NsumlObjectInfo(factory, MInstance.class, "createInstance"));
		elements.put(
			Uml.INTERFACE,
			new NsumlObjectInfo(factory, MInterface.class, "createInterface"));
		elements.put(
			Uml.NODE,
			new NsumlObjectInfo(factory, MNode.class, "createNode"));
		elements.put(
			Uml.NODE_INSTANCE,
			new NsumlObjectInfo(
				factory,
				MNodeInstance.class,
				"createNodeInstance"));
		elements.put(
			Uml.OBJECT,
			new NsumlObjectInfo(factory, MObject.class, "createObject"));
		elements.put(
			Uml.PACKAGE,
			new NsumlObjectInfo(factory, MPackage.class, "createPackage"));
		elements.put(
			Uml.STATE,
			new NsumlObjectInfo(factory, MState.class, "createState"));
		elements.put(
			Uml.COMPOSITESTATE,
			new NsumlObjectInfo(
				factory,
				MCompositeState.class,
				"createCompositeState"));
		elements.put(
			Uml.PSEUDOSTATE,
			new NsumlObjectInfo(
				factory,
				MPseudostate.class,
				"createPseudostate"));
		elements.put(
			Uml.USE_CASE,
			new NsumlObjectInfo(factory, MUseCase.class, "createUseCase"));
		elements.put(
			Uml.ACTION,
			new NsumlObjectInfo(factory, MAction.class, "createAction"));
		elements.put(
			Uml.ASSOCIATION_END,
			new NsumlObjectInfo(
				factory,
				MAssociationEnd.class,
				"createAssociationEnd"));
		elements.put(
			Uml.CALL_ACTION,
			new NsumlObjectInfo(
				factory,
				MCallAction.class,
				"createCallAction"));
		elements.put(
			Uml.NAMESPACE,
			new NsumlObjectInfo(factory, MNamespace.class, "createNamespace"));
		elements.put(
			Uml.RECEPTION,
			new NsumlObjectInfo(factory, MReception.class, "createReception"));
		elements.put(
			Uml.STEREOTYPE,
			new NsumlObjectInfo(
				factory,
				MStereotype.class,
				"createStereotype"));
		elements.put(
			Uml.ATTRIBUTE,
			new NsumlObjectInfo(factory, MAttribute.class, "createAttribute"));
		elements.put(
			Uml.OPERATION,
			new NsumlObjectInfo(factory, MOperation.class, "createOperation"));
		elements.put(
			Uml.DATA_TYPE,
			new NsumlObjectInfo(factory, MDataType.class, "createDataType"));

		// NSUML does not have a factory method for this
		elements.put(
			Uml.ACTION_EXPRESSION,
			new NsumlObjectInfo(
				extra,
				MActionExpression.class,
				"createActionExpression"));

		// NSUML cannot instantiate an Event object
		// elements.put(Uml.EVENT, new NsumlObjectInfo(factory,MEvent.class, "createEvent"));

		// NSUML cannot instantiate a State Vertex object
		// elements.put(Uml.STATE_VERTEX, new NsumlObjectInfo(factory,MStateVertex.class, "createStateVertex"));
	}

	/** Create a UML object from the implementation.
	 * 
	 * @param entity Class to create - must implement {@link org.argouml.model.uml.Uml.Entity}
	 * @throws InvalidObjectRequestException if it cannot create the class.
	 */
	public Object create(Class entity) throws InvalidObjectRequestException {
		NsumlObjectInfo oi = (NsumlObjectInfo) elements.get(entity);
		if (oi == null) {
			throw new InvalidObjectRequestException("Cannot identify the object type", entity);
		}
		String mName = oi.getCreateMethod();
		System.out.println("Method: " + mName);
		Method method = null;
		try {
			method = oi.getFactory().getClass().getMethod(oi.getCreateMethod(),
				                                          new Class[] {} );
		}
		catch (Exception e) {
			throw new InvalidObjectRequestException("Cannot find creator method", entity, e);
		}

		Object obj = null;
		try {
			obj = method.invoke(oi.getFactory(), new Object[] {} );
		}
		catch (Exception e) {
			throw new InvalidObjectRequestException("Cannot execute creator method", entity, e);
		}
		// TODO Figure out how to put in a generic callback to initialize
		// AbstractUmlModelFactory.initialize(expression);
		return obj;
	}

}
