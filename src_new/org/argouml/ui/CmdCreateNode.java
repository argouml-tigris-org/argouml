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
 * @author Jaap
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
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
	
	private String getCreateClassName() {
		String name = ((Class)_args.get("className")).getName();
		name = name.substring(name.lastIndexOf('.')+1, name.length());
		name = name.substring(1, name.lastIndexOf("Impl"));
		return name;
	}

}
