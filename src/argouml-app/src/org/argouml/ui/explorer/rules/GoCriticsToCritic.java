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

// Copyright (c) 2008 The Regents of the University of California. All
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

package org.argouml.ui.explorer.rules;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.Vector;

import org.argouml.cognitive.CompoundCritic;
import org.argouml.cognitive.Critic;
import org.argouml.i18n.Translator;

/**
 * Show the critics exported by a Profile
 * 
 * @author maurelio1234
 */
public class GoCriticsToCritic implements PerspectiveRule {

    /*
     * @see org.argouml.ui.explorer.rules.PerspectiveRule#getRuleName()
     */
    public String getRuleName() {
        return Translator.localize("misc.profile.critic");
    }

    /*
     * @see org.argouml.ui.explorer.rules.PerspectiveRule#getChildren(java.lang.Object)
     */
    public Collection getChildren(final Object parent) {
        if (parent instanceof Collection) {
            Collection v = (Collection) parent;
            if (!v.isEmpty()) {
                if (v.iterator().next() instanceof Critic) {
                    Vector<Object> ret = new Vector<Object>();
                    for (Object critic : v) {
                        final Critic fc = (Critic) critic;
                        if (critic instanceof CompoundCritic) {

                            Object compound = new Vector<Critic>() {
                                {
                                    addAll(((CompoundCritic) fc)
                                            .getCriticList());
                                }

                                /*
                                 * @see java.util.Vector#toString()
                                 */
                                public String toString() {
                                    return Translator
                                            .localize("misc.profile.explorer.compound");
                                }
                            };

                            ret.add(compound);
                        } else {
                            ret.add(critic);
                        }
                    }
                    return ret;
                } else {
                    return (Collection) parent;
                }
            } else {
                return Collections.EMPTY_SET;
            }
        }
        return Collections.EMPTY_SET;
    }

    /*
     * @see org.argouml.ui.explorer.rules.PerspectiveRule#getDependencies(java.lang.Object)
     */
    public Set getDependencies(Object parent) {
        // TODO: What?
        return Collections.EMPTY_SET;
    }
}
