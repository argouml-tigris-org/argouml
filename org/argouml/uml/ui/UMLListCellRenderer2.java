// Copyright (c) 1996-2002 The Regents of the University of California. All
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

// $Id$
package org.argouml.uml.ui;

import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;

import org.apache.log4j.Category;
import org.argouml.application.helpers.ResourceLoaderWrapper;

import ru.novosoft.uml.MBase;
import ru.novosoft.uml.behavior.common_behavior.MSignal;
import ru.novosoft.uml.behavior.state_machines.MPseudostate;
import ru.novosoft.uml.foundation.core.MAbstraction;
import ru.novosoft.uml.foundation.core.MComment;
import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.foundation.data_types.MPseudostateKind;

/**
 * The default cell renderer for uml model elements. Used by UMLList2 and its
 * children.
 * TODO: move the lookup for the icons to its own class.
 * @author jaap.branderhorst@xs4all.nl	
 * @since Jan 2, 2003
 */
public class UMLListCellRenderer2 extends DefaultListCellRenderer {

    private Category cat = Category.getInstance(UMLListCellRenderer.class);

    /**
     * True if the icon for the modelelement should be shown. The icon is, for
     * instance, a small class symbol for a class.
     */
    private boolean _showIcon;

    protected ImageIcon _ActionStateIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("ActionState");
    protected ImageIcon _StateIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("State");
    protected ImageIcon _InitialStateIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("Initial");
    protected ImageIcon _DeepIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("DeepHistory");
    protected ImageIcon _ShallowIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("ShallowHistory");
    protected ImageIcon _ForkIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("Fork");
    protected ImageIcon _JoinIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("Join");
    protected ImageIcon _BranchIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("Branch");
    protected ImageIcon _FinalStateIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("FinalState");

    protected ImageIcon _RealizeIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("Realization");

    protected ImageIcon _SignalIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("SignalSending");

    protected ImageIcon _CommentIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("Note");

    /**
     * Cache for the icons to prevent loading each time.
     * TODO: seperate the loading into a seperate class so other classes can use
     * it.
     */
    private Map _iconCache = new HashMap();

    /**
     * Constructor for UMLListCellRenderer2.
     */
    public UMLListCellRenderer2(boolean showIcon) {
        super();
        _showIcon = showIcon;
    }

    /**
     * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
     */
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        
        if (value instanceof MModelElement) {
            MModelElement elem = (MModelElement) value;
            String name = elem.getName();
            if (name == null || name.equals("")) {
                name = "(anon " + makeTypeName(elem) + ")";
            }
            label.setText(name);            
            if (_showIcon) {
                Icon icon = getIcon(value);
                if (icon != null)
                    label.setIcon(icon);
            }
        }
        
        return label;
    }

    private String makeTypeName(MBase elem) {
        String fullName = elem.getClass().getName();
        fullName = fullName.substring(fullName.lastIndexOf('.') + 2, fullName.length());
        if (fullName.endsWith("Impl"))
            fullName = fullName.substring(0, fullName.indexOf("Impl"));
        return fullName;
    }

    private Icon getIcon(Object value) {
        Icon icon = (Icon) _iconCache.get(value.getClass());

        if (value instanceof MPseudostate) {
            MPseudostate ps = (MPseudostate) value;
            MPseudostateKind kind = ps.getKind();
            if (MPseudostateKind.INITIAL.equals(kind))
                icon = _InitialStateIcon;
            if (MPseudostateKind.DEEP_HISTORY.equals(kind))
                icon = _DeepIcon;
            if (MPseudostateKind.SHALLOW_HISTORY.equals(kind))
                icon = _ShallowIcon;
            if (MPseudostateKind.FORK.equals(kind))
                icon = _ForkIcon;
            if (MPseudostateKind.JOIN.equals(kind))
                icon = _JoinIcon;
            if (MPseudostateKind.BRANCH.equals(kind))
                icon = _BranchIcon;
            //if (MPseudostateKind.FINAL.equals(kind)) icon = _FinalStateIcon;
        }
        if (value instanceof MAbstraction) {
            icon = _RealizeIcon;
        }
        // needs more work: sending and receiving icons
        if (value instanceof MSignal) {
            icon = _SignalIcon;
        }

        if (value instanceof MComment) {
            icon = _CommentIcon;
        }

        if (icon == null) {
            String clsPackName = value.getClass().getName();
            if (clsPackName.startsWith("org") || clsPackName.startsWith("ru")) {
                String cName = clsPackName.substring(clsPackName.lastIndexOf(".") + 1);
                // special case "UML*" e.g. UMLClassDiagram
                if (cName.startsWith("UML"))
                    cName = cName.substring(3);
                if (cName.startsWith("M"))
                    cName = cName.substring(1);
                if (cName.endsWith("Impl"))
                    cName = cName.substring(0, cName.length() - 4);
                icon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource(cName);
                if (icon != null)
                    _iconCache.put(value.getClass(), icon);
                if (icon == null)
                    cat.warn("UMLTreeCellRenderer: using default Icon for " + cName);
            }
        }
        return icon;
    }

}
