// $Id:LanguageComboBox.java 11510 2006-11-24 07:37:59Z tfmorris $
// Copyright (c) 2005-2006 The Regents of the University of California. All
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

package org.argouml.language.ui;

import java.awt.Dimension;
import java.util.Iterator;

import javax.swing.JComboBox;

import org.apache.log4j.Logger;
import org.argouml.application.events.ArgoEventPump;
import org.argouml.application.events.ArgoEventTypes;
import org.argouml.application.events.ArgoGeneratorEvent;
import org.argouml.application.events.ArgoGeneratorEventListener;
import org.argouml.uml.generator.GeneratorManager;
import org.argouml.uml.generator.Language;

/**
 * This class provides a self-updating language combo box.
 * @author Daniele Tamino
 */
public class LanguageComboBox
    extends JComboBox
    implements ArgoGeneratorEventListener {

    /** logger */
    private static final Logger LOG = Logger.getLogger(LanguageComboBox.class);

    /**
     * The constructor.
     */
    public LanguageComboBox() {
        super();
        setEditable(false);
        setMaximumRowCount(6);

        Dimension d = getPreferredSize();
        d.width = 200;
        setMaximumSize(d);

        ArgoEventPump.addListener(ArgoEventTypes.ANY_GENERATOR_EVENT, this);
        refresh();
    }

    /*
     * @see java.lang.Object#finalize()
     */
    protected void finalize() {
        ArgoEventPump.removeListener(this);
    }

    /**
     * Refresh the combobox contents.
     */
    public void refresh() {
        removeAllItems();
        Iterator iterator =
            GeneratorManager.getInstance().getLanguages().iterator();
        while (iterator.hasNext()) {
            try {
                Language ll = (Language) iterator.next();
                addItem(ll);
            } catch (Exception e) {
                LOG.error("Unexpected exception", e);
            }
        }
        setVisible(true);
        invalidate();
    }

    /*
     * @see org.argouml.application.events.ArgoGeneratorEventListener#generatorChanged(org.argouml.application.events.ArgoGeneratorEvent)
     */
    public void generatorChanged(ArgoGeneratorEvent e) {
        refresh();
    }

    /*
     * @see org.argouml.application.events.ArgoGeneratorEventListener#generatorAdded(org.argouml.application.events.ArgoGeneratorEvent)
     */
    public void generatorAdded(ArgoGeneratorEvent e) {
        refresh();
    }

    /*
     * @see org.argouml.application.events.ArgoGeneratorEventListener#generatorRemoved(org.argouml.application.events.ArgoGeneratorEvent)
     */
    public void generatorRemoved(ArgoGeneratorEvent e) {
        refresh();
    }
}
