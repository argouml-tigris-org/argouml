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

package uci.ui;

import java.awt.*;
import java.net.*;
import java.io.IOException;
import java.net.MalformedURLException;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;

public class TinyHTMLViewer extends JFrame {

  public TinyHTMLViewer(String startPage) {
    super("Help -- " + startPage);
    setBounds(200, 25, 600, 400);
    HtmlPane html = new HtmlPane(startPage);
    setContentPane(html);
  }

  ////////////////////////////////////////////////////////////////
  // testing
  //   public static void main(String args[]) {
  //     if (args.length != 1) {
  //       System.out.println("use resourceName as the only cmdline arg");
  //       return;
  //     }
  //     TinyHTMLViewer v = new TinyHTMLViewer(args[0]);
  //     v.setVisible(true);
  //   }
} /* end class TinyHTMLViewer */


class HtmlPane extends JScrollPane implements HyperlinkListener {
  public static String DEFAULT_PAGE = "/uci/uml/help/index.html";

  JEditorPane html;

  public HtmlPane(String startPage) {
    URL url = null;
    try { url = new URL(startPage); }
    catch (Exception ex) {
      try { url = TinyHTMLViewer.class.getResource(startPage); }
      catch (Exception ex2) {
	try {
	  if (DEFAULT_PAGE.startsWith("http"))
	    url = new URL(DEFAULT_PAGE);
	  else
	    url = TinyHTMLViewer.class.getResource(DEFAULT_PAGE);
	}
	catch (Exception ex3) {
	  url = null;
	}
      }
    }

    if (url != null) {
      try {
	html = new JEditorPane(url);
	html.setEditable(false);
	html.addHyperlinkListener(this);

	JViewport vp = getViewport();
	vp.add(html);
      }
      catch (Exception ex4) {
	System.out.println("could not open HTML pane");
      }
    }
  }

    /**
     * Notification of a change relative to a 
     * hyperlink.
     */
    public void hyperlinkUpdate(HyperlinkEvent e) {
	if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
	    linkActivated(e.getURL());
	}
    }

    /**
     * Follows the reference in an
     * link.  The given url is the requested reference.
     * By default this calls <a href="#setPage">setPage</a>,
     * and if an exception is thrown the original previous
     * document is restored and a beep sounded.  If an 
     * attempt was made to follow a link, but it represented
     * a malformed url, this method will be called with a
     * null argument.
     *
     * @param u the URL to follow
     */
    protected void linkActivated(URL u) {
	Cursor c = html.getCursor();
	Cursor waitCursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
	html.setCursor(waitCursor);
	SwingUtilities.invokeLater(new PageLoader(u, c));
    }

    /**
     * temporary class that loads synchronously (although
     * later than the request so that a cursor change
     * can be done).
     */
    class PageLoader implements Runnable {
	
	PageLoader(URL u, Cursor c) {
	    url = u;
	    cursor = c;
	}

        public void run() {
	    if (url == null) {
		// restore the original cursor
		html.setCursor(cursor);

		// PENDING(prinz) remove this hack when 
		// automatic validation is activated.
		Container parent = html.getParent();
		parent.repaint();
	    } else {
		Document doc = html.getDocument();
		try {
		    html.setPage(url);
		} catch (IOException ioe) {
		    html.setDocument(doc);
		    getToolkit().beep();
		} finally {
		    // schedule the cursor to revert after
		    // the paint has happended.
		    url = null;
		    SwingUtilities.invokeLater(this);
		}
	    }
	}

	URL url;
	Cursor cursor;
    }


  ////////////////////////////////////////////////////////////////
  // construtor
//   public TinyHTMLViewer(String resourceName) {
//     _url = this.getClass().getResource(resourceName);
//     init();
//   }

//   public TinyHTMLViewer(URL url) {
//     _url = url;
//     init();
//   }

//   protected void init() {
//     try {
//       _editor = new JEditorPane(_url);
//       _editor.setEditable(false);
//       c.setLayout(new BorderLayout());
//       html.addHyperlinkListener(this);

//       JScrollPane sp
//       vp.add(html);
//       _editor, BorderLayout.CENTER);
//       setSize(WIDTH, HEIGHT);
//     }
//     catch (IOException ex) {
//       System.out.println("got IOException");
//     }
//   }


  
}
