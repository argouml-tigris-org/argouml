// Copyright (c) 1996-99 The Regents of the University of California. All
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

package org.argouml.uml.ui;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

import ru.novosoft.uml.foundation.core.MModelElement;

/**
 * @deprecated as of ArgoUml 0.13.5 (10-may-2003),
 *             replaced by {@link org.argouml.uml.ui.UMLListCellRenderer2},
 *             this class is part of the 'old'(pre 0.13.*) implementation of proppanels
 *             that used reflection a lot.
 */
public class UMLListCellRenderer extends DefaultListCellRenderer {
    ////////////////////////////////////////////////////////////////
    // class variables
    //   protected ImageIcon _AttributeIcon = ResourceLoaderWrapper.lookupIconResource("MAttribute");
    //   protected ImageIcon _OperationIcon = ResourceLoaderWrapper.lookupIconResource("MOperation");
    //   protected ImageIcon _ClassIcon = ResourceLoaderWrapper.lookupIconResource("Class");
    //   protected ImageIcon _PackageIcon = ResourceLoaderWrapper.lookupIconResource("Package");
    //   protected ImageIcon _AssociationIcon = ResourceLoaderWrapper.lookupIconResource("MAssociation");
    //   protected ImageIcon _AssociationIcon2 = ResourceLoaderWrapper.lookupIconResource("Association2");
    //   protected ImageIcon _AssociationIcon3 = ResourceLoaderWrapper.lookupIconResource("Association3");
    //   protected ImageIcon _AssociationIcon4 = ResourceLoaderWrapper.lookupIconResource("Association4");
    //   protected ImageIcon _AssociationIcon5 = ResourceLoaderWrapper.lookupIconResource("Association5");
    //   protected ImageIcon _GeneralizationIcon = ResourceLoaderWrapper.lookupIconResource("MGeneralization");
    //   protected ImageIcon _RealizationIcon = ResourceLoaderWrapper.lookupIconResource("Realization");
    //   protected ImageIcon _ClassDiagramIcon = ResourceLoaderWrapper.lookupIconResource("ClassDiagram");
    //   protected ImageIcon _UseCaseDiagramIcon = ResourceLoaderWrapper.lookupIconResource("UseCaseDiagram");
    //   protected ImageIcon _StateDiagramIcon = ResourceLoaderWrapper.lookupIconResource("StateDiagram");

    //   protected ImageIcon _StateIcon = ResourceLoaderWrapper.lookupIconResource("MState");
    //   protected ImageIcon _StartStateIcon = ResourceLoaderWrapper.lookupIconResource("StartState");
    //   protected ImageIcon _DeepIcon = ResourceLoaderWrapper.lookupIconResource("DeepHistory");
    //   protected ImageIcon _ShallowIcon = ResourceLoaderWrapper.lookupIconResource("ShallowHistory");
    //   protected ImageIcon _ForkIcon = ResourceLoaderWrapper.lookupIconResource("Fork");
    //   protected ImageIcon _JoinIcon = ResourceLoaderWrapper.lookupIconResource("Join");
    //   protected ImageIcon _BranchIcon = ResourceLoaderWrapper.lookupIconResource("Branch");
    //   protected ImageIcon _FinalStateIcon = ResourceLoaderWrapper.lookupIconResource("FinalState");

    //   protected ImageIcon _StateMachineIcon = ResourceLoaderWrapper.lookupIconResource("MStateMachine");
    //   protected ImageIcon _CompositeStateIcon = ResourceLoaderWrapper.lookupIconResource("MCompositeState");
    //   protected ImageIcon _TransitionIcon = ResourceLoaderWrapper.lookupIconResource("MTransition");

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel lab;
        lab = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if ((value instanceof String) && ((String) value).equals("")) {
            lab.setText("\"\"");
            return lab;
        }
        if (!(value instanceof MModelElement))
            return lab;
        String name = ((MModelElement) value).getName();
        if (name == null) {
            lab.setText("(null anon)");
            return lab;
        }
        String nameStr = name;
        if (nameStr.length() == 0)
            nameStr = "(anon)";
        lab.setText(nameStr);
        nameStr = nameStr + " ";
        lab.setToolTipText(nameStr);
        list.setToolTipText(nameStr);
        // icons?
        return lab;
    }

} /* end class UMLListCellRenderer */
