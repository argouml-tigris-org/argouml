// $Id$
// Copyright (c) 2003 The Regents of the University of California. All
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

/*
 * PredNOT.java
 */

package org.argouml.kernel;

import org.tigris.gef.util.*;

/**
 * Predicate to provide logical <B>NOT</B>. The predicate returns 
 * true if its internal <code>Predicate</code> return false;    
 * 
 * This class belongs in org.argouml.util
 * @author Eugenio Alvarez
 *
 */
public class PredNOT implements Predicate {

    Predicate _predicate;

    public PredNOT(Predicate predicate) { 
	_predicate = predicate; 
    }

    /**
     * Returns true it initialized predicate
     * returns false;  
     *
     * @param  Object to test. 
     *
     * @return Returns true if its internal
     *         predicate returns false; 
     */
    public boolean predicate(Object obj) {
	return !_predicate.predicate(obj);
    }

} /* end class PredNOT */

