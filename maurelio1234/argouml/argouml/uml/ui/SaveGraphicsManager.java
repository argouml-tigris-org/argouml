// $Id: SaveGraphicsManager.java 12598 2007-05-11 17:29:50Z tfmorris $
// Copyright (c) 2005-2007 The Regents of the University of California. All
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

package org.argouml.uml.ui;

import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;

import org.argouml.configuration.Configuration;
import org.argouml.configuration.ConfigurationKey;
import org.argouml.i18n.Translator;
import org.argouml.util.FileFilters;
import org.argouml.util.SuffixFilter;
import org.tigris.gef.base.CmdSaveEPS;
import org.tigris.gef.base.CmdSaveGIF;
import org.tigris.gef.base.CmdSaveGraphics;
import org.tigris.gef.base.CmdSavePNG;
import org.tigris.gef.base.CmdSavePS;
import org.tigris.gef.base.CmdSaveSVG;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.SaveEPSAction;
import org.tigris.gef.base.SaveGIFAction;
import org.tigris.gef.base.SaveGraphicsAction;
import org.tigris.gef.base.SavePNGAction;
import org.tigris.gef.base.SavePSAction;
import org.tigris.gef.base.SaveSVGAction;
import org.tigris.gef.persistence.PostscriptWriter;


/**
 * This class has some similar functions like PersistenceManager. <p>
 *
 * It centralizes all knowledge about the different graphical formats.
 * This class is the only one that is supposed to know
 * the complete list of supported graphics formats.
 *
 * @author mvw@tigris.org
 */
public final class SaveGraphicsManager {

    /**
     * The configuration key for the preferred graphics format.
     */
    public static final ConfigurationKey KEY_DEFAULT_GRAPHICS_FILTER =
        Configuration.makeKey("graphics", "default", "filter");

    /**
     * The configuration key for the "save graphics" file location.
     */
    public static final ConfigurationKey KEY_SAVE_GRAPHICS_PATH =
        Configuration.makeKey("graphics", "save", "path");

    /**
     * The configuration key for the "save all graphics" file location.
     */
    public static final ConfigurationKey KEY_SAVEALL_GRAPHICS_PATH =
        Configuration.makeKey("graphics", "save-all", "path");

    /**
     * The configuration key for the export graphics resolution.
     */
    public static final ConfigurationKey KEY_GRAPHICS_RESOLUTION =
        Configuration.makeKey("graphics", "export", "resolution");

    /**
     * The default file format.
     */
    private SuffixFilter defaultFilter;

    /**
     * The list of other file formats.
     */
    private List otherFilters = new ArrayList();

    /**
     * The singleton instance.
     */
    private static SaveGraphicsManager instance;

    /**
     * The constructor.
     */
    private SaveGraphicsManager() {
        defaultFilter = FileFilters.PNG_FILTER;
        otherFilters.add(FileFilters.GIF_FILTER);
        otherFilters.add(FileFilters.SVG_FILTER);
        otherFilters.add(FileFilters.PS_FILTER);
        otherFilters.add(FileFilters.EPS_FILTER);
        setDefaultFilterBySuffix(Configuration.getString(
                KEY_DEFAULT_GRAPHICS_FILTER,
                defaultFilter.getSuffix()));
    }

    /**
     * @param suffix the extension of the new default file-format
     */
    public void setDefaultFilterBySuffix(String suffix) {
        Iterator i = otherFilters.iterator();
        while (i.hasNext()) {
            SuffixFilter sf = (SuffixFilter) i.next();
            if (sf.getSuffix().equalsIgnoreCase(suffix)) {
                setDefaultFilter(sf);
                break;
            }
        }
    }

    /**
     * @param f the new default file-format
     */
    public void setDefaultFilter(SuffixFilter f) {
        otherFilters.remove(f);
        if (!otherFilters.contains(defaultFilter)) {
            otherFilters.add(defaultFilter);
        }
        defaultFilter = f;
        Configuration.setString(
                KEY_DEFAULT_GRAPHICS_FILTER,
                f.getSuffix());

        Collections.sort(otherFilters, new Comparator() {
            /*
             * @see java.util.Comparator#compare(T, T)
             */
            public int compare(Object arg0, Object arg1) {
                return ((SuffixFilter) arg0).getSuffix().compareToIgnoreCase(
                        ((SuffixFilter) arg1).getSuffix());
            }
        });
    }

