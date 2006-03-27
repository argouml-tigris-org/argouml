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

package org.argouml.util.logging;

import java.util.Enumeration;
import java.util.Vector;

/**
 * This class makes it easy to get the time between two or several
 * points in the code.
 *
 * @author Linus Tolke
 */
public class SimpleTimer {
    private Vector points = new Vector();
    private Vector labels = new Vector();

    /**
     * The constructor. Creates a simple timer.
     */
    public SimpleTimer() {
    }

    /**
     * Mark (Store) the current time.
     */
    public void mark() {
	points.add(new Long(System.currentTimeMillis()));
	labels.add(null);
    }

    /**
     * Mark (Store) the current time.
     *
     * @param label the mark will be labelled with this string
     */
    public void mark(String label) {
	mark();
	labels.setElementAt(label, labels.size() - 1);
    }

    /**
     * Returns a string of formatted distances.
     *
     * @return a string representing the results
     */
    public Enumeration result() {
	mark();
	return new SimpleTimerEnumeration();
    }

    /**
     * An enumeration to walk through all entries.<p>
     *
     * This allows us to get a summary at the end.
     *
     * @author Linus Tolke
     */
    class SimpleTimerEnumeration implements Enumeration {
        /**
         * Keep track of where we are in the list.
         */
        private int count = 1;

        /**
         * @see java.util.Enumeration#hasMoreElements()
         */
        public boolean hasMoreElements() {
            return count <= points.size();
        }

        /**
         * @see java.util.Enumeration#nextElement()
         */
        public Object nextElement() {
            StringBuffer res = new StringBuffer();
            synchronized (points) {
                if (count < points.size()) {
                    if (labels.get(count - 1) == null) {
                        res.append("phase ").append(count);
                    } else {
                        res.append(labels.get(count - 1));
                    }
                    res.append("                            ");
                    res.append("                            ");
                    res.setLength(60);
    		    res.append((((Long) points.elementAt(count)).
    		            longValue()
    		            - ((Long) points.elementAt(count - 1))
    		            .longValue()));
                } else if (count == points.size()) {
                    res.append("Total                      ");
                    res.setLength(18);
                    res.append((((Long) points.
                            elementAt(points.size() - 1))
                            .longValue()
                            - ((Long) points.
                                    elementAt(0)).longValue()));
                }
            }
            count++;
            return res.toString();
        }
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
	StringBuffer sb = new StringBuffer("");

	for (Enumeration e = result(); e.hasMoreElements();) {
	    sb.append((String) e.nextElement());
	    sb.append("\n");
	}
	return sb.toString();
    }
}
