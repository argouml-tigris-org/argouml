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

// File: SVGWriter.java
// Classes: SVGWriter
// Original Author: Andreas Rueckert <a_rueckert@gmx.net>
// $Id$

package uci.gef;

import java.io.*;
import java.util.Hashtable;
import java.awt.*;
import java.awt.image.*;
import org.w3c.dom.*;
import com.ibm.xml.dom.*;

public class SVGWriter extends Graphics {

    private PrintWriter p;
    Document svg;
    Element  root;

    private Color fColor = Color.black;
    private Font fFont = new Font("Helvetica",Font.PLAIN,12);
    private Rectangle clip;

    // To keep the SVG output as simple as possible, I handle all
    // the transformations and the scaling in the writer.
    private int    xOffset = 0;
    private int    yOffset = 0;
    private double xScale  = 1.0;
    private double yScale  = 1.0;

    public SVGWriter(OutputStream stream) throws IOException, Exception {
        p = new PrintWriter(stream);

	svg = (Document)Class.forName("com.ibm.xml.dom.DocumentImpl").newInstance();

	root = svg.createElement( "svg");
	root.setAttribute( "width", "600");
	root.setAttribute( "height", "600");
    }

    public Graphics create() { return this; }

    public Graphics create(int x,
			   int y,
			   int width,
			   int height) {
	return this;
    }

    public void dispose() {
        svg.appendChild( root);
	printDOMTree( svg);
	p.close();
    }

    public void printDOMTree( Node node) {

	int type = node.getNodeType();

	switch (type) { 
	    // print the document element 
	case Node.DOCUMENT_NODE: 
	    { 
		p.println("<?xml version=\"1.0\" ?>"); 
		p.println("<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 20000303 Stylable//EN\" \"svg-20000303-stylable.dtd\">");
		printDOMTree(((Document)node).getDocumentElement()); 
		break; 
	    } 
	    // print element with attributes 
	case Node.ELEMENT_NODE: 
	    { 
		p.print("<"); 
		p.print(node.getNodeName()); 

		NamedNodeMap attrs = node.getAttributes(); 
		for (int i = 0; i < attrs.getLength(); i++) { 
		    Node attr = attrs.item(i);
		    p.print(" " + attr.getNodeName() + "=\"" 
				+ attr.getNodeValue() + "\""); 
		} 

		NodeList children = node.getChildNodes(); 

		if (children.getLength() > 0) { 
		    p.println(">");
		    int len = children.getLength(); 
		    for (int i = 0; i < len; i++) printDOMTree(children.item(i)); 	       
	      
		    p.print("</"); 
		    p.print(node.getNodeName()); 
		    p.println('>');
		} else {
		    p.println("/>");
		}
		break; 


	    } 
	    // handle entity reference nodes 
	case Node.ENTITY_REFERENCE_NODE: 
	    { 
		p.print("&"); 
		p.print(node.getNodeName()); 
		p.print(";"); 
		break; 
	    } 
	    // print cdata sections 
	case Node.CDATA_SECTION_NODE: 
	    { 
		p.print("<![CDATA["); 
		p.print(node.getNodeValue()); 
		p.print("]]>"); 
		break; 
	    } 
	    // print text 
	case Node.TEXT_NODE: 
	    { 
		p.print(node.getNodeValue()); 
		break; 
	    } 
	    // print processing instruction 
	case Node.PROCESSING_INSTRUCTION_NODE: 
	    { 
		p.print("<?"); 
		p.print(node.getNodeName()); 
		String data = node.getNodeValue(); 
		{ 
		    p.print(""); 
		    p.print(data); 
		} 
		p.print("?>"); 
		break; 
	    } 
	} 
    }

    public Color getColor() { return fColor; }

    public void setColor(Color c) {
	/*
        fColor = c;
        final float maxColor = 255;
        p.print(((float)c.getRed()) / maxColor + " ");
        p.print(((float)c.getGreen()) / maxColor + " ");
        p.print(((float)c.getBlue()) / maxColor + " ");
	p.println("setrgbcolor");
	*/
    }

