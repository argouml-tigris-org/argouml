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

package org.argouml.xml.todo;

import java.util.Vector;
import java.util.Enumeration;
import org.argouml.cognitive.ResolvedCritic;

/**
 * A helper class to provide a view of a ResolvedCritic that is particularly
 * suited for saving to an XML file.
 *
 * @see	ResolvedCritic
 * @author Michael Stockman
 */
public class ResolvedCriticXMLHelper
{
	/** The ResolvedCritic this instance helps. */
	protected final ResolvedCritic _item;

	/**
	 * Creates a new ResolvedCriticXMLHelper for helping item.
	 *
	 * @param	item	The ResolvedCritic to expose.
	 */
	public ResolvedCriticXMLHelper(ResolvedCritic item)
	{
		if (item == null)
			throw new NullPointerException();
		_item = item;
	}

	/**
	 * Encodes the critic of this ResolvedCritic in an XML safe way and
	 * returns the new String. The String can be regained by running the
	 * returned String through
	 * {@link TodoParser#decode TodoParser::decode}.
	 *
	 * @return	The encoded critic.
	 */
	public String getCritic()
	{
		return TodoParser.encode(_item.getCritic());
	}

	/**
	 * Gets the offender vector of this critic where each offender is
	 * wrapped in an OffenderXMLHelper.
	 *
	 * @return	A Vector of OffenderXMLHelpers, or null if there are
	 *		no offenders.
	 * @see	OffenderXMLHelper
	 */
	public Vector getOffenderList()
	{
		Vector in = _item.getOffenderList();
		Enumeration enum;
		Vector out;

		if (in == null)
			return null;
		out = new Vector();
		enum = in.elements();
		while (enum.hasMoreElements())
		{
			try
			{
				out.addElement(new OffenderXMLHelper((String)enum.nextElement()));
			}
			catch (ClassCastException cce)
			{
			}
		}

		return out;
	}
}

