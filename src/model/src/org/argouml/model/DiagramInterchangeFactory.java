package org.argouml.model;

import java.util.Collection;
import java.util.List;

public interface DiagramInterchangeFactory {

	public abstract List getModelDiagram();

	public abstract void setModelDiagram(List diagrams);

	/**
	 * Return true of the given ModelElement is the owner of the diagram.
	 */
	public abstract boolean isDiagramOwner(Object modelElement, Object diagram);

	/**
	 * Return true of the given ModelElement is the owner of the diagram.
	 */
	public abstract Object getDiagramElementOwner(Object diagram);

	/**
	 * 
	 * @return
	 */
	public abstract Object createDiagram();

	/**
	 * 
	 * @return
	 */
	public abstract Object createDiagramLink();

	/**
	 * 
	 * @return
	 */
	public abstract Object createEllipse();

	/**
	 * 
	 * @return
	 */
	public abstract Object createGraphConnector();

	/**
	 * 
	 * @return
	 */
	public abstract Object createGraphEdge();

	/**
	 * 
	 * @return
	 */
	public abstract Object createGraphNode();

	/**
	 * 
	 * @return
	 */
	public abstract Object createImage();

	/**
	 * 
	 * @return
	 */
	public abstract Object createPolyline();

	/**
	 * 
	 * @return
	 */
	public abstract Object createProperty();

	/**
	 * 
	 * @return
	 */
	public abstract Object createReference();

	/**
	 * 
	 * @return
	 */
	public abstract Object createSimpleSemanticModelElement();

	/**
	 * 
	 * @return
	 */
	public abstract Object createTextElement();

	/**
	 * 
	 * @return
	 */
	public abstract Object createUml1SemanticModelBridge();

	/**
	 * Creates an instance of BezierPoint structure type.
	 * @param base
	 * @param control1
	 * @param control2
	 * @return
	 */
	public abstract Object createBezierPoint(Object base, Object control1,
			Object control2);

	/**
	 * Creates an instance of Dimension structure type.
	 * @param width
	 * @param height
	 * @return
	 */
	public abstract Object createDimension(double width, double height);

	/**
	 * Creates an instance of Point structure type.
	 * @param x
	 * @param y
	 * @return
	 */
	public abstract Object createPoint(double x, double y);

	/**
	 * 
	 * @param modelElement
	 * @param diagram
	 * @return
	 */
	public abstract Object buildUml1SemanticModelBridge(Object modelElement,
			Object diagramOrGraphElement);

	public abstract Object buildSimpleSemanticModelElement(Object grafElement,
			Object diagram, String presentation, String typeInfo);

	/**
	 * Build a diagram
	 * @param model
	 * @return A new diagram
	 */
	public abstract Object buildDiagram(Object model);

	public abstract Object buildGraphNode(Object parentGraphElement,
			Object modelElement);

	public abstract Object buildProperty(String key, String value);

	// ADiagramElementProperty delegate methods

	/* (non-Javadoc)
	 * @see org.omg.uml.diagraminterchange.ADiagramElementProperty#add(org.omg.uml.diagraminterchange.DiagramElement, org.omg.uml.diagraminterchange.Property)
	 */
	public abstract boolean addProperty(Object arg0, Object arg1);

	/* (non-Javadoc)
	 * @see org.omg.uml.diagraminterchange.ADiagramElementProperty#exists(org.omg.uml.diagraminterchange.DiagramElement, org.omg.uml.diagraminterchange.Property)
	 */
	public abstract boolean existsProperty(Object arg0, Object arg1);

	/* (non-Javadoc)
	 * @see org.omg.uml.diagraminterchange.ADiagramElementProperty#getProperty(org.omg.uml.diagraminterchange.DiagramElement)
	 */
	public abstract Collection getProperties(Object arg0);

	/* (non-Javadoc)
	 * @see org.omg.uml.diagraminterchange.ADiagramElementProperty#remove(org.omg.uml.diagraminterchange.DiagramElement, org.omg.uml.diagraminterchange.Property)
	 */
	public abstract boolean removeProperty(Object arg0, Object arg1);

	//easy interface to properties
	public abstract boolean hasProperty(Object diagramElement,
			String propertyName);

	public abstract void setProperty(Object diagramElement, String key,
			String value);

	public abstract String getProperty(Object diagramElement, String key);

}