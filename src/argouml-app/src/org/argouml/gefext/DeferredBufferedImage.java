/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    tfmorris
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2008 The Regents of the University of California. All
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

package org.argouml.gefext;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.tigris.gef.base.Editor;

/**
 * Rendered GEF image which uses a band buffer to minimize memory usage for
 * high resolution images.  Image is rendered on demand in multiple passes
 * as getData is called.
 * <p>
 * NOTE: Only the methods which are called by ImageIO.write have been tested
 * and proven to work (getMinX/Y, getWidth, getHeight, & getData).  This is
 * <em>not</em> a general purpose RenderedImage implementation, so you are on
 * your own with any others.
 *
 * @author Tom Morris <tfmorris@gmail.com>
 * @since ArgoUML 0.26
 */
public class DeferredBufferedImage implements RenderedImage {

    private static final Logger LOG =
        Logger.getLogger(DeferredBufferedImage.class.getName());

    /**
     * RGB values for background color for image. Set as transparent. Chosen
     * because it's unlikely to be selected by the user, and leaves the diagram
     * readable if viewed without transparency.
     */
    public static final int TRANSPARENT_BG_COLOR = 0x00efefef;

    /**
     * Background color
     */
    public static final Color BACKGROUND_COLOR =
            new Color(TRANSPARENT_BG_COLOR, true);

    private static final int BUFFER_HEIGHT = 32;

    private int x, y;
    private int width;
    private int height;
    private int scale;
    private BufferedImage image;
    private Editor editor;

    private int scaledBufferHeight;
    private int y1, y2;

    /**
     * Construct a new DeferredBufferedImage which will use GEF to render the
     * current diagram on demand.
     *
     * @param drawingArea bounding rectangle of the area to be drawn
     * @param imageType Type of image to be created (e.g. TYPE_INT_ARGB). Must
     *                be on an image type which is supported by BufferedImage
     * @param ed GEF editor to be used for rendering the image
     * @param scaleFactor Integer scale factor to multiply by when rendering
     *                image.
     */
    public DeferredBufferedImage(Rectangle drawingArea, int imageType,
            Editor ed, int scaleFactor) {

        editor = ed;
        scale = scaleFactor;

        x = drawingArea.x;
        y = drawingArea.y;
        width = drawingArea.width;
        height = drawingArea.height;

        // Scale everything up
        x = x  * scale;
        y = y  * scale;
        width = width  * scale;
        height = height  * scale;
        scaledBufferHeight = BUFFER_HEIGHT * scale;

        // Create our bandbuffer which is just a small slice of the image
        // TODO: We used a fixed height buffer now, but we could be smarter and
        // compute a height which would fit in some memory budget, allowing us
        // to use taller buffers with narrower images, minimizing the overhead
        // of multiple rendering passes
        image = new BufferedImage(width, scaledBufferHeight, imageType);

        // Initialize band buffer bounds
        y1 = y;
        y2 = y1;
    }


    public Raster getData() {
        LOG.log(Level.FINE, "getData with no params");
        return getData(new Rectangle(x, y, width, height));
    }

    public Raster getData(Rectangle clip) {
//        LOG.log(Level.FINE, "getData Rectangle = " + clip);
        if (!isRasterValid(clip)) {
            LOG.log(Level.FINE, "Raster not valid, computing new raster");
            computeRaster(clip);
        }
        Rectangle oClip = offsetWindow(clip);
        Raster ras = image.getData(oClip);
        Raster translatedRaster = ras.createTranslatedChild(clip.x, clip.y);
//        LOG.log(Level.FINE, "getData returning raster = " + translatedRaster);
        return translatedRaster;
    }

    /**
     * @param clip clipping rectangle to test
     * @return true if clip rectangle is completely contained in image raster
     */
    private boolean isRasterValid(Rectangle clip) {
        if (clip.height > scaledBufferHeight) {
            throw new IndexOutOfBoundsException(
                    "clip rectangle must fit in band buffer");
        }
        return (clip.y >= y1 && (clip.y + clip.height - 1) < y2);
    }