    /**
     * @return returns the singleton
     */
    public static SaveGraphicsManager getInstance() {
        if (instance == null) {
            instance  = new SaveGraphicsManager();
        }
        return instance;
    }

    /**
     * This function allows to add new filters. This can be done e.g.
     * by modules.<p>
     *
     * @param f the filter
     */
    public void register(SuffixFilter f) {
        otherFilters.add(f);
    }

    /**
     * @param chooser the filechooser of which the filters will be set
     * @param defaultName default filename to show when chooser is displayed
     */
    public void setFileChooserFilters(
            JFileChooser chooser, String defaultName) {
        chooser.addChoosableFileFilter(defaultFilter);
        Iterator iter = otherFilters.iterator();
        while (iter.hasNext()) {
            chooser.addChoosableFileFilter((SuffixFilter) iter.next());
        }
        chooser.setFileFilter(defaultFilter);
        String fileName = defaultName + "." + defaultFilter.getSuffix();
        chooser.setSelectedFile(new File(fileName));
        chooser.addPropertyChangeListener(
                JFileChooser.FILE_FILTER_CHANGED_PROPERTY,
                new FileFilterChangedListener(chooser, defaultName));
    }

    /**
     * This class listens to changes in the selected filefilter.
     * If the user changes the filefilter
     * (e.g. he changes from *.gif to *.png),
     * then the filename field got emptied before I introduced this class.
     * Now, a new filename is made up, based on
     * the diagram name + the new extension (suffix).
     *
     * @author mvw@tigris.org
     */
    static class FileFilterChangedListener implements PropertyChangeListener {
        private JFileChooser chooser;
        private String defaultName;

        /**
         * Constructor.
         * @param c
         * @param name
         */
        public FileFilterChangedListener(JFileChooser c, String name) {
            chooser = c;
            defaultName = name;
        }

        /*
         * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
         */
        public void propertyChange(PropertyChangeEvent evt) {
            SuffixFilter filter = (SuffixFilter) evt.getNewValue();
            String fileName = defaultName + "." + filter.getSuffix();
            /* The next line does not work: */
            // chooser.setSelectedFile(new File(fileName));
            /* So, let's do it the hard way: */
            SwingUtilities.invokeLater(new Anonymous1(fileName));
        }

        class Anonymous1 implements Runnable {
            private String fileName;
            /**
             * Constructor.
             *
             * @param fn The filename.
             */
            Anonymous1(String fn) {
                fileName = fn;
            }

            /*
             * @see java.lang.Runnable#run()
             */
            public void run() {
                chooser.setSelectedFile(new File(fileName));
            }
        }
    }

    /**
     * @param name the filename
     * @return the filter
     */
    public SuffixFilter getFilterFromFileName(String name) {
        if (name.toLowerCase()
            .endsWith("." + defaultFilter.getSuffix())) {
            return defaultFilter;
        }
        Iterator iter = otherFilters.iterator();
        while (iter.hasNext()) {
            SuffixFilter filter = (SuffixFilter) iter.next();
            if (name.toLowerCase().endsWith("." + filter.getSuffix())) {
                return filter;
            }
        }
        return null;
    }

    /**
     * @return the extension of the default filter
     *         (just the text, not the ".")
     */
    public String getDefaultSuffix() {
        return defaultFilter.getSuffix();
    }

    /**
     * @param in the input file or path name which may or may not
     *           have a recognised extension
     * @return the amended file or pathname, guaranteed to have
     *         a recognised extension
     */
    public String fixExtension(String in) {
        if (getFilterFromFileName(in) == null) {
            in += "." + getDefaultSuffix();
        }
        return in;
    }

