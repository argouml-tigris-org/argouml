// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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

/*REMOVE_BEGIN*/
package org.argouml.language.cpp.reveng;

/*REMOVE_END*/

import java.io.File;

/**
 * The Modeler receives calls from some reverse engineering system and is able
 * to transform this information into model elements. It expects to receive
 * calls in a logic sequence:
 * <ol>
 * <li>beginClassDeclaration</li>
 * <li>derivedFromClass</li>
 * <li>beginMemberDeclaration</li>
 * <li>endMemberDeclaration</li>
 * <li>endClassDeclaration</li>
 * </ol>
 * 
 * @author Luis Sergio Oliveira
 * @version 0.00
 * @since 0.19.1
 */
public interface Modeler {

    /**
     * Callback that signals the begin of a compilation unit.
     * 
     * @param file
     *            Name of the file that contains the compilation unit
     */
    public void beginCompilationUnit(File file);

    /**
     * Callback that signals the end of the compilation unit.
     */
    public void endCompilationUnit();

    /**
     * Callback that signals the begin of the translation unit.
     */
    public void beginTranslationUnit();

    /**
     * Callback that signals the end of the translation unit.
     */
    public void endTranslationUnit();
}