// Copyright (c) 1996-99 The Regents of the University of California. All
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



// File: FigText.java
// Classes: FigText
// Original Author: ics125 spring 1996
// $Id$

package uci.gef;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.beans.*;
import javax.swing.*;
import javax.swing.event.*;

import uci.ui.*;
import uci.util.*;

/** This class handles painting and editing text Fig's in a
 *  LayerDiagram. Needs-More-Work: should eventually allow styled text
 *  editing, ... someday... */

public class FigText extends Fig implements KeyListener, MouseListener {

  ////////////////////////////////////////////////////////////////
  // constants

  /** Constants to specify text justification. */
  public static final int JUSTIFY_LEFT = 0;
  public static final int JUSTIFY_RIGHT = 1;
  public static final int JUSTIFY_CENTER = 2;

  /** Minimum size of a FigText object. */
  public static final int MIN_TEXT_WIDTH = 30;

  ////////////////////////////////////////////////////////////////
  // instance variables

  /** Font info. */
  protected Font _font = new Font("TimesRoman", Font.PLAIN, 10);
  protected transient FontMetrics _fm;
  protected int _lineHeight;

  /** Color of the actual text characters. */
  protected Color _textColor = Color.black;

  /** Color to be drawn behind the actual text characters. Note that
   *  this will be a smaller area than the bounding box which is
   *  filled with FillColor. */
  protected Color _textFillColor = Color.white;

  /** True if the area behind individual characters is to be filled
   *  with TextColor. */
  protected boolean _textFilled = false;

  /** True if the text should be editable. False for read-only. */
  protected boolean _editable = true;

  /** True if the text should be underlined. needs-more-work. */
  protected boolean _underline = false;

  /** True if more than one line of text is allow. If false, newline
   *  characters will be ignored. True by default. */
  protected boolean _multiLine = true;

  protected boolean _allowsTab = true;

  /** Extra spacing between lines. Default is 0 pixels. */
  protected int _lineSpacing = 0;

  /** Internal margins between the text and the edge of the rectangle. */
  protected int _topMargin = 1;
  protected int _botMargin = 1;
  protected int _leftMargin = 1;
  protected int _rightMargin = 1;

  /** True if the FigText can only grow in size, never shrink. */
  protected boolean _expandOnly = false;

  /** Text justification can be JUSTIFY_LEFT, JUSTIFY_RIGHT, or JUSTIFY_CENTER. */
  protected int _justification = JUSTIFY_LEFT;

  /** The current string to display. */
  protected String _curText;

  ////////////////////////////////////////////////////////////////
  // static initializer

  /** This puts the text properties on the "Text" and "Style" pages of
   * the uci.ui.TabPropFrame. */
  static {
    PropCategoryManager.categorizeProperty("Text", "font");
    PropCategoryManager.categorizeProperty("Text", "underline");
    PropCategoryManager.categorizeProperty("Text", "expandOnly");
    PropCategoryManager.categorizeProperty("Text", "lineSpacing");
    PropCategoryManager.categorizeProperty("Text", "topMargin");
    PropCategoryManager.categorizeProperty("Text", "botMargin");
    PropCategoryManager.categorizeProperty("Text", "leftMargin");
    PropCategoryManager.categorizeProperty("Text", "rightMargin");
    PropCategoryManager.categorizeProperty("Text", "text");
    PropCategoryManager.categorizeProperty("Style", "justification");
    PropCategoryManager.categorizeProperty("Style", "textFilled");
    PropCategoryManager.categorizeProperty("Style", "textFillColor");
    PropCategoryManager.categorizeProperty("Style", "textColor");
  }

  ////////////////////////////////////////////////////////////////
  // constructors

  /** Construct a new FigText with the given position, size, color,
   *  string, font, and font size. Text string is initially empty and
   *  centered. */
  public FigText(int x, int y, int w, int h,
		 Color textColor, String familyName, int fontSize) {
    super(x, y, w, h);
    _x = x; _y = y; _w = w; _h = h;
    _textColor = textColor;
    _font = new Font(familyName, Font.PLAIN, fontSize);
    _justification = JUSTIFY_CENTER;
    _curText = "";
  }

  /** Construct a new FigText with the given position, size, and attributes. */
  public FigText(int x, int y, int w, int h ) {
    this(x, y, w, h, Color.blue, "TimesRoman", 10);
  }

  ////////////////////////////////////////////////////////////////
  // invariant

