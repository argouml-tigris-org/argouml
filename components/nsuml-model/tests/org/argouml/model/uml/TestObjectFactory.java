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

package org.argouml.model.uml;

import junit.framework.TestCase;

import org.argouml.api.InvalidObjectRequestException;
import org.argouml.api.model.ObjectFactoryManager;
import org.argouml.api.model.uml.Uml;
import org.argouml.api.model.uml.UmlObjectFactory;

import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.behavior.collaborations.*;
import ru.novosoft.uml.behavior.common_behavior.*;
import ru.novosoft.uml.behavior.state_machines.*;
import ru.novosoft.uml.behavior.use_cases.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;
import ru.novosoft.uml.model_management.*;

public class TestObjectFactory extends TestCase {

    public TestObjectFactory(String n) { super(n); }

    private void testObjectFactoryType(Class c, Class expected, boolean runTest) {
        // Certain objects cannot be instantiated by NSUML.
        // We allow these to be part of the test list for completeness, but do not test them.
        if (! runTest) {
        	return;
        }
    	assertTrue("Not a valid entity: " + c.getClass(),
		           Uml.Entity.class.isAssignableFrom(c));
		
		UmlObjectFactory f = ObjectFactoryManager.getUmlFactory();
		assertNotNull("Did not get Object Factory", f);
		assertEquals("Did not get the correct Object Factory",
					 f.getClass(), NsumlObjectFactory.class);
        Object o = null;
        try {
			o = f.create(c);
		} catch (InvalidObjectRequestException e) {
			e.printStackTrace();
			e.getCause().printStackTrace();
			fail("Cannot generate " + c.getName());
   		}
        assertNotNull("Did not get an element for " + c.getName(), o);
        assertEquals("Did not get the right element", o.getClass(), expected);
    }

    public void testObjectFactory() {
		testObjectFactoryType(Uml.ABSTRACTION, MAbstractionImpl.class, true);
		testObjectFactoryType(Uml.ASSOCIATION, MAssociationImpl.class, true);
		testObjectFactoryType(Uml.ASSOCIATION_ROLE, MAssociationRoleImpl.class, true);
		testObjectFactoryType(Uml.DEPENDENCY, MDependencyImpl.class, true);
		testObjectFactoryType(Uml.EXTEND, MExtendImpl.class, true);
		testObjectFactoryType(Uml.GENERALIZATION, MGeneralizationImpl.class, true);
		testObjectFactoryType(Uml.INCLUDE, MIncludeImpl.class, true);
		testObjectFactoryType(Uml.LINK, MLinkImpl.class, true);
		testObjectFactoryType(Uml.PERMISSION, MPermissionImpl.class, true);
		testObjectFactoryType(Uml.USAGE, MUsageImpl.class, true);
		testObjectFactoryType(Uml.TRANSITION, MTransitionImpl.class, true);
		testObjectFactoryType(Uml.ACTOR, MActorImpl.class, true);
		testObjectFactoryType(Uml.CLASS, MClassImpl.class, true);
		testObjectFactoryType(Uml.CLASSIFIER, MClassifierImpl.class, true);
		testObjectFactoryType(Uml.CLASSIFIER_ROLE, MClassifierRoleImpl.class, true);
		testObjectFactoryType(Uml.COMPONENT, MComponentImpl.class, true);
		testObjectFactoryType(Uml.COMPONENT_INSTANCE, MComponentInstanceImpl.class, true);
		testObjectFactoryType(Uml.INSTANCE, MInstanceImpl.class, true);
		testObjectFactoryType(Uml.INTERFACE, MInterfaceImpl.class, true);
		testObjectFactoryType(Uml.NODE, MNodeImpl.class, true);
		testObjectFactoryType(Uml.NODE_INSTANCE, MNodeInstanceImpl.class, true);
		testObjectFactoryType(Uml.OBJECT, MObjectImpl.class, true);
		testObjectFactoryType(Uml.PACKAGE, MPackageImpl.class, true);
		testObjectFactoryType(Uml.STATE, MStateImpl.class, true);
		testObjectFactoryType(Uml.COMPOSITESTATE, MCompositeStateImpl.class, true);
		testObjectFactoryType(Uml.PSEUDOSTATE, MPseudostateImpl.class, true);
		testObjectFactoryType(Uml.USE_CASE, MUseCaseImpl.class, true);
		testObjectFactoryType(Uml.ACTION, MActionImpl.class, true);
		testObjectFactoryType(Uml.ASSOCIATION_END, MAssociationEndImpl.class, true);
		testObjectFactoryType(Uml.CALL_ACTION, MCallActionImpl.class, true);
		testObjectFactoryType(Uml.NAMESPACE, MNamespaceImpl.class, true);
		testObjectFactoryType(Uml.RECEPTION, MReceptionImpl.class, true);
		testObjectFactoryType(Uml.STEREOTYPE, MStereotypeImpl.class, true);
		testObjectFactoryType(Uml.ATTRIBUTE, MAttributeImpl.class, true);
		testObjectFactoryType(Uml.OPERATION, MOperationImpl.class, true);
		testObjectFactoryType(Uml.ACTION_EXPRESSION, MActionExpression.class, true);
		testObjectFactoryType(Uml.DATA_TYPE, MDataTypeImpl.class, true);

		// NSUML cannot instantiate a State Vertex object
		testObjectFactoryType(Uml.STATE_VERTEX, MStateVertexImpl.class, false);
		
		// NSUML cannot instantiate an Event object
		testObjectFactoryType(Uml.EVENT, MEventImpl.class, false);


    }
}