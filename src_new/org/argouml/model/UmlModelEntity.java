// $Id$
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

package org.argouml.model;


/** An interface which all ArgoUML-recognized UML model types
 * must implement.
 * 
 * @author Thierry Lach
 */
public class UmlModelEntity extends AbstractModelEntity {

    private boolean availableInFacade;
    private boolean creatable;

    /**
     * @param string the entity name
     */
    public UmlModelEntity(String string) {
        this(string, true, true);
    }

    /**
     * @param string the entity name
     * @param isCreatable true if UmlFactory can create it using create()
     * @param isFacade if the entity can be identified using ModelFacade
     * @deprecated facade is to be removed
     */
    public UmlModelEntity(String string,
                          boolean isCreatable,
                          boolean isFacade) {
        super(string);
        availableInFacade = isFacade;
        creatable = isCreatable;
    }

    /**
     * @return true if ModelFacade can identify
     * @deprecated facade is to be removed
     */
    public boolean isAvailableInFacade() {
        return availableInFacade;
    }

    /**
     * @return true if UmlFactory.create() can create
     */
    public boolean isCreatable() {
        return creatable;
    }

}

