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

// File: UMLModelElementListLinkModel.java
// Classes: UMLModelElementListLinkModel
// Original Author: mail@jeremybennett.com
// $$

// 2 Apr 2002: Jeremy Bennett (mail@jeremybennett.com). Created to facilitate
// development of extension point functionality for use case diagrams.


package org.argouml.uml.ui;

import ru.novosoft.uml.*;
import javax.swing.*;
import ru.novosoft.uml.foundation.core.*;
import java.util.*;
import java.awt.*;


/**
 * <p>An abstract list model which allows linking to existing artifacts in the
 *   UML model.</p>
 *
 * <p>In addition to the menu associated with UMLModelElementListModel (Open,
 *   Add, Delete, Move Up, Move Down), this also provides a "Link" menu entry,
 *   with sub-entries which are valid existing entries that may be linked
 *   to.</p>
 *
 * <p>For the "Link" sub-list, we divide the entries in to two categories,
 *   those that make good semantic sense, and those that make only syntactic
 *   sense. If both groups exist, the two are separated by a line in the
 *   list.</p>
 *
 * <p> For example in an extension point list for an extend relationship,
 *   it is only semantically valid to link to extension points whose owning use
 *   case is the same as the base use case of the extend relationship. However
 *   syntactically we can link to any extension point.</p>
 *
 * <p>Within the spirit of ArgoUML we still permit the latter, since it may be
 *   convenient while in the middle of a major edit/reorganisation of a
 *   model. It will be the responsibility of a critic to point out he flaw.</p>
 *
 * <p>The constructor is parameterised to allow the link entry to be disabled
 *   (in which case there is no functional difference from the base
 *   {@link UMLModelElementListModel}).</p>
 *
 * @author Jeremy Bennett (mail@jeremybennett.com)
 */

