// $Id$
// Copyright (c) 1996-2001 The Regents of the University of California. All
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
import org.argouml.application.api.*;
import java.util.*;

/** Definitions of Argo Event types.
 *
 *  @author Thierry Lach
 *  @since  ARGO0.9.4
 */
public interface ArgoEventTypes {

    public static final int ANY_EVENT                 =  1000;
		  		 
    public static final int ANY_MODULE_EVENT          =  1100;
    public static final int MODULE_LOADED             =  1101;
    public static final int MODULE_UNLOADED           =  1102;
    public static final int MODULE_ENABLED            =  1103;
    public static final int MODULE_DISABLED           =  1104;
		  		 
    public static final int ANY_NOTATION_EVENT        =  1200;
    public static final int NOTATION_CHANGED          =  1201;
    public static final int NOTATION_ADDED            =  1202;
    public static final int NOTATION_REMOVED          =  1203;
    public static final int NOTATION_PROVIDER_ADDED   =  1204;
    public static final int NOTATION_PROVIDER_REMOVED =  1205;
		  		 
    public static final int ARGO_EVENT_END            = 99999;
}
