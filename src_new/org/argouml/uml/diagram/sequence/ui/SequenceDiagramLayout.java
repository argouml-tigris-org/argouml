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

package org.argouml.uml.diagram.sequence.ui;

import java.util.Collection;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.tigris.gef.base.Layer;
import org.tigris.gef.base.LayerPerspectiveMutable;
import org.tigris.gef.graph.MutableGraphModel;
import org.tigris.gef.presentation.Fig;

public class SequenceDiagramLayout extends LayerPerspectiveMutable {
	/**
	 * The distance between two objects on the sequence diagram
	 */
	public final static int OBJECT_DISTANCE = 30;

	/**
	 * The distance between the left side of the diagram and the first FigObject
	 */
	public final static int DIAGRAM_LEFT_MARGE = 50;

	/**
	 * The distance between the top side of the diagram and the top of the highest FigObject
	 */
	public final static int DIAGRAM_TOP_MARGE = 50;

	public SequenceDiagramLayout(String name, MutableGraphModel gm) {
		super(name, gm);

	}

	/** 
	 * @see org.tigris.gef.base.LayerPerspective#putInPosition(org.tigris.gef.presentation.Fig)
	 */
	public void putInPosition(Fig f) {
		if (f instanceof FigObject) {
			distributeFigObjects();
		} else
			super.putInPosition(f);
	}

	private void distributeFigObjects() {
		Layer lay = this;
		distributeFigObjectsHorizontal(lay.getContentsNoEdges());
		distributeFigObjectsVertical(lay.getContentsNoEdges());
	}

	private void distributeFigObjectsVertical(Collection figObjects) {
		Iterator it = figObjects.iterator();
		SortedSet highestSet = new TreeSet();
		while (it.hasNext()) {
			Fig fig = (Fig)it.next();
			if (fig instanceof FigObject) {
				highestSet.add(new Integer(fig.getHalfHeight()));
			}
		}
		int heighestHalfHeight = ((Integer)highestSet.last()).intValue();
		it = figObjects.iterator();
		while (it.hasNext()) {
			Fig fig = (Fig)it.next();
			if (fig instanceof FigObject) {      
				fig.setY(DIAGRAM_TOP_MARGE + (heighestHalfHeight - fig.getHalfHeight()));
				fig.damage();
			}
		}
	}

	private void distributeFigObjectsHorizontal(Collection figObjects) {
		Iterator it = figObjects.iterator();
		SortedMap positionMap = new TreeMap();
		while (it.hasNext()) {
			Fig fig = (Fig)it.next();
			if (fig instanceof FigObject) {
				positionMap.put(
					new Integer(fig.getX() + fig.getHalfWidth()),
					fig);
			}
		}
		int position = DIAGRAM_LEFT_MARGE;
		int mapSize = positionMap.size();
		for (int i = 0; i < mapSize; i++) {
			Object key = positionMap.firstKey();
			Fig fig = (Fig)positionMap.get(key);
			if (fig.getX() != position) {
				// move fig
				fig.setX(position);
				fig.damage();
			}
			position += (fig.getWidth() + OBJECT_DISTANCE);
			positionMap.remove(key);
		}
	}

	/** 
	 * @see org.tigris.gef.base.Layer#presentationFor(java.lang.Object)
	 */
	public Fig presentationFor(Object obj) {
		if (getGraphModel().getNodes().contains(obj)
			|| getGraphModel().getEdges().contains(obj))
			return super.presentationFor(obj);
		else
			return null;
	}

}