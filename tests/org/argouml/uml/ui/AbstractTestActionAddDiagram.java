package org.argouml.uml.ui;

import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import org.argouml.application.security.ArgoSecurityManager;
import org.argouml.uml.diagram.UMLMutableGraphSupport;
import org.argouml.uml.diagram.ui.UMLDiagram;

import ru.novosoft.uml.foundation.core.MNamespace;

/**
 * @author JBranderhorst
 */
public abstract class AbstractTestActionAddDiagram extends TestCase {

    /**
     * The action to be tested
     */
    private ActionAddDiagram _action;
    /**
     * The namespace a created diagram should have
     */
    private MNamespace _ns;

    /**
     * A list with namespaces that should be valid for the diagram to be
     * created.
     */
    private List _validNamespaces;

    /**
     * Constructor for AbstractTestActionAddDiagram.
     * @param arg0
     */
    public AbstractTestActionAddDiagram(String arg0) {
        super(arg0);
    }

    protected void setUp() {
        ArgoSecurityManager.getInstance().setAllowExit(true);
        _action = getAction();

        _ns = getNamespace();
        _validNamespaces = getValidNamespaceClasses();
    }

    protected void tearDown() {
        _action = null;
        _ns = null;
        _validNamespaces = null;
    }

    /**
     * Should return the correct action. (for example, ActionClassDiagram for a
     * creation of a classdiagram).
     * @return ActionAddDiagram
     */
    protected abstract ActionAddDiagram getAction();

    /**
     * Should return a valid namespace for the diagram to be tested
     */
    protected abstract MNamespace getNamespace();

    /**
     * Should return a list with classes that implement MNamespace (or the JMI
     * equivalent in the future) and that are valid to use at creating the
     * diagram.
     * @return List
     */
    protected abstract List getValidNamespaceClasses();

    /**
     * Tests if a created diagram complies to the following conditions:
     * - Has a valid namespace
     * - Has a MutableGraphModel
     * - Has a proper name
     */
    public void testCreateDiagram() {
        try {
            UMLDiagram diagram = _action.createDiagram(_ns);
            assertNotNull(
                "The diagram has no namespace",
                diagram.getNamespace());
            // assertEquals("The diagram does not have the correct namespace", _ns, diagram.getNamespace());
            assertTrue(
                "The diagram has a non-valid namespace",
                _action.isValidNamespace(diagram.getNamespace()));
            assertNotNull(
                "The diagram has no graphmodel",
                diagram.getGraphModel());
            assertTrue(
                "The graphmodel of the diagram is not a UMLMutableGraphSupport",
                diagram.getGraphModel() instanceof UMLMutableGraphSupport);
            assertNotNull("The diagram has no name", diagram.getName());
        } catch (Exception noHead) {
        }
    }

    /**
     * Tests if two diagrams created have different names
     */
    public void testDifferentNames() {
        try {
            UMLDiagram diagram1 = _action.createDiagram(_ns);
            UMLDiagram diagram2 = _action.createDiagram(_ns);
            assertTrue(
                "The created diagrams have the same name",
                !(diagram1.getName().equals(diagram2.getName())));
        } catch (Exception noHead) {
        }
    }

    /**
     * Tests if the namespace created by getNamespace() is a valid namespace for
     * the diagram.
     */
    public void testValidTestNamespace() {
        try {
            assertTrue(
                "The namespace with this testis not valid for this diagram",
                _action.isValidNamespace(_ns));
        } catch (Exception noHead) {
        }
    }

    /**
     * Tests if the list with namespaces defined in getValidNamespaceClasses
     * contains only valid namespaces.
     */
    public void testValidNamespaces() {
        try {
            Iterator it = _validNamespaces.iterator();
            while (it.hasNext()) {
                Class clazz = (Class) it.next();
                try {
                    Object o = clazz.newInstance();
                    assertTrue(
                        clazz.getName()
                            + " is no valid namespace for the diagram",
                        _action.isValidNamespace((MNamespace) o));
                } catch (Exception ex) {
                    fail(ex.getMessage());
                }
            }
        } catch (Exception noHead) {
        }
    }

}
