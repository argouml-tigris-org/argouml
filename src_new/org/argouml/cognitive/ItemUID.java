// $Id$
// Copyright (c) 2002-2006 The Regents of the University of California. All
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

package org.argouml.cognitive;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.argouml.model.Model;
import org.argouml.uml.UUIDHelper;

/**
 * An instances of this class is supposed to be attached to an instance
 * of another class to uniquely identify it. It is intended that such
 * a tagging should be persistent over saving and loading, if applicable.<p>
 *
 * The class also harbors the
 * {@link #getIDOfObject getIDOfObject(Object, boolean)} which provides
 * a way to get the ItemUID of any object with a method
 * <code>ItemUID getItemUID()</code>
 * and creating new ItemUIDs for any object with a method
 * <code>setItemUID(ItemUID)</code>
 * using reflection in java.<p>
 *
 * A class intended to be tagged must at least provide a
 * <code>ItemUID getItemUID()</code>
 * method. It may also provide a
 * <code>void setItemUID(ItemUID id)</code>
 * such that getItemUID() will return id if a call returns successfully,
 * and which is stored persistently should the tagged object be stored.
 * This allows this class to automatically tag an object when necessary,
 * but it is allowed to tag classes by other means and only provide the
 * getItemUID() call.<p>
 *
 * A critical requirement for this class is that the cognitive component
 * is supposed to work with general objects. This class is a wrapper around
 * places where the component needs persistent identities of objects, since
 * I have said that some features cannot be implemented without that, such as
 * ResolvedCritic, and so far noone has shown me wrong (though I wouldn't
 * mind). It is for this reason that some perhaps ugly looking exceptions in
 * this code must be considered perfectly normal conditions. Failure of some
 * object to work with tagging must be handled by the cognitive component
 * programmer and it is (see eg ResolvedCritic).<p>
 *
 * A possible future change would be to allow tag handlers to be registered
 * with this class to handle other preexisting tagging mechanisms, which
 * could be used to remove the dependancy to the model component here, which
 * I find a bit unaesthetic. So far, not enough to write it (though it is not
 * much work).
 *
 * @author Michael Stockman
 */
public class ItemUID {
    /**
     * The logger.
     */
    private static final Logger LOG = Logger.getLogger(ItemUID.class);

    /**
     * Keeps a reference to the Class object of this class.
     */
    private static final Class MYCLASS = (new ItemUID()).getClass();

    /**
     * This actual ID of this instance.
     */
    private String id;

    /**
     * Constructs a new ItemUID and creates a new ID for it.
     */
    public ItemUID() {
	id = generateID();
    }

    /**
     * Constructs a new ItemUID and uses the String param as the ID.
     * Mainly intended to be used during loading of saved objects.
     *
     * @param	param	The ID to used for the new instance.
     * @see		#toString()
     */
    public ItemUID(String param) {
	id = param;
    }

    /**
     * Returns the ID of this ItemUID as a String. If everything works all
     * such Strings will be unique. It is possible to created a new
     * identical ItemUID using this String.
     *
     * @return	The ID as a String.
     * @see		#ItemUID(String)
     */
    public String toString() {
	return id;
    }

    /**
     * Generates a new unique ID and returns it as a String. The contents
     * of the String is supposed to be unique with respect to all Strings
     * generated by other instances of this class.
     *
     * @return	A String with unique content.
     */
    public static String generateID() {
	return (new java.rmi.server.UID()).toString();
    }

    /**
     * Obtains the ID of an object and returns it as a String. If
     * canCreate is true it will try to create a new ID for the object
     * if it has none.
     *
     * @param obj the Object to get the ID of.
     * @param canCreate If an ID can be created, should object not have one.
     * @return	The ID of the object, or null.
     */
    public static String getIDOfObject(Object obj, boolean canCreate) {
	String s = readObjectID(obj);

	if (s == null && canCreate) {
	    s = createObjectID(obj);
	}

	return s;
    }

