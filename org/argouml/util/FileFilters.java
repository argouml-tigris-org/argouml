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

package org.argouml.util;

public class FileFilters {

  ////////////////////////////////////////////////////////////////
  // constants

  public static final SuffixFilter ArgoFilter = new
  SuffixFilter("argo", "Argo project file");

  public static final SuffixFilter XMIFilter = new
  SuffixFilter("xmi", "Argo model file");

  public static final SuffixFilter PGMLFilter = new
  SuffixFilter("pgml", "Argo diagram");

  public static final SuffixFilter ConfigFilter = new
  SuffixFilter("config", "Argo configutation file");

  public static final SuffixFilter HistFilter = new
  SuffixFilter("hist", "Argo history file");

  public static final SuffixFilter LogFilter = new
  SuffixFilter("log", "Argo usage log");

  public static final SuffixFilter GIFFilter = new
  SuffixFilter("gif", "GIF image");

  public static final SuffixFilter PSFilter = new
  SuffixFilter("ps", "PostScript file");

  public static final SuffixFilter EPSFilter = new
  SuffixFilter("eps", "Encapsulated PostScript file");

  public static final SuffixFilter SVGFilter = new
  SuffixFilter("svg", "Scalable Vector Graphics file");

} /* end class FileFilters */
