// Copyright (c) 1996-2002 The Regents of the University of California. All
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

import java.util.Enumeration;
import java.util.Vector;

import org.tigris.gef.util.VectorSet;

import org.argouml.cognitive.critics.Critic;

// Needs-more-work: Maybe the exception strings should be internationalized

/**
 * This class is responsible for identifying one critic that has been resolved
 * by the user in one specific context.
 *
 * @author	Michael Stockman
 */
public class ResolvedCritic
{
	/** The name of the critic. */
	String _critic;

	/** The IDs of the objects that define the context of the critic. */
	Vector _offenders;

	/**
	 * Creates a new ResolvedCritic using the name of the Critic and the
	 * Vector of objects that triggered the Critic given as parameters.
	 *
	 * @param	critic	The name of the Critic that has been resolved
	 * @param	offenders	The Vector of related objects.
	 */
	public ResolvedCritic(String critic, Vector offenders)
	{
		_critic = critic;
		if (offenders != null && offenders.size() > 0)
			_offenders = new Vector(offenders);
		else
			_offenders = null;
	}

	/**
	 * Same as {@link #ResolvedCritic(Critic,VectorSet,boolean) ResolvedCritic(c, offs, true)}.
	 *
	 * @throws	UnresolvableException	If some of the objects does
	 *			not have a ItemUID and does not accept a new
	 *			one.
	 */
	public ResolvedCritic(Critic c, VectorSet offs) throws UnresolvableException
	{
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
	public ResolvedCritic(Critic c, VectorSet offs, boolean canCreate) throws UnresolvableException
	{
		if (c == null)
			throw new NullPointerException();

		//System.out.println("Adding resolution for: " + c.getClass());

		try
		{
			if (offs != null && offs.size() > 0)
			{
				_offenders = new Vector(offs.size());
				importOffenders(offs, canCreate);
			}
			else
				_offenders = null;
		}
		catch (UnresolvableException ure)
		{
			try
			{
				getCriticString(c);
			}
			catch (UnresolvableException ure2)
			{
				throw new UnresolvableException(ure2.getMessage() + "\n" + ure.getMessage());
			}
			throw ure;
		}

		_critic = getCriticString(c);
	}

	/**
	 * equals returns true if and only if obj also is a ResolvedCritic,
	 * has the same critic name, and has all related objects that this
	 * object has. Note that it is not required that this object has all
	 * related objects that that object has.
	 *
	 * @param	obj	Object to compare to.
	 * @return	True if equal according to the description, false
	 *		otherwise.
	 */
	public boolean equals(Object obj)
	{
		ResolvedCritic rc;
		int i, j;

		if (obj == null || !(obj instanceof ResolvedCritic))
			return false;

		rc = (ResolvedCritic) obj;

		if (_critic == null)
		{
			if (rc._critic != null)
				return false;
		}
		else if (!_critic.equals(rc._critic))
			return false;

		if (_offenders != null)
		{
			if (rc._offenders == null)
				return false;
			for (i = 0; i < _offenders.size(); i++)
			{
				if (_offenders.elementAt(i) == null)
					continue;

				for (j = 0; j < rc._offenders.size(); j++)
					if (_offenders.elementAt(i).equals(rc._offenders.elementAt(j)))
						break;

				if (j >= rc._offenders.size())
					return false;
			}
		}

		return true;
	}

	/**
	 * Obtains a String that identifies the type of Critic.
	 *
	 * @param	c	A Critic.
	 * @throws	UnreslovableException	Not implemented.
	 */
	protected String getCriticString(Critic c) throws UnresolvableException
	{
		String s = c.getClass().toString();

		// Needs-more-work: Should throw if the string is not good?

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
	protected void importOffenders(VectorSet set, boolean canCreate) throws UnresolvableException
	{
		Enumeration enum = set.elements();
		String fail = null;

		while (enum.hasMoreElements())
		{
			Object obj = enum.nextElement();
			String id = ItemUID.getIDOfObject(obj, canCreate);
			if (id == null)
			{
				if (!canCreate)
					throw new UnresolvableException("ItemUID missing or unable to create for class: " + obj.getClass());
				if (fail == null)
					fail = obj.getClass().toString();
				else
					fail = fail + ", " + obj.getClass().toString();
				System.out.println("Offender " + obj.getClass() + " unresolvable");
				//throw new UnresolvableException("Unable to create ItemUID for class: " + obj.getClass());
			}
			else
				_offenders.add(id);
		}

		if (fail != null)
			throw new UnresolvableException("Unable to create ItemUID for some class(es): " + fail);
	}

	/**
	 * Gets the content of _critic.
	 */
	public String getCritic()
	{
		return _critic;
	}

	/**
	 * Gets the list of related objects, _offenders.
	 */
	public Vector getOffenderList()
	{
		return _offenders;
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("ResolvedCritic: " + _critic + " : ");
		int i;

		for (i = 0; i < _offenders.size(); i++)
		{
			if (i > 0)
				sb.append(", ");
			sb.append(_offenders.elementAt(i));
		}

		return sb.toString();
	}
}

