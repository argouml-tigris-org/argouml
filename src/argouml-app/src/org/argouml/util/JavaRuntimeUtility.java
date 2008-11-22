// $Id: JavaRuntimeUtility.java 11531 2006-11-27 17:40:06Z bobtarling $
// Copyright (c) 2008 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

package org.argouml.util;

/**
 * Some utility methods to simplify tests of what Java Runtime is currently in
 * use.
 */
public class JavaRuntimeUtility {
    
    /**
     * Determine if the java runtime found is supported by the current version
     * of ArgoUML.
     * 
     * @return true if the version of Java is supported
     */
    public static boolean isJreSupported() {
        String javaVersion = System.getProperty("java.version", "");
        return (!(javaVersion.startsWith("1.4") 
                || javaVersion.startsWith("1.3")
                || javaVersion.startsWith("1.2")
                || javaVersion.startsWith("1.1")));
    }
        
    /**
     * Determine if the java runtime found is JRE 5
     * <p>Note that plugins should not rely on this method existing in
     * it is likely to be removed without deprecation as soon as JRE 5 is
     * no longer supported.
     * 
     * @return true if we're running on JRE 5
     */
    public static boolean isJre5() {
        String javaVersion = System.getProperty("java.version", "");
        return (javaVersion.startsWith("1.5"));
    }
    
    /**
     * Get the JRE version described in system properties
     * @return the JRE version
     */
    public static String getJreVersion() {
        return System.getProperty("java.version", "");
    }
}
