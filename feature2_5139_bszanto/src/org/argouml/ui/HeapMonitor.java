// $Id$
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

package org.argouml.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.MessageFormat;

import javax.swing.JComponent;
import javax.swing.Timer;

/**
 * Swing component to monitor Java heap usage.
 * 
 * @author Tom Morris <tfmorris@gmail.com>
 */
public class HeapMonitor extends JComponent implements ActionListener {

    // % thresholds for bar color changes
    private static final int ORANGE_THRESHOLD = 70;
    private static final int RED_THRESHOLD = 90;
    
    private static final Color GREEN = new Color(0, 255, 0);
    private static final Color ORANGE  = new Color(255, 190, 125);
    private static final Color RED = new Color(255, 70, 70);

    private static final long M = 1024 * 1024;
    
    // Virtual memory (heap) stats
    private long free;
    private long total;
    private long max;
    private long used;
    
    /**
     * Construct a graphical JVM heap monitor component.
     */
    public HeapMonitor() {
         super();
        Dimension size = new Dimension(200, 20);
        setPreferredSize(size);

        // TODO: Add a button to force garbage collection

        updateStats();

        Timer timer = new Timer(1000, this);
        timer.start();
    }
    
    public void paint (Graphics g) {        
        Rectangle bounds = getBounds();
        int usedX = (int) (used * bounds.width / total);
        int warnX = ORANGE_THRESHOLD * bounds.width / 100;
        int dangerX = RED_THRESHOLD * bounds.width / 100;
        
        Color savedColor = g.getColor();
        
//      g.setColor(GREEN);
        // TODO: We want something minimally distracting here.  Another option
        // might be just the background color with a solid end line.
        g.setColor(getBackground().darker());
        g.fillRect(0, 0, Math.min(usedX, warnX), bounds.height);
        
        g.setColor(ORANGE);
        g.fillRect(warnX, 0, 
                Math.min(usedX - warnX, dangerX - warnX), 
                bounds.height);
        
        g.setColor(RED);
        g.fillRect(dangerX, 0, 
                Math.min(usedX - dangerX, bounds.width - dangerX), 
                bounds.height);

        g.setColor(getForeground());

        String s = MessageFormat.format("{0}M used of {1}M total",
                new Object[] {(long) (used / M), (long) (total / M) });
        int x = (bounds.width - g.getFontMetrics().stringWidth(s)) / 2;
        int y = (bounds.height + g.getFontMetrics().getHeight()) / 2;
        g.drawString(s, x, y);
        
        g.setColor(savedColor);
    }

    /*
     * Timer action method.  Periodically update our stats and force a repaint.
     */
    public void actionPerformed(ActionEvent e) {
        updateStats();
        repaint();
    }
    
    private void updateStats() {
        free = Runtime.getRuntime().freeMemory();
        total = Runtime.getRuntime().totalMemory();
        max = Runtime.getRuntime().maxMemory();
        used = total - free;

        String tip = MessageFormat.format(
                "Heap use: {0}%  {1}M used of {2}M total.  Max: {3}M", 
                new Object[] {used * 100 / total, (long) (used / M),
                              (long) (total / M), (long) (max / M)
                });
        setToolTipText(tip);
    }
    
}
