// $Id$
// Copyright (c) 2006 The Regents of the University of California. All
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

package org.argouml.ui.cmd;

import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import org.apache.log4j.Logger;
import org.argouml.application.api.Configuration;
import org.argouml.notation.ui.ActionNotation;
import org.argouml.ui.ActionExportXMI;
import org.argouml.ui.ActionImportXMI;
import org.argouml.ui.ActionProjectSettings;
import org.argouml.ui.ActionSettings;
import org.argouml.ui.ProjectBrowser;
import org.argouml.ui.explorer.ActionPerspectiveConfig;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.ActionActivityDiagram;
import org.argouml.uml.ui.ActionClassDiagram;
import org.argouml.uml.ui.ActionCollaborationDiagram;
import org.argouml.uml.ui.ActionDeploymentDiagram;
import org.argouml.uml.ui.ActionGenerateAll;
import org.argouml.uml.ui.ActionGenerateOne;
import org.argouml.uml.ui.ActionGenerateProjectCode;
import org.argouml.uml.ui.ActionGenerationSettings;
import org.argouml.uml.ui.ActionImportFromSources;
import org.argouml.uml.ui.ActionOpenProject;
import org.argouml.uml.ui.ActionRevertToSaved;
import org.argouml.uml.ui.ActionSaveAllGraphics;
import org.argouml.uml.ui.ActionSaveGraphics;
import org.argouml.uml.ui.ActionSaveProjectAs;
import org.argouml.uml.ui.ActionSequenceDiagram;
import org.argouml.uml.ui.ActionStateDiagram;
import org.argouml.uml.ui.ActionUseCaseDiagram;
import org.argouml.util.KeyEventUtils;
import org.tigris.gef.base.AlignAction;
import org.tigris.gef.base.CmdAdjustGrid;
import org.tigris.gef.base.CmdAdjustGuide;
import org.tigris.gef.base.CmdAdjustPageBreaks;
import org.tigris.gef.base.CmdGroup;
import org.tigris.gef.base.CmdReorder;
import org.tigris.gef.base.CmdSelectAll;
import org.tigris.gef.base.CmdSelectInvert;
import org.tigris.gef.base.CmdSelectNear;
import org.tigris.gef.base.CmdSelectNext;
import org.tigris.gef.base.CmdUngroup;
import org.tigris.gef.base.CmdZoom;
import org.tigris.gef.base.DistributeAction;
import org.tigris.gef.base.NudgeAction;

/**
 * This class manages all Argo's shortcuts
 * 
 * @author nirux
 */
public class ShortcutMgr {

    /** Action key for new project */
    public static final String ACTION_NEW_PROJECT = "newProject";

    /** Action key for open project */
    public static final String ACTION_OPEN_PROJECT = "openProject";

    /** Action key for save project */
    public static final String ACTION_SAVE_PROJECT = "saveProject";

    /** Action key for save project as */
    public static final String ACTION_SAVE_PROJECT_AS = "saveProjectAs";

    /** Action key for print */
    public static final String ACTION_PRINT = "print";

    /** Action key for selectAll */
    public static final String ACTION_SELECT_ALL = "selectAll";

    /** Action key for undo */
    public static final String ACTION_UNDO = "undo";

    /** Action key for redo */
    public static final String ACTION_REDO = "redo";

    /** Action key for remove from diagram */
    public static final String ACTION_REMOVE_FROM_DIAGRAM = "removeFromDiagram";

    /** Action key for delete model elements */
    public static final String ACTION_DELETE_MODEL_ELEMENTS = 
        "deleteModelElements";

    /** Action key for zoom out */
    public static final String ACTION_ZOOM_OUT = "zoomOut";

    /** Action key for zoom in */
    public static final String ACTION_ZOOM_IN = "zoomIn";

    /** Action key for find */
    public static final String ACTION_FIND = "find";

    /** Action key for generate all classes */
    public static final String ACTION_GENERATE_ALL_CLASSES = 
        "generateAllClasses";

    /** Action key for align rights */
    public static final String ACTION_ALIGN_RIGHTS = "alignRights";

    /** Action key for align lefts */
    public static final String ACTION_ALIGN_LEFTS = "alignLefts";

    /** Action key for revert to saved */
    public static final String ACTION_REVERT_TO_SAVED = "revertToSaved";

    /** Action key for import xmi */
    public static final String ACTION_IMPORT_XMI = "importXmi";

    /** Action key for export xmi */
    public static final String ACTION_EXPORT_XMI = "exportXmi";

    /** Action key for import from sources */
    public static final String ACTION_IMPORT_FROM_SOURCES = "importFromSources";

    /** Action key for project settings */
    public static final String ACTION_PROJECT_SETTINGS = "projectSettings";

