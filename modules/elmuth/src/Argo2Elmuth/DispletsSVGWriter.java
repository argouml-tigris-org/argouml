/**
 * DispletsSVGWriter.java
 * @author  Paolo Pinciani (pinciani@cs.unibo.it)
 *
 * Copyright 2001-2002 Department of Computer Science
 * University of Bologna
 * Mura Anteo Zamboni 7, 40127 Bologna, ITALY
 * Tel: +39 051 35.45.16
 * Fax: +39 051 35.45.10
 * Web: http://cs.unibo.it
 */
package Argo2Elmuth;

import java.util.Hashtable;
import java.util.Enumeration;
import java.io.PrintWriter;
import java.io.OutputStream;
import java.awt.image.ImageObserver;
import java.text.AttributedCharacterIterator;
import java.text.CharacterIterator;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import org.w3c.dom.*;

/**
 * <code>DispletsSVGWriter</code> is an updated version of the class <code>org.tigris.gef.persistence.SVGWriter</code>.
 * Each element of an ArgoUML diagram (i.e. a <code>org.tigris.gef.presentation.Fig</code> class) if grouped in a SVG group, i.e. a SVG "g" element.
 * Some other inforomation is added to the group for managing diagram type.
 */
public class DispletsSVGWriter extends Graphics
{
    private PrintWriter _p;
    Document _svg;
    Element _root;
	Element _group = null;
    private Color _fColor;
    private Color _bgColor;
    private Rectangle _drawingArea;
    private Font _font;
    private Rectangle clip;
    private int _xOffset;
    private int _yOffset;
    private int _hInset;
    private int _vInset;
    private double xScale;
    private double yScale;
    private String SVGns;

    public Graphics create()
    {
        return this;
    }

    public Graphics create(int i, int j, int k, int l)
    {
        return this;
    }

    public void dispose()
    {
        _svg.appendChild(_root);
        printDOMTree(_svg);
        _p.close();
    }

    public void printDOMTree(Node node)
    {
        short st = node.getNodeType();
        switch (st)
        {
        case 9:  // '\t'
            _p.println("<?xml version=\"1.0\" ?>");
            //_p.println("<!DOCTYPE svg PUBLIC '-//W3C//DTD SVG 20001102//EN' 'http://www.w3.org/TR/2000/CR-SVG-20001102/DTD/svg-20001102.dtd'>");
            printDOMTree(((Document)node).getDocumentElement());
            break;

        case 1:  // '\001'
            _p.print("<");
            _p.print(node.getNodeName());
            NamedNodeMap namedNodeMap = node.getAttributes();
            for (int i = 0; i < namedNodeMap.getLength(); i++)
            {
                Node node1 = namedNodeMap.item(i);
                _p.print(" " + node1.getNodeName() + "=\"" + node1.getNodeValue() + "\"");
            }
            NodeList nodeList = node.getChildNodes();
            if (nodeList.getLength() > 0)
            {
                _p.println(">");
                int k = nodeList.getLength();
                for (int l = 0; l < k; l++)
                    printDOMTree(nodeList.item(l));
                _p.print("</");
                _p.print(node.getNodeName());
                _p.println('>');
            }
            else
            {
                _p.println("/>");
            }
            break;

        case 5:  // '\005'
            _p.print("&");
            _p.print(node.getNodeName());
            _p.print(";");
            break;

        case 4:  // '\004'
            _p.print("<![CDATA[");
            _p.print(node.getNodeValue());
            _p.print("]]>");
            break;

        case 3:  // '\003'
            String s = node.getNodeValue();
            for (int j = 0; j < s.length(); j++)
                switch (s.charAt(j))
                {
                case 38:  // '&'
                    _p.print("&amp;");
                    break;

                case 60:  // '<'
                    _p.print("&lt;");
                    break;

                case 62:  // '>'
                    _p.print("&gt;");
                    break;

                default:
                    _p.print(s.charAt(j));
                    break;
                }
            break;

        case 7:  // '\007'
            _p.print("<?");
            _p.print(node.getNodeName());
            String s1 = node.getNodeValue();
            _p.print("");
            _p.print(s1);
            _p.print("?>");
            break;
        }
    }

