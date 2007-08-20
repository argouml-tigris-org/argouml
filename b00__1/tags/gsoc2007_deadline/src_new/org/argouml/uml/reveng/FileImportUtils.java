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

package org.argouml.uml.reveng;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.argouml.taskmgmt.ProgressMonitor;
import org.argouml.util.SuffixFilter;

/**
 * Utility methods for source file importers.
 */
public  class FileImportUtils {

    /**
     * Return a List of source files to import.<p>
     *
     * Processing each file in turn is equivalent to a breadth first
     * search through the directory structure.
     *
     * @param file file or directory to import
     * @param recurse if true, descend directory tree recursively
     * @param filters array of file suffixes to match for filtering
     * @return a list of files to be imported
     * @deprecated for 0.25.4 by tfmorris - use 
     * {@link #getList(File, boolean, SuffixFilter[], ProgressMonitor)}
     */
    public static List getList(File file, boolean recurse,
            SuffixFilter[] filters) {
        return getList(file, recurse, filters, null);
    }
        
    /**
     * This method returns a List of source files to import.
     * <p>
     * 
     * Processing each file in turn is equivalent to a breadth first search
     * through the directory structure.
     * 
     * @param file
     *            file or directory to import
     * @param recurse
     *            if true, descend directory tree recursively
     * @param filters
     *            array of file suffixes to match for filtering
     * @param monitor
     *            a progress monitor which will be monitored for cancellation
     *            requests. (Progress updates are not provided since the amount
     *            of time required to get the files is non-deterministic).
     * @return a list of files to be imported
     */
    public static List getList(File file, boolean recurse,
            SuffixFilter[] filters, ProgressMonitor monitor) {
        if (file == null) {
            return Collections.EMPTY_LIST;
        }
        
	List<File> results = new ArrayList<File>();

	List<File> toDoDirectories = new LinkedList<File>();
	Set<File> seenDirectories = new HashSet<File>();

	toDoDirectories.add(file);

	while (!toDoDirectories.isEmpty()) {
            if (monitor != null && monitor.isCanceled()) {
                return results;
            }
	    File curDir = toDoDirectories.remove(0);

	    if (!curDir.isDirectory()) {
	        // For some reason, this alleged directory is a single file
	        // This could be that there is some confusion or just
	        // the normal, that a single file was selected and is
	        // supposed to be imported.
	        results.add(curDir);
	        continue;
	    }

	    // Get the contents of the directory
	    for (File curFile : curDir.listFiles()) {

	        // The following test can cause trouble with
	        // links, because links are accepted as
	        // directories, even if they link files.  Links
	        // could also result in infinite loops. For this
	        // reason we don't do this traversing recursively.
	        if (curFile.isDirectory()) {
	            // If this file is a directory
	            if (recurse && !seenDirectories.contains(curFile)) {
	                toDoDirectories.add(curFile);
                        seenDirectories.add(curFile);
	            }
	        } else {
	            if (matchesSuffix(curFile, filters)) {
	                results.add(curFile);
	            }
	        }
	    }
	}

	return results;
    }

    /**
     * Tells if the filename matches one of the given suffixes.
     *
     * @param file file to be tested.
     * @param filters array of filters to test against.
     * @return true if parseable, false if not.
     */
    public static boolean matchesSuffix(Object file, SuffixFilter[] filters) {
        if (!(file instanceof File)) {
            return false;
        }
        String fileName = ((File) file).getName();
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
