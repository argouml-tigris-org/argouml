package org.argouml.uml.ui.foundation.core;

import javax.swing.JTextField;

import org.argouml.uml.ui.UMLPlainTextDocument;
import org.argouml.uml.ui.UMLTextField2;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.TemplateParameter;

public class PropPanelTemplateParameter extends PropPanelStructuralFeature {

	private static final long serialVersionUID = 8466952187322427678L;
	
	public PropPanelTemplateParameter() {
		super("label.template-parameter", null);
		addField("Name",  new UMLTextField2(new UMLTemplateParameterDocument("TemplateParameter")));
		// TODO: we don't need Type and DefaultValue for Java but they should be implemented to comply with UML 
		addField("Type", new JTextField(""));
		addField("Default Value", new JTextField(""));
	}
	
	public static class UMLTemplateParameterDocument extends UMLPlainTextDocument {

		private static final long serialVersionUID = -2597673735297949111L;

		public UMLTemplateParameterDocument(String name) {
			super(name);
		}
		
		private ModelElement getParameter() {
			Object target = getTarget();
			if (target instanceof TemplateParameter) {
				Object parameter =  ( (TemplateParameter)target).getParameter();
				if (parameter instanceof ModelElement) {
					return (ModelElement)parameter;
				}
				throw new IllegalArgumentException(parameter.getClass().getName() +" is not a ModelElement");
			}
			throw new IllegalArgumentException(target.getClass().getName() +" is not a TemplateParameter");
		}
		
		@Override
		protected void setProperty(String text) {
			getParameter().setName(text);
		}

		@Override
		protected String getProperty() {
			return getParameter().getName();
		}
	}

}
