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

//$Id$

package org.argouml.language.dummylanguage.reveng;

import org.argouml.kernel.*;
import org.argouml.uml.reveng.*;
import org.argouml.application.api.*;
import org.argouml.util.FileFilters;
import org.argouml.util.SuffixFilter;

import java.io.*;

/**
* Class for testing import interface.
* No real import is done.
 */
public class ImportDummy extends FileImportSupport {

	/**
	 * Provides an array of suffix filters for the module.
	 * @return SuffixFilter[] files with these suffixes will be processed.
	 */
	public SuffixFilter[] getSuffixFilters() {
		SuffixFilter[] result = {new SuffixFilter("dummy", "Dummy files")};
		return result;
	}

		/** Display name of the module. */
		public String getModuleName() {
			return "Dummy";
		}

		/** Textual description of the module. */
		public String getModuleDescription() {
			return "Dummy import from files";
		}

		public String getModuleKey() {
			return "module.import.dummy-files";
		}

}







