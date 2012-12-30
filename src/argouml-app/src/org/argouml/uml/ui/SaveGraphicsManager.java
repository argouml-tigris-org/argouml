/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    bobtarling
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2005-2008 The Regents of the University of California. All
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
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.argouml.configuration.Configuration;
import org.argouml.configuration.ConfigurationKey;
import org.argouml.gefext.DeferredBufferedImage;
import org.argouml.i18n.Translator;
import org.argouml.util.FileFilters;
import org.argouml.util.SuffixFilter;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Layer;
import org.tigris.gef.base.SaveEPSAction;
import org.tigris.gef.base.SaveGIFAction;
import org.tigris.gef.base.SaveGraphicsAction;
import org.tigris.gef.base.SavePNGAction;
import org.tigris.gef.base.SavePSAction;
import org.tigris.gef.base.SaveSVGAction;
import org.tigris.gef.persistence.export.PostscriptWriter;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.util.Localizer;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;


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

    private static final int MIN_MARGIN = 15;

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
    private List<SuffixFilter> otherFilters = new ArrayList<SuffixFilter>();

    /**
     * The singleton instance.
     */
    private static SaveGraphicsManager instance = new SaveGraphicsManager();

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
        for (SuffixFilter sf : otherFilters) {
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

        Collections.sort(otherFilters, new Comparator<SuffixFilter>() {
            public int compare(SuffixFilter arg0, SuffixFilter arg1) {
                return arg0.getSuffix().compareToIgnoreCase(
                        arg1.getSuffix());
            }
        });
    }

    /**
     * @return returns the singleton
     */
    public static SaveGraphicsManager getInstance() {
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
     * @param suffix the suffix (extension) of the filename,
     *               which corresponds to the graphics format to be used
     * @return the action that will do the save
     */
    public SaveGraphicsAction getSaveActionBySuffix(String suffix) {
        SaveGraphicsAction cmd = null;
        if (FileFilters.PS_FILTER.getSuffix().equals(suffix)) {
            cmd = new SavePSAction(Translator.localize("action.save-ps"));
        } else if (FileFilters.EPS_FILTER.getSuffix().equals(suffix)) {
            cmd = new SaveScaledEPSAction(
                    Translator.localize("action.save-eps"));
        } else if (FileFilters.PNG_FILTER.getSuffix().equals(suffix)) {
            cmd = new SavePNGAction2(Translator.localize("action.save-png"));
        } else if (FileFilters.GIF_FILTER.getSuffix().equals(suffix)) {
            cmd = new SaveGIFAction(Translator.localize("action.save-gif"));
            // TODO: The following can be used when we drop Java 5 support or
            // when an ImageIO GIF writer plugin is bundled
//            cmd = new SaveGIFAction2(Translator.localize("action.save-gif"));
        } else if (FileFilters.SVG_FILTER.getSuffix().equals(suffix)) {
            // TODO: Use the SVGWriter2D implementation
//            cmd = new SaveSVGAction2(Translator.localize("action.save-svg"));
            cmd = new SaveSVGAction(Translator.localize("action.save-svg"));
        }
        return cmd;
    }


    /**
     * @return the complete collection of SuffixFilters,
     *         the first one is the default one
     */
    public List<SuffixFilter> getSettingsList() {
        List<SuffixFilter> c = new ArrayList<SuffixFilter>();
        c.add(defaultFilter);
        c.addAll(otherFilters);
        return c;
    }

    /**
     * Adjust the drawing area so that instead of a tight bounding box, it
     * includes the canvas origin and some space around the lower and right
     * sides so that the elements will be roughly centered. Elements which are
     * off the top or left side of the canvas may still be clipped (ie if the
     * original drawing area had a negative x or y coordinate).
     *
     * @param area rectangle representing original drawing area
     * @return an expanded rectangle
     */
    static Rectangle adjustDrawingArea(Rectangle area) {
        int xMargin = area.x;
        if (xMargin < 0) {
            xMargin = 0;
        }
        int yMargin = area.y;
        if (yMargin < 0) {
            yMargin = 0;
        }
        int margin = Math.max(xMargin, yMargin);
        if (margin < MIN_MARGIN) {
            margin = MIN_MARGIN;
        }
        return new Rectangle(0, 0,
                area.width + (2 * margin),
                area.height + (2 * margin));
    }
}


class SaveScaledEPSAction extends SaveEPSAction {

    SaveScaledEPSAction(String name) {
        super(name);
    }

    @Override
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

}

/**
 * Write out a PNG image of the current diagram using a more memory efficient
 * scheme than GEF uses.
 *
 * @author Tom Morris <tfmorris@gmail.com>
 */
class SavePNGAction2 extends SavePNGAction {

    private static final Logger LOG = Logger.getLogger(SavePNGAction2.class.getName());

    SavePNGAction2(String name) {
        super(name);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        Editor ce = Globals.curEditor();
        Rectangle drawingArea =
            ce.getLayerManager().getActiveLayer().calcDrawingArea();
        // If the diagram is empty, GEF won't write anything, leaving us with
        // an empty (and invalid) file.  Handle this case ourselves to prevent
        // this from happening.
        if (drawingArea.width <= 0 || drawingArea.height <= 0) {
            Rectangle dummyArea = new Rectangle(0, 0, 50, 50);
            try {
                saveGraphics(outputStream, ce, dummyArea);
            } catch (java.io.IOException e) {
                LOG.log(Level.SEVERE, "Error while exporting Graphics:", e);
            }
            return;
        }

        // Anything else is handled the normal way
        super.actionPerformed(ae);
    }

