/**
 * DiagramInfo.java
 * @author  Paolo Pinciani (pinciani@cs.unibo.it)
 *
 * Copyright 2001-2002 Department of Computer Science
 * University of Bologna
 * Mura Anteo Zamboni 7, 40127 Bologna, ITALY
 * Tel: +39 051 35.45.16
 * Fax: +39 051 35.45.10
 * Web: http://cs.unibo.it
 */
 
package Argo2Elmuth;

import java.awt.Rectangle;

/**
 * <code>DiagramInfo</code> class collects information about an ArgoUML diagram:
 * diagran name, diagram type, diagram size.
 */
public class DiagramInfo {
	private String name;
	private String type;
	private Rectangle size;
	private int nodes;

   /**
	* Constructor.
	*/
	public DiagramInfo() { }
	
   /**
	* Returns the name of the diagram.
	* @return The name of the diagram.
	*/	
	public String getName() {
		return name;
	}
   /**
	* Sets the name of the diagram.
	* @param The name of the diagram.
	*/
	public void setName(String name) {
		this.name = name;
	}

   /**
	* Returns the type of the diagram (e.g.: "UMLClassDiagram").
	* @return The type of the diagram.
	*/		
	public String getType() {
		return type;
	}
   /**
	* Sets the type of the diagram (e.g.: "UMLClassDiagram").
	* @param The type of the diagram.
	*/
	public void setType(String type) {
		this.type = type;
	}

   /**
	* Returns the size of the diagram.
	* @return The size  of the diagram.
	*/		
	public Rectangle getSize() {
		return size;
	}
   /**
	* Sets the size of the diagram.
	* @param The size of the diagram.
	*/
	public void setSize(Rectangle size) {
		this.size = size;
	}
	
   /**
	* Setsthe number of nodes (i.e. menu elements) used
	* int the left side menu of this diagram.
	* @param The number of nodes.
	*/	
	public void setNodesNumber(int nodes) {
		this.nodes = nodes;
	}
   /**
	* Returns the number of nodes (i.e. menu elements) used
	* int the left side menu of this diagram.
	* @return The number of nodes.
	*/	
	public int getNodesNumber() {
		return nodes;
	}
}