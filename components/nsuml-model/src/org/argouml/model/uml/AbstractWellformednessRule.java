package org.argouml.model.uml;

// import org.argouml.application.api.Argo;

import org.workingfrog.i18n.util.Translator;

import ru.novosoft.uml.MBase;

/**
 * Rule definition for wellformedness of some modelelement. In the UML 1.3 spec
 * so called wellformednessrules are defined. This class is the abstract superclass 
 * of implementations of these rules.
 * <p>
 * In several build methods in the uml factories these rules are used. Furthermore
 * they are used in the proppanels to veto some changes.
 * </p>
 * 
 * @author jaap.branderhorst@xs4all.nl
 * @since argouml 0.11.3
 */
public abstract class AbstractWellformednessRule {
	/**
	 * the message key to be looked up to show the message to the user
	 */
	private String _key; 
	
	/**
	 * Checks if the combination of the element and the newValue give a wellformed
	 * result
	 */
	public abstract boolean isWellformed(MBase element, Object newValue);
	
	/**
	 * Returns the localized user message
	 */
	public String getUserMessage() {
		// TODO Use a different bundle name
		return Translator.localize("UMLMenu", "wellformednessrule." + _key);
	}
	
	/**
	 * sets the message key. The message key here is of the form elementname.propertytocheck
	 * <p>
	 * Example:
	 * </p>
	 * <p> 
	 * Say you want to check the namespace of an association. The key will be here
	 * association.namespace
	 * </p>
	 * Keys are looked up in UMLResourceBundle for the time being. Keys start with
	 * wellformednessrule. over there. This is added to the key entered here.
	 */
	public void setUserMessageKey(String key) {
		_key = key;
	}
	
	public AbstractWellformednessRule(String key) {
		_key = key;
	}
	
	public AbstractWellformednessRule() {}

}
