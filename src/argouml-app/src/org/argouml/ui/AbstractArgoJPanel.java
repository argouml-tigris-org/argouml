// $Id: AbstractArgoJPanel.java 14168 2008-02-29 16:44:07Z mvw $
// Copyright (c) 1996-2008 The Regents of the University of California. All
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

package org.argouml.ui;

/**
 * This exists purely for the backwards compatibility of modules. New code
 * should use {@link org.argouml.application.api.AbstractArgoJPanel}.
 * 
 * @deprecated by Bob Tarling in 0.25.5. Use
 *             {@link org.argouml.application.api.AbstractArgoJPanel}.
 *             <p>
 *             Because this is a widely used class it is recommended to retain
 *             the deprecated class for at least two major releases (ie until
 *             after 0.28 is released using the current numbering scheme).
 */
@Deprecated
public abstract class AbstractArgoJPanel extends
        org.argouml.application.api.AbstractArgoJPanel {
    /**
     * The constructor.
     *
     */
    @Deprecated
    public AbstractArgoJPanel() {
        super();
    }

    /**
     * The constructor.
     *
     * @param title The name as a localized string.
     */
    @Deprecated
    public AbstractArgoJPanel(String title) {
        super(title);
    }

    /**
     * The constructor.
     *
     * @param title The name (a localized string).
     * @param t if true, remove tab from parent JTabbedPane
     */
    @Deprecated
    public AbstractArgoJPanel(String title, boolean t) {
        super(title, t);
    }
}
