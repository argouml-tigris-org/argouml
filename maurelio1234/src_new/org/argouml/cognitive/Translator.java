// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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

/*
 * Created on 21.10.2004
 *
 */
package org.argouml.cognitive;

/**
 * @author MarkusK
 *
 */
public class Translator {

    private static AbstractCognitiveTranslator translator;

    /**
     * @param trans the translator
     */
    public static void setTranslator(AbstractCognitiveTranslator trans) {
        translator = trans;
    }

    /**
     * @param key the key for the string to be localized
     * @return the localized string
     */
    public static String localize(String key) {
        return (translator != null) ? translator.i18nlocalize(key) : key;
    }

    /**
     * @param key the key for the string to be localized
     * @param args arguments that will be inserted in the string
     * @return the localized string containing the arguments
     */
    public static String messageFormat(String key, Object[] args) {
        return (translator != null)
            ? translator.i18nmessageFormat(key, args)
            : key;
    }

}
