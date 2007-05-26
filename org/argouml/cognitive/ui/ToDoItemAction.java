// $Id$
// Copyright (c) 2004-2006 The Regents of the University of California. All
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

package org.argouml.cognitive.ui;

import javax.swing.Action;

import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.cognitive.ToDoItem;
import org.argouml.i18n.Translator;
import org.tigris.gef.undo.UndoableAction;



/**
 * A base class for Actions related to ToDoItems.
 *
 */
public abstract class ToDoItemAction extends UndoableAction {

    private Object rememberedTarget = null;

    /**
     * @param name to be localized
     * @param hasIcon true if an icon is to be shown
     */
    public ToDoItemAction(String name, boolean hasIcon) {
        super(Translator.localize(name),
                hasIcon ? ResourceLoaderWrapper.lookupIcon(name) : null);
        // Set the tooltip string:
        putValue(Action.SHORT_DESCRIPTION, 
                Translator.localize(name));
    }

    /**
     * @return returns the rememberedTarget
     */
    protected Object getRememberedTarget() {
        return rememberedTarget;
    }

    /**
     * @param target the target
     */
    public void updateEnabled(Object target) {
	if (target == null) {
	    setEnabled(false);
	    return;
	}

	rememberedTarget = target;
	setEnabled(isEnabled(target));
    }

    /**
     * @param target the current target
     * @return true if the action icon should be enabled (i.e. not downlighted)
     */
    public boolean isEnabled(Object target) {
	return target instanceof ToDoItem;
    }
}

