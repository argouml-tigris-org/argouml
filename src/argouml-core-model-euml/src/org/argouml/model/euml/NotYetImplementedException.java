// $Id$
/***************************************************************************
 * Copyright (c) 2007,2010 Tom Morris and other contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Tom Morris - initial API and implementation
 ***************************************************************************/

package org.argouml.model.euml;

import org.argouml.model.NotImplementedException;

/**
 * Runtime exception for things in eUML implementation which are not
 * yet implemented.  We use NotImplementedException for things which we
 * never plan to implement and NotYetImplemented for things which we
 * plan to implement but haven't gotten around to yet.
 * <p>
 * When the implementation is complete this class may be removed.
 * 
 * @author Tom Morris
 */
class NotYetImplementedException extends NotImplementedException {

    NotYetImplementedException() {
        super();
    }
    
    NotYetImplementedException(String message) {
        super(message);
    }

}
