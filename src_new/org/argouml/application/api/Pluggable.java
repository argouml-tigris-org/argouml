// $Id$
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

package org.argouml.application.api;

/**
 *   An interface which identifies an ArgoUML plug-in.
 *
 *   Plug-ins are specific modules which are replacements
 *   or additions to standard Argo classes.
 *   Argo references the plug-in functionality through interfaces which
 *   define the features of each type of plug-in.<p>
 *
 *   This interface must be extended by another interface which is used
 *   to categorize the plugins.  An example of this is the
 *   <code>PluggableNotation</code> interface,
 *   which provides all the required functionality to request
 *   a notation generator of a notation dialect.<p>
 *
 *   Argo will ask the <code>ModuleLoader</code>
 *   for a plug-in of a certain type.
 *   The loader will determine which plug-in to use and return a
 *   concrete class which implements that interface.<p>
 *
 *   It is the intent of this class that any Argo feature that can be
 *   replaced or extended by this means be callable only by this means.
 *
 *   @author Thierry Lach
 *   @since  0.9.4
 *   @deprecated by Linus Tolke (0.21.1 March 2006).
 *         Call registration in the appropriate subsystem from
 *         {@link org.argouml.moduleloader.ModuleInterface#enable()}.
 *         If the needed registration is not available, add it!
 */
public interface Pluggable extends ArgoModule {

    /**
     * Constant required in plugin manifest.
     * @deprecated
     */
    String PLUGIN_TITLE = "ArgoUML Dynamic Load Module";

    /**
     * Constant required in plugin manifest.
     * @deprecated
     */
    String PLUGIN_VENDOR = "ArgoUML team";

    /**
     * Constant required in plugin manifest.
     * @deprecated
     */
    String PLUGIN_PREFIX =
        "org.argouml.application.api.Pluggable";

    /**
     *  A function which allows a plug-in to decide if it is available
     *  under a specific context.
     *
     *  One example of a plugin with multiple criteria is the PluggableMenu.
     *  PluggableMenu requires the first context to be a JMenuItem
     *  which wants the PluggableMenu attached to as the context,
     *  so that it can determine that it would attach to a menu.  The
     *  second context is an internal (non-localized) description
     *  of the menu such as "File" or "View"
     *  so that the plugin can further decide.
     *
     *  @param context An identification of the context.
     *                 The interpretation of criteria is specific to
     *                 the plug-in type, but must be consistent
     *                 across that type.  The plug-in must want
     *                 to be exposed to all contexts.
     *
     *  @return True if the plug-in wants to make itself available
     *          for this context, otherwise false.
     *  @deprecated
     */
    boolean inContext(Object[] context);

} /* end interface Pluggable */
