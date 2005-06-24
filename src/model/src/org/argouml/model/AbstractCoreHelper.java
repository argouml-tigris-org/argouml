// $Id$
// Copyright (c) 2005 The Regents of the University of California. All
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
 * Solves CoreHelper common things for all Model implementations.
 *
 * @author Linus Tolke
 */
public abstract class AbstractCoreHelper implements CoreHelper {
    /**
     * Sets if some model element is abstract.
     *
     * @param handle is the classifier
     * @param flag is true if it should be abstract
     */
    protected abstract void realSetAbstract(Object handle, boolean flag);

    /**
     * Sets if some model element is abstract.
     *
     * @param handle is the classifier
     * @param flag is true if it should be abstract
     */
    public void setAbstract(final Object handle, final boolean flag) {
        ModelMemento memento =
            Model.notifyMementoCreationObserver(new ModelMemento() {
                private boolean oldValue =
                    Model.getFacade().isAbstract(handle);
                public void undo() {
                    realSetAbstract(handle, oldValue);
                }
                public void redo() {
                    realSetAbstract(handle, flag);
                }
            }
            );

        memento.redo();
    }
}
