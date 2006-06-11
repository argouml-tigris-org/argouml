// $Id$
// Copyright (c) 2004-2006 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

import java.io.File;
import java.net.URL;

import junit.framework.Assert;

import org.argouml.application.Main;

/**
 * This class is a helper for testing things connected to the Main class.
 *
 * @author Linus Tolke
 * @stereotype utility
 */
public final class CheckMain {
    /**
     * Constructor.
     */
    private CheckMain() {
    }

    /**
     * Call main in the application.
     *
     * @param args The arguments to use.
     */
    public static void callMain(String[] args) {
        Main.main(args);
    }


    /**
     * Convert a relative filename (from the tests source tree)
     * to an URL.<p>
     *
     * This function have a set of ways to attempt to find the file. If
     * it doesn't succeed, it makes the test case fail.<p>
     *
     * @param filename The name to search for.
     * @return The URL.
     */
    public static File getTestModel(String filename) {
        // This works when running the test from within Eclipse.
        // Apparantly Eclipse runs the tests using a classloader that
        // has the tests Folder among the URL:s.
        URL url = CheckMain.class.getClassLoader().getResource(filename);
        if (url != null) {
            return new File(url.getFile());
        }

        // We have the path provided from the build script.
        String dir = System.getProperty("argouml.tests.dir");
        if (dir != null) {
            return new File(dir, filename);
        }

        Assert.fail("Could not locate the model " + filename);
        return null;
    }
}