    public Color getColor()
    {
        return _fColor;
    }

    private String getColorAsString()
    {
        return "#" + Integer.toHexString(_fColor.getRGB()).substring(2);
    }

    public void setColor(Color color)
    {
        _fColor = color;
    }

    private Color getBackgroundColor()
    {
        return _bgColor;
    }

    private String getBackgroundColorAsString()
    {
        return "#" + Integer.toHexString(_bgColor.getRGB()).substring(2);
    }

    private void setBackgroundColor(Color color)
    {
        _bgColor = color;
    }

    public void setPaintMode()
    {
        return;
    }

    public void setXORMode(Color color)
    {
        return;
    }

    public Font getFont()
    {
        return _font;
    }

    public void setFont(Font font)
    {
        _font = font;
    }

    public FontMetrics getFontMetrics()
    {
        return getFontMetrics(_font);
    }

    public FontMetrics getFontMetrics(Font font)
    {
        return Toolkit.getDefaultToolkit().getFontMetrics(font);
    }

    private String getFontStyleSVG()
    {
        String s = "font-family:" + _font.getFamily() + "; font-size:" + _font.getSize() + ";";
        if (getFont().isBold())
        {
            s += " font-weight:bold;";
        }
        if (getFont().isItalic())
        {
            s += " font-style:italic;";
        }
        return s;
    }

    public Rectangle getClipBounds()
    {
        return clip;
    }

    public void clipRect(int i, int j, int k, int l)
    {
        setClip(i, j, k, l);
    }

    public Shape getClip()
    {
        return clip;
    }

    public void copyArea(int i, int j, int k, int l, int m, int i1)
    {
        return;
    }

    public boolean drawImage(Image image, int i, int j, ImageObserver imageObserver)
    {
        return false;
    }

    public boolean drawImage(Image image, int i, int j, int k, int l, ImageObserver imageObserver)
    {
        return true;
    }

    public boolean drawImage(Image image, int i, int j, Color color, ImageObserver imageObserver)
    {
        return false;
    }

    public boolean drawImage(Image image, int i, int j, int k, int l, Color color, ImageObserver imageObserver)
    {
        return false;
    }

    public boolean drawImage(Image image, int i, int j, int k, int l, int m, int i1, int j1, int k1, ImageObserver imageObserver)
    {
        return false;
    }

    public boolean drawImage(Image image, int i, int j, int k, int l, int m, int i1, int j1, int k1, Color color, ImageObserver imageObserver)
    {
        return false;
    }

    private int scaleX(int i)
    {
        return (int)((double)i * xScale);
    }

    private int scaleY(int i)
    {
        return (int)((double)i * yScale);
    }

    private int transformX(int i)
    {
        return scaleX(i) + _xOffset;
    }

    private int transformY(int i)
    {
        return scaleY(i) + _yOffset;
    }

    private void drawRect(int i, int j, int k, int l, String s)
    {
        //System.out.println("[SVGWriter] drawRect: x/y/w/h = " + i + "/" + j + "/" + k + "/" + l);
		
        Element element = _svg.createElement("rect");
        element.setAttribute("x", "" + transformX(i));
        element.setAttribute("y", "" + transformY(j));
        element.setAttribute("width", "" + scaleX(k));
        element.setAttribute("height", "" + scaleY(l));
        element.setAttribute("style", s);
        if (_group != null)
			_group.appendChild(element);
		else	
	        _root.appendChild(element);
    }

    public void drawRect(int i, int j, int k, int l)
    {
        drawRect(i, j, k, l, "fill:" + getBackgroundColorAsString() + "; stroke:" + getColorAsString() + "; stroke-width:1");
    }

    public void fillRect(int i, int j, int k, int l)
    {
        drawRect(i, j, k, l, "fill:" + getColorAsString() + "; stroke:" + getColorAsString() + "; stroke-width:1");
    }

    public void clearRect(int i, int j, int k, int l)
    {
        drawRect(i, j, k, l, "fill:" + getBackgroundColorAsString() + "; stroke:" + getBackgroundColorAsString() + "; stroke-width:1");
    }

