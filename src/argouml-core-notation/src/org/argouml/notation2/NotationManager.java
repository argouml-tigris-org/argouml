/* $Id$
 *******************************************************************************
 * Copyright (c) 2011 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bob Tarling
 *******************************************************************************
 */

package org.argouml.notation2;

import java.util.ArrayList;
import java.util.List;

/**
 * The singleton manager for notations.
 * All notation languages and objects requesting notation events register here.
 *
 * @author Bob Tarling
 */
final class NotationManager {

    /**
     * The singleton instance.
     */
    private static NotationManager instance = new NotationManager();
    
    private List<NotationLanguage> languages =
        new ArrayList<NotationLanguage>();
    private List<NotatedItem> items =
        new ArrayList<NotatedItem>();
    
    private NotationManager() {
    }

    public static NotationManager getInstance() {
        return instance;
    }

    void addNotationLanguage(NotationLanguage language) {
        languages.add(language);
    }
    
    void addListener(NotatedItem item) {
        NotationLanguage nl = item.getNotationLanguage();
        nl.createNotationText(item);
        items.add(item);
    }
    
    void removeListener(NotatedItem item) {
        items.remove(item);
    }
}
