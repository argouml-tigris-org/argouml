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

// File: Main.java
// Classes: Main
// Original Author: Jaap Branderhorst

package org.argouml.application.projectrepair;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Category;
import org.argouml.util.SubInputStream;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

/**
 * @author Administrator
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public final class Main {
    
    protected static Category cat = 
        Category.getInstance(org.argouml.application.projectrepair.Main.class);
    private static Main _singleton;
    protected File _corruptedFile;
    protected File _repairedFile;
    
    public static Main getInstance() {
        if (_singleton == null) {
            _singleton = new Main();
        }
        return _singleton;
    }

    /**
     * Constructor for Main.
     */
    public Main() {
        super();
    }
    
    protected void printUsage() {
        System.out.println("Usage:\n");
        System.out.println("First argument: filename of corrupted project" +  
            " file.\n");
        System.out.println("Second argument: filename of repaired project" + 
            " file.\n");
        System.out.println("The repaired project file MUST be non-existing" + 
            " to prevent problems\n" + "saving the repaired file.\n");
    }
    
    protected void init(String[] args) {
        // process the arguments
        if (args.length != 2) {
            printUsage();
            System.exit(0);
        }
        _corruptedFile = new File(args[0]);
        if (!_corruptedFile.exists()) {
            System.out.println("Cannot find corrupted project file.\n");
            printUsage();
            System.exit(0);
        }
        // second argument should be non-existing file
        _repairedFile = new File(args[1]);
        if (_repairedFile.exists()) {
            printUsage();
            System.exit(0);
        }
    }
    
    public static void main(String[] args) {
        getInstance().init(args);
        getInstance().repairProject();
    }
    
    /**
     * Repairs the corrupted .zargo file in _corruptedFile. Writes the repaired
     * .zargo file to _repairedFile.
     */
    protected void repairProject() {
        // all files in the projectfile are zip files
        // so lets load all files and delegate repairing to
        // -XMIRepair
        // -PGMLRepair
        // -ArgoRepair
        
        // open the file
        ZipInputStream zipCorrupt = null;
        SubInputStream subCorrupt = null;
        try {
            zipCorrupt = 
                new ZipInputStream(new FileInputStream(_corruptedFile));
            subCorrupt = new SubInputStream(zipCorrupt);
        }
        catch (IOException e) {
            cat.error(e);
        }
        
        
        ZipOutputStream stream = null;
        try {
            stream = new ZipOutputStream(new FileOutputStream(_repairedFile));
        }
        catch (FileNotFoundException e) {
            cat.error(e);
        }
        
        while (true) {
            ZipEntry entryCorrupt = null;
            try {
                entryCorrupt = subCorrupt.getNextEntry();
            }
            catch (IOException e) {
                cat.error(e);
            }
            if (entryCorrupt == null) {
                break;
            }
            Repair repair = null;
            if (entryCorrupt.getName().endsWith(".argo")) {
                repair = new ArgoRepair();
            } 
            else 
            {
                if (entryCorrupt.getName().endsWith(".xmi")) 
                {
                    repair = new XMIRepair();
                } 
                else 
                {
                    if (entryCorrupt.getName().endsWith(".pgml")) 
                    {
                        repair = new PGMLRepair();
                    } 
                    else 
                    {
                        throw new IllegalStateException("Illegal file in " +
                            "projectfile.\n" + "Filename: " + 
                            entryCorrupt.getName());
                    }
                }
            }
            try {
                repair.loadDocument(subCorrupt); 
                repair.repairDocument();  
                BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(stream, "UTF-8"));
                ZipEntry entryRepaired = new ZipEntry(entryCorrupt.getName());
                stream.putNextEntry (entryRepaired);
                repair.saveDocument(writer);
                writer.flush();
                stream.closeEntry();
            }
            catch (UnsupportedEncodingException e) {
                cat.error(e);
            }
            catch (IOException e) {
                cat.error(e);
            }
        }
        if (stream != null) {
            try {
                stream.close();
            }
            catch (IOException io) {
                cat.error(io);
            }
        }
        
    }
    
    
}