    /**
     * Rasterize the next slice in the band buffer. Raster will be the full
     * width of the image and based vertically at the Y value of the current
     * clip rectangle.
     *
     * @param clip clip rectangle of interest
     */
    private void computeRaster(Rectangle clip) {
        LOG.log(Level.FINE, "Computing raster for rectangle {0}", clip);

        // Create a new graphics context so we start with fresh transforms
        Graphics2D graphics = image.createGraphics();
        graphics.scale(1.0 * scale, 1.0 * scale);

        // Fill with our background color
        graphics.setColor(BACKGROUND_COLOR);
        Composite c = graphics.getComposite();
        graphics.setComposite(AlphaComposite.Src);
        graphics.fillRect(0, 0, width, scaledBufferHeight);
        graphics.setComposite(c);

        // Translate & clip graphic to match region of interest
        graphics.setClip(0, 0, width, scaledBufferHeight);
        graphics.translate(0, -clip.y / scale);
        y1 = clip.y;
        y2 = y1 + scaledBufferHeight;

        // Ask GEF to print a band of the diagram (translated & clipped)
        editor.print(graphics);

        // Make sure it isn't caching anything that should be written
        graphics.dispose();
    }

    /**
     * Return a new clip rectangle which is offset by the base of our band
     * buffer.
     *
     * @param clip clipping rectangle
     * @return adjusted clipping rectangle
     */
    private Rectangle offsetWindow(Rectangle clip) {
        int baseY = clip.y - y1;
        return new Rectangle(clip.x, baseY, clip.width,
                Math.min(clip.height, scaledBufferHeight - baseY));
    }


    /**
     * Not implemented
     *
     * {@inheritDoc}
     * @see java.awt.image.RenderedImage#copyData(java.awt.image.WritableRaster)
     */
    public WritableRaster copyData(WritableRaster outRaster) {
        throw new UnsupportedOperationException();
        // This needs to iterate to fill entire output raster if implemented
//        return image.copyData(outRaster);
    }

    /**
     * Not implemented.
     * {@inheritDoc}
     *
     * @see java.awt.image.RenderedImage#getSources()
     */
    public Vector<RenderedImage> getSources() {
        return null;
    }

    public ColorModel getColorModel() {
        return image.getColorModel();
    }

    public int getMinX() {
        LOG.log(Level.FINE, "getMinX = 0");
        return 0;
    }

    public int getMinY() {
        LOG.log(Level.FINE, "getMinY = 0");
        return 0;
    }

    public int getMinTileX() {
        LOG.log(Level.FINE, "getMinTileX = 0");
        return 0;
    }

    public int getMinTileY() {
        LOG.log(Level.FINE, "getMinTileY = 0");
        return 0;
    }

    public int getNumXTiles() {
        LOG.log(Level.FINE, "getNumXTiles = 1");
        return 1;
    }

    /**
     * Untested. Not guaranteed to work.
     * {@inheritDoc}
     *
     * @see java.awt.image.RenderedImage#getNumYTiles()
     */
    public int getNumYTiles() {
        int tiles = (getHeight() + scaledBufferHeight - 1) / scaledBufferHeight;
        LOG.log(Level.FINE, "getNumYTiles = {0}", tiles);
        return tiles;
    }

    public Object getProperty(String name) {
        return image.getProperty(name);
    }

    public String[] getPropertyNames() {
        return image.getPropertyNames();
    }

    public SampleModel getSampleModel() {
        return image.getSampleModel();
    }


    /**
     * Untested. Not guaranteed to work.
     * {@inheritDoc}
     *
     * @see java.awt.image.RenderedImage#getNumYTiles()
     */

    public Raster getTile(int tileX, int tileY) {
        LOG.log(Level.FINE, "getTile x={0}, y = {1}", new Object[]{tileX, tileY});
        if (tileX < getMinTileX()
                || tileX >= getMinTileX() + getNumXTiles()
                || tileY < getMinTileY()
                || tileY >= getMinTileY() + getNumYTiles()) {
            throw new IndexOutOfBoundsException();
        }
        // FIXME: Boundary condition at end of image for non-integral
        // multiples of BUFFER_HEIGHT
        Rectangle tileBounds = new Rectangle(0, (tileY - getMinTileY()
                * scaledBufferHeight), width, scaledBufferHeight);
        return getData(tileBounds);
    }

    public int getTileGridXOffset() {
        LOG.log(Level.FINE, "getTileGridXOffset = 0");
        return 0;
    }

    public int getTileGridYOffset() {
        LOG.log(Level.FINE, "getTileGridYOffset = 0");
        return 0;
    }

    public int getTileWidth() {
        LOG.log(Level.FINE, "getTileWidth = {0}", width);
        return width;
    }

    public int getTileHeight() {
        LOG.log(Level.FINE, "getTileHeight = {0}", scaledBufferHeight);
        return scaledBufferHeight;
    }

    public int getWidth() {
        LOG.log(Level.FINE, "getWidth = {0}", width);
        return width;
    }

    public int getHeight() {
        LOG.log(Level.FINE, "getHeight = " + height);
        return height;
    }
}
