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

package org.argouml.uml;

import java.util.*;
import java.beans.*;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.model_management.*;

public class DocumentationManager {

  public static String getDocs(Object o) {
    if (o instanceof MModelElementImpl) {
      Collection tValues = ((MModelElement) o).getTaggedValues();
      if (!tValues.isEmpty()) {
		  Iterator iter = tValues.iterator();
		  while(iter.hasNext()) {
			  MTaggedValue tv = (MTaggedValue)iter.next();
			  if ("javadocs".equals(tv.getTag()))
				  return tv.getValue();
		  }
      }
      else return defaultFor(o);
    }
    return defaultFor(o);
  }

  public static void setDocs(Object o, String s) {
	  ((MModelElement)o).setTaggedValue("javadocs", s);
  }

  ////////////////////////////////////////////////////////////////
  // default documentation

  public static String defaultFor(Object o) {
    if (o instanceof MClass) {
      return
	"/** A class that represents ...\n"+
	" * \n"+
	" * @see OtherClasses\n"+
	" * @author your_name_here\n"+
	" */";
    }
    if (o instanceof MAttribute) {
      return
	"/** An attribute that represents ...\n"+
	" */";
    }

    if (o instanceof MOperation) {
      return
	"/** An operation that does ...\n"+
	" * \n"+
	" * @param firstParamName  a description of this parameter\n"+
	" */";
    }
    if (o instanceof MInterface) {
      return
	"/** A interface defining operations expected of ...\n"+
	" * \n"+
	" * @see OtherClasses\n"+
	" * @author your_name_here\n"+
	" */";
    }
    if (o instanceof MModelElement) {
      return
	"/**\n"+
	" * \n"+
	" */";
    }
    return "(No documentation)";
  }

} /* end class DocumentationManager */
