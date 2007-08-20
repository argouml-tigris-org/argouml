// $Id:SettingsTabModules.java 12883 2007-06-19 21:04:47Z mvw $
// Copyright (c) 2004-2007 The Regents of the University of California. All
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
import java.util.Iterator;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;

import org.argouml.application.api.GUISettingsTabInterface;
import org.argouml.i18n.Translator;
import org.tigris.swidgets.LabelledLayout;

/**
 * Tab for the settings dialog that makes it possible to
 * select the modules that are to be enabled.
 *
 * TODO: Header for the table.
 *
 * @author Linus Tolke
 */
class SettingsTabModules extends JPanel
    implements GUISettingsTabInterface {

    /**
     * The table of modules.
     */
    private JTable table;
    
    private JTextField fieldAllExtDirs;

    /**
     * The names of the columns in the table.
     */
    private String[] columnNames = {
	Translator.localize("misc.column-name.module"), 
        Translator.localize("misc.column-name.enabled"),
    };

    /**
     * The objects representing the modules from the new module loader.
     */
    private Object[][] elements;

    /**
     * The constructor.
     */
    SettingsTabModules() {
        // The creation of the actual GUI elements is deferred until
        // they are actually needed. Otherwize we have problems
        // with the initialization.
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

	/*
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	public int getColumnCount() {
	    return columnNames.length;
	}

	/*
	 * @see javax.swing.table.TableModel#getColumnName(int)
	 */
	public String getColumnName(int col) {
	    return columnNames[col];
	}

	/*
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	public int getRowCount() {
	    return elements.length;
	}

	/*
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	public Object getValueAt(int row, int col) {
	    if (row < elements.length) {
		return elements[row][col];
	    } else {
	        return null;       
            }
	}

	/*
	 * @see javax.swing.table.TableModel#setValueAt(
	 *         java.lang.Object, int, int)
	 */
	public void setValueAt(Object ob, int row, int col) {
	    elements[row][col] = ob;
	}

	/*
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

	/*
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

    /*
     * @see GUISettingsTabInterface#handleSettingsTabRefresh()
     */
    public void handleSettingsTabRefresh() {
        table.setModel(new ModuleTableModel());
        
        StringBuffer sb = new StringBuffer();
        List locations = ModuleLoader2.getInstance().getExtensionLocations();
        for (Iterator it = locations.iterator(); it.hasNext();) {
            sb.append((String) it.next());
            sb.append("\n");
        }
        fieldAllExtDirs.setText(sb.substring(0, sb.length() - 1).toString());
    }

    /*
     * @see GUISettingsTabInterface#handleSettingsTabSave()
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

    /*
     * @see GUISettingsTabInterface#handleSettingsTabCancel()
     */
    public void handleSettingsTabCancel() {
        // Do nothing!
        // The next time we refresh, we will fetch the values again.
    }

    /*
     * @see GUISettingsTabInterface#getTabKey()
     */
    public String getTabKey() { return "tab.modules"; }

    /*
     * @see GUISettingsTabInterface#getTabPanel()
     */
    public JPanel getTabPanel() {
        if (table == null) {
            setLayout(new BorderLayout());

            table = new JTable(new ModuleTableModel());
            table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
            table.setShowVerticalLines(true);
            add(new JScrollPane(table), BorderLayout.CENTER);
            int labelGap = 10;
            int componentGap = 5;
            JPanel top = new JPanel(new LabelledLayout(labelGap, componentGap));
            JLabel label = new JLabel(
                    Translator.localize("label.extension-directories"));
            JTextField j = new JTextField();
            fieldAllExtDirs = j;
            fieldAllExtDirs.setEnabled(false);
            label.setLabelFor(fieldAllExtDirs);
            top.add(label);
            top.add(fieldAllExtDirs);
            add(top, BorderLayout.NORTH);
        }

        return this;
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = 8945027241102020504L;

    /*
     * @see org.argouml.ui.GUISettingsTabInterface#handleResetToDefault()
     */
    public void handleResetToDefault() {
        // Do nothing - these buttons are not shown.
    }

}
