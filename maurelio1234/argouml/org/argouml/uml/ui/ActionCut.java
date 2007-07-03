// $Id: ActionCut.java 12546 2007-05-05 16:54:40Z linus $
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

package org.argouml.uml.ui;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Collection;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.JTextComponent;

import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.tigris.gef.base.CutAction;
import org.tigris.gef.base.Globals;

/**
 * The Cut Action.
 */
public class ActionCut extends AbstractAction implements CaretListener {

    private static ActionCut instance = new ActionCut();

    private static final String LOCALIZE_KEY = "action.cut";

    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     * Constructor.
     */
    public ActionCut() {
        super(Translator.localize(LOCALIZE_KEY));
        Icon icon = ResourceLoaderWrapper.lookupIcon(LOCALIZE_KEY);
        if (icon != null) {
            putValue(Action.SMALL_ICON, icon);
	}
        putValue(
		 Action.SHORT_DESCRIPTION,
		 Translator.localize(LOCALIZE_KEY) + " ");
    }

    /**
     * @return the singleton
     */
    public static ActionCut getInstance() {
        return instance;
    }

    private JTextComponent textSource;

    /*
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent ae) {
        if (textSource == null) {
            if (removeFromDiagramAllowed()) {
                CutAction cmd =
                        new CutAction(Translator.localize("action.cut"));
                cmd.actionPerformed(ae);
            }
        } else {
            textSource.cut();
        }
        if (isSystemClipBoardEmpty()
            && Globals.clipBoard == null
            || Globals.clipBoard.isEmpty()) {
            ActionPaste.getInstance().setEnabled(false);
        } else {
            ActionPaste.getInstance().setEnabled(true);
        }
    }

    /**
     * Disable cutting figs from a diagram to prevent issue 3480.
     * See also ActionPaste, which is also disabled for similar reasons.
     *
     * @return true if cut is allowed for the selected items
     */
    private boolean removeFromDiagramAllowed() {
        return false;
    }

    /*
     * @see javax.swing.event.CaretListener#caretUpdate(javax.swing.event.CaretEvent)
     */
    public void caretUpdate(CaretEvent e) {
        if (e.getMark() != e.getDot()) { // there is a selection
            setEnabled(true);
            textSource = (JTextComponent) e.getSource();
        } else {
            Collection figSelection =
                Globals.curEditor().getSelectionManager().selections();
            if (figSelection == null || figSelection.isEmpty()) {
                setEnabled(false);
            } else {
                setEnabled(true);
            }
            textSource = null;
        }

    }

    private boolean isSystemClipBoardEmpty() {
        //      if there is a selection on the clipboard
        boolean hasContents = false;
        Transferable content =
            Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
        DataFlavor[] flavors = content.getTransferDataFlavors();
        try {
            for (int i = 0; i < flavors.length; i++) {
                if (content.getTransferData(flavors[i]) != null) {
                    hasContents = true;
                    break;
                }
            }
        } catch (UnsupportedFlavorException ignorable) {
        } catch (IOException ignorable) {
        }
        return !hasContents;
    }

} /* end class ActionCut */
