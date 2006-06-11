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
 * An interface which must be implemented as the UI for
 * each primary panel.
 *
 * @author Thierry Lach
 * @since 0.9.5
 */
public interface QuadrantPanel {

    /**
     * The bit-number for a side.
     */
    int Q_TOP           = 1;

    /**
     * The bit-number for a side.
     */
    int Q_BOTTOM        = 2;

    /**
     * The bit-number for a side.
     */
    int Q_LEFT          = 4;

    /**
     * The bit-number for a side.
     */
    int Q_RIGHT         = 8;

    /**
     * A bit-combination indicating uniquely 2 orthogonal sides,
     * and hence a corner.
     */
    int Q_TOP_LEFT      = Q_TOP + Q_LEFT;

    /**
     * A bit-combination indicating uniquely 2 orthogonal sides,
     * and hence a corner.
     */
    int Q_TOP_RIGHT     = Q_TOP + Q_RIGHT;

    /**
     * A bit-combination indicating uniquely 2 orthogonal sides,
     * and hence a corner.
     */
    int Q_BOTTOM_LEFT   = Q_BOTTOM + Q_LEFT;

    /**
     * A bit-combination indicating uniquely 2 orthogonal sides,
     * and hence a corner.
     */
    int Q_BOTTOM_RIGHT  = Q_BOTTOM + Q_RIGHT;

    /**
     * This shall return a corner indentification.
     * @return one of Q_TOP_LEFT, Q_TOP_RIGHT, Q_BOTTOM_LEFT, Q_BOTTOM_RIGHT
     */
    int getQuadrant();

} /* End interface QuadrantPanel */

