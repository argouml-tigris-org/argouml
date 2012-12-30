/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
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

package org.argouml.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.argouml.application.api.AbstractArgoJPanel;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ChildGenRelated;
import org.argouml.uml.PredicateSearch;
import org.argouml.uml.TMResults;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.util.ChildGenerator;


/**
 * The results tab for the find dialog.
 * <p>
 * NOTE: An incompatible change was made to the public API for this class
 * before the release of ArgoUML 0.26 to remove exposed internal implementation
 * details (GEF).
 */
public class TabResults
        extends AbstractArgoJPanel
        implements
                Runnable,
                MouseListener,
                ActionListener,
                ListSelectionListener,
                KeyListener {
    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(TabResults.class.getName());

    private static int numJumpToRelated;

    /**
     * Insets in pixels.
     */
    private static final int INSET_PX = 3;

    private PredicateSearch pred;
    private ChildGenerator cg;
    private Object root;
    private JSplitPane mainPane;
    private List results = new ArrayList();
    private Set diagramResults = new HashSet();
    private List related = new ArrayList();
    private List<ArgoDiagram> diagrams = new ArrayList<ArgoDiagram>();
    private boolean relatedShown;

    private JLabel resultsLabel = new JLabel();
    private JTable resultsTable;
    private TMResults resultsModel;

    private JLabel relatedLabel = new JLabel();
    private JTable relatedTable = new JTable(4, 4);
    private TMResults relatedModel = new TMResults();

    /**
     * The constructor.
     *
     */
    public TabResults() {
	this(true);
    }

    /**
     * The constructor.
     *
     * @param showRelated true if related results should be shown
     */
    public TabResults(boolean showRelated) {
	super("Results", true);
	relatedShown = showRelated;
	setLayout(new BorderLayout());
	resultsTable = new JTable(10, showRelated ? 4 : 3);
	resultsModel = new TMResults(showRelated);

	JPanel resultsW = new JPanel();
	JScrollPane resultsSP = new JScrollPane(resultsTable);
	resultsW.setLayout(new BorderLayout());
	resultsLabel.setBorder(BorderFactory.createEmptyBorder(
                INSET_PX, INSET_PX, INSET_PX, INSET_PX));
	resultsW.add(resultsLabel, BorderLayout.NORTH);
	resultsW.add(resultsSP, BorderLayout.CENTER);
	resultsTable.setModel(resultsModel);
	resultsTable.addMouseListener(this);
	resultsTable.addKeyListener(this);
	resultsTable.getSelectionModel().addListSelectionListener(
								   this);
	resultsTable.setSelectionMode(
				       ListSelectionModel.SINGLE_SELECTION);
	resultsW.setMinimumSize(new Dimension(100, 100));

	JPanel relatedW = new JPanel();
	if (relatedShown) {
	    JScrollPane relatedSP = new JScrollPane(relatedTable);
	    relatedW.setLayout(new BorderLayout());
            relatedLabel.setBorder(BorderFactory.createEmptyBorder(
                    INSET_PX, INSET_PX, INSET_PX, INSET_PX));
	    relatedW.add(relatedLabel, BorderLayout.NORTH);
	    relatedW.add(relatedSP, BorderLayout.CENTER);
	    relatedTable.setModel(relatedModel);
	    relatedTable.addMouseListener(this);
	    relatedTable.addKeyListener(this);
	    relatedW.setMinimumSize(new Dimension(100, 100));
	}

	if (relatedShown) {
	    mainPane =
		new JSplitPane(JSplitPane.VERTICAL_SPLIT,
			       resultsW,
			       relatedW);
	    add(mainPane, BorderLayout.CENTER);
	} else {
	    add(resultsW, BorderLayout.CENTER);
	}

    }

    /**
     * @param p the predicate for the search
     *            <p>
     *            NOTE: The type of this parameter was changed incompatibly
     *            before 0.26 from org.argouml.uml.PredicateFind to
     *            org.argouml.uml.PredicateSearch.
     */
    public void setPredicate(PredicateSearch p) {
        pred = p;
    }

    /**
     * @param r the root object for the search
     */
    public void setRoot(Object r) {
	root = r;
    }

    /**
     * @param gen the generator.
     *            <p>
     *            NOTE: The type of this parameter was changed incompatibly
     *            before 0.26 from org.tigris.gef.util.ChildGenerator to
     *            org.argouml.util.ChildGenerator.
     */
    public void setGenerator(ChildGenerator gen) {
        cg = gen;
    }

    /**
     * @param res the results
     * @param dia the diagrams
     */
    public void setResults(List res, List dia) {
        results = res;
        diagrams = dia;
        Object[] msgArgs = {Integer.valueOf(results.size()) };
        resultsLabel.setText(Translator.messageFormat(
            "dialog.tabresults.results-items", msgArgs));
        resultsModel.setTarget(results, diagrams);
        relatedModel.setTarget((List) null, (List) null);
        relatedLabel.setText(
            Translator.localize("dialog.tabresults.related-items"));
    }

    /*
     * @see org.argouml.ui.AbstractArgoJPanel#spawn()
     */
    public AbstractArgoJPanel spawn() {
	TabResults newPanel = (TabResults) super.spawn();
	if (newPanel != null) {
	    newPanel.setResults(results, diagrams);
	}
	return newPanel;
    }

    /**
     * Handle a doubleclick on the results tab.
     */
    public void doDoubleClick() {
	myDoubleClick(resultsTable);
    }

    /**
     * Select the result at the given index.
     *
     * @param index the given index
     */
    public void selectResult(int index) {
	if (index < resultsTable.getRowCount()) {
	    resultsTable.getSelectionModel().setSelectionInterval(index,
								   index);
	}
    }

    ////////////////////////////////////////////////////////////////
    // ActionListener implementation

    /*
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent ae) {
        // ignored
    }

    ////////////////////////////////////////////////////////////////
    // MouseListener implementation

    /*
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    public void mousePressed(MouseEvent me) {
        // ignored
    }

    /*
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    public void mouseReleased(MouseEvent me) {
        // ignored
    }

    /*
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    public void mouseClicked(MouseEvent me) {
	if (me.getClickCount() >= 2) {
            myDoubleClick(me.getSource());
        }
    }

    /*
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     */
    public void mouseEntered(MouseEvent me) {
        // ignored
    }

    /*
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    public void mouseExited(MouseEvent me) {
        // ignored
    }

    private void myDoubleClick(Object src) {
	Object sel = null;
	ArgoDiagram d = null;
	if (src == resultsTable) {
	    int row = resultsTable.getSelectionModel().getMinSelectionIndex();
	    if (row < 0) {
                return;
            }
	    sel = results.get(row);
	    d = diagrams.get(row);
	} else if (src == relatedTable) {
	    int row = relatedTable.getSelectionModel().getMinSelectionIndex();
	    if (row < 0) {
                return;
            }
	    numJumpToRelated++;
	    sel = related.get(row);
	}

	if (d != null) {
            LOG.log(Level.FINE,
                    "go {0} in {1}", new Object[]{sel, d.getName()});
            TargetManager.getInstance().setTarget(d);
        }
	if (Model.getFacade().isATaggedValue(sel)) {
	    // For tagged values, use their containing ModelElement since they
	    // don't have property panels of their own
	    // TODO: May want to do this for other types too?
	    sel = Model.getFacade().getModelElementContainer(sel);
	}
	TargetManager.getInstance().setTarget(sel);
    }

    ////////////////////////////////////////////////////////////////
    // KeyListener implementation

    /*
     * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
     */
    public void keyPressed(KeyEvent e) {
        if (!e.isConsumed() && e.getKeyChar() == KeyEvent.VK_ENTER) {
            e.consume();
            myDoubleClick(e.getSource());
        }
    }

    /*
     * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
     */
    public void keyReleased(KeyEvent e) {
        // ignored
    }

    /*
     * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
     */
    public void keyTyped(KeyEvent e) {
        // ignored
    }

    ////////////////////////////////////////////////////////////////
    // ListSelectionListener implementation

    /*
     * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
     */
    public void valueChanged(ListSelectionEvent lse) {
	if (lse.getValueIsAdjusting()) {
	    return;
	}
	if (relatedShown) {
	    int row = lse.getFirstIndex();
	    Object sel = results.get(row);
            LOG.log(Level.FINE, "selected {0}", sel);
	    related.clear();
	    Enumeration elems =
		ChildGenRelated.getSingleton().gen(sel);
	    if (elems != null) {
		while (elems.hasMoreElements()) {
		    related.add(elems.nextElement());
		}
	    }
	    relatedModel.setTarget(related, null);
	    Object[] msgArgs = {Integer.valueOf(related.size()) };
	    relatedLabel.setText(Translator.messageFormat(
                "dialog.find.related-elements", msgArgs));
	}
    }

    /*
     * @see java.lang.Runnable#run()
     */
    public void run() {
	resultsLabel.setText(Translator.localize("dialog.find.searching"));
	results.clear();
	depthFirst(root, null);
	setResults(results, diagrams);
    }

    /**
     * Do a recursive depth first search of the project. The children of the
     * root are all user models and all the diagrams. Searches of the diagrams
     * will terminate immediately if they fail to match, but the models are
     * searched to their leaves, even if the diagram predicate doesn't match an
     * empty diagram name.  This is inefficient, but shouldn't be a common
     * case.<p>
     *
     * Another effect of the current algorithm is that model elements will
     * appear once for each diagram that they are included in PLUS an additional
     * time with no diagram name given.  It would be slightly more friendly
     * have the non-diagram list only includes those elements which didn't
     * appear in any other diagram, but we're not going to do the bookkeeping
     * for now.  - tfm 20060214
     */
    private void depthFirst(Object node, ArgoDiagram lastDiagram) {
	if (node instanceof ArgoDiagram) {
	    lastDiagram = (ArgoDiagram) node;
	    diagramResults.clear();
	    if (!pred.matchDiagram(lastDiagram)) {
                return;
            }
	    // diagrams are not placed in search results
	}
	Iterator iterator = cg.childIterator(node);
	while (iterator.hasNext()) {
	    Object child = iterator.next();
	    if (pred.evaluate(child)
                    && (lastDiagram != null || pred.matchDiagram(""))) {
	        // Only return once per diagram so we don't, for example, find
	        // a class as a diagram element and also as a child of a package
	        // which is on the diagram
                if (!diagramResults.contains(child)) {
                    diagramResults.add(child);
                    results.add(child);
                    diagrams.add(lastDiagram);
                }
            }
	    depthFirst(child, lastDiagram);
	}
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = 4980167466628873068L;
}
