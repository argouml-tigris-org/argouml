// $Id$
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

// Original author: Linus Tolke

package org.argouml.util.logging;

import java.util.*;

/**
 * This class makes it easy to get the time between two or several 
 * points in the code.
 */
public class SimpleTimer {
    String _name = null;
    Vector _points = new Vector();
    Vector _labels = new Vector();

    public SimpleTimer(String n) {
	_name = n;
    }

    public void mark() {
	_points.add(new Long(System.currentTimeMillis()));
	_labels.add(null);
    }

    public void mark(String label) {
	mark();
	_labels.setElementAt(label, _labels.size() - 1);
    }

    /**
     * Returns a string of formatted distances.
     */
    public Enumeration result() {
	mark();
	return new Enumeration() 
	    {
		int count = 1;

		public boolean hasMoreElements() {
		    return count <= _points.size();
		}

		public Object nextElement() {
		    StringBuffer res = new StringBuffer();
		    synchronized (_points) {
			if (count < _points.size()) {
			    if (_labels.get(count - 1) == null)
				res.append("phase ").append(count);
			    else
				res.append(_labels.get(count - 1));
			    res.append("                            ");
			    res.setLength(18);
			    res.append((((Long) _points.elementAt(count)).
					longValue()
					- ((Long) _points.elementAt(count - 1))
					.longValue()));
			}
			else if (count == _points.size()) {
			    res.append("Total                      ");
			    res.setLength(18);
			    res.append((((Long) _points.
					 elementAt(_points.size() - 1))
					.longValue()
					- ((Long) _points.
					   elementAt(0)).longValue()));
			}
		    }
		    count++;
		    return res.toString();
		}
	    };
    }

    public String toString() {
	StringBuffer sb = new StringBuffer("");

	for (Enumeration e = result(); e.hasMoreElements();) {
	    sb.append((String) e.nextElement());
	    sb.append("\n");
	}
	return sb.toString();
    }
}
