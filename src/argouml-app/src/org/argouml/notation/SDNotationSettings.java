// $Id$
// Copyright (c) 2008 Tom Morris and other contributors. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Contributors.
// The software program and documentation are supplied "AS
// IS", without any accompanying services from The Contributors. The 
// Contributors do not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
// CONTRIBUTORS BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE CONTRIBUTORS HAVE BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE CONTRIBUTORS SPECIFICALLY DISCLAIM ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE CONTRIBUTORS
// HAVE NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

package org.argouml.notation;

/**
 * Notation settings for a sequence diagram.
 * 
 * @author Tom Morris <tfmorris@gmail.com>
 */
public class SDNotationSettings extends NotationSettings {

    private boolean showSequenceNumbers;

    /**
     * @return the showSequenceNumbers setting
     */
    public boolean isShowSequenceNumbers() {
        return showSequenceNumbers;
    }

    /**
     * @param showThem true to show sequence numbers
     */
    public void setShowSequenceNumbers(boolean showThem) {
        this.showSequenceNumbers = showThem;
    }

}
