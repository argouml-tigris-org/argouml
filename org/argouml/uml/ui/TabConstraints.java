
// $Id$
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

package org.argouml.uml.ui;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JToolBar;
import javax.swing.JOptionPane;
import javax.swing.event.EventListenerList;

import org.apache.log4j.Category;

import org.argouml.model.ModelFacade;
import org.argouml.model.uml.UmlFactory;
import org.argouml.ocl.ArgoFacade;
import org.argouml.ocl.OCLUtil;
import org.argouml.ui.TabSpawnable;
import org.argouml.ui.targetmanager.TargetEvent;

import org.tigris.gef.presentation.Fig;

import ru.novosoft.uml.foundation.core.MClassifier;
import ru.novosoft.uml.foundation.core.MConstraint;
import ru.novosoft.uml.foundation.core.MModelElement;

import tudresden.ocl.OclTree;
import tudresden.ocl.check.OclTypeException;
import tudresden.ocl.gui.ConstraintRepresentation;
import tudresden.ocl.gui.EditingUtilities;
import tudresden.ocl.gui.OCLEditor;
import tudresden.ocl.gui.OCLEditorModel;
import tudresden.ocl.gui.events.ConstraintChangeEvent;
import tudresden.ocl.gui.events.ConstraintChangeListener;
import tudresden.ocl.parser.OclParserException;
import tudresden.ocl.OclException;

/**
  * Tab for OCL constraint editing.
  *
  * <p>$Id$
  *
  * @author v1.0: Falk Finger
  * @author v2.0: Steffen Zschaler
  */
public class TabConstraints extends TabSpawnable implements TabModelTarget {

    private static Category _cat = Category.getInstance(TabConstraints.class);

    /**
     * The actual editor pane.
     */
    private OCLEditor m_ocleEditor;

    /**
     * The current target element.
     */
    private MModelElement m_mmeiTarget;

    public TabConstraints() {
        super("tab.constraints");

        setLayout(new BorderLayout(0, 0));

        m_ocleEditor = new OCLEditor();
        m_ocleEditor.setOptionMask(OCLEditor.OPTIONMASK_TYPECHECK
                   /*|  //removed to workaround problems with autosplit
                     OCLEditor.OPTIONMASK_AUTOSPLIT*/ );
        m_ocleEditor.setDoAutoSplit(false);
        setToolbarRollover(true);
        setToolbarFloatable(false);

        add(m_ocleEditor);
    }

    /** Set the toolbar rollover style.
     * @param enable if true then button borders do not become visible until mouse rolls over button
     */
    private void setToolbarRollover(boolean enable) {
        getOclToolbar().putClientProperty("JToolBar.isRollover", Boolean.TRUE);
    }

    /** Set the toolbar floating style
     * @param enable If true then the toolbar can be floated and docked
     */
    private void setToolbarFloatable(boolean enable) {
        getOclToolbar().setFloatable(false);
    }

    /** Get a reference to the toolbar object contained in the OCLEditor component.
     * This is currently a nasty hack. We really require an interface method
     * on OCLEditor (Bob Tarling 8 Feb 2003).
     * @return The toolbar
     */
    private JToolBar getOclToolbar() {
        return (JToolBar) m_ocleEditor.getComponent(0);
    }

    //TabModelTarget interface methods
    
    /**
     * Should this tab be activated for the current target element?
     *
     * <p>Argo only supports constraints for Classes and Features
     * (eg. Attributes and Operations) currently.
     */
    public boolean shouldBeEnabled(Object target) {
        target = (target instanceof Fig) ? ((Fig) target).getOwner() : target;
        return (ModelFacade.isAClass(target) || ModelFacade.isAFeature(target));
    }

    /**
     * Get the target element whose properties this tab presents.
     */
    public Object getTarget() {
        return m_mmeiTarget;
    }

    /**
     * Refresh the tab because the target has changed.
     */
    public void refresh() {
        setTarget(m_mmeiTarget);
    }

    /**
     * Set the target element to be displayed in this tab. Only model elements
     * will be accepted by the constraint tab.
     */
    public void setTarget(Object oTarget) {
        oTarget = (oTarget instanceof Fig) ? ((Fig) oTarget).getOwner() : oTarget;
        if (!(org.argouml.model.ModelFacade.isAModelElement(oTarget))) {
            m_mmeiTarget = null;
            return;
        }

        m_mmeiTarget = (MModelElement) oTarget;

        // Set editor's model
        m_ocleEditor.setModel(new ConstraintModel(m_mmeiTarget));
    }

