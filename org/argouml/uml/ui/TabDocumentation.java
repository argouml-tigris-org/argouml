// $Id$
// Copyright (c) 1996-2002 The Regents of the University of California. All
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

package org.argouml.uml.ui;

import org.argouml.application.api.*;
import org.argouml.ui.*;
import ru.novosoft.uml.foundation.core.*;
import javax.swing.*;

/** This the tab in the details pane for documentation.
 * It has been tested for saving and does work.
 * questions regarding where the data is stored i.e. author etc.
 * edit>settings... and setting 'global' author does not affect the author
 * field in the documentation tab
 * Enabling output from the documentation fields when generating code as embedded
 * in javadocs and for html/ diagram creation is considered important by users
 *
 * When importing sources, already saved javadoc statements
 * are not automatically added to the documenation Jtext window.
 * When Adding notes to classes the notes are not included in
 * the documentation text window.
 * Since: field is not validated for real date. change
 * to DateField?
 *
 * Note all fields in the TabDocumentation are
 * added automatically to the tagged value tab
 * view.
 */
public class TabDocumentation extends PropPanel {

  private static final String BUNDLE = "Cognitive";
    ////////////////////////////////////////////////////////////////
  
    /**
     * Construct new documentation tab
     * @todo convert to use LabelledLayout(Bob Tarling)
     */
    public TabDocumentation() {
        super("tab.documentation", null, 2); // Change this to call labelled layout constructor
        addCaption(Argo.localize(BUNDLE, "docpane.label.author") + ":",1,0,0);
        addField(new UMLTextField(this,new UMLTaggedTextProperty("author")),1,0,0);
        //unknown where this information is stored; it does not go to myproject.argo (xml file)
        addCaption(Argo.localize(BUNDLE, "docpane.label.version") + ":",2,0,0);
        addField(new UMLTextField(this,new UMLTaggedTextProperty("version")),2,0,0);

        addCaption(Argo.localize(BUNDLE, "docpane.label.since") + ":",3,0,0);
        addField(new UMLTextField(this,new UMLTaggedTextProperty("since")),3,0,0);

        addCaption(Argo.localize(BUNDLE, "docpane.label.deprecated") + ":",4,0,0);
        addField(new UMLCheckBox("",this,new UMLTaggedBooleanProperty("deprecated")),4,0,0);

        addCaption(Argo.localize(BUNDLE, "docpane.label.see") + ":",5,0,1);
        UMLTextArea _see = new UMLTextArea(this,new UMLTaggedTextProperty("see"));
        _see.setLineWrap(true);
        _see.setWrapStyleWord(true);
        JScrollPane spSee = new JScrollPane();
        spSee.getViewport().add(_see);
        addField(spSee,5,0,1);

        addCaption(Argo.localize(BUNDLE, "docpane.label.documentation") + ":",0,1,1);
        UMLTextArea _doc = new UMLTextArea(this,new UMLTaggedTextProperty("documentation"));
        _doc.setLineWrap(true);
        _doc.setWrapStyleWord(true);
        JScrollPane spDocs = new JScrollPane();
        spDocs.getViewport().add(_doc);
        addField(spDocs,0,1,1);
    }

    public boolean shouldBeEnabled() {
        Object target = getTarget();
        return target instanceof MModelElement;
    }

} /* end class TabDocumentation */
