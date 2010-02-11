/* $Id$
 *****************************************************************************
 * Copyright (c) 2010 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    euluis
 *****************************************************************************
 */

package org.argouml.profile.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

/**
 * Unit tests for {@link DependencyResolver}.
 * @author Luis Sergio Oliveira (euluis)
 */
public class TestDependencyResolver extends TestCase {
    private DependencyResolver<String> resolver;
    private DummyDependencyChecker checker;

    /**
     * @throws Exception
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * Test invoke of {@link DependencyResolver#resolve()} method without any
     * arguments.
     */
    public void testResolveNoArgs() {
        DependencyChecker<String> checker2 = new DependencyChecker<String>() {
            public boolean check(String item) {
                return true;
            }
        };
        resolver = new DependencyResolver<String>(checker2);
        resolver.resolve();
        assertTrue("There should be no unresolved items.",
            resolver.unresolvedItems.isEmpty());
    }

    /**
     * Test {@link DependentString#equals(Object)}.
     */
    public void testDependentStringEquals() {
        // this is neat...
        assertEquals(new DependentString("A"), "A");
        // but, lookout, it doesn't work both ways
        assertFalse("A".equals(new DependentString("A")));
    }

    /**
     * Test the resolution of several items all without any dependencies.
     */
    public void testResolveWithAllResolvableDependencies() {
        final Collection<DependentString> dependentItems =
            new ArrayList<DependentString>() { {
                add(new DependentString("A"));
                add(new DependentString("B"));
                add(new DependentString("C"));
        } };
        checker = new DummyDependencyChecker(dependentItems);
        resolver = new DependencyResolver<String>(checker);
        final Collection<String> items = new ArrayList<String>() { {
            for (DependentString dependentItem : dependentItems) {
                add(dependentItem.theString);
            }
        } };
        resolver.resolve(items);
        assertEquals(3, checker.calls);
        assertTrue("All the items should have been resolved.",
            checker.resolved.containsAll(items));
        assertTrue("There should be no unresolved items.",
            resolver.unresolvedItems.isEmpty());
    }

    /**
     * Test the resolution of items which are all resolvable, but, handed in
     * inverted order of their resolution:
     * A -> {B, C}, B -> {C} and C -> {}
     */
    public void testResolveWithResolvableItemsButHandedInInvertedOrder() {
        final Collection<DependentString> dependentItems =
            new ArrayList<DependentString>() { {
                add(new DependentString("A", new HashSet<String>()  { {
                    add("B"); add("C"); } }));
                add(new DependentString("B", new HashSet<String>() { {
                    add("C"); } }));
                add(new DependentString("C"));
        } };
        checker = new DummyDependencyChecker(dependentItems);
        resolver = new DependencyResolver<String>(checker);
        Collection<String> items = new ArrayList<String>() { {
            for (DependentString dependentItem : dependentItems) {
                add(dependentItem.theString);
            }
        } };
        resolver.resolve(items);
        assertTrue("All the items should have been resolved.",
            checker.resolved.containsAll(items));
        assertTrue("There should be no unresolved items.",
            resolver.unresolvedItems.isEmpty());
    }

    /**
     * Test the resolution of items which are all resolvable, but, handed first
     * partially and not resolvable, then a new call to resolve delivers the
     * solution.
     * First call: A -> {B, C}, B -> {C} and C -> {D}
     * Second call: D -> {}
     */
    public void testResolveWithResolvableItemsButInTwoCalls() {
        final Collection<DependentString> dependentItems =
            new ArrayList<DependentString>() { {
                add(new DependentString("C", new HashSet<String>() { {
                    add("D"); } }));
                add(new DependentString("D"));
        } };
        checker = new DummyDependencyChecker(dependentItems);
        resolver = new DependencyResolver<String>(checker);
        Collection<String> items1 = new ArrayList<String>() { { add("C"); } };
        resolver.resolve(items1);
        assertEquals("No item should have been resolved.", 0,
            checker.resolved.size());
        Collection<String> items2 = new ArrayList<String>() { { add("D"); } };
        resolver.resolve(items2);
        assertTrue("All the items should have been resolved.",
            checker.resolved.containsAll(items1)
            && checker.resolved.containsAll(items2));
        assertTrue("There should be no unresolved items.",
            resolver.unresolvedItems.isEmpty());
    }

    /**
     * Test the resolution of items which are not resolvable at first, but,
     * which after injecting the dependency, will afterwards be resolved with
     * the non-arguments resolve being invoked.
     * <ol>
     * <li>resolve(X -> {Z, Y}, Z -> {Y}) &rarr; nothing resolved</li>
     * <li>{@link DependencyResolver#resolve()} call &rarr; nothing
     * resolved</li>
     * <li>inject Y as resolved</li>
     * <li>{@link DependencyResolver#resolve()} call &rarr; X and Z
     * resolved</li>
     * </ol>
     */
    public void testResolveWithSolutionInjectedByThirdParty() {
        final Collection<DependentString> dependentItems =
            new ArrayList<DependentString>() { {
                add(new DependentString("X", new HashSet<String>() { {
                    add("Z"); add("Y"); } }));
                add(new DependentString("Z", new HashSet<String>() { {
                    add("Y"); } }));
        } };
        checker = new DummyDependencyChecker(dependentItems);
        resolver = new DependencyResolver<String>(checker);
        Collection<String> items = new ArrayList<String>() { { 
            add("X"); add("Z"); } };
        resolver.resolve(items);
        assertEquals("No item should have been resolved.", 0,
            checker.resolved.size());
        resolver.resolve();
        assertEquals("No item should have been resolved.", 0,
            checker.resolved.size());
        checker.resolved.add("Y");
        resolver.resolve();
        assertTrue("All the items should have been resolved.",
            checker.resolved.containsAll(items));
        assertTrue("There should be no unresolved items.",
            resolver.unresolvedItems.isEmpty());
    }
}

class DependentString {
    String theString;
    Set<String> dependencies;

    DependentString(String s, Set<String> dependencies) {
        theString = s;
        this.dependencies = dependencies;
        if (dependencies == null) {
            this.dependencies = new HashSet<String>();
        }
    }

    DependentString(String s) {
        this(s, new HashSet<String>());
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        return theString.equals(obj);
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return theString.hashCode();
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String dependency : dependencies) {
            sb.append(dependency);
            sb.append(", ");
        }
        return theString + " -> {" + sb.toString() + "}";
    }
}

class DummyDependencyChecker implements DependencyChecker<String> {
    Collection<String> items;
    Collection<DependentString> dependentItems;
    Collection<String> resolved = new HashSet<String>();
    int calls = 0;

    DummyDependencyChecker(final Collection<DependentString> items) {
        this.items = new ArrayList<String>() { {
            for (DependentString item : items) {
                add(item.theString);
            }
        } };
        this.dependentItems = items;
    }

    /**
     * @see org.argouml.profile.internal.DependencyChecker#check(java.lang.Object)
     */
    public boolean check(String item) {
        calls++;
        if (items.contains(item)) {
            DependentString item2Check = null;
            for (DependentString theItem : dependentItems) {
                if (theItem.equals(item)) {
                    item2Check = theItem;
                    break;
                }
            }
            if (resolved.containsAll(item2Check.dependencies)) {
                resolved.add(item2Check.theString);
                return true;
            }
        }
        return false;
    }
}
