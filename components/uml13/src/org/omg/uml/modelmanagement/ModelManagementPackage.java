package org.omg.uml.modelmanagement;

/**
 * Model_Management package interface.
 */
public interface ModelManagementPackage extends javax.jmi.reflect.RefPackage {
    /**
     * Returns UmlPackage class proxy object.
     * @return UmlPackage class proxy object.
     */
    public org.omg.uml.modelmanagement.UmlPackageClass getUmlPackage();
    /**
     * Returns Model class proxy object.
     * @return Model class proxy object.
     */
    public org.omg.uml.modelmanagement.ModelClass getModel();
    /**
     * Returns Subsystem class proxy object.
     * @return Subsystem class proxy object.
     */
    public org.omg.uml.modelmanagement.SubsystemClass getSubsystem();
    /**
     * Returns ElementImport class proxy object.
     * @return ElementImport class proxy object.
     */
    public org.omg.uml.modelmanagement.ElementImportClass getElementImport();
    /**
     * Returns AModelElementElementImport association proxy object.
     * @return AModelElementElementImport association proxy object.
     */
    public org.omg.uml.modelmanagement.AModelElementElementImport getAModelElementElementImport();
    /**
     * Returns APackageElementImport association proxy object.
     * @return APackageElementImport association proxy object.
     */
    public org.omg.uml.modelmanagement.APackageElementImport getAPackageElementImport();
}
