// $Id: ArgoModeCreateFigInk.java 12828 2007-06-13 19:08:44Z mvw $
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

package org.argouml.gefext;

import java.awt.event.MouseEvent;

import org.argouml.i18n.Translator;
import org.tigris.gef.base.ModeCreateFigInk;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigInk;

/**
 * A Mode to interprete user input while creating a FigInk.
 *
 * @author Michiel
 */
public class ArgoModeCreateFigInk extends ModeCreateFigInk {

    public String instructions() { 
        return Translator.localize("statusmsg.help.create.ink"); 
    }

    public Fig createNewItem(MouseEvent me, int snapX, int snapY) {
        FigInk p = new ArgoFigInk(snapX, snapY);
        _lastX = snapX; _lastY = snapY;
        return p;
    }
}
