// $Id$
// Copyright (c) 1996-2004 The Regents of the University of California. All
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

package org.argouml.ui.explorer;

import javax.swing.KeyStroke;

import org.argouml.uml.diagram.ui.ActionSaveDiagramToClipboard;

/**
 * This class extends the Explorer with 'copy to clipboard' capability for 
 * diagrams.
 *
 * see: http://java.sun.com/j2se/1.3/docs/guide/swing/KeyBindChanges.html
 *
 * @author  alexb
 * @since 0.15.2, Created on 18 October 2003, 23:31
 */
public class ExportExplorer
    extends DnDExplorerTree {
    
    public final String DIAGRAM_TO_CLIPBOARD_ACTION = "export Diagram as GIF";
    
    /** Creates a new instance of ExportExplorer */
    public ExportExplorer() {
        
        createDiagramExportBinding();
    }
    
    private void createDiagramExportBinding() {
        
        KeyStroke ctrlC = KeyStroke.getKeyStroke("control C");
        
        this.getInputMap().put(ctrlC, DIAGRAM_TO_CLIPBOARD_ACTION);
        this.getActionMap().put(DIAGRAM_TO_CLIPBOARD_ACTION,
				new ActionSaveDiagramToClipboard());
    }

}
