/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    maurelio1234
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 1996-2006 The Regents of the University of California. All
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

package org.argouml.uml.cognitive.critics;

import org.argouml.cognitive.Translator;

/**
 * "Abstract" Critic subclass that captures commonalities among all
 * critics defined by profiles. This class also defines and registers
 * the categories of design decisions that the critics can
 * address. 
 *
 * @see org.argouml.cognitive.Designer
 * @see org.argouml.cognitive.DecisionModel
 *
 * @author maurelio1234
 */
public class CrProfile extends CrUML {
    private String localizationPrefix;
    
    /**
     * The constructor for this class.<br>
     * <br>
     * By default, CrUML looks for localized strings under the <code>"critics"
     * </code> prefix. Since profiles cannot include their strings at the 
     * <code>critics.profile</code> file located at the ArgoUML source code this
     * class allows critics defined on profile plug-ins to define look for 
     * localized strings at other files.  
     * 
     * @param prefix the prefix of the localized strings
     */
    public CrProfile(final String prefix) {
	super();
	
	if (prefix == null || "".equals(prefix)) {
	    this.localizationPrefix = "critics";
	} else {
	    this.localizationPrefix = prefix;
	}
	
	setupHeadAndDesc();
    }
    
    @Override
    protected String getLocalizedString(String key, String suffix) {
        return Translator.localize(localizationPrefix + "." + key + suffix);
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = 1785043010468681602L;
}
