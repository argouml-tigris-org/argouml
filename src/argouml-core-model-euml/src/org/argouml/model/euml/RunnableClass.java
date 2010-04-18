// $Id$
/*******************************************************************************
 * Copyright (c) 2007,2010 Bogdan Pistol and other contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bogdan Pistol - initial API and implementation
 *******************************************************************************/
package org.argouml.model.euml;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a utility class useful in conjunction with 
 * {@link org.eclipse.uml2.common.edit.command.ChangeCommand ChangeCommand}
 * 
 * @author Bogdan Pistol
 */
public abstract class RunnableClass implements Runnable {
    
    /**
     * A list of parameters useful for extraction/insertion of parameters
     * into a Runnable instance.
     */
    private List<Object> params = new ArrayList<Object>();

    public List<Object> getParams() {
        return params;
    }

}
