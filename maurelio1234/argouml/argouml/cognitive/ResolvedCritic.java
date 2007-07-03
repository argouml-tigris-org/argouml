// $Id: ResolvedCritic.java 12950 2007-07-01 08:10:04Z tfmorris $
// Copyright (c) 2002-2007 The Regents of the University of California. All
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

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

// TODO: Maybe the exception strings should be internationalized

/**
 * This class is responsible for identifying one critic that has been resolved
 * by the user in one specific context.
 *
 * @author	Michael Stockman
 */
public class ResolvedCritic {
    /**
     * The logger.
     */
    private static final Logger LOG = Logger.getLogger(ResolvedCritic.class);

    /**
     * The name of the critic.
     */
    private String critic;

    /**
     * The IDs of the objects that define the context of the critic.
     */
    private List<String> offenders;

    /**
     * Creates a new ResolvedCritic using the name of the Critic and the
     * Vector of objects that triggered the Critic given as parameters.
     *
     * @param	cr	The name of the Critic that has been resolved
     * @param	offs	The Vector of related objects.
     * @deprecated for 0.25.4 by tfmorris.  Use 
     * {@link #ResolvedCritic(String, List)}.
     */
    @Deprecated
    public ResolvedCritic(String cr, Vector offs) {
        this(cr, (List) offs);
    }

    /**
     * Creates a new ResolvedCritic using the name of the Critic and the
     * List of objects that triggered the Critic given as parameters.
     *
     * @param   cr      The name of the Critic that has been resolved
     * @param   offs    The List of related objects.
     */
    public ResolvedCritic(String cr, List<String> offs) {
        critic = cr;
        if (offs != null && offs.size() > 0) {
            offenders = new ArrayList<String>(offs);
        } else {
            offenders = null;
        }
    }
    
    /**
     * Same as {@link #ResolvedCritic(Critic,ListSet,boolean)}.
     *
     * @param c The Critic that has been resolved.
     * @param offs The set of objects that triggered the Critic.
     * @throws	UnresolvableException	If some of the objects does
     *			not have a ItemUID and does not accept a new
     *			one.
     */
    public ResolvedCritic(Critic c, ListSet offs)
    	throws UnresolvableException {

	this(c, offs, true);
    }

    /**
     * Creates a new ResolvedCritic from the given information.
     *
     * @param	c	The Critic that has been resolved.
     * @param	offs	The set of objects that triggered the Critic.
     * @param	canCreate	If it should try to assign new
     *				ItemUIDs to objects that doesn't have.
     * @throws	UnresolvableException	If some of the objects does
     *			not have a ItemUID and does not accept a new
     *			one.
     */
    public ResolvedCritic(Critic c, ListSet offs, boolean canCreate)
	throws UnresolvableException {
	if (c == null) {
	    throw new NullPointerException();
	}

	//LOG.debug("Adding resolution for: " + c.getClass() + " " + canCreate);

	try {
	    if (offs != null && offs.size() > 0) {
		offenders = new ArrayList<String>(offs.size());
		importOffenders(offs, canCreate);
	    } else {
	        offenders = null;
	    }
	} catch (UnresolvableException ure) {
	    try {
		getCriticString(c);
	    } catch (UnresolvableException ure2) {
		throw new UnresolvableException(ure2.getMessage() + "\n"
						+ ure.getMessage());
	    }
	    throw ure;
	}

	critic = getCriticString(c);
    }

    /*
     * @see java.lang.Object#hashCode()
     *
     * This is a rather bad hash solution but with the {@link #equals(Object)}
     * defined as below, it is not possible to do better.
     */
    public int hashCode() {
        if (critic == null) {
            return 0;
        }
        return critic.hashCode();
    }

    /**
     * equals returns true if and only if obj also is a ResolvedCritic,
     * has the same critic name, and has all related objects that this
     * object has. Note that it is not required that this object has all
     * related objects that that object has.<p>
     *
     * Formally that is inconsistent with {@link Object#equals(Object)
     * equals as specified in java.lang.Object},
     * but it was probably practical somehow.<p>
     *
     * The param obj is the Object to compare to.
     * Returns true if equal according to the description, false
     * otherwise.
     *
     * {@inheritDoc}
     */
    public boolean equals(Object obj) {
	ResolvedCritic rc;
	int i, j;

	if (obj == null || !(obj instanceof ResolvedCritic)) {
	    return false;
	}

	rc = (ResolvedCritic) obj;

	if (critic == null) {
	    if (rc.critic != null) {
	        return false;
	    }
	} else if (!critic.equals(rc.critic)) {
	    return false;
	}

	if (offenders == null) {
	    return true;
	}

	if (rc.offenders == null) {
	    return false;
	}

	for (i = 0; i < offenders.size(); i++) {
	    if (offenders.get(i) == null) {
	        continue;
	    }

	    for (j = 0; j < rc.offenders.size(); j++) {
	        if (offenders.get(i).equals(rc.offenders.get(j))) {
	            break;
	        }
	    }

	    if (j >= rc.offenders.size()) {
	        return false;
	    }
	}

	return true;
    }

    /**
     * Obtains a String that identifies the type of Critic.
     *
     * @param	c	A Critic.
     * @throws	UnresolvableException	Not implemented.
     * @return	A identifying name of the critic.
     */
    protected String getCriticString(Critic c) throws UnresolvableException {
        // TODO: Should throw if the string is not good?
        if (c == null) {
            throw (new UnresolvableException("Critic is null"));
        }
        String s = c.getClass().toString();
	return s;
    }

    /**
     * Imports the set of related objects in set to this object. If an
     * object does not have an ItemUID, canCreate determines if one will
     * be provided. If some object does not have an ItemUID and canCreate
     * is false och the object does not accept and ItemUID, then
     * UnresolvableException is thrown.
     *
     * @param	set	The set of related objects to import.
     * @param	canCreate	If ItemUIDs are allowed to be created.
     * @throws	UnresolvableException if not all objects can be
     *		imported.
     */
    protected void importOffenders(ListSet set, boolean canCreate)
	throws UnresolvableException {

	String fail = null;

        for (Object obj : set) {
	    String id = ItemUID.getIDOfObject(obj, canCreate);
	    if (id == null) {
		if (!canCreate) {
		    throw new UnresolvableException("ItemUID missing or "
						    + "unable to "
						    + "create for class: "
						    + obj.getClass());
		}

		if (fail == null) {
		    fail = obj.getClass().toString();
		} else {
		    fail = fail + ", " + obj.getClass().toString();
		}

		LOG.warn("Offender " + obj.getClass() + " unresolvable");

		// Use this for fast fail instead.
		// Sacrificed for complete fail. d00mst
		//throw new UnresolvableException(
		//	"Unable to create ItemUID for class: "
		//	+ obj.getClass());
	    } else {
	        offenders.add(id);
	    }
	}

	if (fail != null) {
	    throw new UnresolvableException("Unable to create ItemUID for "
					    + "some class(es): "
					    + fail);
	}
    }

    /**
     * Gets the content of critic.
     *
     * @return The critic this instance resolves.
     */
    public String getCritic() {
	return critic;
    }

    /**
     * Gets the list of related objects, offenders.
     *
     * @return The list of offenders of the critic this instance resolved.
     */
    public List getOffenderList() {
	return offenders;
    }

    /*
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	StringBuffer sb =
	    new StringBuffer("ResolvedCritic: " + critic + " : ");
	for (int i = 0; i < offenders.size(); i++) {
	    if (i > 0) {
	        sb.append(", ");
	    }
	    sb.append(offenders.get(i));
	}

	return sb.toString();
    }
}

