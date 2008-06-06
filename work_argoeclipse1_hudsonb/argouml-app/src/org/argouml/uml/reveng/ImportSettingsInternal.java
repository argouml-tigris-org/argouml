// $Id$
// Copyright (c) 2006 The Regents of the University of California. All
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

package org.argouml.uml.reveng;

/**
 * Extended version of ImportSettings which must be implemented by GUI
 * implementations. NOT FOR USE BY IMPORT MODULES. It is a superset of the
 * settings available to pluggable import modules.
 */

public interface ImportSettingsInternal extends ImportSettings {

    /**
     * @return true if the directory tree should be descended recursively
     *         importing all parseable files.
     */
    public boolean isDescendSelected();

    /**
     * @return true if user as requested that only sources files which have been
     *         changed since the last import should be imported this time. If
     *         false, all files should be imported, regardless of their
     *         modification date.
     */
    public boolean isChangedOnlySelected();
    

    /**
     * @return true if the user has requested automatic layout for figures
     *         placed on diagrams.
     */
    public boolean isDiagramLayoutSelected();

    /**
     * TODO: This should be moved from the superclass when diagram updating
     * removed from the importers (as it should be). - tfm 20061129
     * 
     * @return true if the user has request diagrams to be created for packages
     *         contained in the imported source code.
     */
//    public boolean isCreateDiagramsSelected();
    
    /**
     * TODO: This should be moved from the superclass when diagram updating
     * removed from the importers (as it should be). - tfm 20061129
     * 
     * @return true, if user has requested that new figures placed in diagrams
     *         should be minimized so they don't show internal compartments.
     */
//    public boolean isMinimizeFigsSelected();


}
