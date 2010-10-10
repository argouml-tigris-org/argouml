/* $Id$
 *******************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    tfmorris
 *******************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2006 The Regents of the University of California. All
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

package org.argouml.model;


/**
 * Exception for problems with XMI files.
 * 
 * @author Tom Morris
 */
public class XmiException extends UmlException {
    

    private String publicId;

    private String systemId;

    private int lineNumber;

    private int columnNumber;

    /**
     * Construct an XmiException with the given message.
     * 
     * @param message the message
     */
    public XmiException(String message) {
        super(message);
    }

    /**
     * Construct an exception with a message and a causing exception.
     *
     * @param message the message
     * @param c the cause of the exception
     */
    public XmiException(String message, Throwable c) {
        super(message, c);
    }

    /**
     * Construct an exception with a causing exception.
     *
     * @param c the cause of the exception
     */
    public XmiException(Throwable c) {
        super(c);
    }
    
    /**
     * Create a new XmiException.
     *
     * @param message The error or warning message.
     * @param pubId The public identifier of the entity that generated
     *              the error or warning.
     * @param sysId The system identifier of the entity that generated
     *              the error or warning.
     * @param line The line number of the end of the text that
     *             caused the error or warning.
     * @param column The column number of the end of the text that
     *               cause the error or warning.
     */
    public XmiException(String message, String pubId, String sysId,
            int line, int column) {
        super(message);
        init(pubId, sysId, line, column);
    }
    
    
    /**
     * Create a new XmiException with an embedded exception.
     * 
     * @param message The error or warning message, or null to use the message
     *                from the embedded exception.
     * @param pubId The public identifier of the entity that generated the
     *              error or warning.
     * @param sysId The system identifier of the entity that generated the
     *              error or warning.
     * @param line The line number of the end of the text that caused the
     *             error or warning.
     * @param column The column number of the end of the text that cause
     *               the error or warning.
     * @param e Another exception to embed in this one.
     */
    public XmiException(String message, String pubId, String sysId,
            int line, int column, Exception e) {
        super(message, e);
        init(pubId, sysId, line, column);
    }


    /**
     * Internal initialization method.
     * 
     * @param pubId The public identifier of the entity which generated the
     *              exception, or null.
     * @param sysId The system identifier of the entity which generated the
     *              exception, or null.
     * @param line The line number of the error, or -1.
     * @param column The column number of the error, or -1.
     */
    private void init(String pubId, String sysId, int line,
            int column) {
        this.publicId = pubId;
        this.systemId = sysId;
        lineNumber = line;
        columnNumber = column;
    }
    
    
    /**
     * Get the public identifier of the entity where the exception occurred.
     * 
     * @return A string containing the public identifier, or null if none is
     *         available.
     */
    public String getPublicId() {
        return this.publicId;
    }
    
    
    /**
     * Get the system identifier of the entity where the exception occurred.
     * <p>
     * If the system identifier is a URL, it will have been resolved fully.
     * </p>
     * 
     * @return A string containing the system identifier, or null if none is
     *         available.
     */
    public String getSystemId() {
        return this.systemId;
    }
    
    
    /**
     * The line number of the end of the text where the exception occurred.
     *
     * <p>The first line is line 1.</p>
     *
     * @return An integer representing the line number, or -1
     *         if none is available.
     */
    public int getLineNumber() {
        return this.lineNumber;
    }
    
    
    /**
     * The column number of the end of the text where the exception occurred.
     *
     * <p>The first column in a line is position 1.</p>
     *
     * @return An integer representing the column number, or -1
     *         if none is available.
     */
    public int getColumnNumber() {
        return this.columnNumber;
    }


}
