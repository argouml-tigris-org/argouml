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

package org.argouml.ui;
import org.argouml.application.api.*;
import org.argouml.application.events.*;
import org.argouml.kernel.*;
import org.argouml.uml.ui.UMLAction;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import javax.swing.*;
import java.util.*;
import org.tigris.gef.util.*;

/** Action object for handling Argo settings
 *
 *  @author Thomas N
 *  @author Thierry Lach
 *  @since  0.9.4
 */
public class ActionSettings extends UMLAction
implements ArgoModuleEventListener {

    ////////////////////////////////////////////////////////////////
    // static variables

    /** One and only instance.
     */
    private static ActionSettings SINGLETON = new ActionSettings();

    /** Get the instance.
     */
    public static ActionSettings getInstance() {
        return SINGLETON;
    }

    ////////////////////////////////////////////////////////////////
    // constructors
    protected JButton buttonOk = null;
    protected JButton buttonCancel = null;
    protected JButton buttonApply = null;
    protected JTabbedPane tabs = null;
    protected JDialog dlg = null;

    protected ActionSettings() {
        super(Argo.localize(Argo.MENU_BUNDLE,"action.settings"), false);
    }

    /** Helper for localization.
     */
    protected String localize(String key) {
        return Argo.localize("CoreSettings", key);
    }

    ////////////////////////////////////////////////////////////////
    // main methods

    public void actionPerformed(ActionEvent event) {
	Object source = event.getSource();

	if (source.equals(buttonOk) && tabs != null) {
	   for (int i = 0; i < tabs.getComponentCount(); i++) {
	       Object o = tabs.getComponent(i);
	       if (o instanceof SettingsTabPanel) {
		   ((SettingsTabPanel)o).handleSettingsTabSave();
	       }
	   }
	   dlg.setVisible(false);
	} else if (source.equals(buttonApply) && tabs != null) {
	   // Same as ok but don't hide it.
	   for (int i = 0; i < tabs.getComponentCount(); i++) {
	       Object o = tabs.getComponent(i);
	       if (o instanceof SettingsTabPanel) {
		   ((SettingsTabPanel)o).handleSettingsTabSave();
	       }
	   }

	} else if (source.equals(buttonCancel) && tabs != null) {
	   for (int i = 0; i < tabs.getComponentCount(); i++) {
	       Object o = tabs.getComponent(i);
	       if (o instanceof SettingsTabPanel) {
		   ((SettingsTabPanel)o).handleSettingsTabCancel();
	       }
	   }
	   dlg.setVisible(false);
	}
	else if (source instanceof JMenuItem) {
            ProjectBrowser pb = ProjectBrowser.TheInstance;
	    if (dlg == null) {
                try {
	            dlg = new JDialog(pb, localize("caption.settings"), true);

	            dlg.getContentPane().setLayout(new BorderLayout());
                    tabs = new JTabbedPane();
	            dlg.getContentPane().add(tabs, BorderLayout.CENTER);
     
	            JPanel buttons = new JPanel();
	            buttons.setLayout(new FlowLayout());
        
	            buttonOk = new JButton(localize("button.ok"));
	            buttonOk.addActionListener(this);
	            buttons.add (buttonOk);
        
	            buttonCancel = new JButton(localize("button.cancel"));
	            buttonCancel.addActionListener(this);
	            buttons.add (buttonCancel);
        
	            buttonApply = new JButton(localize("button.apply"));
	            buttonApply.addActionListener(this);
	            buttons.add (buttonApply);
        
	            dlg.getContentPane().add(buttons, BorderLayout.SOUTH);
        
		    ArrayList list = Argo.getPlugins(PluggableSettingsTab.class);
		    ListIterator iterator = list.listIterator();
                    while (iterator.hasNext()) {
	                Object o = iterator.next();
			SettingsTabPanel stp = ((PluggableSettingsTab)o).getSettingsTabPanel();

	                tabs.addTab(Argo.localize(stp.getTabResourceBundleKey(),
			                          stp.getTabKey()),
		                    stp.getTabPanel());
                    }
                } catch (Exception exception) {
                    Argo.log.error("got an Exception in ActionSettings");
	            Argo.log.error(exception);
                }
	    }
	    dlg.setSize(500, 300);
	    dlg.setLocation(pb.getLocation().x + 100, pb.getLocation().y + 100);
	    // Refresh all the tab data
	    for (int i = 0; i < tabs.getComponentCount(); i++) {
	        Object o = tabs.getComponent(i);
	        if (o instanceof SettingsTabPanel) {
		    ((SettingsTabPanel)o).handleSettingsTabRefresh();
	        }
	    } 
	    dlg.toFront();
	    dlg.setVisible(true);
	}
    }

    public void moduleLoaded(ArgoModuleEvent event) {
    }

    public void moduleUnloaded(ArgoModuleEvent event) {
    }

    public void moduleEnabled(ArgoModuleEvent event) {
    }

    public void moduleDisabled(ArgoModuleEvent event) {
    }

}
/* end class ActionSettings */

