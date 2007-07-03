// $Id: StringNamespace.java 11516 2006-11-25 04:30:15Z tfmorris $
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
import java.util.Stack;

import org.apache.log4j.Logger;

/**
 * A StringNamespace is a string based namespace (StringNamespaceElement)
 * object. It faciliates creation of these objects via a number of helper
 * methods.
 *
 * @author mkl
 */
public class StringNamespace implements Namespace, Cloneable {

    private static final Logger LOG = Logger.getLogger(StringNamespace.class);
    
    private Stack ns = new Stack();

    private String token = JAVA_NS_TOKEN;

    /**
     * Constructor.
     *
     * The empty namespace with java token by default.
     */
    public StringNamespace() {
    }

    /**
     * empty namespace with given default token.
     *
     * @param theToken
     *            the scope seperator to use
     */
    public StringNamespace(String theToken) {
        this();
        this.token = theToken;
    }

    /**
     * construct a namespace from an array of strings with default scope token.
     *
     * @param elements
     *            an array of strings which represent the namespace.
     */
    public StringNamespace(String[] elements) {
        this(elements, JAVA_NS_TOKEN);
    }

    /**
     * construct a namespace from strings with given scope token.
     *
     * @param elements
     *            an array of strings which represent the namespace.
     * @param theToken
     *            scope token to use
     */
    public StringNamespace(String[] elements, String theToken) {
        this(theToken);
        for (int i = 0; i < elements.length; i++) {
            pushNamespaceElement(new StringNamespaceElement(elements[i]));
        }
    }

    /**
     * Construct a namespace from NamespaceElements with given scope token.
     *
     * @param elements
     *            array of NamespaceElements
     * @param theToken
     *            scope token
     */
    public StringNamespace(NamespaceElement[] elements, String theToken) {
        this(theToken);

        for (int i = 0; i < elements.length; i++) {
            pushNamespaceElement(new StringNamespaceElement(elements[i]
                    .toString()));
        }
    }

    /**
     * Construct a namespace from NamespaceElements with default scope token.
     *
     * @param elements
     *            array of NamespaceElements
     */
    public StringNamespace(NamespaceElement[] elements) {
        this(elements, JAVA_NS_TOKEN);
    }

    /*
     * @see Namespace#pushNamespaceElement(NamespaceElement)
     */
    public void pushNamespaceElement(NamespaceElement element) {
        ns.push(element);
    }

    /*
     * @see org.argouml.uml.util.namespace.Namespace#peekNamespaceElement()
     */
    public NamespaceElement peekNamespaceElement() {
        return (NamespaceElement) ns.peek();
    }

    /**
     * Store the next namespace element.
     *
     * @see org.argouml.uml.util.namespace.Namespace#popNamespaceElement()
     * @param element The element to store.
     */
    public void pushNamespaceElement(String element) {
        ns.push(new StringNamespaceElement(element));
    }

    /*
     * @see org.argouml.uml.util.namespace.Namespace#popNamespaceElement()
     */
    public NamespaceElement popNamespaceElement() {
        return (NamespaceElement) ns.pop();
    }

    /*
     * @see org.argouml.uml.util.namespace.Namespace#getBaseNamespace()
     */
    public Namespace getBaseNamespace() {
        StringNamespace result = null;
        try {
            result = (StringNamespace) this.clone();
        } catch (CloneNotSupportedException e) {
            LOG.debug(e);
            return null;
        }
        result.popNamespaceElement();
        return result;
    }

    /*
     * @see org.argouml.uml.util.namespace.Namespace#getCommonNamespace(
     *         org.argouml.uml.util.namespace.Namespace)
     */
    public Namespace getCommonNamespace(Namespace namespace) {
        Iterator i = iterator();
        Iterator j = namespace.iterator();
        StringNamespace result = new StringNamespace(token);

        for (; i.hasNext() && j.hasNext();) {
            NamespaceElement elem1 = (NamespaceElement) i.next();
            NamespaceElement elem2 = (NamespaceElement) j.next();
            if (elem1.toString().equals(elem2.toString())) {
                result.pushNamespaceElement(elem1);
            }
        }

        return result;
    }

    /*
     * @see org.argouml.uml.util.namespace.Namespace#iterator()
     */
    public Iterator iterator() {
        return ns.iterator();
    }

    /*
     * @see org.argouml.uml.util.namespace.Namespace#isEmpty()
     */
    public boolean isEmpty() {
        return ns.isEmpty();
    }

    /*
     * @see org.argouml.uml.util.namespace.Namespace#setDefaultScopeToken(java.lang.String)
     */
    public void setDefaultScopeToken(String theToken) {
        this.token = theToken;
    }

    /**
     * parse a fully qualified namespace to create a Namespace object.
     *
     * @param fqn
     *            string representation of namespace
     * @param token
     *            the token of the namespace
     * @return a namespace object
     */
    public static Namespace parse(String fqn, String token) {

        String myFqn = fqn;
        StringNamespace sns = new StringNamespace(token);
        int i = myFqn.indexOf(token);
        while (i > -1) {
            sns.pushNamespaceElement(myFqn.substring(0, i));
            myFqn = myFqn.substring(i + token.length());
            i = myFqn.indexOf(token);
        }
        if (myFqn.length() > 0) {
            sns.pushNamespaceElement(myFqn);
        }
        return sns;
    }

    /**
     * parse the name of a (java) class.
     *
     * @param c
     *            the class
     * @return the namespace object
     */
    public static Namespace parse(Class c) {
        return parse(c.getName(), JAVA_NS_TOKEN);
    }

    /*
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return toString(JAVA_NS_TOKEN).hashCode();
    }

    /**
     * Two namespaces are equal when they are namespaces and have the same
     * string representation.
     *
     * @param namespace
     *            the namespace to compare with
     * @return true if equal
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object namespace) {
        if (namespace instanceof Namespace) {
            String ns1 = this.toString(JAVA_NS_TOKEN);
            String ns2 = ((Namespace) namespace).toString(JAVA_NS_TOKEN);
            return ns1.equals(ns2);
        }
        return false;
    }

    /*
     * @see org.argouml.uml.util.namespace.Namespace#toString(java.lang.String)
     */
    public String toString(String theToken) {
        StringBuffer result = new StringBuffer();
        Iterator i = ns.iterator();
        while (i.hasNext()) {
            result.append(i.next());
            if (i.hasNext()) {
                result.append(theToken);
            }
        }
        return result.toString();
    }

    /*
     * Create a string representation using the default scope token.
     *
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return toString(token);
    }

}
