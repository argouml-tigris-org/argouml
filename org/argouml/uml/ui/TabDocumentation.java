// Copyright (c) 1996-99 The Regents of the University of California. All
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

import org.argouml.ui.*;
import ru.novosoft.uml.foundation.core.*;
import javax.swing.*;

public class TabDocumentation extends PropPanel {

    ////////////////////////////////////////////////////////////////
  // constructor
  public TabDocumentation() {
    super("Documentation",2);
        addCaption("Author:",1,0,0);
        addField(new UMLTextField(this,new UMLTaggedTextProperty("author")),1,0,0);

        addCaption("Version:",2,0,0);
        addField(new UMLTextField(this,new UMLTaggedTextProperty("version")),2,0,0);

        addCaption("Since:",3,0,0);
        addField(new UMLTextField(this,new UMLTaggedTextProperty("since")),3,0,0);

        addCaption("Deprecated:",4,0,0);
        addField(new UMLCheckBox("",this,new UMLTaggedBooleanProperty("deprecated")),4,0,0);

        addCaption("See:",5,0,1);
        addField(new UMLTextArea(this,new UMLTaggedTextProperty("see")),5,0,1);

        addCaption("Documentation:",0,1,1);
        UMLTextArea _doc = new UMLTextArea(this,new UMLTaggedTextProperty("documentation"));
        _doc.setLineWrap(true);
        _doc.setWrapStyleWord(true);
        addField(_doc,0,1,1);


  }

    public boolean shouldBeEnabled() {
        Object target = getTarget();
        return target instanceof MModelElement;
    }

    protected boolean isAcceptibleBaseMetaClass(String baseClass) {
        return false;
    }


} /* end class TabDocumentation */
