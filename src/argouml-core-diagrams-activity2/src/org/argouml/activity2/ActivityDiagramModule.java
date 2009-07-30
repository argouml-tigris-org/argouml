// $Id: ActivityDiagramModule.java 16924 2009-03-23 17:35:16Z thn $
// Copyright (c) 2007 The Regents of the University of California. All
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

package org.argouml.class2;

import org.argouml.moduleloader.ModuleInterface;

/**
 * The Class Diagram Module description.
 *
 * @see org.argouml.moduleloader.ModuleInterface
 */
public class ActivityDiagramModule implements ModuleInterface {

    public boolean enable() {
        return true;
    }

    public boolean disable() {
        return true;
    }

    public String getName() {
        return "ArgoUML-Class";
    }

    public String getInfo(int type) {
        switch (type) {
        case DESCRIPTION:
            return "The new activity diagram implementation";
        case AUTHOR:
            return "ArgoUML Core Development Team";
        case VERSION:
            return "0.28";
        case DOWNLOADSITE:
            return "http://argouml.tigris.org";
        default:
            return null;
        }
    }
}