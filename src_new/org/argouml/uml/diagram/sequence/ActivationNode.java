// $Id$
// Copyright (c) 2003-2004 The Regents of the University of California. All
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

package org.argouml.uml.diagram.sequence;

public class ActivationNode extends Node {
    private boolean _cutOffTop;

    private boolean _cutOffBottom;

    private boolean _end;

    private boolean _start;

    public ActivationNode() { }

    public boolean isStart() {
        return _start;
    }

    /**
     * @param b
     */
    public void setStart(boolean b) {
        _start = b;
    }

    public void setEnd(boolean end) {
        _end = end;
    }

    public boolean isEnd() {
        return _end;
    }

    public boolean isCutOffBottom() {
        return _cutOffBottom;
    }

    public boolean isCutOffTop() {
        return _cutOffTop;
    }

    /**
     * @param b
     */
    public void setCutOffBottom(boolean b) {
        if (b && !(_end)) {
            throw new IllegalArgumentException("Cannot cutoff "
					       + "an activationNode "
					       + "that is not an end");
        }
        _cutOffBottom = b;
    }

    /**
     * @param b
     */
    public void setCutOffTop(boolean b) {
        if (b && !(_start)) {
            throw new IllegalArgumentException("Cannot cutoff "
					       + "an activationNode "
					       + "that is not a start");
        }
        _cutOffTop = b;
    }

}
