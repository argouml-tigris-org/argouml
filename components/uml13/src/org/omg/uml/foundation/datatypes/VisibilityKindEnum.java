package org.omg.uml.foundation.datatypes;

/**
 * VisibilityKind enumeration class implementation.
 */
public final class VisibilityKindEnum implements VisibilityKind {
    /**
     * Enumeration constant corresponding to literal vk_public.
     */
    public static final VisibilityKindEnum VK_PUBLIC = new VisibilityKindEnum("vk_public");
    /**
     * Enumeration constant corresponding to literal vk_protected.
     */
    public static final VisibilityKindEnum VK_PROTECTED = new VisibilityKindEnum("vk_protected");
    /**
     * Enumeration constant corresponding to literal vk_private.
     */
    public static final VisibilityKindEnum VK_PRIVATE = new VisibilityKindEnum("vk_private");

    private static final java.util.List typeName;
    private final java.lang.String literalName;

    static {
        java.util.ArrayList temp = new java.util.ArrayList();
        temp.add("Foundation");
        temp.add("Data_Types");
        temp.add("VisibilityKind");
        typeName = java.util.Collections.unmodifiableList(temp);
    }

    private VisibilityKindEnum(java.lang.String literalName) {
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
        if (o instanceof VisibilityKindEnum) return (o == this);
        else if (o instanceof VisibilityKind) return (o.toString().equals(literalName));
        else return ((o instanceof javax.jmi.reflect.RefEnum) && ((javax.jmi.reflect.RefEnum) o).refTypeName().equals(typeName) && o.toString().equals(literalName));
    }

    /**
     * Translates literal name to correspondent enumeration value.
     * @param name Enumeration literal.
     * @return Enumeration value corresponding to the passed literal.
     */
    public static VisibilityKind forName(java.lang.String name) {
        if (name.equals("vk_public")) return VK_PUBLIC;
        if (name.equals("vk_protected")) return VK_PROTECTED;
        if (name.equals("vk_private")) return VK_PRIVATE;
        throw new java.lang.IllegalArgumentException("Unknown literal name '" + name + "' for enumeration 'Foundation.Data_Types.VisibilityKind'");
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
