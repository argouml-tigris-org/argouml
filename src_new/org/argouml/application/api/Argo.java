// Copyright (c) 1996-2001 The Regents of the University of California. All
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

package org.argouml.application.api;
import org.apache.log4j.*;
import org.apache.log4j.helpers.*;
import java.util.*;
import java.io.*;

/**
*/

public class Argo {

   /** Standard definition of the logging category for the console.
    */
   public static final String CONSOLE_LOG = "argo.console.log";

   /** Standard definition of system variable to add text prefix to console log.
    */
   public static final String ARGO_CONSOLE_PREFIX = "argo.console.prefix";

   /** Define a static log4j category variable for ArgoUML to log to
    * the console.
    */
   public final static Category log;

   /** Don't let this be instantiated. */
   private Argo() {
  }

   /** Instance initialization to create
    *  logging category <code>argo.console.log</code>.
    */
   static {
       Category newCategory = Category.getInstance(CONSOLE_LOG);
       newCategory.addAppender(
            new ConsoleAppender(
                new PatternLayout(System.getProperty(ARGO_CONSOLE_PREFIX, "") 
                                  + "%m%n"), ConsoleAppender.SYSTEM_OUT));
       log = newCategory;
   }
}