    /** Action key for page setup */
    public static final String ACTION_PAGE_SETUP = "pageSetup";

    /** Action key for save graphics */
    public static final String ACTION_SAVE_GRAPHICS = "saveGraphics";

    /** Action key for save all graphics */
    public static final String ACTION_SAVE_ALL_GRAPHICS = "saveAllGraphics";

    /** Action key for navigate forward */
    public static final String ACTION_NAVIGATE_FORWARD = 
        "navigateTargetForward";

    /** Action key for navigate back */
    public static final String ACTION_NAVIGATE_BACK = "navigateTargetBack";

    /** Action key for select invert */
    public static final String ACTION_SELECT_INVERT = "selectInvert";

    /** Action key for perspective info */
    public static final String ACTION_PERSPECTIVE_CONFIG = "perspectiveConfig";

    /** Action key for settings */
    public static final String ACTION_SETTINGS = "settings";

    /** Action key for notation */
    public static final String ACTION_NOTATION = "notation";

    /** Action key for go to diagram */
    public static final String ACTION_GO_TO_DIAGRAM = "goToDiagram";

    /** Action key for zoom reset */
    public static final String ACTION_ZOOM_RESET = "zoomReset";

    /** Action key for adjust grid */
    public static final String ACTION_ADJUST_GRID = "adjustGrid";

    /** Action key for adjust guide */
    public static final String ACTION_ADJUST_GUIDE = "adjustGuide";

    /** Action key for adjust page breaks */
    public static final String ACTION_ADJUST_PAGE_BREAKS = "adjustPageBreaks";

    /** Action key for show xml dump */
    public static final String ACTION_SHOW_XML_DUMP = "showXmlDump";

    /** Action key for use case diagram */
    public static final String ACTION_USE_CASE_DIAGRAM = "useCaseDiagrams";

    /** Action key for class diagram */
    public static final String ACTION_CLASS_DIAGRAM = "classDiagrams";

    /** Action key for sequence diagram */
    public static final String ACTION_SEQUENCE_DIAGRAM = "sequenceDiagrams";

    /** Action key for collaboration diagram */
    public static final String ACTION_COLLABORATION_DIAGRAM = 
        "collaborationDiagrams";

    /** Action key for state diagram */
    public static final String ACTION_STATE_DIAGRAM = "stateDiagrams";

    /** Action key for activity diagram */
    public static final String ACTION_ACTIVITY_DIAGRAM = "activityDiagrams";

    /** Action key for deployment diagram */
    public static final String ACTION_DEPLOYMENT_DIAGRAM = "deploymentDiagrams";

    /** Action key for generate one */
    public static final String ACTION_GENERATE_ONE = "generateOne";

    /** Action key for generate project code */
    public static final String ACTION_GENERATE_PROJECT_CODE = 
        "generateProjectCode";

    /** Action key for generation settings */
    public static final String ACTION_GENERATION_SETTINGS = 
        "generationSettings";

    /** Action key for preferred size */
    public static final String ACTION_PREFERRED_SIZE = "preferredSize";

    /** Action key for auto critique */
    public static final String ACTION_AUTO_CRITIQUE = "autoCritique";

    /** Action key for open decisions */
    public static final String ACTION_OPEN_DECISIONS = "openDecisions";

    /** Action key for open goals */
    public static final String ACTION_OPEN_GOALS = "openGoals";

    /** Action key for open critics */
    public static final String ACTION_OPEN_CRITICS = "openCritics";

    /** Action key for system info */
    public static final String ACTION_SYSTEM_INFORMATION = "systemInfo";

    /** Action key for about ArgoUML */
    public static final String ACTION_ABOUT_ARGOUML = "aboutArgoUml";

    /** Action key for align tops */
    public static final String ACTION_ALIGN_TOPS = "alignTops";

    /** Action key for align bottoms */
    public static final String ACTION_ALIGN_BOTTOMS = "alignBottoms";

    /** Action key for align h centers */
    public static final String ACTION_ALIGN_H_CENTERS = "alignHCenters";

    /** Action key for align v centers */
    public static final String ACTION_ALIGN_V_CENTERS = "alignVCenters";

    /** Action key for align to grid */
    public static final String ACTION_ALIGN_TO_GRID = "alignToGrid";

    /** Action key for distribute h spacing */
    public static final String ACTION_DISTRIBUTE_H_SPACING = 
        "distributeHSpacing";

    /** Action key for distribute h centers */
    public static final String ACTION_DISTRIBUTE_H_CENTERS = 
        "distributeHCenters";

    /** Action key for distribute v spacing */
    public static final String ACTION_DISTRIBUTE_V_SPACING = 
        "distributeVSpacing";

    /** Action key for distribute v centers */
    public static final String ACTION_DISTRIBUTE_V_CENTERS = 
        "distributeVCenters";

