// $Id:UMLDiagramNameDocument.java 12583 2007-05-09 20:11:45Z mvw $
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

// $header$
package org.argouml.uml.ui.foundation.core;

import java.beans.PropertyVetoException;

import org.argouml.kernel.ProjectManager;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.ui.UMLPlainTextDocument;

/**
 */
public class UMLDiagramNameDocument extends UMLPlainTextDocument {

    /**
     * Constructor.
     */
    public UMLDiagramNameDocument() {
        super("name");
    }

    /*
     * @see org.argouml.uml.ui.UMLPlainTextDocument#setProperty(java.lang.String)
     */
    protected void setProperty(String text) {
        Object target = ProjectManager.getManager().getCurrentProject()
                .getActiveDiagram();
        if (target instanceof ArgoDiagram) {
            try {
                ((ArgoDiagram) target).setName(text);
            } catch (PropertyVetoException e) {
                // TODO: what shall we do with the exception?
            }
        }
    }

    /*
     * @see org.argouml.uml.ui.UMLPlainTextDocument#getProperty()
     */
    protected String getProperty() {
        Object target = ProjectManager.getManager().getCurrentProject()
                .getActiveDiagram();
        if (target instanceof ArgoDiagram) {
            return ((ArgoDiagram) target).getName();
        }
        return "";
    }

}
