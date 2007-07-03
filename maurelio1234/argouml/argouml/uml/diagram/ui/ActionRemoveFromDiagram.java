// $Id: ActionRemoveFromDiagram.java 12908 2007-06-24 18:22:05Z mvw $
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

package org.argouml.uml.diagram.ui;

import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Action;

import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.uml.CommentEdge;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.di.GraphElement;
import org.tigris.gef.graph.MutableGraphSupport;
import org.tigris.gef.presentation.Fig;


/**
 * Removes an modelelement from the diagram, but not from the model.
 */
public class ActionRemoveFromDiagram extends AbstractAction {

    /**
     * The constructor.
     * 
     * @param name the localised (!) name
     */
    public ActionRemoveFromDiagram(String name) {
        super(name, ResourceLoaderWrapper.lookupIcon("RemoveFromDiagram"));
        String localMnemonic =
    	    Translator.localize("action.remove-from-diagram.mnemonic");
        if (localMnemonic != null && localMnemonic.length() == 1) {
            putValue(Action.MNEMONIC_KEY,
		     new Integer(localMnemonic.charAt(0)));
        }
        // Set the tooltip string:
        putValue(Action.SHORT_DESCRIPTION, name);
    }

    /*
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent ae) {
        int size = 0;
        Editor ce = Globals.curEditor();
        MutableGraphSupport graph = (MutableGraphSupport) ce.getGraphModel();
        Vector figs = ce.getSelectionManager().getFigs();
        size = figs.size();
        for (int i = 0; i < size; i++) {
            Fig f = (Fig) figs.elementAt(i);
            if (!(f.getOwner() instanceof CommentEdge)) {
                if (f instanceof GraphElement) {
                    f.removeFromDiagram();
                } else {
                    graph.removeFig(f);
                }
            }
        }
    }
}
