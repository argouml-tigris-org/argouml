// Copyright (c) 1996-98 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation for educational, research and non-profit
// purposes, without fee, and without a written agreement is hereby granted,
// provided that the above copyright notice and this paragraph appear in all
// copies. Permission to incorporate this software into commercial products may
// be obtained by contacting the University of California. David F. Redmiles
// Department of Information and Computer Science (ICS) University of
// California Irvine, California 92697-3425 Phone: 714-824-3823. This software
// program and documentation are copyrighted by The Regents of the University
// of California. The software program and documentation are supplied "as is",
// without any accompanying services from The Regents. The Regents do not
// warrant that the operation of the program will be uninterrupted or
// error-free. The end-user understands that the program was developed for
// research purposes and is advised not to rely exclusively on the program for
// any reason. IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY
// PARTY FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES,
// INCLUDING LOST PROFITS, ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS
// DOCUMENTATION, EVEN IF THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY
// DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
// WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE
// SOFTWARE PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,
// ENHANCEMENTS, OR MODIFICATIONS.

// File: RedrawManager.java
// Classes: RedrawManager
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.awt.*;
import java.awt.image.*;
import java.util.*;


/** Stores a list of rectangles (and sometimes merges them) for use in
 *  determing the invalid region of a Editor. When a Fig changes
 *  state, it notifies its Layer which notifies all Editors that are
 *  viewing it, and the Editor adds that Fig to its RedrawManager.
 *  Eventually, the RedrawManager will ask the Editor to repaint
 *  damaged regions.  It is important that the redraw not happen too
 *  soon after the damage, because we would like to use a single
 *  repaint to repair multple damages to the same part of the screen.
 *  This is handled by keeping a timer and ignoring redraw requests
 *  that come too soon after the last one.  Also it is important that
 *  the redraw not happen while the Editor is processing an event that
 *  could change the state of the Figs, because that can result in
 *  screen-dirt. This is handled by the locking and unlocking the
 *  RedrawManager. */

public class RedrawManager implements Runnable {

  ////////////////////////////////////////////////////////////////
  // constants

  /** The maximum number of damaged rectangles. */
  private final int MAX_NUM_RECTS = 10;

  ////////////////////////////////////////////////////////////////
  // instance variables

  /** The array of damaged rectangles. */
  private Rectangle[] _rects = new Rectangle[MAX_NUM_RECTS];

  /** The current number of damaged rectangles. */
  private int _nRects = 0;

  /** The editor that controls this RedrawManager. */
  private Editor _ed;

  /** The thread spawned to periodically request repaints. */
  private Thread _repairThread;

  /** Time at which to request next repaint. */
  private long deadline = 0;

  /** Milliseconds between most recent addition of damage and next
   *  redraw. */
  private static long _timeDelay = 25;

  private static String LOCK = new String("LOCK"); // could be any object
  private static int _lockLevel = 0;

  ////////////////////////////////////////////////////////////////
  // constructors

