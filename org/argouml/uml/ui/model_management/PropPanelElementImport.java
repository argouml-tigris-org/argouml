// $Id$
// Copyright (c) 2007 The Regents of the University of California. All
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

package org.argouml.uml.ui.model_management;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.ActionNavigateContainerElement;
import org.argouml.uml.ui.UMLCheckBox2;
import org.argouml.uml.ui.UMLDerivedCheckBox;
import org.argouml.uml.ui.UMLModelElementListModel2;
import org.argouml.uml.ui.UMLPlainTextDocument;
import org.argouml.uml.ui.UMLTextField2;
import org.argouml.uml.ui.foundation.core.ActionSetElementOwnershipSpecification;
import org.argouml.uml.ui.foundation.core.PropPanelModelElement;
import org.argouml.util.ConfigLoader;

/**
 * The properties panel for an ElementImport. <p>
 * 
 * The ElementResidence is not a ModelElement according MDR, 
 * hence this properties panel does not show a name field.
 *
 * @author Michiel
 */
public class PropPanelElementImport extends PropPanelModelElement {

    private JPanel modifiersPanel;
    private JTextField aliasTextField;
    private static UMLElementImportAliasDocument aliasDocument =
        new UMLElementImportAliasDocument();
    
    /**
     * Constructor.
     */
    public PropPanelElementImport() {
        super("ElementImport", ConfigLoader.getTabPropsOrientation());

        addField(Translator.localize("label.alias"),
                getAliasTextField());

        add(getNamespaceVisibilityPanel());

        add(getModifiersPanel());

        addSeparator();

        addField(Translator.localize("label.imported-element"),
                getSingleRowScroll(
                        new ElementImportImportedElementListModel()));

        addField(Translator.localize("label.package"),
                getSingleRowScroll(new ElementImportPackageListModel()));

        addAction(new ActionNavigateContainerElement());
        addAction(getDeleteAction());
    }
    
    /**
     * @return a textfield for the alias
     */
    protected JComponent getAliasTextField() {
        if (aliasTextField == null) {
            aliasTextField = new UMLTextField2(aliasDocument);
        }
        return aliasTextField;
    }

    public JPanel getModifiersPanel() {
        if (modifiersPanel == null) {
            modifiersPanel = createBorderPanel(Translator.localize(
                "label.modifiers"));
            modifiersPanel.add(
                    new UMLElementImportIsSpecificationCheckbox());
            modifiersPanel.add(
                    new UMLDerivedCheckBox());
        }
        return modifiersPanel;
    }
}

/**
 * A checkbox for the "isSpecification" of the ElementImport.
 * 
 * @author Michiel
 */
class UMLElementImportIsSpecificationCheckbox extends UMLCheckBox2 {

    /**
     * Constructor for UMLGeneralizableElementRootCheckBox.
     */
    public UMLElementImportIsSpecificationCheckbox() {
        super(Translator.localize("checkbox.is-specification"),
                ActionSetElementOwnershipSpecification.getInstance(), 
                "isSpecification");
    }

    /*
     * @see org.argouml.uml.ui.UMLCheckBox2#buildModel()
     */
    public void buildModel() {
        if (getTarget() != null) {
            setSelected(Model.getFacade().isSpecification(getTarget()));
        }
    }

}

/**
 * The Document for the Alias of the ElementImport.
 * 
 * @author mvw
 */
class UMLElementImportAliasDocument extends UMLPlainTextDocument {

    /**
     * Constructor for UMLModelElementNameDocument.
     */
    public UMLElementImportAliasDocument() {
        super("alias");
    }

    /*
     * @see org.argouml.uml.ui.UMLPlainTextDocument#setProperty(java.lang.String)
     */
    protected void setProperty(String text) {
        Object t = getTarget();
        if (t != null) {
            Model.getModelManagementHelper().setAlias(getTarget(), text);
        }
    }

    /*
     * @see org.argouml.uml.ui.UMLPlainTextDocument#getProperty()
     */
    protected String getProperty() {
        return Model.getFacade().getAlias(getTarget());
    }

}

/**
 * The list model for the importedElement of a ElementImport.
 *
 * @author mvw
 */
class ElementImportImportedElementListModel extends UMLModelElementListModel2 {

    /**
     * Constructor.
     */
    public ElementImportImportedElementListModel() {
        super("importedElement");
    }

    protected void buildModelList() {
        if (getTarget() != null) {
            removeAllElements();
            addElement(Model.getFacade().getImportedElement(getTarget()));
        }
    }

    protected boolean isValidElement(Object element) {
        return Model.getFacade().isAElementImport(getTarget());
    }
}

/**
 * The list model for the package of a ElementImport.
 *
 * @author mvw
 */
class ElementImportPackageListModel extends UMLModelElementListModel2 {

    /**
     * Constructor.
     */
    public ElementImportPackageListModel() {
        super("package");
    }

    protected void buildModelList() {
        if (getTarget() != null) {
            removeAllElements();
            addElement(Model.getFacade().getPackage(getTarget()));
        }
    }

    protected boolean isValidElement(Object element) {
        return Model.getFacade().isAElementImport(getTarget());
    }
}