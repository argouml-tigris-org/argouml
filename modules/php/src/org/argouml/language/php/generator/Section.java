/*
 * Section.java
 *
 * Created on 24. Februar 2002, 15:30
 */

/**
 *
 *Reading and writing preserved sections from the code
 *
 * @author  Marian
 */
package org.argouml.language.php.generator;

import java.util.*;
import java.io.*;
import org.argouml.uml.generator.AbstractSection;


public class Section extends AbstractSection {

    public static String generate(String id, String INDENT){
        String s = "";
        s += INDENT + "// section " + id + " begin\n";
        s += INDENT + "// section " + id + " end\n";
        return s;
    }
}