  /** Check the class invariant to make sure that this FigText is in a
   *  valid state.  Useful for debugging. */
  public boolean OK() {
    if (!super.OK()) return false;
    return _font != null && _lineSpacing > -20 && _topMargin >= 0 &&
      _botMargin >= 0 && _leftMargin >= 0 && _rightMargin >= 0 &&
      (_justification == JUSTIFY_LEFT || _justification == JUSTIFY_CENTER ||
       _justification == JUSTIFY_RIGHT) && _textColor != null &&
      _textFillColor != null;
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  /** Reply a string that indicates how the text is justified: Left,
   *  Center, or Right. */
  public String getJustificationByName() {
    if (_justification == JUSTIFY_LEFT) return "Left";
    else if (_justification == JUSTIFY_CENTER) return "Center";
    else if (_justification == JUSTIFY_RIGHT) return "Right";
    System.out.println("internal error, unknown text alignment");
    return "Unknown";
  }

  /** Set the text justification given one of these strings: Left,
   *  Center, or Right. */
  public void setJustifciaionByName(String justifyString) {
    if (justifyString.equals("Left")) _justification = JUSTIFY_LEFT;
    else if (justifyString.equals("Center")) _justification = JUSTIFY_CENTER;
    else if (justifyString.equals("Right")) _justification = JUSTIFY_RIGHT;
    _fm = null;
  }


  ////////////////////////////////////////////////////////////////
  // accessors and modifiers

  public Color getTextColor() { return _textColor; }
  public void setTextColor(Color c) {
    firePropChange("textColor", _textColor, c);
    _textColor = c;
  }

  public Color getTextFillColor() { return _textFillColor; }
  public void setTextFillColor(Color c) {
    firePropChange("textFillColor", _textFillColor, c);
    _textFillColor = c;
  }

  public boolean getTextFilled() { return _textFilled; }
  public void setTextFilled(boolean b) {
    firePropChange("textFilled", _textFilled, b);
    _textFilled = b;
  }

  public boolean getEditable() { return _editable; }
  public void setEditable(boolean e) {
    firePropChange("editable", _editable, e);
    _editable = e;
  }

  public boolean getUnderline() { return _underline; }
  public void setUnderline(boolean b) {
    firePropChange("underline", _underline, b);
    _underline = b;
  }

  public int getJustification() { return _justification; }
  public void setJustification(int align) {
    firePropChange("justifciaion", getJustification(), align);
    _justification = align;
  }

  public int getLineSpacing() { return _lineSpacing; }
  public void setLineSpacing(int s) {
    firePropChange("lineSpacing", _lineSpacing, s);
    _lineSpacing = s;
    calcBounds();
  }

  public int getTopMargin() { return _topMargin; }
  public void setTopMargin(int m) {
    firePropChange("topMargin", _topMargin, m);
    _topMargin = m;
    calcBounds();
  }

  public int getBotMargin() { return _botMargin; }
  public void setBotMargin(int m) {
    firePropChange("botMargin", _botMargin, m);
    _botMargin = m;
    calcBounds();
  }

  public int getLeftMargin() { return _leftMargin; }
  public void setLeftMargin(int m) {
    firePropChange("leftMargin", _leftMargin, m);
    _leftMargin = m;
    calcBounds();
  }

  public int getRightMargin() { return _rightMargin; }
  public void setRightMargin(int m) {
    firePropChange("rightMargin", _rightMargin, m);
    _rightMargin = m;
    calcBounds();
  }

  public boolean getExpandOnly() { return _expandOnly; }
  public void setExpandOnly(boolean b) {
    firePropChange("expandOnly", _expandOnly, b);
    _expandOnly = b;
  }

  public Font getFont() { return _font; }
  public void setFont(Font f) {
    firePropChange("font", _font, f);
    _font = f;
    _fm = null;
    calcBounds();
  }

  public String getFontFamily() { return _font.getFamily(); }
  public void setFontFamily(String familyName) {
    Font f = new Font(familyName, _font.getStyle(), _font.getSize()); 
    setFont(f);
  }

  public int getFontSize() { return _font.getSize(); }
  public void setFontSize(int size) {
    Font f = new Font(_font.getFamily(), _font.getStyle(), size); 
    setFont(f);
  }

  public boolean getItalic() { return _font.isItalic(); }
  public void setItalic(boolean b) {
    int style = (getBold() ? Font.BOLD : 0) + (b ? Font.ITALIC : 0);
    Font f = new Font(_font.getFamily(), style, _font.getSize()); 
    setFont(f);
  }

  public boolean getBold() { return _font.isBold(); }
  public void setBold(boolean b) {
    int style = (b ? Font.BOLD : 0) + (getItalic() ? Font.ITALIC : 0);
    setFont(new Font(_font.getFamily(), style, _font.getSize()));
  }

  public void setMultiLine(boolean b) { _multiLine = b; }
  public boolean getMultiLine() { return _multiLine; }

  public void setAllowsTab(boolean b) { _allowsTab = b; }
  public boolean getAllowsTab() { return _allowsTab; }

  /** Remove the last char from the current string line and return the
   *  new string.  Called whenever the user hits the backspace key.
   *  Needs-More-Work: Very slow.  This will eventually be replaced by
   *  full text editing... if there are any volunteers to do that...*/
  public String deleteLastCharFromString(String s) {
    int len = Math.max(s.length() - 1, 0);
    char[] chars = s.toCharArray();
    return new String(chars, 0, len);
  }

  /** Delete the last char from the current string. Called whenever
   *  the user hits the backspace key */
  public void deleteLastChar() {
    _curText = deleteLastCharFromString(_curText);
    calcBounds();
  }

  /** Append a character to the current String .*/
  public void append(char c) { setText(_curText + c); }

  /** Append the given String to the current String. */
  public void append(String s) { setText(_curText + s); }

  /** set the current String to be the given String. */
  public void setText(String s) { _curText = s; calcBounds(); }

  /** Get the String held by this FigText. Multi-line text is
   *  represented by newline characters embedded in the String. */
  public String getText() { return _curText; }

  ////////////////////////////////////////////////////////////////
  // painting methods

  /** Paint the FigText. */
  public void paint(Graphics g) {
    int chunkX = _x + _leftMargin;
    int chunkY = _y + _topMargin;
    StringTokenizer lines;

    if (_filled) {
      g.setColor(_fillColor);
      g.fillRect(_x, _y, _w, _h);
    }
    if (_lineWidth > 0) {
      g.setColor(_lineColor);
      g.drawRect(_x, _y, _w - _lineWidth, _h - _lineWidth);
    }
    if (_font != null) g.setFont(_font);
    _fm = g.getFontMetrics(_font);
    int chunkH = _lineHeight + _lineSpacing;
    chunkY = _y + _topMargin + chunkH;
    if (_textFilled) {
      g.setColor(_textFillColor);
      lines = new StringTokenizer(_curText, "\n\r", true);
      while (lines.hasMoreTokens()) {
	String curLine = lines.nextToken();
	//if (curLine.equals("\r")) continue;
	int chunkW = _fm.stringWidth(curLine);
	switch (_justification) {
	case JUSTIFY_LEFT: break;
	case JUSTIFY_CENTER: chunkX = _x + (_w - chunkW) / 2; break;
	case JUSTIFY_RIGHT: chunkX = _x + _w - chunkW - _rightMargin; break;
	}
	if (curLine.equals("\n") || curLine.equals("\r")) chunkY += chunkH;
	else g.fillRect(chunkX, chunkY - chunkH, chunkW, chunkH);
      }
    }

    g.setColor(_textColor);
    chunkX = _x + _leftMargin;
    chunkY = _y + _topMargin + _lineHeight + _lineSpacing;
    lines = new StringTokenizer(_curText, "\n\r", true);
    while (lines.hasMoreTokens()) {
      String curLine = lines.nextToken();
      //if (curLine.equals("\r")) continue;
      int chunkW = _fm.stringWidth(curLine);
      switch (_justification) {
      case JUSTIFY_LEFT: break;
      case JUSTIFY_CENTER: chunkX = _x + ( _w - chunkW ) / 2; break;
      case JUSTIFY_RIGHT: chunkX = _x + _w  - chunkW; break;
      }
      if (curLine.equals("\n")  || curLine.equals("\r")) chunkY += chunkH;
      else {
        if (_underline) g.drawLine(chunkX, chunkY + 1, chunkX + chunkW, chunkY + 1);
	g.drawString(curLine, chunkX, chunkY);
      }
    }
  }

  /** Muse clicks are handled differentlty that the defi]ault Fig
   *  behavior so that it is easier to select text that is not
   *  filled.  Needs-More-Work: should actually check the individual
   *  text rectangles. */
  public boolean hit(Rectangle r) {
    int cornersHit = countCornersContained(r.x, r.y, r.width, r.height);
    return cornersHit > 0;
  }

  public Dimension getMinimumSize() {
    Dimension d = new Dimension(0, 0);
    stuffMinimumSize(d);
    return d;
  }

  public void stuffMinimumSize(Dimension d) {
    if (_font == null) return;
    if (_fm == null) _fm = Toolkit.getDefaultToolkit().getFontMetrics(_font);
    int overallW = 0;
    int numLines = 1;
    StringTokenizer lines = new StringTokenizer(_curText, "\n\r", true);
    while (lines.hasMoreTokens()) {
      String curLine = lines.nextToken();
      int chunkW = _fm.stringWidth(curLine);
      if (curLine.equals("\n") || curLine.equals("\r")) numLines++;
      else overallW = Math.max(chunkW, overallW);
    }
    _lineHeight = _fm.getHeight();
    int maxDescent = _fm.getMaxDescent();
    int overallH = (_lineHeight + _lineSpacing) * numLines +
      _topMargin + _botMargin + maxDescent;
    overallW = Math.max(MIN_TEXT_WIDTH, overallW + _leftMargin + _rightMargin);
    d.width = overallW;
    d.height = overallH;
  }

  ////////////////////////////////////////////////////////////////
  // event handlers: KeyListener implemtation

  /** When the user presses a key when a FigText is selected, that key
   *  should be added to the current string, or if the key was
   *  backspace, the last character is removed.  Needs-More-Work: Should
   *  also catch arrow keys and mouse clicks for full text
   *  editing... someday... */
  public void keyTyped(KeyEvent ke) {
       if (!_editable) return;
//     int mods = ke.getModifiers();
//     if (mods != 0 && mods != KeyEvent.SHIFT_MASK) return;
//     char c = ke.getKeyChar();
//     if (!Character.isISOControl(c)) {
//       FigTextEditor te = startTextEditor(ke);
//       te.keyTyped(ke);
// //       startTrans();
// //       append(c);
// //       endTrans();
//       ke.consume();
//     }
  }

  /** This method handles backspace and enter. */
  public void keyPressed(KeyEvent ke) {
    if (!ke.isActionKey() && !isNonStartEditingKey(ke)) {
      if (!_editable) return;
      FigTextEditor te = startTextEditor(ke);
      te.keyPressed(ke);
      ke.consume();
    }
  }

  /** Not used, does nothing. */
  public void keyReleased(KeyEvent ke) {if (!_editable) return; }

  protected boolean isNonStartEditingKey(KeyEvent ke) {
    int keyCode = ke.getKeyCode();
    switch (keyCode) {
    case KeyEvent.VK_TAB:
    case KeyEvent.VK_CANCEL:
    case KeyEvent.VK_CLEAR:
    case KeyEvent.VK_SHIFT:
    case KeyEvent.VK_CONTROL:
    case KeyEvent.VK_ALT:
    case KeyEvent.VK_META:
    case KeyEvent.VK_HELP:
    case KeyEvent.VK_PAUSE:
    case KeyEvent.VK_DELETE:
      return true;
    }
    if (ke.isControlDown()) return true;
    if (ke.isAltDown()) return true;
    if (ke.isMetaDown()) return true;
    return false;
  }
  ////////////////////////////////////////////////////////////////
  // event handlers: KeyListener implemtation

  public void mouseClicked(MouseEvent me) {
    if (me.isConsumed()) return;
    if (me.getClickCount() >= 2) {
        if (!_editable) return;
        startTextEditor(me);
        me.consume();
    }
  }
  
  public void mousePressed(MouseEvent me) { }
  
  public void mouseReleased(MouseEvent me) { }
  
  public void mouseEntered(MouseEvent me) { }
  
  public void mouseExited(MouseEvent me) { }
  
  public FigTextEditor startTextEditor(InputEvent ie) {
    FigTextEditor te = new FigTextEditor(this, ie);
    return te;
  }


  ////////////////////////////////////////////////////////////////
  // internal utility functions

  /** Compute the overall width and height of the FigText object based
   *  on the font, font size, and current text. Needs-More-Work: Right
   *  now text objects can get larger when you type more, but they
   *  do not get smaller when you backspace.  */
  public void calcBounds() {
    if (_font == null) return;
    if (_fm == null) _fm = Toolkit.getDefaultToolkit().getFontMetrics(_font);
    int overallW = 0;
    int numLines = 1;
    StringTokenizer lines = new StringTokenizer(_curText, "\n\r", true);
    while (lines.hasMoreTokens()) {
      String curLine = lines.nextToken();
      int chunkW = _fm.stringWidth(curLine);
      if (curLine.equals("\n") || curLine.equals("\r")) numLines++;
      else overallW = Math.max(chunkW, overallW);
    }
    _lineHeight = _fm.getHeight();
    int maxDescent = _fm.getMaxDescent();
    int overallH = (_lineHeight + _lineSpacing) * numLines +
      _topMargin + _botMargin + maxDescent;
    overallW = Math.max(MIN_TEXT_WIDTH, overallW + _leftMargin + _rightMargin);
    switch (_justification) {
      case JUSTIFY_LEFT: 
        break;
      
      case JUSTIFY_CENTER:
        if (_w < overallW) 
          _x -= (overallW - _w) / 2;
        break;
    
      case JUSTIFY_RIGHT: 
        if (_w < overallW) 
          _x -= (overallW - _w); 
        break;
    }
    _w = _expandOnly ? Math.max(_w, overallW) : overallW;
    _h = _expandOnly ? Math.max(_h, overallH) : overallH;
  }

  static final long serialVersionUID = 468901636070058091L;


} /* end class FigText */

