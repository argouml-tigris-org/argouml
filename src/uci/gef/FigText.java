// Copyright (c) 1995, 1996 Regents of the University of California.
// All rights reserved.
//
// This software was developed by the Arcadia project
// at the University of California, Irvine.
//
// Redistribution and use in source and binary forms are permitted
// provided that the above copyright notice and this paragraph are
// duplicated in all such forms and that any documentation,
// advertising materials, and other materials related to such
// distribution and use acknowledge that the software was developed
// by the University of California, Irvine.  The name of the
// University may not be used to endorse or promote products derived
// from this software without specific prior written permission.
// THIS SOFTWARE IS PROVIDED ``AS IS'' AND WITHOUT ANY EXPRESS OR
// IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
// WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.

// File: FigText.java
// Classes: FigText
// Original Author: ics125b spring 1996
// $Id$

package uci.gef;

import java.awt.*;
import java.util.*;
import uci.ui.*;
import uci.util.*;

/** This class handles painting and editing text Fig's in a
 *  LayerDiagram. Needs-More-Work: should eventually allow styled text
 *  editing, ... someday...
 *  <A HREF="../features.html#basic_shapes_text">
 *  <TT>FEATURE: basic_shapes_text</TT></A>
 */

public class FigText extends Fig {

  ////////////////////////////////////////////////////////////////
  // constants

  /** Constants to specify text justification. Needs-More-Work: some
   *  parts of the code refer to this as "alignment", and that is
   *  confusing. */
  public final int JUSTIFY_LEFT = 0;
  public final int JUSTIFY_RIGHT = 1;
  public final int JUSTIFY_CENTER = 2;

  /** Minimum size of a FigText object. */
  public final int MIN_TEXT_WIDTH = 30;

  ////////////////////////////////////////////////////////////////
  // instance variables

  /** Font info. */
  protected Font _font = new Font("TimesRoman", Font.PLAIN, 10);
  protected FontMetrics _fm;
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
  protected boolean _underline = false;
  // needs-more-work: use insets instead
  protected int _lineSpacing = -4;
  protected int _topMargin = 1;
  protected int _botMargin = 1;
  protected int _leftMargin = 1;
  protected int _rightMargin = 1;

  /** True if the FigText can only grow in size, never shrink. */
  protected boolean _expandOnly = false;

  /** Text alignment can be JUSTIFY_LEFT, JUSTIFY_RIGHT, or JUSTIFY_CENTER. */
  protected int _alignment;

  /** The current string to display. */
  protected String _curText;

  ////////////////////////////////////////////////////////////////
  // static initializer
  static {
    PropCategoryManager.categorizeProperty("Text", "font");
    PropCategoryManager.categorizeProperty("Text", "underline");
    PropCategoryManager.categorizeProperty("Text", "expandOnly");
    PropCategoryManager.categorizeProperty("Text", "lineSpacing");
    PropCategoryManager.categorizeProperty("Text", "topMargin");
    PropCategoryManager.categorizeProperty("Text", "botMargin");
    PropCategoryManager.categorizeProperty("Text", "leftMargin");
    PropCategoryManager.categorizeProperty("Text", "rightMargin");
    PropCategoryManager.categorizeProperty("Style", "alignment");
    PropCategoryManager.categorizeProperty("Style", "textFilled");
    PropCategoryManager.categorizeProperty("Style", "textFillColor");
    PropCategoryManager.categorizeProperty("Style", "textColor");
  }

  ////////////////////////////////////////////////////////////////
  // constructors

  /** Construct a new FigText with the given position, size, color,
   *  string, and font. */
  public FigText(int x, int y, int w, int h,
		 Color textColor, String familyName, int fontSize) {
    super(x, y, w, h);
    _x = x; _y = y; _w = w; _h = h;
    _textColor = textColor;
    _font = new Font(familyName, Font.PLAIN, fontSize);
    _alignment = JUSTIFY_CENTER;
    _curText = "";
  }

