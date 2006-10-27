// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
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

//$Id$

package org.argouml.application.api;

import javax.swing.JComponent;
import java.util.Vector;

import org.argouml.kernel.Project;
import org.argouml.uml.reveng.Import;
import org.argouml.uml.reveng.DiagramInterface;
import org.argouml.uml.diagram.static_structure.layout.ClassdiagramLayouter;
import org.argouml.uml.diagram.ui.UMLDiagram;


/**
 * An interface which identifies an ArgoUML plug-in to the Import.
 * Plug-ins are replacements or additions to standard Argo classes.
 *
 * @author Alexander Lepekhine
 * @since 0.13.4
 * @deprecated by Linus Tolke (0.21.1 March 2006).
 *         Call registration in {@link org.argouml.uml.reveng.Import} from
 *         {@link org.argouml.moduleloader.ModuleInterface#enable()}.
 *         The needed registration is not currently available. Add it first!
 */
public interface PluggableImport extends Pluggable {

    /**
     * Create chooser for objects we are to import.
     * Chooser must have a button for object selection
     * and optionally a button for cancel action.
     * To close dialog window use importElement.disposeDialog().
     *
     * @param importElement The current import session.
     * @return The panel to show in import dialog.
     */
    JComponent getChooser(Import  importElement);

    /**
     * Provide pannel added to JTabbedPane after general panel.
     *
     * @return the panel with configuration info for plugin
     *         or null if no parameters are needed.
     */
    JComponent getConfigPanel();

    /**
     * This method returns a Vector with objects to import.
     * These objects are selected with chooser and may be
     * rearranged in arbitrary order.
     * @param importElement - current import session
     * @return vector of objects, selected by chooser
     */
    Vector getList(Import importElement);

    /**
     * Tells if the object is parseable or not. Must match with files that are
     * actually parseable.
     * 
     * @param f
     *            object to be tested.
     * @return true if parseable, false if not.
     */
    boolean isParseable(Object f);

    /**
     * One parseable object from the list will be parsed by this method.
     * Objects will be parsed in order defined by getList().
     * @param p - the current project
     * @param o - object to be parsed
     * @param diagram - current class diagram when Import was invoked
     * @param importElement - current import session. Use this object to get
     * common settings.
     * @throws Exception (all kinds)
     */
    void parseFile(Project p, Object o,
		   DiagramInterface diagram, Import importElement)
	throws Exception;

    /**
     * Provide layout for modified or created class diagram.
     * @param diagram to layout.
     * @return the layouter.
     */
    ClassdiagramLayouter getLayout(UMLDiagram diagram);


}
