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

// File: GoUseCaseToExtensionPoint.java
// Classes: GoUseCaseToExtensionPoint
// Original Author: mail@jeremybennett.com
// $Id$

// 16 Apr 2002: Jeremy Bennett (mail@jeremybennett.com). Created to support
// display of extension points in the navigator pane.


package org.argouml.uml.diagram.use_case.ui;

import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

import ru.novosoft.uml.model_management.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.behavior.use_cases.*;

import org.apache.log4j.Category;
import org.argouml.application.api.Argo;
import org.argouml.model.uml.UmlHelper;
import org.argouml.ui.*;
import org.argouml.uml.MMUtil;


/**
 * <p>Provides a rule to display extension points in the navigation pane.</p>
 *
 * @author  16 Apr 2002. Jeremy Bennett (mail@jeremybennett.com).
 */

public class GoUseCaseToExtensionPoint extends AbstractGoRule {
    protected static Category cat = Category.getInstance(GoUseCaseToExtensionPoint.class);


    /**
     * <p>Give a name to this rule.</p>
     *
     * @return  The name of the rule ("<code>Use Case->Extension
     *          Point</code>"). 
     */

    public String getRuleName() {
        return Argo.localize ("Tree", "Use Case->Extension Point");
    }

    public Collection getChildren(Object parent) { 
        if (parent instanceof MUseCase) {
            return ((MUseCase)parent).getExtensionPoints();
        }
        return null;
    }



    /**
     * <p>Test if the given object is a leaf from a use case.</p>
     *
     * @param node  The node to test.
     *
     * @return      <code>false</code> if node is a use case and has
     *              children, <code>true</code> otherwise.
     */

    public boolean isLeaf(Object node) {
        return !((node instanceof MClassifier) && (getChildCount(node) > 0));
    }

}  /* End of class GoUseCaseToExtensionPoint */
