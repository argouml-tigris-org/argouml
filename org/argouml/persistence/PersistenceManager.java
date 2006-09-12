// $Id$
// Copyright (c) 2004-2006 The Regents of the University of California. All
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

package org.argouml.persistence;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.argouml.application.api.Configuration;
import org.argouml.application.api.ConfigurationKey;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.ui.ArgoFrame;
import org.tigris.gef.util.UnexpectedException;


/**
 * This class shall be the only one that knows in which file formats
 * ArgoUML is able to save and load. And all that knowledge is
 * concentrated in the constructor... <p>
 *
 * The PersisterManager manages the list of persisters. <p>
 *
 * This class is a singleton, since this allows external modules to
 * add extra persisters to the ArgoUML application.
 *
 * @author mvw@tigris.org
 */
public final class PersistenceManager {
    /**
     * The singleton instance.
     */
    private static final PersistenceManager INSTANCE =
        new PersistenceManager();

    private AbstractFilePersister defaultPersister;
    private List otherPersisters = new ArrayList();
    private XmiFilePersister quickViewDump;
    private XmiFilePersister xmiPersister;
    private XmiFilePersister xmlPersister;
    private UmlFilePersister umlPersister;
    private ZipFilePersister zipPersister;

    private AbstractFilePersister savePersister;
    
    private boolean lastLoadStatus = true;

    private String lastLoadMessage;

    /**
     * The configuration key for the project file location.
     */
    public static final ConfigurationKey KEY_PROJECT_NAME_PATH =
        Configuration.makeKey("project", "name", "path");

    /**
     * The configuration key for the "open project" file location.
     */
    public static final ConfigurationKey KEY_OPEN_PROJECT_PATH =
        Configuration.makeKey("project", "open", "path");

    /**
     * The configuration key for the "import xmi" file location.
     */
    public static final ConfigurationKey KEY_IMPORT_XMI_PATH =
        Configuration.makeKey("xmi", "import", "path");

    /**
     * Create the default diagram persister.
     */
    private DiagramMemberFilePersister diagramMemberFilePersister
        = new DiagramMemberFilePersister();

    /**
     * @return returns the singleton
     */
    public static PersistenceManager getInstance() {
        return INSTANCE;
    }

    /**
     * The constructor.
     */
    private PersistenceManager() {
        // These are the file formats I know about:
        defaultPersister = new ZargoFilePersister();
        quickViewDump = new XmiFilePersister();
        otherPersisters.add(quickViewDump);
        xmiPersister = new XmiFilePersister();
        otherPersisters.add(xmiPersister);
        xmlPersister = new XmlFilePersister();
        otherPersisters.add(xmlPersister);
        umlPersister = new UmlFilePersister();
        otherPersisters.add(umlPersister);
        zipPersister = new ZipFilePersister();
        otherPersisters.add(zipPersister);
        otherPersisters.add(new OldZargoFilePersister());
    }

    /**
     * This function allows to add new persisters. This can be done e.g.
     * by plugins/modules.
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
        if (defaultPersister.isFileExtensionApplicable(name)) {
            return defaultPersister;
        }
        Iterator iter = otherPersisters.iterator();
        while (iter.hasNext()) {
            AbstractFilePersister persister =
                (AbstractFilePersister) iter.next();
            if (persister.isFileExtensionApplicable(name)) {
                return persister;
            }
        }
        return null;
    }

    /**
     * @param chooser the filechooser of which the filters will be set
     * @param fileName the filename of the file to be saved (optional)
     */
    public void setSaveFileChooserFilters(JFileChooser chooser, 
            String fileName) {
        
        chooser.addChoosableFileFilter(defaultPersister);
        AbstractFilePersister defaultFileFilter = defaultPersister;
        
        Iterator iter = otherPersisters.iterator();
        while (iter.hasNext()) {
            AbstractFilePersister fp = (AbstractFilePersister) iter.next();
            if (fp.isSaveEnabled()
                    && !fp.equals(xmiPersister)
                    && !fp.equals(xmlPersister)) {
                chooser.addChoosableFileFilter(fp);
                if (fileName != null 
                        && fp.isFileExtensionApplicable(fileName)) {
                    defaultFileFilter = fp;
                }
            }
        }
        chooser.setFileFilter(defaultFileFilter);
    }

    /**
     * @param chooser the filechooser of which the filters will be set
     */
    public void setOpenFileChooserFilter(JFileChooser chooser) {
        MultitypeFileFilter mf = new MultitypeFileFilter();
        mf.add(defaultPersister);
        chooser.addChoosableFileFilter(mf);
        chooser.addChoosableFileFilter(defaultPersister);
        Iterator iter = otherPersisters.iterator();
        while (iter.hasNext()) {
            AbstractFilePersister ff = (AbstractFilePersister) iter.next();
            if (ff.isLoadEnabled()) {
                mf.add(ff);
                chooser.addChoosableFileFilter(ff);
            }
        }
        chooser.setFileFilter(mf);
    }

    /**
     * @param chooser the filechooser of which the filters will be set
     */
    public void setXmiFileChooserFilter(JFileChooser chooser) {
        chooser.addChoosableFileFilter(xmiPersister);
        chooser.setFileFilter(xmiPersister);
    }

    /**
     * @return the extension of the default persister
     *         (just the text, not the ".")
     */
    public String getDefaultExtension() {
        return defaultPersister.getExtension();
    }

    /**
     * @return the extension of the xmi persister
     *         (just the text, not the ".")
     */
    public String getXmiExtension() {
        return xmiPersister.getExtension();
    }

