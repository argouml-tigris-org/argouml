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



package org.argouml.xml;

import org.xml.sax.*;
import java.io.*;


/**
 * @author Piotr Kaminski
 */
public class DTDEntityResolver implements EntityResolver {

  ////////////////////////////////////////////////////////////////
  // constants

  public static String DTD_DIR = "/org/argouml/xml/dtd/";
  public static final DTDEntityResolver SINGLETON = new DTDEntityResolver();

  ////////////////////////////////////////////////////////////////
  // constructor

  protected DTDEntityResolver() { super(); }

  ////////////////////////////////////////////////////////////////
  // methods

  public InputSource resolveEntity(String publicId, String systemId)
       throws java.io.IOException, SAXException {
    try {
      java.net.URL url = new java.net.URL(systemId);
      try {
	InputSource source = new InputSource(url.openStream());
	if (publicId != null) source.setPublicId(publicId);
	return source;
      }
      catch (java.io.IOException e) {
	// failed to get from normal system ID, try to get from
	// our own DTD directory
	if (systemId.endsWith(".dtd")) {
	  int i = systemId.lastIndexOf('/');
	  i++;	// go past '/' if there, otherwise advance to 0
	  java.io.InputStream is;
	  is = getClass().getResourceAsStream(DTD_DIR + systemId.substring(i));
          if(is == null) {
             try {
              is = new FileInputStream(DTD_DIR.substring(1) + systemId.substring(i));
             }
             catch(Exception ex) {}
          }
          if (is != null) return new InputSource(is);
	}
	// if we failed to get an alternative, rethrow the original exception
	throw e;
      }
    } // outer try
    catch (java.net.MalformedURLException e2) { }
    return null;
  }

} /* end class DTDEntityResolver */