    public void setPaintMode() {}

    public void setXORMode(Color otherColor) {}

    public Font getFont() { return fFont; }

    public void setFont(Font font) {
	/*
	if (!fFont.equals(font)) {
	    fFont = font;
	    FontMetrics metrics = getFontMetrics();
	    String name = font.getName();
	    if (font.isBold() || font.isItalic()) {
		name += "-";
		if (font.isBold())
		    name += "Bold";
		if (font.isItalic())
		    name += "Oblique";
	    }

	    p.println("isolatin1encoding /_" + name + " /" + name + " RE");
	    p.println("/_" + name + " findfont");
	    p.println(font.getSize() + " scalefont setfont");
	    } */
    }

    /*
    public FontMetrics getFontMetrics() {
		return getFontMetrics(fFont);
    }
    */

    public FontMetrics getFontMetrics(Font font) {
	return Toolkit.getDefaultToolkit().getFontMetrics(font);
    }

    public java.awt.Rectangle getClipBounds() { return clip; }

    public void clipRect(int x, int y, int w, int h) { setClip(x,y,w,h); }

    public Shape getClip() { return clip; }

    public void copyArea(int x, int y, int width, int height, int dx, int dy) {}

    public boolean drawImage(Image img,
			     int x,
			     int y,
			     ImageObserver observer) {
	return false;
    }

    /*
      privat void handlesinglepixel(int x, int y, int pixel) {
      if (((pixel >> 24) & 0xff) == 0) {
      // should be transparent, is printed white:
      pixel = 0xffffff;
      }
      p.print(Integer.toHexString((pixel >> 20) & 0x0f)
      +Integer.toHexString((pixel >> 12) & 0x0f)
      +Integer.toHexString((pixel >> 4)  & 0x0f));
      }
    */

    public boolean drawImage(Image img,
			     int x,
			     int y,
			     int w,
			     int h,
			     ImageObserver observer) {
	/*
	int iw = img.getWidth(observer), ih = img.getHeight(observer);

	p.println("gsave");
	writeCoords(x,y+h); p.println("translate");
	writeCoords(w,-h); p.println("scale");
	p.println("/DatenString "+iw+" string def");
	writeCoords(iw,-ih); p.println("4 [" + iw +" 0 0 "+ (-ih) + " 0 " + ih + "]");
	p.println("{currentfile DatenString readhexstring pop} bind");
	p.println("false 3 colorimage");
      
	int[] pixels = new int[iw * ih];
	PixelGrabber pg = new PixelGrabber(img, 0, 0, iw, ih, pixels, 0, iw);
	//	pg.setColorModel(Toolkit.getDefaultToolkit().getColorModel());
	try {
	    pg.grabPixels();
	} catch (InterruptedException e) {
	    System.err.println("interrupted waiting for pixels!");
	    return false;
	}
	if ((pg.getStatus() & ImageObserver.ABORT) != 0) {
	    System.err.println("image fetch aborted or errored");
	    return false;
	}
	for (int j = 0; j < ih; j++) {
	    for (int i = 0; i < iw; i++) {
		handlesinglepixel(i, j, pixels[j * iw + i]);
	    }
	    if (iw % 2 == 1) p.print("0");
	    p.println();
	}
	if (ih % 2 == 1) {
	    for (int i = 0; i < 3 * (iw + iw % 2); i++)
		p.print("0");
	    p.println();
	}
	p.println("grestore");
	*/
	return true;
    }

    public boolean drawImage(Image img,
			     int x,
			     int y,
			     Color bgcolor,
			     ImageObserver observer) {
	return false;
    }

    public boolean drawImage(Image img,
			     int x,
			     int y,
			     int width,
			     int height,
			     Color bgcolor,
			     ImageObserver observer) {
	return false;
    }

