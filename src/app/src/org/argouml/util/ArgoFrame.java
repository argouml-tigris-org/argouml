// $Id$
// Copyright (c) 2005 The Regents of the University of California. All
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

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

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

    private static final Logger LOG = Logger.getLogger(ArgoFrame.class);
    
    private static JFrame topFrame;
    
    private ArgoFrame() {
        // prohibit instantiation
    }
    
    /**
     * Get a top level frame which can be used as the parent for creating new
     * dialogs. The name is temporarily the same as the old
     * ProjectBrowser.getInstance() usage for compatibility. The implementation
     * is just roughed out for experimentation.
     * 
     * @return a top level JFrame to use as parent for new dialogs
     */
    public static JFrame getInstance() {
        if (topFrame == null) {
            Frame rootFrame = JOptionPane.getRootFrame();
            if ( rootFrame instanceof JFrame) {
                topFrame = (JFrame) rootFrame;
            } else {
                Frame[] frames = Frame.getFrames();
                for (int i = 0; i < frames.length; i++) {
                    if (frames[i] instanceof JFrame) {
                        if (topFrame != null) {
                            LOG.warn("Found multiple JFrames");
                        } else {
                            topFrame = (JFrame) frames[i];
                        }
                    }
                }
                if (topFrame == null) {
                    LOG.warn("Failed to find application JFrame");
                }
            }
            ArgoDialog.setFrame(topFrame);
        }

        return topFrame;
    }

    /**
     * Set the JFrame to use as the main application frame.
     * 
     * @param frame the main application frame.
     */
    public static void setInstance(JFrame frame) {
        topFrame = frame;
    }
}
