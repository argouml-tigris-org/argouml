// Copyright (c) 1996-98 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation for educational, research and non-profit
// purposes, without fee, and without a written agreement is hereby granted,
// provided that the above copyright notice and this paragraph appear in all
// copies. Permission to incorporate this software into commercial products
// must be negotiated with University of California. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "as is",
// without any accompanying services from The Regents. The Regents do not
// warrant that the operation of the program will be uninterrupted or
// error-free. The end-user understands that the program was developed for
// research purposes and is advised not to rely exclusively on the program for
// any reason. IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY
// PARTY FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES,
// INCLUDING LOST PROFITS, ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS
// DOCUMENTATION, EVEN IF THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY
// DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
// WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE
// SOFTWARE PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,
// ENHANCEMENTS, OR MODIFICATIONS.




package uci.uml.ui;

import java.util.*;

import uci.uml.Foundation.Core.*;
import uci.uml.Model_Management.*;

public class DocumentationManager {

  public static Hashtable _docs = new Hashtable();
  
  public static String getDocs(Object o) {
    String s = (String) _docs.get(o);
    if (s == null) {
      return defaultFor(o);

    }
    return s;
  }

  public static void setDocs(Object o, String s) {
    if (o == null) return;
    _docs.put(o, s);
  }

  ////////////////////////////////////////////////////////////////
  // default documentation

  public static String defaultFor(Object o) {
    if (o instanceof MMClass) {
      return
	"/** A class that represents ...\n"+
	" * \n"+
	" * @see OtherClasses\n"+
	" * @author your_name_here\n"+
	" */";
    }
    if (o instanceof Attribute) {
      return
	"/** An attribute that represents ...\n"+
	" */";
    }

    if (o instanceof Operation) {
      return
	"/** An operation that does ...\n"+
	" * \n"+
	" * @param firstParamName  a description of this parameter\n"+
	" */";
    }
    if (o instanceof Interface) {
      return
	"/** A interface defining operations expected of ...\n"+
	" * \n"+
	" * @see OtherClasses\n"+
	" * @author your_name_here\n"+
	" */";
    }
    if (o instanceof ModelElement) {
      return
	"/**\n"+
	" * \n"+
	" */";
    }
    return "(No documentation)";
  }
  
} /* end class DocumentationManager */
