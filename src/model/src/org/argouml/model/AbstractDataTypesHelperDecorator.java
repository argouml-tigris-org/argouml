// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

package org.argouml.model;

/**
 * An abstract Decorator for the {@link DataTypesHelper}.
 *
 * @author Bob Tarling
 */
public abstract class AbstractDataTypesHelperDecorator
	implements DataTypesHelper {

    /**
     * The component.
     */
    private DataTypesHelper impl;

    /**
     * @param component The component to decorate.
     */
    AbstractDataTypesHelperDecorator(DataTypesHelper component) {
        impl = component;
    }

    /**
     * The component we are decorating.
     *
     * @return Returns the component.
     */
    protected DataTypesHelper getComponent() {
        return impl;
    }

    /**
     * @see org.argouml.model.DataTypesHelper#copyTaggedValues(
     *         java.lang.Object,
     *         java.lang.Object)
     */
    public void copyTaggedValues(Object from, Object to) {
        impl.copyTaggedValues(from, to);
    }

    /**
     * @see org.argouml.model.DataTypesHelper#equalsINITIALKind(java.lang.Object)
     */
    public boolean equalsINITIALKind(Object kind) {
        return impl.equalsINITIALKind(kind);
    }

    /**
     * @see org.argouml.model.DataTypesHelper#equalsDeepHistoryKind(java.lang.Object)
     */
    public boolean equalsDeepHistoryKind(Object kind) {
        return impl.equalsDeepHistoryKind(kind);
    }

    /**
     * @see org.argouml.model.DataTypesHelper#equalsShallowHistoryKind(java.lang.Object)
     */
    public boolean equalsShallowHistoryKind(Object kind) {
        return impl.equalsShallowHistoryKind(kind);
    }

    /**
     * @see org.argouml.model.DataTypesHelper#equalsFORKKind(java.lang.Object)
     */
    public boolean equalsFORKKind(Object kind) {
        return impl.equalsFORKKind(kind);
    }

    /**
     * @see org.argouml.model.DataTypesHelper#equalsJOINKind(java.lang.Object)
     */
    public boolean equalsJOINKind(Object kind) {
        return impl.equalsJOINKind(kind);
    }

    /**
     * @see org.argouml.model.DataTypesHelper#equalsCHOICEKind(java.lang.Object)
     */
    public boolean equalsCHOICEKind(Object kind) {
        return impl.equalsCHOICEKind(kind);
    }

    /**
     * @see org.argouml.model.DataTypesHelper#equalsJUNCTIONKind(java.lang.Object)
     */
    public boolean equalsJUNCTIONKind(Object kind) {
        return impl.equalsJUNCTIONKind(kind);
    }

    /**
     * @see org.argouml.model.DataTypesHelper#multiplicityToString(java.lang.Object)
     */
    public String multiplicityToString(Object multiplicity) {
        return impl.multiplicityToString(multiplicity);
    }

    /**
     * @see org.argouml.model.DataTypesHelper#setBody(java.lang.Object,
     *      java.lang.String)
     */
    public Object setBody(Object handle, String body) {
        return impl.setBody(handle, body);
    }

    /**
     * @see org.argouml.model.DataTypesHelper#getBody(java.lang.Object)
     */
    public String getBody(Object handle) {
        return impl.getBody(handle);
    }

    /**
     * @see org.argouml.model.DataTypesHelper#setLanguage(java.lang.Object,
     *      java.lang.String)
     */
    public Object setLanguage(Object handle, String language) {
        return impl.setLanguage(handle, language);
    }

    /**
     * @see org.argouml.model.DataTypesHelper#getLanguage(java.lang.Object)
     */
    public String getLanguage(Object handle) {
        return impl.getLanguage(handle);
    }

}
