// $Id$
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

// File: AbstractSection.java
// Classes: AbstractSection
// Original Author: Marian Heddesheimer

// 09 Feb 2003: Thomas Neustupny (thn@tigris.org), extraced abstract class

package org.argouml.uml.generator;

import java.util.*;
import java.io.*;

import org.apache.log4j.Logger;

/**
 *
 *Reading and writing preserved sections from the code
 *
 * @author  Marian
 */
public abstract class AbstractSection
{
    private static final Logger LOG = 
        Logger.getLogger(AbstractSection.class);

    private Map mAry;

    /** Creates a new instance of Section */
    public AbstractSection() {
        mAry = new HashMap();
        mAry.clear();
    }

    /**
     * @param id
     * @param indent
     * @return
     */
    public static String generate(String id, String indent) {
        return "";
    }

    // write todo:
    // check if sections are not used within the file and put them as comments
    // at the end of the file.
    // hint: use a second Map to compare with the used keys
    // =======================================================================

    public void write(String filename, String indent,
		      boolean outputLostSections)
    {
        try {           
            FileReader f = new FileReader(filename);
            BufferedReader fr = new BufferedReader(f);
            FileWriter fw = new FileWriter(filename + ".out");          
            String line = "";
            while (line != null) {
                line = fr.readLine();
                if (line != null) {
                    String sectionId = getSectId(line);
                    if (sectionId != null) {
                        String content = (String) mAry.get(sectionId);
                        fw.write(line + "\n");
                        if (content != null) {
                            fw.write(content);                        
                        }
                        line = fr.readLine(); // read end section;
                        mAry.remove(sectionId);
                    }
                    fw.write(line + "\n");           
                }
            }
            if ((!mAry.isEmpty()) && (outputLostSections)) {
                fw.write("/* lost code following: \n");
                Set mapEntries = mAry.entrySet();
                Iterator itr = mapEntries.iterator();
                while (itr.hasNext()) {
                    Map.Entry entry = (Map.Entry) itr.next();
                    fw.write(indent + "// section " + entry.getKey()
			     + " begin\n");
                    fw.write((String) entry.getValue());
                    fw.write(indent + "// section " + entry.getKey()
			     + " end\n");
                }
            }
            fr.close();
            fw.close();
        } catch (IOException e) {
            LOG.error("Error: " + e.toString());
        }
    }

    /**
     * @param filename the filename to read from
     */
    public void read(String filename) {
        try {            
            FileReader f = new FileReader(filename);
            BufferedReader fr = new BufferedReader(f);

            String line = "";
            String content = "";
            boolean inSection = false;
            while (line != null) {
                line = fr.readLine();
                if (line != null) {
                    if (inSection) {
                        String sectionId = getSectId(line);
                        if (sectionId != null) {
                            inSection = false;
                            mAry.put(sectionId, content);
                            content = "";
                        } else {
                            content += line + "\n";
                        }
                    } else {
                        String sectionId = getSectId(line);
                        if (sectionId != null) {
                            inSection = true;
                        }
                    }
                }
            }
            fr.close();
        } catch (IOException e) {
            LOG.error("Error: " + e.toString());
        }
    }

    public static String getSectId(String line) {
        final String begin = "// section ";
        final String end1 = " begin";
        final String end2 = " end";
        int first = line.indexOf(begin);
        int second = line.indexOf(end1);
        if (second < 0) {
            second = line.indexOf(end2);
        }
        String s = null;
        if ( (first >= 0) && (second >= 0) ) {
            first = first + new String(begin).length();
            s = line.substring(first, second);
        }
        return s;
    }
}
