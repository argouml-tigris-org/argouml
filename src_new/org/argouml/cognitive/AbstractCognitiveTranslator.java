/*
 * Created on 21.10.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.argouml.cognitive;

/**
 * @author MarkusK
 *
 */
public abstract class AbstractCognitiveTranslator {
    
    public abstract String i18nlocalize(String key);
    
    public abstract String i18nmessageFormat(String key, Object[] args);

}
