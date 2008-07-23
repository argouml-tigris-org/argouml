// $Id$
// Copyright (c) 2007-2008 The Regents of the University of California. All
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

package org.argouml.application.helpers;

/**
 * Receives the version of the application at initialisation time.
 * Also knows about website locations that depend on the version of ArgoUML.
 *
 * @author Michiel
 */
public class ApplicationVersion {
    /**
     * Version number.
     */
    private static String version;
    private static String stableVersion;

    /**
     * Retrieve the version number of the application.
     *
     * @return the version number.
     */
    public static String getVersion() {
        return version;
    }
    
    /**
     * Retrieve the online HTML manual of ArgoUML for a critic. 
     * You need to append the critic class-name.
     * This syntax is synchronized with:
     * <ol>
     * <li>Tags in the manual.
     * <li>Name of the ArgoUML site.
     * <li>How the manual is deployed on the site.
     * </ol>
     * so this must be updated when any of these change.
     *
     * @return the URL
     */
    public static String getManualForCritic() {
        return "http://argouml-stats.tigris.org/documentation/"
            + "manual-" 
            + stableVersion
            + "-single/argomanual.html#critics.";
    }

    /**
     * Retrieve the URL of the online manual of ArgoUML.
     * 
     * @return the URL
     */
    public static String getOnlineManual() {
        return "http://argouml-stats.tigris.org/nonav/documentation/"
                + "manual-" + stableVersion + "/";
    }

    /**
     * Retrieve the URL of the location for online support for ArgoUML.
     * 
     * @return the URL
     */
    public static String getOnlineSupport() {
        return "http://argouml.tigris.org/nonav/support.html";
    }

    /**
     * Don't allow instantiation.
     */
    private ApplicationVersion() {
    }

    /**
     * @param v The version to set.
     * @param sv The stable version to set.
     */
    public static void init(String v, String sv) {
        assert version == null;
        version = v;
        assert stableVersion == null;
        stableVersion = sv;
    }
}
