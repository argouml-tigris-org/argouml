// Copyright (c) 2002 The Regents of the University of California. All
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

package org.argouml.util;

import java.lang.ref.*;
import java.lang.reflect.*;

import junit.framework.*;

import org.apache.log4j.spi.LoggingEvent;
import org.argouml.model.uml.*;
import ru.novosoft.uml.*;
import ru.novosoft.uml.foundation.core.MModelElement;

/**
 *   This class is the base class of tests that tests
 *   the model stuff.
 *
 *   @author Linus Tolke
 *   @since 0.11.2
 */
public class CheckUMLModelHelper {

    public static void createAndRelease(TestCase tc,
					Object mo) {
	Class c = mo.getClass();

	// Call methods that exists for all objects and that always return
	// something meaningfull.
	tc.assert("toString() corrupt in " + c, 
		  mo.toString() instanceof String);
 
	WeakReference wo = new WeakReference(mo);
	mo = null;
	System.gc();
	tc.assert("Could not reclaim " + c, wo.get() == null);
    }

    public static void createAndRelease(TestCase tc,
					MBase mo,
					String name) {
	Class c = mo.getClass();

	// Call methods that exists for all objects and that always return
	// something meaningfull.
	tc.assert("toString() corrupt in " + c, 
		  mo.toString() instanceof String);
	tc.assert("getUMLClassName() corrupt in " + c, 
		  mo.getUMLClassName() instanceof String);

	tc.assert("getUMLClassName() different from expected in " + c, 
		  name.equals(mo.getUMLClassName()));

	WeakReference wo = new WeakReference(mo);
	mo = null;
	System.gc();
	tc.assert("Could not reclaim " + c, wo.get() == null);
    }

    public static void createAndRelease(TestCase tc,
					AbstractUmlModelFactory f,
					String [] names,
					Object [] args) {
	Class [] classes = new Class[args.length];
	for (int i = 0; i < args.length; i++) {
	    classes[i] = args[i].getClass();
	}

	for (int i = 0; i < names.length; i++) {
	    if (names[i] == null) continue;
	    Method m;
	    try {
		m = f.getClass().getMethod("create" + names[i], classes);
	    }
	    catch (NoSuchMethodException e) {
		tc.fail("Method create" + names[i] 
			+ " does not exist in " + f);
		return;
	    }

	    try {
		// Extra careful now, not to keep any references to the
		// second argument.
		try {
		    createAndRelease(tc, (MBase) m.invoke(f, args), names[i]);
		}
		catch (ClassCastException e) {
		    // Here it is another object sent to the test.
		    createAndRelease(tc, m.invoke(f, args));
		}
	    }
	    catch (IllegalAccessException e) {
		tc.fail("Method create" + names[i] 
			+ " in " + f + " cannot be called");
		return;
	    }
	    catch (InvocationTargetException e) {
		tc.fail("Method create" + names[i] 
			+ " in " + f + " throws an exception.");
		return;
	    }
	}
    }

    public static void createAndRelease(TestCase tc,
					AbstractUmlModelFactory f,
					String [] names) {
	Object[] noarguments = { };

	createAndRelease(tc, f, names, noarguments);
    }
}

