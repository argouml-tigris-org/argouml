// $Id$
// Copyright (c) 2004 The Regents of the University of California. All
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

package org.argouml.kernel;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFileChooser;

import org.argouml.util.FileConstants;
import org.tigris.gef.util.UnexpectedException;


/**
 * This class shall be the only one that knows in which file formats 
 * ArgoUML is able to save and load. And all that knowledge is 
 * concentrated in the constructor... <p>
 * 
 * The PersisterManager manages the list of persisters.
 * 
 * @author MVW
 *
 */
public class PersisterManager {

    private AbstractFilePersister defaultPersister;
    private List otherPersisters = new ArrayList();
    
    /**
     * The constructor.
     */
    public PersisterManager() {
        // These are the file formats I know about:
        defaultPersister = new ZargoFilePersister();
        otherPersisters.add(new ArgoFilePersister());
        otherPersisters.add(new XmiFilePersister());
    }

    /**
     * This function allows e.g. modules to easily add new persisters.<p>
     * 
     * If someone wants to use this function, then we first should 
     * make the PersisterManager into a singleton!
     * 
     * @param fp the persister
     */
    public void register(AbstractFilePersister fp) {
        otherPersisters.add(fp);
    }
    
    /**
     * @param name the filename
     * @return the persister
     */
    public AbstractFilePersister getPersisterFromFileName(String name) {
        if (name.toLowerCase().endsWith("." + defaultPersister.getExtension())) 
            return defaultPersister;
        Iterator iter = otherPersisters.iterator();
        while (iter.hasNext()) {
            AbstractFilePersister persister = 
                (AbstractFilePersister) iter.next();
            if (name.toLowerCase().endsWith("." + persister.getExtension())) { 
                return persister;
            }
        }
        return null;
    }

    /**
     * @param chooser the filechooser of which the filters will be set 
     */
    public void setFileChooserFilters(JFileChooser chooser) {
        chooser.addChoosableFileFilter(defaultPersister);
        Iterator iter = otherPersisters.iterator();
        while (iter.hasNext()) {
            chooser.addChoosableFileFilter((AbstractFilePersister) iter.next());
        }
        chooser.setFileFilter(defaultPersister);
    }
    
    /**
     * @return the extension of the default persister 
     *         (just the text, not the ".")
     */
    public String getDefaultExtension() {
        return defaultPersister.getExtension();
    }
    
    /**
     * @param in the input file ot path name
     * @return the file or pathname with a default extension added
     */
    public String fixExtension(String in) {
        if (getPersisterFromFileName(in) == null) {
            in += "." + getDefaultExtension();
        }
        return in;
    }
    
    /**
     * @param in the input url
     * @return the url with default extension added, 
     *         if it did not have a valid extension yet
     */
    public URL fixUrlExtension(URL in) {
        URL newUrl;
        String n = in.toString();
        n = fixExtension(n);
        try {
            newUrl = new URL(n);
        } catch (java.net.MalformedURLException e) {
            throw new UnexpectedException(e);
        }
        return newUrl;
    }
    
    /**
     * Find the base name of the given filename.<p>
     * 
     * This is the name minus any valid file extension.
     *
     * @param n the given file name
     * @return the name (a String) without extension
     */
    public String getBaseName(String n) {
        AbstractFilePersister p = getPersisterFromFileName(n);
        if (p == null) return n;
        int extLength = p.getExtension().length() + 1;
        return n.substring(0, n.length() - extLength);
    }

}
