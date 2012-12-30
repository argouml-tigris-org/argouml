/* $Id$
 *****************************************************************************
 * Copyright (c) 2010-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Michiel van der Wulp
 *    Bob Tarling
 *****************************************************************************
 */

package org.argouml.notation2;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.argouml.moduleloader.ModuleInterface;


public class NotationModule implements ModuleInterface {

    private static final Logger LOG =
        Logger.getLogger(NotationModule.class.getName());

    public boolean enable() {

        LOG.log(Level.INFO, "Notation Module enabled.");
        NotationLanguage lang = new UmlNotationLanguage();
        NotationManager.getInstance().addNotationLanguage(lang);

        return true;
    }

    public boolean disable() {

        LOG.log(Level.INFO, "Notation Module disabled.");
        return true;
    }

    public String getName() {
        return "ArgoUML-Notation";
    }

    public String getInfo(int type) {
        switch (type) {
        case DESCRIPTION:
            return "The notation subsystem";
        case AUTHOR:
            return "The ArgoUML Team";
        case VERSION:
            return "0.34";
        case DOWNLOADSITE:
            return "http://argouml.tigris.org";
        default:
            return null;
        }
    }
}
