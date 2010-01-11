package org.argouml.core.propertypanels.ui;

import javax.swing.DefaultListModel;

class ScrollListFactory {

    static ScrollList create(DefaultListModel model) {
        if (model instanceof UMLModelElementListModel) {
            return new OldScrollList(model);
        } else if (model instanceof org.argouml.core.propertypanels.ui.SimpleListModel) {
            return new ScrollListImpl(
                    (org.argouml.core.propertypanels.ui.SimpleListModel) model);
        }
        return null;
    }
}
