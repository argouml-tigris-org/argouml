/* $Id$
 *******************************************************************************
 * Copyright (c) 2010 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Michiel van der Wulp
 *******************************************************************************
 */

package org.argouml.transformer;

import java.util.HashSet;
import java.util.List;

import org.argouml.i18n.Translator;
import org.argouml.kernel.ActionList;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.ui.ContextActionFactory;

/**
 * This is a singleton.
 *
 * @author Michiel van der Wulp
 */
final class TransformerManager implements ContextActionFactory {
    
    private HashSet<Transformer> transformers;
    
    /**
     * The singleton instance.
     */
    private static TransformerManager instance = new TransformerManager();

    /**
     * Singleton constructor should remain private.
     */
    private TransformerManager() {
    }

    /**
     * Singleton retrieval method.
     * @return the transformermanager
     */
    public static TransformerManager getInstance() {
        return instance;
    }

    private HashSet<Transformer> getTransformers() {
        if (transformers == null) {
            transformers = new HashSet<Transformer>();
        }
        return transformers;
    }

    void addTransformer(Transformer t) {
        getTransformers().add(t);
    }

    public List createContextPopupActions(Object element) {
        final Project p = ProjectManager.getManager().getCurrentProject();
        
        final ActionList result = new ActionList(
                Translator.localize("transform.menu.popup.transform"));
        for (Transformer t : getTransformers()) {
            if (t.canTransform(element)) {
                result.addAll(t.actions(p, element));
            }
        }
        return result.size() > 0 ? result : null;
    }
    
    
    
}
