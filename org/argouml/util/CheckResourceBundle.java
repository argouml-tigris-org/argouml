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
import java.util.*;
import java.io.*;

/**
 *   This class can be temporarily made the base class of a
 *   ListResourceBundle derived class to capture any missing resource names.
 *
 *   Any missing resource names will be written to a file in the
 *   default directory with a file name from the bundle class name
 *   (including locale) + ".log"
 *
 *   Each missing entry causes the log file to be reopened, appended and
 *   closed, so this class should not be used in a production version.
 *
 *   @author Curt Arnold
 *   @since 0.9
 *   @see java.util.ListResourceBundle
 *   @see org.argouml.uml.ui.UMLResourceBundle
 *   @see org.argouml.ui.MenuResourceBundle
 */
abstract public class CheckResourceBundle extends ResourceBundle {

    private String _fileName = null;
    private Set _missing = new HashSet();

    public CheckResourceBundle() {
        //
        //  check that no key is entered twice
        //
        Set set = new HashSet();
        Object[][] contents = getContents();
        PrintWriter writer = null;
        boolean openFailed = false;
        for(int i = 0; i < contents.length; i++) {
            if(!set.add(contents[i][0])) {
                if(writer == null && !openFailed) {
                    String className = getClass().getName();
                    File file = new File(className + ".log");
                    System.out.println("Duplicate keys in " + className +
                        " logged in " + file.getAbsolutePath());
                    try {
                        writer = new PrintWriter(new FileOutputStream(file));
                    }
                    catch(Exception e) {
                        e.printStackTrace();
                        openFailed = true;
                    }
                }
                if(writer != null) {
                    writer.println("Duplicate key:" + ((String) contents[i][0]));
                }
            }
        }
        if(writer != null) {
            writer.close();
        }
    }

    protected abstract Object[][] getContents();

    final public Enumeration getKeys() {
        Object[][] contents = getContents();
        Vector keys = new Vector(contents.length/2);
        for(int i = 0; i < contents.length; i+=2) {
            keys.add(contents[i]);
        }
        return keys.elements();
    }


    final public Object handleGetObject(String key) {
        Object[][] contents = getContents();
        Object[] entry;
        Object obj = null;
        for(int i = 0; i < contents.length; i++) {
            entry = contents[i];
            if(key.equals(entry[0])) {
                obj = entry[1];
            }
        }

        //
        //   if there was not a match for the key
        //      and the key has not been encountered before
        //      append an entry to the log file
        if(obj == null && _missing.add(key)) {
            boolean append = !(_fileName == null);
            if(!append) {
                String className = getClass().getName();
                _fileName = className + ".log";
                System.out.println("Missing resources for " + className +
                    " logged in " + new File(_fileName).getAbsolutePath());
            }
            try {
                FileOutputStream log = new FileOutputStream(_fileName,append);
                new PrintStream(log).println("            {\"" + key + "\", \"" + key + "\" },  // key not found");
                log.close();
            }
            catch(Exception e) {
                if(!append) {
                    System.out.println("Could not open file " + new File(_fileName).getAbsolutePath());
                    e.printStackTrace();
                    System.out.println("Writing missing keys to console");
                }
                System.out.println(getClass().getName() + " missing key " + key);
            }
        }
        return obj;
    }
}
