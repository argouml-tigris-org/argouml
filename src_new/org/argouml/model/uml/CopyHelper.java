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

// $Id$

package org.argouml.model.uml;

import java.lang.reflect.*;
import java.util.HashMap;

import org.apache.log4j.Category;

import ru.novosoft.uml.foundation.core.MClass;
import ru.novosoft.uml.foundation.core.MClassImpl;
import ru.novosoft.uml.foundation.core.MDataType;
import ru.novosoft.uml.foundation.core.MDataTypeImpl;
import ru.novosoft.uml.foundation.core.MInterface;
import ru.novosoft.uml.foundation.core.MInterfaceImpl;
import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.foundation.core.MNamespace;
import ru.novosoft.uml.foundation.extension_mechanisms.MStereotype;
import ru.novosoft.uml.foundation.extension_mechanisms.MStereotypeImpl;
import ru.novosoft.uml.model_management.MPackage;
import ru.novosoft.uml.model_management.MPackageImpl;

import org.argouml.model.uml.foundation.core.CoreFactory;
import org.argouml.model.uml.foundation.extensionmechanisms.ExtensionMechanismsFactory;
import org.argouml.model.uml.modelmanagement.ModelManagementFactory;

class CopyFunction {
	public final Object object;
	public final Method method;

	public CopyFunction(Object obj, Method m) {
		if (m == null)
			throw new NullPointerException();

		// If obj is null then it must be a static function
		if (obj == null && !Modifier.isStatic(m.getModifiers()))
			throw new NullPointerException();

		object = obj;
		method = m;
	}
}

/**
 * Utility class to facilitate copying model elements.
 *
 * @author Michael Stockman
 * @since 0.13.2
 */
public final class CopyHelper {
	protected static Category cat = Category.getInstance(CopyHelper.class);

	private static CopyHelper theInstance;

	private HashMap copyfunctions;

	private CopyHelper() {
		copyfunctions = new HashMap();

		add(MPackageImpl.class,
			MPackage.class,
			ModelManagementFactory.getFactory(),
			"copyPackage");
		add(MClassImpl.class,
			MClass.class,
			CoreFactory.getFactory(),
			"copyClass");
		add(MDataTypeImpl.class,
			MDataType.class,
			CoreFactory.getFactory(),
			"copyDataType");
		add(MInterfaceImpl.class,
			MInterface.class,
			CoreFactory.getFactory(),
			"copyInterface");
		add(MStereotypeImpl.class,
			MStereotype.class,
			ExtensionMechanismsFactory.getFactory(),
			"copyStereotype");
	}

	public static CopyHelper getHelper() {
		if (theInstance == null)
			theInstance = new CopyHelper();
		return theInstance;
	}

	/**
	 * Adds a copy handler for objects of type <i>type</i>.
	 * Since copy functions could be either instance or static
	 * functions, if obj is an instance of Class then the function
	 * is assumed to be static and will be looked up in obj. Otherwise
	 * the function will be looked up in the Class of obj and invoked on
	 * obj.
	 *
	 * @param type is the type to catch.
	 * @param param is the parameter type of the copy function.
	 * @param obj is described above.
	 * @param name is the name of the copy function.
	 */
	private void add(Class type, Class param, Object obj, String name) {
		Method m;
		Class params[] = {param, MNamespace.class};

		try {
			if (obj instanceof Class) {
				m = ((Class) obj).getDeclaredMethod(
					name,
					params);
				obj = null;
			} else {
				m = obj.getClass().getDeclaredMethod(
					name,
					params);
			}

			copyfunctions.put(type, new CopyFunction(obj, m));
		} catch (Exception e) {
			cat.error("Exception resolving copy method", e);
		}
	}

	/**
	 * Make a copy of element.
	 *
	 * This function may fail and return null for any of the following
	 * reasons:
	 * 1. No copy function is known for element's type.
	 * 2. The copy function fails or throws.
	 *
	 * @param element is the element to copy.
	 * @return a copy of element, or null.
	 * @throw NullPointerException if element is null.
	 */
	public MModelElement copy(MModelElement element, MNamespace ns) {
		// Don't explicitly check if element is null
		CopyFunction f =
			(CopyFunction) copyfunctions.get(element.getClass());
		if (f == null) {
			cat.warn("CopyHelper is unable to copy element of " +
				 "type: " + element.getClass());
			return null;
		}

		try {
			Object args[] = {element, ns};
			return (MModelElement) f.method.invoke(f.object, args);
		} catch (Exception e) {
			cat.error("CopyHelper copy method exception", e);
			System.out.println("except");
			return null;
		}
	}
}

