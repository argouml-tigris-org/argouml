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



package uci.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;
import javax.swing.*;

/**
 * @author Piotr Kaminski
 */

public class Util {

  /** No public constructor, this is a utility class with all static
   *  methods, so you never need an instance. */
  private Util() {}

  /**
   * Fixes a platform dependent filename to standard URI form.
   *
   * @param str The string to fix.
   *
   * @return Returns the fixed URI string.
   */
  public static final String filenameToURI(String str) {
	// handle platform dependent strings
	str = str.replace(java.io.File.separatorChar, '/');
	// Windows fix
	if (str.length() >= 2) {
	  if (str.charAt(1) == ':') {
	    char ch0 = Character.toUpperCase(str.charAt(0));
	    if (ch0 >= 'A' && ch0 <= 'Z') str = "/" + str;
	  }
	}
	return str;
  }

  public static final URL fileToURL(File file)
       throws MalformedURLException, IOException {
    return new URL("file", "", filenameToURI(file.getCanonicalPath()));
  }

  public static final URL fixURLExtension(URL url, String desiredExtension) {
    if (!url.getFile().endsWith(desiredExtension)) {
      try { url = new URL(url, url.getFile() + desiredExtension); }
      catch (java.net.MalformedURLException e) {
	throw new UnexpectedException(e);
      }
    }
    return url;
  }

  public static ImageIcon loadIconResource(String imgName) {
    return loadIconResource(imgName, imgName);
  }

  public static ImageIcon loadIconResource(String imgName, String desc) {
    ImageIcon res = null;
    try {
      java.net.URL imgURL = Util.class.getResource(imageName(imgName));
      if (imgURL == null) {
	System.out.println("Icon for " + imgName + " not found");
	return null;
      }
      return new ImageIcon(imgURL, desc + " ");
    }
    catch (Exception ex) {
      System.out.println("Exception in loadIconResource");
      ex.printStackTrace();
      return new ImageIcon(desc);
    }
  }

  protected static String imageName(String name) {
    return "/uci/Images/" + stripJunk(name) + ".gif";
  }

  /*
   * Strip all characters out of <var>s</var> that could not be part of a valid
   * Java identifier.  Return either the given string (if all characters were
   * valid), or a new string with all invalid characters stripped out.
   */
  public static final String stripJunk(String s) {
    int len = s.length();
    int pos=0;
    for (int i=0; i<len; i++, pos++) {
      if (!Character.isJavaIdentifierPart(s.charAt(i))) break;
    }
    if (pos == len) return s;

    StringBuffer buf = new StringBuffer(len);
    for (int i=0; i<pos; i++) buf.append(s.charAt(i));

    // skip pos, we know it's not a valid char from above
    for (int i=pos+1; i<len; i++) {
      char c = s.charAt(i);
      if (Character.isJavaIdentifierPart(c)) buf.append(c);
    }
    return buf.toString();
  }

  /**
   * Fixes a platform dependent filename to standard URI form.
   *
   * @param str The string to fix.
   *
   * @return Returns the fixed URI string.
   */
  public static final String URIToFilename(String str) {
    // Windows fix
    if (str.length() >= 3) {
      if (str.charAt(0) == '/' && str.charAt(2) == ':') {
	char ch1 = Character.toUpperCase(str.charAt(1));
	if (ch1 >= 'A' && ch1 <= 'Z') str = str.substring(1);
      }
    }
    // handle platform dependent strings
    str = str.replace('/', java.io.File.separatorChar);
    return str;
  }

  public static final File URLToFile(URL url) throws MalformedURLException {
    if (!"file".equals(url.getProtocol()))
      throw new MalformedURLException("URL protocol must be 'file'.");
    return new File(URIToFilename(url.getFile()));
  }

  public static final String URLToShortName(URL url) {
    String name = url.getFile();
    return name.substring(name.lastIndexOf('/')+1);
  }

} /* end class Util */
