// $Id$
// Copyright (c) 2003-2004 The Regents of the University of California. All
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

/**
 * Reading and writing preserved sections from the code
 *
 * @author  Marian
 */
package org.argouml.language.csharp.generator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * This class is used by GeneratorCSharp for handling of code sections.
 */
public class Section {
    private Map mAry;

    private static final String BEGIN = "// section ";
    private static final String END1 = " begin";
    private static final String END2 = " end";

    /** 
     * Creates a new instance of Section.
     */
    public Section() {
        mAry = new HashMap();
        mAry.clear();
    }

    public static String generate(String id, String indent) {
        String s = "";
        s += indent + BEGIN + id + END1 + "\n";
        s += indent + BEGIN + id + END2 + "\n";
        return s;
    }

    /**
     * @param filename The filename to write to.
     * @param indent The indent that we use.
     */
    // TODO:
    // check if sections are not used within the file and put them as comments
    // at the end of the file.
    // hint: use a second Map to compare with the used keys
    // =======================================================================
    public void write(String filename, String indent) {
        try {
            System.out.println("Start reading");
            FileReader f = new FileReader(filename);
            BufferedReader fr = new BufferedReader(f);
            FileWriter fw = new FileWriter(filename + ".out");
            System.out.println("Total size of Map: " + mAry.size());
            String line = "";
            while (line != null) {
                line = fr.readLine();
                if (line != null) {
                    String sectionId = getSectionId(line);
                    if (sectionId != null) {
                        String content = (String) mAry.get(sectionId);
                        fw.write(line + "\n");
                        if (content != null) {
                            fw.write(content);
                            // System.out.println(line);
                            // System.out.print(content);
                        }
                        line = fr.readLine(); // read end section;
                        mAry.remove(sectionId);
                    }
                    fw.write(line + "\n");
                    // System.out.println(line);
                }
            }
            if (!mAry.isEmpty()) {
                fw.write("/* lost code following: \n");
                Set mapEntries = mAry.entrySet();
                Iterator itr = mapEntries.iterator();
                while (itr.hasNext()) {
                    Map.Entry entry = (Map.Entry) itr.next();
                    fw.write(indent + BEGIN + entry.getKey() + END1 + "\n");
                    fw.write((String) entry.getValue());
                    fw.write(indent + BEGIN + entry.getKey() + END2 + "\n");
                }
            }

            fr.close();
            fw.close();
        } catch (IOException e) {
            System.out.println("Error: " + e.toString());
        }
    }

    public void read(String filename) {
        try {
            System.out.println("Start reading");
            FileReader f = new FileReader(filename);
            BufferedReader fr = new BufferedReader(f);

            String line = "";
            String content = "";
            boolean inSection = false;
            while (line != null) {
                line = fr.readLine();
                if (line != null) {
                    if (inSection) {
                        String sectionId = getSectionId(line);
                        if (sectionId != null) {
                            inSection = false;
                            mAry.put(sectionId, content);
                            content = "";
                        } else {
                            content += line + "\n";
                        }
                    } else {
                        String sectionId = getSectionId(line);
                        if (sectionId != null) {
                            inSection = true;
                        }
                    }
                }
            }
            fr.close();

        } catch (IOException e) {
            System.out.println("Error: " + e.toString());
        }
    }

    private static String getSectionId(String line) {
        int first = line.indexOf(BEGIN);
        int second = line.indexOf(END1);
        if (second < 0) {
            second = line.indexOf(END2);
        }
        String s = null;
        if ((first > 0) && (second > 0)) {
            first = first + BEGIN.length();
            s = line.substring(first, second);
        }
        return s;
    }

}
