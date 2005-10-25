// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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
import java.util.Collection;

import javax.swing.JComboBox;

import org.apache.log4j.Logger;
import org.argouml.language.ui.LanguageComboBox;
import org.argouml.model.Model;
import org.argouml.ui.TabText;
import org.argouml.uml.generator.GeneratorHelper;
import org.argouml.uml.generator.Language;
import org.argouml.uml.generator.SourceUnit;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigEdge;
import org.tigris.gef.presentation.FigNode;

/**
 * Details panel tabbed panel for displaying a source code representation of
 * a UML model element in a particular Notation.
 */
public class TabSrc
    extends TabText
    implements ItemListener {
    
    private static final Logger LOG = Logger.getLogger(TabSrc.class);
    
    private Language langName = null;
    private String fileName = null;
    private SourceUnit[] files = null;
    
    private LanguageComboBox cbLang = new LanguageComboBox();
    private JComboBox cbFiles = new JComboBox();
    
    ////////////////////////////////////////////////////////////////
    // constructor
    
    /** 
     * Create a tab that contains a toolbar.
     * Then add a notation selector onto it.
     */
    public TabSrc() {
        super("tab.source", true);
        langName = null;
        fileName = null;
        getToolbar().add(cbLang);
        getToolbar().addSeparator();
        cbLang.addItemListener(this);
        getToolbar().add(cbFiles);
        getToolbar().addSeparator();
        cbFiles.addItemListener(this);
    }

    /**
     * @see java.lang.Object#finalize()
     */
    protected void finalize() {
        cbLang.removeItemListener(this);
    }
    
    ////////////////////////////////////////////////////////////////
    // accessors

    /**
     * @see org.argouml.ui.TabText#genText(java.lang.Object)
     */
    protected String genText(Object modelObject) {
        if (modelObject != getTarget()) {
            setTarget(modelObject); // shouldn't happen
        }
        if (files != null && files.length > cbFiles.getSelectedIndex())
            return files[cbFiles.getSelectedIndex()].getContent();
        return "N/A"; // never return null here!
    }

    /**
     * @see org.argouml.ui.TabText#parseText(java.lang.String)
     */
    protected void parseText(String s) {
        LOG.debug("TabSrc   setting src for " + getTarget());
        Object modelObject = getTarget();
        if (getTarget() instanceof FigNode)
            modelObject = ((FigNode) getTarget()).getOwner();
        if (getTarget() instanceof FigEdge)
            modelObject = ((FigEdge) getTarget()).getOwner();
        if (modelObject == null)
            return;
        LOG.debug("TabSrc   setting src for " + modelObject);
        /* TODO: Implement this! */
        //Parser.ParseAndUpdate(modelObject, s);
    }

    /**
     * Sets the target of this tab.
     *
     * @see org.argouml.ui.TabTarget#setTarget(java.lang.Object)
     */
    public void setTarget(Object t) {
        Object modelTarget = (t instanceof Fig) ? ((Fig) t).getOwner() : t;
        setShouldBeEnabled(Model.getFacade().isAClassifier(modelTarget));
        cbFiles.removeAllItems();
        files = null;        
        if (shouldBeEnabled()) {
            LOG.debug("TabSrc getting src for " + modelTarget);
            Collection code =
                GeneratorHelper.generate(langName, modelTarget, false);
            if (!code.isEmpty()) {
                files = new SourceUnit[code.size()];
                files = (SourceUnit[]) code.toArray(files);
                for (int i = 0; i < files.length; i++) {
                    String title = files[i].getName();
                    if (files[i].getBasePath().length() > 0) {
                        title += " ( " + files[i].getFullName() + ")";
                    }
                    cbFiles.addItem(title);
                }
            }
        }
        super.setTarget(t);
    }

    /**
     * Determines if the current tab should be enabled with the given target.
     * Returns true if the given target is either
     * a modelelement or is a fig with as owner a modelelement.
     *
     * @see org.argouml.ui.TabTarget#shouldBeEnabled(java.lang.Object)
     */
    public boolean shouldBeEnabled(Object target) {
        target = (target instanceof Fig) ? ((Fig) target).getOwner() : target;

        setShouldBeEnabled(false);
        if (Model.getFacade().isAClassifier(target)) {
            setShouldBeEnabled(true);
        }

        return shouldBeEnabled();
    }

    /**
     * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
     */
    public void itemStateChanged(ItemEvent event) {
        if (event.getSource() == cbLang) {
            if (event.getStateChange() == ItemEvent.SELECTED) {
                langName = (Language) cbLang.getSelectedItem();
                refresh();
            }
        } else if (event.getSource() == cbFiles) {
            if (event.getStateChange() == ItemEvent.SELECTED) {
                fileName = (String) cbFiles.getSelectedItem();
                super.setTarget(getTarget());
            }
            
        }
    }

    /**
     * @see org.argouml.ui.TabTarget#refresh()
     */
    public void refresh() {
        setTarget(getTarget());
    }


} /* end class TabSrc */