  /** Construct a new FigText with the given position, size, and attributes. */
  public FigText(int x, int y, int w, int h, Hashtable gAttrs ) {
    this(x, y, w, h, Color.blue, "TimesRoman", 10);
    //    put(gAttrs);
  }

  ////////////////////////////////////////////////////////////////
  // invariant

  public boolean OK() {
    if (!super.OK()) return false;
    return _font != null && _lineSpacing > -20 && _topMargin >= 0 &&
      _botMargin >= 0 && _leftMargin >= 0 && _rightMargin >= 0 &&
      (_alignment == JUSTIFY_LEFT || _alignment == JUSTIFY_CENTER ||
       _alignment == JUSTIFY_RIGHT) && _textColor != null &&
      _textFillColor != null;
  }
  ////////////////////////////////////////////////////////////////
  // accessors

  /** Reply a string that indicates how the text is justified: Left,
   *  Center, or Right. */
  public String alignmentByName() {
    // needs-more-work: index into static array?
    // return _AlignmentNames[_alignment];
    if (_alignment == JUSTIFY_LEFT) return "Left";
    else if (_alignment == JUSTIFY_CENTER) return "Center";
    else if (_alignment == JUSTIFY_RIGHT) return "Right";
    System.out.println("internal error, unknown text alignment");
    return "Unknown";
  }

  /** Set the text justification given one of these strings: Left,
   *  Center, or Right. */
  public void alignmentByName(String alignString) {
    if (alignString.equals("Left")) _alignment = JUSTIFY_LEFT;
    else if (alignString.equals("Center")) _alignment = JUSTIFY_CENTER;
    else if (alignString.equals("Right")) _alignment = JUSTIFY_RIGHT;
    _fm = null;
  }



  ////////////////////////////////////////////////////////////////
  // accessors and modifiers

  public Color getTextColor() { return _textColor; }
  public void setTextColor(Color c) { _textColor = c; }
  public Color getTextFillColor() { return _textFillColor; }
  public void setTextFillColor(Color c) { _textFillColor = c; }
  public boolean getTextFilled() { return _textFilled; }
  public void setTextFilled(boolean b) { _textFilled = b; }
  public boolean getUnderline() { return _underline; }
  public void setUnderline(boolean b) { _underline = b; }

  public String getAlignment() { return alignmentByName(); }
  public void setAlignment(String align) { alignmentByName(align); }
  public int getLineSpacing() { return _lineSpacing; }
  public void setLineSpacing(int s) { _lineSpacing = s; calcBounds(); }

  public int getTopMargin() { return _topMargin; }
  public void setTopMargin(int m) { _topMargin = m; calcBounds(); }
  public int getBotMargin() { return _botMargin; }
  public void setBotMargin(int m) { _botMargin = m; calcBounds(); }
  public int getLeftMargin() { return _leftMargin; }
  public void setLeftMargin(int m) { _leftMargin = m; calcBounds(); }
  public int getRightMargin() { return _rightMargin; }
  public void setRightMargin(int m) { _rightMargin = m; calcBounds(); }

  public boolean getExpandOnly() { return _expandOnly; }
  public void setExpandOnly(boolean b) { _expandOnly = b; }

  public Font getFont() { return _font; }
  public void setFont(Font f) { _font = f; _fm = null; calcBounds(); }

  public boolean getItalic() { return _font.isItalic(); }
  public void setItalic(boolean b) {
    int style = (getBold() ? Font.BOLD : 0) + (b ? Font.ITALIC : 0);
    setFont(new Font(_font.getFamily(), _font.getSize(), style));
  }

  public boolean getBold() { return _font.isBold(); }
  public void setBold(boolean b) {
    int style = (b ? Font.BOLD : 0) + (getItalic() ? Font.ITALIC : 0);
    setFont(new Font(_font.getFamily(), _font.getSize(), style));
  }

  /** Remove the last char from the current string line and return the
   *  new string.  Called whenever the user hits the backspace key.
   *  Needs-More-Work: Very slow.  This will eventually be replaced by
   *  full text editing... if there are any volunteers to do that...*/
  public String deleteLastCharFromString(String string) {
    int string_length = Math.max(string.length() - 1, 0);
    char[] string_char = string.toCharArray();
    String new_string = new String(string_char,0,string_length);
    return new_string;
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
      g.drawRect(_x - 1, _y - 1, _w + 1, _h + 1);
    }

