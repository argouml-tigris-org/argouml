package org.argouml.core.propertypanels.ui;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import junit.framework.TestCase;

import org.argouml.core.propertypanels.ModelTestSupport;
import org.argouml.model.AddAssociationEvent;
import org.argouml.model.AttributeChangeEvent;
import org.argouml.model.InvalidElementException;
import org.argouml.model.Model;
import org.argouml.model.RemoveAssociationEvent;
import org.argouml.uml.ui.AbstractActionAddModelElement2;
import org.argouml.uml.ui.AbstractActionNewModelElement;
import org.argouml.uml.ui.AbstractActionRemoveElement;

public class TestUMLModelElementListModel extends TestCase {

    private static class StringListModel extends UMLModelElementListModel {

        private Collection<?> source = Collections.emptyList();
        private int builds;
        private boolean failOnBuild;
        private boolean failOnValidation;

        StringListModel() {
            super("feature");
        }

        void setSource(Collection<?> newSource) {
            source = newSource;
        }

        int getBuilds() {
            return builds;
        }

        void setFailOnBuild(boolean fail) {
            failOnBuild = fail;
        }

        void setFailOnValidation(boolean fail) {
            failOnValidation = fail;
        }

        @Override
        protected void buildModelList() {
            builds++;
            if (failOnBuild) {
                addElement("parcial");
                throw new InvalidElementException("removido");
            }
            setAllElements(source);
        }

        @Override
        protected boolean isValidElement(Object element) {
            if (failOnValidation) {
                throw new InvalidElementException("removido");
            }
            return element instanceof String;
        }
    }

    private static class OrderedStringListModel extends StringListModel
            implements Ordered {

        private Collection<?> elements = Collections.emptyList();

        void setModelElements(Collection<?> newElements) {
            elements = newElements;
        }

        public Collection<?> getModelElements() {
            return elements;
        }
    }

    private static class RecordingListener implements ListDataListener {

        private final List<String> events = new ArrayList<String>();

        public void intervalAdded(ListDataEvent e) {
            events.add("added " + e.getIndex0() + "-" + e.getIndex1());
        }

        public void intervalRemoved(ListDataEvent e) {
            events.add("removed " + e.getIndex0() + "-" + e.getIndex1());
        }

        public void contentsChanged(ListDataEvent e) {
            events.add("changed " + e.getIndex0() + "-" + e.getIndex1());
        }

        List<String> getEvents() {
            return events;
        }
    }

