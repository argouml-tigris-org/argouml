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


public class TabDocumentation extends TabText {
  ////////////////////////////////////////////////////////////////
  // constructor
  public TabDocumentation() {
    super("Documentation");
  }

  ////////////////////////////////////////////////////////////////
  // accessors
    protected String genText() {
        String doc = null;
        Object modelObject = _target;
        if(modelObject instanceof MModelElement) {
            doc = ((MModelElement) modelObject).getTaggedValue("documentation");
        }
        if(doc == null) {
	    doc = "";		
        }
        return doc;
    }

    protected void parseText(String s) {
        Object modelObject = _target;
        if(modelObject instanceof MModelElement) {
            MModelElement element = (MModelElement) modelObject;
            if(s != null && s.length() > 0) {
                element.setTaggedValue("documentation",s);
            }
            else {
                String prev = element.getTaggedValue("documentation");
                if(prev != null) {
                    element.setTaggedValue("documentation",s);
                }
            }
        }
    }

    public boolean shouldBeEnabled() {
        return _target instanceof MModelElement;
    }
} /* end class TabDocumentation */
