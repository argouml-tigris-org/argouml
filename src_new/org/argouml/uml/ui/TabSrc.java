// Copyright (c) 1996-2001 The Regents of the University of California. All
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

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import org.apache.log4j.Category;
import org.argouml.application.api.Notation;
import org.argouml.application.api.NotationContext;
import org.argouml.application.api.NotationName;
import org.argouml.application.events.ArgoEventPump;
import org.argouml.application.events.ArgoEventTypes;
import org.argouml.application.events.ArgoNotationEvent;
import org.argouml.application.events.ArgoNotationEventListener;
import org.argouml.language.ui.NotationComboBox;
import org.argouml.model.ModelFacade;
import org.argouml.ui.TabText;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigEdge;
import org.tigris.gef.presentation.FigNode;

/**
 * Details panel tabbed panel for displaying a source code representation of
 * a UML model element in a particular Notation.
 */
public class TabSrc
    extends TabText
    implements ArgoNotationEventListener, NotationContext, ItemListener {
    ////////////////////////////////////////////////////////////////
    // constructor
    private final Category cat = Category.getInstance(TabSrc.class);

    private NotationName _notationName = null;

    /** Create a tab that contains a toolbar.
     *  Then add a notation selector onto it.
     */
    public TabSrc() {
        // TODO:  Temporarily remove toolbar until src selection
        // is working better.
        //
        super("tab.source", true);
        // super("Source", false);
        _notationName = null;
        _toolbar.add(NotationComboBox.getInstance());
        NotationComboBox.getInstance().addItemListener(this);
        _toolbar.addSeparator();
        ArgoEventPump.addListener(ArgoEventTypes.ANY_NOTATION_EVENT, this);
    }

    public void finalize() {
        ArgoEventPump.removeListener(ArgoEventTypes.ANY_NOTATION_EVENT, this);
        NotationComboBox.getInstance().removeItemListener(this);
    }
    ////////////////////////////////////////////////////////////////
    // accessors

    protected String genText(Object modelObject) {
        modelObject = (modelObject instanceof Fig) ? ((Fig)modelObject).getOwner() : modelObject;       
        if (!ModelFacade.isAElement(modelObject))
            return null;
        
        cat.debug("TabSrc getting src for " + modelObject);
        //return Notation.generate(this, modelObject, true);
        NotationName nn =
            (NotationName) (NotationComboBox.getInstance().getSelectedItem());
        String fileName = getSourceFileFor(modelObject, nn);
        if (fileName != null) {
            // get file content, scroll to the line where modelObject begins, and set background color to white
        }
        return Notation.generate(nn, modelObject, true);
    }

    protected void parseText(String s) {
        cat.debug("TabSrc   setting src for " + _target);
        Object modelObject = _target;
        if (_target instanceof FigNode)
            modelObject = ((FigNode) _target).getOwner();
        if (_target instanceof FigEdge)
            modelObject = ((FigEdge) _target).getOwner();
        if (modelObject == null)
            return;
        cat.debug("TabSrc   setting src for " + modelObject);
        //Parser.ParseAndUpdate(modelObject, s);
    }

    /**
     * Sets the target of this tab. 
     */
    public void setTarget(Object t) {

        t = (t instanceof Fig) ? ((Fig)t).getOwner() : t;
        _target = t;
        _notationName = null;
        _shouldBeEnabled = false;
        if (ModelFacade.isAModelElement(t))
            _shouldBeEnabled = true;
        // If the target is a notation context, use its notation.
        if (t instanceof NotationContext) {
            _notationName = ((NotationContext) t).getContextNotation();
            cat.debug(
                "Target is notation context with notation name: "
                    + _notationName);
        } else {
            // TODO:  Get it from the combo box
            cat.debug(
                "ComboBox.getSelectedItem() '"
                    + NotationComboBox.getInstance().getSelectedItem()
                    + "'");
            _notationName =
                (NotationName) (NotationComboBox
                    .getInstance()
                    .getSelectedItem());
            //_notationName = Notation.getDefaultNotation();
        }
        cat.debug(
            "Going to set target(" + t + "), notation name:" + _notationName);
        super.setTarget(t);
    }

    /**
     * Determines if the current tab should be enabled with the given target. 
     * @return true if the given target is either a modelelement or is a fig with
     * as owner a modelelement.
     */
    public boolean shouldBeEnabled(Object target) {
        target = (target instanceof Fig) ? ((Fig)target).getOwner() : target;

        _shouldBeEnabled = false;
        if (ModelFacade.isAModelElement(target)) {
            _shouldBeEnabled = true;
        }

        return _shouldBeEnabled;
    }

    /**
     * Invoked when any aspect of the notation has been changed.
     */
    public void notationChanged(ArgoNotationEvent e) {
        refresh();
    }

    /** Ignored. */
    public void notationAdded(ArgoNotationEvent e) {
    }

    /** Ignored. */
    public void notationRemoved(ArgoNotationEvent e) {
    }

    /** Ignored. */
    public void notationProviderAdded(ArgoNotationEvent e) {
    }

    /** Ignored. */
    public void notationProviderRemoved(ArgoNotationEvent e) {
    }

    public void itemStateChanged(ItemEvent event) {
        if (event.getStateChange() == ItemEvent.SELECTED) {
            refresh();
        }
    }

    public void refresh() {
        setTarget(_target);
    }

    public NotationName getContextNotation() {
        return _notationName;
    }

    private String getSourceFileFor(Object modelObject, NotationName nn) {
        //Project p = ProjectManager.getManager().getCurrentProject();
        //_outputDirectoryComboBox.getModel().setSelectedItem(p.getGenerationPrefs().getOutputDir());
        return null;
    }

} /* end class TabSrc */
