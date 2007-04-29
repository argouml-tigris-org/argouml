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

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.net.URLClassLoader;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.File;

import org.apache.log4j.Logger;
import org.argouml.application.api.Argo;
import org.argouml.configuration.Configuration;

/**
 * Class to help users reverse engineer class information from arbitrary
 * .jar/.class file resources, like an import classpath.<p>
 *
 * can be used as follows:
 *
 * <pre>
 * <code>
 * try {
 *     ImportClassLoader loader = ImportClassLoader.getInstance();
 *     // add paths...
 *     loader.addFile(new File("/opt/lib/myjar.jar"));
 *
 *     Class clazz = loader.loadClass("org.xyz.MyException");
 *     Object ex = clazz.newInstance();
 *     cat.info("loaded class ok");
 * } catch(Exception e) {
 *     cat.warn("error loading class: "+e.toString());
 * }
 * </code>
 * </pre>
 *
 * It supports adding and removing Files from the import classpath.
 * And saving and loading the path to/from the users properties file.<p>
 *
 * It should be possible to make this the system class loader, but
 * I haven't got this to work yet:
 *
 * <pre>
 * <code>
 * final URLClassLoader urlClassLoader = new URLClassLoader(urls, cl);
 * //create a new custom class with the default classloader as its parent
 * try {
 *     EventQueue eq = Toolkit.getDefaultToolkit().getSystemEventQueue();
 *     eq.invokeAndWait(new Runnable() {
 *         public void run() {
 *             Thread.currentThread().setContextClassLoader(urlClassLoader);
 *             // this will replace the default system class loader with
 *             // the new custom classloader, so that the jvm will use
 *             // the new custom classloader to lookup a class
 *         }
 *     });
 *     //...
 * </code>
 * </pre>
 *
 * @author alexb
 */
public final class ImportClassLoader extends URLClassLoader {

    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(ImportClassLoader.class);

    private static ImportClassLoader instance;

    /**
     * The constructor.
     *
     * @param urls An array of urls.
     */
    private ImportClassLoader(URL[] urls) {
        super(urls);
    }

    /**
     * Try and return the existing instance if one exists.
     *
     * @return the instance
     * @throws MalformedURLException when the url is bad
     */
    public static ImportClassLoader getInstance()
	throws MalformedURLException {

        if (instance == null) {
            String path =
                Configuration.getString(Argo.KEY_USER_IMPORT_CLASSPATH,
                    System.getProperty("user.dir"));
            return getInstance(getURLs(path));
        } else {
            return instance;
        }
    }

    /**
     * There is no default constructor for URLClassloader, so we should provide
     * urls when creating the instance.
     * We crate a new instance in this method.
     *
     * @param urls the URLs
     * @return the instance of this class
     * @throws MalformedURLException when the URL is bad
     */
    public static ImportClassLoader getInstance(URL[] urls)
	throws MalformedURLException {
	instance = new ImportClassLoader(urls);
	return instance;
    }

    /**
     * @param f the file to be added
     * @throws MalformedURLException when the URL is bad
     * 
     * TODO: Either method signature or implementation needs to be
     * changed to make behavior consistent - tfm
     */
    public void addFile(File f) throws MalformedURLException {
        try {
            this.addURL(f.toURL());
        } catch (MalformedURLException e) {
	    LOG.warn("could not add file ", e);
	}
    }

    /**
     * Remove the given file.
     * But we can't remove the last file.
     *
     * @param f the file to be removed
     */
    public void removeFile(File f) {

        URL url = null;
        try {
            url = f.toURL();
        } catch (MalformedURLException e) {
	    LOG.warn("could not remove file ", e);
            return;
	}

        List urls = new ArrayList(); //getURLs();
        for (int i = 0; i < this.getURLs().length; i++) {

            if (!url.equals(getURLs()[i])) {
                urls.add(getURLs()[i]);
            }
        }

        // can't remove the last file
        if (urls.size() == 0) {
            return;
	}

        // can't remove from existing one so create new one.
        instance = new ImportClassLoader((URL[]) urls.toArray());
    }

    /**
     * Add the file for which a path is given.
     *
     * @param path the path in String format
     */
    public void setPath(String path) {

        StringTokenizer st = new StringTokenizer(path, ";");
        st.countTokens();
        while (st.hasMoreTokens()) {

            String token = st.nextToken();

            try {
		this.addFile(new File(token));
            } catch (MalformedURLException e) {
		LOG.warn("could not set path ", e);
	    }
        }
    }

    /**
     * Add the files for which the paths are given, and return in URL format.
     * @param path the paths in String format
     * @return the URLs
     */
    public static URL[] getURLs(String path) {

        java.util.List urlList = new ArrayList();

        StringTokenizer st = new StringTokenizer(path, ";");
        while (st.hasMoreTokens()) {

            String token = st.nextToken();

            try {
		urlList.add(new File(token).toURL());
            } catch (MalformedURLException e) {
		LOG.error(e);
	    }
        }

        URL[] urls = new URL[urlList.size()];
        for (int i = 0; i < urls.length; i++) {
            urls[i] = (URL) urlList.get(i);
        }

        return urls;
    }

    /**
     * @param paths the paths to the files to be added
     */
    public void setPath(Object[] paths) {

        for (int i = 0; i < paths.length; i++) {

            try {
		this.addFile(new File(paths[i].toString()));
            } catch (Exception e) {
		LOG.warn("could not set path ", e);
	    }
        }
    }

    /**
     * Get the user-configured path.
     */
    public void loadUserPath() {
        setPath(Configuration.getString(Argo.KEY_USER_IMPORT_CLASSPATH, ""));
    }

    /**
     * Store the user-configured path.
     */
    public void saveUserPath() {
	Configuration.setString(Argo.KEY_USER_IMPORT_CLASSPATH,
				this.toString());
    }

    public String toString() {

        URL[] urls = this.getURLs();
        String path = "";

        for (int i = 0; i < urls.length; i++) {
            path = path + urls[i].getFile();
            if (i < urls.length - 1) {
                path += ";";
	    }
        }

        return path;
    }
}

// try{
//  ImportClassLoader loader = ImportClassLoader.getInstance();
//  // add paths...
//  loader.addFile(new File("/opt/hibernate/hibernate-2.1/lib/odmg.jar"));
//
//    Class clazz = loader.loadClass("org.odmg.ODMGException");
//   Object db = clazz.newInstance();
//    cat.info("loaded class ok");
//  }catch(Exception e){cat.warn("error loading class: "+e.toString());}