    public boolean drawImage(Image img,
			     int dx1,
			     int dy1,
			     int dx2,
			     int dy2,
			     int sx1,
			     int sy1,
			     int sx2,
			     int sy2,
			     ImageObserver observer) {
	return false;
    }

    public boolean drawImage(Image img,
			     int dx1,
			     int dy1,
			     int dx2,
			     int dy2,
			     int sx1,
			     int sy1,
			     int sx2,
			     int sy2,
			     Color bgcolor,
			     ImageObserver observer) {
	return false;
    }

    private int scaleX( int x) {
	return (int)(x * xScale);
    }

    private int scaleY( int y) {
	return (int)(y * yScale);
    }

    private int transformX( int x) {
	return scaleX( x) + xOffset;
    }

    private int transformY( int y) {
        return scaleY( y) + yOffset;
    }

    private void drawRect( int x, int y, int w, int h, String style) {
	Element rect = svg.createElement( "rect");
	rect.setAttribute( "x", ""+transformX( x));
	rect.setAttribute( "y", ""+transformY( y));
	rect.setAttribute( "width", ""+scaleX( w));
	rect.setAttribute( "height", ""+scaleY( h));
	rect.setAttribute( "style", style);
	root.appendChild( rect);
    }

    public void drawRect( int x, int y, int w, int h) {
	drawRect( x, y, w, h, "fill:white; stroke:black; stroke-width:1");
    }

    public void fillRect(int x, int y, int w, int h) {
	drawRect( x, y, w, h, "fill:black; stroke:black; stroke-width:1");
    }

    public void clearRect(int x, int y, int w, int h) {
	drawRect( x, y, w, h, "fill:white; stroke:white; stroke-width:1");
    }

    private void writeEllipsePath(int x, int y, int w, int h, int startAngle, int arcAngle) {
	/*
	p.println("newpath");
	int dx = w/2, dy = h/2;
	writeCoords(x + dx, y + dy);
	writeCoords(dx, dy);
	writeCoords(startAngle,-(startAngle+arcAngle));
	p.println("ellipse");
	*/
    }


    private void drawOval( int x, int y, int w, int h, String style) {
	Element oval = svg.createElement( "ellipse");
	oval.setAttribute( "cx", ""+transformX( x));
	oval.setAttribute( "cy", ""+transformY( y));
	oval.setAttribute( "rx", ""+((double)scaleX( w))/2);
	oval.setAttribute( "ry", ""+((double)scaleY( h))/2);
	oval.setAttribute( "style", style);
	root.appendChild( oval);	
    }

    public void drawOval(int x, int y, int w, int h) {
	drawOval( x, y, w, h, "fill:white; stroke:black; stroke-width:1");
    }

    public void fillOval(int x, int y, int w, int h) {
	drawOval( x, y, w, h, "fill:black; stroke:black; stroke-width:1");
    }

    public void drawArc(int x, int y, int w, int h, int startAngle, int arcAngle) {
	/*
	writeEllipsePath(x,y,w+1,h+1,startAngle,arcAngle);
	p.println("stroke");
	*/
    }

    public void fillArc(int x, int y, int w, int h, int startAngle, int arcAngle) {
	/*
	writeEllipsePath(x,y,w,h,startAngle,arcAngle);
	p.println("eofill");
	*/
    }

    private void drawRoundRect( int x, int y, int w, int h, int arcw, int arch, String style) {
	Element rect = svg.createElement( "rect");
	rect.setAttribute( "x", ""+transformX( x));
	rect.setAttribute( "y", ""+transformY( y));
	rect.setAttribute( "width", ""+scaleX( w));
	rect.setAttribute( "height", ""+scaleY( h));
	rect.setAttribute( "rx", ""+scaleX( arcw));
	rect.setAttribute( "ry", ""+scaleY( arch));
	rect.setAttribute( "style", style);
	root.appendChild( rect);	
    }

    public void drawRoundRect(int x, int y, int w, int h, int arcw, int arch) {
	drawRoundRect( x, y, w, h, arcw, arch, "fill:white; stroke:black; stroke-width:1");
    }

