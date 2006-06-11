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

package org.argouml.persistence;

import java.io.PrintStream;
import java.io.PrintWriter;

import org.xml.sax.SAXException;

/**
 * An exception to be thrown during failure of a opening
 * and reading some storage medium.
 * @author Bob Tarling
 */
public class OpenException extends PersistenceException {

    /**
     * The constructor.
     *
     * @param message the message to show
     */
    public OpenException(String message) {
        super(message);
    }

    /**
     * The constructor.
     *
     * @param message the message to show
     * @param cause the cause for the exception
     */
    public OpenException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * The constructor.
     *
     * @param cause the cause for the exception
     */
    public OpenException(Throwable cause) {
        super(cause);
    }


    /**
     * @see java.lang.Throwable#printStackTrace()
     */
    public void printStackTrace() {
        super.printStackTrace();
        if (getCause() instanceof SAXException
                && ((SAXException) getCause()).getException() != null) {
            ((SAXException) getCause()).getException().printStackTrace();
        }
    }

    /**
     * @see java.lang.Throwable#printStackTrace(java.io.PrintStream)
     */
    public void printStackTrace(PrintStream ps) {
        super.printStackTrace(ps);
        if (getCause() instanceof SAXException
                && ((SAXException) getCause()).getException() != null) {
            ((SAXException) getCause()).getException().printStackTrace(ps);
        }
    }

    /**
     * @see java.lang.Throwable#printStackTrace(java.io.PrintWriter)
     */
    public void printStackTrace(PrintWriter pw) {
        super.printStackTrace(pw);
        if (getCause() instanceof SAXException
                && ((SAXException) getCause()).getException() != null) {
            ((SAXException) getCause()).getException().printStackTrace(pw);
        }
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = -4787911270548948677L;
}

