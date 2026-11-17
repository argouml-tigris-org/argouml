package org.argouml.core.propertypanels.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;

import org.argouml.core.propertypanels.ModelTestSupport;
import org.argouml.kernel.Command;
import org.argouml.model.Model;
import org.argouml.model.NotImplementedException;

public class TestGetterSetterManagerImpl extends TestCase {

    private static final String[] REGISTERED_PROPERTIES = {
        "action", "actualArgument", "aggregation", "baseClass", "base",
        "body", "changeability", "classifier", "concurrency",
        "deferrableEvent", "definedTag", "derived", "doActivity", "entry",
        "exit", "extensionPoint", "effect", "elementImport", "feature",
        "guard", "internalTransition", "isAbstract", "isActive", "isLeaf",
        "isNavigable", "isQuery", "isRoot", "isSynch", "isAsynchronous",
        "kind", "literal", "message", "method", "navigable", "ordering",
        "ownedElement", "ownerScope", "parameter", "qualifier",
        "raisedException", "raisedSignal", "receiver", "reception",
        "region", "residentElement", "sender", "subvertex", "targetScope",
        "templateParameter", "trigger", "visibility", "association",
    };

    private GetterSetterManager manager;
    private Object model;
    private Object umlClass;
    private Object dataType;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        ModelTestSupport.initializeModel();
        manager = GetterSetterManager.getGetterSetter(Object.class);
        model = Model.getModelManagementFactory().createModel();
        umlClass = Model.getCoreFactory().buildClass("Alvo", model);
        dataType = Model.getCoreFactory().buildDataType("int", model);
    }

    @Override
    protected void tearDown() throws Exception {
        Model.getUmlFactory().delete(model);
        super.tearDown();
    }

    private Collection<Class<?>> types(Object... metaTypes) {
        List<Class<?>> list = new ArrayList<Class<?>>();
        for (Object metaType : metaTypes) {
            list.add((Class<?>) metaType);
        }
        return list;
    }

    private List<Object> list(Collection<?> collection) {
        return new ArrayList<Object>(collection);
    }

    private Object buildOperation() {
        return Model.getCoreFactory().buildOperation2(umlClass, dataType, "op");
    }

    private Object buildAttribute() {
        return Model.getCoreFactory().buildAttribute2(umlClass, dataType);
    }

    private Object buildAssociationEnd() {
        Object other = Model.getCoreFactory().buildClass("Outra", model);
        Object association =
            Model.getCoreFactory().buildAssociation(umlClass, other);
        return Model.getFacade().getConnections(association)
                .iterator().next();
    }

    private Object buildTopState() {
        Object stateMachine =
            Model.getStateMachinesFactory().buildStateMachine(umlClass);
        return Model.getFacade().getTop(stateMachine);
    }

    public void testFactoryReturnsUml14Implementation() {
        assertTrue(manager instanceof GetterSetterManagerImpl);
        assertEquals('1', Model.getFacade().getUmlVersion().charAt(0));
    }

    public void testRegisteredPropertyNames() {
        for (String property : REGISTERED_PROPERTIES) {
            assertTrue(property, manager.contains(property));
        }
        assertFalse(manager.contains("ownedOperation"));
        assertFalse(manager.contains("inexistente"));
        assertFalse(manager.contains(null));
    }

    public void testUnknownPropertyIsIgnoredEverywhere() {
        manager.set(umlClass, Boolean.TRUE, "inexistente");
        assertNull(manager.get(umlClass, "inexistente", null));
        assertNull(manager.getOptions(umlClass, "inexistente", types()));
        assertNull(manager.create("inexistente", "java", "x"));
        assertNull(manager.getMetaType("inexistente"));
        assertFalse(manager.isValidElement("inexistente", types(), umlClass));
        assertFalse(manager.isFullBuildOnly("inexistente"));
        assertNull(manager.getAddCommand("inexistente", umlClass));
        assertNull(manager.getRemoveCommand("inexistente", umlClass, umlClass));
        assertNull(manager.getAdditionalCommands("inexistente", umlClass));
    }

    public void testNonListPropertiesHaveNoListBehaviour() {
        assertNull(manager.getMetaType("visibility"));
        assertFalse(manager.isValidElement("visibility", types(), umlClass));
        assertFalse(manager.isFullBuildOnly("isAbstract"));
        assertNull(manager.getAdditionalCommands("isAbstract", umlClass));
        assertNull(manager.getOptions(umlClass, "isAbstract", types()));
        assertNull(manager.create("isAbstract", "java", "x"));
        assertNull(manager.getAddCommand("visibility", umlClass));
        assertNull(manager.getRemoveCommand("visibility", umlClass, umlClass));
    }

    public void testClassBooleanProperties() {
        String[] properties = {"isAbstract", "isLeaf", "isRoot", "isActive"};
        for (String property : properties) {
            assertEquals(property, Boolean.FALSE,
                    manager.get(umlClass, property, null));
            manager.set(umlClass, Boolean.TRUE, property);
            assertEquals(property, Boolean.TRUE,
                    manager.get(umlClass, property, null));
            manager.set(umlClass, Boolean.FALSE, property);
            assertEquals(property, Boolean.FALSE,
                    manager.get(umlClass, property, null));
        }
    }

    public void testOperationIsQuery() {
        Object operation = buildOperation();
        assertEquals(Boolean.FALSE, manager.get(operation, "isQuery", null));
        manager.set(operation, Boolean.TRUE, "isQuery");
        assertEquals(Boolean.TRUE, manager.get(operation, "isQuery", null));
    }

    public void testOwnerScopeAndTargetScopeShareStaticFlag() {
        Object attribute = buildAttribute();
        assertEquals(Boolean.FALSE, manager.get(attribute, "ownerScope", null));
        manager.set(attribute, Boolean.TRUE, "ownerScope");
        assertEquals(Boolean.TRUE, manager.get(attribute, "ownerScope", null));
        assertEquals(Boolean.TRUE, manager.get(attribute, "targetScope", null));
        manager.set(attribute, Boolean.FALSE, "targetScope");
        assertEquals(Boolean.FALSE, manager.get(attribute, "ownerScope", null));
    }

    public void testVisibilityOptionsOrder() {
        assertEquals(
                Arrays.asList("public", "package", "protected", "private"),
                list(manager.getOptions(umlClass, "visibility", null)));
    }

    public void testVisibilityRoundTrip() {
        String[] values = {"public", "package", "protected", "private"};
        for (String value : values) {
            manager.set(umlClass, value, "visibility");
            assertEquals(value, manager.get(umlClass, "visibility", null));
        }
    }

    public void testUnknownVisibilityBecomesPrivate() {
        manager.set(umlClass, "public", "visibility");
        manager.set(umlClass, "qualquer", "visibility");
        assertEquals("private", manager.get(umlClass, "visibility", null));
    }

    public void testAggregationOnAssociationEnd() {
        Object end = buildAssociationEnd();
        assertEquals(Arrays.asList("aggregate", "composite", "none"),
                list(manager.getOptions(end, "aggregation", null)));
        assertEquals("none", manager.get(end, "aggregation", null));
        manager.set(end, "aggregate", "aggregation");
        assertEquals("aggregate", manager.get(end, "aggregation", null));
        manager.set(end, "composite", "aggregation");
        assertEquals("composite", manager.get(end, "aggregation", null));
        manager.set(end, "qualquer", "aggregation");
        assertEquals("none", manager.get(end, "aggregation", null));
    }

    public void testOrderingOnAssociationEnd() {
        Object end = buildAssociationEnd();
        assertEquals(Boolean.FALSE, manager.get(end, "ordering", null));
        manager.set(end, Boolean.TRUE, "ordering");
        assertEquals(Boolean.TRUE, manager.get(end, "ordering", null));
        manager.set(end, Boolean.FALSE, "ordering");
        assertEquals(Boolean.FALSE, manager.get(end, "ordering", null));
    }

    public void testNavigableAndIsNavigableAreEquivalent() {
        Object end = buildAssociationEnd();
        assertEquals(Boolean.TRUE, manager.get(end, "navigable", null));
        manager.set(end, Boolean.FALSE, "isNavigable");
        assertEquals(Boolean.FALSE, manager.get(end, "navigable", null));
        assertEquals(Boolean.FALSE, manager.get(end, "isNavigable", null));
    }

    public void testParameterDirection() {
        Object operation = buildOperation();
        Object parameter =
            Model.getFacade().getParameters(operation).iterator().next();
        assertEquals(Arrays.asList("in", "out", "inout", "return"),
                list(manager.getOptions(parameter, "kind", null)));
        assertEquals("return", manager.get(parameter, "kind", null));
        String[] values = {"in", "out", "inout", "return"};
        for (String value : values) {
            manager.set(parameter, value, "kind");
            assertEquals(value, manager.get(parameter, "kind", null));
        }
    }

    public void testConcurrencyOnOperation() {
        Object operation = buildOperation();
        assertEquals(
                Arrays.asList("sequential", "guarded", "concurrent", "undefined"),
                list(manager.getOptions(operation, "concurrency", null)));
        assertEquals("sequential", manager.get(operation, "concurrency", null));
        manager.set(operation, "guarded", "concurrency");
        assertEquals("guarded", manager.get(operation, "concurrency", null));
        manager.set(operation, "concurrent", "concurrency");
        assertEquals("concurrent", manager.get(operation, "concurrency", null));
        manager.set(operation, "sequential", "concurrency");
        assertEquals("sequential", manager.get(operation, "concurrency", null));
    }

    public void testChangeabilityOnAttribute() {
        Object attribute = buildAttribute();
        assertEquals(Arrays.asList("addonly", "changeable", "frozen"),
                list(manager.getOptions(attribute, "changeability", null)));
        assertEquals("changeable", manager.get(attribute, "changeability", null));
        manager.set(attribute, "frozen", "changeability");
        assertEquals("frozen", manager.get(attribute, "changeability", null));
        manager.set(attribute, "addonly", "changeability");
        assertEquals("addonly", manager.get(attribute, "changeability", null));
        manager.set(attribute, "qualquer", "changeability");
        assertEquals("frozen", manager.get(attribute, "changeability", null));
        manager.set(attribute, "changeable", "changeability");
        assertEquals("changeable", manager.get(attribute, "changeability", null));
    }

    public void testChangeableIsIgnoredWhenCurrentValueIsAddOnly() {
        Object attribute = buildAttribute();
        manager.set(attribute, "addonly", "changeability");
        manager.set(attribute, "changeable", "changeability");
        assertEquals("addonly", manager.get(attribute, "changeability", null));
    }

    public void testDerivedWithoutTagDefinitionAndRootModelFails() {
        try {
            manager.set(umlClass, Boolean.TRUE, "derived");
            fail("IllegalArgumentException esperada");
        } catch (IllegalArgumentException e) {
            assertEquals("namespace: null", e.getMessage());
        }
    }

    public void testDerivedIsStoredAsSingleTaggedValue() {
        Model.getExtensionMechanismsFactory()
                .buildTagDefinition("derived", null, model);
        assertEquals(Boolean.FALSE, manager.get(umlClass, "derived", null));
        manager.set(umlClass, Boolean.TRUE, "derived");
        assertEquals(Boolean.TRUE, manager.get(umlClass, "derived", null));
        Object taggedValue = Model.getFacade().getTaggedValue(umlClass, "derived");
        assertNotNull(taggedValue);
        assertEquals("true", Model.getFacade().getValueOfTag(taggedValue));
        manager.set(umlClass, Boolean.FALSE, "derived");
        assertEquals(Boolean.FALSE, manager.get(umlClass, "derived", null));
        assertSame(taggedValue,
                Model.getFacade().getTaggedValue(umlClass, "derived"));
        assertEquals("false", Model.getFacade().getValueOfTag(taggedValue));
    }

    public void testListMetaTypes() {
        assertSame(Model.getMetaTypes().getAction(), manager.getMetaType("action"));
        assertSame(Model.getMetaTypes().getArgument(),
                manager.getMetaType("actualArgument"));
        assertSame(Class.class, manager.getMetaType("baseClass"));
        assertSame(Model.getMetaTypes().getClassifier(), manager.getMetaType("base"));
        assertSame(Model.getMetaTypes().getClassifier(),
                manager.getMetaType("classifier"));
        assertSame(Model.getMetaTypes().getEvent(),
                manager.getMetaType("deferrableEvent"));
        assertSame(Model.getMetaTypes().getTagDefinition(),
                manager.getMetaType("definedTag"));
        assertSame(Model.getMetaTypes().getAction(), manager.getMetaType("doActivity"));
        assertSame(Model.getMetaTypes().getAction(), manager.getMetaType("entry"));
        assertSame(Model.getMetaTypes().getAction(), manager.getMetaType("exit"));
        assertSame(Model.getMetaTypes().getAction(), manager.getMetaType("effect"));
        assertSame(Model.getMetaTypes().getExtensionPoint(),
                manager.getMetaType("extensionPoint"));
        assertSame(Model.getMetaTypes().getModelElement(),
                manager.getMetaType("elementImport"));
        assertSame(Model.getMetaTypes().getGuard(), manager.getMetaType("guard"));
        assertSame(Model.getMetaTypes().getTransition(),
                manager.getMetaType("internalTransition"));
        assertSame(Model.getMetaTypes().getEnumerationLiteral(),
                manager.getMetaType("literal"));
        assertSame(Model.getMetaTypes().getMessage(), manager.getMetaType("message"));
        assertSame(Model.getMetaTypes().getMethod(), manager.getMetaType("method"));
        assertSame(Model.getMetaTypes().getParameter(),
                manager.getMetaType("parameter"));
        assertSame(Model.getMetaTypes().getReception(),
                manager.getMetaType("reception"));
        assertSame(Model.getMetaTypes().getElementResidence(),
                manager.getMetaType("residentElement"));
        assertSame(Model.getMetaTypes().getState(), manager.getMetaType("subvertex"));
        assertSame(Model.getMetaTypes().getTemplateParameter(),
                manager.getMetaType("templateParameter"));
        assertSame(Model.getMetaTypes().getEvent(), manager.getMetaType("trigger"));
        assertSame(Model.getMetaTypes().getAssociationEnd(),
                manager.getMetaType("association"));
        assertSame(Model.getMetaTypes().getSignal(),
                manager.getMetaType("raisedException"));
        assertSame(Model.getMetaTypes().getSignal(),
                manager.getMetaType("raisedSignal"));
    }

    public void testRegionMetaTypeIsNotAvailableInUml14() {
        try {
            manager.getMetaType("region");
            fail("NotImplementedException esperada");
        } catch (NotImplementedException e) {
            assertEquals("Not a UML 1.4 element", e.getMessage());
        }
    }

    public void testListMetaTypesThatDoNotMatchTheirContent() {
        assertSame(Model.getMetaTypes().getOperation(), manager.getMetaType("feature"));
        assertSame(Model.getMetaTypes().getOperation(),
                manager.getMetaType("ownedElement"));
        assertSame(Model.getMetaTypes().getAttribute(), manager.getMetaType("sender"));
        assertSame(Model.getMetaTypes().getAttribute(),
                manager.getMetaType("receiver"));
        assertSame(Model.getMetaTypes().getAttribute(),
                manager.getMetaType("qualifier"));
    }

    public void testFullBuildOnlyProperties() {
        assertTrue(manager.isFullBuildOnly("elementImport"));
        assertTrue(manager.isFullBuildOnly("residentElement"));
        assertFalse(manager.isFullBuildOnly("deferrableEvent"));
        assertFalse(manager.isFullBuildOnly("feature"));
        assertFalse(manager.isFullBuildOnly("subvertex"));
    }

    public void testFeatureOptionsDependOnRequestedTypes() {
        Object attribute = buildAttribute();
        Object operation = buildOperation();
        Collection<?> attributes = manager.getOptions(umlClass, "feature",
                types(Model.getMetaTypes().getAttribute()));
        assertEquals(1, attributes.size());
        assertTrue(attributes.contains(attribute));
        Collection<?> operations = manager.getOptions(umlClass, "feature",
                types(Model.getMetaTypes().getOperation(),
                        Model.getMetaTypes().getReception()));
        assertEquals(1, operations.size());
        assertTrue(operations.contains(operation));
        assertSame(Collections.EMPTY_LIST, manager.getOptions(umlClass, "feature",
                types(Model.getMetaTypes().getOperation())));
    }

    public void testIsValidElementComparesElementWithItsOwnOptions() {
        Collection<Class<?>> attributeTypes =
            types(Model.getMetaTypes().getAttribute());
        buildAttribute();
        assertFalse(manager.isValidElement("feature", attributeTypes, umlClass));
        assertFalse(manager.isValidElement("ownedElement", types(), model));
        assertFalse(manager.isValidElement("parameter", types(), buildOperation()));
    }

    public void testOwnedElementAndParameterOptions() {
        assertTrue(manager.getOptions(model, "ownedElement", types())
                .contains(umlClass));
        Object operation = buildOperation();
        Collection<?> parameters = manager.getOptions(operation, "parameter", types());
        assertEquals(1, parameters.size());
    }

    public void testSingleValuedListOptionsContainNullWhenUnset() {
        Object top = buildTopState();
        Object state = Model.getStateMachinesFactory().buildSimpleState(top);
        Object other = Model.getStateMachinesFactory().buildSimpleState(top);
        Object transition =
            Model.getStateMachinesFactory().buildTransition(state, other);
        String[] stateProperties = {"entry", "exit", "doActivity"};
        for (String property : stateProperties) {
            List<Object> options = list(
                    manager.getOptions(state, property, types()));
            assertEquals(property, Collections.singletonList(null), options);
        }
        String[] transitionProperties = {"guard", "effect", "trigger"};
        for (String property : transitionProperties) {
            List<Object> options = list(
                    manager.getOptions(transition, property, types()));
            assertEquals(property, Collections.singletonList(null), options);
        }
    }

    public void testGuardOptionsReturnExistingGuard() {
        Object top = buildTopState();
        Object state = Model.getStateMachinesFactory().buildSimpleState(top);
        Object other = Model.getStateMachinesFactory().buildSimpleState(top);
        Object transition =
            Model.getStateMachinesFactory().buildTransition(state, other);
        Object guard = Model.getStateMachinesFactory().buildGuard(transition);
        assertEquals(Collections.singletonList(guard),
                list(manager.getOptions(transition, "guard", types())));
    }

    public void testSubvertexOptions() {
        Object top = buildTopState();
        Object state = Model.getStateMachinesFactory().buildSimpleState(top);
        Collection<?> subvertices = manager.getOptions(top, "subvertex", types());
        assertTrue(subvertices.contains(state));
    }

    public void testSubvertexAdditionalCommandsCreateSevenPseudostates() {
        Object top = buildTopState();
        List<Command> commands = manager.getAdditionalCommands("subvertex", top);
        assertEquals(7, commands.size());
        for (Command command : commands) {
            assertTrue(command instanceof Named);
            assertTrue(command instanceof IconIdentifiable);
            assertNotNull(((Named) command).getName());
        }
    }

    public void testOtherListsHaveNoAdditionalCommands() {
        assertEquals(Collections.emptyList(),
                manager.getAdditionalCommands("feature", umlClass));
    }

    public void testAddCommandsExistOnlyForAddableProperties() {
        Object stereotype = Model.getExtensionMechanismsFactory()
                .buildStereotype(umlClass, "estereotipo", model);
        Object top = buildTopState();
        Object state = Model.getStateMachinesFactory().buildSimpleState(top);
        assertNotNull(manager.getAddCommand("elementImport", model));
        assertNotNull(manager.getAddCommand("deferrableEvent", state));
        assertNotNull(manager.getAddCommand("reception", umlClass));
        assertNotNull(manager.getAddCommand("residentElement", umlClass));
        assertNotNull(manager.getAddCommand("classifier", umlClass));
        assertNotNull(manager.getAddCommand("baseClass", stereotype));
        assertNotNull(manager.getAddCommand("base", umlClass));
        assertNull(manager.getAddCommand("feature", umlClass));
        assertNull(manager.getAddCommand("subvertex", top));
    }

    public void testAddCommandRejectsNullTarget() {
        try {
            manager.getAddCommand("elementImport", null);
            fail("IllegalArgumentException esperada");
        } catch (IllegalArgumentException e) {
            assertEquals("target expected", e.getMessage());
        }
    }

    public void testRemoveCommandsExistOnlyForRemoveableProperties() {
        assertNotNull(manager.getRemoveCommand("elementImport", model, umlClass));
        assertNotNull(manager.getRemoveCommand("deferrableEvent", model, umlClass));
        assertNotNull(manager.getRemoveCommand("reception", model, umlClass));
        assertNotNull(manager.getRemoveCommand("residentElement", model, umlClass));
        assertNotNull(manager.getRemoveCommand("classifier", model, umlClass));
        assertNotNull(manager.getRemoveCommand("baseClass", model, umlClass));
        assertNotNull(manager.getRemoveCommand("base", model, umlClass));
        assertNull(manager.getRemoveCommand("feature", model, umlClass));
    }

    public void testRemoveBaseClassCommand() {
        Object stereotype = Model.getExtensionMechanismsFactory()
                .buildStereotype(umlClass, "estereotipo", model);
        Model.getExtensionMechanismsHelper().addBaseClass(stereotype, "Class");
        Model.getExtensionMechanismsHelper().addBaseClass(stereotype, "Attribute");
        List<Object> options = list(
                manager.getOptions(stereotype, "baseClass", types()));
        assertEquals(Arrays.asList("Attribute", "Class"), options);
        manager.getRemoveCommand("baseClass", stereotype, "Class").execute();
        assertEquals(Collections.singletonList("Attribute"),
                list(manager.getOptions(stereotype, "baseClass", types())));
    }

    public void testRemoveElementImportCommand() {
        Object pack = Model.getModelManagementFactory().buildPackage("pacote");
        Model.getCoreHelper().addOwnedElement(model, pack);
        Model.getModelManagementFactory().buildElementImport(pack, umlClass);
        assertTrue(manager.getOptions(pack, "elementImport", types())
                .contains(umlClass));
        manager.getRemoveCommand("elementImport", pack, umlClass).execute();
        assertTrue(manager.getOptions(pack, "elementImport", types()).isEmpty());
    }

    public void testRemoveDeferrableEventCommand() {
        Object top = buildTopState();
        Object state = Model.getStateMachinesFactory().buildSimpleState(top);
        Object event = Model.getStateMachinesFactory().buildSignalEvent(model);
        Model.getStateMachinesHelper().addDeferrableEvent(state, event);
        assertTrue(manager.getOptions(state, "deferrableEvent", types())
                .contains(event));
        manager.getRemoveCommand("deferrableEvent", state, event).execute();
        assertTrue(manager.getOptions(state, "deferrableEvent", types()).isEmpty());
    }

    public void testRemoveResidentElementCommand() {
        Object component = Model.getCoreFactory().createComponent();
        Model.getCoreHelper().addOwnedElement(model, component);
        Model.getCoreFactory().buildElementResidence(umlClass, component);
        assertEquals(Collections.singletonList(umlClass),
                manager.getOptions(component, "residentElement", types()));
        Object other = Model.getCoreFactory().buildClass("NaoResidente", model);
        manager.getRemoveCommand("residentElement", component, other).execute();
        assertEquals(1, manager.getOptions(
                component, "residentElement", types()).size());
        manager.getRemoveCommand("residentElement", component, umlClass).execute();
        assertTrue(manager.getOptions(
                component, "residentElement", types()).isEmpty());
    }

    public void testMethodBodyExpression() {
        Object expression = manager.create("body", "java", "return 1;");
        assertTrue(Model.getFacade().isAExpression(expression));
        assertEquals("java", Model.getDataTypesHelper().getLanguage(expression));
        assertEquals("return 1;", Model.getFacade().getBody(expression));
        Object method = Model.getCoreFactory().buildMethod("metodo");
        manager.set(method, expression, "body");
        Object stored = manager.get(method, "body", null);
        assertEquals("java", Model.getDataTypesHelper().getLanguage(stored));
        assertEquals("return 1;", Model.getFacade().getBody(stored));
    }

    public void testListGettersAndSettersAreNoOps() {
        String[] listProperties = {
            "action", "actualArgument", "baseClass", "base", "classifier",
            "deferrableEvent", "definedTag", "doActivity", "entry", "exit",
            "extensionPoint", "effect", "elementImport", "feature", "guard",
            "internalTransition", "literal", "message", "method",
            "ownedElement", "parameter", "qualifier", "raisedException",
            "receiver", "reception", "region", "residentElement", "sender",
            "subvertex", "templateParameter", "trigger", "association",
        };
        for (String property : listProperties) {
            manager.set(umlClass, "valor", property);
            assertNull(property, manager.get(umlClass, property, null));
        }
    }

    public void testCollectionOptionsOnEmptyElements() {
        Object operation = buildOperation();
        assertTrue(manager.getOptions(operation, "raisedSignal", types()).isEmpty());
        assertTrue(manager.getOptions(operation, "method", types()).isEmpty());
        assertTrue(manager.getOptions(umlClass, "templateParameter", types())
                .isEmpty());
        Object enumeration =
            Model.getCoreFactory().buildEnumeration("Cores", model);
        Object literal = Model.getCoreFactory()
                .buildEnumerationLiteral("VERMELHO", enumeration);
        assertEquals(Collections.singletonList(literal),
                list(manager.getOptions(enumeration, "literal", types())));
        Object end = buildAssociationEnd();
        assertTrue(manager.getOptions(end, "qualifier", types()).isEmpty());
    }

    public void testAssociationOptionsListClassifierEndsInUml14() {
        Object end = buildAssociationEnd();
        assertEquals(Collections.singletonList(end),
                list(manager.getOptions(umlClass, "association", types())));
        Object association = Model.getFacade().getAssociation(end);
        try {
            manager.getOptions(association, "association", types());
            fail("IllegalArgumentException esperada");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().startsWith("Unrecognized object"));
        }
    }
}
