package org.omg.uml;

/**
 * UML package interface.
 * Helper package that clusters all other outermost packages in UML 1.3 metamodel.
 */
public interface UmlPackage extends javax.jmi.reflect.RefPackage {
    public org.omg.uml.foundation.FoundationPackage getFoundation();
    public org.omg.uml.modelmanagement.ModelManagementPackage getModelManagement();
    public org.omg.uml.behavioralelements.BehavioralElementsPackage getBehavioralElements();
}
