// Copyright (c) 1996-2002 The Regents of the University of California. All
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
package org.argouml.util.logging;

/**
 * The <code>Console</code> class provides static methods for console
 * (System.out.println) output, with duplication of output in
 * the debug logs if enabled.
 */
public class Console
{
    /** Define a static {link org.apache.commons.logging.Log} logger
     *  variable for ArgoUML to log to the console.
     */
    public final static org.apache.commons.logging.Log logger =
         org.apache.commons.logging.LogFactory.getLog("org.argouml.console");

    /**
     * Write an info message to the console, and to the log if enabled.
     */
    public static void info(String s) {
        logger.info(s);
        System.out.println (s);
    }

    /**
     * Write a warning message to the console, and to the log if enabled.
     */
    public static void warn(String s) {
        logger.warn(s);
        System.out.println ("WARNING: " + s);
    }

    /**
     * Write an error message and stack trace to the console,
     * and to the log if enabled.
     */
    public static void error(String s, Throwable t) {
        logger.error(s, t);
        System.out.println ("ERROR: " + s);
        t.printStackTrace(System.out);
    }

    /**
     * Write an error message to the console, and to the log if enabled.
     */
    public static void error(String s) {
        logger.error(s);
        System.out.println ("ERROR: " + s);
    }

}
