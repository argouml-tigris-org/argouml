package org.argouml.util;

import java.io.File;
import javax.swing.filechooser.*;

public class SuffixFilter extends FileFilter {

  ////////////////////////////////////////////////////////////////
  // instance varaibles

  public final String _suffix;
  public final String _desc;

  ////////////////////////////////////////////////////////////////
  // constructor

  public SuffixFilter(String s, String d) {
    _suffix = s;
    _desc = d;
  }

  ////////////////////////////////////////////////////////////////
  // FileFilter API

  public boolean accept(File f) {
    if (f == null) return false;
    if (f.isDirectory()) return true;
    String extension = getExtension(f);
    if (_suffix.equalsIgnoreCase(extension)) return true;
    return false;
  }

  public static String getExtension(File f) {
    if (f == null) return null;
    return getExtension(f.getName());
  }

  public static String getExtension(String filename) {
    int i = filename.lastIndexOf('.');
    if (i>0 && i<filename.length()-1) {
      return filename.substring(i+1).toLowerCase();
    }
    return null;
  }

  public String getDescription() {
    return _desc + " (*." + _suffix + ")";
  }

} /* end class SuffixFilter */