    private void writeEllipsePath(int i, int j, int k, int l, int m, int i1)
    {
        return;
    }

    private void drawOval(int i, int j, int k, int l, String s)
    {
        Element element = _svg.createElement("ellipse");
        element.setAttribute("cx", "" + transformX(i + k / 2));
        element.setAttribute("cy", "" + transformY(j + l / 2));
        element.setAttribute("rx", "" + (double)scaleX(k) / 2.0);
        element.setAttribute("ry", "" + (double)scaleY(l) / 2.0);
        element.setAttribute("style", s);
        if (_group != null)
			_group.appendChild(element);
		else	
	        _root.appendChild(element);
    }

    public void drawOval(int i, int j, int k, int l)
    {
        drawOval(i, j, k, l, "fill:" + getBackgroundColorAsString() + "; stroke:" + getColorAsString() + "; stroke-width:1");
    }

    public void fillOval(int i, int j, int k, int l)
    {
        drawOval(i, j, k, l, "fill:" + getColorAsString() + "; stroke:" + getColorAsString() + "; stroke-width:1");
    }

    public void drawArc(int i, int j, int k, int l, int m, int i1)
    {
        return;
    }

    public void fillArc(int i, int j, int k, int l, int m, int i1)
    {
        return;
    }

    private void drawRoundRect(int i, int j, int k, int l, int m, int i1, String s)
    {
        Element element = _svg.createElement("rect");
        element.setAttribute("x", "" + transformX(i));
        element.setAttribute("y", "" + transformY(j));
        element.setAttribute("width", "" + scaleX(k));
        element.setAttribute("height", "" + scaleY(l));
        element.setAttribute("rx", "" + scaleX(m));
        element.setAttribute("ry", "" + scaleY(i1));
        element.setAttribute("style", s);
        if (_group != null)
			_group.appendChild(element);
		else	
	        _root.appendChild(element);
    }

    public void drawRoundRect(int i, int j, int k, int l, int m, int i1)
    {
        drawRoundRect(i, j, k, l, m, i1, "fill:" + getBackgroundColorAsString() + "; stroke:" + getColorAsString() + "; stroke-width:1");
    }

    public void fillRoundRect(int i, int j, int k, int l, int m, int i1)
    {
        drawRoundRect(i, j, k, l, m, i1, "fill:" + getColorAsString() + "; stroke:" + getColorAsString() + "; stroke-width:1");
    }

    private void drawPolygon(int an[], int an1[], int i, String s)
    {
        double d = 0.0;
        double d1 = 0.0;
        Element element = _svg.createElement("polygon");
        element.setAttribute("style", s);
        StringBuffer stringBuffer = new StringBuffer();
        for (int j = 0; j < i; j++)
        {
            if (j > 0)
            {
                stringBuffer.append(" ");
            }
            stringBuffer.append("" + transformX(an[j]) + "," + transformY(an1[j]));
            if ((double)transformX(an[j]) > d)
            {
                d = transformX(an[j]);
            }
            if ((double)transformY(an1[j]) > d1)
            {
                d1 = transformY(an1[j]);
            }
        }
        element.setAttribute("points", stringBuffer.toString());
        if (_group != null)
			_group.appendChild(element);
		else	
	        _root.appendChild(element);
    }

    public void drawPolygon(int an[], int an1[], int i)
    {
        drawPolygon(an, an1, i, "fill:" + getBackgroundColorAsString() + "; stroke:" + getColorAsString() + "; stroke-width:1");
    }

    public void drawPolygon(Polygon polygon)
    {
        drawPolygon(polygon.xpoints, polygon.ypoints, polygon.npoints);
    }

    public void fillPolygon(int an[], int an1[], int i)
    {
        drawPolygon(an, an1, i, "fill:" + getColorAsString() + "; stroke:" + getColorAsString() + "; stroke-width:1");
    }

    public void fillPolygon(Polygon polygon)
    {
        fillPolygon(polygon.xpoints, polygon.ypoints, polygon.npoints);
    }

