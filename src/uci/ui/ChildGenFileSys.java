package uci.ui;

import java.util.*;
import uci.util.*;
import java.io.*;

public class ChildGenFileSys implements ChildGenerator {

  public Enumeration gen(Object o) {
    if (o instanceof File) {
      File f = (File) o;
      if (f.isDirectory()) {
	String[] l = f.list();
	if (l != null) {
	  Vector v = new Vector();
	  for (int ii = 0; ii < l.length; ii++) {
            File d = new File(f.getAbsolutePath() + File.separator + l[ii]);
	    v.addElement(l[ii]);
	  }
	  return v.elements();
	}
      }
    }
    return EnumerationEmpty.theInstance();
  }


  public static void main(String args[]) {
    if (args.length < 1) {
      System.out.println("usage: java ChildGenFileSys path");
      return;
    }
    String path = args[0];
    if (path.endsWith(File.separator) && path.length() > 1)
      path = path.substring(0, path.length() - 1);

    File root = new File(path);

    if (root == null) {
      System.out.println("Directory not found: " + path);
      return;
    }
    ChildGenFileSys cg = new ChildGenFileSys();
    Enumeration children = cg.gen(root);
    while (children.hasMoreElements())
      System.out.println("  " + children.nextElement().toString());

  }
} /* end interface ChildGenerator */

