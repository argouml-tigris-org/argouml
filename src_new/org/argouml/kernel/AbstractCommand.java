// $Id$
// Copyright (c) 1996-2007 The Regents of the University of California. All
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

package org.argouml.kernel;

/**
 * The base class of all mementos. Any undoable methods should create a
 * concrete instance that will undo (and possibly redo) that method.
 * @author Bob Tarling
 */
public abstract class AbstractCommand implements Command {
    /**
     * Set by the undo framework to flag the first memento of a chain of
     * mementos that represent a single user interaction with the application.
     */
    private boolean startChain;
    
    /**
     * Determine if this is the start of a chain of mementos
     * @return true if this is the start of a memento chain
     */
    protected boolean isStartChain() {
        return startChain;
    }
    
    /**
     * Flag a memento as being the start of a user interaction
     */
    void startChain() {
        startChain = true;
    }
    
    /**
     * 
     * @see org.argouml.kernel.Command#execute()
     */
    public abstract void execute();
    
    public String toString() {
        return (isStartChain() ? "*" : " ") + this.getClass().getName();
    }
}
