// $Id:CmdSetPreferredSize.java 13349 2007-08-14 19:50:07Z mvw $
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

package org.argouml.ui.cmd;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.argouml.i18n.Translator;
import org.tigris.gef.base.Cmd;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.SelectionManager;
import org.tigris.gef.presentation.Fig;


/**
 * A command to set selected figs to their minimum size. <p>
 * Despite its name, really the minimum size is selected here! <p>
 * 
 * TODO: Rename this class.
 *
 * @author Markus Klink
 */
public class CmdSetPreferredSize extends Cmd {

    /**
     * Constant to indicate command should set preferred size of Fig.
     * @deprecated by mvw for 0.24.alpha3. Nobody uses this...
     */
    @Deprecated
    public static final int PREFERRED_SIZE = 0;

    /** constant for MINIMUM_SIZE */
    public static final int MINIMUM_SIZE = 1;

    private int mode;

    /**
     * Default constructor - set minimum size command.
     */
    public CmdSetPreferredSize() {
        this(MINIMUM_SIZE);
    }
    
    /**
     * Constructor for the command.
     * @deprecated by MVW in V0.25.3. 
     * Use the constructor without params instead.
     *
     * @param theMode one of the defined constants
     */
    @Deprecated
    public CmdSetPreferredSize(int theMode) {
//        super(Translator.localize("action.size-to-fit-contents"));
	super(Translator.localize("action.set-" + wordFor(theMode) + "-size"));
        mode = theMode;
    }

    private static String wordFor(int r) {
        switch (r) {
        case PREFERRED_SIZE: 
            return "preferred";
        case MINIMUM_SIZE: 
            return "minimum";
        default:
            throw new IllegalArgumentException(
                    "CmdSetPreferredSize invoked with " 
                            + "incompatible mode: " + r);
        }
    }

    /**
     * Set the fig to be resized.
     *
     * @param f the fig to resize
     */
    public void setFigToResize(Fig f) {
        List figs = new ArrayList(1);
        figs.add(f);
        setArg("figs", figs);
    }

    /**
     * Set the figs to be resized.
     *
     * @param figs the list of figs to resize
     */
    public void setFigToResize(Vector figs) {
        setArg("figs", figs);
    }


    /**
     * Set all the figs in the selection or passed by param "figs" to the
     * size according to the mode of the command.
     */
    public void doIt() {
        Editor ce = Globals.curEditor();
        List figs = (List) getArg("figs");
        if (figs == null) {
            SelectionManager sm = ce.getSelectionManager();
            if (sm.getLocked()) {
                Globals.showStatus("Cannot Modify Locked Objects");
                return;
            }
            figs = sm.getFigs();
        }

        if (figs == null) return;
        int size = figs.size();
        if (size == 0) return;

        for (int i = 0; i < size; i++) {
            Fig fi = (Fig) figs.get(i);
            /* Only resize elements which the user would also be able
             * to resize: */
            if (fi.isResizable() 
                    /* But exclude elements that enclose others, 
                     * since their algorithms to calculate the minimum size 
                     * does not take enclosed objects into account: */
                    && (fi.getEnclosedFigs() == null 
                            || fi.getEnclosedFigs().size() == 0)) {
                if (mode == PREFERRED_SIZE) {
                    fi.setSize(fi.getPreferredSize());
                } else {
                    fi.setSize(fi.getMinimumSize());
                }
                /* TODO: Beautify the 2nd part of this string: */
                Globals.showStatus("Setting size for " + fi);
            }
            fi.endTrans();
        }
    }


    /*
     * @see org.tigris.gef.base.Cmd#undoIt()
     */
    public void undoIt() {
        // unsupported. 
    }

}
