// $Id$
// Copyright (c) 1996-2007 The Regents of the University of California. All
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
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.event.EventListenerList;

import org.apache.log4j.Logger;
import org.argouml.model.Model;
import org.argouml.ocl.ArgoFacade;
import org.argouml.ocl.OCLUtil;
import org.argouml.swingext.ArgoToolbarManager;
import org.argouml.ui.AbstractArgoJPanel;
import org.argouml.ui.targetmanager.TargetEvent;
import org.tigris.gef.presentation.Fig;
import org.tigris.toolbar.ToolBarManager;

import tudresden.ocl.OclException;
import tudresden.ocl.OclTree;
import tudresden.ocl.check.OclTypeException;
import tudresden.ocl.gui.ConstraintRepresentation;
import tudresden.ocl.gui.EditingUtilities;
import tudresden.ocl.gui.OCLEditor;
import tudresden.ocl.gui.OCLEditorModel;
import tudresden.ocl.gui.events.ConstraintChangeEvent;
import tudresden.ocl.gui.events.ConstraintChangeListener;
import tudresden.ocl.parser.OclParserException;
import tudresden.ocl.parser.analysis.DepthFirstAdapter;
import tudresden.ocl.parser.node.AConstraintBody;
import tudresden.ocl.parser.node.TName;

/**
  * Tab for OCL constraint editing.
  *
  * @author v1.0: Falk Finger
  * @author v2.0: Steffen Zschaler
  */
