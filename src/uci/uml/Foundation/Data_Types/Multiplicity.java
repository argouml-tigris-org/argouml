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


package uci.uml.Foundation.Data_Types;

import java.util.*;

public class Multiplicity implements java.io.Serializable {

  public static final Multiplicity ONE = new Multiplicity(1, 1);
  public static final Multiplicity ONE_OR_ZERO = new Multiplicity(0, 1);
  public static final Multiplicity ONE_OR_MORE =
  new Multiplicity(new Integer(1), null);
  public static final Multiplicity ZERO_OR_MORE =
  new Multiplicity(new Integer(0), null);
  public static int MAX_MULTIPLICITY_RANGES = 10;

  public Vector _ranges;

  public Multiplicity() { }
  public Multiplicity(int low, int high) {
    addRange(new MultiplicityRange(low, high));
  }
  public Multiplicity(Integer low, Integer high) {
    addRange(new MultiplicityRange(low, high));
  }

  public Vector getRange() { return _ranges; }
  public void setRange(Vector x) {
    _ranges = x;
  }
  public void addRange(MultiplicityRange x) {
    if (_ranges == null) _ranges = new Vector();
    _ranges.addElement(x);
  }
  public void removeRange(MultiplicityRange x) {
    _ranges.removeElement(x);
  }

  // needs-more-work: Should allow ranges in any order, need not be
  // corresponding order. Also, should ((1..5), (5..7)) equal ((1..7))?
  public boolean equals(Object obj) {
    if (!(obj instanceof Multiplicity)) return false;
    Multiplicity m = (Multiplicity) obj;
    Vector oRanges = m.getRange();
    int oSize = oRanges.size();
    int size = _ranges.size();
    if (oSize != size) return false;
    for (int i = 0; i < size; i++) {
      MultiplicityRange myMR = (MultiplicityRange) _ranges.elementAt(i);
      MultiplicityRange oMR = (MultiplicityRange) oRanges.elementAt(i);
      if (!myMR.equals(oMR)) return false;
    }
    return true;
  }

  ////////////////////////////////////////////////////////////////
  // debugging methods

  public String toString() {
    // needs-more-work:should be no dependencies out of UML meta-model
    return uci.uml.generate.GeneratorDisplay.Generate(this);
  }

  ////////////////////////////////////////////////////////////////
  // utility methods

  public int max() {
    int res = 0;
    if (_ranges == null) return Integer.MAX_VALUE;
    java.util.Enumeration rangeEnum = _ranges.elements();
    while (rangeEnum.hasMoreElements()) {
      MultiplicityRange mr = (MultiplicityRange) rangeEnum.nextElement();
      Integer upBound = mr.getUpper();
      int upper = (upBound == null) ? Integer.MAX_VALUE : upBound.intValue();
      res = Math.max(res, upper);
    }
    return res;
  }

  public int min() {
    int res = Integer.MAX_VALUE;
    if (_ranges == null) return 0;
    java.util.Enumeration rangeEnum = _ranges.elements();
    while (rangeEnum.hasMoreElements()) {
      MultiplicityRange mr = (MultiplicityRange) rangeEnum.nextElement();
      Integer loBound = mr.getLower();
      int lower = (loBound == null) ? 0 : loBound.intValue();
      res = Math.min(res, lower);
    }
    return res;
  }

  public String maxString() {
    int max = max();
    if (max == Integer.MAX_VALUE) return "*";
    else return Integer.toString(max);
  }

  static final long serialVersionUID = -7746929584910122951L;
}