    /**
     * Tries to read the ID of the object. It uses the reflective
     * properties of java to access a method named getItemUID of the
     * object which is expected to return an ItemUID.
     *
     * @param obj The object whose ID to read.
     * @return	The ID of the object, or null.
     */
    protected static String readObjectID(Object obj) {
        // TODO: We really want this coupled to the model subsystem?
        if (Model.getFacade().isAModelElement(obj)) {
            return UUIDHelper.getUUID(obj);
        }
        
	/*
	// Want to use the "built in" UID of the MXxx instances
	// d00mst 2002-10-08
	if (obj instanceof MModelElement)
	{
	String id = ((MModelElement)obj).getTaggedValue("org.argouml.uid");
	//LOG.debug("Read UID " + id + " from an object!");
	return id;
	}
	*/

	Object rv;
	try {
	    Method m = obj.getClass().getMethod("getItemUID", null);
	    rv = m.invoke(obj, null);
	} catch (NoSuchMethodException nsme) {
	    // Apparently this object had no getItemUID
	    return null;
	} catch (SecurityException se) {
	    // Apparently it had a getItemUID,
	    // but we're not allowed to call it
	    return null;
	} catch (InvocationTargetException tie) {
	    LOG.error("getItemUID for " + obj.getClass() + " threw: ",
		      tie);
	    return null;
	} catch (IllegalAccessException iace) {
	    // Apparently it had a getItemUID,
	    // but we're not allowed to call it
	    return null;
	} catch (IllegalArgumentException iare) {
	    LOG.error("getItemUID for " + obj.getClass()
		      + " takes strange parameter: ",
		      iare);
	    return null;
	} catch (ExceptionInInitializerError eiie) {
	    LOG.error("getItemUID for " + obj.getClass()
		      + " exception: ",
		      eiie);
	    return null;
	}

	if (rv == null) {
	    return null;
	}

	if (!(rv instanceof ItemUID)) {
	    LOG.error("getItemUID for " + obj.getClass()
		      + " returns strange value: " + rv.getClass());
	    return null;
	}

	return rv.toString();
    }

    /**
     * Tries to create a new ID for the object. It uses the reflective
     * properties of java to access a method named setItemUID(ItemUID).
     * If that method exist and doesn't throw when called, then the call
     * is assumed to have been successful and the object is responsible
     * for remembering the ID.
     *
     * @param obj The object to assign a new ID.
     * @return	The new ID of the object, or null.
     */
    protected static String createObjectID(Object obj) {
        // TODO: Do we really want this coupled to the model subsystem?
	if (Model.getFacade().isAModelElement(obj)) {
	    return null;
	}

	Class[] params = new Class[1];
	Object[] mparam;
	params[0] = MYCLASS;
	try {
	    Method m = obj.getClass().getMethod("setItemUID", params);
	    mparam = new Object[1];
	    mparam[0] = new ItemUID();
	    m.invoke(obj, mparam);
	} catch (NoSuchMethodException nsme) {
	    // Apparently this object had no setItemUID
	    return null;
	} catch (SecurityException se) {
	    // Apparently it had a setItemUID,
	    // but we're not allowed to call it
	    return null;
	} catch (InvocationTargetException tie) {
	    LOG.error("setItemUID for " + obj.getClass() + " threw",
		      tie);
	    return null;
	} catch (IllegalAccessException iace) {
	    // Apparently it had a setItemUID,
	    // but we're not allowed to call it
	    return null;
	} catch (IllegalArgumentException iare) {
	    LOG.error("setItemUID for " + obj.getClass()
		      + " takes strange parameter",
		      iare);
	    return null;
	} catch (ExceptionInInitializerError eiie) {
	    LOG.error("setItemUID for " + obj.getClass() + " threw",
		      eiie);
	    return null;
	}

	return mparam[0].toString();
    }
}

