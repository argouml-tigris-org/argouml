// $Id$
// Copyright (c) 2004 The Regents of the University of California. All
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

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import org.argouml.application.ArgoVersion;
import org.argouml.application.api.SettingsTabPanel;
import org.argouml.application.helpers.SettingsTabHelper;

/**
 * Makes it possible to select the module that are to be enabled.
 *
 * TODO: Header for the table.
 *
 * @author Linus Tolke
 */
public class SettingsTabModules
    extends SettingsTabHelper
    implements SettingsTabPanel
{
    private JTable table;

    private String[] columnNames = {
	"Module", "Enabled",
    };
    private Object[][] elements;
	
    /**
     * The constructor.
     */
    public SettingsTabModules() {
        super();

        setLayout(new BorderLayout());
	
        table = new JTable(new ModuleTableModel());
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
	table.setShowVerticalLines(false);
        add(table, BorderLayout.CENTER);
    }

    /**
     * Table model for the table with modules.
     */
    class ModuleTableModel extends AbstractTableModel {
	public ModuleTableModel() {
	    Object[] arr = ModuleLoader2.allModules().toArray();

	    elements = new Object[arr.length][2];

	    for (int i = 0; i < elements.length; i++) {
		elements[i][0] = arr[i];
		elements[i][1] =
		    new Boolean(ModuleLoader2.isSelected((String) arr[i]));
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
	    return elements.length;
	}

	/**
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	public Object getValueAt(int row, int col) {
	    return elements[row][col];
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
	    return col >= 1;
	}
    }

    /**
     * @see SettingsTabPanel#handleSettingsTabRefresh()
     */
    public void handleSettingsTabRefresh() {
        table.setModel(new ModuleTableModel());
    }

    /**
     * @see SettingsTabPanel#handleSettingsTabSave()
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
    }

    /**
     * @see SettingsTabPanel#handleSettingsTabCancel()
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
    public String getModuleVersion() { return ArgoVersion.getVersion(); }
    
    /**
     * @see org.argouml.application.api.ArgoModule#getModuleKey()
     */
    public String getModuleKey() { return "module.settings.modules"; }

    /**
     * @see org.argouml.application.api.SettingsTabPanel#getTabKey()
     */
    public String getTabKey() { return "tab.modules"; }
}
