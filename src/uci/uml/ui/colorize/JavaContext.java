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

package uci.uml.ui.colorize;

import java.awt.*;
import java.util.Vector;

import javax.swing.text.*;

/**
 * A collection of styles used to render java text.  
 * This class also acts as a factory for the views used 
 * to represent the java documents.  Since the rendering 
 * styles are based upon view preferences, the views need
 * a way to gain access to the style settings which is 
 * facilitated by implementing the factory in the style 
 * storage.  Both functionalities can be widely shared across
 * java document views.
 *
 * @author   Timothy Prinzing
 * @version  1.2 05/04/98
 */
public class JavaContext extends StyleContext implements ViewFactory {

    /**
     * Constructs a set of styles to represent java lexical 
     * tokens.  By default there are no colors or fonts specified.
     */
    public JavaContext() {
	super();
	Style root = getStyle(DEFAULT_STYLE);
	tokenStyles = new Style[Token.MaximumScanValue + 1];
	Token[] tokens = Token.all;
	int n = tokens.length;
	for (int i = 0; i < n; i++) {
	    Token t = tokens[i];
	    Style parent = getStyle(t.getCategory());
	    if (parent == null) {
		parent = addStyle(t.getCategory(), root);
	    }
	    Style s = addStyle(null, parent);
	    s.addAttribute(Token.TokenAttribute, t);
	    tokenStyles[t.getScanValue()] = s;

	}
    }

    /**
     * Fetch the foreground color to use for a lexical
     * token with the given value.
     * 
     * @param attr attribute set from a token element
     *  that has a Token in the set.
     */
    public Color getForeground(int code) {
	if (tokenColors == null) {
	    tokenColors = new Color[Token.MaximumScanValue + 1];
	}
	if ((code >= 0) && (code < tokenColors.length)) {
	    Color c = tokenColors[code];
	    if (c == null) {
		Style s = tokenStyles[code];
		c = StyleConstants.getForeground(s);
	    }
	    return c;
	}
	return Color.black;
    }

    /**
     * Fetch the font to use for a lexical
     * token with the given scan value.
     */
    public Font getFont(int code) {
	if (tokenFonts == null) {
	    tokenFonts = new Font[Token.MaximumScanValue + 1];
	}
	if (code < tokenFonts.length) {
	    Font f = tokenFonts[code];
	    if (f == null) {
		Style s = tokenStyles[code];
		f = getFont(s);
	    }
	    return f;
	}
	return null;
    }

    /**
     * Fetches the attribute set to use for the given
     * scan code.  The set is stored in a table to
     * facilitate relatively fast access to use in 
     * conjunction with the scanner.
     */
    public Style getStyleForScanValue(int code) {
	if (code < tokenStyles.length) {
	    return tokenStyles[code];
	}
	return null;
    }

    // --- ViewFactory methods -------------------------------------

    public View create(Element elem) {
	return new JavaView(elem);
    }

    // --- variables -----------------------------------------------

    /**
     * The styles representing the actual token types.
     */
    Style[] tokenStyles;

    /**
     * Cache of foreground colors to represent the 
     * various tokens.
     */
    transient Color[] tokenColors;

    /**
     * Cache of fonts to represent the various tokens.
     */
    transient Font[] tokenFonts;

    /**
     * View that uses the lexical information to determine the
     * style characteristics of the text that it renders.  This
     * simply colorizes the various tokens and assumes a constant
     * font family and size.
     */
    class JavaView extends WrappedPlainView {

	/**
	 * Construct a simple colorized view of java
	 * text.
	 */
	JavaView(Element elem) {
	    super(elem);
	    JavaDocument doc = (JavaDocument) getDocument();
	    lexer = doc.createScanner();
	    lexerValid = false;
	}

	/**
	 * Renders using the given rendering surface and area 
	 * on that surface.  This is implemented to invalidate
	 * the lexical scanner after rendering so that the next
	 * request to drawUnselectedText will set a new range
	 * for the scanner.
	 *
	 * @param g the rendering surface to use
	 * @param a the allocated region to render into
	 *
	 * @see View#paint
	 */
        public void paint(Graphics g, Shape a) {
	    super.paint(g, a);
	    lexerValid = false;
	}

	/**
	 * Renders the given range in the model as normal unselected
	 * text.  This is implemented to paint colors based upon the
	 * token-to-color translations.  To reduce the number of calls
	 * to the Graphics object, text is batched up until a color
	 * change is detected or the entire requested range has been
	 * reached.
	 *
	 * @param g the graphics context
	 * @param x the starting X coordinate
	 * @param y the starting Y coordinate
	 * @param p0 the beginning position in the model
	 * @param p1 the ending position in the model
	 * @returns the location of the end of the range
	 * @exception BadLocationException if the range is invalid
	 */
        protected int drawUnselectedText(Graphics g, int x, int y, 
					 int p0, int p1) throws BadLocationException {
	    Document doc = getDocument();
	    Color last = null;
	    int mark = p0;
	    for (; p0 < p1; ) {
		updateScanner(p0);
		int p = Math.min(lexer.getEndOffset(), p1);
		p = (p <= p0) ? p1 : p;
		Color fg = getForeground(lexer.token);
		if (fg != last && last != null) {
		    // color change, flush what we have
		    g.setColor(last);
		    Segment text = getLineBuffer();
		    doc.getText(mark, p0 - mark, text);
		    x = Utilities.drawTabbedText(text, x, y, g, this, mark);
		    mark = p0;
		}
		last = fg;
		p0 = p;
	    }
	    // flush remaining
	    g.setColor(last);
	    Segment text = getLineBuffer();
	    doc.getText(mark, p1 - mark, text);
	    x = Utilities.drawTabbedText(text, x, y, g, this, mark);
	    return x;
	}

	/**
	 * Update the scanner (if necessary) to point to the appropriate
	 * token for the given start position needed for rendering.
	 */
	void updateScanner(int p) {
	    try {
		if (! lexerValid) {
		    JavaDocument doc = (JavaDocument) getDocument();
		    lexer.setRange(doc.getScannerStart(p), doc.getLength());
		    lexerValid = true;
		}
		while (lexer.getEndOffset() <= p) {
		    lexer.scan();
		}
	    } catch (Throwable e) {
		// can't adjust scanner... calling logic
		// will simply render the remaining text.
		e.printStackTrace();
	    }
	}
	
	JavaDocument.Scanner lexer;
	boolean lexerValid;
    }

}
