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
    if (o instanceof uci.uml.Foundation.Core.Class) {
      return
	"/** A class that represents ...\n"+
	" * \n"+
	" * @see OtherClasses\n"+
	" * @author your_name_here\n"+
	" */";
    }
    if (o instanceof uci.uml.Foundation.Core.Attribute) {
      return
	"/** An attribute that represents ...\n"+
	" */";
    }

    if (o instanceof uci.uml.Foundation.Core.Operation) {
      return
	"/** An operation that does ...\n"+
	" * \n"+
	" * @param firstParamName  a description of this parameter\n"+
	" */";
    }
    if (o instanceof uci.uml.Foundation.Core.Interface) {
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
