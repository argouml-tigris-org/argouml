// Copyright (c) 1996-99 The Regents of the University of California. All
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
package org.argouml.ui;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.argouml.model.uml.UmlFactory;

/** 
 *
 * Command to create nodes with the appropriate modelelement. The modelelement is
 * initialized via the create methods on the uml factories
 * @see org.argouml.model.uml.foundation.core.CoreFactory
 * @author jaap.branderhorst@xs4all.nl
 */
public class CmdCreateNode extends org.tigris.gef.base.CmdCreateNode {

	/**
	 * Constructor for CmdCreateNode.
	 * @param args
	 * @param resource
	 * @param name
	 */
	public CmdCreateNode(Hashtable args, String resource, String name) {
		super(args, resource, name);
	}

	/**
	 * Constructor for CmdCreateNode.
	 * @param args
	 * @param name
	 */
	public CmdCreateNode(Hashtable args, String name) {
		super(args, name);
	}

	/**
	 * Constructor for CmdCreateNode.
	 * @param nodeClass
	 * @param resource
	 * @param name
	 */
	public CmdCreateNode(Class nodeClass, String resource, String name) {
		super(nodeClass, resource, name);
	}

	/**
	 * Constructor for CmdCreateNode.
	 * @param nodeClass
	 * @param name
	 */
	public CmdCreateNode(Class nodeClass, String name) {
		super(nodeClass, name);
	}

	/**
	 * Constructor for CmdCreateNode.
	 * @param nodeClass
	 * @param sticky
	 * @param resource
	 * @param name
	 */
	public CmdCreateNode(
		Class nodeClass,
		boolean sticky,
		String resource,
		String name) {
		super(nodeClass, sticky, resource, name);
	}

	/**
	 * Constructor for CmdCreateNode.
	 * @param nodeClass
	 * @param sticky
	 * @param name
	 */
	public CmdCreateNode(Class nodeClass, boolean sticky, String name) {
		super(nodeClass, sticky, name);
	}
	
	

	/**
	 * Creates a modelelement using the uml model factories.
	 * @see org.tigris.gef.graph.GraphFactory#makeNode()
	 */
	public Object makeNode() {
		// here we should implement usage of the factories
		// since i am kind of lazy i use reflection
		
		// factories
		
	
	try {
		Vector factoryMethods = new Vector();
		Method[] methodArray = UmlFactory.class.getMethods();
		for (int i = 0 ; i<methodArray.length; i++) {
			if (methodArray[i].getName().startsWith("get") && 
				!methodArray[i].getName().equals("getFactory") && 
				!methodArray[i].getName().equals("getClass")) {
					factoryMethods.add(methodArray[i]);
			}
		}
		Iterator it = factoryMethods.iterator();
		while (it.hasNext()) {
			Object factory = ((Method) it.next()).invoke(UmlFactory.getFactory(), new Object[] {});
			List createMethods = Arrays.asList(factory.getClass().getMethods());
			Iterator it2 = createMethods.iterator();
			String classname = getCreateClassName();
			while (it2.hasNext()) {
				Method method = (Method)it2.next();
				String methodname = method.getName();
				if (methodname.endsWith(classname) && methodname.substring(0, methodname.lastIndexOf(classname)).equals("create")) {					
					return method.invoke(factory, new Object[] {});
				}
			}
		}					
	} catch (IllegalAccessException e2) {
	} catch (InvocationTargetException e3) {
	}
	
		return super.makeNode();
	}
	
	/**
	 * returns the name of the uml modelelement without impl, M or the fullname
	 * @return String
	 */
	private String getCreateClassName() {
		String name = ((Class)_args.get("className")).getName();
		name = name.substring(name.lastIndexOf('.')+2, name.length());
		if (name.endsWith("Impl")) {
			name = name.substring(0, name.lastIndexOf("Impl"));
		}
		return name;
	}

}
