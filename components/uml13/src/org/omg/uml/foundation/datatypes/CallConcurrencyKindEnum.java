package org.omg.uml.foundation.datatypes;

/**
 * CallConcurrencyKind enumeration class implementation.
 */
public final class CallConcurrencyKindEnum implements CallConcurrencyKind {
    /**
     * Enumeration constant corresponding to literal cck_sequential.
     */
    public static final CallConcurrencyKindEnum CCK_SEQUENTIAL = new CallConcurrencyKindEnum("cck_sequential");
    /**
     * Enumeration constant corresponding to literal cck_guarded.
     */
    public static final CallConcurrencyKindEnum CCK_GUARDED = new CallConcurrencyKindEnum("cck_guarded");
    /**
     * Enumeration constant corresponding to literal cck_concurrent.
     */
    public static final CallConcurrencyKindEnum CCK_CONCURRENT = new CallConcurrencyKindEnum("cck_concurrent");

    private static final java.util.List typeName;
    private final java.lang.String literalName;

    static {
        java.util.ArrayList temp = new java.util.ArrayList();
        temp.add("Foundation");
        temp.add("Data_Types");
        temp.add("CallConcurrencyKind");
        typeName = java.util.Collections.unmodifiableList(temp);
    }

    private CallConcurrencyKindEnum(java.lang.String literalName) {
        this.literalName = literalName;
    }

    /**
     * Returns fully qualified name of the enumeration type.
     * @return List containing all parts of the fully qualified name.
     */
    public java.util.List refTypeName() {
        return typeName;
    }

    /**
     * Returns a string representation of the enumeration value.
     * @return A string representation of the enumeration value.
     */
    public java.lang.String toString() {
        return literalName;
    }

    /**
     * Returns a hash code for this the enumeration value.
     * @return A hash code for this enumeration value.
     */
    public int hashCode() {
        return literalName.hashCode();
    }

    /**
     * Indicates whether some other object is equal to this enumeration value.
     * @param o The reference object with which to compare.
     * @return true if the other object is the enumeration of the same type and 
     * of the same value.
     */
    public boolean equals(java.lang.Object o) {
        if (o instanceof CallConcurrencyKindEnum) return (o == this);
        else if (o instanceof CallConcurrencyKind) return (o.toString().equals(literalName));
        else return ((o instanceof javax.jmi.reflect.RefEnum) && ((javax.jmi.reflect.RefEnum) o).refTypeName().equals(typeName) && o.toString().equals(literalName));
    }

    /**
     * Translates literal name to correspondent enumeration value.
     * @param name Enumeration literal.
     * @return Enumeration value corresponding to the passed literal.
     */
    public static CallConcurrencyKind forName(java.lang.String name) {
        if (name.equals("cck_sequential")) return CCK_SEQUENTIAL;
        if (name.equals("cck_guarded")) return CCK_GUARDED;
        if (name.equals("cck_concurrent")) return CCK_CONCURRENT;
        throw new java.lang.IllegalArgumentException("Unknown literal name '" + name + "' for enumeration 'Foundation.Data_Types.CallConcurrencyKind'");
    }
    /**
     * Resolves serialized instance of enumeration value.
     * @return Resolved enumeration value.
     */
    protected java.lang.Object readResolve() throws java.io.ObjectStreamException {
        try {
            return forName(literalName);
        } catch (java.lang.IllegalArgumentException e) {
            throw new java.io.InvalidObjectException(e.getMessage());
        }
    }
}