    if (_font != null) g.setFont(_font);
    _fm = g.getFontMetrics(_font);
    int chunkH = _lineHeight + _lineSpacing;
    chunkY = _y + _topMargin + chunkH;
    if (_textFilled) {
      g.setColor(_textFillColor);
      lines = new StringTokenizer(_curText, "\n", true);
      while (lines.hasMoreTokens()) {
	String curLine = lines.nextToken();
	int chunkW = _fm.stringWidth(curLine);
	switch (_alignment) {
	case JUSTIFY_LEFT: break;
	case JUSTIFY_CENTER: chunkX = _x + (_w - chunkW) / 2; break;
	case JUSTIFY_RIGHT: chunkX = _x + _w - chunkW - _rightMargin; break;
	}
	if (curLine.equals("\n")) chunkY += chunkH;
	else g.fillRect(chunkX, chunkY - chunkH, chunkW, chunkH);
      }
    }

    g.setColor(_textColor);
    chunkX = _x + _leftMargin;
    chunkY = _y + _topMargin + _lineHeight + _lineSpacing;
    lines = new StringTokenizer(_curText, "\n", true);
    while (lines.hasMoreTokens()) {
      String curLine = lines.nextToken();
      int chunkW = _fm.stringWidth(curLine);
      switch (_alignment) {
      case JUSTIFY_LEFT: break;
      case JUSTIFY_CENTER: chunkX = _x + ( _w - chunkW ) / 2; break;
      case JUSTIFY_RIGHT: chunkX = _x + _w  - chunkW; break;
      }
      if (curLine.equals("\n")) chunkY += chunkH;
      else g.drawString(curLine, chunkX, chunkY);
    }
  }



  ////////////////////////////////////////////////////////////////
  // event handlers

  /** When the user presses a key when a FigText is selected, that key
   *  should be added to the current string, or if the key was
   *  backspace, the last character is removed.  Needs-More-Work: Should
   *  also catch arrow keys and mouse clicks for full text
   *  editing... someday... */
  public boolean keyDown(Event evt,int key) {
    startTrans();
    if ((key == 8) || (key == 127)) deleteLastChar();
    else append((char)key);
    endTrans();
    return true; /* needs-more-work: not all keys are processed... */
  }

  ////////////////////////////////////////////////////////////////
  // internal utility functions

  /** Compute the overall width and height of the FigText object based
   *  on the font, font size, and current text. Needs-More-Work: Right
   *  now text objects can get larger when you type more, but they
   *  do not get smaller when you backspace.  */
  protected void calcBounds() {
    if (_font == null) return;
    if (_fm == null) _fm = Toolkit.getDefaultToolkit().getFontMetrics(_font);
    int overallW = 0;
    int numLines = 1;
    StringTokenizer lines = new StringTokenizer(_curText, "\n", true);
    while (lines.hasMoreTokens()) {
      String curLine = lines.nextToken();
      int chunkW = _fm.stringWidth(curLine);
      if (curLine.equals("\n")) numLines++;
      else overallW = Math.max(chunkW, overallW);
    }
    _lineHeight = _fm.getHeight();
    int maxDescent = _fm.getMaxDescent();
    int overallH = (_lineHeight + _lineSpacing) * numLines +
      _topMargin + _botMargin + maxDescent;
    overallW = Math.max(MIN_TEXT_WIDTH, overallW + _leftMargin + _rightMargin);
    switch (_alignment) {
    case JUSTIFY_LEFT: break;
    case JUSTIFY_CENTER: if (_w < overallW) _x -= (overallW - _w) / 2; break;
    case JUSTIFY_RIGHT: if (_w < overallW) _x -= (overallW - _w); break;
    }
    _w = _expandOnly ? Math.max(_w, overallW) : overallW;
    _h = _expandOnly ? Math.max(_h, overallH) : overallH;
  }

} /* end class FigText */