    /** Action key for reorder forward */
    public static final String ACTION_REORDER_FORWARD = "reorderForward";

    /** Action key for reorder backward */
    public static final String ACTION_REORDER_BACKWARD = "reorderBackward";

    /** Action key for reorder to front */
    public static final String ACTION_REORDER_TO_FRONT = "reorderToFront";

    /** Action key for reorder to back */
    public static final String ACTION_REORDER_TO_BACK = "reorderToBack";

    /** Action key for nudge left */
    public static final String ACTION_NUDGE_LEFT = "nudgeLeft";

    /** Action key for nudhe right */
    public static final String ACTION_NUDGE_RIGHT = "nudgeRight";

    /** Action key for nudge up */
    public static final String ACTION_NUDGE_UP = "nudgeUp";

    /** Action key for nudge down */
    public static final String ACTION_NUDGE_DOWN = "nudgeDown";

    /** Action key for select next element */
    public static final String ACTION_SELECT_NEXT = "selectNext";

    /** Action key for select previous element */
    public static final String ACTION_SELECT_PREVIOUS = "selectPrevious";

    /** Action key for group selected elements */
    public static final String ACTION_GROUP = "group";

    /** Action key for ungroup selected elements */
    public static final String ACTION_UNGROUP = "ungroup";

    /** Action key for select left */
    public static final String ACTION_SELECT_LEFT = "selectLeft";

    /** Action key for select left */
    public static final String ACTION_SELECT_RIGHT = "selectRight";

    /** Action key for select left */
    public static final String ACTION_SELECT_UP = "selectUp";

    /** Action key for select left */
    public static final String ACTION_SELECT_DOWN = "selectDown";

    /** Action key for nudge left 2x */
    public static final String ACTION_NUDGE_LEFT_2X = "nudgeLeft2x";

    /** Action key for nudhe right 2x */
    public static final String ACTION_NUDGE_RIGHT_2X = "nudgeRight2x";

    /** Action key for nudge up 2x */
    public static final String ACTION_NUDGE_UP_2X = "nudgeUp2x";

    /** Action key for nudge down 2x */
    public static final String ACTION_NUDGE_DOWN_2X = "nudgeDown2x";

    /** Action key for nudge left 4x */
    public static final String ACTION_NUDGE_LEFT_4X = "nudgeLeft4x";

    /** Action key for nudhe right 4x */
    public static final String ACTION_NUDGE_RIGHT_4X = "nudgeRight4x";

    /** Action key for nudge up 4x */
    public static final String ACTION_NUDGE_UP_4X = "nudgeUp4x";

    /** Action key for nudge down 4x */
    public static final String ACTION_NUDGE_DOWN_4X = "nudgeDown4x";
    
    
    /** 
     * The expression between modifier/modifier and between modifier/text 
     */
    public static final String MODIFIER_JOINER = " + ";

    /** 
     * The text for the shift modifier 
     */
    public static final String SHIFT_MODIFIER = "SHIFT";

    /** 
     * The text for the ctrl modifier 
     */
    public static final String CTRL_MODIFIER = "CTRL";

    /** 
     * The text for the meta modifier 
     */
    public static final String META_MODIFIER = "META";

    /** 
     * The text for the alt modifier 
     */
    public static final String ALT_MODIFIER = "ALT";

    /** 
     * The text for the alt-gr modifier 
     */
    public static final String ALT_GRAPH_MODIFIER = "altGraph";

    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(ShortcutMgr.class);

    private static final int DEFAULT_MASK = Toolkit.getDefaultToolkit()
            .getMenuShortcutKeyMask();

    private static final int SHIFTED_DEFAULT_MASK = Toolkit.getDefaultToolkit()
            .getMenuShortcutKeyMask() | KeyEvent.SHIFT_DOWN_MASK;

    private static final int META_MASK = KeyEvent.META_DOWN_MASK;

    private static final int SHIFT_MASK = KeyEvent.SHIFT_DOWN_MASK;

    private static final int ALT_MASK = KeyEvent.ALT_DOWN_MASK;
    
    // TODO: should this two be internationalized?
    private static final String SUFFIX_2X = " 2x";
    private static final String SUFFIX_4X = " 4x";

    private static HashMap shortcutHash = new HashMap(90);

    private static HashMap duplicate = new HashMap(10);

