// $Id$
// Copyright (c) 2005 The Regents of the University of California. All
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

package org.argouml.language.cpp.reveng;

import java.io.File;
import java.io.FileInputStream;

import org.apache.log4j.Logger;
import org.argouml.kernel.Project;
import org.argouml.uml.reveng.DiagramInterface;
import org.argouml.uml.reveng.FileImportSupport;
import org.argouml.uml.reveng.Import;
import org.argouml.util.SuffixFilter;

/**
 * Implementation of the reverse engineering interface of ArgoUML,
 * <code>PluggableImport</code>, for the C++ module.
 * 
 * FIXME i18n support?!
 * 
 * @author Luis Sergio Oliveira (euluis)
 * @version 0.00
 * @since 0.19.2
 */
public class CppImport extends FileImportSupport {

    /** logger */
    private static final Logger LOG = Logger.getLogger(CppImport.class);

    /**
     * @see org.argouml.application.api.PluggableImport#parseFile(
     *      org.argouml.kernel.Project, java.lang.Object,
     *      org.argouml.uml.reveng.DiagramInterface,
     *      org.argouml.uml.reveng.Import)
     */
    public void parseFile(Project p, Object o, DiagramInterface diagram,
            Import theImport) throws Exception {
        LOG.warn("Not fully implemented yet!");
        if (o instanceof File) {
            File f = (File) o;
            FileInputStream in = new FileInputStream(f);
            try {
                Modeler modeler = new ModelerImpl();
                CPPLexer lexer = new CPPLexer(in);
                CPPParser parser = new CPPParser(lexer);
                parser.translation_unit(modeler);
            } finally {
                in.close();
            }
        } else
            LOG.error("o isn't a File!");
    }

    /**
     * The suffix filters for C++ files. Header sufixes are left out, since the
     * module should deal with files that originate translation units.
     */
    private static final SuffixFilter[] CPP_SUFFIX_FILTERS = {
        new SuffixFilter("cxx", "C++ source files"),
        new SuffixFilter("c++", "C++ source files"),
        new SuffixFilter("C++", "C++ source files"),
        new SuffixFilter("CPP", "C++ source files"),
        new SuffixFilter("cpp", "C++ source files"), };

    /**
     * TODO: I would like that an option to have all suffix applied to exist.
     * Maybe this has to be fixed within ArgoUML...
     * 
     * @see org.argouml.uml.reveng.FileImportSupport#getSuffixFilters()
     */
    public SuffixFilter[] getSuffixFilters() {
        return CPP_SUFFIX_FILTERS;
    }

    /**
     * @see org.argouml.application.api.ArgoModule#getModuleName()
     */
    public String getModuleName() {
        return "C++";
    }

    /**
     * @see org.argouml.application.api.ArgoModule#getModuleDescription()
     */
    public String getModuleDescription() {
        return "C++ reverse engineering support";
    }

    /**
     * @see org.argouml.application.api.ArgoModule#getModuleKey()
     */
    public String getModuleKey() {
        return "module.language.cpp.reveng";
    }

    /**
     * @see org.argouml.application.api.ArgoModule#getModuleAuthor()
     */
    public String getModuleAuthor() {
        return "Luis Sergio Oliveira (euluis)";
    }

    /**
     * @see org.argouml.application.api.ArgoModule#getModuleVersion()
     */
    public String getModuleVersion() {
        return "0.00";
    }

}