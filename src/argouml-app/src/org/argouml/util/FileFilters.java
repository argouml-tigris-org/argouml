/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
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

package org.argouml.util;

import javax.swing.filechooser.FileFilter;

import org.argouml.i18n.Translator;

/** This class handles the the various file extensions.
 * It's not clear whether all of these are supported
 * for input or output or a mixture of both.
 * There is no file open>pgml/svg/xmi/log/gif
 * and there is no known way to save only a log or
 * an xmi out of Argo.
 */
public class FileFilters {

    /**
     * This is a filter for uncompressed project format.
     */
    public static final SuffixFilter UNCOMPRESSED_FILE_FILTER = new
        SuffixFilter(FileConstants.UNCOMPRESSED_FILE_EXT.substring(1),
                     "Argo uncompressed project file");

    /**
     * This is a filter for compressed project format.
     */
    public static final SuffixFilter COMPRESSED_FILE_FILTER = new
        SuffixFilter(FileConstants.COMPRESSED_FILE_EXT.substring(1),
                     "Argo compressed project file");

    /**
     * This is a filter for xmi files.
     */
    public static final SuffixFilter XMI_FILTER = new
        SuffixFilter("xmi", "XML Metadata Interchange");

    /** This is for Precision Graphics Markup Language
     * a very old and now mostly dead standard.
     * see W3C.org for more info
     */
    public static final SuffixFilter PGML_FILTER = new
        SuffixFilter("pgml", "Argo diagram");

    /** This should read or write a config file
     * but as yet not fully implemented.
     */
    public static final SuffixFilter CONFIG_FILTER = new
        SuffixFilter("config", "Argo configutation file");

    /** History Filter...Argo has trouble with remembering
     * things at times. Maybe this filter helps.
     * status is unknown. last reveiwed 8 months ago.
     */
    public static final SuffixFilter HIST_FILTER = new
        SuffixFilter("hist", "Argo history file");

    /**
     * Log file filter.
     */
    public static final SuffixFilter LOG_FILTER = new
        SuffixFilter("log", "Argo usage log");

    /** 
     * This writes the GIF file.
     */
    public static final SuffixFilter GIF_FILTER = new
        SuffixFilter("gif", Translator.localize("combobox.filefilter.gif"));

    /**
     * Filter for portable network graphics (png) files.
     */
    public static final SuffixFilter PNG_FILTER = new
        SuffixFilter("png", Translator.localize("combobox.filefilter.png"));

    /**
     * This writes a Postscript file.
     */
    public static final SuffixFilter PS_FILTER = new
        SuffixFilter("ps", Translator.localize("combobox.filefilter.ps"));

    /**
     * This writes an E-Postscript file.
     */
    public static final SuffixFilter EPS_FILTER = new
        SuffixFilter("eps", Translator.localize("combobox.filefilter.eps"));

    /** SVG is the standard set by the W3C re vector graphics
     * The current output for SVG goes through GEF.
     * The output is considered to be 'poor' in
     * quality and builds multiple duplicate artifacts
     * in the SVG output.
     *
     * SVG is considered very useful for documentation
     * generation over standard raster images like
     * gif (a patented format), jpg and png.
     *
     * It is possible to embed links within SVG to
     * other areas in the svg or on the web. This
     * means that javadocs generated with SVG based
     * diagrams will have links to the classes from
     * within the diagram!
     * Not too mention zooming and animation (animation
     * is considered very useful for modeling the
     * behaviors of a class for example in state or
     * sequence diagrams.
     */
    public static final SuffixFilter SVG_FILTER = new
        SuffixFilter("svg", Translator.localize("combobox.filefilter.svg"));

    /**
     * Returns the suffix for which a FileFilter filters.
     * @param filter The FileFilter from which we want to know the suffix
     * @return String The suffix of the FileFilter.
     * Returns null if the FileFilter is not an instance of SuffixFilter.
     */
    public static String getSuffix(FileFilter filter) {
        if (filter instanceof SuffixFilter) {
            return ((SuffixFilter) filter).getSuffix();
        }
        return null;
    }

} 
