// $Id: eclipse-argo-codetemplates.xml 11347 2006-10-26 22:37:44Z linus $
// Copyright (c) 2008 The Regents of the University of California. All
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

package org.argouml.ui;

import java.awt.event.ActionEvent;
import java.text.MessageFormat;

import javax.swing.AbstractAction;

import org.apache.log4j.Logger;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.IllegalModelElementConnectionException;
import org.argouml.model.Model;
import org.argouml.ui.explorer.ExplorerPopup;

/**
 * An action to create a relation between 2 model elements.
 *
 * @author Bob Tarling
 */
public class ActionCreateEdgeModelElement extends AbstractAction {
    
    private static final Logger LOG =
        Logger.getLogger(ExplorerPopup.class);
    
    private final Object metaType; 
    private final Object source; 
    private final Object dest;

    /**
     * Create a new model element which in graph terminology is an edge,
     * ie a model element that links two other model elements and makes
     * no sense to exist by itelf.
     * @param theMetaType The type of model element to create
     * @param theSource The source model element to link
     * @param theDestination The destination model element to link
     * @param relationshipDescr A textual description that describes how
     *                          source relates to destination
     */
    public ActionCreateEdgeModelElement(
            final Object theMetaType, 
            final Object theSource, 
            final Object theDestination,
            final String relationshipDescr) {
        super(MessageFormat.format(
            relationshipDescr,
            new Object[] {
                DisplayTextTree.getModelElementDisplayName(theSource),
                DisplayTextTree.getModelElementDisplayName(
                        theDestination)}));
        this.metaType = theMetaType;
        this.source = theSource;
        this.dest = theDestination;
    }

    public void actionPerformed(ActionEvent e) {
        Object rootModel = 
            ProjectManager.getManager().getCurrentProject().getModel();
        try {
            Model.getUmlFactory().buildConnection(
                metaType,
                source,
                null,
                dest,
                null,
                null,
                rootModel);
        } catch (IllegalModelElementConnectionException e1) {
            LOG.error("Exception", e1);
        }
    }
}

