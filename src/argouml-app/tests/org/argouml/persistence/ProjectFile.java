/* $Id$
 *****************************************************************************
 * Copyright (c) 2020 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Linus Tolke
 *****************************************************************************
 */

package org.argouml.persistence;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Map;
import java.util.TreeMap;

import org.argouml.kernel.TestProject;

/**
 * Create a project from testmodels.
 *
 * Several tests require a file to test loading.
 */
public class ProjectFile {
    private static Map<String, String> names;
    static {
        names = new TreeMap<String, String>();
        names.put("href", "/testmodels/uml14/href-test.xmi");
        names.put("xmi", "/testmodels/uml13/Alittlebitofeverything.xmi");
        names.put("zargo", "/testmodels/uml14/Alittlebitofeverything.zargo");
    }
    private String name = null;
    private File file = null;

    /**
     * Creator.
     * @param selection Short name for the file needed or the file name.
     */
    public ProjectFile(String selection) {
        name = names.get(selection);

        if (name == null) {
            name = selection;
        }
    }
    /**
     * Get the name of the file.
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Create a real file from the contents of testmodels.
     * 
     * @return the File.
     * @throws IOException if the file cannot be created.
     */
    public File getFile() throws IOException {
        URL url = getClass().getResource(name);
        file = File.createTempFile("TestProject", ".zargo");
        file.delete();
        Files.copy(url.openStream(), file.toPath());
        file.deleteOnExit();
        return file;
    }

    /**
     * Delete the file to clean up immediately.
     *
  )   * The file will also be removed on exit. 
     */
    public void delete() {
        if (file != null) {
            file.delete();
            file = null;
        }
    }
}
