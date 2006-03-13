// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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

package org.argouml.ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ListIterator;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import org.apache.log4j.Logger;
import org.argouml.application.api.Argo;
import org.argouml.application.api.PluggableSettingsTab;
import org.argouml.application.api.SettingsTabPanel;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;

/**
 * Action for starting the Argo settings window.
 *
 * @author Thomas N
 * @author Thierry Lach
 * @since 0.9.4
 */
public class ActionSettings extends AbstractAction {

    ////////////////////////////////////////////////////////////////
    // static variables

    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(Translator.class);

    ////////////////////////////////////////////////////////////////
    // constructors
    private JButton applyButton;

    private JTabbedPane tabs;

    private ArgoDialog dialog;

    /**
     * Constructor.
     */
    public ActionSettings() {
        super(localize("action.settings"),
                ResourceLoaderWrapper.lookupIcon("action.settings"));
    }

    /**
     * Helper for localization.
     *
     * @param key The key to localize.
     * @return The localized String.
     */
    private static String localize(String key) {
        return Translator.localize(key);
    }

    ////////////////////////////////////////////////////////////////
    // main methods

    /**
     * @see java.awt.event.ActionListener#actionPerformed(
     *         java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent event) {

        ProjectBrowser pb = ProjectBrowser.getInstance();
        if (dialog == null) {
            try {
                dialog =
                    new ArgoDialog(pb, localize("dialog.settings"),
                        ArgoDialog.OK_CANCEL_OPTION, true) {

                    /**
                     * @see java.awt.event.ActionListener#actionPerformed(
                     *         java.awt.event.ActionEvent)
                     */
                    public void actionPerformed(ActionEvent ev) {
                        super.actionPerformed(ev);
                        if (ev.getSource() == getOkButton()) {
                            handleSave();
                        } else if (ev.getSource() == getCancelButton()) {
                            handleCancel();
                        }
                    }
                };

                tabs = new JTabbedPane();

                applyButton = new JButton(localize("button.apply"));
                String mnemonic = localize("button.apply.mnemonic");
                if (mnemonic != null && mnemonic.length() > 0) {
                    applyButton.setMnemonic(mnemonic.charAt(0));
                }
                applyButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        handleSave();
                    }
                });
                dialog.addButton(applyButton);

                List list = Argo.getPlugins(PluggableSettingsTab.class);
                ListIterator iterator = list.listIterator();
                while (iterator.hasNext()) {
                    Object o = iterator.next();
                    SettingsTabPanel stp =
                        ((PluggableSettingsTab) o).getSettingsTabPanel();

                    tabs.addTab(
                            Translator.localize(stp.getTabKey()),
                            stp.getTabPanel());
                }

                // Increase width to accommodate all tabs on one row.
                // (temporary solution until tabs are replaced with tree)
                final int minimumWidth = 480;
                tabs.setPreferredSize(
                        new Dimension(Math.max(tabs.getPreferredSize().width,
                                	       minimumWidth),
                                      tabs.getPreferredSize().height));

                tabs.setTabPlacement(SwingConstants.LEFT);
                dialog.setContent(tabs);
            } catch (Exception exception) {
                LOG.error("got an Exception in ActionSettings", exception);
            }
        }

        handleRefresh();
        dialog.toFront();
        dialog.setVisible(true);
    }

    /**
     * Called when the user has pressed Save. Performs "Save" in all Tabs.
     */
    private void handleSave() {
        for (int i = 0; i < tabs.getComponentCount(); i++) {
            Object o = tabs.getComponent(i);
            if (o instanceof SettingsTabPanel) {
                ((SettingsTabPanel) o).handleSettingsTabSave();
            }
        }
    }

    /**
     * Called when the user has pressed Cancel. Performs "Cancel" in all Tabs.
     */
    private void handleCancel() {
        for (int i = 0; i < tabs.getComponentCount(); i++) {
            Object o = tabs.getComponent(i);
            if (o instanceof SettingsTabPanel) {
                ((SettingsTabPanel) o).handleSettingsTabCancel();
            }
        }
    }

    /**
     * Called when the user has pressed Refresh. Performs "Refresh" in all Tabs.
     */
    private void handleRefresh() {
        for (int i = 0; i < tabs.getComponentCount(); i++) {
            Object o = tabs.getComponent(i);
            if (o instanceof SettingsTabPanel) {
                ((SettingsTabPanel) o).handleSettingsTabRefresh();
            }
        }
    }
}
/* end class ActionSettings */

