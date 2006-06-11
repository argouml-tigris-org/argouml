// $Id$
// Copyright (c) 2003-2006 The Regents of the University of California. All
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

package org.argouml.ui.cmd;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import org.argouml.application.api.Configuration;
import org.argouml.application.api.ConfigurationKey;
import org.argouml.application.api.Argo;
import org.argouml.uml.ui.ActionReopenProject;
import java.io.File;

/**
 * Menu extension for last recently used files menu.<p>
 *
 * functionality:
 * it is created with a link to the (file-)menu,
 * if it is created it is reading its content from config information
 * the add entry method adds a specific filename to the list, ensures that it
 * bubbles at top of list if it is already member of list
 * typically called by SaveFile method
 * LRU is added at the specific position at creation time
 * and all entries are going to be inserted or deletied
 *
 * @author  Frank Jelinek
 * @since 9. November 2003 (0.15.2)
 */
public class LastRecentlyUsedMenuList {

    /**
     * Default value for maxcount if there is no configuration.
     */
    private static final int MAX_COUNT_DEFAULT = 4;

    /**
     * Menu where the list is bound to.
     */
    private JMenu fileMenu;

    /**
     * Recent loaded count.
     */
    private int lruCount;

    /**
     * Maxcount read from configuration.
     */
    private int maxCount = MAX_COUNT_DEFAULT;

    /**
     * Index where the menu entries should be inserted
     * -1 to be sure (adds at end).
     */
    private int menuIndex = -1;

    /**
     * Menuitems actually created and added to menu.
     */
    private JMenuItem[] menuItems;

    /**
     * Array of conf keys for accessing the stored entries.
     */
    private ConfigurationKey[] confKeys;

    /**
     * Adds the eventhandler to the menu and renames the entry.
     *
     * @param filename the text for the entry
     * @param addAt the location
     * @return the menuitem
     */
    private JMenuItem addEventHandler(String filename, int addAt) {
        // the text is used by the event handler for opening the project
        File f = new File(filename);
        //JMenuItem item = _fileMenu.add(new ActionReopenProject(filename));
        JMenuItem item =
            fileMenu.insert(new ActionReopenProject(filename), addAt);

        // set maximum length of menu entry
        String entryName = f.getName();
        if (entryName.length() > 40) {
            entryName = entryName.substring(0, 40) + "...";
        }

        // text is short, tooltip is long
        item.setText(entryName);
        item.setToolTipText(filename);

        return item;
    }

    /**
     * Creates a new instance of LastRecentlyUsedMenuList with respect
     * to the menu where the lru is bound to.
     *
     * @param filemenu at this menu the lru is bound after last entry
     * it is added with a separator
     *
     */
    public LastRecentlyUsedMenuList(JMenu filemenu) {
        String newName;
        int i;

        // holds file menu
        fileMenu = filemenu;
        lruCount = 0;
        menuIndex = filemenu.getItemCount();

        // init from config
        // read number, write result as new default and prepare keys
        maxCount =
            Configuration.getInteger(Argo.KEY_NUMBER_LAST_RECENT_USED,
                                     MAX_COUNT_DEFAULT);
        Configuration.setInteger(Argo.KEY_NUMBER_LAST_RECENT_USED, maxCount);
        confKeys = new ConfigurationKey[maxCount];
        menuItems = new JMenuItem[maxCount];

        // create all nessessary configuration keys for lru
        for (i = 0; i < maxCount; i++) {
            confKeys[i] =
		Configuration.makeKey("project",
				      "mostrecent",
				      "filelist".concat(Integer.toString(i)));
        }

        // read existing file names from configuration
        i = 0;
        boolean readOK = true;
        while (i < maxCount && readOK) {
            newName = Configuration.getString(confKeys[i], "");
            if (newName.length() > 0) {
                menuItems[i] = addEventHandler(newName, menuIndex + i);
                i++;
            } else {
                readOK = false; // empty entry stops reading --> last line!
            }
        }

        // this is the recent count
        lruCount = i;
    }

    /**
     * Adds a new entry to lru list and removes the last one if
     * if it increases maxCount.
     *
     * @param filename name of link which is to be used to represent
     * _and_ reopen the file
     */
    public void addEntry(String filename) {
        // get already existing names from menu actions
        // real file names, not action names !

        String[] tempNames = new String[maxCount];
        for (int i = 0; i < lruCount; i++) {
            ActionReopenProject action =
                (ActionReopenProject) menuItems[i].getAction();
            tempNames[i] = action.getFilename();
        }

        // delete all existing entries
        for (int i = 0; i < lruCount; i++) {
            fileMenu.remove(menuItems[i]);
        }

        // add new entry as first entry
        menuItems[0] = addEventHandler(filename, menuIndex);

        // add other existing entries, but filter the just added one
        int i, j;
        i = 0;
	j = 1;
        while (i < lruCount && j < maxCount) {
            if (!(tempNames[i].equals(filename))) {
                menuItems[j] = addEventHandler(tempNames[i], menuIndex + j);
                j++;
            }
            i++;
        }

        // save count
        lruCount = j;

        // and store configuration props
        for (int k = 0; k < lruCount; k++) {
            ActionReopenProject action =
                (ActionReopenProject) menuItems[k].getAction();
            Configuration.setString(confKeys[k], action.getFilename());
        }
    }
}
