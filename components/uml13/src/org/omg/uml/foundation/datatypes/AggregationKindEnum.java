package org.omg.uml.foundation.datatypes;

/**
 * AggregationKind enumeration class implementation.
 */
public final class AggregationKindEnum implements AggregationKind {
    /**
     * Enumeration constant corresponding to literal ak_none.
     */
    public static final AggregationKindEnum AK_NONE = new AggregationKindEnum("ak_none");
    /**
     * Enumeration constant corresponding to literal ak_aggregate.
     */
    public static final AggregationKindEnum AK_AGGREGATE = new AggregationKindEnum("ak_aggregate");
    /**
     * Enumeration constant corresponding to literal ak_composite.
     */
    public static final AggregationKindEnum AK_COMPOSITE = new AggregationKindEnum("ak_composite");

    private static final java.util.List typeName;
    private final java.lang.String literalName;

    static {
        java.util.ArrayList temp = new java.util.ArrayList();
        temp.add("Foundation");
        temp.add("Data_Types");
        temp.add("AggregationKind");
        typeName = java.util.Collections.unmodifiableList(temp);
    }

    private AggregationKindEnum(java.lang.String literalName) {
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
        if (o instanceof AggregationKindEnum) return (o == this);
        else if (o instanceof AggregationKind) return (o.toString().equals(literalName));
        else return ((o instanceof javax.jmi.reflect.RefEnum) && ((javax.jmi.reflect.RefEnum) o).refTypeName().equals(typeName) && o.toString().equals(literalName));
    }

    /**
     * Translates literal name to correspondent enumeration value.
     * @param name Enumeration literal.
     * @return Enumeration value corresponding to the passed literal.
     */
    public static AggregationKind forName(java.lang.String name) {
        if (name.equals("ak_none")) return AK_NONE;
        if (name.equals("ak_aggregate")) return AK_AGGREGATE;
        if (name.equals("ak_composite")) return AK_COMPOSITE;
        throw new java.lang.IllegalArgumentException("Unknown literal name '" + name + "' for enumeration 'Foundation.Data_Types.AggregationKind'");
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