    /**
     * Return the shortcuts as an Actions array
     * 
     * @return an array of Actions
     */
    public static Action[] getShortcuts() {
        Action[] actions = (Action[]) shortcutHash.values().toArray(
                new Action[shortcutHash.size()]);
        Arrays.sort(actions, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Action) o1).getActionName().compareTo(((Action) o2)
                                                            .getActionName());
            }
        });
        return actions;
    }

    /**
     * Assign a shortcut to the given JMenuItem
     * 
     * @param menuItem
     *            the menu item
     * @param shortcutKey
     *            the shortcut key
     */
    public static void assignAccelerator(JMenuItem menuItem, 
            String shortcutKey) {
        Action shortcut = (Action) shortcutHash.get(shortcutKey);

        if (shortcut != null) {
            KeyStroke keyStroke = shortcut.getCurrentShortcut();
            if (keyStroke != null) {
                menuItem.setAccelerator(keyStroke);
            }
            KeyStroke alternativeKeyStroke = (KeyStroke) duplicate
                    .get(keyStroke);
            if (alternativeKeyStroke != null) {
                String actionName = (String) menuItem.getAction().getValue(
                        AbstractAction.NAME);

                menuItem.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                        alternativeKeyStroke, actionName);
                menuItem.getActionMap().put(actionName, menuItem.getAction());
            }
        }
    }

    /**
     * Assign a shortcut to the given JPanel (only when focused)
     * 
     * @param panel
     *            the panel
     * @param shortcutKey
     *            the shortcut key
     */
    public static void assignAccelerator(JPanel panel, 
            String shortcutKey) {
        Action shortcut = (Action) shortcutHash.get(shortcutKey);

        if (shortcut != null) {
            KeyStroke keyStroke = shortcut.getCurrentShortcut();
            if (keyStroke != null) {
                panel.registerKeyboardAction(shortcut.getActionInstance(), 
                        keyStroke, JComponent.WHEN_FOCUSED);
            }
            KeyStroke alternativeKeyStroke = (KeyStroke) duplicate
                    .get(keyStroke);
            if (alternativeKeyStroke != null) {
                String actionName = (String) 
                    shortcut.getActionInstance().getValue(AbstractAction.NAME);

                panel.getInputMap(JComponent.WHEN_FOCUSED).put(
                        alternativeKeyStroke, actionName);
                panel.getActionMap().put(actionName, 
                        shortcut.getActionInstance());
            }
        }
    }
    
    /**
     * Search for the duplicate of a given KeyStroke
     * 
     * @param keyStroke         the KeyStroke to search for
     * @return                  the duplicate, or null if not present
     */
    public static KeyStroke getDuplicate(KeyStroke keyStroke) {
        return (KeyStroke) duplicate.get(keyStroke);
    }
    
    /**
     * Returns a shortcut for the given action id
     * 
     * @param actionId
     *            the id of the action
     * @return the given action, or null if the action is not found
     */
    public static Action getShortcut(String actionId) {
        return (Action) shortcutHash.get(actionId);
    }

    private static void putDefaultShortcut(String shortcutKey,
        KeyStroke defaultKeyStroke, AbstractAction action) {
        putDefaultShortcut(shortcutKey, defaultKeyStroke, action, 
                getActionDefaultName(action));
    }

    private static void putDefaultShortcut(String shortcutKey,
        KeyStroke defaultKeyStroke, AbstractAction action, String actionName) {
        // let's load the current shortcut from the configuration (as a string)
        String confCurrentShortcut = Configuration.getString(Configuration
                .makeKey(shortcutKey), null);
        KeyStroke currentKeyStroke = null;

        if (confCurrentShortcut == null) {
            // if the current shortcut has not been set, then the actual
            // shortcut value is the default one
            currentKeyStroke = defaultKeyStroke;
        } else if (confCurrentShortcut.compareTo("") != 0) {
            // if the current shortcut has been set but is void, then the
            // current shortcut is not set --> then the default value does not
            // change
            currentKeyStroke = decodeKeyStroke(confCurrentShortcut);
        }

        Action currentShortcut = new Action(shortcutKey, currentKeyStroke,
                defaultKeyStroke, action, actionName);
        shortcutHash.put(shortcutKey, currentShortcut);
    }

    /**
     * This method decodes the given String into the corresponding KeyStroke
     * 
     * @param strKeyStroke
     *            the String to be decoded
     * @return the corresponding KeyStroke
     */
    public static KeyStroke decodeKeyStroke(String strKeyStroke) {
        assert (strKeyStroke != null);

        StringTokenizer tokenizer = new StringTokenizer(strKeyStroke,
                MODIFIER_JOINER);
        int modifiers = 0;
        while (tokenizer.hasMoreElements()) {
            String nextElement = (String) tokenizer.nextElement();
            if (tokenizer.hasMoreTokens()) {
                modifiers |= decodeModifier(nextElement);
            } else {
                try {
                    Field f = KeyEvent.class.getField("VK_" + nextElement);
                    return KeyStroke.getKeyStroke(f.getInt(null), modifiers);

                } catch (Exception exc) {
                    LOG.error("Exception: " + exc);
                }
            }
        }
        return null;
    }

    /**
     * Saves the given actions in the configuration file
     * 
     * @param newActions
     *            the actions array
     */
    public static void saveShortcuts(Action[] newActions) {
        for (int i = 0; i < newActions.length; i++) {
            Action oldAction = (Action) shortcutHash
                    .get(newActions[i].getKey());
            if (newActions[i].getCurrentShortcut() == null
                    && newActions[i].getDefaultShortcut() != null) {
                // if a default action was voided then we have to save it
                Configuration.setString(Configuration.makeKey(oldAction
                        .getKey()), "");
            } else if (newActions[i].getCurrentShortcut() != null
                    && !newActions[i].getCurrentShortcut().equals(
                            newActions[i].getDefaultShortcut())) {
                // if a not-default current shortcut was added, then we have to
                // save it
                Configuration.setString(Configuration.makeKey(oldAction
                        .getKey()), KeyEventUtils.formatKeyStroke(newActions[i]
                        .getCurrentShortcut()));
            } else {
                // if the actual is not going to be saved, then try to remove it
                // (as it could have been cancelled)
                Configuration.removeKey(Configuration.makeKey(oldAction
                        .getKey()));
            }
        }
    }

    private static int decodeModifier(String modifier) {
        if (modifier == null || modifier.length() == 0) {
            return 0;
        } else if (modifier.equals(CTRL_MODIFIER)) {
            return InputEvent.CTRL_DOWN_MASK;
        } else if (modifier.equals(ALT_MODIFIER)) {
            return InputEvent.ALT_DOWN_MASK;
        } else if (modifier.equals(ALT_GRAPH_MODIFIER)) {
            return InputEvent.ALT_GRAPH_DOWN_MASK;
        } else if (modifier.equals(META_MODIFIER)) {
            return InputEvent.META_DOWN_MASK;
        } else if (modifier.equals(SHIFT_MODIFIER)) {
            return InputEvent.SHIFT_DOWN_MASK;
        } else {
            // it should never go here!
            LOG.debug("Unknown modifier: " + modifier);
            return 0;
        }
    }

    private static String getActionDefaultName(AbstractAction action) {
        return (String) action.getValue(AbstractAction.NAME);
    }
    
    // let's load the default shortcut for every action
    static {
        // First of all, let's set up the duplicate hash. This hash contains
        // all the duplicate key for another key. 
        //
        // TODO: every duplicate.put() is done twice - but how to avoid this?
        duplicate.put(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, DEFAULT_MASK),
                KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, DEFAULT_MASK));
        duplicate.put(KeyStroke.getKeyStroke(
                KeyEvent.VK_SUBTRACT, DEFAULT_MASK),
                KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, DEFAULT_MASK));
        duplicate.put(KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, DEFAULT_MASK), 
                KeyStroke.getKeyStroke(KeyEvent.VK_ADD, DEFAULT_MASK));
        duplicate.put(KeyStroke.getKeyStroke(KeyEvent.VK_ADD, DEFAULT_MASK), 
                KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, DEFAULT_MASK));

        // file menu
        putDefaultShortcut(ACTION_NEW_PROJECT, KeyStroke.getKeyStroke(
                KeyEvent.VK_N, DEFAULT_MASK), new ActionNew());
        putDefaultShortcut(ACTION_OPEN_PROJECT, KeyStroke.getKeyStroke(
                KeyEvent.VK_O, DEFAULT_MASK), new ActionOpenProject());
        putDefaultShortcut(ACTION_SAVE_PROJECT, KeyStroke.getKeyStroke(
                KeyEvent.VK_S, DEFAULT_MASK), ProjectBrowser.getInstance()
                .getSaveAction());
        putDefaultShortcut(ACTION_SAVE_PROJECT_AS, null,
                new ActionSaveProjectAs());
        putDefaultShortcut(ACTION_REVERT_TO_SAVED, null,
                new ActionRevertToSaved());
        putDefaultShortcut(ACTION_IMPORT_XMI, null, new ActionImportXMI());
        putDefaultShortcut(ACTION_EXPORT_XMI, null, new ActionExportXMI());
        putDefaultShortcut(ACTION_IMPORT_FROM_SOURCES, null,
                ActionImportFromSources.getInstance());
        putDefaultShortcut(ACTION_PROJECT_SETTINGS, null,
                new ActionProjectSettings());
        putDefaultShortcut(ACTION_PAGE_SETUP, null, new ActionPageSetup());
        putDefaultShortcut(ACTION_SAVE_GRAPHICS, null, 
                new ActionSaveGraphics());
        putDefaultShortcut(ACTION_SAVE_ALL_GRAPHICS, null,
                new ActionSaveAllGraphics());
        putDefaultShortcut(ACTION_NOTATION, null, new ActionNotation());
        putDefaultShortcut(ACTION_PRINT, KeyStroke.getKeyStroke(KeyEvent.VK_P,
                DEFAULT_MASK), new ActionPrint());

        // edit menu
        putDefaultShortcut(ACTION_SELECT_ALL, KeyStroke.getKeyStroke(
                KeyEvent.VK_A, DEFAULT_MASK), new CmdSelectAll());
        putDefaultShortcut(ACTION_REDO, KeyStroke.getKeyStroke(KeyEvent.VK_Y,
                DEFAULT_MASK), ProjectBrowser.getInstance().getRedoAction());
        putDefaultShortcut(ACTION_UNDO, KeyStroke.getKeyStroke(KeyEvent.VK_Z,
                DEFAULT_MASK), ProjectBrowser.getInstance().getUndoAction());
        putDefaultShortcut(ACTION_NAVIGATE_FORWARD, null,
                new NavigateTargetForwardAction());
        putDefaultShortcut(ACTION_NAVIGATE_BACK, null,
                new NavigateTargetBackAction());
        putDefaultShortcut(ACTION_SELECT_INVERT, null, new CmdSelectInvert());
        putDefaultShortcut(ACTION_PERSPECTIVE_CONFIG, null,
                new ActionPerspectiveConfig());
        putDefaultShortcut(ACTION_SETTINGS, null, new ActionSettings());
        putDefaultShortcut(ACTION_REMOVE_FROM_DIAGRAM, KeyStroke.getKeyStroke(
                KeyEvent.VK_DELETE, 0), ProjectBrowser.getInstance()
                .getRemoveFromDiagramAction());
        putDefaultShortcut(ACTION_DELETE_MODEL_ELEMENTS, KeyStroke
                .getKeyStroke(KeyEvent.VK_DELETE, DEFAULT_MASK), TargetManager
                .getInstance().getDeleteAction());

        // view menu
        putDefaultShortcut(ACTION_GO_TO_DIAGRAM, null, new ActionGotoDiagram());
        putDefaultShortcut(ACTION_ZOOM_RESET, null, new CmdZoom(0.0));
        putDefaultShortcut(ACTION_ADJUST_GRID, null, new CmdAdjustGrid());
        putDefaultShortcut(ACTION_ADJUST_GUIDE, null, new CmdAdjustGuide());
        putDefaultShortcut(ACTION_ADJUST_PAGE_BREAKS, null,
                new CmdAdjustPageBreaks());
        putDefaultShortcut(ACTION_SHOW_XML_DUMP, null, new ActionShowXMLDump());
        putDefaultShortcut(ACTION_ZOOM_IN, KeyStroke.getKeyStroke(
                KeyEvent.VK_MINUS, DEFAULT_MASK), new CmdZoom(
                (1.0) / (GenericArgoMenuBar.ZOOM_FACTOR)));
        putDefaultShortcut(ACTION_ZOOM_OUT, KeyStroke.getKeyStroke(
                KeyEvent.VK_PLUS, DEFAULT_MASK), new CmdZoom(
                GenericArgoMenuBar.ZOOM_FACTOR));
        putDefaultShortcut(ACTION_FIND, KeyStroke.getKeyStroke(KeyEvent.VK_F3,
                0), new ActionFind());

        // create menu
        putDefaultShortcut(ACTION_USE_CASE_DIAGRAM, null,
                new ActionUseCaseDiagram());
        putDefaultShortcut(ACTION_CLASS_DIAGRAM, null, 
                new ActionClassDiagram());
        putDefaultShortcut(ACTION_SEQUENCE_DIAGRAM, null,
                new ActionSequenceDiagram());
        putDefaultShortcut(ACTION_COLLABORATION_DIAGRAM, null,
                new ActionCollaborationDiagram());
        putDefaultShortcut(ACTION_STATE_DIAGRAM, null, 
                new ActionStateDiagram());
        putDefaultShortcut(ACTION_ACTIVITY_DIAGRAM, null,
                new ActionActivityDiagram());
        putDefaultShortcut(ACTION_DEPLOYMENT_DIAGRAM, null,
                new ActionDeploymentDiagram());

        // generate menu
        putDefaultShortcut(ACTION_GENERATE_ONE, null, new ActionGenerateOne());
        putDefaultShortcut(ACTION_GENERATE_PROJECT_CODE, null,
                new ActionGenerateProjectCode());
        putDefaultShortcut(ACTION_GENERATION_SETTINGS, null,
                new ActionGenerationSettings());
        putDefaultShortcut(ACTION_GENERATE_ALL_CLASSES, KeyStroke.getKeyStroke(
                KeyEvent.VK_F7, 0), new ActionGenerateAll());

        // critique menu
        putDefaultShortcut(ACTION_AUTO_CRITIQUE, null, 
                new ActionAutoCritique());
        putDefaultShortcut(ACTION_OPEN_DECISIONS, null,
                new ActionOpenDecisions());
        putDefaultShortcut(ACTION_OPEN_GOALS, null, new ActionOpenGoals());
        putDefaultShortcut(ACTION_OPEN_CRITICS, null, new ActionOpenCritics());

        // critique menu
        putDefaultShortcut(ACTION_SYSTEM_INFORMATION, null,
                new ActionSystemInfo());
        putDefaultShortcut(ACTION_ABOUT_ARGOUML, null, 
                new ActionAboutArgoUML());

        // arrange menu
        putDefaultShortcut(ACTION_PREFERRED_SIZE, null,
                new CmdSetPreferredSize(CmdSetPreferredSize.MINIMUM_SIZE));

        // align submenu
        putDefaultShortcut(ACTION_ALIGN_TOPS, null, new AlignAction(
                AlignAction.ALIGN_TOPS));
        putDefaultShortcut(ACTION_ALIGN_BOTTOMS, null, new AlignAction(
                AlignAction.ALIGN_BOTTOMS));
        putDefaultShortcut(ACTION_ALIGN_RIGHTS, KeyStroke.getKeyStroke(
                KeyEvent.VK_R, DEFAULT_MASK), 
                new AlignAction(AlignAction.ALIGN_RIGHTS));
        putDefaultShortcut(ACTION_ALIGN_LEFTS, KeyStroke.getKeyStroke(
                KeyEvent.VK_L, DEFAULT_MASK), 
                new AlignAction(AlignAction.ALIGN_LEFTS));
        putDefaultShortcut(ACTION_ALIGN_H_CENTERS, null, new AlignAction(
                AlignAction.ALIGN_H_CENTERS));
        putDefaultShortcut(ACTION_ALIGN_V_CENTERS, null, new AlignAction(
                AlignAction.ALIGN_V_CENTERS));
        putDefaultShortcut(ACTION_ALIGN_TO_GRID, null, new AlignAction(
                AlignAction.ALIGN_TO_GRID));

        // distribute submenu
        putDefaultShortcut(ACTION_DISTRIBUTE_H_SPACING, null,
                new DistributeAction(DistributeAction.H_SPACING));
        putDefaultShortcut(ACTION_DISTRIBUTE_H_CENTERS, null,
                new DistributeAction(DistributeAction.H_CENTERS));
        putDefaultShortcut(ACTION_DISTRIBUTE_V_SPACING, null,
                new DistributeAction(DistributeAction.V_SPACING));
        putDefaultShortcut(ACTION_DISTRIBUTE_V_CENTERS, null,
                new DistributeAction(DistributeAction.V_CENTERS));

        // reorder submenu
        putDefaultShortcut(ACTION_REORDER_FORWARD, KeyStroke.getKeyStroke(
                KeyEvent.VK_F, DEFAULT_MASK), new CmdReorder(
                CmdReorder.BRING_FORWARD));
        putDefaultShortcut(ACTION_REORDER_BACKWARD, KeyStroke.getKeyStroke(
                KeyEvent.VK_B, DEFAULT_MASK), new CmdReorder(
                CmdReorder.SEND_BACKWARD));
        putDefaultShortcut(ACTION_REORDER_TO_FRONT, KeyStroke.getKeyStroke(
                KeyEvent.VK_F, SHIFTED_DEFAULT_MASK), new CmdReorder(
                CmdReorder.BRING_TO_FRONT));
        putDefaultShortcut(ACTION_REORDER_TO_BACK, KeyStroke.getKeyStroke(
                KeyEvent.VK_B, SHIFTED_DEFAULT_MASK), new CmdReorder(
                CmdReorder.SEND_TO_BACK));

        // nudge submenu
        putDefaultShortcut(ACTION_NUDGE_LEFT, KeyStroke.getKeyStroke(
                KeyEvent.VK_LEFT, DEFAULT_MASK), new NudgeAction(
                NudgeAction.LEFT));
        putDefaultShortcut(ACTION_NUDGE_RIGHT, KeyStroke.getKeyStroke(
                KeyEvent.VK_RIGHT, DEFAULT_MASK), new NudgeAction(
                NudgeAction.RIGHT));
        putDefaultShortcut(ACTION_NUDGE_UP, KeyStroke.getKeyStroke(
                KeyEvent.VK_UP, DEFAULT_MASK), new NudgeAction(
                NudgeAction.UP));
        putDefaultShortcut(ACTION_NUDGE_DOWN, KeyStroke.getKeyStroke(
                KeyEvent.VK_DOWN, DEFAULT_MASK), new NudgeAction(
                NudgeAction.DOWN));
        
        // JGraph editor
        putDefaultShortcut(ACTION_SELECT_NEXT, KeyStroke.getKeyStroke(
                KeyEvent.VK_TAB, 0), new CmdSelectNext(true));

        putDefaultShortcut(ACTION_SELECT_PREVIOUS, KeyStroke.getKeyStroke(
                KeyEvent.VK_TAB, KeyEvent.SHIFT_MASK), 
                new CmdSelectNext(false));

        putDefaultShortcut(ACTION_GROUP, KeyStroke.getKeyStroke(
                KeyEvent.VK_G, DEFAULT_MASK), new CmdGroup());

        putDefaultShortcut(ACTION_UNGROUP, KeyStroke.getKeyStroke(
                KeyEvent.VK_U, DEFAULT_MASK), new CmdUngroup());

        putDefaultShortcut(ACTION_SELECT_LEFT, KeyStroke.getKeyStroke(
                KeyEvent.VK_LEFT, META_MASK), 
                new CmdSelectNear(CmdSelectNear.LEFT));
        putDefaultShortcut(ACTION_SELECT_RIGHT, KeyStroke.getKeyStroke(
                KeyEvent.VK_RIGHT, META_MASK), 
                new CmdSelectNear(CmdSelectNear.RIGHT));
        putDefaultShortcut(ACTION_SELECT_UP, KeyStroke.getKeyStroke(
                KeyEvent.VK_UP, META_MASK), 
                new CmdSelectNear(CmdSelectNear.UP));
        putDefaultShortcut(ACTION_SELECT_DOWN, KeyStroke.getKeyStroke(
                KeyEvent.VK_DOWN, META_MASK), 
                new CmdSelectNear(CmdSelectNear.DOWN));
        
        AbstractAction leftAction2x = new NudgeAction(NudgeAction.LEFT, 8);
        AbstractAction rightAction2x = new NudgeAction(NudgeAction.RIGHT, 8);
        AbstractAction upAction2x = new NudgeAction(NudgeAction.UP, 8);
        AbstractAction downAction2x = new NudgeAction(NudgeAction.DOWN, 8);
        
        putDefaultShortcut(ACTION_NUDGE_LEFT_2X, KeyStroke.getKeyStroke(
                KeyEvent.VK_LEFT, SHIFT_MASK), leftAction2x, 
                getActionDefaultName(leftAction2x) + SUFFIX_2X);
        putDefaultShortcut(ACTION_NUDGE_RIGHT_2X, KeyStroke.getKeyStroke(
                KeyEvent.VK_RIGHT, SHIFT_MASK), rightAction2x,
                getActionDefaultName(rightAction2x) + SUFFIX_2X);
        putDefaultShortcut(ACTION_NUDGE_UP_2X, KeyStroke.getKeyStroke(
                KeyEvent.VK_UP, SHIFT_MASK), upAction2x,
                getActionDefaultName(upAction2x) + SUFFIX_2X);
        putDefaultShortcut(ACTION_NUDGE_DOWN_2X, KeyStroke.getKeyStroke(
                KeyEvent.VK_DOWN, SHIFT_MASK), downAction2x,        
                getActionDefaultName(downAction2x) + SUFFIX_2X);
        
        AbstractAction leftAction4x = new NudgeAction(NudgeAction.LEFT, 18);
        AbstractAction rightAction4x = new NudgeAction(NudgeAction.RIGHT, 18);
        AbstractAction upAction4x = new NudgeAction(NudgeAction.UP, 18);
        AbstractAction downAction4x = new NudgeAction(NudgeAction.DOWN, 18);
        
        putDefaultShortcut(ACTION_NUDGE_LEFT_4X, KeyStroke.getKeyStroke(
                KeyEvent.VK_LEFT, ALT_MASK), leftAction4x,
                getActionDefaultName(leftAction4x) + SUFFIX_4X);
        putDefaultShortcut(ACTION_NUDGE_RIGHT_4X, KeyStroke.getKeyStroke(
                KeyEvent.VK_RIGHT, ALT_MASK), rightAction4x,
                getActionDefaultName(rightAction4x) + SUFFIX_4X);
        putDefaultShortcut(ACTION_NUDGE_UP_4X, KeyStroke.getKeyStroke(
                KeyEvent.VK_UP, ALT_MASK), upAction4x,
                getActionDefaultName(upAction4x) + SUFFIX_4X);
        putDefaultShortcut(ACTION_NUDGE_DOWN_4X, KeyStroke.getKeyStroke(
                KeyEvent.VK_DOWN, ALT_MASK), downAction4x,
                getActionDefaultName(downAction4x) + SUFFIX_4X);
    }
}
