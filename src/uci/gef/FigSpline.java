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


package uci.gef;

import java.applet.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import uci.ui.*;
import uci.util.*;

public class FigSpline extends FigPoly {

  protected int[] _xknots = new int[5];
  protected int[] _yknots = new int[5];
  protected int _nknots;

  protected Polygon _curve;
  protected int _threshold = 5;
  protected int SPLINE_THRESH = 2;
  protected int ARR_WIDTH = 5, ARR_HEIGHT = 16;
  protected double _junc_t, _delta0, _delta1, _denom, _d_curve_length;
  protected int _juncX, _juncY;

  protected int _pattern  = 0x88888888;
  protected BitSet _style    = new BitSet(32);

  public FigSpline(Color line_color, Color fill_color) {
    super(line_color, fill_color);
  }

  public FigSpline(Color line_color) { super(line_color); }


  public FigSpline(int x, int y) {
    super();
    addPoint(x, y);
  }

  public void moveVertex(Handle h, int x, int y, boolean ov) {
    super.moveVertex(h, x, y, ov);
    setSpline();
  }

  public void translate(int dx, int dy) {
    super.translate(dx, dy);
    setSpline();
  }

  public void addPoint(int x, int y) {
    super.addPoint(x, y);
    setCount();
    setSpline();
  }

  public void removePoint(int i) {
    super.removePoint(i);
    setCount();
    setSpline();
  }

  public void appendTwoPoints() {
    super.appendTwoPoints();
    setCount();
    setSpline();
  }

  public void prependTwoPoints() {
    super.prependTwoPoints();
    setCount();
    setSpline();
  }

  public void insertPoint(int i, int x, int y) {
    super.insertPoint(i, x, y);
    setCount();
    setSpline();
  }

  public void paint(Graphics g) {
    if (_npoints == 2) drawStraight(g);
    else { drawCurve(g); }
  }


//   protected void drawControlLine(Graphics g) {
//     for(int i=0; i<=_npoints-2; i++) {
//       g.setColor(Color.black);
//       drawDottedLine(g, _xpoints[i],   _ypoints[i], 
// 		     _xpoints[i+1], _ypoints[i+1]);
//     }
//   }

//   protected void drawDottedLine(Graphics g, int x0, int y0, int x1, int y1) {

//     int temp, dx, dy, i, x, y, x_sign, y_sign, decision, mask;

//     for (int j = 0; j < 32; j++) {
//       mask = 1 << j;
//       if ((mask & _pattern) == 0) _style.clear(j);
//       else _style.set(j);
//     }

//     dx = Math.abs(x1 - x0);
//     dy = Math.abs(y1 - y0);
//     if (((dx >= dy) && (x0 > x1)) || ((dy > dx) && (y0 > y1))) {
//       temp = x0;
//       x0   = x1;
//       x1   = temp;
//       temp = y0;
//       y0   = y1;
//       y1   = temp;
//     }
//     if ((y1 - y0) < 0) y_sign = -1;
//     else               y_sign = 1;
//     if ((x1 - x0) < 0) x_sign = -1;
//     else               x_sign = 1;
//     if (dx >= dy)	{
//       for (x = x0, y = y0, i = 0, decision=0; 
// 	   x <= x1; x++, i++, decision += dy) {
// 	if (decision >= dx) {
// 	  decision -= dx;
// 	  y+= y_sign;
// 	}
// 	if (_style.get(i%32)) g.drawLine(x,y,x,y);
//       }
//     }
//     else {
//       for (x = x0, y = y0, 
// 	     i = 0,  decision = 0; 
// 	   y <= y1; y++, i++, decision += dx) {
// 	if (decision >= dy) {
// 	  decision -= dy;
// 	  x += x_sign;
// 	}
// 	if (_style.get(i%32)) g.drawLine(x, y, x, y);
//       }
//     }
//   }  	