    /**
     * @param suffix
     *            the suffix (extension) of the filename, which corresponds to
     *            the graphics format to be used
     * @return the command that will do the save
     * @deprecated for 0.25.3 by tfmorris - use
     *             {@link #getSaveActionBySuffix(String)}
     */
    @SuppressWarnings("deprecation")
    public CmdSaveGraphics getSaveCommandBySuffix(String suffix) {
        CmdSaveGraphics cmd = null;
        if (FileFilters.PS_FILTER.getSuffix().equals(suffix)) {
            cmd = new CmdSavePS();
        } else if (FileFilters.EPS_FILTER.getSuffix().equals(suffix)) {
            cmd = new ActionSaveGraphicsCmdSaveEPS();
        } else if (FileFilters.PNG_FILTER.getSuffix().equals(suffix)) {
            cmd = new CmdSavePNG();
        } else if (FileFilters.GIF_FILTER.getSuffix().equals(suffix)) {
            cmd = new CmdSaveGIF();
        } else if (FileFilters.SVG_FILTER.getSuffix().equals(suffix)) {
            cmd = new CmdSaveSVG();
        }
        return cmd;
    }
    
    /**
     * @param suffix the suffix (extension) of the filename,
     *               which corresponds to the graphics format to be used
     * @return the action that will do the save
     */
    public SaveGraphicsAction getSaveActionBySuffix(String suffix) {
        SaveGraphicsAction cmd = null;
        if (FileFilters.PS_FILTER.getSuffix().equals(suffix)) {
            cmd = new SavePSAction(Translator.localize("action.save-ps"));
        } else if (FileFilters.EPS_FILTER.getSuffix().equals(suffix)) {
            cmd = new SaveEPSAction(Translator.localize("action.save-eps"));
        } else if (FileFilters.PNG_FILTER.getSuffix().equals(suffix)) {
            cmd = new SavePNGAction(Translator.localize("action.save-png"));
        } else if (FileFilters.GIF_FILTER.getSuffix().equals(suffix)) {
            cmd = new SaveGIFAction(Translator.localize("action.save-gif"));
        } else if (FileFilters.SVG_FILTER.getSuffix().equals(suffix)) {
            cmd = new SaveSVGAction(Translator.localize("action.save-svg"));
        }
        return cmd;
    }
    

    /**
     * @return the complete collection of SuffixFilters,
     *         the first one is the default one
     */
    public Collection getSettingsList() {
        Collection c = new ArrayList();
        c.add(defaultFilter);
        Iterator iter = otherFilters.iterator();
        while (iter.hasNext()) {
            c.add((iter.next()));
        }
        return c;
    }
}

/**
 * Class to adjust {@link org.tigris.gef.base.CmdSaveEPS} for our purpuses.<p>
 *
 * TODO: While doing this refactoring (February 2004) it is unclear to me (Linus
 * Tolke) why this modification in the {@link org.tigris.gef.base.CmdSaveEPS}
 * behavior is needed. Is it a bug in GEF? Is it an added feature?
 * The old comment was: override gef default to cope with scaling.
 * 
 * TODO: Does this behavior need to be replicated for {@link SaveEPSAction} 
 * as well? - tfm - 20070511
 */
class ActionSaveGraphicsCmdSaveEPS extends CmdSaveEPS {
    /*
     * @see org.tigris.gef.base.CmdSaveGraphics#saveGraphics(
     *         java.io.OutputStream, org.tigris.gef.base.Editor,
     *         java.awt.Rectangle)
     */
    protected void saveGraphics(OutputStream s, Editor ce,
                                Rectangle drawingArea)
        throws IOException {

        double editorScale = ce.getScale();
        int x = (int) (drawingArea.x * editorScale);
        int y = (int) (drawingArea.y * editorScale);
        int h = (int) (drawingArea.height * editorScale);
        int w = (int) (drawingArea.width * editorScale);
        drawingArea = new Rectangle(x, y, w, h);

        PostscriptWriter ps = new PostscriptWriter(s, drawingArea);

        ps.scale(editorScale, editorScale);

        ce.print(ps);
        ps.dispose();
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = 2859279843998315644L;
}

