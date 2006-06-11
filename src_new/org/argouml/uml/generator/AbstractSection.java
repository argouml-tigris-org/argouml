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

package org.argouml.uml.generator;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * Reading and writing preserved sections from the code.
 *
 * @author Marian Heddesheimer
 */
public abstract class AbstractSection {
    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(AbstractSection.class);

    /**
     * System newline separator.
     */
    private static final String LINE_SEPARATOR =
	System.getProperty("line.separator");

    private Map mAry;

    /**
     * Creates a new instance of Section.
     */
    public AbstractSection() {
        mAry = new HashMap();
        mAry.clear();
    }

    /**
     * @param id the string to generate
     * @param indent the current indentation
     * @return the generated string
     */
    public static String generate(String id, String indent) {
        return "";
    }

    /**
     * write TODO: Check if sections are not used within the file and
     * put them as comments at the end of the file.
     * Hint: use a second Map to compare with the used keys.
     *
    * @param filename the file name
     * @param indent the current indentation
     * @param outputLostSections true if lost sections are to be written
     */
    public void write(String filename, String indent,
		      boolean outputLostSections) {
        try {
            FileReader f = new FileReader(filename);
            BufferedReader fr = new BufferedReader(f);
            FileWriter fw = new FileWriter(filename + ".out");
            String line = "";
            line = fr.readLine();
            while (line != null) {
                String sectionId = getSectId(line);
                if (sectionId != null) {
                    String content = (String) mAry.get(sectionId);
                    if (content != null) {
                        fw.write(line + LINE_SEPARATOR);
                        fw.write(content);
                        // read until the end section is found, discard
                        // generated content
                        String endSectionId = null;
                        do {
                            line = fr.readLine();
                            if (line == null) {
                                throw new EOFException(
                                        "Reached end of file while looking "
                                        + "for the end of section with ID = \""
                                        + sectionId + "\"!");
                            }
                            endSectionId = getSectId(line);
                        } while (endSectionId == null);
                        if (!endSectionId.equals(sectionId)) {
                            LOG.error("Mismatch between sectionId (\""
                                    + sectionId + "\") and endSectionId (\""
                                    + endSectionId + "\")!");
                        }
                    }
                    mAry.remove(sectionId);
                }
                fw.write(line);
                line = fr.readLine();
                if (line != null) {
                    fw.write(LINE_SEPARATOR);
                }
            }
            if ((!mAry.isEmpty()) && (outputLostSections)) {
                fw.write("/* lost code following: " + LINE_SEPARATOR);
                Set mapEntries = mAry.entrySet();
                Iterator itr = mapEntries.iterator();
                while (itr.hasNext()) {
                    Map.Entry entry = (Map.Entry) itr.next();
                    fw.write(indent + "// section " + entry.getKey()
			     + " begin" + LINE_SEPARATOR);
                    fw.write((String) entry.getValue());
                    fw.write(indent + "// section " + entry.getKey()
			     + " end" + LINE_SEPARATOR);
                }
                fw.write("*/");
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
                            content += line + LINE_SEPARATOR;
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

    /**
     * @param line the given line
     * @return the section identifier
     */
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
        if ((first >= 0) && (second >= 0)) {
            first = first + begin.length();
            s = line.substring(first, second);
        }
        return s;
    }
}