abstract public class UMLModelElementListLinkModel extends
                      UMLModelElementListModel {

    /**
     * <p>A constant to indicate a potential link menu entry makes semantic (as
     *   well as syntactic) sense.</p>
     */

    protected final int SEMANTIC_ENTRY = 0;


    /**
     * <p>A constant to indicate a potential link menu entry makes syntactic
     *   (but not semantic) sense.</p>
     */

    protected final int SYNTACTIC_ENTRY = 1;


    /**
     * <p>A constant to indicate a potential link menu entry is not valid.</p>
     */

    protected final int INVALID_ENTRY = 2;


    /**
     * <p>The list for the link pop-up menu. Null at creation.</p>
     */
 
    private Vector _links = null;


    /**
     * <p>A flag for whether to use the "Link" entry on the menu. Default value
     *   is <code>false</code> (no "Link" menu).</p>
     */
 
    private boolean _useLink = false;


    /**
     * <p>Create a new list model, with the "Link" sub-menu enabled or disabled
     *   according to a supplied flag.<p>
     *
     * <p>Implementation is just an invocation of the parent constructor and a
     *   setting of the flag.</p>
     *
     * @param container    The container for this list&mdash;the use case
     *                     or extend property panel.
     *
     * @param elementType  The element type of entries in the Link menu entry.
     *
     * @param showNone     <code>true</code> if an empty list is represented by
     *                     the keyword "none", <code>false</code> otherwise.
     *
     * @param useLink      <code>true</code> if the "Link" menu should be
     *                     shown, <code>false</code> otherwise.
     */

    public UMLModelElementListLinkModel(UMLUserInterfaceContainer container,
                                        Class elementType, boolean showNone,
                                        boolean useLink) {

        // The property entry of the superconstructor is irrelevant we'll do
        // that ourselves.

        super(container, null, showNone);

        // Set instance variables

        _useLink = useLink;

        // Finally add a third party listener if the container is a prop panel,
        // so we can here of any changes to objects of meta class elementType.
        // Other containers will have to do this for themselves.

        if (container instanceof PropPanel) {
            Object[] eventsToWatch = {elementType, null};
            ((PropPanel) container).addThirdPartyEventListening(eventsToWatch);
        }
    }


    /**
     * <p>Accessor function to get the {@link #_useLink} flag value.</p>
     *
     * @return  the value of the {@link #_useLink} flag.
     */

    protected boolean useLink() {
        return _useLink;
    }


    /**
     * <p>Accessor function to set the {@link #_useLink} flag value.</p>
     *
     * @useLink  the value to be used for the {@link #_useLink} flag.
     */

    protected void setUseLink(boolean useLink) {
        _useLink = useLink;
    }


    /**
     * <p>Override the popup in {@link UMLModelElementListModel}, to add
     *   (optionally) a new entry, "Link" with sub-menu entries allowing the
     *   selection of existing model elements.</p>
     *
     * <p><em>Note</em>. The "Link" entry will only appear if the {#_useLink}
     *   flag has been set. Its entries are divided into those that meet both
     *   syntactic and semantic rules of UML modelling and those that meet only
     *   syntactic rules, with a separator between.</p>
     *
     * <p>Usual rules apply. No "Open" if there is no entry, no "Add" if we
     *   have reached the limit (not anticipated for
     *   UMLExtensionPointListModel, but could happen for subclasses), no
     *   "Delete" if there is nothing to be deleted, and "Move Up" and "Move
     *   Down" only if they make sense.</p>
     *
     * <p>The "Link" entry appears after "Add" and is greyed out if there are
     *   entries in it. The validation of model element entries to appear on
     *   the menu is through the {@link #isAcceptable} method, which must be
     *   provided. They are formatted by the {@link #formatElement} method,
     *   which may be overridden.</p>
     *
     * <p>If the "Link" menu is enabled, a link method must be provided, which
     *   takes two arguments, the index of the element in the main list on
     *   which the pop-up was invoked (as for the other action methods), and
     *   the modelElement in the link sub-menu.</p>
     *
     * <p>This method may be overridden to provide a different type of popup
     *   menu if desired.</p>
     *
     *  @param popup  popup menu
     *
     *  @param index  index of selected list item on which this popUp is being
     *                invoked.
     *
     *  @return       <code>true</code> if popup menu should be displayed,
     *                <code>false</code> otherwise. In the current
     *                implementation we always return <code>true</code>.  */

    public boolean buildPopup(JPopupMenu popup, int index) {
        UMLUserInterfaceContainer container = getContainer();
        Object                    target    = getTarget();

        // Each entry in turn. First "Open"

        UMLListMenuItem open =
            new UMLListMenuItem(container.localize("Open"), this,
                                "open", index);

        if (getModelElementSize() <= 0) {
            open.setEnabled(false);
        }

        popup.add(open);

        // "Add"

        UMLListMenuItem add =
            new UMLListMenuItem(container.localize("Add"), this,
                                "add", index);

        if ((_upper >= 0) && (getModelElementSize() >= _upper)) {
            add.setEnabled(false);
        }

        popup.add(add);

        // "Link". Create a new sub-menu if the flag is set.

        if (useLink()) {
            JMenu link = new JMenu(container.localize("Link"));

            // Grey out if we didn't have anything on the sub-popup

            if (!buildSubPopup(link, index)) {
                link.setEnabled(false);
            }

            popup.add(link);
        }

        // "Delete"

        UMLListMenuItem delete =
            new UMLListMenuItem(container.localize("Delete"), this,
                                "delete", index);

        if (getModelElementSize() <= 0) {
            delete.setEnabled(false);
        }

        popup.add(delete);

        // "Move Up"

        UMLListMenuItem moveUp =
            new UMLListMenuItem(container.localize("Move Up"), this,
                                "moveUp", index);

        if (index == 0) {
            moveUp.setEnabled(false);
        }

        popup.add(moveUp);

        // "Move Down"

        UMLListMenuItem moveDown =
            new UMLListMenuItem(container.localize("Move Down"), this,
                                "moveDown", index);

        if (index == (getSize() - 1)) {
            moveDown.setEnabled(false);
        }

        popup.add(moveDown);

        return true;
    }


    /**
     * <p>Build a sub-menu of existing extension points for the "Link"
     *   menu.</p>
     *
     * <p>We make two collections, first of the elements which are valid both
     *   syntactically and semantically, secondly of elements which are only
     *   syntactically valid. If both groups have members, then they are shown
     *   separated by a horizontal bar on the sub-menu.</p>
     *
     * <p>The method may be overridden to provide a different sort of
     *   sub-menu.</p>
     *
     *  @param popup  popup menu to use for the sub-menu
     *
     *  @param index  index of selected list item on which this sub-popUp is
     *                being invoked.
     *
     *  @return       <code>true</code> if popup sub-menu has any entries, and
     *                should be displayed, <code>false</code> otherwise.
     */

    public boolean buildSubPopup(JMenu submenu, int index) {

        UMLUserInterfaceContainer container = getContainer();
        Object                    target    = getTarget();

        // Two lists for the elements we collect.

        Vector semanticList  = new Vector();
        Vector syntacticList = new Vector();

        // Build the lists from the model and the profile model

        collectElements(((MModelElement) target).getModel(), semanticList,
                        syntacticList);
        collectElements(container.getProfile().getProfileModel(), semanticList,
                        syntacticList);

        // We can give up if both lists were empty. Return false, to indicate
        // the "Link" entry should be greyed out.

        if ((semanticList.size() == 0) && (syntacticList.size() == 0)) {
            return false;
        }

        // Loop through each list adding to the sub-menu

        if (semanticList.size() != 0) {
            Iterator iter = semanticList.iterator();

            while (iter.hasNext()) {
                MModelElement      me    = (MModelElement) iter.next();
                UMLListSubMenuItem entry =
                    new UMLListSubMenuItem((String) formatElement(me), this,
                                           "link", index, me);

                submenu.add(entry);
            }
        }

        // Add a divider if both lists have entries

        if ((semanticList.size() != 0) && (syntacticList.size() != 0)) {
            submenu.addSeparator();
        }

        if (syntacticList.size() != 0) {
            Iterator iter = syntacticList.iterator();

            while (iter.hasNext()) {
                MModelElement      me    = (MModelElement) iter.next();
                UMLListSubMenuItem entry =
                    new UMLListSubMenuItem((String) formatElement(me), this,
                                           "link", index, me);

                submenu.add(entry);
            }
        }

        // We had some entries, so return "true"

        return true;
    }


    /**
     * <p>Collect all the elements that may appear on the sub-menu list.</p>
     *
     * <p>The implementation considers i) elements in the model, and ii) the
     *   standard elements in the profile. Eventually this second should be a
     *   configurable option.</p>
     *
     * @param ns
     *
     * @param semanticList   The list of elements that are semantically and
     *                       syntactically valid.
     *
     * @param syntacticList  The list of elements that are only syntactically
     *                       valid.
     *
     */
        
    public void collectElements(MNamespace ns, java.util.List semanticList,
                                java.util.List syntacticList) {

        Collection collection = ns.getOwnedElements();

        // Nothing to do if this namespace has nothing in it.

        if(collection == null) {
            return;
        }

        // Loop round the elements in the namespace collecting them in to the
        // relevant lists.

        Iterator iter = collection.iterator();

        while(iter.hasNext()) {

            MModelElement me = (MModelElement) iter.next();

            // Add to the correct list

            switch (isAcceptable(me)) {

            case SEMANTIC_ENTRY:

                semanticList.add(me);
                break;

            case SYNTACTIC_ENTRY:

                syntacticList.add(me);
                break;
            }

            // Recurse if we found a namespace

            if(me instanceof MNamespace) {
                collectElements((MNamespace) me, semanticList, syntacticList);
            }
        }
    }


    /**
     * <p>The routine to determine if a "Link" sub-menu entry is valid.</p>
     *
     * <p>We give an implementation, rather than declaring as abstract, so a
     *   sub-class that disables the "Link" menu need not provide an
     *   implementation. <em>Note</em>. If used this default implementation
     *   declares all entries invalid, so the "Link" menu will appear but be
     *   disabled.</p>
     *
     * <p>The routine should return one of the three predefined constants,
     *   {#SEMANTIC_ENTRY}, {#SYNTACTIC_ENTRY} or {#INVALID_ENTRY}, according
     *   to whether the given model element is valid i) semantically and
     *   syntactically , ii) only syntactically or iii) not valid at all.</p>
     *
     * @param me  The {@link MModelElement} to test.
     *
     * @return    {#SEMANTIC_ENTRY} if the element is valid both semantically
     *            and syntactically, {#SYNTACTIC_ENTRY} if the element is valid
     *            only syntactically, and {#INVALID_ENTRY} if the element is
     *            not valid. The effect of returning any other value is
     *            undefined.  */

    protected int isAcceptable(MModelElement me) {
        return INVALID_ENTRY;
    }

} /* End of class UMLExtensionPointListModel */
