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

package org.argouml.transformer;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.argouml.moduleloader.ModuleInterface;
import org.argouml.ui.ContextActionFactoryManager;


public class TransformerModule implements ModuleInterface {

    private static final Logger LOG =
        Logger.getLogger(TransformerModule.class.getName());

    public boolean enable() {

        TransformerManager.getInstance().addTransformer(
                new EventTransformer());
        TransformerManager.getInstance().addTransformer(
                new SimpleStateTransformer());
        ContextActionFactoryManager.addContextPopupFactory(TransformerManager.getInstance());
        LOG.log(Level.INFO, "Transformer Module enabled.");
        return true;
    }

    public boolean disable() {
        ContextActionFactoryManager.removeContextPopupFactory(TransformerManager.getInstance());

        LOG.log(Level.INFO, "Transformer Module disabled.");
        return true;
    }

    public String getName() {
        return "ArgoUML-Transformer";
    }

    public String getInfo(int type) {
        switch (type) {
        case DESCRIPTION:
            return "The model element transformer";
        case AUTHOR:
            return "The ArgoUML Team";
        case VERSION:
            return "0.32";
        case DOWNLOADSITE:
            return "http://argouml.tigris.org";
        default:
            return null;
        }
    }
}
