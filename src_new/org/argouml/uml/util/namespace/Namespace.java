// $Id$
// Copyright (c) 2003-2006 The Regents of the University of California. All
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

package org.argouml.uml.util.namespace;

import java.util.Iterator;

/**
 * The Namespace interface provides methods and constants which any class
 * dealing with namespaces needs to implement.
 *
 * @author mkl
 *
 */
public interface Namespace {

    /**
     * the scope token for java.
     */
    public static final String JAVA_NS_TOKEN = ".";

    /**
     * the scope token for uml.
     */
    public static final String UML_NS_TOKEN = "::";

    /**
     * the scope token for c++.
     */
    public static final String CPP_NS_TOKEN = "::";

    /**
     * returns the namespace which is common to both namespaces, e.g.
     * <code>org.argouml.model</code>
     * and <code>org.argouml.util</code> have <code>org.argouml</code> in
     * common.
     *
     * @param namespace
     *            a namespace
     * @return the common or empty namespace
     */
    Namespace getCommonNamespace(Namespace namespace);

    /**
     * returns the base of a namespace, e.g. the base of
     * <code>org.argouml.util</code>
     * is <code>org.argouml</code>.
     *
     * @return base namespace
     */
    Namespace getBaseNamespace();

    /**
     * add another element to a namespace.
     *
     * @param element the element to add
     */
    void pushNamespaceElement(NamespaceElement element);

    /**
     * reduces the innermost namespace element,
     * e.g. <code>org.argouml.model</code>
     * will return <code>model</code>, and change the namespace
     * to <code>org.argouml</code>.
     *
     * @return the popped element.
     */
    NamespaceElement popNamespaceElement();

    /**
     * return the innermost namespace element without removing it.
     *
     * @return the innermost namespace element
     */
    NamespaceElement peekNamespaceElement();

    /**
     * namespaces usually have a scope operator when used in representational
     * form. see also the predifined constants.
     *
     * @param token
     *            the token to use from now on.
     */
    void setDefaultScopeToken(String token);

    /**
     * return an iterator to the namespace elements.
     *
     * @return an iterator of NamespaceElements.
     */
    Iterator iterator();

    /**
     * check if the namespace is empty.
     *
     * @return true if empty
     */
    boolean isEmpty();

    /**
     * return a string representation of the namespace with the given token.
     * The existence of the method implies that classes also must implement
     * toString() in a reasonable manner using the default token.
     *
     * @param token the token to be converted
     * @return a string representation of the namespace
     */
    String toString(String token);
}