    /**
     * Write the diagram contained by the current editor into an OutputStream as
     * a PNG image.
     */
    @Override
    protected void saveGraphics(OutputStream s, Editor ce,
            Rectangle drawingArea)
        throws IOException {

        Rectangle canvasArea =
            SaveGraphicsManager.adjustDrawingArea(drawingArea);

        // Create an image which will do deferred rendering of the GEF
        // diagram on demand as data is pulled from it
        RenderedImage i = new DeferredBufferedImage(canvasArea,
                BufferedImage.TYPE_INT_ARGB, ce, scale);

        LOG.log(Level.FINE,
                "Created DeferredBufferedImage - drawingArea = {0} , scale = {1}",
                new Object[]{canvasArea, scale});

        ImageIO.write(i, "png", s);
    }


}

/**
 * Action to save a diagram as a GIF image in a supplied OutputStream.
 *
 * TODO: This requires Java 6 in its current state, so don't use.
 *
 * @author Tom Morris <tfmorris@gmail.com>
 */
class SaveGIFAction2 extends SaveGIFAction {

    /**
     * Creates a new SaveGIFAction
     *
     * @param name The name of the action
     */
    SaveGIFAction2(String name) {
        super(name);
    }


    /**
     * Write the diagram contained by the current editor into an OutputStream as
     * a GIF image.
     */
    @Override
    protected void saveGraphics(OutputStream s, Editor ce,
            Rectangle drawingArea) throws IOException {

        Rectangle canvasArea =
            SaveGraphicsManager.adjustDrawingArea(drawingArea);

        RenderedImage i = new DeferredBufferedImage(canvasArea,
                BufferedImage.TYPE_INT_ARGB, ce, scale);

        // NOTE: GEF's GIF writer uses Jeff Poskanzer's GIF encoder, but that
        // saves a copy of the entire image in an internal buffer before
        // starting work, defeating the whole purpose of our incremental
        // rendering.

        // Java SE 6 has a native GIF writer, but it's not in Java 5.  One
        // is available in the JAI-ImageIO library, but we don't currently
        // bundle that and at 6+ MB it seems like a heavyweight solution, but
        // I don't have time to produce a stripped down version right now - tfm
        // https://jai-imageio.dev.java.net/

        ImageIO.write(i, "gif", s);

    }

}

class SaveSVGAction2 extends SaveGraphicsAction {

    /**
     * Creates a new SaveSVGAction
     *
     * @param name
     *                The name of the action
     */
    public SaveSVGAction2(String name) {
        this(name, false);
    }

    /**
     * Creates a new SaveSVGAction
     *
     * @param name
     *                The name of the action
     * @param icon
     *                The icon of the action
     */
    public SaveSVGAction2(String name, Icon icon) {
        this(name, icon, false);
    }

    /**
     * Creates a new SaveSVGAction
     *
     * @param name
     *                The name of the action
     * @param localize
     *                Whether to localize the name or not
     */
    public SaveSVGAction2(String name, boolean localize) {
        super(localize ? Localizer.localize("GefBase", name) : name);
    }

    /**
     * Creates a new SaveSVGAction
     *
     * @param name
     *                The name of the action
     * @param icon
     *                The icon of the action
     * @param localize
     *                Whether to localize the name or not
     */
    public SaveSVGAction2(String name, Icon icon, boolean localize) {
        super(localize ? Localizer.localize("GefBase", name) : name, icon);
    }

    protected void saveGraphics(OutputStream outStream, Editor ce,
    		Rectangle drawingArea)
        throws IOException {

//        LayerPerspective layer = DiagramUtils.getActiveDiagram().getLayer();
        Layer layer = ce.getLayerManager().getActiveLayer();

        Rectangle bounds = new Rectangle();
        for (Fig fig : layer.getContents()) {
            bounds.x = Math.min(bounds.x, fig.getX());
            bounds.y = Math.min(bounds.y, fig.getY());
            // we actually are computing max x & max y, not width & height
            bounds.width = Math.max(bounds.width, fig.getX() + fig.getWidth());
            bounds.height = Math.max(bounds.height,
                    fig.getY() + fig.getHeight());
        }

        // Convert max x/y to width/height
        bounds.width -= bounds.x;
        bounds.height -= bounds.y;

        // Get a DOMImplementation
        DOMImplementation domImpl =
        GenericDOMImplementation.getDOMImplementation();
        // Create an instance of org.w3c.dom.Document
        Document document = domImpl.createDocument(null, "svg", null);
        // Create an instance of the SVG Generator
        SVGGraphics2D svgGenerator = new SVGGraphics2D(document);

        ce.print(svgGenerator);

//            for (Fig f : layer.getContents()) {
//                // ignore clipping
//                String url = null;
//                String clazz = null;
//                Object owner = f.getOwner();
//                if (Model.getFacade().isAUMLElement(owner)) {
//                    clazz = Model.getMetaTypes().getName(owner);
//                    // TODO: toLower?
//                    if (Model.getFacade().isAModelElement(owner)) {
//                        String name = Model.getFacade().getName(owner);
//                        if (name == null) {
//                            name = "";
//                        }
//                        url = "http://argoeclipse.tigris.org" + "#" + name;
//                    }
//                }
////                writer.beginFig(f, clazz, url);
//                f.paint(writer);
//  //              writer.endFig();
//            }
            // Finally, stream out SVG to the standard output using UTF-8
            // character to byte encoding
        boolean useCSS = true; // we want to use CSS style attribute
        Writer out = new OutputStreamWriter(outStream, "UTF-8");
        svgGenerator.stream(out, useCSS);
    }
}
