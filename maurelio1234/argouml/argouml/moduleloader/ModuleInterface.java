// $Id: ModuleInterface.java 11498 2006-11-18 07:36:14Z linus $
// Copyright (c) 2004-2006 The Regents of the University of California. All
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

package org.argouml.moduleloader;

/**
 * This is the interface that each module needs to implement in order to be
 * loadable into ArgoUML according to the new module loader API.
 *
 * @author Linus Tolke
 * @since 0.17.1
 */
public interface ModuleInterface {
    /**
     * Method to enable the module.<p>
     *
     * If it cannot enable the module because some other module is
     * not enabled it can return <code>false</code>.
     * In that case the module loader will defer this attempt until
     * all other modules are loaded (or until some more of ArgoUML is loaded
     * if at startup). Eventually it is only this and some other modules
     * that is not loaded and they will then be listed as having problems.
     *
     * @return true if all went well
     */
    boolean enable();

    /**
     * Method to disable the module.<p>
     *
     * If we cannot disable the module because some other module relies
     * on it, we return false. This will then make it impossible to turn off.
     * (An error is signalled at the attempt).
     *
     * @return true if all went well.
     */
    boolean disable();

    /**
     * The name of the module.<p>
     *
     * This should be a short string. For the purpose of having the GUI
     * that turns on and off the module look nice there is no whitespace in
     * this string (no spaces, tabs or newlines).<p>
     *
     * This name is also used as the key internally when modules checks for
     * other modules, if they are available.
     *
     * @return the name (A String).
     */
    String getName();

    /**
     * The info about the module.<p>
     *
     * This returns texts with information about the module.<p>
     *
     * The possible informations are retrieved by giving any of the
     * arguments:<ul>
     * <li>{@link #DESCRIPTION}
     * <li>{@link #AUTHOR}
     * <li>{@link #VERSION}
     * <li>{@link #DOWNLOADSITE}
     * </ul>
     *
     * If a module does not provide a specific piece of information,
     * <code>null</code> can be returned. Hence the normal implementation
     * should be:<pre>
     * public String getInfo(int type) {
     *     switch (type) {
     *     case DESCRIPTION:
     *         return "This module does ...";
     *     case AUTHOR:
     *         return "Annie Coder";
     *     default:
     *         return null;
     * }
     * </pre>
     *
     * @param type The type of information.
     * @return The description. A String.
     */
    String getInfo(int type);

    /**
     * The description of the module.
     */
    int DESCRIPTION = 0;

    /**
     * The author of the module.
     */
    int AUTHOR = 1;

    /**
     * The version of the module.
     */
    int VERSION = 2;

    /**
     * The URL of the website stating information on where to download the
     * module.
     */
    int DOWNLOADSITE = 3;
}
