/*
 * Created on Jun 26, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.argouml.model.uml;

/**
 * @author thierry
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
class NsumlObjectInfo {
	
	private Object factory;

	private String createMethod;

	private Class javaClass;

	NsumlObjectInfo (Object factory, Class javaClass, String method) {
		this.factory = factory;
		this.javaClass = javaClass;
		this.createMethod = method;
	}
	/**
	 * @return
	 */
	public Class getJavaClass() {
		return javaClass;
	}

	/**
	 * @return
	 */
	public String getCreateMethod() {
		return createMethod;
	}

	/**
	 * @return
	 */
	public Object getFactory() {
		return factory;
	}

}