  /** Construct a new RedrawManager */
  public RedrawManager(Editor ed) {
    for (int i = 0; i < MAX_NUM_RECTS; ++i) _rects[i] = new Rectangle();
    _ed = ed;
    _repairThread = new Thread(this, "RepairThread");
    // Needs-More-Work: this causes a security violation in Netscape
    // _repairThread.setDaemon(true);
    // _repairThread.setPriority(Thread.MAX_PRIORITY);
    _repairThread.start();
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  /** Get the minimum number of milliseconds between damage being
   *  added and a repair being done. */
  public static long getTimeBetweenRepairs() { return _timeDelay; }

  /** Set the minimum number of milliseconds between damage being
   *  added and a repair being done. */
  public static void setTimeBetweenRepairs(long t) { _timeDelay = t; }

  /** If screen repainting is fast enough, then try do it more
   *  often. This can be called from the Editor to try to optimize the
   *  tradeoff between screen updates and latency in event
   *  processing. */
  public static void moreRepairs() {
    _timeDelay -= 5;
    if (_timeDelay < 10) _timeDelay = 10;
  }

  /** If screen repainting is getting really slow and the application
   *  cannot process events, then make redraws less frequent */
  public static void fewerRepairs() {
    _timeDelay += 50;
    if (_timeDelay > 2000) _timeDelay = 2000;
  }

  public static void setFramesPerSecond(float fps) {
    if (fps > 100.0 || fps < 0.5) return;
    int _timeDelay = (int) (1000/fps);
  }

  public static float getFramesPerSecond() { return (float) (1000.0 / _timeDelay); }

  ////////////////////////////////////////////////////////////////
  // managing damage

  /** Internal function to forget all damage. */
  private void removeAllElements() { _nRects = 0; }

  /** Reply true iff some damage has been added but not yet redrawn. */
  public boolean pendingDamage() { return _nRects != 0; }

  /** Add a new rectangle of damage, it will soon be
   * redrawn. needs-more-work: how much time is spent here? Could it
   * be faster if I pass in Figs instead of Rectangles?  How much
   * garbage is generated when I call Fig.getBounds()? */
  public void add(Rectangle r) {
    if (r.isEmpty()) return;
    synchronized (LOCK) {
      if (!merge(r)) _rects[_nRects++].reshape(r.x, r.y, r.width, r.height);
      if (_nRects == MAX_NUM_RECTS) forceMerge();
      if (deadline == 0) deadline = System.currentTimeMillis() + _timeDelay;
    }
  }

  public void add(Fig f) {
    synchronized (LOCK) {
      f.stuffBounds(_rects[_nRects]);
      if (!merge(_rects[_nRects])) _nRects++;
      if (_nRects == MAX_NUM_RECTS) forceMerge();
      if (deadline == 0) deadline = System.currentTimeMillis() + _timeDelay;
    }
  }

  public void add(Selection sel) {
    synchronized (LOCK) {
      sel.stuffBounds(_rects[_nRects]);
      if (!merge(_rects[_nRects])) _nRects++;
      if (_nRects == MAX_NUM_RECTS) forceMerge();
      if (deadline == 0) deadline = System.currentTimeMillis() + _timeDelay;
    }
  }

  /** Try to merge the given rect into one of my existing damaged
   *  rects. Rects can be merged if they are overlapping. In general,
   *  they shuold be merged when the area added by the merger is
   *  small enough that one large repaint is faster than two smaller
   *  repaints. Reply true on success. */
  private boolean merge(Rectangle r) {
    for (int i = 0; i < _nRects; ++i)
      if (r.intersects(_rects[i])) { _rects[i].add(r); return true; }
    return false;
  }

  /** Merge all the rectangles together, even if that means that a lot
   *  more area is redrawn than needs to be. */
  public void forceMerge() {
    for (int i = 1; i < _nRects; ++i) _rects[0].add(_rects[i]);
    _nRects = 1;
    deadline = 1;
  }

  ////////////////////////////////////////////////////////////////
  // locking

  /** Lock all RedrawManagers during changes to the diagram. This
   *  prevents repainting of Fig's that are in invalid
   *  states. Needs-More-Work: This takes a fair amount of time. */
  public static void lock() { synchronized (LOCK) { _lockLevel++; } }

  /** Unlock all RedrawManager after changes to the diagram. This
   *  prevents repainting of Fig's that are in invalid
   *  states. Needs-More-Work: This takes a fair amount of time. */
  public static void unlock() {
    synchronized (LOCK) {
      _lockLevel--;
      if (_lockLevel < 0) _lockLevel = 0;
    }
  }

  ////////////////////////////////////////////////////////////////
  // repaint and frame-rate logic

  /** The main method of the _repairThread, basically it just keeps
   *  checking if there is damage that has not been repaired, and that
   *  damage is old enough. */
  public void run() {
    while (true) {
      try { _repairThread.sleep(_timeDelay * 10); }
      catch (InterruptedException ignore) { }
      repairDamage();
    }
  }

  /** Ask the Editor to repaint all damaged regions, if the Editor is
   *  ready and there are not Fig transactions in progress. */
  public void repairDamage() {
    Graphics g = _ed.getGraphics();
    if (_lockLevel == 0 && g != null)
      synchronized (LOCK) { if (_lockLevel == 0) paint(_ed, g); }
  }

  /** Ask the Editor to repaint all damaged regions, either on screen
   *  or off screen */
  private void paint(Editor ed, Graphics g) {
    long startTime = System.currentTimeMillis();
    if (startTime > deadline) {
      if (Globals.getPrefs().shouldPaintOffScreen()) paintOffscreen(ed, g);
      else paintOnscreen(ed, g);
      Globals.getPrefs().lastRedrawTime(System.currentTimeMillis() - startTime);
      deadline = 0;
    }
  }

  /** Ask the Editor to repaint damaged Rectangle's on the Graphics for
   *  the drawing window. This allows the user to see some flicker, but
   *  it gives more feedback that something is happening if the
   *  computer is slow or the diagram is complex. */
  private void paintOnscreen(Editor ed, Graphics g) {
    int F = 16; // fudgefactor, extra repaint area;
    if (ed == null || g == null) return;
    for (int i = 0; i < _nRects; ++i) {
      Rectangle r = _rects[i];
      Graphics offG = g.create();
      //offG.setColor(ed.getBackground());
      // offG.fillRect(r.x, r.y, r.width, r.height);
      offG.clearRect(r.x-F, r.y-F, r.width+F*2, r.height+F*2);
      offG.clipRect(r.x-F-1, r.y-F-1, r.width+F*2+2, r.height+F*2+2);
      ed.paint(offG);
      offG.dispose();
    }
    removeAllElements();
  }

  /** Ask the Editor to repaint damaged Rectangle's on an off screen
   *  Image, and then bitblt that Image to the Graphics used by the
   *  drawing window. This takes more time, but the user will never
   *  see any flicker. */
  private void paintOffscreen(Editor ed, Graphics g) {
    int F = 16; // fudgefactor, extra redraw area;
    Image offscreen;

    if (ed == null || g == null) return;
    for (int i = 0; i < _nRects; ++i) {
      Rectangle r = _rects[i];
      r.reshape(r.x-F, r.y-F, r.width+F*2, r.height+F*2);
      offscreen = findReusedImage(r.width, r.height, ed);
      r.width = offscreen.getWidth(null);
      r.height = offscreen.getHeight(null);
      if (offscreen == null) {
	System.out.println("failed to alloc image!!!");
	paintOnscreen(ed, g);
	return;
      }
      Graphics offG = offscreen.getGraphics();
      offG.translate(-r.x, -r.y);
      offG.setColor(ed.getBackground());
      offG.fillRect(r.x, r.y, r.width, r.height);
      //offG.clearRect(r.x, r.y, r.width, r.height);
      offG.clipRect(r.x-1, r.y-1, r.width+2, r.height+2);
      ed.paint(offG);
      // It is important that paintRect and the various Fig paint()
      // routines do not attempt to call add to register damage. There
      // should not be any reason to do that. It would cause deadlock...
      g.drawImage(offscreen, r.x, r.y, null);
      offG.dispose();
      offscreen.flush();
    }
    removeAllElements();
  }

  /** Images used to draw off screen in common sizes. Creating these
   * images on first use and saving them for later uses, avoids all
   * lot of allocation and deallocation of large objects. */
  protected Image image64x64, image256x64, image64x256, image256x256,
    image64x512, image512x64, image512x512;

  /** Reply an Image that is as least as big as (x, y), hopefully not
   * too much larger.  If no retained image is suitable, ask the given
   * Editor to make a new one. */
  protected Image findReusedImage(int x, int y, Editor ed) {
    if (x < 64 && y < 64) {
      if (image64x64 == null) image64x64 = ed.createImage(64, 64);
      return image64x64;
    }
    else if (x < 256 && y < 64) {
      if (image256x64 == null) image256x64 = ed.createImage(256, 64);
      return image256x64;
    }
    else if (x < 64 && y < 256) {
      if (image64x256 == null) image64x256 = ed.createImage(64, 256);
      return image64x256;
    }
    else if (x < 256 && y < 256) {
      if (image256x256 == null) image256x256 = ed.createImage(256, 256);
      return image256x256;
    }
    else if (x < 64 && y < 512) {
      if (image64x512 == null) image64x512 = ed.createImage(64, 512);
      return image64x512;
    }
    else if (x < 512 && y < 64) {
      if (image512x64 == null) image512x64 = ed.createImage(512, 64);
      return image512x64;
    }
    else if (x < 512 && y < 512) {
      if (image512x512 == null) image512x512 = ed.createImage(512, 512);
      return image512x512;
    }
    else return ed.createImage(x, y);
  }

} /* end class RedrawManager */