public class TabConstraints extends AbstractArgoJPanel
    implements TabModelTarget, ComponentListener {

    private static final Logger LOG = Logger.getLogger(TabConstraints.class);

    /**
     * The actual editor pane.
     */
    private OCLEditor mOcleEditor;

    /**
     * The current target element.
     */
    private Object mMmeiTarget;

    /**
     * The constructor.
     */
    public TabConstraints() {
        super("tab.constraints");

        setLayout(new BorderLayout(0, 0));

        mOcleEditor = new OCLEditor();
        mOcleEditor.setOptionMask(OCLEditor.OPTIONMASK_TYPECHECK
                   /*|  //removed to workaround problems with autosplit
                     OCLEditor.OPTIONMASK_AUTOSPLIT*/);
        mOcleEditor.setDoAutoSplit(false);
        setToolbarRollover(true);
        setToolbarFloatable(false);
        ArgoToolbarManager.getInstance().registerToolbar(this.getClass(),
                getOclToolbar(), 7);

        add(mOcleEditor);
        
        addComponentListener(this);
    }

    /**
     * Set the toolbar rollover style.
     *
     * @param enable If true then button borders do not become visible
     * until mouse rolls over button.
     */
    private void setToolbarRollover(boolean enable) {
        if (!ToolBarManager.alwaysUseStandardRollover()) {
            getOclToolbar().putClientProperty(
                    "JToolBar.isRollover", Boolean.TRUE);
        }
    }

    /**
     * Set the toolbar floating style.
     *
     * @param enable If true then the toolbar can be floated and docked
     */
    private void setToolbarFloatable(boolean enable) {
        getOclToolbar().setFloatable(false);
    }

    /**
     * Get a reference to the toolbar object contained in the
     * OCLEditor component.  This is currently a nasty hack. We really
     * require an interface method on OCLEditor (Bob Tarling 8 Feb
     * 2003).
     *
     * @return The toolbar
     */
    private JToolBar getOclToolbar() {
        return (JToolBar) mOcleEditor.getComponent(0);
    }

    //TabModelTarget interface methods

    /**
     * Should this tab be activated for the current target element?<p>
     *
     * Argo only supports constraints for Classes and Features
     * (eg. Attributes and Operations) currently.
     *
     * {@inheritDoc}
     */
    public boolean shouldBeEnabled(Object target) {
        target = (target instanceof Fig) ? ((Fig) target).getOwner() : target;
        return (Model.getFacade().isAClass(target)
                || Model.getFacade().isAFeature(target));
    }

    /*
     * @see org.argouml.ui.TabTarget#getTarget()
     */
    public Object getTarget() {
        return mMmeiTarget;
    }

    /**
     * Refresh the tab because the target has changed.
     */
    public void refresh() {
        setTarget(mMmeiTarget);
    }

    /**
     * Set the target element to be displayed in this tab. Only model elements
     * will be accepted by the constraint tab.
     *
     * {@inheritDoc}
     */
    public void setTarget(Object oTarget) {
        oTarget =
	    (oTarget instanceof Fig) ? ((Fig) oTarget).getOwner() : oTarget;
        if (!(Model.getFacade().isAModelElement(oTarget))) {
            mMmeiTarget = null;
            return;
        }

        mMmeiTarget = oTarget;

        if (isVisible()) {
            setTargetInternal(mMmeiTarget);
        }
    }

    private void setTargetInternal(Object oTarget) {
        // Set editor's model
        mOcleEditor.setModel(new ConstraintModel(oTarget));
    }

    /**
     * Adapter to provide information and a manipulation interface for a
     * target element's set of constraints to the constraint editor.
     */
    private static class ConstraintModel implements OCLEditorModel {

        /**
         * The target element being edited.
         */
        private Object theMMmeiTarget;

        /**
         * A list of all the constraints in m_nmeiTarget. This is necessary to
         * induce a consistent order on the constraints.
         */
        private ArrayList theMAlConstraints;

        /**
         * List of listeners.
         */
        private EventListenerList theMEllListeners = new EventListenerList();

        /**
         * Construct a new ConstraintModel.
         */
        public ConstraintModel(Object mmeiTarget) {
            super();

            theMMmeiTarget = mmeiTarget;

            theMAlConstraints =
		new ArrayList(Model.getFacade().getConstraints(theMMmeiTarget));
        }

        /**
         * Return the number of constraints in this model.
         */
        public int getConstraintCount() {
            return theMAlConstraints.size();
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
            if ((nIdx < 0) || (nIdx > theMAlConstraints.size())) {
                return;
            }

            Object mc = theMAlConstraints.remove(nIdx);

            if (mc != null) {
                Model.getCoreHelper().removeConstraint(theMMmeiTarget, mc);
            }

            fireConstraintRemoved(mc, nIdx);
        }

        /**
         * Add a fresh constraint to the model.<p>
         *
         * There are 2 restrictions on what can be parsed, given
         * the current OCL grammar:
         * <ol>
         *   <li>Class names must have a capital first letter.
         *   <li>Feature name must have a lower case first letter.
         * </ol>
         */
        public void addConstraint() {

            // check ocl parsing constraints
            Object mmeContext = OCLUtil
                    .getInnerMostEnclosingNamespace(theMMmeiTarget);
            String contextName = Model.getFacade().getName(mmeContext);
            String targetName = Model.getFacade().getName(theMMmeiTarget);
            if ((contextName == null
		 || contextName.equals (""))
		||  // this is to fix issue #2056
                (targetName == null
		 || targetName.equals (""))
		||   // this is to fix issue #2056
                !Character.isUpperCase(contextName.charAt(0))
		|| (Model.getFacade().isAClass (theMMmeiTarget)
		    && !Character.isUpperCase(targetName.charAt(0)))
		|| (Model.getFacade().isAFeature(theMMmeiTarget)
		    && !Character.isLowerCase(targetName.charAt(0)))) {
                // TODO: I18n
                JOptionPane.showMessageDialog(
		    null,
                    "The OCL Toolkit requires that:\n\n"
		    + "Class names have a capital first letter and\n"
		    + "Attribute or Operation names have "
		    + "a lower case first letter.",
                    "Require Correct name convention:",
                    JOptionPane.ERROR_MESSAGE);
                // do not create a constraint:
                return;
            }

            // null elements represent new constraints, which will be
            // added to the target the first time any actual editing
            // takes place.  This is done to ensure syntactical
            // correctness of constraints stored with the target.
            theMAlConstraints.add(null);

            fireConstraintAdded();
        }

        // TODO: - please add some javadoc - ugly classname also
        private class CR implements ConstraintRepresentation {
            /**
             * The constraint being represented.
             */
            private Object theMMcConstraint;

            /**
             * The constraint's index in the list of
             * constraints. Necessary only for new constraints, where
             * m_mcConstraint is still <tt>null</tt>.
             */
            private int theMNIdx = -1;

            public CR(Object mcConstraint, int nIdx) {
                super();

                theMMcConstraint = mcConstraint;
                theMNIdx = nIdx;
            }

            public CR(int nIdx) {
                this(null, nIdx);
            }

            /**
             * Get the name of the constraint.
             */
            public String getName() {
                if (theMMcConstraint == null) {
                    return "newConstraint";
                }
                return Model.getFacade().getName(theMMcConstraint);
            }

            /**
             * Get the constraint's body.
             */
            public String getData() {
                if (theMMcConstraint == null) {
                    return OCLUtil.getContextString(theMMmeiTarget);
                }
                return (String) Model.getFacade().getBody(
                        Model.getFacade().getBody(theMMcConstraint));
            }

            /**
             * Set the constraint's body text. For the exceptions the
             * detailed message must be human readable.
             *
             * @param sData the new body of the constraint
             *
             * @exception IllegalStateException if the constraint is
             *            not in a state to accept body changes.
             * @exception OclParserException if the specified constraint is not
             *     syntactically correct.
             * @exception OclTypeException if the specified constraint
             *            does not adhere by OCL type rules.
             */
            public void setData(String sData, EditingUtilities euHelper)
		throws OclParserException, OclTypeException {
                // Parse and check specified constraint.
                OclTree tree = null;

                try {
                    Object mmeContext = OCLUtil
                            .getInnerMostEnclosingNamespace(theMMmeiTarget);

                    try {
                        tree =
			    euHelper.parseAndCheckConstraint(
				sData,
				new ArgoFacade(mmeContext));
                    } catch (IOException ioe) {
                        // Ignored: Highly unlikely, and what would we
                        // do anyway?  log it
                        LOG.error("problem parsing And Checking Constraints",
				   ioe);
                        return;
                    }

                    // Split constraint body, if user wants us to
                    if (euHelper.getDoAutoSplit()) {
                        List lConstraints = euHelper.splitConstraint(tree);

                        if (lConstraints.size() > 0) {
                            removeConstraintAt(theMNIdx);

                            for (Iterator i = lConstraints.iterator();
				 i.hasNext();) {
                                OclTree ocltCurrent = (OclTree) i.next();

                                Object mc =
                                    Model.getCoreFactory()
				        .createConstraint();
                                Model.getCoreHelper().setName(mc, ocltCurrent
                                    .getConstraintName());
                                Model.getCoreHelper().setBody(mc,
                                        Model.getDataTypesFactory()
                                        	.createBooleanExpression(
                                        	        "OCL",
                                        	        ocltCurrent
                                        	        .getExpression()));
                                Model.getCoreHelper().addConstraint(
                                        theMMmeiTarget,
                                        mc);

                                // the constraint _must_ be owned by a namespace
                                if (Model.getFacade().getNamespace(
                                        theMMmeiTarget)
                                        != null) {
                                    Model.getCoreHelper().addOwnedElement(
                                            Model.getFacade().getNamespace(
                                                    theMMmeiTarget),
                                            mc);
                                } else if (Model.getFacade().getNamespace(
                                        mmeContext) != null) {
                                    Model.getCoreHelper().addOwnedElement(
                                            Model.getFacade().getNamespace(
                                                    mmeContext),
                                            theMMcConstraint);
                                }

                                theMAlConstraints.add(mc);
                                fireConstraintAdded();
                            }

                            return;
                        }
                    }

                    // Store constraint body
                    Object mcOld = null;

                    if (theMMcConstraint == null) {
                        // New constraint, first time setData is called
                        theMMcConstraint =
                            Model.getCoreFactory().createConstraint();

                        Model.getCoreHelper().setName(
                                theMMcConstraint,
                                "newConstraint");
                        Model.getCoreHelper().setBody(
                                theMMcConstraint,
                                Model.getDataTypesFactory()
                                	.createBooleanExpression("OCL", sData));

                        Model.getCoreHelper().addConstraint(theMMmeiTarget,
                                theMMcConstraint);

                        // the constraint _must_ be owned by a namespace
                        Object targetNamespace =
                            Model.getFacade().getNamespace(theMMmeiTarget);
                        Object contextNamespace =
                            Model.getFacade().getNamespace(mmeContext);
                        if (targetNamespace != null) {
                            Model.getCoreHelper().addOwnedElement(
                                    targetNamespace,
                                    theMMcConstraint);
                        } else if (contextNamespace != null) {
                            Model.getCoreHelper().addOwnedElement(
                                    contextNamespace,
                                    theMMcConstraint);
                        }

                        theMAlConstraints.set(theMNIdx, theMMcConstraint);
                    } else {
                        mcOld = Model.getCoreFactory().createConstraint();
                        Model.getCoreHelper().setName(
                                mcOld,
                                Model.getFacade().getName(theMMcConstraint));
                        Model.getCoreHelper().setBody(
                                mcOld,
                                Model.getDataTypesFactory()
                                	.createBooleanExpression("OCL",
                                	        (String) Model.getFacade()
                                	        	.getBody(
                                                Model.getFacade().getBody(
                                                        theMMcConstraint))));
                        Model.getCoreHelper().setBody(theMMcConstraint,
                                Model.getDataTypesFactory()
                                	.createBooleanExpression("OCL", sData));
                    }

                    fireConstraintDataChanged(theMNIdx, mcOld,
                            theMMcConstraint);

                } catch (OclTypeException pe) {
                    LOG.warn("There was some sort of OCL Type problem", pe);
                    throw pe;
                } catch (OclParserException pe1) {
                    LOG.warn("Could not parse the constraint", pe1);
                    throw pe1;
                } catch (OclException oclExc) {
                    // a runtime exception that occurs when some
                    // internal test fails
                    LOG.warn("There was some unidentified problem");
                    throw oclExc;
                }
            }

            /**
             * Set the constraint's name.
             */
            public void setName(
                final String sName,
                final EditingUtilities euHelper) {
                if (theMMcConstraint != null) {
                    // Check name for consistency with spec
                    if (!euHelper.isValidConstraintName(sName)) {
                        throw new IllegalArgumentException(
                                "Please specify a valid name.");
                    }

                    // Set name
                    Object mcOld =
                        Model.getCoreFactory().createConstraint();
                    Model.getCoreHelper().setName(mcOld,
                            Model.getFacade().getName(theMMcConstraint));
                    Object constraintBody =
                        Model.getFacade().getBody(theMMcConstraint);
                    Model.getCoreHelper().setBody(mcOld,
                            Model.getDataTypesFactory()
                                .createBooleanExpression(
                                        "OCL",
                                        (String) Model.getFacade().getBody(
                                                constraintBody)));

                    Model.getCoreHelper().setName(theMMcConstraint, sName);

                    fireConstraintNameChanged(theMNIdx, mcOld,
                            theMMcConstraint);

                    // Also set name in constraint body -- Added 03/14/2001
                    try {
                        OclTree tree = null;
                        Object mmeContext = OCLUtil
                                .getInnerMostEnclosingNamespace(theMMmeiTarget);

                        constraintBody =
                            Model.getFacade().getBody(theMMcConstraint);
                        tree =
                            euHelper.parseAndCheckConstraint(
                                (String) Model.getFacade().getBody(
                                        constraintBody),
                                new ArgoFacade(mmeContext));

                        if (tree != null) {
                            tree.apply(new DepthFirstAdapter() {
                                private int nameID = 0;
                                public void caseAConstraintBody(
                                        AConstraintBody node) {
                                    // replace name
                                    if (nameID == 0) {
                                        node.setName(new TName(sName));
                                    } else {
                                        node.setName(new TName(
                                                sName + "_" + nameID));
                                    }
                                    nameID++;
				}
                            });

                            setData(tree.getExpression(), euHelper);
                        }
                    } catch (Throwable t) {
                        // OK, so that didn't work out... Just ignore
                        // any problems and don't set the name in the
                        // constraint body better had log it.
                        LOG.error("some unidentified problem", t);
                    }
                } else {
                    throw new IllegalStateException(
                        "Please define and submit a constraint body first.");
                }
            }
        }

        /**
         * Create a representation adapter for the given constraint.
         */
        private CR representationFor(int nIdx) {
            if ((nIdx < 0) || (nIdx >= theMAlConstraints.size())) {
                return null;
            }

            Object mc = theMAlConstraints.get(nIdx);

            if (mc != null) {
                return new CR(mc, nIdx);
            }
            return new CR(nIdx);
        }

        /**
         * Add a listener to be informed of changes in the model.
         *
         * @param ccl the new listener
         */
        public void addConstraintChangeListener(ConstraintChangeListener ccl) {
            theMEllListeners.add(ConstraintChangeListener.class, ccl);
        }

        /**
         * Remove a listener to be informed of changes in the model.
         *
         * @param ccl the listener to be removed
         */
        public void removeConstraintChangeListener(
                ConstraintChangeListener ccl) {
            theMEllListeners.remove(ConstraintChangeListener.class, ccl);
        }

        protected void fireConstraintRemoved(
                Object mc, int nIdx) {
            // Guaranteed to return a non-null array
            Object[] listeners = theMEllListeners.getListenerList();

            ConstraintChangeEvent cce = null;

            // Process the listeners last to first, notifying
            // those that are interested in this event
            for (int i = listeners.length - 2; i >= 0; i -= 2) {
                if (listeners[i] == ConstraintChangeListener.class) {
                    // Lazily create the event:
                    if (cce == null) {
                        cce = new ConstraintChangeEvent(
                            this,
                            nIdx,
                            new CR(mc, nIdx),
                            null);
                    }
                    ((ConstraintChangeListener) listeners[i + 1])
                        .constraintRemoved(cce);
                }
            }
        }

        protected void fireConstraintAdded() {
            // Guaranteed to return a non-null array
            Object[] listeners = theMEllListeners.getListenerList();

            ConstraintChangeEvent cce = null;

            // Process the listeners last to first, notifying
            // those that are interested in this event
            for (int i = listeners.length - 2; i >= 0; i -= 2) {
                if (listeners[i] == ConstraintChangeListener.class) {
                    // Lazily create the event:
                    if (cce == null) {
                        int nIdx = theMAlConstraints.size() - 1;
                        cce =
                            new ConstraintChangeEvent(
                                    this,
                                    nIdx,
                                    null,
                                    representationFor(nIdx));
                    }
                    ((ConstraintChangeListener) listeners[i + 1])
                        .constraintAdded(cce);
                }
            }
        }

        protected void fireConstraintDataChanged(
                         int nIdx,
                         Object mcOld,
                         Object mcNew) {
            // Guaranteed to return a non-null array
            Object[] listeners = theMEllListeners.getListenerList();

            ConstraintChangeEvent cce = null;

            // Process the listeners last to first, notifying
            // those that are interested in this event
            for (int i = listeners.length - 2; i >= 0; i -= 2) {
                if (listeners[i] == ConstraintChangeListener.class) {
                    // Lazily create the event:
                    if (cce == null) {
                        cce = new ConstraintChangeEvent(
                            this,
                            nIdx,
                            new CR(mcOld, nIdx),
                            new CR(mcNew, nIdx));
                    }

                    ((ConstraintChangeListener) listeners[i + 1])
                        .constraintDataChanged(cce);
                }
            }
        }

        protected void fireConstraintNameChanged(
                         int nIdx,
                         Object mcOld,
                         Object mcNew) {
            // Guaranteed to return a non-null array
            Object[] listeners = theMEllListeners.getListenerList();

            ConstraintChangeEvent cce = null;

            // Process the listeners last to first, notifying
            // those that are interested in this event
            for (int i = listeners.length - 2; i >= 0; i -= 2) {
                if (listeners[i] == ConstraintChangeListener.class) {
                    // Lazily create the event:
                    if (cce == null) {
                        cce = new ConstraintChangeEvent(
                            this,
                            nIdx,
                            new CR(mcOld, nIdx),
                            new CR(mcNew, nIdx));
                    }

                    ((ConstraintChangeListener) listeners[i + 1])
                        .constraintNameChanged(cce);
                }
            }
        }
    }

    /*
     * @see org.argouml.ui.targetmanager.TargetListener#targetAdded(
     *         org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetAdded(TargetEvent e) {
        // TODO: Why is this ignored? - tfm - 20070110
    }

    /*
     * @see org.argouml.ui.targetmanager.TargetListener#targetRemoved(
     *         org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetRemoved(TargetEvent e) {
        setTarget(e.getNewTarget());
    }

    /*
     * @see org.argouml.ui.targetmanager.TargetListener#targetSet(
     *         org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetSet(TargetEvent e) {
        setTarget(e.getNewTarget());
    }
    
    /*
     * @see java.awt.event.ComponentListener#componentShown(java.awt.event.ComponentEvent)
     */
    public void componentShown(ComponentEvent e) {
        // Update our model with our saved target
        setTargetInternal(mMmeiTarget);
    }
    
    /*
     * @see java.awt.event.ComponentListener#componentHidden(java.awt.event.ComponentEvent)
     */
    public void componentHidden(ComponentEvent e) {
        // We have no model event listeners, so no need to do anything
    }

    public void componentMoved(ComponentEvent e) {
        // ignored
    }

    public void componentResized(ComponentEvent e) {
        // ignored
    }


}
