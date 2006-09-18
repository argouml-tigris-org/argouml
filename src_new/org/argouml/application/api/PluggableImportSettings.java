// $Id$
// Copyright (c) 2006 The Regents of the University of California. All
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

package org.argouml.application.api;

/**
 * Interface for settings which are common to all pluggable importers.
 * 
 * @author Bogdan Pistol
 */
public interface PluggableImportSettings {
    
    /**
     * The path to the source where the import is located.
     * @return the path
     */
    String getSourcePath();
    
    /**
     * If we should descend in the whole directory tree.
     * @return true for descent or false otherwise
     */
    boolean isDescendRecursivelyEnabled();
    
    /**
     * If we consider changed files or only new files.  
     * @return true for changed files and false for new files only
     */
    boolean isChangedEnabled();
    
    /**
     * If we should create diagrams.
     * @return true for create and false for not to create
     */
    boolean isCreateDiagramsEnabled();
    
    /**
     * If we should not show all the attributes and methods or we can hide them.
     * @return if minimise is enabled
     */
    boolean isMinimiseEnabled();
    
    /**
     * If we layout the diagrams.
     * @return if the layout is enabled 
     */
    boolean isAutomaticDiagramLayoutEnabled();
    
    /**
     * The encoding for the files to parse.
     * @return the encoding type
     */
    String getSourceEncoding();
    
    /**
     * Returns the current import level that currently is intended for use.
     * <p>
     * If the user chose import levels higher than 0 then before using that
     * level we must use level 0. So the currentImportLevel can be different
     * from the level chose by the user.
     * @return the level of import
     */
    int getCurrentImportLevel();
    
    /**
     * Changes the level of import.
     * @param level the new level
     */
    void setCurrentImportLevel(int level);
    
}
