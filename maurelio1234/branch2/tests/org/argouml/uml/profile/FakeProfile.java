package org.argouml.uml.profile;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.argouml.model.Model;

/**
 * This class simulates a real profile, containing a model and some strategies
 * 
 * @author Marcos Aurélio
 */
public class FakeProfile extends Profile {
	@SuppressWarnings("unchecked")
	private Collection model = null;
	private FigNodeStrategy figNodeStrategy = null;
	private FormatingStrategy formatStrategy = null;

	/**
	 * This class simulates a FigNodeStrategy
	 * 
	 * @author Marcos Aurélio
	 */
	private class FakeFigNodeStrategy implements FigNodeStrategy {

		public Image getIconForStereotype(Object stereotype) {
			return null;
		}

	}

	/**
	 * This class simulates a FormatingStrategy
	 * 
	 * @author Marcos Aurélio
	 */
	private class FakeFormatingStrategy implements FormatingStrategy {

		@SuppressWarnings("unchecked")
		public String formatCollection(Iterator iter, Object namespace) {
			return "";
		}

		public String formatElement(Object element, Object namespace) {
			return "";
		}

	}

	@SuppressWarnings("unchecked")
	public FakeProfile() {
		model = new ArrayList();
		model.add(Model.getModelManagementFactory().createModel());
		figNodeStrategy = new FakeFigNodeStrategy();
		formatStrategy = new FakeFormatingStrategy();
	}

	public String getDisplayName() {
		return toString();
	}

	public FigNodeStrategy getFigureStrategy() {
		return figNodeStrategy;
	}

	public FormatingStrategy getFormatingStrategy() {
		return formatStrategy;
	}

	public FigNodeStrategy getFigNodeStrategy() {
		return figNodeStrategy;
	}

	public void setFigNodeStrategy(FigNodeStrategy figNodeStrategy) {
		this.figNodeStrategy = figNodeStrategy;
	}

	public FormatingStrategy getFormatStrategy() {
		return formatStrategy;
	}

	public void setFormatStrategy(FormatingStrategy formatStrategy) {
		this.formatStrategy = formatStrategy;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection getProfilePackages() throws ProfileException {
		return new ArrayList();
	}

}
