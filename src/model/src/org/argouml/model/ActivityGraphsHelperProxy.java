// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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
 * A proxy onto a specific implementation of the ActivityGraphsHelper.
 * The proxy is responsible for any framework processing
 * before and after the implementation is called.
 * For the current implementation that is to generate mementos for any
 * mutable methods.
 * @author Bob Tarling
 */
public class ActivityGraphsHelperProxy implements ActivityGraphsHelper {

    /**
     * The component.
     */
    private ActivityGraphsHelper impl;

    /**
     * @param component The component to decorate.
     */
    public ActivityGraphsHelperProxy(ActivityGraphsHelper component) {
        impl = component;
    }

    /**
     * @see org.argouml.model.ActivityGraphsHelper#findClassifierByName(java.lang.Object,
     *      java.lang.String)
     */
    public Object findClassifierByName(Object ofs, String s) {
        return impl.findClassifierByName(ofs, s);
    }

    /**
     * @see org.argouml.model.ActivityGraphsHelper#findStateByName(java.lang.Object,
     *      java.lang.String)
     */
    public Object findStateByName(Object c, String s) {
        return impl.findStateByName(c, s);
    }

    /**
     * @see org.argouml.model.ActivityGraphsHelper#isAddingActivityGraphAllowed(java.lang.Object)
     */
    public boolean isAddingActivityGraphAllowed(Object context) {
        return impl.isAddingActivityGraphAllowed(context);
    }

    /**
     * @see org.argouml.model.ActivityGraphsHelper#addInState(java.lang.Object,
     *      java.lang.Object)
     */
    public void addInState(Object classifierInState, Object state) {
        impl.addInState(classifierInState, state);
    }
}