  // Draw a three-point spline using DeCasteljau algorithm
  protected void drawBezier(Graphics g,
			    int x1, int y1,
			    int x2, int y2,
			    int x3, int y3) {
    int xa, ya, xb, yb, xc, yc, xp, yp;
    xa = ( x1 + x2 ) / 2;
    ya = ( y1 + y2 ) / 2;
    xc = ( x2 + x3 ) / 2;
    yc = ( y2 + y3 ) / 2;
    xb = ( xa + xc ) / 2;
    yb = ( ya + yc ) / 2;

    xp = ( x1 + xb ) / 2;
    yp = ( y1 + yb ) / 2;
    if ( Math.abs( xa - xp ) + Math.abs( ya - yp ) > SPLINE_THRESH )
      drawBezier( g, x1, y1, xa, ya, xb, yb );
    else {
      g.drawLine( x1, y1, xb, yb );
      _curve.addPoint(xb, yb);
    }
    xp = ( x3 + xb ) / 2;
    yp = ( y3 + yb ) / 2;
    if ( Math.abs( xc - xp ) + Math.abs( yc - yp ) > SPLINE_THRESH )
      drawBezier( g, xb, yb, xc, yc, x3, y3 );
    else {
      g.drawLine( xb, yb, x3, y3 );
      _curve.addPoint(x3, y3);
    }
  }

  protected void drawCurve(Graphics g) {
    int nSegments = _npoints-2;
    _curve = new Polygon();
    g.setColor(_lineColor);
    for (int i=0; i<=nSegments-1; i++)	{
      drawBezier(g, _xknots[2*i],   _yknots[2*i],
		 _xknots[2*i+1], _yknots[2*i+1],
		 _xknots[2*i+2], _yknots[2*i+2]);
    }
    if (_filled) {
      g.setColor(_fillColor);
      g.fillPolygon(_curve);	   // here the curve gets partially destroyed
      g.setColor(_lineColor);
      g.drawPolyline(_curve.xpoints, _curve.ypoints, _curve.npoints);
    }
  }

  protected void drawStraight(Graphics g) {
    g.setColor(_lineColor);
    g.drawLine(_xknots[0], _yknots[0], 
	       _xknots[1], _yknots[1]);
  }

  protected void setCount() {
    if (_npoints == 2) _nknots   = 2;
    else if (_npoints == 3) _nknots   = 3;
    _nknots = 2*_npoints-3;
  }


  protected void setSpline() {
    if (_npoints>=4) {
      _xknots[0] = _xpoints[0];
      _yknots[0] = _ypoints[0];
      _xknots[_nknots-1] = _xpoints[_npoints-1];
      _yknots[_nknots-1] = _ypoints[_npoints-1];
      for (int i=0; i<=_npoints-4; i++) {
	setJunctionPoint(_xpoints[i],   _ypoints[i],
			 _xpoints[i+1], _ypoints[i+1],
			 _xpoints[i+2], _ypoints[i+2]);
	_xknots[2*(i+1)] = _juncX;
	_yknots[2*(i+1)] = _juncY;
      }
      for(int i=1; i<=_npoints-2; i++) {
	_xknots[2*i-1] = _xpoints[i];
	_yknots[2*i-1] = _ypoints[i];
      }
    }
    else if (_npoints<4) {
      for (int i=0; i<_npoints; i++) {
	_xknots[i] = _xpoints[i];
	_yknots[i] = _ypoints[i];
      }
    }
  }


  protected void setJunctionPoint(int p1x, int p1y,
				  int p2x, int p2y,
				  int p3x, int p3y) {
    _delta0 = dist(p1x, p1y, p2x, p2y);
    _delta1 = dist(p2x, p2y, p3x, p3y);
    _denom = _delta0 + _delta1;
    if (_denom<=_threshold) _junc_t = 0;
    else _junc_t = _delta1 / _denom;
    _juncX = (int)(_junc_t*p2x + (1-_junc_t)*p3x);
    _juncY = (int)(_junc_t*p2y + (1-_junc_t)*p3y);
  }


  protected void growIfNeeded() {
    if (_npoints >= _xpoints.length) {
      int tmp[];

      tmp = new int[_npoints*2];
      System.arraycopy(_xpoints, 0, tmp, 0, _npoints);
      _xpoints = tmp;

      tmp = new int[_npoints*2];
      System.arraycopy(_ypoints, 0, tmp, 0, _npoints);
      _ypoints = tmp;

      tmp = new int[_npoints*4-1];
      System.arraycopy(_xknots, 0, tmp, 0, _nknots);
      _xknots = tmp;

      tmp = new int[_npoints*4-1];
      System.arraycopy(_yknots, 0, tmp, 0, _nknots);
      _yknots = tmp;
    }
  }

  private double dist(int x0, int y0, int x1, int y1) {
    double dx, dy;
    dx = (double)(x0-x1);
    dy = (double)(y0-y1);
    return Math.sqrt(dx*dx+dy*dy);
  }

  private double dist(double dx, double dy) {
    return Math.sqrt(dx*dx+dy*dy);
  }

  public void cleanUp() { }

} /* end class FigSpline */

