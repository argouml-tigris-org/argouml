/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    tfmorris
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 1996-2008 The Regents of the University of California. All
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

package org.argouml.util;

import java.util.StringTokenizer;

/**
 * Predicate to match strings/substrings.  It only supports simple
 * wildcard processing, not full regex style matching.
 */
public class PredicateStringMatch implements Predicate {

    public static int MAX_PATS = 10;

    private String patterns[];

    private int patternCount;

    protected PredicateStringMatch(String matchPatterns[], int count) {
        patterns = matchPatterns;
        patternCount = count;
    }

    public static Predicate create(String pattern) {
        pattern = pattern.trim();
        if ("*".equals(pattern) || "".equals(pattern)) {
            return PredicateTrue.getInstance();
        }
        String pats[] = new String[MAX_PATS];
        int count = 0;
        if (pattern.startsWith("*")) {
            pats[count++] = "";
        }
        StringTokenizer st = new StringTokenizer(pattern, "*");
        while (st.hasMoreElements()) {
            String token = st.nextToken();
            pats[count++] = token;
        }
        if (pattern.endsWith("*")) {
            pats[count++] = "";
        }
        if (count == 0) {
            return PredicateTrue.getInstance();
        }
        if (count == 1) {
            return new PredicateEquals(pats[0]);
        }
        return new PredicateStringMatch(pats, count);
    }

    public boolean evaluate(Object o) {
        if (o == null) {
            return false;
        }
        String target = o.toString();
        if (!target.startsWith(patterns[0])) {
            return false;
        }
        if (!target.endsWith(patterns[patternCount - 1])) {
            return false;
        }
        for (String pattern : patterns) {
            int index = (target + "*").indexOf(pattern);
            if (index == -1) {
                return false;
            }
            target = target.substring(index + pattern.length());
        }
        return true;
    }

}
