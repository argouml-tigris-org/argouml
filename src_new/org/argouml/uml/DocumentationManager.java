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

  public static String getDocs(Object o, String indent) {
    /*
     * Added 2001-10-05 STEFFEN ZSCHALER
     */
    String sResult = defaultFor(o,indent);

    if (o instanceof MModelElement) {
      Collection tValues = ((MModelElement) o).getTaggedValues();
      if (!tValues.isEmpty()) {
        Iterator iter = tValues.iterator();
        while(iter.hasNext()) {
          MTaggedValue tv = (MTaggedValue)iter.next();
          String tag = tv.getTag();
          if (tag.equals("documentation") || tag.equals("javadocs")) {
            sResult = tv.getValue();
            if (tag.equals("documentation")) break; // give priority to "documentation"
          }
        }
      }
    }

    /*
     * Removed final return 2001-10-05 STEFFEN ZSCHALER
     *
     * Was:
     *
    return defaultFor(o);
     *
     */

    /*
     * Added 2001-10-05 STEFFEN ZSCHALER
     *
     * Add comment signature.
     */
    if (sResult != null) {
      sResult = "/** " + sResult;

      for (int nNewLinePos = sResult.indexOf ('\n');
           nNewLinePos >= 0;
           nNewLinePos = sResult.indexOf ('\n', nNewLinePos + 1)) {
        sResult = sResult.substring (0, nNewLinePos + 1) +
                  indent + " *  " + sResult.substring (nNewLinePos + 1);
      }

      return sResult + '\n' + indent + " */";
    }
    else {
      return "(No comment)";
    }
  }

  public static void setDocs(Object o, String s) {
	  ((MModelElement)o).setTaggedValue("documentation", s);
  }

  /**
   * Determine whether documentation is associated with the given element or not.
   *
   * Added 2001-10-05 STEFFEN ZSCHALER for use by org.argouml.language.java.generator.CodeGenerator
   *
   */
  public static boolean hasDocs (Object o) {
    if (o instanceof MModelElement) {
      Collection tValues = ((MModelElement) o).getTaggedValues();

      if (! tValues.isEmpty()) {
        for (Iterator i = tValues.iterator(); i.hasNext();) {
          MTaggedValue tv = (MTaggedValue) i.next();
          String tag = tv.getTag();
          String value = tv.getValue();
          if ((tag.equals("documentation") || tag.equals("javadocs"))
              && value != null && value.trim().length() > 0) {
            return true;
          }
        }
      }
    }
    return false;
  }

  ////////////////////////////////////////////////////////////////
  // default documentation

  public static String defaultFor(Object o, String indent) {
    if (o instanceof MClass) {
      /*
       * Changed 2001-10-05 STEFFEN ZSCHALER
       *
       * Was (space added below!):
       *
      return
	"/** A class that represents ...\n"+
	" * \n"+
	" * @see OtherClasses\n"+
	" * @author your_name_here\n"+
	" * /";
       *
       */
      return " A class that represents ...\n\n" +
             indent + " @see OtherClasses\n" +
             indent + " @author your_name_here";
    }
    if (o instanceof MAttribute) {
      /*
       * Changed 2001-10-05 STEFFEN ZSCHALER
       *
       * Was (space added below!):
       *
      return
	"/** An attribute that represents ...\n"+
	" * /";
       *
       */
      return " An attribute that represents ...";
    }

    if (o instanceof MOperation) {
      /*
       * Changed 2001-10-05 STEFFEN ZSCHALER
       *
       * Was (space added below!):
       *
      return
	"/** An operation that does ...\n"+
	" * \n"+
	" * @param firstParamName  a description of this parameter\n"+
	" * /";
       *
       */
      return " An operation that does...\n\n" +
             indent + " @param firstParam a description of this parameter";
    }
    if (o instanceof MInterface) {
      /*
       * Changed 2001-10-05 STEFFEN ZSCHALER
       *
       * Was (space added below!):
       *
      return
	"/** A interface defining operations expected of ...\n"+
	" * \n"+
	" * @see OtherClasses\n"+
	" * @author your_name_here\n"+
	" * /";
       *
       */
      return " A interface defining operations expected of ...\n\n" +
             indent + " @see OtherClasses\n" +
             indent + " @author your_name_here";
    }
    if (o instanceof MModelElement) {
      /*
       * Changed 2001-10-05 STEFFEN ZSCHALER
       *
       * Was (space added below!):
       *
      return
	"/**\n"+
	" * \n"+
	" * /";
       *
       */
      return "\n";
    }

    /*
     * Changed 2001-10-05 STEFFEN ZSCHALER
     *
     * Was:
    return "(No documentation)";
     *
     */
    return null;
  }


  ////////////////////////////////////////////////////////////////
  // comments

  /**
   * Get the comments (the notes in a diagram) for a modelelement.
   */
  public static String getComments(Object o) {
      StringBuffer result = new StringBuffer();

      if(o instanceof MModelElement) {
	  Collection comments = ((MModelElement) o).getComments();
	  if (!comments.isEmpty()) {
	    for(Iterator iter = comments.iterator(); iter.hasNext(); ) {
		  MComment c = (MComment)iter.next();
		  String s = c.getName().trim();
		  if (s != null && s.length() > 0) {
		    if(result.length() > 0) {
		       result.append("\n");
		    }
		    result.append(s);
	      }
	    }
      }
      }

      // If there are no comments, just return an empty string.
      if(result.length() == 0)
	  return "";

      // Let every line start with a '*'.
      for(int i=0; i < result.length() - 1; i++) {
	  if(result.charAt(i) == '\n') {
	      result.insert(i+1, " * ");
	  }
      }

      // I add a CR before the end of the comment, so I remove a CR at the
      // end of the last note.
      if(result.charAt(result.length()-1) == '\n') {
	  result.deleteCharAt(result.length()-1);
      }

      return "/*\n * " + result.toString() + "\n */\n";
  }

} /* end class DocumentationManager */





