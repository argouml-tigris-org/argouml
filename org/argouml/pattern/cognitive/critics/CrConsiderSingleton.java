// Copyright (c) 1996-99 The Regents of the University of California. All
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



// File: CrConsiderSingleton.java
// Classes: CrConsiderSingleton
// Original Author: jrobbins@ics.uci.edu
// $Id$

// 5 Feb 2002: Jeremy Bennett (mail@jeremybennett.com). Code factored by use of
// static methods in central org.argouml.cognitive.critics.CriticUtils utility
// class. <<singleton>> allowed as well as <<Singleton>> for consistency with
// CrSingletonViolated.


package org.argouml.pattern.cognitive.critics;

import java.util.*;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;

import org.argouml.cognitive.*;
import org.argouml.cognitive.critics.*;
import org.argouml.uml.*;
import org.argouml.uml.cognitive.critics.*;

/**
 * <p>A critic to detect when a class can never have more than one instance (of
 * itself of any subclasses), and thus whether it is suitable for declaration
 * as a Singleton (with stereotype &laquo;Singleton&raquo;.<p>
 *
 * <p>This stereotype is used to indicate a class which only ever has a single
 * instance. The critic will trigger whenever a class has stereotype
 * &laquo;Singleton&raquo; (or &laquo;singleton&raquo;), but does not meet the
 * requirements of a Singleton class. These are:</p>
 *
 * <ol>
 *   <li>An static variable to hold the sole instance of the class;</li>
 *
 *   <li>only private constructors to create the sole instance; and</li>
 *
 *   <li>At least one constructor to override the default constructor.</li>
 * </ol>
 *
 * <p>The original version would always trigger for any class with stereotype
 * &laquo;Singleton&raquo;. This version includes an implementation for the
 * three tests above!</p>
 *
 * <p>Internally we use some of the static utility methods of the {@link
 * org.argouml.cognitive.critics.CriticUtils CriticUtils} class.</p>
 *
 * @see <a href="http://argouml.tigris.org/documentation/snapshots/manual/argouml.html/#s2.ref.critics_singleton_violated">ArgoUML User Manual: Singleton Violated</a>
 */

public class CrConsiderSingleton extends CrUML {

    /**
     * <p>Constructor for the critic.</p>
     *
     * <p>Sets up the resource name, which will allow headline and description
     * to be found for the current locale. Provides a design issue category
     * (PATTERNS), sets a priority for any to-do items (LOW) and adds triggers
     * for metaclasses "stereotype", "structuralFeature" and
     * "associationEnd".</p>
     */

    public CrConsiderSingleton() {

        setResource("CrConsiderSingleton");

        addSupportedDecision(CrUML.decPATTERNS);
        setPriority(ToDoItem.LOW_PRIORITY);

        // These may not actually make any difference at present (the code
        // behind addTrigger needs more work).

        addTrigger("stereotype");
        addTrigger("structuralFeature");
        addTrigger("associationEnd");
    }


    /**
     * <p>The trigger for the critic.</p>
     *
     * <p>First check we are actually stereotyped "Singleton" (or we will
     * accept "singleton").</p>
     *
     * <p>Otherwise plausible candidates for the Singleton design pattern are
     * classes with no instance variables (i.e. non-static attributes) and no
     * outgoing associations.<p>
     *
     * @param  dm    the {@link java.lang.Object Object} to be checked against
     *               the critic.
     *
     * @param  dsgr  the {@link org.argouml.cognitive.Designer Designer}
     *               creating the model. Not used, this is for future
     *               development of ArgoUML.
     *
     * @return       {@link #PROBLEM_FOUND PROBLEM_FOUND} if the critic is
     *               triggered, otherwise {@link #NO_PROBLEM NO_PROBLEM}.  
     */

    public boolean predicate2(Object dm, Designer dsgr) {

        // Only look at classes

        if (!(dm instanceof MClass)) {
            return NO_PROBLEM;
        }

        // Now we know it is a class, handle the object as a class

        MClass cls = (MClass) dm;
	if (!(CriticUtils.isPrimaryObject(cls))) return NO_PROBLEM;

        // Check for Singleton stereotype, uninitialised instance variables and
        // outgoing associations, as per JavaDoc above.

        if (CriticUtils.hasSingletonStereotype(cls)) {
            return NO_PROBLEM;
        }

        if (CriticUtils.hasInstanceVariables(cls)) {
            return NO_PROBLEM;
        }

        if (CriticUtils.hasOutgoingAssociations(cls)) {
            return NO_PROBLEM;
        }
        else {
            return PROBLEM_FOUND;
        }
    }

} /* end class CrConsiderSingleton */

