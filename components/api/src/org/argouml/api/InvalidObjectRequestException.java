// Copyright (c) 2003 The Regents of the University of California. All
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


package org.argouml.api;

/**
 * Allows any factory to post the fact that it got an invalid request.
 * 
 * @author  Thierry Lach
 */
public class InvalidObjectRequestException extends RuntimeException {

	// TODO Determine if this should be an Exception or a RuntimeException
	    
	/**
	 *  The object that was not recognized as being valid
	 */
	private Object reason;

	/**
	 *  The root cause of the request failure if caused by an exception.
	 */
	private Throwable cause;


	/**
	 * @param string Text describing why the requested object was not created.
	 * @param reason The identifying class passed to the factory which was not recognized.
	 */
	public InvalidObjectRequestException(String message, Object reason) {
        this(message, reason, null);
	}


	/**
	 * @param string Text describing why the requested object was not created.
	 * @param reason The identifying class passed to the factory which was not recognized.
	 * @param cause  The underlying exception which caused the object creation to fail.
	 */
	public InvalidObjectRequestException(String message,
	                                     Object reason,
	                                     Throwable cause) {
		super(message);
		this.reason = reason;
		this.cause = cause;
	}


	/**
	 * @return the underlying exception which caused the object creation to fail, or null if not directly caused by an exception.
	 */
	public Throwable getCause() {
		return cause;
	}

	/**
	 * @return the identifying class which was not recognized.
	 */
	public Object getReason() {
		return reason;
	}

}
