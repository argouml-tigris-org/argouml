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
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Translator {
    
    private static AbstractCognitiveTranslator translator;
    
    public static void setTranslator(AbstractCognitiveTranslator trans) {
        translator = trans;
    }
    
    public static String localize(String key) {
        return (translator != null) ? translator.i18nlocalize(key) : key;
    }
    
    public static String messageFormat(String key, Object[] args) {
        return (translator != null) ? translator.i18nmessageFormat(key, args) : key;
    }

}