    private StringListModel listModel;
    private RecordingListener listener;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        listModel = new StringListModel();
        listener = new RecordingListener();
        listModel.addListDataListener(listener);
    }

    private List<Object> contents(UMLModelElementListModel model) {
        return new ArrayList<Object>(Arrays.asList(model.toArray()));
    }

    private void deliver(UMLModelElementListModel model, PropertyChangeEvent e)
        throws Exception {
        model.propertyChange(e);
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
            }
        });
    }

    private AddAssociationEvent added(Object changed) {
        return new AddAssociationEvent(
                "fonte", "feature", null, changed, changed, null);
    }

    private RemoveAssociationEvent removed(Object changed) {
        return new RemoveAssociationEvent(
                "fonte", "feature", changed, null, changed, null);
    }

    public void testDefaultConstructorState() {
        StringListModel model = new StringListModel();
        assertEquals("feature", model.getEventName());
        assertNull(model.getMetaType());
        assertNull(model.getTarget());
        assertFalse(model.isReverseDropConnection());
        assertTrue(model.isShowIcon());
        assertTrue(model.isShowPath());
        assertNull(model.getAddAction());
        assertNull(model.getNewAction());
        assertNull(model.getRemoveAction());
    }

    public void testConstructorVariants() {
        UMLModelElementListModel noName = new StringListModel() {
            {
                setEventName(null);
            }
        };
        assertNull(noName.getEventName());

        UMLModelElementListModel flags =
            new UMLModelElementListModel("nome", false, false) {
                protected void buildModelList() {
                }
                protected boolean isValidElement(Object element) {
                    return false;
                }
            };
        assertEquals("nome", flags.getEventName());
        assertFalse(flags.isShowIcon());
        assertFalse(flags.isShowPath());
        assertNull(flags.getMetaType());

        UMLModelElementListModel flagsAndType =
            new UMLModelElementListModel("nome", true, false, String.class) {
                protected void buildModelList() {
                }
                protected boolean isValidElement(Object element) {
                    return false;
                }
            };
        assertTrue(flagsAndType.isShowIcon());
        assertFalse(flagsAndType.isShowPath());
        assertSame(String.class, flagsAndType.getMetaType());

        UMLModelElementListModel typed =
            new UMLModelElementListModel("nome", Integer.class) {
                protected void buildModelList() {
                }
                protected boolean isValidElement(Object element) {
                    return false;
                }
            };
        assertSame(Integer.class, typed.getMetaType());
        assertTrue(typed.isShowIcon());
        assertTrue(typed.isShowPath());

        UMLModelElementListModel reversed =
            new UMLModelElementListModel("nome", Integer.class, true) {
                protected void buildModelList() {
                }
                protected boolean isValidElement(Object element) {
                    return false;
                }
            };
        assertTrue(reversed.isReverseDropConnection());
        assertSame(Integer.class, reversed.getMetaType());
    }

    public void testConstructorsWithActions() {
        UMLModelElementListModel all = new UMLModelElementListModel("nome",
                Integer.class, (AbstractActionAddModelElement2) null,
                (AbstractActionNewModelElement) null,
                (AbstractActionRemoveElement) null) {
            protected void buildModelList() {
            }
            protected boolean isValidElement(Object element) {
                return false;
            }
        };
        assertEquals("nome", all.getEventName());
        assertSame(Integer.class, all.getMetaType());
        assertNull(all.getAddAction());
        assertNull(all.getNewAction());
        assertNull(all.getRemoveAction());

        UMLModelElementListModel add = new UMLModelElementListModel("nome",
                Integer.class, (AbstractActionAddModelElement2) null) {
            protected void buildModelList() {
            }
            protected boolean isValidElement(Object element) {
                return false;
            }
        };
        assertSame(Integer.class, add.getMetaType());

        UMLModelElementListModel create = new UMLModelElementListModel("nome",
                Integer.class, (AbstractActionNewModelElement) null) {
            protected void buildModelList() {
            }
            protected boolean isValidElement(Object element) {
                return false;
            }
        };
        assertSame(Integer.class, create.getMetaType());

        UMLModelElementListModel addRemove = new UMLModelElementListModel(
                "nome", Integer.class, (AbstractActionAddModelElement2) null,
                (AbstractActionRemoveElement) null) {
            protected void buildModelList() {
            }
            protected boolean isValidElement(Object element) {
                return false;
            }
        };
        assertSame(Integer.class, addRemove.getMetaType());

        UMLModelElementListModel addNew = new UMLModelElementListModel(
                "nome", Integer.class, (AbstractActionAddModelElement2) null,
                (AbstractActionNewModelElement) null) {
            protected void buildModelList() {
            }
            protected boolean isValidElement(Object element) {
                return false;
            }
        };
        assertSame(Integer.class, addNew.getMetaType());
    }

    public void testAddElementIgnoresNullAndDuplicates() {
        listModel.addElement("a");
        listModel.addElement(null);
        listModel.addElement("a");
        listModel.addElement("b");
        assertEquals(Arrays.asList("a", "b"), contents(listModel));
        assertEquals(Arrays.asList("added 0-0", "added 1-1"),
                listener.getEvents());
    }

    public void testAddAllWithNullThrowsNullPointerException() {
        try {
            listModel.addAll(null);
            fail("NullPointerException esperada");
        } catch (NullPointerException e) {
            assertEquals(0, listModel.getSize());
        }
    }

    public void testAddAllWithEmptyCollectionFiresNothing() {
        listModel.addAll(Collections.emptyList());
        assertEquals(0, listModel.getSize());
        assertTrue(listener.getEvents().isEmpty());
    }

    public void testAddAllOnEmptyListFiresSingleInterval() {
        listModel.addAll(Arrays.asList("a", "b", "c"));
        assertEquals(Arrays.asList("a", "b", "c"), contents(listModel));
        assertEquals(Collections.singletonList("added 0-2"),
                listener.getEvents());
    }

    public void testAddAllOnFilledListStartsIntervalAtPreviousLastIndex() {
        listModel.addElement("a");
        listModel.addElement("b");
        listener.getEvents().clear();
        listModel.addAll(Arrays.asList("c", "d"));
        assertEquals(Arrays.asList("a", "b", "c", "d"), contents(listModel));
        assertEquals(Collections.singletonList("added 1-3"),
                listener.getEvents());
    }

    public void testAddAllOfExistingElementsStillFiresInterval() {
        listModel.addElement("a");
        listener.getEvents().clear();
        listModel.addAll(Collections.singletonList("a"));
        assertEquals(Collections.singletonList("a"), contents(listModel));
        assertEquals(Collections.singletonList("added 0-0"),
                listener.getEvents());
    }

    public void testSetAllElementsReplacesContent() {
        listModel.addElement("velho");
        listModel.setAllElements(Arrays.asList("x", "y"));
        assertEquals(Arrays.asList("x", "y"), contents(listModel));
        listModel.setAllElements(Collections.emptyList());
        assertEquals(0, listModel.getSize());
    }

    public void testContainsWithCollections() {
        listModel.addElement("a");
        listModel.addElement("b");
        assertTrue(listModel.contains("a"));
        assertFalse(listModel.contains("z"));
        assertTrue(listModel.contains(Arrays.asList("a", "b")));
        assertFalse(listModel.contains(Arrays.asList("a", "z")));
        assertTrue(listModel.contains(Collections.emptyList()));
        assertTrue(new StringListModel().contains(new HashSet<Object>()));
    }

    public void testEventsAreSuppressedWhileBuildingModel() {
        listModel.setBuildingModel(true);
        listModel.addElement("a");
        listModel.removeElement("a");
        listModel.addElement("b");
        listModel.set(0, "c");
        assertTrue(listener.getEvents().isEmpty());
        listModel.setBuildingModel(false);
        listModel.set(0, "d");
        assertEquals(Collections.singletonList("changed 0-0"),
                listener.getEvents());
        listModel.removeElement("d");
        assertEquals(Arrays.asList("changed 0-0", "removed 0-0"),
                listener.getEvents());
    }

    public void testGetChangedElementByEventType() {
        assertEquals("mudado", listModel.getChangedElement(added("mudado")));
        assertEquals("fonte", listModel.getChangedElement(
                new AttributeChangeEvent("fonte", "name", "a", "b", null)));
        assertEquals("novo", listModel.getChangedElement(
                new PropertyChangeEvent("fonte", "name", "velho", "novo")));
    }

    public void testIsValidEventForSingleElements() {
        assertTrue(listModel.isValidEvent(added("texto")));
        assertFalse(listModel.isValidEvent(added(Integer.valueOf(1))));
        assertTrue(listModel.isValidEvent(new PropertyChangeEvent(
                "fonte", "feature", Integer.valueOf(1), null)));
        assertFalse(listModel.isValidEvent(new PropertyChangeEvent(
                "fonte", "feature", null, null)));
    }

    public void testIsValidEventForCollections() {
        assertTrue(listModel.isValidEvent(added(Arrays.asList("a", "b"))));
        assertFalse(listModel.isValidEvent(
                added(Arrays.asList("a", Integer.valueOf(1)))));
        assertFalse(listModel.isValidEvent(
                added(Collections.emptyList())));
        assertTrue(listModel.isValidEvent(new PropertyChangeEvent("fonte",
                "feature", Collections.singletonList("a"),
                Collections.emptyList())));
        assertFalse(listModel.isValidEvent(new PropertyChangeEvent("fonte",
                "feature", Collections.emptyList(), Collections.emptyList())));
    }

    public void testAddEventAppendsValidElement() throws Exception {
        deliver(listModel, added("a"));
        deliver(listModel, added("a"));
        deliver(listModel, added(Integer.valueOf(1)));
        assertEquals(Collections.singletonList("a"), contents(listModel));
    }

    public void testAddEventWithCollectionAppendsEachElement() throws Exception {
        listModel.addElement("a");
        deliver(listModel, added(Arrays.asList("a", "b", "c")));
        assertEquals(Arrays.asList("a", "b", "c"), contents(listModel));
    }

    public void testAddEventOnOrderedModelUsesListPosition() throws Exception {
        OrderedStringListModel ordered = new OrderedStringListModel();
        ordered.addElement("a");
        ordered.addElement("c");
        ordered.setModelElements(Arrays.asList("a", "b", "c"));
        deliver(ordered, added("b"));
        assertEquals(Arrays.asList("a", "b", "c"), contents(ordered));
    }

    public void testAddEventOnOrderedModelUsesIterationPosition()
        throws Exception {
        OrderedStringListModel ordered = new OrderedStringListModel();
        ordered.addElement("a");
        ordered.addElement("c");
        String b = "b";
        Collection<Object> elements = new java.util.LinkedHashSet<Object>(
                Arrays.asList("a", b, "c"));
        ordered.setModelElements(elements);
        deliver(ordered, added(b));
        assertEquals(Arrays.asList("a", "b", "c"), contents(ordered));
    }

    public void testRemoveEventRemovesContainedElement() throws Exception {
        listModel.addElement("a");
        listModel.addElement("b");
        deliver(listModel, removed("a"));
        deliver(listModel, removed("z"));
        assertEquals(Collections.singletonList("b"), contents(listModel));
    }

    public void testRemoveEventWithCollectionRequiresAllContained()
        throws Exception {
        listModel.addElement("a");
        listModel.addElement("b");
        listModel.addElement("c");
        deliver(listModel, removed(Arrays.asList("a", "z")));
        assertEquals(Arrays.asList("a", "b", "c"), contents(listModel));
        deliver(listModel, removed(Arrays.asList("a", "b")));
        assertEquals(Collections.singletonList("c"), contents(listModel));
    }

    public void testOtherEventsAreIgnored() throws Exception {
        listModel.addElement("a");
        deliver(listModel,
                new AttributeChangeEvent("b", "name", "a", "b", null));
        deliver(listModel, new PropertyChangeEvent("fonte", "x", null, "b"));
        assertEquals(Collections.singletonList("a"), contents(listModel));
    }

    public void testInvalidElementDuringEventIsSwallowed() throws Exception {
        listModel.setFailOnValidation(true);
        deliver(listModel, added("a"));
        assertEquals(0, listModel.getSize());
    }

    public void testPopupAndToolbarDefaults() {
        assertFalse(listModel.buildPopup(null, 0));
        assertFalse(listModel.hasPopup());
        assertNull(listModel.getToolbar());
        assertTrue(listModel.getActions().isEmpty());
        assertTrue(listModel.getNewActions().isEmpty());
    }

    public void testSetTargetBuildsListAndRegistersTarget() {
        ModelTestSupport.initializeModel();
        Object model = Model.getModelManagementFactory().createModel();
        Object umlClass = Model.getCoreFactory().buildClass("Alvo", model);
        listModel.setSource(Arrays.asList("a", "b"));
        listModel.setTarget(umlClass);
        assertSame(umlClass, listModel.getTarget());
        assertEquals(1, listModel.getBuilds());
        assertEquals(Arrays.asList("a", "b"), contents(listModel));
        assertEquals(Collections.singletonList("added 0-1"),
                listener.getEvents());
        listModel.removeModelEventListener();
        Model.getUmlFactory().delete(model);
    }

    public void testSetTargetWithEmptySourceFiresNothing() {
        ModelTestSupport.initializeModel();
        Object model = Model.getModelManagementFactory().createModel();
        listModel.setTarget(model);
        assertEquals(0, listModel.getSize());
        assertTrue(listener.getEvents().isEmpty());
        listModel.removeModelEventListener();
        Model.getUmlFactory().delete(model);
    }

    public void testSetTargetKeepsPartialListWhenBuildFails() {
        ModelTestSupport.initializeModel();
        Object model = Model.getModelManagementFactory().createModel();
        listModel.setFailOnBuild(true);
        listModel.setTarget(model);
        assertEquals(Collections.singletonList("parcial"), contents(listModel));
        assertEquals(Collections.singletonList("added 0-0"),
                listener.getEvents());
        listModel.removeModelEventListener();
        Model.getUmlFactory().delete(model);
    }

    public void testSetTargetTwiceFailsAssertion() {
        ModelTestSupport.initializeModel();
        Object model = Model.getModelManagementFactory().createModel();
        listModel.setTarget(model);
        try {
            listModel.setTarget(model);
            fail("AssertionError esperado");
        } catch (AssertionError e) {
            assertEquals(1, listModel.getBuilds());
        }
        listModel.removeModelEventListener();
        Model.getUmlFactory().delete(model);
    }

    public void testSetListTargetBypassesRegistration() {
        listModel.setListTarget("qualquer");
        assertEquals("qualquer", listModel.getTarget());
        assertEquals(0, listModel.getBuilds());
    }
}