    /**
     * Adapter to provide information and a manipulation interface for a
     * target element's set of constraints to the constraint editor.
     */
    private static class ConstraintModel implements OCLEditorModel {

        /**
     * The target element being edited.
     */
        private MModelElement m_mmeiTarget;

        /**
     * A list of all the constraints in m_nmeiTarget. This is necessary to
     * induce a consistent order on the constraints.
     */
        private ArrayList m_alConstraints;

        /**
     * List of listeners.
     */
        private EventListenerList m_ellListeners = new EventListenerList();

        /**
     * Construct a new ConstraintModel.
     */
        public ConstraintModel(MModelElement mmeiTarget) {
            super();

            m_mmeiTarget = mmeiTarget;

            m_alConstraints = new ArrayList(m_mmeiTarget.getConstraints());
        }

        /**
     * Return the number of constraints in this model.
     */
        public int getConstraintCount() {
            return m_alConstraints.size();
        }

        /**
     * Return the constraint with the specified index.
     *
     * @param nIdx the index of the constraint to be returned.
     *             0 <= nIdx < {@link #getConstraintCount}
     */
        public ConstraintRepresentation getConstraintAt(int nIdx) {
            return representationFor(nIdx);
        }

        /** 
     * Remove the specified constraint from the model.
     *
     * @param nIdx the index of the constraint to be removed.
     *             0 <= nIdx < {@link #getConstraintCount}
     */
        public void removeConstraintAt(int nIdx) {
            if ((nIdx < 0) || (nIdx > m_alConstraints.size())) {
                return;
            }

            MConstraint mc = (MConstraint) m_alConstraints.remove(nIdx);

            if (mc != null) {
                m_mmeiTarget.removeConstraint(mc);
            }

            fireConstraintRemoved(mc, nIdx);
        }

        /**
         * Add a fresh constraint to the model.
         * 
         * <p>There are 2 restrictions on what can be parsed, given
         * the current OCL grammar:
         * <ol>
         *   <li>Class names must have a capital first letter.</li>
         *   <li>Feature name must have a lower case first letter.
         *   </li>
         * </ol>
         */
        public void addConstraint() {
            
            // check ocl parsing constraints
            // sz9: I think this should read mmeContext = OclUtil.getInnerMostEnclosingNamespace (m_mmeiTarget);
            MModelElement mmeContext = m_mmeiTarget;

            while (!(org.argouml.model.ModelFacade.isAClassifier(mmeContext)) && 
                   (mmeContext != null)) {
                mmeContext = mmeContext.getModelElementContainer();
            }
            
            if (ModelFacade.getName(mmeContext) == null   ||
                ModelFacade.getName(m_mmeiTarget) == null ||
                !Character.isUpperCase(
                    ModelFacade.getName(mmeContext).charAt(0)
                  ) ||
                (ModelFacade.isAClass (m_mmeiTarget) &&
                 !Character.isUpperCase(
                     ModelFacade.getName(m_mmeiTarget).charAt(0)
                   )
                ) ||
                (ModelFacade.isAFeature(m_mmeiTarget) && 
                 !Character.isLowerCase(
                     ModelFacade.getName(m_mmeiTarget).charAt(0)
                   )
                )
               ) 
            {
                // TODO I18n
                JOptionPane.showMessageDialog(null,
                          "The OCL Toolkit requires that:\n\n" +
                          "Class names have a capital first letter and\n" +
                          "Attribute or Operation names have a lower case first letter.",
                          "Require Correct name convention:",
                          JOptionPane.ERROR_MESSAGE);
                // do not create a constraint:
                return;
            }
            
            // null elements represent new constraints, which will be added to the
            // target the first time any actual editing takes place.
            // This is done to ensure syntactical correctness of constraints stored
            // with the target.
            m_alConstraints.add(null);

            fireConstraintAdded();
        }

        private class CR implements ConstraintRepresentation {

            /**
         * The constraint being represented.
         */
            private MConstraint m_mcConstraint;

