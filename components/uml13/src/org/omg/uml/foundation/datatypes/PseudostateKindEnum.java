package org.omg.uml.foundation.datatypes;

/**
 * PseudostateKind enumeration class implementation.
 */
public final class PseudostateKindEnum implements PseudostateKind {
    /**
     * Enumeration constant corresponding to literal pk_initial.
     */
    public static final PseudostateKindEnum PK_INITIAL = new PseudostateKindEnum("pk_initial");
    /**
     * Enumeration constant corresponding to literal pk_deepHistory.
     */
    public static final PseudostateKindEnum PK_DEEP_HISTORY = new PseudostateKindEnum("pk_deepHistory");
    /**
     * Enumeration constant corresponding to literal pk_shallowHistory.
     */
    public static final PseudostateKindEnum PK_SHALLOW_HISTORY = new PseudostateKindEnum("pk_shallowHistory");
    /**
     * Enumeration constant corresponding to literal pk_join.
     */
    public static final PseudostateKindEnum PK_JOIN = new PseudostateKindEnum("pk_join");
    /**
     * Enumeration constant corresponding to literal pk_fork.
     */
    public static final PseudostateKindEnum PK_FORK = new PseudostateKindEnum("pk_fork");
    /**
     * Enumeration constant corresponding to literal pk_branch.
     */
    public static final PseudostateKindEnum PK_BRANCH = new PseudostateKindEnum("pk_branch");
    /**
     * Enumeration constant corresponding to literal pk_junction.
     */
    public static final PseudostateKindEnum PK_JUNCTION = new PseudostateKindEnum("pk_junction");
    /**
     * Enumeration constant corresponding to literal pk_final.
     */
    public static final PseudostateKindEnum PK_FINAL = new PseudostateKindEnum("pk_final");

    private static final java.util.List typeName;
    private final java.lang.String literalName;

    static {
        java.util.ArrayList temp = new java.util.ArrayList();
        temp.add("Foundation");
        temp.add("Data_Types");
        temp.add("PseudostateKind");
        typeName = java.util.Collections.unmodifiableList(temp);
    }

    private PseudostateKindEnum(java.lang.String literalName) {
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
        if (o instanceof PseudostateKindEnum) return (o == this);
        else if (o instanceof PseudostateKind) return (o.toString().equals(literalName));
        else return ((o instanceof javax.jmi.reflect.RefEnum) && ((javax.jmi.reflect.RefEnum) o).refTypeName().equals(typeName) && o.toString().equals(literalName));
    }

    /**
     * Translates literal name to correspondent enumeration value.
     * @param name Enumeration literal.
     * @return Enumeration value corresponding to the passed literal.
     */
    public static PseudostateKind forName(java.lang.String name) {
        if (name.equals("pk_initial")) return PK_INITIAL;
        if (name.equals("pk_deepHistory")) return PK_DEEP_HISTORY;
        if (name.equals("pk_shallowHistory")) return PK_SHALLOW_HISTORY;
        if (name.equals("pk_join")) return PK_JOIN;
        if (name.equals("pk_fork")) return PK_FORK;
        if (name.equals("pk_branch")) return PK_BRANCH;
        if (name.equals("pk_junction")) return PK_JUNCTION;
        if (name.equals("pk_final")) return PK_FINAL;
        throw new java.lang.IllegalArgumentException("Unknown literal name '" + name + "' for enumeration 'Foundation.Data_Types.PseudostateKind'");
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
