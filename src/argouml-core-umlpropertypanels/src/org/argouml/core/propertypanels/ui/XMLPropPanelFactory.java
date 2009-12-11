// $Id$
// Copyright (c) 2008 The Regents of the University of California. All
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

package org.argouml.core.propertypanels.ui;

import java.io.InputStream;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.argouml.core.propertypanels.meta.PanelMeta;
import org.argouml.core.propertypanels.meta.PanelMetaCache;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.PropPanelFactory;

/**
 * 
 * @author penyaskito
 */
public class XMLPropPanelFactory implements PropPanelFactory {

    private static final Logger LOG =
        Logger.getLogger(XMLPropPanelFactory.class);
    
    private final PanelMetaCache cache =
        new PanelMetaCache();
    
    private static XMLPropPanelFactory instance;
    
    public static synchronized XMLPropPanelFactory getInstance() 
        throws Exception {
        if (instance == null) {
            instance = new XMLPropPanelFactory();
        }
        return instance;
    }
    
    private XMLPropPanelFactory() throws Exception {
    }
    
    /**
     * Create the XML driven property panel for the given target
     */
    public JPanel createPropPanel(Object target) {
        if (Model.getFacade().isAModelElement(target)) {
            JPanel panel =
                new XmlPropertyPanel();
            build(panel, target);
            return panel;
        } else {
            return null;
        }
    }
    
    private void build(JPanel panel, Object target) {
        // if we have anything or multiple elements selected,
        // we don't do anything
        // TODO: We need to support multiple selection.
        // See issue 2552: http://argouml.tigris.org/issues/show_bug.cgi?id=2552        
        panel.removeAll();
        if (target == null){
            return;
        }
        
        try {
            // TODO: This references the concrete factory
            // We need a factories factory
            SwingUIFactory builder = new SwingUIFactory();
            builder.createGUI(target, panel);
        } catch (Exception e) {
            // TODO: Auto-generated catch block
            LOG.error("Exception", e);
        }        
    }
    
    public PanelMeta getPropertyPanelsData (String forType) {
        return cache.get(forType);
    }
    
    /**
     * @return the title of the panel, according to the target 
     */
    private String getPanelTitle(Object target) {
        String title = null;
        // if is a pseudostate, we have to look for the pseudostate kind.
        if (Model.getFacade().isAPseudostate(target)) {
            Object kind = Model.getFacade().getKind(target);
            if (Model.getFacade().equalsPseudostateKind(kind,
                    Model.getPseudostateKind().getFork())) {
                title = Translator.localize("label.pseudostate.fork");
            }
            if (Model.getFacade().equalsPseudostateKind(kind,
                    Model.getPseudostateKind().getJoin())) {
                title = Translator.localize("label.pseudostate.join");
            }
            if (Model.getFacade().equalsPseudostateKind(kind,
                    Model.getPseudostateKind().getChoice())) {
                title = Translator.localize("label.pseudostate.choice");
            }
            if (Model.getFacade().equalsPseudostateKind(kind,
                    Model.getPseudostateKind().getDeepHistory())) {
                title = Translator.localize("label.pseudostate.deephistory");
            }
            if (Model.getFacade().equalsPseudostateKind(kind,
                    Model.getPseudostateKind().getShallowHistory())) {
                title = Translator.localize("label.pseudostate.shallowhistory");
            }
            if (Model.getFacade().equalsPseudostateKind(kind,
                    Model.getPseudostateKind().getInitial())) {
                title = Translator.localize("label.pseudostate.initial");
            }
            if (Model.getFacade().equalsPseudostateKind(kind,
                    Model.getPseudostateKind().getJunction())) {
                title = Translator.localize("label.pseudostate.junction");
            }
        }
        // there are other cases that need special treatment, 
        // like concurrent regions
        if (Model.getFacade().isACompositeState(target)) {
            if (Model.getFacade().isAConcurrentRegion(target)) {
                title = Translator.localize("label.concurrent.region");
            } else if (Model.getFacade().isConcurrent(target)) {
                title = Translator.localize("label.concurrent.composite.state");
            } else if (!Model.getFacade().isASubmachineState(target)) {
                // PropPanelSubmachine is a subclass that handles its own title
                title = Translator.localize("label.composite-state");
            }
        }
        else {
            title = Model.getMetaTypes().getName(target); 
        }            
        return title; 
    }
}
