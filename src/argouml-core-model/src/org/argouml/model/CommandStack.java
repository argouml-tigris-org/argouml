// $Id: CommandStack.java 13420 2007-08-20 16:59:01Z b00__1 $
// Copyright (c) 2007 The Regents of the University of California. All
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
 * This class contains methods for controlling the commands applied to the model
 * as a stack of commands. There are methods to undo a command (moving the stack
 * pointer down to the previous executed command) or to redo a command (moving
 * the stack pointer up, to the next command in the stack)
 * 
 * @deprecated for 0.25.5 by tfmorris.  Use ArgoUMLs command stack.
 */
@Deprecated
public interface CommandStack {

    /**
     * The event's name that is triggered when the command stack executes a
     * command.
     */
    String COMMAND_STACK_UPDATE_EVENT = "COMMAND_STACK_CHANGED";

    /**
     * This is a basic default instance of this interface.
     */
    CommandStack DEFAULT_INSTANCE = new CommandStack() {

        public boolean canRedo() {
            return false;
        }

        public boolean canUndo() {
            return false;
        }

        public String getRedoLabel() {
            return null;
        }

        public String getUndoLabel() {
            return null;
        }

        public boolean isCommandStackCapabilityAvailable() {
            return false;
        }

        public void redo() {
        }

        public void undo() {
        }

    };

    /**
     * Determines if the command stack operations are supported by the current
     * implementation.
     * 
     * @return true if the operations of this interface are implemented
     */
    boolean isCommandStackCapabilityAvailable();

    /**
     * Undo the most recent command.
     * <p>
     * If canUndo() returns false this will have no effect.
     */
    void undo();

    /**
     * Redo the most recent undone command.
     * <p>
     * If canRedo() returns false this will have no effect.
     */
    void redo();

    /**
     * Verifies if the command stack can undo the last changes in the model.
     * 
     * @return true if undo is possible, false otherwise
     */
    boolean canUndo();

    /**
     * Verifies if the command stack can redo the last undone command.
     * 
     * @return true if redo is possible, false otherwise
     */
    boolean canRedo();

    /**
     * Describes the command to undo.
     * <p>
     * If canUndo() returns false then this will return null.
     * 
     * @return a description of the current undo command or null
     */
    String getUndoLabel();

    /**
     * Describes the command to redo.
     * <p>
     * If canRedo() returns false then this will return null.
     * 
     * @return a description of the current redo command or null
     */
    String getRedoLabel();

}
