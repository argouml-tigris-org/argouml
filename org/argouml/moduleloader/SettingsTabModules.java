// $Id$
// Copyright (c) 2004-2006 The Regents of the University of California. All
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

package org.argouml.moduleloader;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Map;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import org.argouml.application.helpers.SettingsTabHelper;
import org.argouml.application.modules.ModuleLoader;

/**
 * Tab for the settings dialog that makes it possible to
 * select the modules that are to be enabled.
 *
 * TODO: Header for the table.
 *
 * @author Linus Tolke
 */
public class SettingsTabModules extends SettingsTabHelper {
    /**
     * The table of modules.
     */
    private JTable table;

    /**
     * The panel below the table that shows not yet loaded modules.
     */
    private JPanel notYetLoadedPanel;

    /**
     * The names of the columns in the table.
     */
    private String[] columnNames = {
	"Module", "Enabled",
    };

    /**
     * The objects representing the modules from the new module loader.
     */
    private Object[][] elements;

    /**
     * The constructor.
     */
    public SettingsTabModules() {
        super();

        setLayout(new BorderLayout());

        table = new JTable(new ModuleTableModel());
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
	table.setShowVerticalLines(true);
        add(new JScrollPane(table), BorderLayout.CENTER);

	createNotYetLoaded();
    }

    /**
     * Table model for the table with modules.
     */
    class ModuleTableModel extends AbstractTableModel {
        /**
	 * Constructor.
	 */
	public ModuleTableModel() {
	    Object[] arr = ModuleLoader2.allModules().toArray();

	    elements = new Object[arr.length][2];

	    for (int i = 0; i < elements.length; i++) {
		elements[i][0] = arr[i];
		elements[i][1] =
		    Boolean.valueOf(ModuleLoader2.isSelected((String) arr[i]));
	    }
	}

	/**
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	public int getColumnCount() {
	    return columnNames.length;
	}

	/**
	 * @see javax.swing.table.TableModel#getColumnName(int)
	 */
	public String getColumnName(int col) {
	    return columnNames[col];
	}

	/**
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	public int getRowCount() {
	    return elements.length
		+ ModuleLoader.getInstance().getModules().size();
	}

	/**
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	public Object getValueAt(int row, int col) {
	    if (row < elements.length) {
		return elements[row][col];
	    } else {
		switch (col) {
		case 0:
		    return ModuleLoader.getInstance()
			.getModules().get(row - elements.length);
		case 1:
		    return Boolean.TRUE;

		default:
		    throw new IllegalArgumentException("Too many columns");
		}
	    }
	}

	/**
	 * @see javax.swing.table.TableModel#setValueAt(
	 *         java.lang.Object, int, int)
	 */
	public void setValueAt(Object ob, int row, int col) {
	    elements[row][col] = ob;
	}

	/**
	 * @see javax.swing.table.TableModel#getColumnClass(int)
	 */
	public Class getColumnClass(int col) {
	    switch (col) {
	    case 0:
		return String.class;
	    case 1:
		return Boolean.class;
	    default:
		return null;
	    }
	}

	/**
	 * @see javax.swing.table.TableModel#isCellEditable(int, int)
	 */
	public boolean isCellEditable(int row, int col) {
	    return col >= 1 && row < elements.length;
	}

        /**
         * The UID.
         */
        private static final long serialVersionUID = -5970280716477119863L;
    }

    /**
     * Create the pane with not yet loaded modules.
     */
    private void createNotYetLoaded() {
	if (notYetLoadedPanel != null) {
	    remove(notYetLoadedPanel);
	    notYetLoadedPanel = null;
	}

        Container allNotYetLoaded = Box.createVerticalBox();

	Iterator iter =
	    ModuleLoader2.notYetLoadedModules().entrySet().iterator();
        boolean seen = false;
	while (iter.hasNext()) {
	    Map.Entry entry = (Map.Entry) iter.next();
	    final String name = (String) entry.getKey();
	    final String classname = (String) entry.getValue();

	    JButton button = new JButton(name);
	    button.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent event) {
		    try {
		        getClass().getClassLoader().loadClass(classname);
		        ModuleLoader2.addClass(classname);
		        handleSettingsTabRefresh();
		    } catch (ClassNotFoundException e) {
			JOptionPane.showMessageDialog(
				notYetLoadedPanel,
			        "Cannot find class " + classname
				+ " needed to load module " + name,
				"Cannot find class",
				JOptionPane.ERROR_MESSAGE);
			return;
		    }
		}
	    });

	    allNotYetLoaded.add(button);
            seen = true;
	}

        if (seen) {
            notYetLoadedPanel = new JPanel();
            notYetLoadedPanel.setLayout(new BorderLayout());
            notYetLoadedPanel.add(new JLabel("Attempt to load:"),
                                  BorderLayout.NORTH);
            notYetLoadedPanel.add(allNotYetLoaded, BorderLayout.CENTER);
            add(notYetLoadedPanel, BorderLayout.SOUTH);
        }
        validate();
    }

    /**
     * @see org.argouml.application.api.SettingsTabPanel#handleSettingsTabRefresh()
     */
    public void handleSettingsTabRefresh() {
        table.setModel(new ModuleTableModel());
	createNotYetLoaded();
    }

    /**
     * @see org.argouml.application.api.SettingsTabPanel#handleSettingsTabSave()
     */
    public void handleSettingsTabSave() {
        if (elements != null) {
            for (int i = 0; i < elements.length; i++) {
                ModuleLoader2.setSelected(
                        (String) elements[i][0],
                        ((Boolean) elements[i][1]).booleanValue());
            }
            ModuleLoader2.doLoad(false);
        }
	createNotYetLoaded();
    }

    /**
     * @see org.argouml.application.api.SettingsTabPanel#handleSettingsTabCancel()
     */
    public void handleSettingsTabCancel() {
        // Do nothing!
        // The next time we refresh, we will fetch the values again.
    }



    // TODO: This is rather ironic. We use the old moduleloader mechanism
    //       to get a settings tab that will allow us to turn on an off
    //       the new moduleloader.

    /**
     * @see org.argouml.application.api.ArgoModule#getModuleName()
     */
    public String getModuleName() { return "SettingsTabModules"; }

    /**
     * @see org.argouml.application.api.ArgoModule#getModuleDescription()
     */
    public String getModuleDescription() { return "Selecting Modules"; }

    /**
     * @see org.argouml.application.api.ArgoModule#getModuleAuthor()
     */
    public String getModuleAuthor() { return "ArgoUML Core"; }

    /**
     * @see org.argouml.application.api.ArgoModule#getModuleVersion()
     */
    public String getModuleVersion() { return "1.0"; }

    /**
     * @see org.argouml.application.api.ArgoModule#getModuleKey()
     */
    public String getModuleKey() { return "module.settings.modules"; }

    /**
     * @see org.argouml.application.api.SettingsTabPanel#getTabKey()
     */
    public String getTabKey() { return "tab.modules"; }

    /**
     * The UID.
     */
    private static final long serialVersionUID = 8945027241102020504L;
}
