// Copyright (c) 1996-2001 The Regents of the University of California. All
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

package org.argouml.util;
import org.argouml.application.api.*;
import org.argouml.util.logging.*;
import java.io.*;
import java.util.*;

public class Tools {

    static String packageList[] = new 
String[]{"org.argouml.application","ru.novosoft.uml","org.tigris.gef.base","org.xml.sax","java.lang","org.apache.commons.logging"};

    private static void getComponentVersionInfo(StringBuffer sb, String pn) 
{
	sb.append("Package: ");
	sb.append(pn);
	sb.append('\n');
	Package pkg = Package.getPackage(pn);
	if(pkg == null) {
	    sb.append("-- No Versioning Information --\nMaybe you don't use the jar?\n");
	}
	else {
	    String in = pkg.getImplementationTitle();
	    if(in!=null) {
	        sb.append("Component: ");
	        sb.append(in);
	    }
	    in = pkg.getImplementationVendor();
	    if(in!=null) {
	        sb.append(", by: ");
	        sb.append(in);
	    }
	    in = pkg.getImplementationVersion();
	    if(in!=null) {
	        sb.append(", version: ");
	        sb.append(in);
	        sb.append('\n');
	    }
	}
	sb.append('\n');
    }

    public static String getVersionInfo()
    {
      try{

	// class preloading, so packages are there...
	Class cls = Class.forName("ru.novosoft.uml.MBase");
	cls = Class.forName("org.tigris.gef.base.Editor");
	cls = Class.forName("ru.novosoft.uml.MBase");
	cls = Class.forName("org.xml.sax.AttributeList");
	cls = Class.forName("org.apache.commons.logging.Log");

	StringBuffer sb = new StringBuffer();

	String saxFactory = System.getProperty("javax.xml.parsers.SAXParserFactory");
	if(saxFactory != null) {
	    sb.append("SAX Parser Factory " + saxFactory+ " specified using system property\n");
	}

	Object saxObject = null;
	try {
	    saxObject = javax.xml.parsers.SAXParserFactory.newInstance();
	    sb.append("SAX Parser Factory " +
		       saxObject.getClass().getName() + " will be used.\n");
	    sb.append("\n");
	}
	catch(Exception ex) {
	    sb.append("Error determining SAX Parser Factory\n.");
	}

	for(int i=0;i<packageList.length;i++) {
	    getComponentVersionInfo(sb, packageList[i]);
	}

	if (saxObject != null) {
	    // ...getPackage() can return null's, so we have to cater for this:
	    Package pckg = saxObject.getClass().getPackage();
	    if (pckg != null) {
	    	getComponentVersionInfo(sb, pckg.getName());
            }
	}
      


	sb.append("\n");
	sb.append("Operating System is: ");
	sb.append(System.getProperty("os.name", "unknown"));
	sb.append('\n');
	sb.append("Operating System Version: ");
	sb.append(System.getProperty("os.version", "unknown"));
	sb.append('\n');
	sb.append("Language: ");
	sb.append(Locale.getDefault().getLanguage());
	sb.append('\n');
	sb.append("Country: ");
	sb.append(Locale.getDefault().getCountry());
	sb.append('\n');
	sb.append('\n');


	return sb.toString();

      } catch (Exception e) { return e.toString();}

    }

    public static void logVersionInfo()
    {
      BufferedReader r = new BufferedReader(new StringReader(getVersionInfo()));

      try {
          while (true) {
              String s = r.readLine();
	      if (s == null) break;
	      Console.info(s);
          }
      }
      catch (IOException ioe) { }
    }
}

