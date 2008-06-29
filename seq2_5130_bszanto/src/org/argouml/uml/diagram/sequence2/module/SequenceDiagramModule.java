// $Id$
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

package org.argouml.uml.diagram.sequence2.module;

import org.apache.log4j.Logger;
import org.argouml.moduleloader.ModuleInterface;
import org.argouml.proppanel.sequence2.SequenceDiagramPropPanelFactory;
import org.argouml.uml.diagram.DiagramFactory;
import org.argouml.uml.diagram.DiagramFactory.DiagramType;
import org.argouml.uml.ui.PropPanelFactoryManager;

/**
 * The Sequence Diagram Module description.
 * 
 * @see org.argouml.moduleloader.ModuleInterface
 * @author penyaskito
 */
public class SequenceDiagramModule implements ModuleInterface {

    /**
     * Logger.
     */
    private static final Logger LOG = Logger
            .getLogger(SequenceDiagramModule.class);

    private SequenceDiagramPropPanelFactory propPanelFactory;
        
    public boolean enable() {
        
        propPanelFactory =
            new SequenceDiagramPropPanelFactory();
        PropPanelFactoryManager.addPropPanelFactory(propPanelFactory);
        
        DiagramFactory.getInstance().registerDiagramFactory(
                DiagramType.Sequence, new SequenceDiagramFactory());

        LOG.info("SequenceDiagram Module enabled.");
        return true;
    }

    public boolean disable() {

        PropPanelFactoryManager.removePropPanelFactory(propPanelFactory);

        DiagramFactory.getInstance().registerDiagramFactory(
                DiagramType.Sequence, null);

        LOG.info("SequenceDiagram Module disabled.");
        return true;
    }

    public String getName() {
        return "ArgoUML-Sequence";
    }

    public String getInfo(int type) {
        switch (type) {
        case DESCRIPTION:
            return "The new sequence diagram implementation";
        case AUTHOR:
            return "Christian López Espínola";
        case VERSION:
            return "0.1";
        case DOWNLOADSITE:
            return "http://argouml-sequence.tigris.org";
        default:
            return null;
        }
    }
}