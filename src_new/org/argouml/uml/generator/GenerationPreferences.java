// $Id$
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

package org.argouml.uml.generator;

import java.util.*;
import java.io.*;

/** Stores preference information entered by the user. */

public class GenerationPreferences implements java.io.Serializable {
    ////////////////////////////////////////////////////////////////
    // instance variables
    protected String _headerComment =
	"Your copyright and other header comments";
    protected String _outputDir = "/home/jrobbins/vtmp/";

    public GenerationPreferences() {
	if (System.getProperty("file.separator").equals("/") )
	    _outputDir = "/tmp";
	else
	    _outputDir = "c:\\temp";
    }

    ////////////////////////////////////////////////////////////////
    // accessors
    public String getOutputDir() { return _outputDir; }
    public void setOutputDir(String od) { _outputDir = od; }

    public String getHeaderComment() { return _headerComment; }
    public void setHeaderComment(String c) { _headerComment = c; }

} /* end class GenerationPreferences */

