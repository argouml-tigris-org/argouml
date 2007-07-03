// $Id: ArgoEventTypes.java 12546 2007-05-05 16:54:40Z linus $
// Copyright (c) 1996-2007 The Regents of the University of California. All
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

package org.argouml.application.events;

/**
 * Definitions of Argo Event types.
 *
 * @author Thierry Lach
 * @since  ARGO0.9.4
 */
public interface ArgoEventTypes {

    /**
     * Id which matches any event.
     */
    int ANY_EVENT                 =  1000;

    /**
     * Id which matches any module event.
     */
    int ANY_MODULE_EVENT          =  1100;

    /**
     * Id indicating a module was loaded.
     */
    int MODULE_LOADED             =  1101;

    /**
     * Id indicating a module was unloaded.
     */
    int MODULE_UNLOADED           =  1102;

    /**
     * Id indicating a module was enabled.
     */
    int MODULE_ENABLED            =  1103;

    /**
     * Id indicating a module was disabled.
     */
    int MODULE_DISABLED           =  1104;

    /**
     * Last module event.
     */
    int LAST_MODULE_EVENT         =  1199;


    /**
     * Id indicating any notation event.
     */
    int ANY_NOTATION_EVENT        =  1200;

    /**
     * Id indicating the default notation was changed.
     */
    int NOTATION_CHANGED          =  1201;

    /**
     * Id indicating a notation was added.
     */
    int NOTATION_ADDED            =  1202;

    /**
     * Id indicating a notation was removed.
     */
    int NOTATION_REMOVED          =  1203;

    /**
     * Id indicating a notation provider was added.
     */
    int NOTATION_PROVIDER_ADDED   =  1204;

    /**
     * Id indicating a notation provider was removed.
     */
    int NOTATION_PROVIDER_REMOVED =  1205;

    /**
     * Last module event.
     */
    int LAST_NOTATION_EVENT       =  1299;


    /**
     * Id indicating any GENERATION event.
     */
    int ANY_GENERATOR_EVENT        =  1300;

    /**
     * Id indicating the default GENERATION was changed.
     */
    int GENERATOR_CHANGED          =  1301;

    /**
     * Id indicating a GENERATION was added.
     */
    int GENERATOR_ADDED            =  1302;

    /**
     * Id indicating a GENERATION was removed.
     */
    int GENERATOR_REMOVED          =  1303;

    /**
     * Last generation event.
     */
    int LAST_GENERATOR_EVENT       =  1399;


    /**
     * Id indicating any GENERATION event.
     */
    int ANY_HELP_EVENT        =  1400;

    /**
     * Id indicating the default GENERATION was changed.
     */
    int HELP_CHANGED          =  1401;

    /**
     * Id indicating a GENERATION was removed.
     */
    int HELP_REMOVED          =  1403;

    /**
     * Last generation event.
     */
    int LAST_HELP_EVENT       =  1499;
    

    /**
     * Id marker for the last Argo event.
     */
    int ARGO_EVENT_END            = 99999;
}
