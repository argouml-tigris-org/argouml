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

/**
 *
 *Reading and writing preserved sections from the code
 *
 * @author  Marian
 */
abstract public class AbstractSection
{
    protected Map m_ary;

    /** Creates a new instance of Section */
    public AbstractSection() {
        m_ary = new HashMap();
        m_ary.clear();
    }

    public static String generate(String id, String INDENT) {
        return "";
	}

    // write todo:
    // check if sections are not used within the file and put them as comments
    // at the end of the file.
    // hint: use a second Map to compare with the used keys
    // =======================================================================

    public void write(String filename, String INDENT, boolean OutputLostSections) {
        try{
            System.out.println("Start reading");
            FileReader f = new FileReader(filename);
            BufferedReader fr = new BufferedReader(f);
            FileWriter fw = new FileWriter(filename + ".out");
            System.out.println("Total size of Map: " + m_ary.size());
            String line = "";
            while (line != null){
                line = fr.readLine();
                if (line != null){
                    String section_id = get_sect_id(line);
                    if (section_id != null){
                        String content = (String)m_ary.get(section_id);
                        fw.write(line + "\n");
                        if (content != null){
                            fw.write(content);
                            // System.out.println(line);
                            // System.out.print(content);
                        }
                        line = fr.readLine(); // read end section;
                        m_ary.remove(section_id);
                    }
                    fw.write(line + "\n");
                    // System.out.println(line);
                }
            }
            if ((m_ary.isEmpty() != true) && (OutputLostSections)){
                fw.write("/* lost code following: \n");
                Set map_entries = m_ary.entrySet();
                Iterator itr = map_entries.iterator();
                while (itr.hasNext()){
                    Map.Entry entry = (Map.Entry)itr.next();
                    fw.write(INDENT + "// section " + entry.getKey() + " begin\n");
                    fw.write((String)entry.getValue());
                    fw.write(INDENT + "// section " + entry.getKey() + " end\n");
                }
            }
            fr.close();
            fw.close();
        } catch (IOException e){
            System.out.println("Error: " + e.toString());
        }
    }

    public void read(String filename) {
        try{
            System.out.println("Start reading");
            FileReader f = new FileReader(filename);
            BufferedReader fr = new BufferedReader(f);

            String line = "";
            String content = "";
            boolean in_section = false;
            while (line != null){
                line = fr.readLine();
                if (line != null) {
                    if (in_section){
                        String section_id = get_sect_id(line);
                        if (section_id != null){
                            in_section = false;
                            m_ary.put(section_id, content);
                            content = "";
                        } else{
                            content += line + "\n";
                        }
                    } else {
                        String section_id = get_sect_id(line);
                        if (section_id != null){
                            in_section = true;
                        }
                    }
                }
            }
            fr.close();
        } catch (IOException e){
            System.out.println("Error: " + e.toString());
        }
    }

    public static String get_sect_id(String line){
        final String BEGIN = "// section ";
        final String END1 = " begin";
        final String END2 = " end";
        int first = line.indexOf(BEGIN);
        int second = line.indexOf(END1);
        if (second < 0){
            second = line.indexOf(END2);
        }
        String s = null;
        if ( (first > 0) && (second > 0) ){
            first = first + new String(BEGIN).length();
            s = line.substring(first, second);
        }
        return s;
    }
}
