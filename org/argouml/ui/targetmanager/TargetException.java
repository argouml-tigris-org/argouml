// $Id$
// Copyright (c) 2002 The Regents of the University of California. All
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
package org.argouml.ui.targetmanager;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * @author gebruiker
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class TargetException extends RuntimeException {

    /** The cause of this exception, or null */
    private Throwable cause;

    /**
     * 
     */
    public TargetException() {
	super();
	// TODO Auto-generated constructor stub
    }

    /**
     * @param message
     */
    public TargetException(String message) {
	super(message);
	// TODO Auto-generated constructor stub
    }

    /**
     * @param message
     * @since 0.15.2
     */
    public TargetException(String message, Throwable cause) {
	super(message);
	this.cause = cause;
    }

    /*
     * @see java.lang.RuntimeException#toString
     */
    public String toString() {
	if (cause == null)
		return super.toString();
	else
		return super.toString() + " caused by: " + cause.toString();
    }

    /*
     * @see java.lang.RuntimeException#printStackTrace()
     */
    public void printStackTrace() {
	super.printStackTrace();
	if (cause != null) {
	    System.err.println("Caused by:");
	    cause.printStackTrace();
	}
    }

    /*
     * @see java.lang.RuntimeException#printStackTrace(PrintStream)
     */
    public void printStackTrace(PrintStream s) {
	super.printStackTrace();
	if (cause != null) {
	    s.println("Caused by:");
	    cause.printStackTrace();
	}
    }

    /*
     * @see java.lang.RuntimeException#printStackTrace(PrintWriter)
     */
    public void printStackTrace(PrintWriter s) {
	super.printStackTrace();
	if (cause != null) {
	    s.println("Caused by:");
	    cause.printStackTrace();
	}
    }
}

