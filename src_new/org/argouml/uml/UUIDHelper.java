// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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

package org.argouml.uml;

import org.apache.log4j.Logger;
import org.argouml.model.ModelFacade;
import org.argouml.uml.diagram.static_structure.ui.CommentEdge;

/**
 * @stereotype singleton
 */
public class UUIDHelper {

    /**
     * The logger.
     */
    private static final Logger LOG = Logger.getLogger(UUIDHelper.class);

    ////////////////////////////////////////////////////////////////
    // static variables

    private static final UUIDHelper INSTANCE = new UUIDHelper();

    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     * Constructor for the UUIDManager. This is private to make sure that
     * we are a proper singleton.
     *
     * @deprecated by Linus Tolke as of 0.15.4. Will be made private.
     * Use the UUIDManager singleton instead.
     */
    protected UUIDHelper() { }

    /**
     * Return the UUIDManager singleton.
     *
     * @return an UUIDManager
     */
    public static UUIDHelper getInstance() {
        return INSTANCE;
    }

    /**
     * Return the UUID of the element.
     *
     * @param base base element (MBase type)
     * @return UUID
     */
    public String getUUID(Object base) {
        if (base instanceof CommentEdge) {
            return (String) ((CommentEdge) base).getUUID();
        }
        return ModelFacade.getUUID(base);
    }

    /**
     * @return a new UUID
     */
    public String getNewUUID() {
        return org.argouml.model.UUIDManager.getInstance().getNewUUID();
    }

} /* end class UUIDManager */