    public void drawPolyline(int an[], int an1[], int i)
    {
        double d = 0.0;
        double d1 = 0.0;
        Element element = _svg.createElement("polyline");
        element.setAttribute("style", "fill:" + getBackgroundColorAsString() + "; stroke:" + getColorAsString() + "; stroke-width:1");
        StringBuffer stringBuffer = new StringBuffer();
        for (int j = 0; j < i; j++)
        {
            if (j > 0)
            {
                stringBuffer.append(" ");
            }
            stringBuffer.append("" + transformX(an[j]) + "," + transformY(an1[j]));
            if ((double)transformX(an[j]) > d)
            {
                d = transformX(an[j]);
            }
            if ((double)transformY(an1[j]) > d1)
            {
                d1 = transformY(an1[j]);
            }
        }
        element.setAttribute("points", stringBuffer.toString());
        if (_group != null)
			_group.appendChild(element);
		else	
	        _root.appendChild(element);
    }

    public void drawLine(int i, int j, int k, int l)
    {
        Element element = _svg.createElement("line");
        element.setAttribute("x1", "" + transformX(i));
        element.setAttribute("y1", "" + transformY(j));
        element.setAttribute("x2", "" + transformX(k));
        element.setAttribute("y2", "" + transformY(l));
        element.setAttribute("style", "fill:" + getColorAsString() + "; stroke:" + getColorAsString() + "; stroke-width:1");
        if (_group != null)
			_group.appendChild(element);
		else	
	        _root.appendChild(element);
    }

    public void setClip(int i, int j, int k, int l)
    {
        return;
    }

    public void setClip(Shape shape)
    {
        setClip(shape.getBounds());
    }

    public void translate(int i, int j)
    {
        _xOffset = i;
        _yOffset = j;
    }

    public void scale(double d, double d1)
    {
        xScale = d;
        yScale = d1;
    }

    public void drawString(String s, int i, int j) {
        Element element = _svg.createElement("text");
        element.setAttribute("x", "" + transformX(i));
        element.setAttribute("y", "" + transformY(j));
        element.setAttribute("style", getFontStyleSVG());
        element.appendChild(_svg.createTextNode(s));
		if (_group != null)
			_group.appendChild(element);
		else	
	        _root.appendChild(element);	
    }

    public void drawString(AttributedCharacterIterator attributedCharacterIterator, int i, int j)
    {
        return;
    }

    public void drawString(CharacterIterator characterIterator, int i, int j)
    {
        return;
    }

	public void createGroup(String id, Hashtable attributes) {
		_group = _svg.createElement("g");
		_group.setAttribute("id",id);
		if (attributes != null) {
			//Element foreignObject = _svg.createElement("foreignObject");
			//Element privateData = _svg.createElement("privateData");
			for (Enumeration e=attributes.keys(); e.hasMoreElements();) {
				String key = (String)e.nextElement();
				if (!key.equals("id"))
					_group.setAttribute(key,(String)attributes.get(key));
					//foreignObject.setAttribute(key,(String)attributes.get(key));	
			}
			//foreignObject.appendChild(privateData);
			//if (foreignObject == null)
				//System.out.println("foreignObject is null");
			//_group.appendChild(foreignObject);
		}
		_root.appendChild(_group);
	}

    public DispletsSVGWriter(OutputStream outputStream, Rectangle rectangle)
        throws java.io.IOException, Exception
    {
        _fColor = Color.black;
        _bgColor = Color.white;
        _font = new Font("Verdana", 0, 8);
        _xOffset = 0;
        _yOffset = 0;
        _hInset = 10;
        _vInset = 10;
        xScale = 1.0;
        yScale = 1.0;
        SVGns = "http://www.w3.org/2000/SVG";
        _p = new PrintWriter(outputStream);
        _drawingArea = rectangle;
        translate(_hInset - rectangle.x, _vInset - rectangle.y);
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(false);
        documentBuilderFactory.setValidating(false);
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        _svg = documentBuilder.newDocument();
        _root = _svg.createElement("svg");
        //_root.setAttribute("xmlns", "http://www.w3.org/2000/svg");
        _root.setAttribute("width", "" + (2 * _hInset + scaleX(_drawingArea.width)));
        _root.setAttribute("height", "" + (2 * _vInset + scaleY(_drawingArea.height)));
    }
}