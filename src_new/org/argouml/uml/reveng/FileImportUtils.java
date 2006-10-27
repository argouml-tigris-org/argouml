// $Id: FileImportSupport.java 11168 2006-09-14 20:35:24Z andrea_nironi $
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

package org.argouml.uml.reveng;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.argouml.util.SuffixFilter;

/**
 * Utility methods for source file importers.
 */
public  class FileImportUtils {

   
    /**
     * This method returns a List of source files to import.<p>
     *
     * Processing each file in turn is equivalent to a breadth first
     * search through the directory structure.
     *
     * @param file file or directory to import
     * @param recurse if true, descend directory tree recursively
     * @param filters array of file suffixes to match for filtering
     * @return a list of files to be imported
     */
    public static List getList(File file, boolean recurse,
            SuffixFilter[] filters) {
        if (file == null) {
            return Collections.EMPTY_LIST;
        }
        
	List res = new ArrayList();

	List toDoDirectories = new ArrayList();
	List doneDirectories = new ArrayList();

	toDoDirectories.add(file);

	while (toDoDirectories.size() > 0) {
	    File curDir = (File) toDoDirectories.get(0);
	    toDoDirectories.remove(0);
	    doneDirectories.add(curDir);

	    if (!curDir.isDirectory()) {
	        // For some reason, this alleged directory is a single file
	        // This could be that there is some confusion or just
	        // the normal, that a single file was selected and is
	        // supposed to be imported.
	        res.add(curDir);
	        continue;
	    }

	    // Get the contents of the directory
	    String [] files = curDir.list();

	    for (int i = 0; i < files.length; i++) {
	        File curFile = new File(curDir, files[i]);

	        // The following test can cause trouble with
	        // links, because links are accepted as
	        // directories, even if they link files.  Links
	        // could also result in infinite loops. For this
	        // reason we don't do this traversing recursively.
	        if (curFile.isDirectory()) {
	            // If this file is a directory
	            if (recurse) {
	                if (doneDirectories.indexOf(curFile) >= 0
	                        || toDoDirectories.indexOf(curFile) >= 0) {
	                    ; // This one is already seen or to be seen.
	                } else {
	                    toDoDirectories.add(curFile);
	                }
	            }
	        } else {
	            if (matchesSuffix(curFile, filters)) {
	                res.add(curFile);
	            }
	        }
	    }
	}

	return res;
    }

    /**
     * Tells if the filename matches one of the given suffixes.
     *
     * @param f file to be tested.
     * @param filters array of filters to test against.
     * @return true if parseable, false if not.
     */
    public static boolean matchesSuffix(Object f, SuffixFilter[] filters) {
        if (!(f instanceof File)) {
            return false;
        }
        String fileName = ((File) f).getName();
	if (filters != null) {
	    for (int i = 0; i < filters.length; i++) {
		if (fileName.endsWith(filters[i].getSuffix())) {
		    return true;
		}
	    }
	}
	return false;
    }

}
