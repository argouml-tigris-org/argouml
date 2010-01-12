/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    mvw
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2003-2007 The Regents of the University of California. All
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

package org.argouml.uml.ui;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import org.argouml.model.Model;

/**
 * This class provides a text field that can be used to display
 * the text of all Comments associated with an annotated ModelElement.
 *
 * TODO: This could use more work.  Currently it returns all
 * Comment.name or Comment.body attributes concatenated together
 * into a single read-only document.
 *
 * @since 1 Nov 2005
 * @author Tom Morris (tfmorris@gmail.com)
 */
public class UMLModelElementCommentDocument extends UMLPlainTextDocument {

    private boolean useBody;

    /**
     * Creates a UMLPlainTextDocument object that represents the text of a
     * Comment associated with a ModelElement
     *
     * @param useBody
     *            use the UML 1.4 body attribute instead of the UML 1.3 name
     *            attribute
     */
    public UMLModelElementCommentDocument(boolean useBody) {
        super("comment");
        this.useBody = useBody;
    }

    /**
     * Add a Comment with the given string
     *
     * TODO: Currently a no-op, doc is read only
     *
     * @param text the property
     */
    protected void setProperty(String text) {
//        if (Model.getFacade().isAModelElement(getTarget())) {
//            Model.getCoreHelper().addComment(
//                    getTarget(),
//                    text);
//        }
    }

    /**
     * Get the text of all comments annotating this Model Element
     *
     * @return the text of all comments
     */
    protected String getProperty() {
        StringBuffer sb = new StringBuffer();
        Collection comments = Collections.EMPTY_LIST;
        if (Model.getFacade().isAModelElement(getTarget())) {
            comments = Model.getFacade().getComments(getTarget());
        }
        for (Iterator i = comments.iterator(); i.hasNext();) {
            Object c = i.next();
            String s;
            if (useBody) {
                s = (String) Model.getFacade().getBody(c);
                //sb.append((String) Model.getFacade().getBody(c));
            } else {
                s = Model.getFacade().getName(c);
                //sb.append(Model.getFacade().getName(c));
            }
            if (s == null) {
                s = "";
            }
            sb.append(s);
            sb.append(" // ");
        }
        if (sb.length() > 4) {
            return (sb.substring(0, sb.length() - 4)).toString();
        } else {
            return "";
        }
    }
}
