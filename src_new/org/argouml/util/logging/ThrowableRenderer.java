// $Id$
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

package org.argouml.util.logging;

import org.apache.log4j.or.ObjectRenderer;

import java.io.*;
import java.sql.*;

  /**
   *  Renderer used for <code>log4j</code> to place full information
   *  from { @link java.lang.Throwable } into the log.
   * 
   * @author Thierry Lach
   * @since 0.9.4
   * 
   * @deprecated without replacement since log4j provides this functionality now.
   */

public class ThrowableRenderer implements ObjectRenderer {

	//TODO: remove class after release 0.17 because of deprecation.

    /** 
	Constructor does no special processing.
    */
    public ThrowableRenderer() {
    }
   
    /** Internal worker for the beginning of a throwable log
     */
    private void startThrowable(StringBuffer sb, String name, Throwable t) {
	sb.append(name);
	sb.append(": ");
	sb.append(t.getClass().getName());
	sb.append('\n');
	sb.append(t.getMessage());
	sb.append('\n');
    }

    /** Internal worker for the end of a throwable log
     */
    private void endThrowable(StringBuffer sb, Throwable t) {
	StringWriter sw = new StringWriter();
	t.printStackTrace(new PrintWriter(sw));
	sb.append(sw.toString());
	sb.append('\n');
    }

    /**
       Render a {@link java.lang.Throwable}.
    */
    public String doRender(Object o) {
	StringBuffer sb = new StringBuffer();

	// Put the most specialized exceptions before simpler ones.
	if (o instanceof SQLException) {  
	    SQLException se = (SQLException) o;
	    startThrowable (sb, "SQLException", (Throwable) o);
	    sb.append("SQLState: ");
	    sb.append(se.getSQLState());
	    sb.append('\n');
	    endThrowable (sb, (Throwable) o);
	    return sb.toString();
	}
	else if (o instanceof Throwable) {  
	    // This is a generic handler that will get called if
	    // nothing else handles before it.
	    Throwable t = (Throwable) o;
	    //
	    startThrowable (sb, "Exception", t);
	    endThrowable (sb, t);
	    return sb.toString();
	} else {
	    return o.toString();
	}
    }
}