            /**
         * The constraint's index in the list of constraints. Necessary only for
         * new constraints, where m_mcConstraint is still null.
         */
            private int m_nIdx = -1;

            public CR(MConstraint mcConstraint, int nIdx) {
                super();

                m_mcConstraint = mcConstraint;
                m_nIdx = nIdx;
            }

            public CR(int nIdx) {
                this(null, nIdx);
            }

            /**
         * Get the name of the constraint.
         */
            public String getName() {
                if (m_mcConstraint == null) {
                    return "newConstraint";
                } else {
                    return m_mcConstraint.getName();
                }
            }

            /**
         * Get the constraint's body.
         */
            public String getData() {
                if (m_mcConstraint == null) {
                    return OCLUtil.getContextString(m_mmeiTarget);
                } else {
                    return m_mcConstraint.getBody().getBody();
                }
            }

            /**
         * Set the constraint's body text. For the exceptions the detailed message must
         * be human readable.
         *
         * @param sData the new body of the constraint
         *
         * @exception IllegalStateException if the constraint is not in a state to
         *     accept body changes.
         * @exception OclParserException if the specified constraint is not
         *     syntactically correct.
         * @exception OclTypeException if the specified constraint does not adhere by
         *     OCL type rules.
         */
            public void setData(String sData, EditingUtilities euHelper)
                throws OclParserException, OclTypeException {
                // Parse and check specified constraint.
                OclTree tree = null;

                MModelElement mmeContext = m_mmeiTarget;

                try {
                    while (!(org.argouml.model.ModelFacade.isAClassifier(mmeContext))) {
                        mmeContext = mmeContext.getModelElementContainer();
                    }

                    try {
                        tree =
                            euHelper.parseAndCheckConstraint(
                                 sData,
                                 new ArgoFacade(mmeContext));
                    } catch (java.io.IOException ioe) {
                        // Ignored: Highly unlikely, and what would we do anyway?
                        // log it
                        _cat.error(
                   "problem parsing And Checking Constraints",
                   ioe);
                        return;
                    }

                    // Split constraint body, if user wants us to
                    if (euHelper.getDoAutoSplit()) {
                        List lConstraints = euHelper.splitConstraint(tree);

                        if (lConstraints.size() > 0) {
                            removeConstraintAt(m_nIdx);

                            for (Iterator i = lConstraints.iterator();
                 i.hasNext(); )
                {
                                OclTree ocltCurrent = (OclTree) i.next();

                                MConstraint mc =
                                    UmlFactory.getFactory().getCore()
                        .createConstraint();
                                mc.setName(ocltCurrent.getConstraintName());
                                mc.setBody(
                       UmlFactory
                       .getFactory()
                       .getDataTypes()
                       .createBooleanExpression(
                                    "OCL",
                                    ocltCurrent.getExpression()));

                                m_mmeiTarget.addConstraint(mc);

                                // the constraint _must_ be owned by a namespace
                                if (m_mmeiTarget.getNamespace() != null) {
                                    m_mmeiTarget
                                        .getNamespace()
                                        .addOwnedElement(
                             mc);
                                }
                                else if (mmeContext.getNamespace() != null) {
                                    
                                    mmeContext.getNamespace().addOwnedElement(
                                          m_mcConstraint);
                                }

                                m_alConstraints.add(mc);
                                fireConstraintAdded();
                            }

                            return;
                        }
                    }

                    // Store constraint body
                    MConstraint mcOld = null;

                    if (m_mcConstraint == null) {
                        // New constraint, first time setData is called
                        m_mcConstraint =
                            UmlFactory
                .getFactory()
                .getCore()
                .createConstraint();

                        m_mcConstraint.setName("newConstraint");
                        m_mcConstraint.setBody(
                           UmlFactory
                           .getFactory()
                           .getDataTypes()
                           .createBooleanExpression(
                                    "OCL",
                                    sData));

                        m_mmeiTarget.addConstraint(m_mcConstraint);

                        // the constraint _must_ be owned by a namespace
                        if (m_mmeiTarget.getNamespace() != null) {
                            m_mmeiTarget.getNamespace().addOwnedElement(
                                    m_mcConstraint);
                        }
                        else if (mmeContext.getNamespace() != null) {
                            
                            mmeContext.getNamespace().addOwnedElement(
                                      m_mcConstraint);
                        }

                        m_alConstraints.set(m_nIdx, m_mcConstraint);
                    } else {
                        mcOld =
                            UmlFactory
                .getFactory()
                .getCore()
                .createConstraint();
                        mcOld.setName(m_mcConstraint.getName());
                        mcOld.setBody(
                      UmlFactory
                      .getFactory()
                      .getDataTypes()
                      .createBooleanExpression(
                                   "OCL",
                                   m_mcConstraint.getBody().getBody()));

                        m_mcConstraint.setBody(
                           UmlFactory
                           .getFactory()
                           .getDataTypes()
                           .createBooleanExpression(
                                    "OCL",
                                    sData));
                    }

                    fireConstraintDataChanged(m_nIdx, mcOld, m_mcConstraint);

                } catch (OclTypeException pe) {
                    _cat.warn("There was some sort of OCL Type problem", pe);
                    throw pe;
                } catch (OclParserException pe1) {
                    _cat.warn(
                  "Could not parse the constraint",
                  pe1);
                    throw pe1;
                } catch (OclException oclExc) {
                    // a runtime exception that occurs when some
                    // internal test fails
                    _cat.warn(
                  "There was some unidentified problem");
                    throw oclExc;
                }
            }

