package org.argouml.core.propertypanels;

import java.net.URL;

import junit.framework.TestCase;

public class TestPropertyPanelsClasspath extends TestCase {

    private URL locate(String className) {
        String resource = className.replace('.', '/') + ".class";
        return getClass().getClassLoader().getResource(resource);
    }

    public void testGetterSetterManagerImplIsOnClasspath() {
        assertNotNull(locate(
                "org.argouml.core.propertypanels.model.GetterSetterManagerImpl"));
    }

    public void testUMLModelElementListModelIsOnClasspath() {
        assertNotNull(locate(
                "org.argouml.core.propertypanels.ui.UMLModelElementListModel"));
    }
}
