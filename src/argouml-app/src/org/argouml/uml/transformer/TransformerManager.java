/* $Id$
 *******************************************************************************
 * Copyright (c) 2010 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    michiel
 *******************************************************************************
 */

package org.argouml.uml.transformer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.swing.Action;

import org.argouml.kernel.Project;

/**
 * This is a singleton.
 *
 * @author mvw
 */
public final class TransformerManager {
    
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

    /**
     * Returns an ordered list of Actions that transform the given 
     * modelelement into another.
     * 
     * @param p the current project
     * @param sourceModelElement the given source modelelement
     * @return ordered list of Actions suitable for inclusion in a Menu
     */
    public List<Action> actions(Project p, Object sourceModelElement) {
        List<Action> result = new ArrayList<Action>();
        for (Transformer t : getTransformers()) {
            if (t.canTransform(sourceModelElement)) {
                result.addAll(t.actions(p, sourceModelElement));
            }
        }
        return result;
    }
}