            /**
         * Set the constraint's name.
         */
            public void setName(
                final String sName,
                final EditingUtilities euHelper) {
                if (m_mcConstraint != null) {
                    // Check name for consistency with spec
                    if (!euHelper.isValidConstraintName(sName)) {
                        throw new IllegalArgumentException("Please specify a valid name.");
                    }

                    // Set name
                    MConstraint mcOld =
                        UmlFactory.getFactory().getCore().createConstraint();
                    mcOld.setName(m_mcConstraint.getName());
                    mcOld.setBody(
                  UmlFactory
                  .getFactory()
                  .getDataTypes()
                  .createBooleanExpression(
                               "OCL",
                               m_mcConstraint.getBody().getBody()));

                    m_mcConstraint.setName(sName);

                    fireConstraintNameChanged(m_nIdx, mcOld, m_mcConstraint);

                    // Also set name in constraint body -- Added 03/14/2001
                    try {
                        OclTree tree = null;

                        MModelElement mmeContext = m_mmeiTarget;
                        while (!(org.argouml.model.ModelFacade.isAClassifier(mmeContext))) {
                            mmeContext = mmeContext.getModelElementContainer();
                        }

                        tree =
                            euHelper.parseAndCheckConstraint(
                                 m_mcConstraint.getBody().getBody(),
                                 new ArgoFacade(mmeContext));

                        if (tree != null) {
                            tree.apply(new tudresden.ocl.parser.analysis
                           .DepthFirstAdapter() 
                {
                    int name_ID = 0;
                    public void caseAConstraintBody(
                                    tudresden
                                    .ocl
                                    .parser
                                    .node
                                    .AConstraintBody node) {
                    // replace name
                    if (name_ID == 0) {
                        node.setName(
                             new tudresden
                             .ocl
                             .parser
                             .node
                             .TName(
                                sName));
                    } else {
                        node.setName(
                             new tudresden
                             .ocl
                             .parser
                             .node
                             .TName(
                                sName + "_" + name_ID));
                    }
                    name_ID++;
                    }
                });

                            setData(tree.getExpression(), euHelper);
                        }
                    } catch (Throwable t) {
                        // OK, so that didn't work out... Just ignore any problems and don't
                        // set the name in the constraint body
                        // better had log it.
                        _cat.error("some unidentified problem", t);
                    }
                } else {
                    throw new IllegalStateException("Please define and submit a constraint body first.");
                }
            }
        }

        /**
     * Create a representation adapter for the given constraint.
         *
     */
        private CR representationFor(int nIdx) {
            if ((nIdx < 0) || (nIdx >= m_alConstraints.size())) {
                return null;
            }

            MConstraint mc = (MConstraint) m_alConstraints.get(nIdx);

            if (mc != null) {
                return new CR(mc, nIdx);
            } else {
                return new CR(nIdx);
            }
        }

        /**
     * Add a listener to be informed of changes in the model.
     *
     * @param ccl the new listener
     */
        public void addConstraintChangeListener(ConstraintChangeListener ccl) {
            m_ellListeners.add(ConstraintChangeListener.class, ccl);
        }