    public void fillRoundRect(int x, int y, int w, int h, int arcw, int arch) {
	drawRoundRect( x, y, w, h, arcw, arch, "fill:black; stroke:black; stroke-width:1");
    }

    private void drawPolygon( int xPoints[], int yPoints[], int nPoints, String style) {
	Element polygon = svg.createElement( "polygon");
	polygon.setAttribute( "style", style);

	// Create the list of points for this tag.
	// I.e. points="100,100 150,150 200,200"
	StringBuffer pointList = new StringBuffer();
	for ( int i=0; i < nPoints; i++) {
	    if ( i > 0)
		pointList.append( " ");

	    pointList.append( "" 
			      + transformX( xPoints[i])
			      + ","
			      + transformY( yPoints[i]));
	}

	polygon.setAttribute( "points", pointList.toString());

	root.appendChild( polygon);
    } 

    public void drawPolygon(int xPoints[], int yPoints[], int nPoints) {
	drawPolygon( xPoints, yPoints, nPoints, "fill:white; stroke:black; stroke-width:1");
    }

    public void drawPolygon( Polygon poly) {
	drawPolygon( poly.xpoints, poly.ypoints, poly.npoints);
    }

    public void fillPolygon(int xPoints[], int yPoints[], int nPoints) {
	drawPolygon( xPoints, yPoints, nPoints, "fill:black; stroke:black; stroke-width:1");
    }

    public void fillPolygon(Polygon poly) {
	fillPolygon( poly.xpoints, poly.ypoints, poly.npoints);
    }

    public void drawPolyline(int xPoints[], int yPoints[], int nPoints) {
	Element polyline = svg.createElement( "polyline");
	polyline.setAttribute( "style", "fill:white; stroke:black; stroke-width:1");

	// Create the list of points for this tag.
	// I.e. points="100,100 150,150 200,200"
	StringBuffer pointList = new StringBuffer();
	for ( int i=0; i < nPoints; i++) {
	    if ( i > 0)
		pointList.append( " ");

	    pointList.append( "" 
			      + transformX( xPoints[i])
			      + ","
			      + transformY( yPoints[i]));
	}

	polyline.setAttribute( "points", pointList.toString());

	root.appendChild( polyline);
    }

    public void drawLine(int x1, int y1, int x2, int y2) {
	Element line = svg.createElement( "line");
	line.setAttribute( "x1", ""+transformX( x1));
	line.setAttribute( "y1", ""+transformY( y1));
	line.setAttribute( "x2", ""+transformX( x2));
	line.setAttribute( "y2", ""+transformY( y2));
	line.setAttribute( "style", "fill:black; stroke:black; stroke-width:1");
	root.appendChild( line);
    }

    public void setClip(int x, int y, int w, int h) {
	/*
	clip = new Rectangle(x,y,w,h);
	writeRectanglePath(x,y,w,h);
	p.println("clip");
	*/
    }

    public void setClip(Shape clip) {
	setClip(clip.getBounds());
    }

    public void translate(int x, int y) {
	this.xOffset = x;
	this.yOffset = y;
    }

    public void scale(double xscale, double yscale) {
	this.xScale = xscale;
	this.yScale = yscale;
    }

    public void drawString(String t, int x, int y) {
	Element text = svg.createElement("text");
	text.setAttribute( "x", ""+transformX( x));
        text.setAttribute( "y", ""+transformY( y));
	text.appendChild( svg.createTextNode( t));
	root.appendChild( text);
    }


    // if you want to compile this with jdk1.1, you have to comment out this method.
    // if you want to compile this with jdk1.2, you MUST NOT comment out this method.
    // Did sun make a good job implementing jdk1.2? :-(((
    public void drawString(java.text.AttributedCharacterIterator aci, int i1, int i2) {}

    public void drawString(java.text.CharacterIterator aci, int i1, int i2) {}
}
