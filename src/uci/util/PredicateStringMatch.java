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

package uci.util;

import java.util.*;

public class PredicateStringMatch implements Predicate {

  ////////////////////////////////////////////////////////////////
  // constants
  public static int MAX_PATS = 10;

  ////////////////////////////////////////////////////////////////
  // instance variables
  String _patterns[];
  int _numPats;

  ////////////////////////////////////////////////////////////////
  // constructor
  protected PredicateStringMatch(String pats[], int numPats) {
    _patterns = pats;
    _numPats = numPats;
  }

  public static Predicate create(String pat) {
    pat = pat.trim();
    String pats[] = new String[MAX_PATS];
    int numPats = 0;
    if (pat.startsWith("*")) pats[numPats++] = "";
    StringTokenizer st = new StringTokenizer(pat, "*");
    //needs-more-work: support ? to match one character
    while (st.hasMoreElements()) {
      String token = st.nextToken();
      pats[numPats++] = token;
    }
    if (pat.endsWith("*")) pats[numPats++] = "";
    if (numPats == 0) return PredicateTrue.theInstance();
    if (numPats == 1) return new PredicateEquals(pats[0]);
    return new PredicateStringMatch(pats, numPats);
  }

  public boolean predicate(Object o) {
    String target = o.toString();
    if (!target.startsWith(_patterns[0])) return false;
    if (!target.endsWith(_patterns[_numPats-1])) return false;
    for (int i = 0; i < _numPats; i++) {
      String p = _patterns[i];
      int index = (target + "*").indexOf(p);
      if (index == -1) return false;
      target = target.substring(index + p.length());
    }
    return true;
  }

//   public static void main(String args[]) {
//     if (args.length <= 1) {
//       System.out.println("Arguments:  pattern targets...");
//       System.out.println("outputs targets that match pattern");
//       System.out.println("be sure to protect pattern from shell expansion");
//       return;
//     }
//     System.out.println("Pattern = " + args[0]);
//     Predicate p = PredicateStringMatch.create(args[0]);
//     for (int i = 1; i < args.length; i++) {
//       if (p.predicate(args[i])) System.out.println(args[i]);
//     }
//   }


} /* end class PredicateStringMatch */

