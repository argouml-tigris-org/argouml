// Copyright (c) 1996-99 The Regents of the University of California. All
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

// $Id$

package org.argouml.util;

import javax.swing.filechooser.FileFilter;

/** This class handles the the various file extensions.
 * It's not clear whether all of these are supported
 * for input or output or a mixture of both.
 * There is no file open>pgml/svg/xmi/log/gif
 * and there is no known way to save only a log or
 * an xmi out of Argo.
 */
public class FileFilters {

    ////////////////////////////////////////////////////////////////
    // constants

    /**
     * This is a filter for uncompressed project format.
     */
    public static final SuffixFilter UncompressedFileFilter = new
        SuffixFilter(FileConstants.UNCOMPRESSED_FILE_EXT.substring(1), 
                     "Argo uncompressed project file");
  
    /**
     * This is a filter for compressed project format.
     */
    public static final SuffixFilter CompressedFileFilter = new
        SuffixFilter(FileConstants.COMPRESSED_FILE_EXT.substring(1), 
                     "Argo compressed project file");
  
    /**
     * This is a filter for xmi files.
     */
    public static final SuffixFilter XMIFilter = new
        SuffixFilter("xmi", "XML Metadata Interchange");
  
    /** This is for Precision Graphics Markup Language
     * a very old and now mostly dead standard.
     * see W3C.org for more info
     */  
    public static final SuffixFilter PGMLFilter = new
        SuffixFilter("pgml", "Argo diagram");

    /** This should read or write a config file
     * but as yet not fully implemented.
     */  
    public static final SuffixFilter ConfigFilter = new
        SuffixFilter("config", "Argo configutation file");

    /** History Filter...Argo has trouble with remembering
     * things at times. Maybe this filter helps.
     * status is unknown. last reveiwed 8 months ago.
     */  
    public static final SuffixFilter HistFilter = new
        SuffixFilter("hist", "Argo history file");

    public static final SuffixFilter LogFilter = new
        SuffixFilter("log", "Argo usage log");

    /** Java Source File Filter */
    public static final SuffixFilter JavaFilter = new
        SuffixFilter("java", "Java Source File");

    /** Java Class File Filter */
    public static final SuffixFilter JavaClassFilter = new
        SuffixFilter("class", "Java Class File");

    /** Java JAR File Filter */
    public static final SuffixFilter JavaJarFilter = new
        SuffixFilter("jar", "Java JAR File");

    /** This writes the GIF file, known issues
     * http://argouml.tigris.org/issues/show_bug.cgi?id=396
     * http://argouml.tigris.org/issues/show_bug.cgi?id=407
     *
     */  
    public static final SuffixFilter GIFFilter = new
        SuffixFilter("gif", "GIF image");

    public static final SuffixFilter PSFilter = new
        SuffixFilter("ps", "PostScript file");

    public static final SuffixFilter EPSFilter = new
        SuffixFilter("eps", "Encapsulated PostScript file");

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
    public static final SuffixFilter SVGFilter = new
        SuffixFilter("svg", "Scalable Vector Graphics file");
  
    /**
     * Returns the suffix for which a FileFilter filters. 
     * @param filter The FileFilter from which we want to know the suffix
     * @return String The suffix of the FileFilter. Returns null if the FileFilter is not an instance of SuffixFilter.
     */
    public static String getSuffix(FileFilter filter) {
        if (filter instanceof SuffixFilter) {
            return ((SuffixFilter)filter)._suffix;
        }
        return null;
    }

} /* end class FileFilters */