    /**
     * @param in the input file or path name which may or may not
     *           have a recognised extension
     * @return the amended file or pathname, guaranteed to have
     *         a recognised extension
     */
    public String fixExtension(String in) {
        if (getPersisterFromFileName(in) == null) {
            in += "." + getDefaultExtension();
        }
        return in;
    }

    /**
     * @param in the input file or path name which may or may not
     *           have a "xmi" extension
     * @return the amended file or pathname, guaranteed to have
     *         a "xmi" extension
     */
    public String fixXmiExtension(String in) {
        if (getPersisterFromFileName(in) != xmiPersister) {
            in += "." + getXmiExtension();
        }
        return in;
    }


    /**
     * @param in the input uri which may or may not have a recognised extension
     * @return the uri with default extension added,
     *         if it did not have a valid extension yet
     */
    public URI fixUriExtension(URI in) {
        URI newUri;
        String n = in.toString();
        n = fixExtension(n);
        try {
            newUri = new URI(n);
        } catch (java.net.URISyntaxException e) {
            throw new UnexpectedException(e);
        }
        return newUri;
    }

    /**
     * Find the base name of the given filename.<p>
     *
     * This is the name minus any valid file extension.
     * Invalid extensions are left alone.
     *
     * @param n the given file name
     * @return the name (a String) without extension
     */
    public String getBaseName(String n) {
        AbstractFilePersister p = getPersisterFromFileName(n);
        if (p == null) {
            return n;
        }
        int extLength = p.getExtension().length() + 1;
        return n.substring(0, n.length() - extLength);
    }

    /**
     * Generates a String dump of the current model for quick viewing.
     *
     * @param project The project to generate.
     * @return The whole model in a String.
     */
    public String getQuickViewDump(Project project) {
        OutputStream stream = new ByteArrayOutputStream();
        try {
            quickViewDump.writeProject(project, stream, null);
        } catch (Exception e) {
            // If anything goes wrong return the stack
            // trace as a string so that we get some
            // useful feedback.
            e.printStackTrace(new PrintStream(stream));
        }
        return stream.toString();
    }

    /**
     * Get the file persister for diagrams.
     *
     * @return the diagram file persister.
     */
    DiagramMemberFilePersister getDiagramMemberFilePersister() {
    	return diagramMemberFilePersister;
    }

    /**
     * Set an alternative file persister for diagrams.
     *
     * @param persister the persister to use instead of the default
     */
    public void setDiagramMemberFilePersister(
            DiagramMemberFilePersister persister) {
    	diagramMemberFilePersister = persister;
    }

    /**
     * Returns true if we are allowed to overwrite the given file.
     *
     * @param overwrite if true, then the user is not asked
     * @param file the given file
     * @return true if we are allowed to overwrite the given file
     */
    public boolean confirmOverwrite(boolean overwrite, File file) {
        if (file.exists() && !overwrite) {
            String sConfirm =
                Translator.messageFormat(
                    "optionpane.confirm-overwrite",
                    new Object[] {file});
            int nResult =
                JOptionPane.showConfirmDialog(
                        ArgoFrame.getInstance(),
                        sConfirm,
                        Translator.localize(
                            "optionpane.confirm-overwrite-title"),
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
            if (nResult != JOptionPane.YES_OPTION) {
                return false;
            }
        }
        return true;
    }

    /**
     * Supply the encoding to be used throughout the persistence
     * mechanism.
     * @return the encoding.
     */
    public static String getEncoding() {
        return "UTF-8";
    }
    
    /**
     * Get the last message which caused loading to fail. Used for junit tests.
     *
     * @return the last message which caused loading to fail
     */
    public String getLastLoadMessage() {
        return lastLoadMessage;
    }

    /**
     * Set the last load message. Used for junit tests.
     *
     * @param msg the last load message
     */
    public void setLastLoadMessage(String msg) {
        lastLoadMessage = msg;
    }

    /**
     * @return the status of the last load attempt. Used for junit tests.
     */
    public boolean getLastLoadStatus() {
        return lastLoadStatus;
    }

    /**
     * Set the status of the last load attempt. Used for junit tests.
     *
     * @param status the status of the last load attempt
     */
    public void setLastLoadStatus(boolean status) {
        lastLoadStatus = status;
    }
    
    public void setSavePersister(AbstractFilePersister persister) {
        savePersister = persister;
    }
    
    public AbstractFilePersister getSavePersister() {
        return savePersister;
    }
}

/**
 * Composite file filter which will accept any
 * file type added to it.
 */
class MultitypeFileFilter extends FileFilter {
    private ArrayList filters;
    private ArrayList extensions;
    private String desc;

    /**
     * Constructor
     */
    public MultitypeFileFilter() {
        super();
        filters = new ArrayList();
        extensions = new ArrayList();
    }

    /**
     * Add a FileFilter to list of file filters to be accepted
     * 
     * @param filter FileFilter to be added
     */
    public void add(AbstractFilePersister filter) {
        filters.add(filter);
        String extension = filter.getExtension();
        if (!extensions.contains(extension)) {
            extensions.add(filter.getExtension());
            desc =
                ((desc == null)
                    ? ""
                    : desc + ", ")
                + "*." + extension;
        }
    }

    /**
     * Return all added FileFilters.
     * 
     * @return collection of FileFilters
     */
    public Collection getAll() {
        return filters;
    }

    /**
     * Accept any file that any of our filters will accept.
     *
     * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
     */
    public boolean accept(File arg0) {
        Iterator it = filters.iterator();
        while (it.hasNext()) {
            if (((FileFilter) it.next()).accept(arg0)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @see javax.swing.filechooser.FileFilter#getDescription()
     */
    public String getDescription() {
        Object[] s = {desc};
        return Translator.messageFormat("filechooser.all-types-desc", s);
    }
}
