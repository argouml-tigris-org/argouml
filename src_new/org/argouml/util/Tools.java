// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Locale;

import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
import org.argouml.i18n.Translator;

/**
 * Some tools thrown together...
 *
 */
public class Tools {

    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(Tools.class);

    private static final String[] PACKAGELIST =
	new String[]{
	    "org.argouml.application", 
            // TODO: The following is MDR specific.  We need something generic
            // to all Model subsystems - tfm 20070716
	    "org.netbeans.mdr",
            "org.tigris.gef.base", 
            "org.xml.sax",
            "java.lang", 
            "org.apache.log4j",
	};

    private static void getComponentVersionInfo(StringBuffer sb, String pn) {
        sb.append(Translator.localize("label.package")).append(": ");
        sb.append(pn);
        sb.append('\n');
        Package pkg = Package.getPackage(pn);
        if (pkg == null) {
            sb.append(Translator.localize("label.no-version"));
        } else {
            String in = pkg.getImplementationTitle();
            if (in != null) {
                sb.append(Translator.localize("label.component"));
		sb.append(": ");
                sb.append(in);
            }
            in = pkg.getImplementationVendor();
            if (in != null) {
                sb.append(Translator.localize("label.by"));
		sb.append(": ");
                sb.append(in);
            }
            in = pkg.getImplementationVersion();
            if (in != null) {
                sb.append(", ");
		sb.append(Translator.localize("label.version"));
		sb.append(" ");
                sb.append(in);
                sb.append('\n');
            }
        }
        sb.append('\n');
    }

    /**
     * @return a String containing the version information
     */
    public static String getVersionInfo() {
        try {

            // class preloading, so packages are there...
            Class cls = org.tigris.gef.base.Editor.class;
            cls = org.xml.sax.AttributeList.class;
            cls = org.apache.log4j.Logger.class;
            
            // TODO: The following is MDR specific.  We need something generic
            // to all Model subsystems - tfm 20070716
            cls = org.netbeans.api.mdr.MDRManager.class;

            StringBuffer sb = new StringBuffer();

            String saxFactory =
                System.getProperty("javax.xml.parsers.SAXParserFactory");
            if (saxFactory != null) {
                Object[] msgArgs = {
                    saxFactory,
                };
                sb.append(Translator.messageFormat("label.sax-factory1",
                                                   msgArgs));
            }

            Object saxObject = null;
            try {
                saxObject = SAXParserFactory.newInstance();
                Object[] msgArgs = {
                    saxObject.getClass().getName(),
                };
                sb.append(Translator.messageFormat("label.sax-factory2",
                                                   msgArgs));
                sb.append("\n");
            } catch (Exception ex) {
                sb.append(Translator.localize("label.error-sax-factory"));
            }

            for (int i = 0; i < PACKAGELIST.length; i++) {
                getComponentVersionInfo(sb, PACKAGELIST[i]);
            }

            if (saxObject != null) {
                // ...getPackage() can return null's, so we have to
                // cater for this:
                Package pckg = saxObject.getClass().getPackage();
                if (pckg != null) {
                    getComponentVersionInfo(sb, pckg.getName());
                }
            }



            sb.append("\n");
            sb.append(Translator.localize("label.os"));
            sb.append(System.getProperty("os.name", "unknown"));
            sb.append('\n');
            sb.append(Translator.localize("label.os-version"));
            sb.append(System.getProperty("os.version", "unknown"));
            sb.append('\n');
            sb.append(Translator.localize("label.language"));
            sb.append(Locale.getDefault().getLanguage());
            sb.append('\n');
            sb.append(Translator.localize("label.country"));
            sb.append(Locale.getDefault().getCountry());
            sb.append('\n');
            sb.append('\n');


            return sb.toString();

        } catch (Exception e) {
            return e.toString();
        }

    }

    /**
     * Print out some version info for debugging.
     */
    public static void logVersionInfo() {
        BufferedReader r =
            new BufferedReader(new StringReader(getVersionInfo()));

        try {
            while (true) {
                String s = r.readLine();
                if (s == null) {
                    break;
                }
                LOG.info(s);
            }
        } catch (IOException ioe) { }
    }

    /**
     * Gets the file extension of a file.
     *
     * @param file the File to examine
     * @return extension including the dot, or null
     */
    public static String getFileExtension(File file) {
        String ext = null;
        String s = file.getName();
        int i = s.lastIndexOf('.');

        if (i > 0) {
            ext = s.substring(i).toLowerCase();
        }
        return ext;
    }
}

