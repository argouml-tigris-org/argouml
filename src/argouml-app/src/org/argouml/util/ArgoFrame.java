/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    tfmorris
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2006,2009 The Regents of the University of California. All
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

package org.argouml.util;

import java.awt.Frame;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Helper class to store/find a top level application frame.
 *
 * This has been factored out of ProjectBrowser solely to allow easy
 * identification of things that want a ProjectBrowser because it is the top
 * level JFrame and those things that want to actually call a ProjectBrowser
 * method for some other purpose.
 *
 * @author Tom Morris
 *
 */
public class ArgoFrame {

    private static final Logger LOG =
        Logger.getLogger(ArgoFrame.class.getName());

    private static Frame topFrame;

    private ArgoFrame() {
        // prohibit instantiation
    }

    /**
     * Get a top level JFrame which can be used as the parent for creating new
     * dialogs. The method name and return type were the same as the old
     * ProjectBrowser.getInstance() usage for compatibility, but getFrame should
     * be used for new uses.
     *
     * @return a top level JFrame to use as parent for new dialogs
     * @deprecated for 0.29.1 by tfmorris. Use {@link #getFrame()}.
     */
    public static JFrame getInstance() {
        Frame frame = getFrame();
        if (frame instanceof JFrame) {
            return (JFrame) frame;
        }
        return null;
    }
    /**
     * Get a top level frame which can be used as the parent for creating new
     * dialogs.
     *
     * @return a top level JFrame to use as parent for new dialogs
     */
    public static Frame getFrame() {
        if (topFrame == null) {
            Frame rootFrame = JOptionPane.getRootFrame();
            if ( rootFrame instanceof JFrame) {
                setFrame(rootFrame);
            } else {
                Frame[] frames = Frame.getFrames();
                for (int i = 0; i < frames.length; i++) {
                    if (frames[i] instanceof JFrame) {
                        if (topFrame != null) {
                            LOG.log(Level.WARNING, "Found multiple JFrames");
                        } else {
                            setFrame(frames[i]);
                        }
                    }
                }
                if (topFrame == null) {
                    LOG.log(Level.WARNING,
                            "Failed to find JFrame - using rootFrame");
                    setFrame(JOptionPane.getRootFrame());
                }
            }
        }

        return topFrame;
    }

    /**
     * Set the given JFrame to use as the main application frame.
     *
     * @param frame the main application frame.
     * @deprecated for 0.29.1 by tfmorris.  Use {@link #setFrame(Frame)}.
     */
    @Deprecated
    public static void setInstance(JFrame frame) {
        setFrame(frame);
    }

    /**
     * Set the given Frame to use as the main application frame.
     *
     * @param frame the main application frame.
     */
    public static void setFrame(Frame frame) {
        topFrame = frame;
        ArgoDialog.setFrame(topFrame);
    }
}
