// $Id$
// Copyright (c) 1996-2001 The Regents of the University of California. All
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

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.JTextComponent;

import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.tigris.gef.base.CmdPaste;
import org.tigris.gef.base.Globals;

/** 
 * Action to paste the content of either the GEF clipboard or the
 * system clipboard.  The action is enabled if there is content on
 * either clipboard AND either the mouse hovers over the JGraph (the
 * diagram) or the caret in a UMLTextField2 or UMLTextArea2 is
 * enabled.
 */
public class ActionPaste
    extends AbstractAction
    implements CaretListener, FocusListener {

    ////////////////////////////////////////////////////////////////
    // static variables

    private static ActionPaste _Instance = new ActionPaste();

    private static final String LOCALIZE_KEY = "action.paste";

    /**
     * Default constructor for action. We cannot use UMLChangeAction
     * as a parent for this class since it works with shouldBeEnabled
     * etc. which doesn't give enough control about enabling/disabling
     * this action.
     *
     */
    private ActionPaste() {
        super(Translator.localize(LOCALIZE_KEY));
        Icon icon =
            ResourceLoaderWrapper
	    .getResourceLoaderWrapper()
	    .lookupIconResource(
				Translator.getImageBinding(LOCALIZE_KEY),
				Translator.localize(LOCALIZE_KEY));
        if (icon != null)
            putValue(Action.SMALL_ICON, icon);
        putValue(
		 Action.SHORT_DESCRIPTION,
		 Translator.localize(LOCALIZE_KEY) + " ");
        // setEnabled((Globals.clipBoard != null &&
        // !Globals.clipBoard.isEmpty()) ||
        // !isSystemClipBoardEmpty());
        setEnabled(false);
    }

    /**
     * Singleton implementation.
     * @return The singleton
     */
    public static ActionPaste getInstance() {
        return _Instance;
    }

    /**
     * The source textcomponent where the caret is positioned
     */
    private JTextComponent _textSource;

    /**
     * Flag to indicate that the mouse is hovering over the JGraph
     */
    private boolean _inJGraph;

    /**
     * Copies some text or a fig
     */
    public void actionPerformed(ActionEvent ae) {
        if (Globals.clipBoard != null && !Globals.clipBoard.isEmpty()) {
            CmdPaste cmd = new CmdPaste();
            cmd.doIt();
        } else {
            if (!isSystemClipBoardEmpty() && _textSource != null) {
                _textSource.paste();
            }
        }
              
    }

    

    private boolean isSystemClipBoardEmpty() {
        //      if there is a selection on the clipboard
	boolean hasContents = false;
	try {
	    Object text =
		Toolkit
		.getDefaultToolkit()
		.getSystemClipboard()
		.getContents(null)
		.getTransferData(DataFlavor.stringFlavor);
	    return text == null;
	} catch (IOException ignorable) {
	} catch (UnsupportedFlavorException ignorable) {
	}
	return true;
    }    

    /**
     * @see java.awt.event.FocusListener#focusLost(java.awt.event.FocusEvent)
     */
    public void focusLost(FocusEvent e) {        
        if (e.getSource() == _textSource) _textSource = null;
    }

    /**
     * @see
     * javax.swing.event.CaretListener#caretUpdate(javax.swing.event.CaretEvent)
     */
    public void caretUpdate(CaretEvent e) {
        _textSource = (JTextComponent) e.getSource();

    }

    /**
     * @see java.awt.event.FocusListener#focusGained(java.awt.event.FocusEvent)
     */
    public void focusGained(FocusEvent e) {
        _textSource = (JTextComponent) e.getSource();
    }

} /* end class ActionPaste */