        /**
     * Remove a listener to be informed of changes in the model.
     *
     * @param ccl the listener to be removed
     */
        public void removeConstraintChangeListener(ConstraintChangeListener ccl) {
            m_ellListeners.remove(ConstraintChangeListener.class, ccl);
        }

        protected void fireConstraintRemoved(MConstraint mc, int nIdx) {
            // Guaranteed to return a non-null array
            Object[] listeners = m_ellListeners.getListenerList();

            ConstraintChangeEvent cce = null;

            // Process the listeners last to first, notifying
            // those that are interested in this event
            for (int i = listeners.length - 2; i >= 0; i -= 2) {
                if (listeners[i] == ConstraintChangeListener.class) {
                    // Lazily create the event:
                    if (cce == null) {
                        cce =
                            new ConstraintChangeEvent(
                              this,
                              nIdx,
                              new CR(mc, nIdx),
                              null);
                    }

                    (
             (ConstraintChangeListener) listeners[i
                              + 1]).constraintRemoved(
                                          cce);
                }
            }
        }

        protected void fireConstraintAdded() {
            // Guaranteed to return a non-null array
            Object[] listeners = m_ellListeners.getListenerList();

            ConstraintChangeEvent cce = null;

            // Process the listeners last to first, notifying
            // those that are interested in this event
            for (int i = listeners.length - 2; i >= 0; i -= 2) {
                if (listeners[i] == ConstraintChangeListener.class) {
                    // Lazily create the event:
                    if (cce == null) {
                        int nIdx = m_alConstraints.size() - 1;
                        cce =
                            new ConstraintChangeEvent(
                              this,
                              nIdx,
                              null,
                              representationFor(nIdx));
                    }

                    (
             (ConstraintChangeListener) listeners[i
                              + 1]).constraintAdded(
                                        cce);
                }
            }
        }

        protected void fireConstraintDataChanged(
                         int nIdx,
                         MConstraint mcOld,
                         MConstraint mcNew) {
            // Guaranteed to return a non-null array
            Object[] listeners = m_ellListeners.getListenerList();

            ConstraintChangeEvent cce = null;

            // Process the listeners last to first, notifying
            // those that are interested in this event
            for (int i = listeners.length - 2; i >= 0; i -= 2) {
                if (listeners[i] == ConstraintChangeListener.class) {
                    // Lazily create the event:
                    if (cce == null) {
                        cce =
                            new ConstraintChangeEvent(
                              this,
                              nIdx,
                              new CR(mcOld, nIdx),
                              new CR(mcNew, nIdx));
                    }

                    (
             (ConstraintChangeListener) listeners[i
                              + 1]).constraintDataChanged(
                                              cce);
                }
            }
        }

        protected void fireConstraintNameChanged(
                         int nIdx,
                         MConstraint mcOld,
                         MConstraint mcNew) {
            // Guaranteed to return a non-null array
            Object[] listeners = m_ellListeners.getListenerList();

            ConstraintChangeEvent cce = null;

            // Process the listeners last to first, notifying
            // those that are interested in this event
            for (int i = listeners.length - 2; i >= 0; i -= 2) {
                if (listeners[i] == ConstraintChangeListener.class) {
                    // Lazily create the event:
                    if (cce == null) {
                        cce =
                            new ConstraintChangeEvent(
                              this,
                              nIdx,
                              new CR(mcOld, nIdx),
                              new CR(mcNew, nIdx));
                    }

                    (
             (ConstraintChangeListener) listeners[i
                              + 1]).constraintNameChanged(
                                              cce);
                }
            }
        }
    }
    /* (non-Javadoc)
     * @see org.argouml.ui.targetmanager.TargetListener#targetAdded(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetAdded(TargetEvent e) {

    }

    /* (non-Javadoc)
     * @see org.argouml.ui.targetmanager.TargetListener#targetRemoved(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetRemoved(TargetEvent e) {
        setTarget(e.getNewTargets()[0]);

    }

    /* (non-Javadoc)
     * @see org.argouml.ui.targetmanager.TargetListener#targetSet(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetSet(TargetEvent e) {
        setTarget(e.getNewTargets()[0]);

    }

}