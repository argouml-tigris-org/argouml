package org.omg.uml.foundation.core;

/**
 * A_supplier_supplierDependency association proxy interface.
 */
public interface ASupplierSupplierDependency extends javax.jmi.reflect.RefAssociation {
    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param supplier Value of the first association end.
     * @param supplierDependency Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.omg.uml.foundation.core.ModelElement supplier, org.omg.uml.foundation.core.Dependency supplierDependency);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param supplier Required value of the first association end.
     * @return Collection of related objects.
     */
    public java.util.Collection getSupplier(org.omg.uml.foundation.core.Dependency supplierDependency);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param supplierDependency Required value of the second association end.
     * @return Collection of related objects.
     */
    public java.util.Collection getSupplierDependency(org.omg.uml.foundation.core.ModelElement supplier);
    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param supplier Value of the first association end.
     * @param supplierDependency Value of the second association end.
     */
    public boolean add(org.omg.uml.foundation.core.ModelElement supplier, org.omg.uml.foundation.core.Dependency supplierDependency);
    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param supplier Value of the first association end.
     * @param supplierDependency Value of the second association end.
     */
    public boolean remove(org.omg.uml.foundation.core.ModelElement supplier, org.omg.uml.foundation.core.Dependency supplierDependency);
}
