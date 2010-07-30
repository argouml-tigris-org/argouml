/* $Id$
 *******************************************************************************
 * Copyright (c) 2010 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    mvw
 *******************************************************************************
 */

package org.argouml.uml.transformer;

import java.util.Collections;
import java.util.List;

import org.argouml.application.api.AbstractArgoJPanel;
import org.argouml.application.api.GUISettingsTabInterface;
import org.argouml.application.api.InitSubsystem;

/**
 * Initialise this subsystem.
 *
 * @author michiel
 */
public class InitTransformer implements InitSubsystem {

    public List<AbstractArgoJPanel> getDetailsTabs() {
        return Collections.emptyList();
    }

    public List<GUISettingsTabInterface> getProjectSettingsTabs() {
        return Collections.emptyList();
    }

    public List<GUISettingsTabInterface> getSettingsTabs() {
        return Collections.emptyList();
    }

    public void init() {
        TransformerManager.getInstance().addTransformer(
                new EventTransformer());
        TransformerManager.getInstance().addTransformer(
                new SimpleStateTransformer());
    }

}
